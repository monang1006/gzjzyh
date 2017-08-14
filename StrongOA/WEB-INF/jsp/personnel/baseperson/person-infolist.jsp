<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>操作内容</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<script language='javascript'
			src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<!--右键菜单脚本 -->
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js"
			type="text/javascript"></script>
		<SCRIPT LANGUAGE="JavaScript"
			src="<%=path%>/common/js/commontab/service.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript"
			src="<%=path%>/common/js/commontab/workservice.js"></SCRIPT>
		<style>
			#contentborder {
				BORDER-RIGHT: #506eaa 0px solid;
				PADDING-RIGHT: 0px;
				PADDING-LEFT: 0px;
				BACKGROUND: white;
				PADDING-BOTTOM: 0px;
				OVERFLOW: auto;
				BORDER-LEFT: #506eaa 0px solid;
				WIDTH: 100%;
				BORDER-BOTTOM: #506eaa 0px solid;
				POSITION: absolute;
				HEIGHT: 100%;
			}
			TH{
				padding:0px 20px;			
			}
		</style>
	</HEAD>
	<BODY class=contentbodymargin onload="initMenuT()" oncontextmenu="return false;">
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="100%">
						<s:form id="myTableForm" theme="simple"
							action="/personnel/baseperson/person!viewRelationInfos.action">
							<input type="hidden" name="pkey" value="${pkey}" />
							<input type="hidden" name="orgId" value="${orgId}" />
							<input type="hidden" name="personId" value="${personId}">
							<input type="hidden" name="disLogo" value="${disLogo}">
							<input type="hidden" name="infoSetCode" value="${infoSetCode}" />	
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td height="40"
											 style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
											<table width="100%" border="0" cellspacing="0"
												cellpadding="00">
												<tr>
													<td>&nbsp;</td>
													<td width="18%">
														<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
															height="9">&nbsp;
														<s:property value="objName"/>信息列表
													</td>
													<td width="20%">
														&nbsp;
													</td>
													<td width="55%">
														<table border="0" align="right" cellpadding="00"
															cellspacing="0">
															<tr>
																<td width="*">
																	&nbsp;
																</td>
																<s:if test="disLogo!=null&&disLogo==\"viewinfo\"">												
																</s:if>	
																<s:else>
																	<td width="50">
																		<a class="Operation" href="javascript:infoAdd();"> <img
																				src="<%=root%>/images/ico/tianjia.gif"
																				width="15" height="15" class="img_s">增加</a>
																	</td>
																	<td width="5"></td>
																	<td width="50">
																		<a class="Operation" href="javascript:infoEdit();"> <img
																				src="<%=root%>/images/ico/bianji.gif" width="15"
																				height="15" class="img_s">编辑</a>
																	</td>
																	<td width="5"></td>
																	<td width="50">
																		<a class="Operation" href="javascript:infoDel();">
																			<img src="<%=root%>/images/ico/shanchu.gif" width="15"
																				height="15" class="img_s">删除</a>
																	</td>
																	<td width="5"></td>	
																</s:else>
																<td width="50">
																	<a class="Operation" href="javascript:infoView();">
																		<img src="<%=root%>/images/ico/chakan.gif" width="15"
																			height="15" class="img_s">查看</a>
																</td>	
																<td width="5"></td>															
																
																<td width="50">
																	<a class="Operation" href="javascript:goback();">
																		<img src="<%=root%>/images/ico/ht.gif" width="15"
																			height="15" class="img_s">返回</a>
																</td>
																<td width="5"></td>																					
															</tr>
														</table>
													</td>
												</tr>
											</table>
										</td>
									</tr>
								</table>	
							<table width="100%" id="myTable_div" cellpadding=0 cellspacing=0
								onmousedown="downBody(this)" onmouseover="moveBody(this)" 
								onmouseup="upBody(this)" onselectstart="selectBody(this)">
								<div id="myDiv"
									style="display:none; height:201px; left:12px; position:absolute; top:50px; width:28px; z-index:1">
									<hr id="myLine" width="1" size="200" noshade color="#F4F4F4">
								</div>
								<!-- 定位表格,距离顶,左,右2个像素,-->
								<tr>
									<td valign=top align="left">
										<!-- 定位表格包含数据表格开始-->
										<!-- 数据显示表格定义开始-->
										<!--表格定义 直接拷贝 idColNums定义为表格相应的数据区的ID列　nameColNum定义为表格相应的数据区的名称列(列值为下面Td序号从0开始)-->
										<table id="myTable" style="vertical-align: top;" align="left"
											cellSpacing=1 cellPadding=1 border=0 class="table1"
											width="100%" height="100%" style="overflow: auto;" >
											<!--thead 表格Title定义开始，表格的宽度定义不要在这里定义放到数据区定义，所有TD的class="headerTD"-->
											<!--第一个Td为空-->
											<!--第二个Td为全选复选框，该复选框的ID　一定要为　id="checkall"-->
											<!--第三个Td及以后是显示数据区的Title定义所有的TD class="headerTD"-->
											<thead>
												<tr
													style="position:relative;top:expression(this.parentElement.offsetParent.parentElement.scrollTop);z-index:1;">
													<th style="display:none"></th>
													<th class="biao_bg2" align="center" width="4%">
														<input id="checkall" type="checkbox"
															onclick="checkAll(this,document.getElementById('myTable_td'),'#A9B2CA',true)">
													</th>
													<s:iterator value="columnList" var="rowtitle"
														status="status">
														<s:if test="infoItemDatatype!=\"15\"">
															<th class="biao_bg2" nowrap width="4%" height="" showsize="15" 
																onmousemove="moveCol(this,document.getElementById('myTable_div'))"
																onclick="sort(this,true)">
															<s:property value="infoItemSeconddisplay"/>
															</th>
														</s:if>
													</s:iterator>
													<th class="biao_bg2" width="*%" style="text-indent: 0px;"></th>
													<s:set value="columnList.size()" name="procount" />
												</tr>
											</thead>
											<!--thead 表格Title定义结束-->
											<!--tbody 表格数据区定义开始-->
											<!--第一个TD是提供记录ID值，不会在页面上显示-->
											<!--第二个ＴＤ的复选框，该Td的id一定要为　id="chkButtonTd"；　该复选框的值为记录ID值，复选框的ID和Name一定要为　id="chkButton" name="chkButton"-->
											<!--第三个TD及后面是显示数据区的定义，每项数据前加&nbsp;　如果需要在这里定义表格宽度-->
											<tbody oncontextmenu="chickRightMouse(event)"
												onmousedown="TableMouseDown('#A9B2CA')">
												<s:iterator value="infopage.result" id="infoRowList"
													status="status">
													<tr>
														<s:iterator value="infoRowList" id="property"
															status="status">
															<s:if test="infoItemDatatype==\"15\"">
																<td style="display:none">
																	<s:property value="infoItemValue" />
																</td>
																<td align="center" id="chkButtonTd" width="4%"
																	class="td1" style="text-indent: 0px;" align="center"
																	onmousemove="moveCol(this,document.getElementById('myTable_div'))">
																	<input value="<s:property value="infoItemValue"/>"
																		showValue="<s:property value="infoItemValue"/>"
																		onclick="checkValue(this,document.getElementById('myTable_td'),'#A9B2CA',true);"
																		type="checkbox" id="chkButton" name="chkButton" />
																</td>
															</s:if>
															<s:else>
																<td nowrap align="left" width="4%" class="td1">
																	<s:if test="infoItemDatatype==\"1\"">
																		<s:property value="refernceDesc" />
																	</s:if>
																	<s:elseif test="infoItemDatatype==\"11\"">
																		<s:if test="infoItemValue!=null">
																			<img width='30' height='30'
																				src='<%=path%>/<s:property value="infoItemValue"/>' />
																		</s:if>
																	</s:elseif>
																	<s:else>
																		<s:property value="infoItemValue" />
																	</s:else>
																</td>
															</s:else>
														</s:iterator>
														<td class="td1" width="*%" style="text-indent: 0px;"></td>
													</tr>
												</s:iterator>
											</tbody>
											<!--tbody 表格数据区定义结束-->
											<!--tfoot 表格提示区定义开始-->
											<tfoot>
												<tr style="display:none">
													<td  id="myTable_td" colspan="<s:property value="#procount+2"/>" align="left" class="td1">
														请选择：</td>
												</tr>
												<script>
													setTableStatus(myTable_td); setFootNum(1);
												</script>
											</tfoot>
											<!--tfoot 表格提示区定义开始-->
										</table>
									</td>
								</tr>
								<tr>
									<td>
										<!-- 数据显示表格定义结束-->
										<!-- 分页显示表格定义开始-->
										<table width="100%" border="0" cellspacing="0" cellpadding="0">
											<tr class="biao_bg3">
												<td width="2%">
													&nbsp;
													<s:if test="infopage.orderBy!=null">
														<input type="hidden" id="orderBy" name="infopage.orderBy"
															value="${infopage.orderBy}">
													</s:if>
													<s:if test="infopage.order!=null">
														<input type="hidden" id="order" name="infopage.order"
															value="${infopage.order}">
													</s:if>
												</td>
												<td width="5%">
													当前
												</td>
												<td width="5%">
													${infopage.pageNo}/
													${infopage.totalPages }
												</td>
												<td width="2%">
													页
												</td>
												<td width="5%">
													每页
												</td>
												<td width="5%">
													<input id="pagesize" name="infopage.pageSize" type="text"
														size="2" value="${infopage.pageSize}">
												</td>
												<td width="6%">
													<input name="Submit" type="button" class="input_bg"
														value="设置" onclick="gotoPage()">
												</td>
												<td width="8%">
													数据总量
												</td>
												<td width="4%">
													${infopage.totalCount}
												</td>
												<td width="10%">
													&nbsp;
												</td>
												<s:if test="infopage.isHasPre()==true">
													<td width="6%">
														<input name="Submit2" type="submit" class="input_bg"
															value="首页" onclick="gotoPage(1)">
													</td>
													<td width="7%">
														<input name="Submit22" type="submit" class="input_bg"
															value="上一页" onclick="gotoPage(${infopage.pageNo-1})">
													</td>
												</s:if>
												<s:if test="infopage.isHasNext()==true">
													<td width="7%">
														<input name="Submit23" type="submit" class="input_bg"
															value="下一页" onclick="gotoPage(${infopage.pageNo+1})">
													</td>
													<td width="6%">
														<input name="Submit24" type="submit" class="input_bg"
															value="尾页"
															onclick="gotoPage(<s:property value="infopage.totalPages"/>)">
													</td>
												</s:if>
												<td width="4%" align="center" nowrap>
													转到
												</td>
												<td width="3%"  align="center">
													<input id="pageNo" name="infopage.pageNo" type="text" size="4"
														value="${infopage.pageNo}">
												</td>
												<td width="3%" align="center">
													页
												</td>
												<td width="4%" align="right">
													<input name="Submit242" type="submit" class="input_bg"
														value="跳转" onclick="gotoPage()">
												</td>
											</tr>
										</table>
										<!-- 分页显示表格定义结束-->
									</td>
								</tr>
							</table>
						</s:form>
					</td>
				</tr>
			</table>
			<IFRAME style="display: none;" id="delhelpframe"></IFRAME>
		</DIV>
	<script language="javascript">
		setTableBorder(myTable);
		setHasCheck("true");
		setOrColor('#ffffff');
		var sMenu = new Menu();
		var disLogo=document.getElementById("disLogo").value;
		function initMenuT(){
			sMenu.registerToDoc(sMenu);
			var item = null;
			if(disLogo!=null&&disLogo=="viewinfo"){
			}else{
				item = new MenuItem("<%=root%>/images/ico/tianjia.gif","添加","infoAdd",1,"ChangeWidthTable","checkMoreDis");
				sMenu.addItem(item);
				item = new MenuItem("<%=root%>/images/ico/bianji.gif","编辑","infoEdit",1,"ChangeWidthTable","checkOneDis");
				sMenu.addItem(item);
				item = new MenuItem("<%=root%>/images/ico/shanchu.gif","删除","infoDel",1,"ChangeWidthTable","checkOneDis");
				sMenu.addItem(item);
			}
			item = new MenuItem("<%=root%>/images/ico/chakan.gif","查看","infoView",1,"ChangeWidthTable","checkOneDis");
			sMenu.addItem(item);
			item = new MenuItem("<%=root%>/images/ico/fankui.gif","返回","goback",1,"ChangeWidthTable","checkOneDis");
			sMenu.addItem(item);
			sMenu.addShowType("ChangeWidthTable");
		    registerMenu(sMenu);
		  
		}
		//添加信息
		function infoAdd(){  
		    var orgId=document.getElementById("orgId").value;
		    var personId=document.getElementById("personId").value;
		    var infoSetCode=document.getElementById("infoSetCode").value;
			var url="<%=path%>/personnel/baseperson/person!initViewAddPerson.action?forward=addRelationInfo&orgId="+orgId+"&personId="+personId+"&infoSetCode="+infoSetCode;
			OpenWindow(url,520,450,window);			
		}
		//编辑一条数据
		function infoEdit(){
		    var orgId=document.getElementById("orgId").value;
		    var personId=document.getElementById("personId").value;
		    var infoSetCode=document.getElementById("infoSetCode").value;
       		var selectedValue = getValue();
			if(selectedValue == ""){
				alert("请选择要编辑的记录！");
				return ;
			}else{
				if(selectedValue.split(",").length>1){
					alert("请选择一条记录编辑！");
					return ;
				}
			}
       		var url="<%=path%>/personnel/baseperson/person!initEditPerson.action?forward=editRelationInfo&orgId="+orgId+"&personId="+personId+"&infoSetCode="+infoSetCode+"&keyid="+selectedValue;
			OpenWindow(url,520,450,window);	
		}
	
		// 删除行数据
		function infoDel(ids,names){
		    var orgId=document.getElementById("orgId").value;
		    var personId=document.getElementById("personId").value;
		    var infoSetCode=document.getElementById("infoSetCode").value;
     		var id=getValue();
			if(id == ""){
				alert("请选择要删除的记录！");
				return ;
			}
     		if(confirm('确定删除吗？')){
     			window.location="<%=path%>/personnel/baseperson/person!deleteRelationInfo.action?orgId="+orgId+"&personId="+personId+"&infoSetCode="+infoSetCode+"&keyid="+id;
     		}
		}	
		
		function infoView(){
			var orgId=document.getElementById("orgId").value;
		    var personId=document.getElementById("personId").value;
		    var infoSetCode=document.getElementById("infoSetCode").value;
       		var selectedValue = getValue();
			if(selectedValue == ""){
				alert("请选择要查看的记录！");
				return ;
			}else{
				if(selectedValue.split(",").length>1){
					alert("请选择一条记录查看！");
					return ;
				}
			}
       		var url="<%=path%>/personnel/baseperson/person!initEditPerson.action?forward=viewRelationInfo&orgId="+orgId+"&personId="+personId+"&infoSetCode="+infoSetCode+"&keyid="+selectedValue;
			OpenWindow(url,520,450,window);	
		}			
	
		function gotoPage(no){
			if(no!=null&&no!=""){
				document.getElementById('pageNo').value=no;
			}
			document.getElementById('myTableForm').submit();
		}
		
		function submitForm(){
	    	document.forms[0].submit();
	    }
	    
	    function goback(){
		    var orgId=document.getElementById("orgId").value;
			window.parent.location="<%=path%>/personnel/baseperson/person.action?orgId="+orgId;
		}
</script>
</BODY>
</HTML>
