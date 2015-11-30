package com.mod_snmp.smiparser.errorhandler;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/
/**
 * RecoveryItem
 */

public class RecoveryItem {
    private int look_ahead_one;
    private int look_ahead_two[];

    public RecoveryItem() {
        this(0, new int[0]);
    }
    public RecoveryItem(int first) {
        this(first, new int[0]);
    }
    public RecoveryItem(int first, int second[]) {
        look_ahead_one = first;
        look_ahead_two = second;
    }
    public boolean has(int first, int second) {
        if (has(first)) {
            if (look_ahead_two.length == 0) {
                return true;
            }
            for (int i = 0; i < look_ahead_two.length; i++) {
                if (look_ahead_two[i] == second) {
                    return true;
                }
            }
        }
        return false;
    }
    public boolean has(int first) {
        if (look_ahead_one == first) {
            return true;
        }
        return false;
    }
}
