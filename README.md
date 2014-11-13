Calligraphy
===========

Custom fonts in Android the easy way.

Are you fed up of Custom views to set fonts? Or traversing the ViewTree to find TextViews? Yeah me too.

![alt text](https://github.com/chrisjenx/Calligraphy/raw/master/screenshot.png "ScreenShot Of Font Samples")

## Known Issue

Lollipop `support-v4:21.0.0` has broken inflation inside `Fragment`s, inside `onCreateView` you need
to inflate using the activity `LayoutInflater`.

```java
@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state) {
    return getActivity().getLayoutInflater().inflate(R.layout.fragment_main, container, false);
}
```

##Getting started

### Dependency

[Download from Maven Central (.jar)](http://search.maven.org/remotecontent?filepath=uk/co/chrisjenx/calligraphy/1.2.0/calligraphy-1.2.0.jar)

__OR__

Include the dependency:

```groovy
dependencies {
    compile 'uk.co.chrisjenx:calligraphy:1.2.0'
}
```
### Fonts

Add your custom fonts to `assets/fonts/` all font definitions are relative to this path. 

### Custom Attribute

We don't package an `R.attr` with Calligraphy to keep it a Jar. So you will need to add your own Attr.

The most common one is: `res/values/attrs.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <attr name="fontPath" format="string"/>
</resources>
```

### Configuration

Define your default font using `CalligraphyConfig`, in your `Application` class, unfortunately 
`Activity#onCreate(Bundle)` is called _after_ `Activity#attachBaseContext(Context)` so the config 
needs to be defined before that.

```java
protected void onCreate() {
    super.onCreate();
    CalligraphyConfig.initDefault("fonts/Roboto-Regular.ttf", R.attr.fontPath);
    //....
}
```
_Note: You don't need to define `CalligraphyConfig` anymore (1.0.0+) but the library will apply
no default font. I recommend defining at least a default font or attribute._

### Inject into Context

Wrap the Activity Context:

```java
@Override
protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(new CalligraphyContextWrapper(newBase));
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


#FAQ

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

### Why not ship with custom attribute?

No resources means that the library can compile down to a `jar` instead of an `aar`, as I know allot
of users are still not using Gradle yet.

As of 1.0+ you *have* to define a custom attribute.

### Multiple Typeface's per TextView / Spannables

It is possible to use multiple Typefaces inside a `TextView`, this isn't new concept to Android.

But this could be achieved using something like the following code.

```java
SpannableStringBuilder builder = new SpannableStringBuilder();
sBuilder.append("Hello!") // Bold this
        .append("I use Calligraphy"); // Default TextView font.
// Create the Typeface you want to apply to certain text
CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(getAssets(), "fonts/Roboto-Bold.ttf");
// Apply typeface to the Spannable 0 - 6 "Hello!" This can of course by dynamic.
sBuilder.setSpan(typefaceSpan, 0, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
setText(sBuilder);
```
Of course this is just an example. Your mileage may vary.

#Colaborators

- [@mironov-nsk](https://github.com/mironov-nsk)
- [@Roman Zhilich](https://github.com/RomanZhilich)
- [@Smuldr](https://github.com/Smuldr)
- [@Codebutler](https://github.com/codebutler)
- [@loganj](https://github.com/loganj)

#Note

This library was created because it is currently not possible to declare a custom font in XML files in Android.

If you feel this should be possible to do, please star [this issue](https://code.google.com/p/android/issues/detail?id=1087) on the official Android bug tracker.

#Licence

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
