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
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;


import com.kyo.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.List;

public class ListViewExample extends Activity {

	private List<Item> mItems;
	private ListView mListView;
	private MyAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_listview);

		mItems = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			mItems.add(new Item());
		}

		mAdapter = new MyAdapter(mItems);
		mListView = (ListView) this.findViewById(R.id.listview);
		mListView.setAdapter(mAdapter);
	}

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			ViewHolder holder = (ViewHolder) v.getTag();
			boolean result = holder.expandableLayout.toggleExpansion();
			Item info = mItems.get(holder.position);
			info.isExpand = result ? !info.isExpand : info.isExpand;
		}
	};

	private ExpandableLayout.OnExpandListener mOnExpandListener = new ExpandableLayout.OnExpandListener() {
		@Deprecated
		@Override
		public void onToggle(ExpandableLayout view, View child, boolean isExpanded) {
		}

		@Override
		public void onExpandOffset(ExpandableLayout view, View child,
								   float offset, boolean isExpanding) {
			ViewHolder holder = (ViewHolder) view.getTag();
			if (holder.position == mItems.size() - 1) {
				mListView.setSelection(holder.position);
			}
		}
	};

	private class MyAdapter extends BaseAdapter {
		private List<Item> items;

		public MyAdapter(List<Item> items) {
			this.items = items;
		}

		@Override
		public int getCount() {
			return items.size();
		}

		@Override
		public Item getItem(int position) {
			return items.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			ViewHolder holder = null;
			if (convertView == null) {
				view = getLayoutInflater().inflate(R.layout.item_listview,
					parent, false);
				holder = new ViewHolder();
				holder.expandableLayout = (ExpandableLayout) view
					.findViewById(R.id.expandablelayout);
				holder.expandableLayout.setOnExpandListener(mOnExpandListener);
				holder.expandableLayout.setTag(holder);
				holder.imageView = (ImageView) view
					.findViewById(R.id.imageview);
				holder.imageView.setOnClickListener(mOnClickListener);
				holder.imageView.setTag(holder);
				view.setTag(holder);
			} else {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}
			Item item = getItem(position);
			holder.position = position;
			holder.expandableLayout.setExpanded(item.isExpand, false);
			return view;
		}
	}

	static class ViewHolder {
		int position;
		ExpandableLayout expandableLayout;
		ImageView imageView;
	}

	static class Item {
		boolean isExpand;
	}
}
