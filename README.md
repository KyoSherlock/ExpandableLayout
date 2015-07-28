# ExpandableLayout 

A simple Expandable Layout, You can use it in ListView or RecyclerView like CheckBox.

![](https://github.com/KyoSherlock/ExpandableLayout/raw/master/screenshots/1.png) ![](https://github.com/KyoSherlock/ExpandableLayout/raw/master/screenshots/3.png)

# Usage

Below is an example of a ExpandableLayout, what you need to do is just to set canExpand="true" to a child view.

	```xml
    <com.sherlock.expandlayout.ExpandableLayout
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
    </com.sherlock.expandlayout.ExpandableLayout>
