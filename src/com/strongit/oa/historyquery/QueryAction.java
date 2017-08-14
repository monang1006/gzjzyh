package com.strongit.oa.historyquery;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.base.JRBasePrintPage;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.archive.archivefolder.ArchiveFolderManager;
import com.strongit.oa.bo.ToaReportBean;
import com.strongit.oa.bo.ToaSysmanageDictitem;
import com.strongit.oa.bo.ToaSysmanageProperty;
import com.strongit.oa.bo.ToaSysmanageStructure;
import com.strongit.oa.dict.IDictService;
import com.strongit.oa.dict.dictItem.DictItemManager;
import com.strongit.oa.infotable.infoitem.InfoItemManager;
import com.strongit.oa.infotable.infoset.InfoSetManager;
import com.strongit.oa.util.BaseDataExportInfo;
import com.strongit.oa.util.CommonUtilWay;
import com.strongit.oa.util.ProcessXSL;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.uums.bo.TUumsBaseUser;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * 
 * @company  Strongit
 * @author 	 彭小青
 * @date 	 Dec 23, 2009 1:39:25 PM
 * @version  2.0.4
 * @comment	 历史记录查询
 */
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "/query.action") })
public class QueryAction extends BaseActionSupport {

	private static final long serialVersionUID = 1L;
	
	 private static final String TABLESTR="T_OA_HISTORY_ATT,INFOTB323,INFOTB327,INFOTB328,INFOTB329,INFOTB361,INFOTB362,INFOTB365,INFOTB367,INFOTB368";

	private Page<List> infopage = new Page<List>(20, true);	//分页列表
	
	private List<ToaSysmanageProperty> columnList;			//信息项列表
	
	private List<ToaSysmanageStructure> structureList;		//信息集列表
	
	private List<ToaReportBean> reportDateList;				//报表数据列表
	
	private InfoSetManager infoManager;						//信息集manager
	
	private InfoItemManager itemManager;					//信息项manager
	
	@Autowired private QueryManager manager;				
	
	private DictItemManager dictItemManager;				//字典项Manager
	
	private String infoSetValue;		//信息集值
	
	private String objName;				//信息集名称
	
	private String pkey;				//信息集主键

	private String flowId;				//对应流程表单

	private String inputId;				//对应数据ID
	
	private String infoItemId;			//信息项ID		
	
	private String length;				//查询条件个数
	
	private String condition;			//查询条件
	
	private String bds;					//条件表达式
    
    private String querydescript;	//查询条件描述
	
	private static final String VAR = "0"; 	// 字符串类型

    private static final String CODE = "1"; // 代码类型
    
    private static final String NUM = "2"; 	// 数值类型

    private static final String YEAR = "3"; // 年份
    
    private static final String MONTH = "4";// 年月
    
    private static final String DATE = "5"; //  年月日
    
    private static final String DATE2="6";	//年月日时间

    private static final String PHOTO = "11";// 照片类型
    
    private static final String DES = "14"; // 备注

    private String module;			//模块标识

    private String targetTable;		//目标表
    
    private String groupByFiled;	//分组字段
    
    private int currentPage;		//当前页
    
    private int totalNum;
    
	/** 报表统计时间*/
	private String reportDate;
	
	private String reportEndDate;
	
	private String docTitle;
	
    
    private String exportType;		//导出方式
    
    private static String sessionKey="mysessionkey";	//session名			
    
	private HashMap<String, String> ordermap = new HashMap<String, String>();
	
	private String checkedvalue;
	
	private final static String PERSON_TABLE_NAME="T_OA_BASE_PERSON";
	
	private final static String PERSON_PKEY="PERSONID";
	
	private final static String PERSON_TABLE_CODE="40288239230c361b01230c7a60f10015";
	
	/** 字典接口 */
	private IDictService dictService;
	
	/** 字典项列表 */
	private List<ToaSysmanageDictitem> levelList;
	
	/** 机构列表*/
	private List orgList;
	
	@Autowired private ArchiveFolderManager amanager;
	/** 对象编号*/
	private String objId = null;


	/** 对应模块类型*/
	private String moduletype = null;


	
	
	/** 登记分发报表查询条件 **/
    private String qszt;
	
	private String recvNum;
	
	private String workTitle;
	
    private String docNumber;
	
	private String departSigned;
	
	private String zbcs;
	
	 private String csqs;
		
	private String recvstartTime;
		
	private String recvendTime;
	
	private String recvStartNum;
	
	private String recvEndNum;

	@Override
	public String delete() throws Exception {
		return null;
	}

	@Override
	public String input() throws Exception {
		ToaSysmanageStructure struct=infoManager.getInfoSetByInfoSetName(infoSetValue);
		pkey=struct.getInfoSetPkey();
		objName=struct.getInfoSetName();
		return "input";
	}

	@Override
	public String list() throws Exception {
		return "record";
	}
	
	/*
	 * 
	 * Description:进入复合查询界面
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Dec 23, 2009 3:21:11 PM
	 */
	public String initialQuery()throws Exception{
		ToaSysmanageStructure struct;
		if(module!=null&&"recordQuery".equals(module)){	//综合查询
			structureList=infoManager.getCreatedStructure();
			struct=structureList.get(0);
			infoSetValue=struct.getInfoSetValue();
			columnList=itemManager.getCreatedItemsByValue(infoSetValue);	//过滤掉照片、备注、文件类型和隐藏的字段，并且condtion为空的。
		}else if(module!=null&&"personQuery".equals(module)){	//人事信息查询
			structureList=infoManager.getChildCreatedInfoSet2(PERSON_TABLE_NAME, true);
			struct=structureList.get(0);
			infoSetValue=struct.getInfoSetValue();
			columnList=itemManager.getCreatedItemsByValue(infoSetValue);	//过滤掉照片、备注、文件类型和隐藏的字段，并且condtion为空的。
		}else{		//历史数据查询
			struct=infoManager.getInfoSetByInfoSetName(infoSetValue);
			if(struct!=null)
				objName=struct.getInfoSetName();
			columnList=itemManager.getCreatedItemsByValueAndCon(infoSetValue, "1");
		}
		return "initial";
	}
	
