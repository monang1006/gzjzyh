<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<HTML><HEAD><TITLE>调查表管理</TITLE>
<%@include file="/common/include/meta.jsp" %>
<% 
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragrma","no-cache");
	response.setDateHeader("Expires",0);
%>

        	
        <link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet"><!--右键菜单样式 -->
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
		<script language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script language="javascript" src="<%=path%>/common/js/common/search.js"></script>
		<LINK type=text/css rel=stylesheet href="<%=frameroot%>/css/search.css">
		<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT><!--右键菜单脚本 -->
	   <script language="javascript" type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		
		
<SCRIPT>


function sendByAjax(url,par){  
	//ajax异步提交，三个参数分别为：处理页面，参数，返回显示结果
	$.post(url,par,
		function(data){
		}
		
	);

	return false;
}

function showCheckBox(va){		  		    
			var rv ;
			if(va == '1'){
				//rv = "<input type='checkbox' checked  />";
				rv = "不可重复";
			}else
			{
				//rv = "<input type='checkbox' />";
				rv = "可重复";
			}			
			return rv;
		}

function getdate()
{   
  var now=new Date();
  y=now.getFullYear();
  m=now.getMonth()+1;
  d=now.getDate();
  m=m<10?"0"+m:m;
  d=d<10?"0"+d:d;
  return y+"-"+m+"-"+d;
}


function showState(sta,surveyId,surveyEndTime){	
                             		    
			var rv ;
	        var NOW_DATE = getdate(); 
	        var END_DATE = surveyEndTime; 
	
	        if(NOW_DATE>END_DATE)
	        {
	             rv = "<font color='#999999'>过期</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"javascript:window.showModalDialog('<%=path%>/survey/survey!redTime.action?surveyId="+surveyId+"',window,'help:no;status:no;scroll:no;dialogWidth:210px; dialogHeight:280px');\">[重置]</a>";		
	              url="<%=path%>/survey/survey!setState.action";
	              par="surveyId="+surveyId+"&state=2";
	             sendByAjax(url,par);
	             
	        }else
	        {
	           if(sta == '0'){
				  rv = "<font color='red'>未激活</font>&nbsp;&nbsp;<a href='#' onclick='if(confirm(\"确定激活调查吗?\")){location = \"<%=path%>/survey/survey!setState.action?surveyId="+surveyId+"&state=1\";}'>[激活]</a>";
			   }
			   if(sta == '1'){
				rv = "<font color='green'>激活</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='#' onclick='if(confirm(\"确定终止调查吗?\")){location = \"<%=path%>/survey/survey!setState.action?surveyId="+surveyId+"&state=0\";}'>[终止]</a>";
			   }	
	        }		
			return rv;
}


//function showOperate(id){	
           
//			var rv ;
//		rv= "<a href='<=path%>/survey/surveyVote!input.action?surveyId="+id+"'>[设置问卷调查]</a> <a href=\"javascript:window.showModalDialog('<=path%>/survey/surveyVote!view.action?viewType=view&surveyId="+id+"',window,'help:no;status:no;scroll:yes;dialogWidth:800px; dialogHeight:600px');\">[预览]</a> <a href=\"javascript:window.showModalDialog('<%=path%>/survey/surveyVote!view.action?viewType=see&surveyId="+id+"',window,'help:no;status:no;scroll:yes;dialogWidth:800px; dialogHeight:600px');\">[查看结果]</a>";
			
				
			
//			return rv;
//		}
</SCRIPT>

</HEAD>

