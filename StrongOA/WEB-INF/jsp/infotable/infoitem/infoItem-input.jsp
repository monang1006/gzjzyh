<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>保存信息项</title>
		<!-- 页面样式 -->
		<LINK href="<%=frameroot%>/css/properties_windows_add.css" type=text/css rel=stylesheet>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/upload/jquery.MultiFile.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/upload/jquery.blockUI.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/validate/jquery.validate.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/validate/formvalidate.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/commontab/service.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/commontab/workservice.js" type="text/javascript" language="javascript"></script>
		<SCRIPT>
		$(document).ready(function(){
			var value=document.getElementById("infoItemDatatype").value;
			//var value=$.trim(obj);
			if(value!="2"){
				document.getElementById("infoItemDecimal").readOnly=true;
				$("#infoItemDecimal").css("color","#999");
				$("#infoItemDecimal").focus(function(){$(this).blur()});
			}
			if(value=="1" || value=="3" || value=="4" || value=="5" || value=="6" ){
				$("#infoItemLength").attr("readonly",true);
				$("#infoItemLength").css("color","#999");
				$("#infoItemLength").focus(function(){$(this).blur()});

			}
		}); 
		setPageListenerEnabled(true);//设置页面数据发生改变是否监听
		function Test(){
			var obj=document.getElementById("infoItemDatatype");
			var value=obj.value;
			var div1=document.getElementById("dictid");
			var daimaselect=document.getElementById("infoItemDictCode");
			var itemlength = document.getElementById("infoItemLength");
			var itemdecimal =document.getElementById("infoItemDecimal");
			if(value=="2")
			{   
				itemlength.value="";
		  		document.getElementById("infoItemDecimal").readOnly=false;
		  		daimaselect.disabled=true;	 
		  		document.getElementById("infoItemLength").readOnly=false;
		  		$("#infoItemLength").unbind("focus");
		  		$("#infoItemLength").css("color","");
		  		$("#infoItemDecimal").unbind("focus");
		  		$("#infoItemDecimal").css("color","");
		  		//$("#infoItemLength").focus(function(){});
		  				
			}
			else{
		 		document.getElementById("infoItemDecimal").readOnly=true;
				$("#infoItemDecimal").css("color","#999");
				$("#infoItemDecimal").focus(function(){$(this).blur()});
		 		itemdecimal.value=0;
		 		if(value=="0"){
		 			itemlength.value="";
		 			daimaselect.disabled=true;
		 			document.getElementById("infoItemLength").readOnly=false;
		 			$("#infoItemLength").unbind("focus");
			  		$("#infoItemLength").css("color","");
		 		}
		 		else if(value=="1"){
		 			//if(itemlength=="0"||itemlength=="")
						itemlength.value=20;
						$("#infoItemLength").attr("readonly",true);
						$("#infoItemLength").css("color","#999");
						$("#infoItemLength").focus(function(){$(this).blur()});
					daimaselect.disabled=false;
		 		}
				else{
					//div1.style.display="none";
					daimaselect.disabled=true;	
					if(value=="3"){
						itemlength.value=4;
						$("#infoItemLength").attr("readonly",true);
						$("#infoItemLength").css("color","#999");
						$("#infoItemLength").focus(function(){$(this).blur()});
					}
					else if(value=="4"){
						itemlength.value=7;
						$("#infoItemLength").attr("readonly",true);
						$("#infoItemLength").css("color","#999");
						$("#infoItemLength").focus(function(){$(this).blur()});
					}
					else if(value=="5"){
						itemlength.value=10;
						$("#infoItemLength").attr("readonly",true);
						$("#infoItemLength").css("color","#999");
						$("#infoItemLength").focus(function(){$(this).blur()});
					}
					else if(value=="6"){
						itemlength.value=20;
						$("#infoItemLength").attr("readonly",true);
						$("#infoItemLength").css("color","#999");
						$("#infoItemLength").focus(function(){$(this).blur()});
					}
					else{
						if(itemlength=="0"||itemlength=="")
							itemlength.value=0;
						$("#infoItemLength").attr("readonly",false);
						$("#infoItemLength").unbind("focus");
				  		$("#infoItemLength").css("color","");
					}
			
		 		}//第一个else
		 	}	  //第二个else
		}
		
		function gosubmit(){
			//信息项值
			var infoItemField = document.getElementById("infoItemField").value;
			//信息项别名
			var infoItemSeconddisplay = document.getElementById("infoItemSeconddisplay").value;
			//信息项长度
			var infoItemLength = document.getElementById("infoItemLength").value;
			//alert(infoSetName);
			//信息项排序序号
			var infoItemOrderby = document.getElementById("infoItemOrderby").value;
			//信息项类型
			var infoItemDatatype = document.getElementById("infoItemDatatype").value;
			var msg="";
			if($.trim(infoItemField)==""){
				if(msg==""){
				  document.getElementById("infoItemField").focus();
				}
				msg="信息项值不能为空。\n";
				
			}else{
				if(!/^[A-Z]+([_A-Z])*$/.test(infoItemField)){
					 document.getElementById("infoItemField").value = "";
					 if(msg==""){
						  document.getElementById("infoItemField").focus();
						}
					 	//alert("信息集值只能包含大写字母或_符号,并且只能以大写字母开头");
					 	
					 	msg+="信息项值只能包含大写字母或_符号,并且只能以大写字母开头。\n"
					 	
					 }
				if(infoItemField.length>40)	 {
				   msg+="信息项值不能超过40个字符。\n"
				}
			}
			
			 
			if($.trim(infoItemSeconddisplay)==""){
				if(msg==""){
				  document.getElementById("infoItemSeconddisplay").focus();
				}
				msg+="信息项别名不能为空。\n";
				
			}
			if($.trim(infoItemLength)==""){
				if(msg==""){
				  document.getElementById("infoItemLength").focus();
				}
				msg+="信息项长度不能为空。\n";
				
			}
			if($.trim(infoItemLength)!=""){
			    var info=parseInt(infoItemLength,10);  // 转换成十进制
			    if(info==0){
			    document.getElementById("infoItemLength").focus();
			    msg+="信息项长度不能为零。\n";
			    }else{
			        $("#infoItemLength").val($.trim(infoItemLength));
			    }
			}
			if($.trim(infoItemOrderby)==""){
				if(msg==""){
				  document.getElementById("infoItemOrderby").focus();
				}
				msg+="信息项排序序号不能为空。\n";
			}else{
				$("#infoItemOrderby").val($.trim(infoItemOrderby));
			}
			
			if($.trim(infoItemDatatype)==""){
				if(msg==""){
				  document.getElementById("infoItemDatatype").focus();
				}
				msg+="信息项类型不能为空。\n";
				
			}
			if(document.getElementById("infoItemDatatype").value=='2'&&document.getElementById("infoItemLength").value*1>38*1){
				document.getElementById("infoItemLength").value = "";
				if(msg==""){
					  document.getElementById("infoItemLength").focus();
					}
			 	msg+="信息项类型为数值型，其长度不能超过38。\n";
			 	
			 		 
			 }
			 if(document.getElementById("infoItemDatatype").value=='0'&&document.getElementById("infoItemLength").value*1>2000){
				 document.getElementById("infoItemLength").value = "";
					if(msg==""){
						  document.getElementById("infoItemLength").focus();
						}
				 	msg+="信息项类型为字符串，其长度不能超过2000。";
				 	//document.getElementById("infoItemLength").value = "";
				 		 
				 }
			if(msg!=""){
				alert(msg);
				return false;
			}
			$("#infoitemForm").submit();
			//判断排序号不能重复 by tj
			//var infoSetCode=$("#infoSetCode").val();
			//var infoItemOrderby = '${infoItemOrderby}';
			//var tempinfoItemOrderby = $("#infoItemOrderby").val();
			//if(infoItemOrderby!=tempinfoItemOrderby){
			//	var url = "<%=path%>/infotable/infoitem/infoItem!check.action?infoItemOrderby="+tempinfoItemOrderby+"&infoSetCode="+infoSetCode;
			//	$.post(url, function(data) {
			//	  if(data=="true"){
			//		  $("#infoitemForm").submit();
			//	  }else{
			//		  alert("排序号重复，请重新输入。");
			//		  return;
			//	  }
			//	});
			//}else{
			//	$("#infoitemForm").submit();
			//}
		}
		
		function historyGo(){
			var infoSetCode=$("#infoSetCode").val();
			window.location="<%=path%>/infotable/infoitem/infoItem.action?infoSetCode="+ infoSetCode;
		}
	</SCRIPT>
		
	</head>
	<body class=contentbodymargin oncontextmenu="return false;">
		<DIV id=contentborder align=center>
			<s:form id="infoitemForm" theme="simple" action="/infotable/infoitem/infoItem!save.action">
				<input id="infoItemCode"  name="model.infoItemCode" type="hidden" size="32" value="${model.infoItemCode}">
				<input id="infoSetCode" name="infoSetCode" type="hidden" size="32" value="${infoSetCode}">
				<input id="type" name="type" type="hidden" size="32" value="${type}">
				<input name="infoItemState" type="hidden" size="32" value="${model.infoItemState}">
				<input name="infoSetState" type="hidden" size="32" value="${infoSetState}">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
					<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
							   <s:if test="infoItemCode==null">
										<strong>新建信息项</strong>
									</s:if>
									<s:else>
										<strong>编辑信息项</strong>
									</s:else>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
					                <tr>
					                 	
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onclick="gosubmit();">&nbsp;保&nbsp;存&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
				                  		<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
					                 	<td class="Operation_input1" onclick="historyGo();">&nbsp;返&nbsp;回&nbsp;</td>
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
					<td>
				
				
							<table  border="0" cellpadding="0" cellspacing="0" class="table1" width="100%" >
								<tr>
									<td  class="biao_bg1" align="right">
										<span class="wz">对应信息集：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input name="model.toaSysmanageStructure.infoSetCode" type="hidden"  size="32" value="${infoSetCode}">
										<span class="wz">${infoSetName}</span>
									</td>
								</tr>
								<tr>
									<td  class="biao_bg1" align="right">
										<span class="wz"><FONT color="red">*</FONT>&nbsp;信息项值：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<s:if test="model.infoItemState==1">
											<input id="infoItemField" name="model.infoItemField" type="text" size="32"  onchange="notifyChange(true);" value="${model.infoItemField}" style="color:#999" maxlength="255" readonly="readonly">
										</s:if>
										<s:else>										
											<input id="infoItemField" name="model.infoItemField" type="text" size="32" onchange="notifyChange(true);"  value="${model.infoItemField}"  maxlength="255">
										</s:else>
									</td>
								</tr>
								<tr>
									<td  class="biao_bg1" align="right">
										<span class="wz"><FONT color="red">*</FONT>&nbsp;信息项别名：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="infoItemSeconddisplay"  name="model.infoItemSeconddisplay" type="text" size="32" onchange="notifyChange(true);" value="${model.infoItemSeconddisplay}"  maxlength="50">
									</td>
								</tr>
								
								<tr>
									<td  class="biao_bg1" align="right">
										<span class="wz">引用的字典：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<div id="dictid">
											<s:if test="model.infoItemDatatype==\"1\"">
												<s:select list="dictList" listKey="dictCode" listValue="dictName"  headerKey="" headerValue="请选择字典" id="infoItemDictCode" name="model.infoItemDictCode" onchange="notifyChange(true);" style='width:219px;'/>
											</s:if>
											<s:else>
												<s:select list="dictList" listKey="dictCode" listValue="dictName" headerKey=""  headerValue="请选择字典" id="infoItemDictCode" name="model.infoItemDictCode" onchange="notifyChange(true);" disabled="true" style='width:219px;'/>
											</s:else>
										</div>
									</td>
								</tr>
								<tr>
									<td  class="biao_bg1" align="right">
										<span class="wz"><FONT color="red">*</FONT>&nbsp;信息项长度：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<s:if test="model.infoItemState==1">
											<input  id="infoItemLength"  name="model.infoItemLength"  type="text" size="32" maxlength="4" onchange="notifyChange(true);" value="${model.infoItemLength}" class="required digits" style="color:#999" readonly="readonly">
										</s:if>
										<s:else>
											<input id="infoItemLength" name="model.infoItemLength"  type="text" size="32" maxlength="4" onchange="notifyChange(true);" value="${model.infoItemLength}" class="required digits">
										
										</s:else>
									</td>
								</tr>
								<tr>
									<td  class="biao_bg1" align="right">
										<span class="wz">信息项小数位数：</span>
									</td>
									<td class="td1" colspan="3" align="left">   
									             
										<s:if test="model.infoItemState==1">
										    <s:if test="model.infoItemDecimal==null">
										    	<input id="infoItemDecimal" name="model.infoItemDecimal"   type="text" size="32" maxlength="2"  onchange="notifyChange(true);" readonly="readonly" value="0"  style="color:#999" readonly="readonly">										
										    </s:if>
										    <s:else>
										    <input id="infoItemDecimal" name="model.infoItemDecimal"   type="text" size="32" maxlength="2"  onchange="notifyChange(true);" readonly="readonly" value="${model.infoItemDecimal }"  style="color:#999" readonly="readonly">										
										    </s:else>
										</s:if>
										<s:else>
										 <s:if test="model.infoItemDecimal==null">
										    	<input id="infoItemDecimal" name="model.infoItemDecimal"  type="text" size="32"  maxlength="2" onchange="notifyChange(true);"  value="0" class="digits">	
										    </s:if>
										    <s:else>
										   <input id="infoItemDecimal"  name="model.infoItemDecimal"  type="text" size="32"  maxlength="2" onchange="notifyChange(true);"  value="${model.infoItemDecimal}" class="digits">										
										    </s:else>
										</s:else>
									</td>
								</tr>
								<tr>
									<td align="right"  class="biao_bg1">
										<span class="wz"><FONT color="red">*</FONT>&nbsp;信息项排序序号：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input type="text" id="infoItemOrderby" name="model.infoItemOrderby" size="32"  maxlength="3" onchange="notifyChange(true);" value="${model.infoItemOrderby}" class="digits">
									    <span style="color:#999">信息项排序序号最大值为999，排序号序号就为999</span>
									</td>
								</tr>
								<tr>
									<td  class="biao_bg1" align="right">
										<span class="wz">信息项描述：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="infoItemDescription" name="model.infoItemDescription"  type="text" size="32" onchange="notifyChange(true);" value="${model.infoItemDescription}" maxlength="100">
									</td>
								</tr>
									<tr>
										<td class="biao_bg1" align="right">
											<span class="wz">是否必填：</span>
										</td>
										<td class="td1" colspan="3" align="left">
										<s:if test="model.infoItemState==1||infoSetState==\"1\"">
											<s:radio id="infoItemFlag1" name="model.infoItemFlag" list="#{'0':'否','1':'是'}"  onchange="notifyChange(true);" onselect="0" disabled="true" />
											<span style="color:#999">请在信息集构建之前，设置是否必填</span>
											<input name="infoItemFlag" type="hidden" size="32" value="${model.infoItemFlag}">
										</s:if>
										<s:else>
											<s:radio id="infoItemFlag" name="model.infoItemFlag" list="#{'0':'否','1':'是'}"  onchange="notifyChange(true);" onselect="0"/>
										     <span style="color:#999">请在信息集构建之前，设置是否必填</span>
										</s:else>
										</td>
									</tr>
									<tr>
										<td  class="biao_bg1" align="right">
											<span class="wz">是否查询字段：</span>
										</td>
										<td class="td1" colspan="3" align="left">
											<s:radio id="isQuery1" name="model.isQuery" list="#{'0':'否','1':'是'}" onselect="0"/>
										</td>
									</tr>
									<tr>
										<td  class="biao_bg1" align="right">
											<span class="wz">是否显示字段：</span>
										</td>
										<td class="td1" colspan="3" align="left">
											<s:radio id="isView" name="model.isView" list="#{'0':'否','1':'是'}" onselect="0" />
										</td>
									</tr>
									<tr>
										<td  class="biao_bg1" align="right">
											<span class="wz">编辑方式：</span>
										</td>
										<td class="td1" colspan="3" align="left">
