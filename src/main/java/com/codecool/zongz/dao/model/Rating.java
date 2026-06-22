package com.codecool.zongz.dao.model;

public enum Rating {
    first(1, '*'),
    second(2, '**'),
    third(3, '***'),
    fourth(4, '****'),
    fifth(5, '*****');

    private int value;
    private String valueString;
    public Rating(int value, String valueString){
        this.valueString = valueString;
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    public String getValueString() {
        return valueString;
    }
}
