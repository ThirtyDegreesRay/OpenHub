

package com.thirtydegreesray.openhub.common;

import org.greenrobot.eventbus.EventBus;

/**
 * 事件总线
 * Created by ThirtyDegreesRay on 2016/8/22 14:55
 */

public enum  AppEventBus {
    INSTANCE;

    AppEventBus(){
        init();
    }

    private EventBus eventBus ;

    private void init(){
        eventBus = EventBus.builder()
                .installDefaultEventBus();
    }

    public EventBus getEventBus() {
        return eventBus;
    }
}
