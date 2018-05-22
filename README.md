# This version of Calligraphy has reached its end-of-life and is no longer maintained. Please migrate to [Calligraphy 3](https://github.com/InflationX/Calligraphy)!

Calligraphy
===========

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Calligraphy-blue.svg?style=flat)](http://android-arsenal.com/details/1/163)

Custom fonts in Android an OK way.

Are you fed up of Custom Views to set fonts? Or traversing the ViewTree to find TextViews? Yeah me too.

![alt text](https://github.com/chrisjenx/Calligraphy/raw/master/screenshot.png "ScreenShot Of Font Samples")

## Getting started

### Dependency

Include the dependency [Download (.aar)](http://search.maven.org/remotecontent?filepath=uk/co/chrisjenx/calligraphy/2.3.0/calligraphy-2.3.0.aar) :

```groovy
dependencies {
    compile 'uk.co.chrisjenx:calligraphy:2.3.0'
}
```
### Add Fonts

Add your custom fonts to `assets/`. All font definitions are relative to this path.

Assuming that you are using Gradle you should create the assets directory under `src/main/` in your project directory if it does not already exist.
As it's popular to use multi-project build with Gradle the path is usually `app/src/main/assets/`, where `app` is the project name.

You might consider creating a `fonts/` subdirectory in the assets directory (as in examples).

### Usage

```xml
<TextView fontPath="fonts/MyFont.ttf"/>
``` 
**Note: The missing namespace, this __IS__ intentional.**

### Installation

Define your default font using `CalligraphyConfig`, in your `Application` class in the `#onCreate()` method.

```java
@Override
public void onCreate() {
    super.onCreate();
    CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                            .setDefaultFontPath("fonts/Roboto-RobotoRegular.ttf")
                            .setFontAttrId(R.attr.fontPath)
                            .build()
            );
    //....
}
```

_Note: You don't need to define `CalligraphyConfig` but the library will apply
no default font and use the default attribute of `R.attr.fontPath`._

### Inject into Context

Wrap the `Activity` Context:

```java
@Override
protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
}
```

_You're good to go!_


## Usage

### Custom font per TextView

```xml
<TextView
    android:text="@string/hello_world"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    fontPath="fonts/Roboto-Bold.ttf"/>
```

_Note: Popular IDE's (Android Studio, IntelliJ) will likely mark this as an error despite being correct. You may want to add `tools:ignore="MissingPrefix"` to either the View itself or its parent ViewGroup to avoid this. You'll need to add the tools namespace to have access to this "ignore" attribute. `xmlns:tools="
http://schemas.android.com/tools"`. See https://code.google.com/p/android/issues/detail?id=65176._

### Custom font in TextAppearance


```xml
<style name="TextAppearance.FontPath" parent="android:TextAppearance">
    <!-- Custom Attr-->
    <item name="fontPath">fonts/RobotoCondensed-Regular.ttf</item>
</style>
```

```xml
<TextView
    android:text="@string/hello_world"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:textAppearance="@style/TextAppearance.FontPath"/>

```

### Custom font in Styles


```xml
<style name="TextViewCustomFont">
    <item name="fontPath">fonts/RobotoCondensed-Regular.ttf</item>
</style>
```

### Custom font defined in Theme

```xml
<style name="AppTheme" parent="android:Theme.Holo.Light.DarkActionBar">
    <item name="android:textViewStyle">@style/AppTheme.Widget.TextView</item>
</style>

<style name="AppTheme.Widget"/>

<style name="AppTheme.Widget.TextView" parent="android:Widget.Holo.Light.TextView">
    <item name="fontPath">fonts/Roboto-ThinItalic.ttf</item>
</style>
```


# FAQ

### Font Resolution 

The `CalligraphyFactory` looks for the font in a pretty specific order, for the _most part_ it's
 very similar to how the Android framework resolves attributes.
 
1. `View` xml - attr defined here will always take priority.
2. `Style` xml - attr defined here is checked next.
3. `TextAppearance` xml - attr is checked next, the only caveat to this is **IF** you have a font 
 defined in the `Style` and a `TextAttribute` defined in the `View` the `Style` attribute is picked first!
4. `Theme` - if defined this is used.
5. `Default` - if defined in the `CalligraphyConfig` this is used of none of the above are found 
**OR** if one of the above returns an invalid font. 

### Why *not* piggyback off of fontFamily attribute?

We originally did, but it conflicted with users wanting to actually use that attribute, you now 
have to define a custom attribute.

### Why no jar?

We needed to ship a custom ID with Calligraphy to improve the Font Injection flow. This
unfortunately means that it has to be an `aar`. But you're using Gradle now anyway right?

### Multiple Typeface's per TextView / Spannables

It is possible to use multiple Typefaces inside a `TextView`, this isn't new concept to Android.

This _could_ be achieved using something like the following code.

```java
SpannableStringBuilder sBuilder = new SpannableStringBuilder();
sBuilder.append("Hello!") // Bold this
        .append("I use Calligraphy"); // Default TextView font.
// Create the Typeface you want to apply to certain text
CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(getAssets(), "fonts/Roboto-Bold.ttf"));
// Apply typeface to the Spannable 0 - 6 "Hello!" This can of course by dynamic.
sBuilder.setSpan(typefaceSpan, 0, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
setText(sBuilder, TextView.BufferType.SPANNABLE);
```
Of course this is just an example. Your mileage may vary.

### Exceptions / Pitfalls

To our knowledge (try: `grep -r -e "void set[^(]*(Typeface " <android source dir>`) there are two standard Android widgets that have multiple methods to set typefaces. They are:

 - android.support.v7.widget.SwitchCompat
 - android.widget.Switch

Both have a method called `setSwitchTypeface` that sets the typeface within the switch (e.g. on/off, yes/no). `SetTypeface` sets the typeface of the label. You will need to create your own subclass that overrides `setTypeface` and calls both `super.setTypeface` and `super.setSwitchTypeface`.





# Collaborators

- [@mironov-nsk](https://github.com/mironov-nsk)
- [@Roman Zhilich](https://github.com/RomanZhilich)
- [@Smuldr](https://github.com/Smuldr)
- [@Codebutler](https://github.com/codebutler)
- [@loganj](https://github.com/loganj)
- [@dlew](https://github.com/dlew)
- [@ansman](https://github.com/ansman)

# Note

This library was created because it is currently not possible to declare a custom font in XML files in Android.

If you feel this should be possible to do, please star [this issue](https://code.google.com/p/android/issues/detail?id=88945) on the official Android bug tracker.

# Licence

    Copyright 2013 Christopher Jenkins
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
        http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

[![Badge](http://www.libtastic.com/static/osbadges/79.png)](http://www.libtastic.com/technology/79/)
