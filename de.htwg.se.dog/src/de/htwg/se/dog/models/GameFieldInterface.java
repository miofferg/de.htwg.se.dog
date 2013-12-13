package de.htwg.se.dog.models;

public interface GameFieldInterface extends Cloneable {

    /**
     * returns the owner number of a field
     * 
     * @param fieldNr
     *            number of the field
     * 
     * @return int: ownerNr of the fieldNr, if it has no owner zero is returned
     */
    int getOwner(int fieldNr);

    /**
     * Returns the gamefieldarray
     * 
     * @return Field[]: the complete GameField
     */
    FieldInterface[] getField();

    /**
     * returns the gamefieldsize
     * 
     * @return int: the size of the complete Field
     */
    int getFieldSize();

    /**
     * returns number of housefields
     * 
     * @return int: the numberofHouseFields for each Player
     */
    int getHouseCount();

    /**
     * returns number of fields between two houses
     * 
     * @return
     */
    int getFieldsTillHouse();

    /**
     * returns the number of players playing
     * 
     * @return
     */
    int getPlayerCount();

    /**
     * Creates a String representation of the Gamefield
     * 
     * @return the gamfield as String
     */
    @Override
    String toString();

    /**
     * Clone a GameField
     * @return
     */
    Object clone() throws CloneNotSupportedException;
}
