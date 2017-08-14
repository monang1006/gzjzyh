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
	
	function add(){
		var width = screen.width;
		var height = screen.height;
		var a = WindowOpen("<%=path%>/eformManager/eformManager!formdesigner.action?operating=add","add",width,height,"新建表单");
		if(a=="reload"){
			window.location.reload();
		}
	}
	
	function edit(id){
		if(!id){
			id = getValue();
		}
		if(id==null|id==""){
			alert("请选择要编辑的表单!");
			return;
		}
		if(id.indexOf(",")>0){
			alert("一次只能编辑一个表单！");
			return;
		}
	    var width = screen.width;
		var height = screen.height;           
		var a = WindowOpen("<%=path%>/eformManager/eformManager!formdesigner.action?operating=edit&id="+id,"edit",width,height,"编辑表单");
		a.focus();
		if(a=="reload"){
			window.location.reload();
		}		
	}
	
	//数据同步   对使用的表单中的信息集和信息项，增加到中间表中，并添加
	function data(){
		if(!confirm("表单数据同步可能需要花费一段时间，确定要继续操作吗？")){
			return;
		}
		show("同步表单数据使用记录,请耐心等待...");
			$.post("<%=path%>/eformManager/eformManager!dataEForm.action",
				function(data){
					if(data == "ok"){
						show("从表单管理向表单使用记录同步数据完成!");
						setTimeout("hidden()", 2000);
<%--						setTimeout("over()", 2000);--%>
					}else{
						show("从表单管理向表单使用记录同步数据失败!请重试或与管理员联系!");
						setTimeout("hidden()", 2000);
					}
					
				});		
	}
	
	function del(){
		var id = getValue();
		if(id==null|id==""){
			alert("请选择要删除的表单!");
			return;
		}
		if(id.indexOf(",")>0){
			alert("一次只能删除一个表单！");
			return;
		}
		
		if(id=="101"){
			alert("不允许删除公文下发单！");
			return;
		}
		var url = "<%=path%>/eformManager/eformManager!delete.action";
		if(confirm("确定要删除选中表单吗?")){
				$.ajax({
					type:"post",
					url:url,
					data:{
						id:id
					},
					success:function(data){
							if(data!="" && data!=null){
								alert(data);					
							}else{
								location.reload() ;
							}
						},
					error:function(data){
						alert("对不起，操作异常"+data);
					}
			   });
			}
	}
	function view(){
		var id = getValue();
		if(""==id|null==id){
			alert("请选择一个表单!");
			return;
		}
		if(id.indexOf(",")>0){
			alert("一次只能查看一个表单！");
			return;
		}
		//var a = OpenWindow("<%=path%>/eformManager/eformManager!view.action?id="+id,700,600);
		var width = screen.width-10;
		var height = screen.height-10;  
		var a = OpenWindow("<%=path%>/eformManager/eformManager!formdesigner.action?title="+title,width,height);
	}
	
	function clickTitle(id){
		edit(id);
	}
	
	//根据orgcode显示是否全局表单
	function showValue(val){
		if(val=="0"){
			return "全局表单";
		}else{
			return "非全局表单";
		}
	}
	
	
	//修改选择表单是否全局表单
	function conf(){
		var id = getValue();
		if(""==id|null==id){
			alert("请选择一个表单!");
			return;
		}
		if(id.indexOf(",")>0){
			alert("一次只能设置一个表单！");
			return;
		}
		var orgcode= $("#chkButton"+id).parent().next().next().next().next().next().val();
		var tempAlert = "当前选择表单为全局表单，是否取消其全局属性，设置为仅当前用户所在机构及其父机构可用的表单？";
		if("0"!=orgcode){
			tempAlert = "当前选择表单为非全局表单，是否设置其全局属性，设置为本系统所有用户的可用表单？";
		}
		
		var url = "<%=path%>/eformManager/eformManager!conf.action";
		if(confirm(tempAlert)){
				$.ajax({
					type:"post",
					url:url,
					data:{
						orgCode:orgcode,
						id:id
					},
					success:function(data){
							if(data!="" && data!=null){
								alert(data);					
							}else{
								location.reload() ;
							}
						},
					error:function(data){
						alert("对不起，操作异常"+data);
					}
			   });
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
            	<strong>电子表单列表</strong>
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
               		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
                 	<td class="Operation_list" onclick="conf();"><img src="<%=root%>/images/operationbtn/Shear.png"/>&nbsp;表单全局设置&nbsp;</td>
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
	<s:form id="myTableForm" action="/eformManager/eformManager.action" method="get">
    	 <%--<input type="hidden" name="folderId" id="folderId" value="${folderId }">
    	 <input type="hidden" name="type" id="type" value="${returnType }">
    	 <input type="hidden" name="beginDate" id="beginDate" value="${firstDate }">
    	 <input type="hidden" name="endDate" id="endDate" value="${otherDate }">
	     --%>
    	 <input type="hidden" name="model.title" id="title" value="${model.title}">
	     <webflex:flexTable name="myTable" width="100%" height="100%" wholeCss="table1" property="0" isCanDrag="true" isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck" getValueType="getValueByArray" collection="${page.result}" page="${page}">
			<table width="100%" border="0" cellpadding="0" cellspacing="1" class="table1_search">
			     <tr>
			     	<td>
			       		&nbsp;&nbsp;表单名称：&nbsp;<input name="titleTEXT" id="titleTEXT" type="text" class="search"  title="请您输入表单名称" value="${title}">
			       		&nbsp;&nbsp;表单类型：&nbsp;<s:select name="model.type"  list="#{'':'全部','SF':'启动表单','QF':'查询表单','VF':'展现表单'}" listKey="key" listValue="value" onchange='$("#img_sousuo").click();'/>
			       		&nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo" type="button" />
			       	</td>
			       <%--
			       <td width="30%" align="center" class="biao_bg1"><strong:newdate  name="starttime" id="starttime" width="100%" skin="whyGreen" isicon="true"  classtyle="search" title="请输入起始日期"/></td>
			       <td width="20%" align="center" class="biao_bg1"><strong:newdate  name="endtime" id="endtime" width="100%" skin="whyGreen" isicon="true"  classtyle="search" title="请输入截止日期"/></td>
			       --%>
			     </tr>
			</table> 
			
			<webflex:flexCheckBoxCol caption="选择" isCheckAll="true" valuepos="0" valueshowpos="1" width="5%"isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
			<%--
			<webflex:flexRadioCol caption="选择" valuepos="0" valueshowpos="1" width="5%"isCanDrag="false" isCanSort="false"></webflex:flexRadioCol>
			--%>
			<webflex:flexTextCol caption="表单ID" align="center" valuepos="0" valueshowpos="0" width="10%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
			<webflex:flexTextCol caption="表单名称" valuepos="0" valueshowpos="1" width="40%" isCanDrag="true" isCanSort="true" onclick="clickTitle(this.value)" showsize="34"></webflex:flexTextCol>
			<webflex:flexDateCol caption="添加时间" valuepos="3" valueshowpos="3" width="25%" isCanDrag="true" isCanSort="true" dateFormat="yyyy-MM-dd HH:mm" showsize="16"></webflex:flexDateCol>
			<webflex:flexEnumCol caption="表单类型" mapobj="${typemap}" valuepos="2"  align="center"
								valueshowpos="2" width="10%" isCanDrag="true" isCanSort="true"></webflex:flexEnumCol>
			<webflex:flexTextCol caption="是否全局表单" align="center" valuepos="7" valueshowpos="javascript:showValue(7)" width="10%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
			
			<%--
			<webflex:flexTextCol caption="添加人"  valuepos="2" valueshowpos="2" width="15%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
			<webflex:flexTextCol caption="添加人"  valuepos="4" valueshowpos="4" width="15%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
			<webflex:flexTextCol caption="附件" valuepos="3" valueshowpos="3" width="5%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
			--%>
		</webflex:flexTable>
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
	//item = new MenuItem("<%=root%>/images/ico/chakan.gif","查看","view",1,"ChangeWidthTable","checkOneDis");
	//sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/operationbtn/add.png","新建","add",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/operationbtn/edit.png","编辑","edit",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/operationbtn/del.png","删除","del",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/operationbtn/Shear.png","表单全局设置","conf",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}
</script>
</BODY></HTML>
