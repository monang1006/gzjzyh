<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/tags/c.tld" prefix="c" %>
<%@include file="/common/include/rootPath.jsp"%>
<% 
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragrma","no-cache");
	response.setDateHeader("Expires",0);
%>
<HTML>
	<HEAD>
		<TITLE>选择分发机构</TITLE>
		<%@include file="/common/include/meta.jsp" %>
		<link href="<%=root%>/doc/sends/dtree.css" type="text/css" rel="stylesheet">
		<link href="<%=root%>/doc/sends/windows.css" type="text/css" rel="stylesheet">
		<LINK href="<%=frameroot%>/css/properties_windows_add.css" type=text/css rel=stylesheet>
		<script language="javascript" src="<%=root%>/doc/sends/dtree_checkbox.js" type="text/javascript"></script>
		<link rel="stylesheet" type="text/css" href="<%=path%>/common/js/autocomplete/css/jquery.autocomplete.css" />
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script type='text/javascript' src='<%=path%>/common/js/autocomplete/js/jquery.autocomplete.min.js'></script>
		<script src="<%=path%>/common/js/common/map.js" type="text/javascript"></script>
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
		var orgMap = new Map();
		var orgNameMap = new Map();
		
			/**
			* 调用 autoComplete时不需要显示第2项,
			*截取第一项
			*/
		function cutFirstItem(con){
			con=con.substring(0,con.indexOf(","));
			return con;
		}
 			
		function gotoSelectCheck(checkFlag,nodeValue){
			//alert($("input[id!='001999014'][id^='001999014']").length == 0);
			//alert("gotoSelectCheck:"+"checkFlag("+checkFlag+")"+"nodeValue("+nodeValue+")");
			if(nodeValue.indexOf("grp_")==0 || nodeValue != ""){
				//alert($("#"+nodeValue).parent().next().children("[type=checkbox ]").length);
				//全选功能
				$("#"+nodeValue).parent().next().children().find("input[name='rightCheckBox'][disabled!='disabled']").each(function(){
					var chk = $("input[name='rightCheckBox'][disabled!='false'][value='"+$(this).val()+"']");
					if(chk.attr("disabled")==false){
						chk.attr("checked",checkFlag);
					}
				});
				var sel = $("#selectOrg");
				$("#selectOrg").html("");
				$("input[name='rightCheckBox']").each(function(){
					if($(this).attr("checked")&&$(this).val().indexOf("grp_")<0){
						var val = $(this).val();
						var text = $(this).parent().text();
						//alert("text="+text);
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
		function selectTreeNode(checkFlag, nodeValue) {
			alert("selectTreeNode");
		}
		function SelectNodeItem(checkFlag,nodeValue) {
			alert("SelectNodeItem:");
    	}
		
		function setNodeItem(checkFlag, id, name) {
			alert("setNodeItem");
		}
		
		function  addOption(alias, strName, strValue){
			alert("addOption");
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
			
		//双击已经选中的人员,删除人员 
		function dblclickRemove(sel){
			if(sel.selectedIndex!=-1){
				var opt = sel.options[sel.selectedIndex];
				sel.removeChild(opt);
				$("input[name='rightCheckBox'][value='"+opt.value+"']").attr("checked",false);
			}
		}
		
		//把不是叶子节点的右边删除，而左边可以全选
		function selRemove(sel){
			if(sel.selectedIndex!=-1){
				var opt = sel.options[sel.selectedIndex];
				sel.removeChild(opt);
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
		
          //搜索确定按钮
         function gotoSearch(){  
                var orgSyscode=document.getElementById("search23").value; 
                 if($("#"+orgSyscode).attr("checked")){
                    alert("该部门已被选中!");
                 }else{
                  $("#"+orgSyscode).click();
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
		
			if("${flaginfo}"!=""){
				//alert("${flaginfo}");
				window.close();
			}
		
			//发送公文
			var defPrintNum= /^((1|2|3|4|5|6|7|8|9)\d{0,3})$/;
			$("#btnOK").click(function(){
				var docId  = "${docId}";
				var defPrint = $.trim($("#defPrint").val());
				$("#defPrint").val(defPrint);
				
				//alert("我是defPrint：" + defPrint);
				
				var orgIds = "";
				var orgNames = "";
				$("#selectOrg > option").each(function(){
					orgIds += $(this).val() + ";";
					orgNames += $(this).html() + ";";
					//orgIds += $(this).val()+","+$(this).html()+";";
					//alert("我是val" + $(this).val());
					//alert("我是html" + $(this).html());
				});
				if(""==orgIds){
					alert("请选择要分发的单位！");
					return false;
				}
				//alert(orgIds);
				orgIds = orgIds.substring(0,orgIds.length-1);
				orgNames = orgNames.substring(0,orgNames.length-1);
				
				/*
				*	modify by luosy 2013-06-13
					替换分组单位
				*/
				var orgN = orgNames;
				for (var i = 0; i < orgMap.arr.length; i++) {
			 		orgN = orgN.replace(orgMap.arr[i].value,orgMap.arr[i].key);
			 	}
			 	for (var i = 0; i < orgNameMap.arr.length; i++) {
			 		orgN = orgN.replace(orgNameMap.arr[i].key,orgNameMap.arr[i].value);
			 	}
				window.returnValue=orgIds+":"+orgNames+":"+orgN;
				window.close();
			});
			
			//关闭窗口
			$("#btnNO").click(function(){
				window.returnValue="";
				window.close();
			});
			
			//初始化数据
			var args = window.dialogArguments;
			
			var orgIds = args[0];	//默认选择的单位
			var orgIdss = args[1];	//默认不可以选择的单位
			//alert("orgIds:"+orgIds+"\norgIdss:"+orgIdss);
			
			
		  	if(orgIds!=""&orgIds!=null&orgIds!=":"){
		  		var orgs = orgIds.split(":");
				var orgId = orgs[0].split(";");
				var orgName = orgs[1].split(";");
				var orgHtml = "";
		  		for(var i = 0;i<orgId.length;i++){
	  				orgHtml= orgHtml + "<option value="+orgId[i]+">"+orgName[i]+"</option>";
		  		}
				$("#selectOrg").html(orgHtml);
				$("#selectOrg > option").each(function(){
					$("input[name='rightCheckBox'][value='"+$(this).val()+"']").attr("checked",true);
				});
			}


			if(orgIdss!=""&orgIdss!=null&orgIdss!=":"){
		  		var orgs2 = orgIdss.split(":");
				var orgId2 = orgs2[0].split(";");
				var orgName2 = orgs2[1].split(";");
				for(var i = 0;i<orgId2.length;i++){
					$("input[name='rightCheckBox'][value='"+orgId2[i]+"']").attr("disabled","disabled");
				}
			}
			if("${selectedOrg}"!=""){
				$("#selectedOrg").html("${selectedOrg}");
				$("#selectedOrg > option").each(function(){
				//alert("option:"+$(this).val());
					$("input[name='rightCheckBox'][value='"+$(this).val()+"']").attr("disabled","disabled");
				});
			}
		});
		
		function OpenWindow(Url, Width, Height, WindowObj) {
			var ReturnStr = showModalDialog(Url,
											WindowObj,
											"dialogWidth:" + Width + "pt;dialogHeight:" + Height + "pt;"+
											"status:no;help:no;scroll:no;");
			return ReturnStr;
		}
		</script>
	</HEAD>
	<base target="_self"/>
	<body class=contentbodymargin oncontextmenu="return false;"  topmargin="0" leftmargin="0">
			<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td colspan="3" class="table_headtd">
						<table height="100%" width="100%" border="0" cellspacing="0" cellpadding="0">
										<tr>
											<td class="table_headtd_img" >
												<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
											</td>
											<td align="left">
												选择分发机构${selectedOrg}
											</td>
											<td align="right">
												<table border="0" align="right" cellpadding="00" cellspacing="0">
									                <tr>
									                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
									                 	<td class="Operation_input" onclick="$('#btnOK').click();">&nbsp;确&nbsp;定&nbsp;</td>
									                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
								                  		<td width="5"></td>
									                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
									                 	<td class="Operation_input1" onclick="$('#btnNO').click();">&nbsp;关&nbsp;闭&nbsp;</td>
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
			
			<select style="display: none ;" id="select_person"></select>
			
			<table width="100%" height="391" border="0" cellpadding="0" cellspacing="0" bordercolor="#FFFFFF">
				<tr height="100%" >
					<td width="100%" height="390">
						<table width="100%" height="422" border="1" cellpadding="0" cellspacing="0" bordercolor="#FFFFFF">
							<tr>
							<td width="42%" height="390" valign="top" style="border-top-color: gray;background-color: white;OVERFLOW: auto;">
				<table width="100%" height="30" border="1" cellpadding="0" cellspacing="0" bordercolor="#FFFFFF">
				<tr>
				<td width="100%" id="switchOrgTree" align="center" class="Operation" onclick="switchTree('orgtree')" ><b>所有机构</b>
				</td>
				 
				<!-- <td width="50%" id="switchGroupTree" align="center" class="Operation" onclick="switchTree('grouptree')" >机构分组
				</td> -->
				
				</tr>
				</table>
				<table width="100%" height="390" border="1" cellpadding="0" cellspacing="0" bordercolor="#FFFFFF">
				<tr>
				<td id="orgtree" width="100%" >
								<div id=contentborder class="dtree" style="top:76px; padding:20px;background-color: white;OVERFLOW: auto;">
								<script language="javascript" type="text/javascript">
		                          var imageRootPath='<%=root%>/workflow';
								  var dd = new dTree('dd');
						//所有机构  
								  dd.add('0','-1','所有机构','','','','<%=root%>/workflow/images/tree/folder_closed.gif','2');
								  
								 <c:forEach items="${orgList}" var="tree" varStatus="status">
								 	if("${tree.orgSyscode}".length==3){
								 	  dd.add('${tree.orgSyscode}','0','${tree.orgName}','','','','<%=root%>/workflow/images/tree/folder_closed.gif','-2');
								 	}else{
								 	  var temp = "${tree.orgSyscode}".substring(0,"${tree.orgSyscode}".length-3);
									  dd.add('${tree.orgSyscode}',temp,'${tree.orgName}','','','','<%=root%>/workflow/images/tree/folder_closed.gif','-2');

									 	if(orgMap.get(temp)==null){
									 		orgMap.put(temp,"${tree.orgName}");
									 	}else{
									 		orgMap.put(temp,orgMap.get(temp)+";"+"${tree.orgName}");
									 	}
								 	}	
								 	if("${tree.orgSyscode}".length>6){
									 	orgNameMap.put("${tree.orgSyscode}","${tree.orgName}");
								 	}
								 </c:forEach>
								  document.write(dd);
								</script>
								</div>
				</td>
			
				<!--  <td  id="grouptree" width="100%" style="display: none">
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
               -->
				</tr>
				</table>
								
					  	    </td>
					  	    <td width="2%" style="border-top-color: #FFFFFF;">
								<DIV id=contentborder style="background-color: white;" cellpadding="0">
						  		</DIV>	
							  </td>
							  <td width="37%" style="border-top-color: #FFFFFF;">
								<table width="100%" height="391" border="0" cellpadding="0" cellspacing="0" bordercolor="#FFFFFF">
								<tr height="100%" >
									<td width="100%" height="390" style="background-color: white;OVERFLOW: auto;">
												<table width="100%" height="30" border="1" cellpadding="0" cellspacing="0" bordercolor="#FFFFFF">
													<tr>
														<td width="100%" id="ffdw" align="center" class="Operation" ><b>新分发单位： </b>
														</td>
													</tr>
												</table>
												<table width="100%" height="386" border="0" cellpadding="0" cellspacing="0" bordercolor="#FFFFFF">
													<tr>
														<td >
														<input id="search23" maxlength="32"  class="search" title="输入分发单位" size="30" value=" " type="hidden"/>
														<input id="search2" maxlength="32" url="<%=root%>/sends/docSend!autoComplete.action" name="searchOrgName" class="search" title="输入分发单位" style="width: 220px;" value="" onchange="search23.value=''" />
														<a id="btnok" href="#" class="button" onclick="gotoSearch();" style="margin-bottom: 4px;">确定</a>
														<br/>
														<select id="selectOrg" ondblclick="dblclickRemove(this);" title="双击单位名移去"
														  		multiple="multiple" size="21" style="width:301px;height:346px;">
													      </select>
													      </td>
													      </tr>
												</table>
								</td>
							 <%-- </td>
								 <td width="9%" style="border-top-color: #FFFFFF;">
									<DIV id=contentborder style="background-color: white;" cellpadding="0">
							  </DIV>	
							  </td>
							 <td width="27%" style="border-top-color: #FFFFFF;">
								<DIV id=contentborder style="background-color: white;" cellpadding="0">
									<table width="100%" height="360" border="0" cellpadding="0" cellspacing="0" bordercolor="#FFFFFF">
										<tr>
										  <td >
											<p>&nbsp;&nbsp;已分发单位：<br>
											  <select id="selectedOrg" multiple="multiple" size="25" style="width:254px;" disabled="disabled">
									      		</select>
									      	</p>
										  </td>
										</tr>
									</table>
								</DIV>	
							  </td>--%>
							</tr>
					  </table>
				  </td>
				</tr>
						<input type="hidden" id="btnOK"  class="input_bg"  value="确定"/>
						<input type="hidden" id="btnNO"  class="input_bg"  value="关闭"/>
			</table>
	</body>
</HTML>

