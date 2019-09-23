package com.tsystems.javaschool.tasks.calculator;

import jdk.nashorn.internal.ir.annotations.Immutable;

@Immutable
public class CharacterToken extends Token {
    private char character;

    public CharacterToken(char character) {
        this.character = character;
    }

    public char getCharacter() {
        return character;
    }

    public boolean is(char ch) {
        return ch == character;
    }

    public int getPriority() {
        switch (character) {
            case '*':
            case '/':
                return 2;
            case '+':
            case '-':
                return 1;
            default:
                return 0;
        }
    }

    public boolean isOperation() {
        return getPriority() > 0;
    }
}
