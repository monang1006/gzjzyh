<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>

		<title>增加机构</title>
		<%@include file="/common/include/meta.jsp"%>
		<LINK type=text/css rel=stylesheet
			href="<%=frameroot%>/css/properties_windows.css">
		<script type="text/javascript"
			src="<%=root%>/common/js/jquery/jquery-1.2.6.js"></script>

		<script language="javascript">

      //添加机构
     var addOrg=function(){
       var code=$("#iscode").val();
       var id=$("#orgId").val();
       if(id==""){
       code="";
       }
       window.location ="<%=root%>/personnel/personorg/personOrg!addchild.action?orgSysCode="+code+"&orgId="+id;
    }
    
    //编辑
    function edit(){
	 var id=$("#orgId").val();
	 if(id!=""){
	 window.location="<%=path %>/personnel/personorg/personOrg!input.action?orgId="+id;
	 }
	 }
	 //合并
	function hebing(){
	//合并机构
		function merge(){
			var id=$("#orgId").val();
			var name="${model.orgName}";
			if(id!=""){
			var audit= window.showModalDialog("<%=root%>/personnel/personorg/personOrg!selectTree.action?orgId="+id, window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:400px');
		
		    if(audit==undefined||audit==null){
		      return;
		    }else  if(id==audit[0]){
                 alert("不可以自己移动到自己下面！");
                 return;		    
		    }else{
		     $.post("<%=root%>/personnel/personorg/personOrg!parentOrgs.action",
		        {"orgId":id},
		          function(data){
		           ids=  data.split(",");
		           ids1=audit.split(",");
		           for(var i=0;i<ids.length;i++){
		              for(var j=0;j<ids1.length;j++){
		                 if(ids1[j]==ids[i]){
		                 alert("请不要选择"+name+"的父机构！");
		                   return;
		                 }
		              }
		           }
		         if(confirm("确定要合并吗？")){
		          window.showModalDialog("<%=root%>/personnel/personorg/personOrg!input.action?moveOrgId="+audit+"&orgId="+id+"&audittype=merge"
		                                ,window,'help:no;status:no;scroll:no;dialogWidth:500px; dialogHeight:450px');
		          window.parent.document.location.reload();     
		          
		         }
		       });
		    }
		   
		}
		}
	 
	}
	
	//移动
	function yidong(){
	        var id=$("#orgId").val();
			var name="${model.orgName}";
			if(id!=""){
			var audit= window.showModalDialog("<%=root%>/personnel/personorg/personOrg!radiotree.action?orgId="+id,window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:400px');
		
		    if(audit==undefined||audit==null){
		      return;
		    }else  if(id==audit[0]){
                 alert("不可以自己移动到自己下面！");
                 return;		    
		    }
		    if(confirm(name+"移动到"+audit[1]+"下，"+name+"下的子机构也会跟着移动。确定要移动吗？")){
		      location="<%=root%>/personnel/personorg/personOrg!tomove.action?moveOrgId="+audit[0]+"&orgId="+id;
		    }else{
		    return;
		    }
		    }
	
	}
	function daoru(){
		var url="<%=path %>/personnel/personorg/personOrg!baseList.action";
		window.showModalDialog(url,window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:500px');
	
	}
	
    //保存
    function onSub(){
    var orgSyscode=$("#orgSyscode").val();
    var orgName=$("#orgName").val();
    var result=/^(([0\+]\d{2,3}-)?(0\d{2,3})-)?(\d{7,8})(-(\d{3,}))?$/;   
    var noLeadNumber=$("#noLeadNumber").val();
    var leadnumber=$("#leadNumbe").val();
    if(orgSyscode==""){
    alert("机构编码不可以为空！");
    return false;
    }else if(orgName==""){
    alert("机构名称不可以为空！");
    return false;
    }
     if(leadnumber.search(/^\d+$/)==-1){
		alert("领导人数不可以为空，并且只能为整数！");
			return;
		  	}	 
	 if(noLeadNumber.search(/^\d+$/)==-1){
		alert("非领导人数不可以为空，并且只能为整数！");
			return;
		  	}	   
    if($("#orgTel").val()!=""&&!result.test($("#orgTel").val())){
    alert("电话号码格式不正确！")
    return;
    }
     if($("#orgFax").val()!=""&&!result.test($("#orgFax").val())){
    alert("传真号码格式不正确！")
    return;
    }
    if($("#orgDescription").val().length>100){
     alert("机构描述请不要超过一百字！")
    return;
    }
    $("#veteranform").attr("action","<%=path %>/personnel/personorg/personOrg!save.action");
    veteranform.submit();
    }
    function showNature(){
      var nature="${model.orgNature}";
      if(nature!=""&&nature!=null){
        $("#${model.orgNature}").attr("selected",true);
      }
    }
	</script>
	</head>
	<body class=contentbodymargin oncontextmenu="return false;" onload="showNature()">
		<script type="text/javascript" language="javascript"
			src="<%=jsroot%>/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
		<s:form action="" id="veteranform" name="veteranform" method="post" enctype="multipart/form-data">
		<input type="hidden" id="orgId" name="orgId"
								value="${model.orgid}">
		<input type="hidden" id="userOrgid" name="userOrgid"
								value="${model.userOrgid}">
		<input type="hidden" id="userOrgcode" name="userOrgcode"
								value="${model.userOrgcode}">
							<input type="hidden" id="iscode" name="iscode"
								value="${model.orgSyscode}">
									<table width="100%" border="0" cellspacing="0" cellpadding="00"
									style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
										<br>
									      <tr>
											<td>&nbsp;</td>
											<td width="30%">
												<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
													height="9">&nbsp;
												人事机构管理
											</td>
											<td width="1%">
												&nbsp;
											</td>
											<td width="70%">
												<table width="100%" border="0" align="right" cellpadding="0"
													cellspacing="0">
													<tr>
														<td width="100%" align="right">
															<table border="0" align="right"
																cellpadding="0" cellspacing="0">

																<tr>
																	<td width="*">
																		&nbsp;
																	</td>
																	
																	<td>
																		<a class="Operation"
																			href="javascript:addOrg();">
																			<img src="<%=root%>/images/ico/tianjia.gif"
																				width="15" height="15" class="img_s">添加</a>
																	</td>
																	<td width="5"></td>
																<!--  	<td width="50">
																		<a class="Operation"
																			href="javascript:yidong();">
																			<img src="<%=root%>/images/ico/yidong.gif"
																				width="15" height="15" class="img_s">移动</a>
																	</td>
																	<td width="5"></td>
																	<td width="50">
																		<a class="Operation"
																			href="javascript:hebing();">
																			<img src="<%=root%>/images/ico/yidong.gif"
																				width="15" height="15" class="img_s">合并</a>
																	</td>
																	<td width="5"></td>
																	<td width="50">
																		<a class="Operation"
																			href="javascript:daoru()">
																			<img src="<%=root%>/images/ico/yidong.gif"
																				width="15" height="15" class="img_s">导入</a>
																	</td>
																 
																	<td width="5"></td>
																	<td width="50">
																		<a class="Operation"
																			href="#">
																			<img src="<%=root%>/images/ico/yidong.gif"
																				width="15" height="15" class="img_s">导出</a>
																	</td>
																	
																	
																	
																	<td width="5"></td>
																
																	<td width="80" >
																	<a class="Operation"
																			href="javascript:yidong();">
																			<img src="<%=root%>/images/ico/yidong.gif"
																				width="15" height="15" class="img_s">编制情况</a>
																	</td>
																	<td width="5"></td>
																	<td width="80" >
																	<a class="Operation"
																			href="javascript:yidong();">
																			<img src="<%=root%>/images/ico/yidong.gif"
																				width="15" height="15" class="img_s">人员信息</a>
																	</td>
																	
																	<td width="5"></td> -->
																	</tr>
													

															
												</table>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
						 <table width="100%">
			            	<tr>
				        	<td height="10">	
					  
				         	</td>
			              	</tr>
			           </table>
							
					<table width="100%" height="10%" border="0" cellpadding="0"
								cellspacing="1" align="center" class="table1">
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">机构编号(<font color="red">*</font>)：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="orgSyscode"
											
											name="model.orgSyscode" type="text" onkeydown="return false;" size="22"
											value="${model.orgSyscode }">
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">是否删除：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<s:select disabled="true" name="model.orgIsdel" list="#{'0':'未删除','1':'已删除'}" id="orgIsdel"
											listKey="key" listValue="value" style="width:11em" />
										
										
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">机构名称(<font color="red">*</font>)：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="orgName" name="model.orgName" maxlength="50" type="text" size="22"
											value="${model.orgName}">
									</td>
								</tr>
								
							
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">机构代码：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="orgCode" name="model.orgCode"
											type="text" size="22" maxlength="20" value="${model.orgCode }">
										
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">机构性质：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<select id="orgNature" name="model.orgNature">
										<s:iterator id="vo" value="dictlist">
										<option id="${vo.dictItemCode }" value="${vo.dictItemCode }">${vo.dictItemName }</option>
										</s:iterator>
										</select>
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">领导人数(<font color="red">*</font>)：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="leadNumbe" name="model.leadNumbe" size="22" maxlength="5" 
											value="${model.leadNumbe }">
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">非领导人数(<font color="red">*</font>)：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="noLeadNumber" name="model.noLeadNumber" size="22" maxlength="5" 
											value="${model.noLeadNumber }">
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">电话：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="orgTel" name="model.orgTel" type="text" size="22"
											value="${model.orgTel }">
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">传真：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="orgFax" name="model.orgFax" type="text" size="22"
											value="${model.orgFax }">
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">负责人：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="orgManager" name="model.orgManager" type="text" size="22"
											value="${model.orgManager }" >
										
										
									</td>
								</tr>
								
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">地址：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="orgAddr" name="model.orgAddr" maxlength="60" type="text" size="60"
											value="${model.orgAddr }">
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">机构描述：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<textarea id="orgDescription" maxlength="100" name="model.orgDescription"
											rows="4" cols="38">${model.orgDescription }</textarea>
									</td>
								</tr>

							</table>
							<table id="annex" width="90%" height="10%" border="0"
								cellpadding="0" cellspacing="1" align="center" class="table1">
							</table>
				
						<table width="90%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td align="center" valign="middle">
									<table width="27%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td width="30%">
												<input name="Submit2" type="button" class="input_bg"
													value="保 存" onclick="onSub();">
											</td>
											<td width="30%">
											<!-- 	<input name="gonext" type="button" onclick="window.history.go(-1);" class="input_bg"
													value="取 消"> -->
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
				
		</s:form>
		</DIV>
	</body>
</html>
