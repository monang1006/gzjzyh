<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/tags/c.tld" prefix="c"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>选择人员</title>
		<link href="<%=frameroot%>/css/windows.css" type='text/css'
			rel="stylesheet">
		<script type="text/javascript" language="javascript"
			src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
	</head>
	<body class="contentbodymargin">
		<br>
		<div id="contentborder" cellpadding="0">
			<center>
				<table border="1" cellspacing="0" width="95%" height="90%"
					bordercolordark="#FFFFFF" bordercolorlight="#000000"
					bordercolor="#333300" cellpadding="2">
					<tr>
						<td width="100%">
							<table border="0" width="100%" cellpadding="0" cellspacing="0">
								<tr>
									<td valign='middle'></td>
								</tr>
								<tr><td>&nbsp;</td>
									<td valign='middle' width="50%">
										<img src='<%=root%>/images/ico/tb-change.gif'
											align='absmiddle' border="0" alt="">
										&nbsp;
										<strong>请选择</strong>
									</td>
									<td width="50%">&nbsp;</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td id="radioDiv">
						</td>
					</tr>
					<tr>
						<td id="treeIframe" style="height:400px;">
							<table border="0" width="95%" bordercolor="#FFFFFF"
								cellspacing="0" cellpadding="0" height="100%">
								<tr>
									<td width="45%" align="middle">
										<table border="0" width="95%" height="100%"
											bordercolor="#FFFFFF" cellspacing="0" cellpadding="0">
											<tr>
												<td width="100%" height="100%" id="frameDiv">
												</td>
											</tr>
										</table>
									</td>
									<td width="5%" align="middle">
										<p>
											<input type="button" style="width: 30" value="&lt;--"
												onclick="deleteOptionItem();" />
										</p>
										<p>
											<input type="button" style="width: 30" value="&lt;&lt;--"
												onclick="deleteOptionItems();" />
										</p>
									</td>
									<td width="45%" align="middle" valign="top" id="selectDiv">
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr height="5%">
						<td  align="center">
							<input type="button" class="input_bg" value="确定"
								onclick="submitFunction();" />
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<input type="button" class="input_bg" value="取消"
								onclick="cancelFunction();" />
						</td>
					</tr>
				</table>
			</center>
		</div>
	</body>
