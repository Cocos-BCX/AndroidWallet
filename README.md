[中文](https://github.com/Cocos-BCX/AndroidWallet/blob/master/README_cn.md)

# Cocos-BCX Android Wallet Compiling and Running Guide
### 1. Compiler selection and environment building

##### Download and install [Android Studio](https://developer.android.google.cn/studio) 3.0 +

##### Download [JDK](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) 1.7+

##### Jdk environment variable configuration:[window](https://www.cnblogs.com/yanhuan123/p/7093211.html)、[mac](https://www.cnblogs.com/xd502djj/p/6642133.html)

##### Android Studio finds and uses the installed jdk by searching the JAVA_HOME variable.

##### [Configure SDK, JDK path](https://www.cnblogs.com/lzwangshubo/p/10165064.html) in Android Studio

### 2. Compille

1.64K problem:

build.gradle

... dependencies { ...

implementation 'com.android.support:multidex:1.0.3' //add
} ... android {

defaultConfig {
    ...

	multiDexEnabled true //add
}
}

ref url

https://stackoverflow.com/questions/48249633/errorcannot-fit-requested-classes-in-a-single-dex-file-try-supplying-a-main-dex .

2.http limit

Android P (version 27) limits the http network requests. However, http request is used in the SDK. if your application is target Android 9 (API level 28) or higher, you can modify AndroidManifest.xml like below:

AndroidManifest.xml :

<manifest ...> <application ... android:usesCleartextTraffic="true" //add ...>

ref url

https://stackoverflow.com/questions/45940861/android-8-cleartext-http-traffic-not-permitted

3.relase 编译问题 编译可能会出现没有配置Relaese_key 的错误提示，去app模块的build.gradle下注释release 配置即可。


### 3. After the compilation is complete, user can run the project to the virtual machine or mobile phone;

#### Any problem about compilation can be raised in the issues for a timely response.

Note: [The cocos-sdk module source code](https://github.com/Cocos-BCX/AndroidSdk) in the project can also be compiled and run following above steps.
