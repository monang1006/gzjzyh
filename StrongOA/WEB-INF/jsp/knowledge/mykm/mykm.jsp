<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>

<HTML>
	<HEAD>
		<TITLE>我的收藏列表</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet"><!--右键菜单样式 -->
		<LINK href="<%=frameroot%>/css/properties_windows_list.css" type=text/css rel=stylesheet>
		<script language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
<%--		<script language="javascript" src="<%=path%>/common/js/common/search.js"></script>--%>
		<LINK type=text/css rel=stylesheet href="<%=frameroot%>/css/search.css">
		<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT><!--右键菜单脚本 -->
		<script language="JavaScript" src="<%=path%>/common/js/commontab/service.js"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></script>
        <script type="text/javascript">
          function gowai(id,name){//连接到外部网络
            //top.perspective_content.actions_container.personal_properties_toolbar.navigate(value,name);	
            //getSysConsole().navigate(value,name);
            getSysConsole().navigate("<%=root%>/knowledge/mykm/mykm!show.action?mykmId="+id,"查看信息");
           }
          function showarticles(value,name){//显示知识信息
              if(value==null||value==""){
                  value=getValue();
              }
               //top.perspective_content.actions_container.personal_properties_toolbar.navigate("<%=root%>/knowledge/mykm/mykm!show.action?mykmId="+value,name);
               getSysConsole().navigate("<%=root%>/knowledge/mykm/mykm!show.action?mykmId="+value,name);
          }
          function showtitle(mykmname,id,url){//列表标题显示
             var obj='';
             if(!url==''&&url.length>4)//判断路径是否为空
             {
        
               obj=url.substring(0,4);
             }
             var rv='';
             //判断收藏的是否是外部信息
             if(obj=='http'){
             //rv="<a href=javascript:gowai('"+url+"','"+mykmname+"') style=\"color: blue\">"+mykmname+"</a>";
             rv="<a href=javascript:gowai('"+id+"','"+mykmname+"') style=\"color: blue\">"+mykmname+"</a>";
             }else if(obj==''){
             rv="<a href=javascript:alert('没有文章可供查看！') style=\"color: blue\">"+mykmname+"</a>"
             }else{
             rv="<a href=javascript:showarticles('"+id+"','"+mykmname+"') style=\"color: blue\">"+mykmname+"</a>";
             }
             return rv;
          }  
       </script>
</HEAD>
<BODY class=contentbodymargin oncontextmenu="return false;" onLoad="initMenuT()">
	<script language="javascript" type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
<DIV id=contentborder align=center>
  <table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
  <tr>
    <td height="100%">
 <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="40" style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
        <table width="100%" border="0" cellspacing="0" cellpadding="00">
          <tr>
            <td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
								<strong>我的收藏列表</strong>
								 <s:if test="mykmsort!=null">
           						 --${mykmsort.mykmSortName }
          						  </s:if>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
						            <tr>
						            	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="showinfo();"><img src="<%=root%>/images/operationbtn/consult.png"/>&nbsp;查&nbsp;阅&nbsp;</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
				                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="shore();"><img src="<%=root%>/images/operationbtn/share.png"/>&nbsp;分&nbsp;享&nbsp;</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
				                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="addMykm();"><img src="<%=root%>/images/operationbtn/add.png"/>&nbsp;添&nbsp;加&nbsp;</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
				                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="editMykm();"><img src="<%=root%>/images/operationbtn/edit.png"/>&nbsp;编&nbsp;辑&nbsp;</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
				                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="del();"><img src="<%=root%>/images/operationbtn/del.png"/>&nbsp;删&nbsp;除&nbsp;</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
				                  		<td width="2%"></td>
					                 	<%--<security:authorize ifAllGranted="001-0003000600020004">
					                 	<td width="50"><a class="Operation" href="javascript:searchNotify();"><img src="<%=root%>/images/ico/sousuo.gif" width="15" height="15" class="img_s">查找</a></td>
					                 	<td width="5"></td>
					                 	</security:authorize>
					                --%></tr>
					            </table>
							</td>
						</tr>
					</table>
				</td>
          <tr>
          		<td>
          			<s:form theme="simple" id="myTableForm" action="/knowledge/mykm/mykm.action"   >
          			<webflex:flexTable name="myTable" width="100%" height="365px" wholeCss="table1" property="mykmId" isCanDrag="true"  isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck" getValueType="getValueByProperty" collection="${page.result}" page="${page}">
					        <table width="100%" border="0" cellpadding="0" cellspacing="0" class="table1_search">
							     <tr>
							       <td>
							       		&nbsp;&nbsp;标题：&nbsp;<input name="mykmName" id="mykmName" type="text" class="search" title="请您输入标题" value="${model.mykmName }">
							       		&nbsp;&nbsp;类型：&nbsp;<s:select id="mykmSortId" name="mykmSortId"  list="sortList" listKey="mykmSortId" listValue="mykmSortName" onchange='$("#img_sousuo").click();'/>
							       		&nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo" type="button" />
							       	</td>
							     </tr>
							</table> 
		<webflex:flexCheckBoxCol caption="选择" property="mykmId" showValue="mykmName" width="5%" isCheckAll="true" isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
		<webflex:flexTextCol caption="标题" property="mykmId" showValue="javascript:showtitle(mykmName,mykmId,mykmUrl)" width="40%" isCanDrag="true" isCanSort="true" showsize="15" ></webflex:flexTextCol>
		<webflex:flexTextCol caption="作者" onclick="" property="mykmAuthor" showValue="mykmAuthor" width="15%" isCanDrag="true" isCanSort="true" showsize="15" ></webflex:flexTextCol>
		<webflex:flexTextCol caption="来源" onclick="" property="mykmSource" showValue="mykmSource" width="15%" isCanDrag="true" isCanSort="true" showsize="15" ></webflex:flexTextCol>
	    <webflex:flexDateCol caption="日期" property="mykmDate" showValue="mykmDate" dateFormat="yyyy-MM-dd" width="30%" isCanDrag="true" isCanSort="true"></webflex:flexDateCol>
	</webflex:flexTable>
      </td>
  </tr>
  
