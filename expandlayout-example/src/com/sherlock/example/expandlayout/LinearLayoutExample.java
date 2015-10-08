package com.sherlock.example.expandlayout;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.sherlock.expandlayout.ExpandableLayout;

public class LinearLayoutExample extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_linearlayout);

		final ExpandableLayout expandableLayout = (ExpandableLayout) this
				.findViewById(R.id.expandlayout);

		this.findViewById(R.id.imageview).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						expandableLayout.toggleExpansion();
					}
				});
	}
}
