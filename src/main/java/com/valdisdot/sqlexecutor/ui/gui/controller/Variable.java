package com.valdisdot.sqlexecutor.ui.gui.controller;

//variable for lambdas
public class Variable<V> {
    private V value;

    public Variable() {
    }

    public Variable(V initial) {
        this.value = initial;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }
}
