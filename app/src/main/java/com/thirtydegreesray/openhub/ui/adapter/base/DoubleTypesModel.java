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

package com.thirtydegreesray.openhub.ui.adapter.base;

/**
 * Created by ThirtyDegreesRay on 2017/10/18 16:03:04
 */

public class DoubleTypesModel<M1, M2> {

    private M1 m1;
    private M2 m2;

    public DoubleTypesModel(M1 m1, M2 m2) {
        this.m1 = m1;
        this.m2 = m2;
    }

    public M1 getM1() {
        return m1;
    }

    public M2 getM2() {
        return m2;
    }

    public int getTypePosition(){
        if(m1 != null){
            return 0;
        } else if(m2 != null){
            return 1;
        } else {
            return 0;
        }
    }

}
