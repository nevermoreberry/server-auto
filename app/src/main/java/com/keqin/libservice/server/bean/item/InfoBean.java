package com.keqin.libservice.server.bean.item;

/**
 * 基础数据
 *
 * @author Created by gold on 2018/3/6 11:25
 */
public class InfoBean {
    private String name;
    private String value;
    private String url;
    private String thumbUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }
}
