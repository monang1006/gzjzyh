
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<html>
	<head>
		<title>填写意见</title>
		<%@include file="/common/include/meta.jsp"%>
		<LINK href="<%=frameroot%>/css/windows.css" type=text/css
			rel=stylesheet>
		<link href="<%=frameroot%>/css/properties_windows_add.css" type="text/css"
			rel=stylesheet>
		<script language="javascript"
			src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script src="<%=path%>/common/js/common/common.js"
			type="text/javascript"></script>
		<style type="text/css">
		#cyyj{ margin-bottom: 4px;
		}
		</style>
		<script type="text/javascript">
		   
			String.prototype.trim = function() {
				//截掉后面的空格
                var strTrim = this.replace(/(\s*$)/g, "");
                //strTrim = strTrim.replace(/^[\s　\t]+|[\s　\t]+$/, ""); 
                return strTrim;
            }
		
			function doSwitchSuggestion(obj){
				var value = obj.value;
	        	if(value != "0"){
	        		if(obj.options[obj.selectedIndex].text.length>40){
	   			        obj.options[obj.selectedIndex].text= obj.options[obj.selectedIndex].text.substring(0,40)+"......"; 
	        		} 
	   			    //插入意见到光标所在位置
	        		insertText(value);
	        	}
			}
			$(document).ready(function(){
				//tj
				var h = window.screen.height;//"屏幕分辨率的高："
      			var w = window.screen.width;//"屏幕分辨率的宽："
      			//alert(w)
      			if(w=='1366')  {
					$("#yj").css("width","770px");
      			} 
      			//
				var parWin = window.dialogArguments;
				if(parWin[1]){
				    $("#suggestion").val(parWin[1]);
				}else{
				    $("#suggestion").val(" ");
				    $("#suggestion").val("");
				}
		    /**	  if($("suggestion").val() == "" ){
		    *			$("#td_delete").hide();  
		    *    		} 
			*	 if($("#suggestion").val() != ""){     //意见存在时显示删除按钮
			*		$("#td_delete").show();
			*	
			*	}
			*/     //去掉意见存在时显示删除按钮 
			
			
				$.each($("#yj").find("option"),function(i,opt){//对下拉过长的意见进行截取
					//checkText +=","+opt.id;
					var obj = $(opt).text();
					if(obj.length>36){
	   			     $(opt).text(obj.substring(0,36)+"..."); 
	        		} 
				});
			});
			
			 //编辑意见
		      function editDict(){
		          var ReturnStr=OpenWindow("<%=root%>/suggestion/approvalSuggestion.action?state=state",600,360, window);
		              if(ReturnStr!=null&&ReturnStr!=""&&ReturnStr=="ok"){
		              		 $.post('<%=root%>/suggestion/approvalSuggestion!select.action',
					              function(data){
						              $("#yj").html(data);			          
					       });
		              }  
	              $.each($("#yj").find("option"),function(i,opt){//对下拉过长的意见进行截取
						//checkText +=","+opt.id;
						var obj = $(opt).text();
						if(obj.length>36){
		   			     $(opt).text(obj.substring(0,36)+"..."); 
		        		} 
					});    
		      }
		      //保存意见
		      function doSave(){
		      	var suggestion = document.getElementById("suggestion").value;
		      	suggestion = suggestion.trim();
		      	if($.trim(suggestion) != ""){
		      		window.returnValue = suggestion;
		      	    window.close();
		      		// alert("意见输入不能为空，请输入!");
		      		// return ;
		      	}
		      	else if (suggestion.length>100){
		      		alert("意见内容不能超过100个汉字!");
		         	return ;
		      	}
		      	else if ($.trim(suggestion)==""){
		      	if(confirm("意见内容为空，确定要保存吗？"))
		      	{
		      	window.returnValue = suggestion;
		      	window.close();
		      	}
		      	else
		      	{
		      	return;
		      	}
		      	 }
		      }
		      
		     var cap_max=100;//可发送的字数
		     function onCharsChange(varField,dd){
			 	 var suggestion = document.getElementById("suggestion");
			 	 var charsmonitor1 = document.getElementById("charsmonitor1");
			 	 var charsmonitor2 = document.getElementById("charsmonitor2");
			     var leftChars = getLeftChars(varField);
			     if ( leftChars >= 0){
				 	//charsmonitor1.value=cap_max-leftChars;
				 	//charsmonitor2.value=leftChars;
				 	charsmonitor1.innerHTML=cap_max-leftChars;
				 	charsmonitor2.innerHTML=leftChars;
				 	return true;
			     } else{
			     	charsmonitor1.value=cap_max;
			     	charsmonitor2.value="0";
			     	window.alert("意见内容超过字数限制!");
			     	var len = suggestion.value.length + leftChars;
				 	suggestion.value = suggestion.value.substring(0, len);
				 	leftChars = getLeftChars(suggestion);
			     	if ( leftChars >= 0){
						charsmonitor1.innerHTML=cap_max-leftChars;
						charsmonitor2.innerHTML=leftChars;
					}
		        return false;
		     }
		     $('#suggestion').focus();
		 }
		 
		 
		    function getLeftChars(varField){
		    var cap = cap_max;
		    var leftchars = cap - varField.value.length;
		    return (leftchars);
		 }
		 
		     // 清空意见 删除改成清空叫贴切
		      function doDelete(){
		          if(document.getElementById("suggestion").value == ""){
		           alert("没有意见内容!"); //当无意见内容时提示
		           return false;
		            }
		      	if( confirm("确定要清空意见吗？")){
			     // 	window.returnValue = "";  
			     //  window.close();
			       document.getElementById("suggestion").value="";  //当无内容点击清空是弹出提示
			     //  document.getElementById('empty').style.display='none';
		      	}
		      }
		   
		   
	
		      
	/**
	 * author:luosy
	 * description:在意见文本框当前光标位置 插入选定内容
	 * modifyer:
	 * description:
	 * @param str
	 * @throws Exception
	 */
		function insertText(str) {
			obj = document.getElementById("suggestion");
			obj.focus();
		    if (document.selection) {
		        var sel = document.selection.createRange();
		        sel.text = str;
		    } else if (typeof obj.selectionStart === 'number' && typeof obj.selectionEnd === 'number') {
		        var startPos = obj.selectionStart,
		            endPos = obj.selectionEnd,
		            cursorPos = startPos,
		            tmpStr = obj.value;
		        obj.value = tmpStr.substring(0, startPos) + str + tmpStr.substring(endPos, tmpStr.length);
		        cursorPos += str.length; 
		        obj.selectionStart = obj.selectionEnd = cursorPos;
		    } else {
		        obj.value += str;
		    }
		}
		</script>
	</head>
	<body class=contentbodymargin>
		<div id=contentborder align=center>
			<form>
				<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td></td>
						</tr>
						<tr style="padding-top: 10px;">
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
								填写意见
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
					                <tr>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onclick="doDelete();">&nbsp;清&nbsp;空&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onclick="doSave();">&nbsp;保&nbsp;存&nbsp;</td>
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
				<br>
				<fieldset style="width: 98%">
					<legend>
						<span class="wz"> <script>
									var parWin = window.dialogArguments;
				            		var nodeName = parWin[0];
				            		document.write("当前环节：" + nodeName + "&nbsp;");
				            	</script> </span>
					</legend>
					<div
						style="padding: 10px 10px 10px 10px; text-align: left; font-size: 12px;">
						<div>
							<select id="yj" style="width: 550px;"
								onchange="doSwitchSuggestion(this);">
								<option value="0">
									选择常用意见
								</option>
								<s:iterator value="ideaLst">
									<option value="${suggestionContent }">
										${suggestionContent }
									</option>
								</s:iterator>
							</select>
							&nbsp;
							<!--<span style="cursor:hand;" color="red" onclick="editDict();"><font color="red">常用意见</></span>-->
							<a id="cyyj"  href="#" class="button" onclick="editDict();">常用意见</a>&nbsp;
						</div>
						<div>
							<textarea style="width: 100%;" rows="16"
								onpaste="return onCharsChange(this);"
								onpropertychange="return onCharsChange(this);"
								onfocus="return onCharsChange(this);" name="model.suggestion"
								id="suggestion" wrap="on">${model.suggestion }</textarea>
						</div>
					</div>
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
						<tr>
							<span class="wz" ><font color="#999999">
											&nbsp;已输入<font color="green"><span id="charsmonitor1">0</span></font>个字，剩余<font color="blue"><span id="charsmonitor2">100</span></font>个字，最多输入<font color="red">100</font>个字
										</font></span>
							<!-- 用不同颜色表示不同状态的字数 -->
						</tr>
						</tr>
					</table>
				</fieldset>
			</form>
		</div>
	</body>
</html>
