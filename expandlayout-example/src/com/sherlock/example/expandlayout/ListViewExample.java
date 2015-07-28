package com.sherlock.example.expandlayout;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.sherlock.expandlayout.ExpandableLayout;
import com.sherlock.expandlayout.ExpandableLayout.OnExpandListener;

public class ListViewExample extends Activity {

	private List<Info> mInfos;
	private ListView mListView;
	private MyAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_listview);

		mInfos = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			mInfos.add(new Info());
		}

		mAdapter = new MyAdapter(mInfos);
		mListView = (ListView) this.findViewById(R.id.listview);
		mListView.setAdapter(mAdapter);
	}

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			ViewHolder holder = (ViewHolder) v.getTag();
			boolean result = holder.expandableLayout.toggle();
			Info info = mInfos.get(holder.position);
			info.isExpand = result ? !info.isExpand : info.isExpand;
		}
	};

	private OnExpandListener mOnExpandListener = new OnExpandListener() {
		@Deprecated
		@Override
		public void onToggle(ExpandableLayout view, View child, boolean isExpand) {
		}

		@Override
		public void onExpandOffset(ExpandableLayout view, View child,
				float offset, boolean toExpanding) {
			ViewHolder holder = (ViewHolder) view.getTag();
			if (holder.position == mInfos.size() - 1) {
				mListView.setSelection(holder.position);
			}
		}
	};

	private class MyAdapter extends BaseAdapter {
		private List<Info> infos;

		public MyAdapter(List<Info> infos) {
			this.infos = infos;
		}

		@Override
		public int getCount() {
			return infos.size();
		}

		@Override
		public Info getItem(int position) {
			return infos.get(position);
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
						.findViewById(R.id.expandlayout);
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
			Info info = getItem(position);
			holder.position = position;
			holder.expandableLayout.setExpand(info.isExpand, false);
			return view;
		}
	}

	static class ViewHolder {
		int position;
		ExpandableLayout expandableLayout;
		ImageView imageView;
	}

	static class Info {
		boolean isExpand;
	}
}
