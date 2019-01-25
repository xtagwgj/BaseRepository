# BaseRepository
[![](https://jitpack.io/v/xtagwgj/BaseRepository.svg)](https://jitpack.io/#xtagwgj/BaseRepository)

## 关于

使用前，在 `project` 级别的 `build.gradle` 下配置
```xml
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
 	}
}
```
然后在应用中添加依赖如下
```xml
dependencies {
	//同时添加了base 和 view 的依赖
	implementation 'com.github.xtagwgj:BaseRepository:latest-version'

	//以下是根据具体的需求添加依赖
	implementation 'com.github.xtagwgj.BaseRepository:base:latest-version'
	implementation 'com.github.xtagwgj.BaseRepository:view:latest-version'
}
```

## 文档

### 基础类

* [中文文档][base] 

### 自定义视图

* [中文文档][view] 

### 网络请求

* [中文文档][http] 

### 

## 联系我


[base]:base/README.md
[view]:view/README.md
[http]:http/README.md