package ru.lfdesigns.selecto.file;

import ru.lfdesigns.selecto.R;
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
		View root = LayoutInflater.from(getActivity()).inflate(R.layout.file_explorer, container, false);
		if (getArguments()!=null){
			Bundle args = getArguments();
			boolean allowFileSelect = args.getBoolean(ARG_FILE_SELECTION_ALLOWED);
			mFx.setFileSelectionAllowed(allowFileSelect);
		}
		return root;
	}
}
