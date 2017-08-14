package com.strongit.oa.personnel.structure.statistic;

import java.awt.Color;
import java.awt.Font;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import com.strongit.oa.infopub.statistic.ChangeColor;

import javax.servlet.http.HttpServletRequest;

/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-5-15 上午09:25:38
 * Autour: hull
 * Version: V1.0
 * Description：柱形图
 */
public class DrawBar {

	private String title = ""; // 图片标题
	private String XTitle = ""; // 图片横坐标标题
	private String YTitle = ""; // 图片垂直坐标标题
	private int Xsz = 10; // X轴标尺字体大小
	private int Ysz = 10; // X轴标尺字体大小
	private Color bgcolor = null; // 图片背景颜色
	private int width = 800; // 要生成的图片的宽度
	private int height = 600; // 要生成的图片的高度
	private double margin = 0.2; // 每组柱间的间距 0--1之间
	private boolean isV = true; // 柱图显示方式：0:垂直 1:水平显示
	private String fileName = ""; // 图片名称(可以加路经)

//	private DefaultCategoryDataset dataset = null; // 显示图片需用的数据集
	CategoryDataset dataset =null; 
	private FileOutputStream fosJpg = null; // 生成图片是用到的输出流

	private static DrawBar instance = null;

	public CategoryDataset getDataset() {
		return dataset;
	}

	public void setDataset(CategoryDataset dataset) {
		this.dataset = dataset;
	}

	/**
	 * 单态模式生成类对象
	 * 
	 * @return 该类的一个对象
	 */
	public static synchronized DrawBar getInstance() {
		if (instance == null)
			instance = new DrawBar();
		return instance;
	}

	/**
	 * 改变 图表标题
	 * 
	 * @param str
	 *            图表标题
	 */
	public void setTitle(String str) {
		this.title = str;
	}

	/**
	 * 改变 目录轴的显示标签
	 * 
	 * @param str
	 *            目录轴的显示标签
	 */
	public void setXTitle(String str) {
		this.XTitle = str;
	}

	/**
	 * 改变 数值轴的显示标签
	 * 
	 * @param str
	 *            数值轴的显示标签
	 */
	public void setYTitle(String str) {
		this.YTitle = str;
	}

	/**
	 * 改变 轴的显示标签
	 * 
	 * @param i
	 *            轴的显示标签
	 */
	public void setXFontSize(int i) {
		this.Xsz = i;
	}

	/**
	 * 改变 轴的显示标签
	 * 
	 * @param i
	 *            轴的显示标签
	 */
	public void setYFontSize(int i) {
		this.Ysz = i;
	}

	/**
	 * 设置背景颜色
	 * 
	 * @param red
	 *            红色色素值
	 * @param green
	 *            绿色色素值
	 * @param blue
	 *            兰色色素值
	 */
	public void setBgcolor(int red, int green, int blue) {
		this.bgcolor = new Color(red, green, blue);
	}

	/**
	 * 改变背景颜色
	 * 
	 * @param str
	 *            背景颜色描述 比如:BLACK black blue Blue 等
	 */
	public void setBgcolor(Color color) {
		this.bgcolor = color;
	}

	/**
	 * 改变背景颜色
	 * 
	 * @param str
	 *            背景颜色描述 比如:BLACK black blue Blue 等
	 */
	public void setBgColor(String str) {
		this.bgcolor = ChangeColor.getColor(str);
	}

	/**
	 * 改变图片宽度
	 * 
	 * @param width
	 *            图片宽度
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * 改变图片高度
	 * 
	 * @param height
	 *            图片高度
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * 设置每组柱间的距离
	 * 
	 * @param margin
	 *            间距
	 */
	public void setMargin(double margin) {
		this.margin = margin;
	}

	/**
	 * 改变图片显示方式
	 * 
	 * @param str
	 *            图片显示方式 垂直显示 水平显示
	 */
	public void setIsV(boolean str) {
		this.isV = str;
	}

	/**
	 * 改变文件名称
	 * 
	 * @param str
	 *            文件名称
	 */
	private void setFileName(String str) {
		this.fileName = str;
	}

	/**
	 * 初始化参数
	 */
	public void init() {
		setTitle("柱状图");
		setXTitle("横标题");
		setYTitle("纵标题");
		setXFontSize(10);
		setYFontSize(10);
		setWidth(800);
		setHeight(600);
		setMargin(0.2);
		setIsV(true);
		setBgcolor(255, 255, 255);
		setFileName("temp.jpg");
	}

	/**
	 * 添加要进行画柱状图的数据
	 * 
	 * @param value
	 *            单元值
	 * @param name
	 *            单元项名称
	 * @param group
	 *            该单元项所属的组
	 */
//	public void addData(String name, int value, String group) {
//		if (dataset != null) {
//			dataset.addValue(value, group, name);
//		} else {
//			dataset = new DefaultCategoryDataset();
//			dataset.addValue(value, group, name);
//		}
//	}

