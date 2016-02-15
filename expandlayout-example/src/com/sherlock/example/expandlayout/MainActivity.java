package com.sherlock.example.expandlayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.findViewById(R.id.in_linearlayout).setOnClickListener(
				mOnClickListener);
		this.findViewById(R.id.in_listview)
				.setOnClickListener(mOnClickListener);
		this.findViewById(R.id.in_recyclerview).setOnClickListener(
				mOnClickListener);
	}

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.in_linearlayout: {
				startActivity(new Intent(MainActivity.this,
						LinearLayoutExample.class));
				break;
			}
			case R.id.in_listview: {
				startActivity(new Intent(MainActivity.this,
						ListViewExample.class));
				break;
			}
			case R.id.in_recyclerview: {
				startActivity(new Intent(MainActivity.this,
						RecyclerViewExample.class));
				break;
			}
			default:
				break;
			}
		}
	};
}
