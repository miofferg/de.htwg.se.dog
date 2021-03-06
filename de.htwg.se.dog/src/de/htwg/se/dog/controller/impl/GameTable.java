package de.htwg.se.dog.controller.impl;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.google.inject.Inject;

import de.htwg.se.dog.controller.CardDealerInterface;
import de.htwg.se.dog.controller.GameTableInterface;
import de.htwg.se.dog.models.CardInterface;
import de.htwg.se.dog.models.FieldInterface;
import de.htwg.se.dog.models.GameFieldInterface;
import de.htwg.se.dog.models.PlayerInterface;
import de.htwg.se.dog.models.impl.Card;
import de.htwg.se.dog.models.impl.GameField;
import de.htwg.se.dog.models.impl.Player;
import de.htwg.se.dog.util.IOMsgEvent;
import de.htwg.se.dog.util.Observable;

/**
 * The Gametable controller who controlls the game logik
 * 
 * @author Michael, Christian
 * 
 */
public class GameTable extends Observable implements GameTableInterface {

    private static final int CARD14 = 14;
    private static final int CARD13 = 13;
    private static final int CARD11 = 11;
    private static final int FIELDSTILLHOUSE = 16;
    private static final int HOUSECOUNT = 4;

    private final GameFieldInterface game;
    private final List<PlayerInterface> players;
    private Queue<PlayerInterface> turnPlayer;
    private final CardDealerInterface dealer;
    private final Movement movement;
    private PlayerInterface currentPlayer;

    /**
     * Constructor to generate a new gametable
     * 
     * @param playerCount
     *            number of players
     * @param figCount
     *            number of figures per player
     */
    @Inject
    public GameTable(int playerCount) {
        game = new GameField(FIELDSTILLHOUSE, playerCount, HOUSECOUNT);
        players = new LinkedList<PlayerInterface>();
        dealer = new CardDealer(playerCount);
        movement = new Movement(game);
        //add players
        for (int i = 1; i <= playerCount; i++) {
            players.add(new Player(i, HOUSECOUNT, game.calculatePlayerStart(i)));
        }
    }

    /**
     * Returns the Gamefield currently playing on
     * 
     * @return
     */
    @Override
    public GameFieldInterface getGameField() {
        return this.game;
    }

    /**
     * Returns weather the playerqueue is empty or not
     * 
     * @return true if empty otherwise false
     */
    @Override
    public boolean playerQueueIsEmpty() {
        return turnPlayer.isEmpty();
    }

    /**
     * Sets the player that can play first
     * 
     * @param playernum
     */
    @Override
    public void setStartingPlayer(int playernum) {
        Collections.rotate(players, -playernum + 1);
    }

    /**
     * Refills the Playerqueue and deals cards to every player
     */
    @Override
    public void dealCards() {

        //now put all players into the queue
        turnPlayer = new LinkedList<PlayerInterface>();
        for (PlayerInterface p : players) {
            dealer.dealCards(p);
            turnPlayer.add(p);

        }
        //rotate player list clockwise
        Collections.rotate(players, -1);
    }

    @Override
    public void newRound() {

        dealCards();
        dealer.newRound();
        sendObserverMessage("Neue Runde");
    }

    @Override
    public void nextPlayer() {
        PlayerInterface temp;
        do {
            if (turnPlayer.isEmpty()) {
                newRound();
            }
            temp = turnPlayer.poll();
            if (!canPlay(temp)) {
                sendObserverMessage((String.format("Spieler %d kann nicht spielen", temp.getPlayerID())));
                temp = null;
            }
            currentPlayer = temp;
        } while (currentPlayer == null);
    }

    /**
     * Returns true if the Player has a card that can be played
     * 
     * @param p
     *            the Player that wants to play
     * @return true if he can play, otherwise false
     */
    @Override
    public boolean canPlay(PlayerInterface p) {
        boolean retval = false;
        if (!possibleCards(p).isEmpty()) {
            retval = true;
            turnPlayer.offer(p);
        } else {
            p.clearCardList();
        }
        return retval;
    }

    @Override
    public boolean playerHasCard(int cardval) {
        boolean retval = false;
        if (currentPlayer.getCardfromCardNr(cardval) != null) {
            retval = true;
        }
        return retval;
    }

