<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>请假信息列表</title>
		<META http-equiv=Content-Type content="text/html; charset=utf-8">
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
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

		<style media="screen" type="text/css">
.tabletitle {
	FILTER: progid :                          
		    DXImageTransform.Microsoft.Gradient (      	 		      
	  gradientType =     0, startColorStr =      #ededed, endColorStr =   
		 #ffffff );
}

.hand {
	cursor: pointer;
}
</style>
<style>
.btn1_mouseout {
	BORDER-RIGHT: #7b9ebd 1px solid;
	PADDING-RIGHT: 2px;
	BORDER-TOP: #7b9ebd 1px solid;
	PADDING-LEFT: 2px;
	FONT-SIZE: 12px;
	FILTER: progid :                         
		   DXImageTransform.Microsoft.Gradient (        
		       GradientType =     0, StartColorStr =      #ffffff, EndColorStr
		=   
		 #cecfde );
	BORDER-LEFT: #7b9ebd 1px solid;
	CURSOR: hand;
	COLOR: black;
	PADDING-TOP: 2px;
	BORDER-BOTTOM: #7b9ebd 1px solid
}

.btn1_mouseover {
	BORDER-RIGHT: #FF0000 1px solid;
	PADDING-RIGHT: 2px;
	BORDER-TOP: #FF0000 1px solid;
	PADDING-LEFT: 2px;
	FONT-SIZE: 12px;
	FILTER: progid :                         
		   DXImageTransform.Microsoft.Gradient (     
		            GradientType =     0, StartColorStr =      #ffffff,
		EndColorStr =      #CAE4B6 );
	BORDER-LEFT: #FF0000 1px solid;
	CURSOR: hand;
	COLOR: black;
	PADDING-TOP: 2px;
	BORDER-BOTTOM: #FF0000 1px solid
}
</style>

	</head>
	<body class=contentbodymargin oncontextmenu="return false;"
		onload="initMenuT();">
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
			<s:form theme="simple" id="myTableForm"
				action="/leave/leave!content.action">
				<input type="hidden" name="conId" id="conId" value="${conId}">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td height="100%">
							<table width="100%" border="0" cellspacing="0" cellpadding="00">
								<tr>
									<td height="40"
										style="FILTER: progid :   DXImageTransform.Microsoft.Gradient ( gradientType =   0, startColorStr = #ededed, endColorStr =   #ffffff );">
										<table width="100%" border="0" cellspacing="0"
											cellpadding="00">
											<tr>
												<td>
													&nbsp;
												</td>
												<td width="45%">
													<img
														src="<%=frameroot%>/images/perspective_leftside/ico.gif"
														width="9" height="9" align="center">
													&nbsp; 请假信息列表
												</td>
												<td width="*">
													&nbsp;
												</td>
												<td width="70%">
													<table align="right">
														<tr>
															<td class="dshowxld">
																<a class="Operation" href="javascript:qj();"><img
																		src="<%=root%>/images/ico/tb-add.gif" width="15"
																		height="15" class="img_s">请假&nbsp;</a>
															</td>
															<td width="5"></td>
															<td>
																<a class="Operation" href="#" onclick="xj()"> <img
																		src="<%=root%>/images/ico/shanchu.gif" width="15"
																		height="15" class="img_s"><span id="test"
																		style="cursor: hand">销假&nbsp;</span> </a>
															</td>
															<td width="5"></td>
															<td class="dshowxld">
																<a class="Operation" href="javascript:ckqj();"><img
																		src="<%=root%>/images/ico/bianji.gif" width="15"
																		height="15" class="img_s">编辑&nbsp;</a>
															</td>
															<td width="5"></td>
														</tr>
													</table>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
							<webflex:flexTable name="myTable" width="100%" height="370px"
								wholeCss="table1" property="id" isCanDrag="true"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByProperty" 
								pagename="personpage"
								collection="${personpage.result}"
								page="${personpage}">
								<table width="100%" border="0" cellpadding="0" cellspacing="1"
									class="table1">
									<tr>																											
										<td width="15%" align="center" class="biao_bg1">
											<s:textfield name="model.conferee.personName" cssClass="search" title="姓名"
												maxlength="10"></s:textfield>
										</td>
										<td width="10%" align="center" class="biao_bg1">
											<s:select name="model.conferee.personSax"
												list="#{'':'所有类型','男':'男','女':'女'}" listKey="key"
												listValue="value" style="width:100%"
												onChange='$("#myTableForm").submit();' />
										</td>
										<td width="10%" align="center" class="biao_bg1">
											<s:textfield name="model.conferee.personNation" cssClass="search"
												title="民族" maxlength="50"></s:textfield>
										</td>
										
										<td width="15%" align="center" class="biao_bg1">
											<s:textfield name="model.conferee.personPost" cssClass="search"
												title="职务" maxlength="50"></s:textfield>
										</td>
										
										<td width="25%" align="center" class="biao_bg1">
											<s:textfield name="model.conferee.personMobile" cssClass="search"
												title="手机 号码" maxlength="50"></s:textfield>
										</td>
										
										<td width="25%" align="center" class="biao_bg1">
											<s:textfield name="model.conferee.personPhone" cssClass="search"
												title="办公电话" maxlength="50"></s:textfield>
										</td>
										
										<td width="10%" align="center" class="biao_bg1">
											<s:select name="state"
												list="#{'':'所有类型','2':'请假','3':'销假'}" listKey="key"
												listValue="value" style="width:100%"
												onChange='$("#myTableForm").submit();' />
										</td>
					
										<td width="5%" align="center" class="biao_bg1">
											<input type='button' id="img_sousuo" class=btn1_mouseout
												onmouseover="this.className='btn1_mouseover'"
												onmouseout="this.className='btn1_mouseout'" value='搜 索' />
										</td>
									</tr>
								</table>
								<webflex:flexCheckBoxCol caption="选择" property="reportingId"
									showValue="reportingId" width="5%" isCheckAll="true"
									isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>								
								<webflex:flexTextCol caption="姓名" property="conferee.personName"
									showValue="conferee.personName" width="10%" isCanDrag="true"
									isCanSort="false"></webflex:flexTextCol>
								<webflex:flexTextCol caption="性别" property="conferee.personSax"
									showValue="conferee.personSax" width="5%" isCanDrag="true"
									isCanSort="false"></webflex:flexTextCol>									
								<webflex:flexTextCol caption="民族" property="conferee.personNation"
									showValue="conferee.personNation" width="10%" isCanDrag="true"
									isCanSort="false"></webflex:flexTextCol>
								<webflex:flexTextCol caption="职务" property="conferee.personPost"
									showValue="conferee.personPost" width="25%" isCanDrag="true"
									isCanSort="false"></webflex:flexTextCol>
								<webflex:flexTextCol caption="手机号" property="conferee.personMobile"
									showValue="conferee.personMobile" width="20%" isCanDrag="true"
									isCanSort="false"></webflex:flexTextCol>
								<webflex:flexTextCol caption="办公电话" property="conferee.personPhone"
									showValue="conferee.personPhone" width="15%" isCanDrag="true"
									isCanSort="false"></webflex:flexTextCol>
								<webflex:flexEnumCol caption="当前状态" mapobj="${typeMap}" 
									property="reportingId" showValue="reportingId" width="10%"
									isCanDrag="true" isCanSort="false"></webflex:flexEnumCol>
						
							</webflex:flexTable>
						</td>
					</tr>
				</table>
			</s:form>
		</DIV>
	</body>
		<script language="javascript">	    
	      var sMenu = new Menu();
	      function initMenuT(){
	        sMenu.registerToDoc(sMenu);
	        var item = null;
	        item = new MenuItem("<%=root%>/images/ico/tb-add.gif","请假","qj",1,"ChangeWidthTable","checkOneDis");
	        sMenu.addItem(item);
			item = new MenuItem("<%=root%>/images/ico/shanchu.gif","销假","xj",1,"ChangeWidthTable","checkOneDis");
	        sMenu.addItem(item);
	        item = new MenuItem("<%=root%>/images/ico/bianji.gif","编辑","ckqj",1,"ChangeWidthTable","checkOneDis");
	        sMenu.addItem(item);
	        sMenu.addShowType("ChangeWidthTable");
	        registerMenu(sMenu);
		}	
		
		//请假
		function qj(){
		   	var id = getValue();
			if(id==null || id==""){
				alert("请选择一条记录！");
				return;
			}
			if(id.length >32){
				alert("一次只能选择一条记录！");
				return;
			}
			var width=screen.availWidth*2/3;
			//var width=800;
    		var height=screen.availHeight*3/5;  
    		//var height=400; 
    		$.post(
				"<%=root%>/leave/leave!isLeave.action", {
				"reportingId" : id
			}, function(data) {
				var s=data.split("@");
				if(s[0]=="OKQJ"){
					var ret = showDialog("<%=root%>/leave/leave!leaveInput.action?reportingId="+id, width, height);   	  	
					if(ret && ret == 'OK'){
						$("form").submit();
						alert(s[1]+"请假成功！");
		   		 	}
					
				}else{
					alert(s[1]+"已经请假！");
					return false;
				}
			});
		}
		
		//查看请假
		function ckqj(){
		   	var id = getValue();
			if(id==null || id==""){
				alert("请选择一条记录！");
				return;
			}
			if(id.length >32){
				alert("一次只能选择一条记录！");
				return;
			}
			var width=screen.availWidth*2/3;
			//var width=800;
    		var height=screen.availHeight*3/5;  
    		//var height=400; 
    		$.post(
				"<%=root%>/leave/leave!isLeave.action", {
				"reportingId" : id
			}, function(data) {
				var s=data.split("@");
				if(s[0]=="OKQJ"){
					alert(s[1]+"没有请假！");
					return false;
				}else{
					var ret = showDialog("<%=root%>/leave/leave!leaveEdit.action?reportingId="+id, width, height);   
					if(ret && ret == 'OK'){
						$("form").submit();
						alert(s[1]+"请假修改成功！");
		    		}
					
				}
			});
		}
		
		//销假
		function xj(){
			var id = getValue();
			if(id==null || id==""){
				alert("请选择一条记录！");
				return;
			}
			if(id.length >32){
				alert("一次只能选择一条记录！");
				return;
			}
			var width=screen.availWidth*2/3;
			//var width=800;
    		var height=screen.availHeight*3/5;  
    		//var height=400;  
    		$.post(
				"<%=root%>/leave/leave!isLeave.action", {
				"reportingId" : id
			}, function(data) {
				var s=data.split("@");
				if(s[0]=="OKXJ"){
					$.post(
						"<%=root%>/leave/leave!cancleLeave.action", {
						"reportingId" : id,
						"state":"3"
					}, function(data) {
						$("form").submit();
						alert(s[1]+"销假成功！");
					});
				}else{
					if(s[2]=="3"){
						alert(s[1]+"已经销假！");
						return false;
					}else{
						alert(s[1]+"还没有请假，不能进行销假操作！");
						return false;
					}
				}
			});
			
		}
				
		function del(){
			var id = getValue();
			if(id==null || id==""){
				alert("请选择需要删除的记录！");
				return;
			}
			if(confirm("您确定要删除吗？")){
				$.post(
						"<%=root%>/seatset/property/property!delete.action", {
				"capId" : id
			}, function(data) {
				if(data=='0'){
				 $("form").submit();
				}else{
                    alert('删除失败，请检查约束！');				
				}
			});
		}
	}
		
	function callback(){
   		window.location.href = window.location.href;
	}

	$(document).ready(function() {
		$("#img_sousuo").click(function() {
			$("#myTableForm").submit();
		});
	});
</script>

</html>
