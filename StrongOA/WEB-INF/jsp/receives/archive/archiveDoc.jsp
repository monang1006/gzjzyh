<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/security" prefix="security"%>

<HTML>
	<HEAD>
		<TITLE>操作内容</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<!--右键菜单样式 -->
		<LINK type=text/css rel=stylesheet
			href="<%=frameroot%>/css/properties_windows.css">
		<LINK type=text/css rel=stylesheet
			href="<%=frameroot%>/css/search.css">
		<script language="javascript"
			src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script language="javascript"
			src="<%=path%>/common/js/common/search.js"></script>
		<script language='javascript' type="text/javascript"
			src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<SCRIPT language="javascript" type="text/javascript"
			src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
		<!--右键菜单脚本 -->
		<script language="javascript"
			src="<%=path%>/common/js/common/common.js"></script>
			<SCRIPT LANGUAGE="JavaScript" src="<%=path%>/common/js/commontab/service.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></SCRIPT>
		<script type="text/javascript">
			$(function(){
				//使得搜索按钮居中显示
				$('.biao_bg1:first').css('textAlign','center');
				//去除一个空td
				$('.biao_bg1:last').remove();
			});
<%--		$(document).ready(function(){--%>
<%--			//禁用全选--%>
<%--			$("input:checkbox[name='checkall']").attr("disabled",true);--%>
<%--			//处理CHECKBOX单击事件--%>
<%--			$("input:checkbox[name!='checkall']").click(function(){--%>
<%--				var dept = $(this).parent().next().next().next().next().attr("value");--%>
<%--				doClick(dept);--%>
<%--				if($("input:checkbox:checked").size() == 0){--%>
<%--					$("input:checkbox[name!='checkall']").attr("disabled",false);--%>
<%--				}--%>
<%--			});--%>
<%--		});	--%>
<%--		function doClick(dept){--%>
<%--			$("input:checkbox[name!='checkall']").each(function(){--%>
<%--				var org = $(this).parent().next().next().next().next().attr("value");--%>
<%--				if(org!=dept){--%>
<%--					$(this).attr("disabled",true);--%>
<%--				}--%>
<%--			});--%>
<%--		}--%>
	 </script>
	</HEAD>
	<BODY class=contentbodymargin 
		onload="initMenuT()">
		<script language="javascript" type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
			
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="100%">
						<s:form theme="simple" id="myTableForm"
							action="/receives/archive/archiveDoc.action">
							<input type="hidden" id="disLogo" name="disLogo"
								value="${disLogo }">
							<input type="hidden" id="treeValue" name="treeValue"
								value="${treeValue }">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td height="40"
										style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
										<table width="100%" border="0" cellspacing="0"
											cellpadding="00">
											<tr>
												<td>
												</td>
												<td width="30%">
												<img src="<%=frameroot%>/images/ico.gif" width="9"
														height="9">
													档案中心--归档文件列表	
												</td>			
												<td width="70%">
													<table width="100%" border="0" align="right"
														cellpadding="0" cellspacing="0">
														<tr>
															<td width="*">
																&nbsp;
															</td>
<%--															<td width="80">
															<security:authorize ifAllGranted="001-000800010001">
																<a class="Operation" href="javascript:downloadArchiveFile();"><img src="<%=frameroot%>/images/xia.gif" width="14"
																	height="14" class="img_s">下载公文</a>
																	</security:authorize>
															</td>
															<td width="5">--%>
															 <td width="70%"><table align="right"><tr>
															<td>
<%--															<security:authorize ifAllGranted="001-000800010001">--%>
																<a class="Operation" href="javascript:viewArchiveFileInfo();"><img src="<%=frameroot%>/images/chakan.gif" width="14"
																	height="14" class="img_s">查看公文</a>
