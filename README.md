# ExpandableLayout 

This ExpandableLayout is like a smooth expand/collapse animation CheckBox, so it can be used anyplace (ListView or RecyclerView).

![](https://github.com/KyoSherlock/ExpandableLayout/raw/master/screenshots/expandablelayout_screenshot.gif)

# Usage

Below is an example of a ExpandableLayout, what you need to do is just to set canExpand="true" to a child view/viewgroup.

```xml
    <com.kyo.expandablelayout.ExpandableLayout
        android:id="@+id/expandlayout"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:padding="12dp" >

        <ImageView
            android:id="@+id/imageview"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:contentDescription="@null"
            android:scaleType="centerCrop"
            android:src="@drawable/parent" />

        <ImageView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="6dp"
            android:contentDescription="@null"
            android:scaleType="centerCrop"
            android:src="@drawable/child"
            app:canExpand="true" />
    </com.kyo.expandable.ExpandableLayout>
```
# Proguard
Please add the following line in proguard-rules.pro of the main module. It is because I use ObjectAnimator that maps "height" -> setHeight

```xml
-keep class com.kyo.expandablelayout.** {*;}
```

# Changelog

### Version: 1.0
  * Initial Build
  
### Version: 1.2
  * Improve field and method names
  
### Version: 2.0
  * Eclipse to Android Studio
  
# License

    Copyright 2015, KyoSherlock
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
       http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
