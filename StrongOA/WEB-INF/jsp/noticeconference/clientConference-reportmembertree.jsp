<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-tree" prefix="tree"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>单位人员上报_领导名册选择</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<LINK href="<%=path%>/common/css/treeview.css" type=text/css
			rel=stylesheet>
		<script type="text/javascript">
			var imageRootPath='<%=path%>/common/frame';
		</script>
		<SCRIPT src="<%=root%>/common/js/mztree_check/mztreeview_check.js"></SCRIPT>
		<script type="text/javascript"
			src="<%=path%>/scripts/easyui-1.3/jquery.js"></script>
		<script type="text/javascript"
			src="<%=path%>/scripts/easyui-1.3/jquery.easyui.min.js"></script>
		<script src="<%=path%>/common/js/common/btn.js" type="text/javascript"></script>
		<style type="text/css">
#contentborder {
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
				    var info=document.frames.selectsTree.getCurrentSelItes();				  
			        window.returnValue = info; // 返回数组对象
				 	window.close();					
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
					var objSelect = document.getElementById("sel_person");
					for(var i=(objSelect.options.length-1);i>=0;i--){  
					  if(objSelect.options[i]!=null){ 
					  		document.frames.selectsTree.removeSelected(objSelect.options[i].value);	//取消选择中项
					  		objSelect.removeChild(objSelect.options[i]); 
					       }   
					  }  
				};
			
			//删除选择中项
				function removeSelected(){
					var objSelect = document.getElementById("sel_person");
					for(var i=(objSelect.options.length-1);i>=0;i--){  
					  if((objSelect.options[i]!=null)&&(objSelect.options[i].selected ==true)){ 
							document.frames.selectsTree.removeSelected(objSelect.options[i].value);	//取消选择中项
					  		objSelect.removeChild(objSelect.options[i]);
					       }   
					  }  
					
				}
			
			//全选添加
			function allSelect(){
				//var objSelect = document.getElementById("sel_person");
				var objSelect = $("#sel_person");
				document.frames.selectsTree.Select(objSelect);
			}

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
				document.getElementById("selectsTree").src = "<%=path%>/address/addressOrg!choosetree.action?chooseType="+type;
			}
		 
		</script>

	</HEAD>
	<BODY oncontextmenu="return false;">
		<div align="center"><form id="formid" method="post">
			<input type="hidden" name="initleader" id="initleader" value="${initleader}"> 
		</form></div>
		<DIV id=contentborder style="overflow: auto; height: 100%;"
			cellpadding="0">
			<table width="100%" height="100%;" border="0" cellpadding="0"
				cellspacing="0" bordercolor="#FFFFFF">
				<tr height="5%;">
					<td colspan="2"
						style="FILTER: progid : DXImageTransform . Microsoft . Gradient(gradientType = 0, startColorStr = #ededed, endColorStr = #ffffff);">
						<img src=<%=frameroot%>/images/ico/ico.giff" width="9" height="9">
						&nbsp;&nbsp;&nbsp;&nbsp;选择人员
					</td>
				</tr>

				<tr height="85%">
					<td width="100%">
						<table width="100%" style="height: 100%;" border="1"
							cellpadding="0" cellspacing="0" bordercolor="#FFFFFF">
							<tr>
								<td>
									<DIV class="zzjg" id=contentborder
										style="background-color: white;" cellpadding="0">
										<iframe id="selectsTree" name="selectsTree"
											src="<%=path%>/noticeconference/clientConference!selectOrgPerson.action?isShowAllUser=<%=request.getParameter("isShowAllUser")%>"
											frameborder="0" scrolling='auto' width='100%' height='100%'
											style="display: "></iframe>
									</DIV>
								</td>
									<td width="30%" height="405" style="border-top-color: #FFFFFF; display:none" >
										<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0" bordercolor="#FFFFFF">
											<tr style="margin-top: 5px;" >
											  <td  >
												已选择：<br>
												  <select id="sel_person" multiple="multiple" size="7" style="width:220px;height:388px">
											      </select>
											  </td>
											</tr>
										</table>
								  </td>
							 
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td></td>
				</tr>
				<tr>
					<td align="center">
						<input type="button" id="btnOK" class="input_bg"
							onclick="submitForm();"
							icoPath="<%=root%>/images/ico/queding.gif" value="  确定" />
						&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="button" id="btnNO" class="input_bg"
							onclick="closeWindow();"
							icoPath="<%=root%>/images/ico/quxiao.gif" value="  取消" />
					</td>
				</tr>
				<tr>
					<td></td>
				</tr>
			</table>
		</DIV>
	</BODY>
</HTML>
