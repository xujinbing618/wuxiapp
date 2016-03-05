package com.magus.enviroment.ep.bean;

import java.io.Serializable;

/**
 * Created by pau on 15/6/8.
 */
public class PagerInfo implements Serializable {
    private String code;
    private String url;
    public PagerInfo(){

    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
