# Android Bucksapp SDK
This sample app will show how Android Bucksapp SDK can integrate with your own android app.

<img src="https://user-images.githubusercontent.com/74667619/161836464-52b5e585-4588-4756-8e8b-3fcdc4af2b35.gif" width="350px" />

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
  implementation 'com.github.bucksapp-corp:android-bucksapp-sdk:1.0.1'
}
```

Now, you can import BucksappFragment class
```
import com.bucksapp.androidsdk.BucksappFragment
```

Launch the fragment, for example:
```
supportFragmentManager.beginTransaction()
                .replace(
                    R.id.fragment_container, BucksappFragment.newInstance(
                        "API_KEY",
                        "USER_UUID",
                        "development",
                        "es"
                    )
                )
                .commitNow()
```

`API_KEY`, `USER_UUID` are required

`env` by default is `development`. values `['development', 'staging', 'production']`
`language` by default is `es`. Values `['es', 'en']`

