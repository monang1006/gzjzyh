package com.strongit.oa.noticeconference.util;

/**
 * <p>Title: 江西省财政综合业条系统 </p>
 * <p>Description: XLS(Excel)处理类 </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: 江西省思创数码科技股份有限公司</p>
 * @author xuzm
 * @version 1.0
 */

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;

public class ProcessXSL {
	HSSFWorkbook wb = new HSSFWorkbook();
	int titleIndex = 1;// 标题在第几列

	String workbookFileName = "test.xls";
	protected final Log log = LogFactory.getLog(getClass());

	public ProcessXSL() {
	}

	/**
	 * 创建 Execl 文件
	 * 
	 * @param wb
	 *            HSSFWorkbook
	 * @param exportInfo
	 *            BaseDataExportInfo
	 */
	public HSSFWorkbook createWorkBookSheet(BaseDataExportInfo exportInfo) {

		Vector cellList = null;
		if (exportInfo.getWorkbookFileName() != null)
			workbookFileName = exportInfo.getWorkbookFileName() + ".xls";
		try {

			HSSFSheet sheet = wb.createSheet(exportInfo.getSheetIndex() + "");
			wb.setSheetName(exportInfo.getSheetIndex(), exportInfo
					.getSheetName());

			/** 设置列宽 */

			for (int i = 0; i <= exportInfo.getTableHead().size(); i++) {
				// if (i == 0 || i == exportInfo.getTableHead().size() - 1) {
				/*
				 * if (i == titleIndex ) { sheet.setColumnWidth( (short) i,
				 * (short) 10000); } else { sheet.setColumnWidth( (short) i,
				 * (short) 3000); }
				 */
				if (i == 0 || i == 5) {
					sheet.setColumnWidth((short) i, (short) 5000);
				} else {
					sheet.setColumnWidth((short) i, (short) 3000);
				}
			}

			/** 合并单元格 */
			sheet.addMergedRegion(new Region(0, (short) 0, 0,
					(short) (exportInfo.getTableHead().size() - 1)));

			/** 表 标题 */
			HSSFRow row = sheet.createRow((short) 0);
			row.setHeight((short) 500); // 设置行高

			HSSFFont titleFont = wb.createFont();
			titleFont.setFontName("宋体");
			titleFont.setFontHeightInPoints((short) 16);
			titleFont.setBoldweight((short) 30);
			titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			HSSFCellStyle titleStyle = wb.createCellStyle();
			titleStyle.setFont(titleFont);

			titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION); // 居中

			row = this.createCell(row, (short) 0, titleStyle, exportInfo
					.getSheetTitle());

			/** 表头 */
			HSSFFont headFont = wb.createFont();
			headFont.setFontName("宋体");
			headFont.setFontHeightInPoints((short) 12);
			headFont.setBoldweight((short) 30);
			headFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

			HSSFCellStyle headStyle = wb.createCellStyle();
			headStyle.setFont(headFont);
			headStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框

			headStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN); // 左边框

			headStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); // 右边框

			headStyle.setBorderTop(HSSFCellStyle.BORDER_THIN); // 上边框

			headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中

			headStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 垂直居中

			HSSFRow row2 = sheet.createRow((short) 1);
			row2.setHeight((short) 400); // 设置行高
			// sheet.autoSizeColumn((short)3);
			// sheet.autoSizeColumn((short)2);

			for (int i = 0; i < exportInfo.getTableHead().size(); i++) {
				row2 = this.createCell(row2, (short) i, headStyle, exportInfo
						.getTableHead().get(i));
			}
			/** 表体 */
			HSSFFont font = wb.createFont();
			/** 设置字体样式 */
			font.setFontName("宋体");
			HSSFCellStyle cellStyle = wb.createCellStyle();
			cellStyle.setFont(font);
			cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框

			cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN); // 左边框

			cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); // 右边框

			cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN); // 上边框

			cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 居左

			cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 垂直居中

			for (int i = 0; i < exportInfo.getRowList().size(); i++) {
				cellList = (Vector) exportInfo.getRowList().get(i);
				HSSFRow row3 = sheet.createRow((short) i + 2);
				if (exportInfo.getRowHigh() != null
						&& exportInfo.getRowHigh().get(i + "") != null
						&& !"".equals(exportInfo.getRowHigh().get(i + ""))) {
					row3.setHeight(Short.parseShort((String) exportInfo
							.getRowHigh().get(i + ""))); // 设置行高
				} else {
					row3.setHeight((short) 300); // 设置行高
				}
				for (int j = 0; j < cellList.size(); j++) {
					row3 = this.createCell(row3, (short) j, cellStyle, cellList
							.get(j));
				}
			}
			if (exportInfo.getRegionList() != null
					&& exportInfo.getRegionList().size() > 0) {// 合并单元格
				short[] regionSize;
				Region region;
				for (int i = 0; i < exportInfo.getRegionList().size(); i++) {
					regionSize = exportInfo.getRegionList().get(i);
					region = new Region(regionSize[0], regionSize[1],
							regionSize[2], regionSize[3]);
					wb.getSheetAt(0).addMergedRegion(region);
				}
			}
		} catch (Exception ex) {
			log.info("error while create work book sheet ", ex);
		}
		return wb;
	}

	
	/**
	 * 会议通知创建 Execl 文件
	 * 
	 * @param wb
	 *            HSSFWorkbook
	 * @param exportInfo
	 *            BaseDataExportInfo
	 */
	public HSSFWorkbook createWorkBookSheet1(BaseDataExportInfo exportInfo) {

		Vector cellList = null;
		if (exportInfo.getWorkbookFileName() != null)
			workbookFileName = exportInfo.getWorkbookFileName() + ".xls";
		try {

			HSSFSheet sheet = wb.createSheet(exportInfo.getSheetIndex() + "");
			wb.setSheetName(exportInfo.getSheetIndex(), exportInfo
					.getSheetName());

			/** 设置列宽 */

			for (int i = 0; i <= exportInfo.getTableHead().size(); i++) {
				// if (i == 0 || i == exportInfo.getTableHead().size() - 1) {
				/*
				 * if (i == titleIndex ) { sheet.setColumnWidth( (short) i,
				 * (short) 10000); } else { sheet.setColumnWidth( (short) i,
				 * (short) 3000); }
				 */
				if (i == 0 || i == 5) {
					sheet.setColumnWidth((short) i, (short) 5000);
				}
				else if(i == 4){
					sheet.setColumnWidth((short) i, (short) 1000);
				}
				else {
					sheet.setColumnWidth((short) i, (short) 3000);
				}
			}

			/** 合并单元格 */
			sheet.addMergedRegion(new Region(0, (short) 0, 0,
					(short) (exportInfo.getTableHead().size() - 1)));

			/** 表 标题 */
			HSSFRow row = sheet.createRow((short) 0);
			row.setHeight((short) 500); // 设置行高

			HSSFFont titleFont = wb.createFont();
			titleFont.setFontName("宋体");
			titleFont.setFontHeightInPoints((short) 16);
			titleFont.setBoldweight((short) 30);
			titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			HSSFCellStyle titleStyle = wb.createCellStyle();
			titleStyle.setFont(titleFont);

			titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION); // 居中

			row = this.createCell(row, (short) 0, titleStyle, exportInfo
					.getSheetTitle());

			/** 表头 */
			HSSFFont headFont = wb.createFont();
			headFont.setFontName("宋体");
			headFont.setFontHeightInPoints((short) 12);
			headFont.setBoldweight((short) 30);
			headFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

			HSSFCellStyle headStyle = wb.createCellStyle();
			headStyle.setFont(headFont);
			headStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框

			headStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN); // 左边框

			headStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); // 右边框

			headStyle.setBorderTop(HSSFCellStyle.BORDER_THIN); // 上边框

			headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中

			headStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 垂直居中

			HSSFRow row2 = sheet.createRow((short) 1);
			row2.setHeight((short) 400); // 设置行高
			// sheet.autoSizeColumn((short)3);
			// sheet.autoSizeColumn((short)2);

			for (int i = 0; i < exportInfo.getTableHead().size(); i++) {
				row2 = this.createCell(row2, (short) i, headStyle, exportInfo
						.getTableHead().get(i));
			}
			/** 表体 */
			HSSFFont font = wb.createFont();
			/** 设置字体样式 */
			font.setFontName("宋体");
			HSSFCellStyle cellStyle = wb.createCellStyle();
			cellStyle.setFont(font);
			cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框

			cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN); // 左边框

			cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); // 右边框

			cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN); // 上边框

			cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 居左

			cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 垂直居中

			for (int i = 0; i < exportInfo.getRowList().size(); i++) {
				cellList = (Vector) exportInfo.getRowList().get(i);
				HSSFRow row3 = sheet.createRow((short) i + 2);
				if (exportInfo.getRowHigh() != null
						&& exportInfo.getRowHigh().get(i + "") != null
						&& !"".equals(exportInfo.getRowHigh().get(i + ""))) {
					row3.setHeight(Short.parseShort((String) exportInfo
							.getRowHigh().get(i + ""))); // 设置行高
				} else {
					row3.setHeight((short) 300); // 设置行高
				}
				for (int j = 0; j < cellList.size(); j++) {
					row3 = this.createCell(row3, (short) j, cellStyle, cellList
							.get(j));
				}
			}
			if (exportInfo.getRegionList() != null
					&& exportInfo.getRegionList().size() > 0) {// 合并单元格
				short[] regionSize;
				Region region;
				for (int i = 0; i < exportInfo.getRegionList().size(); i++) {
					regionSize = exportInfo.getRegionList().get(i);
					region = new Region(regionSize[0], regionSize[1],
							regionSize[2], regionSize[3]);
					wb.getSheetAt(0).addMergedRegion(region);
				}
			}
		} catch (Exception ex) {
			log.info("error while create work book sheet ", ex);
		}
		return wb;
	}
	
	/**
	 * 创建 包含多个bookSheet 的 Execl 文件
	 * 
	 * @param wb
	 *            HSSFWorkbook
	 * @param exportInfo
	 *            BaseDataExportInfo
	 */
	public HSSFWorkbook createMoreWorkBookSheet(BaseDataExportInfo exportInfo) {
		if (exportInfo.getWorkbookFileName() != null)
			workbookFileName = exportInfo.getWorkbookFileName() + ".xls";
		try {

			for (int n = 0; n < exportInfo.getRowList().size(); n++) {
				// JOptionPane.showMessageDialog(null,"第" +(n+1)+ "分页！");
				HSSFSheet sheet = wb.createSheet(n + "");
				wb.setSheetName(n, exportInfo.getSheetName() + " " + (n + 1));

				/** 设置列宽 */
				// 设置第二列和倒数第二类的宽度为7000，其它列宽度为4000
				// 显示一个英文字符大概275
				for (int i = 0; i < exportInfo.getTableHead().size(); i++) {
					if (i == 1 || i == exportInfo.getTableHead().size() - 1) {
						sheet.setColumnWidth((short) i, (short) 7000);
					} else {
						sheet.setColumnWidth((short) i, (short) 4000);
					}
				}

				/** 合并单元格 */
				// 生成标题行, Region的四个参数分别对应 (x1,y1,x2,y2)
				sheet.addMergedRegion(new Region(0, (short) 0, 0,
						(short) (exportInfo.getTableHead().size() - 1)));

				/** 表标题 */
				HSSFRow row = sheet.createRow((short) 0);
				row.setHeight((short) 300); // 设置行高

				// 格式化表标题
				HSSFFont titleFont = wb.createFont();
				titleFont.setFontName("宋体");
				titleFont.setFontHeightInPoints((short) 16);
				titleFont.setBoldweight((short) 20);
				HSSFCellStyle titleStyle = wb.createCellStyle();
				titleStyle.setFont(titleFont);
				titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION); // 居中

				row = this.createCell(row, (short) 0, titleStyle, exportInfo
						.getSheetTitle());

				/** 表头 */
				// 格式化表头
				HSSFFont headFont = wb.createFont();
				headFont.setFontName("宋体");
				headFont.setFontHeightInPoints((short) 12);
				headFont.setBoldweight((short) 20);

				HSSFCellStyle headStyle = wb.createCellStyle();
				headStyle.setFont(headFont);
				headStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框

				headStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN); // 左边框

				headStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); // 右边框

				headStyle.setBorderTop(HSSFCellStyle.BORDER_THIN); // 上边框

				headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中

				HSSFRow row2 = sheet.createRow((short) 1);
				row2.setHeight((short) 300); // 设置行高

				for (int i = 0; i < exportInfo.getTableHead().size(); i++) {
					row2 = this.createCell(row2, (short) i, headStyle,
							exportInfo.getTableHead().get(i));
				}

				/** 表体 */
				// 格式化表体
				HSSFFont font = wb.createFont();
				font.setFontName("宋体");
				HSSFCellStyle cellStyle = wb.createCellStyle();
				cellStyle.setFont(font);
				cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框

				cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN); // 左边框

				cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); // 右边框

				cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN); // 上边框

				cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 居左

				for (int i = 0; i < 98; i++) {
					if (i < exportInfo.getRowList().size()) {
						if ((i + n * 100) >= exportInfo.getRowList().size())
							break;

						Vector cellList = null;
						cellList = (Vector) exportInfo.getRowList().get(
								i + n * 100);
						HSSFRow row3 = sheet.createRow((short) i + 2);
						row3.setHeight((short) 300); // 设置行高
						for (int j = 0; j < cellList.size(); j++) {
							row3 = this.createCell(row3, (short) j, cellStyle,
									cellList.get(j));
						}

					}
				}

				if ((100 * (n + 1) - 1) >= exportInfo.getRowList().size())
					break;
			}
		} catch (Exception ex) {
			log.info("error while create work book sheet ", ex);
		}
		return wb;
	}

	/**
	 * 创建单元格
	 * 
	 * 
	 * @param row
	 *            HSSFRow
	 * @param cellIndex
	 *            short
	 * @param cellStyle
	 *            HSSFCellStyle
	 * @param cellValue
	 *            Object
	 * @return HSSFRow
	 */
	public HSSFRow createCell(HSSFRow row, short cellIndex,
			HSSFCellStyle cellStyle, Object cellValue) {
		HSSFCell cell = row.createCell((short) cellIndex);
		cellStyle.setWrapText(true);// 自动换行 yanjian 2012-01-10 15:30
		try {
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			if (cellStyle != null) {
				cell.setCellStyle(cellStyle);
			}

			if (cellValue == null) {
				cell.setCellValue("");
			} else if (cellValue instanceof Boolean) {
				cell.setCellValue(((Boolean) cellValue).booleanValue());
			} else if (cellValue instanceof String) {
				cell.setCellValue((String.valueOf(cellValue)));

				int length = (String.valueOf(cellValue)).length();
				if (length > 20 && cellIndex == (short) titleIndex) {
					int higth = 300 * (length / 20 + 1);

					if (row.getHeight() < higth) {
						row.setHeight((short) higth);
					}

				} else if (length > 7 && cellIndex != (short) titleIndex) {
					int higth = 300 * (length / 7 + 1);

					if (row.getHeight() < higth) {
						row.setHeight((short) higth);
					}
				}
				System.out.println("#####>" + cellValue);
			} else if (cellValue instanceof Date) {
				cell.setCellValue((Date) cellValue);
			} else {
				cell.setCellValue("");
			}

			// log.info("this cell value is " + cellValue);
		} catch (Exception ex) {
			log.error("error while execut create cell ", ex);
		}
		return row;
	}

	/**
	 * 读取Excel 表
	 * 
	 * 
	 * @param aSheet
	 *            HSSFSheet
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
			for (int rowNumOfSheet = 2; rowNumOfSheet <= aSheet.getLastRowNum(); rowNumOfSheet++) {
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
							case HSSFCell.CELL_TYPE_NUMERIC: // 整形

								// rowVector.add(cellNumOfRow,
								// String.valueOf(aCell.getNumericCellValue())
								// .substring(0,
								// String.valueOf(aCell.getNumericCellValue()).indexOf(".")));
								rowVector.add(cellNumOfRow, String
										.valueOf(aCell.getNumericCellValue()));
								break;
							case HSSFCell.CELL_TYPE_STRING: // 字符串型
								rowVector.add(cellNumOfRow, aCell
										.getStringCellValue().trim());
								break;
							case HSSFCell.CELL_TYPE_FORMULA: // double 型

								rowVector.add(cellNumOfRow, String
										.valueOf(aCell.getNumericCellValue()));
								break;
							case HSSFCell.CELL_TYPE_BLANK: // 空字符

								rowVector.add(cellNumOfRow, "");
								break;
							case HSSFCell.CELL_TYPE_BOOLEAN: // 布尔型

								rowVector.add(cellNumOfRow, String
										.valueOf(aCell.getBooleanCellValue()));
								break;
							default:
								rowVector.add(cellNumOfRow, "");
							}
						} else {
							rowVector.add(cellNumOfRow, "");
						}
					}
					rowList.add(rowVector);
				}
			}

		} catch (Exception ex) {
			log.error("error while execut read week book sheet with " + rowNum
					+ " rows and " + cellNum + "cols", ex);
			throw new Exception();
		}

		return rowList;
	}

	/**
	 * 写Execl 文件
	 * 
	 * @param wb
	 *            HSSFWorkbook
	 * @param outPutStream
	 *            OutputStream
	 */
	public void writeWorkBook(HttpServletResponse response) {
		try {
			OutputStream outputStream = new BufferedOutputStream(response
					.getOutputStream());
			response.reset();
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("content-disposition", "attachment;filename=\""
					+ new String(workbookFileName.getBytes(), response
							.getCharacterEncoding()) + "\"");
			wb.write(outputStream);
			outputStream.close();
		} catch (Exception ex) {
			log.error("error while create work book ", ex);
		}
	}

	/**
	 * 写Execl 文件
	 * 
	 * @param outPutStream
	 *            OutputStream
	 */
	public void writeWorkBook(OutputStream os) {
		try {
			wb.write(os);
		} catch (Exception ex) {
			log.error("error while create work book ", ex);
		}
	}
}
