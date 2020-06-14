package me.rarstman.basictools.util;

import org.apache.commons.lang.StringUtils;

public class MinecraftCycleUtil {

    public static Long getMinecraftTime(final String string){
        Long time = null;

        switch (string.toLowerCase()) {
            case "daystart":
            case "day": {
                time = 0L;
                break;
            }
            case "noon":
            case "midday": {
                time = 6000L;
                break;
            }
            case "dawn":
            case "sunrise": {
                time = 23000L;
                break;
            }
            case "morning": {
                time = 1000L;
                break;
            }
            case "afternoon": {
                time = 9000L;
                break;
            }
            case "sundown":
            case "nightfall":
            case "dusk":
            case "sunset": {
                time = 12000L;
                break;
            }
            case "nightstart":
            case "night": {
                time = 14000L;
                break;
            }
            case "midnight": {
                time = 18000L;
                break;
            }
            default: {
                if (!StringUtils.isNumeric(string)) {
                    break;
                }
                time = Long.valueOf(string);
                break;
            }
        }
        return time > 240000L ? null : time;
    }

    public static boolean isStormy(final String string){
        Boolean isStormy = null;

        switch (string.toLowerCase()) {
            case "thunder":
            case "rain":
            case "storm": {
                isStormy = true;
                break;
            }
            case "clear":
            case "sun": {
                isStormy = false;
                break;
            }
        }
        return isStormy;
    }

    public static boolean isThundering(final String string){
        Boolean isThundering = null;

        switch (string.toLowerCase()) {
            case "thunder": {
                isThundering = true;
                break;
            }
            case "storm":
            case "rain":
            case "clear":
            case "sun": {
                isThundering = false;
                break;
            }
        }
        return isThundering;
    }

    public static String minecraftHourFromMills(long time){
        time = time - 18000L + 24000L;
        final int hours = Math.round(time/1000L);
        final int minutes = Math.round((time-hours*1000L)/60L);
        return (hours < 10 ? "0" + String.valueOf(hours) : String.valueOf(hours)) + ":" + (minutes < 10 ? "0" + String.valueOf(minutes) : String.valueOf(minutes));
    }
}