<BODY class=contentbodymargin oncontextmenu="return false;" onload="initMenuT()">
<DIV id=contentborder align=center>
  <table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
  <tr>
    <td height="100%">

     <s:form theme="simple" id="myTableForm" action="/survey/survey.action"  >	
		<table width="100%" border="0" cellspacing="0" cellpadding="0"  > 
		 <tr>
		   <td height="40"style="FILTER: progid : DXImageTransform . Microsoft . Gradient(gradientType = 0, startColorStr = #ededed, endColorStr = #ffffff);">
			   <table width="100%" border="0" cellspacing="0" cellpadding="00">
				 <tr>
					<td>&nbsp;</td>
					<td width="20%">
					<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
					调查列表
					</td>
					<td width="*">&nbsp;</td>
					<td >
						<a class="Operation" href="#" onclick="surveyAdd()">
						   <img src="<%=root%>/images/ico/tianjia.gif" width="15" height="15" class="img_s">
						   <span id="test" style="cursor: hand">添加&nbsp;</span>
						</a>
					</td>
					<td width="5"></td>
					<td >
						<a class="Operation" href="#" onclick="surveyEdit()">
						 <img src="<%=root%>/images/ico/bianji.gif" width="15" height="15" class="img_s">
						 <span id="test" style="cursor: hand">编辑&nbsp;</span>
						</a>
					</td>
					<td width="5"></td>
					<td >
						<a class="Operation" href="#" onclick="surveyDel()"> 
						<img src="<%=root%>/images/ico/shanchu.gif" width="15" height="15" class="img_s">
						<span id="test" style="cursor: hand">删除&nbsp;</span>
						</a>
					</td>
					<td width="5"></td>	
				
					<td >
						<a class="Operation" href="#" onclick="surveyVote()"> 
						<img src="<%=root%>/images/ico/set.gif" width="15" height="15" class="img_s">
						<span id="test" style="cursor: hand">设置调查内容&nbsp;</span>
						</a>
					</td>	
					<td width="5"></td>	
					<td >
						<a class="Operation" href="#" onclick="surveyView()"> 
						<img src="<%=root%>/images/ico/chakan.gif" width="15" height="15" class="img_s">
						<span id="test" style="cursor: hand">预览&nbsp;</span>
						</a>
					</td>	
					<td width="5"></td>	
					<td >
						<a class="Operation" href="#" onclick="surveySee()"> 
						<img src="<%=root%>/images/ico/chakan.gif" width="15" height="15" class="img_s">
						<span id="test" style="cursor: hand">查看结果&nbsp;</span>
						</a>
					</td>			
						</tr>
					</table>
				</td>
			</tr>
		</table>
	
	<webflex:flexTable name="myTable" width="100%" height="370px" wholeCss="table1" property="surveyId" isCanDrag="true" isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck" getValueType="getValueByProperty" 
      collection="${page.result}" page="${page}">   
       <input id="search" type="hidden" name="search" />
       <table width="100%" border="0" cellpadding="0" cellspacing="1" class="table1">
        <tr>
          <td width="5%" align="center"  class="biao_bg1"><img src="<%=root%>/images/ico/sousuo.gif" width="17" height="16" style="cursor: hand;" title="单击搜索" onclick="getListBySta();"></td>
          <td width="30%" align="center"  class="biao_bg1">
			<input id="surveyName" name="surveyName"
			type="text" style="width:100%" value="${surveyName}" class="search" title="请输入调查名称">
          </td>
         <td width="10%" align="center"  class="biao_bg1">
         <strong:newdate id="surveyStartTime" name="surveyStartTime"
												dateform="yyyy-MM-dd" isicon="true" width="100%"
												dateobj="${surveyStartTime}" classtyle="search" title="开始时间"/>
          </td>
         <td width="10%" align="center"  class="biao_bg1">
         <strong:newdate id="surveyEndTime" name="surveyEndTime"
												dateform="yyyy-MM-dd" isicon="true" width="100%"
												dateobj="${surveyEndTime}" classtyle="search" title="结束时间"/>
          </td>
          <td width="*%" align="center" class="biao_bg1">
          	<input name="asdf" type="text" style="width:100%" readonly="readonly" disabled="disabled" >
          </td>
      </table> 
      
		<webflex:flexCheckBoxCol caption="选择" property="surveyId" showValue="surveyName" width="5%" isCheckAll="true" isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
			    <webflex:flexTextCol caption="状态操作"  property="state" showValue="state" width="0" isCanDrag="true"  isCanSort="true"></webflex:flexTextCol>
		<webflex:flexTextCol caption="调查名称" property="surveyName" showValue="surveyName" width="30%" isCanDrag="true" isCanSort="true" showsize="15" ></webflex:flexTextCol>
		<webflex:flexDateCol caption="开始时间" property="surveyStartTime" showValue="surveyStartTime" dateFormat="yyyy-MM-dd" width="10%" isCanDrag="true" isCanSort="true"></webflex:flexDateCol>
		<webflex:flexDateCol caption="结束时间" property="surveyEndTime" showValue="surveyEndTime" dateFormat="yyyy-MM-dd" width="10%" isCanDrag="true" isCanSort="true"></webflex:flexDateCol>
		<webflex:flexTextCol caption="限制重复" property="surveyUnRepeat" showValue="javascript:showCheckBox(surveyUnRepeat)" width="8%"   isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
	    <webflex:flexTextCol caption="状态操作"  property="state" showValue="javascript:showState(state,surveyId,surveyEndTime)" width="12%" isCanDrag="true"  isCanSort="true"></webflex:flexTextCol>
	  	<webflex:flexTextCol caption="说明"  property="explain" showValue="explain" width="*%" isCanDrag="true" showsize="10" isCanSort="true"></webflex:flexTextCol>
	</webflex:flexTable>
	
	
		</s:form>

