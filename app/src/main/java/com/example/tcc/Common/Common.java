package com.example.tcc.Common;

public class Common {
    public static final int TIME_SLOT_TOTAL = 8;

    public static String convertTimeSlotToString(int slot){
        switch(slot) {
            case 0:
                return "08:00-10:00";
            case 1:
                return "10:00-12:00";
            case 2:
                return "12:00-14:00";
            case 3:
                return "14:00-16:00";
            case 4:
                return "16:00-18:00";
            case 5:
                return "18:00-20:00";
            case 6:
                return "20:00-22:00";
            case 7:
                return "22:00-00:00";
            default:
                return "Fechado";
        }
    }
}
