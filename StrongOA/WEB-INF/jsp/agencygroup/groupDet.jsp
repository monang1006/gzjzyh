<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML><HEAD><TITLE>个人发文组-机构列表</TITLE>
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
<script src="<%=path%>/oa/js/agencygroup/groupDet.js" type="text/javascript"></script>
<script type="text/javascript">
	$(document).ready(function(){       	
      	//搜索
	       $("#img_search").click(function(){  
	       	$("form").submit();
	       });
      })
	//导入机构
	function importPublic(){
		//var url = "<%=root%>/agencygroup/agencyGroup!importOrgList.action";
		//var ret = window.showModalDialogOpenWindow(url,window,'help:no;status:no;scroll:auto;dialogWidth:600px; dialogHeight:400px');
		var groupId = $("#groupId").val();
		var ret=OpenWindow("<%=root%>/agencygroup/agencyGroup!importOrgList.action","600","400",window);
		//只有真正执行了导入动作才刷新当前页面
		if("yes" == ret){
			parent.document.location="<%=root%>/fileNameRedirectAction.action?toPage=agencygroup/agencyGroup-personal.jsp?groupId="+groupId ;
		}
	} 
	//删除
	function gotoDel(){
		var id = getValue();
		var groupId = $("#groupId").val();
		if(id==null|id==""){
				alert("请选择要删除的分组机构！");
          		return ;
			}else{
				//var name = $(":checked").parent().next().attr("value");
				var url = $("#hrfDel").attr("url");
				if(confirm("确定要删除选中的分组机构？")){
					$.ajax({
						type:"post",
						url:url,
						data:{detId:id},
						success:function(data){
							parent.document.location="<%=root%>/fileNameRedirectAction.action?toPage=agencygroup/agencyGroup-personal.jsp?groupId="+groupId ;
						},
						error:function(){
							alert("对不起，操作出错！");
						}
					});
				}
			}
		
	} 
</script>
</HEAD>
<BODY class=contentbodymargin  oncontextmenu="return false;" onload=initMenuT()>
<script type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
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
            <td width="5%" align="center"><img src="<%=frameroot%>/images/ico.gif" width="9" height="9"></td>
            <td><label id="l_address_groupName">${groupName }</label></td>
            <td>
            <table border="0" align="right" cellpadding="00" cellspacing="0">
                <tr>
                  <td width="70"><a class="Operation" href="javascript:gotoDel();" url="<%=root%>/agencygroup/groupDet!delete.action" id="hrfDel"><img class="img_s" src="<%=frameroot%>/images/shanchu.gif" width="15" height="15">删除</a></td>
                  <td width="5">&nbsp;</td>
                </tr>
            </table>
            </td>
          </tr>
        </table>
        </td>
      </tr>
	<s:form id="myTableForm" action="/agencygroup/groupDet.action">
		<input id="groupId" type="hidden" name="groupId" value="${groupId }"/>
		 <input id="groupName" type="hidden" name="groupName" value="${groupName }"/><!-- 用于将文件名传到后台然后传回此页面显示在<label> -->
	     <webflex:flexTable name="myTable" width="100%" height="370px" wholeCss="table1" property="0" 
	     isCanDrag="true" isCanFixUpCol="true" clickColor="#A9B2CA" 
	     footShow="showCheck" getValueType="getValueByArray" 
	     collection="${page.result}" page="${page}">
	     <table width="100%" border="0" cellpadding="0" cellspacing="1" class="table1">
		 <tr>
          <td width="40px" align="center"  class="biao_bg1"  style="cursor: hand;" id="img_search"><img  src="<%=frameroot%>/images/sousuo.gif" title="单击搜索" width="17" height="16"></td>
          <td width="18%" align="center"  class="biao_bg1"><s:textfield name="orgCode" cssClass="search" title="输入机构编号"></s:textfield></td> 
          <td width="17%" align="center" class="biao_bg1"><s:textfield name="orgName" cssClass="search" title="输入机构名称"></s:textfield></td>
 		  <td class="biao_bg1">&nbsp;</td> 
         </tr>
		 </table>
		<webflex:flexCheckBoxCol caption="选择" valuepos="0" 
			valueshowpos="3" width="4%" isCheckAll="true" isCanDrag="false"
			isCanSort="false"></webflex:flexCheckBoxCol>
		<webflex:flexTextCol caption="机构编号" valuepos="3" 
			valueshowpos="2" showsize="50"  width="19%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
		<webflex:flexTextCol caption="机构名称" valuepos="3" 
			valueshowpos="3" width="18%" showsize="50" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
		<webflex:flexTextCol caption="电话" showsize="50" valuepos="4" 
			valueshowpos="4" width="18%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
		<webflex:flexTextCol caption="地址" valuepos="5"
			valueshowpos="5" width="45%" showsize="50" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
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
		item = new MenuItem("<%=frameroot%>/images/tb-delete3.gif","删除","gotoDel",1,"ChangeWidthTable","checkOneDis");
		sMenu.addItem(item); 
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}  
</script>
</BODY></HTML>
