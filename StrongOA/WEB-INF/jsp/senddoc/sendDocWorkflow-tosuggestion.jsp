<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<%@taglib uri="/tags/web-newdate" prefix="strong"%>
<html>
  	<head>
    	<title>填写意见</title>
		<%@include file="/common/include/meta.jsp"%>
		<LINK href="<%=frameroot%>/css/windows.css" type=text/css rel=stylesheet>
		<link href="<%=frameroot%>/css/properties_windows.css" type="text/css" rel=stylesheet>
		<script language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
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
	        		//$("#suggestion").val(value);
	        		//插入意见到光标所在位置
	        		insertText(value);
	        	}
			}
			$(document).ready(function(){
				var parWin = window.dialogArguments;
				$("#suggestion").val(parWin[1]);
				if($("#suggestion").val() != ""){//意见存在时显示删除按钮
					$("#td_delete").show();
				}
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
		      }
		      //保存意见
		      function doSave(){
		      	var suggestion = document.getElementById("suggestion").value;
		      	suggestion = suggestion.trim();
		      	if($.trim(suggestion) == ""){
		      		alert("意见输入不能为空，请输入。");
		      		return ;
		      	}
		      	if(suggestion.length >= 2000){
		      		alert("意见内容不能超过2000个汉字。");
		      		return ;
		      	}
		      	var objSelect = document.getElementById("taskSelect");
		      	var info = new Array();
		      	if(objSelect != null){
			      	for(var i = 0; i<objSelect.length; i++){
				   		if(objSelect.options[i].selected == true){
				   			info[0] = suggestion;
				   			info[1] = objSelect.options[i].taskId;
		      				info[2] = objSelect.options[i].value;
		      				info[3] = objSelect.options[i].text;
				   		}
				    }
		      	}
		      	
		      	info[4] = document.getElementById("startDate").value;
		      	window.returnValue = info;
		      	window.close();
		      }
		      //删除意见
		      function doDelete(){
		      	if(confirm("确定要删除意见吗？")){
			      	var objSelect = document.getElementById("taskSelect");
			      	var info = new Array();
			      	if(objSelect != null){
				      	for(var i = 0; i<objSelect.length; i++){
					   		if(objSelect.options[i].selected == true){
					   			info[0] = "";
					   			info[1] = objSelect.options[i].taskId;
			      				info[2] = objSelect.options[i].value;
			      				info[3] = objSelect.options[i].text;
					   		}
					    }
			      	}
			      	info[4] = document.getElementById("startDate").value;
			      	window.returnValue = info;
			      	window.close();
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
  				<br>
	  			<fieldset style="width: 90%">
	  					<legend>
							<span class="wz"> 
								<script>
									var parWin = window.dialogArguments;
				            		var nodeName = parWin[0];
				            		document.write("当前环节：" + nodeName + "&nbsp;");
				            	</script>
							</span>
						</legend>
						<div style="padding: 10px 10px 10px 10px; text-align: left;font-size: 12px;">
				            <div>
								<select id="yj" style="width:400px;" onchange="doSwitchSuggestion(this);">
									<option value="0">&lt;选择常用意见&gt;</option>
									<s:iterator value="ideaLst">
										<option value="${suggestionContent }">${suggestionContent }</option>
									</s:iterator>
								</select>
								&nbsp; 
								<input name="button" type="button" onclick="editDict();" class="input_bg" value="常用意见">
				            </div>
				            <div>
				              <textarea style="width: 98%" id="suggestion" name="suggestion" rows="20" wrap="on"></textarea>
				            </div>
				          </div>
						<table width="100%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td align="center" valign="middle">
									<table border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td width="25%" align="left">
												<script type="text/javascript">
													var userTask = parWin[2];
													if(userTask != null && userTask.length > 0){
														document.write("实际处理人:");
														document.write("<select id=taskSelect>");
														$.each(userTask,function(i,jobj){
															document.write("<option taskId='"+jobj.taskId+"' value='"+jobj.userId+"'>");
															document.write(jobj.userName);
															document.write("</option>");
														});
														document.write("</select>");
													}
												</script>
											</td>
											
											<td width="35%" align="left">
												<script type="text/javascript">
													document.write("处理时间:");
												</script>
						                    	<strong:newdate name="startDate" id="startDate" width="65%"
						                    		skin="whyGreen" isicon="true" dateform="yyyy-MM-dd HH:mm:ss" dateobj="${date}"></strong:newdate>
						                  	</td>
											
											<td width="10%"></td>
												<td id="td_delete" style="display: none;">
													<input id="save" name="save" type="button" onclick="doDelete();" class="input_bg" value="删 除">
												</td>
												<td>
													&nbsp;&nbsp;
												</td>
												<td>
													<input id="save" name="save" type="button" onclick="doSave();" class="input_bg" value="保 存">
												</td>
												<td>
													&nbsp;&nbsp;
												</td>
												<td >
													<input name="button" type="button" onclick="window.close();" class="input_bg" value="取 消">
												</td>
											<td width="10%"></td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
	  			</fieldset>
  			</form>
  		</div>
  	</body>
</html>
