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
		<title>已收公文列表</title>
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
      function docTurnDraft() {
          var taskId = getValue();
          if(taskId == ""){
          	alert("请选择要操作的公文！");
          	return ;
          }else{
          
          /**
          	*modify by luosy
          	*修改后台接口允许操作多条记录
          	*
          	var taskIds = taskId.split(",");
          	if(taskIds.length>1){
          		alert("一次只能操作一份来文！");
          		return ;
          	}
          */
          	if(confirm("确定转入来文草稿？")){
          		var url = "<%=path%>/receives/recvDoc!docTurnDraftHandle.action?senddocId="+taskId;
	          	$.post(url,function(date){
	          		if(date=="true"){
	          			alert("转入成功！");
	          			window.opener.location.reload();
	          			window.location.reload();
	          		}else if(date.indexOf("false")>-1){
							date.replace("false", "");
	          				alert("个别或所有附件已丢失，但基本数据已完成转移成功。具体信息请询问管理人员！\n\n"+date);
	          				window.opener.location.reload();
	          				window.close();
					}else if(date=="1"){
	          				alert("个别或所有附件已丢失，但基本数据已完成转移成功。具体信息请询问管理人员！");
	          				window.opener.location.reload();
	          				window.close();
	          			}else{
	          				alert("操作失败，请询问管理人员！");
	          				window.opener.location.reload();
	          				window.close();
	          			}
	          	});
          	}
          }
			  
      }
      function showTurnDraft(){
    	  	var url = "<%=path%>/receives/recvDoc!docTurnDrafts.action";
			var width=screen.availWidth-10;
			var height=screen.availHeight-30;
			var a=OpenWin(url,width, height, window);
      }
      function getinfo(id){
           	var url = "<%=path%>/receives/recvDoc!showDoc.action?senddocId="+id+"&showType=view2";
			var width=screen.availWidth-10;
			var height=screen.availHeight-30;
			var a=OpenWindow(url,width, height, window);
      }
       function submitForm(){
			document.getElementById("myTableForm").submit();
		}
       function show(value){
    	   if(value=="1"){
    		   return "已转入";
    	   }else{
    		   return "未转入";
    	   }
       }
