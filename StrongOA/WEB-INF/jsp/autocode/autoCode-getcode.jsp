<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<% 
		response.setHeader("Cache-Control","no-store");
		response.setHeader("Pragrma","no-cache");
		response.setDateHeader("Expires",0);
		
	%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>文号生成器</title>
		<link href="<%=frameroot%>/css/properties_windows_add.css" type="text/css" rel="stylesheet">
		<script type="text/javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script src="<%=path%>/common/js/upload/jquery.MultiFile.js" type="text/javascript" language="javascript"></script>
		<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<script src="<%=path%>/common/js/upload/jquery.blockUI.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/service.js"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></script>
		<script type="text/javascript">
			var fparent = this.parent.window.document;
			$(document).ready(function(){
				
				$("#preview").click(function(){
					var ruleId = $("#ruletype").val();
					var sels = $("select[name='oasel']").attr("ctype","oahava");
					if(ruleId==""||ruleId=="null"){
					}else{
						var selIds="";
						if(sels.length==0){

						}else{
							for(var i=0;i<sels.length;i++){
								if(i==0){
									selIds=sels[i].value;
								}else{
									selIds=selIds+","+sels[i].value;
								}
							}					
						}
						$.ajax({
							type:"post",
							dataType:"text",
							url:"<%=root%>/autocode/autoCode!getPreview.action",
							data:"id="+ruleId+"&selIds="+selIds,
							success:function(message){
								if(message.indexOf("错误")!=-1){
									$("#result").html("<span id=presult>"+message+"</span>&nbsp;&nbsp;&nbsp;<a  href=\"#\" class=\"button\" id=\"cbtn\" disabled=\"true\" onclick=\"createCode()\">生成文号</a>&nbsp;&nbsp;&nbsp;<a  href=\"#\" class=\"button\" id=\"cbtn\" disabled=\"true\" onclick=\"saveReservedCode()\">预留文号</a>");
								}else{
									$("#result").html("<span id=presult>"+message+"</span>&nbsp;&nbsp;&nbsp;<a  href=\"#\" class=\"button\" id=\"cbtn\" onclick=\"createCode()\">生成文号</a>&nbsp;&nbsp;&nbsp;<a  href=\"#\" class=\"button\" id=\"cbtn\" onclick=\"saveReservedCode()\">预留文号</a>");
								}
							}
						});
					}
				});
				$("#recBtn").click(function(){
					var codename = $("#recyclecode").val();
					if(codename==""){
						alert("请您填写想要回收的文号！");
						return ;
					}
					$.ajax({
						type:"post",
						dataType:"text",
						url:"<%=root%>/autocode/autoCode!createRecCode.action",
						data:"codeName="+codename,
						success:function(msg){
							if(msg=="no"){
								alert("文号："+codename+"在数据库中不存在，不能进行回收操作。");
							}else if(msg=="res"){
								alert("文号:"+codename+"已经预留，不需要再进行回收操作。");
							}else if(msg=="rec"){
								alert("文号:"+codename+"已经回收，不需要再进行回收操作。");
							}else if(msg=="true"){
								alert("文号:"+codename+"回收成功");
								//window.parent.document.frames["used"].refreshIframe();
							}else{
								alert("文号:"+codename+"回收失败，未知异常，请与管理员联系。");
							}
						}
					});
				});
				//当只有一个记录时默认显示第一个记录并触发点击事件
				var myoption = document.getElementById("ruletype");
				//var myoption = document.getElementsByName("ruletype");
				if(myoption.length==2){
					//alert("ok")
					myoption[1].selected=true;
					onSelectChange();
				}
			});
			
		
			function onSelectChange(){
				var myoption=getSelectOption();
				if(myoption.value=="0"){
					$("#exampleblank").hide();
					$("#exampletr").hide();
					$("#ruleExample").html("");
					$("#result").html("");
					$(fparent).find("li").eq(1).attr("disabled","disabled");
					$(fparent).find("li").eq(2).attr("disabled","disabled");
					$(fparent).find("li").eq(3).attr("disabled","disabled");
				}else{
					$.ajax({
						type:"post",
						dataType:"text",
						url:"<%=root%>/autocode/autoCode!getXmlInfo.action",
						data:"id="+myoption.value,
						success:function(xml){
							createHtml(xml);
							$("#exampleblank").show();
							$("#exampletr").show();
							$("#result").html("");
						},
						error:function(){
							alert("Ajax调用异常。");
							$("#result").html("");
						}
					});
					fparent.getElementById("tmptxt").value=myoption.value;
					$(fparent).find("li").eq(1).attr("disabled","");
					$(fparent).find("li").eq(2).attr("disabled","");
					$(fparent).find("li").eq(3).attr("disabled","");
					//alert($(fparent).find("#leavecode").attr("src"));
					//$(fparent).find("#leavecode").attr("src","<%=root%>/autocode/autoCode!getReservedCode.action?prevCode="+myoption.value);
					
				}
				return true;
			}
			
			function createHtml(xml){
		        var xmlDoc=null;
		        //判断浏览器的类型
		        //支持IE浏览器 
		       	var rHtml="";
		        if(!window.DOMParser && window.ActiveXObject){   //window.DOMParser 判断是否是非ie浏览器
		            var xmlDomVersions = ['MSXML.2.DOMDocument.6.0','MSXML.2.DOMDocument.3.0','Microsoft.XMLDOM'];
		            for(var i=0;i<xmlDomVersions.length;i++){
		                try{
		                    xmlDoc = new ActiveXObject(xmlDomVersions[i]);
		                    xmlDoc.async = false;
		                    xmlDoc.loadXML(xml); //loadXML方法载入xml字符串
		                    break;
		                }catch(e){
		                	
		                }
		            }
		           	var root = xmlDoc.childNodes[0];
		           	for(var i=0;i<root.childNodes.length;i++){
		           		if(root.childNodes[i].nodeName=="String"){
		           			rHtml = rHtml + root.childNodes[i].getAttribute("Value");
		           		}else if(root.childNodes[i].nodeName=="Sequence"){
		           			rHtml = rHtml + " $序号$ ";
		           		}else if(root.childNodes[i].nodeName=="Variant"){
		           			var type = root.childNodes[i].getAttribute("Type");
		           			if(type=="Data LongYear"){
		           				rHtml = rHtml + " $年份$ ";
		           			}else if(type=="Data ShortYear"){
		           				rHtml = rHtml + " $短年份$";
		           			}else if(type=="Data Month"){
		           				rHtml = rHtml + " $月份$ ";
		           			}else if(type=="Data Day"){
		           				rHtml = rHtml + " $日期$ ";
		           			}else if(type=="Array"){
		           				var myArrays = root.childNodes[i].childNodes;
		           				if(myArrays.length>0){
		           					rHtml = rHtml + "<select id=\"mySel\""+i+" name=\"oasel\" ctype=\"oahave\">";
		           				}
		           				for(var t=0;t<myArrays.length;t++){
		           					rHtml = rHtml + "<option value="+myArrays[t].getAttribute("Id")+">"+myArrays[t].getAttribute("Value")+"</option>";
		           				}
		           				rHtml = rHtml + "</select>";
		           				//规则内容下拉框和后面的内容换行：rHtml = rHtml + "</select><br>";
		           			}
		           		}
		           	}
		           	$("#ruleExample").html(rHtml);
		        }
				
			}
			
			function getSelectOption(){
				var myoption = document.getElementById("ruletype");
				for(var i=0;i<myoption.length;i++){
					if(myoption[i].selected){
						return myoption[i];
					}
				}
			}
			
			function saveReservedCode(){
				var ruleId = $("#ruletype").val();
				var sels = $("select[name='oasel']").attr("ctype","oahava");
				var prevCode = $("#presult").html();
				if(ruleId==""||ruleId=="null"){
				}else{
					var selIds="";
					if(sels.length==0){

					}else{
						for(var i=0;i<sels.length;i++){
							if(i==0){
								selIds=sels[i].value;
							}else{
								selIds=selIds+","+sels[i].value;
							}
						}					
					}
					$.ajax({
						type:"post",
						dataType:"text",
						url:"<%=root%>/autocode/autoCode!createCode.action",
						data:"id="+ruleId+"&selIds="+selIds+"&prevCode="+prevCode+"&type=reserved",
						success:function(message){
							if(message=="error"){
								alert("生成xml信息异常。");
							}else if(message=="error2"){
								alert("不存在的变量。");
							}
							else if(message=="noeq"){
								alert("预览信息与生成信息不符，请您核对，预留文号失败。");
							}else if(message=="true"){
								window.close();
							}else{
								alert("未知异常信息，请您与系统管理员联系。");
							}
						}
					});
				}
			}
			
			function createCode(){
				var ruleId = $("#ruletype").val();
				var sels = $("select[name='oasel']").attr("ctype","oahava");
				var prevCode = $("#presult").html();
				if(ruleId==""||ruleId=="null"){
				}else{
					var selIds="";
					if(sels.length==0){

					}else{
						for(var i=0;i<sels.length;i++){
							if(i==0){
								selIds=sels[i].value;
							}else{
								selIds=selIds+","+sels[i].value;
							}
						}					
					}
					$.ajax({
						type:"post",
						dataType:"text",
						url:"<%=root%>/autocode/autoCode!createCode.action",
						data:"id="+ruleId+"&selIds="+selIds+"&prevCode="+prevCode,
						success:function(message){
							if(message=="error"){
								alert("生成xml信息异常。");
							}else if(message=="error2"){
								alert("不存在的变量。");
							}
							else if(message=="noeq"){
								alert("预览信息与生成信息不符，请您核对。");
							}else if(message=="true"){
								window.returnValue=prevCode;
								window.close();
							}else{
								alert("未知异常信息，请您与系统管理员联系");
							}
						}
					});
				}
			}
		</script>
	</head>
	<body class="contentbodymargin">
	   <DIV id=contentborder align=center>
		   <table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
				<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
								<strong>文号生成器</strong>
							</td>
							<td align="right">
								
							</td>
				</tr>
				</table>
				</td>
				</tr>
				<tr>
					<td>
			<fieldset style="width: 100%" align=center>
					<legend>
						<span class="wz"  style='height:25px'>文号生成工具 </span>
					</legend>
					<div>
					<table width="100%" border="0" cellpadding="0" cellspacing="0"
						>												
						<tr>
							<td class="biao_bg1" align="right">
								<span class="wz">选择规则：</span>
							</td>
							<td class="td1"  align="left" style="width:120px">
									<select onchange="onSelectChange()" style="width: 160px;" id="ruletype" name="ruletype">
																<s:iterator id="vo" value="list">
																	<option value="${vo.id }">
																		${vo.ruleName }
																	</option>
																</s:iterator>
															</select>
								</td>
								<td> &nbsp</td>
						</tr>						
						<tr id="exampleblank" style="display:none">
														<td></td>
														<td></td>
													</tr>
													<tr id="exampletr" style="display:none">
														<td class="biao_bg1" align="right">
															<span class="wz">规则内容：</span>
														</td>
														<td class="td1"  align="left" style="width:320px">
															<span id="ruleExample"></span>
														</td>
														<td >
															<input type="hidden" id="preview" class="input_bg" value="预览文号">
															<a id="preview"  href="#" class="button" onclick="$('#preview').click();">预览文号</a>
														</td>
													</tr>
													<tr>
														<td></td>
														<td></td>
													</tr>
													<tr>
														<td class="biao_bg1" align="right"><span class="wz">当前文号：</span></td>
														<td style="width:380px"><span id="result"></span></td>
													</tr>			
					</table>
					</div>
					<div id="yuliu"  style=" display: none;">
												<table><tr><td valign="top">预留文号：</td><td valign="top"></td></tr></table>
											</div>
				</fieldset>	
				</td>
				</tr>
					
				
				
			
			<!--  
			<div id="recycleid">
				<table>
					<tr>
						<td valign="top">
							回收文号：&nbsp;&nbsp;&nbsp;&nbsp;<input type=text id="recyclecode" value="" maxlength="100">
						</td>
						<td valign="top">
							&nbsp;&nbsp;&nbsp;&nbsp;<input type = button id ="recBtn" class="input_bg" value="回收文号" >
						</td>
					</tr>
				</table>
			</div>
			-->
			<tr>
							<td class="table1_td"></td>
							<td></td>
						</tr>
		</table>
		</DIV>
	</body>
</html>
