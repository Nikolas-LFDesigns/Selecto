package ru.lfdesigns.selecto.file;

public class Item {
	public String file;
	public int icon;
	private boolean mIsSelected;

	public Item(String file, Integer icon) {
		this.file = file;
		this.icon = icon;
	}

	public boolean isSelected() {
		return mIsSelected;
	}

	public void setSelected(boolean isSelected) {
		this.mIsSelected = isSelected;
	}
	
	@Override
	public String toString() {
		return file;
	}
}
