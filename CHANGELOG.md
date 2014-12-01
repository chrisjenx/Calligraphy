#Changelog

#2.0.0 (WIP)
This is a massive rewrite of the interception model. Look at `CalligraphyLayoutInflater` and
`CalligraphyConfig` for more information on options and changes.

Notable changes:
- Removed jar binary, We build an @aar due to the way we tag `View`s.
- Intercept View creation at each stage.
- Defer view creation to native components (Except CustomViews).
- Inject `setPrivateFactory` on the `Activity`.
- We wrap Factory not disturbing underlying factory and layout inflater invocation.
- Better support for `cloneInContext()` which the compat library uses heavily.

#1.2.0 (20/10/2014)
- Fixes issues with `appcompat-v7:21+` (uses underlying `Toolbar` impl).
- Lollipop support.
- Fast path view with font already set by us.

#1.1.0 (02/08/2014)
- Fixes ActionBar Title/SubTitle `textStyles`.
- Fixes `textAllCaps` bug, now works correctly.
- Fixes some `Spannable` issues reported, we are more careful what we apply `Spannable`'s too now.
- Fixes missing Typeface on hint text on `EditText`/`AutoComplete`.
- Fixes empty source and javadoc jars on maven.

#1.0.0 (05/07/2014)
- Added ActionBar Title/SubTitle support.
- Toast support via default style/or TextView theme style.
- Removed FontFamily parsing as it lead to users not being able to use `fontFamily`
- Added TextAppearance Support - Thanks [@codebutler](https://github.com/codebutler) & [@loganj](https://github.com/loganj)
- Default Font no longer required.

#0.8/9 (Skipped major API change)

#0.7.1 (22/04/2014)
- Fixed Resources not found Exception - [@Smuldr](https://github.com/Smuldr)

#0.7.0 (28/01/2014)
- Added Anti-aliasing support
- Added custom font attribute support - Thanks [@Roman Zhilich](https://github.com/RomanZhilich)
- Changed Maven groupId to `uk.co.chrisjenx` artifact is now `calligraphy`. `compile 'uk.co.chrisjenx:calligraphy:0.+'`

#0.6.0 (02/01/2014)
- Supports all Android implementations of `TextView`
- Supports Custom `TextView`s - Thanks [@mironov-nsk](https://github.com/mironov-nsk)
- Caches none found fonts as null

#0.5.0
- Added support for `Button` class

#0.4.0
- Initial Release
