<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>

<HTML>
	<HEAD>
		<TITLE>申请类型列表</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<!--右键菜单样式 -->
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<script language="javascript"
			src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script language="javascript"
			src="<%=path%>/common/js/common/search.js"></script>
		<LINK type=text/css rel=stylesheet
			href="<%=frameroot%>/css/search.css">
		<script language='javascript'
			src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
			<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
		<!--右键菜单脚本 -->
		<script language="JavaScript"
			src="<%=path%>/common/js/commontab/service.js"></script>
		<script language="JavaScript"
			src="<%=path%>/common/js/commontab/workservice.js"></script>
		<script type="text/javascript">
       
        function showcanRewriter(value){
           if(value=="0"){
              return "可以补填";
           }else{
              return "不可补填";
           }
        }
        function showisAbsence(value){
           if(value=="0"){
              return "统计缺勤";
           }else{
              return "不统计缺勤";
           }
        }
        function showisSystem(value){
           if(value=="0"){
              return "是";
           }else{
              return "否";
           }
        }
        function showisEnable(value){
           if(value=="0"){
              return "启用";
           }else{
              return "禁用";
           }
        }
       </script>
	</HEAD>
	<BODY class=contentbodymargin oncontextmenu="return false;"
		onLoad="initMenuT()">
		<script language="javascript" type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
			<s:form theme="simple" id="myTableForm"
				action="/attendance/applytype/applyType.action">
				<input type="hidden" name="disLogo" id="disLogo" value="${disLogo}">
				<table width="100%" border="0" cellspacing="0" cellpadding="0"
					style="vertical-align: top;">
					<tr>
						<td height="100%">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">


								<tr>
									<td height="40"
										style="FILTER: progid : DXImageTransform . Microsoft . Gradient(gradientType = 0, startColorStr = #ededed, endColorStr = #ffffff);">
										<table width="100%" border="0" cellspacing="0"
											cellpadding="00">
											<tr>
												<td>
												&nbsp;
												</td>
												<td width="30%">
													<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
														height="9">&nbsp;
													申请单类型列表
												</td>
												<td width=70%">
													<table width="100%" border="0" align="right"
														cellpadding="0" cellspacing="0">
														<tr>
															<td width="*">
																&nbsp;
															</td>
															<td width="70">
																<a class="Operation" href="javascript:show();"> <img
																		src="<%=root%>/images/ico/chakan.gif" width="30"
																		height="15" class="img_s">查看</a>
															</td>
															<td width="5"></td>
															<td width="70">
																<a class="Operation" href="javascript:addtype();"> <img
																		src="<%=root%>/images/ico/tianjia.gif" width="15"
																		height="15" class="img_s">添加</a>
															</td>
															<td width="5"></td>
															<td width="70">
																<a class="Operation" href="editType()"> <img
																		src="<%=root%>/images/ico/bianji.gif" width="15"
																		height="15" class="img_s">编辑</a>
															</td>
															<td width="5"></td>
															<td width="70">
																<a class="Operation" href="javascript:del();"> <img
																		src="<%=root%>/images/ico/shanchu.gif" width="15"
																		height="15" class="img_s">删除</a>
															</td>
															<td width="5"></td>
															<td width="70">
																<a class="Operation" href="javascript:updateYESenable();"> <img
																		src="<%=root%>/images/ico/kaishi.gif" width="15"
																		height="15" class="img_s">启用</a>
															</td>
															<td width="5"></td>
															<td width="70">
																<a class="Operation" href="javascript:updateNOenable();"> <img
																		src="<%=root%>/images/ico/guanbi.gif" width="15"
																		height="15" class="img_s">禁用</a>
															</td>
															<td width="5">
																&nbsp;
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
								wholeCss="table1" property="mykmId" isCanDrag="true"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByProperty" collection="${page.result}"
								page="${page}">
								<table width="100%" border="0" cellpadding="0" cellspacing="1"
									class="table1">
									<tr>
										<td width="5%" align="center" class="biao_bg1">
											<img src="<%=root%>/images/ico/sousuo.gif" width="17"
												height="16" style="cursor: hand;" title="单击搜索"
												onClick="getListBySta();">
										</td>
										<td width="24%" align="center" class="biao_bg1">
										<input id="typeName" maxlength="25"  class="search" name="model.typeName" title="请输入申请单名称" type="text" style="width: 100%;" value="${model.typeName}" >
										</td>
										<td width="18%" align="center" class="biao_bg1">
										<s:select id="canRewriter" style="width: 100%;"
											name="model.canRewriter" list="#{'请选择补填状态':'','可以补填':'0','不可以补填':'1'}"
											listKey="value" listValue="key"></s:select>
										</td>
										<td width="18%" align="center" class="biao_bg1">
										<s:select id="isAbsence" style="width: 100%;"
											name="model.isAbsence" list="#{'缺勤状态':'','统计缺勤':'0','不统计缺勤':'1'}"
											listKey="value" listValue="key"></s:select>
										</td>
										<td width="10%" align="center" class="biao_bg1">
										<s:select id="isSystem" style="width: 100%;"
											name="model.isSystem" list="#{'是否是系统的':'','是':'0','否':'1'}"
											listKey="value" listValue="key"></s:select>
										</td>
										<td width="10%" align="center" class="biao_bg1">
										<s:select id="isEnable" style="width:100%;"
											name="model.isEnable" list="#{'是否启用':'','启用':'0','禁用':'1'}"
											listKey="value" listValue="key"></s:select>
										</td>
										<td width="15%" align="center" class="biao_bg1">
											 <strong:newdate id="model.typeCreateDate" name="model.typeCreateDate"
												dateform="yyyy-MM-dd" isicon="true" width="100%"
												dateobj="${model.typeCreateDate}" classtyle="search" title="编辑日期"/>
										</td></tr>
								</table>
								<webflex:flexCheckBoxCol caption="选择" property="typeId"
									showValue="typeName" width="5%" isCheckAll="true"
									isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexTextCol caption="名称" property="typeId"
									showValue="typeName" width="24%" isCanDrag="true"
									isCanSort="true" showsize="15"></webflex:flexTextCol>
								<webflex:flexTextCol caption="补填状态" onclick=""
									property="canRewriter"
									showValue="javascript:showcanRewriter(canRewriter);"
									width="18%" isCanDrag="true" isCanSort="true" showsize="15"></webflex:flexTextCol>
								<webflex:flexTextCol caption="缺勤状态" onclick=""
									property="isAbsence"
									showValue="javascript:showisAbsence(isAbsence);" width="18%"
									isCanDrag="true" isCanSort="true" showsize="15"></webflex:flexTextCol>
								<webflex:flexTextCol caption="是否为系统数据" onclick=""
									property="isSystem"
									showValue="javascript:showisSystem(isSystem);" width="10%"
									isCanDrag="true" isCanSort="true" showsize="15"></webflex:flexTextCol>
								<webflex:flexTextCol caption="是否启用" onclick=""
									property="isEnable"
									showValue="javascript:showisEnable(isEnable);" width="10%"
									isCanDrag="true" isCanSort="true" showsize="15"></webflex:flexTextCol>
								<webflex:flexDateCol caption="创建日期" property="typeCreateDate"
									showValue="typeCreateDate" dateFormat="yyyy-MM-dd" width="15%"
									isCanDrag="true" isCanSort="true"></webflex:flexDateCol>
							</webflex:flexTable>
						</td>
					</tr>
				</table>
			</s:form>
		</DIV>
		<script language="javascript">
