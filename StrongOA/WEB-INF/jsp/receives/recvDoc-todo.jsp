<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>待办待收公文列表</title>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<LINK href="<%=frameroot%>/css/search.css" type=text/css
			rel=stylesheet>
		<script language='javascript'
			src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/search.js"
			type="text/javascript"></script>
		<SCRIPT LANGUAGE="JavaScript"
			src="<%=path%>/common/js/commontab/service.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript"
			src="<%=path%>/common/js/commontab/workservice.js"></SCRIPT>
		<style media="screen" type="text/css">
    .tabletitle {
      FILTER:progid:DXImageTransform.Microsoft.Gradient(
                            gradientType = 0, 
                            startColorStr = #ededed, 
                            endColorStr = #ffffff);
    }
    
    .hand {
      cursor:pointer;
    }
    </style>	
		<script type="text/javascript">      
      function receiveDoc() {
          var taskId = getValue();
          if(taskId == ""){
          	alert("请选择要接收的公文！");
          	return ;
          }else{
          	var taskIds = taskId.split(",");
          	if(taskIds.length>1){
          		alert("一次只能接收一份来文！");
          		return ;
          	}  
          	url = "<%=path%>/receives/recvDoc!showDoc.action?senddocId="+taskId+"&showType=todo";
          	//alert(url);
          	var width=screen.availWidth-10;
   			var height=screen.availHeight-30;
   			var a=OpenWindow(url,width, height, window);
			if(a=="true"){
				//window.location.reload();
				document.getElementById("myTableForm").submit();
			}
          }
           
			  
      }  
      function getinfo(taskId){
        
            url = "<%=path%>/receives/recvDoc!showDoc.action?senddocId="+taskId+"&showType=todo";
          	//alert(url);
          	var width=screen.availWidth-10;
   			var height=screen.availHeight-30;
   			var a=OpenWindow(url,width, height, window);
   			if(a=="true"){
				//window.location.reload();
				document.getElementById("myTableForm").submit();
			}
      }
      function submitForm(){
			document.getElementById("myTableForm").submit();
		}
</script>
	</HEAD>
	<body class="contentbodymargin" oncontextmenu="return false;"
		onload="initMenuT();">
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<div id="contentborder" align="center">
		<s:form name="myTableForm" theme="simple"
							action="/receives/recvDoc!todo.action">
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="40" class="tabletitle">
						<table width="100%" border="0" align="right" cellpadding="0"
							cellspacing="0">
							<tr>
								<td>
								</td>
								<td width="30%">
								<img src="<%=frameroot%>/images/ico.gif" width="9" height="9"
										alt="">
									待收公文列表
								</td>
								<td>
									&nbsp;
								</td>
								 <td width="70%"><table align="right"><tr>
								<td>
									<a class="Operation" href="javascript:receiveDoc();"> <img
											src="<%=frameroot%>/images/banli.gif" width="14" height="14"
											alt="查看接收" class="img_s">查看接收</a>  
								</td>
								</tr></table></td>
								<td width="5"></td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td height="100%">
						
							<webflex:flexTable name="myTable" width="100%" height="200px"
								wholeCss="table1" property="0" isCanDrag="true"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByArray"
								collection="${page.result}" page="${page}">
								<table width="100%" border="0" cellpadding="0" cellspacing="1"
                class="table1" style="table-layout:fixed" >
                <tr>
                  <td width="40px" align="center" class="biao_bg1" id="img_search" style="cursor: hand;" onclick="submitForm();" >
                    <img src="<%=frameroot%>/images/sousuo.gif" width="17"
                      height="16" >
                  </td>
                  <s:if test="#request.rest3=='001'">
                  <td width="31%" class="biao_bg1">
                    <s:textfield name="modelDoc.docTitle" title="请输入公文标题" cssClass="search"></s:textfield> 
                  </td>
                  <td width="12%" class="biao_bg1">
                  	<s:textfield name="modelDoc.docSubmittoDepart" title="请输入主送单位" cssClass="search"></s:textfield> 
                  </td>
                  </s:if>
                  <s:else>
                  <td width="36%" class="biao_bg1">
                    <s:textfield name="modelDoc.docTitle" title="请输入公文标题" cssClass="search"></s:textfield> 
                  </td>
                  </s:else>
                  <td width="16%" class="biao_bg1">
                    <s:textfield name="modelDoc.docCode" title="请输入发文文号" cssClass="search"></s:textfield> 
                  </td>
                  <td width="12%" class="biao_bg1">
                    <s:textfield name="modelDoc.docIssueDepartSigned" title="请输入单位" cssClass="search"></s:textfield> 
                  </td>
					<td width="9%" align="center" class="biao_bg1">
						<s:select name="modelDoc.docEmergency"  onchange="submitForm()" list="emergencyMap" listKey="dictItemName"
							listValue="dictItemName" headerValue="紧急程度" headerKey=""
							style="width:100%" 
							title="紧急程度" />
					</td>
                  <td width="8%" align="center" class="biao_bg1">
                    <strong:newdate name="sendStartDate" id="searchDraftDate" width="98%" classtyle="search"
                      skin="whyGreen" isicon="true" dateform="yyyy-MM-dd" title="请输入发文开始时间" dateobj="${sendStartDate}"></strong:newdate>
                  </td>
                  <td width="8%" align="center" class="biao_bg1">
                   <strong:newdate name="sendEndDate" id="searchDraftDate" width="98%" classtyle="search"
                      skin="whyGreen" isicon="true" dateform="yyyy-MM-dd" title="请输入发文结束时间" dateobj="${sendEndDate}"></strong:newdate>
                  </td>
                  <td class="biao_bg1" width="10px" >
                  &nbsp;
                  </td>
                </tr>
              </table>
	              <webflex:flexCheckBoxCol caption="选择" valuepos="11" valueshowpos="0"
	                width="3%" isCheckAll="true"
	                isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
	              <s:if test="#request.rest3=='001'">
	              <webflex:flexTextCol caption="公文标题" valuepos="11" valueshowpos="1" isCanDrag="true"
	                width="32%" isCanSort="true" onclick="getinfo(this.value)"></webflex:flexTextCol> 
	              <webflex:flexTextCol caption="主送单位" valuepos="11" valueshowpos="14" isCanDrag="true"
	                width="13%" isCanSort="true"></webflex:flexTextCol> 
	              </s:if>
	              <s:else>
	              <webflex:flexTextCol caption="公文标题" valuepos="11" valueshowpos="1" isCanDrag="true"
	                width="45%" isCanSort="true" onclick="getinfo(this.value)"></webflex:flexTextCol> 
	              </s:else> 
	              <webflex:flexTextCol caption="发文文号" valuepos="0" valueshowpos="0" isCanDrag="true"
	                width="16%" isCanSort="true" showsize="15"></webflex:flexTextCol> 
	              <webflex:flexTextCol caption="发文单位" valuepos="2" valueshowpos="2" isCanDrag="true"
	                width="12%" isCanSort="true" showsize="10"></webflex:flexTextCol>
	                <webflex:flexTextCol caption="紧急程度" valuepos="9" valueshowpos="9" isCanDrag="true"
	                width="9%" isCanSort="true" showsize="10"></webflex:flexTextCol>
	              <webflex:flexDateCol caption="发文时间" valuepos="3" valueshowpos="3"
	                width="18%" isCanDrag="true" dateFormat="yyyy-MM-dd HH:mm" showsize="30" isCanSort="true"></webflex:flexDateCol>
	            </webflex:flexTable>
						
					</td>
				</tr>
			</table>
			</s:form>
		</div>
		<script language="javascript">
      var sMenu = new Menu();
      function initMenuT(){
        sMenu.registerToDoc(sMenu);
        var item = null;
	        item = new MenuItem("<%=frameroot%>/images/banli.gif","查看接收","receiveDoc",1,"ChangeWidthTable","checkMoreDis");
	        sMenu.addItem(item);
        sMenu.addShowType("ChangeWidthTable");
        registerMenu(sMenu);
      }
    </script>
	</body>
</HTML>
