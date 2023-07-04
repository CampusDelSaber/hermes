package com.isc.hermes.model.navigation.directions;

/**
 * This class have the types of directions.
 */
public enum DirectionEnum {
    GO_STRAIGHT("Go straight"),
    TURN_LEFT("Turn left"),
    TURN_RIGHT("Turn right");


    private final String text;

    DirectionEnum(String text) {
        this.text = text;
    }

    /**
     * This method get the tex of the type of direction.
     *
     * @return the type of direction in text.
     */
    public String getText() {
        return text;
    }
}
