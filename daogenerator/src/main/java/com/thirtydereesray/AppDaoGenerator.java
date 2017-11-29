

package com.thirtydereesray;

import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

public class AppDaoGenerator {

    public static void main(String...args){
        Schema rootSchema = new Schema(5, "com.thirtydegreesray.openhub.dao");
        addAuthUser(rootSchema);
        addTraceUser(rootSchema);
        addTraceRepo(rootSchema);
        addBookMarkUser(rootSchema);
        addBookMarkRepo(rootSchema);
        addLocalUser(rootSchema);
        addLocalRepo(rootSchema);
        addTrace(rootSchema);
        addBookmark(rootSchema);
        addMyTrendingLanguage(rootSchema);
        try {
            new DaoGenerator().generateAll(rootSchema, "E:/Work/Android/github/OpenHub/OpenHub/app/src/main/java");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * add auth user
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

    private static void addTraceUser(Schema schema){
        Entity entity = schema.addEntity("TraceUser");
        entity.addStringProperty("login").primaryKey().notNull();
        entity.addStringProperty("name");
        entity.addStringProperty("avatarUrl");
        entity.addIntProperty("followers");
        entity.addIntProperty("following");

        entity.addDateProperty("startTime");
        entity.addDateProperty("latestTime");
        entity.addIntProperty("traceNum");
    }

    private static void addTraceRepo(Schema schema){
        Entity entity = schema.addEntity("TraceRepo");
        entity.addLongProperty("id").primaryKey().notNull();
        entity.addStringProperty("name").notNull();
        entity.addStringProperty("description");
        entity.addStringProperty("language");
        entity.addIntProperty("stargazersCount");
        entity.addIntProperty("watchersCount");
        entity.addIntProperty("forksCount");
        entity.addBooleanProperty("fork");

        entity.addStringProperty("ownerLogin");
        entity.addStringProperty("ownerAvatarUrl");

        entity.addDateProperty("startTime");
        entity.addDateProperty("latestTime");
        entity.addIntProperty("traceNum");
    }

    private static void addBookMarkUser(Schema schema){
        Entity entity = schema.addEntity("BookMarkUser");
        entity.addStringProperty("login").primaryKey().notNull();
        entity.addStringProperty("name");
        entity.addStringProperty("avatarUrl");
        entity.addIntProperty("followers");
        entity.addIntProperty("following");

        entity.addDateProperty("markTime");
    }

    private static void addBookMarkRepo(Schema schema){
        Entity entity = schema.addEntity("BookMarkRepo");
        entity.addLongProperty("id").primaryKey().notNull();
        entity.addStringProperty("name").notNull();
        entity.addStringProperty("description");
        entity.addStringProperty("language");
        entity.addIntProperty("stargazersCount");
        entity.addIntProperty("watchersCount");
        entity.addIntProperty("forksCount");
        entity.addBooleanProperty("fork");

        entity.addStringProperty("ownerLogin");
        entity.addStringProperty("ownerAvatarUrl");

        entity.addDateProperty("markTime");
    }

    private static void addLocalUser(Schema schema){
        Entity entity = schema.addEntity("LocalUser");
        entity.addStringProperty("login").primaryKey().notNull();
        entity.addStringProperty("name");
        entity.addStringProperty("avatarUrl");
        entity.addIntProperty("followers");
        entity.addIntProperty("following");
    }

    private static void addLocalRepo(Schema schema){
        Entity entity = schema.addEntity("LocalRepo");
        entity.addLongProperty("id").primaryKey().notNull();
        entity.addStringProperty("name").notNull();
        entity.addStringProperty("description");
        entity.addStringProperty("language");
        entity.addIntProperty("stargazersCount");
        entity.addIntProperty("watchersCount");
        entity.addIntProperty("forksCount");
        entity.addBooleanProperty("fork");

        entity.addStringProperty("ownerLogin");
        entity.addStringProperty("ownerAvatarUrl");
    }

    private static void addTrace(Schema schema){
        Entity entity = schema.addEntity("Trace");
        entity.addStringProperty("id").primaryKey().notNull();
        entity.addStringProperty("type");
        entity.addStringProperty("userId");
        entity.addLongProperty("repoId");

        entity.addDateProperty("startTime");
        entity.addDateProperty("latestTime");
        entity.addIntProperty("traceNum");
    }

    private static void addBookmark(Schema schema){
        Entity entity = schema.addEntity("Bookmark");
        entity.addStringProperty("id").primaryKey().notNull();
        entity.addStringProperty("type").notNull();
        entity.addStringProperty("userId");
        entity.addLongProperty("repoId");

        entity.addDateProperty("markTime");
    }

    private static void addMyTrendingLanguage(Schema schema){
        Entity entity = schema.addEntity("MyTrendingLanguage");
        entity.addStringProperty("slug").primaryKey().notNull();
        entity.addStringProperty("name").notNull();
        entity.addIntProperty("order").notNull();
    }

}
