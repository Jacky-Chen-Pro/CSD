package com.android.incongress.cd.conference.beans;

/**
 * Created by Jacky on 2016/1/29.
 */
public class MessageBean {
    private String content;
    private String createTime;
    private int type;
    private String url;
    private int tokenMessageId;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getTokenMessageId() {
        return tokenMessageId;
    }

    public void setTokenMessageId(int tokenMessageId) {
        this.tokenMessageId = tokenMessageId;
    }

    @Override
    public String toString() {
        return "MessageBean{" +
                "content='" + content + '\'' +
                ", createTime='" + createTime + '\'' +
                ", type=" + type +
                ", url='" + url + '\'' +
                ", tokenMessageId=" + tokenMessageId +
                '}';
    }
}
