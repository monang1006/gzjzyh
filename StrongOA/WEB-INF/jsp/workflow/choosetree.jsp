<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/c.tld" prefix="c" %>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>工作流选择人员</TITLE>
		<link href="<%=root%>/workflow/css/dtree.css" type="text/css" rel="stylesheet">
		<link href="<%=root%>/workflow/css/windows.css" type="text/css" rel="stylesheet">
		<script language="javascript" src="<%=root%>/workflow/js/dtree_checkbox.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script language="javascript" src="<%=path%>/common/js/loadmask/jquery.loadmask.js"></script>
		<script type="text/javascript">
			$(document).ready(function(){
				var count = $("input:checkbox[id^=u]").size();//人员节点数目
				if(count == 2){//说明只需要选择一个人人员：按人员显示有一个人员节点；按机构显示也有一个人员节点
					$("input:checkbox[id^=u]").attr("checked",true);
					gotoSelectCheck(true,$("input:checkbox[id^=u]").eq(0).val());
				}
				//$(window.parent.document).find("#userFieldset").unmask();
				$("#userFieldset",window.parent.document).unmask();
			});
		
			//全选
			function doSelectAll(flag){
				$("input:checkbox[id^=u]").each(function(){
					gotoSelectCheck(flag,$(this).val());
				});
			}
		
			function gotoSelectCheck(checkFlag,nodeValue){
	        	if (nodeValue.substring(0, 1) == 'g') {						          
	          		selectTreeNode(checkFlag,nodeValue);
	        	} else {
	          		SelectNodeItem(checkFlag,nodeValue);
	        	}
	        	var nodeId = '${nodeId}';
		  	    var objSelect = document.getElementById("select_person");
		  	    var strTaskActors = "";
				for(var j = 0; j<objSelect.options.length; j++){
					strTaskActors+=","+objSelect.options[j].value.substring(1)+"|"+nodeId;
			   	}
			   	if(strTaskActors != ""){
			   		strTaskActors = strTaskActors.substring(1);
			   	}
				parent.document.getElementById("strTaskActors_" + nodeId).value = strTaskActors;
				parent.selectCount = objSelect.options.length;
				if(parent.maxActors == ""){
		  	    	parent.maxActors = '${maxTaskActors}';
				}
		  	}						  
			
			function selectTreeNode(checkFlag, nodeValue) {
		      for(var i=0; i<d.aNodes.length; i++) {
		        if (d.aNodes[i].pid == nodeValue) {
		          $("input:checkbox[id='"+d.aNodes[i].id+"']").each(function(){
			          var checkBox = this;//document.getElementById(d.aNodes[i].id);
				      if (checkBox.checked != checkFlag) {
				        checkBox.checked = checkFlag;
				      }
				      if (checkBox.value.substring(0, 1) == 'u') {
				        setNodeItem(checkFlag, d.aNodes[i].id, d.aNodes[i].name);
				      } else {
				        selectTreeNode(checkFlag, checkBox.value);
				      }
		          
		          });
		        }
		      }
		  }
		  
		  function SelectNodeItem(checkFlag,nodeValue) {
		    var checkBoxes=document.getElementsByName('rightCheckBox');
		    for(var i=0; i<d.aNodes.length; i++){
		      if (d.aNodes[i].id == nodeValue) {
		      	var checkBox = document.getElementById(d.aNodes[i].id);
			    if (checkBox.checked != checkFlag) {
			      checkBox.checked = checkFlag;							        
				}

		        setNodeItem(checkFlag, d.aNodes[i].id, d.aNodes[i].name);
		        
		        break;
			  }
		    }
		  }
		  
		  function setNodeItem(checkFlag, id, name) {
		      if (checkFlag) {
		        addOption("person", name, id);						        
			  } else {
		        removeOption("person", name, id);
		      }
		  }
			
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
			
			function initChecked() {
               var objSelect = document.getElementById("select_person");;
               //var checkBoxes=document.getElementsByName('rightCheckBox');
               $("input:checkbox").attr("checked",false);
                    if (objSelect.length != 0) {
                        for(var j=0; j<objSelect.options.length; j++){
                        	$("input:checkbox[id^=u][value='"+objSelect.options[j].value+"']").attr("checked",true);
                      		/*for(var i=0; i<checkBoxes.length; i++){
                        		if (checkBoxes[i].value.substring(0, 1) == 'u'){
                         			if (checkBoxes[i].value == objSelect.options[j].value) {
                             			checkBoxes[i].checked = true;
                         			}
                      			}
            				}*/
                    	}
                  }
			}
			function showTree(type){
				if(type == "0"){
					initChecked();
					$("#tr2").show();
					$("#tr3").hide();
					$("#other").attr("src","");
					$("#tr4").hide();
				}
				if(type == "1"){
					initChecked();
					$("#tr3").show();
					$("#tr2").hide();
					$("#other").attr("src","");
					$("#tr4").hide();
				}
				if(type == "2"){
					$("#tr3").hide();
					$("#tr2").hide();
					$("#tr4").show();
					var url = "<%=root %>/senddoc/sendDoc!draft.action";
					$("#other").attr("src",url);
				}
			}
					      
		</script>
	</HEAD>
	<body topmargin="0" leftmargin="0" oncontextmenu="return false;" >
			<table border="0" cellpadding="2" cellspacing="0">
			<%
				String isOrganizationSelect = (String)request.getAttribute("isOrganizationSelect");
				if(!"1".equals(isOrganizationSelect)){
			%>
				<tr>
					<td>
						<table cellspacing="1" width="240" bgcolor="#000000" cellpadding="0" border="0">
							<thead>
								<tr>
									<td bgcolor="#cecece" align="center" width="50%">
										<input id="radioPerson" value="0" onclick="showTree(this.value);" type = "radio" name="rperson" checked="checked" />
										<label for="radioPerson">相关人员</label>
									</td>
									<td bgcolor="#cecece" align="center" width="50%">
										<input id="radioDept" value="1" onclick="showTree(this.value);" name="rperson" type = "radio" />
										<label for="radioDept">按机构显示</label>
									</td>
								</tr>
							</thead>
						</table>
					</td>
				</tr>
				<tr id="tr2">
					<td>
						<div style="width:100%;height:120px;overflow-y:auto;" class="dtree">
						<table width="100%" cellspacing="1" cellpadding="0" border="0">
						<tbody>
							<script language="javascript" type="text/javascript">
                          var imageRootPath='<%=root%>/workflow';
						  var person = new dTree('person');
						  person.add('g0','-1','<input id=chkall type="checkbox" onclick="doSelectAll(this.checked);" style="position:absolute;clip: rect(auto auto auto auto)" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<label for=chkall>${nodeName}</label>','','','','<%=root%>/workflow/images/tree/folder_closed.gif','2');
					      <c:forEach items="${userList}" var="user" varStatus="status">
					          person.add('u<c:out value="${user[0]}"/>','g0','<label title="<c:out value="${user[1]}"/><c:out value="${user[3]}"/>"><c:out value="${user[1]}"/> [<c:out value="${user[2]}"/>]</label>','','','','<%=root%>/workflow/images/tree/renyuan.gif','-2');
					          //person.add('u<c:out value="${user[0]}"/>','g0','<c:out value="${user[2]}"/>','','','','<%=root%>/workflow/images/tree/renyuan.gif','-2');
					      </c:forEach>
						  document.write(person);
						</script>
						</tbody>
				</table>
						</div>
					</td>
				</tr>
				<tr id="tr3" style="display: none;">
					<td>
						<div style="width:100%;height:120px;overflow-y:auto;" class="dtree">
						<table width="100%" cellspacing="1" cellpadding="0" border="0">
						<tbody>
							<script language="javascript" type="text/javascript">
                          var imageRootPath='<%=root%>/workflow';
						  var d = new dTree('d');
						  d.add('g0','-1','${nodeName}','','','','<%=root%>/workflow/images/tree/folder_closed.gif','2');
						  <c:set var="topTreeId" value="0"/>
						  <c:forEach items="${orgList}" var="tree" varStatus="status">
						  <c:choose>
						    <c:when test="${tree[1] == topTreeId}">
						      d.add('g<c:out value="${tree[0]}"/>','g0','<c:out value="${tree[2]}"/>','','','','<%=root%>/workflow/images/tree/folder_closed.gif','-2');
						    </c:when>
						    <c:otherwise>
						      d.add('g<c:out value="${tree[0]}"/>','g<c:out value="${tree[1]}"/>','<c:out value="${tree[2]}"/>','','','','<%=root%>/workflow/images/tree/folder_closed.gif','-2');
						    </c:otherwise>
						  </c:choose>						  
						  </c:forEach>
						  <c:forEach items="${orgList}" var="tree" varStatus="status">
						      <c:forEach items="${tree[3]}" var="user" varStatus="status">
						      	if('<c:out value="${user[0]}"/>' != "") {
						          d.add('u<c:out value="${user[0]}"/>','g<c:out value="${tree[0]}"/>','<label title="<c:out value="${user[1]}"/><c:out value="${user[2]}"/>"><c:out value="${user[1]}"/> </label>','','','','<%=root%>/workflow/images/tree/renyuan.gif','-2');
						      	}
						      </c:forEach>
						  </c:forEach>
						  document.write(d);
						</script>
						</tbody>
				</table>
						</div>
					</td>
				</tr>
			<%
				} else {
			%>	
					<tr id="tr2">
					<td>
						<div style="width:100%;height:120px;overflow-y:auto;" class="dtree">
						<table width="100%" cellspacing="1" cellpadding="0" border="0">
						<tbody>
							<script language="javascript" type="text/javascript">
                          var imageRootPath='<%=root%>/workflow';
						  var person = new dTree('person');
						  person.add('g0','-1','${nodeName}','','','','<%=root%>/workflow/images/tree/folder_closed.gif','2');
					      <c:forEach items="${userList}" var="user" varStatus="status">
					          person.add('@<c:out value="${user[0]}"/>','g0','<label title="<c:out value="${user[1]}"/><c:out value="${user[3]}"/>"><c:out value="${user[1]}"/></label>','','','','<%=root%>/workflow/images/tree/folder_closed.gif','-2');
					      </c:forEach>
						  document.write(person);
						</script>
						</tbody>
				</table>
						</div>
					</td>
				</tr>
				<tr id="tr3" style="display: none;">
					<td>
						<div style="width:100%;height:120px;overflow-y:auto;" class="dtree">
						<table width="100%" cellspacing="1" cellpadding="0" border="0">
						<tbody>
							<script language="javascript" type="text/javascript">
                          var imageRootPath='<%=root%>/workflow';
						  var d = new dTree('d');
						  d.add('g0','-1','${nodeName}','','','','<%=root%>/workflow/images/tree/folder_closed.gif','2');
						  <c:set var="topTreeId" value="0"/>
						  <c:forEach items="${orgList}" var="tree" varStatus="status">
						  <c:choose>
						    <c:when test="${tree[1] == topTreeId}">
						      d.add('g<c:out value="${tree[0]}"/>','g0','<c:out value="${tree[2]}"/>','','','','<%=root%>/workflow/images/tree/folder_closed.gif','-2');
						    </c:when>
						    <c:otherwise>
						      d.add('g<c:out value="${tree[0]}"/>','g<c:out value="${tree[1]}"/>','<c:out value="${tree[2]}"/>','','','','<%=root%>/workflow/images/tree/folder_closed.gif','-2');
						    </c:otherwise>
						  </c:choose>						  
						  </c:forEach>
						  <c:forEach items="${orgList}" var="tree" varStatus="status">
						      <c:forEach items="${tree[3]}" var="user" varStatus="status">
						      	if('<c:out value="${user[0]}"/>' != "") {
						          d.add('@<c:out value="${user[0]}"/>','g<c:out value="${tree[0]}"/>','<label title="<c:out value="${user[1]}"/><c:out value="${user[2]}"/>"><c:out value="${user[1]}"/></label>','','','','<%=root%>/workflow/images/tree/renyuan.gif','-2');
						      	}
						      </c:forEach>
						  </c:forEach>
						  document.write(d);
						</script>
						</tbody>
				</table>
						</div>
					</td>
				</tr>
			<%
				}
			%>	
				<tr id="tr4" style="display: none ;">
					<td>
						<iframe id="other" frameborder="0" src=""></iframe>
					</td>
				</tr>
			</table>
			<select style="display: none ;" id="select_person"></select>
	</body>
</HTML>
