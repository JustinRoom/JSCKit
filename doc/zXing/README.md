# ZXingLibrary
[ ![Download](https://api.bintray.com/packages/justinquote/maven/ZXingLibrary/images/download.svg) ](https://bintray.com/justinquote/maven/ZXingLibrary/_latestVersion)

### dependencies:  
```
implementation 'com.android.support:appcompat-v7:27.1.1'
api 'com.google.zxing:core:3.3.2'
```
Here is more about [zxing](https://github.com/zxing/zxing).  
Other resources about zxing library:  
+ [zxing-android-embedded](https://github.com/journeyapps/zxing-android-embedded)
+ [BGAQRCode-Android](https://github.com/bingoogolapple/BGAQRCode-Android)

##### Maven:
```
<dependency>
  <groupId>jsc.kit.zxing</groupId>
  <artifactId>ZXingLibrary</artifactId>
  <version>_latestVersion</version>
  <type>pom</type>
</dependency>
```
##### Gradle:
```
compile 'jsc.kit.zxing:ZXingLibrary:_latestVersion'
```
### ProGuard:
```
#>>>zxing
-keep class com.google.zxing.** { *; }
```
### Content:
### Component list:

| index | Component | Article |
|:---:|:---|:---|
