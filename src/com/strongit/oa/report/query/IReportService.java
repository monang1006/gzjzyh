package com.strongit.oa.report.query;

import java.util.List;
import java.util.Map;

import com.strongit.oa.bo.TgwReportBean;
import com.strongit.oa.bo.ToaFeedback;
import com.strongit.oa.bo.ToaFeedbackBean;
import com.strongit.oa.bo.ToaReportBean;
import com.strongit.oa.bo.ToaReportBean1;
import com.strongmvc.exception.DAOException;
import com.strongmvc.orm.hibernate.Page;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

/**
 * 生成报表接口
 * @author 邓志城
 * @company  Strongit Ltd. (C) copyright
 * @date 2010-12-17 下午01:50:03
 * @version  3.0
 * @classpath com.strongit.oa.report.query.IReportService
 * @comment
 * @email dengzc@strongit.com.cn
 */
public interface IReportService {

	public static String COLUMN_TEXT = "COLUMNTEXT";	//列头KEY值 		标题
	
	public static String COLUMN_VALUE = "COLUMNVALUE";	//列头Value值	字段名称

	/**
	 * 根据表名称得到表中所有字段名称
	 * @author:邓志城
	 * @date:2010-12-23 下午06:57:11
	 * @param tableName		表名称
	 * @return				表字段列表
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Integer> getColumnField(String tableName);

	/**
	 * 将生成的对象输出到打印机
	 * @author:邓志城
	 * @date:2010-12-24 下午02:39:56
	 * @param jasperPrint		报表对象
	 * @param objects			扩展参数（留待以后扩展）
	 */
	public void exportPrinter(JasperPrint jasperPrint,Object...objects);

	/**
	 * 报表导出Excel格式
	 * @author:邓志城
	 * @date:2010-12-24 下午03:04:16
	 * @param jasperPrint		报表对象
	 * @param objects			扩展参数（第一个参数为输出标题）
	 */
	@SuppressWarnings("deprecation")
	public void exportExcel(JasperPrint jasperPrint,Object...objects);
	
	/**
	 * 报表导出PDF格式
	 * @author:邓志城
	 * @date:2010-12-24 下午03:04:16
	 * @param jasperPrint		报表对象
	 * @param objects			扩展参数（第一个参数为输出标题）
	 */
	public void exportPdf(JasperPrint jasperPrint,Object...objects);
	
	/**
	 * 将报表对象生成HTML格式输出
	 * @author:邓志城
	 * @date:2010-12-23 下午04:57:32
	 * @param jasperPrint		报表对象
	 * @param objects			扩展参数（留待以后扩展）
	 * @return					生成的HTML内容
	 */
	public String exportHtml(JasperPrint jasperPrint,Object...objects);

	/**
	 * 生成报表
	 * @author:邓志城
	 * @date:2010-12-17 下午03:21:49
	 * @param parameters	报表参数
	 * @param columnList	报表列头列表
	 * 	例如:
	 * 		List columnList = new ArrayList();
	 * 		Map map = new HashMap();
	 * 		map.put("COLUMNTEXT","流程标题")
	 * 		map.put("COLUMNVALUE","SENDDOC_TITME"); 
	 * 		columnList.add(map);
	 * @param sql			由调用方传入的SQL语句
	 * @param page			分页对象(支持跨数据库查询)
	 * @return				报表
	 */
	public JasperPrint generateJasperPrint(Map parameters,List<Map<String, String>> columnList,String sql,Page page,String key,int columnWidth);
	
	/**
	 * 生成报表
	 * @author:邓志城
	 * @date:2010-12-17 下午01:38:54
	 * @param parameters	报表参数
	 * @param columnList	报表列头列表
	 * 	例如:
	 * 		List columnList = new ArrayList();
	 * 		Map map = new HashMap();
	 * 		map.put("COLUMNTEXT","流程标题")
	 * 		map.put("COLUMNVALUE","SENDDOC_TITME"); 
	 * 		columnList.add(map);
	 * @param data			报表列头对应的数据列表
	 * 	例如：
	 * 		List data = new ArrayList();
	 * 		Map map = new HashMap();
	 * 		map.put("SENDDOC_TITME","这是测试标题");
	 * 		data.add(map);
	 * @return				报表
	 */
	public JasperPrint generateJaserPrint(Map parameters,List columnList,List data,String key,int columnWidth);
	
	/**
	 * 根据报表模板生成报表
	 * @author:邓志城
	 * @date:2010-12-17 下午01:26:12
	 * @param columnList	List<Map<列头名,列头值>>	//列头
	 * 	例如Map map = new HashMap();
	 * 		map.put("COLUMNTEXT","标题")
	 * 		map.put("COLUMNVALUE","测试标题");
	 * @return
	 */
	public JasperReport generateReport(List columnList,int columnWidth);
	
	/**
	 * 生成报表
	 * @author:邓志城
	 * @date:2010-12-17 下午01:34:24
	 * @param parameters		报表需要的参数
	 * @param jasperReport		报表
	 * @param data				填充报表的数据
	 * @return					报表数据
	 */
	public JasperPrint generateJaserPrint(Map parameters,JasperReport jasperReport,List data);
	/**
     * 填充退文数据
     * @author:xush
     * @date:5/6/2013 2:50 PM
     * @param parameters        报表需要的参数
     * @param jasperReport      报表
     * @param data              填充报表的数据
     * @return                  报表数据
     */
    public List<ToaReportBean1> getReturnData(String year,String startTime);
    /**
     * 填充反馈意见数据
     * @author:xush
     * @date:5/6/2013 2:50 PM
     * @param parameters        报表需要的参数
     * @param jasperReport      报表
     * @param data              填充报表的数据
     * @return                  报表数据
     */
    public List<ToaFeedbackBean> getFeedbackData(String year,String startTime);
    public String getDeptList();
    /**
     * 填充公文统计数据
     * @author:xush
     * @date:5/6/2013 2:47 PM
     * @param parameters        报表需要的参数
     * @param jasperReport      报表
     * @param data              填充报表的数据
     * @return                  报表数据
     */
    public List<TgwReportBean> getgW(String yearGw,String selectDept);
    
    public List<ToaReportBean1> getDeptList2() throws DAOException;
    public int getTotalNum(String yearGw,String selectDept);
    /**
     * 退文总数
     * @author:xush
     * @date:6/6/2013 2:56 PM
     * @param parameters        报表需要的参数
     * @param jasperReport      报表
     * @param data              退文总数
     * @return                  报表数据
     */
    public int getReturnTotal(String year,String startTime);
}
