/*
 *    Copyright 2017 ThirtyDegreesRay
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
