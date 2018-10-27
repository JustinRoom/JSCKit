# BaseComponentLibrary
[ ![Download](https://api.bintray.com/packages/justinquote/maven/BaseComponentLibrary/images/download.svg) ](https://bintray.com/justinquote/maven/BaseComponentLibrary/_latestVersion)

### dependencies:
```
    api "com.android.support:appcompat-v7:27.1.1"
    api "com.android.support:recyclerview-v7:27.1.1"
    api "com.android.support:design:27.1.1"
    api 'com.android.support.constraint:constraint-layout:2.0.0-alpha1'
```
### Maven:
```
<dependency>
  <groupId>jsc.kit.component</groupId>
  <artifactId>BaseComponentLibrary</artifactId>
  <version>_latestVersion</version>
  <type>pom</type>
</dependency>
```
### Gradle:
```
compile 'jsc.kit.component:BaseComponentLibrary:_latestVersion'
```
### optional:
You may need to add maven url in your app gradle file as below:
```
allprojects {
    repositories {
        ...
        maven { url "https://dl.bintray.com/justinquote/maven" }
    }
}
``` 
### ProGuard:
### Content:
### Component list:

| index | Component | Article |
|:---:|:---|:---|
| 1  | [**LGradientArcHeaderView**](/BaseComponentLibrary/src/main/java/jsc/kit/component/archeaderview) | [**ArcHeaderView和ArcHeaderDrawable**](https://www.jianshu.com/p/ded0dc4ea528) |
| 2  | [**PictureArcHeaderView**](/BaseComponentLibrary/src/main/java/jsc/kit/component/archeaderview) | [***同上***](https://www.jianshu.com/p/ded0dc4ea528) |
| 3  | [**ArcHeaderDrawable**](/BaseComponentLibrary/src/main/java/jsc/kit/component/archeaderview) | [***同上***](https://www.jianshu.com/p/ded0dc4ea528) |
| 4  | [**JSCBannerView**](/BaseComponentLibrary/src/main/java/jsc/kit/component/bannerview) | [**用ViewPager打造高性能广告轮播控件BannerView**](https://www.jianshu.com/p/652090682b31) |
| 5  | [**MonthView**](/BaseComponentLibrary/src/main/java/jsc/kit/component/monthview) | [**日历 MonthView**](https://www.jianshu.com/p/2387952b3d34) |
| 6  | [**ReboundFrameLayout**](/BaseComponentLibrary/src/main/java/jsc/kit/component/reboundlayout) | [**仿IOS拖拽回弹之进阶ReboundFrameLayout**](https://www.jianshu.com/p/53d13719a6c4) |
| 7  | [**RefreshLayout**](/BaseComponentLibrary/src/main/java/jsc/kit/component/refreshlayout) | [**打造类似SwipeRefreshLayout的下拉刷新控件**](https://www.jianshu.com/p/b582bd08d4f9) |
| 8  | [**VerticalStepView**](/BaseComponentLibrary/src/main/java/jsc/kit/component/stepview) | [**公交线路 VerticalStepView**](https://www.jianshu.com/p/7721572fe13c) |
| 9  | [**JSCRoundCornerProgressBar**](/BaseComponentLibrary/src/main/java/jsc/kit/component/progressbar) |  |
| 10 | [**JSCItemLayout**](/BaseComponentLibrary/src/main/java/jsc/kit/component/itemlayout) |  |
| 11 | [**VScrollScreenLayout**](/BaseComponentLibrary/src/main/java/jsc/kit/component/vscrollscreen) | [**打造上下滑动翻屏VScrollScreenLayout**](https://www.jianshu.com/p/b12afbf7de30) |
| 12 | [**RadarView**](/BaseComponentLibrary/src/main/java/jsc/kit/component/radarview) | [**雷达(蜘蛛网)图RadarView**](https://www.jianshu.com/p/94a4b763a4e5) |
| 13 | [**TurntableView**](/BaseComponentLibrary/src/main/java/jsc/kit/component/turntable) | [**抽奖转盘TurntableView**](https://www.jianshu.com/p/3c473e1e007b) |
| 14 | [**SwipeRefreshRecyclerView**](/BaseComponentLibrary/src/main/java/jsc/kit/component/swiperecyclerview) | [**用SwipeRefreshLayout+RecyclerView精心打造下拉刷新控件**](https://www.jianshu.com/p/f1da8cd366cb)|
| 15 | [**RippleView**](/BaseComponentLibrary/src/main/java/jsc/kit/component/rippleview) | [**水波纹效果RippView**](https://www.jianshu.com/p/e573110c38d4)|
| 16 | [**CustomToast**](/BaseComponentLibrary/src/main/java/jsc/kit/component/utils) | |
| 17 | [**AntiShakeUtils**](/BaseComponentLibrary/src/main/java/jsc/kit/component/utils) | [**这才是实现防抖动(防快速点击)的最优雅写法**](https://www.jianshu.com/p/06c5b35b4e51) |
| 18 | [**VerticalColumnarGraphView**](/BaseComponentLibrary/src/main/java/jsc/kit/component/graph) | [**自定义优美的柱形图控件**](https://www.jianshu.com/p/c4b08cf5ce48) |
| 19 | [**AdvertisementView**](/BaseComponentLibrary/src/main/java/jsc/kit/component/advertisement/AdvertisementView.java) | [**记一次App从后台切换到前台显示全屏广告实践**](https://www.jianshu.com/p/b6b0f3c4efb1) |
| 20 | [**PermissionChecker**](/BaseComponentLibrary/src/main/java/jsc/kit/component/baseui/permission/PermissionChecker.java) | [**简洁易用andrioid6.0+权限请求组件**](https://www.jianshu.com/p/47052d575f5b)|
| 21 | [**BaseAppCompatActivity**](/BaseComponentLibrary/src/main/java/jsc/kit/component/baseui/BaseAppCompatActivity.java) |  |
| 22 | [**BasePhotoActivity**](/BaseComponentLibrary/src/main/java/jsc/kit/component/baseui/photo/BasePhotoActivity.java) | [**从相册选取图片，拍照、裁剪一篇就够了**](https://www.jianshu.com/p/bab57479bbad) |
| 23 | [**BaseMVPActivity**](/BaseComponentLibrary/src/main/java/jsc/kit/component/baseui/basemvp/BaseMVPActivity.java) | [**MVP+Retrofit2+RxAndroid解锁新姿势**](https://www.jianshu.com/p/754c0841ba30) |
| 24 | [**ReboundRecyclerView**](/BaseComponentLibrary/src/main/java/jsc/kit/component/reboundlayout) | [**酷炫拖拽反弹ReboundRecyclerView**](https://www.jianshu.com/p/c3f2c9f852ef) |
| 25 | [**CameraMask**](/BaseComponentLibrary/src/main/java/jsc/kit/component/widget/CameraMask.java) | [**二维码扫描，强大的相机遮罩CameraMask**](https://www.jianshu.com/p/ca8a14e1aedc) |
| 26 | [**ScannerCameraMask**](/BaseComponentLibrary/src/main/java/jsc/kit/component/widget/ScannerCameraMask.java) | [***同上***](https://www.jianshu.com/p/ca8a14e1aedc) |
| 27 | [**VerticalStepLinearLayout**](/BaseComponentLibrary/src/main/java/jsc/kit/component/stepview/VerticalStepLinearLayout.java) | [***超级酷炫的Step View，不看你会后悔***](https://www.jianshu.com/p/ccf64bf2e3ed) |
| 28 | [GuideRippleView](/BaseComponentLibrary/src/main/java/jsc/kit/component/guide/GuideRippleView.java) | [**强大实用的功能引导组件**](https://www.jianshu.com/p/c1aaddd93245) |
| 29 | [GuideLayout](/BaseComponentLibrary/src/main/java/jsc/kit/component/guide/GuideLayout.java) | [***同上***](https://www.jianshu.com/p/c1aaddd93245) |
| 30 | [AutoTextSizeView](/BaseComponentLibrary/src/main/java/jsc/kit/component/widget/AutoTextSizeView.java) | [**巧用二分法自动调整字体大小**](https://www.jianshu.com/p/ec3cf23044b6) |
| 31 | [FragmentBackHelper](/BaseComponentLibrary/src/main/java/jsc/kit/component/baseui/fragmentmanager/FragmentBackHelper.java) | [**内存管理之Fragment回退栈管理**](https://www.jianshu.com/p/cf32e55864aa) |
| 32 | [AverageLayout](/BaseComponentLibrary/src/main/java/jsc/kit/component/widget/AverageLayout.java) | [**超级好用的均分布局AverageLayout**](https://www.jianshu.com/p/c88c2650b062) |
| 33 | [LineChartView](/BaseComponentLibrary/src/main/java/jsc/kit/component/graph/LineChartView.java) | [**折线分布图LineChartView**](https://www.jianshu.com/p/fb7099e4332e) |
| 34 | [ReboundLinearLayout](/BaseComponentLibrary/src/main/java/jsc/kit/component/reboundlayout/ReboundLinearLayout.java) |  |

### Usage examples：

| index | Component | Article |
|:---:|:---|:---|
| 1  | [**BottomNavigationView**](/app/src/main/java/jsc/exam/jsckit/ui/BottomNavigationViewActivity.java) | [**给BottomNavigationView添加未读消息红点提示**](https://www.jianshu.com/p/c08ed0c6d31d) |
| 2  | [**GuidePopupView**](/app/src/main/java/jsc/exam/jsckit/ui/BottomNavigationViewActivity.java) | [**强大实用的功能引导组件**](https://www.jianshu.com/p/c1aaddd93245) |
| 2  | [**GuidePopupWindow**](/app/src/main/java/jsc/exam/jsckit/ui/BottomNavigationViewActivity.java) | [**同上**](https://www.jianshu.com/p/c1aaddd93245) |
| 2  | [**GuideDialog**](/app/src/main/java/jsc/exam/jsckit/ui/BottomNavigationViewActivity.java) | [**同上**](https://www.jianshu.com/p/c1aaddd93245) |


##### update log:
## v0.4.7:
add some components:
+ [ReboundLinearLayout](/BaseComponentLibrary/src/main/java/jsc/kit/component/reboundlayout/ReboundLinearLayout.java)  
optimize component:
+ [AverageLayout](/BaseComponentLibrary/src/main/java/jsc/kit/component/widget/AverageLayout.java)  
update advertisement demo:
+ [MyApplication](/app/src/main/java/jsc/exam/jsckit/MyApplication.java)
+ [PhotoActivity](/app/src/main/java/jsc/exam/jsckit/ui/PhotoActivity.java)

## v0.4.6:
add some components:
+ [AverageLayout](/BaseComponentLibrary/src/main/java/jsc/kit/component/widget/AverageLayout.java)
+ [LineChartView](/BaseComponentLibrary/src/main/java/jsc/kit/component/graph/LineChartView.java)

## v0.4.5:
add some components:
+ [AutoTextSizeView](/BaseComponentLibrary/src/main/java/jsc/kit/component/widget/AutoTextSizeView.java)
+ [FragmentBackHelper](/BaseComponentLibrary/src/main/java/jsc/kit/component/baseui/fragmentmanager/FragmentBackHelper.java)

## v0.4.4:
add guide navigation components:
+ [GuideRippleView](/BaseComponentLibrary/src/main/java/jsc/kit/component/guide/GuideRippleView.java)
+ [GuideLayout](/BaseComponentLibrary/src/main/java/jsc/kit/component/guide/GuideLayout.java)
+ [GuidePopupView](/BaseComponentLibrary/src/main/java/jsc/kit/component/guide/GuidePopupView.java)
+ [GuidePopupWindow](/BaseComponentLibrary/src/main/java/jsc/kit/component/guide/GuidePopupWindow.java)
+ [GuideDialog](/BaseComponentLibrary/src/main/java/jsc/kit/component/guide/GuideDialog.java)

## v0.4.3:
update [VerticalStepLinearLayout](/BaseComponentLibrary/src/main/java/jsc/kit/component/stepview/VerticalStepLinearLayout.java):
+ support show index at *LEFT* or *RIGHT*.
+ support sort index base on *SORT_BASE_TOP* or *SORT_BASE_FIRST*.

## v0.4.2:
fix a bug about [PermissionChecker](/BaseComponentLibrary/src/main/java/jsc/kit/component/baseui/permission/PermissionChecker.java).  
add some components:  
+ [VerticalStepLinearLayout](/BaseComponentLibrary/src/main/java/jsc/kit/component/stepview/VerticalStepLinearLayout.java)
+ [BlankSpaceItemDecoration](/BaseComponentLibrary/src/main/java/jsc/kit/component/swiperecyclerview/BlankSpaceItemDecoration.java)
+ [VerticalStepItemDecoration](/BaseComponentLibrary/src/main/java/jsc/kit/component/swiperecyclerview/VerticalStepItemDecoration.java)
+ [OverLayCardLayoutManager](/BaseComponentLibrary/src/main/java/jsc/kit/component/swiperecyclerview/manager/OverLayCardLayoutManager.java)  

update some components:

+ [CustomToast](/BaseComponentLibrary/src/main/java/jsc/kit/component/utils/CustomToast.java)
+ [DotView](/BaseComponentLibrary/src/main/java/jsc/kit/component/widget/DotView.java)
+ [JSCItemLayout](/BaseComponentLibrary/src/main/java/jsc/kit/component/itemlayout/JSCItemLayout.java)

## v0.4.1:
fix a bug about [DateTimePicker](/DateTimePickerLibrary/src/main/java/jsc/kit/datetimepicker/widget/DateTimePicker.java) : There are 12 months when the start year is same as the end year.

## v0.4.0:
update demo Components activity
optimize [CameraMask](/BaseComponentLibrary/src/main/java/jsc/kit/component/widget/CameraMask.java)
+ 1、support two different mask shape: square and circular. Call method `setMaskShape(@MaskShape int maskShape)` to change mask shape.

## v0.3.9:
+ 1、add base ui structure. [BaseEmptyFragmentActivity](/BaseComponentLibrary/src/main/java/jsc/kit/component/baseui/BaseEmptyFragmentActivity.java), a custom activity for loading any fragment. For example, see [EmptyFragmentActivity](/app/src/main/java/jsc/exam/jsckit/ui/EmptyFragmentActivity.java).
+ 2、optimize base view structure.
+ 3、optimize component [CameraMask](/BaseComponentLibrary/src/main/java/jsc/kit/component/widget/CameraMask.java).
+ 4、add component [ScannerCameraMask](/BaseComponentLibrary/src/main/java/jsc/kit/component/widget/ScannerCameraMask.java).

## v0.3.8:
add the usage of [CameraMask](/BaseComponentLibrary/src/main/java/jsc/kit/component/widget/CameraMask.java) - [CameraMaskActivity](/app/src/main/java/jsc/exam/jsckit/ui/component/CameraMaskActivity.java).  
optimize component [CameraMask](/BaseComponentLibrary/src/main/java/jsc/kit/component/widget/CameraMask.java):
+ 1、support set tip text dynamically
+ 2、support set the location of tip text dynamically
+ 3、support set the location of camera lens dynamically
+ 4、support set the mask color of camera lens dynamically

## V0.3.7:
+ 1、fix a bug about [VerticalColumnarGraphView](/BaseComponentLibrary/src/main/java/jsc/kit/component/graph) : The detail information view isn't closed when click the blank area.
+ 2、update base view structure : Change [BaseViewProvider](/BaseComponentLibrary/src/main/java/jsc/kit/component/baseui/baseview/BaseViewProvider.java)'s root view as ConstraintLayout.
+ 3、add component [CameraMask](/BaseComponentLibrary/src/main/java/jsc/kit/component/widget/CameraMask.java).