<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/tags/c.tld" prefix="c" %>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>选择单位</TITLE>
		<%@include file="/common/include/meta.jsp" %>
		<link href="<%=root%>/doc/sends/dtree.css" type="text/css" rel="stylesheet">
		<link href="<%=root%>/doc/sends/windows.css" type="text/css" rel="stylesheet">
		<link rel="stylesheet" type="text/css" href="<%=path%>/common/js/autocomplete/css/jquery.autocomplete.css" />
		<script language="javascript" src="<%=root%>/doc/sends/dtree_checkbox.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script type='text/javascript' src='<%=path%>/common/js/autocomplete/js/jquery.autocomplete.min.js'></script>
		<style type="text/css">
		.Operation{
			height:19px;
			line-height:19px;
			text-align:center;
			text-decoration:none;
			display:block;
			FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#fbf9f6,endColorStr=#d5d2ca);
			border:1px solid #848285;
			CURSOR: hand;
		}
		</style>
		
		<script type="text/javascript">
		function gotoSelectCheck(checkFlag,nodeValue){
			if(typeof nodeValue != "undefined" || nodeValue.indexOf("grp_")==0 || nodeValue != ""){
				//将所有与当前选择单位相同的节点都勾选（机构分组中可能有相同的节点）
				if($("input[id=" + nodeValue + "]").attr("disabled")==false){
					$("input[id=" + nodeValue + "]").attr("checked",checkFlag);
					checkParentsOfLeaf(checkFlag,nodeValue);
				}
				//通过树中的DOM结构勾选所有子节点（这里没有用"[id^='" + nodeValue + "']"直接全部获取，是因为”机构分组“中的父节点没有这种筛选规则）
				$("#"+nodeValue).parent().next().children().children("input[name='rightCheckBox'][disabled!='disabled']").each(function(){
					var chk = $("input[name='rightCheckBox'][disabled!='false'][value='"+$(this).val()+"']");
					if(chk.attr("disabled")==false){
						chk.attr("checked",checkFlag);
						checkParentsOfLeaf(checkFlag,$(this).val());
					}
					checkChildren(checkFlag,$(this).val());
					
				});
				
				var sel = $("#selectOrg");
				$("#selectOrg").html("");
				//以下代码作用为：将当前选择的单位添加进已选框
				$("input[name='rightCheckBox']").each(function(){
					//排除分组机构中的父节点
					if($(this).attr("checked")&&$(this).val().indexOf("grp_")<0){
						var val = $(this).val();
						var text = $(this).parent().text();
						var flag = true;
						var myflag = true;
						
						//判断是否有孩子节点
						if($("input[id!='" + val + "'][id^='" + val + "']").length != 0){
							myflag = false;
						}
						
						$("option").each(function(){
							if(val==$(this).val()){
								flag = false;
							}
						});
						if(myflag){
							if(flag){
								sel.append($("<option value="+$(this).val()+">"+$(this).parent().find("span").text()+"</option>"));
							}
						}
					}
				});
			}else{
				addOpt(checkFlag,nodeValue);
			}
		}

		//勾选叶节点的所有父节点
		function checkParentsOfLeaf(checkFlag,nodeValue){
			//排除非叶节点
			if($("input[id!='" + nodeValue + "'][id^='" + nodeValue + "']").size() > 0 ||nodeValue.indexOf("grp_") >= 0)
				return;
			//用'[id=' + nodeValue + ']'而非#nodeValue方式筛选，是因为可能有多个相同ID的节点（比如分组机构中也有）
			$('[id=' + nodeValue + ']').each(function(){
				   
				   var parentInput = $(this).parent().parent().prev().children('input');
				   if(parentInput.size() > 0 && parentInput .attr('id').indexOf("grp_") >= 0)
					   if(checkFlag)
					   		parentInput.attr("checked",checkFlag);
					   else{
						   var sFlag = true;
						   $(this).parent().siblings().children('input').each(function(){
								   if($(this).attr('checked') != checkFlag){
									   sFlag = false;
										return false;
									}
						   });
						   if(sFlag){
							   parentInput.attr("checked",checkFlag);
						   }
					   }
				   if(parentInput.size() > 0 && parentInput .attr('id').indexOf("grp_") < 0)
					   if(!checkFlag)
							unCheckParent(checkFlag,nodeValue);
						else
							checkParent(checkFlag,nodeValue);
			});
		}

		
		function unCheckParent(checkFlag,nodeValue){
			if(typeof nodeValue != "undefined" && nodeValue != "")
				var parentVal = nodeValue.substring(0, nodeValue.length - 3);
			else
				return;
			if(typeof parentVal == "undefined" || parentVal == ""){
				return;
			}
			var pFlag = true;
			$("input[id!='" + parentVal + "'][id^='" + parentVal + "']").each(function(){
				if($(this).attr('checked') != checkFlag){
					pFlag = false;
					return false;
				}
			});
			if(pFlag){
				$("input[id='" + parentVal + "']").attr("checked",checkFlag);
				arguments.callee(checkFlag,$("input[id='" + parentVal + "']").attr('id'));
			}
		}

		function checkParent(checkFlag,nodeValue){
			if(typeof nodeValue != "undefined" && nodeValue != "")
				var parentVal = nodeValue.substring(0, nodeValue.length - 3);
			else
				return;
			if(typeof parentVal == "undefined" || parentVal == ""){
				return;
			}
			
			$("input[id='" + parentVal + "']").attr("checked",checkFlag);
			arguments.callee(checkFlag,$("input[id='" + parentVal + "']").attr('id'));
		}

		function checkChildren(checkFlag,nodeValue){
			if(typeof nodeValue == "undefined" || nodeValue == "")
				return;
			$("input[id!='" + nodeValue + "'][id^='" + nodeValue + "']").each(function(){
				if($(this).attr("disabled")==false){
					$(this).attr("checked",checkFlag);
					checkParentsOfLeaf(checkFlag,$(this).attr('id'));
				}
			});
		}
		function selectTreeNode(checkFlag, nodeValue) {
			alert("selectTreeNode");
		}
		function SelectNodeItem(checkFlag,nodeValue) {
			alert("SelectNodeItem:");
    	}
		
		function setNodeItem(checkFlag, id, name) {
			alert("setNodeItem");
		}
		
		function  addOption(strName, strValue){
			var objSelect = $("#selectOrg")[0]; 
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
			alert("removeOption");
		}
		
		function initChecked() {
			alert("initChecked");
		}
		
		//选择人员到列表
		function addOpt(checkFlag,nodeValue){
			var sel = $("#selectOrg");
			$("#selectOrg").html("");
			if(checkFlag){
				$("input[name='rightCheckBox']").each(function(){
					if($(this).attr("checked")&&$(this).val().indexOf("grp_")<0){
						var val = $(this).val();
						var text = $(this).parent().text();
						var flag = true;
						$("option").each(function(){
							if(val==$(this).val()){
								flag = false;
							}
						});
						if(flag){
							sel.append($("<option value="+$(this).val()+">"+$(this).parent().find("span").text()+"</option>"));
						}
						$("input[name='rightCheckBox'][value='"+$(this).val()+"']").attr("checked",true);
					}
				});
			}else{
				$("input[name='rightCheckBox'][value='"+nodeValue+"']").attr("checked",false);
				$("input[name='rightCheckBox']").each(function(){
					if($(this).attr("checked")&&$(this).val().indexOf("grp_")<0){
						var val = $(this).val();
						var text = $(this).parent().text();
						var flag = true;
						$("option").each(function(){
							if(val==$(this).val()){
								flag = false;
							}
						});
						if(flag){
							sel.append($("<option value="+$(this).val()+">"+$(this).parent().find("span").text()+"</option>"));
						}
					}
				});
			}
			
		}
		
		
	      //搜索确定按钮
         function gotoSearch(){  
                var orgSyscode=document.getElementById("search23").value; 
                 //alert(orgSyscode);
                 if($("#"+orgSyscode).attr("checked")){
                    alert("该部门已被选中!");
                 }else{
                  $("#"+orgSyscode).click();
                 }                  	 
           }		
		
			
		//双击已经选中的人员,删除人员 
		function dblclickRemove(sel){
			if(sel.selectedIndex!=-1){
				var opt = sel.options[sel.selectedIndex];
				sel.removeChild(opt);
				$("input[name='rightCheckBox'][value='"+opt.value+"']").attr("checked",false);
			}
		}
		
		//切换机构树
		function switchTree(treeTd){
			if("orgtree"==treeTd){
				$("#orgtree").show();
				$("#switchOrgTree").html("<b>所有机构</b>");
				$("#grouptree").hide();
				$("#switchGroupTree").html("机构分组");
			}else{
				$("#orgtree").hide();
				$("#switchOrgTree").html("所有机构");
				$("#grouptree").show();
				$("#switchGroupTree").html("<b>机构分组</b>");
			}
		}
		
		
		
		$(document).ready(function(){
		
		//搜索--自动完成
	var search = $("#search2");
	if(search && undefined!=search.val()){
		search.autocomplete(search.attr("url"), {
				width: 200,
				max:10,
				selectFirst: true,
				scroll:true,
				formatResult: function(data, value) {
				}}).result(function(event, data, formatted) { //回调
				$("#search23").val("");
				var  data1=data+"";
				var das=data1.split(",");
				var orgName=das[0];
				var orgSyscode=das[1];
				$("#search23").val(orgSyscode+"");
				$("#search2").val(orgName+"");
				
			});
	}
		
		
			var params = window.dialogArguments;
			if(params != null){
				if(params.length > 1){
					var ids   = params[0];
					var names = params[1];
					var noids = params[2];
					if(ids != ""){
						var id = ids.split(",");
						var name = names.split(",");
						for(var i=0;i<id.length;i++){
						   $("input[name='rightCheckBox'][value='"+id[i]+"']").attr("checked",true);
							addOption(name[i],id[i]);
						}
					}
					if(noids != ""){
						var noid = noids.split(",");
						for(var i=0;i<noid.length;i++){
							$("input[name='rightCheckBox'][value='"+noid[i]+"']").attr("disabled","disabled");
						}
					}
				}
			}
		});

		$(function(){
			$('.Operation').css('backgroundColor','white');
		});
		
		//确定
		function doSubmit(){
			var orgIds = "";
			var orgNames = "";
			var info = new Array();
			$("#selectOrg > option").each(function(){
				orgIds += $(this).val()+",";
				orgNames += $(this).text()+",";
			});
			
			orgIds = orgIds.substring(0,orgIds.length - 1);
			orgNames = orgNames.substring(0,orgNames.length - 1);
			
			info[0] = orgIds;
			info[1] = orgNames;
			window.returnValue = info;
			window.close();
		}
		
		</script>
	</HEAD>
	<base target="_self"/>
	<body class=contentbodymargin oncontextmenu="return false;"  topmargin="0" leftmargin="0">
			<table border="0" cellpadding="2" cellspacing="0" width="100%">
				<tr>
					<td height="30"
									style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
									<table height="100%" width="100%" border="0" cellspacing="0" cellpadding="0">
										<tr>
								            <td width="5%" align="center"><img src="<%=frameroot%>/images/ico.gif" width="9" height="9"></td>
								            <td width="20%">选择单位</td>
								            <td>&nbsp;</td>
								            <td width="75%">
											</td>
										</tr>
									</table>
								</td>
				</tr>
			</table>
			
			<select style="display: none ;" id="select_person"></select>
			
			<table width="100%" height="391" border="0" cellpadding="0" cellspacing="0" bordercolor="#FFFFFF">
				<tr height="80%" >
					<td width="100%" height="307">
						<table width="100%" height="386" border="1" cellpadding="0" cellspacing="0" bordercolor="#FFFFFF">
							<tr>
							<td width="42%" height="350" valign="top" style="border-top-color: gray;background-color: white;OVERFLOW: auto;">
				<table width="100%" height="30" border="1" cellpadding="0" cellspacing="0" bordercolor="#FFFFFF">
				<tr>
				<td width="50%" id="switchOrgTree" align="center" class="Operation" onclick="switchTree('orgtree')" ><b>所有机构</b>
				</td>
				<td width="50%" id="switchGroupTree" align="center" class="Operation" onclick="switchTree('grouptree')" >机构分组
				</td>
				</tr>
				</table>
				<table width="100%" height="360" border="1" cellpadding="0" cellspacing="0" bordercolor="#FFFFFF">
				<tr>
				<td id="orgtree" width="100%" >
								<div id=contentborder class="dtree" style="top:20px; padding:20px;background-color: white;OVERFLOW: auto;">
									<script language="javascript" type="text/javascript">
		                          var imageRootPath='<%=root%>/workflow';
								  var dd = new dTree('dd');
						//所有机构  
								  dd.add('0','-1','所有机构','','','','<%=root%>/workflow/images/tree/folder_closed.gif','2');
								 <c:forEach items="${orgList}" var="tree" varStatus="status">
								 //	if("${tree.orgSyscode}".length==3){
								 	var suporgcode = "${tree.supOrgCode}";
								 	//if("${tree.orgSyscode}" == suporgcode){
								 	if("${tree.orgSyscode}".length==3){
								 	  dd.add('${tree.orgSyscode}','0','${tree.orgName}','','','','<%=root%>/workflow/images/tree/folder_closed.gif','-2');
								 	}else{
								 	   //这个是之前的temp
								 	  //var temp = "${tree.supOrgCode}".substring(0,3);
								 	  var temp = '${tree.orgSyscode}'.substring(0,'${tree.orgSyscode}'.length-3);
									  dd.add('${tree.orgSyscode}',temp,'${tree.orgName}','','','','<%=root%>/workflow/images/tree/folder_closed.gif','-2');
								 	}
								 </c:forEach>
								  document.write(dd);
								</script>
								</div>
				</td>
				<td  id="grouptree" width="100%" style="display: none">
								<div id=contentborder class="dtree" style="top:20px; padding:20px;background-color: white;OVERFLOW: auto;">
									<script language="javascript" type="text/javascript">
		                          var imageRootPath='<%=root%>/workflow';
		                          var valueArray = new Array();
		                          
								  var d = new dTree('d');
						//机构分组  
								  d.add('0','-1','机构分组','','','','<%=root%>/workflow/images/tree/folder_closed.gif','2');
								  <c:forEach items="${orgGroupList}" var="orgGroup" varStatus="status">
								  	
								  	if(valueArray['<c:out value="${orgGroup[0]}"/>'] != "true"){
								  		valueArray['<c:out value="${orgGroup[0]}"/>'] = "true";
								  		d.add('grp_${orgGroup[0]}','0','${orgGroup[1]}','','','','<%=root%>/workflow/images/tree/folder_closed.gif','-2');
								  	}
								  	d.add('<c:out value="${orgGroup[2]}"/>','grp_<c:out value="${orgGroup[0]}"/>','<c:out value="${orgGroup[3]}"/>','','','', '<%=root%>/workflow/images/tree/folder_closed.gif','-2');
								  	
								 </c:forEach>
								  document.write(d);
								</script>
								</div>
				</td>
				</tr>
				</table>
								
					  	    </td>
					  	    <td width="9%" style="border-top-color: #FFFFFF;">
								<DIV id=contentborder style="background-color: white;" cellpadding="0">
						  		</DIV>	
							  </td>
							  <td width="37%" style="border-top-color: #FFFFFF;">
								<DIV id=contentborder style="background-color: white;" cellpadding="0">
									<table width="100%" height="360" border="0" cellpadding="0" cellspacing="0" bordercolor="#FFFFFF">
										<tr><td >
										<p>&nbsp;&nbsp;选择单位： 
										<br>
									    <input id="search23" maxlength="32"  class="search" title="输入分发单位" size="30" value=" " type="hidden"/>
										
										<input id="search2" maxlength="32" url="<%=root%>/sends/docSend!autoComplete.action" name="searchOrgName" class="search" title="输入分发单位" size="30" value="" onchange="search23.value=''" />
										&nbsp;<input id="btnok" name="btnok" type="button" onclick="javascript:gotoSearch();" class="input_bg" value="确定">
										<br>
										  <select id="selectOrg" ondblclick="dblclickRemove(this);" title="双击单位名移去"
										  		multiple="multiple" size="25" style="width:254px;">
									      </select>
									      </p></td>
										</tr>
									</table>
								</DIV>	
							  </td>
							</tr>
					  </table>
				  </td>
				</tr>
				<tr>
					<td align="center"><br>
						<input type="button" id="btnOK"  class="input_bg" onclick="doSubmit();"  value="确定"/>&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="button" id="btnNO"  class="input_bg" onclick="window.close();"  value="关闭"/>
					</td>
				</tr>
			</table>
	</body>
</HTML>
