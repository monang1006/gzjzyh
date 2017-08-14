<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="com.strongit.oa.util.TempPo"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@include file="/common/include/rootPath.jsp"%>

<html>
  <head>  
    <title>公文登记</title>
	<%@include file="/common/include/meta.jsp"%>
	<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet"><!--右键菜单样式 -->
	<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
	<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
			<script language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
	<script language="javascript" src="<%=path%>/common/js/common/search.js"></script>
	<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
	<LINK type=text/css rel=stylesheet href="<%=frameroot%>/css/search.css">
	<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT><!--右键菜单脚本 -->
	<script language="JavaScript" src="<%=path%>/common/js/commontab/service.js"></script>
	<script language="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></script>
	<script type="text/javascript">
		$().ready(
			function(){
				$("#searchSecret").val("${requestScope.searchSecret}");
				$("#img_sousuo").click(function(){
					$("#type").val("search");
					$("#searchDocTitle").val(encodeURI($("#searchDocT").val()));
					$("#searchSend").val(encodeURI($("#searchS").val()));
					$("#searchRoom").val(encodeURI($("#searchR").val()));
					$("#searchDocCode").val(encodeURI($("#searchDocC").val()));
					$("#searchSecret").val(encodeURI($("#ss").val()));
					$("form").submit();
				});
				$("#ss").change(function(){
					$("#type").val("search");
					$("#searchDocTitle").val(encodeURI($("#searchDocT").val()));
					$("#searchSend").val(encodeURI($("#searchS").val()));
					$("#searchRoom").val(encodeURI($("#searchR").val()));
					$("#searchDocCode").val(encodeURI($("#searchDocC").val()));
					$("#searchSecret").val(encodeURI($("#ss").val()));
					$("form").submit();
				});
			}
		);
		
//		function getListBySta(){
//			document.getElementById("type").value = "search";
//			document.getElementById("myTableForm").submit();
//		}
		
		function departscelet(){			
 				var objId = "tempfileDepartment";
 				var objName = "tempfileDepartmentName";
 				var URLStr = "<%=path %>"+"/senddocRegistSta/sendDocRegistSta!orgtree.action?objId="+objId+"&objName="+objName+"&moduletype=pige";
	 			var sty="dialogWidth:550px;dialogHeight:280px;help:no;status:no;scroll:no";
				showModalDialog(URLStr, window, sty);
				$(".searchinputdiv").eq(2).focus();
 		}
 		
 		function getOrgInfo(orgid,orgName){
			//alert("orgid"+orgid+"orgName:"+orgName);
			document.getElementById("searchR").value = orgName;
		}
		
		function clearOrgInfo(){
			document.getElementById("searchR").value = "";
		}
	</script>
  </head> 
  	<base target="_self"/>
  <body oncontextmenu="return false;" onload=initMenuT()>
