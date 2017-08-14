package com.strongit.workflow.workflowreport;

import java.awt.Color;
import java.awt.Font;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongmvc.webapp.action.BaseActionSupport;

/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-2-6 下午01:33:57
 * Autour: lanlc
 * Version: V1.0
 * Description：
 */
public class WorkFlowReportAction extends BaseActionSupport {
	
	private List<WorkFlowTypeDataBean> processTypeDataList;//填充报表数据
	private List getAllProcessTypeList;//所有的流程类型
	private WorkFlowReportManager manager;
	private String processTypeId;//流程类型ID
	public int type; //输出类型
	private JFreeChart chart; //jfreechart数据
	/**
	 * author:lanlc
	 * description:
	 * modifyer:
	 */
	@Override
	public String delete() throws Exception {
		return null;
	}
	
	/**
	 * author:lanlc
	 * description:
	 * modifyer:
	 */
	@Override
	public String input() throws Exception {
		return null;
	}
	
	/**
	 * author:lanlc
	 * description:
	 * modifyer:
	 */
	@Override
	public String list() throws Exception {
		return null;
	}
	
	/**
	 * author:lanlc
	 * description:
	 * modifyer:
	 */
	@Override
	protected void prepareModel() throws Exception {
	}
	
	/**
	 * author:lanlc
	 * description:
	 * modifyer:
	 */
	@Override
	public String save() throws Exception {
		return null;
	}
	/**
	 * author:lanlc
	 * description:
	 * modifyer:
	 */
	public Object getModel() {
		return null;
	}
	
	/**
	 * author:lanlc
	 * description:流程类型树
	 * modifyer:
	 * @return
	 */
	public String tree()throws Exception{
		getAllProcessTypeList = manager.getAllProcessTypeList();
		return "tree";
	}
		
	/**
	 * author:lanlc
	 * description:创建柱状图数据集
	 * modifyer:
	 * @return
	 */
	private CategoryDataset getDataSet()throws Exception{
		processTypeDataList = new ArrayList<WorkFlowTypeDataBean>();
		processTypeDataList = manager.getProcessAnalyzeByProcessForList(processTypeId);
		int listsize = processTypeDataList.size();
		CategoryDataset dataset=null;
		if(listsize>0){
			double[][] data = new double[listsize][2];		
			String[] rowKeys=new String[listsize];
			String[] columnKeys ={ "待办", "已办"};
			if(processTypeDataList.size()>0){
				for(int i=0;i<processTypeDataList.size();i++){
					WorkFlowTypeDataBean bean = processTypeDataList.get(i);
					rowKeys[i]=bean.getProcessName();
					data[i][0]=bean.getProcessTodo();
					data[i][1]=bean.getProcessDone();
				}
			}
				dataset = DatasetUtilities.createCategoryDataset(rowKeys, columnKeys, data);
			}
		return dataset;
	}	
	
