<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>

		<title>机构编制情况统计</title>
		<%@include file="/common/include/meta.jsp"%>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<!--右键菜单样式 -->
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<LINK type=text/css rel=stylesheet
			href="<%=frameroot%>/css/search.css">
		<script language='javascript'
			src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
				<script type="text/javascript" language="javascript"
			src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
		<!--右键菜单脚本 -->
		<script language="javascript"
			src="<%=path%>/common/js/common/search.js"></script>
			<script type="text/javascript">
		
      //显示编制状态
         var showState=function(value){
           if(value=="0"){
           return "正常";
           }else if(value=="1"){
           return "满编";
           }else if( value=="2"){
           return "超编";
           }else{
             return "正常";
           }
         }
         function onSub(){
		    $("#myTableForm").submit();
		}
		//查看编制下详细人员
	   function	showPerson(){
	      var id=getValue();
	      if(id==""){
	      alert("请先选择编制!");
	      return;
	      }
	      if(id.length>32){
	        alert("不可以同时查看多个编制!");
	        return;
	      }
	     // top.perspective_content.actions_container.personal_properties_toolbar.navigate("<%=path%>/personnel/baseperson/person.action?strucId="+id,"人员信息");
	 
 var result=window.showModalDialog("<%=path%>/personnel/baseperson/person.action?strucId="+id,window,'help:no;status:no;scroll:yes;dialogWidth:660px; dialogHeight:600px');
		}
	   function showPersoninfo(value){
	    top.perspective_content.actions_container.personal_properties_toolbar.navigate("<%=path%>/personnel/baseperson/person.action?strucId="+value,"人员信息");
	   }
		
			</script>
	</head>
	<body class=contentbodymargin oncontextmenu="return false;"   onLoad="initMenuT();">
		<script type="text/javascript" language="javascript"
			src="<%=jsroot%>/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
			
									<table width="100%" border="0" cellspacing="0" cellpadding="00"
									 style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
										<br><tr>
											<td>&nbsp;</td>
											<td width="20%">
												<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
													height="9">&nbsp;
												机构详细统计信息
											</td>
											<td width="1%">
												&nbsp;
											</td>
											<td width="79%">
												<table width="100%" border="0" align="right" cellpadding="0"
													cellspacing="0">
													<tr>
														<td width="100%" align="right">
															<table width="100%" border="0" align="right"
																cellpadding="0" cellspacing="0">

																<tr>
																	<td width="*">
																		&nbsp;
																	</td>
																	<!--  
																	<td  width="100">
																		<a class="Operation" href="javascript:showPerson();"> <img
																				src="<%=root%>/images/ico/chakan.gif" width="15"
																				height="15" class="img_s">查看编内人员</a>
																	</td>
																	-->
																	<td width="5"></td>
																	</tr>
												</table>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
						<br>
						<s:form theme="simple" id="myTableForm"
				                    action="/personnel/structure/personStructure!statisticInfo.action">
				                      <input type="hidden" name="isgoogle" value="search">
				                      <input type="hidden" name="audittype" value="structure">
							<table width="100%" height="10%" border="0" cellpadding="0"
								cellspacing="1" align="center" class="table1">
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">机构编号：</span>
										<input type="hidden" name="orgId" id="orgId" value="${org.orgid }">
									</td>
									<td class="td1" colspan="3" width="30%" align="left">
										<span>${org.orgSyscode}</span>
										
									</td>
								
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">机构名称：</span>
									</td>
									<td class="td1" colspan="3" align="left">
									<span>${org.orgName}</span>
									</td>
								</tr>
								
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">机构性质：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<span>${org.orgNatureName}</span>
									</td>
								
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">机构代码：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<span>${org.orgCode }</span>
										
										
									</td>
								</tr>
								
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">电话：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<span>${org.orgTel }</span>
									</td>
								
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">传真：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<span>${org.orgFax }</span>
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">负责人：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<span>${org.orgManager }</span>
										
									</td>
								
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">地址：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<span>${org.orgAddr }</span>
									</td>
								</tr>
								<tr >
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">机构描述：</span>
									</td>
									<td class="td1" colspan="7" align="left">
										<textarea id="" name=""
											rows="4" cols="50">${org.orgDescription }</textarea>
									</td>
								</tr>

							</table>
						
						
					<br>
			<div>
				<webflex:flexTable name="myTable" width="100%" height="370px"
								wholeCss="table1" property="strucId" isCanDrag="true"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByProperty" collection="${page.result}"
								page="${page}">
								<table width="100%" border="0" cellpadding="0" cellspacing="1"
									class="table1">
									<tr>
										<td width="5%" align="center" class="biao_bg1">
											<img src="<%=root%>/images/ico/sousuo.gif" width="17" id="img_sousuo"
												height="16" onclick="onSub();" style="cursor: hand;" title="单击搜索">
										</td>
									   <td width="35%" align="center" class="biao_bg1">
											 <s:select id="strucType" 
												name="model.strucType" list="dictlist"
												listKey="dictItemCode" listValue="dictItemName"
												cssStyle="width:100%">
											</s:select>
										</td>
										<td width="60%" align="center" class="biao_bg1">
											<input name="" type="text" style="width:100%" readonly="readonly">
										</td>
										
										<td width="*%" align="center" class="biao_bg1">
											&nbsp;
										</td>
										</tr>
								</table>
								<webflex:flexCheckBoxCol caption="选择" property="strucId"
									showValue="strucTypeName" width="5%" isCheckAll="true"
									isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexTextCol caption="编制类型"  
									property="strucId" showValue="strucTypeName" width="35%"
									isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="应有编内人数" property="strucNumber"
									showValue="strucNumber" width="22%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
									<webflex:flexTextCol caption="实际人数" property="realityPerson"
									showValue="realityPerson" width="22%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
							    	<webflex:flexTextCol caption="超编人数" property="outPerson"
									showValue="outPerson" width="21%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
								
								
							</webflex:flexTable>
			</div>
			</s:form>
			<div id="bar">
			<img src="<%=path %>/img/${temp1}.jpg">
			</div>
		</DIV>
		<script language="javascript">
var sMenu = new Menu();

function initMenuT(){
	sMenu.registerToDoc(sMenu);
	var item = null;
	<%-- 
	item = new MenuItem("<%=root%>/images/ico/chakan.gif","查看编内人员","showPerson",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	--%>
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
  
    }

	</script>
	</body>
	
</html>