<DIV id=contentborder align=center>
  <s:form theme="simple" id="myTableForm" action="/senddocRegistSta/sendDocRegistSta!wjtjList.action" method="get">
  <script type="text/javascript" src="<%=path %>/common/js/newdate/WdatePicker.js"></script>
  <input type="hidden" id="type" name="type" value="${type}">
  <input type="hidden" id="searchDocTitle" name="searchDocTitle" value="${searchDocTitle}">
  <input type="hidden" id="searchSend" name="searchSend" value="${searchSend}">
  <input type="hidden" id="searchRoom" name="searchRoom" value="${searchRoom}">
  <input type="hidden" id="searchDocCode" name="searchDocCode" value="${searchDocCode}">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
    	<webflex:flexTable name="myTable" width="100%" wholeCss="table1" property="registId" isCanDrag="true" isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck" getValueType="getValueByProperty" collection="${page.result}" page="${page}">
    		<table width="100%" border="0" cellpadding="0" cellspacing="1" class="table1">
	        	<tr>
		          <td width="5%" align="center"  class="biao_bg1">
		          	<img id="img_sousuo" src="<%=root%>/images/ico/sousuo.gif" width="15" height="15" style="cursor: hand;" title="单击搜索">
		          </td>
		          <td width="15%" align="center"  class="biao_bg1">
		          	<input id="searchDocT" name="searchDocT" type="text" style="width:100%" value="${searchDocTitle}" maxlength="150" class="search" title="请输入公文标题">
		          </td>
		          <td width="10%" align="center"  class="biao_bg1">
		          	<input id="searchS" name="searchS" type="text" style="width:100%" value="${searchSend}" maxlength="25" class="search" title="签发">
		          </td>
		          <td width="10%" align="center"  class="biao_bg1" onclick="departscelet();">
		          	<input id="searchR" name="searchR" type="text" style="width:100%" value="${searchRoom}" class="search" title="处室">
		          </td>
		          <td width="13%" align="center"  class="biao_bg1">
		          	<input id="searchDocC" name="searchDocC" type="text" style="width:100%" value="${searchDocCode}" maxlength="25" class="search" title="公文编号">
		          </td>	          
		          <td width="7%" align="center"  class="biao_bg1">
		          	<select id="ss" name="ss" cssStyle="width:100%">
								<option value="">全部</option>
						<%
							List<TempPo> mmdjList = (List)request.getAttribute("MMDJ");
							String ss = (String)request.getAttribute("searchSecret");
							for(TempPo mmdj : mmdjList){
								if(ss!=null && ss.equals(mmdj.getName())){
								%>
								<option value="<%=mmdj.getName() %>" selected="selected"><%=mmdj.getName() %></option>
								<%
								}else{
								%>
								<option value="<%=mmdj.getName() %>"><%=mmdj.getName() %></option>
								<%
								}
							}
						%>				
					</select>
					<input id="searchSecret" name="searchSecret" type="hidden" value="${searchDocCode}">
		          </td>
		          <td width="10%" align="center"  class="biao_bg1">
		          	<strong:newdate name="searchStaTime" id="searchStaTime" classtyle="search" title="收办开始时间" dateform="yyyy-MM-dd" dateobj="${searchStaTime}"
							width="100px" skin="whyGreen" isicon="true"></strong:newdate>
					 </td>
		          <td width="10%" align="center"  class="biao_bg1">
		          <strong:newdate name="searchEndTime" id="searchEndTime" classtyle="search" title="收办结束时间" dateform="yyyy-MM-dd" dateobj="${searchEndTime}"
							width="100px" skin="whyGreen" isicon="true"></strong:newdate>
		          </td>
		          <td width="10%" align="center"  class="biao_bg1">
		          <strong:newdate name="searchSendStaTime" id="searchSendStaTime" classtyle="search" title="发文开始时间" dateform="yyyy-MM-dd" dateobj="${searchSendStaTime}"
							width="100px" skin="whyGreen" isicon="true"></strong:newdate>
					 </td>
		          <td width="10%" align="center"  class="biao_bg1">
		          <strong:newdate name="searchSendEndTime" id="searchSendEndTime" classtyle="search" title="发文结束时间" dateform="yyyy-MM-dd" dateobj="${searchSendEndTime}"
							width="100px" skin="whyGreen" isicon="true"></strong:newdate>
		          </td>
          		  <td width="*%" align="center" class="biao_bg1">&nbsp;</td>
          		</tr>
		    </table>
    		<webflex:flexCheckBoxCol caption="选择" property="registId" showValue="registId" width="5%" isCheckAll="true" isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
			<webflex:flexTextCol caption="公文标题" property="docTitle" showValue="docTitle" width="25%" isCanDrag="true" isCanSort="false" showsize="12"></webflex:flexTextCol>
			<webflex:flexTextCol caption="签发" property="send" showValue="send" width="10%" isCanDrag="true" isCanSort="false" showsize="4"></webflex:flexTextCol>
			<webflex:flexTextCol caption="处室" property="toRoomName" showValue="toRoomName" width="10%" isCanDrag="true" isCanSort="false" showsize="4"></webflex:flexTextCol>	    	
	    	<webflex:flexTextCol caption="公文编号" property="docCode" showValue="docCode" width="23%" isCanDrag="true" isCanSort="false" showsize="8"></webflex:flexTextCol>
	    	<webflex:flexTextCol caption="密级" property="secret" showValue="secret" width="7%" isCanDrag="true" isCanSort="false" showsize="3"></webflex:flexTextCol>
	    	<webflex:flexDateCol caption="收办时间" property="receiveTime" showValue="receiveTime" showsize="20" dateFormat="yyyy-MM-dd" width="10%" isCanDrag="true" isCanSort="false"></webflex:flexDateCol>
	    	<webflex:flexDateCol caption="发文日期" property="sendTime" showValue="sendTime" showsize="20" dateFormat="yyyy-MM-dd" width="10%" isCanDrag="true" isCanSort="false"></webflex:flexDateCol>
    	</webflex:flexTable>
    </table>
    <script language="javascript">
			var sMenu = new Menu();
			function initMenuT(){
				sMenu.registerToDoc(sMenu);
				var item = null;
				sMenu.addShowType("ChangeWidthTable");
			    registerMenu(sMenu);
			}
		</script>
  </s:form>
  </DIV>
  </body>
</html>
