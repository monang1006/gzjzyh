<HTML><HEAD><TITLE>电子表单列表</TITLE>
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
		//搜索功能
		$("#img_search").click(function(){
			$("form").submit();
		});
		//初始化
		initCheck();
		$("input:checkbox").click(function(){
			if($(this).attr("checked")){
				if($(this).attr("name") == "checkall"){
					addAll();
				}else{
					parent.window.addOpt($(this).val(),$(this).val());
				}
			}else{
				if($(this).attr("name") == "checkall"){
					removeAll();
				}else{
					parent.window.removeOpt($(this).val());
				}
			}
		});
	});

	function addAll(){
		$("input:checkbox[name!='checkall']").each(function(){
			parent.window.addOpt($(this).val(),$(this).val());
		});
	}

	function removeAll(){
		$("input:checkbox[name!='checkall']").each(function(){
			parent.window.removeOpt($(this).val());
		});
	}

	//初始化已经选择的表单
	function initCheck(){
		var checkedId = '${id}';//得到已经选择的表单id.多个以逗号隔开.
		var checkedIds = checkedId.split(",");
		var objSelect = parent.document.getElementById("sel_accept");
		if(objSelect.options.length == 0){
			parent.window.initSelect(checkedIds);
		}
		var len = objSelect.options.length;
		if(len > 0){
			$("input:checkbox").each(function(){
				var formId = $(this).val();
				if(checkInArray(formId,objSelect)){
					$(this).attr("checked",true);
				}
			});
		}
	}

	

	//校验某个字符是否在数组中	
	function checkInArray(str,objSelect){
		var exist = false;
		for(var i=0;i<objSelect.options.length;i++){
			if(str == objSelect.options[i].value){
				exist = true;
				break;
			}
		}
		return exist;
	}
	
	//确定
	function doSelect(){
		var id = getSelect();//得到已选择的表单id,多个以逗号隔开；id1,id2,id3...
		var checkedId = '${id}';//得到已经选择的表单id.多个以逗号隔开.
		if(checkedId == ""){
			if(id == ""){
				//showTip('<div class="tip" id="loading">请选择要设置的表单！</div>');
				alert("请选择要设置的表单！");
				return ;
			}
		}
		
		$.post("<%=root%>/common/eform/eForm!setFormForOrganization.action",
			   {orgId:'${orgId}',id:id},
			   function(ret){
			   	if(ret == "0"){
			   		alert("保存成功！");
			   		window.close();
			   	}else{
			   		alert("保存失败！");
					return ;
			   	}
			   });
		
		
	}
	function getSelect(){
		var objSelect = parent.document.getElementById("sel_accept");
		var info = "";
		if(objSelect.options.length>0){
			for(var i=0;i<objSelect.options.length;i++){
				info += objSelect.options[i].value + ",";
			}
		}
		if(info.length > 0){
			info = info.substring(0,info.length - 1);
		}
		return info;
	}
	
	//取消
	function cancelSelect(){
		window.close();
	}
</script>
</HEAD>
<base target="_self"/>
<BODY class=contentbodymargin oncontextmenu="return false;" onload=initMenuT()>
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
            <td width="30%">
            <img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
            <label>表单列表</label>
            </td>
            <td>&nbsp;</td>
            <td width="70%">
            	<table border="0" align="right" cellpadding="00" cellspacing="0">
                <tr>
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
	<s:form id="myTableForm" action="/common/eform/eForm.action">
		 <s:hidden name="orgId"></s:hidden>
	     <webflex:flexTable name="myTable" width="100%" height="370px" wholeCss="table1" property="id" 
	     isCanDrag="true" isCanFixUpCol="true" clickColor="#A9B2CA" 
	     footShow="showCheck" getValueType="getValueByProperty" 
	     collection="${page.result}" page="${page}">
	     <table width="100%" border="0" cellpadding="0" cellspacing="1" class="table1">
		 <tr>
          <td width="4%" align="center"  class="biao_bg1"><img id="img_search" style="cursor: hand;" src="<%=root%>/images/ico/sousuo.gif" width="17" height="16"></td>
          <td width="96%" align="center" class="biao_bg1"><s:textfield name="eform.title" cssClass="search" title="输入表单名称"></s:textfield></td>
          </td>
          <td class="biao_bg1">&nbsp;</td>
         </tr>
		 </table>
		<webflex:flexCheckBoxCol caption="选择" property="id" 
			showValue="title" width="4%" isCheckAll="true" isCanDrag="false"
			isCanSort="false"></webflex:flexCheckBoxCol>
		<webflex:flexTextCol caption="表单名称" showsize="30" property="title"
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
		
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}
</script>
</BODY></HTML>
