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
		<title>已拒收公文列表</title>
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
		    #contentborder{height:580px;}
		    </style>	
<script type="text/javascript">      
     
      $(document).ready(function(){       	
      	//搜索
	       $("#img_search").click(function(){
	       	$("form").submit();
	       });
      })
      
      function viewDoc() {
          var taskId = getValue();
          if(taskId == ""){
          	alert("请选择要查看的公文！");
          	return ;
          }else{
          	var taskIds = taskId.split(",");
          	if(taskIds.length>1){
          		alert("一次只能查看一份来文！");
          		return ;
          	}
          	var url = "<%=path%>/receives/recvDoc!showDoc.action?senddocId="+taskId+"&showType=view";
          	
          	var width=screen.availWidth-10;
   			var height=screen.availHeight-30;
   			var a=OpenWindow(url,width, height, window);
          }
			  
      }
       function viewDocClick(temp) {
          var taskId = temp;
          if(taskId == ""){
          	alert("请选择要查看的公文！");
          	return ;
          }else{
          	var taskIds = taskId.split(",");
          	if(taskIds.length>1){
          		alert("一次只能查看一份来文！");
          		return ;
          	}
          	var url = "<%=path%>/receives/recvDoc!showDoc.action?senddocId="+taskId+"&showType=view";
          	
          	var width=screen.availWidth-10;
   			var height=screen.availHeight-30;
   			var a=OpenWindow(url,width, height, window);
          }
			  
      }
			function OpenWindows(danwei,docRecvRemark,liyous,qita,mytitle)
			{
				var laiwenDW = encodeURI(danwei);
				var liyou2 = encodeURI(liyous);
				var docRecvRemark2=encodeURI(docRecvRemark);
				var qita2 = encodeURI(qita);
				var laiwentitle = encodeURI(mytitle);
				var urls = "<%=path%>/fileNameRedirectAction.action?toPage=sends/tuiwendan.jsp?remark="+docRecvRemark2+"&liyou="+liyou2+"&laiwenDW="+laiwenDW+"&qita="+qita2+"&laiwentitle="+laiwentitle;
				//OpenWindow(urls,window,'help:no;status:no;scroll:auto;dialogWidth:600px; dialogHeight:760px');
				showModalDialog(urls,window,"dialogWidth:600px;dialogHeight:760px;"+
											"status:no;help:no;scroll:auto;");
			}
			function showLiyou(danwei,docRecvRemark,liyous,qita,mytitle){
				//alert(mytitle);
				if(docRecvRemark == 'null')
				{
					docRecvRemark= "";
				}
				if(qita == 'null')
				{
					qita= "";
				}
				if(liyous != 'null')
				{
					
					
					return "<a href=\"#\" onclick=\"javascript:OpenWindows('"+danwei+"','"+docRecvRemark+"','"+liyous+"','"+qita+"','"+mytitle+"');\"  >查看退文单</a>";
					//alert(a);
					//return a;
				}
				
				return "<font>"+docRecvRemark+"</font>";
			}
      
