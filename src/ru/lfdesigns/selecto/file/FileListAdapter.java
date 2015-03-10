package ru.lfdesigns.selecto.file;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class FileListAdapter<T> extends ArrayAdapter<Item> {
	Item[] mFileList;
	Context mContext;
	
	public FileListAdapter(Context activity, int resource,
			int textViewResourceId, Item[] objects) {
		super(activity, resource, textViewResourceId, objects);
		mContext = activity;
		mFileList = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView textView = (TextView) super.getView(position, convertView, parent);
		textView.setCompoundDrawablesWithIntrinsicBounds(mFileList[position].icon, 0, 0, 0);
		int dp5 = (int) (5 * mContext.getResources().getDisplayMetrics().density + 0.5f);
		textView.setCompoundDrawablePadding(dp5);
		return textView;
	}
}