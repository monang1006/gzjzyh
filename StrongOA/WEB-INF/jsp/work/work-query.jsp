<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@taglib uri="/tags/web-newdate" prefix="strong"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/tags/c.tld" prefix="c" %>
<%--<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">--%>
<html>
	<head>
		
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>工作查询</title>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<script language='javascript'
			src='<%=path%>/common/js/jquery/jquery-1.2.6.js'></script>
		<script language="javascript"
			src="<%=path%>/common/js/common/common.js"></script>
					<script src="<%=path%>/common/js/common/btn.js" type="text/javascript"></script>
	<script src="<%=path%>/oa/js/prsnfldr/prsnfldr.js" type="text/javascript"></script>
		
<script type="text/javascript">

 //转换时间格式(yyyy-MM-dd HH:mm)--->(yyyyMMddHHmm)
         function date2string(stime){
         	var arrsDate1=stime.split('-');
         	stime=arrsDate1[0]+""+arrsDate1[1]+""+arrsDate1[2];
         	var arrsDate2=stime.split(' ');
         	stime=arrsDate2[0]+""+arrsDate2[1];
         	var arrsDate3=stime.split(':');
         	stime=arrsDate3[0]+""+arrsDate3[1]+""+arrsDate3[2];
         	return stime;
         }
         
   function set_user()
{
  if(document.form1.flow_query_type.value==3)
    SET_USER.style.display="inline";
  else
    SET_USER.style.display="none";
}
         
function check_form(){
	
	
 if(document.form1.flow_query_type.value==3 && document.form1.userId.value=="")
  {
    alert("请指定人员！");
    return false;
  }
  var t_id=document.form1.userId.value;
   if(t_id.length>32){
		alert('指定发起人查询只能选择一个人');
		return;
	}	
	
    var stime=document.getElementById("prcs_date1").value;
    var etime=document.getElementById("prcs_date2").value;
  
    if(stime!='' && etime!==''){
    if(date2string(stime)>date2string(etime)){
			alert("开始时间不能比结束时间晚！");
		return;
	}
	}
    document.form1.action="<%=path %>/work/work!querycontent.action";
	document.form1.submit();
	
}
		
function empty_date()
{
	 document.form1.prcs_date1.value="";
	 document.form1.prcs_date2.value="";
}
	//选择人员
	function selectUser(){
		var ret=OpenWindow("<%=root%>/address/addressOrg!tree.action","600","400",window);
	}
</script>	 
	</head>
	<body class=contentbodymargin oncontextmenu="return false;">
	 <script type="text/javascript"
      src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
		<form name="form1" action="/work/work!querycontent.action" method="POST" target="frame_query">
			<table width="100%" height="10%" border="0" cellpadding="0"
					cellspacing="1" align="center" class="table1">
					<tr>
						<td width="8%" height="21" class="biao_bg1" align="right">
							<span class="wz">流程：</span>
						</td>
						<td class="td1" align="left">
	<select name="flow_id" id="flow_id" style="width:120px">
	<c:if test="${workflowType=='null'}"> 
	   <option value="">所有流程类型</option> 
   </c:if> 
    
    <c:forEach items="${typeList}" var="type">    
    	 <option value="<c:out value="${type}"/>"><c:out value="${type}"/></option>
     </c:forEach>  
    </select>
			</td>
			<td width="8%" height="21" class="biao_bg1" align="right">
							<span class="wz">状态：</span>
						</td>
						<td class="td1" align="left">
     <select name="flow_status" style="width:120px">
      <option value="0">所有状态</option>
      <option value="1">正在执行</option>
      <option value="2">已经结束</option>
    </select>
						</td>
						<td width="8%" height="21" class="biao_bg1" align="right">
							<span class="wz">范围：</span>
						</td>
						<td class="td1" align="left">
      <select name="flow_query_type"onchange="set_user();" style="width:123px">
      <option value="0">所有范围</option>
      <option value="1">我经办的</option>
      <option value="2">我发起的</option>
      <option value="3" >指定发起人</option>
    </select>
						</td>
						
						<td class="td1" colspan="2" align="left">
			 <div id="SET_USER" style="display:none">
         <input type="text" id="orgusername" name="userName" style="vertical-align: top;width:98px" readonly>
		<input type="hidden" name="userId" id="orguserid" value="<s:property value='userId'/>"></input>
		<input type="button" class="input_bg" onclick="JavaScript:selectUser();" value="选 择"/>
    </div>
						</td>
					</tr>
					<tr>
						<td width="12%" height="21" class="biao_bg1" align="right">
							<span class="wz">开始时间≥：</span>
						</td>
						<td class="td1" align="left">
 <strong:newdate name="prcs_date1" id="prcs_date1" 
                      skin="whyGreen" isicon="true" dateobj="${prcs_date1}" dateform="yyyy-MM-dd" width="120"></strong:newdate>
						</td>
						<td width="12%" height="21" class="biao_bg1" align="right">
							<span class="wz">结束时间≤：</span>
						</td>
						<td class="td1" align="left">
 <strong:newdate name="prcs_date2" id="prcs_date2" 
                      skin="whyGreen" isicon="true" dateobj="${prcs_date2}" dateform="yyyy-MM-dd" width="120" ></strong:newdate> 
                    <%--   <a href="javascript:empty_date()">清空</a>--%>
						</td>
						<td width="8%" height="21" class="biao_bg1" align="right">
							<span class="wz">名称：</span>
						</td>
						<td class="td1"  align="left">
   <input type="text" name="run_name" size="20">
						</td>
					
						<td class="td1" colspan="2" align="left">
<input id="queryBtn" type="button" onclick="check_form();" value="开始查询">  
 <input type="button" onClick="JavaScript:location='<%=root%>/work/work!advancedSearch.action?workflowType='+${workflowType };" title="工作流高级查询,允许设定复杂查询条件" value="高级查询">
						</td>
					</tr>
								
				</table>

 

<div id="pagebar" style="margin-top:0px"></div>
  <div id="content">
	<iframe id="blank" name="frame_query" width="100%" src="<%=path %>/fileNameRedirectAction.action?toPage=/work/work-blank.jsp" height="85%" frameborder="no" border="0" marginwidth="0" marginheight="0"  scrolling="no"></iframe>
</div>
		</DIV>
 </form>
	</body>
</html>
