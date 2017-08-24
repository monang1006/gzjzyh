package com.strongit.gzjzyh.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import com.strongmvc.exception.SystemException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class FileKit {

	public final static String getProjectPath() {
		String ProjectPath = FileKit.class.getClassLoader().getResource("")
				.toString();
		ProjectPath = StringKit.replaceSlash(ProjectPath);
		int i = 0;
		while (ProjectPath.indexOf("//") != -1 && i < 20) {
			ProjectPath = ProjectPath.replaceAll("\\Q//\\E", "/");
			i++;
		}
		ProjectPath = ProjectPath.replaceAll("\\Qfile:\\E", "");
		if (ProjectPath.indexOf(":") != -1)
			ProjectPath = ProjectPath.substring(1);
		if (ProjectPath.indexOf("WEB-INF") != -1)
			ProjectPath = ProjectPath.substring(0,
					ProjectPath.indexOf("WEB-INF") - 1);
		return ProjectPath;
	}

	public final static String getNewFileRelativePath(String fileName) {
		Date now = new Date();
		String folderName = TimeKit.formatDate(now, "yyyyMMdd");
		String folderPath = FileKit.getProjectPath() + "/upload/" + folderName;
		File folder = new File(folderPath);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		String newFileName = new Date().getTime() + fileName;
		String filePath = "/upload/" + folderName + "/" + newFileName;
		return filePath;
	}

	public final static String getBase64EncodedFileContentByRelativePath(
			String relativeFilePath) throws Exception {
		return getProjectPath() + relativeFilePath;
	}

	public final static String getBase64EncodedFileContent(String filePath)
			throws Exception {
		String encodedFileContent = null;
		if (filePath != null && !"".equals(filePath)) {
			byte[] fileContent = FileKit.getFileContent(filePath);
			if (fileContent != null) {
				encodedFileContent = new BASE64Encoder().encode(fileContent);
			}
		}
		return encodedFileContent;
	}

	public final static byte[] getFileContent(String filePath) throws Exception {
		byte[] fileContent = null;
		File file = new File(filePath);
		InputStream fileInputStream = null;
		BufferedInputStream bufferedInputStream = null;
		ByteArrayOutputStream outputStream = null;
		try {
			if (file.exists() && !file.isDirectory()) {
				fileInputStream = new FileInputStream(file);
				bufferedInputStream = new BufferedInputStream(fileInputStream);
				outputStream = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int count = 0;
				while ((count = bufferedInputStream.read(buffer, 0, 1024)) > 0) {
					outputStream.write(buffer, 0, count);
				}
				fileContent = outputStream.toByteArray();
			}
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} finally {
			if (fileInputStream != null) {
				fileInputStream.close();
			}
			if (bufferedInputStream != null) {
				bufferedInputStream.close();
			}
			if (outputStream != null) {
				outputStream.close();
			}
		}

		return fileContent;
	}

	public final static String relativePath2AbsolutePath(String relativePath) {
		return FileKit.getProjectPath() + relativePath;
	}

	public final static String saveFile(String originalFileName, InputStream is) {
		String filePath = null;
		Date now = new Date();
		String folderName = TimeKit.formatDate(now, "yyyyMMdd");
		String folderPath = FileKit.getProjectPath() + "/upload/" + folderName;
		String newFileName = new Date().getTime()
				+ originalFileName.substring(originalFileName.lastIndexOf("."));
		filePath = "/upload/" + folderName + "/" + newFileName;
		try {
			FileKit.saveFile(is, folderPath, newFileName);
			filePath = "/upload/" + folderName + "/" + newFileName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return filePath;
	}

	public final static void saveFileByRelativePathAndEncodedContent(
			String relativeFilePath, String encodedFileContent)
			throws Exception {
		InputStream is = null;
		try {
			byte[] fileContent = (new BASE64Decoder())
					.decodeBuffer(encodedFileContent);
			is = new ByteArrayInputStream(fileContent);

			int index = relativeFilePath.lastIndexOf("\\/");
			String destFileName = relativeFilePath.substring(index + 1);
			String destFilePath = getProjectPath() + relativeFilePath.substring(0, index);
			saveFile(is, destFileName, destFilePath);
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (is != null) {
				is.close();
			}
		}
	}

	public final static void saveFile(InputStream is, String destFilePath,
			String destFileName) throws Exception {
		FileOutputStream os = null;
		try {
			if (is != null) {
				File folder = new File(destFilePath);
				if (!folder.exists()) {
					folder.mkdirs();
				}
				File file = new File(destFilePath + "/" + destFileName);
				if(file.exists()){
					return;
				}
				os = new FileOutputStream(destFilePath + "/" + destFileName);
				int b = 0;
				while ((b = is.read()) != -1) {
					os.write(b);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
