# ExpandableLayout 

A simple Expandable Layout, You can use it in ListView or RecyclerView like CheckBox.

<img src="http://g.picphotos.baidu.com/album/s%3D900%3Bq%3D90/sign=b2e1ac2307e939015202813e4bd725da/4afbfbedab64034fb6c8d605a9c379310a551daf.jpg" alt="alt text" style="width:185;height:287">

<img src="http://d.picphotos.baidu.com/album/s%3D900%3Bq%3D90/sign=3386d605a9c3793179688a29dbffc678/b3b7d0a20cf431ad8099573a4d36acaf2edd986d.jpg" alt="alt text" style="width:185;height:315">

# Usage

Below is an example of a ExpandableLayout, what you need to do is just to set canExpand="true" to a child view.

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
