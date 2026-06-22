package com.codecool.zongz.dao.model;

import java.awt.*;

public class Colour {
    private String label;
    private Color color;

    public Colour(String value, Color color) {
        this.label = value;
        this.color = color;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setColour(Color color) {
        this.color = color;
    }

    public String getLabel() {
        return label;
    }
    public Color getColor() {
        return color;
    }



}