</html>
<script>
			var actorSettings = ${actorSettings};

			function  addOption(alias, strName, strValue){
			   var objSelect = $("#select_" + alias)[0]; 
			   var aleadyHave = false;
			   for(var i = 0; i<objSelect.options.length; i++){
			   		if(objSelect.options[i].value == strValue){
			   			aleadyHave = true;
			   			break;
			   		}
			   }
			   
		       if(aleadyHave == false){
		       		strValue = $.trim(strValue);
		      		var  objOption  =  new  Option(strName,strValue);  
		      		objSelect.options.add(objOption, objSelect.options.length);
		      	}
			}
			
			function  removeOption(alias, strName, strValue){ 
				var objSelect = $("#select_" + alias)[0];  
	            for(var i=0; i<objSelect.options.length; i++){
	            	if(objSelect.options[i].value == strValue){
	            		objSelect.options[i] = null;
	            	}
	            }
			}
			
			function deleteOptionItem(){
				if(actorSettings.length > 0){
					for(var i=0; i<actorSettings.length; i++){
						var objSelect = $("#select_" + actorSettings[i].alias)[0];
						if(objSelect.selectedIndex >= 0){
							if($("#frame_" + actorSettings[i].alias).attr("src") != ""){
								eval("frame_" + actorSettings[i].alias).unChecked(objSelect.options[objSelect.selectedIndex].value);
							}
							objSelect.remove(objSelect.selectedIndex);
						}
					}
				}
			}  
			
			function deleteOptionItems(){
				if(actorSettings.length > 0){
					for(var i=0; i<actorSettings.length; i++){
						var objSelect = $("#select_" + actorSettings[i].alias)[0];
						var index = objSelect.selectedIndex;
						while(index >= 0){
							if($("#frame_" + actorSettings[i].alias).attr("src") != ""){
								eval("frame_" + actorSettings[i].alias).unChecked(objSelect.options[index].value)
							}
							objSelect.remove(index);
							index = objSelect.selectedIndex;
						}
					}
				}
			}
			
			function submitFunction() {
				var returnValue = [];
				if(actorSettings.length > 0){
					for(var i=0; i<actorSettings.length; i++){
						var objSelect = $("#select_" + actorSettings[i].alias)[0];
						for(var j = 0; j<objSelect.options.length; j++){
							returnValue.push(objSelect.options[j].value + "," + objSelect.options[j].text);
					   	}
					}
				}
				if(typeof(window.dialogArguments.setSelectedData) != "undefined"){
					//alert(returnValue);
					window.dialogArguments.setSelectedData(returnValue);
				}
				window.close();
			}
			
			function cancelFunction() {
				window.close();
			}
	
	/**
	 * 任务处理人员的先后顺序调整
	 * @param {Object} direction
	 * @param {Object} selectObjName
	 * @return {TypeName} 
	 */
	function moveUp(direction, selectObjName){
		var currentSel = document.getElementById(selectObjName);
		if(currentSel == null){
			alert("请选择要移动的项!");
			return;
		}
		var index = currentSel.selectedIndex;
		if(index == -1){
			alert("请选择要移动的项!");
			return;
		}
		if(direction == 1 || direction == 2){//上移
			if(index == 0){
				alert("选中的项无法上移!");
				return;
			}
			
			var toIndex = index - 1;//和上一个元素进行交换
			if(direction == 1){//置顶
				var value = currentSel.options[index].value; 
				var text = currentSel.options[index].text; 
				for(var i=index; i>0; i--){
					currentSel.options[i].value = currentSel.options[i-1].value; 
					currentSel.options[i].text = currentSel.options[i-1].text; 
				}
				currentSel.options[0].value = value;
				currentSel.options[0].text = text;
				currentSel.options[index].selected = false; 
				currentSel.options[0].selected = true; 
			}else if(direction == 2){//上移
				var value = currentSel.options[toIndex].value; 
				var text = currentSel.options[toIndex].text; 
				
				currentSel.options[toIndex].value = currentSel.options[index].value; 
				currentSel.options[toIndex].text = currentSel.options[index].text; 
				
				currentSel.options[index].value = value; 
				currentSel.options[index].text = text; 
				
				currentSel.options[index].selected = false; 
				currentSel.options[toIndex].selected = true; 
			}
		} else if(direction == 3 || direction == 4){//下移 
			if(index == (currentSel.length-1)){
				alert("选中的项无法下移!");
				return; 
			}
		
			var toIndex = index + 1;//和下一个元素进行交换
			if(direction == 4){//置底
				var value = currentSel.options[index].value; 
				var text = currentSel.options[index].text; 
				for(var i=index; i<currentSel.length-1; i++){
					currentSel.options[i].value = currentSel.options[i+1].value; 
					currentSel.options[i].text = currentSel.options[i+1].text; 
				}
				currentSel.options[currentSel.length-1].value = value;
				currentSel.options[currentSel.length-1].text = text;
				currentSel.options[index].selected = false; 
				currentSel.options[currentSel.length-1].selected = true; 
			}else if(direction == 3){//下移
				var value = currentSel.options[toIndex].value; 
				var text = currentSel.options[toIndex].text; 
				
				currentSel.options[toIndex].value = currentSel.options[index].value; 
				currentSel.options[toIndex].text = currentSel.options[index].text; 
				
				currentSel.options[index].value = value; 
				currentSel.options[index].text = text; 
				
				currentSel.options[index].selected = false; 
				currentSel.options[toIndex].selected = true; 
			}
		} 
	}
			
	$(window).load(function(){
		function changeTree(alias, url) {
			$("iframe").hide();
		    $("#frame_" + alias).show();
		    if($("#frame_" + alias).attr("visited") == "0"){
		    	var currentSelect = $("#select_" + alias)[0];
		    	var selected = "";
		    	for(var k=0; k<currentSelect.options.length; k++){
		    		selected = selected + "," + currentSelect.options[k].value;
		    	}
		    	if(selected != ""){
		    		selected = selected.substring(1);
		    	}
		    	if(url.indexOf("?") == -1){
		    		url = url + "?1=1";
		    	}
		    	 //解决bug-2593  yanjian 2011-11-04
		    	/*
		    	if(url.indexOf("?") == -1){
		    		url = url + "?initData=" + selected;
		    	}else{
		    		url = url + "&initData=" + selected;
		    	}
		    	*/
		    	//增加对全局流程的支持 dengzc 2011年6月3日15:18:09
		    	var type = "<%=request.getAttribute("type")%>";
		    	if(type == "null" || type == ""){
		    		type = "<%=request.getParameter("type")%>";
		    	}
		    	if(type != "null"){
		    		url += "&type="+type;
		    	}
		    		url += "&dispatch=${dispatch}";//dispatch = ["ag"：动态选择处理人,"ra":指派选人]
		    		
		    	//------------------- End ---------------------
		    	$("#frame_" + alias).attr("src", url);
		    	$("#frame_" + alias).attr("visited", "1");
		    }
		    return true;
		}
	
		if(actorSettings.length > 0){
			var initDatas;
			if(typeof(window.dialogArguments.getInitData) == "undefined"){
				initDatas = [];
			}else{
				initDatas = window.dialogArguments.getInitData();
			}
			//alert(initDatas);
			var selectHeight = parseInt(360/actorSettings.length);
			var alias;
			var prefix;
			for(var i=0; i<actorSettings.length; i++){
				prefix = actorSettings[i].prefix;
				alias = actorSettings[i].alias;
				$("#radioDiv").append("<input type='radio' name='radio_' id='radio_" + alias + "' value='" + alias + "' />" + actorSettings[i].name);
				$("#radio_" + alias).bind("click", {alias:alias, url:actorSettings[i].url}, function(event){
					$(this).attr("checked", "true");
					changeTree(event.data.alias, event.data.url);
				});
				
				$("#frameDiv").append("<iframe visited='0' id='frame_" + alias + "' frameborder='0' scrolling='no' style='width:100%; height:100%;' style='display:none;' />");
				
				//$("#selectDiv").append("<p><select id='select_" + alias + "' size='100' multiple='multiple' style='width:200px; height:" + selectHeight + "px;'></select><span style='width:50px;'><input type='button' value='置顶' onclick='moveUp(1,\"select_" + alias + "\")'><input type='button' value='上移' onclick='moveUp(2,\"select_" + alias + "\")'><input type='button' value='下移' onclick='moveUp(3,\"select_" + alias + "\")'><input type='button' value='置底' onclick='moveUp(4,\"select_" + alias + "\")'></span></p>");
				
				$("#selectDiv").append("<p><select id='select_" + alias + "' size='100' multiple='multiple' style='width:200px; height:" + selectHeight + "px;'></select><span style='width:50px;'></span></p>");
				if(initDatas.length > 0){
					var initData;
					for(var j=0; j<initDatas.length; j++){
						if(initDatas[j].substring(0, 1) == prefix){
							initData = initDatas[j].split(",");
							$("#select_" + alias).append("<option value='" + initData[0] + "'>" + initData[1] + "</option>");
						}
					}
				}
			}
			$("#radio_" + actorSettings[0].alias).click();
		}
	});
</script>
