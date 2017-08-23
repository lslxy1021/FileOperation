package nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import fileoperation.UnMap;

public class NIOSegment {

	private static String destPath;// 分割文件目标文件路径
	private static File destFile;// 分割文件目标文件夹
	private static File outFile;// 分割文件
	private static File outInfoFile;// 分割信息文件，合并时需要
	private static FileInputStream inSource;// 原文件输入流
	private static FileWriter outInfo;// 分割信息文件输入流,字符流
	private static RandomAccessFile outDest;
	private static MappedByteBuffer out;
	private static MappedByteBuffer read;

	public static void fileSeperate(File file, int number, String suffix) throws Exception {
		destPath = file.getParentFile().toString() + File.separator
				+ file.getName().substring(0, file.getName().indexOf("."));
		destFile = new File(destPath);
		if (!destFile.exists()) {
			destFile.mkdirs();
		}
		outInfoFile = new File(destFile, file.getName().substring(0, file.getName().indexOf(".")) + suffix + ".txt");
		outInfo = new FileWriter(outInfoFile);
		inSource = new FileInputStream(file);
		long fileSize = file.length();
		long size = fileSize / number + 1;
		long temp = 0;
		int index = 0;
		for (int i = 0; i < number; i++) {
			outFile = new File(destFile,
					file.getName().substring(0, file.getName().indexOf(".")) + "part" + (index++) + suffix);
			outDest = new RandomAccessFile(outFile, "rw");
			if ((fileSize - temp) > size) {
				read = inSource.getChannel().map(FileChannel.MapMode.READ_ONLY, temp, size);
				out = outDest.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, size);

			} else {
				read = inSource.getChannel().map(FileChannel.MapMode.READ_ONLY, temp, fileSize - temp);
				out = outDest.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, fileSize - temp);
				for (long j = 0; j < fileSize - temp; j++) {
					byte b = read.get();
					out.put(b);
				}
				outInfo.write(outFile.getAbsolutePath());
				outInfo.write(System.getProperty("line.separator"));
				outInfo.flush();
				outDest.close();
				UnMap.unmap(read);
				UnMap.unmap(out);
				break;
			}
			temp += size;
			for (int j = 0; j < size; j++) {
				byte b = read.get();
				out.put(b);
			}
			outInfo.write(outFile.getAbsolutePath());
			outInfo.write(System.getProperty("line.separator"));
			outInfo.flush();
			outDest.close();
			UnMap.unmap(read);
			UnMap.unmap(out);
		}
		outInfo.close();
		inSource.close();
	}

	public static void main(String[] args) {

	}
}
