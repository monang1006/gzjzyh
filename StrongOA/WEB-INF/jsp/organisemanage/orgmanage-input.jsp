<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@	include file="/common/include/rootPath.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<title>增加机构</title>
		<%@include file="/common/include/meta.jsp"%>
		<LINK type=text/css rel=stylesheet href="<%=frameroot%>/css/properties_windows_add.css">
		<script type="text/javascript" language="javascript" src="<%=jsroot%>/jquery/jquery-1.2.6.js"></script>
     	<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<script language="javascript">
		var numtest = /^\d+$/; 
 $(document).ready(function(){
	 //第一次打开机构管理页面光标定位不到输入框内
	 document.getElementById("orgSyscode").focus();
	 
	 
	 var message = $(".actionMessage").text();
	 if(message.indexOf("reload")>-1){
		parent.parent.organiseTree.location = "<%=path%>/organisemanage/orgmanage!tree.action";
		alert("保存成功。")
	 }
 })
	function testMessage(){
	   if(document.getElementById("orgSyscode").value == ""||document.getElementById("orgSyscode").value==null){
			alert('组织机构编号不能为空。');
        	document.getElementById("orgSyscode").focus();
        	return;    
	   }
	   else
	   { 
	   	if(!numtest.test(document.getElementById("orgSyscode").value)){
	        	alert('组织机构编号必须为数字。');
	        	document.getElementById("orgSyscode").focus();
	        	return;
	    }
	   	else if(document.getElementById("iscode").value == ""||document.getElementById("iscode").value==null){
	       	 comparecode(document.getElementById('orgSyscode'),'1');	
	    }
	    else{
	       var flag=document.getElementById("iscode").value;
	       var orgId = document.getElementById("orgId").value;
	       if(flag==document.getElementById("orgSyscode").value && orgId != "" && orgId != null){
	       	alert("合法编码。");
           	document.getElementById("orgSyscode").focus();
	       }
	       else{
	         comparecode(document.getElementById('orgSyscode'),'1');
	       }  	
	    }
	  }
	}
	function testcode(){
	   //alert($.trim($("#orgSyscode").val()).substring(0,3));
	   if("${model.orgIsdel}" == "1"){
			alert("组织机构[${model.orgName}]已删除，不允许进行编辑保存操作。");
			return;
	   }
	   if($.trim($("#orgSyscode").val()).substring(0,3)!="001"){
	      alert("组织机构编号必须以001开头。");
	      document.getElementById("orgSyscode").focus();
	        return;
	   }
	   if(document.getElementById("orgSyscode").value == ""||document.getElementById("orgSyscode").value==null){
			alert('组织机构编号不能为空。');
	        document.getElementById("orgSyscode").focus();
	        return;
	    }
	    if(!numtest.test(document.getElementById("orgSyscode").value)){
	        alert('组织机构编号必须为数字。');
	        document.getElementById("orgSyscode").focus();
	        return;
	    }
		if(document.getElementById("iscode").value == ""||document.getElementById("iscode").value==null){
	       	 comparecode(document.getElementById('orgSyscode'),'0');
	       }
	     else{
	       var flag=document.getElementById("iscode").value;
	       var orgId = document.getElementById("orgId").value;
	       if(flag==document.getElementById("orgSyscode").value && orgId != "" && orgId != null){
	           onsubmit(); 
	       }
	       else{
	         comparecode(document.getElementById('orgSyscode'),'0');
	       }
	        
	        }
	}
	function comparecode(obj,flag){	
			
				 $.ajax({
					url:obj.url,
					type:"post",
					data:{orgcode:obj.value, orgId:document.getElementById("orgId").value},
					success:function(message){
					
						if(message=="000"){
							alert("编码不唯一。");
							obj.focus();		
						}else if(message=="333"){
							alert("编码不符合规则。");
							obj.focus();		
						}else if(message == "222"){
							alert("该组织机构编码的父级不存在。");
							obj.focus();
						}else if(message == "444"){
							alert("该编码的父级机构是部门，不允许添加。");
							obj.focus();
						}
						else{
							if($("#iscode").val() != null && $("#iscode").val() != "" &&
								$("#orgId").val() != null && $("#orgId").val() != "" && $("#iscode").val() != $("#orgSyscode").val()){
								$("#needChildren").val("1");
							}
							if(flag=='0')
							onsubmit();
							else if(message=="111")
							alert("合法编码。");
						}
						
					},
					error:function(message){
						alert("异步出错。");
					}
					
				});
				
			}
	
		function areatree(){
			parent.parent.organiseTree.location = "areaTree.jsp";
			document.getElementById("gogo").innerHTML = '<a href="javascript:tree()">按级别划分</a>';
		}
		
		function tree(){
			parent.parent.organiseTree.location = "clearanceEdit2.jsp";
			document.getElementById("gogo").innerHTML = '<a href="javascript:areatree();">按区域划分</a>';
			
		}
		function coalition(){
			window.showModalDialog("coalition.jsp",window,'help:no;status:no;scroll:no;dialogWidth:500px; dialogHeight:400px');
		}
		
		function move(){
			window.showModalDialog("move.jsp",window,'help:no;status:no;scroll:no;dialogWidth:500px; dialogHeight:400px');
		}
		
		function split(){
			window.showModalDialog("split.jsp",window,'help:no;status:no;scroll:no;dialogWidth:700px; dialogHeight:500px');
		}
		
		function onsubmit(){
		
			 var isOrg=document.getElementById("isOrg").value;
	        if(isOrg==""){
	        	alert('请选择机构类型。');
	        	document.getElementById("isOrg").focus();
	        	return;
	        }
	   		
	   	    var filter=/^(([0\+]\d{2,3}-)?(0\d{2,3})-)?(\d{7,8})(-(\d{3,}))?$/;   
	   		  if(document.getElementById("orgSyscode").value == ""){
	        	alert('组织机构编号不能为空。');
	        	document.getElementById("orgSyscode").focus();
	        	return;
	        }
	           if(!numtest.test(document.getElementById("orgSyscode").value)){
	        	alert('组织机构编号必须为数字。');
	        	document.getElementById("orgSyscode").focus();
	        	return;
	        }
	        if(document.getElementById("orgName").value == ""){
	       		alert('组织机构名称不能为空。');
	       		document.getElementById("orgName").focus();
	  			return;
	        }
	        if(document.getElementById("orgName").value.length > 40){
	        	alert('组织机构名称最大长度为40字。');
	        	document.getElementById("orgName").focus();
	        	return;
	        }
	         if(document.getElementById("orgCode").value == ""){
	        	alert('组织机构编码不能为空。');
	        	document.getElementById("orgCode").focus();
	        	return;
	        }
	        if(document.getElementById("orgCode").value.length > 10){
	        	alert('组织机构编码过长。');
	        	document.getElementById("orgCode").focus();
	        	return;
	        }
	      // if(document.getElementById("orgCode").value.length < 1){
	        //	alert('组织机构编码不正确。');
	       // 	document.getElementById("orgCode").focus();
	        //	return;
	        //}
	      if(document.getElementById("orgTel").value!="" && !filter.test(document.getElementById("orgTel").value)){
	      	alert('电话号码格式不正确。');
	        	document.getElementById("orgTel").focus();
	        	return;
	      } 
	        if(document.getElementById("orgTel").value.length > 20){
	        	alert('电话号码过长。');
	        	document.getElementById("orgTel").focus();
	        	return;
	        }
	        
	        if(document.getElementById("orgFax").value!="" && !filter.test(document.getElementById("orgFax").value)){
	        	alert('传真号码格式不正确。');
	        	document.getElementById("orgFax").focus();
	        	return;
	        }
	        if(document.getElementById("orgFax").value.length > 20){
	        	alert('传真号码过长。');
	        	document.getElementById("orgFax").focus();
	        	return;
	        }
	  if(document.getElementById("orgSequence").value==""){
    	alert("排序序号不能为空，请输入。");
    	document.getElementById("orgSequence").focus();
    	return false;
       }
       
        if(document.getElementById("orgSequence").value != ""){
	        	if(!numtest.test(document.getElementById("orgSequence").value)){
		        	alert('排序必须为数字。');
		        	document.getElementById("orgSequence").focus();
		        	return;
	        	}
	         if(document.getElementById("orgSequence").value.length>10){
	   	       alert('排序序号不能超过10位数。');
		      document.getElementById("orgSequence").focus();
		        return;
	           }
	        }
	        if(document.getElementById("orgAddr").value.length > 32){
	        	alert('地址过长。');
	        	document.getElementById("orgAddr").focus();
	        	return;
	        }
	        if(document.getElementById("orgDescription").value.length > 200){
	        	alert('机构描述不能超过200字。');
	        	document.getElementById("orgDescription").focus();
	        	return;
	        }
	       
	        
	        //验证父级元素是否是被删除状态
	        $.ajax({
					url:"<%=path%>/organisemanage/orgmanage!checkFatherIsdel.action",
					type:"post",
					async:false,
					data:{orgSysCode:document.getElementById("orgSyscode").value},
					success:function(message){
						var orgId = document.getElementById("orgId").value;
						if(message=="true" && (orgId == null || orgId == "")){
							alert("该组织机构的上级机构已被删除，无法添加。");
							return false;
						}else{
							document.forms("mytable").submit();
							//parent.parent.organiseTree.location = "<%=path%>/organisemanage/orgmanage!tree.action";
							//parent.parent.organiseTree.location = "<%=path%>/organisemanage/orgmanage!tree.action";
						}
					},
					error:function(message){
						alert("异步错误。");
					}
				});
		}
		
		function del(){
			
			if("001"=="${model.orgSyscode}"){
				alert("不能删除顶级机构。");
				return;
			}
		
			if("${model.orgIsdel}" == "1"){
				alert("组织机构[${model.orgName}]已删除，不允许进行删除操作。");
				return;
			}
			var id = document.getElementById("orgId").value;

			if(id==null||id==""){
				alert("请选择机构。");
				return;
			}
			if(confirm("该操作将导致该组织机构下子级组织机构和用户也被删除,确定删除组织机构吗?")) 
			{ 
				location = "<%=path%>/organisemanage/orgmanage!delete.action?orgId="+id;
			} 
		}
		
		function reductOrg(){
			if("${model.orgIsdel}" != "1"){
				if("${model.orgName}" == ""){
					alert("请选择机构。");
				}else{
					alert("组织机构[${model.orgName}]未被删除，不允许进行还原操作。");
				}
				return;
			}
			var id = document.getElementById("orgId").value;
			if(id==null||id==""){
				alert("请选择组织机构。");
				return;
			}
		    if(confirm("是否将该组织机构下的下级机构和所有用户一起还原？")){
		    	location ="<%=root%>/organisemanage/orgmanage!reduction.action?orgId="+id+"&userTogether=1";
		    }else{		
				location ="<%=root%>/organisemanage/orgmanage!reduction.action?orgId="+id+"&userTogether=0";
			}
		}
		
		function addOrg(){
			if("${model.orgIsdel}" == "1"){
				alert("组织机构[${model.orgName}]已删除，不允许进行添加操作。");
				return;
			}
			if("${model.isOrg}" == "0"){
				alert("部门组织机构下不能创建组织机构。");
				return;
			}
			var code =document.getElementById("iscode").value;
			var orgId = document.getElementById("orgId").value;
			location ="<%=root%>/organisemanage/orgmanage!addchild.action?orgSysCode="+code+"&orgId="+orgId;
		}
		
		function selectArea(){
			var retValue = window.showModalDialog("<%=path%>/organisemanage/orgmanage!selectArea.action",window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:500px');
		}
		
		function setAreaCodeId(code,name,id){
			document.getElementById("orgAreaName").value = name;
			document.getElementById("orgAreacode").value = code;
		}
		
		//设置部门负责人,可选择多个
		function selectUser(){
			var url = "<%=path%>/organisemanage/orgmanage!chooseOrgManager.action";
			var id = document.getElementById("orgManager").value;
			if(id != ""){
				url += "?id=" + id;
			}
			var ret = window.showModalDialog(url,window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:500px');
			if(ret){
				if(ret[0].split(",").length >= 100){
					alert("部门负责人不能超过100个。");
					return ;
				}
				setUserIdName(ret[0],ret[1]); 
			}
		}
		
		function setUserIdName(id,name){
			document.getElementById("orgManager").value = id;
			document.getElementById("username").value = name;
		}
		
		//设置部门分管领导,可选择多个
		function selectRest2(){
			var url = "<%=path%>/organisemanage/orgmanage!chooseOrgManager.action";
			var id = document.getElementById("rest2id").value;
			if(id != ""){
				url += "?id=" + id;
			}
			var ret = window.showModalDialog(url,window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:500px');
			if(ret){
				/*
				if(ret[0].split(",").length > 1){
					alert("部门分管领导至多只能设置1人");
					return ;
				}
				*/
				setRest2IdName(ret[0],ret[1]); 
			}
		}
		
		
		//设置编辑责任人,可选择多个
		function selecteditor(){
			var url = "<%=path%>/organisemanage/orgmanage!chooseOrgManager.action";
			var id = document.getElementById("editorid").value;
			if(id != ""){
				url += "?id=" + id;
			}
			var ret = window.showModalDialog(url,window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:500px');
			if(ret){
				/*
				if(ret[0].split(",").length > 1){
					alert("部门分管领导至多只能设置1人");
					return ;
				}
				*/
				setEditorIdName(ret[0],ret[1]); 
			}
		}
		
		function setEditorIdName(id,name){
			document.getElementById("editorid").value = id;
			document.getElementById("editor").value = name;
		}
		
		//设置签发领导,可选择多个
		function selecteIssuePeople(){
			var url = "<%=path%>/organisemanage/orgmanage!chooseOrgManager.action";
			var id = document.getElementById("IssuePeopleid").value;
			if(id != ""){
				url += "?id=" + id;
			}
			var ret = window.showModalDialog(url,window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:500px');
			if(ret){
				/*
				if(ret[0].split(",").length > 1){
					alert("部门分管领导至多只能设置1人");
					return ;
				}
				*/
				setIssuePeopleIdName(ret[0],ret[1]); 
			}
		}
		
		function setIssuePeopleIdName(id,name){
			document.getElementById("IssuePeopleid").value = id;
			document.getElementById("IssuePeople").value = name;
		}
		
		
		
		
		//设置部门分管厅领导,可选择多个
		function selectRest4(){
			var url = "<%=path%>/organisemanage/orgmanage!chooseOrgManager.action";
			var id = document.getElementById("rest4id").value;
			if(id != ""){
				url += "?id=" + id;
			}
			var ret = window.showModalDialog(url,window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:500px');
			if(ret){
				setRest4IdName(ret[0],ret[1]); 
			}
		}
		
		function setRest4IdName(id,name){
			document.getElementById("rest4id").value = id;
			document.getElementById("rest4name").value = name;
		}
		
		
		function setRest2IdName(id,name){
			document.getElementById("rest2id").value = id;
			document.getElementById("rest2name").value = name;
		}
		
		//与财政系统同步数据	
	function setSynchronize(){
		var ret=window.showModalDialog("<%=path%>/synchroni/synchroni!tree.action",window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:500px');
		if(ret=="yes"){
			alert("同步成功。");
			window.location='<%=path%>/organisemanage/orgmanage!addchild.action';
			parent.parent.organiseTree.location='<%=path%>/organisemanage/orgmanage!tree.action';
		}else if(ret=="no")
		{
			alert("遇到不可预知的服务错误，同步失败。");
		}
	}
	
	//导入导出EXCEL数据
	function importExcels(){
	   if("${model.orgIsdel}" == "1"){
				alert("组织机构[${model.orgName}]已删除，不允许进行导入操作。");
				return;
			}
			/**if("${model.orgId}" == ""||null=="${model.orgId}"){
				alert("请先选择组织机构，不允许进行导入操作。");
				return;
			}**/
	   if("${model.isOrg}" == "0"){
				alert("部门组织机构下不能导入组织机构。");
				return;
			}
			
		//var tempConfim = "在最上级机构下导入会把以前的机构和部门以及下面的人员清除，是否继续？";	
		//if("001"!=$("#orgSyscode").val()){
		//	tempConfim = "在本机构下导入的数据会在本机构的编码下扩展并新建，是否继续？";
		//}
			
	  //if(confirm(tempConfim)){
		var orgId=$("#orgId").val();
		var ret=window.showModalDialog("<%=path%>/fileNameRedirectAction.action?toPage=organisemanage/orgmanage-import.jsp?orgId="+orgId,window,'help:no;status:no;scroll:no;dialogWidth:660px; dialogHeight:350px');
		// window.open ('<%=path%>/fileNameRedirectAction.action?toPage=organisemanage/organisemanage-import.jsp', 'newwindow', 'height=350, width=600, top=0, left=0, toolbar=yes, menubar=yes, scrollbars=yes, resizable=yes,location=yes, status=yes'); //这句要写成一
	 // }
	 }
	 
	 function exportExcels(){
		if(confirm("已删除的不会导出到excel中。")){
			var orgSysCode=$("#orgSyscode").val();
		    document.getElementById('tempframe').src="<%=path%>/organisemanage/orgmanage!exportExcels.action?orgSysCode="+orgSysCode;
		}
	  }
//特殊字符过滤
//匹配中文 数字 字母 下划线       
function checkInput(vvv) {
	var s = vvv.value;
	if(s==""||s==null) return;
	//var pattern = new RegExp("[`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）&mdash;—|{}【】‘；：”“'。，、？]")
    var pattern = new RegExp("[`~!@#$^&*()=|{}':;'\\[\\].<>/?~！@#￥……&*（）&mdash;—|{}【】‘；：”“'。？]")
    var rs = "";
	var ttt =/^[A-Za-z]+$/;//英文字母
	var k;
    for (var i = 0; i < s.length; i++) {
        //rs = rs + s.substr(i, 1).replace(pattern, '');
        //alert(pattern.test(s.substr(i,i+1)))
        var mmm = s.substr(i,1);
        if(ttt.test(mmm)){
        	continue;
        }
        if(pattern.test(mmm)){
        	//alert();
        	k=i;
        	rs="……";
        	break;
        }
    }
    if(rs!=""){
    	alert("不允许输入特殊字符。");
    	vvv.value = vvv.value.substring(0,k);
    }else{
    	return  true;
    }
}
	</script>
	<style>
	.Operation_list{
	cursor: pointer;
	height:24px;
	line-height:24px;
	text-align:center;
	text-decoration:none;
	background:url(<%=frameroot%>/images/bt_m.jpg) repeat-x;
}

	</style>
	</head>
	<body class=contentbodymargin oncontextmenu="return false;">
		<script type="text/javascript" language="javascript" src="<%=jsroot%>/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
				<tr>
					<td height="100%">
						<iframe scr='' id='tempframe' name='tempframe' style='display:none'>
						</iframe>
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td height="40" class="table_headtd">
									<table width="100%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td>
											&nbsp;
											</td>
											<td class="table_headtd_img">
												<img src="<%=frameroot%>/images/ico/ico.gif">&nbsp;
											</td>
											<td align="left">
												<strong>组织机构管理</strong>
											</td>
											<td align="right">
												<table border="0" align="right" cellpadding="0" cellspacing="0">
													<tr>
														<td width="100%" align="right">
															<table border="0" align="right" cellpadding="00" cellspacing="0">
																<tr>
																	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
												                 	<td class="Operation_list" onclick="addOrg();"><img src="<%=root%>/images/operationbtn/add.png"/>&nbsp;新&nbsp;建&nbsp;</td>
												                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
											                  		<td width="5"></td>
											                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
												                 	<td class="Operation_list" onclick="del();"><img src="<%=root%>/images/operationbtn/del.png"/>&nbsp;删&nbsp;除&nbsp;</td>
												                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
											                  		<td width="5"></td>
											                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
												                 	<td class="Operation_list" onclick="reductOrg();"><img src="<%=root%>/images/operationbtn/Reduction.png"/>&nbsp;还&nbsp;原&nbsp;</td>
												                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
											                  		<td width="5"></td>
											                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
												                 	<td class="Operation_list" onclick="importExcels();"><img src="<%=root%>/images/operationbtn/daoru.png"/>&nbsp;导&nbsp;入&nbsp;</td>
												                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
											                  		<td width="5"></td>
											                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
												                 	<td class="Operation_list" onclick="exportExcels();"><img src="<%=root%>/images/operationbtn/daochu.png"/>&nbsp;导&nbsp;出&nbsp;</td>
												                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
											                  		<td width="5"></td>
											                  		
																	<%--<td width="4"><img src="<%=frameroot%>/images/ch_h_l.gif"></td>
																	<td class="Operation_input" onclick="addOrg();">&nbsp;新&nbsp;建&nbsp;</td>
																	<td width="4"><img src="<%=frameroot%>/images/ch_h_r.gif"></td>
																	<td width="5"></td>
																	<td width="4"><img src="<%=frameroot%>/images/ch_h_l.gif"></td>
																	<td class="Operation_input" onclick="testcode();">&nbsp;保&nbsp;存&nbsp;</td>
																	<td width="4"><img src="<%=frameroot%>/images/ch_h_r.gif"></td>
																	<td width="5"></td>
																	<td width="4"><img src="<%=frameroot%>/images/ch_h_l.gif"></td>
																	<td class="Operation_input" onclick="reductOrg();">&nbsp;还&nbsp;原&nbsp;</td>
																	<td width="4"><img src="<%=frameroot%>/images/ch_h_r.gif"></td>
																	<td width="5"></td>
																	<td width="4"><img src="<%=frameroot%>/images/ch_h_l.gif"></td>
																	<td class="Operation_input" onclick="importExcels();">&nbsp;导&nbsp;入&nbsp;</td>
																	<td width="4"><img src="<%=frameroot%>/images/ch_h_r.gif"></td>
	                                                       			<td width="5"></td>
	                                                       			<td width="4"><img src="<%=frameroot%>/images/ch_h_l.gif"></td>
	                                                     			<td class="Operation_input" onclick="exportExcels();">&nbsp;导&nbsp;出&nbsp;</td>
                                                   					<td width="4"><img src="<%=frameroot%>/images/ch_h_r.gif"></td>
																	<td width="5"></td>
																	<td width="4"><img src="<%=frameroot%>/images/ch_h_l.gif"></td>
																	<td class="Operation_input" onclick="del();">&nbsp;删&nbsp;除&nbsp;</td>
																	<td width="4"><img src="<%=frameroot%>/images/ch_h_r.gif"></td>
																	<td width="5"></td>--%>
																	<!-- 暂未启用 屏蔽
																	<td >
																		<a class="Operation" href="javascript:setSynchronize();"> <img
																				src="<%=root%>/images/ico/copyprivil.gif" width="15"
																				height="15" class="img_s">同步&nbsp;</a>
																	</td>
																	 -->
																	<%--
																	<td width="5"></td>																	
																	<td width="50">
																		<a class="Operation" href="#" disabled> <img
																				src="<%=root%>/images/ico/shanchu.gif" width="15"
																				height="15" class="img_s">导入</a>
																	</td>
																	<td width="5"></td>
																	<td width="50">
																		<a class="Operation" href="#" disabled> <img
																				src="<%=root%>/images/ico/shanchu.gif" width="15"
																				height="15" class="img_s">导出</a>
																	</td>
																	<td width="5"></td>
																	<td width="86">
																		<a class="Operation" href="#" disabled> <img
																				src="<%=frameroot%>/images/shanchu.gif" width="15"
																				height="15" class="img_s">并转机构</a>
																	</td>
																	<td width="5"></td>
																	<td width="86">
																		<a class="Operation" href="#" disabled> <img
																				src="<%=frameroot%>/images/shanchu.gif" width="15"
																				height="15" class="img_s">拆分机构</a>
																	</td>
																	--%>
																</tr>
															</table>
														</td>
													</tr>
													<%--
													<tr higth="20"></tr>
													<tr>
														<td width="100%" align="right">
															<table width="100%" border="0" align="right"
																cellpadding="0" cellspacing="0">
																<tr>
																	<td width="*">
																		&nbsp;
																	</td>
																	<td width="86">
																		<a class="Operation" href="#" disabled> <img
																				src="<%=frameroot%>/images/shanchu.gif" width="15"
																				height="15" class="img_s">区域划分</a>
																	</td>
																	<td width="5"></td>
																	<td width="86">
																		<a class="Operation" href="#" disabled> <img
																				src="<%=frameroot%>/images/shanchu.gif" width="15"
																				height="15" class="img_s">合并机构</a>
																	</td>
																</tr>
															</table>
														</td>
													</tr>
													--%>
												</table>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
						<s:form id="mytable" action="/organisemanage/orgmanage!save.action" theme="simple">
							<label style="display: none;"><s:actionmessage/></label>
							<input type="hidden" id="orgId" name="orgId" value="${model.orgId}">
							<input type="hidden" id="iscode" name="iscode" value="${model.orgSyscode}">							
							<input type="hidden" id="needChildren" name="needChildren" value="0"/>
							<table width="100%" height="10%" border="0" cellpadding="0" cellspacing="1" align="center" class="table1">
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz"><font color="red">*</font>&nbsp;组织机构编号：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="orgSyscode" url="<%=path%>/organisemanage/orgmanage!compareCode.action" name="model.orgSyscode" type="text" size="22" value="${model.orgSyscode}" style="width:12em" onkeyup="checkInput(this);">
										<input type="hidden">
										<a  href="#" class="button" onclick="testMessage()">检测合法性</a>
										&nbsp;<font color="#999999">编号规则：33333443333</font>
										&nbsp;<font color="#999999">不允许建与001机构同级的机构</font>
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">机构是否删除：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<s:select name="model.orgIsdel" list="#{'0':'未删除','1':'已删除'}" id="orgIsdel"
											listKey="key" listValue="value" style="width:11em" disabled="true" style="width:12em"/>&nbsp;
										<font color="#999999">当机构删除后在其它模块不可用</font>
									</td>
								</tr>
								
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz"><font color="red">*</font>&nbsp;组织机构名称：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="orgName" name="model.orgName" maxlength="40" type="text" size="22" value="${model.orgName}" style="width:12em" onkeyup="checkInput(this);">
										<font color="#999999">&nbsp;简称：</font>
										<input id="rest5" name="model.rest5" type="text" size="22" value="${model.rest5}" style="width:12em" maxlength="500" onkeyup="checkInput(this);">
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<font color="#FF0000">&nbsp;</font> 机构性质：
									</td>
									<td class="td1" colspan="3" align="left">
										<s:select cssClass="formin" name="model.orgNature"
											list="#{'0':'省政府各有关部门','1':'各设区政府','2':'各县（市区）政府','3':'驻外办','4':'秘书处','5':'机构类型','6':'屏蔽类型'}"
											listKey="key" listValue="value" style="width:12em"/>
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">机构标识：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<s:select name="model.rest1"
											list="#{'0':'实机构','1':'虚机构'}"
											listKey="key" listValue="value" style="width:12em" />
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz"><font color="red">*</font>&nbsp;组织机构编码：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="orgCode" name="model.orgCode" type="text" size="22" onkeyup="checkInput(this);"
											value="${model.orgCode}" style="width:12em" maxlength="10">&nbsp;
											<font color="#999999">建议与组织机构编号相同</font>
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">区划代码：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="orgAreaName" name="areacodeName" type="text" size="22" value="${areacodeName}" onclick="selectArea()" readonly="readonly" style="width:12em">
										<input id="orgAreacode" name="model.orgAreaCode" type="hidden" size="22" value="${model.orgAreaCode}">
										<a  href="#" class="button" onclick="selectArea()">设置</a>
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">机构级别：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<s:select name="model.orgGrade"
											list="#{'0':'无','1':'省（部）级','2':'副省（部）级','3':'正厅（局）级','4':'副厅（局）级','5':'正处级','6':'副处级','7':'正科级','8':'副科级'}"
											listKey="key" listValue="value" style="width:12em" />
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">电话：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="orgTel" name="model.orgTel" type="text" size="22"
											value="${model.orgTel}" style="width:12em">&nbsp;
											<font color="#999999">电话格式：0791-8181111</font>
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">传真：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="orgFax" name="model.orgFax" type="text" size="22"
											value="${model.orgFax}" style="width:12em">&nbsp;
											<font color="#999999">传真格式：0791-8181111</font>
									</td>
								</tr>
				<!-- modify by luosy 2013-12-04 注释签发领导和责任编辑，这个是采编系统用的字段
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">签发领导：</span>
									</td>
									<%--<td class="td1" colspan="3" align="left">
										<input class="information_out_input" id="rest3"
											name="model.rest3" type="text" value="${IssuePeople}" style="width:12em"/>
									</td>--%>
									
									<td class="td1" colspan="3" align="left">
										   <input readonly="readonly" id="IssuePeople" name="IssuePeople" type="text"
											size="22" value="${IssuePeople}" style="width:12em">
									       <input id="IssuePeopleid" name="model.rest3" type="hidden"
											size="22" value="${model.rest3}">
									<a  href="#" class="button" onclick="selecteIssuePeople()">设置</a>
									</td>
									
									</tr>
								
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">责任编辑：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										   <input readonly="readonly" id="editor" name="editor" type="text"
											size="22" value="${editor}" style="width:12em">
									       <input id="editorid" name="model.rest3" type="hidden"
											size="22" value="${model.rest3}">
									<a  href="#" class="button" onclick="selecteditor()">设置</a>
									</td>
									
			
								</tr>
								-->
								
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">分管领导：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input readonly="readonly" id="rest2name" name="rest2name" type="text"
											size="22" value="${rest2name}" style="width:12em">
										<input id="rest2id" name="model.rest2" type="hidden"
											size="22" value="${model.rest2}">
										<a  href="#" class="button" onclick="selectRest2()">设置</a>
										&nbsp;<font color="#999999">供流程处理人选择-当前用户所在部门分管领导 </font>
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">部门分管厅领导：</span>
									</td>
									<td class="td1" colspan="3" align="left" >
										<input readonly="readonly" id="rest4name" name="rest4name" type="text"
											size="22" value="${rest4name}" style="width:12em">
										<input id="rest4id" name="model.rest4" type="hidden"
											size="22" value="${model.rest4}">
											<a  href="#" class="button" onclick="selectRest4()">设置</a>
										&nbsp;<font color="#999999">设置本部门的分管厅领导，在厅领导统计其分管部门数据时使用</font>
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right" valign="top">
										<span class="wz">负责人：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<%--<input id="username" name="username" type="text" size="22"
											value="${userName}" onclick="selectUser();"
											readonly="readonly">
										--%>
								<s:textarea title="点击此处,选择人员" rows="4" cols="38" readonly="true" id="username" name="userName" onclick="selectUser();"></s:textarea>	
										<input id="orgManager" name="model.orgManager" type="hidden"
											size="22" value="${model.orgManager}" onclick="selectUser();">
										
											<a  href="#" class="button" onclick="selectUser()">设置</a>
										&nbsp;<font color="#999999">供流程处理人选择-部门负责人 </font>
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz"><font color="red">*</font>&nbsp;排序序号：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="orgSequence" name="model.orgSequence" maxlength="10" type="text"
											size="22" value="${model.orgSequence}" style="width:12em">&nbsp;
										<font color="#999999">数值越小排名越前</font>
									</td>
								</tr>
								
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz"><font color="red">*</font>&nbsp;机构类型：</span>
									</td>
									<td class="td1" colspan="3" align="left">
									<s:if test="model.orgId != null && model.orgId !=''">
										<s:select id="isOrg" name="model.isOrg" 
											list="#{'0':'部门','1':'机构'}"
											listKey="key" listValue="value" style="width:11em" headerKey="" headerValue="请选择机构类型" disabled="true" style="width:12em"/>
									
									</s:if>	
									<s:else>
										<s:select id="isOrg" name="model.isOrg" 
											list="#{'0':'部门','1':'机构'}"
											listKey="key" listValue="value" style="width:11em" headerKey="" headerValue="请选择机构类型" style="width:12em"/>
									</s:else>
									</td>
								</tr>						
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">地址：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="orgAddr" name="model.orgAddr" type="text" size="40"
											maxlength="32" value="${model.orgAddr}" onkeyup="checkInput(this);" style="width:23.2em">
											<font color="#999999">&nbsp;最大长度为32字</font>
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right" valign="top">
										<span class="wz">机构描述：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<textarea id="orgDescription" name="model.orgDescription"
											rows="4" cols="38">${model.orgDescription }</textarea>
									</td>
								</tr>
								<tr>
									<td class="table1_td"></td>
									<td></td>
								</tr>
							</table>
							<table id="annex" width="90%" height="10%" border="0"
								cellpadding="0" cellspacing="1" align="center" class="table1">
							</table>
						</s:form>
						<table width="100%" border="0" cellspacing="0" cellpadding="00" bgcolor="#f4f4f4">
							<tr>
								<td align="center" valign="middle">
									<table border="0" align="center" cellpadding="00" cellspacing="0">
										<tr>
											<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
						                 	<td class="Operation_input" onclick="testcode();">&nbsp;保&nbsp;存&nbsp;</td>
						                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
					                  		<td width="5"></td>
										</tr>
									</table> 
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</DIV>
	</body>
</html>