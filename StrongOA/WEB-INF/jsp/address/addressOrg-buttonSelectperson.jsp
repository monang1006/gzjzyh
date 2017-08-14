<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-tree" prefix="tree"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>选择人员1</TITLE>
		<%@include file="/common/include/meta.jsp" %>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
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
					var maxActors = "${maxTaskActors}";
					if(info[0].split(",").length > parseInt(maxActors)){
						alert("允许最大参与人数为" + maxActors + "人，目前选择人数大于" + maxActors + "人，请重新选择！");
						return false;
					}
				  	var parWin = window.dialogArguments;
				  	var tranObj = $("input:radio:checked",parWin.document);
      				var nodeId = tranObj.attr("nodeid");
      				var selectTransId = tranObj.attr("id");
					if(info[0].length == 0){
						$("SPAN[id='users_"+nodeId+"'][transId='"+selectTransId+"']",parWin.document).html("");
		          		$("input[id=strTaskActors_"+nodeId+"][transId='"+selectTransId+"']",parWin.document).val("");
		          		$("input[id=taskActor_"+nodeId+"][transId='"+selectTransId+"']",parWin.document).val("");
					}else{
						var strTaskActors="";
	      				 var username=","+info[1];
	      				 var userid = info[0].split(",")
		          		//组合用户ID和节点ID传给后台处理
				          for(var j=0;j<userid.length;j++){
				          	if(userid[j]!=null && userid[j]!=''){
				          		strTaskActors+=","+userid[j]+"|"+nodeId;
				          	}
				          }
	      				if(username!=null){
	      		      		var index=username.indexOf("[");
	      		      		username=username.substring(0,index);
	      		      	 }
			            $("span[id=users_"+nodeId+"][transId='"+selectTransId+"']",parWin.document).html(username.substring(1));
	          	  		$("input[id=strTaskActors_"+nodeId+"][transId='"+selectTransId+"']",parWin.document).val(strTaskActors.substring(1));
					}
      				 
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
				var tranObj = $("input:radio:checked",parWin.document);
   				var nodeId = tranObj.attr("nodeid");
   				var selectTransId = tranObj.attr("id");
				var username = $("span[id=users_"+nodeId+"][transId='"+selectTransId+"']",parWin.document).html();
				var userid   = $("input[id=strTaskActors_"+nodeId+"][transId='"+selectTransId+"']",parWin.document).val();
				if(username && userid){
					var usernames = username.split(",");
					var userids   = userid.split(",");
					if(userids!=""){
						for(var i=0;i<userids.length;i++){
							addOpt(usernames[i],userids[i].split("|")[0]);
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
					
					<tr height="85%" >
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
											 <iframe id="selectsTree" name="selectsTree" src="<%=path%>/address/address!selectperson.action?isShowAllUser=<%=request.getParameter("isShowAllUser") %>"
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
								  <td width="6%" height="405" style="border-top-color: #FFFFFF;">
										<DIV id=contentborder style="background-color: white;" cellpadding="0">
										 <table width="100%" height="350" border="0" align="center" cellpadding="0" cellspacing="0" bordercolor="#FFFFFF">
										 	<tr><td height="110" align="center">
									        <%--<input id="btn_accept_right" type="button" class="input_bg" value="-->" title="添加左侧选中的收件人"/><br><br>
									        --%>
									        <input id="btn_accept_left" type="button" onclick="removeSelected();" style="width: 30"  class="input_bg" value="<--" title="移除右侧选中的收件人"/><br><br>
									        <input id="btn_all_select" type="button"  onclick="allSelect();" style="width: 30" class="input_bg" value="->>" title="全选添加左侧的收件人"/><br><br>
									        <input id="btn_all_delete" type="button"  onclick="removeSelect();" style="width: 30" class="input_bg" value="<<-" title="全选删除右侧的收件人"/>
							              </td></tr>
									      
									     </table>
								  </DIV>	
								  </td>
<%--									<td width="30%" height='100%'>
									  <select id="sel_person" multiple="multiple"  style="width:80%;height: 100%;">
								      </select>
								  	</td>
								  	--%>
								  	<td width="30%" height="405" style="border-top-color: #FFFFFF;">
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
					<tr><td></td></tr>
					<tr>
						<td align="center">
							<input type="button" id="btnOK"  class="input_bg" onclick="submitForm();" icoPath="<%=root%>/images/ico/queding.gif" value="  确定"/>&nbsp;&nbsp;&nbsp;&nbsp;
							<input type="button" id="btnNO"  class="input_bg" onclick="closeWindow();" icoPath="<%=root%>/images/ico/quxiao.gif" value="  取消"/>
						</td>
					</tr>
					<tr><td></td></tr>
			  </table>
		</DIV>
	</BODY>
</HTML>
