package bufferedIO;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;

public class BMerge {
	private static BufferedOutputStream output;//合并文件输出流
	private static BufferedInputStream fileReader;//分割文件读取流
	private static BufferedReader childReader;//分割信息文件读取流
	private static File outFile;//合并文件
	private static File cFile;//子文件
	public static void fileCombination(File file, String targetFile) throws Exception {
		outFile = new File(targetFile);
		output = new BufferedOutputStream(new FileOutputStream(outFile),128*1024);		
		childReader = new BufferedReader(new FileReader(file));
		String name;
		while ((name = childReader.readLine()) != null) {
			cFile = new File(name);
			fileReader = new BufferedInputStream(new FileInputStream(cFile),128*1024);
			int content;
			while ((content = fileReader.read()) != -1) {
				output.write(content);
			}
			output.flush();
		}
		output.close();
		fileReader.close();
		childReader.close();
	}
}
