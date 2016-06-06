package com.software.xdtextbookgo.event;

/**
 * Created by huang on 2016/6/6.
 */
public class ConnectionChangeEvent {
    public boolean isConnect;
    public ConnectionChangeEvent(boolean isConnect) {
        this.isConnect = isConnect;
    }
}