</script>
	</HEAD>
	<body class="contentbodymargin" oncontextmenu="return false;"
		onload="initMenuT();">
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<div id="contentborder" align="center">
		<s:form name="myTableForm" theme="simple"
							action="/receives/recvDoc!docTurnDraft.action">
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
										alt="">&nbsp;
									已收公文列表（公文传输）
								</td>
								<td>
									&nbsp;
								</td>
								<td width="70%"><table align="right"><tr>
									<td>
										<a class="Operation" href="javascript:showTurnDraft();"> <img
												src="<%=frameroot%>/images/chakan.gif" width="14" height="14"
												alt="查看已转入列表" class="img_s">查看已转入列表</a>
									</td>
									<td width="5">
										&nbsp;
									</td>
									<td>
										<a class="Operation" href="javascript:docTurnDraft();"> <img
												src="<%=frameroot%>/images/tianjia.gif" width="14" height="14"
												alt="转移到来文草稿" class="img_s">转移到来文草稿</a>
									</td>
									<td width="5">
										&nbsp;
									</td>
								</tr></table></td>
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
                class="table1">
                <tr>
                  <td width="40px" align="center" class="biao_bg1"  id="img_search" style="cursor: hand;"   onclick="submitForm();" >
                    <img id="img_search" style="cursor: hand;"   onclick="submitForm();" src="<%=frameroot%>/images/sousuo.gif" width="17"
                      height="16">
                  </td>
                  <td width="26%" class="biao_bg1">
                    <s:textfield name="modelDoc.docTitle" title="公文标题" cssClass="search"></s:textfield> 
                  </td>
                  <td width="14%" class="biao_bg1">
                    <s:textfield name="modelDoc.docCode" title="发文文号" cssClass="search"></s:textfield> 
                  </td>
                  <td width="13%" class="biao_bg1">
                    <s:textfield name="modelDoc.docIssueDepartSigned" title="发文单位" cssClass="search"></s:textfield> 
                  </td>
                  <td width="6%" align="center" class="biao_bg1">
                    <strong:newdate name="sendStartDate" id="searchDraftDate" width="98%" classtyle="search"
                      skin="whyGreen" isicon="true" dateform="yyyy-MM-dd" title="开始时间" dateobj="${sendStartDate}"></strong:newdate>
                  </td>
                  <td width="6%" align="center" class="biao_bg1">
                   <strong:newdate name="sendEndDate" id="searchDraftDate" width="98%" classtyle="search"
                      skin="whyGreen" isicon="true" dateform="yyyy-MM-dd" title="结束时间" dateobj="${sendEndDate}"></strong:newdate>
                  </td>
                  <td width="6%" align="center" class="biao_bg1">
                    <strong:newdate name="startDate" id="searchDraftDate" width="98%" classtyle="search"
                      skin="whyGreen" isicon="true" dateform="yyyy-MM-dd" title="开始时间" dateobj="${startDate}"></strong:newdate>
                  </td>
                  <td width="6%" align="center" class="biao_bg1">
                   <strong:newdate name="endDate" id="searchDraftDate" width="98%" classtyle="search"
                      skin="whyGreen" isicon="true" dateform="yyyy-MM-dd" title="结束时间" dateobj="${endDate}"></strong:newdate>
                    
                  </td>
					<td width="11%" align="center" class="biao_bg1">
						<s:select name="docFiling"  onchange="submitForm()" list="filingMap" listKey="key"
							listValue="value" headerValue="全部" headerKey=""
							style="width:100%" 
							title="全部" />
					</td>
					<td class="biao_bg1" width="*%">
					&nbsp;
					</td>
                </tr>
              </table>
	              <webflex:flexCheckBoxCol caption="选择" valuepos="11" valueshowpos="0"
	                width="3%" isCheckAll="true"
	                isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
	              <webflex:flexTextCol caption="公文标题" valuepos="11" valueshowpos="1" isCanDrag="true"
	                width="27%" isCanSort="true" showsize="23" onclick="getinfo(this.value)"></webflex:flexTextCol> 
	              <webflex:flexTextCol caption="发文文号" valuepos="0" valueshowpos="0" isCanDrag="true"
	                width="15%" isCanSort="true" showsize="15"></webflex:flexTextCol>  
	              <webflex:flexTextCol caption="发文单位" valuepos="2" valueshowpos="2" isCanDrag="true"
	                width="14%" isCanSort="true" showsize="10"></webflex:flexTextCol>
	              <webflex:flexDateCol caption="发文时间" valuepos="3" valueshowpos="3"
	                width="12%" isCanDrag="true" dateFormat="yyyy-MM-dd HH:mm" showsize="30" isCanSort="true"></webflex:flexDateCol> 
	              <webflex:flexDateCol caption="接收时间" valuepos="4" valueshowpos="4"
	                width="12%" isCanDrag="true" dateFormat="yyyy-MM-dd HH:mm" showsize="30" isCanSort="true"></webflex:flexDateCol>  
	              <webflex:flexDateCol caption="归档时间" valuepos="6" valueshowpos="6"
	                width="12%" isCanDrag="true" dateFormat="yyyy-MM-dd HH:mm" showsize="30" isCanSort="true"></webflex:flexDateCol> 
                <webflex:flexDateCol caption="来文草稿" valuepos="12" valueshowpos="javascript:show(12);"
	                width="8%" isCanDrag="true" dateFormat="yyyy-MM-dd HH:mm" showsize="30" isCanSort="true"></webflex:flexDateCol> 
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
        	//item = new MenuItem("<%=frameroot%>/images/chakan.gif","查看已转入列表","showTurnDraft",1,"ChangeWidthTable","checkMoreDis");
	        //sMenu.addItem(item);
	        item = new MenuItem("<%=frameroot%>/images/tianjia.gif","转移到来文草稿","docTurnDraft",1,"ChangeWidthTable","checkMoreDis");
	        sMenu.addItem(item);
        sMenu.addShowType("ChangeWidthTable");
        registerMenu(sMenu);
      }
    </script>
	</body>
</HTML>
