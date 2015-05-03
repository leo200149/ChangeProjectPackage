package edu.hyc.changepackage.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.hyc.changepackage.core.model.Dir;

public class FileUtil {

	public final String DEFAULT_CHARSET = "ISO-8859-1";

	public void replaceFileString(File file, String oldStr, String newStr) {
		StringBuilder content = new StringBuilder();
		List<String> fileContent = readFile(file);
		Iterator<String> contentsIt = fileContent.iterator();
		while (contentsIt.hasNext()) {
			content.append(contentsIt.next().replaceAll(oldStr, newStr));
			content.append("\n");
		}
		try {
			BufferedWriter br = new BufferedWriter(new FileWriter(file));
			br.write(content.toString());
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<String> readFile(File file) {
		List<String> content = new ArrayList<>();
		if (file.isFile()) {
			try {
				content = Files.readAllLines(Paths.get(file.getPath()),
						Charset.forName(getFileEncode(file)));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return content;
	}

	@SuppressWarnings("resource")
	public String getFileEncode(File file) {
		String encode = DEFAULT_CHARSET;
		try {
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis);
			encode = isr.getEncoding();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return encode;
	}

	public void findFilesInDirs(List<File> resultFiles, Dir rootDir, String str) {
		resultFiles.addAll(findFiles(rootDir, str));
		Iterator<Dir> dirsIt = rootDir.getDirs().iterator();
		while (dirsIt.hasNext()) {
			Dir dir = dirsIt.next();
			findFilesInDirs(resultFiles, dir, str);
		}
	}

	public List<File> findFiles(Dir dir, String str) {
		List<File> resultFiles = new ArrayList<>();
		Iterator<String> filesIt = dir.getFiles().iterator();
		while (filesIt.hasNext()) {
			File file = new File(filesIt.next());
			List<String> fileContent = readFile(file);
			Iterator<String> contentsIt = fileContent.iterator();
			while (contentsIt.hasNext()) {
				String contentLine = contentsIt.next();
				if (contentLine.indexOf(str) > -1) {
					resultFiles.add(file);
					break;
				}
			}
		}
		return resultFiles;
	}

	public void createNewPath(String newPath) {
		File newDir = new File(newPath);
		if (!newDir.exists()) {
			newDir.mkdirs();
		}
	}

}