</td></tr></table>
</DIV>
<script language="javascript">
var sMenu = new Menu();
function initMenuT(){
 $("input:checkbox").parent().next().hide(); //隐藏第二列
	sMenu.registerToDoc(sMenu);
	var item = null;
	item = new MenuItem("<%=root%>/images/ico/tianjia.gif","添加","surveyAdd",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	
	item = new MenuItem("<%=root%>/images/ico/bianji.gif","编辑","surveyEdit",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	
	item = new MenuItem("<%=root%>/images/ico/shanchu.gif","删除","surveyDel",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	
	item = new MenuItem("<%=root%>/images/ico/bianji.gif","设置调查内容","surveyVote",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	
	item = new MenuItem("<%=root%>/images/ico/chakan.gif","预览","surveyView",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	
	item = new MenuItem("<%=root%>/images/ico/chakan.gif","查看结果","surveySee",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}



function surveyAdd(){//增加调查
	var audit= window.showModalDialog("<%=root%>/survey/survey!input.action",window,'help:no;status:no;scroll:no;dialogWidth:610px; dialogHeight:400px');
}
function surveyEdit(){
	var id=getValue();
	if(id == null||id == ''){
		alert('请选择要编辑的调查！');
		return;
	}
	if(id.length >32){
		alert('每次只能编辑一个调查！');
		return;
	}
	var audit = window.showModalDialog("<%=path%>/survey/survey!initEdit.action?surveyId="+id,window,'help:no;status:no;scroll:no;dialogWidth:610px; dialogHeight:400px');
}
function surveyDel(){//删除调查
	var id=getValue();
	if(id == null||id == ''){
		alert('请选择调查！');
		return;
	}
	if(confirm("确定删除调查吗?")) 
	{ 
	location = '<%=path%>/survey/survey!delete.action?surveyId='+id;
	} 	
}

function surveyVote(){//设置问卷内容
	var id=getValue();
	if(id == null||id == ''){
		alert('请选择要设置的调查！');
		return;
	}
	if(id.length >32){
		alert('每次只能设置一个调查！');
		return;
	}
	var ss=$(":checked").parent().next().attr("value");
  //  alert(ss);
   if(ss=='1'){
   		alert('该调查已经在投票当中，不允许设置了！');
   		return ;
   }else
	 location = "<%=path%>/survey/surveyVote!input.action?surveyId="+id;
}

function surveyView(){
	var id=getValue();
	if(id == null||id == ''){
		alert('请选择要预览的调查！');
		return;
	}
	if(id.length >32){
		alert('每次只能预览一个调查！');
		return;
	}
	var audit = window.showModalDialog("<%=path%>/survey/surveyVote!viewMan.action?viewType=view&surveyId="+id,window,'help:no;status:no;scroll:yes;dialogWidth:860px; dialogHeight:680px');
}
function surveySee(){
	var id=getValue();
	if(id == null||id == ''){
		alert('请选择要查看的调查结果！');
		return;
	}
	if(id.length >32){
		alert('每次只能查看一个调查结果！');
		return;
	}
	var audit = window.showModalDialog("<%=path%>/survey/surveyVote!viewMan.action?viewType=see&surveyId="+id,window,'help:no;status:no;scroll:yes;dialogWidth:860px; dialogHeight:680px');
}

function getListBySta(){	//根据属性查询

     document.getElementById("search").value="true";
	document.getElementById("myTableForm").submit();
}
</script>


</BODY></HTML>
