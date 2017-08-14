<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@	include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>操作内容</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<LINK type=text/css rel=stylesheet href="<%=frameroot%>/css/properties_windows_list.css">
		<link type="text/css" rel="stylesheet" href="<%=frameroot%>/css/strongitmenu.css">
		<LINK type=text/css rel=stylesheet href="<%=frameroot%>/css/search.css">
		<script language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script language="javascript" src="<%=path%>/common/js/common/search.js"></script>
		<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
		<!--右键菜单脚本 -->
		<script language="javascript" src="<%=path%>/common/js/common/common.js"></script>
	</HEAD>
	<script type="text/javascript">
		var i=0;
		function showChecked(status,value,infoSetCode){
			var tempIndex=i;
			if(status=='read'){	//只读
				if(value=='0'){
					return "<input type='checkbox' name='c1' value='"+infoSetCode+"' onclick='readCheck(this,"+tempIndex+")'>"
				}else{	
					return "<input type='checkbox' name='c1' value='"+infoSetCode+"' onclick='readCheck(this,"+tempIndex+")' checked='checked'>"
				}
			}else if(status=='write'){// 读写
				if(value=='0'){
					return "<input type='checkbox' name='c2' value='"+infoSetCode+"' onclick='readwCheck(this,"+tempIndex+")'>"
				}else{	
					return "<input type='checkbox' name='c2' value='"+infoSetCode+"' onclick='readwCheck(this,"+tempIndex+")' checked='checked'>"
				}
			}else{		//隐藏
				i++;
				if(value=='0'){
					return "<input type='checkbox' name='c3' value='"+infoSetCode+"' onclick='hiddenCheck(this,"+tempIndex+")'>"
				}else{	
					return "<input type='checkbox' name='c3' value='"+infoSetCode+"' onclick='hiddenCheck(this,"+tempIndex+")' checked='checked'>"
				}
			}
		}
		 function readCheck(obj,index){  
		 	var postId=document.getElementById("postId").value;      
	       	var infoSetCode = obj.value;
	       	if(obj.checked==false){
	       		obj.checked=true;
	       		return;
	       	}
	       	$.ajax({
				type:"post",
				data:{
					infoSetCode:infoSetCode,
					postId:postId		
				},
				url:"<%=path%>/dataprivil/postDataPrivil!readSet.action",
				success:function(data){	
				  	if(data=="failed"){
				  		alert("设置失败！");
				  	}else{
				  		showTip('<div class="tip" id="loading">设置成功!</div>');
				  		document.getElementsByName("c2")[index].checked=false;
				  		document.getElementsByName("c3")[index].checked=false;
				  	}
				},
				error:function(data){
					alert("对不起，操作异常"+data);
				}
			});     
	    }
	    function readwCheck(obj,index){  
		      var postId=document.getElementById("postId").value;      
		       	var infoSetCode = obj.value;
		       	if(obj.checked==false){
		       		obj.checked=true;
		       		return;
		       	}
		       	$.ajax({
					type:"post",
					data:{
						infoSetCode:infoSetCode,
						postId:postId		
					},
					url:"<%=path%>/dataprivil/postDataPrivil!readWriteSet.action",
					success:function(data){	
					  	if(data=="failed"){
					  		alert("设置失败！");
					  	}else{
					  		showTip('<div class="tip" id="loading">设置成功!</div>');
					  		document.getElementsByName("c1")[index].checked=false;
				  			document.getElementsByName("c3")[index].checked=false;
					  	}
					},
					error:function(data){
						alert("对不起，操作异常"+data);
					}
				}); 
	    }
	    
	    function hiddenCheck(obj,index){
	     	var postId=document.getElementById("postId").value;      
	       	var infoSetCode = obj.value;
	       	if(obj.checked==false){
	       		obj.checked=true;
	       		return;
	       	}
	       	$.ajax({
				type:"post",
				data:{
					infoSetCode:infoSetCode,
					postId:postId		
				},
				url:"<%=path%>/dataprivil/postDataPrivil!hiddenSet.action",
				success:function(data){	
				  	if(data=="failed"){
				  		alert("设置失败！");
				  	}else{
				  		showTip('<div class="tip" id="loading">设置成功!</div>');
				  		document.getElementsByName("c1")[index].checked=false;
				  		document.getElementsByName("c2")[index].checked=false;
				  	}
				},
				error:function(data){
					alert("对不起，操作异常"+data);
				}
			}); 
	    }
	    
	    function checkallR(){
	    	var infoSetCode=getValue();
	    	var postId=document.getElementById("postId").value;   
	    	$.ajax({
				type:"post",
				data:{
					infoSetCode:infoSetCode,
					postId:postId		
				},
				url:"<%=path%>/dataprivil/postDataPrivil!readSet.action",
				success:function(data){	
				  	if(data=="failed"){
				  		alert("设置失败！");
				  	}else{
				  		showTip('<div class="tip" id="loading">设置成功!</div>');
				  		var readObjs=document.getElementsByName("c1");
				    	var readWObjs=document.getElementsByName("c2");
				    	var hiddenObjs=document.getElementsByName("c3");
				    	for(var i=0;i<readObjs.length;i++){
				    		if(infoSetCode==""||infoSetCode.indexOf(readObjs[i].value)!=-1){
					    		readObjs[i].checked=true;
					    		readWObjs[i].checked=false;
					    		hiddenObjs[i].checked=false;
				    		}
				    	}
				  	}
				},
				error:function(data){
					alert("对不起，操作异常"+data);
				}
			}); 
	    }
	    
	    function checkallRW(){
	    	var infoSetCode=getValue();
	    	var postId=document.getElementById("postId").value;   
	    	$.ajax({
				type:"post",
				data:{
					infoSetCode:infoSetCode,
					postId:postId		
				},
				url:"<%=path%>/dataprivil/postDataPrivil!readWriteSet.action",
				success:function(data){	
				  	if(data=="failed"){
				  		alert("设置失败！");
				  	}else{
				  		showTip('<div class="tip" id="loading">设置成功!</div>');
				  		var readObjs=document.getElementsByName("c1");
				    	var readWObjs=document.getElementsByName("c2");
				    	var hiddenObjs=document.getElementsByName("c3");
				    	for(var i=0;i<readWObjs.length;i++){
					    	if(infoSetCode==""||infoSetCode.indexOf(readWObjs[i].value)!=-1){
					    		readObjs[i].checked=false;
					    		readWObjs[i].checked=true;
					    		hiddenObjs[i].checked=false;
					    	}
				    	}
				  	}
				},
				error:function(data){
					alert("对不起，操作异常"+data);
				}
			}); 
	    }
	    
	    function checkallH(){
	   		var infoSetCode=getValue();
	    	var postId=document.getElementById("postId").value;   
	    	$.ajax({
				type:"post",
				data:{
					infoSetCode:infoSetCode,
					postId:postId		
				},
				url:"<%=path%>/dataprivil/postDataPrivil!hiddenSet.action",
				success:function(data){	
				  	if(data=="failed"){
				  		alert("设置失败！");
				  	}else{
				  		showTip('<div class="tip" id="loading">设置成功!</div>');
				  		var readObjs=document.getElementsByName("c1");
				    	var readWObjs=document.getElementsByName("c2");
				    	var hiddenObjs=document.getElementsByName("c3");
				    	for(var i=0;i<hiddenObjs.length;i++){
				    		if(infoSetCode==""||infoSetCode.indexOf(hiddenObjs[i].value)!=-1){
					    		readObjs[i].checked=false;
					    		readWObjs[i].checked=false;
					    		hiddenObjs[i].checked=true;
				    		}
				    	}
				  	}
				},
				error:function(data){
					alert("对不起，操作异常"+data);
				}
			}); 
	    }
	</script>
	<BODY class=contentbodymargin oncontextmenu="return false;">
		<DIV id=contentborder align=center>
			<s:form id="myTableForm" theme="simple" action="/dataprivil/postDataPrivil.action">
				<input type="hidden" id="postId" name="postId" value="${postId}">
				<table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
					<tr>
						<td height="100%">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td height="40" class="table_headtd">
										<table width="100%" border="0" cellspacing="0" cellpadding="00">
											<tr>
												<td class="table_headtd_img" >
													<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
												</td>
												<td align="left">
													<strong>设置岗位的信息集权限</strong>
												</td>
												<td align="right">
	            									<table border="0" align="right" cellpadding="0" cellspacing="0">
		               								 	<tr>
		               								 		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
										                 	<td class="Operation_list" onclick="checkallR();"><img src="<%=root%>/images/operationbtn/A_read-only_file.png"/>&nbsp;只&nbsp;读&nbsp;</td>
										                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
									                  		<td width="5"></td>
									                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
										                 	<td class="Operation_list" onclick="checkallRW();"><img src="<%=root%>/images/operationbtn/Read_and_write_files.png"/>&nbsp;读&nbsp;写&nbsp;</td>
										                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
									                  		<td width="5"></td>
									                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
										                 	<td class="Operation_list" onclick="checkallH();"><img src="<%=root%>/images/operationbtn/Hidden.png"/>&nbsp;隐&nbsp;藏&nbsp;</td>
										                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
									                  		<td width="5"></td>
									                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
										                 	<td class="Operation_list" onclick="window.close();"><img src="<%=root%>/images/operationbtn/close.png"/>&nbsp;关&nbsp;闭&nbsp;</td>
										                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
									                  		<td width="5"></td>
														</tr>
													</table>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
							<webflex:flexTable name="myTable" width="100%" height="370px"
								wholeCss="table1" property="dataPrivalId" isCanDrag="true"
								showSearch="false" isCanFixUpCol="true" clickColor="#A9B2CA"
								footShow="showCheck" isShowMenu="false" 
								getValueType="getValueByProperty" collection="${infoSetList}">
								<webflex:flexCheckBoxCol caption="选择" property="infoSet.infoSetCode"
									showValue="infoSet.infoSetName" width="5%" isCheckAll="true"
									isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexTextCol caption="信息集值" 
									property="infoSet.infoSetCode" showValue="infoSet.infoSetValue"
									width="30%" isCanDrag="true" isCanSort="false"></webflex:flexTextCol>
								<webflex:flexTextCol caption="信息集名称"
									property="infoSet.infoSetCode" showValue="infoSet.infoSetName"
									width="30%" isCanDrag="true" isCanSort="false"></webflex:flexTextCol>
								<webflex:flexTextCol caption="只读"
									property="postStructureReadonly"
									showValue="javascript:showChecked(read,postStructureReadonly,infoSet.infoSetCode)" width="10%" isCanDrag="true"
									isCanSort="false" align="center"></webflex:flexTextCol>
								<webflex:flexTextCol caption="读写"
									property="postStructureReadwrite"
									showValue="javascript:showChecked(write,postStructureReadwrite,infoSet.infoSetCode)" width="10%" isCanDrag="true"
									isCanSort="false" align="center"></webflex:flexTextCol>
								<webflex:flexTextCol caption="隐藏" property="postStructureHide"
									showValue="javascript:showChecked(hidden,postStructureHide,infoSet.infoSetCode)" width="10%" isCanDrag="true"
									isCanSort="false" align="center"></webflex:flexTextCol>
							</webflex:flexTable>
						</td>
					</tr>
				</table>
				<br>
				<table>
					<tr>
						<td>
						</td>
					</tr>
				</table>
			</s:form>
		</DIV>
	</BODY>
</HTML>