	/*
	 * 
	 * Description:复合查询
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Dec 24, 2009 10:25:29 AM
	 */
	public String complexQuery()throws Exception{
		HttpServletRequest request=this.getRequest();
		HttpSession session=this.getSession();
		session.removeAttribute(sessionKey);
		ToaSysmanageStructure struct=infoManager.getInfoSetByInfoSetName(infoSetValue);// 获取实体对象
		String infosetCode = struct.getInfoSetCode();
		String reference;
		String para1 = "operator";
		String flag ="";
		String flag1="";
		String paraOperator="";
		String infoItemValue = "";
		String conditions="";
		String temp="";
		int k=0;
		condition="";
		if(bds!=null&&!"".equals(bds)){
			for (int i = 0;i < Integer.parseInt(length); i++)// 多个条件的时候
			{
				flag = Integer.toOctalString(i).toString();// 用于组合符合浏览器端参数值
				flag1 = Integer.toOctalString(i + 1).toString();
				paraOperator = para1 + flag;
				String conName= request.getParameter( "values" + flag); 	
				String operator = request.getParameter(paraOperator); 	// 获取响应的浏览器端传递参数值当作条件参数。
				if (operator.equalsIgnoreCase("9")|| operator.equalsIgnoreCase("10")){
					infoItemValue = "";
				}else{
					infoItemValue = request.getParameter("infoItemValue" + flag1); 	//获取响应的浏览器端传递参数值当作条件参数
					infoItemValue=infoItemValue.trim();
				}
				
				// 分离参数中的信息集和信息项.
				int a = conName.indexOf(".");	
				String second = conName.substring(a + 1);	//字段名
				ToaSysmanageProperty infoItem =itemManager.getInfoItemByValue(second, infosetCode);
				String infoItemDatatype = infoItem.getInfoItemDatatype();// 选择的信息项的类型
				reference = infoItem.getInfoItemDictCode(); // 引用字典类编码	
				
				String itemCondition = this.QueryConditon(infoSetValue, operator,
						conName, infoItemValue, infoItemDatatype, reference,
						Integer.parseInt(length), i);
				condition = condition + itemCondition;
			}
			if(condition!=null&&!"".equals(condition)){
				for(int j=0;j<bds.length();j++){
					temp=bds.substring(j,j+1);
					if(temp.equals("*")){
						conditions+=" and ";
					}else if(temp.equals("+")){
						conditions+=" or ";
					}else if(temp.equals("(")||temp.equals(")")){
						conditions+=temp;
					}else if(temp.equals(" ")){
						
					}else{
						k=Integer.parseInt(temp);
						conditions+=getConditionByPlace(condition,k);
					}
				}
			}
			condition=conditions;
		}
		StringBuffer sb = new StringBuffer(
			"<script> var scriptroot = '")
			.append(getRequest().getContextPath())
			.append("'</script>")
			.append("<SCRIPT src='")
			.append(getRequest().getContextPath())
			.append("/common/js/commontab/workservice.js'>")
			.append("</SCRIPT>")
			.append("<SCRIPT src='")
			.append(getRequest().getContextPath())
			.append("/common/js/commontab/service.js'>")
			.append("</SCRIPT>")
			.append("<script>");
			if(module!=null&&"recordQuery".equals(module)){	//综合查询模块
				sb.append("window.dialogArguments.setValue(\"").append(condition).append("\",\"").append(infoSetValue).append("\",\"").append(groupByFiled).append("\");")
					.append("window.dialogArguments.parent.frames[1].submitSearch(\"").append(condition).append("\",\"").append(infoSetValue).append("\",\"").append(groupByFiled).append("\");")
					.append("window.close();</script>");		
			}else{	//历史数据查询模块
				sb.append(" window.dialogArguments.submitSearch(\"").append(condition).append("\",\"").append(infoSetValue).append("\",\"").append(groupByFiled).append("\");")
				.append("window.close();</script>");
			}	
		return renderHtml(sb.toString());
	}
	
	
	/*
	 * 
	 * Description:多表复合查询（人事信息查询模块的复合查询专用）
	 * param: 
	 * @author 	    彭小青
	 * @date 	    May 4, 2010 5:38:09 PM
	 */
	public String multipleTableQuery()throws Exception{
		HttpServletRequest request=this.getRequest();
		HttpSession session=this.getSession();
		session.removeAttribute(sessionKey);
		String reference;
		String para1 = "operator";
		String flag ="";
		String flag1="";
		String paraOperator="";
		String infoItemValue = "";
		String conditions="";
		String temp="";
		int k=0;
		condition="";
		String tables="";
		String tempTable="";
		if(bds!=null&&!"".equals(bds)){
			for (int i = 0;i < Integer.parseInt(length); i++)// 多个条件的时候
			{
				flag = Integer.toOctalString(i).toString();// 用于组合符合浏览器端参数值
				flag1 = Integer.toOctalString(i + 1).toString();
				paraOperator = para1 + flag;
				String conName=	request.getParameter( "values" + flag); 	
				String operator = request.getParameter(paraOperator); 	// 获取响应的浏览器端传递参数值当作条件参数。
				if (operator.equalsIgnoreCase("9")|| operator.equalsIgnoreCase("10")){//为空或者不为空
					infoItemValue = "";
				}else{
					infoItemValue = request.getParameter("infoItemValue" + flag1); 	//获取响应的浏览器端传递参数值当作条件参数
					infoItemValue=infoItemValue.trim();
				}
				
				// 分离参数中的信息集和信息项.
				int a = conName.indexOf(".");	
				String second = conName.substring(a + 1);	//字段名
				String first=conName.substring(0,a);
				if(tables.indexOf(first)==-1&&!PERSON_TABLE_NAME.equals(first)){
					tables+=","+first+" "+first;
					tempTable+=","+first;
				}
				ToaSysmanageProperty infoItem =itemManager.getItemByTableNameAndFiled(first,second);
				String infoItemDatatype = infoItem.getInfoItemDatatype();// 选择的信息项的类型
				reference = infoItem.getInfoItemDictCode(); // 引用字典类编码	
				
				String itemCondition = this.QueryConditon(infoSetValue, operator,
						conName, infoItemValue, infoItemDatatype, reference,
						Integer.parseInt(length), i);
				condition = condition + itemCondition;
			}
			if(condition!=null&&!"".equals(condition)){
				for(int j=0;j<bds.length();j++){
					temp=bds.substring(j,j+1);
					if(temp.equals("*")){
						conditions+=" and ";
					}else if(temp.equals("+")){
						conditions+=" or ";
					}else if(temp.equals("(")||temp.equals(")")){
						conditions+=temp;
					}else if(temp.equals(" ")){
						
					}else{
						k=Integer.parseInt(temp);
						conditions+=getConditionByPlace(condition,k);
					}
				}
			}
			condition=conditions;
			if(!"".equals(tempTable)){
				tables=tables.substring(1);
				tempTable=tempTable.substring(1);
				tables+=","+PERSON_TABLE_NAME+" "+PERSON_TABLE_NAME;
				String[] tempTableArr=tempTable.split(",");
				for(int i=0;i<tempTableArr.length;i++){
					condition+=" and "+PERSON_TABLE_NAME+"."+PERSON_PKEY+"="+tempTableArr[i]+"."+PERSON_PKEY;
				}
			}else{
				tables=PERSON_TABLE_NAME+" "+PERSON_TABLE_NAME;
			}
		}
		StringBuffer sb = new StringBuffer(
			"<script> var scriptroot = '")
			.append(getRequest().getContextPath())
			.append("'</script>")
			.append("<SCRIPT src='")
			.append(getRequest().getContextPath())
			.append("/common/js/commontab/workservice.js'>")
			.append("</SCRIPT>")
			.append("<SCRIPT src='")
			.append(getRequest().getContextPath())
			.append("/common/js/commontab/service.js'>")
			.append("</SCRIPT>")
			.append("<script>")
			.append(" window.dialogArguments.submitSearch(\"").append(condition).append("\",\"").append(tables).append("\");")
			.append("window.dialogArguments.parent.otherinfo.location='")
			.append(request.getContextPath())
			.append("/fileNameRedirectAction.action?toPage=/personnel/baseperson/temp.jsp?type=statistic';window.close();</script> ");
			return renderHtml(sb.toString());
	}
	
	
	/*
	 * 
	 * Description:
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Dec 28, 2009 9:45:00 AM
	 */
	public String getConditionByPlace(String condition ,int k){
		int fromIndex=0;						//开始位置
		int endIndex=condition.indexOf("and");	//结束位置
		if(endIndex==-1){
			endIndex=condition.length();
		}
		int temp=1;
		while(temp!=k){
			fromIndex=endIndex+3;
			endIndex=condition.indexOf("and",fromIndex);
			if(endIndex==-1){
				endIndex=condition.length();
			}
			temp++;
		}
		return condition.substring(fromIndex,endIndex);
	}
	
	/*
	 * 
	 * Description:
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Dec 23, 2009 6:35:28 PM
	 */
	public String getOtherCheckFields()throws Exception{
        ToaSysmanageStructure structure=infoManager.getInfoSetByInfoSetName(infoSetValue);
        ToaSysmanageProperty field =itemManager.getInfoItem(infoItemId);
        String name = structure.getInfoSetName() + "." +field.getInfoItemSeconddisplay(); 
        String values=structure.getInfoSetValue()+"."+field.getInfoItemField();
        String text =this.getProText(length, field.getInfoItemField(),field.getInfoItemDatatype(),field.getInfoItemDictCode());
        JSONArray  array= getOperator(field.getInfoItemDatatype());
        renderText(array.toString()+"|"+name+"|"+text+"|"+values);
        return null;
	}

