# JSCKit
current version:&#8195;![](https://jitpack.io/v/JustinRoom/JSCKit.svg)

# Download
**Download apk by scanning the QRCode below:**  
&#32;&#32;Demo version:0.2.3
![JSCKitDemo.apk](/capture/download_qr_code.png)  
[**Local Download**](/capture/JSCKitDemo.apk?raw=true)

# Usage
##### Gradle: 
**library** dependencies:
```
    implementation 'com.android.support:appcompat-v7:27.1.1'
    implementation 'com.android.support:recyclerview-v7:27.1.1'
```
**ZXingLibrary** dependencies:  
Here is more about [zxing](https://github.com/zxing/zxing).  
Other resources about zxing library:  
+ [zxing-android-embedded](https://github.com/journeyapps/zxing-android-embedded)
+ [BGAQRCode-Android](https://github.com/bingoogolapple/BGAQRCode-Android)
```
    implementation 'com.android.support:appcompat-v7:27.1.1'
    api 'com.google.zxing:core:3.3.2'
```
**Retrofit2Library** dependencies:  
Here is more about [retrofit](https://github.com/square/retrofit).  
```
    //https://github.com/square/retrofit
    api 'com.squareup.retrofit2:retrofit:2.4.0'
    api 'com.squareup.retrofit2:converter-gson:2.4.0'
    api 'com.squareup.retrofit2:converter-scalars:2.4.0'
    api 'com.squareup.retrofit2:adapter-rxjava2:2.4.0'
    //https://github.com/square/okhttp
    api 'com.squareup.okhttp3:logging-interceptor:3.10.0'

    //https://github.com/ReactiveX/RxAndroid
    api 'io.reactivex.rxjava2:rxandroid:2.0.2'
    api 'io.reactivex.rxjava2:rxjava:2.1.12'
```
**DateTimePickerLibrary** dependencies:
```
    implementation 'com.android.support:appcompat-v7:27.1.1'
```
+ 1、Add it in your root build.gradle at the end of repositories:
```
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
+ 2、Add the dependency
```
	dependencies {
	        compile 'com.github.JustinRoom:JSCKit:0.2.3'
	}
```
##### Maven: 
+ 1、Add the JitPack repository to your build file:
```
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
```
+ 2、Add the dependency
```
	<dependency>
	    <groupId>com.github.JustinRoom</groupId>
	    <artifactId>JSCKit</artifactId>
	    <version>0.2.3</version>
	</dependency>
```
# ProGuard
If you are using ProGuard you need to add the following options:
```
-keep class jsc.kit.** { *; }
-keep class jsc.lib.retrofitlibrary.** { *; }
-keep class jsc.lib.zxinglibrary.** { *; }  
-keep class jsc.lib.datetimepicker.** { *; }  
  
#>>>zxing
-keep class com.google.zxing.** { *; }
  
#>>>retrofit2
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain service method parameters.
-keepclassmembernames,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
# Ignore annotation used for build tooling.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
  
#>>>okhttp3
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn org.conscrypt.**
# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase
  
#>>>rxandroid
-keep class io.reactivex.android.**{*;}
-keep class io.reactivex.**{*;}
```

# Content
### Component list:

| index | Component | Article |
|:---:|:---|:---|
| 1  | [**LGradientArcHeaderView**](/library/src/main/java/jsc/kit/archeaderview) | [**ArcHeaderView和ArcHeaderDrawable**](https://www.jianshu.com/p/ded0dc4ea528) |
| 2  | [**PictureArcHeaderView**](/library/src/main/java/jsc/kit/archeaderview) | [***同上***](https://www.jianshu.com/p/ded0dc4ea528) |
| 3  | [**ArcHeaderDrawable**](/library/src/main/java/jsc/kit/archeaderview) | [***同上***](https://www.jianshu.com/p/ded0dc4ea528) |
| 4  | [**JSCBannerView**](/library/src/main/java/jsc/kit/bannerview) | [**用ViewPager打造高性能广告轮播控件BannerView**](https://www.jianshu.com/p/652090682b31) |
| 5  | [**MonthView**](/library/src/main/java/jsc/kit/monthview) | [**日历 MonthView**](https://www.jianshu.com/p/2387952b3d34) |
| 6  | [**ReboundFrameLayout**](/library/src/main/java/jsc/kit/reboundlayout) | [**仿IOS拖拽回弹之进阶ReboundFrameLayout**](https://www.jianshu.com/p/53d13719a6c4) |
| 7  | [**RefreshLayout**](/library/src/main/java/jsc/kit/refreshlayout) | [**打造类似SwipeRefreshLayout的下拉刷新控件**](https://www.jianshu.com/p/b582bd08d4f9) |
| 8  | [**VerticalStepView**](/library/src/main/java/jsc/kit/stepview) | [**公交线路 VerticalStepView**](https://www.jianshu.com/p/7721572fe13c) |
| 9  | [**JSCRoundCornerProgressBar**](/library/src/main/java/jsc/kit/progressbar) |  |
| 10 | [**JSCItemLayout**](/library/src/main/java/jsc/kit/itemlayout) |  |
| 11 | [**VScrollScreenLayout**](/library/src/main/java/jsc/kit/vscrollscreen) | [**打造上下滑动翻屏VScrollScreenLayout**](https://www.jianshu.com/p/b12afbf7de30) |
| 12 | [**RadarView**](/library/src/main/java/jsc/kit/radarview) | [**雷达(蜘蛛网)图RadarView**](https://www.jianshu.com/p/94a4b763a4e5) |
| 13 | [**TurntableView**](/library/src/main/java/jsc/kit/turntable) | [**抽奖转盘TurntableView**](https://www.jianshu.com/p/3c473e1e007b) |
| 14 | [**SwipeRefreshRecyclerView**](/library/src/main/java/jsc/kit/swiperecyclerview) | [**用SwipeRefreshLayout+RecyclerView精心打造下拉刷新控件**](https://www.jianshu.com/p/f1da8cd366cb)|
| 15 | [**RippleView**](/library/src/main/java/jsc/kit/rippleview) | [**水波纹效果RippView**](https://www.jianshu.com/p/e573110c38d4)|

### About functionality:

| index | Functionality | Article |
|:---:|:---|:---|
| 1  | [**MyPermissionChecker**](/library/src/main/java/jsc/kit/utils) | [**简洁易用andrioid6.0+权限请求组件**](https://www.jianshu.com/p/47052d575f5b)|
| 2  | [**DateTimePicker**](/DateTimePickerLibrary/src/main/java/jsc/lib/datetimepicker/widget/DateTimePicker.java) | [**日期选择器DateTimePicker**](https://www.jianshu.com/p/db19efcaa226) |
| 3  | [**CustomToast**](/library/src/main/java/jsc/kit/utils) | |
