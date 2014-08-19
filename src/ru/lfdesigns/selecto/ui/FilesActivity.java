package ru.lfdesigns.selecto.ui;

import ru.lfdesigns.selecto.R;
import ru.lfdesigns.selecto.file.FileExplorerFragment;
import android.app.Activity;
import android.os.Bundle;

public class FilesActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_files);
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new FileExplorerFragment()).commit();
		}
	}
}
