package com.xtagwgj.view.banner;

public class BannerBean<T> {
    private String title;
    private String urls;
    private T data;

    public BannerBean() {
        this.title = "";
        this.urls = "";
    }

    public BannerBean(T data) {
        this();
        this.data = data;
    }

    public BannerBean(String title, String urls, T data) {
        this.title = title;
        this.urls = urls;
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrls() {
        return urls;
    }

    public void setUrls(String urls) {
        this.urls = urls;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