	/**
	 * author:lanlc
	 * description:绘制分组柱状图
	 * modifyer:
	 * @return
	 */
	public String chart()throws Exception{
		CategoryDataset dataset =getDataSet();
		chart = ChartFactory.createBarChart3D(
				"流程统计图", // 图表标题
				"流程类型", // 目录轴的显示标签
				"数量", // 数值轴的显示标签
				dataset, // 数据集
				//PlotOrientation.HORIZONTAL , // 图表方向：水平
				PlotOrientation.VERTICAL , // 图表方向：垂直
				true, 	// 是否显示图例(对于简单的柱状图必须是false)
				true, 	// 是否生成工具
				false 	// 是否生成URL链接
				);
		chart.setBackgroundPaint(new Color(0xECF3F9));
		//重新设置图标标题，改变字体，否则可能出现口字乱码
		chart.setTitle(new TextTitle("流程统计图", new Font("黑体", Font.ITALIC , 18))); 
		//取得统计图标的第一个图例
		LegendTitle legend = chart.getLegend(0);
		//修改图例的字体
		legend.setItemFont(new Font("宋体", Font.BOLD, 14)); 
		CategoryPlot plot = (CategoryPlot)chart.getPlot();
		//取得横轴
		CategoryAxis categoryAxis = plot.getDomainAxis();
		//设置横轴显示标签的字体
		categoryAxis.setLabelFont(new Font("宋体" , Font.BOLD , 18));
		//分类标签以45度角倾斜
		categoryAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
		categoryAxis.setTickLabelFont(new Font("宋体" , Font.BOLD , 18));
		//取得纵轴
		NumberAxis numberAxis = (NumberAxis)plot.getRangeAxis();
		//设置纵轴显示标签的字体
		numberAxis.setLabelFont(new Font("宋体" , Font.BOLD , 18));
		
		BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setDrawBarOutline(false);
		renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator()); 
		renderer.setBaseItemLabelsVisible(true);
		return "chart";
	}
	
	/**
	 * author:lanlc
	 * description:报表显示页面
	 * modifyer:
	 * @return
	 */
	public String reportpage() throws Exception{		
		return "reportpage";
	}
	
	/**
	 * author:lanlc
	 * description:报表填充展现
	 * modifyer:
	 * @return
	 */
	public String report() throws Exception{
		processTypeDataList = new ArrayList<WorkFlowTypeDataBean>();
		processTypeDataList = manager.getProcessAnalyzeByProcessForList(processTypeId);	
		if(processTypeDataList.size()<=0){
			WorkFlowTypeDataBean p = new WorkFlowTypeDataBean();
			processTypeDataList.add(p);
			return "notdate";
		}
		return "report";
	}	
	
	/**
	 * author:lanlc
	 * description:以xls格式导出报表
	 * modifyer:
	 * @return
	 * @throws IOException
	 * @throws JRException
	 */
	public String exportxls() throws IOException,JRException,Exception{
		processTypeDataList = new ArrayList<WorkFlowTypeDataBean>();
		processTypeDataList = manager.getProcessAnalyzeByProcessForList(processTypeId);
		HttpServletResponse response = this.getResponse();
		String jasper = "/WEB-INF/jsp/workflowreport/jasperfile/jasper_template.jasper"; // 你的jasper文件地址   
		Map map = new HashMap();
		//读取jasper   
		File exe_rpt = new File(this.getRequest().getRealPath(jasper));
		JasperReport jasperReport = null;
		jasperReport = (JasperReport) JRLoader.loadObject(exe_rpt.getPath());
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map,new MyDataSource(processTypeDataList));
		ByteArrayOutputStream oStream = new ByteArrayOutputStream();
		JRXlsExporter exporter = new JRXlsExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, oStream);
		exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,Boolean.TRUE);
		exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,Boolean.FALSE);
		exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,Boolean.FALSE);
		exporter.setParameter(JRXlsExporterParameter.IS_AUTO_DETECT_CELL_TYPE,Boolean.TRUE);
		exporter.exportReport();
		byte[] bytes = oStream.toByteArray();
		if (bytes != null && bytes.length > 0) {
			response.reset();
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition",
					"attachment; filename="
							+ java.net.URLEncoder.encode("流程类型统计.xls",
									"utf-8"));
			response.setContentLength(bytes.length);
			ServletOutputStream outputStream = response.getOutputStream();
			outputStream.write(bytes, 0, bytes.length);
			outputStream.flush();
			outputStream.close();
		}
		oStream.close();
		return null;
	}
	
	/**
	 * author:lanlc
	 * description:以pdf格式导出报表
	 * modifyer:
	 * @return
	 * @throws IOException
	 * @throws JRException
	 */
	public String exportpdf() throws IOException,JRException,Exception{
		processTypeDataList = new ArrayList<WorkFlowTypeDataBean>();
		processTypeDataList = manager.getProcessAnalyzeByProcessForList(processTypeId);
		HttpServletResponse response = this.getResponse();
		String jasper = "/WEB-INF/jsp/workflowreport/jasperfile/jasper_template.jasper"; // 你的jasper文件地址   
		Map map = new HashMap();
		//读取jasper   
		File exe_rpt = new File(this.getRequest().getRealPath(jasper));
		JasperReport jasperReport = null;
		jasperReport = (JasperReport) JRLoader.loadObject(exe_rpt.getPath());
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map,new MyDataSource(processTypeDataList));
		ServletOutputStream ouputStream;
		try{
			ouputStream = response.getOutputStream();
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ java.net.URLEncoder.encode("流程类型统计.pdf", "utf-8"));
			JasperExportManager.exportReportToPdfStream(jasperPrint,
					ouputStream);
			ouputStream.flush();
			ouputStream.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	

	public List getGetAllProcessTypeList() {
		return getAllProcessTypeList;
	}

	public List<WorkFlowTypeDataBean> getProcessTypeDataList() {
		return processTypeDataList;
	}
	public void setGetAllProcessTypeList(List getAllProcessTypeList) {
		this.getAllProcessTypeList = getAllProcessTypeList;
	}

	public void setProcessTypeDataList(
			List<WorkFlowTypeDataBean> processTypeDataList) {
		this.processTypeDataList = processTypeDataList;
	}
	@Autowired
	public void setManager(WorkFlowReportManager manager) {
		this.manager = manager;
	}

	public String getProcessTypeId() {
		return processTypeId;
	}

	public void setProcessTypeId(String processTypeId) {
		this.processTypeId = processTypeId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public JFreeChart getChart() {
		return chart;
	}

	public void setChart(JFreeChart chart) {
		this.chart = chart;
	}

}
