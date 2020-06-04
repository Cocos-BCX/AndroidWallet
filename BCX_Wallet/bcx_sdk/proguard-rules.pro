# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-keepattributes SourceFile,LineNumberTable

-allowaccessmodification
-useuniqueclassmembernames

-dontobfuscate
-dontoptimize
-optimizationpasses 3
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*,!code/allocation/variable

-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-dontnote
-printusage ./build/outputs/mapping/release/shrink.txt

-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keepattributes Signature
-keepattributes *JavascriptInterface*
-keepattributes DynamicViewId
-keepattributes *DynamicViewId*



-keep class com.cocos.bcx_sdk.bcx_wallet.chain.** { *; }
-keep class com.cocos.bcx_sdk.bcx_wallet.common.** { *; }
-keep class com.cocos.bcx_sdk.bcx_wallet.fc.** { *; }
-keep class com.cocos.bcx_sdk.bcx_entity.** { *; }
-keep class com.cocos.bcx_sdk.bcx_callback.** { *; }



-keep public class com.cocos.bcx_sdk.bcx_rpc.R$*{
    public static final int *;
    public static final String *;
    public static final boolean *;
}

#for okhttp3
-dontwarn okhttp3.**
-dontwarn okio.**

#-keep class com.google.**
-dontwarn com.google.**

#-keep class org.bitcoinj.**
-dontwarn org.bitcoinj.**

-dontwarn sun.misc.*
#-keep class org.iq80.**
-dontwarn org.iq80.**

-dontwarn com.fasterxml.jackson.**


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

# For native methods, see http://proguard.sourceforge.net/manual/examples.html#native
-keepclasseswithmembernames class * {
    native <methods>;
}

# Keep setters in Views so that animations can still work.
# Setters for listeners can still be removed.
# see http://proguard.sourceforge.net/manual/examples.html#beans
-keepclassmembers public class * extends android.view.View {
    void set*(%);
    void set*(%, %);
    void set*(%, %, %, %);
    void set*(%[]);
    void set*(**[]);

    % get*();
    %[] get*();
    **[] get*();
}


# For enumeration classes, see http://proguard.sourceforge.net/manual/examples.html#enumerations
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclassmembers class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

-keepclassmembernames class **.R$* {
    public static <fields>;
}

# The support libraries contains references to newer platform versions.
# Don't warn about those in case this app is linking against an older
# platform version. We know about them, and they are safe.
-dontnote android.support.**
-dontwarn android.support.**

# Understand the @Keep support annotation.
-keep class android.support.annotation.Keep

-keep @android.support.annotation.Keep class * {*;}

-keepclasseswithmembers class * {
    @android.support.annotation.Keep <methods>;
}

-keepclasseswithmembers class * {
    @android.support.annotation.Keep <fields>;
}

-keepclasseswithmembers class * {
    @android.support.annotation.Keep <init>(...);
}

-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

-keep @com.json2view.DynamicViewId class * {*;}
-keepclassmembers class * {
    @com.json2view.DynamicViewId <methods>;
}
-keepclasseswithmembers class * {
    @com.json2view.DynamicViewId <fields>;
}

-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.os.*
-keep public class * extends android.widget.*
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

-keepclasseswithmembers public class * {
    public static void main(java.lang.String[]);
}


-keep class * extends java.sql.Driver

-keepclasseswithmembers class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * implements android.os.Parcelable {
    static android.os.Parcelable$Creator CREATOR;
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}


-keepclassmembers class * extends java.lang.Enum {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepnames class * implements java.io.Serializable
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keep class * extends android.database.sqlite.SQLiteOpenHelper {
	public <init>(android.content.Context, java.lang.String);
}

-keep class android.os.*** {
	<methods>;
	<fields>;
}

-keep class LibcoreWrapper.os {*;}
-keep class android.support.v4.app.** {*;}
-keep interface android.support.v4.app.** { *; }
-keep class android.support.v7.widget.** { *; }
-keep class android.support.v7.internal.widget.** { *; }
-keep class android.support.v7.internal.view.menu.** { *; }


-keep class com.android.volley.toolbox.Volley {*;}


-keep class com.google.analytics.tracking.**
-dontwarn com.google.android.gms.**
-dontwarn com.google.analytics.tracking.**

-keepattributes *JavascriptInterface*
-keep class android.webkit.WebSettings { *; }
-dontwarn android.webkit.WebSettings

# Gson
-keep class com.google.gson.stream.** { *; }
-keepattributes EnclosingMethod
-keep class org.xz_sale.entity.**{*;}
-keep class com.google.gson.** {*;}
-keep class com.google.**{*;}
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.examples.android.model.** { *; }

#okhttp
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.squareup.okhttp.* { *; }
-keep interface com.squareup.okhttp.** { *; }
-dontwarn com.squareup.okhttp.**
-keepattributes Signature
-keepattributes *Annotation*
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**
-dontwarn com.squareup.**
-dontwarn okio.**
-keep public class org.codehaus.* { *; }
-keep public class java.nio.* { *; }
