<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@include file="/common/include/rootPath.jsp" %>

<HTML><HEAD><TITLE>公共文件列表</TITLE>
<%@include file="/common/include/meta.jsp" %>
<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet"><!--右键菜单样式 -->
<LINK href="<%=frameroot%>/css/properties_windows_list.css" type=text/css  rel=stylesheet>
<LINK href="<%=frameroot%>/css/search.css" type=text/css  rel=stylesheet>
<link rel="stylesheet" type="text/css" href="<%=path%>/common/js/autocomplete/css/jquery.autocomplete.css" />
<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT><!--右键菜单脚本 -->
<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
<script src="<%=path%>/common/js/upload/jquery.blockUI.js" type="text/javascript"></script>
<script src="<%=path%>/common/js/validate/checkboxvalidate.js" type="text/javascript"></script>
<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
<script src="<%=path%>/common/js/common/search.js" type="text/javascript"></script>
<script type='text/javascript' src='<%=path%>/common/js/autocomplete/js/jquery.autocomplete.min.js'></script>
<script src="<%=path%>/oa/js/prsnfldr/prsnfldr.js" type="text/javascript"></script>


</HEAD>
<BODY class=contentbodymargin oncontextmenu="return false;" onload=initMenuT()>
<script type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
<label id="l_actionMessage" style="display: none;"><s:actionmessage/></label>
<DIV id=contentborder align=center>
  <table width="100%" border="0" cellspacing="0" cellpadding="00">
  <tr>
    <td height="100%">
    <table width="100%" border="0" cellspacing="0" cellpadding="00">
      <tr>
        <td height="40" style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
        <table width="100%" border="0" cellspacing="0" cellpadding="00">
          <tr>
          
          
          <%--
            <img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
            <label id="l_prsnfldr_folderName">${folderName }</label>
            </td>
            --%>
            
            
            <td class="table_headtd_img" >
			<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
			</td>
			<td align="left">
			<label id="l_prsnfldr_folderName">${folderName }</label>
			</td>
			
			
			<%--
            <td width="70%">
            <table border="0"  align="right" cellpadding="00" cellspacing="0">
                <tr>
                
                  <security:authorize ifAnyGranted="001-0001000700030006">
	                  <td ><a class="Operation" href="javascript:gotoAdd();" id="hrfLocalNewFile"><img src="<%=root%>/images/ico/tb-add.gif" width="15" height="15" class="img_s" />添加&nbsp;</a></td>
	                  <td width="5">&nbsp;</td>
                  </security:authorize>
                   <security:authorize ifAnyGranted="001-0001000700030005">
	                  <td ><a class="Operation" href="javascript:gotoView();" id="hrfView"><img class="img_s" src="<%=root%>/images/ico/page.gif" width="15" height="15" />查阅&nbsp;</a></td>
	                  <td width="5">&nbsp;</td>
                  </security:authorize>
                  <security:authorize ifAnyGranted="001-0001000700030004">
	                  <td ><a class="Operation" href="#" url="<%=root%>/prsnfldr/privateprsnfldr/prsnfldrFile!delete.action" id="hrfDel"><img class="img_s" src="<%=root%>/images/ico/shanchu.gif" width="15" height="15" />删除&nbsp;</a></td>
	                  <td width="5">&nbsp;</td>
                  </security:authorize>
                  <security:authorize ifAnyGranted="001-0001000700030009">
	                  <td ><a class="Operation" href="#" url="<%=root%>/prsnfldr/privateprsnfldr/prsnfldrFile!cut.action" id="hrfCut"><img class="img_s" src="<%=path%>/oa/image/prsnfldr/cut.gif" width="15" height="15" />剪切&nbsp;</a></td>
	                  <td width="5">&nbsp;</td>
                  </security:authorize>
                   <security:authorize ifAnyGranted="001-0001000700030008">
	                  <td ><a class="Operation" href="#" url="<%=root%>/prsnfldr/privateprsnfldr/prsnfldrFile!copy.action" id="hrfCopy"><img class="img_s" src="<%=path%>/oa/image/prsnfldr/copy.gif" width="15" height="15" />复制&nbsp;</a></td>
	                  <td width="5">&nbsp;</td>
                  </security:authorize>
                  <security:authorize ifAnyGranted="001-0001000700030007">
	                  <td ><a class="Operation" href="javascript:gotoParse();"><img class="img_s" src="<%=path%>/oa/image/prsnfldr/paste.gif" width="15" height="15">粘贴&nbsp;</a></td>
	                  <td width="5">&nbsp;</td>
                  </security:authorize>
                </tr>
            </table>
            </td>
           --%>
           
           <td width="70%">
											<table border="0" align="right" cellpadding="00" cellspacing="0">
								                <tr>
								                <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	        <td class="Operation_list" onclick="gotoAdd();"><img src="<%=root%>/images/operationbtn/add.png"/>&nbsp;新&nbsp;建&nbsp;</td>
					                 	        <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		        <td width="5"></td>
								                 	
								                	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	            <td class="Operation_list" onclick="gotoView();"><img src="<%=root%>/images/operationbtn/Consult_the_reply.png"/>&nbsp;查&nbsp;看&nbsp;</td>
					                 	            <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		            <td width="5"></td>
								                	
								                 	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	            <td class="Operation_list" href="#" url="<%=root%>/prsnfldr/privateprsnfldr/prsnfldrFile!delete.action" id="hrfDel"><img src="<%=root%>/images/operationbtn/del.png"/>&nbsp;删&nbsp;除&nbsp;</td>
					                 	            <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		            <td width="5"></td>
				                  		            
								                 	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	            <td class="Operation_list"href="#" url="<%=root%>/prsnfldr/privateprsnfldr/prsnfldrFile!cut.action" id="hrfCut"><img src="<%=path%>/images/operationbtn/Shear.png" width="15" height="15">&nbsp;剪&nbsp;切&nbsp;</td>
					                 	            <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		            <td width="5"></td>
				                  		            
				                  		            <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	            <td class="Operation_list"href="#" url="<%=root%>/prsnfldr/privateprsnfldr/prsnfldrFile!copy.action" id="hrfCopy"><img src="<%=path%>/images/operationbtn/Replication.png" width="15" height="15">&nbsp;复&nbsp;制&nbsp;</td>
					                 	            <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		            <td width="5"></td>
				                  		            
				                  		             <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	            <td class="Operation_list" onclick="gotoParse();"><img src="<%=root%>/images/operationbtn/Paste.png"/>&nbsp;粘&nbsp;贴&nbsp;</td>
					                 	            <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		            <td width="5"></td>
				                  		            
								                </tr>
								            </table>
											</td>
            
            
            
            
            
          </tr>
        </table>
        </td>
      </tr>
	<s:form id="myTableForm" action="/prsnfldr/privateprsnfldr/prsnfldrFile!publicFileList.action">
		<input id="folderId" type="hidden" name="folderId" value="${folderId }"/>
		 <input id="folderName" type="hidden" name="folderName" value="${folderName }"/><!-- 用于将文件名传到后台然后传回此页面显示在<label> -->
		 <input id="fileType" type="hidden" name="fileType" value="${fileType }"/>
	     <webflex:flexTable name="myTable" width="100%" height="370px" wholeCss="table1" property="fileId" 
	     isCanDrag="true" isCanFixUpCol="true" clickColor="#A9B2CA" 
	     footShow="showCheck" getValueType="getValueByArray" 
	     collection="${page.result}" page="${page}">
		 <%--<table width="100%" border="0" cellpadding="0" cellspacing="1" class="table1">
        <tr>
          <td width="5%" align="center"  class="biao_bg1"><img onclick="javascript:gotoSearch();" title="单击搜索" style="cursor: hand;" src="<%=root%>/images/ico/sousuo.gif" width="15" height="15" ></td>
          <td width="30%"  class="biao_bg1">
			<input id="search1" maxlength="32" url="<%=root%>/prsnfldr/privateprsnfldr/prsnfldrFile!autoComplete.action" name="searchFileName" class="search" title="输入文件名称" value="${searchFileName }"/>
			
          </td>
          <td class="biao_bg1">&nbsp;
          </td></tr>
      	</table> 
      	--%>
      	
      	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table1_search">
							     <tr>
							       <td>
							       		&nbsp;&nbsp;标题：&nbsp;<input id="search1"  maxlength="32" url="<%=root%>/prsnfldr/privateprsnfldr/prsnfldrFile!autoComplete.action" name="searchFileName" class="search" title="输入文件名称" value="${searchFileName }"/>
						
							       		&nbsp;&nbsp;&nbsp;&nbsp;<input onclick="javascript:gotoSearch();" id="img_sousuo" type="button" />
							       	</td>
							     </tr> 
							     
							     
							</table>
      	
      	
      	
      	
      	
      	
      	
		<webflex:flexCheckBoxCol caption="选择" valuepos="0" valueshowpos="1" width="4%" isCheckAll="true" isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
		<webflex:flexTextCol caption="标题" valuepos="0" onclick="javascript:gotoViewByTitle(this.value);" valueshowpos="1"  width="25%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
		<webflex:flexDateCol  caption="添加日期" valuepos="2" valueshowpos="2" dateFormat="yyyy-MM-dd" width="20%" isCanDrag="true" isCanSort="true"></webflex:flexDateCol>
		<webflex:flexTextCol caption="文件大小" valuepos="3" valueshowpos="3" width="15%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
		<webflex:flexTextCol caption="查阅数" valuepos="4" valueshowpos="4" width="15%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
		<webflex:flexTextCol caption="添加人" valuepos="5" valueshowpos="5" width="20%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
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
	sMenu.registerToDoc(sMenu);
	var item = null;
	<security:authorize ifAnyGranted="001-0001000700030006">
		item = new MenuItem("<%=root%>/images/operationbtn/add.png","新建","gotoAdd",1,"ChangeWidthTable","checkOneDis");
		sMenu.addItem(item);
	</security:authorize>
	<security:authorize ifAnyGranted="001-0001000700030005">
		item = new MenuItem("<%=root%>/images/operationbtn/Consult_the_reply.png","查看","gotoView",1,"ChangeWidthTable","checkOneDis");
		sMenu.addItem(item);
	</security:authorize>
	<security:authorize ifAnyGranted="001-0001000700030004">
		item = new MenuItem("<%=root%>/images/operationbtn/del.png","删除","gotoDel",1,"ChangeWidthTable","checkOneDis");
		sMenu.addItem(item);
	</security:authorize>
	<security:authorize ifAnyGranted="001-0001000700030009">
		item = new MenuItem("<%=path%>/images/operationbtn/Shear.png","剪切","gotoCut",1,"ChangeWidthTable","checkOneDis");
		sMenu.addItem(item);
	</security:authorize>
	<security:authorize ifAnyGranted="001-0001000700030008">
		item = new MenuItem("<%=path%>/images/operationbtn/Replication.png","复制","gotoCopy",1,"ChangeWidthTable","checkOneDis");
		sMenu.addItem(item);
	</security:authorize>
	<security:authorize ifAnyGranted="001-0001000700030007">
		item = new MenuItem("<%=path%>/images/operationbtn/Paste.png","粘贴","gotoParse",1,"ChangeWidthTable","checkOneDis");
		sMenu.addItem(item);
	</security:authorize>
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}
function show(i){
	$.blockUI({ overlayCSS: { backgroundColor: '#B3B3B3' }});
	$.blockUI({ message: '<font color="#008000"><b>'+i+'</b></font>' });
}
function hidden(){
	$.unblockUI();
}
//添加
function gotoAdd(){
	if($("#folderId").val() == null || $("#folderId").val() == ""){
		alert("请先添加文件夹。");
		return ;
	}
	var ret=OpenWindow("<%=root%>/prsnfldr/privateprsnfldr/prsnfldrFile!init.action?folderId="+$("#folderId").val(),"500","321",window);
	if(ret!=undefined && ret!="None"){
		parent.project_work_content.document.location="<%=root%>/prsnfldr/privateprsnfldr/prsnfldrFile!publicFileList.action?folderId="+$("#folderId").val();
	}
}
//搜索
function gotoSearch(){
	if($("#folderId").val() == null || $("#folderId").val() == ""){
		alert("请先添加文件夹。");
		return ;
	}
	$("form").submit();
}

