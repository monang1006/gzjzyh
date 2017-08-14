<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-tree" prefix="tree"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>选择人员</TITLE>
		<%@include file="/common/include/meta.jsp" %>
		<LINK href="<%=frameroot%>/css/properties_windows_add.css" type=text/css rel=stylesheet>
		<LINK href="<%=path%>/common/css/treeview.css" type=text/css rel=stylesheet>
		<script type="text/javascript">
			var imageRootPath='<%=path%>/common/frame';
		</script>
		<SCRIPT src="<%=root%>/common/js/mztree_check/mztreeview_check.js"></SCRIPT>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/btn.js" type="text/javascript"></script>
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
					var info = getInfo();
					var parWin = window.dialogArguments;
					// 栏目选择权限新加  start 已选查看的  不能选管理 反之也是
					var selectIdName = '<%=request.getParameter("selectId")%>';//
					var selectNameName = '<%=request.getParameter("selectName")%>';//
					var selectId;
					var selectName;
					if(selectIdName && selectIdName!='null'){
						selectId  = parWin.document.getElementById(selectIdName).value;
					}
					if(selectNameName && selectNameName!='null'){
						selectName = parWin.document.getElementById(selectNameName).value;
					}
					//alert(selectId)
					//alert(selectName)
					if(selectId!=null&&selectId!=""){
						var selectIds = selectId.split(",");
						var sss = info[0].split(",");
						var nnn = info[1].split(",");
						for(var i=0;i<sss.length;i++){
							for(var j=0;j<selectIds.length;j++){
								if(sss[i]==selectIds[j]){
									alert("人员："+nnn[i]+" 不能同时设置查看权限和管理权限，请重新选择。");
									return;
								}
							}
						}
					}
					//栏目选择权限新加 end
					
					var elementId = '<%=request.getParameter("elementId")%>';//父页面中存储ID的域
					var elementName = '<%=request.getParameter("elementName")%>';//父页面中存储姓名的域
					var isNeedSet = '<%=request.getParameter("isNeedSet")%>';//是否需要直接赋值给父页面.
					var defaultElementId = "orguserid";
					var defaultElementName = "orgusername";
					if(elementId && elementId!='null'){
						defaultElementId = elementId;
					}
					if(elementName && elementName!='null'){
						defaultElementName = elementName;
					}
					var orgusername = parWin.document.getElementById(defaultElementName);
					var orguserid   = parWin.document.getElementById(defaultElementId);
					if(orguserid && isNeedSet=='null'){
						orguserid.value = info[0];
					}
					if(orgusername && isNeedSet=='null'){
						orgusername.value = info[1];
					}
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
			//初始化,根据父窗口的文本框加载
			function initCheck(){
				var parWin = window.dialogArguments;
				var elementId = '<%=request.getParameter("elementId")%>';
				var elementName = '<%=request.getParameter("elementName")%>';
				var defaultElementId = "orguserid";
				var defaultElementName = "orgusername";
				if(elementId && elementId!='null'){
					defaultElementId = elementId;
				}
				if(elementName && elementName!='null'){
					defaultElementName = elementName;
				}
				var orgusername = parWin.document.getElementById(defaultElementName);
				var orguserid   = parWin.document.getElementById(defaultElementId);
				if(orgusername && orguserid){
					var username = orgusername.value;
					var userid   = orguserid.value;
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
				//设置已选框的边框
				$('#sel_person').parent().css('border','gray 1px solid');
				
			});
		</script>
		
	</HEAD>
	<BODY oncontextmenu="return false;">
		<DIV id=contentborder style="overflow: auto;height: 100%;" cellpadding="0">
				<table width="100%" height="100%;" border="0" cellpadding="0" cellspacing="0" bordercolor="#FFFFFF">
				<tr>
				<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
								<strong>选择人员</strong>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
					                <tr>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onClick="submitForm();">&nbsp;确&nbsp;定&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
					                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
					                 	<td class="Operation_input1" onClick="closeWindow();">&nbsp;取&nbsp;消&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
				                  		<td width="6"></td>
					                </tr>
					            </table>
							</td>
						</tr>
					</table>
				</td>
				</tr>
					
					<tr height="90%" >
						<td width="100%">
							<table width="100%" style="height: 100%;" border="1" cellpadding="0" cellspacing="0" bordercolor="#FFFFFF">
								<tr>
								<%--
									<td width="20%" height="405" style="border-top-color: gray;">
										<DIV id=contentborder style="background-color: white;" cellpadding="0">
											<tree:strongtree title="组织机构"  check="false" dealclass="com.strongit.oa.address.util.AddressOrgPersonDeal" data="${groupLst}" target="project_work_content"  iconpath="frame/theme_gray/images/"/>	
											
										</DIV>
									</td>
									<td width="42%" height="405" bgcolor="#FFFFFF" style="border-top-color: #FFFFFF;">
										<DIV style="background-color: white;" style="overflow: no;"  cellpadding="0">
											<iframe frameborder=0 src="<%=root%>/address/address!showlist.action"" width=100% name="project_work_content" id="project_work_content" height=100% scrolling="no"></iframe>

								  		</DIV>	
							  	    </td>--%>
								
								
									<td>
										<DIV class="zzjg" id=contentborder style="background-color: white;" cellpadding="0">
											 <iframe id="selectsTree" name="selectsTree" src="<%=path%>/address/address!selectperson.action?isShowAllUser=<%=request.getParameter("isShowAllUser") %>&typewei=${typewei}"
															 frameborder="0" scrolling='auto' width='100%' height='100%'
															style="display: "></iframe>
										</DIV>
									</td>
