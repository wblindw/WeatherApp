package com.blind.wakemeup.utils;

/**
 * Available temperature units.
 */
public enum UnitTempEnum {

    CELCIUS ("°C"),
    FAHRENHEIT ("°F");

    private String displauStr;
    private UnitTempEnum(String s) {
    displauStr = s;
    }

    /**
     * Get next available unit related to the available units and input one.
     * @param current the unit reference.
     * @return the next temperature.
     */
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
