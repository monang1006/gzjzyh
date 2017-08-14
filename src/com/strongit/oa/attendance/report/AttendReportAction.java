package com.strongit.oa.attendance.report;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.base.JRBasePrintPage;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.attendance.gather.GatherManager;
import com.strongit.oa.attendance.register.MyRecord;
import com.strongit.oa.attendance.register.RegisterManager;
import com.strongit.oa.bo.ToaAttendRecord;
import com.strongit.oa.bo.ToaAttendStatistic;
import com.strongit.oa.bo.ToaBaseOrg;
import com.strongit.oa.bo.ToaReportBean;
import com.strongit.oa.bo.ToaSysmanageProperty;
import com.strongit.oa.historyquery.DataSource;
import com.strongit.oa.infotable.IInfoTableService;
import com.strongit.oa.infotable.infoitem.InfoItemManager;
import com.strongit.oa.infotable.infoset.InfoSetManager;
import com.strongit.oa.personnel.personorg.PersonOrgManager;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongit.oa.common.user.util.Const;
import com.strongit.oa.common.user.util.PropertiesUtil;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;
/**
 * 考勤报表
 * @author 胡丽丽
 * @date:2009-12-10
 *
 */
public class AttendReportAction extends BaseActionSupport {

	private Page<ToaAttendRecord> page=new Page<ToaAttendRecord>(FlexTableTag.MAX_ROWS,true);
	private Page<MyRecord> recordpage=new Page<MyRecord>(FlexTableTag.MAX_ROWS,true);
	private static final long serialVersionUID = 1L;
	private final static String ZERO="0";
	private final static String infoSetCode="40280cc22526195301252649bd340001";
	private Page<List> infopage = new Page<List>(FlexTableTag.MAX_ROWS, true);
	private List<ToaReportBean> infoList;					//信息内容列表
	private ToaAttendStatistic model=new ToaAttendStatistic();//汇总考勤BO对象
	private InfoSetManager infoManager;						//信息集manager
	private InfoItemManager itemManager;					//信息项manager
	private IInfoTableService infoTable;					//信息集服务接口
	private PersonOrgManager orgmanager;					//机构编制manager
	private IInfoTableService manager;
	private List<ToaBaseOrg> orgList;
	private List<ToaSysmanageProperty> columnList;			//信息项列表
	private String pkey;
	private String codeType;	
	private String infoItems;
	private String orgId;									//机构ID				
	private Date beginTime;//汇总出勤开始时间
	private Date endTime;//汇总出勤结束时间
	private String userIds;//人员ID
	private String userNames;//人员名称
	private RegisterManager registerManager;
	private Map<String, BigDecimal> recordMap=new HashMap<String, BigDecimal>();
	private GatherManager gatherManager;
	private int applyTypecount;
	/** 考勤明细合计后的总数据*/
	private List<MyRecord> recordList;
	private String disLogo;//搜索
	private int pagesize;//每页行数
	private int pages;//总页数
	private int newpage;//当前页
	private int begin;//开始行
	private int end;//结束行
	private int bancisize;
	private String userName;//用户姓名
	private String isGather;//是否汇总信息查看明细
	private String userId;//用户ID
	private String begindate;//开始时间
	private String enddate;//结束时间
	/** 考勤明细manager*/
	private RegisterManager recordmanager;
	/** 考勤明细列表*/
	private List<ToaAttendRecord> reList;
    private String exportType;//报表格式
    private int totalNum;
    private int currentPage;		//当前页
    private String reportType;//报表类型
	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public List<ToaAttendRecord> getReList() {
		return reList;
	}

	public void setReList(List<ToaAttendRecord> reList) {
		this.reList = reList;
	}

	public int getApplyTypecount() {
		return applyTypecount;
	}

	public void setApplyTypecount(int applyTypecount) {
		this.applyTypecount = applyTypecount;
	}

	@Autowired
	public void setRegisterManager(RegisterManager registerManager) {
		this.registerManager = registerManager;
	}

	@Override
	public String delete() throws Exception {
		return null;
	}

	@Override
	public String input() throws Exception {
		return null;
	}

	/**
	 * 考勤汇总列表展现
	 * @author 胡丽丽
	 * 
	 */
	@Override
	public String list() throws Exception {
		return null;
	}
	
