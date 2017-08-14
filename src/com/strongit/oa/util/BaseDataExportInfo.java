package com.strongit.oa.util;

/**
 * <p>Title: 江西省财政综合业条系统 </p>
 * <p>Description: 基础数据导出信息类</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: 江西省思创数码科技股份有限公司</p>
 * @author xuzm
 * @version 1.0
 */

import java.util.List;
import java.util.Map;

public class BaseDataExportInfo {
  private String sheetTitle; //工作表标题

  private String sheetName; //工作表名称

  private String workbookFileName; //文件名

  private short sheetIndex = (short)0; //创建工作表的顺序
  private List tableHead; //表头
  private List rowList; //行 Vector v = new Vector(); v.add(i, "");data.add(v);
  private Map rowHigh;//行高
  private List<short[]> regionList;//需要合并的项坐标 short[]{(0)起始行,(1)起始列,(2)结束行,(3)结束列}
  private int colWidth;//列宽
  private int sheetTitleHigh;//表标题高度
  
  public List getRowList() {
    return rowList;
  }

  public void setRowList(List rowList) {
    this.rowList = rowList;
  }

  public String getSheetName() {
    return sheetName;
  }

  public void setSheetName(String sheetName) {
    this.sheetName = sheetName;
  }

  public String getSheetTitle() {
    return sheetTitle;
  }

  public void setSheetTitle(String sheetTitle) {
    this.sheetTitle = sheetTitle;
  }

  public List getTableHead() {
    return tableHead;
  }

  public void setTableHead(List tableHead) {
    this.tableHead = tableHead;
  }

  public String getWorkbookFileName() {
    return workbookFileName;
  }

  public void setWorkbookFileName(String workbookFileName) {
    this.workbookFileName = workbookFileName;
  }

  public short getSheetIndex() {
    return sheetIndex;
  }

  public void setSheetIndex(short sheetIndex) {
    this.sheetIndex = sheetIndex;
  }

/**
 * @return the regionList
 */
public List<short[]> getRegionList() {
	return regionList;
}

/**
 * @param regionList the regionList to set
 */
public void setRegionList(List<short[]> regionList) {
	this.regionList = regionList;
}

public Map getRowHigh() {
	return rowHigh;
}

public void setRowHigh(Map rowHigh) {
	this.rowHigh = rowHigh;
}

public int getColWidth() {
	return colWidth;
}

public void setColWidth(int colWidth) {
	this.colWidth = colWidth;
}

public int getSheetTitleHigh() {
	return sheetTitleHigh;
}

public void setSheetTitleHigh(int sheetTitleHigh) {
	this.sheetTitleHigh = sheetTitleHigh;
}

}
