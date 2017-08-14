<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/tags/c.tld" prefix="c" %>
<%@include file="/common/include/rootPath.jsp"%>
<HTML><HEAD><TITLE>请选择复制至……</TITLE>
<script type="text/javascript">
var imageRootPath='<%=path%>/common/frame';
</script>

<%@include file="/common/include/meta.jsp"%>
  	<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet"><!--右键菜单样式 -->
  	<LINK  type=text/css rel=stylesheet href="<%=frameroot%>/css/properties_windows.css" >
	<link href="<%=frameroot%>/css/JTableTree.css" rel=stylesheet>
	<LINK href="<%=path%>/uums/css/jTableTree/bar.css" rel=stylesheet>
	<LINK href="<%=path%>/uums/css/jTableTree/jtree.css" rel=stylesheet>
	<script src="<%=path %>/uums/js/JTableTree.js"></script>
	<script src="<%=path %>/uums/js/JTableTreeHelp.js"></script>
	<script type="text/javascript">
		//页面缓存的已经选中的数据项,格式为,itemCode1,itemCode2,...
		var hasSelectCode = "";
		if("${initPrivil}" != "" && "${initPrivil}" != null){
			hasSelectCode = "," + "${initPrivil}" + ",";
		}
	</script>
</HEAD>
<base target="_self"/>
<BODY  class=contentbodymargin >
<DIV id=treecontentborder>
			<table border="0" cellpadding="0" cellspacing="0" width="100%"
				height="100%"  style="vertical-align: top;">
      <tr>
        <td height="40" style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
        <table width="100%" border="0" cellspacing="0" cellpadding="00"  style="vertical-align: top;">
          <tr>
           <td>&nbsp;</td>
            <td width="35%">
            <img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
            	请选择复制至……
            </td>
            <td width="5%">&nbsp;</td>
            <td width="60%">
           <s:form id="getpostform" action="/optprivilmanage/baseOptPrivil!setCopyOper.action">
				<input type="hidden" name="copytoid" id="copytoid">
				<input type="hidden" name="userId" id="userId">
				<input type="hidden" id="orgId" name="orgId" value="${orgId}">
		  </s:form>
<table width="100%" border="0" align="right" cellpadding="0" cellspacing="0">
                 <tr>
                  <td width="*">&nbsp;</td>
                  	<td width="5"></td>
                  		<td width="50">
					<input type="button" value="确 定" class="input_bg"
										onclick="setcopy();" />
					</td>
                  	<td width="3"></td>
                  		<td width="50">
									<input type="button" value="取 消" class="input_bg"
										onclick="javascript:window.close();" />
				</td>
				<td width="3">&nbsp;</td>
                </tr>
            </table>
            </td>
          </tr>
        </table>
        </td>
      </tr>
				<tr>
					<td valign=top align="center">
						<div id="divMain" oncontextmenu="return ( mTree.OnMenuShow());"
							style="overflow :auto;"
							onclick="return ( mTree.OnClick());"
							onresizeend="">
						</div>
					</td>
				</tr>

				<tr>
					<td valign='top' height='10' colspan="2">
						<img src='/StrongUUMS/common/images/keyline.gif' height='2'
							width='100%' border=0 align='top' />
					</td>
				</tr>
				<tr>
					<td align="center">
						<table>
							<tr>
								<td>
									<input type="button" value="确 定" class="input_bg"
										onclick="setcopy();" />
								</td>
								<td>
									<input type="button" value="关 闭" class="input_bg"
										onclick="javascript:window.close();" />
								</td>
							</tr>
						</table>
					<td>
				</tr>
			</table>
		</div>
		<!--bodyEnd-->
		

	</body>
</html>

<script language="javascript">
var mTree =null ;
var mTreeData =null;
var mBuild = true;
var myObject = new Object();


mTreeData = new JTableTreeDataClass("<%=path%>/uums/images/jTableTree/",true,"B_MODULE");


mTreeData.addHead(new Array("组织机构","编号"),
             null, null,
				null ,
             null);
mTreeData.HeadCellWidthArr=new Array("240","130");

// 见样式表 bar.css
var lStyleArr = new Array("Treetaskname","Treetaskname","TreeTextCenter", "TreeTextCenter","TreeTextCenter");


window.onload = window_onload;

function window_onload()
{
	//initMenuT();
	ReShowTree();

}

function ReShowTree() {
   if(mTree == null)
   {
		//mTree =new JTableTreeClass(divMain,mTreeData);
                mTree =new JTableTreeClass('mTree',divMain,mTreeData,true,true,false,1,true);
		//mTree.SetMenu(sMenu);
   }

	mTree.RebuildTree();
	mBuild = true;
}

function shownode(aShow) {
	
}

function ClickNode() {
   if(mTree.mSelected == null) return;
}
function ClickPlusMinus()
{


}

</script>
<c:forEach items="${orgList}" var="orgList">
 
  
	<script language="JavaScript">
	var code = findFatherCode('<c:out value="${orgList.orgSyscode}" />','<c:out value="${codeType}" />');
	mTreeData.addTreeNode('<c:out value="${orgList.orgSyscode}" />',code,
	                    new Array("<c:out value='${orgList.orgName}' />","<c:out value='${orgList.orgSyscode}' />"),
	                     '', true,
	                     "", "folder_closed.gif", lStyleArr, '','2'); 
	                                                  	                     

	</script>
	 </c:forEach>
	 
	<c:forEach items="${userList}" var="userList">	
	
		<script language="JavaScript">
			
			mTreeData.addTreeNode('<c:out value="${userList.userId}" />','<c:out value="${userList.userSyscode}" />',
	                    new Array("<c:out value='${userList.userName}' />","<c:out value='${userList.userSyscode}' />"),
	                     '', true,
	                     "", "renyuan.gif", lStyleArr, "<c:out value='${userList.userId}' />",'0'); 
	                                                  	                     
	</script>
	

</c:forEach>


	<script>
		function checkSelected(id){
			return true;
		}
		function select(){
			var sNode = mTree.GetTreeNode(mTree.mSelected );
			var sid = sNode.others;
			window.dialogArguments.setUserIdName(sNode.id,sid);
			window.close();
		}
		
		var orgid ;

		function setOrgId(id){
			orgid = id;
			//alert(orgid);
		}
		
		
		function setcopy(){
			var copytoid = getAllCheckedOthers(mTree);
			var userid = '${userId}' ;
			//var copytoid = getValue();
			if(copytoid == null || copytoid == ""){
				alert("请选择要复制的人员！");
				return false;
			}
			document.getElementById("userId").value = userid;
			document.getElementById("copytoid").value = copytoid;
			//location = '<%=path%>/usermanage/usermanage!setCopy.action?userId='+userid+'&copytoid='+copytoid;
			document.getElementById("getpostform").submit();
			//window.close();
		}
	</script>
