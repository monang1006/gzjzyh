/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-2-26
 * Autour: qindh
 * Version: V1.0
 * Description： 全文检索
 */
package com.strongit.oa.search;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.hslf.HSLFSlideShow;
import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.model.TextRun;
import org.apache.poi.hslf.usermodel.SlideShow;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.pdfbox.pdfparser.PDFParser;
import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.util.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.strongit.oa.bo.ToaArchiveFile;
import com.strongit.oa.bo.ToaArchiveFileAppend;
import com.strongit.oa.bo.ToaArchiveTempfile;
import com.strongit.oa.bo.ToaArchiveTfileAppend;
import com.strongit.oa.bo.ToaInfopublishColumnArticl;
import com.strongit.oa.bo.ToaPrsnfldrFile;
import com.strongit.plugin.search.index.CompassIndexPojo;
import com.strongit.plugin.search.index.IIndesOperator;
import com.strongit.plugin.search.index.IndexFactory;

@Service
public class SearchManager {

	private static final String INFOPUB = "1";// 信息发布搜索

	private static final String PUBLICFILE = "2";// 公共文件夹
	
	private static final String ARCHIVEFILE = "3";// 档案管理
	
	private static final String ARCHIVEFILE2 = "4";// 档案管理(没有附件的)

	IIndesOperator indexFactory = IndexFactory.getInstance().getOperator();//获取全文检索
	
	Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 增加索引
	 * 
	 * @param obj
	 *            增加索引的对象
	 * @return reVal false保存失败 true 保存成功
	 */
	public Boolean saveIndex(Object obj) {
		Boolean reVal = false;
		try {
			CompassIndexPojo pojo = verdictObj(obj);
			indexFactory.createIndex(pojo);
			reVal = true;
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return reVal;
	}

    /**
     * 用于公共文件柜的全文检索,增加索引
     * @param obj 对像
     * @param is 文件输入流
     * @return ?成功
     */
	public Boolean saveIndex(Object obj, FileInputStream is) {
		Boolean reVal = false;
		try {
			CompassIndexPojo pojo = verdictObj(obj, is);
			indexFactory.createIndex(pojo);
			reVal = true;
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return reVal;
	}
	
	  /**
     * 用于档案管理的全文检索,增加索引
     * @param obj 对像
     * @param is 文件输入流
     * @return ?成功
     */
	public Boolean saveIndex(Object obj,String[] fild, FileInputStream is) {
		Boolean reVal = false;
		try {
			CompassIndexPojo pojo = verdictObj(obj,fild,is);
			indexFactory.createIndex(pojo);
			reVal = true;
		} catch (java.lang.NoClassDefFoundError e) {
			logger.error("保存索引时发生异常", e);
		}catch(Exception ex){
			logger.error("保存索引时发生异常", ex);
		}
		return reVal;
	}

	/**
	 * 删除索引
	 * 
	 * @param id
	 *            组键ID
	 * @return reVal false删除失败 true 删除成功
	 */
	public Boolean delIndex(String id) {
		Boolean reVal = false;
		try {
			indexFactory.deleteIndex(id);
			reVal = true;
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return reVal;
	}

	/**
	 * 删除索引数组
	 * 
	 * @param id
	 *            主键数组
	 * @return reVal false删除失败 true 删除成功
	 */
	public Boolean delIndex(String[] id) {
		Boolean reVal = false;
		try {
			for (int i = 0; i < id.length; i++) {
				indexFactory.deleteIndex(id[i]);
			}
			reVal = true;
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return reVal;
	}

	/**
	 * 更新索引
	 * 
	 * @param obj
	 * @return reVal false删除失败 true 删除成功
	 */
	public Boolean updetIndex(Object obj) {
		Boolean reVal = false;
		try {
			CompassIndexPojo pojo = verdictObj(obj);
			indexFactory.updateIndex(pojo);
			reVal = true;
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return reVal;
	}
	
	public Boolean updetIndex(Object obj,String[] fildes) {
		Boolean reVal = false;
		try {
			CompassIndexPojo pojo = verdictObj(obj,fildes);
			indexFactory.updateIndex(pojo);
			reVal = true;
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return reVal;
	}
	/**
	 * 此方法用于判断对象类型
	 * 
	 * @param obj
	 * @return CompassIndexPojo 为索引公共pojo
	 */
	public CompassIndexPojo verdictObj(Object obj) {// 此方法用于判断对象
		CompassIndexPojo pojo = new CompassIndexPojo();
		if (obj instanceof ToaInfopublishColumnArticl) {
			ToaInfopublishColumnArticl bo = (ToaInfopublishColumnArticl) obj;
			pojo.setId(bo.getColumnArticleId());
			pojo.setArticlecontent(bo.getToaInfopublishArticle()
					.getArticlesArticlecontent());
			pojo.setAuthor(bo.getToaInfopublishArticle().getArticlesAuthor());
			pojo.setCreatedate(bo.getToaInfopublishArticle()
					.getArticlesCreatedate());
			pojo.setModifydate(bo.getColumnArticleLatestchangtime());
			pojo.setSubtitle(bo.getToaInfopublishArticle()
					.getArticlesSubtitle());
			pojo.setTitle(bo.getToaInfopublishArticle().getArticlesTitle());
			pojo.setField1(bo.getToaInfopublishArticle().getArticlesKeyword());
		//	pojo.setField8("/infopub/articles/articles!showColumnArticl.action?columnArticleId="+bo.getColumnArticleId());
			pojo.setField9("INFOPUB" + bo.getColumnArticleState());// 用于状态,查询不同模块的不同状态
			pojo.setField10(INFOPUB);// 为信息发布,前台好处理URL
		}
		else if (obj instanceof ToaArchiveTempfile) {
			ToaArchiveTempfile bo = (ToaArchiveTempfile) obj;
			pojo.setId(bo.getTempfileId());
			pojo.setArticlecontent(bo.getTempfileAwardsContent());
			pojo.setAuthor(bo.getTempfileAuthor());
			pojo.setCreatedate(bo.getTempfileDate());
			pojo.setModifydate(bo.getTempfileDate());
			pojo.setSubtitle(bo.getTempfileDesc());
			pojo.setTitle(bo.getTempfileTitle());
			pojo.setField1(bo.getTempfileNo());
			 pojo.setField2(bo.getTempfileDepartmentName());
			   pojo.setField3(bo.getTempfileDeadline());
			  // pojo.setField4(fildes[3]);
			//   pojo.setField5(fildes[4]);
			 //  pojo.setField6(fildes[5]);
		//	pojo.setField8("/infopub/articles/articles!showColumnArticl.action?columnArticleId="+bo.getColumnArticleId());
			pojo.setField9("ARCHIVEFILE9");
			pojo.setField10(ARCHIVEFILE2);//  为档案管理附件,前台好处理URL（没有附件的档案）
		}
		
		else if (obj instanceof ToaArchiveFile) {
			ToaArchiveFile bo = (ToaArchiveFile) obj;
			pojo.setId(bo.getFileId());
			pojo.setArticlecontent(bo.getFileAwardsContent());
			pojo.setAuthor(bo.getFileAuthor());
			pojo.setCreatedate(bo.getFileDate());
			pojo.setModifydate(bo.getFileDate());
			pojo.setSubtitle(bo.getFileDesc());
			pojo.setTitle(bo.getFileTitle());
			pojo.setField1(bo.getFileNo());
			 pojo.setField2(bo.getFileDepartmentName());
			   pojo.setField3(bo.getFileDeadline());
			  // pojo.setField4(fildes[3]);
			//   pojo.setField5(fildes[4]);
			 //  pojo.setField6(fildes[5]);
		//	pojo.setField8("/infopub/articles/articles!showColumnArticl.action?columnArticleId="+bo.getColumnArticleId());
			pojo.setField9("ARCHIVEFILE9");
			pojo.setField10(ARCHIVEFILE2);//  为档案管理附件,前台好处理URL（没有附件的档案）
		}
		return pojo;
	}

	/**
	 * 此方法用于判断对象类型
	 * 
	 * @param obj
	 * @return CompassIndexPojo 为索引公共pojo
	 */
	public CompassIndexPojo verdictObj(Object obj, FileInputStream is) {// 此方法用于判断对象
		CompassIndexPojo pojo = new CompassIndexPojo();
		if (obj instanceof ToaPrsnfldrFile) {
			ToaPrsnfldrFile bo = (ToaPrsnfldrFile) obj;
			String content = getFileContent(bo.getFileContent(), bo
					.getFileExt(), is);
			pojo.setId(bo.getFileId());
			pojo.setTitle(bo.getFileName());
			pojo.setArticlecontent(content);
			pojo.setField9("INFOPUB9");
			pojo.setField10(PUBLICFILE);// 为公共文件,前台好处理URL
		}
		return pojo;
	}
	
	/**
	 * 此方法用于判断对象类型
	 * 
	 * @param obj
	 * @return CompassIndexPojo 为索引公共pojo
	 */
	public CompassIndexPojo verdictObj(Object obj,String[] fildes,FileInputStream is) {// 此方法用于判断对象
		CompassIndexPojo pojo = new CompassIndexPojo();
		if (obj instanceof ToaArchiveTfileAppend) {
			ToaArchiveTfileAppend bo = (ToaArchiveTfileAppend) obj;
			String filename=bo.getTempappendName();
			String ext= filename.substring(filename.lastIndexOf(".")+1);
			String content = getFileContent(bo.getTempappendContent(),ext, is);
			pojo.setId(bo.getTempappendId());
			pojo.setTitle(fildes[0]);
			pojo.setArticlecontent(content);
		
		   pojo.setField1(fildes[0]);
//		   pojo.setField2(fildes[1]);
//		   pojo.setField3(fildes[2]);
//		   pojo.setField4(fildes[3]);
//		   pojo.setField5(fildes[4]);
//		   pojo.setField6(fildes[5]);
//		   pojo.setField7(fildes[6]);
		  
			pojo.setField9("ARCHIVEFILE9");
			pojo.setField10(ARCHIVEFILE);// 为档案管理附件,前台好处理URL
		}
		else if (obj instanceof ToaArchiveFileAppend) {
			ToaArchiveFileAppend bo = (ToaArchiveFileAppend) obj;
			String filename=bo.getAppendName();
			String ext= filename.substring(filename.lastIndexOf(".")+1);
			String content = getFileContent(bo.getAppendContent(),ext, is);
			pojo.setId(bo.getAppendId());
			pojo.setTitle(fildes[0]);
			pojo.setArticlecontent(content);
		
		   pojo.setField1(fildes[0]);
//		   pojo.setField2(fildes[1]);
//		   pojo.setField3(fildes[2]);
//		   pojo.setField4(fildes[3]);
//		   pojo.setField5(fildes[4]);
//		   pojo.setField6(fildes[5]);
//		   pojo.setField7(fildes[6]);
		  
			pojo.setField9("ARCHIVEFILE9");
			pojo.setField10(ARCHIVEFILE);// 为档案管理附件,前台好处理URL
		}
		return pojo;
	}
	
	public CompassIndexPojo verdictObj(Object obj,String[] fildes) {// 此方法用于判断对象
		CompassIndexPojo pojo = new CompassIndexPojo();
		if (obj instanceof ToaArchiveTfileAppend) {
			ToaArchiveTfileAppend bo = (ToaArchiveTfileAppend) obj;
		    //String filename=bo.getTempappendName();
			//String ext= filename.substring(filename.lastIndexOf(".")+1);
			//String content = getFileContent(bo.getTempappendContent(),ext, is);
			pojo.setId(bo.getTempappendId());
			pojo.setTitle(bo.getTempappendName());
			//pojo.setArticlecontent(content);
		
		   pojo.setField1(fildes[0]);
		   pojo.setField2(fildes[1]);
		   pojo.setField3(fildes[2]);
		   pojo.setField4(fildes[3]);
		   pojo.setField5(fildes[4]);
		   pojo.setField6(fildes[5]);
		   pojo.setField7(fildes[6]);
		  
			pojo.setField9("ARCHIVEFILE9");
			pojo.setField10(ARCHIVEFILE);// 为档案管理附件,前台好处理URL
		}
		
		else if (obj instanceof ToaArchiveFileAppend) {
			ToaArchiveFileAppend bo = (ToaArchiveFileAppend) obj;
		//	String filename=bo.getAppendName();
		//	String ext= filename.substring(filename.lastIndexOf(".")+1);
		//	String content = getFileContent(bo.getAppendContent(),ext, is);
			pojo.setId(bo.getAppendId());
			pojo.setTitle(bo.getAppendName());
		//	pojo.setArticlecontent(content);
		
		   pojo.setField1(fildes[0]);
		   pojo.setField2(fildes[1]);
		   pojo.setField3(fildes[2]);
		   pojo.setField4(fildes[3]);
		   pojo.setField5(fildes[4]);
		   pojo.setField6(fildes[5]);
		   pojo.setField7(fildes[6]);
		  
			pojo.setField9("ARCHIVEFILE9");
			pojo.setField10(ARCHIVEFILE);// 为档案管理附件,前台好处理URL
		}
		return pojo;
	}
	/**
	 * 用于公共文件柜的全文检索获取文件中(如:word,pdf等)的内容
	 * @param content 内容
	 * @param ext 后缀名(如.doc)
	 * @param is 文件输入流
	 * @return
	 * @throws IOException 
	 */
	public String getFileContent(byte[] content, String ext, FileInputStream is)  {
		String fileText = "";
		ext = ext.toLowerCase();
		if ("doc".equals(ext)) {//如果是word文件
		 
			
			try {
				ClassLoader classloader = org.apache.poi.poifs.filesystem.POIFSFileSystem.class.getClassLoader();
			//	URL res = classloader.getResource("org/apache/poi/poifs/filesystem/POIFSFileSystem.class");

				WordExtractor ex = new WordExtractor(is);// 
				fileText = ex.getText();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		 
	
	}
		if ("xls".equals(ext)) {//如果为excel文件
			StringBuffer contentText = new StringBuffer();
			try {
				HSSFWorkbook workbook = new HSSFWorkbook(is);// 创建对Excel工作簿文件的引用
				for (int numSheets = 0; numSheets < workbook
						.getNumberOfSheets(); numSheets++) {
					if (null != workbook.getSheetAt(numSheets)) {
						HSSFSheet aSheet = workbook.getSheetAt(numSheets);// 获得一个sheet
						for (int rowNumOfSheet = 0; rowNumOfSheet <= aSheet
								.getLastRowNum(); rowNumOfSheet++) {
							if (null != aSheet.getRow(rowNumOfSheet)) {
								HSSFRow aRow = aSheet.getRow(rowNumOfSheet); // 获得一个行
								for (short cellNumOfRow = 0; cellNumOfRow <= aRow
										.getLastCellNum(); cellNumOfRow++) {
									if (null != aRow.getCell(cellNumOfRow)) {
										HSSFCell aCell = aRow
												.getCell(cellNumOfRow);// 获得列值
										contentText.append(aCell
												.getStringCellValue());
									}
								}
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			fileText = contentText.toString();

		}
		if("ppt".equals(ext)){//如果有ppt
			StringBuffer contentText = new StringBuffer("");
			try {
				SlideShow ss = new SlideShow(new HSLFSlideShow(is));// path为文件的全路径名称，建立SlideShow
				Slide[] slides = ss.getSlides();// 获得每一张幻灯片
				for (int i = 0; i < slides.length; i++) {
					TextRun[] t = slides[i].getTextRuns();// 为了取得幻灯片的文字内容，建立TextRun
					for (int j = 0; j < t.length; j++) {
						contentText.append(t[j].getText());// 这里会将文字内容加到content中去
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			fileText = contentText.toString();
		}
		if ("pdf".equals(ext)) {// pdf输入
			PDDocument document = null;
			try {
				PDFParser parser = new PDFParser(is);
				parser.parse();
				document = parser.getPDDocument();
				PDFTextStripper stripper = new PDFTextStripper();
				fileText = stripper.getText(document);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (document != null) {
					try {
						document.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		if ("txt".equals(ext) || "html".equals(ext) || "htm".equals(ext)) {//其它文件内容
			byte[] buff = new byte[1024];
			int n = 0;
			try {
				n = is.read(buff);
			} catch (IOException e) {
				e.printStackTrace();
			} // 从文件读取数据
			System.out.write(buff, 0, n); // 写入System.out中
			fileText = new String(buff, 0, n);
		}

		return fileText;
	}

	
}
