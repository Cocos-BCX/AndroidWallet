[English](https://github.com/Cocos-BCX/AndroidWallet/blob/master/README.md)

# Cocos-BCX Android Wallet 编译运行指南
### 1.编译器选择和环境搭建

##### 下载安装3.0版本以上的[Android Studio](https://developer.android.google.cn/studio)

##### 下载[JDK 1.7及以上版本](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)

##### Jdk 环境变量配置:[window](https://www.cnblogs.com/yanhuan123/p/7093211.html)、[mac](https://www.cnblogs.com/xd502djj/p/6642133.html)
##### Android Studio 通过搜索JAVA_HOME变量来找到并使用安装好的jdk。

##### Android Studio中 [配置 SDK、JDK 路径](https://www.cnblogs.com/lzwangshubo/p/10165064.html) 

### 2.编译
clone 项目代码到本地，使用 Android Studio 导入项目，编译器会自动编译，编译会下载编译工具和项目中的依赖库，时间可能较长。
编译可能遇到的问题：
1.64K问题:

build.gradle

...
dependencies {
    ...

    implementation 'com.android.support:multidex:1.0.3' //add
}
...
android {

	defaultConfig {
        ...

		multiDexEnabled true //add
	}
}

参考链接
https://stackoverflow.com/questions/48249633/errorcannot-fit-requested-classes-in-a-single-dex-file-try-supplying-a-main-dex .

2.网络权限的问题

Android P(版本27以上) 对网络请求http限制，SDK中有使用http请求； 如果项目的 compileSdkVersion是 28 及以上, 修改 AndroidManifest.xml 如下:

AndroidManifest.xml :

<manifest ...> <application ... android:usesCleartextTraffic="true" //add ...>
AndroidManifest.xml :

<?xml version="1.0" encoding="utf-8"?>
<manifest ...>
    <application
        ...
        android:usesCleartextTraffic="true" //add
        ...>
        ...
    </application>
</manifest>
参考链接
https://stackoverflow.com/questions/45940861/android-8-cleartext-http-traffic-not-permitted

3.relase 编译问题
编译可能会出现没有配置Relaese_key 的错误提示，去app模块的build.gradle下注释release 配置即可。

### 4.编译结束后即可运行项目到虚拟机或手机上；

#### 如果编译出现问题可在 issues 中提出。





附： 项目中 cocos-sdk moudle [源码地址](https://github.com/Cocos-BCX/AndroidSdk),同上可编译运行。
