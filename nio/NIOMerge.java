package nio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import fileoperation.UnMap;

public class NIOMerge {
	private static FileInputStream fileReader;//分割文件读取流
	private static BufferedReader childReader;//分割信息文件读取流
	private static File outFile;//合并文件
	private static File cFile;//子文件
	private static MappedByteBuffer out;
	private static MappedByteBuffer read;
	private static RandomAccessFile outDest;
	private static String name;
	
	public static void fileCombination(File file, String targetFile) throws Exception {
		outFile = new File(targetFile);	
		outDest = new RandomAccessFile(outFile,"rw");     
		childReader = new BufferedReader(new FileReader(file));
		long temp = 0;
		while ((name = childReader.readLine()) != null) {
			cFile = new File(name);
			long size = cFile.length();
			fileReader = new FileInputStream(cFile);	
			read = fileReader.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, size); 
			out = outDest.getChannel().map(FileChannel.MapMode.READ_WRITE, temp, size); 	
			temp += size; 
			for (long j = 0; j < size; j++) {  
		         byte b = read.get();    
		         out.put(b);               
		    }  	
		    fileReader.close();
			UnMap.unmap(read);
			UnMap.unmap(out);
		}			
		outDest.close();      
		childReader.close();
	}	
}
