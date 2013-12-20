Calligraphy
===========

Custom fonts in Android the easy way.

Are you fed up of Custom views to set fonts? Or traversing the ViewTree to find TextViews? Yeah me too.

![alt text](https://github.com/chrisjenx/Calligraphy/raw/master/screenshot.png "ScreenShot Of Font Samples")

##Getting started

Include the dependency: (not yet pushed to maven)

```java
dependencies {
    compile 'uk.co.chrisjenx.calligraphy:calligraphy:0.4.+'
}
```

*Use `compile "uk.co.chrisjenx.calligraphy:calligraphy:0.4.0-SNAPSHOT"` until staged.*
(You will also need to add `maven { url "https://oss.sonatype.org/content/repositories/snapshots/" }` to your repositories. See the sample build.gradle.

Add your custom fonts to `assets/` all font definition is relative to this path.

Define your default font using `CalligraphyConfig`.

```java
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    CalligraphyConfig.initDefault("fonts/Roboto-Regular.ttf");
    //....
}
```

###*Important*

Wrap the Activity Context:

```java
@Override
protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(new CalligraphyContextWrapper(newBase));
}
```

Your good to go!


---
#### Custom font per TextView
Of course:

```xml
<TextView
    android:text="@string/hello_world"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:fontFamily="fonts/Roboto-Bold.ttf"/>
```

#### Custom font in styles
No problem:

```xml
<style name="TextViewCustomFont">
    <item name="android:fontFamily">fonts/RobotoCondensed-Regular.ttf</item>
</style>
```

#FAQ

## Why piggyback off of fontFamily attribute?
Means the the library can compile down to a jar instead of an aar, as it is not dependant on any resources.
(This may of course change in the future if we run into issues)

## ButtonView support
Comming soon!

## CustomText/Button View support
Coming soon!
