<HTML><HEAD><TITLE>电子表单列表</TITLE>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@include file="/common/include/meta.jsp" %>
<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet"><!--右键菜单样式 -->
<LINK href="<%=frameroot%>/css/properties_windows_list.css" type=text/css  rel=stylesheet>
<LINK href="<%=frameroot%>/css/search.css" type=text/css  rel=stylesheet>
<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT><!--右键菜单脚本 -->
<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
<script src="<%=path%>/common/js/upload/jquery.blockUI.js" language="javascript"></script>
<script src="<%=path%>/common/js/validate/checkboxvalidate.js" type="text/javascript"></script>
<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
<script src="<%=path%>/common/js/common/search.js" type="text/javascript"></script>
<script type="text/javascript">
	function show(i){
				$.blockUI({ overlayCSS: { backgroundColor: '#B3B3B3' }});
				$.blockUI({ message: '<font color="#008000"><b>'+i+'</b></font>' });
			}
			function hidden(){
				$.unblockUI();
			}
	
	$(document).ready(function(){
		//搜索功能
		$("#img_sousuo").click(function(){
			$("#title").val(encodeURI($("#titleTEXT").val()));
			$("form").submit();
		});
		//获取焦点
		$("input").focus();
	});
	//新建表单
	function add(){
		var a = OpenWindow("<%=path%>/eformJspManager/eformJspManager!input.action",400,150);
		if(a=="reload"){
			window.location=window.location;
		}
	}
	
	//编辑表单
	function edit(){
		var id = getValue();
		if(id==null||id==""){
			alert("请选择要编辑的记录。");
		}else{
			if(id.indexOf(",")!=-1){
				alert("只可以编辑一条记录。");
			}else{
				var a = OpenWindow("<%=path%>/eformJspManager/eformJspManager!input.action?id="+id,400,150);
					if(a=="reload"){
						window.location=window.location;
					}
			}
		}
	}
	
	//删除
	function del(){
		var id = getValue();
		var url = "eformJspManager!delete.action";
		if(id==null|id==""){
			alert("请选择要删除的记录。");
		}else{
			if(confirm("确定要删除吗？")){
				$.ajax({
					type:"post",
					url:url,
					data:{
						id:id
					},
					success:function(data){
							if(data=="exit"){
								alert("表单已经被流程引用，不能删除。");					
							}else if(data=="ok"){
								 //alert("删除成功");
								location.reload() ;
							}
						},
					error:function(data){
						alert("对不起，操作异常"+data);
					}
			   });
			}
		}
	}
