# JSCKit
current version:&#8195;![](https://jitpack.io/v/JustinRoom/JSCKit.svg)
### Usage
##### Gradle: 
library dependencies:
```
    implementation 'com.android.support:appcompat-v7:27.1.1'
    implementation 'com.android.support:recyclerview-v7:27.1.1'
```
1、Add it in your root build.gradle at the end of repositories:
```
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
2、Add the dependency
```
	dependencies {
	        compile 'com.github.JustinRoom:JSCKit:0.1.4'
	}
```
##### Maven: 
1、Add the JitPack repository to your build file:
```
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
```
2、Add the dependency
```
	<dependency>
	    <groupId>com.github.JustinRoom</groupId>
	    <artifactId>JSCKit</artifactId>
	    <version>0.1.4</version>
	</dependency>
```

### Download
**Download apk by scanning the QRCode below:**  
&#32;&#32;Demo version:0.1.4
![JSCKitDemo.apk](/capture/apk_qr_code.png)  
[**Local Download**](/capture/JSCKitDemo.apk?raw=true)

### Component list:

+ [**LGradientArcHeaderView**](/library/src/main/java/jsc/kit/archeaderview)————————Document:[ArcHeaderView和ArcHeaderDrawable](https://www.jianshu.com/p/ded0dc4ea528)
+ [**PictureArcHeaderView**](/library/src/main/java/jsc/kit/archeaderview)————————Document:[ArcHeaderView和ArcHeaderDrawable](https://www.jianshu.com/p/ded0dc4ea528)
+ [**ArcHeaderDrawable**](/library/src/main/java/jsc/kit/archeaderview)————————Document:[ArcHeaderView和ArcHeaderDrawable](https://www.jianshu.com/p/ded0dc4ea528)
+ [**JSCBannerView**](/library/src/main/java/jsc/kit/bannerview)————————Document:[用ViewPager打造高性能广告轮播控件BannerView](https://www.jianshu.com/p/652090682b31)
+ [**MonthView**](/library/src/main/java/jsc/kit/monthview)————————Document:[日历 MonthView](https://www.jianshu.com/p/2387952b3d34)
+ [**ReboundFrameLayout**](/library/src/main/java/jsc/kit/reboundlayout)————————Document:[仿IOS拖拽回弹之进阶ReboundFrameLayout](https://www.jianshu.com/p/53d13719a6c4)
+ [**RefreshLayout**](/library/src/main/java/jsc/kit/refreshlayout)————————Document:[打造类似SwipeRefreshLayout的下拉刷新控件](https://www.jianshu.com/p/b582bd08d4f9)
+ [**VerticalStepView**](/library/src/main/java/jsc/kit/stepview)————————Document:[公交线路 VerticalStepView](https://www.jianshu.com/p/7721572fe13c)
+ [**JSCRoundCornerProgressBar**](/library/src/main/java/jsc/kit/progressbar)————————Document:[]()
+ [**JSCItemLayout**](/library/src/main/java/jsc/kit/itemlayout)————————Document:[]()
+ [**VScrollScreenLayout**](/library/src/main/java/jsc/kit/vscrollscreen)————————Document:[打造上下滑动翻屏VScrollScreenLayout](https://www.jianshu.com/p/b12afbf7de30)
+ [**RadarView**](/library/src/main/java/jsc/kit/radarview)————————Document:[雷达(蜘蛛网)图RadarView](https://www.jianshu.com/p/94a4b763a4e5)
+ [**TurntableView**](/library/src/main/java/jsc/kit/turntable)————————Document:[抽奖转盘TurntableView](https://www.jianshu.com/p/3c473e1e007b)
+ [**SwipeRefreshRecyclerView**](/library/src/main/java/jsc/kit/swiperecyclerview)————————Document:[用SwipeRefreshLayout+RecyclerView精心打造下拉刷新控件](https://www.jianshu.com/p/f1da8cd366cb)
+ [**RippleView**](/library/src/main/java/jsc/kit/rippleview)————————Document:[水波纹效果RippView](https://www.jianshu.com/p/e573110c38d4)
