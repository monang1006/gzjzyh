<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>外挂系统</title>
		<LINK type=text/css rel=stylesheet
			href="<%=frameroot%>/css/properties_windows_add.css">
		<script type="text/javascript" language="javascript"
			src="<%=jsroot%>/jquery/jquery-1.2.6.js"></script>
		<script type="text/javascript"
			src="<%=jsroot%>/validate/jquery.validate.js"></script>
		<script type="text/javascript"
			src="<%=jsroot%>/validate/formvalidate.js"></script>
		<script type="text/javascript" language="javascript"
			src="<%=jsroot%>/upload/jquery.MultiFile.js"></script>
		<script type="text/javascript" language="javascript"
			src="<%=jsroot%>/upload/jquery.blockUI.js"></script>
		<script type="text/javascript" language="javascript"
			src="<%=jsroot%>/newdate/WdatePicker.js"></script>
		<script language="javascript">
	function testMessage(){
	var flag=document.getElementById("iscode").value;
	   if(document.getElementById("sysSyscode").value == ""||document.getElementById("sysSyscode").value==null){
			alert('系统编号不能为空。');
	        	document.getElementById("sysSyscode").focus();
	        	return;
	    } else if(document.getElementById("iscode").value == ""||document.getElementById("iscode").value==null){
	       	 comparecode(document.getElementById('sysSyscode'),'1');	
	    } else if(flag==document.getElementById("sysSyscode").value){
	           alert("合法编码。");
	            document.getElementById("sysSyscode").focus();
	    } else{
	         comparecode(document.getElementById('sysSyscode'),'1');
	       }	    	
	    }
	var messg = "";
	function testSumbit(){
	var flag=document.getElementById("iscode").value;

	   if(document.getElementById("sysSyscode").value == ""||document.getElementById("sysSyscode").value==null){
			//messg = messg+'系统编号不能为空。\n';
	        //	document.getElementById("sysSyscode").focus();
	        	 formsubmit();
	        	//return;
	    } else if(document.getElementById("iscode").value == ""||document.getElementById("iscode").value==null){
	       	 comparecode(document.getElementById('sysSyscode'),'0');	
	    } else if(flag==document.getElementById("sysSyscode").value){
	           formsubmit();
	    } else{
	         comparecode(document.getElementById('sysSyscode'),'0');
	       }	    	
	    }
	function comparecode(obj,flag){	
				 $.ajax({
					url:obj.content,
					type:"post",
					data:{orgcode:obj.value},
					success:function(message){
						if(message!="0"){
							alert("非法编码。");
							obj.focus();		
						}
						else{
						if(flag=='0')
						formsubmit();
						else 
						alert("合法编码。");
						}						
					},
					error:function(message){
						alert("异步出错!");
					}					
				});				
			}	 	
		function formsubmit(){		
			var oldIsactive = document.getElementById("oldIsactive").value;
			var sysIsactive = document.getElementById("sysIsactive").value;
			if(oldIsactive != null && oldIsactive != "" && oldIsactive != sysIsactive){
				if(sysIsactive == "0"){
					if(!confirm("设定该系统为未启用将导致其下级资源都变为未启用状态，是否继续操作?")){
						return false;
					}
				}else if(sysIsactive == "1"){
					if(confirm("是否同时将该系统下的资源都设置为启用状态?")){
						document.getElementById("childTogether").value = "1";
					}
				}
			}
		
			var sysSyscode = document.getElementById("sysSyscode").value;
	        var flag=document.getElementById("iscode").value;
			var sysName = document.getElementById("sysName").value;
			var sysStartdate = document.getElementById("sysStartdate").value;
			var sysDescription = document.getElementById("sysDescription").value;
			var sysSequence = document.getElementById("sysSequence").value;
			if($.trim(sysSyscode)==null||$.trim(sysSyscode)==""){
				messg = messg+"请输入系统编号。\n";
				document.getElementById("sysSyscode").focus();
				//return;
			}
			
			if($.trim(sysName)==null||$.trim(sysName)==""){
				messg = messg+"请输入系统名称。\n";
				document.getElementById("sysName").focus();
				//return;
			}
			if($.trim(sysStartdate)==null||$.trim(sysStartdate)==""){
				messg = messg+"请选择启用时间。\n";
				document.getElementById("sysStartdate").focus();
				//return;
			}
			if($.trim(sysSequence)==null||$.trim(sysSequence)==""){
				messg = messg+"请输入排序序号。\n";
				document.getElementById("sysSequence").focus();
				//return;
			}
			if($.trim(sysDescription) != null){
				if(sysDescription.length > 200){
					messg = messg+"系统描述字数不能大于200。\n";
					//return;
				}
			}
			if(messg!=null&&messg!=""){
				alert(messg);
				messg="";
				return;
			}
			document.forms[0].submit();
		}
	</script>
		<base target=_self>
	</head>
	<body class=contentbodymargin oncontextmenu="return false;">
		<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
		<DIV id=contentborder align=center>
			<s:actionmessage />
			<s:form id="mytable" action="/basesystem/baseSystem!save.action"
				method="POST">

				<input type="hidden" id="sysId" name="sysId1"
					value="${boBaseSystem.sysId}">
				<input type="hidden" id="iscode" name="iscode"
					value="${boBaseSystem.sysSyscode}">
				<input type="hidden" id="oldIsactive" name="oldIsactive"
					value="${boBaseSystem.sysIsactive }" />
				<input type="hidden" id="childTogether" name="childTogether" value="0" />
				<table width="100%" border="0" cellspacing="0" cellpadding="0"
					style="vertical-align: top;">
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
													<strong>外挂系统</strong>
												</td>
												<td align="right">
													<table border="0" align="right" cellpadding="00" cellspacing="0">
										                <tr>
										                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
										                 	<td class="Operation_input" onclick="testSumbit();">&nbsp;保&nbsp;存&nbsp;</td>
										                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
									                  		<td width="5"></td>
										                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
										                 	<td class="Operation_input1" onclick="window.close();">&nbsp;取&nbsp;消&nbsp;</td>
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
							<table width="100%" height="10%" border="0" cellpadding="0"
								cellspacing="1" align="center" class="table1">
								<tr>
									<td width="15%" height="21" class="biao_bg1" align="right">
										<span class="wz"><font color="red">*</font>&nbsp;系统编号：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="sysSyscode" name="boBaseSystem.sysSyscode" type="text" maxLength="21" size="22" value="${boBaseSystem.sysSyscode}" content="<%=path%>/basesystem/baseSystem!compareCode.action" />
															<a  href="#" class="button" onclick="testMessage()">检测合法性</a>
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz"><font color="red">*</font>&nbsp;系统名称：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="sysName" name="boBaseSystem.sysName" type="text" maxLength="50"
											size="22" value="${boBaseSystem.sysName}">
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz"><font color="red">*</font>&nbsp;启用时间：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<strong:newdate id="sysStartdate"
											name="boBaseSystem.sysStartdate"
											dateobj="${boBaseSystem.sysStartdate}" dateform="yyyy-MM-dd"
											width="133" />
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">启用标志：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<s:select name="boBaseSystem.sysIsactive"
											list="#{'1':'是','0':'否'}" listKey="key" listValue="value"
											id="sysIsactive" style="width:11em" theme="simple" />
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz"><font color="red">*</font>&nbsp;排序序号：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="sysSequence" name="model.sysSequence" type="text"
											size="22" style="width:11em" value="${model.sysSequence}">
									</td>
								</tr>								
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right" valign="top">
										<span class="wz">系统描述：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<textarea id="sysDescription"
											name="boBaseSystem.sysDescription" rows="6" cols="30">${boBaseSystem.sysDescription}</textarea>
									</td>
								</tr>
								<tr>
									<td class="table1_td"></td>
									<td></td>
								</tr>
							</table>
							<table id="annex" width="90%" height="10%" border="0"
								cellpadding="0" cellspacing="1" align="center" class="table1">
							</table>
							<table width="90%" border="0" cellspacing="0" cellpadding="00">
								<tr>
									<td align="center" valign="middle">
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</s:form>
		</DIV>
	</body>
</html>
