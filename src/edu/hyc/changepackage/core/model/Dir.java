package edu.hyc.changepackage.core.model;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

public class Dir {

	private File path;

	private List<Dir> dirs;

	private List<String> files;

	public Dir(File path) {
		this.path = path;
		dirs = new ArrayList<Dir>();
		files = new ArrayList<String>();
		File[] listFiles = getListFile();
		for (final File fileEntry : listFiles) {
			if (fileEntry.isDirectory()) {
				dirs.add(new Dir(fileEntry));
			} else {
				files.add(fileEntry.getPath());
			}
		}

	}

	private File[] getListFile() {
		File[] listFiles = path.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (dir.isDirectory() && name.endsWith("bin")) {
					return false;
				}
				return true;
			}
		});
		return listFiles;
	}

	public void addFile(String file) {
		files.add(file);
	}

	public void addDir(Dir dir) {
		dirs.add(dir);
	}

	public File getPath() {
		return path;
	}

	public String getPathString() {
		return path.getPath();
	}

	public List<Dir> getDirs() {
		return dirs;
	}

	public List<String> getFiles() {
		return files;
	}

}
