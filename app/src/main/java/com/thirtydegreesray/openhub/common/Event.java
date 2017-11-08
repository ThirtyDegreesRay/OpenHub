

package com.thirtydegreesray.openhub.common;

import com.thirtydegreesray.openhub.mvp.model.Repository;
import com.thirtydegreesray.openhub.mvp.model.SearchModel;

/**
 * 事件
 * Created by ThirtyDegreesRay on 2016/8/22 14:32
 */

public class Event {

    /**
     * 网络状态改变事件
     */
    public static class NetChangedEvent{
        public int preNetStatus;
        public int curNetStatus;
        public NetChangedEvent(int preNetStatus, int curNetStatus){
            this.preNetStatus = preNetStatus;
            this.curNetStatus = curNetStatus;
        }
    }

    public static class ServerStatusChangedEvent{
        public final static int SERVER_START = 0;

        public final static int SERVER_STOP = 1;

        public final static int SERVER_STARTED = 2;

        public int serverStatus;

        public ServerStatusChangedEvent(int serverStatus) {
            this.serverStatus = serverStatus;
        }
    }

    public static class RepoInfoUpdatedEvent{
        public Repository repository;

        public RepoInfoUpdatedEvent(Repository repository) {
            this.repository = repository;
        }
    }

    public static class SearchEvent{
        public SearchModel searchModel;

        public SearchEvent(SearchModel searchModel) {
            this.searchModel = searchModel;
        }
    }

}
