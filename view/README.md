## 圆形图片
```xml
<com.xtagwgj.view.CircleImageView
android:layout_width="180dp"
android:layout_height="180dp"
android:layout_margin="16dp"
android:src="@mipmap/bg_login"
app:civ_border_color="@color/colorPrimary"
app:civ_border_overlay="false"
app:civ_border_width="1dp"
app:civ_fill_color="@android:color/white" />

```
参数说明：

| 参数名称  | 格式 | 含义 | 默认值 |
| --- | ---| ---|---|
| civ_border_width | dimension | 边框的宽度 | 0 |
| civ_border_overlay | boolean | 边框是否覆盖在图片上 | false |
| civ_border_color | color |边框的颜色 | 黑色 |
| civ_fill_color | color |图片的底色 | 透明 |


## 可一键删除文字的输入控件
> 不能使用 `drawableStart` 替代 `drawableLeft`，同理不能使用 `drawableEnd` 替代 `drawableRight`

```xml
<com.xtagwgj.view.CleanableEditView2
android:id="@+id/cityInputText"
android:layout_width="match_parent"
android:layout_height="48dp"
android:layout_margin="8dp"
android:background="@drawable/edittext_bg"
android:drawableLeft="@mipmap/search_normal"
android:drawableRight="@mipmap/input_close"
android:drawablePadding="8dp"
android:gravity="start|center_vertical"
android:hint="请输入城市"
android:singleLine="true"
android:padding="10dp"
android:textColor="@color/color_text_02"
android:textColorHint="@color/color_text_01"
android:textSize="14sp" />
```
参数说明：

| 参数名称   | 参数含义 | 默认值 |
| ----- | -----| -----|
| drawableLeft | 左侧图标 | 无 |
| drawableRight | 右侧图标 | 无 |


## 常用控件背景自定义
> 减少 selector 文件的编写

```xml
<com.xtagwgj.view.roundview.RoundButton
android:layout_width="match_parent"
android:layout_height="48dp"
android:layout_margin="16dp"
android:text="Button"
android:textColor="@android:color/white"
app:rv_backgroundColor="#28ace0"
app:rv_backgroundPressColor="#cc28ace0"
app:rv_isRadiusHalfHeight="true"
app:rv_textPressColor="@android:color/black" />
```

|参数名称|格式|含义|
|---|---|---|
| rv_backgroundColor | color | 背景色
| rv_backgroundPressColor | color | 按下时的背景色
| rv_cornerRadius | dimension | 背景矩形的角度尺寸，单位dp
| rv_strokeWidth | dimension | 背景矩形的外框宽度，单位 dp
| rv_strokeColor | color |背景矩形的外框颜色
| rv_strokePressColor | color |按下时背景矩形的外框颜色
| rv_textPressColor | color |按下时文字颜色
| rv_isRadiusHalfHeight | boolean | 角度是否为高度的一半
| rv_isWidthHeightEqual | boolean | 宽高是否一样
| rv_cornerRadius_TL | dimension | 左上角矩形的半径，单位 dp
| rv_cornerRadius_TR | dimension | 右上角矩形的半径，单位 dp
| rv_cornerRadius_BL | dimension | 左下角矩形的半径，单位 dp
| rv_cornerRadius_BR | dimension | 右上角矩形的半径，单位 dp
| rv_isRippleEnable | boolean | 是否有波纹效果，21以上生效


## 城市选择
> 这里有两种方式，一种是显示当前的城市，第二种是不需要显示当前的城市，两种引入的方式不一样

- 要显示当前的城市，这里为了方便当前的城市是由当前 app 传过去的值

在要引用的布局中加入 FrameLayout

```xml
<FrameLayout
android:id="@+id/container"
android:layout_width="match_parent"
android:layout_height="match_parent" />
```

在相应的 Activity 中实现接口`CityListSelectFragment.OnCitySelectedListener`，并获取事务加载 Fragment

```kotlin
class MainActivity : BaseActivity<_ViewModel>(), CityListSelectFragment.OnCitySelectedListener {
override fun onCitySelected(infoBean: CityInfoBean) {
LogUtils.e("MainActivity", infoBean)
}

override fun getLayoutId(): Int = R.layout.activity_main

override fun getViewModel(): _ViewModel? {
return null
}

override fun initView() {
supportFragmentManager
.beginTransaction()
//城市名为空，则隐藏当前的城市，与第二种方式一样
.add(R.id.container,CityListSelectFragment.newInstance("城市名"))
.commit()
}

override fun initEventListener() {
}
}
```

- 不需要显示当前城市

在要引用的布局中加入城市选择的 Fragment

```xml
<fragment
android:id="@+id/cityFragment"
android:name="com.xtagwgj.view.citylist.CityListSelectFragment"
android:layout_width="match_parent"
android:layout_height="match_parent"/>
```

在相应的 Activity 中实现接口`CityListSelectFragment.OnCitySelectedListener`


## 轮播图

- 自定义加载图片的方式
- 自定义轮播图显示的布局
- 自定义标题的显示
- 自定义轮播图切换动画
- 自定义指示器样式
- 自定义传入的轮播图信息

布局文件
```xml
<com.xtagwgj.view.banner.Banner
android:id="@+id/banner"
android:layout_width="match_parent"
android:layout_height="150dp"
app:banner_default_image="@mipmap/ic_home_banner"
app:delay_time="5000"
app:image_scale_type="fit_xy"
app:indicator_height="8dp"
app:indicator_width="8dp"
app:is_auto_play="true" />
```
定义图片的显示方式
```kotlin
banner?.setBannerImageLoader(object : BannerImageLoader<String>() {
override fun displayView(context: Context, item: BannerBean<String>, showView: View, position: Int) {
Glide.with(this@AccessFragment)
.load(item.data)
.error(R.mipmap.ic_home_banner)
.into(showView as ImageView)
}
})
```

定义图片的点击事件
```kotlin
banner?.setOnBannerListener { banner, _ ->
if (banner.urls.isNotEmpty()) {
//具体操作
}
}
```
更新轮播图片
```kotlin
banner?.update(bannerBean)
```

最重要的配置，进入页面开启轮播，离开页面停止轮播
```kotlin
override fun onStart() {
super.onStart()
banner?.startAutoPlay()
}

override fun onPause() {
super.onPause()
banner?.stopAutoPlay()
}
```
自定义样式列表如下

|参数名称|格式|含义|默认值
|---|---|---| --- |
| indicator_width | dimension | 指示器的宽度 |屏幕分辨率/40
| indicator_height | dimension | 指示器的高度 |同上
| indicator_margin | dimension | 指示器的外间距 | 5dp
| indicator_drawable_selected | reference |  指示器选择时的资源 |R.drawable.gray_radius
| indicator_drawable_unselected | reference | 指示器未选择时的资源 |R.drawable.white_radius
| image_scale_type | enum |图片的 scaleType 属性|center_crop
| delay_time | integer |轮播间隔|2000
| scroll_time | integer | 图片变换的时间|800
| is_auto_play | boolean | 是否自动轮播|false
| is_indicator_show | boolean | 是否显示指示器|true
| title_background |  color/reference |  标题背景|BannerConfig.TITLE_BACKGROUND
| title_height | dimension |  标题高度|BannerConfig.TITLE_HEIGHT
| title_textcolor | color |  标题字体颜色| BannerConfig.TITLE_TEXT_COLOR
| title_textsize | dimension | 标题字体大小| BannerConfig.TITLE_TEXT_SIZE
| banner_layout | reference | 可自定义轮播图样式的布局|R.layout.banner
| banner_default_image | reference | 未加载时的背景图|R.drawable.no_banner













