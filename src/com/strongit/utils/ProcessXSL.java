package com.strongit.utils;

/**
 * <p>Title: 江西省财政综合业条系统 </p>
 * <p>Description: XLS(Excel)处理类 </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: 江西省思创数码科技股份有限公司</p>
 * @author xuzm
 * @version 1.0
 */

import java.io.OutputStream;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.batik.css.engine.value.StringValue;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;

import java.util.Date;
import java.util.List;

import java.util.Vector;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.hssf.usermodel.HSSFFont;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;

import com.strongit.utils.BaseDataExportInfo;

public class ProcessXSL {
  HSSFWorkbook wb = new HSSFWorkbook();
  String workbookFileName = "test.xls";
  protected final Log log = LogFactory.getLog(getClass());
  public ProcessXSL() {
  }

  /**
   * 创建　Execl 文件
   * @param wb HSSFWorkbook
   * @param exportInfo BaseDataExportInfo
   */
  public void createWorkBookSheet(BaseDataExportInfo exportInfo) {

    Vector cellList = null;
    if(exportInfo.getWorkbookFileName()!= null )
      workbookFileName = exportInfo.getWorkbookFileName()+".xls";
    try {

      HSSFSheet sheet = wb.createSheet(exportInfo.getSheetIndex() + "");
      wb.setSheetName(exportInfo.getSheetIndex(), exportInfo.getSheetName(),
                      (short) 1);

      /** 设置列宽 */

      for (int i = 0; i < exportInfo.getTableHead().size(); i++) {
        if (i == 1 || i == exportInfo.getTableHead().size() - 1) {
          sheet.setColumnWidth( (short) i, (short) 5800);
        }
        else {
          sheet.setColumnWidth( (short) i, (short) 5800);
        }
        sheet.setColumnWidth( (short) 0, (short) 5000);
      }

      /**  合并单元格 */
      sheet.addMergedRegion(new Region(0, (short) 0, 0,
                                       (short) (exportInfo.getTableHead().size() -
                                                1)));

      /** 表 标题 */
      HSSFRow row = sheet.createRow( (short) 0);
      row.setHeight( (short) 500); //设置行高

      HSSFFont titleFont = wb.createFont();
      titleFont.setFontName("宋体");
      titleFont.setFontHeightInPoints( (short) 16);
      titleFont.setBoldweight( (short) 20);
      HSSFCellStyle titleStyle = wb.createCellStyle();
      titleStyle.setFont(titleFont);

      titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION); //居中

      row = this.createCell(row, (short) 0, titleStyle,
                            exportInfo.getSheetTitle());

      /** 表头 */
      HSSFFont headFont = wb.createFont();
      headFont.setFontName("宋体");
      headFont.setFontHeightInPoints( (short) 12);
      headFont.setBoldweight( (short) 20);

      HSSFCellStyle headStyle = wb.createCellStyle();
      headStyle.setFont(headFont);
      headStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框

      headStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN); //左边框

      headStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); //右边框

      headStyle.setBorderTop(HSSFCellStyle.BORDER_THIN); //上边框

      headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); //居中

      HSSFRow row2 = sheet.createRow( (short) 1);
      row2.setHeight( (short) 400); //设置行高

      for (int i = 0; i < exportInfo.getTableHead().size(); i++) {
        row2 = this.createCell(row2, (short) i, headStyle,
                               exportInfo.getTableHead().get(i));
      }
      /** 表体*/
      HSSFFont font = wb.createFont(); /** 设置字体样式 */
      font.setFontName("宋体");
      HSSFCellStyle cellStyle = wb.createCellStyle();
      cellStyle.setFont(font);
      cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框

      cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN); //左边框

      cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); //右边框

      cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN); //上边框

      cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT); //居左

      for (int i = 0; i < exportInfo.getRowList().size(); i++) {
        cellList = (Vector) exportInfo.getRowList().get(i);
        HSSFRow row3 = sheet.createRow( (short) i + 2);
        row3.setHeight( (short) 300); //设置行高
        if(cellList!=null&&!"".equals(cellList)){
        	 for (int j = 0; j < cellList.size(); j++) {
                 row3 = this.createCell(row3, (short) j, cellStyle, cellList.get(j));
             }
        }
      }

    }
    catch (Exception ex) {
      log.info("error while create work book sheet ", ex);
    }

  }

  
  
  /**
   * 创建　Execl 文件
   * @param wb HSSFWorkbook
   * @param exportInfo BaseDataExportInfo
   */
  public void createWorkBookSheet2(BaseDataExportInfo exportInfo) {

    Vector cellList = null;
    if(exportInfo.getWorkbookFileName()!= null )
      workbookFileName = exportInfo.getWorkbookFileName()+".xls";
    try {

      HSSFSheet sheet = wb.createSheet(exportInfo.getSheetIndex() + "");
      wb.setSheetName(exportInfo.getSheetIndex(), exportInfo.getSheetName(),
                      (short) 1);

      /** 设置列宽 */

      for (int i = 0; i < exportInfo.getTableHead().size(); i++) {
        if (i == 1 || i == exportInfo.getTableHead().size() - 1) {
          sheet.setColumnWidth( (short) i, (short) 2800);
        }
        else {
          sheet.setColumnWidth( (short) i, (short) 1800);
        }
        
      }
      sheet.setColumnWidth( (short) 0, (short) 5000);
      sheet.setColumnWidth( (short) (exportInfo.getTableHead().size()-1), (short) 1500);

      /**  合并单元格 */
      sheet.addMergedRegion(new Region(0, (short) 0, 0,
                                       (short) (exportInfo.getTableHead().size() -
                                                1)));

      /** 表 标题 */
      HSSFRow row = sheet.createRow( (short) 0);
      row.setHeight( (short) 500); //设置行高

      HSSFFont titleFont = wb.createFont();
      titleFont.setFontName("宋体");
      titleFont.setFontHeightInPoints( (short) 20);
      titleFont.setBoldweight( (short) 20);
      HSSFCellStyle titleStyle = wb.createCellStyle();
      titleStyle.setFont(titleFont);

      titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION); //居中

      row = this.createCell(row, (short) 0, titleStyle,
                            exportInfo.getSheetTitle());

      /** 表头 */
      HSSFFont headFont = wb.createFont();
      headFont.setFontName("宋体");
      headFont.setFontHeightInPoints( (short) 12);
      headFont.setBoldweight( (short) 20);

      HSSFCellStyle headStyle = wb.createCellStyle();
      headStyle.setFont(headFont);
      headStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框


      headStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN); //左边框


      headStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); //右边框


      headStyle.setBorderTop(HSSFCellStyle.BORDER_THIN); //上边框


      headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); //居中

      HSSFRow row2 = sheet.createRow( (short) 1);
      row2.setHeight( (short) 400); //设置行高

      for (int i = 0; i < exportInfo.getTableHead().size(); i++) {
        row2 = this.createCell(row2, (short) i, headStyle,
                               exportInfo.getTableHead().get(i));
      }
      /** 表体*/
      HSSFFont font = wb.createFont(); /** 设置字体样式 */
      font.setFontName("宋体");
      HSSFCellStyle cellStyle = wb.createCellStyle();
      cellStyle.setFont(font);
      cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框


      cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN); //左边框


      cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); //右边框


      cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN); //上边框


      cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT); //居左

      for (int i = 0; i < exportInfo.getRowList().size(); i++) {
        cellList = (Vector) exportInfo.getRowList().get(i);
        HSSFRow row3 = sheet.createRow( (short) i + 2);
        row3.setHeight( (short) 300); //设置行高
        if(cellList!=null&&!"".equals(cellList)){
        	 for (int j = 0; j < cellList.size(); j++) {
                 row3 = this.createCell(row3, (short) j, cellStyle, cellList.get(j));
             }
        }
      }

    }
    catch (Exception ex) {
      log.info("error while create work book sheet ", ex);
    }

  }
  
  /**
   * 创建　Execl 文件(含合并表体单元格)
   * @param wb HSSFWorkbook
   * @param exportInfo BaseDataExportInfo
   */
  public void createWorkBookSheet1(BaseDataExportInfo exportInfo) {

    Vector cellList = null;
    if(exportInfo.getWorkbookFileName()!= null )
      workbookFileName = exportInfo.getWorkbookFileName()+".xls";
    try {

      HSSFSheet sheet = wb.createSheet(exportInfo.getSheetIndex() + "");
      wb.setSheetName(exportInfo.getSheetIndex(), exportInfo.getSheetName(),
                      (short) 1);

      /** 设置列宽 */

      for (int i = 0; i < exportInfo.getTableHead().size(); i++) {
        if (i == 1 || i == exportInfo.getTableHead().size() - 1) {
          sheet.setColumnWidth( (short) i, (short) 1600);
        }
        else {
          sheet.setColumnWidth( (short) i, (short) 1600);
        }
        
      }
      sheet.setColumnWidth( (short) 0, (short) 3000);
      sheet.setColumnWidth( (short) 1, (short) 3000);
      sheet.setColumnWidth( (short) (exportInfo.getTableHead().size()-1), (short) 1500);

      /**  合并单元格 */
      sheet.addMergedRegion(new Region(0, (short) 0, 0,
                                       (short) (exportInfo.getTableHead().size() -
                                                1)));
      
      

      /** 表 标题 */
      HSSFRow row = sheet.createRow( (short) 0);
      row.setHeight( (short) 500); //设置行高

      HSSFFont titleFont = wb.createFont();
      titleFont.setFontName("宋体");
      titleFont.setFontHeightInPoints( (short) 16);
      titleFont.setBoldweight( (short) 20);
      HSSFCellStyle titleStyle = wb.createCellStyle();
      titleStyle.setFont(titleFont);

      titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION); //居中

      row = this.createCell(row, (short) 0, titleStyle,
                            exportInfo.getSheetTitle());

      /** 表头 */
      HSSFFont headFont = wb.createFont();
      headFont.setFontName("宋体");
      headFont.setFontHeightInPoints( (short) 12);
      headFont.setBoldweight( (short) 20);

      HSSFCellStyle headStyle = wb.createCellStyle();
      headStyle.setFont(headFont);
      headStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框


      headStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN); //左边框


      headStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); //右边框


      headStyle.setBorderTop(HSSFCellStyle.BORDER_THIN); //上边框


      headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); //居中

      HSSFRow row2 = sheet.createRow( (short) 1);
      row2.setHeight( (short) 400); //设置行高

      for (int i = 0; i < exportInfo.getTableHead().size(); i++) {
        row2 = this.createCell(row2, (short) i, headStyle,
                               exportInfo.getTableHead().get(i));
      }
      /** 表体*/
      HSSFFont font = wb.createFont(); /** 设置字体样式 */
      font.setFontName("宋体");
      HSSFCellStyle cellStyle = wb.createCellStyle();
      cellStyle.setFont(font);
      cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框


      cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN); //左边框


      cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); //右边框


      cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN); //上边框


      cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT); //居左
      int n=2;
      int m=2;
      int k=1;
      String orgId = "";
      for (int i = 0; i < exportInfo.getRowList().size(); i++) {
    	/*  List list = exportInfo.getRowList();
    	 
    	  String ss = ((Vector)exportInfo.getRowList().get(i)).get(0).toString();
    	  String ss2 = ((Vector)exportInfo.getRowList().get(i+1)).get(0).toString();
    	  if(ss.equals(ss2)){
    		  n++;
    		  m=i;
    	  }*/
    	  //;
        cellList = (Vector) exportInfo.getRowList().get(i);
        HSSFRow row3 = sheet.createRow( (short) i + 2);
        row3.setHeight( (short) 300); //设置行高
        if(cellList!=null&&!"".equals(cellList)){
        	 for (int j = 0; j < cellList.size()-1; j++) {
                 row3 = this.createCell(row3, (short) j, cellStyle, cellList.get(j));
                 
             }
        	 row3 = this.createCell(row3, (short) (cellList.size()-1), cellStyle,String.valueOf(k));
        	 k++;
        }
        String ss = ((Vector)exportInfo.getRowList().get(i)).get(0).toString();
        if(orgId.equals("")){
        	orgId = ss;
        	n++;
        }
        else if(orgId.equals(ss)){
        	n++;
        }else{
        	sheet.addMergedRegion(new Region(m, (short) 0, (n-1), (short) 0));
        	orgId = ss;
        	m = n++;
        	k=1;
        }
       
      }
      sheet.addMergedRegion(new Region(m, (short) 0, (n-1), (short) 0));
      
      sheet.addMergedRegion(new Region(1, (short) 0, 1,
              (short) 1));

    }
    catch (Exception ex) {
      log.info("error while create work book sheet ", ex);
    }

  }
  
  
  /**
   * 创建　上报Execl 文件
   * @param wb HSSFWorkbook
   * @param exportInfo BaseDataExportInfo
   */
  public void createWorkBookSheet3(BaseDataExportInfo exportInfo) {

    Vector cellList = null;
    if(exportInfo.getWorkbookFileName()!= null )
      workbookFileName = exportInfo.getWorkbookFileName()+".xls";
    try {

      HSSFSheet sheet = wb.createSheet(exportInfo.getSheetIndex() + "");
      wb.setSheetName(exportInfo.getSheetIndex(), exportInfo.getSheetName(),
                      (short) 1);

      /** 设置列宽 */

      for (int i = 0; i < exportInfo.getTableHead().size(); i++) {
        if (i == 1 || i == exportInfo.getTableHead().size() - 1) {
          sheet.setColumnWidth( (short) i, (short) 1000);
        }
        else {
          sheet.setColumnWidth( (short) i, (short) 1000);
        }
        sheet.setColumnWidth( (short) 0, (short) 6000);
        
      }
      sheet.setColumnWidth( (short) (exportInfo.getTableHead().size()-1), (short) 1500);
      sheet.setColumnWidth( (short) (exportInfo.getTableHead().size()-2), (short) 3000);
      sheet.setColumnWidth( (short) (exportInfo.getTableHead().size()-3), (short) 3000);

      /**  合并单元格 */
      sheet.addMergedRegion(new Region(0, (short) 0, 0,
                                       (short) (exportInfo.getTableHead().size() -
                                                1)));

      /** 表 标题 */
      HSSFRow row = sheet.createRow( (short) 0);
      row.setHeight( (short) 500); //设置行高

      HSSFFont titleFont = wb.createFont();
      titleFont.setFontName("宋体");
      titleFont.setFontHeightInPoints( (short) 20);
      titleFont.setBoldweight( (short) 20);
      HSSFCellStyle titleStyle = wb.createCellStyle();
      titleStyle.setFont(titleFont);

      titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION); //居中

      row = this.createCell(row, (short) 0, titleStyle,
                            exportInfo.getSheetTitle());

      /** 表头 */
      HSSFFont headFont = wb.createFont();
      headFont.setFontName("宋体");
      headFont.setFontHeightInPoints( (short) 12);
      headFont.setBoldweight( (short) 20);

      HSSFCellStyle headStyle = wb.createCellStyle();
      headStyle.setFont(headFont);
      headStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框


      headStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN); //左边框


      headStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); //右边框


      headStyle.setBorderTop(HSSFCellStyle.BORDER_THIN); //上边框


      headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); //居中

      HSSFRow row2 = sheet.createRow( (short) 1);
      row2.setHeight( (short) 400); //设置行高

      for (int i = 0; i < exportInfo.getTableHead().size(); i++) {
        row2 = this.createCell(row2, (short) i, headStyle,
                               exportInfo.getTableHead().get(i));
      }
      /** 表体*/
      HSSFFont font = wb.createFont(); /** 设置字体样式 */
      font.setFontName("宋体");
      HSSFCellStyle cellStyle = wb.createCellStyle();
      cellStyle.setFont(font);
      cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框


      cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN); //左边框


      cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); //右边框


      cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN); //上边框


      cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT); //居左

      for (int i = 0; i < exportInfo.getRowList().size(); i++) {
        cellList = (Vector) exportInfo.getRowList().get(i);
        HSSFRow row3 = sheet.createRow( (short) i + 2);
        row3.setHeight( (short) 300); //设置行高
        if(cellList!=null&&!"".equals(cellList)){
        	 for (int j = 0; j < cellList.size(); j++) {
                 row3 = this.createCell(row3, (short) j, cellStyle, cellList.get(j));
             }
        }
      }
     /* sheet.addMergedRegion(new Region(1, (short) 1, 1, (short) 2));
      sheet.addMergedRegion(new Region(1, (short) 3, 1, (short) 4));
      sheet.addMergedRegion(new Region(1, (short) 5, 1, (short) 6));
      sheet.addMergedRegion(new Region(1, (short) 7, 1, (short) 8));
      sheet.addMergedRegion(new Region(1, (short) 9, 1, (short) 10));
      sheet.addMergedRegion(new Region(1, (short) 11, 1, (short) 12));
      sheet.addMergedRegion(new Region(1, (short) 13, 1, (short) 14));
      sheet.addMergedRegion(new Region(1, (short) 15, 1, (short) 16));
      sheet.addMergedRegion(new Region(1, (short) 17, 1, (short) 18));
      sheet.addMergedRegion(new Region(1, (short) 19, 1, (short) 20));
      sheet.addMergedRegion(new Region(1, (short) 21, 1, (short) 22));
      sheet.addMergedRegion(new Region(1, (short) 23, 1, (short) 24));*/

    }
    catch (Exception ex) {
      log.info("error while create work book sheet ", ex);
    }

  }
  /**
   * 创建　县级上报Execl 文件
   * @param wb HSSFWorkbook
   * @param exportInfo BaseDataExportInfo
   */
  public void createWorkBookSheet4(BaseDataExportInfo exportInfo) {

    Vector cellList = null;
    if(exportInfo.getWorkbookFileName()!= null )
      workbookFileName = exportInfo.getWorkbookFileName()+".xls";
    try {

      HSSFSheet sheet = wb.createSheet(exportInfo.getSheetIndex() + "");
      wb.setSheetName(exportInfo.getSheetIndex(), exportInfo.getSheetName(),
                      (short) 1);

      /** 设置列宽 */

      for (int i = 0; i < exportInfo.getTableHead().size(); i++) {
        if (i == 1 || i == exportInfo.getTableHead().size() - 1) {
          sheet.setColumnWidth( (short) i, (short) 1000);
        }
        else {
          sheet.setColumnWidth( (short) i, (short) 1000);
        }
      }
      sheet.setColumnWidth( (short) 0, (short) 3000);
      sheet.setColumnWidth( (short) 1, (short) 3000);
      sheet.setColumnWidth( (short) (exportInfo.getTableHead().size()-1), (short) 1500);
      sheet.setColumnWidth( (short) (exportInfo.getTableHead().size()-2), (short) 2500);
      sheet.setColumnWidth( (short) (exportInfo.getTableHead().size()-3), (short) 2500);

      /**  合并单元格 */
      sheet.addMergedRegion(new Region(0, (short) 0, 0,
                                       (short) (exportInfo.getTableHead().size() -
                                                1)));

      /** 表 标题 */
      HSSFRow row = sheet.createRow( (short) 0);
      row.setHeight( (short) 500); //设置行高

      HSSFFont titleFont = wb.createFont();
      titleFont.setFontName("宋体");
      titleFont.setFontHeightInPoints( (short) 20);
      titleFont.setBoldweight( (short) 20);
      HSSFCellStyle titleStyle = wb.createCellStyle();
      titleStyle.setFont(titleFont);

      titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION); //居中

      row = this.createCell(row, (short) 0, titleStyle,
                            exportInfo.getSheetTitle());

      /** 表头 */
      HSSFFont headFont = wb.createFont();
      headFont.setFontName("宋体");
      headFont.setFontHeightInPoints( (short) 12);
      headFont.setBoldweight( (short) 20);

      HSSFCellStyle headStyle = wb.createCellStyle();
      headStyle.setFont(headFont);
      headStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框


      headStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN); //左边框


      headStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); //右边框


      headStyle.setBorderTop(HSSFCellStyle.BORDER_THIN); //上边框


      headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); //居中

      HSSFRow row2 = sheet.createRow( (short) 1);
      row2.setHeight( (short) 400); //设置行高

      for (int i = 0; i < exportInfo.getTableHead().size(); i++) {
        row2 = this.createCell(row2, (short) i, headStyle,
                               exportInfo.getTableHead().get(i));
      }
      /** 表体*/
      HSSFFont font = wb.createFont(); /** 设置字体样式 */
      font.setFontName("宋体");
      HSSFCellStyle cellStyle = wb.createCellStyle();
      cellStyle.setFont(font);
      cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框


      cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN); //左边框


      cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); //右边框


      cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN); //上边框


      cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT); //居左

      int n=2;
      int m=2;
      int k =1;
      String orgId = "";
      for (int i = 0; i < exportInfo.getRowList().size(); i++) {
    	/*  List list = exportInfo.getRowList();
    	 
    	  String ss = ((Vector)exportInfo.getRowList().get(i)).get(0).toString();
    	  String ss2 = ((Vector)exportInfo.getRowList().get(i+1)).get(0).toString();
    	  if(ss.equals(ss2)){
    		  n++;
    		  m=i;
    	  }*/
    	  //;
        cellList = (Vector) exportInfo.getRowList().get(i);
        HSSFRow row3 = sheet.createRow( (short) i + 2);
        row3.setHeight( (short) 300); //设置行高
        if(cellList!=null&&!"".equals(cellList)){
        	 for (int j = 0; j < cellList.size()-1; j++) {
                 row3 = this.createCell(row3, (short) j, cellStyle, cellList.get(j));
                 
             }
        	 row3 = this.createCell(row3, (short) (cellList.size()-1), cellStyle,String.valueOf(k));
        	 k++;
        }
        String ss = ((Vector)exportInfo.getRowList().get(i)).get(0).toString();
        if(orgId.equals("")){
        	orgId = ss;
        	n++;
        }
        else if(orgId.equals(ss)){
        	n++;
        }else{
        	sheet.addMergedRegion(new Region(m, (short) 0, (n-1), (short) 0));
        	orgId = ss;
        	m = n++;
        	k=1;
        }
       
      }
      sheet.addMergedRegion(new Region(m, (short) 0, (n-1), (short) 0));
      
      sheet.addMergedRegion(new Region(1, (short) 0, 1,
              (short) 1));
      
      sheet.addMergedRegion(new Region(1, (short) 2, 1, (short) 3));
      sheet.addMergedRegion(new Region(1, (short) 4, 1, (short) 5));
      sheet.addMergedRegion(new Region(1, (short) 6, 1, (short) 7));
      sheet.addMergedRegion(new Region(1, (short) 8, 1, (short) 9));
      sheet.addMergedRegion(new Region(1, (short) 10, 1, (short) 11));
      sheet.addMergedRegion(new Region(1, (short) 12, 1, (short) 13));
      sheet.addMergedRegion(new Region(1, (short) 14, 1, (short) 15));
      sheet.addMergedRegion(new Region(1, (short) 16, 1, (short) 17));
      sheet.addMergedRegion(new Region(1, (short) 18, 1, (short) 19));
      sheet.addMergedRegion(new Region(1, (short) 20, 1, (short) 21));
      sheet.addMergedRegion(new Region(1, (short) 22, 1, (short) 23));
      sheet.addMergedRegion(new Region(1, (short) 24, 1, (short) 25));

    }
    catch (Exception ex) {
      log.info("error while create work book sheet ", ex);
    }

  }
  
  
  /**
   * 月政务信息统计导出EXCEL
   * @param wb HSSFWorkbook
   * @param exportInfo BaseDataExportInfo
   */
  public void createWorkBookSheet5(BaseDataExportInfo exportInfo) {

    Vector cellList = null;
    if(exportInfo.getWorkbookFileName()!= null )
      workbookFileName = exportInfo.getWorkbookFileName()+".xls";
    try {

      HSSFSheet sheet = wb.createSheet(exportInfo.getSheetIndex() + "");
      wb.setSheetName(exportInfo.getSheetIndex(), exportInfo.getSheetName(),
                      (short) 1);

      /** 设置列宽 */

      for (int i = 0; i < exportInfo.getTableHead().size(); i++) {
        if (i == 1 || i == exportInfo.getTableHead().size() - 1) {
          sheet.setColumnWidth( (short) i, (short) 2800);
        }
        else {
          sheet.setColumnWidth( (short) i, (short) 1800);
        }
        
      }
      sheet.setColumnWidth( (short) 0, (short) 5000);
      sheet.setColumnWidth( (short) (exportInfo.getTableHead().size()-1), (short) 1500);

      /**  合并单元格 */
      sheet.addMergedRegion(new Region(0, (short) 0, 0,
                                       (short) (exportInfo.getTableHead().size() -
                                                1)));

      /** 表 标题 */
      HSSFRow row = sheet.createRow( (short) 0);
      row.setHeight( (short) 500); //设置行高

      HSSFFont titleFont = wb.createFont();
      titleFont.setFontName("宋体");
      titleFont.setFontHeightInPoints( (short) 20);
      titleFont.setBoldweight( (short) 20);
      HSSFCellStyle titleStyle = wb.createCellStyle();
      titleStyle.setFont(titleFont);

      titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION); //居中

      row = this.createCell(row, (short) 0, titleStyle,
                            exportInfo.getSheetTitle());

      /** 表头 */
      HSSFFont headFont = wb.createFont();
      headFont.setFontName("宋体");
      headFont.setFontHeightInPoints( (short) 12);
      headFont.setBoldweight( (short) 20);

      HSSFCellStyle headStyle = wb.createCellStyle();
      headStyle.setFont(headFont);
      headStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框


      headStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN); //左边框


      headStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); //右边框


      headStyle.setBorderTop(HSSFCellStyle.BORDER_THIN); //上边框


      headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); //居中

      HSSFRow row2 = sheet.createRow( (short) 1);
      row2.setHeight( (short) 400); //设置行高

      for (int i = 0; i < exportInfo.getTableHead().size(); i++) {
        row2 = this.createCell(row2, (short) i, headStyle,
                               exportInfo.getTableHead().get(i));
      }
      /** 表体*/
      HSSFFont font = wb.createFont(); /** 设置字体样式 */
      font.setFontName("宋体");
      HSSFCellStyle cellStyle = wb.createCellStyle();
      cellStyle.setFont(font);
      cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框


      cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN); //左边框


      cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); //右边框


      cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN); //上边框


      cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT); //居左

      for (int i = 0; i < exportInfo.getRowList().size(); i++) {
        cellList = (Vector) exportInfo.getRowList().get(i);
        HSSFRow row3 = sheet.createRow( (short) i + 2);
        row3.setHeight( (short) 300); //设置行高
        if(cellList!=null&&!"".equals(cellList)){
        	 for (int j = 0; j < cellList.size(); j++) {
                 row3 = this.createCell(row3, (short) j, cellStyle, cellList.get(j));
             }
        }
      }
      sheet.addMergedRegion(new Region(1, (short) 0, 2, (short) 0));
      sheet.addMergedRegion(new Region(1, (short) 1, 1, (short) 3));
      sheet.addMergedRegion(new Region(1, (short) 4, 1, (short) 6));
      sheet.addMergedRegion(new Region(1, (short) 7, 1, (short) 8));
      sheet.addMergedRegion(new Region(1, (short) 9, 2, (short) 9));
      sheet.addMergedRegion(new Region(1, (short) 10, 2, (short) 10));
      sheet.addMergedRegion(new Region(1, (short) 11, 2, (short) 11));
      sheet.addMergedRegion(new Region(1, (short) 12, 2, (short) 12));
      sheet.addMergedRegion(new Region(1, (short) 13, 2, (short) 13));
    }
    catch (Exception ex) {
      log.info("error while create work book sheet ", ex);
    }

  }

	/**
	   * 创建　包含多个bookSheet 的 Execl 文件
	   * @param wb HSSFWorkbook
	   * @param exportInfo BaseDataExportInfo
	   */
	  public void createMoreWorkBookSheet(BaseDataExportInfo exportInfo) {
		  
	    
	    if(exportInfo.getWorkbookFileName()!= null )
	      workbookFileName = exportInfo.getWorkbookFileName()+".xls";
	    try {

	      for(int n=0;n<exportInfo.getRowList().size();n++){
	      
	      //JOptionPane.showMessageDialog(null,"第" +(n+1)+ "分页！");
	      
	      HSSFSheet sheet = wb.createSheet( n + "");
	      wb.setSheetName(n, exportInfo.getSheetName()+" "+ (n+1),(short) 1);

	      /** 设置列宽 */
	      //设置第二列和倒数第二类的宽度为7000，其它列宽度为4000
	      for (int i = 0; i < exportInfo.getTableHead().size(); i++) {
	        if (i == 1 || i == exportInfo.getTableHead().size() - 1) {
	          sheet.setColumnWidth( (short) i, (short) 7000);
	        }
	        else {
	          sheet.setColumnWidth( (short) i, (short) 4000);
	        }
	      }

	      /**  合并单元格 */
	      //生成标题行, Region的四个参数分别对应 (x1,y1,x2,y2)
	      sheet.addMergedRegion(new Region(0, (short) 0, 0,(short) (exportInfo.getTableHead().size() -1)));

	      /** 表标题 */
	      HSSFRow row = sheet.createRow( (short) 0);
	      row.setHeight( (short) 500); //设置行高
	      
	      //格式化表标题
	      HSSFFont titleFont = wb.createFont();
	      titleFont.setFontName("宋体");
	      titleFont.setFontHeightInPoints( (short) 16);
	      titleFont.setBoldweight( (short) 20);
	      HSSFCellStyle titleStyle = wb.createCellStyle();
	      titleStyle.setFont(titleFont);
	      titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION); //居中

	      row = this.createCell(row, (short) 0, titleStyle,
	                            exportInfo.getSheetTitle());

	      /** 表头 */
	      //格式化表头
	      HSSFFont headFont = wb.createFont();
	      headFont.setFontName("宋体");
	      headFont.setFontHeightInPoints( (short) 12);
	      headFont.setBoldweight( (short) 20);

	      HSSFCellStyle headStyle = wb.createCellStyle();
	      headStyle.setFont(headFont);
	      headStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
	      headStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN); //左边框
	      headStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); //右边框
	      headStyle.setBorderTop(HSSFCellStyle.BORDER_THIN); //上边框
	      headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); //居中

	      HSSFRow row2 = sheet.createRow( (short) 1);
	      row2.setHeight( (short) 400); //设置行高

	      for (int i = 0; i < exportInfo.getTableHead().size(); i++) {
	        row2 = this.createCell(row2, (short) i, headStyle,
	                               exportInfo.getTableHead().get(i));
	      }
	      
	      
	      /** 表体*/
	      //格式化表体
	      HSSFFont font = wb.createFont(); 
	      font.setFontName("宋体");
	      HSSFCellStyle cellStyle = wb.createCellStyle();
	      cellStyle.setFont(font);
	      cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
	      cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN); //左边框
	      cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); //右边框
	      cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN); //上边框
	      cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT); //居左

	      for (int i = 0; i <98; i++) {
	    	if(i<exportInfo.getRowList().size()){ 
	    		if((i+n*100)>=exportInfo.getRowList().size()) break;
	    		
	    		Vector cellList = null;
		        cellList = (Vector) exportInfo.getRowList().get(i+n*100);
		        HSSFRow row3 = sheet.createRow( (short) i + 2);
		        row3.setHeight( (short) 300); //设置行高
		        for (int j = 0; j < cellList.size(); j++) {
		          row3 = this.createCell(row3, (short) j, cellStyle, cellList.get(j));
		        }
		        
	    	 }
	      }
	      
	      if((100*(n+1)-1)>=exportInfo.getRowList().size()) break;
	     }
	    }
	    catch (Exception ex) {
	      log.info("error while create work book sheet ", ex);
	    }

	  }
	


  
  /**
   * 创建单元格

   * @param row HSSFRow
   * @param cellIndex short
   * @param cellStyle HSSFCellStyle
   * @param cellValue Object
   * @return HSSFRow
   */
  public HSSFRow createCell(HSSFRow row, short cellIndex,
                            HSSFCellStyle cellStyle, Object cellValue) {
    HSSFCell cell = row.createCell( (short) cellIndex);
    try {
      cell.setEncoding(HSSFCell.ENCODING_UTF_16);
      if (cellStyle != null) {
        cell.setCellStyle(cellStyle);
      }

      if (cellValue == null) {
        cell.setCellValue("");
      }
      else if (cellValue instanceof Boolean) {
        cell.setCellValue( ( (Boolean) cellValue).booleanValue());
      }
      else if (cellValue instanceof String) {
        cell.setCellValue( (String.valueOf(cellValue)));
      }
      else if (cellValue instanceof Date) {
        cell.setCellValue( (Date) cellValue);
      }
      else {
        cell.setCellValue("");
      }

      //log.info("this cell value is " + cellValue);
    }
    catch (Exception ex) {
      log.error("error while execut create cell ", ex);
    }
    return row;
  }

  /**
   * 读取Excel 表

   * @param aSheet HSSFSheet
   * @throws Exception
   * @return List
   */
  public List readWeekBookSheet(HSSFSheet aSheet) throws Exception {
    List rowList = new ArrayList();
    Vector rowVector = null;
    int rowNum = 1;
    int cellNum = 1;
    int maxCellNum = aSheet.getRow(1).getLastCellNum();
    try {
      for (int rowNumOfSheet = 2; rowNumOfSheet <= aSheet.getLastRowNum();
           rowNumOfSheet++) {
        rowNum = rowNumOfSheet;
        if (null != aSheet.getRow(rowNumOfSheet)) {
          HSSFRow aRow = aSheet.getRow(rowNumOfSheet);
          rowVector = new Vector();
          for (short cellNumOfRow = 0; cellNumOfRow <= maxCellNum; cellNumOfRow++) {
            cellNum = cellNumOfRow;
            if (null != aRow.getCell(cellNumOfRow)) {
              HSSFCell aCell = aRow.getCell(cellNumOfRow);
              int cellType = aCell.getCellType();

              switch (cellType) {
                case HSSFCell.CELL_TYPE_NUMERIC: //整形

//                  rowVector.add(cellNumOfRow,
//                                String.valueOf(aCell.getNumericCellValue())
//                                .substring(0, String.valueOf(aCell.getNumericCellValue()).indexOf(".")));
                  rowVector.add(cellNumOfRow,
                                String.valueOf(aCell.getNumericCellValue()));
                  break;
                case HSSFCell.CELL_TYPE_STRING: //字符串型
                  rowVector.add(cellNumOfRow, aCell.getStringCellValue().trim());
                  break;
                case HSSFCell.CELL_TYPE_FORMULA: //double 型

                  rowVector.add(cellNumOfRow,
                                String.valueOf(aCell.getNumericCellValue()));
                  break;
                case HSSFCell.CELL_TYPE_BLANK: //空字符

                  rowVector.add(cellNumOfRow, "");
                  break;
                case HSSFCell.CELL_TYPE_BOOLEAN: //布尔型

                  rowVector.add(cellNumOfRow,
                                String.valueOf(aCell.getBooleanCellValue()));
                  break;
                default:
                  rowVector.add(cellNumOfRow, "");
              }
            }
            else {
              rowVector.add(cellNumOfRow, "");
            }
          }
          rowList.add(rowVector);
        }
      }

    }
    catch (Exception ex) {
      log.error("error while execut read week book sheet with " + rowNum +
                " rows and " + cellNum + "cols", ex);
      throw new Exception();
    }

    return rowList;
  }

  /**
   * 写Execl 文件
   * @param wb HSSFWorkbook
   * @param outPutStream OutputStream
   */
  public void writeWorkBook(HttpServletResponse response) {
    try {
      
      OutputStream outputStream = new BufferedOutputStream(response.
          getOutputStream());
      response.reset();
      response.setContentType("application/vnd.ms-excel");
      response.setHeader("content-disposition", "attachment;filename=\"" +
                         new String(workbookFileName.getBytes(),
                                    response.getCharacterEncoding()) + "\"");
      wb.write(outputStream);
      outputStream.close();
    }
    catch (Exception ex) {
      log.error("error while create work book ", ex);
    }

  }
}
