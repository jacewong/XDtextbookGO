package com.software.xdtextbookgo.event;

/**
 * Created by huang on 2016/6/5.
 * 因为 RecyclerView 没有 onItemClickListener，所以添加此事件
 */
public class ConversationItemClickEvent {
    public String anotherId;
    public ConversationItemClickEvent(String anotherId) {
        this.anotherId = anotherId;
    }
}