	/*
	 * 
	 * Description:
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Dec 23, 2009 6:52:33 PM
	 */
	public String getProText(String len,String fieldvalue,String datatype,String reference) {
		String text = "";
		if (datatype.equals(VAR) ||
		        datatype.equals(DES) ||
		        datatype.equals(NUM)) {
		    text = "<input type='text' name='infoItemValue" + len +"' id='infoItemValue" + len +
		        "' style='width:170px;'/>";
		} else if (datatype.equals(CODE)) {
		    text = "<input type='text' name='infoItemValue" + len +"' id='infoItemValue" + len +
		        "' style='width:170;' readonly><input type='button' value='...' onclick=selectBaseData('infoItemId" +
		        len + "','infoItemValue" + len + "','" +
		        reference+
		        "')><input type='hidden' name='infoItemId" + len +"' id='infoItemId" + len + "'>";
		} else if (datatype.equals(PHOTO)) {
		    text = "<input type='text' name='infoItemValue" + len +"' id='infoItemValue" + len +
		        "' style='width:170px;' disabled/>";
		} else if (datatype.equals(YEAR)) {
		    text = "<input type='text' name='infoItemValue" + len +"' id='infoItemValue" + len +
		        "' style='width:170px;' preset='YEAR'/>";
		} else if (datatype.equals(MONTH)) {
		    text = "<input type='text' name='infoItemValue" + len +"' id='infoItemValue" + len +
		        "' style='width:170px;' preset='MONTH'/>";
		} else if (datatype.equals(DATE)||datatype.equals(DATE2)) {
		    text = "<input type='text' name='infoItemValue" + len +"' id='infoItemValue" + len +
		        "' style='width:170px;' preset='DATE'/>";
		}/*else if(datatype.equals(DATE2)){
			 text = "<strong:newdate  name='infoItemValue" + len +"' id='infoItemValue" + len +
		        "' width='100%' dateform='yyyy-MM-dd'/>";							
		}*/
		return text;
	}
	
	/*
	 * 
	 * Description:
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Dec 23, 2009 6:56:20 PM
	 */
	public JSONArray getOperator(String type) {
		JSONArray proArray = new JSONArray();
		JSONObject proInfo = new JSONObject();
        proInfo.put("CHECK_OPERATOR_ID", "0");
        proInfo.put("CHECK_OPERATOR_NAME", "等于");

        JSONObject proInfo1 = new JSONObject();
        proInfo1.put("CHECK_OPERATOR_ID", "1");
        proInfo1.put("CHECK_OPERATOR_NAME", "不等于");

        JSONObject proInfo2 = new JSONObject();
        proInfo2.put("CHECK_OPERATOR_ID", "2");
        proInfo2.put("CHECK_OPERATOR_NAME", "大于");

        JSONObject proInfo3 = new JSONObject();
        proInfo3.put("CHECK_OPERATOR_ID", "3");
        proInfo3.put("CHECK_OPERATOR_NAME", "小于");

        JSONObject proInfo4 = new JSONObject();
        proInfo4.put("CHECK_OPERATOR_ID", "4");
        proInfo4.put("CHECK_OPERATOR_NAME", "大于等于");

        JSONObject proInfo5 = new JSONObject();
        proInfo5.put("CHECK_OPERATOR_ID", "5");
        proInfo5.put("CHECK_OPERATOR_NAME", "小于等于");

        JSONObject proInfo6 = new JSONObject();
        proInfo6.put("CHECK_OPERATOR_ID", "6");
        proInfo6.put("CHECK_OPERATOR_NAME", "包含");

        JSONObject proInfo7 = new JSONObject();
        proInfo7.put("CHECK_OPERATOR_ID", "7");
        proInfo7.put("CHECK_OPERATOR_NAME", "左包含");

        JSONObject proInfo8 = new JSONObject();
        proInfo8.put("CHECK_OPERATOR_ID", "8");
        proInfo8.put("CHECK_OPERATOR_NAME", "右包含");

        JSONObject proInfo9 = new JSONObject();
        proInfo9.put("CHECK_OPERATOR_ID", "9");
        proInfo9.put("CHECK_OPERATOR_NAME", "为空");

        JSONObject proInfo10 = new JSONObject();
        proInfo10.put("CHECK_OPERATOR_ID", "10");
        proInfo10.put("CHECK_OPERATOR_NAME", "不为空");

        if (type.equals(NUM) || type.equals(CODE)) {
        	proArray.add(proInfo);
        	proArray.add(proInfo1);
        	proArray.add(proInfo2);
        	proArray.add(proInfo3);
        	proArray.add(proInfo4);
        	proArray.add(proInfo5);
        	proArray.add(proInfo6);
        	proArray.add(proInfo7);
        	proArray.add(proInfo8);
        } else if (type.equals(YEAR) || type.equals(MONTH) || type.equals(DATE)||type.equals(DATE2)) {
        	proArray.add(proInfo);
            proArray.add(proInfo1);
            proArray.add(proInfo2);
            proArray.add(proInfo3);
            proArray.add(proInfo4);
            proArray.add(proInfo5);
        } else if (type.equals(VAR) || type.equals(DES)) {
        	proArray.add(proInfo);
        	proArray.add(proInfo1);
        	proArray.add(proInfo6);
        	proArray.add(proInfo7);
        	proArray.add(proInfo8);
        } else if (type.equals(PHOTO)) {
        	proArray.add(proInfo9);
        	proArray.add(proInfo10);
        }

        if(proArray.size()>0)
        	return proArray;
        else
        	return null;
    }
	
