package View;

/**
* item of a combo box
*/
final class ItemComboBox {
    private final boolean key; // real value
    private final String value; // value to show to the user

    ItemComboBox(boolean key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * returns the key of the element of the combo box.
     * @return value used as an identifier of the element of the combo box
     */
    public boolean getKey() { return key; }

    /**
     * returns the value of the element to show to the user.
     * @return value to show the user
     */
    @Override
    public String toString() {return value; }
}