<%--											<s:if test="model.infoItemState==1">
												<s:radio id="infoItemProset1" name="model.infoItemProset" list="#{'0':'读写','2':'只读','1':'不可读'}" onchange="notifyChange(true);" onselect="0" disabled="true" />
												<input name="infoItemProset" type="hidden" size="32" value="${model.infoItemProset}">
											</s:if>
											<s:else>--%>
												<s:radio id="infoItemProset1"  name="model.infoItemProset" list="#{'0':'读写','2':'只读','1':'不可读'}" onchange="notifyChange(true);" onselect="0" />
												
<%--											</s:else>--%>
										</td>
									</tr>
								
								
								
								<tr>
									<td  class="biao_bg1" align="right">
										<span class="wz">所属信息项分类：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<s:select list="proTypeList" listKey="propertypeId"  listValue="propertypeName" headerKey="" headerValue="请选择分类" id="properTypeId" name="model.properTypeId" onchange="notifyChange(true);" />
									</td>
								</tr>
								<tr>
									<td  class="biao_bg1" align="right">
										<span class="wz"><FONT color="red">*</FONT>&nbsp;信息项类型：</span>
									</td>
									<%--<td class="td1" colspan="3" align="left">
										<s:select id="infoItemDatatype" onchange="notifyChange(true);Test();" name="model.infoItemDatatype" list="#{'0':'字符串','1':'代码','2':'数值','3':'年','4':'年月','5':'年月日','6':'年月日时间','10':'文件','11':'图片','12':'电话号码','13':'文本','14':'备注'}" />
									</td>
									--%>
									<td class="td1" colspan="3" align="left">
										<s:if test="model.infoItemState==1">
											<s:select id="infoItemDatatype1"  onchange="notifyChange(true);Test();" name="model.infoItemDatatype" list="#{'0':'字符串','1':'代码','2':'数值','3':'年','4':'年月','5':'年月日','6':'年月日时间','10':'文件','11':'图片','12':'电话号码','14':'备注','20':'大字段'}" style="color:#999" disabled="true"/>
											<input name="infoItemDatatype"  type="hidden" size="32" value="${model.infoItemDatatype}">
										</s:if>
										<s:else>										
											<s:select id="infoItemDatatype" onchange="notifyChange(true);Test();"  name="model.infoItemDatatype" list="#{'0':'字符串','1':'代码','2':'数值','3':'年','4':'年月','5':'年月日','6':'年月日时间','10':'文件','11':'图片','12':'电话号码','14':'备注','20':'大字段'}" />
										</s:else>
									</td>
								</tr>
								<tr>
							      <td class="table1_td"></td>
							      <td></td>
						       </tr>
							</table>
							<table id="annex" width="90%" height="10%" border="0" cellpadding="0" cellspacing="1" align="center" class="table1">
							</table>

							
						</td>
					</tr>
				</table>
			</s:form>
		</DIV>
	</body>
</html>
