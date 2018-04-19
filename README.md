# JSCKit
current version:&#8195;![](https://jitpack.io/v/JustinRoom/JSCKit.svg)

# Download
**Download apk by scanning the QRCode below:**  
&#32;&#32;Demo version:0.1.6
![JSCKitDemo.apk](/capture/apk_qr_code.png)  
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
	        compile 'com.github.JustinRoom:JSCKit:0.1.6'
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
	    <version>0.1.6</version>
	</dependency>
```
# ProGuard
If you are using ProGuard you need to add the following options:
```
-keep class jsc.kit.** { *; }
-keep class jsc.lib.retrofitlibrary.** { *; }
-keep class jsc.lib.zxinglibrary.** { *; }  
  
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
| 1  | [**LGradientArcHeaderView**](/library/src/main/java/jsc/kit/archeaderview) | <a href="https://www.jianshu.com/p/ded0dc4ea528" target="_blank">**ArcHeaderView和ArcHeaderDrawable**</a> |
| 2  | [**PictureArcHeaderView**](/library/src/main/java/jsc/kit/archeaderview) | <a href="https://www.jianshu.com/p/ded0dc4ea528" target="_blank">**同上**</a> |
| 3  | [**ArcHeaderDrawable**](/library/src/main/java/jsc/kit/archeaderview) | <a href="https://www.jianshu.com/p/ded0dc4ea528" target="_blank">**同上**</a> |
| 4  | [**JSCBannerView**](/library/src/main/java/jsc/kit/bannerview) | <a href="https://www.jianshu.com/p/652090682b31" target="_blank">**用ViewPager打造高性能广告轮播控件BannerView**</a> |
| 5  | [**MonthView**](/library/src/main/java/jsc/kit/monthview) | <a href="https://www.jianshu.com/p/2387952b3d34" target="_blank">**日历 MonthView**</a> |
| 6  | [**ReboundFrameLayout**](/library/src/main/java/jsc/kit/reboundlayout) | <a href="https://www.jianshu.com/p/53d13719a6c4" target="_blank">**仿IOS拖拽回弹之进阶ReboundFrameLayout**</a> |
| 7  | [**RefreshLayout**](/library/src/main/java/jsc/kit/refreshlayout) | <a href="https://www.jianshu.com/p/b582bd08d4f9" target="_blank">**打造类似SwipeRefreshLayout的下拉刷新控件**</a> |
| 8  | [**VerticalStepView**](/library/src/main/java/jsc/kit/stepview) | <a href="https://www.jianshu.com/p/7721572fe13c" target="_blank">**公交线路 VerticalStepView**</a> |
| 9  | [**JSCRoundCornerProgressBar**](/library/src/main/java/jsc/kit/progressbar) |  |
| 10 | [**JSCItemLayout**](/library/src/main/java/jsc/kit/itemlayout) |  |
| 11 | [**VScrollScreenLayout**](/library/src/main/java/jsc/kit/vscrollscreen) | <a href="https://www.jianshu.com/p/b12afbf7de30" target="_blank">**打造上下滑动翻屏VScrollScreenLayout**</a> |
| 12 | [**RadarView**](/library/src/main/java/jsc/kit/radarview) | <a href="https://www.jianshu.com/p/94a4b763a4e5" target="_blank">**雷达(蜘蛛网)图RadarView**</a> |
| 13 | [**TurntableView**](/library/src/main/java/jsc/kit/turntable) | <a href="https://www.jianshu.com/p/3c473e1e007b" target="_blank">**抽奖转盘TurntableView**</a> |
| 14 | [**SwipeRefreshRecyclerView**](/library/src/main/java/jsc/kit/swiperecyclerview) | <a href="https://www.jianshu.com/p/f1da8cd366cb" target="_blank">**用SwipeRefreshLayout+RecyclerView精心打造下拉刷新控件**</a> |
| 15 | [**RippleView**](/library/src/main/java/jsc/kit/rippleview) | <a href="https://www.jianshu.com/p/e573110c38d4" target="_blank">**水波纹效果RippView**</a> |

### About functionality:

| index | Functionality | Document |
|:---:|:---|:---|
| 1  | [**MyPermissionChecker**](/library/src/main/java/jsc/kit/utils) | <a href="https://www.jianshu.com/p/47052d575f5b" target="_blank">**简洁易用andrioid6.0+权限请求组件**</a> |
| 2  | [**CustomToast**](/library/src/main/java/jsc/kit/utils) | |

# Attributions
+ [**RadarView**](/library/src/main/java/jsc/kit/radarview):

| index | parameter name | meaning | default value |
|:---:|:---|:---| :---:|
| 1 | rv_layer_count | 多边形层数 | 4 |
| 2 | rv_line_width | 边的粗细 | 1px |
| 3 | rv_line_color | 边的颜色 | 0xFFCCCCCC |
| 4 | rv_output_color | 数据图层颜色 | 0x66FF4081 |
| 5 | rv_dot_color | 顶点颜色 | Color.CYAN |
| 6 | rv_dot_radius | 顶点半径 | 8px |

[example](http://yinping4256.github.io){:target="_blank"}