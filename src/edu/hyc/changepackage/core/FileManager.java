package edu.hyc.changepackage.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.hyc.changepackage.core.model.Dir;

public class FileManager {

	private FileUtil fileUtil;

	private Monitor monitor;

	private Dir rootDir;

	private List<Dir> srcDirs;

	private String rootPath;

	public FileManager(File path, List<File> srcPaths, Monitor monitor) {
		setPath(path, srcPaths);
		this.monitor = monitor;
		fileUtil = new FileUtil();
	}

	public void replacePackage(String oldPackage, String newPackage) {
		List<File> files = findFileByString(oldPackage);
		printFile(files);
		changePackage(files, oldPackage, newPackage);
	}

	private void printFile(List<File> files) {
		Iterator<File> filesIt = files.iterator();
		monitor.print("====MODIFY FILE LIST=START===");
		while (filesIt.hasNext()) {
			File file = filesIt.next();
			monitor.print(file.getPath().replace(rootPath, ""));
		}
		monitor.print("====MODIFY FILE LIST=END=====");
	}

	public List<File> findFileByString(String str) {
		List<File> resultFiles = new ArrayList<>();
		fileUtil.findFilesInDirs(resultFiles, rootDir, str);
		rootDir = null;
		System.gc();
		return resultFiles;
	}

	private void changePackage(List<File> files, String oldPackage,
			String newPackage) {
		replaceFilePackage(files, oldPackage, newPackage);
		moveFileInSrcs(oldPackage, newPackage);
	}

	private void replaceFilePackage(List<File> files, String oldPackage,
			String newPackage) {
		Iterator<File> filesIt = files.iterator();
		while (filesIt.hasNext()) {
			File file = filesIt.next();
			fileUtil.replaceFileString(file, oldPackage, newPackage);
		}
	}

	private void moveFileInSrcs(String oldPackage, String newPackage) {
		Iterator<Dir> srcDirsIt = srcDirs.iterator();
		while (srcDirsIt.hasNext()) {
			Dir srcDir = srcDirsIt.next();
			String oldPath = convertPackageToPath(srcDir, oldPackage);
			String newPath = convertPackageToPath(srcDir, newPackage);
			Dir oldPathDir = getOldPathDir(srcDir, oldPath);
			monitor.print("=====MOVE FILE LIST=START====");
			moveFile(oldPathDir, oldPath, newPath);
			monitor.print("=====MOVE FILE LIST=END======");
		}

	}

	private String convertPackageToPath(Dir srcDir, String packageName) {
		return srcDir.getPathString() + "/" + packageName.replace(".", "/");
	}

	private void moveFile(Dir rootDir, String oldPath, String newPath) {
		if (!oldPath.equals(newPath)) {
			String path = rootDir.getPathString().replace(oldPath, newPath);
			fileUtil.createNewPath(path);
			monitor.print("mkdir " + path.replace(rootPath, ""));
			Iterator<String> files = rootDir.getFiles().iterator();
			while (files.hasNext()) {
				File file = new File(files.next());
				String toFilePath = file.getPath().replace(oldPath, newPath);
				File toFile = new File(toFilePath);
				file.renameTo(toFile);
				monitor.print("move " + file.getPath().replace(rootPath, "")
						+ "\n\t-> " + toFile.getPath().replace(rootPath, ""));
			}
		}

		Iterator<Dir> dirs = rootDir.getDirs().iterator();
		while (dirs.hasNext()) {
			Dir dir = dirs.next();
			moveFile(dir, oldPath, newPath);
		}
		rootDir.getPath().delete();
		monitor.print("delete " + rootDir.getPathString().replace(rootPath, ""));

	}

	private Dir getOldPathDir(Dir rootDir, String oldPath) {
		Dir oldPathDir = rootDir;
		Iterator<Dir> dirs = rootDir.getDirs().iterator();
		while (dirs.hasNext()) {
			Dir dir = dirs.next();
			if (oldPath.indexOf(dir.getPathString()) > -1) {
				oldPathDir = getOldPathDir(dir, oldPath);
				break;
			}
		}
		return oldPathDir;
	}

	public void setPath(File path, List<File> srcPaths) {
		if (path != null) {
			rootDir = new Dir(path);
			rootPath = rootDir.getPathString();
			setSrcDirs(srcPaths);
		}
	}

	private void setSrcDirs(List<File> srcPaths) {
		srcDirs = new ArrayList<Dir>();
		Iterator<File> srcPathsIt = srcPaths.iterator();
		while (srcPathsIt.hasNext()) {
			File path = srcPathsIt.next();
			findSrcDir(rootDir, path);
		}
	}

	private void findSrcDir(Dir rootDir, File path) {
		Iterator<Dir> dirsIt = rootDir.getDirs().iterator();
		while (dirsIt.hasNext()) {
			Dir dir = dirsIt.next();
			if (dir.getPathString().equals(path.getPath())) {
				srcDirs.add(dir);
				break;
			}
			findSrcDir(dir, path);
		}
	}

}
