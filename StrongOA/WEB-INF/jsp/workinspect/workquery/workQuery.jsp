<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>操作内容</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<LINK type=text/css rel=stylesheet
			href="<%=path%>/common/css/gd.css">
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js"
			type="text/javascript"></script>
		<SCRIPT type="text/javascript">
		  	//统计方法
		  	$(document).ready(function(){ 
			  	$("#yj").change(function(){
		        	var value = $(this).val();
		        	if(value != "0"){
		        		$("#worktaskUnitName").val(value);
		        	}else{
		        		$("#worktaskUnitName").val("");
		        	}
	       		 });
       			}
   			 );
   		</SCRIPT>
	</HEAD>
	<body class="contentbodymargin">
	<script language="javascript" type="text/javascript"
			src="<%=path%>/common/script/My97DatePicker/WdatePicker.js"></script>
			<div class="gd_name">
				<s:if test="reportType==\"0\"">
				<div class="gd_name_left">我的办理件查询</div>
				</s:if>
				<s:else>
				<div class="gd_name_left">我的发送件查询</div>
				</s:else>
			  <br style="clear:both;"/>
			</div>
		<DIV id=contentborder align=center style="OVERFLOW: hidden;">
		<s:form id="myTableForm"
				action="/workinspect/workquery/workQuery!getBorrowReport.action"
				target="SearchContent1">
				<input type="hidden" id="exportType" name=exportType />
				<input type="hidden" id="reportType" name=reportType value="${reportType }"/>
					<input type="hidden" id="reporttongji" name="reporttingji"/>
				<input type="hidden" id="currentPage" name=currentPage />
				<table width="100%" height="25" class="gd_fslist">
							<s:if test="reportType==\"0\"">
								<tr>
								    <td width="120" height="43" align="right">任务标题：</td>
								    <td>
								      	<input id="worktaskTitle" name="worktaskTitle"
											value="${worktaskTitle}"  type="text" class="input_big" />
								    </td>
								    <td width="120" align="right">发起单位：</td>
								    <td>
								    	<select id="yj"  class="select_big">
										<option value="0">&lt;选择科室&gt;</option>
										<s:iterator value="orgList">
											<option value="${orgName }">${orgName }</option>
										</s:iterator>
										</select>
										<input type="hidden" id="worktaskUnitName" name="worktaskUnitName"></td>
								  </tr>
								  <tr>
								    <td width="120" height="43" align="right">任务编号：</td>
								    <td><input id="worktaskNo" name="worktaskNo"
										value="${worktaskNo}" type="text" class="input_big" /></td>
								    <td width="120" align="right">任务状态：</td>
								    <td>
								    	<s:select name="managetState" id="managetState" cssClass="select_big" 
											list="#{'':'全部','0':'待签收','1':'办理中','2':'已办结'}" listKey="key"
											listValue="value" /></td>
								  </tr>
								  <tr>
								    <td width="120" height="43" align="right">发送人：</td>
								    <td><input id="worktaskUser" name="worktaskUser"
										 value="${worktaskUser}" type="text" class="input_big" /></td>
								    <td width="120" align="right">紧急程度：</td>
								    <td><s:select name="worktaskEmerlevel" id="worktaskEmerlevel" cssClass="select_big" 
										list="#{'':'全部','2':'紧急','1':'快速','0':'普通'}" listKey="key"
										listValue="value" />
									</td>
								  </tr>
								  <tr>
								    <td width="120" height="43" align="right">办理时限：</td>
								    <td>
									<strong:newdate id="worktaskEtime" width="265px;" classtyle="input_md"
											name="worktaskEtime" dateform="yyyy-MM-dd" isicon="true"
											 title="办理时限" />
									</td>
								    <td width="120" align="right">发送时间：</td>
								    <td>
									<strong:newdate id="startTime" width="108px;" name="startTime" classtyle="input_md"
										dateform="yyyy-MM-dd" isicon="true" dateobj="<%=new Date() %>"
										title="开始时间" /> 至<strong:newdate id="endTime" width="108px;" name="endTime" classtyle="input_md"
										dateform="yyyy-MM-dd" isicon="true" dateobj="<%=new Date() %>"
										title="结束日期" />
									</td>
								  </tr>
								</s:if>
								<s:else>
								<tr>
									<td width="120" height="43" align="right">任务标题：</td>
									    <td>
									      	<input id="worktaskTitle" name="worktaskTitle"
												value="${worktaskTitle}"  type="text" class="input_big" />
									    </td>
									    <td width="120" align="right">任务状态：</td>
										<td>
								    		<s:select name="managetState" id="managetState" cssClass="select_big" 
											list="#{'':'全部','0':'待签收','1':'办理中','2':'已办结'}" listKey="key"
											listValue="value"/>
										</td>
								</tr>
								</tr>
								<tr>
									<td width="120" height="43" align="right">任务编号：</td>
									    <td><input id="worktaskNo" name="worktaskNo"
											value="${worktaskNo}" type="text" class="input_big" /></td>
									<td width="120" align="right">紧急程度：</td>
									 <td><s:select name="worktaskEmerlevel" id="worktaskEmerlevel" cssClass="select_big" 
											list="#{'':'全部','2':'紧急','1':'快速','0':'普通'}" listKey="key"
											listValue="value"/>
									 </td>
								</tr>
								<tr>
								    <td width="120" height="43" align="right">办理时限：</td>
								    <td>
									<strong:newdate id="worktaskEtime" width="265px;" classtyle="input_md"
											name="worktaskEtime" dateform="yyyy-MM-dd" isicon="true"
											title="办理时限" />
									</td>
								    <td width="120" align="right">发送时间：</td>
								    <td>
									<strong:newdate id="startTime" width="108px;" name="startTime" classtyle="input_md"
										dateform="yyyy-MM-dd" isicon="true" dateobj="<%=new Date() %>"
										title="开始时间" /> 至<strong:newdate id="endTime" width="108px;" name="endTime" classtyle="input_md"
										dateform="yyyy-MM-dd" isicon="true" dateobj="<%=new Date() %>"
										title="结束日期" />
									</td>
								</tr>
							</s:else>
						</table>
				</s:form>
				<div class="gd_search_icon">
				<input id="column" name="column"  type="button" class="input_cx" value=" " onclick="onsub()"/>
				<input id="column2" name="column2" type="button" class="input_excle" value=""
											onclick="exportReport('excel')">
											
		      <input id="column3" name="column3" type="button"
											class="input_pdf" value="" onclick="exportReport('pdf')">
				</div>
			<hr>
			<iframe id='SearchContent1' style="display: " name='SearchContent1'
				src='<%=root%>/fileNameRedirectAction.action?toPage=workinspect/workquery/reportpage.jsp'
				frameborder=0 scrolling='auto' width='100%' height='60%'></iframe>
			<hr>
		</div>
		<script type="text/javascript">
		//转换时间格式(yyyy-MM-dd )--->(yyyyMMddHH)
        function date2string(stime){
        	var arrsDate1=stime.split('-');
        	stime=arrsDate1[0]+""+arrsDate1[1]+""+arrsDate1[2];
        	//var arrsDate2=stime.split(' ');
       // 	stime=arrsDate2[0]+""+arrsDate2[1];
    //    	var arrsDate3=stime.split(':');
     //   	stime=arrsDate3[0]+""+arrsDate3[1]+""+arrsDate3[2];
        	return stime;
        }
		function onsub() {
				 if($("#startTime").val()==""){
					   alert("请选发送计开始日期！");
					   return;
				   }
				     
				    if($("#endTime").val()==""){
					   alert("请选择发送结束日期！");
					   return;
				   }
				   if(date2string($("#startTime").val())>date2string($("#endTime").val())){
					 alert("开始日期不能比结束日期晚！");
					$("#startTime").focus();
					 return false;
				   }
				   $("#reporttongji").val("1");
				document.getElementById("exportType").value="";
     			document.getElementById("currentPage").value = "0";
	     		document.getElementById("myTableForm").submit();
		}
	function goPages(page) {
		$('#currentPage').val(page);
		document.getElementById("myTableForm").submit();
	}
	//导出报表
	function exportReport(exportType){
	    if($("#reporttongji").val()!="1"){
	       alert("报表没有数据!");
	       return ;
	    }
		document.getElementById("exportType").value=exportType;
		document.getElementById("myTableForm").submit();
	}	
</script>
	</body>
</HTML>