<%--									<td width="5%" height='100%' >
										<DIV id=contentborder style="background-color: white;text-align: center;" cellpadding="0">
									        <p>&nbsp;								          </p>
									        <p>&nbsp;</p>
									        <p>&nbsp;</p>
									        <p><input id="left" type="button" onclick="removeSelected();"  class="input_bg" value=" <- " /></p>
									        <p>
									          <input id="left" type="button" onclick="removeSelect();"  class="input_bg" value=" <<- " />
								                              </p>
										</DIV>	
								  </td>--%>
								  <td width="21%" height="405" style="border-top-color: #FFFFFF;">
										<DIV id=contentborder style="background-color: white;" cellpadding="0">
										 <table width="100%" height="350" border="0" align="center" cellpadding="0" cellspacing="0" bordercolor="#FFFFFF">
										 	<tr><td height="110" align="center">
									        <%--<input id="btn_accept_right" type="button" class="input_bg" value="-->" title="添加左侧选中的收件人"/><br><br>--%>
									        
									        <a id="btn_accept_left"  href="#" class="button" onClick="removeSelected();" title="移除右侧选中的收件人">&lt;--</a><br><br>
									        <s:if test="typewei!='weipai'">
									        <a id="btn_all_select"  href="#" class="button" onClick="allSelect();" title="全选添加左侧的收件人">-&gt;&gt;</a><br><br>
									        <a id="btn_all_delete"  href="#" class="button" onClick="removeSelect();" title="全选删除右侧的收件人">&lt;&lt;-</a>
									        </s:if>
									       <%--<input id="btn_accept_left" type="button" onclick="removeSelected();" style="width: 30"  class="input_bg" value="<--" title="移除右侧选中的收件人"/><br><br>
									        <input id="btn_all_select" type="button"  onclick="allSelect();" style="width: 30" class="input_bg" value="->>" title="全选添加左侧的收件人"/><br><br>
									        <input id="btn_all_delete" type="button"  onclick="removeSelect();" style="width: 30" class="input_bg" value="<<-" title="全选删除右侧的收件人"/>--%>
							              </td></tr>
									      
									     </table>
								  </DIV>	
								  </td>
<%--									<td width="30%" height='100%'>
									  <select id="sel_person" multiple="multiple"  style="width:80%;height: 100%;">
								      </select>
								  	</td>
								  	--%>
								  	<td width="5%" height="405" >
                                    <div style="width:200px; height:100%;border:1px #848284 solid; ">
									
												<div style="height:30px; line-height:30px; padding-left:5px;">已选择：</div>

                                            
												  <select id="sel_person" multiple="multiple" size="7" style="width:180px;height:418px; margin-left:5px;">
											      </select>
											
                                     </div>   
								  </td>
								</tr>
						  </table>
					  </td>
					</tr>
					<tr>
							<td class="table1_td"></td>
							<td></td>
						</tr>
			  </table>
		</DIV>
	</BODY>
</HTML>
