package com.bh.websocket.entity;

/**
 * Created by zhouyongyi on 2018/7/5.
 */
public class RocketMqMsg {
    String tag;
    String key;
    String msgBody;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMsgBody() {
        return msgBody;
    }

    public void setMsgBody(String msgBody) {
        this.msgBody = msgBody;
    }

    @Override
    public String toString() {
        return "RocketMqMsg{" +
                "tag='" + tag + '\'' +
                ", key='" + key + '\'' +
                ", msgBody='" + msgBody + '\'' +
                '}';
    }
}
