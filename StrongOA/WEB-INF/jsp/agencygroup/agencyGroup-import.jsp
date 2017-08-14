<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML><HEAD><TITLE>机构列表</TITLE>
<%@include file="/common/include/meta.jsp" %>
<link href="<%=path%>/common/css/strongitmenu.css" type="text/css" rel="stylesheet"><!--右键菜单样式 -->
<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css  rel=stylesheet>
<LINK href="<%=frameroot%>/css/search.css" type=text/css  rel=stylesheet>
<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT><!--右键菜单脚本 -->
<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
<script src="<%=path%>/common/js/common/search.js" type="text/javascript"></script>
<script type="text/javascript">
	$(document).ready(function(){
		//搜索
		$("#img_search").click(function(){
			$("form").submit();
		});
	});
	var hasDoImport = false;//标示是否执行过导入动作
	//选择组|直接导入到个人通讯录
	function importPublic(){
		var id = getValue();
		if(id == ""){
			showTip('<div class="tip" id="loading">请选择要导入的机构！</div>');
			return ;
		}
	//获取选择的人员【返回JSON格式:[{\"name\":\"dengzc\",\"age\":123},{\"name\":\"andy\",\"age\":1111}]】

		var orgIds = id.split(",");
		var orgs = "[";
		for(var i=0;i<orgIds.length;i++){
			//orgs += "{orgId:\""+orgIds[i]+"\"}";
			orgs += "{";
			<s:iterator value="page.result" var="obj">
				var currentId = '${obj[0]}'; 
				if(orgIds[i] == currentId){
					orgs += "orgId:\""+'${obj[0]}'+"\",";//
					orgs += "orgName:\""+'${obj[2]}'+"\",";//
				}
			</s:iterator>
			orgs += "}";
			if(i<orgIds.length-1){
				orgs += ",";
			}
		}
		orgs += "]"; 
		id = '${groupId}'; 
		doImport(id,orgs);
	}
	// 验证是否有重复
	function doImport(groupId,orgs){
		$.post("<%=root%>/agencygroup/agencyGroup!checkUserName.action",
			   {groupId:groupId,orgs:orgs},
			   function(data){
			   		if(data.length>0){//不导入重复机构
			   			if(confirm("您选择的分组已存在以下机构名称：\n\n    "+data+"\n\n点击『确定』忽略重复项，继续添加。\n点击『取消』取消本次添加操作。\n ")){
							doImportOrg(groupId,orgs);
			   			}
			   		}else if(data == "error"){
			   			alert("系统出错");
			   		}else{
			   			doImportOrg(groupId,orgs);
			   		}
			   });
	
	}
	//执行导入组
	function doImportOrg(groupId,orgs){
		$.post("<%=root%>/agencygroup/agencyGroup!doImport.action",
			   {groupId:groupId,orgs:orgs},
			   function(data){
			   		if(data == "success"){
			   			showTip('<div class="tip" id="loading">导入成功！</div>');
			   			hasDoImport = true;
			   			//window.close();
			   		}else if(data == "error"){
			   			showTip('<div class="tip" id="loading">导入过程出现错误！</div>');
			   		}
			   });
	}
	
	//刷新父窗口
	function refreshParentWin(){
		var parentWin = window.dialogArguments;
		var parentParentWin = parentWin.parent;//父窗口的父窗口【address-personal.jsp】
		parentParentWin.document.location.reload();
	}
	//关闭窗口【回传是否执行了导入动作的标示】
	function closeImport(){
		var ret = "no";
		if(hasDoImport){
			ret = "yes";
		}
		window.returnValue = ret;
	} 
</script>
<base >
</HEAD>
<base target="_self">

<BODY class=contentbodymargin oncontextmenu="return false;" onunload="closeImport();">
<script type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
<DIV id=contentborder align=center>
  <table width="100%" border="0" cellspacing="0" cellpadding="00">
  <tr>
    <td height="100%">
    <table width="100%" border="0" cellspacing="0" cellpadding="00">
      <tr>
        <td height="40" style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
        <table width="100%" border="0" cellspacing="0" cellpadding="00">
          <tr>
            <td width="5%" align="center"><img src="<%=frameroot%>/images/ico.gif" width="9" height="9"></td>
            <td>机构信息</td> 
            <td width="5">&nbsp;</td>	
            <td width="70">
            	<a class="Operation" href="#" onclick="JavaScript:importPublic();"><img class="img_s" src="<%=frameroot%>/images/tijiao.gif" width="15" height="15">添加</a>
            </td>
            <td width="5">&nbsp;</td>	
          </tr>
        </table>
        </td>
      </tr>
	<s:form id="myTableForm" action="/agencygroup/agencyGroup!importOrgList.action">
		 <input id="groupId" type="hidden" name="groupId" value="${groupId }"/><!-- 用于将文件名传到后台然后传回此页面显示在<label> -->
		 <s:hidden name="orgId"></s:hidden>
	     <webflex:flexTable name="myTable" width="100%" height="370px" wholeCss="table1" property="0" 
	     isCanDrag="true" isCanFixUpCol="true" clickColor="#A9B2CA" 
	     footShow="showCheck" getValueType="getValueByArray" 
	     collection="${page.result}" page="${page}">
	     <table width="100%" border="0" cellpadding="0" cellspacing="1" class="table1">
		 <tr>
          <td width="4%" align="center"  class="biao_bg1"><img style="cursor: hand;" id="img_search" src="<%=frameroot%>/images/sousuo.gif" title="单击搜索" width="17" height="16"></td>
          <td width="20%" align="center"  class="biao_bg1"><s:textfield name="orgCode" cssClass="search" title="输入机构编号"></s:textfield></td> 
          <td width="18%" align="center" class="biao_bg1"><s:textfield name="orgName" cssClass="search" title="输入机构名称"></s:textfield></td>
          <td class="biao_bg1">&nbsp;</td> 
         </tr>
		 </table>
		<webflex:flexCheckBoxCol caption="选择" valuepos="0" 
			valueshowpos="2" width="4%" isCheckAll="true" isCanDrag="false"
			isCanSort="false"></webflex:flexCheckBoxCol>
		<webflex:flexTextCol caption="机构编号" valuepos="1" 
			valueshowpos="1" showsize="50"  width="20%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
		<webflex:flexTextCol caption="机构名称" valuepos="2" 
			valueshowpos="2" width="18%" showsize="50" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
		<webflex:flexTextCol caption="电话" showsize="50" valuepos="3" 
			valueshowpos="3" width="18%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
		<webflex:flexTextCol caption="地址" valuepos="4"
			valueshowpos="4" width="45%" showsize="50" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
	  </webflex:flexTable>
	</s:form>
	</table>
      </td>
  </tr>
</table>
</DIV> 
</BODY></HTML>
