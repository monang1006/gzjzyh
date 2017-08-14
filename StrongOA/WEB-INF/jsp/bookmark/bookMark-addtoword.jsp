<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp" %>
<html>
	<head>
	<%@include file="/common/include/meta.jsp" %>
	<LINK href="<%=frameroot%>/css/properties_windows_add.css" type=text/css rel=stylesheet>
	<script language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
	<title>
		标签说明		
	</title>
		<script>
			$(document).ready(function(){
				var parentWin = window.dialogArguments;		//得到父窗口句柄
				var TANGER_OCX_OBJ = parentWin.document.getElementById("TANGER_OCX_OBJ");//得到OCX控件
				var doc =TANGER_OCX_OBJ.ActiveDocument;
				var bks = doc.Bookmarks;
				var bksCount = bks.Count;
				for(i=1;i<=bksCount ;i++){
					var name = bks(i).Name ;
					var value = TANGER_OCX_OBJ.GetBookmarkValue(name);
					var bookmarkHtml = "<tr flag='0' onclick='changeColor(this.id)' id='tr"+i+"'>";
						bookmarkHtml += "<td class=\"td1\" width=\"50%\" height=\"21\" class=\"biao_bg1\" align=\"center\">";
						bookmarkHtml += name;
						bookmarkHtml += "</td>";
						bookmarkHtml += "<td class=\"td1\" width=\"50%\"  height=\"21\" class=\"biao_bg1\" align=\"center\">";
						bookmarkHtml += value;
						bookmarkHtml += "</td>";
				        bookmarkHtml += "</tr>";
						
					$("#bookmark").append(bookmarkHtml);	
				} 
				
				
				
			});	
		
			//添加选择的标签至模板中
			function doSave(){
				var parentWin = window.dialogArguments;		//得到父窗口句柄
				var TANGER_OCX_OBJ = parentWin.document.getElementById("TANGER_OCX_OBJ");//得到OCX控件
				var doc =TANGER_OCX_OBJ.ActiveDocument;
				var objSelect = document.getElementById("select");
				var objOption = objSelect.options[objSelect.selectedIndex];
				if(!objOption){
					alert("请选择要添加的书签。");
					return ;
				}
				var name = objOption.value ;
				var text = objOption.text ;
				if(doc.Bookmarks.Exists(name)){
					TANGER_OCX_OBJ.SetBookmarkValue(name,"");
					doc.Bookmarks(name).Delete();
				}
				doc.Bookmarks.Add(name,doc.Application.Selection.range); 
				TANGER_OCX_OBJ.SetBookmarkValue(name,text);
				window.close();
			}
			
			//删除书签
			function doDel(){
				var parentWin = window.dialogArguments;		//得到父窗口句柄
				var TANGER_OCX_OBJ = parentWin.document.getElementById("TANGER_OCX_OBJ");//得到OCX控件
				var doc =TANGER_OCX_OBJ.ActiveDocument;
				if($("tr[flag='1']").size() == 0){
					alert("请点击要删除的标签。");
					return ;
				}
				var doFlag = false;
				$("tr[flag='1']").each(function(i){
					var id = this.id;
					var name = $("#"+id+" td:first-child").text();
					if(doc.Bookmarks.Exists(name)){
						TANGER_OCX_OBJ.SetBookmarkValue(name,"");
						doc.Bookmarks(name).Delete();
					}
					doFlag = true;
				});
				if(doFlag){
					window.close();
				}
			}
			
			//单击某行时修改其背景颜色
			function changeColor(currentTr){
				if($("#"+currentTr).attr("flag") == '0'){
					$("#"+currentTr+">td").css("background-color","#A9B2CA");
					$("#"+currentTr).attr("flag",'1')
				}else{
					$("#"+currentTr+">td").css("background-color","");
					$("#"+currentTr).attr("flag",'0')
				}
			}
			
			//返回
			function doReturn(){
				window.close();
			}	
		</script>
	</head>
	<body class="contentbodymargin">

		<DIV id=contentborder cellpadding="0" >
			<s:form action="/bookmark/bookMark!save.action" theme="simple" name="bookMarkForm" id="bookMarkForm">
			<s:hidden name="model.id"></s:hidden>
			<table width="100%" border="0" cellspacing="0" cellpadding="0" >
				<tr>
					<td colspan="3" class="table_headtd">
						<table width="100%" border="0" cellspacing="0" cellpadding="00" >
							<tr>
								<td class="table_headtd_img" >
									<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
								</td>
								<td align="left">
									<strong>标签说明</strong>
								</td>
								<td align="right">
									<table border="0" align="right" cellpadding="00" cellspacing="0">
						                <tr>
						                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
						                 	<td class="Operation_input" onclick="doSave();">&nbsp;添&nbsp;加&nbsp;</td>
						                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
					                  		<td width="5"></td>
						                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
						                 	<td class="Operation_input" onclick="doDel();">&nbsp;删&nbsp;除&nbsp;</td>
						                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
					                  		<td width="5"></td>
						                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
						                 	<td class="Operation_input1" onclick="doReturn();">&nbsp;返&nbsp;回&nbsp;</td>
						                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
					                  		<td width="6"></td>
					                	</tr>
					            	</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				
				<tr>
					<td align="center">
						<s:select id="select" cssStyle="width:70%;" list="bookMarkList" listKey="name" listValue="desc"></s:select>
					</td>
				</tr>						
				<tr>
					<td>
					<table width="100%" height="100%" border="0" cellpadding="0"
				cellspacing="0" align="center" class="table1" >
					<tr>
						<td>
							<table id="bookmark" width="100%" height="100%" border="0" cellpadding="0" cellspacing="1" align="center" >
									<tr>
									<td    align="center">
										标签名称 
									</td>
									<td   align="center"> 标签说明
									</td>
								</tr>
							<!--  这里显示此模板中已经设置的标签列表 
								<tr id="tr1">
									<td class="td1" width="10%" height="21" class="biao_bg1" align="right">
										
									</td>
									<td class="td1">
										
									</td>
								</tr>-->
							</table>
						</td>
					</tr>
					<!-- <tr align="center">
						<td class="td1">
							<br>
							<input id="btnSave" onclick="doSave();" type="button" class="input_bg" value="添加" />
							&nbsp;
							<input id="btnSave" onclick="doDel();" type="button" class="input_bg" value="删除" />
							&nbsp;
							<input id="btnReturn" onclick="doReturn();" type="button" class="input_bg" value="返回" />
						</td>
					</tr> -->
				</table>
					</td>
					</tr>
					</table>
					</s:form>
					
		</DIV>
	</body>

</html>