</table>
  </s:form>
</DIV>
<!--郑志斌 2010-04-07  为右菜单添加“添加”按键 -->
<script language="javascript">
var sMenu = new Menu();
function initMenuT(){
	sMenu.registerToDoc(sMenu);
	var item = null;
	item = new MenuItem("<%=root%>/images/operationbtn/consult.png","查阅","showinfo",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/operationbtn/share.png","分享","shore",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/operationbtn/add.png","添加","addMykm",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);    
	item = new MenuItem("<%=root%>/images/operationbtn/edit.png","编辑","editMykm",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);   
	item = new MenuItem("<%=root%>/images/operationbtn/del.png","删除","del",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
    sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}
function shore(){//知识分享
  var id=getValue();
 
  var ids=id.split(",");
  if(id == null||id == ''){
		alert('请选择要分享的知识！');
		return;
	}
  if(ids[1]!=null)
  {
    alert("不可以同时分享多条信息！");
    return;
  }
	
var audit= window.showModalDialog("<%=root%>/knowledge/mykm/mykm!shore.action?mykmId="+id,window,'help:no;status:no;scroll:no;dialogWidth:500px; dialogHeight:300px');
}

function addMykm(){//增加稿件
	var audit= window.showModalDialog("<%=root%>/knowledge/mykm/mykm!input.action?type=0",window,'help:no;status:no;scroll:no;dialogWidth:550px; dialogHeight:350px');
	if(audit=="reload"){
		window.location.reload();
	}
// window.open ("<%=root%>/knowledge/mykm/mykm!input.action?type=0", '', 'height=400, width=550, top=200, left=400, toolbar=no,menubar=no, scrollbars=no, resizable=no,location=no, status=no');
}

function showinfo(){//查看收藏知识信息
 var id=getValue();
	if(id == null||id == ''){
		alert('请选择要查阅的知识！');
		return;
	}
	if(id.length >32){
		alert('不能同时查阅多个知识点！');
		return;
	}
	//top.perspective_content.actions_container.personal_properties_toolbar.navigate("<%=root%>/knowledge/mykm/mykm!show.action?mykmId="+id,"查看信息");
	getSysConsole().navigate("<%=root%>/knowledge/mykm/mykm!show.action?mykmId="+id,"查看信息");
}
function editMykm(){//编辑收藏
	var id=getValue();
	if(id == null||id == ''){
		alert('请选择要修改的知识！');
		return;
	}
	if(id.length >32){
		alert('不能同时修改多个知识点！');
		return;
	}
	var audit = window.showModalDialog("<%=path%>/knowledge/mykm/mykm!input.action?mykmId="+id,window,'help:no;status:no;scroll:no;dialogWidth:550px; dialogHeight:350px');
	if(audit=="reload"){
		window.location.reload();
	}
}
function del(){//废除稿件
	var id=getValue();
	if(id == null||id == ''){
		alert('请选择要删除的信息！');
		return;
	}
	if(confirm("确定删除这个收藏的知识吗?")) 
	{ 
	   location = '<%=path%>/knowledge/mykm/mykm!delete.action?mykmId='+id;
	} 
	

	
}

function show(){//显示收藏
	var id=getValue();
	if(id == null||id == ''){
		alert('请选择知识点！');
		return;
	}
	if(id.length >32){
		alert('只能同时编辑一个知识点！');
		return;
	}
	var audit = window.showModalDialog("<%=path%>/knowledge/mykm/mykm!input.action?mykmId="+id,window,'help:no;status:no;scroll:no;dialogWidth:860px; dialogHeight:680px');
}


function getListBySta(){	//根据属性查询
	//document.getElementById("disLogo").value="search";
	document.getElementById("myTableForm").submit();
}

function nothing(a){alert(a)}

$(document).ready(function(){
	$("#img_sousuo").click(function(){
		$("form").submit();
	});
}); 
</script>
</BODY></HTML>
