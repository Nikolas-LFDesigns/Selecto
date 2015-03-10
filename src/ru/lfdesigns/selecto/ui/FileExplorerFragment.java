package ru.lfdesigns.selecto.ui;

import ru.lfdesigns.selecto.R;
import ru.lfdesigns.selecto.file.FileExplorer;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Fragment with file explorer contents
 */
public class FileExplorerFragment extends Fragment {
	
	public static final String ARG_FILE_SELECTION_ALLOWED = "allow_file_select";
	
	FileExplorer mFx; // common file explorer UI
		
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mFx = new FileExplorer(activity);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_files, container, false);
		if (getArguments()!=null){
			Bundle args = getArguments();
			boolean allowFileSelect = args.getBoolean(ARG_FILE_SELECTION_ALLOWED);
			mFx.setFileSelectionAllowed(allowFileSelect);
		}
		mFx.initAdapter(root);
		return root;
	}
}
