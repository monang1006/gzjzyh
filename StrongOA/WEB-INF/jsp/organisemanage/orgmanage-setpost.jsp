<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@include file="/common/include/meta.jsp"%>

<HTML>
	<HEAD>
		<TITLE>请选择岗位</TITLE>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet"><!--右键菜单样式 -->
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
		<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT><!--右键菜单脚本 -->
</HEAD>
<base target="_self"/>
<BODY class=contentbodymargin oncontextmenu="return false;" onload="initMenuT()">
<DIV id=contentborder align=center>
<s:form id="getpostform" action="/organisemanage/orgmanage!getPost.action">
	<input type="hidden" name="postId" id="postId">
</s:form>
  <table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
  <tr>
    <td height="100%">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="40" style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
        <table width="100%" border="0" cellspacing="0" cellpadding="00">
          <tr>
            <td width="5%" align="center">
            </td>
            <td width="50%">
            <img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
      		      机构岗位列表
            </td>
            <td width="10%">&nbsp;</td>
            <td width="35%">
            <table border="0" align="right" cellpadding="0" cellspacing="0">
                <tr>
                  <td width="*">&nbsp;</td>
                  <td width="7%"><img src="<%=root%>/images/ico/queding.gif" width="15" height="15"></td>
                  <td width="20%"><a href="javascript:getPost();">确定</a></td>
                  <td width="7%"><img src="<%=root%>/images/ico/quxiao.gif" width="15" height="15" ></td>
                  <td width="20%"><a href="#">取消</a></td>
				  <td width="25">&nbsp;</td>
                </tr>
            </table>
            </td>
          </tr>
        </table>
        </td>
      </tr>
    </table>
     <webflex:flexTable name="myTable" width="100%" height="370px" wholeCss="table1" property="postId" isCanDrag="true" isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck" 
     getValueType="getValueByProperty" collection="${pageWholePost.result}" page="${pageWholePost}">
		 <table width="100%" border="0" cellpadding="0" cellspacing="1" class="table1">
        <tr>
          <td width="5%" align="center"  class="biao_bg1"><img src="<%=root%>/images/ico/sousuo.gif" width="15" height="15" ></td>
          <td width="20%" align="center"  class="biao_bg1"><input name="textfield1" type="text" style="width:100%"></td>
          <td width="60%" align="center"  class="biao_bg1"><input name="textfield2" type="text" style="width:100%"></td>
          <td width="*%" align="center" class="biao_bg1"><input name="textfield4" type="text" style="width:100%"></td>
      </table> 
		<webflex:flexCheckBoxCol caption="选择" property="postId" showValue="postName" width="5%" isCheckAll="true" isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
		<webflex:flexTextCol caption="岗位名称" property="postName" showValue="postName" width="20%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
		<webflex:flexTextCol caption="岗位编号" property="postName" showValue="postDescription" width="60%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
	</webflex:flexTable>
      </td>
  </tr>
</table>
</DIV>
<script language="javascript">
var sMenu = new Menu();
function initMenuT(){
	sMenu.registerToDoc(sMenu);
	var item = null;
	item = new MenuItem("<%=request.getContextPath()%>/common/images/tianjia.gif","增加","addPost",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=request.getContextPath()%>/common/images/bianji.gif","编辑","editPost",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=request.getContextPath()%>/common/images/shanchu.gif","删除","",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}
function getPost(){
	var postId=getValue();
	document.getElementById("postId").value=postId;
	document.getElementById("getpostform").submit();
	
}

</script>
</BODY></HTML>
