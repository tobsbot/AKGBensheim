# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /usr/local/opt/android-sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html
#
# Add any project specific keep options here:
# -keep class android.support.v4.app.** { *; }
# -keep interface android.support.v4.app.** { *; }
# -keep class android.support.v7.** { *; }
# -keep interface android.support.v7.** { *; }

# print config
-printconfiguration build/outputs/mapping/release/config.txt

# remove log calls to android log
-assumenosideeffects class android.util.Log { *; }

# remove log calls to log wrapper
-assumenosideeffects class de.akg_bensheim.akgbensheim.utils.Log { *; }