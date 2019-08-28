[中文](https://github.com/Cocos-BCX/AndroidWallet/blob/master/README_cn.md)

# Cocos-BCX Android Wallet Compiling and Running Guide
### 1. Compiler selection and environment building

##### Download and install [Android Studio](https://developer.android.google.cn/studio) 3.0 +

##### Download [JDK](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) 1.7+

##### Jdk environment variable configuration:[window](https://www.cnblogs.com/yanhuan123/p/7093211.html)、[mac](https://www.cnblogs.com/xd502djj/p/6642133.html)
##### Android Studio finds and uses the installed jdk by searching the JAVA_HOME variable.

##### [Configure SDK, JDK path](https://www.cnblogs.com/lzwangshubo/p/10165064.html) in Android Studio

### 2. Clone the project code to the local, use Android Studio to import the project, then the compiler will automatically compile. During compilation, the compiler tool and the dependent library in the project will be downloaded, which will take a relatively long time.

### 3. Compiling may result in an error message of Relaese_key not configured. Go to the build.gradle of the app module to remove the release configuration.

### 4. After the compilation is complete, user can run the project to the virtual machine or mobile phone;

#### Any problem about compilation can be raised in the issues for a timely response.

Note: [The cocos-sdk module source code](https://github.com/Cocos-BCX/AndroidSdk) in the project can also be compiled and run following above steps.