</script>
	</HEAD>
	<body class="contentbodymargin"
		onload="initMenuT();"> 
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<div id="contentborder" align="center">
		<s:form name="myTableForm" theme="simple"
							action="/receives/recvDoc!rejected.action">
			<table width="100%" border="0" cellspacing="0" cellpadding="0"  
				style="vertical-align: top;">
				<tr>
					<td height="40" class="tabletitle">
						<table width="100%" border="0" align="right" cellpadding="0"
							cellspacing="0">
							<tr>
								<td width="5%" align="center">
									<img src="<%=frameroot%>/images/ico.gif" width="9" height="9"
										alt="">
								</td>
								<td width="30%">
									已拒收公文列表
								</td>
								<td width=70%>
									<table align="right"><tr><td>
										<a class="Operation" href="javascript:viewDoc();"> <img
												src="<%=frameroot%>/images/banli.gif" width="14" height="14"
												alt="查看公文" class="img_s">查看公文</a>
									</td></tr></table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td height="100%">
						<webflex:flexTable name="myTable" width="100%" height="370px"
								wholeCss="table1" property="0" isCanDrag="true"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByArray" collection="${page.result}"
								page="${page}">
							<table width="100%" border="0" cellpadding="0" cellspacing="1"
                class="table1">
                <tr>
                  <td width="40px"  align="center" class="biao_bg1"  id="img_search" style="cursor: hand;"  >
                    <img  src="<%=frameroot%>/images/sousuo.gif" width="17"
                      height="16">
                  </td>
                  <td width="18%" class="biao_bg1">
                    <s:textfield name="modelDoc.docTitle" title="请输入公文标题" cssClass="search"></s:textfield> 
                  </td>
                  <td width="15%" class="biao_bg1">
                    <s:textfield name="modelDoc.docCode" title="请输入发文文号" cssClass="search"></s:textfield> 
                  </td>
                  <td width="11%" class="biao_bg1">
                    <s:textfield name="modelDoc.docIssueDepartSigned" title="请输入发文单位" cssClass="search"></s:textfield> 
                  </td>
                  <td width="11%" align="center" class="biao_bg1">
                    <strong:newdate name="sendStartDate" id="searchDraftDate" width="98%" classtyle="search"
                      skin="whyGreen" isicon="true" dateform="yyyy-MM-dd HH:mm" title="发文开始时间" dateobj="${sendStartDate}"></strong:newdate>
                  </td>
                  <td width="11%" align="center" class="biao_bg1">
                   <strong:newdate name="sendEndDate" id="searchDraftDate" width="98%" classtyle="search"
                      skin="whyGreen" isicon="true" dateform="yyyy-MM-dd HH:mm" title="发文结束时间" dateobj="${sendEndDate}"></strong:newdate>
                  </td>
                  <td width="11%" align="center" class="biao_bg1">
                    <strong:newdate name="startDate" id="searchDraftDate" width="98%" classtyle="search"
                      skin="whyGreen" isicon="true" dateform="yyyy-MM-dd HH:mm" title="拒收开始时间" dateobj="${startDate}"></strong:newdate>
                  </td>
                  <td width="10%" align="center" class="biao_bg1">
                   <strong:newdate name="endDate" id="searchDraftDate" width="98%" classtyle="search"
                      skin="whyGreen" isicon="true" dateform="yyyy-MM-dd HH:mm" title="拒收结束时间" dateobj="${endDate}"></strong:newdate>
                  </td  class="biao_bg1" >
                  <td >  
                  &nbsp;
                  </td>
                </tr>
              </table>
	              <webflex:flexCheckBoxCol caption="选择" valuepos="11" valueshowpos="1"
	                width="3%" isCheckAll="true"
	                isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
	              <webflex:flexTextCol caption="公文标题" valuepos="11" valueshowpos="1" isCanDrag="true"
	                width="19%" isCanSort="true" showsize="23" onclick="viewDocClick(this.value)"></webflex:flexTextCol>  
	              <webflex:flexTextCol caption="来文文号" valuepos="0" valueshowpos="0" isCanDrag="true"
	                width="15%" isCanSort="true" showsize="15"></webflex:flexTextCol> 
	              <webflex:flexTextCol caption="发文单位" valuepos="2" valueshowpos="2" isCanDrag="true"
	                width="12%" isCanSort="true" showsize="10"></webflex:flexTextCol>
	              <webflex:flexDateCol caption="发文时间" valuepos="3" valueshowpos="3"
	                width="12%" isCanDrag="true" dateFormat="yyyy-MM-dd HH:mm" showsize="30" isCanSort="true"></webflex:flexDateCol>
	              <webflex:flexTextCol caption="拒收原因" valuepos="5" valueshowpos="javascript:showLiyou(2,5,12,13,1)" isCanDrag="true"
	                width="10%" isCanSort="true" showsize="10"></webflex:flexTextCol> 
	              <webflex:flexDateCol caption="拒收时间" valuepos="4" valueshowpos="4"
	                width="12%" isCanDrag="true" dateFormat="yyyy-MM-dd HH:mm" showsize="30" isCanSort="true"></webflex:flexDateCol> 
	              <webflex:flexTextCol caption="操作IP" valuepos="7" valueshowpos="7" isCanDrag="true"
	                width="10%" isCanSort="true" showsize="15"></webflex:flexTextCol>
                <webflex:flexTextCol caption="收文编号" valuepos="11" valueshowpos="15" isCanDrag="true"
	                width="10%" isCanSort="true" showsize="15"></webflex:flexTextCol>
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
        registerMenu(sMenu);
         var item = null;
	        item = new MenuItem("<%=frameroot%>/images/banli.gif","查看公文","viewDoc",1,"ChangeWidthTable","checkMoreDis");
	        sMenu.addItem(item);
	     sMenu.addShowType("ChangeWidthTable");
        registerMenu(sMenu);
      }
    </script>
	</body>
</HTML>
