<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/c.tld" prefix="c" %>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>工作流选择人员</TITLE>
		<link href="<%=root%>/workflow/css/dtree.css" type="text/css" rel="stylesheet">
		<link href="<%=root%>/workflow/css/windows.css" type="text/css" rel="stylesheet">
		<script language="javascript" src="<%=path%>/workflow/designer/dtree/dtree_radio.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script language="javascript" src="<%=path%>/common/js/loadmask/jquery.loadmask.js"></script>
		<script type="text/javascript">
			$(document).ready(function(){
				var checkobjs=document.getElementsByName("chkRadio");
				var flag = false;//是否处理办公厅意见征询机制 严建 2012-0619 15:27
				if(checkobjs!=null){
					if(checkobjs.length==1){
						checkobjs[0].checked=true;
						var currentTr=checkobjs[0].value;
						var nodeId = '${nodeId}';
						var transitionId = '${transitionId}';//yanjian 2011-11-08 14:07
						$("#selectTaskActor").val(currentTr);
				  	    var strTaskActors = "";
				  	    strTaskActors=currentTr+"|"+nodeId;
				  	    //parent.document.getElementById("strTaskActors_" + nodeId).value = strTaskActors;
				  	     $("input[id=strTaskActors_"+nodeId+"][transId='"+transitionId+"']", parent.document).val(strTaskActors);//yanjian
				  	    parent.maxActors = 1;
				  	    flag = true;
					}
				}
				
				$("#userFieldset",window.parent.document).unmask();
				if(flag){
					if(typeof(window.parent.parent.isEnableYjzx) != "undefined"){
						if(window.parent.parent.isEnableYjzx()){
							if(typeof(window.parent.parent.doNewYjzx) != "undefined"){
								window.parent.parent.doNewYjzx();
							}
						}
					}
				}
		});
			
			function initChecked(type) {
				var checkobjs=null;
				if(type == "0"){
					checkobjs=document.getElementsByName("chkRadio");
				}else{
					checkobjs=document.getElementsByName("chkRadioG");
				}
				
				var selectTaskActor=$("#selectTaskActor").val();
				for(var i=0;i<checkobjs.length;i++){	
					var currentTr=checkobjs[i].value;
					//alert("currentTr："+currentTr+"&&&&&&&&selectTaskActor:"+selectTaskActor);
					if(currentTr==selectTaskActor){
						checkobjs[i].checked=true;
					}else{
						checkobjs[i].checked=false;
					}				
				}
				
				//$("input:radio[value='"+selectTaskActor+"']").attr("checked",true);
			
			}
			
			function showTree(type){
				if(type == "0"){
					initChecked(type);
					$("#tr2").show();
					$("#tr3").hide();
					$("#other").attr("src","");
					$("#tr4").hide();
				}
				if(type == "1"){
					initChecked(type);
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
			
			function gotoSelectNode(value){
				//alert(value);
				var nodeId = '${nodeId}';
				var transitionId = '${transitionId}'; //yanjian 2011-11-08 14:07
				$("#selectTaskActor").val(value);
		  	    var strTaskActors = "";
		  	    strTaskActors=value+"|"+nodeId;
		  	    //parent.document.getElementById("strTaskActors_" + nodeId).value = strTaskActors;
		  	    $("input[id=strTaskActors_"+nodeId+"][transId='"+transitionId+"']", parent.document).val(strTaskActors); //yanjian 2011-11-08 14:07
		  	    parent.maxActors = 1;
			}
					      
		</script>
<style type="text/css">
body td {
	font-size: 14px;
}
.dtree{
	font-size: 14px;
}
</style>
	</HEAD>
	<body topmargin="0" leftmargin="0" oncontextmenu="return false;" >
		 <s:hidden id="selectTaskActor" name="selectTaskActor"></s:hidden>
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
                          var systemroot='<%=root%>/workflow/designer';
						  var person = new dTree('person');
						  person.add('g0','-1','<label>${nodeName}</label>','','','','<%=root%>/workflow/images/tree/folder_closed.gif','2');
					      <c:forEach items="${userList}" var="user" varStatus="status">
					      	  //alert('u<c:out value="${user[0]}"/>'+" -- "+'<c:out value="${user[1]}"/>' + " === " + '[<c:out value="${tree[2]}"/>]');
					          person.add('u<c:out value="${user[0]}"/>','g0','<input name="chkRadio" type="radio"  style="position:absolute;clip: rect(auto auto auto auto)" onclick="gotoSelectNode(this.value)" value=<c:out value="${user[0]}"/> />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<label for=chkall title="<c:out value="${user[1]}"/><c:out value="${user[3]}"/>"><c:out value="${user[1]}"/> [<c:out value="${user[2]}"/>]</label>','','','','','','<%=root%>/workflow/images/tree/renyuan.gif','2');
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
                          var systemroot='<%=root%>/workflow/designer';
						  var d = new dTree('d');
						  d.add('g0','-1','${nodeName}','','','','<%=root%>/workflow/images/tree/folder_closed.gif','2');
						  <c:set var="topTreeId" value="0"/>
						  <c:forEach items="${orgList}" var="tree" varStatus="status">
						  	//alert('zzb<c:out value="${tree[2]}"/>');
						  <c:choose>
						    <c:when test="${tree[1] == topTreeId}">
						      //d.add('g<c:out value="${tree[0]}"/>','g0','思创数码科技股份有限公司','','','','','','<%=root%>/workflow/images/tree/folder_closed.gif','-2');
						       d.add('g<c:out value="${tree[0]}"/>','g0','<label ><font color="black"><c:out value="${tree[2]}"/></font> </label>','','','','','','<%=root%>/workflow/images/tree/folderopen.gif','2');	
						    </c:when>
						    <c:otherwise>
						      d.add('g<c:out value="${tree[0]}"/>','g<c:out value="${tree[1]}"/>','<label ><font color="black"><c:out value="${tree[2]}"/></font> </label>','','','','','','<%=root%>/workflow/images/tree/folderopen.gif','-2');
						    </c:otherwise>
						  </c:choose>						  
						  </c:forEach>
						  <c:forEach items="${orgList}" var="tree" varStatus="status">
						      <c:forEach items="${tree[3]}" var="user" varStatus="status">
						      	if('<c:out value="${user[0]}"/>' != "") {
						          d.add('u<c:out value="${user[0]}"/>','g<c:out value="${tree[0]}"/>','<input name="chkRadioG" type="radio"  style="position:absolute;clip: rect(auto auto auto auto)" onclick="gotoSelectNode(this.value)" value=<c:out value="${user[0]}"/> />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<label title="<c:out value="${user[1]}"/><c:out value="${user[2]}"/>"><c:out value="${user[1]}"/></label>','','','','','','<%=root%>/workflow/images/tree/renyuan.gif','2');
						      	}
						      </c:forEach>
						  </c:forEach>
						  //alert(d);
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
                          var systemroot='<%=root%>/workflow/designer';
						  var person = new dTree('person');
						  person.add('g0','-1','<label>${nodeName}</label>','','','','<%=root%>/workflow/images/tree/folder_closed.gif','2');
					      <c:forEach items="${userList}" var="user" varStatus="status">
					          person.add('@<c:out value="${user[0]}"/>','g0','<input name="chkRadio" type="radio"  style="position:absolute;clip: rect(auto auto auto auto)" onclick="gotoSelectNode(this.value)" value=<c:out value="${user[0]}"/> />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<label for=chkall title="<c:out value="${user[1]}"/><c:out value="${user[3]}"/>"><c:out value="${user[1]}"/></label>','','','','','','<%=root%>/workflow/images/tree/folder_closed.gif','2');
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
                          var systemroot='<%=root%>/workflow/designer';
						  var d = new dTree('d');
						  d.add('g0','-1','${nodeName}','','','','<%=root%>/workflow/images/tree/folder_closed.gif','2');
						  <c:set var="topTreeId" value="0"/>
						  <c:forEach items="${orgList}" var="tree" varStatus="status">
						  	//alert('zzb<c:out value="${tree[2]}"/>');
						  <c:choose>
						    <c:when test="${tree[1] == topTreeId}">
						      //d.add('g<c:out value="${tree[0]}"/>','g0','思创数码科技股份有限公司','','','','','','<%=root%>/workflow/images/tree/folder_closed.gif','-2');
						       d.add('g<c:out value="${tree[0]}"/>','g0','<label ><font color="black"><c:out value="${tree[2]}"/></font> </label>','','','','','','<%=root%>/workflow/images/tree/folderopen.gif','2');	
						    </c:when>
						    <c:otherwise>
						      d.add('g<c:out value="${tree[0]}"/>','g<c:out value="${tree[1]}"/>','<label ><font color="black"><c:out value="${tree[2]}"/></font> </label>','','','','','','<%=root%>/workflow/images/tree/folderopen.gif','-2');
						    </c:otherwise>
						  </c:choose>						  
						  </c:forEach>
						  <c:forEach items="${orgList}" var="tree" varStatus="status">
						      <c:forEach items="${tree[3]}" var="user" varStatus="status">
						      	if('<c:out value="${user[0]}"/>' != "") {
						          d.add('@<c:out value="${user[0]}"/>','g<c:out value="${tree[0]}"/>','<input name="chkRadioG" type="radio"  style="position:absolute;clip: rect(auto auto auto auto)" onclick="gotoSelectNode(this.value)" value=<c:out value="${user[0]}"/> />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<label title="<c:out value="${user[1]}"/><c:out value="${user[2]}"/>"><c:out value="${user[1]}"/></label>','','','','','','<%=root%>/workflow/images/tree/renyuan.gif','2');
						      	}
						      </c:forEach>
						  </c:forEach>
						  //alert(d);
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
<%--			<select style="display: none ;" id="select_person"></select>--%>
	</body>
</HTML>