	/**
	 * 按文件路径保存生成柱状图
	 * 
	 * @param fileName
	 *            保存文件名称 文件名称为（使用路径为）: d:\\web\test.jpg
	 * @return 执行成功这返回true 否则返回 false
	 */
	public boolean saveAbs(String fileName) {
		if (!fileName.equals("temp.jpg")) {
			this.setFileName(fileName);
		}

		if (dataset == null) {
			return false;
		} else {
			JFreeChart chart = null;
			if (isV == true) {
				chart = ChartFactory.createBarChart3D(this.title, this.XTitle,
						this.YTitle, dataset, PlotOrientation.VERTICAL, true,
						false, false);
			} else {
				chart = ChartFactory.createBarChart3D(this.title, this.XTitle,
						this.YTitle, dataset, PlotOrientation.HORIZONTAL, true,
						false, false);
			}
			BarRenderer3D renderer = new BarRenderer3D();
			//显示每个柱的数值，并修改该数值的字体属性
			renderer.setItemLabelGenerator(new StandardCategoryItemLabelGenerator());
			renderer.setItemLabelFont(new Font("黑体",Font.PLAIN,12));
			renderer.setItemLabelsVisible(true);
			renderer.setItemMargin(0.06);//组内柱子间隔为组宽的10%

			
            CategoryPlot categoryplot = chart.getCategoryPlot();
			CategoryAxis categoryaxis = categoryplot.getDomainAxis();
			categoryaxis.setCategoryMargin(this.margin);
			chart.setBackgroundPaint(this.bgcolor); // 设置背景颜色
			
			categoryplot.setRenderer(renderer);
			// 设置Y轴
			NumberAxis numAxis = (NumberAxis) categoryplot.getRangeAxis();
			numAxis.setTickLabelFont(new Font("black", Font.ITALIC, this.Ysz));

			// 设置Y轴
			categoryaxis.setTickLabelFont(new Font("black", Font.ITALIC,
					this.Xsz));

			try {
				fosJpg = new FileOutputStream(fileName);
				ChartUtilities.writeChartAsJPEG(fosJpg, 100, chart, this.width,
						this.height, null);

			} catch (Exception e) {
			} finally {
			//	this.dataset.clear();
				try {
					fosJpg.close();
				} catch (Exception e) {
				}
			}
			return true;
		}

	}

	/**
	 * 按文件路径保存生成柱状图
	 * 
	 * @param request
	 *            在jsp页面中的request对象 用于获取文件路径
	 * @param fileName
	 *            保存文件名称 文件名称必须使用站点的绝对路径 如 : “/admin/test.jpg"
	 * @return true 成功 false 失败
	 */
	public boolean saveWebFile(HttpServletRequest request, String fileName) {

		if (!fileName.equals("temp.jpg")) {
			this.setFileName(fileName);
		}
		fileName = request.getSession().getServletContext().getRealPath("")
				+ fileName;
		if (dataset == null) {
			return false;
		} else {
			JFreeChart chart = null;
			if (isV == true) {
				chart = ChartFactory.createBarChart3D(this.title, this.XTitle,
						this.YTitle, dataset, PlotOrientation.VERTICAL, true,
						false, false);
			} else {
				chart = ChartFactory.createBarChart3D(this.title, this.XTitle,
						this.YTitle, dataset, PlotOrientation.HORIZONTAL, true,
						false, false);
			}
			CategoryPlot categoryplot = chart.getCategoryPlot();
			CategoryAxis categoryaxis = categoryplot.getDomainAxis();
			categoryaxis.setCategoryMargin(this.margin);
			chart.setBackgroundPaint(this.bgcolor); // 设置背景颜色

			try {
				fosJpg = new FileOutputStream(fileName);
				ChartUtilities.writeChartAsJPEG(fosJpg, 100, chart, this.width,
						this.height, null);

			} catch (Exception e) {
			} finally {
			//	this.dataset.clear();
				try {
					fosJpg.close();
				} catch (Exception e) {
				}
			}
			return true;
		}

	}

	/**
	 * 向web页面输出已生成的图片
	 * 
	 * @param response
	 * @return
	 */
	public boolean writeWebChartAsJPEG(OutputStream os) {
		if (dataset == null) {
			return false;
		} else {
			JFreeChart chart = null;
			if (isV == true) {
				chart = ChartFactory.createBarChart3D(this.title, this.XTitle,
						this.YTitle, dataset, PlotOrientation.VERTICAL, true,
						false, false);
			} else {
				chart = ChartFactory.createBarChart3D(this.title, this.XTitle,
						this.YTitle, dataset, PlotOrientation.HORIZONTAL, true,
						false, false);
			}
			CategoryPlot categoryplot = chart.getCategoryPlot();
			CategoryAxis categoryaxis = categoryplot.getDomainAxis();
			categoryaxis.setCategoryMargin(this.margin);
			chart.setBackgroundPaint(this.bgcolor); // 设置背景颜色

			try {
				ChartUtilities.writeChartAsJPEG(os, 100, chart, this.width,
						this.height);

			} catch (Exception e) {
			} finally {
		//		this.dataset.clear();
			}
			return true;
		}

	}

	/**
	 * @return 要显示的文件的名称（包括文件路径）
	 */
	public String show() {
		return fileName;
	}

	/**
	 * 恢复成员变量为初试状态
	 */
	public void reset() {
//		dataset.clear();
		init();
	}


}