

package com.thirtydegreesray.openhub.util;

/**
 * Created by ThirtyDegreesRay on 2017/11/8 12:46:17
 */

public class StarWishesHelper {

    private final static int STAR_WISHES_TIP_MAX_TIMES = 3;

    private final static long TIP_START_TIME = 10 * 24 * 60 * 60 * 1000;

    private final static long TIP_INTERVAL = 10 * 24 * 60 * 60 * 1000;

    public static boolean isStarWishesTipable(){
        long lastTipTime = PrefUtils.getLastStarWishesTipTime();
        int tipTimes = PrefUtils.getStarWishesTipTimes();
        long curTime = System.currentTimeMillis();
        long firstInstallTime = AppUtils.getFirstInstallTime();
        if(curTime - firstInstallTime > TIP_START_TIME
                && tipTimes < STAR_WISHES_TIP_MAX_TIMES
                && curTime - lastTipTime > TIP_INTERVAL){
            return true;
        }
        return false;
    }

    public static void addStarWishesTipTimes(){
        int times = PrefUtils.getStarWishesTipTimes();
        times++;
        PrefUtils.set(PrefUtils.STAR_WISHES_TIP_TIMES, times);
        PrefUtils.set(PrefUtils.LAST_STAR_WISHES_TIP_TIME, System.currentTimeMillis());
    }

}
