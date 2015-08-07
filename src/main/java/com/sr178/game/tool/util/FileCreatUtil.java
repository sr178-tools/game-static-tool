package com.sr178.game.tool.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileCreatUtil {
	public static void outFile(String path, String name, String info) throws FileNotFoundException, IOException {
// 		String path2 = "D:";
// 		System.out.println(path.equals(path2));
		String sDirF = System.getProperty("file.separator");
		// 检测指定路径
		if (path.substring(path.length() - 1).equals(sDirF)) {
			path = path.substring(0, path.length() - 1);
		}

		File pathDir = null;
		File file = null;
		FileOutputStream fout = null;
			// 创建目录
			pathDir = new File(path + sDirF );
			if (!(pathDir.isDirectory())) {
				if (!(pathDir.mkdirs())) {
					System.out.println("制定输出目录错误"+pathDir);
				}
			}
			file = new File(path + sDirF +  name);
			System.out.println("开始创建文件："+file);
			if (file.exists()) {
				file.delete();
			}
			if (file.createNewFile()) {
				fout = new FileOutputStream(file);
				fout.write(info.getBytes("utf-8"));
				System.out.println("成功创建文件"+name);
			}
			if (pathDir != null) {
				pathDir = null;
			}
			if (file != null) {
				file = null;
			}
			if (fout != null) {
					fout.close();
			}

	}
}
