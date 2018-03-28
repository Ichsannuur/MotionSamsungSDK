package com.ics.callmotionsample.Object;

/**
 * Created by Ichsan.Fatiha on 1/2/2018.
 */

public class Contact {
    public String name;
    public String number;

    public Contact(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
