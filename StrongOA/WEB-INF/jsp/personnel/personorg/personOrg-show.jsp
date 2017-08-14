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
    var addOrg=function(){
       var code=$("#iscode").val();
       var id=$("#orgId").val();
      
       var rest=$("#rest").val();
			if(rest!=null && rest!=''){
				alert('该机构属于统一用户那边机构不能添加！');
				return;
				
			}
       window.location ="<%=root%>/personnel/personorg/personOrg!addchild.action?orgSysCode="+code+"&orgId="+id;
    }
	function hebing(){
	 window.showModalDialog("org-merge.jsp",window,'help:no;status:no;scroll:no;dialogWidth:260px; dialogHeight:300px');
	 
	}
	 
	function edit(){
	 var id=$("#orgId").val();
	 if('${model.orgIsdel}'=="1"){
	 alert("该机构已经删除，不可以在修改！");
	 return;
	 }
	      var rest=$("#rest").val();
			if(rest!=null && rest!=''){
				alert('该机构属于统一用户那边机构不能随意修改！');
				return;
				
			}
	 window.location="<%=path %>/personnel/personorg/personOrg!input.action?orgId="+id;
	 }
	 
	function restoreOrg(){
	 var id=$("#orgId").val();
	 if(confirm("该操作将导致该组织机构上级组织机构一起还原,确定还原组织机构吗?")) 
    { 
	  window.location ="<%=path%>/personnel/personorg/personOrg!restore.action?orgId="+id;
	 }
	 }
	 
	function del(){
	 var id=$("#orgId").val();
	 
	  var rest=$("#rest").val();
			if(rest!=null && rest!=''){
				alert('该机构属于统一用户那边机构不能删除！');
				return;
				
			}
	 if(confirm("该操作将导致该组织机构下子级组织机构和用户也被删除,确定删除组织机构吗?")) 
	{ 
	 window.location="<%=path %>/personnel/personorg/personOrg!delete.action?orgId="+id;
	 }
	 }
    function onSub(){
    $("#veteranform").attr("action","<%=path %>/personnel/personorg/personOrg!save.action");
    veteranform.submit();
    }
    
    	
		//移动机构
		function tomove(){
			var id=$("#orgId").val();
			var name="${model.orgName}";
				var isdel="${model.orgIsdel}";
			if(isdel=="1"){
			   alert("该机构已经删除，不可以移动！");
			   return;
			}
			var rest=$("#rest").val();
			if(rest!=null && rest!=''){
				alert('该机构属于统一用户那边机构不能移动！');
				return;
			}
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
		//合并机构
		function merge(){
			
			var id=$("#orgId").val();
			var name="${model.orgName}";
			var isdel="${model.orgIsdel}";
			if(isdel=="1"){
			   alert("该机构已经删除，不可以执行合并！");
			   return;
			}
			var rest=$("#rest").val();
			if(rest!=null && rest!=''){
				alert('该机构属于统一用户那边机构不能合并！');
				return;
				
			}
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
		
	function daoru(){
		var url="<%=path %>/personnel/personorg/personOrg!baseList.action";
		window.showModalDialog(url,window,'help:no;status:no;scroll:no;dialogWidth:500px; dialogHeight:600px');
	
	}
		function showstructure(){
		    top.perspective_content.actions_container.personal_properties_toolbar.navigate("<%=path%>/fileNameRedirectAction.action?toPage=personnel/structure/personStructure-content.jsp","编制管理");
		}
		function showperson(){
		 top.perspective_content.actions_container.personal_properties_toolbar.navigate("<%=root%>/personnel/baseperson/person.action?orgId=${model.orgid}","人员信息");
		}
	</script>
	</head>
	<body class=contentbodymargin oncontextmenu="return false;">
		<script type="text/javascript" language="javascript"
			src="<%=jsroot%>/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
		<s:form action="" id="veteranform" name="veteranform" method="post" enctype="multipart/form-data">
		<input type="hidden" id="rest" name="rest"
								value="${model.rest}">
		<input type="hidden" id="orgId" name="orgId"
								value="${model.orgid}">
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
															<table width="100%" border="0" align="right"
																cellpadding="0" cellspacing="0">

																<tr>
																	<td width="*">
																		&nbsp;
																	</td>
																	
																	<td width="50">
																		<a class="Operation"
																			href="javascript:addOrg();">
																			<img src="<%=root%>/images/ico/tianjia.gif"
																				width="15" height="15" class="img_s">添加</a>
																	</td>
																	<td width="5"></td>
																	<td width="50">
																		<a class="Operation" href="javascript:edit();"> <img
																				src="<%=root%>/images/ico/bianji.gif" width="15"
																				height="15" class="img_s">编辑</a>
																	</td>
																	<td width="5"></td>
																	<td width="50">
																		<a class="Operation" href="javascript:del();"> <img
																				src="<%=root%>/images/ico/shanchu.gif" width="15"
																				height="15" class="img_s">删除</a>
																	</td>
																	<td width="5"></td>
																	<td width="50">
																		<a class="Operation" href="javascript:restoreOrg();"> <img
																				src="<%=root%>/images/ico/cexiao.gif" width="15"
																				height="15" class="img_s">还原</a>
																	</td>
																	<td width="5"></td>
																	<td width="50">
																		<a class="Operation"
																			href="javascript:tomove();">
																			<img src="<%=root%>/images/ico/yidong.gif"
																				width="15" height="15" class="img_s">移动</a>
																	</td>
																	<td width="5"></td>
																	<td width="80">
																		<a class="Operation"
																			href="javascript:merge();">
																			<img src="<%=root%>/images/ico/yidong.gif"
																				width="15" height="15" class="img_s">合并</a>
																	</td>
																	
																	<%-- 
																	<td width="5"></td>
																	<td width="80">
																		<a class="Operation"
																			href="javascript:daoru();">
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
																	
																	
																	<td width="5" ></td>
																	</tr>
																	<tr>
																	<td width="*"  height=5px;>
																	</td>
																	
																	<td width="50"  height=5px;>
																		
																	</td>
																	<td width="5"  height=5px;></td>
																	<td width="50" height=5px;>
																		
																	</td>
																	<td width="5"  height=5px;></td>
																	<td width="50" height=5px;>
																		
																	</td>
																	<td width="5"  height=5px;></td>
																	<td width="50"  height=5px;>
																		
																	</td>
																	<td width="5"  height=5px;></td>
																	<td width="50"  height=5px;>
																		
																	</td>
																	<td width="5"  height=5px;></td>
																	<td width="80"   height=5px;>
																	
																	</td>
																	<td width="5"  height=5px;></td>
																	<td width="80" height=5px; >
																	</td>
																	
																	<td width="5"  height=5px;></td>
																	</tr>
														<tr>
																	<td width="*">
																		&nbsp;
																	</td>
																	
																	<td width="50">
																		
																	</td>
																	<td width="5"></td>
																	<td width="50">
																		
																	</td>
																	<td width="5"></td>
																	<td width="50">
																		
																	</td>
																	<td width="5"></td>
																	<td width="50">
																		
																	</td>
																	<td width="5"></td>
																	<td width="50">
																		
																	</td>
																	<td width="5"></td>
																	
																
																	<td width="80" >
																	<a class="Operation"
																			href="javascript:showstructure();">
																			<img src="<%=root%>/images/ico/yidong.gif"
																				width="15" height="15" class="img_s">编制情况</a>
																	</td>
																	<td width="5"></td>
																	<td width="80" >
																	<a class="Operation"
																			href="javascript:showperson();">
																			<img src="<%=root%>/images/ico/yidong.gif"
																				width="15" height="15" class="img_s">人员信息</a>
																	</td>
																	--%>
																	<td width="5"></td>
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
										<span>${model.orgSyscode }</span>
										
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">是否删除：</span>
									</td>
									<td class="td1" colspan="3" align="left">
									<s:if test="model.orgIsdel==0">
										<span>否</span>
										</s:if>
										<s:else>
										<span>是</span>
										</s:else>
										
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">机构名称(<font color="red">*</font>)：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<span>${model.orgName}</span>
									</td>
								</tr>
								
							
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">机构代码：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<span>${model.orgCode }</span>
										
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">机构性质：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<span>${model.orgNatureName}</span>
									
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">领导人数：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<span class="wz">${model.leadNumbe }</span>
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">非领导人数：</span>
									</td>
									<td class="td1" colspan="3" align="left">
									<span class="wz">${model.noLeadNumber }</span>
										
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">电话：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<span>${model.orgTel }</span>
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">传真：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<span>${model.orgFax }</span>
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">负责人：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<span>${model.orgManager }</span>
										
										
									</td>
								</tr>
								
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">地址：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<span>${model.orgAddr }</span>
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">机构描述：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										${model.orgDescription }
									</td>
								</tr>

							</table>
							<table id="annex" width="90%" height="10%" border="0"
								cellpadding="0" cellspacing="1" align="center" class="table1">
							</table>
				
						
				
		</s:form>
		</DIV>
	</body>
</html>