	/**
	 * 判断一天的班次
	 * @author 胡丽丽
	 * 
	 */
	public void  getTypeCount(){
		applyTypecount=0;
		for(ToaSysmanageProperty property:columnList){
			if("AFFAIR_TIME".equals(property.getInfoItemField())){
				applyTypecount++;
			}
			if("ILL_TIME".equals(property.getInfoItemField())){
				applyTypecount++;
			}
			if("INJURE_TIME".equals(property.getInfoItemField())){
				applyTypecount++;
			}
			if("FUNERAL_LEAVE".equals(property.getInfoItemField())){
				applyTypecount++;
			}
			if("ANNUAL_LEAVE".equals(property.getInfoItemField())){
				applyTypecount++;
			}
			if("WEDDING_LEAVE".equals(property.getInfoItemField())){
				applyTypecount++;
			}
			if("MATEMITY_LEAVE".equals(property.getInfoItemField())){
				applyTypecount++;
			}
		}
	}
	 /**
     * 考勤年报表
     * @return
     * @throws IOException
     * @throws JRException
     * @throws Exception
     */
    public String getYearReport() throws IOException,JRException,Exception{
		try {
			infoList=gatherManager.getReportBeanList(orgId, begindate);
			totalNum=infoList!=null?infoList.size():totalNum;		
			String jasper = "/WEB-INF/jsp/attendance/report/attendReport-year.jasper"; // jasper文件地址   
			//表头
			String titlename=begindate+"年"+"考勤汇总表";
			//填报日期
			String newdate="填 报 日 期:"+gettime("yyyy年MM月dd日",new Date());
			Map map = new HashMap();
			map.put("titleName", titlename);
			map.put("newdate", newdate);
			
			HttpSession session = this.getSession();
			HttpServletRequest request=this.getRequest();
			HttpServletResponse response=this.getResponse();
			JasperPrint jasperPrint=(JasperPrint) session.getAttribute("mysessionkey");
			if(exportType==null||"".equals(exportType)){
				//读取jasper   
				JasperReport jasperReport = null;
				File exe_rpt = new File(this.getRequest().getRealPath(jasper));
				jasperReport = (JasperReport) JRLoader.loadObject(exe_rpt.getPath());
				jasperPrint=JasperFillManager.fillReport(jasperReport, map,new DataSource(infoList));
				
			}
			session.setAttribute("mysessionkey", jasperPrint);
			if(exportType!=null&&"excel".equals(exportType)){
				this.printToExcel(jasperPrint, request, response,titlename);
			}else if(exportType!=null&&"pdf".equals(exportType)){
				this.printToPDF(jasperPrint, request, response,titlename);
			}else if(exportType!=null&&"print".equals(exportType)){
				this.printToPrinter(jasperPrint, request, response);
			}else{
				this.yearGenerateHtml(jasperPrint,request,response);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }
   
	/**
	 * 月考勤报表统计
	 * @return
	 */
	public String dateReport(){
		try {
			SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
			beginTime=df.parse(begindate);
			endTime=df.parse(enddate);
			reList=recordmanager.getRecordByreport(orgId, beginTime,endTime);
            if(reList==null||reList.size()==0){	//是否为空
				bancisize=0;
			}else{
				if(orgId==null||"".equals(orgId)){//判断机构是否为空
					bancisize=recordmanager.getCountBanci();
				}else{
					bancisize=recordmanager.getCountBanci(orgId);
				}
			}
			getRequest().setAttribute("bancisize",bancisize);
			recordZuhe();
			String text="";//表头
			if(endTime!=null){
				text=gettime("yyyy年MM月份", endTime)+"日考勤报表";
			}else{
				text=gettime("yyyy",new Date())+"年"+gettime("MM",new Date())+"月份日考勤报表";
			}
			HttpSession session = this.getSession();
			HttpServletRequest request=this.getRequest();
			HttpServletResponse response=this.getResponse();
			JasperPrint jasperPrint=(JasperPrint) session.getAttribute("mysessionkey");
			if(exportType==null||"".equals(exportType)){
				jasperPrint=this.getJasperPrint();
			}
			session.setAttribute("mysessionkey", jasperPrint);
			if(exportType!=null&&"excel".equals(exportType)){
				this.printToExcel(jasperPrint, request, response,text);
			}else if(exportType!=null&&"pdf".equals(exportType)){
				this.printToPDF(jasperPrint, request, response,text);
			}else if(exportType!=null&&"print".equals(exportType)){
				this.printToPrinter(jasperPrint, request, response);
			}else{
				this.generateHtml(jasperPrint,request,response);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
     * 根据日期计算考勤明细列表
     * @author Administrator胡丽丽
     * @date:2009-12-02
     * @return
     * @throws Exception
     */
	public void recordZuhe(){
		Map<String,Date> map=new HashMap<String, Date>();//已经计算人员
		recordList=new ArrayList<MyRecord>();
		String name="";//用户名称
		if(recordList==null||recordList.size()==0){//判断是否为空
		//	reList=manager.getRecordByOrgId(orgId);
			List<Object> obj=new ArrayList<Object>();//班趟的上下班时间
			MyRecord myreco=null;
			for(ToaAttendRecord record:reList){
				//该人员的这一天是否已经开始计算
				if(map.get(record.getUserId())!=null&&(record.getAttendTime()==map.get(record.getUserId())||record.getAttendTime().equals(map.get(record.getUserId())))){
					String time="";
					if(record.getMregisterTime()!=null){//上班时间
						time=gettime("MM/dd HH:mm",record.getMregisterTime())+"-";
					}
					if(record.getAregisterTime()!=null){//下班时间
					    time=time+	"-"+gettime("MM/dd HH:mm",record.getAregisterTime());
					}
					myreco.setAttendId(myreco.getAttendId()+","+record.getAttendId());
					//迟到
					if(record.getAttendLaterTime()!=null){
					myreco.setAttendLaterTime(myreco.getAttendLaterTime()+record.getAttendLaterTime().intValue());
					}
					//早退
					if(record.getAttendEarlyTime()!=null){
					myreco.setAttendEarlyTime(record.getAttendEarlyTime().intValue()+myreco.getAttendEarlyTime());
					}
					//是否计算缺勤
					if("0".equals(record.getIsCalcuAbsence())){
					myreco.setAbsenceHours(BigDecimal.valueOf(record.getAbsenceHours().floatValue()+myreco.getAbsenceHours().floatValue()).setScale(1,   BigDecimal.ROUND_HALF_UP));
					}
					if("加班".equals(record.getAttendanceType())){
						myreco.setJiaBanHours(record.getAbsenceHours());
					}
					//申请类型是否为空
					if(record.getAttendanceType()!=null&&!"".equals(record.getAttendanceType())){
					myreco.setAttendanceType(myreco.getAttendanceType()+","+record.getAttendanceType());
					}
					if(record.getAttendanceTypeId()!=null&&!"".equals(record.getAttendanceTypeId())){//类型ID是否为空
					myreco.setAttendanceTypeId(myreco.getAttendanceTypeId()+","+record.getAttendanceTypeId());
					}
					//描述
				    if(record.getAttendDesc()!=null&&!"".equals(record.getAttendDesc())){
				    	if(myreco.getAttendDesc()!=null&&!"".equals(myreco.getAttendDesc())){
					        myreco.setAttendDesc(myreco.getAttendDesc()+","+record.getAttendDesc());
				    	}else{
				    		myreco.setAttendDesc(record.getAttendDesc());
				    	}
				    }
				    if(record.getIsCalcu()!=null&&"0".equals(record.getIsCalcu())){//是否计算
				    	myreco.setIsCalcu(record.getIsCalcu());
				    }
				    //应出勤
				    if(record.getShouldAttendHours()!=null&&!"".equals(record.getShouldAttendHours())){
				    	myreco.setShouldAttendHours(BigDecimal.valueOf(record.getShouldAttendHours().floatValue()+myreco.getShouldAttendHours().floatValue()).setScale(1,   BigDecimal.ROUND_HALF_UP));
				    }
					obj.add(time);
					myreco.setWorkList(obj);
				}else{
					myreco=new MyRecord();
					if("0".equals(record.getIsCalcuAbsence())){
					myreco.setAbsenceHours(record.getAbsenceHours());//缺勤时间
					}else{
						myreco.setAbsenceHours(BigDecimal.valueOf(0));
					}
					if(record.getAttendanceType()!=null&&!"".equals(record.getAttendanceType())){//类型名称
					myreco.setAttendanceType(record.getAttendanceType());
					}else{
						myreco.setAttendanceType("");
					}
					if(record.getAttendanceTypeId()!=null&&!"".equals(record.getAttendanceTypeId())){//类型ID是否为空
						myreco.setAttendanceTypeId(record.getAttendanceTypeId());
					}else{
						myreco.setAttendanceTypeId("");
					}
					if(record.getAttendDesc()!=null&&!"".equals(record.getAttendDesc())){//说明描述是否为空
					myreco.setAttendDesc(record.getAttendDesc());
					}else{
						myreco.setAttendDesc("");
					}
					if(record.getAttendEarlyTime()!=null){
					  myreco.setAttendEarlyTime(record.getAttendEarlyTime().intValue());
					}
					//迟到
					if(record.getAttendLaterTime()!=null){
					  myreco.setAttendLaterTime(record.getAttendLaterTime().intValue());
					}
					if("加班".equals(record.getAttendanceType())){
						myreco.setJiaBanHours(record.getAbsenceHours());
					}else{
						myreco.setJiaBanHours(BigDecimal.valueOf(0));
					}
					myreco.setAttendTime(gettime("yyyy-MM-dd",record.getAttendTime()));
					myreco.setIsCalcu(record.getIsCalcu());
					myreco.setIsCalcuAbsence(record.getIsCalcuAbsence());
					myreco.setOrgId(record.getOrgId());
					myreco.setOrgName(record.getOrgName());
					if(record.getShouldAttendHours()!=null){
					myreco.setShouldAttendHours(record.getShouldAttendHours());//应出勤
					}else{
						myreco.setShouldAttendHours(BigDecimal.valueOf(0));//应出勤
					}
					myreco.setUserId(record.getUserId());
					myreco.setUserName(record.getUserName());
					myreco.setAttendId(record.getAttendId());
					obj=new ArrayList<Object>();
					String time="";
					if(record.getMregisterTime()!=null){
						time=gettime("MM/dd HH:mm",record.getMregisterTime())+"-";
					}
					if(record.getAregisterTime()!=null){
					    time=time+	"-"+gettime("MM/dd HH:mm",record.getAregisterTime());
					}
					obj.add(time);
					myreco.setWorkList(obj);
					recordList.add(myreco);
					map.put(record.getUserId(), record.getAttendTime());
				}
			}
		}
	}
	/**
	 * 格式化时间
	 * @author 胡丽丽
	 * @date:2009-11-19
	 * @param time
	 * @return
	 */
	private String gettime(String temp,Date time){
		SimpleDateFormat df = new SimpleDateFormat(temp);//设置日期格式  
		String time1=df.format(time);
		return time1;
	}
	/*
	 * 
	 * @author: 彭小青
	 * @date: 	Nov 24, 2009 9:58:59 PM
	 * Desc:	展现机构树
	 * param:
	 */
	public String orgtree()throws Exception{
		try {
			codeType=PropertiesUtil.getCodeRule(Const.RULE_CODE_ORG);
			orgList = orgmanager.getOrgsByIsdel("0");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "orgtree";
	}

	

	/**
	 * 画日考勤报表信息
	 * @author 胡丽丽
	 * @date 2010-01-28
	 * @return
	 */
	public JasperReport getDatereport()throws Exception{
		
		try {
			String filePath ="/WEB-INF/jsp/attendance/report/attendReport-date.jrxml"; // 报表文件地址  
            //读取报表信息
			File file = new File(this.getRequest().getRealPath(filePath));
            JasperDesign jasperDesign = new JasperDesign();
			jasperDesign = JRXmlLoader.load(file);
			int columnWidth = 110;
			int columnNum = (columnWidth+20)*bancisize;
			jasperDesign.setPageWidth(columnNum+675);//设置页面宽度
			jasperDesign.setColumnWidth( columnNum+615);//设置column宽度
			jasperDesign.setPageHeight(1190);
			int xwidth=0;//左边距
			//报表对象
			JRDesignBand columnHeader = (JRDesignBand)jasperDesign.getColumnHeader();
			JRDesignBand detail = (JRDesignBand)jasperDesign.getDetail();
			JRDesignBand titleheader=(JRDesignBand)jasperDesign.getTitle();
			JRDesignBand pagefooter=(JRDesignBand)jasperDesign.getPageFooter();
			JRDesignBand pageheader=(JRDesignBand)jasperDesign.getPageHeader();

			//表头
			JRDesignStaticText pagetext=(JRDesignStaticText)titleheader.getElementByKey("staticText-1");
			pagetext.setX((columnNum+675)/2-pagetext.getWidth()/2);
			String text=gettime("yyyy",endTime)+"年"+gettime("MM",endTime)+"月份日考勤报表";
			pagetext.setText(text);
			JRDesignTextField footertext=(JRDesignTextField)pagefooter.getElementByKey("textField");
			footertext.setX((columnNum+675)/2-footertext.getWidth()/2);
			JRDesignStaticText pagetext1=(JRDesignStaticText)pageheader.getElementByKey("staticText-17");
			pagetext1.setX((columnNum+615)-pagetext1.getWidth());
            pagetext1.setText("填 报 日 期:"+gettime("yyyy年 MM月 dd日", new Date()));
			//列头动态
			JRDesignStaticText  statictext3 = (JRDesignStaticText)columnHeader.getElementByKey("staticText-3");
			//生成表内容
			JRDesignTextField field1=(JRDesignTextField)detail.getElementByKey("textField-2");
			//循环班趟
			for(int i=1;i<=bancisize;i++){
				JRDesignStaticText statictban=(JRDesignStaticText)statictext3.clone();
				statictban.setX(xwidth+230+(columnWidth+20)*(i-1));
				statictban.setWidth(columnWidth+20);
				statictban.setText("第"+i+"趟班");
				columnHeader.addElement(statictban);
				
				//生成表内容
				JRDesignTextField field=(JRDesignTextField)field1.clone();
				field.setX(xwidth+230+(columnWidth+20)*(i-1));
				field.setWidth(columnWidth+20);
				String fieldtext = "$F{test"+i+"}";
				JRDesignExpression expression1 = new JRDesignExpression();
				expression1.setValueClass(java.lang.String.class);
				expression1.setText(fieldtext);
				field.setExpression(expression1);
				detail.addElement(field);
			}
			JRDesignStaticText  statictext4 = (JRDesignStaticText)columnHeader.getElementByKey("staticText-4");
			statictext4.setX(statictext4.getX()+(columnWidth+20)*bancisize);
			JRDesignStaticText  statictext5 = (JRDesignStaticText)columnHeader.getElementByKey("staticText-5");
			statictext5.setX(statictext5.getX()+(columnWidth+20)*bancisize);
			JRDesignStaticText  statictext7 = (JRDesignStaticText)columnHeader.getElementByKey("staticText-7");
			statictext7.setX(statictext7.getX()+(columnWidth+20)*bancisize);
			JRDesignStaticText  statictext11 = (JRDesignStaticText)columnHeader.getElementByKey("staticText-11");
			statictext11.setX(statictext11.getX()+(columnWidth+20)*bancisize);
			JRDesignStaticText  statictext9 = (JRDesignStaticText)columnHeader.getElementByKey("staticText-9");
			statictext9.setX(statictext9.getX()+(columnWidth+20)*bancisize);
			JRDesignStaticText  statictext12 = (JRDesignStaticText)columnHeader.getElementByKey("staticText-12");
			statictext12.setX(statictext12.getX()+(columnWidth+20)*bancisize);
			JRDesignStaticText  statictext13 = (JRDesignStaticText)columnHeader.getElementByKey("staticText-13");
			statictext13.setX(statictext13.getX()+(columnWidth+20)*bancisize);
			JRDesignStaticText  statictext14 = (JRDesignStaticText)columnHeader.getElementByKey("staticText-14");
			statictext14.setX(statictext14.getX()+(columnWidth+20)*bancisize);
			JRDesignStaticText  statictext15 = (JRDesignStaticText)columnHeader.getElementByKey("staticText-15");
			statictext15.setX(statictext15.getX()+(columnWidth+20)*bancisize);
			
			//移动表内容
			JRDesignTextField field3=(JRDesignTextField)detail.getElementByKey("textField-3");
			field3.setX(field3.getX()+(columnWidth+20)*bancisize);
			JRDesignTextField field4=(JRDesignTextField)detail.getElementByKey("textField-4");
			field4.setX(field4.getX()+(columnWidth+20)*bancisize);
			JRDesignTextField field5=(JRDesignTextField)detail.getElementByKey("textField-5");
			field5.setX(field5.getX()+(columnWidth+20)*bancisize);
			JRDesignTextField field6=(JRDesignTextField)detail.getElementByKey("textField-6");
			field6.setX(field6.getX()+(columnWidth+20)*bancisize);
			JRDesignTextField field7=(JRDesignTextField)detail.getElementByKey("textField-7");
			field7.setX(field7.getX()+(columnWidth+20)*bancisize);
			JRDesignTextField field8=(JRDesignTextField)detail.getElementByKey("textField-8");
			field8.setX(field8.getX()+(columnWidth+20)*bancisize);
			
			return  JasperCompileManager.compileReport(jasperDesign);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			throw e;
		}
	}

	/**
	 * 
	 * Description:生成HTML报表
	 * param: 
	 * @author 	    胡丽丽
	 * @date 	    Jan 28, 2010 4:25:18 PM
	 */
	public void generateHtml(JasperPrint jasperPrint, HttpServletRequest request,HttpServletResponse response) throws Exception{
		  int pages=0;//总页数 
		  StringBuffer htmlString = new StringBuffer();
		  StringBuffer sbuffer = new StringBuffer();
		  String contextPath = request.getContextPath();
		  totalNum=recordList!=null?recordList.size():totalNum;		
		  if(jasperPrint.getPages().size()>0){
			  Object jaspersize=jasperPrint.getPages().get(jasperPrint.getPages().size()-1);//获取最后一页对象
			  int laftsize=((JRBasePrintPage)jaspersize).getElements().size();//获取最后一页数据量
			  if(laftsize>0){
				  pages=jasperPrint.getPages().size();
			  }else{
				  pages=jasperPrint.getPages().size()-1;
			  }
		  }
		  StringBuffer url=new StringBuffer(contextPath+"/attendance/report/attendReport!dateReport.action");
		  try {
			  PrintWriter out = response.getWriter();
			  JRHtmlExporter exporter = new JRHtmlExporter();
			  int pageIndex = currentPage;
			  if (pageIndex < 0) {
				  pageIndex = 0;
			  }
			  int lastPageIndex = 0;
			  if (pages != 0) {
				  lastPageIndex =pages-1 ;
			  }
			  if (pageIndex > lastPageIndex) {
				  pageIndex = lastPageIndex;
			  }
			  if (pages <= 0) {
				  sbuffer.append("报表内容为空！");
			  }else{
				  exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN,Boolean.FALSE);
				  exporter.setParameter(JRHtmlExporterParameter.IMAGES_MAP, new HashMap());
				  exporter.setParameter(JRExporterParameter.OUTPUT_STRING_BUFFER,sbuffer);
				  exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				  exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "utf-8");
				  exporter.setParameter(JRExporterParameter.PAGE_INDEX, new Integer(pageIndex));  
				  exporter.exportReport();
			  }
			  htmlString
			  .append("<%@ page contentType=\"text/html; charset=UTF-8\" %>")
				.append("<html>")
				.append(
						"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">")
				.append("<head>")
				.append("<script src='"+contextPath+"/common/js/jquery/jquery-1.2.6.js' type='text/javascript'></script>\n")
				.append("<script language='javascript'>\n")
				.append("function onsub(exportType,begindate,enddate,orgId,reportType){\n")
				.append("$('#exportType').val(exportType);\n")
				.append("$('#orgId').val(orgId);")
				.append("$('#reportType').val(reportType);")
				.append("$('#begindate').val(begindate);\n")
				.append("$('#enddate').val(enddate);\n")
				.append("$('#myTableForm').submit();")
				.append(" }\n")
				.append("function goPages(page){\n")
				.append("$('#currentPage').val(page);")
				.append("$('#myTableForm').submit();")
				.append(" }\n")
				.append("</script>")
				.append("</head>")
				.append("<body text=\"#000000\" link=\"#000000\" alink=\"#000000\" vlink=\"#000000\" oncontextmenu=\"\">")
			    .append(" <div id=\"contentborder\" align=\"center\">");
			  htmlString.append("<form id='myTableForm'  method='get'  target='targetWindow' action='"+contextPath+"/attendance/report/attendReport!dateReport.action'>\n")
			     .append("<input type='hidden' id='exportType' name='exportType'  value='"+exportType+"'/>\n")
			  
			     .append("<input type='hidden' id='begindate' name='begindate'  value='"+begindate+"' />\n")
			      .append("<input type='hidden' id='enddate' name='enddate'   value='"+enddate+"'/>\n")
			     .append("<input type='hidden' id='orgId' name='orgId'  value='"+orgId+"' />\n")
			      .append("<input type='hidden' id='reportType' name='reportType' value='"+reportType+"' />\n")
			     .append("<input type='hidden' id='totalNum' name='totalNum' value='"+totalNum+"'/>\n")
			  	 .append("<input type='hidden' id='currentPage' name='currentPage' value='"+currentPage+"' />\n");
			  htmlString.append(" <table cellpadding='0' cellspacing='0' border='0' width='778' align=center>")
				 .append("<tr>\n")
				 .append("<td align='left' width=33%>\n")
				 .append("<table width='100%' cellpadding='0' cellspacing='0' border='0'><tr><td><table width='100%' cellpadding='0' cellspacing='0' border='0'>\n")
				 .append("<tr>\n");
			  
	            if (pageIndex > 0) {
	                htmlString.append(" <td><a href='")
	                    .append("javascript:goPages(0)")
	                    .append("'><img src='"+contextPath+"/oa/image/query/first.GIF' border='0'></a></td>\n")
	                    .append(" <td><a href='")
	                    .append("javascript:goPages("+(pageIndex-1)+")")
	                    .append("'><img src='"+contextPath+"/oa/image/query/previous.GIF' border='0'></a></td>\n");
	            }else {
	                htmlString.append(" <td><img src='"+contextPath+"/oa/image/query/first_grey.GIF' border='0'></td>\n")
	                    .append("<td><img src='"+contextPath+"/oa/image/query/previous_grey.GIF' border='0'></td>\n");
	            }
	            if (pageIndex < lastPageIndex) {
	                htmlString.append(" <td><a href='")
	                    .append("javascript:goPages("+(pageIndex+1)+")")
	                    .append("'><img src='"+contextPath+"/oa/image/query/next.GIF' border='0'></a></td>\n")
	                    .append(" <td><a href='")
	                    .append("javascript:goPages("+lastPageIndex+")")
	                    .append("'><img src='"+contextPath+"/oa/image/query/last.GIF' border='0'></a></td>\n");
	            }else {
	                htmlString.append(" <td><img src='"+contextPath+"/oa/image/query/next_grey.GIF' border='0'></td>\n")
	                    .append(" <td><img src='"+contextPath+"/oa/image/query/last_grey.GIF' border='0'></td>\n");
	            }
	            
	            htmlString.append(" <td width='100%'>&nbsp;&nbsp;"+(pageIndex+1)+"/"+pages+"&nbsp;&nbsp;共"+totalNum+"条记录</td>\n")
	                .append(" </tr>\n")
	                .append(" </table></td>\n")
	                .append("</tr></table>")
	                .append("</td>\n")
	                .append("</tr>\n")
	                .append("<tr>\n")
	                .append(" <td align='center'>\n")
	                .append(sbuffer)
	                .append(" </td>\n")
	                .append("</tr>\n")
	                .append("</table>\n")
	                .append("</form>")
					.append("<div></body></html>");            
	            out.println(htmlString.toString());
	            out.flush();
	            out.close();
		  } catch (Exception e) {
			  logger.error(e);
			  e.printStackTrace();
		  } finally {
			  sbuffer.setLength(0);
			  sbuffer = null;
			  htmlString.setLength(0);
			  htmlString = null;
			  url.setLength(0);
			  url = null;
		  }
	}
	/**
	 * 
	 * Description:生成HTML报表
	 * param: 
	 * @author 	    胡丽丽
	 * @date 	    Jan 28, 2010 4:25:18 PM
	 */
	public void yearGenerateHtml(JasperPrint jasperPrint, HttpServletRequest request,HttpServletResponse response) throws Exception{
		  int pages=0;//总页数   
		  StringBuffer htmlString = new StringBuffer();
		  StringBuffer sbuffer = new StringBuffer();
		  String contextPath = request.getContextPath();
		  if(jasperPrint.getPages().size()>0){//报表是否有数据
			  Object jaspersize=jasperPrint.getPages().get(jasperPrint.getPages().size()-1);//获取最后一页对象
			  int laftsize=((JRBasePrintPage)jaspersize).getElements().size();//获取最后一页数据量
			  if(laftsize>0){
				  pages=jasperPrint.getPages().size();
			  }else{
				  pages=jasperPrint.getPages().size()-1;
			  }
		  }
		  StringBuffer url=new StringBuffer(contextPath+"/attendance/report/attendReport!dateReport.action");
		  try {
			  PrintWriter out = response.getWriter();
			  JRHtmlExporter exporter = new JRHtmlExporter();
			  int pageIndex = currentPage;
			  if (pageIndex < 0) {
				  pageIndex = 0;
			  }
			  int lastPageIndex = 0;
			  if (pages!=0) {
				  lastPageIndex = pages-1 ;
			  }
			  if (pageIndex > lastPageIndex) {
				  pageIndex = lastPageIndex;
			  }
			  if (pages <=0) {
				  sbuffer.append("报表内容为空！");
			  }else{
				  exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN,Boolean.FALSE);
				  exporter.setParameter(JRHtmlExporterParameter.IMAGES_MAP, new HashMap());
				  exporter.setParameter(JRExporterParameter.OUTPUT_STRING_BUFFER,sbuffer);
				  exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				  exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "utf-8");
				  exporter.setParameter(JRExporterParameter.PAGE_INDEX, new Integer(pageIndex));  
				  exporter.exportReport();
			  }
			  htmlString
			  .append("<%@ page contentType=\"text/html; charset=UTF-8\" %>")
				.append("<html>")
				.append(
						"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">")
				.append("<head>")
				.append("<script src='"+contextPath+"/common/js/jquery/jquery-1.2.6.js' type='text/javascript'></script>\n")
				.append("<script language='javascript'>\n")
				.append("function onsub(exportType,begindate,enddate,orgId,reportType){\n")
				.append("$('#exportType').val(exportType);\n")
				.append("$('#orgId').val(orgId);")
				.append("$('#reportType').val(reportType);")
				.append("$('#begindate').val(begindate);\n")
				.append("$('#enddate').val(enddate);\n")
				.append("document.forms[0].action='"+contextPath+"/attendance/report/attendReport!getYearReport.action';")
			    .append("$('#myTableForm').submit();")
				.append(" }\n")
				.append("function goPages(page){\n")
				.append("$('#currentPage').val(page);")
				.append("$('#myTableForm').submit();")
				.append(" }\n")
				.append("</script>")
				.append("</head>")
				.append("<body text=\"#000000\" link=\"#000000\" alink=\"#000000\" vlink=\"#000000\" oncontextmenu=\"\">")
			    .append(" <div id=\"contentborder\" align=\"center\">");
			  htmlString.append("<form id='myTableForm'  method='get'  target='targetWindow' action='"+contextPath+"/attendance/report/attendReport!getYearReport.action'>\n")
			     .append("<input type='hidden' id='exportType' name='exportType'  value='"+exportType+"'/>\n")
			     .append("<input type='hidden' id='enddate' name='enddate'  value='"+enddate+"' />\n")
			      .append("<input type='hidden' id='begindate' name='begindate'   value='"+begindate+"'/>\n")
			     .append("<input type='hidden' id='orgId' name='orgId'  value='"+orgId+"' />\n")
			      .append("<input type='hidden' id='reportType' name='reportType' value='"+reportType+"' />\n")
			     .append("<input type='hidden' id='totalNum' name='totalNum' value='"+totalNum+"'/>\n")
			  	 .append("<input type='hidden' id='currentPage' name='currentPage' value='"+currentPage+"' />\n");
			  htmlString.append(" <table cellpadding='0' cellspacing='0' border='0' width='778' align=center>")
				 .append("<tr>\n")
				 .append("<td align='left' width=33%>\n")
				 .append("<table width='100%' cellpadding='0' cellspacing='0' border='0'><tr><td><table width='100%' cellpadding='0' cellspacing='0' border='0'>\n")
				 .append("<tr>\n");
			  
	            if (pageIndex > 0) {
	                htmlString.append(" <td><a href='")
	                    .append("javascript:goPages(0)")
	                    .append("'><img src='"+contextPath+"/oa/image/query/first.GIF' border='0'></a></td>\n")
	                    .append(" <td><a href='")
	                    .append("javascript:goPages("+(pageIndex-1)+")")
	                    .append("'><img src='"+contextPath+"/oa/image/query/previous.GIF' border='0'></a></td>\n");
	            }else {
	                htmlString.append(" <td><img src='"+contextPath+"/oa/image/query/first_grey.GIF' border='0'></td>\n")
	                    .append("<td><img src='"+contextPath+"/oa/image/query/previous_grey.GIF' border='0'></td>\n");
	            }
	            if (pageIndex < lastPageIndex) {
	                htmlString.append(" <td><a href='")
	                    .append("javascript:goPages("+(pageIndex+1)+")")
	                    .append("'><img src='"+contextPath+"/oa/image/query/next.GIF' border='0'></a></td>\n")
	                    .append(" <td><a href='")
	                    .append("javascript:goPages("+lastPageIndex+")")
	                    .append("'><img src='"+contextPath+"/oa/image/query/last.GIF' border='0'></a></td>\n");
	            }else {
	                htmlString.append(" <td><img src='"+contextPath+"/oa/image/query/next_grey.GIF' border='0'></td>\n")
	                    .append(" <td><img src='"+contextPath+"/oa/image/query/last_grey.GIF' border='0'></td>\n");
	            }
	            
	            htmlString.append(" <td width='100%'>&nbsp;&nbsp;"+(pageIndex+1)+"/"+pages+"&nbsp;&nbsp;共"+totalNum+"条记录</td>\n")
	                .append(" </tr>\n")
	                .append(" </table></td>\n")
	                .append("</tr></table>")
	                .append("</td>\n")
	                .append("</tr>\n")
	                .append("<tr>\n")
	                .append(" <td align='center'>\n")
	                .append(sbuffer)
	                .append(" </td>\n")
	                .append("</tr>\n")
	                .append("</table>\n")
	                .append("</form>")
					.append("<div></body></html>");            
	            out.println(htmlString.toString());
	            out.flush();
	            out.close();
		  } catch (Exception e) {
			  logger.error(e);
			  e.printStackTrace();
		  } finally {
			  sbuffer.setLength(0);
			  sbuffer = null;
			  htmlString.setLength(0);
			  htmlString = null;
			  url.setLength(0);
			  url = null;
		  }
	}
	
	/**
	 * 打印报表
	 * @author 胡丽丽
	 * @param jasperPrint
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void printToPrinter(JasperPrint jasperPrint, HttpServletRequest request,HttpServletResponse response)throws Exception{
		try{
			ServletOutputStream ouputStream = response.getOutputStream();	
			//JasperPrintManager.printReport(jasperPrint, true);//直接打印,不用预览PDF直接打印  true为弹出打印机选择.false为直接打印
			response.setContentType("application/octet-stream");
			ObjectOutputStream oos = new ObjectOutputStream(ouputStream);
			oos.writeObject(jasperPrint);//将JasperPrint对象写入对象输出流中 
			oos.flush();
			oos.close();  
			ouputStream.flush();
			ouputStream.close();	
		}catch (Exception e) {
			logger.error(e);
			throw e;
		}
    }

	/**
	 * 导出excel
	 * @author 胡丽丽
	 * @param jasperPrint
	 * @param request
	 * @param response
	 * @param struct
	 * @throws Exception
	 */
    public void printToExcel(JasperPrint jasperPrint, HttpServletRequest request,HttpServletResponse response,String struct) throws Exception {
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
			response.setHeader("Content-Disposition","attachment; filename="+ java.net.URLEncoder.encode(struct+".xls","utf-8"));
			response.setContentLength(bytes.length);
			ServletOutputStream outputStream = response.getOutputStream();
			outputStream.write(bytes, 0, bytes.length);
			outputStream.flush();
			outputStream.close();
		}
		oStream.close();

    }
    
    /**
     * 导出PDF
     * @author 胡丽丽
     * @param jasperPrint
     * @param request
     * @param response
     * @param struct
     */
    public void printToPDF(JasperPrint jasperPrint, HttpServletRequest request,HttpServletResponse response,String struct){
    	ServletOutputStream ouputStream;
		try{
			ouputStream = response.getOutputStream();
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ java.net.URLEncoder.encode(struct+".pdf", "utf-8"));
			JasperExportManager.exportReportToPdfStream(jasperPrint,
					ouputStream);
			ouputStream.flush();
			ouputStream.close();
		}catch(Exception e){
			e.printStackTrace();
		}
    }
    
    public JasperPrint getJasperPrint()throws Exception{
		try{
			JasperReport jasperReport = null;	//编译后的报表文件	
			JasperPrint jasperPrint=null;
			Map parameters = new HashMap();
			jasperReport=getDatereport();			//组装不分组报表
			jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,new AttendDataSource(recordList));
            return jasperPrint;	
		}catch(Exception e){
			e.printStackTrace();
			logger.error(e);
			throw e;
		}
	}
   
    /**
     * 展现机构树
     * @return
     * @throws Exception
     */
    public String orgTree()throws Exception{
    	try {
			codeType=PropertiesUtil.getCodeRule(Const.RULE_CODE_ORG);
			orgList = orgmanager.getOrgsByIsdel("0");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "orgtree";
    }
	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public String save() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}
	public List<ToaSysmanageProperty> getColumnList() {
		return columnList;
	}

	@Autowired
	public void setInfoManager(InfoSetManager infoManager) {
		this.infoManager = infoManager;
	}

	@Autowired
	public void setItemManager(InfoItemManager itemManager) {
		this.itemManager = itemManager;
	}

	public String getPkey() {
		return pkey;
	}

	public void setPkey(String pkey) {
		this.pkey = pkey;
	}

	public Page<List> getInfopage() {
		return infopage;
	}

	public void setInfopage(Page<List> infopage) {
		this.infopage = infopage;
	}

	@Autowired
	public void setInfoTable(IInfoTableService infoTable) {
		this.infoTable = infoTable;
	}

	public List<ToaBaseOrg> getOrgList() {
		return orgList;
	}

	@Autowired
	public void setOrgmanager(PersonOrgManager orgmanager) {
		this.orgmanager = orgmanager;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getCodeType() {
		return codeType;
	}

	public String getInfoItems() {
		return infoItems;
	}

	public void setInfoItems(String infoItems) {
		this.infoItems = infoItems;
	}

	@Autowired
	public void setManager(IInfoTableService manager) {
		this.manager = manager;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getUserIds() {
		return userIds;
	}

	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}

	public String getUserNames() {
		return userNames;
	}

	public void setUserNames(String userNames) {
		this.userNames = userNames;
	}

	public Map<String, BigDecimal> getRecordMap() {
		return recordMap;
	}

	public void setRecordMap(Map<String, BigDecimal> recordMap) {
		this.recordMap = recordMap;
	}

	@Autowired
	public void setGatherManager(GatherManager gatherManager) {
		this.gatherManager = gatherManager;
	}

	public Page<ToaAttendRecord> getPage() {
		return page;
	}

	public void setPage(Page<ToaAttendRecord> page) {
		this.page = page;
	}

	public Page<MyRecord> getRecordpage() {
		return recordpage;
	}

	public void setRecordpage(Page<MyRecord> recordpage) {
		this.recordpage = recordpage;
	}

	public List<MyRecord> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<MyRecord> recordList) {
		this.recordList = recordList;
	}

	public int getPagesize() {
		return pagesize;
	}

	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	public int getNewpage() {
		return newpage;
	}

	public void setNewpage(int newpage) {
		this.newpage = newpage;
	}

	public int getBegin() {
		return begin;
	}

	public void setBegin(int begin) {
		this.begin = begin;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public int getBancisize() {
		return bancisize;
	}

	public void setBancisize(int bancisize) {
		this.bancisize = bancisize;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getIsGather() {
		return isGather;
	}

	public void setIsGather(String isGather) {
		this.isGather = isGather;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getBegindate() {
		return begindate;
	}

	public void setBegindate(String begindate) {
		this.begindate = begindate;
	}

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public void setColumnList(List<ToaSysmanageProperty> columnList) {
		this.columnList = columnList;
	}

	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}

	@Autowired
	public void setRecordmanager(RegisterManager recordmanager) {
		this.recordmanager = recordmanager;
	}

	public String getExportType() {
		return exportType;
	}

	public void setExportType(String exportType) {
		this.exportType = exportType;
	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public List<ToaReportBean> getInfoList() {
		return infoList;
	}

	public void setInfoList(List<ToaReportBean> infoList) {
		this.infoList = infoList;
	}


}