	/*
	 * 
	 * Description:
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Dec 24, 2009 11:17:55 AM
	 */
	public String QueryConditon(String name, String operator,
			String infoItemField, String infoItemValue,
			String infoItemDatatype, String reference, int conLength, int length) {
		String operatorName = "";
		String querycondition = "";
		
		if (infoItemValue.equalsIgnoreCase("") || infoItemValue.equals(null)){// 如果输入值为空情况；
			infoItemValue = "is null";
		}else {
			// 如果查询字段是引用类型，则要将值转换为引用类型的代码
			if (infoItemDatatype.equalsIgnoreCase(CODE)) {
				ToaSysmanageDictitem dictitem = dictItemManager.getDictitemByShort(infoItemValue, reference);
				infoItemValue = dictitem.getDictItemCode().toString();
			}
			if (infoItemDatatype.equalsIgnoreCase(YEAR)){// 如果信息项的类型是时间按类型；		
				infoItemField = "substr(" +infoItemField + ",0,4)";
				infoItemValue = "'" + infoItemValue + "'";
			}else if( infoItemDatatype.equalsIgnoreCase(MONTH)){
				infoItemField = "substr(" +infoItemField + ",0,7)";
				infoItemValue = "'" + infoItemValue + "'";
			}else if(infoItemDatatype.equalsIgnoreCase(DATE)){
				infoItemField = "substr(" +infoItemField + ",0,10)";
				infoItemValue = "'" + infoItemValue + "'";
			}else if(infoItemDatatype.equalsIgnoreCase(DATE2)){
				infoItemField = "to_char(" +infoItemField + ",'yyyy-MM-dd')";
				infoItemValue = "'" + infoItemValue + "'";
			}else{	
				if (operator.equalsIgnoreCase("6")|| operator.equalsIgnoreCase("7")|| operator.equalsIgnoreCase("8"))// 如果是包含类操作，值展现不变；
					;
				else// 如果不是，则把它标识为值。	
					infoItemValue = "'" + infoItemValue + "'";
			}
		}
		// 判断字段类型
		if (operator.equalsIgnoreCase("0"))// 判断操作符类型=
		{
			operatorName = "=";
			if (infoItemValue.equalsIgnoreCase("is null"))
				querycondition = infoItemField + " " + infoItemValue;
			else
				querycondition = infoItemField + operatorName + infoItemValue;
		}
		if (operator.equalsIgnoreCase("1"))// 判断操作符类型!=
		{
			operatorName = "!=";
			if (infoItemValue.equalsIgnoreCase("is null"))
				querycondition = infoItemField + " is not null";
			else
				querycondition = "("+infoItemField + operatorName + infoItemValue+" or "+infoItemField +" is null )";
		}
		if (operator.equalsIgnoreCase("2"))// 判断操作符类型>
		{
			operatorName = ">";
			if (infoItemValue.equalsIgnoreCase("is null"))
				querycondition = infoItemField + " " + infoItemValue;
			else
				querycondition = infoItemField + operatorName + infoItemValue;
		}
		if (operator.equalsIgnoreCase("3"))// 判断操作符类型<
		{
			operatorName = "<";
			if (infoItemValue.equalsIgnoreCase("is null"))
				querycondition = infoItemField + " " + infoItemValue;
			else
				querycondition = infoItemField + operatorName + infoItemValue;
		}
		if (operator.equalsIgnoreCase("4"))// 判断操作符类型>=
		{
			operatorName = ">=";
			if (infoItemValue.equalsIgnoreCase("is null"))
				querycondition = infoItemField + " " + infoItemValue;
			else
				querycondition = infoItemField + operatorName + infoItemValue;
		}
		if (operator.equalsIgnoreCase("5"))// 判断操作符类型<=
		{
			operatorName = "<=";
			if (infoItemValue.equalsIgnoreCase("is null"))
				querycondition = infoItemField + " " + infoItemValue;
			else
				querycondition = infoItemField + operatorName + infoItemValue;
		}

		if (operator.equalsIgnoreCase("6")) {// 判断操作符类型包含

			if (infoItemValue.equalsIgnoreCase("is null"))
				querycondition = infoItemField + " is not null";
			else {
				operatorName = "%" + infoItemValue + "%";
				querycondition = infoItemField + " Like" + " '" + operatorName+ "'";
			}
		}
		if (operator.equalsIgnoreCase("7")) {// 判断操作符类型左包含
			if (infoItemValue.equalsIgnoreCase("is null"))
				querycondition = infoItemField + "is not null";
			else {
				operatorName = infoItemValue + "%";
				querycondition = infoItemField + " Like" + " '" + operatorName+ "'";
			}
		}
		if (operator.equalsIgnoreCase("8")) { // 判断操作符类型右包含
			if (infoItemValue.equalsIgnoreCase("is null"))
				querycondition = infoItemField + " is not null";
			else {
				operatorName = "%" + infoItemValue;
				querycondition = infoItemField + " Like" + " '" + operatorName+ "'";
			}
		}
		if(infoItemDatatype.equals(PHOTO)){//如果为照片
			if (operator.equalsIgnoreCase("9")){// 判断操作符类型为空
				querycondition =" dbms_lob.getlength("+infoItemField+")=0 ";
			}else{
				querycondition =" dbms_lob.getlength("+infoItemField+")>0 ";
			}
		}else{
			if (operator.equalsIgnoreCase("9")){// 判断操作符类型为空
				querycondition = infoItemField + " is null";
			}
			if (operator.equalsIgnoreCase("10")){// 判断操作符类型不为空
				querycondition = infoItemField + " is not null";
			}
		}
		if (length < conLength - 1) {
			querycondition = querycondition + " and ";
			return querycondition;
		} else{
			return querycondition;
		}
	}
	
	/*
	 * 
	 * Description:
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Dec 28, 2009 8:20:04 PM
	 */
	public String getFields()throws Exception{
		JSONArray  array= getFieldsByInfoSetValue(infoSetValue);
        renderText(array.toString());
        return null;
	}
	
	/*
	 * 
	 * Description:组装成JSON数组
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Dec 28, 2009 8:22:00 PM
	 */
	public JSONArray getFieldsByInfoSetValue(String infoSetValue){
		columnList=itemManager.getCreatedItemsByValue(infoSetValue);//过滤掉照片、备注、文件类型和隐藏的字段，并且condtion为空的。
		ToaSysmanageProperty property;
		JSONArray proArray = new JSONArray();
		JSONObject proInfo;
		if(columnList!=null&&columnList.size()>0){
			for(int i=0;i<columnList.size();i++){
				property=columnList.get(i);
				proInfo = new JSONObject();
				proInfo.put("CHECK_FIELD_ID", property.getInfoItemCode());
			    proInfo.put("CHECK_FIELD_NAME", property.getInfoItemSeconddisplay());
			    proInfo.put("CHECK_FIELD_VALUE", property.getInfoItemField());
			    proArray.add(proInfo);
			}
		}else{
			proInfo = new JSONObject();
			proInfo.put("CHECK_FIELD_ID", "");
		    proInfo.put("CHECK_FIELD_NAME","");
		    proInfo.put("CHECK_FIELD_VALUE","");
		    proArray.add(proInfo);
		}
		return proArray;
	}
	

	/*
	 * 
	 * Description:
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Dec 29, 2009 1:57:27 PM
	 */
	public void query(){
		HttpServletRequest request=this.getRequest();
		ToaSysmanageStructure struct=infoManager.getInfoSetByInfoSetName(infoSetValue);// 获取实体对象
		String infosetCode = struct.getInfoSetCode();
		String reference;
		String para1 = "operator";
		String flag ="";
		String flag1="";
		String paraOperator="";
		String infoItemValue = "";
		condition="";
		targetTable="";
		String conditions="";
		String temp="";
		int k=0;
		if(bds!=null&&!"".equals(bds)){
			for (int i = 0;i < Integer.parseInt(length); i++)// 多个条件的时候
			{
				flag = Integer.toOctalString(i).toString();// 用于组合符合浏览器端参数值
				flag1 = Integer.toOctalString(i + 1).toString();
				paraOperator = para1 + flag;
				//String conName = request.getParameter( "name" + flag); 		// 获取响应的浏览器端传递参数值当作条件参数。
				String conName= request.getParameter( "values" + flag); 	
				String operator = request.getParameter(paraOperator); 	// 获取响应的浏览器端传递参数值当作条件参数。
				if (operator.equalsIgnoreCase("9")|| operator.equalsIgnoreCase("10"))
					infoItemValue = "";
				else
					infoItemValue = request.getParameter("infoItemValue" + flag1); 	//获取响应的浏览器端传递参数值当作条件参数
				
				// 分离参数中的信息集和信息项.
				int a = conName.indexOf(".");	
				String first=conName.substring(0,a);		//表名
				String second = conName.substring(a + 1);	//字段名
				if(targetTable.indexOf(first)==-1){
					targetTable+=","+first;
				}
				ToaSysmanageProperty infoItem =itemManager.getInfoItemByValue(second, infosetCode);
				String infoItemDatatype = infoItem.getInfoItemDatatype();// 选择的信息项的类型
				reference = infoItem.getInfoItemDictCode(); // 引用字典类编码	
				
				String itemCondition = this.QueryConditon(infoSetValue, operator,
						conName, infoItemValue, infoItemDatatype, reference,
						Integer.parseInt(length), i);
				condition = condition + itemCondition;
			}
			targetTable=targetTable.substring(1);	
			if(condition!=null&&!"".equals(condition)){
				for(int j=0;j<bds.length();j++){
					temp=bds.substring(j,j+1);
					if(temp.equals("*")){
						conditions+=" and ";
					}else if(temp.equals("+")){
						conditions+=" or ";
					}else if(temp.equals("(")||temp.equals(")")){
						conditions+=temp;
					}else if(temp.equals(" ")){
						
					}else{
						k=Integer.parseInt(temp);
						conditions+=getConditionByPlace(condition,k);
					}
				}
			}
			condition=conditions;
		}else{
			targetTable=infoSetValue;
			querydescript="";
		}
	}
	
	
	
	
	/*
	 * 
	 * Description:生成报表
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Jan 28, 2010 10:41:13 AM
	 */
	public String generateReport()throws Exception{
		try {
			HttpSession session = this.getSession();
			HttpServletRequest request=this.getRequest();
			HttpServletResponse response=this.getResponse();
			ToaSysmanageStructure struct=infoManager.getInfoSetByInfoSetName(infoSetValue);	//获取信息集对象
			if(struct!=null){	//信息集不为空
				JasperPrint jasperPrint=(JasperPrint) session.getAttribute("mysessionkey");
				if(jasperPrint==null){
					if(condition!=null&&!"".equals(condition.trim())){		//查询条件不为空
						condition = URLDecoder.decode(condition, "utf-8");
					}	
					columnList=manager.generateColumnList(request, struct, groupByFiled);
					reportDateList=manager.generateReportData(columnList, struct, condition, groupByFiled);
					jasperPrint=manager.getJasperPrint(request,columnList,reportDateList,struct,groupByFiled);
				}
				session.setAttribute("mysessionkey", jasperPrint);
				if(exportType!=null&&"excel".equals(exportType)){
					manager.printToExcel(jasperPrint, request, response,struct);
				}else if(exportType!=null&&"pdf".equals(exportType)){
					manager.printToPDF(jasperPrint, request, response,struct);
				}else if(exportType!=null&&"print".equals(exportType)){
					manager.printToPrinter(jasperPrint, request, response);
				}else{
					this.generateHtml(jasperPrint,request,response);
				}
			}
		}catch (Exception e) {
			logger.error(e);
			throw e;
		}
		return null;	
	}

