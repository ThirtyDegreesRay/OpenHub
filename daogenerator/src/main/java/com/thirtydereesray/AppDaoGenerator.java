

package com.thirtydereesray;

import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

public class AppDaoGenerator {
    public static void main(String...args) throws Exception {
        Schema rootSchema = new Schema(1, "com.thirtydegreesray.openhub.dao");
        addAuthUser(rootSchema);
        new DaoGenerator().generateAll(rootSchema, "app/src/main/java");
    }

    /**
     * add use
     * @param schema
     */
    private static void addAuthUser(Schema schema){
        Entity entity = schema.addEntity("AuthUser");
        entity.addStringProperty("accessToken").primaryKey().notNull();
        entity.addDateProperty("authTime").notNull();
        entity.addIntProperty("expireIn").notNull();
        entity.addStringProperty("scope").notNull();
        entity.addBooleanProperty("selected").notNull();

        entity.addStringProperty("loginId").notNull();
        entity.addStringProperty("name");
        entity.addStringProperty("avatar");
    }
}
