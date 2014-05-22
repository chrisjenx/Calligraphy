Calligraphy
===========

Custom fonts in Android the easy way.

Are you fed up of Custom views to set fonts? Or traversing the ViewTree to find TextViews? Yeah me too.

![alt text](https://github.com/chrisjenx/Calligraphy/raw/master/screenshot.png "ScreenShot Of Font Samples")

##Getting started

### Dependency

[Download from Maven Central (.jar)](http://search.maven.org/remotecontent?filepath=uk/co/chrisjenx/calligraphy/0.7.2/calligraphy-0.7.2.jar)

__OR__

Include the dependency:

```groovy
dependencies {
    compile 'uk.co.chrisjenx:calligraphy:1.0.+'
}
```
### Fonts

Add your custom fonts to `assets/` all font definition is relative to this path.

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

Define your default font using `CalligraphyConfig`, in your `Application` class, unfortunately `Activity#onCreate(Bundle)` 
is called _after_ `Activity#attachBaseContext(Context)` so the config needs to be defined before that.

```java
protected void onCreate() {
    super.onCreate();
    CalligraphyConfig.initDefault("fonts/Roboto-Regular.ttf", R.attr.fontPath);
    //....
}
```
_Note: You don't need to define a Config anymore (1.0.0+) but the library will do nothing. So define at least a default 
font or attribute._

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
    android:textAppearance="@style/TextAppearance_FontPath/>

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

### Why *not* piggyback off of fontFamily attribute?

We originally did, but it conflicted with users wanting to actually use that attribute, you now have 
have to define a custom attribute.


### Why not ship with custom attribute?

No resources means that the library can compile down to a `jar` instead of an `aar`, as I know alot 
of users are still not using Gradle yet.

As of 1.0+ you *have* to define a custom attribute.

#Colaborators

- [@mironov-nsk](https://github.com/mironov-nsk)
- [@Roman Zhilich](https://github.com/RomanZhilich)
- [@Smuldr](https://github.com/Smuldr)
- [@Codebutler](https://github.com/codebutler)
- [@loganj](https://github.com/loganj)

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
