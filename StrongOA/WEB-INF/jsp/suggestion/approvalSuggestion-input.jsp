<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
    
    <title>保存意见</title>
    
	<link href="<%=frameroot%>/css/properties_windows_add.css" type="text/css" rel="stylesheet">
	<script type="text/javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
	<script src="<%=path%>/common/js/upload/jquery.MultiFile.js" type="text/javascript" language="javascript"></script>
	<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<script src="<%=path%>/common/js/upload/jquery.blockUI.js" type="text/javascript" language="javascript"></script>
			<script src="<%=path%>/common/js/common/common.js"
      type="text/javascript"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/service.js"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></script>
		<script type="text/javascript">
		$(document).ready(function() {
<%--			//选择接收人--%>
<%--			$("#addPerson").click(function(){
				var ret=OpenWindow(this.url,"600","400",window);
			});
			
			//清空接收人
			$("#clearPerson").click(function(){
				$("#pcname").val("");
				$("#orguserid").val("");
			});--%>
			
				<%--var appSuggestionForm = document.getElementById("appSuggestionForm");
				appSuggestionForm.suggestionContent.value = '${suggestionContent}';
				
				 	
				onCharsChange(appSuggestionForm.suggestionContent);--%>
				
				var appSuggestionForm = document.getElementById("appSuggestionForm");
				
				var dictItemValueLength='${suggestionLength}';
				
				
				//appSuggestionForm.dictItemValue.value = $.trim(dictItemValue);
				
				onCharsChangeLength(dictItemValueLength);
		});
		
		
		//表单提交
		 var onSub=function(){
		 var suggestionContent= $.trim($("#suggestionContent").val());
		 
		 suggestionContent = suggestionContent.replace(new RegExp("\"","gm"),"“");
		 suggestionContent = suggestionContent.replace(/[\']/gm,"’");
		 /*
		 suggestionContent = suggestionContent.replace(new RegExp("\n","gm")," ");
		 suggestionContent = suggestionContent.replace(new RegExp("\r","gm"),"");
		 */
		 suggestionContent = suggestionContent.replace(new RegExp("<","gm"),"＜");
		 suggestionContent = suggestionContent.replace(new RegExp(">","gm"),"＞");
		 suggestionContent = suggestionContent.replace(/[\\]/gm, "＼");
		 $("#suggestionContent").val(suggestionContent);
		 
		  var suggestionCode=$("#suggestionCode").val();
		 
		  var suggestionSeq=$("#suggestionSeq").val();
		 var seqRet1=/^\d{1,8}(\.\d{1,2})?$/; 
		  if(suggestionSeq==null||suggestionSeq==""||suggestionSeq=="null"){
		  	 alert("排序顺序不能为空。");
			 document.getElementById("suggestionSeq").focus();
			 return false;
		  }
		 var flag = true;
			 if(suggestionSeq!=""){
			 	if(suggestionSeq.lengt>11){
			 		flag=false;
			 	}else{
			 		if(!seqRet1.test(suggestionSeq)){
			 			flag=false;
			 		}
			 	}
			 }
			if(flag){
				 if(suggestionContent==""){
					 alert("常用意见内容不能为空。");
					 document.getElementById("suggestionContent").focus();
					 return false;
				 }
				
				 
				 $.post("<%=path%>/suggestion/approvalSuggestion!IsSuggestionContent.action",
			           {"suggestionContent":suggestionContent,"suggestionCode":suggestionCode},
			           function(data){
			           		if(data=="0"){
			           			//$("#appSuggestionForm").submit();
			           			$.post("<%=path%>/suggestion/approvalSuggestion!save.action",
						 			{"model.suggestionCode":$("#suggestionCode").val(),"model.suggestionUserid":$("#suggestionUserid").val(),"model.suggestionContent":$("#suggestionContent").val(),
						 			 "model.suggestionSeq":$("#suggestionSeq").val()},
						 			function(ret){
						 				if(ret == "0"){
						 					//alert("数据保存成功。");
				           					window.returnValue="ok";
						 					window.close();
						 				} else if(ret == "-1"){
						 					alert("数据保存失败。");
						 					return;
						 				}
						 			});
			           			
				 				
			           		}	
			           		else{
								alert(data);
								document.getElementById("suggestionContent").value="";
								document.getElementById("suggestionContent").focus();
							 }
			           	 });	
			
			}else{
				alert("请输入正确的排序号。(如：1.00 ，1.1 ，1)");
			}
		 
		 }
		 
		 function cancelWindow(){
		 	window.close();
		 	//window.location.href="<%=path%>/suggestion/approvalSuggestion.action";
		 }
		 
		  var cap_max=100;//可发送的字数
		 function onCharsChange(varField){
		 	 var suggestionContent = document.getElementById("suggestionContent");
		 	 var charsmonitor1 = document.getElementById("charsmonitor1");
		 	 var charsmonitor2 = document.getElementById("charsmonitor2");
		     var leftChars = getLeftChars(varField);
		     if ( leftChars >= 0)
		     {
			 	//charsmonitor1.value=cap_max-leftChars;
			 	//charsmonitor2.value=leftChars;
			 	charsmonitor1.innerHTML=cap_max-leftChars;
			 	charsmonitor2.innerHTML=leftChars;
			 	return true;
		     }
		     else
		     {
		     	charsmonitor1.value=cap_max;
		     	charsmonitor2.value="0";
		     	window.alert("意见内容超过字数限制。");
		     	var len = suggestionContent.value.length + leftChars;
			 	suggestionContent.value = suggestionContent.value.substring(0, len);
			 	leftChars = getLeftChars(suggestionContent);
		     	if ( leftChars >= 0)
		     	{
				charsmonitor1.innerHTML=cap_max-leftChars;
				charsmonitor2.innerHTML=leftChars;
				}
		        return false;
		     }
		 }
		 
		 
		 function getLeftChars(varField){
		    var cap = cap_max;
		    var leftchars = cap - varField.value.length;
		    return (leftchars);
		 }
		 
		 
		  function onCharsChangeLength(varFieldLength){
		 	 var dictItemValue = document.getElementById("dictItemValue");
		 	 var charsmonitor1 = document.getElementById("charsmonitor1");
		 	 var charsmonitor2 = document.getElementById("charsmonitor2");
		     var leftChars = getLeftCharsLength(varFieldLength);
		     if ( leftChars >= 0)
		     {
			 	//charsmonitor1.value=cap_max-leftChars;
			 	//charsmonitor2.value=leftChars;
			 	charsmonitor1.innerHTML=cap_max-leftChars;
			 	charsmonitor2.innerHTML=leftChars;
			 	return true;
		     }
		     else
		     {
		     	charsmonitor1.value=cap_max;
		     	charsmonitor2.value="0";
		     	window.alert("意见内容超过字数限制。");
		     	var len = dictItemValue.value.length + leftChars;
			 	dictItemValue.value = dictItemValue.value.substring(0, len);
			 	leftChars = getLeftChars(dictItemValue);
		     	if ( leftChars >= 0)
		     	{
				charsmonitor1.innerHTML=cap_max-leftChars;
				charsmonitor2.innerHTML=leftChars;
				}
		        return false;
		     }
		 }
		 
		  function getLeftCharsLength(varField){
		    var cap = cap_max;
		    var leftchars = cap - varField;
		    return (leftchars);
		 }
		 
		</script>
		<style type="text/css">
		input,select,textarea{
		background-color:#ffffff;
		}
		</style>
  </head>

  <body  overflow="hidden" class="contentbodymargin">
 	<form action="<%=path%>/suggestion/approvalSuggestion!save.action" method="post"  id="appSuggestionForm" name="appSuggestionForm">
 	<input type="hidden" name="model.suggestionUserid" id="suggestionUserid" value="${model.suggestionUserid}">
 	<input type="hidden" name="model.suggestionCode" id="suggestionCode" value="${model.suggestionCode }">
    <div  style="overflow:visible"  id="contentborder">
    
    <table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
				<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
								<strong>保存意见</strong>
							</td>
								<td align="right">
									<table border="0" align="right" cellpadding="00" cellspacing="0">
					     	           <tr>
						                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
						                 	<td class="Operation_input" onclick="onSub();">&nbsp;保&nbsp;存&nbsp;</td>
						                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
					                  		<td width="5"></td>
						                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
						                 	<td class="Operation_input1" onclick="cancelWindow();">&nbsp;取&nbsp;消&nbsp;</td>
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
							<td class="biao_bg1" align="right" width="30" valign="top">
								<span class="wz"><font color=red>*</font>&nbsp;常用意见 ：&nbsp;</span>
							</td>
							<td width="70">
							<%-- <textarea cols="20" id="suggestionContent" name="model.suggestionContent" rows="5" wrap="on">${model.suggestionContent }</textarea>--%>
                				<textarea cols="52" rows="21" name="model.suggestionContent" id="suggestionContent" class="BigInput" wrap="on" onpaste="return onCharsChange(this);"
											  onpropertychange="return onCharsChange(this);" >${model.suggestionContent }</textarea>
                			</td>
						</tr>
						<tr>
						<td>
						</td>
						<td>
						<span class="wz" ><font color="#999999">
											已输入<font color="green"><span id="charsmonitor1">0</span></font>个字，剩余<font color="blue"><span id="charsmonitor2">100</span></font>个字，最多输入<font color="red">100</font>个字
										</font></span>
						</td>
						</tr>
						<tr>
						<td class="biao_bg1" align="right" valign="top" >
							<span class="wz"><font color=red>*</font>&nbsp;排序序号 ：&nbsp;</span>
						</td>
						<td>
						<s:textfield id="suggestionSeq" name="model.suggestionSeq" style="border:1px solid #b3bcc3;background-color:#ffffff;" cssClass="txtput" size="50" maxlength="8" ></s:textfield>
                				<div style="display:none;">
	                				<s:textfield id="dectItemSeq1" name="model.dectItemSeq1" cssClass="txtput" ></s:textfield>
	                			</div>
	                		</td>
	                </tr>
	                <tr>
							<td class="table1_td"></td>
							<td></td>
						</tr>
	                </table>
	                </td>
	                </tr>
	                
	                </table>
      <!--<div id="step1"
        style="padding: 10px 10px 10px 10px; font-size: 12px;">
        <fieldset>
          <legend>
          <table >
          	<tr><td><img src="<%=frameroot%>/images/ico/ico.gif" width="7" height="7">&nbsp;常用意见</td></tr>
          </table>
          </legend>
          
          <div style="padding: 10px 10px 10px 10px;text-align: center;">
                        
              <div style="padding: 1px 1px 1px 1px; text-align: center;">
                <div>
                	<table>
                		
                	
                		<tr>
                			<td>
			                  (<span><font color="red">*</font>)常用意见：</span>:
                			</td>
                		</tr>
                		<tr>
                			<td>
<%--				                 <textarea cols="20" id="suggestionContent" name="model.suggestionContent" rows="5" wrap="on">${model.suggestionContent }</textarea>--%>
                				<textarea cols="53" rows="21" name="model.suggestionContent" id="suggestionContent" class="BigInput" wrap="on" onpaste="return onCharsChange(this);"
											  onpropertychange="return onCharsChange(this);" > ${model.suggestionContent }</textarea>
                			</td>
                			
                		</tr>
                		<tr>
                			<td align="center">
								
									&lt;已输入<font color="green"><span id="charsmonitor1">0</span></font>个字符，剩余<font color="blue"><span id="charsmonitor2">2000</span></font>个字符，最多输入<font color="red">2000</font>个字符&gt;
                			</td>
                		</tr>
                		<tr>
                			<td>
                				排序顺序(<span><font color="red">*</font>)</span>:&nbsp;&nbsp;&nbsp;&nbsp;
                				<s:textfield id="suggestionSeq" name="model.suggestionSeq" cssClass="txtput" size="25" maxlength="10" ></s:textfield>
                				<div style="display:none;">
	                				<s:textfield id="dectItemSeq1" name="model.dectItemSeq1" cssClass="txtput" ></s:textfield>
                					
                				</div>
                			</td>
                		</tr>
                	</table>
                
                  
<%--                 <input type="text" name="model.suggestionContent" maxlength="25" value="${model.suggestionContent }" id="suggestionContent">--%>
                 
                  
                </div>

       
          </div>-->
          <!--<div style="width: 100%;" align="center">
     
       <p>&nbsp;</p>
        <span><input type="button"  value="保  存" id="sumit" 
            onclick="onSub()" class="input_bg"></span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <span><input type="button" id="cancel" value="返  回" class="input_bg" onclick="cancelWindow()"></span>
      </div>
      -->
     </div>
     </div>
     </form>
  </body>
</html>
