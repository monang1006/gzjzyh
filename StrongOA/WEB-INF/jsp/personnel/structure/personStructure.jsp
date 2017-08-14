<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>机构编制列表</TITLE>
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
			<STYLE type="text/css">
			.nowin{
			  color: #999999;
			}
			</STYLE>
		<script type="text/javascript">
			//添加编制
			function addStructure(){
	         var audit= window.showModalDialog("<%=path%>/personnel/structure/personStructure!input.action",window,'help:no;status:no;scroll:no;dialogWidth:500px; dialogHeight:400px');
	
             }
             
             //编辑编制
           function editStructure(){
             var id=getValue();
              if(id==""){
	            alert("请先选择编制！");
	            return ;
	         }
	         if(id.length>32){
	            alert("只可以同时修改一个编制！");
	            return;
	         }
	         var audit = window.showModalDialog("<%=path%>/personnel/structure/personStructure!input.action?structureId="+id,window,'help:no;status:no;scroll:no;dialogWidth:500px; dialogHeight:400px');
	
           }
           
           function del(){
             var id=getValue();
              if(id==""){
	            alert("请先选择编制！");
	            return ;
	         }
	         if(confirm("确定要删除吗?")) {
             window.location="<%=path%>/personnel/structure/personStructure!delete.action?structureId="+id;
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
         
         function radioOrg(){
		    var audit= window.showModalDialog("<%=root%>/personnel/personorg/personOrg!radiotree.action",window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:400px');
		
		    if(audit==undefined||audit==null){
		      return false;
		    }else{
              $("#baseorgName").val(audit[1]);	
              $("#baseorgid").val(audit[0]);    
		    }
		}
		function remove(){
		    $("#baseorgName").val("");	
              $("#baseorgid").val("");   
		}
		function onSub(){
		    $("#myTableForm").submit();
		}
			</script>
	</HEAD>
	<BODY class=contentbodymargin oncontextmenu="return false;" onload="initMenuT();" >
	<script language="javascript" type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
			<s:form theme="simple" id="myTableForm"
				action="/personnel/structure/personStructure.action">
				<input type="hidden" name="org.orgid" value="${org.orgid }" id="baseorgid">
				<input type="hidden" name="isgoogle" value="search">
				<table width="100%" border="0" cellspacing="0" cellpadding="0"
					style="vertical-align: top;">
					<tr>
						<td height="100%">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td height="40"
										style="FILTER: progid :   DXImageTransform .   Microsoft .   Gradient(gradientType =   0, startColorStr =   #ededed, endColorStr =   #ffffff);">
										<table width="100%" border="0" cellspacing="0" cellpadding="0">
											<tr>
												<td>&nbsp;</td>
												<td width="30%">
													<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
														height="9">&nbsp;
													编制列表
												</td>
												<td width="5%">
													&nbsp;
												</td>
												<td width="70%">
													<table width="100%" border="0" align="right"
														cellpadding="0" cellspacing="0">
														<tr>
															<td width="100%" align="right">
																<table width="100%" border="0" align="right"
																	cellpadding="0" cellspacing="0">

																	<tr>
																		<td width="*">
																			&nbsp;
																		</td>
																		<td width="58" align="right">
																			<a class="Operation"
																				href="javascript:addStructure();"><img
																					src="<%=root%>/images/ico/tianjia.gif" width="14"
																					height="15" class="img_s">添加</a>
																		</td>
																		<td width="5"></td>
																		<td width="58" align="right">
																			<a class="Operation"
																				href="javascript:editStructure();"> <img
																					src="<%=root%>/images/ico/bianji.gif" width="15"
																					height="15" class="img_s"> 编辑</a>
																		</td>
																		<td width="5"></td>
																		<td width="58" align="right">
																			<a class="Operation" href="javascript:del();"> <img
																					src="<%=root%>/images/ico/shanchu.gif" width="15"
																					height="15" class="img_s">删除</a>
																		</td>
																		<td width="5"></td>




																	</tr>
																</table>
															</td>

														</tr>

														<tr higth="20"></tr>

														<tr>


														</tr>
													</table>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
							<webflex:flexTable name="myTable" width="100%" height="370px"
								wholeCss="table1" property="id" isCanDrag="true"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByProperty" collection="${page.result}"
								page="${page}">
								<table width="100%" border="0" cellpadding="0" cellspacing="1"
									class="table1">
									<tr>
										<td width="5%" align="center" class="biao_bg1">
											<img src="<%=root%>/images/ico/sousuo.gif" width="17"
												id="img_sousuo" height="16" style="cursor: hand;"
												onclick="onSub();" title="单击搜索">
										</td>
										<td width="28%" align="center" class="biao_bg1" nowrap>
										<input type="button" value="清空" style="width:25%;"  class="input_bg" onclick="remove();">
											<s:textfield id="baseorgName" name="org.orgName" onclick="radioOrg();"
												cssClass="" style="width:75%; color: #999999;" value="请选择所属机构" onkeydown="return false;"  title="请选择所属机构"></s:textfield>
												
												
										</td>
										<td width="25%" align="center" class="biao_bg1">
                                            <s:select id="strucType" 
												name="model.strucType" list="dictlist"
												listKey="dictItemCode" listValue="dictItemName"
												cssStyle="width:100%">
											</s:select>
										</td>
										<td width="22%" align="center" class="biao_bg1">
											<s:textfield name="model.strucNumber" cssClass="search"
												title="请输入编制数量"></s:textfield>
										</td>
										<td width="25%" align="center" class="biao_bg1">
										 <strong:newdate id="strucEdittime" name="model.strucEdittime"
												dateform="yyyy-MM-dd" isicon="true" width="100%"
												dateobj="${strucEdittime}" classtyle="search" title="编辑日期"/>
										 </td>

									    <td width="*%" align="center" class="biao_bg1">
											&nbsp;
										</td>
								</table>
								<webflex:flexCheckBoxCol caption="选择" property="strucId"
									showValue="toaBaseOrg.orgName" width="5%" isCheckAll="true" isCanDrag="false"
									isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexTextCol caption="所属机构" 
									property="toaBaseOrg.orgid" showValue="toaBaseOrg.orgName" width="28%"
									isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="编制类型" 
									property="strucType" showValue="strucTypeName" width="25%"
									isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="编制数量" property="strucNumber"
									showValue="strucNumber" width="22%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
								<webflex:flexDateCol caption="修改日期" property="strucEdittime"
									showValue="strucEdittime" width="25%" isCanDrag="true"
									isCanSort="true" dateFormat="yyyy-MM-dd"></webflex:flexDateCol>
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
	item = new MenuItem("<%=root%>/images/ico/tianjia.gif","添加","addStructure",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/bianji.gif","修改","editStructure",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
//	item = new MenuItem("<%=root%>/images/ico/yidong.gif","移动","move",1,"ChangeWidthTable","checkOneDis");
//	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/shanchu.gif","删除","del",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
  
}




</script>
	</BODY>
</HTML>
