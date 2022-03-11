# Android Bucksapp SDK
This sample app will show how Android Bucksapp SDK can integrate with your own android app.

<img src="https://user-images.githubusercontent.com/74667619/157908611-a4445ea5-c133-4482-bf37-78f8010b1641.gif" width="350px" />

# Getting started

Add it in your root build.gradle at the end of repositories:

```
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```

Add the dependency in your app build.gradle
```
dependencies {
  ...
  implementation 'com.github.bucksapp-corp:android-bucksapp-sdk:1.0'
}
```

Now, you can import Bucksapp util class
```
import com.bucksapp.androidsdk.Bucksapp;
```

Start the activity with init function
```
public static void init(Context context,  String apiKey, String uuid, String language)
```

`context`, `apiKey`, `uuid` are required

`language` by default is `es`

Example:
```
Bucksapp.init(MainActivity.this,
                        "12TvAswlCh03Qhj5uxiM7w",
                        "1c111bf4-7646-4b84-bc4c-4426fb596a87",
                        "es");
```
