package me.rarstman.basictools.util;

public class BooleanUtil {

    public static Boolean stringStatusToBoolean(final String string){
        Boolean status = null;

        switch(string){
            case "on": {
                status = true;
                break;
            }
            case "off": {
                status = false;
                break;
            }
        }
        return status;
    }
}
