<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML><HEAD><TITLE>系统通讯录-用户列表</TITLE>
<%@include file="/common/include/meta.jsp" %>
<link href="<%=path%>/common/css/strongitmenu.css" type="text/css" rel="stylesheet"><!--右键菜单样式 -->
<LINK href="<%=frameroot%>/css/properties_windows_list.css" type=text/css  rel=stylesheet>
<LINK href="<%=frameroot%>/css/search.css" type=text/css  rel=stylesheet>
<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT><!--右键菜单脚本 -->
<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
<%--<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>--%>
<script src="<%=path%>/common/js/common/search.js" type="text/javascript"></script>
<script type="text/javascript">
	$(document).ready(function(){
		//搜索
		$("#img_sousuo").click(function(){
			$("form").submit();
		});
	});
	var hasDoImport = false;//标示是否执行过导入动作
	//选择组|直接导入到个人通讯录
	function importPublic(){
		var id = getValue();
		if(id == ""){
			showTip('<div class="tip" id="loading">请选择要导入的人员。</div>');
			return ;
		}
		/**
			因为此功能只是针对发送邮件,因此这里考虑直接传递【用户姓名、电话、邮件、手机号码等信息】
			1、降低编程难度；
			2、提高性能：无需每次根据用户ID去获取用户基本信息。
			3、因为系统通讯录中的信息是通过统一用户和OA的个人信息表组合而成。因此降低了复杂度。
		*/
		var user = getSelectInfo(id);
		//选择组之前,先判断是否已经设置默认的存放组
		$.post("<%=root%>/address/addressGroup!getGroupDefault.action",
			   function(data){
			   		if(data == "null"){
			   			var ret=OpenWindow("<%=root%>/address/addressGroup!tree.action?groupId=${groupId}","300","250",window);
						if(ret!=null && ret!="" && ret!=undefined){
							doImport(ret,user);
						}
			   		}else if(data == "error"){
			   			showTip('<div class="tip" id="loading">会话过程失败啦。</div>');
			   		}else{
			   			var groupId = data;
			   			doImport(groupId,user);
			   		}	
			   });
		
	}
	// 验证是否有重复用户名
	function doImport(groupId,users){
		$.post("<%=root%>/address/addressGroup!checkUserName.action",
			   {groupId:groupId,users:users},
			   function(data){
			   		if(data.length>0){
			   			if(confirm("您选择的分组已存在以下人员姓名：\n\n    "+data+"\n\n点击『确定』忽略该提示，继续添加。\n点击『取消』取消本次添加操作。\n ")){
							doImportUser(groupId,users);
			   			}
			   		}else if(data == "error"){
			   			alert("系统出错。");
			   		}else{
			   			doImportUser(groupId,users);
			   		}
			   });
	
	}
	//执行导入系统人员到指定的个人通讯录组
	function doImportUser(groupId,users){
		$.post("<%=root%>/address/addressGroup!doImport.action",
			   {groupId:groupId,users:users},
			   function(data){
			   		if(data == "success"){
			   			showTip('<div class="tip" id="loading">导入成功。</div>');
			   			hasDoImport = true;
			   		}else if(data == "error"){
			   			showTip('<div class="tip" id="loading">导入过程出现错误。</div>');
			   		}
			   });
	}
	//获取选择的人员【返回JSON格式:[{\"name\":\"dengzc\",\"age\":123},{\"name\":\"andy\",\"age\":1111}]】
	function getSelectInfo(id){
		var userIds = id.split(",");
		var info = "[";
		for(var i=0;i<userIds.length;i++){
			info += "{";
			<s:iterator value="page.result">
				var currentId = '${userId}';
				if(userIds[i] == currentId){
					info += "userName:\""+'${userName}'+"\",";//姓名
					info += "userTel:\""+'${userTel}'+"\",";//电话
					info += "userPhone:\""+'${rest2}'+"\",";//手机
					info += "userEmail:\""+'${userEmail}'+"\",";//Email
					info += "userId:\""+'${userId}'+"\"";//userId
				}
			</s:iterator>
			info += "}";
			if(i<userIds.length-1){
				info += ",";
			}
		}
		info += "]";
		return info;
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
	function showAll(){
		parent.project_work_content.location = "<%=root%>/address/addressOrg!importOrgUserList.action";
	}
</script>
</HEAD>
<BODY class=contentbodymargin oncontextmenu="return false;" onload="initMenuT()" onunload="closeImport();">
<script type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
<DIV id=contentborder align=center>
  <table width="100%" border="0" cellspacing="0" cellpadding="00">
  <tr>
   	<td colspan="3" class="table_headtd">
    <table width="100%" border="0" cellspacing="0" cellpadding="00">
    	<tr>
			<td height="8px;"></td>
		</tr>
      <tr>
      	<td class="table_headtd_img" >
			<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
		</td>
		<td align="left">
			<strong>${orgName }</strong>
		</td>
		<td align="right">
			<table border="0" align="right" cellpadding="00" cellspacing="0">
	            <tr>
	            	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
                 	<td class="Operation_list" onclick="showAll();"><img src="<%=root%>/images/operationbtn/view.png"/>&nbsp;显&nbsp;示&nbsp;所&nbsp;有&nbsp;用&nbsp;户&nbsp;</td>
                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
                 		<td width="5"></td>
                 		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
                 	<td class="Operation_list" onclick="importPublic();"><img src="<%=root%>/images/operationbtn/daoru.png"/>&nbsp;导&nbsp;入&nbsp;</td>
                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
                 		<td width="5"></td>
          </tr>
        </table>
        </td>
      </tr>
      <tr>
			<td height="8px;"></td>
		</tr>
	<s:form id="myTableForm" action="/address/addressOrg!importOrgUserList.action">
		 <input id="orgName" type="hidden" name="orgName" value="${orgName }"/><!-- 用于将文件名传到后台然后传回此页面显示在<label> -->
		 <s:hidden name="orgId"></s:hidden>
	     <webflex:flexTable name="myTable" width="100%" height="370px" wholeCss="table1" property="fileId" 
	     isCanDrag="true" isCanFixUpCol="true" clickColor="#A9B2CA" 
	     footShow="showCheck" getValueType="getValueByProperty" 
	     collection="${page.result}" page="${page}">
	     <table width="100%" border="0" cellpadding="0" cellspacing="0" class="table1_search">
		 <tr>
		 <td>
       		&nbsp;&nbsp;姓名：&nbsp;<input name="userName" id="userName" type="text" class="search" title="请您输入姓名" value="${model.userName }" />
       		&nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo" type="button" />
  		</td>
         </tr>
		 </table>
		<webflex:flexCheckBoxCol caption="选择" property="userId" 
			showValue="userName" width="4%" isCheckAll="true" isCanDrag="false"
			isCanSort="false"></webflex:flexCheckBoxCol>
		<webflex:flexTextCol caption="姓名" property="userName" 
			showValue="userName" showsize="50"  width="20%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
		<webflex:flexNumricCol caption="电话" property="userTel" 
			showValue="userTel" width="18%" showsize="12" isCanDrag="true" isCanSort="true"></webflex:flexNumricCol>
		<webflex:flexNumricCol caption="手机号码" property="rest2" 
			showValue="rest2" showsize="13" width="18%" isCanDrag="true" isCanSort="true"></webflex:flexNumricCol>	
		<webflex:flexTextCol caption="Email" property="userEmail"
			showValue="userEmail" width="45%" showsize="50" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
	  </webflex:flexTable>
	</s:form>
	</table>
      </td>
  </tr>
</table>
</DIV>
<script type="text/javascript">
	var sMenu = new Menu();
	function initMenuT(){
		sMenu.registerToDoc(sMenu);
		sMenu.addShowType("ChangeWidthTable");
	    registerMenu(sMenu);
	}
</script>
</BODY></HTML>
