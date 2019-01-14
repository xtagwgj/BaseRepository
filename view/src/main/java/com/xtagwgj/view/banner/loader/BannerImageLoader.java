package com.xtagwgj.view.banner.loader;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public abstract class BannerImageLoader<T> implements ImageLoaderInterface<T> {

    @Override
    public View createView(Context context, ViewGroup parent) {
        return new ImageView(context);
    }

}
