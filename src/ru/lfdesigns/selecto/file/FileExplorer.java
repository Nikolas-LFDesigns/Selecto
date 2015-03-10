package ru.lfdesigns.selecto.file;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

import ru.lfdesigns.selecto.R;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 *  A common file explorer UI
 */
public class FileExplorer implements OnItemClickListener, OnItemLongClickListener {
	
	public static final String SEARCH_ENTRY_POINT = "/";
	public static final int MODE_CLICK = 0;
	public static final int MODE_SELECTION_MULTI = 1;
	
	private int mSelectionMode = MODE_CLICK;
	ArrayList<String> str = new ArrayList<String>(); // Paths list
	private Boolean firstLvl = true; // identifies first level when back button is inactive

	private Item[] fileList; // current directory files
	private File path = new File(SEARCH_ENTRY_POINT); // path to selected file/folder initialized by starting point

	private String chosenFile; // selected file path
	private boolean mFileSelectionAllowed = false; // indicates file selection allowed

	private ListView mListOfFilesView; // visual list
	Context mContext;
	
	OnDialogResultListener mListener; // dialog result callback
		
	public FileExplorer(Context context){
		mContext = context;
	}
	/**
	 * Determines if files selection allowed (by default user may select only folders)
	 * @return allow true to allow false to disallow
	 */
	public boolean isFileSelectionAllowed() {
		return mFileSelectionAllowed;
	}
	/**
	 * Sets if files selection allowed (by default user may select only folders)
	 * @param allow true to allow false to disallow
	 */
	public void setFileSelectionAllowed(boolean allow) {
		this.mFileSelectionAllowed = allow;
	}
	/**
	 * Sets dialog result callback
	 * @param l X implements OnDialogResultListener
	 */
	public void SetOnDialogResultListener(OnDialogResultListener l){
		mListener = l;
	}
	/**
	 * Gets user-selected path
	 * @return
	 */
	public File getPath() {
		return path;
	}
	/**
	 * Initializes visual list component
	 * @param v main view
	 */
	public void initAdapter(View v){
		FileListAdapter<Item> adapter = loadFileList();
		mListOfFilesView = (ListView) v.findViewById(R.id.file_list);
		mListOfFilesView.setAdapter(adapter);
		mListOfFilesView.setOnItemClickListener(this);
		mListOfFilesView.setOnItemLongClickListener(this);
	}
	
	public void prepareForSelecto() {
		BaseAdapter a = (BaseAdapter) mListOfFilesView.getAdapter();
		if (a instanceof SelectoAdapter) {
			SelectoAdapter selAdapter = (SelectoAdapter) a;
			selAdapter.prepareForSelecto();
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> listview, View listitem,
			int position, long row) {
		if (mSelectionMode != MODE_CLICK) // don't allow clicks if choice mode activated
			return;
		
		TextView tv = (TextView) listitem.findViewById(android.R.id.text1);
		chosenFile = (String) tv.getText();
		File sel = new File(path + "/" + chosenFile);
		if (sel.isDirectory()) {
			firstLvl = false;
			str.add(chosenFile);
			fileList = null;
			path = new File(sel + "");

			mListOfFilesView.setAdapter(loadFileList());
			mListOfFilesView.requestLayout();
		} else if (chosenFile.equalsIgnoreCase(mContext.getString(R.string.up_button_name))
				&& !sel.exists()) {
			String s = str.remove(str.size() - 1);
			path = new File(path.toString().substring(0,
					path.toString().lastIndexOf(s)));
			fileList = null;
			if (str.isEmpty()) {
				firstLvl = true;
			}
			mListOfFilesView.setAdapter(loadFileList());
			mListOfFilesView.requestLayout();
		} else if (mFileSelectionAllowed) {
			if (!path.isFile())
				path = new File(sel.getAbsolutePath());
			else
				path = new File(path.getParentFile() + "/" + chosenFile);
			if (mListener!=null)
				mListener.OnDialogResult("");
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> listview, View listitem, int position,
			long id) {
		int selMode = -1;
		BaseAdapter adapter = null;
		switch (mSelectionMode) {
		case MODE_CLICK:
			adapter = createMultiSelectAdapter();
			mListOfFilesView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
			selMode = MODE_SELECTION_MULTI;
			break;
		case MODE_SELECTION_MULTI:
			adapter = createClickAdapter();
			selMode = MODE_CLICK;
			break;
		}
		if (adapter != null) {
			mListOfFilesView.setAdapter(adapter);
			mListOfFilesView.requestLayout();
			mSelectionMode = selMode;
		}
		return true;
	}
	/**
	 * Populates items adapter with files from current folder
	 * @return populated adapter
	 */
	public FileListAdapter<Item> loadFileList() {
		if (path.exists()) {
			FilenameFilter filter = new FilenameFilter() {
				@Override
				public boolean accept(File dir, String filename) {
					File sel = new File(dir, filename);
					return (sel.isFile() || sel.isDirectory())
							&& !sel.isHidden();

				}
			};
			ArrayList<String> fList = new ArrayList<String>();
			int length = 0;
			boolean justUp = false;
			try {
				String[] tempList = path.list(filter);
				for (int i = 0; i < tempList.length; i++)
					fList.add(tempList[i]);
				length = fList.size();
				fileList = new Item[length];
			} catch (NullPointerException permission_denied) {
				fileList = new Item[1];
				justUp = true;
			}
			for (int i = 0; i < length; i++) {
				fileList[i] = new Item(fList.get(i), R.drawable.ic_file);
				File sel = new File(path, fList.get(i));
				if (sel.isDirectory()) {
					fileList[i].icon = R.drawable.ic_folder;
				}
			}

			if (!firstLvl) {
				length = fileList.length;
				if (!justUp)
					length++;
				Item temp[] = new Item[length];
				if (!justUp) {
					for (int i = 0; i < fileList.length; i++) {
						temp[i + 1] = fileList[i];
					}
				}
				temp[0] = new Item(mContext.getString(R.string.up_button_name),
						R.drawable.ic_back_to_parent);
				fileList = temp;
			} else {

			}
		}		
		return (FileListAdapter<Item>) createClickAdapter();
	}
	
	private BaseAdapter createClickAdapter() {
		FileListAdapter<Item> adapter = new FileListAdapter<Item>(
				mContext, android.R.layout.select_dialog_item,
				android.R.id.text1, fileList);
		return adapter;
	}
	
	private BaseAdapter createMultiSelectAdapter() {
		SelectoAdapter adapter = new SelectoAdapter(mContext);
		adapter.setListItems(fileList);
		return adapter;
	}

}
