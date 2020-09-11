# This is a configuration file for ProGuard.
# http://proguard.sourceforge.net/index.html#manual/usage.html

-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose
-obfuscationdictionary dict.txt #外部字典
-classobfuscationdictionary dict.txt #类字典
-packageobfuscationdictionary dict.txt


#包字典
# 将.class信息中的类名重新定义为"Proguard"字符串
#-renamesourcefileattribute Lit
# 并保留源文件名为"Proguard"字符串，而非原始的类名 并保留行号 
-keepattributes SourceFile,LineNumberTable

# Optimization is turned off by default. Dex does not like code run
# through the ProGuard optimize and preverify steps (and performs some
# of these optimizations on its own).
-dontoptimize
-dontpreverify
# Note that if you want to enable optimization, you cannot just
# include optimization flags in your own project configuration file;
# instead you will need to point to the
# "proguard-android-optimize.txt" file instead of this one from your
# project.properties file.

-keepattributes *Annotation*
-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService

# For native methods, see http://proguard.sourceforge.net/manual/examples.html#native
-keepclasseswithmembernames class * {
    native <methods>;
}

# keep setters in Views so that animations can still work.
# see http://proguard.sourceforge.net/manual/examples.html#beans
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}

# We want to keep methods in Activity that could be used in the XML attribute onClick
#-keepclassmembers class * extends android.app.Activity {
#  public void *(android.view.View);
#}

# For enumeration classes, see http://proguard.sourceforge.net/manual/examples.html#enumerations
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclassmembers class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator CREATOR;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

# The support library contains references to newer platform versions.
# Don't warn about those in case this app is linking against an older
# platform version.  We know about them, and they are safe.
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
-keep public class com.zmide.lit.receiver.DownLoadCompleteReceiver
-keep public class * extends android.database.sqlite.SQLiteOpenHelper
-keep public class com.zmide.lit.javascript.EasySearch
-keep public class com.zmide.lit.javascript.LitJavaScript
-keepattributes *Annotation*
-keepattributes *JavascriptInterface*
-keepclassmembers class * {
@android.webkit.JrvascriptInterface <methods>;
}
-keepclassmembers public class com.zmide.lit.javascript.EasySearch{
<fields>;
<methods>;
public *;
private *;
}
-keepclassmembers public class com.zmide.lit.javascript.LitJavaScript{
<fields>;
<methods>;
public *;
private *;
}

#Gson
##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-keep class com.zmide.lit.object.json.DataUpdate { *; }
-keep class com.zmide.lit.object.json.DataStd { *; }
-keep class com.zmide.lit.object.json.DataUpdate.UpdateData { *; }
-keep class com.zmide.lit.object.json.BaiduSug { *; }
-keep class com.zmide.lit.object.json.NewsData { *; }
-keep class com.zmide.lit.object.json.MarksData { *; }
-keep class com.zmide.lit.object.json.MarkBean { *; }
-keep class com.zmide.lit.object.json.ParentBean { *; }
-keepnames class com.zmide.lit.object.json.MarksData$* {
    public <fields>;
    public <methods>;
}
-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }

##---------------End: proguard configuration for Gson  --------

#Project Condom
-dontwarn com.oasisfeng.condom.CondomContext$CondomContentResolver
-dontwarn com.oasisfeng.condom.ContentResolverWrapper
-dontwarn com.oasisfeng.condom.PackageManagerWrapper
-dontwarn com.oasisfeng.condom.PseudoContextWrapper
-dontwarn com.oasisfeng.condom.kit.NullDeviceIdKit$CondomTelephonyManager
-keep class com.oasisfeng.condom.**

#GSYPlayer
-keep class tv.danmaku.ijk.** { *; }
-dontwarn tv.danmaku.ijk.**
-keep class com.shuyu.gsyvideoplayer.** { *; }
-dontwarn com.shuyu.gsyvideoplayer.**


#Umeng+
-keep class com.umeng.** {*;}
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep public class com.zmide.lit.R$*{
public static final int *;
}
