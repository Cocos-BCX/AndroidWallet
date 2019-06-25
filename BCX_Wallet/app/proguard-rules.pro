
-dontusemixedcaseclassnames          #混淆时不使用大小写混合类名
-dontskipnonpubliclibraryclasses     #不跳过library中的非public的类
-verbose                             #打印混淆的详细信息
-dontoptimize                        #不进行优化，建议使用此选项，
-dontpreverify                       #不进行预校验,Android不需要,可加快混淆速度。
-ignorewarnings                      #忽略警告

#entity
-keep class com.cocos.bcx_wallet.entity.** { *; }
-keep class com.cocos.library_base.entity.** { *; }
-keep class com.cocos.library_base.entity.js_params.** { *; }
-keep class com.cocos.library_base.entity.js_response.** { *; }
-keep class com.cocos.module_asset.entity.** { *; }
-keep class com.cocos.module_found.entity.** { *; }
-keep class com.cocos.module_login.entity.** { *; }
-keep class com.cocos.module_mine.entity.** { *; }

#tkrefreshlayout
-keep class com.lcodecore.tkrefreshlayout.** { *; }
-dontwarn com.lcodecore.tkrefreshlayout.**

#support
-keep class android.support.** { *; }
-keep interface android.support.** { *; }
-dontwarn android.support.**

#databinding
-keep class android.databinding.** { *; }
-dontwarn android.databinding.**

#annotation
-keep class android.support.annotation.** { *; }
-keep interface android.support.annotation.** { *; }

#retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Exceptions

-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}

-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}

-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}

-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

#RxLifecycle
-keep class com.trello.rxlifecycle2.** { *; }
-keep interface com.trello.rxlifecycle2.** { *; }
-dontwarn com.trello.rxlifecycle2.**

#RxPermissions
-keep class com.tbruyelle.rxpermissions2.** { *; }
-keep interface com.tbruyelle.rxpermissions2.** { *; }

#=====================bindingcollectionadapter=====================
-keep class me.tatarka.bindingcollectionadapter.** { *; }
-dontwarn me.tatarka.bindingcollectionadapter.**

#---------------------------------4.反射相关的类和方法-----------------------
-keep public class * extends com.cocos.library_base.base.BaseActivity{ *; }
-keep public class * extends com.cocos.library_base.base.BaseFragment{ *; }
-keep public class * extends com.cocos.library_base.binding.command.BindingCommand{ *; }
-keep public class * extends com.cocos.library_base.binding.command.ResponseCommand{ *; }
-keep public class * implements com.cocos.library_base.config.IModuleInit{ *; }
-keep class com.cocos.library_base.config.** { *; }

#---------------------------------5.自定义控件------------------------------
-keep class com.cocos.library_base.widget.** { *; }

#---------------------------------6.其他定制区-------------------------------
#native方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}
#Parcelable 不被混淆
-keepclassmembers class * implements android.os.Parcelable {
    static android.os.Parcelable$Creator CREATOR;
}
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
#Serializable 不被混淆
-keepnames class * implements java.io.Serializable
#Serializable 不被混淆并且enum 类也不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
#保持枚举 enum 类不被混淆 如果混淆报错，建议直接使用上面的 -keepclassmembers class * implements java.io.Serializable即可
-keepclassmembers enum * {
  public static **[] values();
  public static ** valueOf(java.lang.String);
}
-keepclasseswithmembers,allowshrinking class * {
    native <methods>;
}
-keepclasseswithmembernames class * {
    native <methods>;
}
-keepclassmembers class * {
    public void *ButtonClicked(android.view.View);
}
#不混淆资源类
-keepclassmembers class **.R$* {
    public static <fields>;
}
#保持类中的所有方法名
-keepclassmembers class * {
    public <methods>;
    private <methods>;
}

#保护注解
-keepattributes *Annotation*

#保留SourceFile和LineNumber属性
-keepattributes SourceFile,LineNumberTable

#---------------------------------默认保留区---------------------------------
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}
#保护注解
-keepattributes *Annotation*
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService
-keep class android.support.** {*;}

-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}


-keepattributes Signature

-keepclassmembers class * extends android.content.Context {
   public void *(android.view.View);
   public void *(android.view.MenuItem);
}

#-keepnames class * implements java.io.Serializable
-keep public class * implements java.io.Serializable {
        public *;
}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keep class **.R$* {
 *;
}
-keepclassmembers class * {
    void *(**On*Event);
}
#---------------------------------webview------------------------------------
-keepclassmembers class fqcn.of.javascript.interface.for.Webview {
   public *;
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, jav.lang.String);
}
-dontwarn android.webkit.WebView
# 适配框架混淆
 -keep class me.jessyan.autosize.** { *; }
 -keep interface me.jessyan.autosize.** { *; }


 -keepattributes RuntimeVisibleAnnotations
 -keepattributes RuntimeInvisibleAnnotations
 -keepattributes RuntimeVisibleParameterAnnotations
 -keepattributes RuntimeInvisibleParameterAnnotations

 -keepclassmembers class ** {
     @com.threshold.rxbus2.annotation.RxSubscribe <methods>;
 }
 -keep enum com.threshold.rxbus2.util.EventThread { *; }

#-keep class org.bitcoinj.**
-dontwarn org.bitcoinj.**

