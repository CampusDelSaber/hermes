package com.isc.hermes.model.navigation.directions;

public enum DirectionEnum {
    GO_STRAIGHT("Go straight"),
    TURN_LEFT("Turn left"),
    TURN_RIGHT("Turn right");


    private final String text;

    DirectionEnum(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
