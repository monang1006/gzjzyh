package com.strongit.oa.util;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.hssf.usermodel.HSSFHeader;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.strongit.oa.work.util.WorkFlowBean;

/**
 * Excel工具类
 * @author 邓志城
 *
 */
public class ExcelUtils {

	/**
	 * 表头
	 */
	private static final String[] tableHeader = {"工作名称/文号","开始时间","状态"};
	/**
	 * 创建工作本
	 */
	public static HSSFWorkbook workBook = new HSSFWorkbook();
	/**
	 * 创建表
	 */
	public static HSSFSheet sheet = workBook.createSheet("工作查询结果");
	/**
	 * 表头的单元格数目
	 */
	public static int cellNum = tableHeader.length;
	/**
	 * 数据库表的列数
	 */
	public static int columnNum = 18;

	/**
	 * 创建表头
	 * @author:邓志城
	 * @date:2009-6-2 下午03:19:31
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public void createTableHeader()throws Exception{
		HSSFHeader header = sheet.getHeader();
		header.setCenter("工作查询结果表");
		HSSFRow headerRow = sheet.createRow(0);
		/*for(int i=0;i<cellNum;i++){
			HSSFCell headerCell = headerRow.createCell(i);
			headerCell.setCellValue(tableHeader[i]);
		}*/
	}

	/**
	 * 创建行
	 * @author:邓志城
	 * @date:2009-6-2 下午04:21:41
	 * @param cells
	 * @param rowIndex
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public void createTableRow(List<Object> cells,int rowIndex)throws Exception{
		//创建第rowIndex行
		HSSFRow row = sheet.createRow(rowIndex);
		/*for(int i=0;i<cells.size();i++){
			//创建第i个单元格
			HSSFCell cell = row.createCell(i);
			cell.setCellValue(cells.get(i).toString());
		}*/
	}

	/**
	 * 创建整个表
	 * @author:邓志城
	 * @date:2009-6-2 下午04:27:22
	 * @param cells 每行填充的列数据
	 * @param rows
	 * @throws Exception
	 */
	public void createExcelSheet(List<Object> cells,int rows)throws Exception{
		createTableHeader();
		for(int j=1;j<=rows;j++){
			createTableRow(cells, j);
		}
	}

	/**
	 * 导出Excel
	 * @author:邓志城
	 * @date:2009-6-2 下午04:32:42
	 * @param sheet
	 * @param os
	 * @throws Exception
	 */
	public void exportSheet(HSSFSheet sheet,OutputStream os)throws Exception{
		sheet.setGridsPrinted(true);
		HSSFFooter footer = sheet.getFooter();
		footer.setRight("Page "+HSSFFooter.page() + "of" + HSSFFooter.numPages());
		workBook.write(os);
	}
	
	public static void main(String[] args)throws Exception{
	        String fileName = "D:\\世界五百强企业名次表.xls";   
	        List<WorkFlowBean> beans = new ArrayList<WorkFlowBean>();
	         FileOutputStream fos = null;   
	            try {   
	            	ExcelUtils pd = new ExcelUtils();   
	            	List<String> cells = new ArrayList<String>();
	            	for(int i=0;i<=5;i++){
	            		WorkFlowBean bean = new WorkFlowBean();
	            		bean.setBussinessName("test"+i);
	            		bean.setFlowStartDate("date"+i);
	            		bean.setFlowStatus("status"+i);
	            		beans.add(bean);
	            		ListUtils.bean2List(bean);
	            	}
	            	/*for(WorkFlowBean bean:beans){
	            		
	            		pd.createExcelSheet(ListUtils.bean2List(bean), 5);  
	            	}
	                fos = new FileOutputStream(fileName);   
	                pd.exportSheet(sheet, fos);
	                System.out.println("表格已成功导出到 : "+fileName);   */
	            } catch (Exception e) {   
	                e.printStackTrace();   
	            } finally {   
	                try {   
	                    fos.close();   
	                } catch (Exception e) {   
	                    e.printStackTrace();   
	                }   
	            }   
	}
}
