package com.tsystems.javaschool.tasks.calculator;

public class DoubleToken extends Token {
    private double value;

    public DoubleToken(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

}