</script>
</HEAD>
<base target="_self"/>
<BODY class=contentbodymargin oncontextmenu="return false;" onload=initMenuT()>
<DIV id=contentborder align=center>
  <table width="100%" border="0" cellspacing="0" cellpadding="00">
  <tr>
    <td height="100%">
    <table width="100%" border="0" cellspacing="0" cellpadding="00">
      <tr>
        <td height="40" class="table_headtd">
        <table width="100%" border="0" cellspacing="0" cellpadding="00">
          <tr>
            <td class="table_headtd_img" >
				<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
			</td>
            <td align="left">
            	<strong>Jsp表单列表</strong>
            </td>
            <td align="right">
            	<table border="0" align="right" cellpadding="00" cellspacing="0">
                <tr>
                	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
                 	<td class="Operation_list" onclick="add();"><img src="<%=root%>/images/operationbtn/add.png"/>&nbsp;新&nbsp;建&nbsp;</td>
                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
               		<td width="5"></td>
               		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
                 	<td class="Operation_list" onclick="edit();"><img src="<%=root%>/images/operationbtn/edit.png"/>&nbsp;编&nbsp;辑&nbsp;</td>
                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
               		<td width="5"></td>
               		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
                 	<td class="Operation_list" onclick="del();"><img src="<%=root%>/images/operationbtn/del.png"/>&nbsp;删&nbsp;除&nbsp;</td>
                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
               		<td width="5"></td>
                	<!-- <td ><a class="Operation" href="javascript:view();"><img src="<%=root%>/images/ico/chakan.gif" width="14" height="14" class="img_s">查看&nbsp;</a></td>-->
					<!--
					<td >
						<security:authorize ifAllGranted="001-0005000200060001">
							<a class="Operation" href="javascript:data();"><img src="<%=root%>/images/ico/date.gif" width="14" height="14" class="img_s">数据同步&nbsp;</a>						
						</security:authorize>
					</td>
					-->
                </tr>
            </table>
            </td>
          </tr>
        </table>
        </td>
      </tr>
	<s:form id="myTableForm" action="/eformJspManager/eformJspManager.action" method="post">
    	 	<webflex:flexTable name="myTable" width="100%" height="370px"
											wholeCss="table1" property="code" isCanDrag="true"
											isCanFixUpCol="true" clickColor="#A9B2CA"
											footShow="showCheck" getValueType="getValueByProperty"
											collection='${page.result}' page="${page}">
			<table width="100%" border="0" cellpadding="0" cellspacing="1" class="table1_search">
			     <tr>
			     	<td>
			       		&nbsp;&nbsp;表单名称：&nbsp;<input name="name" id="name" type="text" class="search" title="请您输入表单名称"  value="${model.name}">
			       		&nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo" type="button" />
			       	</td>
			       <%--
			       <td width="30%" align="center" class="biao_bg1"><strong:newdate  name="starttime" id="starttime" width="100%" skin="whyGreen" isicon="true"  classtyle="search" title="请输入起始日期"/></td>
			       <td width="20%" align="center" class="biao_bg1"><strong:newdate  name="endtime" id="endtime" width="100%" skin="whyGreen" isicon="true"  classtyle="search" title="请输入截止日期"/></td>
			       --%>
			     </tr>
			</table> 
				<webflex:flexCheckBoxCol caption="选择" property="id" 
								showValue="id" width="4%" isCheckAll="true" isCanDrag="false"
								isCanSort="false"></webflex:flexCheckBoxCol>
							<webflex:flexTextCol caption="表单ID" property="id" 
								showValue="id" align="center" width="10%" isCanDrag="true" isCanSort="true" ></webflex:flexTextCol>
							<webflex:flexTextCol caption="表单名称" property="name" align="left"
								showValue="name" width="43%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="URL配置地址" property="url" showValue="url" width="30%" isCanDrag="true" isCanSort="true"  showsize="34"></webflex:flexTextCol>
							<webflex:flexDateCol caption="最后修改时间" property="modifyTime" showValue="modifyTime"  width="13%" isCanDrag="true" isCanSort="true" dateFormat="yyyy-MM-dd HH:mm" showsize="16"></webflex:flexDateCol>
						</webflex:flexTable>
<%--			<webflex:flexCheckBoxCol caption="选择" isCheckAll="true" valuepos="0" valueshowpos="1" width="5%"isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>--%>
<%--			<webflex:flexTextCol caption="表单ID" valuepos="0" valueshowpos="0" width="10%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>--%>
<%--			<webflex:flexTextCol caption="表单名称" valuepos="0" valueshowpos="1" width="50%" isCanDrag="true" isCanSort="true" onclick="clickTitle(this.value)" showsize="34"></webflex:flexTextCol>--%>
<%--			<webflex:flexDateCol caption="最后修改时间" valuepos="3" valueshowpos="3" width="25%" isCanDrag="true" isCanSort="true" dateFormat="yyyy-MM-dd HH:mm" showsize="16"></webflex:flexDateCol>--%>
<%--			<webflex:flexTextCol caption="表单名称" valuepos="0" valueshowpos="1" width="25%" isCanDrag="true" isCanSort="true" onclick="clickTitle(this.value)" showsize="34"></webflex:flexTextCol>--%>
	</s:form>
	</table>
      </td>
  </tr>
</table>
</DIV>
<script language="javascript">
var sMenu = new Menu();
function initMenuT(){
	$("#title").val(encodeURI($("#titleTEXT").val()));
	sMenu.registerToDoc(sMenu);
	var item = null;
	item = new MenuItem("<%=root%>/images/operationbtn/add.png","新建","add",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/operationbtn/edit.png","编辑","edit",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/operationbtn/del.png","删除","del",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}
</script>
</BODY></HTML>
