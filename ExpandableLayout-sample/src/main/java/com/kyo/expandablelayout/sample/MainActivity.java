/**
 * Copyright 2015, KyoSherlock
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kyo.expandablelayout.sample;

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