#proguard for org.spongycastle
-keep class org.spongycastle.crypto.* {*;}
-keep class org.spongycastle.crypto.agreement.** {*;}
-keep class org.spongycastle.crypto.digests.* {*;}
-keep class org.spongycastle.crypto.ec.* {*;}
-keep class org.spongycastle.crypto.encodings.* {*;}
-keep class org.spongycastle.crypto.engines.* {*;}
-keep class org.spongycastle.crypto.macs.* {*;}
-keep class org.spongycastle.crypto.modes.* {*;}
-keep class org.spongycastle.crypto.paddings.* {*;}
-keep class org.spongycastle.crypto.params.* {*;}
-keep class org.spongycastle.crypto.prng.* {*;}
-keep class org.spongycastle.crypto.signers.* {*;}

-keep class org.spongycastle.jcajce.provider.asymmetric.* {*;}
-keep class org.spongycastle.jcajce.provider.asymmetric.util.* {*;}
-keep class org.spongycastle.jcajce.provider.asymmetric.dh.* {*;}
-keep class org.spongycastle.jcajce.provider.asymmetric.ec.* {*;}
-keep class org.spongycastle.jcajce.provider.asymmetric.x509.* {*;}
-keep class org.spongycastle.jcajce.provider.asymmetric.rsa.* {*;}

-keep class org.spongycastle.jcajce.provider.digest.** {*;}
-keep class org.spongycastle.jcajce.provider.keystore.** {*;}
-keep class org.spongycastle.jcajce.provider.symmetric.** {*;}
-keep class org.spongycastle.jcajce.spec.* {*;}
-keep class org.spongycastle.jce.** {*;}

-dontwarn org.spongycastle.**


-keep public class com.google.vending.licensing.ILicensingService
-dontnote com.google.vending.licensing.ILicensingService

-keep public class com.android.vending.licensing.ILicensingService
-dontnote com.android.vending.licensing.ILicensingService

-keepclassmembers public class * extends android.view.View {
    void set*(%);
    void set*(%, %);
    void set*(%, %, %, %);
    void set*(%[]);
    void set*(**[]);
    void set*(**Listener);

    % get*();
    %[] get*();
    **[] get*();
    **Listener get*();
}

-keepclassmembernames class **.R$* {
    public static <fields>;
}

-keep class SQLite.Database
-keep interface * extends android.*
-keep interface * extends com.android.*
-keepclassmembers class * extends android.content.pm.IPackageDataObserver$Stub { *;}
-keep class android.content.pm.IPackageMoveObserver { *; }

-dontwarn android.webkit.WebView
-dontwarn android.content.pm.IPackageDataObserver
-dontwarn android.content.pm.IPackageMoveObserver
-dontwarn android.os.UserHandle
-dontwarn android.service.notification.StatusBarNotification
-dontwarn android.app.ApplicationPackageManager$MoveCallbackDelegate
-dontwarn javax.management.**
-dontwarn java.lang.management.**
-dontwarn org.apache.log4j.**
-dontwarn org.apache.commons.logging.**
-dontwarn org.slf4j.**
-dontwarn org.json.**
-dontwarn android.support.**

-keep class * extends android.database.sqlite.SQLiteOpenHelper {
	public <init>(android.content.Context, java.lang.String);
}

-keep class LibcoreWrapper.os {*;}
-keep class android.support.v4.app.** {*;}
-keep interface android.support.v4.app.** { *;}
-keep class android.support.v7.widget.** { *;}
-keep class android.support.v7.internal.widget.** { *;}
-keep class android.support.v7.internal.view.menu.** { *;}
-keep class com.android.volley.toolbox.Volley {*;}


-keep class com.google.analytics.tracking.**
-dontwarn com.google.android.gms.**
-dontwarn com.google.analytics.tracking.**

-keepattributes *JavascriptInterface*
-keep class android.webkit.WebSettings {*;}
-dontwarn android.webkit.WebSettings

# 保留常量类
-keep class com.cocos.library_base.sql.contract.**{*;}
-keep class com.cocos.library_base.utils.constant.**{*;}
-keep class com.cocos.library_base.router.**{*;}
-keep class com.cocos.library_base.global.**{*;}


-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# ARouter
-keep public class com.alibaba.android.arouter.routes.**{*;}
-keep class * implements com.alibaba.android.arouter.facade.template.ISyringe{*;}
-keep interface * implements com.alibaba.android.arouter.facade.template.IProvider

#okhttp
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.squareup.okhttp.* { *;}
-keep interface com.squareup.okhttp.** { *;}
-dontwarn com.squareup.okhttp.**
-keepattributes Signature
-keepattributes *Annotation*
-keep class okhttp3.** { *;}
-keep interface okhttp3.** { *;}
-dontwarn okhttp3.**
-dontwarn com.squareup.**
-dontwarn okio.**
-keep public class org.codehaus.* { *;}
-keep public class java.nio.* { *; }

# Retrolambda
-dontwarn java.lang.invoke.*

#rxandroid-1.2.1
-keepclassmembers class rx.android.**{*;}
-dontwarn rx.**
-keep class rx.**{*;}
-dontwarn sun.misc.**

# Gson
-keep class com.google.gson.stream.** { *; }
-keepattributes EnclosingMethod
-keep class org.xz_sale.entity.**{*;}
-keep class com.google.gson.** {*;}
-keep class com.google.**{*;}
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.examples.android.model.** { *; }
-keep class com.sunloto.shandong.bean.** { *; }
-dontwarn com.google.**