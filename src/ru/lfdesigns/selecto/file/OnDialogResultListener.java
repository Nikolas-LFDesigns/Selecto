package ru.lfdesigns.selecto.file;

public interface OnDialogResultListener {
	/**
	 * Receives file dialog result
	 * @param result in current context, full path to the folder
	 */
	public void OnDialogResult(String result);
}