var sMenu = new Menu();
function initMenuT(){
	sMenu.registerToDoc(sMenu);
	var item = null;
	item = new MenuItem("<%=root%>/images/ico/chakan.gif","查看","show",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);   
	item = new MenuItem("<%=root%>/images/ico/tianjia.gif","添加","addtype",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/bianji.gif","编辑","editType",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);   
	item = new MenuItem("<%=root%>/images/ico/shanchu.gif","删除","del",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/bianji.gif","启用","updateYESenable",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);   
	item = new MenuItem("<%=root%>/images/ico/bianji.gif","禁用","updateNOenable",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
    sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}
 function addtype(){
           var audit= window.showModalDialog("<%=root%>/attendance/applytype/applyType!input.action",window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:400px');
		
        }
function editType(){
	var id=getValue();
	if(id==""){
	alert("请选择一条申请单类型!");
	return;
	}
	if(id.length>32){
	alert("不可以同时修改多条申请单类型!");
	return;
	}
	var audit= window.showModalDialog("<%=root%>/attendance/applytype/applyType!input.action?typeId="+id,window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:400px');
		
}
function show(){
	var id=getValue();
	if(id==""){
	alert("请选择一条申请单类型!");
	return;
	}
	if(id.length>32){
	alert("不可以同时查看多条申请单类型!");
	return;
	}
	 var audit= window.showModalDialog("<%=root%>/attendance/applytype/applyType!show.action?typeId="+id,window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:400px');
		
}
function del(){//废除稿件
	var id=getValue();
	if(id == null||id == ''){
		alert('请选择要删除的申请单类型！');
		return;
	}
	if(id.indexOf(",")!=-1){
		alert('请选择一条申请单类型！');
		return;
	}
	 $.post('<%=root%>/attendance/applytype/applyType!isSystem.action',
		             { 'typeId':id},
		       function(data){
		            if(data=="0"){
		               alert("系统申请单类型不可以删除！");
		               return;
		            }else{
						if(confirm("确定删除吗?")) 
						{ 
						   location = "<%=root%>/attendance/applytype/applyType!delete.action?typeId="+id
						} 
					}
				}
			);
		
}
function updateYESenable(){//启用
	var id=getValue();
	if(id==null||id==""){
	  alert("请先选择申请单类型!");
	  return;
	}
	 location = "<%=root%>/attendance/applytype/applyType!isEnableType.action?typeId="+id+"&isEnable=0";
}
function updateNOenable(){//禁用
	var id=getValue();
	if(id==null||id==""){
	  alert("请先选择申请单类型!");
	  return;
	}
	 location = "<%=root%>/attendance/applytype/applyType!isEnableType.action?typeId="+id+"&isEnable=1";
}
function getListBySta(){	//根据属性查询
	document.getElementById("disLogo").value="search";
	document.getElementById("myTableForm").submit();
}



</script>
	</BODY>
</HTML>
