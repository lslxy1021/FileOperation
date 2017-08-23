package bufferedIO;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;

public class BSegment {

	private static String destPath;// 分割文件目标文件路径
	private static File destFile;// 分割文件目标文件夹
	private static File outFile;// 分割文件
	private static File outInfoFile;// 分割信息文件，合并时需要
	private static BufferedInputStream inSource;// 原文件输入流
	private static BufferedOutputStream outDest;// 分割文件输入流，字节流
	private static FileWriter outInfo;// 分割信息文件输入流,字符流

	public static void fileSeperate(File file, int number, String suffix) throws Exception {
		destPath = file.getParentFile().toString() + File.separator
				+ file.getName().substring(0, file.getName().indexOf("."));
		destFile = new File(destPath);
		if (!destFile.exists()) {
			destFile.mkdirs();
		}
		outInfoFile = new File(destFile, file.getName().substring(0, file.getName().indexOf(".")) + suffix + ".txt");
		outInfo = new FileWriter(outInfoFile);
		inSource = new BufferedInputStream(new FileInputStream(file), 64 * 1024);
		long fileLength = file.length();
		long size = fileLength / number + 1;
		long eIndex = 0;
		long bIndex = 0;
		for (int i = 0; i < number; i++) {
			outFile = new File(destFile,
					file.getName().substring(0, file.getName().indexOf(".")) + "part" + i + suffix);
			outDest = new BufferedOutputStream(new FileOutputStream(outFile), 64 * 1024);// 设置缓冲区大小至64KB(默认是8KB)
			eIndex += size;
			eIndex = (eIndex > fileLength) ? fileLength : eIndex;
			while (bIndex < eIndex) {
				outDest.write(inSource.read());
				bIndex++;
			}
			outInfo.write(outFile.getAbsolutePath());
			outInfo.write(System.getProperty("line.separator"));
			outDest.flush();
			outInfo.flush();
		}
		outDest.close();
		inSource.close();
		outInfo.close();
	}

	public static void main(String[] args) {

	}
}