	/*
	 * 
	 * Description:生成HTML报表
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Jan 28, 2010 4:25:18 PM
	 */
	public void generateHtml(JasperPrint jasperPrint, HttpServletRequest request,HttpServletResponse response) throws Exception{
		  StringBuffer htmlString = new StringBuffer();
		  StringBuffer sbuffer = new StringBuffer();
		  String contextPath = request.getContextPath();
		  totalNum=reportDateList!=null?reportDateList.size():totalNum;		
		  StringBuffer url=new StringBuffer(contextPath+"/historyquery/query!generateReport.action");
		  try {
			  PrintWriter out = response.getWriter();
			  JRHtmlExporter exporter = new JRHtmlExporter();
			  int pageIndex = currentPage;
			  if (pageIndex < 0) {
				  pageIndex = 0;
			  }
			  int lastPageIndex = 0;
			  if (jasperPrint.getPages() != null) {
				  lastPageIndex = jasperPrint.getPages().size()-1 ;
			  }
			  if (pageIndex > lastPageIndex) {
				  pageIndex = lastPageIndex;
			  }
			  if (lastPageIndex < 0) {
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
				.append("function submitSearch(cdtion,infoSetValue,groupByFiled){\n")
				.append("$('#condition').val(encodeURI(cdtion));\n")
				.append("$('#infoSetValue').val(infoSetValue);")
				.append("$('#groupByFiled').val(groupByFiled);")
				.append("$('#myTableForm').submit();")
				.append(" }\n")
				.append("function goPages(page){\n")
				.append("$('#currentPage').val(page);")
				.append("$('#myTableForm').submit();")
				.append(" }\n")
				.append("</script>")
				.append("<style type=\"text/css\">")
				.append("#contentborder { BORDER-RIGHT: #DBDBDB  1px solid; PADDING-RIGHT: 1px; PADDING-LEFT: 1px; BACKGROUND: white; PADDING-BOTTOM: 10px; OVERFLOW: auto; BORDER-LEFT: #DBDBDB 1px solid; WIDTH: 100%; BORDER-BOTTOM: #DBDBDB 1px solid; POSITION: absolute; HEIGHT: 85%; margin-left: 1px; }")
				.append("</style>")
				.append("</head>")
				.append("<body text=\"#000000\" link=\"#000000\" alink=\"#000000\" vlink=\"#000000\" oncontextmenu=\"return false;\">")
			    .append(" <div id=\"contentborder\" align=\"center\">");
			  htmlString.append("<form id='myTableForm'  method='get'  target='targetWindow' action='"+contextPath+"/historyquery/query!generateReport.action'>\n")
			     .append("<input type='hidden' id='condition' name='condition'/>\n")
			     .append("<input type='hidden' id='groupByFiled' name='groupByFiled' value='"+groupByFiled+"' />\n")
			     .append("<input type='hidden' id='module' name='module' value='recordQuery' />\n")
			     .append("<input type='hidden' id='infoSetValue' name='infoSetValue' value='"+infoSetValue+"'/>\n")
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
	            
	            htmlString.append(" <td width='100%'>&nbsp;&nbsp;"+(pageIndex+1)+"/"+jasperPrint.getPages().size()+"&nbsp;&nbsp;共"+totalNum+"条记录</td>\n")
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
					.append("</div></body></html>");            
	            out.println(htmlString.toString());
	            out.flush();
	            out.close();
		  } catch (Exception e) {
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
	
	
    /*
     * 
     * Description:选择列
     * param: 
     * @author 	    彭小青
     * @date 	    Feb 2, 2010 3:31:05 PM
     */
    public String rowlist() throws Exception{
    	HttpServletRequest request=this.getRequest();
		Cookie[] cookies = request.getCookies();
		Cookie sCookie = null;
		for (int i=0;cookies!=null&&i<cookies.length; i++){
			sCookie = cookies[i];
			if (sCookie.getName().startsWith(infoSetValue)&&sCookie.getValue()!= null&&sCookie.getValue().length() > 0) {
				checkedvalue=sCookie.getValue();
			}
		}
		
    	ToaSysmanageStructure struct=infoManager.getInfoSetByInfoSetName(infoSetValue);	//获取信息集对象
    	columnList=itemManager.getCreatedItemsByValue(infoSetValue);	//过滤掉照片、备注、文件类型和隐藏的字段，并且condtion为空的。
    	objName=struct.getInfoSetName();
		return "searchrow";
	}
    
    /*
     * 
     * Description:将列保存入cookie中
     * param: 
     * @author 	    彭小青
     * @date 	    Feb 3, 2010 9:09:42 AM
     */
    public String remberCloumn()throws Exception{
    	HttpServletResponse response=this.getResponse();
        Cookie cookie = new Cookie(infoSetValue+"ABCDEFG",checkedvalue);
        cookie.setMaxAge(365 * 24 * 60 * 60);
        response.addCookie(cookie);
        renderText("success");
    	return null;
    }
    
    /*
     * 
     * Description:判断表达式是否合法
     * param: 
     * @author 	    彭小青
     * @date 	    Apr 8, 2010 9:12:48 PM
     */
    public String isRegulate()throws Exception{
    	 CommonUtilWay help=new CommonUtilWay();
    	 /*List list=help.infixExpToPostExp(bds);
    	 String result=help.doEval(list);
         if(result.equals(""))   
             this.renderText("ok");
         else 
        	 this.renderText("no");*/
    	return null;
    }
    
    public String exportExcels() throws Exception {
    	List<Object> list;
    	if(docTitle!=null && !docTitle.equals("")){
		//	docTitle=URLDecoder.decode(docTitle, "utf-8");
    		docTitle=new String((docTitle).trim().getBytes("ISO8859_1"), "utf-8");
		}
	     list = manager.getObjList(reportDate,reportEndDate,docTitle);
   	 
   		this.exportfils(list);
   		return null;
   	}
    
    private void exportfils(List<Object> orglist){
		try{
			
			HttpServletResponse response=getResponse();
			
			//创建EXCEL对象
			BaseDataExportInfo export = new BaseDataExportInfo();
			String str=toUtf8String("收文登记薄");
			export.setWorkbookFileName(str);
			export.setSheetTitle("收文登记薄");
			export.setSheetName("收文登记薄");
			//描述行信息
			List<String> tableHead = new ArrayList<String>();
			tableHead.add("收文标题");
			tableHead.add("发文单位");
			tableHead.add("收文文号");
			tableHead.add("收文字号");
			tableHead.add("收文日期");
		
			
	
			export.setTableHead(tableHead);
			
			
			//获取导出信息
		    List rowList=new ArrayList();
		    Map rowhigh=new HashMap();
		    int rownum=1;
		  SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		    for (Object obj : orglist) {
				Vector cols=new Vector();
				Object[] strobj=(Object[])obj;
				cols.add(strobj[2]);//收文标题
				cols.add(strobj[3]);//发文单位
				cols.add(strobj[4]);//收文文号
				cols.add(strobj[0]);//收文字号
				Date begin=(Date)strobj[1];//收文日期
				if(begin!=null){
					cols.add(df.format(begin));//收文日期
				}else{
					cols.add(" ");//收文日期
				}
			
				rowList.add(cols);
				rownum++;
			}
			export.setRowList(rowList);
			export.setRowHigh(rowhigh);
			ProcessXSL xsl = new ProcessXSL();
		    xsl.createWorkBookSheet(export);
		    xsl.writeWorkBook(response);
			
		}catch(Exception e){

          e.printStackTrace();

      }
          
}
    
    public String poerateDjcx() throws Exception{
    	levelList = dictService.getItemsByDictValue("NUMTYPE");// 
    	return "poeratedjcx";
    }
 public String exportdjcxExcels()throws Exception {
        try{
        	
        	if(recvNum!=null && !recvNum.equals("")){
        		recvNum=new String((recvNum).trim().getBytes("ISO8859_1"), "utf-8");
				
			}
			if(workTitle!=null && !workTitle.equals("")){
				
				workTitle=new String((workTitle).trim().getBytes("ISO8859_1"), "utf-8");
			}
			if(docNumber!=null && !docNumber.equals("")){
				
				docNumber=new String((docNumber).trim().getBytes("ISO8859_1"), "utf-8");
			}
			if(departSigned!=null && !departSigned.equals("")){
			
				departSigned=new String((departSigned).trim().getBytes("ISO8859_1"), "utf-8");
			}
			if(zbcs!=null && !zbcs.equals("")){
				
				zbcs=new String((zbcs).trim().getBytes("ISO8859_1"), "utf-8");
			}
			if(csqs!=null && !csqs.equals("")){
				
				csqs=new String((csqs).trim().getBytes("ISO8859_1"), "utf-8");
			}
			
			HttpServletResponse response=getResponse();
			
			//创建EXCEL对象
			BaseDataExportInfo export = new BaseDataExportInfo();
			String str=toUtf8String("已分办文件");
			export.setWorkbookFileName(str);
			export.setSheetTitle("已分办文件");
			export.setSheetName("已分办文件");
			//描述行信息
			List<String> tableHead = new ArrayList<String>();
			tableHead.add("收文编号");
			tableHead.add("文件标题");
			tableHead.add("来文单位");
			tableHead.add("来文字号");
			tableHead.add("来文日期");
			tableHead.add("主办处室");
			tableHead.add("处室签收");
		
			
	
			export.setTableHead(tableHead);
			
			
			//获取导出信息
		    List rowList=new ArrayList();
		    Map rowhigh=new HashMap();
		    int rownum=1;
		  SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		  List<ToaReportBean> list=manager.getDJCXObjList(qszt,recvNum,workTitle,docNumber,departSigned,zbcs,csqs,recvstartTime,recvendTime,recvStartNum,recvEndNum);
		    for (ToaReportBean obj : list) {
				Vector cols=new Vector();
				
				cols.add(obj.getText1());
				cols.add(obj.getText2());
				cols.add(obj.getText3());
				cols.add(obj.getText4());
				cols.add(obj.getText5());
				cols.add(obj.getText6());
				cols.add(obj.getText7());
			
				rowList.add(cols);
				rownum++;
			}
			export.setRowList(rowList);
			export.setRowHigh(rowhigh);
			ProcessXSL xsl = new ProcessXSL();
		    xsl.createWorkBookSheet(export);
		    xsl.writeWorkBook(response);
			
		}catch(Exception e){

          e.printStackTrace();

      }
    	
    	return null;
    }
    /**
     *  把字符串转成utf8编码，保证中文文件名不会乱码
      * @param s
      * @return
      */
     public static String toUtf8String(String s){
         StringBuffer sb = new StringBuffer();
         for (int i=0;i<s.length();i++){
            char c = s.charAt(i);
            if (c >= 0 && c <= 255){sb.append(c);}
           else{
               byte[] b;
               try { b = Character.toString(c).getBytes("utf-8");}
               catch (Exception ex) {
                    System.out.println(ex);
                    b = new byte[0];
               }
               for (int j = 0; j < b.length; j++) {
                    int k = b[j];
                    if (k < 0) k += 256;
                    sb.append("%" + Integer.toHexString(k).toUpperCase());
                }
            }
        }
        return sb.toString();
    }
  	
     /**
      * 登记查询报表
      * @description	
      * @author  Jianggb
      * @return
      */
  public String getDjcxReport(){
	  try {
			if(recvNum!=null && !recvNum.equals("")){
				recvNum=URLDecoder.decode(recvNum, "utf-8");
			}
			if(workTitle!=null && !workTitle.equals("")){
				workTitle=URLDecoder.decode(workTitle, "utf-8");
			}
			if(docNumber!=null && !docNumber.equals("")){
				docNumber=URLDecoder.decode(docNumber, "utf-8");
			}
			if(departSigned!=null && !departSigned.equals("")){
				departSigned=URLDecoder.decode(departSigned, "utf-8");
			}
			if(zbcs!=null && !zbcs.equals("")){
				zbcs=URLDecoder.decode(zbcs, "utf-8");
			}
			if(csqs!=null && !csqs.equals("")){
				csqs=URLDecoder.decode(csqs, "utf-8");
			}
			if(recvStartNum!=null && !recvStartNum.equals("")){
				recvStartNum=URLDecoder.decode(recvStartNum, "utf-8");
			}
			if(recvEndNum!=null && !recvEndNum.equals("")){
				recvEndNum=URLDecoder.decode(recvEndNum, "utf-8");
			}
			SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
			List<ToaReportBean> list=manager.getDJCXObjList(qszt,recvNum,workTitle,docNumber,departSigned,zbcs,csqs,recvstartTime,recvendTime,recvStartNum,recvEndNum);
			String borrowTitle="已分办文件表";
			Map map = new HashMap();
			totalNum=list.size();//总行数
			String jasper = "/WEB-INF/jsp/historyquery/report/dengjicx.jasper"; // 你的jasper文件地址 
			HttpSession session = this.getSession();
			HttpServletRequest request=this.getRequest();
			HttpServletResponse response=this.getResponse();
			JasperPrint jasperPrint=(JasperPrint) session.getAttribute("mysessionkey");
			if(exportType==null||"".equals(exportType)){
				//读取jasper   
				JasperReport jasperReport = null;
				File exe_rpt = new File(this.getRequest().getRealPath(jasper));
				jasperReport = (JasperReport) JRLoader.loadObject(exe_rpt.getPath());
				jasperPrint=JasperFillManager.fillReport(jasperReport, map,new DataSource(list));
			}
			session.setAttribute("mysessionkey", jasperPrint);
			if(exportType!=null&&"excel".equals(exportType)){
				this.printToExcel(jasperPrint, request, response,borrowTitle);
			}else if(exportType!=null&&"pdf".equals(exportType)){
				this.printToPDF(jasperPrint, request, response,borrowTitle);
			}else if(exportType!=null&&"print".equals(exportType)){
				this.printToPrinter(jasperPrint, request, response);
			}else{
				this.generateHtmlDjcx(jasperPrint,request,response);
			}
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
  
	public void generateHtmlDjcx(JasperPrint jasperPrint, HttpServletRequest request,HttpServletResponse response) throws Exception{
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
				.append("function onsub(exportType,qszt,recvNum,workTitle,docNumber,departSigned,zbcs,csqs,recvstartTime,recvendTime,recvStartNum,recvEndNum){\n")
				.append("$('#exportType').val(exportType);\n")
				.append("$('#qszt').val(qszt);\n")
				.append("$('#recvNum').val(recvNum);\n")
				.append("$('#workTitle').val(workTitle);\n")
				.append("$('#docNumber').val(docNumber);\n")
				.append("$('#departSigned').val(departSigned);\n")
				.append("$('#zbcs').val(zbcs);\n")
				.append("$('#csqs').val(csqs);\n")
				.append("$('#recvstartTime').val(recvstartTime);\n")
				.append("$('#recvendTime').val(recvendTime);\n")
				
				.append("$('#recvStartNum').val(recvStartNum);\n")
				.append("$('#recvEndNum').val(recvEndNum);\n")
				.append("$('#currentPage').val(0);\n")
				.append("document.forms[0].action='"+contextPath+"/historyquery/query!getDjcxReport.action';")
			    .append("$('#myTableForm').submit();")
				.append(" }\n")
				.append("function goPages(page){\n")
				.append("$('#currentPage').val(page);")
				.append("$('#myTableForm').submit();")
				.append(" }\n")
				.append("</script>")
				.append("<style type=\"text/css\">")
				.append("#contentborder { BORDER-RIGHT: #DBDBDB  1px solid; PADDING-RIGHT: 1px; PADDING-LEFT: 1px; BACKGROUND: white; PADDING-BOTTOM: 10px; OVERFLOW: auto; BORDER-LEFT: #DBDBDB 1px solid; WIDTH: 100%; BORDER-BOTTOM: #DBDBDB 1px solid; POSITION: absolute; HEIGHT: 85%; margin-left: 1px; }")
				.append("</style>")
				.append("</head>")
				.append("<body text=\"#000000\" link=\"#000000\" alink=\"#000000\" vlink=\"#000000\" oncontextmenu=\"\">")
			    .append(" <div id=\"contentborder\" align=\"center\">");
			  htmlString.append("<form id='myTableForm'  method='post'  target='targetWindow' action='"+contextPath+"/historyquery/query!getDjcxReport.action'>\n")
			     .append("<input type='hidden' id='exportType' name='exportType'  value='"+exportType+"'/>\n")
			     .append("<input type='hidden' id='totalNum' name='totalNum' value='"+totalNum+"'/>\n")
			     .append("<input type='hidden' id='qszt' name='qszt' value='"+qszt+"'/>\n")
			     .append("<input type='hidden' id='recvNum' name='recvNum' value='"+recvNum+"'/>\n")
			     .append("<input type='hidden' id='workTitle' name='workTitle' value='"+workTitle+"'/>\n")
			     .append("<input type='hidden' id='docNumber' name='docNumber' value='"+docNumber+"'/>\n")
			     .append("<input type='hidden' id='departSigned' name='departSigned' value='"+departSigned+"'/>\n")
			     .append("<input type='hidden' id='zbcs' name='zbcs' value='"+zbcs+"'/>\n")
			     .append("<input type='hidden' id='csqs' name='csqs' value='"+csqs+"'/>\n")
			     .append("<input type='hidden' id='recvstartTime' name='recvstartTime' value='"+recvstartTime+"'/>\n")
			       .append("<input type='hidden' id='reportEndDate' name='reportEndDate' value='"+reportEndDate+"'/>\n")
			         .append("<input type='hidden' id='recvendTime' name='recvendTime' value='"+recvendTime+"'/>\n")
			          .append("<input type='hidden' id='recvStartNum' name='recvStartNum' value='"+recvStartNum+"'/>\n")
			     .append("<input type='hidden' id='recvEndNum' name='recvEndNum' value='"+recvEndNum+"'/>\n")
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
	 * 统计收文登记情况
	 * @return
	 */
	public String getBorrowReportSend(){
		try {
			//String userId = user.getCurrentUser().getUserId();
			//Organization org= user.getUserDepartmentByUserId(userId);
		    // docTitle=URLDecoder.decode(docTitle, "utf-8");
			if(docTitle!=null && !docTitle.equals("")){
				docTitle=URLDecoder.decode(docTitle, "utf-8");
			}
			SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
			List<ToaReportBean> list=manager.getBorrowReport(reportDate,reportEndDate,docTitle);
		//	String borrow=manager.getDateToString(df.parse(reportDate), "yyyy-MM-dd");//报表参数
		//	String endDate=manager.getDateToString(df.parse(reportEndDate), "yyyy-MM-dd");//报表参数
		//	String orgName=org.getOrgName();
			String borrowTitle="收文登记表";
			Map map = new HashMap();
		//	map.put("borrowdate", borrow);
		//	map.put("endDate", endDate);
			//map.put("orgName", orgName);
			totalNum=list.size();//总行数
			String jasper = "/WEB-INF/jsp/historyquery/report/receiveDoc.jasper"; // 你的jasper文件地址 
			
			HttpSession session = this.getSession();
			HttpServletRequest request=this.getRequest();
			HttpServletResponse response=this.getResponse();
			JasperPrint jasperPrint=(JasperPrint) session.getAttribute("mysessionkey");
			if(exportType==null||"".equals(exportType)){
				//读取jasper   
				JasperReport jasperReport = null;
				File exe_rpt = new File(this.getRequest().getRealPath(jasper));
				jasperReport = (JasperReport) JRLoader.loadObject(exe_rpt.getPath());
				jasperPrint=JasperFillManager.fillReport(jasperReport, map,new DataSource(list));
				
			}
			session.setAttribute("mysessionkey", jasperPrint);
			if(exportType!=null&&"excel".equals(exportType)){
				this.printToExcel(jasperPrint, request, response,borrowTitle);
			}else if(exportType!=null&&"pdf".equals(exportType)){
				this.printToPDF(jasperPrint, request, response,borrowTitle);
			}else if(exportType!=null&&"print".equals(exportType)){
				this.printToPrinter(jasperPrint, request, response);
			}else{
				this.generateHtmlSend(jasperPrint,request,response);
			}
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	public void generateHtmlSend(JasperPrint jasperPrint, HttpServletRequest request,HttpServletResponse response) throws Exception{
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
			  docTitle=this.docTitle;
			  htmlString
			  .append("<%@ page contentType=\"text/html; charset=UTF-8\" %>")
				.append("<html>")
				.append(
						"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">")
				.append("<head>")
				.append("<script src='"+contextPath+"/common/js/jquery/jquery-1.2.6.js' type='text/javascript'></script>\n")
				.append("<script language='javascript'>\n")
				.append("function onsub(exportType,date,endDate,docTitle){\n")
				.append("$('#exportType').val(exportType);\n")
				.append("$('#reportDate').val(date);\n")
				.append("$('#reportEndDate').val(endDate);\n")
				.append("$('#docTitle').val(docTitle);\n")
				//.append(" alert($('#docTitle').val());\n")
				.append("document.forms[0].action='"+contextPath+"/historyquery/query!getBorrowReportSend.action';")
			    .append("$('#myTableForm').submit();")
				.append(" }\n")
				.append("function goPages(page){\n")
				.append("$('#currentPage').val(page);")
				.append("$('#myTableForm').submit();")
				.append(" }\n")
				.append("</script>")
				.append("<style type=\"text/css\">")
				.append("#contentborder { BORDER-RIGHT: #DBDBDB  1px solid; PADDING-RIGHT: 1px; PADDING-LEFT: 1px; BACKGROUND: white; PADDING-BOTTOM: 10px; OVERFLOW: auto; BORDER-LEFT: #DBDBDB 1px solid; WIDTH: 100%; BORDER-BOTTOM: #DBDBDB 1px solid; POSITION: absolute; HEIGHT: 70%; margin-left: 1px; }")
				.append("</style>")
				.append("</head>")
				.append("<body text=\"#000000\" link=\"#000000\" alink=\"#000000\" vlink=\"#000000\" oncontextmenu=\"\">")
			    .append(" <div id=\"contentborder\" align=\"center\">");
			  htmlString.append("<form id='myTableForm'  method='post'  target='targetWindow' action='"+contextPath+"/historyquery/query!getBorrowReportSend.action'>\n")
			     .append("<input type='hidden' id='exportType' name='exportType'  value='"+exportType+"'/>\n")
			     .append("<input type='hidden' id='totalNum' name='totalNum' value='"+totalNum+"'/>\n")
			     .append("<input type='hidden' id='reportDate' name='reportDate' value='"+reportDate+"'/>\n")
			       .append("<input type='hidden' id='reportEndDate' name='reportEndDate' value='"+reportEndDate+"'/>\n")
			         .append("<input type='hidden' id='docTitle' name='docTitle' value='"+docTitle+"'/>\n")
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
					.append("</div></body></html>");            
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
	 * 获取机构树
	 * 
	 * @return java.lang.String
	 * @roseuid 494F58830109
	 */
	public String orgtree() throws Exception{
		orgList = amanager.getOrgList(moduletype);
		return "orgtree";
	}
	
	
	public void printToPrinter(JasperPrint jasperPrint, HttpServletRequest request,HttpServletResponse response)throws Exception{
		try{
			ServletOutputStream ouputStream = response.getOutputStream();	
		//	JasperPrintManager.printReport(jasperPrint, true);//直接打印,不用预览PDF直接打印  true为弹出打印机选择.false为直接打印
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

	@Override
	protected void prepareModel() throws Exception {

	}
	
	@Override
	public String save() throws Exception {
		return null;
	}

	public Object getModel() {
		return null;
	}

	public List<ToaSysmanageProperty> getColumnList() {
		return columnList;
	}

	@Autowired
	public void setItemManager(InfoItemManager itemManager) {
		this.itemManager = itemManager;
	}

	@Autowired
	public void setInfoManager(InfoSetManager infoManager) {
		this.infoManager = infoManager;
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

	public String getInfoSetValue() {
		return infoSetValue;
	}

	public void setInfoSetValue(String infoSetValue) {
		this.infoSetValue = infoSetValue;
	}

	public String getObjName() {
		return objName;
	}

	public void setObjName(String objName) {
		this.objName = objName;
	}

	public void setInfoItemId(String infoItemId) {
		this.infoItemId = infoItemId;
	}

	public void setLength(String length) {
		this.length = length;
	}
	
	@Autowired
	public void setDictItemManager(DictItemManager dictItemManager) {
		this.dictItemManager = dictItemManager;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getCondition() {
		return condition;
	}

	public String getBds() {
		return bds;
	}

	public void setBds(String bds) {
		this.bds = bds;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public List<ToaSysmanageStructure> getStructureList() {
		return structureList;
	}

	public void setStructureList(List<ToaSysmanageStructure> structureList) {
		this.structureList = structureList;
	}

	public String getQuerydescript() {
		return querydescript;
	}

	public void setQuerydescript(String querydescript) {
		this.querydescript = querydescript;
	}

	public String getTargetTable() {
		return targetTable;
	}

	public void setTargetTable(String targetTable) {
		this.targetTable = targetTable;
	}

	public String getFlowId() {
		return flowId;
	}

	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}

	public String getInputId() {
		return inputId;
	}

	public void setInputId(String inputId) {
		this.inputId = inputId;
	}

	public QueryAction() {
		ordermap.put("INFOTB323", "FIELD2256");
		ordermap.put("INFOTB327", "FIELD2300");
		ordermap.put("INFOTB328", "FIELD2309");
		ordermap.put("INFOTB329", "FIELD2312");
		ordermap.put("INFOTB361", "FIELD2524");
		ordermap.put("INFOTB362", "FIELD2532");
		ordermap.put("INFOTB365", "FIELD2545");
		ordermap.put("INFOTB367", "FIELD2568");
		ordermap.put("INFOTB368", "FIELD2578");
	}

	public String getGroupByFiled() {
		return groupByFiled;
	}

	public void setGroupByFiled(String groupByFiled) {
		this.groupByFiled = groupByFiled;
	}

	public List<ToaReportBean> getReportDateList() {
		return reportDateList;
	}

	public void setReportDateList(List<ToaReportBean> reportDateList) {
		this.reportDateList = reportDateList;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
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

	public String getCheckedvalue() {
		return checkedvalue;
	}

	public void setCheckedvalue(String checkedvalue) {
		this.checkedvalue = checkedvalue;
	}

	public String getDocTitle() {
		return docTitle;
	}

	public void setDocTitle(String docTitle) {
		this.docTitle = docTitle;
	}

	public String getReportDate() {
		return reportDate;
	}

	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}

	public String getReportEndDate() {
		return reportEndDate;
	}

	public void setReportEndDate(String reportEndDate) {
		this.reportEndDate = reportEndDate;
	}

	public String getCsqs() {
		return csqs;
	}

	public void setCsqs(String csqs) {
		this.csqs = csqs;
	}

	public String getDepartSigned() {
		return departSigned;
	}

	public void setDepartSigned(String departSigned) {
		this.departSigned = departSigned;
	}

	public String getDocNumber() {
		return docNumber;
	}

	public void setDocNumber(String docNumber) {
		this.docNumber = docNumber;
	}

	public String getQszt() {
		return qszt;
	}

	public void setQszt(String qszt) {
		this.qszt = qszt;
	}

	public String getRecvendTime() {
		return recvendTime;
	}

	public void setRecvendTime(String recvendTime) {
		this.recvendTime = recvendTime;
	}

	public String getRecvNum() {
		return recvNum;
	}

	public void setRecvNum(String recvNum) {
		this.recvNum = recvNum;
	}

	public String getRecvstartTime() {
		return recvstartTime;
	}

	public void setRecvstartTime(String recvstartTime) {
		this.recvstartTime = recvstartTime;
	}

	public String getWorkTitle() {
		return workTitle;
	}

	public void setWorkTitle(String workTitle) {
		this.workTitle = workTitle;
	}

	public String getZbcs() {
		return zbcs;
	}

	public void setZbcs(String zbcs) {
		this.zbcs = zbcs;
	}

	public IDictService getDictService() {
		return dictService;
	}
	@Autowired
	public void setDictService(IDictService dictService) {
		this.dictService = dictService;
	}

	public List<ToaSysmanageDictitem> getLevelList() {
		return levelList;
	}

	public void setLevelList(List<ToaSysmanageDictitem> levelList) {
		this.levelList = levelList;
	}

	public QueryManager getManager() {
		return manager;
	}

	public void setManager(QueryManager manager) {
		this.manager = manager;
	}

	public String getModuletype() {
		return moduletype;
	}

	public void setModuletype(String moduletype) {
		this.moduletype = moduletype;
	}

	public String getObjId() {
		return objId;
	}

	public void setObjId(String objId) {
		this.objId = objId;
	}

	public List getOrgList() {
		return orgList;
	}

	public void setOrgList(List orgList) {
		this.orgList = orgList;
	}

	public String getRecvEndNum() {
		return recvEndNum;
	}

	public void setRecvEndNum(String recvEndNum) {
		this.recvEndNum = recvEndNum;
	}

	public String getRecvStartNum() {
		return recvStartNum;
	}

	public void setRecvStartNum(String recvStartNum) {
		this.recvStartNum = recvStartNum;
	}

}
