<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-tree" prefix="tree"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>选择人员</TITLE>
		<%@include file="/common/include/meta.jsp" %>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
		<LINK href="<%=path%>/common/css/treeview.css" type=text/css rel=stylesheet>
		<script type="text/javascript">
			var imageRootPath='<%=path%>/common/frame';
		</script>
		<SCRIPT src="<%=root%>/common/js/mztree_check/mztreeview_check.js"></SCRIPT>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/btn.js" type="text/javascript"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/service.js"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></script>
		<style type="text/css">
			#contentborder{
				BORDER-RIGHT: #848284 1px solid;
				PADDING-RIGHT: 3px;
				BORDER-TOP: #848284 1px solid;
				PADDING-LEFT: 3px;
				BACKGROUND: white;
				PADDING-BOTTOM: 10px;
				OVERFLOW: auto;
				BORDER-LEFT: #848284 1px solid;
				PADDING-TOP: 0px;
				BORDER-BOTTOM: #848284 1px solid;
			}
			
		</style>
		<script type="text/javascript">
			var loadCount = 0;
				//确定
				function submitForm(){
					var parWin = window.dialogArguments;
					var orgusername1 = parWin.document.getElementById("orgusername1");
					var orguserid1   = parWin.document.getElementById("orguserid1");
					if(undefined == orgusername1 || undefined == orguserid1){
						alert("ID为orgusername1或orguserid1的文本框不存在！");
					}else{
						var info = getInfo();
						orgusername1.value = info[1];
						orguserid1.value = info[0];
						window.close();
					}
					
				}
				//向选择框中添加
				function addOpt(strName,strValue){
					if(!hasSelect(strName,strValue)){
						var objSelect = document.getElementById("sel_person");
						var  objOption  =  new  Option(strName,strValue);  
			      		objSelect.options.add(objOption, objSelect.options.length);
					}
				}
				//判断是否已经选择
				function hasSelect(strName,strValue){
					var isExist = false;
					var objSelect = document.getElementById("sel_person");
					for(var i=0;i<objSelect.options.length;i++){
						if(strName == objSelect.options[i].text && objSelect.options[i].value == strValue){
							isExist = true;
							break;
						}
					}
					return isExist;
				}
				
				//删除选择框中的
				function removeOpt(id){
					var objSelect = document.getElementById("sel_person");
					for(var i=0;i<objSelect.options.length;i++){
						if(id == objSelect.options[i].value){
							objSelect.removeChild(objSelect.options[i]);
						}
					}
					
				}
				//取消
				function closeWindow(){
					window.close();
				}
				//删除
				function removeSelect(){
					var sel = $("#sel_person");
					var frameTree = document.frames[0];
					frameTree.cancelChk();
					sel.find("option").each(function(){
					 	sel.get(0).removeChild(this);
					});
				};
			

			//获取选择的人员
			function getInfo(){
				var info = new Array();
				info[0] = "";
				info[1] = "";
				$("option").each(function(){
					var id = this.value;
					var txt = this.text;
					info[0] += id +",";
					info[1] += txt+",";
				});
				if(info[0].length>0){
					info[0] = info[0].substring(0,info[0].length-1);
					info[1] = info[1].substring(0,info[1].length-1);
				}
				return info;
			}

			//按部门、岗位、用户组选择人员
			function changeTree(type){
				document.getElementById("selectsTree").src = "<%=root%>/infopub/column/column!choosetree.action?chooseType="+type;
			}
			//初始化,根据父窗口的文本框加载
			function initCheck(){
				var parWin = window.dialogArguments;
				var orgusername1 = parWin.document.getElementById("orgusername1");
				var orguserid1   = parWin.document.getElementById("orguserid1");
			 	if(undefined == orgusername1 || undefined == orguserid1){
					alert("ID为orgusername或orguserid的文本框不存在！");
				}else{
					var username = orgusername1.value;
					var userid   = orguserid1.value;
					var usernames = username.split(",");
					var userids   = userid.split(",");
					if(userids!=""){
						for(var i=0;i<userids.length;i++){
							addOpt(usernames[i],userids[i]);
						}
					}
				}
			}
			$(document).ready(function(){
				initCheck();
			});
		</script>
		
	</HEAD>
	<BODY oncontextmenu="return false;">
		<DIV id=contentborder style="overflow: auto;height: 100%;" cellpadding="0">
				<table width="100%" height="100%;" border="0" cellpadding="0" cellspacing="0" bordercolor="#FFFFFF">
					<tr height="5%;">
						<td  colspan="2" style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);"><img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;&nbsp;&nbsp;&nbsp;选择人员</td>
					</tr>
					<tr>
						<td>
							<input type="radio" name="tree" checked="checked"
								onclick="changeTree('bm');" value="bm" />
							按部门
							<input type="radio" name="tree" onclick="changeTree('gw');"
								value="gw" />
							按岗位
							<input type="radio" name="tree" onclick="changeTree('yhz');"
								value="yhz" />
							按用户组
						</td>
					</tr>
					<tr height="85%" >
						<td width="100%">
							<table width="100%" style="height: 100%;" border="1" cellpadding="0" cellspacing="0" bordercolor="#FFFFFF">
								<tr>
									<td>
										<DIV class="zzjg" id=contentborder style="background-color: white;" cellpadding="0">
											 <iframe id="selectsTree" name="selectsTree" src="<%=root%>/infopub/column/column!choosetree.action?chooseType=bm";
															frameborder="0" scrolling='yes' width='100%' height='100%'
															style="display: "></iframe>
										</DIV>
									</td>
									
									<td width="15%" >
										<DIV id=contentborder style="background-color: white;text-align: center;" cellpadding="0">
									        <p>&nbsp;								          </p>
									        <p>&nbsp;</p>
									        <p>&nbsp;</p>
									        <p>&nbsp;</p>
									        <p>&nbsp;</p>
									        <p>
									          <input id="left" type="button" onclick="removeSelect();"  class="input_bg" value=" <<- " />
								                              </p>
										</DIV>	
								  </td>
									<td width="40%">
										<DIV id=contentborder style="background-color: white;" cellpadding="0">
										<table width="100%" border="0" cellpadding="0" cellspacing="0" bordercolor="#FFFFFF">
											<tr>
											  <td align="center">
												  <select id="sel_person" multiple="multiple" size="7" style="width:198px;height: 314px;">
											      </select>
											  </td>
											</tr>
										</table>
										</DIV>	
								  </td>
								</tr>
						  </table>
					  </td>
					</tr>
					
					<tr>
						<td align="center">
							<input type="button" id="btnOK"  class="input_bg" onclick="submitForm();" icoPath="<%=root%>/images/ico/queding.gif" value="  确定"/>&nbsp;&nbsp;&nbsp;&nbsp;
							<input type="button" id="btnNO"  class="input_bg" onclick="closeWindow();" icoPath="<%=root%>/images/ico/quxiao.gif" value="  取消"/>
						</td>
					</tr>
			  </table>
		</DIV>
	</BODY>
</HTML>
