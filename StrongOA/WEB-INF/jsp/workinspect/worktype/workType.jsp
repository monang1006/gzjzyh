<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>操作内容</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<!--右键菜单样式 -->
		<LINK type=text/css rel=stylesheet href="<%=frameroot%>/css/properties_windows.css">
		<LINK type=text/css rel=stylesheet
			href="<%=path%>/common/css/gd.css">
		<LINK type=text/css rel=stylesheet href="<%=frameroot%>/css/search.css">
		<script language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script language="javascript" src="<%=path%>/common/js/common/search.js"></script>
		<SCRIPT language="javascript" type="text/javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT><!--右键菜单脚本 -->
		<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js' type="text/javascript"></script>
		<script language="javascript" src="<%=path%>/common/js/common/common.js"></script>
			<SCRIPT LANGUAGE="JavaScript" src="<%=path%>/common/js/commontab/service.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></SCRIPT>
		<script type="text/javascript">
	/*
	*添加工作分类
	*/
	function add(){
	  var ret = window.showModalDialog("<%=path%>/workinspect/worktype/workType!input.action",window,'help:no;status:no;scroll:no;dialogWidth:600px; dialogHeight:250px');
	  if( ret && ret=="OK"){
		  document.myTableForm.submit();
	   }
	}
	
	/*
	*编辑工作分类
	*/
	function edit(){
			var id=getValue();
			if(id==null || id==""){
				alert("请选择需要修改的记录！");
				return;
			}
			if(id.length>32){
				alert("只能修改一条记录!");
				return;
			}	
			 var ret =window.showModalDialog("<%=path%>/workinspect/worktype/workType!input.action?worktypeId="+id,window,'help:no;status:no;scroll:no;dialogWidth:600px; dialogHeight:350px');
			 if( ret && ret=="OK"){
				  document.myTableForm.submit();
			   }
	}
	/*
	*删除工作分类
	*/
	function del(){
		var id=getValue();
		if(id==null || id==""){
			alert("请选择需要删除的记录！");
			return;
		}
		if(id.length>32){
			alert("只能删除一条记录!");
			return;
		}
		if(confirm("是否确定要删除!")){
			location='<%=path%>/workinspect/worktype/workType!delete.action?worktypeId='+id;
		}else{
			return;
		}
	}
	function getListBySta(){	//根据属性查询
		//document.getElementById("disLogo").value="search";
		document.getElementById("myTableForm").submit();
	}
		
</script>
	</HEAD>
	<BODY class=contentbodymargin oncontextmenu="return false;"
		onload="initMenuT()">
		<div class="gd_name"><div class="gd_name_left">
		<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">
												&nbsp;工作分类</div>
		<div class="gd_name_right" style="margin-bottom: 5px">
			<input name="" type="button" class="gd_add2" value="添加" onclick="add();"/>
			<input name="" type="button" class="gd_qs" value="编辑" onclick="edit();"/>
			<input name="" type="button" class="gd_delete" value="删除" onclick="del();"/>
		</div>
		<br style="clear:both;"/>
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="100%">
						<s:form theme="simple" id="myTableForm" name="myTableForm"
							action="">
							<webflex:flexTable name="myTable" width="100%" height="370px"
								wholeCss="table1" property="worktypeId" isCanDrag="true"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByProperty" collection="${page.result}"
								page="${page}">
								<table width="100%" border="0" cellpadding="0" cellspacing="1"
									class="table1">
									<tr>
										<td width="3%" align="center" class="biao_bg1">
											<img src="<%=root%>/images/ico/sousuo.gif" width="17"
												height="16" onclick="getListBySta()" style="cursor: hand;">
										</td>
										<td width="35%" align="center" class="biao_bg1">
											<input id="worktypeName" name="model.worktypeName" type="text"
												style="width:100%" maxlength="25" value="${model.worktypeName}" class="search" title="请输入名称">
										</td>
										<td width="30%" align="center" class="biao_bg1">
											<input id="worktypeDemo" name="model.worktypeDemo"
												type="text" style="width:100%"
												value="${model.worktypeDemo}" class="search" maxlength="10" title="请输入备注">
										</td>
										<td width="32%" align="center" class="biao_bg1">
										</td>
									</tr>
								</table>
								<webflex:flexCheckBoxCol caption="选择" property="worktypeId"
									showValue="worktypeValue" width="5%" isCheckAll="true"
									isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								
								<webflex:flexTextCol caption="名称" property="worktypeName" 
									showValue="worktypeName" width="35%" isCanDrag="true"  showsize="15" 
									isCanSort="false"></webflex:flexTextCol>
								<webflex:flexTextCol caption="备注" property="worktypeDemo" 
									showValue="worktypeDemo" width="30%" isCanDrag="true" showsize="30"
									isCanSort="false"></webflex:flexTextCol>
								<webflex:flexTextCol caption="排序" property="worktypeSequence" 
									showValue="worktypeSequence" width="30%" isCanDrag="true" showsize="30" 
									isCanSort="false"></webflex:flexTextCol>
							</webflex:flexTable>
						</s:form>
					</td>
				</tr>
			</table>
		</DIV>
		</div>
		<script language="javascript">
         var sMenu = new Menu();
          function initMenuT(){
	        sMenu.registerToDoc(sMenu);
	        var item = null;
	        item = new MenuItem("<%=root%>/images/ico/gd_tj.gif","添加","add",1,"ChangeWidthTable","checkMoreDis");
	        sMenu.addItem(item);
	        item = new MenuItem("<%=root%>/images/ico/gd_bj.gif","编辑","edit",1,"ChangeWidthTable","checkOneDis");
	        sMenu.addItem(item);
	        item = new MenuItem("<%=root%>/images/ico/gd_sc.gif","删除","del",1,"ChangeWidthTable","checkOneDis");
	        sMenu.addItem(item);
	        sMenu.addShowType("ChangeWidthTable");
            registerMenu(sMenu);
         }
</script>
	</BODY>
</HTML>
