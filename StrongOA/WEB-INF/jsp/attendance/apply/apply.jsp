<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>申请单列表</title>
		<META http-equiv=Content-Type content="text/html; charset=utf-8">
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<LINK type=text/css rel=stylesheet
			href="<%=frameroot%>/css/search.css">
		<script language='javascript'
			src='<%=request.getContextPath()%>/common/js/grid/ChangeWidthTable.js'></script>
		<SCRIPT language="javascript"
			src="<%=request.getContextPath()%>/common/js/menu/menu.js"></SCRIPT>

		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<LINK href="<%=frameroot%>/css/search.css" type=text/css
			rel=stylesheet>
		<script language='javascript'
			src='<%=path%>/common/js/jquery/jquery-1.2.6.js'></script>
		<script language="javascript"
			src="<%=path%>/common/js/common/search.js"></script>
		<script language="javascript"
			src="<%=path%>/common/js/common/common.js"></script>
		<SCRIPT LANGUAGE="JavaScript"
			src="<%=path%>/common/js/commontab/service.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript"
			src="<%=path%>/common/js/commontab/workservice.js"></SCRIPT>
		<script type="text/javascript">
		 function showimg(topicStatus){
      
			var rv = '' ;
			if(topicStatus == '0'){
				rv = "<font color='#90036'>未提交</font>&nbsp&nbsp";
			}
			if(topicStatus == '1'){
				rv = "<font color='#ff224a'>审核中</font>&nbsp&nbsp";
			}
			if(topicStatus == '2'){
			   rv = "<font color='#34c700'>已审核</font>&nbsp&nbsp";
			}
			
			return rv;
		}
		
		 
		 </script>
	</HEAD>

	<BODY class=contentbodymargin oncontextmenu="return false;"
		onload="initMenuT();">
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center style="float:right">
			<s:form theme="simple" id="myTableForm"
				action="/attendance/apply/apply.action" method="post">
				<table width="100%" border="0" cellspacing="0" cellpadding="00">
					<tr>
						<td height="100%">
							<table width="100%" border="0" cellspacing="0" cellpadding="00">
								<tr>
									<td height="40"
										style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
										<table width="100%" border="0" cellspacing="0"
											cellpadding="00">
											<tr>
												<td>
												&nbsp;
												</td>
												<td width="30%">
													<img src="<%=frameroot%>/images/perspective_leftside/ico.gif"
														width="9" height="9" align="center">&nbsp;
													请假单列表
												</td>
												<td width="*">
													&nbsp;
												</td>
												<td width="70%">
												<table border="0" align="right" cellpadding="00"
											cellspacing="0">
												<tr>
												<td width="5"></td>
												<td >
													<a class="Operation" href="#" onclick="addTitle()"> <img
															src="<%=root%>/images/ico/tb-add.gif" width="14"
															height="14" class="img_s"><span id="test"
														style="cursor:hand">添加&nbsp;</span> </a>
												</td>
												<td width="5"></td>
												<td >
													<a class="Operation" href="#" onclick="editTitle()"> <img
															src="<%=root%>/images/ico/bianji.gif" width="15"
															height="15" class="img_s"><span id="test"
														style="cursor:hand">编辑&nbsp;</span> </a>
												</td>
												<td width="5"></td>
												<td >
													<a class="Operation" href="#" onclick="dotowork()"> <img
															src="<%=root%>/images/ico/songshen.gif" width="15"
															height="15" class="img_s"><span id="test"
														style="cursor:hand">送审&nbsp;</span> </a>
												</td>
												<td width="5"></td>
												<td >
													<a class="Operation" href="#" onclick="deleteTitle()">
														<img src="<%=root%>/images/ico/shanchu.gif" width="15"
															height="15" class="img_s"><span id="test"
														style="cursor:hand">删除&nbsp;</span> </a>
												</td>

												<td width="5"></td>
												<td >
													<a class="Operation" href="#" onclick="setcancle()"> <img
															src="<%=root%>/images/ico/xiaohui.gif" width="15"
															height="15" class="img_s"><span id="test"
														style="cursor:hand">销假&nbsp;</span> </a>
												</td>
												<td width="5"></td>
												<td >
													<a class="Operation" href="#" onclick="canclelist()"> <img
															src="<%=root%>/images/ico/xiaohui.gif" width="15"
															height="15" class="img_s"><span id="test"
														style="cursor:hand">销假列表&nbsp;</span> </a>
												</td>
												<td width="5"></td>
												</tr></table></td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
							<webflex:flexTable name="myTable" width="100%" height="364px"
								wholeCss="table1" property="applyId" isCanDrag="true"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByProperty" collection="${page.result}"
								page="${page}">
								<table width="100%" border="0" cellpadding="0" cellspacing="1"
									class="table1">
									<tr>
										<td width="5%" align="center" class="biao_bg1">
											<img src="<%=root%>/images/ico/sousuo.gif" id="img_sousuo"
												width="15" height="15" style="cursor: hand;" title="单击搜索">
										</td>
										<td width="10%" class="biao_bg1">
											<s:textfield name="applyer" cssClass="search"
												title="请输入申请人姓名"></s:textfield>
										</td>
										<td width="15%" class="biao_bg1">
											<strong:newdate id="applyDate" name="applyDate"
												dateform="yyyy-MM-dd" isicon="true" width="100%"
												dateobj="${applyDate}" classtyle="search" title="申请日期" />
										</td>
										<td width="10%" class="biao_bg1">
											<s:select list="typeList" listKey="typeName"
												listValue="typeName" headerKey="" headerValue="请选择类型"
												id="applyType" name="applyType" style="width:100%" />

										</td>
										<td width="18%" class="biao_bg1">
											<strong:newdate name="startDate" id="startDate"
												skin="whyGreen" isicon="true" dateobj="${startDate}"
												dateform="yyyy-MM-dd" width="100%" classtyle="search" title="开始日期"></strong:newdate>
										</td>
										<td width="18%" class="biao_bg1">
											<strong:newdate name="endDate" id="endDate" skin="whyGreen"
												isicon="true" dateobj="${endDate}" dateform="yyyy-MM-dd"
												width="100%" classtyle="search" title="结束日期"></strong:newdate>
										</td>
										<td width="24%" class="biao_bg1">
											<s:select name="status"
												list="#{'':'选状态','0':'未提交','1':'审核中','2':'已审核'}"
												listKey="key" listValue="value" style="width:100%" />
										</td>

										<td width="5%" class="biao_bg1">
											&nbsp;
										</td>
									</tr>
								</table>
								<webflex:flexCheckBoxCol caption="选择" property="applyId"
									showValue="applyTypeName" width="5%" isCheckAll="true"
									isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexTextCol caption="申请状态" property="applyState"
									showValue="applyState" width="0" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="申请人" property="applicants"
									showValue="applicants" width="10%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
								<webflex:flexDateCol caption="申请日期" property="applyTime"
									showValue="applyTime" dateFormat="yyyy-MM-dd" width="15%"
									isCanDrag="true" isCanSort="true"></webflex:flexDateCol>
								<webflex:flexTextCol caption="申请类型" property="applyTypeName"
									showValue="applyTypeName" width="10%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
								<webflex:flexDateCol caption="开始时间" property="applyStime"
									showsize="20" showValue="applyStime"
									dateFormat="yyyy-MM-dd HH:mm" width="18%" isCanDrag="true"
									isCanSort="true"></webflex:flexDateCol>
								<webflex:flexDateCol caption="结束时间" property="applyEtime"
									showsize="20" showValue="applyEtime"
									dateFormat="yyyy-MM-dd HH:mm" width="18%" isCanDrag="true"
									isCanSort="true"></webflex:flexDateCol>
								<webflex:flexTextCol caption="说明" property="applyReason"
									showValue="applyReason" width="14%"
									onclick="viewDesc(this.value)" isCanDrag="true"
									isCanSort="true" showsize="8"></webflex:flexTextCol>
								<webflex:flexTextCol caption="申请状态" property="applyState"
									showValue="javascript:showimg(applyState)" width="10%"
									isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
							</webflex:flexTable>
						</td>
					</tr>
				</table>
			</s:form>
		</DIV>

		<script type="text/javascript">
		var sMenu = new Menu();
		function initMenuT(){
		 $("input:checkbox").parent().next().hide(); //隐藏第二列
		 
			sMenu.registerToDoc(sMenu);
			var item = null;
			item = new MenuItem("<%=root%>/images/ico/tb-add.gif","添加","addTitle",1,"ChangeWidthTable","checkMoreDis");
			sMenu.addItem(item);
			item = new MenuItem("<%=root%>/images/ico/bianji.gif","编辑","editTitle",1,"ChangeWidthTable","checkOneDis");
			sMenu.addItem(item);
			item = new MenuItem("<%=root%>/images/ico/songshen.gif","送审","dotowork",1,"ChangeWidthTable","checkOneDis");
			sMenu.addItem(item);
			item = new MenuItem("<%=root%>/images/ico/shanchu.gif","删除","deleteTitle",1,"ChangeWidthTable","checkOneDis");
			sMenu.addItem(item);
			item = new MenuItem("<%=root%>/images/ico/goujian.gif","销假","setcancle",1,"ChangeWidthTable","checkOneDis");
			sMenu.addItem(item);
			//item = new MenuItem("<%=root%>/images/ico/kaiguan.gif","销假列表","setRevive",1,"ChangeWidthTable","checkOneDis");
			//sMenu.addItem(item);
			sMenu.addShowType("ChangeWidthTable");
		    registerMenu(sMenu);
		}
		//添加
		function addTitle(){
				$.post("<%=root%>/attendance/apply/apply!isPerson.action",
						{},
						function(data){
						    if(data=="0"){
						        alert("您无权操作，请于考勤管理员联系！");
						        return;
						    }else{
						      	window.location="<%=path%>/attendance/apply/apply!input.action";
						    }
				});
		}
		//编辑
		function editTitle(){
			var id=getValue();
			
		    $.post("<%=root%>/attendance/apply/apply!isPerson.action",
				{},
				function(data){
				    if(data=="0"){
				        alert("您无权操作，请于考勤管理员联系！");
				        return;
				    }else{
				    if(id==null || id==""){
						alert("请选择需要编辑的记录！");
							return;
						}
						if(id.length >32){
						alert('一次只能编辑一条记录!');
						return;
					    }	
						var state=$(":checked").parent().next().attr("value");
						if(state=="1"){
							alert("该申请单已提交审核，不允许编辑！");
							return;
						}else if(state=="2"){
							alert("该申请单已审核完毕，不允许编辑！");
							return;
						}
					 	window.location="<%=path%>/attendance/apply/apply!input.action?bussinessId="+id+"&applyId="+id;
				    }
		    });	
		}
		
		 function setcancle(){
		    var id=getValue();
			
			$.post("<%=root%>/attendance/apply/apply!checkisCancle.action",
				   {ids:id},
				   function(data){
				   		if("success" == data){       
					   		if(id==null || id==""){
								alert("请选择需要销假的记录！");
								return;
							}
							if(id.length >32){
								alert('一次只能销假一条记录!');
								return;
							}	
							var state=$(":checked").parent().next().attr("value");
							if(state=="0" || state=="1"){
								alert("该申请单还未通过审核，不允许销假！");
								return;
							}
					         window.location="<%=path%>/attendance/apply/apply!inputcanc.action?ids="+id+"&apptype=apply";    
				   		}else if(data=="0"){
				   			 alert("您无权操作，请于考勤管理员联系！");
				   			 return ;
				   		}else{
				   			alert("该申请单已销假，不允许再次销假！");
				   			return ;
				   		}
				   	}	
			);
					
		
		 	//window.location="<%=path%>/attendance/apply/apply!inputcanc.action?ids="+id;
		 	
		 }
		 //删除
		function deleteTitle(){
		    var temp=true;
			var id=getValue();
			
		  $.post("<%=root%>/attendance/apply/apply!isPerson.action",
			{},
			function(data){
			    if(data=="0"){
			        alert("您无权操作，请于考勤管理员联系！");
			        return;
			    }else{
				    if(id==null || id==""){
					    alert("请选择需要删除的记录！");
						return;
					}
					if(id.split(",").length>1){
						alert("请选择一条记录！");
						return;
					}
					$(":checked").each(function(){
					    if( $(this).parent().next().attr("value")=="1"){
					    	temp=false;
					    	alert("该申请单已提交送审，不可以删除！");
					    	return;
					    }else if($(this).parent().next().attr("value")=="2"){
					    	temp=false;
					    	alert("该申请单已审核完毕，不可以删除！");
					    	return;
					    }
					});
					if(temp){
						 if(confirm("您确定要删除吗？")){
					    	location="<%=path%>/attendance/apply/apply!delete.action?ids="+id;	
					     }	
				    }
		    	}
		    });
		
		}
		//查看销假列表
		function canclelist(){
			var id=getValue();
			  $.post("<%=root%>/attendance/apply/apply!isPerson.action",
				{},
				function(data){
				    if(data=="0"){
				        alert("您无权操作，请于考勤管理员联系！");
				        return;
				    }else{
						window.location="<%=path%>/attendance/apply/apply!canclelist.action?ids="+id;
					}
				}
				);
		}
		$(document).ready(function(){
	        $("#img_sousuo").click(function(){
	      
	        	$("form").submit();
	        });     
		 });
		
		
		//请假单送审
		function dotowork(){
		  var bussinessId = getValue();
	      $.post("<%=root%>/attendance/apply/apply!isPerson.action",
			{"bussinessId":bussinessId},
			function(data){
			    if(data=="0"){
			        alert("您无权操作，请于考勤管理员联系！");
			        return;
			    }else if(data=="2"){
			    	alert("该申请单的申请类型不存在！");
			        return;
			    }else if(data=="3"){
			    	alert("该申请单的申请类型已禁用！");
			        return;
			    }else{
			    	 if(bussinessId == ""){
						 	alert("请选择要送审的请假单！");
						 	return ;
					  }else{
						 	var docIds = bussinessId.split(",");
						 	if(docIds.length>1){
						 		alert("一次只能送审一份请假单！");
						 		return ;
						 	}
					  }
				  	var state=$(":checked").parent().next().attr("value");
					if(state=="1"){
						alert("该申请单已提交送审，不可以再次送审！");
						return;
					}else if(state=="2"){
						alert("该申请单已审核完毕，不需要再次送审！");
						return;
					}
				   var info = getInfo();
				   var formId = info[0];
				   var businessName = info[1];
				   bussinessId = "T_OA_ATTEN_APPLY;APPLY_ID;"+bussinessId;
				   //改为直接提交到流程
				   var contextPath = "<%=root%>/attendance/apply/apply";
				   var url="<%=path%>/attendance/apply/apply!wizard.action?bussinessId="+bussinessId+"&fromPath="+contextPath+"&formId="+formId+"&businessName="+encodeURI(encodeURI(businessName));
				   var ret = OpenWindow(url, 550, 500, window);
				   if(ret){
					  	if(ret == "OK"){
					  		alert("发送成功！");
					  		window.location = "<%=root%>/attendance/apply/apply.action?applyId="+bussinessId+"&apptype=apply";
					  	}else if(ret == "NO"){
					  		alert("发送失败！");
					  	}
				   }
		  		}
		  });
		}
		
		function viewDesc(value){
			if(value!=null&&value!="null"&&value!="")
				alert(value);
		}
		 //得到列表信息
		      function getInfo(){
		      	var info = new Array();
		      	var id = getValue();
		      	<s:iterator value="page.result">
		      		if(id == '${applyId}'){
		      			info[0] = '${applyFormId}';
		      			info[1] = '${applicants}'+'申请'+'${applyTypeName}';
		      		}
		      	</s:iterator>
		      	return info;
		      }
</script>
	</BODY>
</HTML>
