package com.thinkincode.events_android.viewmodel;

public enum Messages {

    SAVE_ENTITY_ERROR( "We couldn't save the entity"),
    SAVE_ENTITY_SUCCESSFUL("Saved!!"),
    SAVE_USER_SUCCESSFUL("User registered"),
    SAVE_USER_ERROR ("Error in register"),
    TOKEN_ERROR ("Password or User incorrect"),
    LOGIN_USER_ERROR ("We can't find the user"),
    ERROR_GET_EVENTS("A error getting the data");


    private final String text;

    /**
     * @param text
     */
    Messages(final String text) {
        this.text = text;
    }

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return text;
    }
}
