package com.blind.wakemeup.utils;

/**
 * Created by delor on 29/01/2018.
 */

public enum UnitTempEnum {
    CELCIUS ("°C"),
    FAHRENHEIT ("°F");

    private String displauStr;
    private UnitTempEnum(String s) {
    displauStr = s;
    }

    public static UnitTempEnum next(UnitTempEnum current){
        int nextOrdinal = current.ordinal() + 1;
        if(nextOrdinal >= UnitTempEnum.values().length) {
            return UnitTempEnum.values()[0];
        } else {
            return UnitTempEnum.values()[nextOrdinal];
        }

    }

    @Override
    public String toString() {
        return displauStr;
    }
}
