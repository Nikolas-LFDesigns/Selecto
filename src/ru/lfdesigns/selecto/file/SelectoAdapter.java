package ru.lfdesigns.selecto.file;

import ru.lfdesigns.selecto.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class SelectoAdapter extends BaseAdapter {
	
	private LayoutInflater mInflater;
	private Item[] mItems;
	Context mContext;
	
	private int mLastSelectedPosition = -1;
	private boolean mSelectoActivated = false;
	
	public SelectoAdapter(Context context){
		mContext = context;
		mInflater = LayoutInflater.from(context);
	}
	
	public void prepareForSelecto() {
		mSelectoActivated = true;
	}
	
	@Override
	public int getCount(){
		if (mItems!=null)
			return mItems.length;
		else
			return 0;
	}
	
	public Item[] getItems(){		
		return mItems;
	}

	@Override
	public Object getItem(int position){		
		return mItems[position];
	}

	@Override
	public long getItemId(int position){
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent){
		MultiViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.file_with_selection, parent, false);
			holder = new MultiViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (MultiViewHolder) convertView.getTag();
		}
		convertView.setClickable(true);
		convertView.setLongClickable(true);
		Item item = (Item) getItem(position);
		holder.setPosition(position);
		holder.setTitle(item.file);
		holder.setIcon(item.icon);
		holder.highlightItem(item.isSelected());
		return convertView;
	}

	public void setListItems(Item[] fileList){
		mItems = fileList;
	}
	
	public class MultiViewHolder implements OnClickListener {
		private TextView  mTitle;
		private CheckBox  mSelect;
		private int mPosition;

		public MultiViewHolder(View view){
			mTitle = (TextView) view.findViewById(R.id.text);
			mSelect = (CheckBox) view.findViewById(R.id.select);
			mSelect.setOnClickListener(this);
		}
		
		public void setPosition(int position){
			mPosition = position;
		}

		public void setIcon(int iconResId) {
			mTitle.setCompoundDrawablesWithIntrinsicBounds(iconResId, 0, 0, 0);
			int dp5 = (int) (5 * mContext.getResources().getDisplayMetrics().density + 0.5f);
			mTitle.setCompoundDrawablePadding(dp5);			
		}

		public void setTitle(String title){
			mTitle.setText(title);
		}
		
		public void highlightItem(boolean highlight){
			mSelect.setChecked(highlight);
		}

		@Override
		public void onClick(View v) {
			if (mSelectoActivated) {
				int startValue = mLastSelectedPosition > 0 ? mLastSelectedPosition : 0; 
				boolean directionNormal = mLastSelectedPosition <= mPosition;
				int increment = directionNormal ? 1 : -1;
				for (int i=startValue; 
						(directionNormal ? i<=mPosition : i>=mPosition);
						i+=increment) {
					mItems[i].setSelected(true);
				}
				notifyDataSetChanged();
				mSelectoActivated = false;
			}
			mItems[mPosition].setSelected(true);
			mLastSelectedPosition = mPosition;
		}
	}
}
