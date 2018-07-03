# Retrofit2Library
[ ![Download](https://api.bintray.com/packages/justinquote/maven/Retrofit2Library/images/download.svg?version=0.3.4) ](https://bintray.com/justinquote/maven/Retrofit2Library/0.3.4/link)

### dependencies:   
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
Here is more about [retrofit](https://github.com/square/retrofit). 

### Maven:
```
<dependency>
  <groupId>jsc.kit.retrofit2</groupId>
  <artifactId>Retrofit2Library</artifactId>
  <version>_latestVersion</version>
  <type>pom</type>
</dependency>
```
### Gradle: 
```
compile 'jsc.kit.retrofit2:Retrofit2Library:_latestVersion'
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
```
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
### Content:
### Component list:

| index | Component | Article |
|:---:|:---|:---|
