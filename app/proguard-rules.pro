# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Android\sdk/tools/proguard/proguard-android.txt
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


 #指定代码的压缩级别
-optimizationpasses 5
 #包名不混合大小写
-dontusemixedcaseclassnames
 #不去忽略非公共的库类
-dontskipnonpubliclibraryclasses
-dontpreverify
 #混淆时是否记录日志
-verbose

#proguard在做混淆的时候，会对一些代码进行优化，若遇到一些相对复杂的方法时，可能会抛出errors。对付的办法是增加配置参数-dontoptimize
#-dontoptimize
#-dontobfuscate
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*


-dontwarn android.support.**
-dontwarn org.dom4j.**
-dontwarn org.jaxen.**
-dontwarn org.w3c.dom.**
-dontwarn org.slf4j.**
-dontwarn org.http.mutipart.**
-dontwarn org.apache.**
-dontwarn org.apache.log4j.**
-dontwarn org.apache.commons.logging.**
-dontwarn org.apache.commons.codec.binary.**
  # 保持哪些类不被混淆
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-keep public class * extends android.app.Fragment
-keep public class * extends android.support.v4.app.Fragment

-dontwarn org.parceler.**
-keep public class org.parceler.** {*;}
-dontwarn javax.**
-keep public class javax.** {*;}
-dontwarn android.webkit.**
-keep public class android.webkit.** {*;}
-dontwarn com.daimajia.**



-keepattributes *Annotation*
-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService

-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}

#-keepclassmembers class **.R$* {
  #    public static <fields>;
  #}
-keep class com.google.**{*;}
##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.hudong.framework.bean.* { *; }

##---------------End: proguard configuration for Gson  ----------
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keep public class * implements java.io.Serializable {*;}

-keep class **.R$* { *; }  #保持R文件不被混淆，否则，你的反射是获取不到资源id的

-keep class android.support.** { *; }
-keep interface android.support.** { *; }
#-keep class com.makeramen.roundedimageview.** { *; }

-keepclasseswithmembers class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Parcel library
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-dontwarn com.makeramen.roundedimageview.**

# Parceler configuration
-keep interface org.parceler.Parcel
-keep @org.parceler.Parcel class * { *; }
-keep class **$$Parcelable { *; }
-keep class org.parceler.Parceler$$Parcels

-keep class cn.xiandu.app.bean.**{*;}


-keepclassmembers class ** {
    public void onEvent*(**);
}

#umeng
-keep class com.umeng.**  {*;}
-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}
-dontwarn okio.**
-keep class okio.**{*;}
-dontwarn okhttp3.**
-keep class okhttp3.**{*;}
-dontwarn com.zhy.http.**
-keep class com.zhy.http.**{*;}

-keep class com.alibaba.sdk.android.feedback.impl.FeedbackServiceImpl {*;}
-keep class com.alibaba.sdk.android.feedback.impl.FeedbackAPI {*;}
-keep class com.alibaba.sdk.android.feedback.util.IWxCallback {*;}
-keep class com.alibaba.sdk.android.feedback.util.IUnreadCountCallback{*;}
-keep class com.alibaba.sdk.android.feedback.FeedbackService{*;}
-keep public class com.alibaba.mtl.log.model.LogField {public *;}
-keep class com.taobao.securityjni.**{*;}
-keep class com.taobao.wireless.security.**{*;}
-keep class com.ut.secbody.**{*;}
-keep class com.taobao.dp.**{*;}
-keep class com.alibaba.wireless.security.**{*;}
-keep class com.ta.utdid2.device.**{*;}

#greendao 3
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
public static java.lang.String TABLENAME;
}
-keep class **$Properties
# If you do not use SQLCipher:
-dontwarn org.greenrobot.greendao.database.**
# If you do not use RxJava:
-dontwarn rx.**

#eventbus
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

#Bugly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}

-dontwarn com.roughike.bottombar.**
-keep public class com.roughike.bottombar.**{*;}
# 保留源文件名及行号
-keepattributes SourceFile,LineNumberTable

-dontwarn com.bumptech.glide.integration.**
-keep class com.bumptech.glide.integration.**{*;}