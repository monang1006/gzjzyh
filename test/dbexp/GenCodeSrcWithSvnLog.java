package dbexp;

import java.io.*;

/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2013-9-7
 * Autour: luosy
 * Version: V1.0
 * Description： 	根据svn日志文件生成补丁
 */
public class GenCodeSrcWithSvnLog {
	
	public static String buDingjspDir ="D:\\MyDocument\\StrongOA\\代码行统计源文件\\StrongOA\\";
	public static String buDingClassDir ="D:\\MyDocument\\StrongOA\\代码行统计源文件\\src\\";
	public static String svnClassDir ="/G3/OA5.1/1.development_warehouse/a.development/source/OA_V5.1/src/";
	public static String svnjspDir ="/G3/OA5.1/1.development_warehouse/a.development/source/OA_V5.1/StrongOA/";
	public static String localClassDir ="D:/work/Workspaces/OA5.1/src/";
	public static String localjspDir ="D:/work/Workspaces/OA5.1/StrongOA/";

	public static void main(String[] args) {
		String fileName = "C:/log.txt";
		GenCodeSrcWithSvnLog.CopylogFileByLines(fileName);
		
	}
	
 // 复制文件
    public static void copyFile(File sourceFile, File targetFile) throws IOException {
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        try {

        	System.out.println("sourceFile: " + sourceFile);
            // 新建文件输入流并对它进行缓冲
            inBuff = new BufferedInputStream(new FileInputStream(sourceFile));

            // 新建文件输出流并对它进行缓冲
            outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

            // 缓冲数组
            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            // 刷新此缓冲的输出流
            outBuff.flush();
        } finally {
            // 关闭流
            if (inBuff != null)
                inBuff.close();
            if (outBuff != null)
                outBuff.close();
        }
    }

	/**
	 * 以行为单位读取文件，常用于读面向行的格式化文件
	 * 
	 * @param fileName
	 *            文件名
	 */
	public static void CopylogFileByLines(String fileName) {
		File file = new File(fileName);
		File budingfile = null;
		File tempfile = null;
		BufferedReader reader = null;
		try {
			System.out.println("以行为单位读取文件内容，一次读一整行：");
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			String budingdir = null;
			int line = 1;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				// 显示行号
				//System.out.println("line " + line + ": " + tempString);
				budingdir = tempString.replace(svnClassDir, buDingClassDir);
				budingdir = budingdir.replace(svnjspDir, buDingjspDir);
				budingdir = budingdir.replaceAll("/","\\\\");
//				budingdir = budingdir.replaceAll(".java",".class");
				
				tempString = tempString.replace(svnClassDir, localClassDir);
				tempString = tempString.replace(svnjspDir, localjspDir);
				tempString = tempString.replaceAll("/","\\\\");
//				tempString = tempString.replaceAll(".java",".class");
				
				budingfile = new File(budingdir);
				budingfile.getParentFile().mkdirs();
				tempfile =  new File(tempString);
				
//				System.out.println("line " + line + " tempString: " + tempString);
//				System.out.println("line " + line + " budingdir: " + budingdir);
//				System.out.println("line " + line + " getPath: " + budingfile.getParentFile().getPath());
				
				String type = budingfile.getName().substring(budingfile.getName().lastIndexOf(".") + 1);

				if(tempfile.isFile()){
					
					if("class".equals(type)){
						copyClassFile(tempfile.getParentFile().getPath(),budingfile.getParentFile().getPath(),budingfile.getName());
					}else{
						copyFile(tempfile,budingfile);
					}
					
				}
				
				line++;
				file = null;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
	}
	
    /**
     * author:luosy 2013-9-8
     * description:
     * modifyer:
     * description:
     * @param sourceDirPath
     * @param targetDirPath
     * @throws IOException
     */
    public static void copyClassFile(String sourceDirPath, String targetDirPath,String fileName) throws IOException {
        // 创建目标文件夹
//        (new File(targetDirPath)).mkdirs();
        // 获取源文件夹当前下的文件或目录
        File[] file = (new File(sourceDirPath)).listFiles();
    	String fileName1 = fileName.substring(0,fileName.lastIndexOf("."));
        for (int i = 0; i < file.length; i++) {
            if (file[i].isFile()) {
            	String name = file[i].getName().split("\\$")[0];
            	if(name.equals(fileName1) || name.equals(fileName)){
                    copyFile(file[i], new File(targetDirPath +File.separator+ file[i].getName()));
            	}
            }
        }
    }
}
