package com.xtagwgj.view.banner.loader;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.xtagwgj.view.banner.BannerBean;

import java.io.Serializable;


public interface ImageLoaderInterface<T> extends Serializable {

    void displayView(Context context, BannerBean<T> item, View showView, int position);

    View createView(Context context, ViewGroup parent);
}
