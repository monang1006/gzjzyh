<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/include/rootPath.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>增加文件</title>
		<link href="<%=frameroot%>/css/properties_windows_add.css" type="text/css" rel="stylesheet">
		<script type="text/javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script src="<%=path%>/common/js/upload/jquery.MultiFile.js" type="text/javascript" language="javascript"></script>
		<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<script src="<%=path%>/common/js/upload/jquery.blockUI.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/service.js"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></script>
	</head>
	<body class=contentbodymargin oncontextmenu="return false;">
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="100%">
						<s:form theme="simple" id="mytable" action="/archive/sort/archiveSort!input.action">
							<s:hidden id="sortId" name="modle.sortId"></s:hidden>
							<s:hidden id="sortParentNo" name="modle.sortParentNo"></s:hidden>
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td height="40" class="table_headtd">
										<table width="100%" border="0" cellspacing="0" cellpadding="00">
											<tr>
												<td class="table_headtd_img" >
													<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
												</td>
												<td align="left">
													<strong>类目管理</strong>
												</td>
												<td align="right">
													<table width="100%" border="0" align="right" cellpadding="0" cellspacing="0">
														<tr>
															<td >
															<table border="0" align="right" cellpadding="0" cellspacing="0">
																<tr>
																	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
												                 	<td class="Operation_input" onclick="create();">&nbsp;新&nbsp;建&nbsp;</td>
												                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
											                  		<td width="5"></td>
												                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
												                 	<td class="Operation_input" onclick="create1();">&nbsp;新建子类目&nbsp;</td>
												                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
											                  		<td width="5"></td>
											                  		<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
												                 	<td class="Operation_input" onclick="save();">&nbsp;保&nbsp;存&nbsp;</td>
												                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
											                  		<td width="5"></td>
											                  		<s:if test="modle.sortId==null">
											                  		<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
												                 	<td class="Operation_input" onclick="folderImport();">&nbsp;导&nbsp;入&nbsp;</td>
												                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
											                  		<td width="5"></td>
											                  		</s:if>
												                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
												                 	<td class="Operation_input" onclick="deleteObj();">&nbsp;删&nbsp;除&nbsp;</td>
												                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
											                  		<td width="5"></td>
											                  		<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
												                 	<td class="Operation_input1" onclick="back();">&nbsp;返&nbsp;回&nbsp;</td>
												                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
											                  		<td width="6"></td>
																</tr>
															</table>
															</td>
														</tr>
													</table>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
							<table width="100%" height="10%" border="0" cellpadding="0"
								cellspacing="1" align="center" class="table1">
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">类目序号：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<s:textfield  id="sortArrayNo" name="modle.sortArrayNo" maxlength="15"  size="30"></s:textfield>
									
									</td>
								</tr>
								<tr>
									<td width="15%" height="21" class="biao_bg1" align="right">
										<span class="wz"><FONT color="red">*</FONT>&nbsp;类目编号：</span>
									</td>
									<td class="td1" colspan="3" align="left">
									<s:textfield  id="sortNo"  name="modle.sortNo"  maxlength="15"  size="30"></s:textfield>
										
									</td>
								</tr>
								<tr>
									<td width="15%" height="21" class="biao_bg1" align="right">
										<span class="wz"><FONT color="red">*</FONT>&nbsp;类目名称：</span>
									</td>
									<td class="td1" colspan="3" align="left">
									<s:textfield  id="sortName" name="modle.sortName" maxlength="15"   size="30"></s:textfield>
									
									</td>
								</tr>
								<tr>
									<td width="15%" height="21" class="biao_bg1" align="right" valign="top">
										<span class="wz">类目描述：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<textarea id="sortDesc" name="modle.sortDesc" rows="10" 
											cols="50" style="overflow: auto;" onkeyup="checkValueLength(this.value)">${modle.sortDesc}</textarea>
											<br><font color="#999999">请不要超过200字</font>
									</td>
								</tr>
								<tr>
									<td class="table1_td"></td>
									<td></td>
								</tr>
							</table>
						</s:form>
					</td>
				</tr>
			</table>
		</DIV>
		<SCRIPT type="text/javascript">
		<%--$(document).ready(function(){
			$("#sortNo").blur(function(){
				var sortNo=document.getElementById("sortNo").value;
				var sortId=document.getElementById("sortId").value;
				if((sortId==null||sortId==""||sortId=="null")&&sortNo!=""){
					$.ajax({
						type:"post",
						url:"<%=path%>/archive/sort/archiveSort!isExist.action",
						data:{
							sortNo:sortNo				
						},
						success:function(data){	
							if(data!=null&&data=="0"){//无后缀，说明是用户新建的而不是上传的文件
								alert("该文件编号已经存在，请重新输入！");
								return;
						 		document.getElementById("sortNo").value="";
						 		document.getElementById("sortNo").focus();
							}
						},
						error:function(data){
							alert("对不起，操作异常"+data);
						}
					});
				}
			});
		});--%>
		function checkValueLength(value){
			if(value.length>200){
				alert('请不要超过200字!');
				$("#sortDesc").val($("#sortDesc").val().substring(0,200));
			}
		}
		
		function save(){
		    var msg="";
			var obj=document.getElementById("mytable");
			var sortArrayNo=document.getElementById("sortArrayNo").value;
			var no=document.getElementById("sortNo").value;
			var name=document.getElementById("sortName").value;
			var sortId=document.getElementById("sortId").value;
			var str = new RegExp("^([\u4E00-\uFA29]|[\uE7C7-\uE7F3]|[a-zA-Z0-9])*$"); 
			if(no==""||no==null||no=="null"){
				alert("类目编号不能为空！");
				return;
			}
			if(!str.test($.trim(no)) && $.trim(no) !=""){
			    alert("输入类目编号包含特殊字符！\n请输入汉字，数字或字母");
  		        return; 
			}
			if(document.getElementById("sortNo").value.length > 15){
	        	alert("类目编号太长！");
				return;
	        }
			if(name==""||name==null||name=="null"){
				alert("类目名称不能为空！");
				return;
			}
			if(!str.test($.trim(name)) && $.trim(name) !=""){
			    alert("输入类目名称包含特殊字符！\n请输入汉字，数字或字母");
  		        return; 
			}
			if(document.getElementById("sortName").value.length > 15){
	        	alert("类目名称太长！");
				return;
	        }
	        
	        if(document.getElementById("sortDesc").value.length > 200){
	        	alert("描述太长！");
				return;
	        }
	        if(!str.test($.trim(sortArrayNo)) && $.trim(sortArrayNo) !=""){
			    alert("输入类目序号包含特殊字符！\n请输入汉字，数字或字母");
  		        return; 
			}
			$.ajax({
						type:"post",
						url:"<%=path%>/archive/sort/archiveSort!isExist.action",
						data:{
							sortNo:no,sortId:sortId				
						},
						async:false,
						success:function(data){	
						  if(data=="0"){//无后缀，说明是用户新建的而不是上传的文件
						 
								alert("该文件编号已经存在，请重新输入！");
								document.getElementById("sortNo").value="";
						 		document.getElementById("sortNo").focus();
						 		msg="1";
						 		return;
						 	}
						},
						error:function(data){
							alert("对不起，操作异常"+data);
						}
					});
					//alert(msg);
					if(msg!="1"){
			obj.action="<%=root%>/archive/sort/archiveSort!save.action";
			obj.submit();}
		}
		
		function create(){
			var sortId=document.getElementById("sortId").value
			var parentId=document.getElementById("sortId").value;
			if(sortId!=""){	
				location="<%=root%>/archive/sort/archiveSort!input.action";
			}else{
				alert("该页面已经是新建类目页面！！");
			}
		}
		function create1(){
			var sortId=document.getElementById("sortId").value
			var parentId=document.getElementById("sortId").value;
			if(sortId!=""){	
				location="<%=root%>/archive/sort/archiveSort!input.action?parentId="+parentId;
			}else{
				alert("该页面已经是新建类目页面！！");
			}
		}
		function deleteObj(){
			var sortId=document.getElementById("sortId").value
			var obj=document.getElementById("mytable");
			if(sortId!=""){
				if(confirm("你确定要删除吗？")){
					obj.action="<%=root%>/archive/sort/archiveSort!delete.action";
					obj.submit();
				}
			}else{
				alert("请选择类目，再删除！");
			}
		}
		function folderImport(){
		 $.post("<%=path%>/archive/sort/archiveSort!importFolder.action",
                 function(data){
                   //alert(data);
                      if(data=="1"){
                       alert("导入成功！")
                      }else if(data=="2"){
                         alert("已导入！")
                      }else{
                       alert("导入失败，请重新导入!");
                      }
                   });
		}
		function back(){
		 	history.go(-1);
		}
		</SCRIPT>
		<%--		<SCRIPT type="text/javascript">--%>
		<%--			var msg=document.getElementById("msg").value;--%>
		<%--				if(msg!=null&&msg!=""&&msg!="null"){--%>
		<%--					alert(msg);--%>
		<%--				}--%>
		<%--		</SCRIPT>--%>
	</body>
</html>
