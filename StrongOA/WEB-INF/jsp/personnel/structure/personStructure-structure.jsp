<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>

		<title>增加编制</title>
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
			//添加编制
			function addStructure(){
			  var orgid=$("#orgId").val();
			  // var rest=$("#rest").val();
			//   if(rest!=null && rest!=''){
			//	alert('该机构属于统一用户那边机构不能随意操作！');
			//	return;
			//}
	          var audit= window.showModalDialog("<%=path%>/personnel/structure/personStructure!input.action?orgId="+orgid,window,'help:no;status:no;scroll:no;dialogWidth:500px; dialogHeight:400px');
	
             }
             
             //编辑编制
           function editStructure(){
             var id=getValue();
	         var orgid=$("#orgId").val();
	         if(id==""){
	            alert("请先选择编制！");
	            return ;
	         }
	         if(id.length>32){
	            alert("只可以同时修改一个编制！");
	            return;
	         }
	         var audit = window.showModalDialog("<%=path%>/personnel/structure/personStructure!input.action?orgId="+orgid+"&structureId="+id,window,'help:no;status:no;scroll:no;dialogWidth:500px; dialogHeight:400px');
	
           }
           
           function del(){
             var id=getValue();
              var orgid=$("#orgId").val();
               if(id==""){
	            alert("请先选择编制！");
	            return ;
	         }
	       if(confirm("确定要删除吗?")) {
             window.location="<%=path%>/personnel/structure/personStructure!delete.action?orgId="+orgid+"&structureId="+id;
            }
           }
      
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
		function showperson(){
		 var id=getValue();
         var orgid=$("#orgId").val();
           if(id==""){
	         alert("请先选择编制！");
	          return ;
	       }
	    top.perspective_content.actions_container.personal_properties_toolbar.navigate("<%=path%>/personnel/baseperson/person.action?strucId="+id,"人员信息");
	// var result=window.showModalDialog("<%=path%>/personnel/baseperson/person.action?strucId="+id,window,'help:no;status:no;scroll:yes;dialogWidth:660px; dialogHeight:600px');
		}
			</script>
	</head>
	<body class=contentbodymargin oncontextmenu="return false;" onLoad="initMenuT();">
		<script type="text/javascript" language="javascript"
			src="<%=jsroot%>/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
			
									<table width="100%" border="0" cellspacing="0" cellpadding="00"
									 style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
										<br><tr>
											<td>&nbsp;</td>
											<td width="12%">
												<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
													height="9">&nbsp;
												组织机构信息
											</td>
											<td width="1%">
												&nbsp;
											</td>
											<td width="87%">
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
																	<td width="50">
																		<a class="Operation"
																			href="javascript:addStructure();">
																			<img src="<%=root%>/images/ico/tianjia.gif"
																				width="14" height="14" class="img_s">添加</a>
																	</td>
																	<td width="5"></td>
																	<td width="50">
																		<a class="Operation" href="javascript:editStructure();"> <img
																				src="<%=root%>/images/ico/bianji.gif" width="15"
																				height="15" class="img_s">编辑</a>
																	</td>
															<!-- 	<td width="5"></td>
																	<td width="90">
																		<a class="Operation" href="javascript:goStructure();"> <img
																				src="<%=root%>/images/ico/goujian.gif" width="15"
																				height="15" class="img_s">提交编制</a>
																	</td> -->	
																	<td width="5"></td>
																	<td width="50">
																		<a class="Operation" href="javascript:del();"> <img
																				src="<%=root%>/images/ico/shanchu.gif" width="15"
																				height="15" class="img_s">删除</a>
																	</td>
																	<td width="5"></td>
																	<%--  
																 	<td width="100">
																		<a class="Operation" href="javascript:showperson();"> <img
																				src="<%=root%>/images/ico/chakan.gif" width="15"
																				height="15" class="img_s">查看编内人员</a>
																	</td>
																	--%>
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
				                    action="/personnel/structure/personStructure.action">
				                      <input type="hidden" name="isgoogle" value="search">
				                      <input type="hidden" name="audittype" value="structure">
				                      <input type="hidden" name="rest" id="rest" value="${org.rest}">
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
										<td width="30%" align="center" class="biao_bg1">
											 <s:textfield name="model.strucNumber" cssClass="search" title="请输入编制数量"></s:textfield> 
										</td>
										<td width="30%" align="center" class="biao_bg1">
											 <strong:newdate id="strucEdittime" name="model.strucEdittime"
												dateform="yyyy-MM-dd" isicon="true" width="100%"
												dateobj="${strucEdittime}" classtyle="search" title="编辑日期"/>
										 </td>
										
										
										<td width="*%" align="center" class="biao_bg1">
											&nbsp;
										</td>
								</table>
								<webflex:flexCheckBoxCol caption="选择" property="strucId"
									showValue="strucTypeName" width="5%" isCheckAll="true"
									isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexTextCol caption="编制类型"  
									property="strucType" showValue="strucTypeName" width="35%"
									isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="编制数量" property="strucNumber"
									showValue="strucNumber" width="30%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
								
								<webflex:flexDateCol caption="修改日期" property="strucEdittime"
									showValue="strucEdittime" width="30%" isCanDrag="true"
									isCanSort="true" dateFormat="yyyy-MM-dd"></webflex:flexDateCol>
								
							</webflex:flexTable>
			</div>
			</s:form>
		</DIV>
	</body>
	<script language="javascript">
var sMenu = new Menu();

function initMenuT(){
	sMenu.registerToDoc(sMenu);
	var item = null;
	item = new MenuItem("<%=root%>/images/ico/tianjia.gif","添加","addStructure",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/bianji.gif","编辑","editStructure",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	
	item = new MenuItem("<%=root%>/images/ico/shanchu.gif","删除","del",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	<%--
	item = new MenuItem("<%=root%>/images/ico/yidong.gif","查看编内人员","showperson",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	--%>
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
  
    }

	</script>
</html>
