<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>

<HTML>
	<HEAD>
		<TITLE>操作内容</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<!--右键菜单样式 -->
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet">
		<link href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
		<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<!--右键菜单脚本 -->
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
	</HEAD>
	<BODY class=contentbodymargin oncontextmenu="return false;" onload="initMenuT()">
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
				<tr>
					<td height="100%">
						<s:form id="myTableForm" theme="simple" action="/infotable/infoTable.action">
							<input type="hidden" name="querycondition">
							<input type="hidden" name="infoItems" value="${infoItems}" />
							<input type="hidden" name="functionModule" value="${functionModule}">
							<input type="hidden" name="tableName" value="${tableName}" />
							<input type="hidden" name="pkey" value="${pkey}" />
							<input type="hidden" name="fpro" value="${fpro}" />
							<input type="hidden" name="fid" value="${fid}">
							<input type="hidden" name="funcPro" value="${funcPro}" />
							<input type="hidden" name="otherPro" value="${otherPro}" />
							<input type="hidden" name="funcid" value="${funcid}">
							<input type="hidden" name="struct" value="${struct}">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td height="40" style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
										<table width="100%" border="0" cellspacing="0" cellpadding="00">
											<tr>
												<td>&nbsp;</td>
												<td width="30%">
													<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9" onclick="search()">&nbsp;
													${tableDesc}列表
												</td>
												<td width="15%">
													&nbsp;
												</td>
												<td width="55%">
													<table width="100%" border="0" align="right" cellpadding="0" cellspacing="0">
														<tr>
															<td width="*">
																&nbsp;
															</td>
															<td >
																<a class="Operation" href="javascript:infoAdd();"><img class="img_s" src="<%=root%>/images/ico/tianjia.gif" width="15" height="15">增加&nbsp;</a>
															</td>
															<td width="5"></td>
															<td >
																<a class="Operation" href="javascript:infoEdit();"><img class="img_s" src="<%=root%>/images/ico/bianji.gif" width="15" height="15">编辑&nbsp;</a>
															</td>
															<td width="5"></td>
															<td >
																<a class="Operation" href="javascript:infoDel();"><img class="img_s" src="<%=root%>/images/ico/shanchu.gif" width="15" height="15">删除&nbsp;</a>
															</td>
															<td width="5"></td>
															<td >
																<a class="Operation" href="javascript:searchRow();"><img class="img_s" src="<%=root%>/images/ico/queding.gif" width="15" height="15">选择列&nbsp;</a>
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
							<table width="100%" id="myTable_div" cellpadding=0 cellspacing=0 onmousedown="downBody(this)" onmouseover="moveBody(this)" onmouseup="upBody(this)" onselectstart="selectBody(this)">
								<div id="myDiv" style="display:none; height:201px; left:12px; position:absolute; top:50px; width:28px; z-index:1">
									<hr id="myLine" width="1" size="200" noshade color="#F4F4F4">
								</div>
								<!-- 定位表格,距离顶,左,右2个像素,-->
								<tr>
									<td valign=top align="left">
										<!-- 定位表格包含数据表格开始-->
										<!-- 数据显示表格定义开始-->
										<!--表格定义 直接拷贝 idColNums定义为表格相应的数据区的ID列　nameColNum定义为表格相应的数据区的名称列(列值为下面Td序号从0开始)-->
										<table id="myTable" style="vertical-align: top;" align="left" cellSpacing=1 cellPadding=1 border=0 class="table1" width="100%" height="100%">
											<!--thead 表格Title定义开始，表格的宽度定义不要在这里定义放到数据区定义，所有TD的class="headerTD"-->
											<!--第一个Td为空-->
											<!--第二个Td为全选复选框，该复选框的ID　一定要为　id="checkall"-->
											<!--第三个Td及以后是显示数据区的Title定义所有的TD class="headerTD"-->
											<thead>
												<tr style="position:relative;top:expression(this.parentElement.offsetParent.parentElement.scrollTop);z-index:1;">
													<td style="display:none"></td>
													<th class="biao_bg2">
														<input id="checkall" type="checkbox" onclick="checkAll(this,document.getElementById('myTable_td'),'#A9B2CA',true)">
													</th>
													<s:iterator value="dataRowTitle" var="rowtitle" status="status">
														<s:if test="infoItemDatatype!=\"15\"">
															<th class="biao_bg2" width="" height="" onmousemove="moveCol(this,document.getElementById('myTable_div'))" onclick="sort(this,true)">
																<s:property value="infoItemSeconddisplay" />
															</th>
														</s:if>
													</s:iterator>
													<th class="biao_bg2" style="text-indent: 0px;"></th>
													<s:set value="dataRowTitle.size()" name="procount" />
												</tr>
											</thead>
											<!--thead 表格Title定义结束-->
											<!--tbody 表格数据区定义开始-->
											<!--第一个TD是提供记录ID值，不会在页面上显示-->
											<!--第二个ＴＤ的复选框，该Td的id一定要为　id="chkButtonTd"；　该复选框的值为记录ID值，复选框的ID和Name一定要为　id="chkButton" name="chkButton"-->
											<!--第三个TD及后面是显示数据区的定义，每项数据前加&nbsp;　如果需要在这里定义表格宽度-->
											<tbody oncontextmenu="chickRightMouse(event)" onmousedown="TableMouseDown('#A9B2CA')">
												<s:iterator value="page.result" id="infoRowList" status="status">
													<tr>
														<s:iterator value="infoRowList" id="property" status="status">
															<s:if test="infoItemDatatype==\"15\"">
																<td style="display:none">
																	<s:property value="infoItemValue" />
																</td>
																<td align="center" width="4%" id="chkButtonTd" class="td1" style="text-indent: 0px;" align="center" onmousemove="moveCol(this,document.getElementById('myTable_div'))">
																	<input value="<s:property value="infoItemValue"/>" showValue="<s:property value="infoItemValue"/>" onclick="checkValue(this,document.getElementById('myTable_td'),'#A9B2CA',true);" type="checkbox" id="chkButton" name="chkButton" />
																</td>
															</s:if>
															<s:else>
																<td nowrap align="center" class="td1">
																	<s:if test="infoItemDatatype==\"1\"">
										 								<s:property value="refernceDesc" />
																	</s:if>
																	<s:elseif test="infoItemDatatype==\"11\"">
										  							    <s:if test="infoItemValue!=null">
																			<img width='30' height='30' src='<%=path%>/<s:property value="infoItemValue"/>' />
																		</s:if>
																	</s:elseif>
																	<s:else>
										  								<s:property value="infoItemValue" />
																	</s:else>
																</td>
															</s:else>
														</s:iterator>
														<td class="td1" style="text-indent: 0px;"></td>
													</tr>
												</s:iterator>
											</tbody>
											<!--tbody 表格数据区定义结束-->
											<!--tfoot 表格提示区定义开始-->
											<tfoot>
												<tr>
													<td colspan="<s:property value="#procount+2"/>" align="left" class="td1" id="myTable_td">
														请选择:
													</td>
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
											<tr>
												<td width="2%">
													&nbsp;
													<s:if test="page.orderBy!=null">
														<input type="hidden" id="orderBy" name="page.orderBy" value="${page.orderBy}">
													</s:if>
													<s:if test="page.order!=null">
														<input type="hidden" id="order" name="page.order" value="${page.order}">
													</s:if>
												</td>
												<td width="5%">
													当前
												</td>
												<td width="4%">
													<s:if test="page.totalCount==-1">
														<s:set value="1" name="totalpages" />
													</s:if>
													<s:else>
														<s:set value="page.totalCount/page.pageSize" name="totalpages" />
														<s:if test="page.totalCount%page.pageSize>0">
															<s:set value="page.totalCount/page.pageSize+1" name="totalpages" />
														</s:if>
													</s:else>
													${page.pageNo}/
													<s:property value="#totalpages" />
												</td>
												<td width="2%">
													页
												</td>
												<td width="3%">
													每页
												</td>
												<td width="5%">
													<input id="pagesize" name="page.pageSize" type="text" size="2" value="${page.pageSize}">
												</td>
												<td width="6%">
													<input name="Submit" type="button" class="input_bg" value="设置" onclick="gotoPage()">
												</td>
												<td width="7%">
													数据总量
												</td>
												<td width="4%">
													${page.totalCount}
												</td>
												<td width="21%">
													&nbsp;
												</td>
												<s:if test="page.pageNo>1">
													<td width="6%">
														<input name="Submit2" type="submit" class="input_bg" value="首页" onclick="gotoPage(1)">
													</td>
													<td width="7%">
														<input name="Submit22" type="submit" class="input_bg" value="上一页" onclick="gotoPage(${page.pageNo-1})">
													</td>
												</s:if>
												<s:if test="page.pageNo<=#totalpages-1">
													<td width="7%">
														<input name="Submit23" type="submit" class="input_bg" value="下一页" onclick="gotoPage(${page.pageNo+1})">
													</td>
													<td width="6%">
														<input name="Submit24" type="submit" class="input_bg" value="尾页" onclick="gotoPage(<s:property value="#totalpages"/>)">
													</td>
												</s:if>
												<td width="3%">
													转到
												</td>
												<td width="3%">
													<input id="pageNo" name="page.pageNo" type="text" size="4" value="${page.pageNo}">
												</td>
												<td width="2%">
													页
												</td>
												<td width="4%">
													<input name="Submit242" type="submit" class="input_bg" value="转" onclick="gotoPage()">
												</td>
												<td width="3%">
													&nbsp;
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
function initMenuT(){
	sMenu.registerToDoc(sMenu);
	var item = null;
	item = new MenuItem("<%=root%>/images/ico/tianjia.gif","增加","infoAdd",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/bianji.gif","编辑","infoEdit",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/shanchu.gif","删除","infoDel",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/shanchu.gif","选择列","searchRow",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}
	var tableDesc = "${tableDesc}";
	var struct = "${struct}";
	//增加一条数据
	function infoAdd(){
		var fpro ="${fpro}";
		var fid = "${fid}";
		var url = null;
		var pkey = document.getElementById("pkey").value;
		if(fpro!="null"&&fpro!=""){
			if(fid=="0"||fid==""){
				var parentablename = "${parentablename}";
				alert("请选择"+parentablename+"！");
				return;
			}	
			
			url = "<%=path%>/infotable/infoTable!input.action?struct="+struct+"&fpro="+fpro+"&fid="+fid+"&pkey="+pkey;
		}else{
			url = "<%=path%>/infotable/infoTable!input.action?struct="+struct+"&pkey="+pkey;
		}
		//window.open(url);
		window.showModalDialog(url,window,'dialogWidth:500pt ;dialogHeight:500pt;status:no;help:no;scroll:no;status:0;help:0;scroll:0;');
		
	}
	//编辑一条数据
	function infoEdit(){
		if(checkSelectedOneDis()){
       		var selectedValue = getValue();
       		var pkey = document.getElementById("pkey").value;
       		var url = "<%=path%>/infotable/infoTable!input.action?struct="+struct+"&keyid="+selectedValue+"&pkey="+pkey;
			window.showModalDialog(url,window,'dialogWidth:500pt ;dialogHeight:500pt;status:no;help:no;scroll:no;status:0;help:0;scroll:0;');
		}
	}
	
	// 删除行数据
	function infoDel(ids,names){
		 if(checkSelectedMoreDis()){
     		var selectedValue = getValue();
     		if(confirm('确定要删除所有选中的'+tableDesc+'?')){
      			//window.location=contextPath+"/lwc/sysapp/DelTable.do?tableName=<c:out value="${tableForm.infoSetValue}"/>&fpro=<c:out value="${fpro}"/>&fid=<c:out value="${fid}"/>&delids="+selectedValue;
     			document.getElementById("delhelpframe").src="<%=path%>/infotable/infoTable!delete.action?struct="+struct+"&keyid="+selectedValue;
     			submitForm();
     		}
   		}
	}
	
	function gotoSearch(){
		var url= '<%=root%>/query/initalEasyQuery.do?tableName=<c:out value="${tableForm.infoSetValue}"/>&fpro=<c:out value="${fpro}"/>&fid=<c:out value="${fid}"/>&search=chaxun';
  			
		window.showModalDialog(url,580,400,window);
	}
	
	function searchRow(){
			var url= '<%=root%>/infotable/infoTable!rowlist.action?forward=searchrow&struct='+struct;
  			showModalDialog(url,window,'dialogWidth:300pt ;dialogHeight:350pt;status:no;help:no;scroll:no;status:0;help:0;scroll:0;');

	}
	function setSelectRow(str){
		document.all.infoItems.value=str;
	}
	
	function showreport(){
		var selectedValue = getValue();
		var url= '<%=root%>/report/reportAction.do?type=<c:out value="${tableForm.infoSetValue}"/>&id='+selectedValue;
  			
		OpenWindow(url,580,400,window);
	}

	function gotoPage(no){
			if(no!=null&&no!="")
				document.getElementById('pageNo').value=no;
			document.getElementById('myTableForm').submit();
	}
	function submitForm(){
    	document.forms[0].submit();
    }
</script>
	</BODY>
</HTML>
