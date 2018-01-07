

package com.thirtydegreesray.openhub.util;

/**
 * Created by ThirtyDegreesRay on 2017/11/8 12:46:17
 */

public class StarWishesHelper {

    private final static long TIP_INTERVAL = 30L * 24 * 60 * 60 * 1000;

    public static boolean isStarWishesTipable(){
        long lastTipTime = PrefUtils.getLastStarWishesTipTime();
        int tipTimes = PrefUtils.getStarWishesTipTimes();
        long curTime = System.currentTimeMillis();
        long firstInstallTime = AppUtils.getFirstInstallTime();
        long preTime = Math.max(lastTipTime, firstInstallTime);
        //2, 9, 64, 625...
        double intervalTimes = Math.pow(tipTimes + 2, tipTimes + 1);

        return curTime - preTime > TIP_INTERVAL * intervalTimes;
    }

    public static void addStarWishesTipTimes(){
        int times = PrefUtils.getStarWishesTipTimes();
        times++;
        PrefUtils.set(PrefUtils.STAR_WISHES_TIP_TIMES, times);
        PrefUtils.set(PrefUtils.LAST_STAR_WISHES_TIP_TIME, System.currentTimeMillis());
    }

    public static int getInstalledDays(){
        long installTime = AppUtils.getFirstInstallTime();
        long subTime = System.currentTimeMillis() - installTime;
        int dayTime = 24 * 60 * 60 * 1000;
        int dayNum = (int) (subTime / dayTime);
        dayNum = subTime % dayTime == 0 ? dayNum : dayNum + 1 ;
        return dayNum;
    }

}
