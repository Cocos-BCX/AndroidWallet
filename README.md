# Cocos-BCX Android Wallet 编译运行指南
### 1.编译器选择和环境搭建

##### 下载安装 3.0版本以上的[Android Studio](https://developer.android.google.cn/studio)

##### 下载[JDK 1.7及以上版本](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)

##### Jdk 环境变量配置:[window](https://www.cnblogs.com/yanhuan123/p/7093211.html)、[mac](https://www.cnblogs.com/xd502djj/p/6642133.html)
##### Android Studio 通过搜索JAVA_HOME变量来找到并使用安装好的jdk。

##### Android Studio中 [配置 SDK、JDK 路径](https://www.cnblogs.com/lzwangshubo/p/10165064.html) 

### 2.clone 项目代码到本地，使用 Android Studio 导入项目，编译器会自动编译，编译会下载编译工具和项目中的依赖库，时间可能较长。

### 3.编译可能会出现没有配置Relaese_key 的错误提示，去app模块的build.gradle下删除release 配置即可。

### 4.编译结束后即可运行项目到虚拟机或手机上；

#### 如果编译出现问题可在 issues 中提出，会得到及时回复。

附： 项目中 cocos-sdk moudle [源码地址](https://github.com/Cocos-BCX/AndroidSdk),同上可编译运行。
