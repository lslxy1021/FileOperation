package io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;

public class Merge {
	private static FileOutputStream outWriter;//合并文件输出流
	private static FileInputStream fileReader;//分割文件读取流
	private static BufferedReader childReader;//分割信息文件读取流
	private static File outFile;//合并文件
	private static File cFile;//子文件
	public static void fileCombination(File file, String targetFile) throws Exception {
		outFile = new File(targetFile);
		outWriter = new FileOutputStream(outFile);		
		childReader = new BufferedReader(new FileReader(file));
		String name;
		while ((name = childReader.readLine()) != null) {
			cFile = new File(name);
			fileReader = new FileInputStream(cFile);
			int content;
			while ((content = fileReader.read()) != -1) {
				outWriter.write(content);
			}
		}
		outWriter.close();
		fileReader.close();
		childReader.close();
	}
}
