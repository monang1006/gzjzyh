<HTML><HEAD><TITLE>工作流挂接表单</TITLE>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@include file="/common/include/meta.jsp" %>
<link href="<%=path%>/common/css/strongitmenu.css" type="text/css" rel="stylesheet"><!--右键菜单样式 -->
<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css  rel=stylesheet>
<LINK href="<%=frameroot%>/css/search.css" type=text/css  rel=stylesheet>
<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT><!--右键菜单脚本 -->
<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
<script src="<%=path%>/common/js/validate/checkboxvalidate.js" type="text/javascript"></script>
<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
<script src="<%=path%>/common/js/common/search.js" type="text/javascript"></script>
<script type="text/javascript">
	$(document).ready(function(){
		$("#img_search").click(function(){
			$("form").submit();
		});
		init();
	});
	//初始化设置
	function init(){
		var queryFormId = '${queryFormId}';
		var viewFormId = '${viewFormId}';
		var type = '${remindType}';
		if(type == 'query'){
			if(queryFormId!='0' && queryFormId!=''){
				initCheckForm(queryFormId);
			}
		}
		if(type == 'view'){
			if(viewFormId!='' && viewFormId!='0'){
				initCheckForm(viewFormId);
			}
		}
	}
	//初始化检验是否已选择了表单
	function initCheckForm(formId){
		$("input:radio").each(function(){
			if(formId == $(this).val()){
				$(this).attr("checked",true);
			}
		});
	}
	//确定
	function doSelect(){
		var id = $("input:radio:checked").val();
		id = $.trim(id);
		if(id == ""){
			alert("请选择要挂接的表单！");
			return ;
		}else{
			var formName = $(":checked").parent().next().text();
			url="<%=path%>/relat/workForm!save.action";
			var type = '${remindType}';
	        par="formId="+id+"&formName="+encodeURI(encodeURI(formName))+"&workflowId="+${workflowId}+"&workflowName="+encodeURI(encodeURI('${workflowName}'))+"&type="+type;
	        sendByAjax(url,par);
		}
		
	}
	//取消
	function cancelSelect(){
		window.close();
	}
	function sendByAjax(url,par){  
        $.post(url,par,
	    	function(data){
	    	    if("ok"==data){
	      		alert("挂接成功");
	      		window.close();
	      		}else
	      		alert("挂接失败");
	     		
	    	}
   	 );
   }
   
     //查阅督查反馈单
		function viewInspect() {
	          var id = $("input:radio:checked").val();
			  id = $.trim(id);
	          if(id == ""){
			     alert("请选择要查阅的表单！");
		       	 return ;
		      }else{
	             var url  = "<%=root%>/work/work!seeFormSee.action?formId="+id;
	             var audit= window.showModalDialog(url,window,'help:no;status:no;scroll:no;dialogWidth:900px; dialogHeight:680px');
	          }
	   }
</script>
</HEAD>
<base target="_self"/>
<BODY class=contentbodymargin oncontextmenu="return false;" onload="initMenuT()" >
<label id="l_actionMessage" style="display: none;"><s:actionmessage/></label>
<DIV id=contentborder align=center>

  <table width="100%" border="0" cellspacing="0" cellpadding="00">
  <tr>
    <td height="100%">
    <table width="100%" border="0" cellspacing="0" cellpadding="00">
      <tr>
        <td height="40" style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
        <table width="100%" border="0" cellspacing="0" cellpadding="00">
          <tr>
            <td>&nbsp;</td>
            <td width="20%">
            <img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
            <label>表单列表</label>
            </td>
             <td width="60%">
            	<table border="0" align="right" cellpadding="00" cellspacing="0">
	                <tr>
	                	<td ><a class="Operation" href="JavaScript:viewInspect();"><img class="img_s" src="<%=root%>/images/ico/page.gif" width="15" height="15">查阅&nbsp;</a></td>
	                    <td width="5"></td>
			            <td ><a class="Operation" href="JavaScript:doSelect();"><img class="img_s" src="<%=root%>/images/ico/queding.gif" width="15" height="15">确定&nbsp;</a></td>
			            <td width="5"></td>
			            <td ><a class="Operation" href="JavaScript:cancelSelect();"><img class="img_s" src="<%=root%>/images/ico/quxiao.gif" width="15" height="15">取消&nbsp;</a></td>
			            <td width="5"></td>
			            
			        </tr>
		        </table>  
		      </td>    
          </tr>
        </table>
        </td>
      </tr>
	<s:form id="myTableForm" action="/work/work!queryFormLst.action">
		<s:hidden name="remindType"></s:hidden>
		<s:hidden name="workflowId"></s:hidden>
	     <webflex:flexTable name="myTable" width="100%" height="370px" wholeCss="table1" property="id" 
	     isCanDrag="true" isCanFixUpCol="true" clickColor="#A9B2CA" 
	     footShow="showno" getValueType="getValueByProperty" 
	     collection="${page.result}" page="${page}">
	     <table width="100%" border="0" cellpadding="0" cellspacing="1" class="table1">
		 <tr>
          <td width="4%" align="center"  class="biao_bg1"><img id="img_search" style="cursor: hand;" src="<%=root%>/images/ico/sousuo.gif" width="17" height="16"></td>
          <td width="96%" align="center" class="biao_bg1"><s:textfield name="formName" cssClass="search" title="输入表单名称"></s:textfield></td>
          <td class="biao_bg1">&nbsp;</td>
         </tr>
		 </table>
		<webflex:flexRadioCol caption="" property="id" showValue="" width="4%" isCanDrag="false"
			isCanSort="false"></webflex:flexRadioCol>	
		<webflex:flexTextCol caption="表单名称" property="title"
			showValue="title" width="96%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>	
	  </webflex:flexTable>
	</s:form>
	</table>
      </td>
  </tr>
</table>
</DIV>
<script language="javascript">
	var sMenu = new Menu();
	function initMenuT(){
		sMenu.registerToDoc(sMenu);
		var item = null;
		item = new MenuItem("<%=root%>/images/ico/page.gif","查阅","viewInspect",1,"ChangeWidthTable","checkMoreDis");
		sMenu.addItem(item);
		
		sMenu.addShowType("ChangeWidthTable");
	    registerMenu(sMenu);
	}
</script>
</BODY></HTML>