    /**
     * Returns a list containing the cards that can be played by Player p
     * 
     * @param p
     *            the player that wants to play
     * @return a list containing the cards that can be played
     */
    @Override
    public List<CardInterface> possibleCards(PlayerInterface p) {
        List<CardInterface> cards = new LinkedList<CardInterface>(p.getCardList());
        Iterator<CardInterface> it = cards.iterator();
        cardIsPossible: while (it.hasNext()) {
            CardInterface c = it.next();
            //Put new Figure on field
            boolean validMoveStartCard = (c.getValue() == 1 || c.getValue() == CARD14 || c.getValue() == CARD13);
            if (!game.getGameArray()[game.calculatePlayerStart(p.getPlayerID())].isBlocked() && !p.getFigureList().isEmpty() && validMoveStartCard) {
                continue;
            }

            //move figure on field
            for (Integer field : p.getFigureRegister()) {
                movement.setMoveStrategie(c.getValue());
                //Normal-Move possible?
                if (movePossible(c, field)) {
                    continue cardIsPossible;
                }

            }
            it.remove();
        }
        return cards;
    }

    private boolean movePossible(CardInterface c, Integer field) {
        boolean ret = false;
        if (c.getValue() != CARD11 && movement.validMove(c.getValue(), field) || c.getValue() == CARD11 && movement.anySwitchMove(field)) {
            ret = true;
        }
        return ret;
    }

    @Override
    public boolean currentPlayerHaswon() {
        boolean retval = false;
        FieldInterface[] array = game.getGameArray();
        if (currentPlayer.getFigureRegister().size() == game.getHouseCount()) {
            for (Integer fieldID : currentPlayer.getFigureRegister()) {
                if (!array[fieldID].isHouse()) {
                    retval = false;
                    break;
                }
                retval = true;
            }
        }
        return retval;
    }

    @Override
    public int getFigureOwnerID(int fieldnr) {
        return game.getFieldForNum(fieldnr).getFigureOwnerNr();
    }

    @Override
    public boolean fieldIsEmpty(int fieldnr) {
        boolean retVal = true;
        if (fieldnr >= 0 && fieldnr < game.getFieldSize()) {
            retVal = movement.fieldEmpty(game.getFieldForNum(fieldnr));
        }
        return retVal;
    }

    @Override
    public PlayerInterface getCurrentPlayer() {
        return currentPlayer;
    }

    @Override
    public String getGameFieldString() {
        return game.toString();
    }

    @Override
    public String getPlayerString() {
        return currentPlayer.toString();
    }

    @Override
    public String getPlayerHandString() {
        return currentPlayer.printCardsOnHand();
    }

    @Override
    public boolean playCard(int cardNr, int steps, int startfieldNr) {
        boolean retval = false;
        movement.setMoveStrategie(cardNr);
        retval = movement.move(steps, startfieldNr);
        if (retval) {
            currentPlayer.removeCard(currentPlayer.getCardfromCardNr(cardNr));
        }
        return retval;
    }

    @Override
    public boolean isValidMove(int cardNr, int steps, int startfieldNr) {
        boolean retval = false;
        if (startfieldNr >= 0 && startfieldNr < game.getFieldSize()) {
            movement.setMoveStrategie(cardNr);
            retval = movement.validMove(steps, startfieldNr);
        }
        return retval;
    }

    @Override
    public boolean isPlayerStartfieldBlocked() {
        return game.getGameArray()[currentPlayer.getStartFieldNr()].isBlocked();
    }

    @Override
    public boolean moveFigureToStart(int card) {
        boolean retval = false;
        if (movement.moveStart(currentPlayer)) {
            currentPlayer.removeCard(currentPlayer.getCardfromCardNr(card));
            retval = true;
        }
        return retval;
    }

    @Override
    public int getCurrentPlayerID() {
        return currentPlayer.getPlayerID();
    }

    @Override
    public int getRound() {
        return dealer.getRound();
    }

    /**
     * sends the message msg to all observers
     * 
     * @param msg
     *            the message
     */
    private void sendObserverMessage(String msg) {
        notifyObservers(new IOMsgEvent(msg));
    }

    @Override
    public int getTargetField(int steps, int startfieldnr) {
        return movement.getTargetfield(steps, startfieldnr);
    }

    @Override
    @Inject
    public CardInterface playJoker(int cardVal) {
        CardInterface retCard = null;
        CardInterface newCard = new Card(cardVal);
        CardInterface oldCard = new Card(CARD14);
        currentPlayer.addCard(newCard);
        currentPlayer.removeCard(oldCard);
        if (!this.canPlay(currentPlayer)) {
            currentPlayer.removeCard(newCard);
            currentPlayer.addCard(oldCard);
            retCard = oldCard;
        } else {
            retCard = newCard;
        }

        return retCard;
    }

    @Override
    public List<PlayerInterface> getPlayerList() {
        return players;
    }
}
