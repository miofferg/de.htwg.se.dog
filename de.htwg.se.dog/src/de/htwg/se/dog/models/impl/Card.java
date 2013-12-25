package de.htwg.se.dog.models.impl;

import de.htwg.se.dog.models.CardInterface;

public class Card implements CardInterface {

    private final int value;
    private static final int ONE = 1;
    private static final int FOURTEEN = 14;

    private static final String[] CARDNAMES = { "Ass", "Zwei", "Drei", "Vier", "Fuenf", "Sechs", "Sieben", "Acht", "Neun", "Zehn", "Bube", "Dame", "Koenig", "Joker" };

    /**
     * Constructor to create a new card
     * 
     * @param value
     *        : int, is the value of the card
     */
    public Card(int value) {
        if (value < ONE || value > FOURTEEN) {
            throw new IllegalArgumentException("Failed to create card with value:" + value);
        } else {
            this.value = value;
        }
    }

    /**
     * returns the Value of the card
     * 
     * @return int: value of card between [0,14]
     */
    @Override
    public int getValue() {
        return this.value;
    }

    /**
     * Return the CardName of a Card
     * 
     * @param value
     *        : Int value of the card
     * @return String: the cardname as a string
     */
    @Override
    public String getCardName() {
        return CARDNAMES[this.value - 1];
    }

    @Override
    public String toString() {
        return String.format("%s(%d)", getCardName(), this.value);
    }

    @Override
    public int compareTo(CardInterface o) {
        return ((Integer) this.value).compareTo(o.getValue());
    }


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + value;
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Card)) {
			return false;
		}
		Card other = (Card) obj;
		if (value != other.value) {
			return false;
		}
		return true;
	}


}