//查看
function gotoView(){
	var url = "<%=root%>/prsnfldr/privateprsnfldr/prsnfldrFile!download.action";
	var id = getValue();
	if(id == ""){
		alert("请选择要查看的记录。");
		return;
	}else{
		var ids = id.split(",");
		if(ids.length>1){
			alert("只可以查看一条记录。");
			return ;
		}
	}
	var ret=OpenWindow("<%=root%>/prsnfldr/privateprsnfldr/prsnfldrFile!viewFile.action?folderId="+$("#folderId").val()+"&id="+id,"500","300",window);
	if(ret!=undefined && ret!="None"){
		parent.project_work_content.document.location="<%=root%>/prsnfldr/privateprsnfldr/prsnfldrFile!publicFileList.action?folderId="+$("#folderId").val();
	}else{
		var readCount = $(":checked").parent().next().next().next().next().text();
		$(":checked").parent().next().next().next().next().text(parseInt(readCount)+1)
	}
}
//直接点标题进去查看
function gotoViewByTitle(id){
	var ret=OpenWindow("<%=root%>/prsnfldr/privateprsnfldr/prsnfldrFile!viewFile.action?folderId="+$("#folderId").val()+"&id="+id,"500","300",window);
	if(ret!=undefined && ret!="None"){
		parent.project_work_content.document.location="<%=root%>/prsnfldr/privateprsnfldr/prsnfldrFile!publicFileList.action?folderId="+$("#folderId").val();
	}else{
		var readCount = $(event.srcElement).next().next().next().text();
		$(event.srcElement).next().next().next().text(parseInt(readCount)+1);
	}
}
//复制
function gotoCopy(){
	copyFile($("#hrfCopy").get(0).url);
}
//剪切
function gotoCut(){
	cutFile($("#hrfCut").get(0).url);
}
//粘贴
function gotoParse(){
	if($("#folderId").val() == null || $("#folderId").val() == ""){
		alert("请先添加文件夹。");
		return ;
	}
	parseFile("<%=root%>/prsnfldr/privateprsnfldr/prsnfldrFile!parse.action");
}
//删除
function gotoDel(){
		var name = $(":checked").parent().next().attr("value");
		var txt = $(":checked").parent().next().text();
		if(txt==""||txt==null){
			alert("请选择要删除的记录。");
			return;
		}
		var url = $("#hrfDel").attr("url");
		if(confirm("确定要删除文件“"+txt+"”，吗？")){
		show("正在处理中,请稍后...");
				$.ajax({
					type:"post",
					url:url,
					data:{
						id:getValue()
					},
					success:function(data){
						if(data!="" && data!=null){
							alert(data);						
						}else{
						//	alert("删除成功!");
							parent.project_work_content.document.location="<%=root%>/prsnfldr/privateprsnfldr/prsnfldrFile!publicFileList.action?folderId="+$("#folderId").val();
						}
					},
					error:function(data){
						alert("对不起，操作异常"+data);
					}
			   });
			}
}

	//添加文件夹
	function addFolder(){
		var ret=OpenWindow("<%=root%>/prsnfldr/publicprsnfldr/publicPrsnfldrFolder!init.action","400","150",window);
		if(ret!=undefined && ret!="None"){
			parent.document.location.reload() ;
		}
	}
</script>
</BODY></HTML>
