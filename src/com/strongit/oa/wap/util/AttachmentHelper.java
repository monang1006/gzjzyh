package com.strongit.oa.wap.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.strongit.form.vo.FormAttachment;
import com.strongit.oa.prsnfldr.util.Round;
import com.strongit.oa.util.PathUtil;
import com.strongit.oa.util.UUIDGenerator;
import com.strongmvc.exception.SystemException;

public class AttachmentHelper {

	private static Logger logger = LoggerFactory.getLogger(AttachmentHelper.class);
	
	private static final int INVALID_TIME = 7200000;//失效时间为2个小时
	
	public static final String ATTACHMENT_PATH = "mobileoa402883ec33ed10a20133ed10a21c0000";

	private static ConcurrentHashMap<String, Long> mapFormAttachment = new ConcurrentHashMap<String, Long>();

	/**
	 * 保存附件,并返回新的路径名称，正常情况下会以日期+一个5位数的随机数作为新的文件名 若在并发情况下仍然存在同名文件,则以UUID作为文件名
	 * 
	 * @param cxtPath
	 * @param is
	 * @param fileName
	 * @return
	 */
	public static synchronized String saveFile(String cxtPath, InputStream is,
			String fileName) {
		deleteInvalid();
		String path = PathUtil.getRootPath() + ATTACHMENT_PATH + File.separatorChar;// 得到相对工程目录的路径
		String newPath;
		File file = new File(path);
		if (!file.exists()) {
			if(!file.mkdir()) {
				throw new SystemException("创建目录 " + path + " 目录不存在。");
			}
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String now = sdf.format(new Date());// 当前时间
		int index = fileName.lastIndexOf(".");
		String ext = fileName.substring(index, fileName.length());
		Random random = new Random();
		String randomNum = random.nextInt(9) + "" + random.nextInt(9) + ""
				+ random.nextInt(9) + "" + random.nextInt(9) + ""
				+ random.nextInt(9);
		String newFileName = now + randomNum + ext;// 通过当前时间和5位随机数参数新的文件名
		newPath = cxtPath + File.separatorChar + ATTACHMENT_PATH + File.separatorChar + newFileName;
		file = new File(path + newFileName);
		if (file.exists()) {// 如果还是存在了重复的名称,则通过UUID来生成文件名
			UUIDGenerator uuid = new UUIDGenerator();
			String strUUID = uuid.generate().toString();
			newPath = cxtPath + File.separatorChar + ATTACHMENT_PATH + File.separatorChar + strUUID + ext;
			file = new File(path + strUUID + ext);
		}
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			int BUFFER_SIZE = 8192;
			byte[] buffer = new byte[BUFFER_SIZE];
			while (true) {
				int len = is.read(buffer);
				if (len > 0) {
					fos.write(buffer, 0, len);
				} else if (len < 0) {
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
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
		mapFormAttachment.put(newPath, System.currentTimeMillis());
		newPath=newPath.replace("\\", "/");
		return newPath;
	}

	public static void deleteInvalid() {
		try {
			for (Iterator iter = mapFormAttachment.entrySet().iterator(); iter.hasNext();) {
				Map.Entry entry = (Map.Entry) iter.next();
				Long UploadTime = (Long) entry.getValue();
				if ((UploadTime != null)
						&& (UploadTime > System.currentTimeMillis())) {
					UploadTime = System.currentTimeMillis();
				} else if ((UploadTime != null)
						&& (System.currentTimeMillis() - UploadTime >= INVALID_TIME)) {
					File file = new File(entry.getKey().toString());
					if ((file != null)&& (file.exists()) && (file.delete())) {
						logger.info(file.getAbsolutePath()
								+ " 失效的手机OA附件文件已成功删除。");
					}
					iter.remove();
				}
			}
		} catch (Exception e) {
			logger.error("删除手机OA中失效的附件发生错误", e);
		}
	}

	public static String getFileSize(int length_byte) {
		String size = "";
		if (length_byte >= 1024) {
			double length_k = ((double) length_byte) / 1024;
			if (length_k >= 1024) {
				size = Round.round(length_k / 1024, 2) + "MB";
			} else {
				size = length_byte / 1024 + "k";
			}
		} else {
			size = length_byte + "字节";
		}
		return size;
	}
}