<%--																	</security:authorize>--%>
															</td>
															</tr></table></td>
															<td width="5">
															</td>
										
														</tr>
													</table>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
							<webflex:flexTable name="myTable" width="100%" height="370px"
								wholeCss="table1" property="archiveId" isCanDrag="true"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByProperty" collection="${page.result}"
								page="${page}">
								<table width="100%" border="0" cellpadding="0" cellspacing="1"
									class="table1">
									<tr>
										<td width="4%"  class="biao_bg1" onclick="search();" style="cursor: hand;" >
											<img src="<%=frameroot%>/images/sousuo.gif" width="17" height="16" >
										</td>
										
										<td width="26%"  class="biao_bg1">
											 <s:textfield  name="model.docTitle"  cssClass="search" title="公文标题"  > </s:textfield>
										</td>
										<td width="18%"  class="biao_bg1">
											 <s:textfield  name="model.docCode"  cssClass="search" title="发文文号" ></s:textfield>
										</td>
										<td width="10%"  class="biao_bg1">
											<s:select onchange="search()" cssStyle="width:100%" list="jjcdItems" headerKey="" headerValue="全部" listKey="dictItemName" listValue="dictItemName" name="model.docEmergency"></s:select>
										</td>
										<td width="17%"  class="biao_bg1">
											 <s:textfield  name="model.docIssueDepartSigned"  cssClass="search" title="发文单位" ></s:textfield>
										</td>
										
										 <td width="10%" align="center" class="biao_bg1">
						                    <strong:newdate name="startDate" id="startDate"  width="98%" dateobj="${startDate}"
						                      skin="whyGreen" isicon="true"  dateform="yyyy-MM-dd"></strong:newdate>
						                  </td>
						                  <td width="10%" align="center" class="biao_bg1">
						                    <strong:newdate name="endDate" id="endDate" width="98%" dateobj="${endDate}"
						                      skin="whyGreen" isicon="true" dateform="yyyy-MM-dd"></strong:newdate>
						                  </td>
										<td class="biao_bg1" >
											&nbsp;
										</td>
									</tr>
								</table>
								<webflex:flexCheckBoxCol caption="选择" property="archiveId" showValue="docTitle" width="4%" isCheckAll="true" isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>

								<webflex:flexTextCol caption="公文标题" property="archiveId"   showValue="docTitle" width="28%" isCanDrag="true" isCanSort="true" onclick="viewFileInfo(this.value);"></webflex:flexTextCol>
								
								<webflex:flexTextCol caption="发文文号" property="docCode" showValue="docCode" width="20%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="紧急程度" property="docEmergency" showValue="docEmergency" width="10%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="发文单位" property="docIssueDepartSigned" showValue="docIssueDepartSigned" width="20%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>							
								<webflex:flexDateCol caption="发文日期" property="docSendTime" showValue="docSendTime" width="18%" isCanDrag="true" isCanSort="true" dateFormat="yyyy-MM-dd HH:mm"></webflex:flexDateCol>
							    </webflex:flexTable>
							
						</s:form>
					</td>
				</tr>
			</table>
			<iframe id="annexFrame" style="display:none"></iframe>
		</DIV>
		<script language="javascript">
var sMenu = new Menu();
function initMenuT(){
	sMenu.registerToDoc(sMenu);
	var item = null;
<%--	<security:authorize ifAllGranted="001-000800010001">
	item = new MenuItem("<%=frameroot%>/images/xia.gif","下载公文","downloadArchiveFile",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	</security:authorize>--%>
	
	item = new MenuItem("<%=frameroot%>/images/chakan.gif","查看公文","viewArchiveFileInfo",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}
function downloadArchiveFile(){		//添加文件
	var id=getValue();
	if(id=="")
		alert("请选择需要下载的档案文件！");
	else if(id.indexOf(",")!=-1)
		alert("不可以同时下载多个档案文件！");
	else{
		var frame=document.getElementById("annexFrame");
		frame.src="<%=path%>/receives/archive/archiveDoc!download.action?archiveId="+id;	
	}
}


function search(){			//按条件查询
	document.getElementById("disLogo").value="search";
	document.getElementById("myTableForm").submit();
}

//查看档案文件
function viewArchiveFileInfo(){
	var id=getValue();
	if(id=="")
		alert("请选择需要查看的档案文件！");
	else if(id.indexOf(",")!=-1)
		alert("不可以同时查看多个档案文件！");
	else{
		viewFileInfo(id);
	}
}

function viewFileInfo(value){//查看文件
 $.post("<%=path%>/receives/archive/archiveDoc!isHasArchiveFile.action",
           {"archiveId":value},
           function(data){
         
	    if(data=="1"){
	      alert('当前档案文件不存在！');
	      return;
	    }else{
	    	var width=screen.availWidth-10;
   			var height=screen.availHeight-30;
   			url = "<%=path%>/fileNameRedirectAction.action?toPage=receives/archive/archiveDoc-viewPage.jsp?archiveId="+value;
   			var ret=OpenWindow(url,width, height, window);
   			//var ret=OpenWindow("<%=path%>/receives/archive/archiveDoc!input.action?archiveId="+value+"&forwardStr=view",width, height, window);
			//top.perspective_content.actions_container.personal_properties_toolbar.navigate("<%=path%>/receives/archive/archiveDoc!input.action?archiveId="+value+"&forwardStr=view",data);
	
		}
		});
}


</script>
	</BODY>
</HTML>
