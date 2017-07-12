/*
 *    Copyright 2014 ThirtyDegressRay
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

package com.thirtydereesray;

import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

public class AppDaoGenerator {
    public static void main(String[] args) throws Exception {
        Schema rootSchema = new Schema(1, "com.thirtydegreesray.openhub.db");
        new DaoGenerator().generateAll(rootSchema, "app/src/main/java");
    }

    private static void addConfig(Schema schema){
        Entity entity = schema.addEntity("AppConfig");
        entity.addStringProperty("baseUrl");
    }

    /**
     * 添加user表
     * @param schema
     */
    private static void addUser(Schema schema){
        Entity entity = schema.addEntity("User");
        entity.addStringProperty("userId").primaryKey();
        entity.addStringProperty("password");
    }
}
