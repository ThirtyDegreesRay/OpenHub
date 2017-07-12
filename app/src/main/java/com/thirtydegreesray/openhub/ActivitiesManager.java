/*
 *    Copyright 2017 ThirtyDegressRay
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.thirtydegreesray.openhub;

import android.app.Activity;

import com.thirtydegreesray.openhub.ui.activity.base.BaseActivity;

import java.util.ArrayList;

/**
 * activity管理
 * Created on 2016/10/9.
 *
 * @author YuYunhao
 */

public class ActivitiesManager {

    private static class SingletonHolder{
        private static ActivitiesManager instance = new ActivitiesManager();
    }

    private ActivitiesManager(){
        activities = new ArrayList<>();
    }

    public static ActivitiesManager getInstance(){
        return SingletonHolder.instance;
    }

    private ArrayList<BaseActivity> activities;

    public void addActivity(BaseActivity activity){
        activities.add(activity);
    }

    public void removeActivity(BaseActivity activity){
        activities.remove(activity);
    }

    public void clearActivity(){
        for(Activity activity : activities){
            if(activity != null && !activity.isFinishing()){
                activity.finish();
            }
        }
        activities.clear();
    }


    public void finishActivity(Activity activity, int layerNum, boolean refreshPage){
        for (int i = activities.size() - 1, j = 0; i >= 0; i--) {
            if(activity.equals(activities.get(i)) || ( j > 0 && j < layerNum )){
                activities.get(i).finish();
                j++;
            }else if( j >= layerNum ){
                if(refreshPage){
                    activities.get(i).onRefreshWebPage();
                }
                return ;
            }
        }
    }

    public void exitApp(){
        clearActivity();
        System.exit(0);
    }

}
