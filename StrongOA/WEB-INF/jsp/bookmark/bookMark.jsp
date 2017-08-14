<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>标签列表</title>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<link href="<%=frameroot%>/css/properties_windows_list.css" type="text/css"
			rel="stylesheet">
		<link href="<%=frameroot%>/css/search.css" type="text/css"
			rel="stylesheet">
		<script language='javascript'
			src='<%=path%>/common/js/grid/ChangeWidthTable.js'
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
		<script language="javascript" src="<%=path%>/common/js/menu/menu.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/search.js"
			type="text/javascript"></script>
		<style media="screen" type="text/css">
.tabletitle {
	FILTER: progid :       DXImageTransform.Microsoft.Gradient (    
                                gradientType =         0, startColorStr
		= 
		   
		   #ededed, endColorStr =         #ffffff );
}

.hand {
	cursor: pointer;
}
</style>
		<script type="text/javascript">
    	
    	//增加标签
    	function addBookMark(){
    		location = "<%=root%>/bookmark/bookMark!input.action";
    	}
    
    	//修改标签
    	function editBookMark(){
    		var id = getValue();
    		if(id == ""){
    			alert("请选择要修改的标签。");
    			return ;
    		}
    		if(id.split(",").length>1){
    			alert("一次只能修改一条记录。");
    			return ;
    		}
    		location = "<%=root%>/bookmark/bookMark!input.action?model.id="+id;
    	}
    	
    	//删除标签
    	function delBookMark(){
    		var id = getValue();
    		if(id == ""){
    			alert("请选择要删除的标签。");
    			return ;
    		}
    		if(confirm("确定要删除吗？")){
	    		location = "<%=root%>/bookmark/bookMark!delete.action?model.id="+id;
    		}
    	}
    
    	//保存映射
    	//@param bookmarkId 要保存的书签id
    	//@param formId		所选择的电子表单模板id
    	function doSave(bookmarkId,formId){
    		var currentButton = event.srcElement;
    		var objSelect = $(currentButton).prev().get(0);
    		
    		var objOption = objSelect.options[objSelect.selectedIndex];
    		if(objSelect.selectedIndex=="0"){
    		alert("未指定，请选择要映射的表单字段。");
    		}else if(objSelect.selectedIndex!==0){
    		var fieldName = objOption.value;//字段名称
    		var tableName = objOption.name;//表名称
    		var editId    = objOption.id ;//控件id
    		
    		$.post(scriptroot + "/bookmark/bookMark!doSaveRelate.action",
    			  {formId:formId,"model.id":bookmarkId,"model.rest1":fieldName,"model.rest2":tableName,"model.rest3":editId},
    			  function(ret){
    				if(ret == "0"){
    					alert("操作成功。");
    					return ;
    				}else if(ret == "-1"){
    					alert("对不起，操作失败，请与管理员联系。");
    					return ;
    				}
    			  });
    	}
    	}
    
       //搜索	
       function doSelect(){
<%--       	var descValue = $("#desc").val();
       	if(descValue != ""){
       		descValue = encodeURI(descValue);
       	}
       	$("#desc").val(descValue);--%>
       	$("form").submit();
       }		
    
       $(document).ready(function(){
       	$("#td_name").attr("width",$("th:eq(1)").attr("width"));
        $("#td_desc").attr("width",$("th:eq(2)").attr("width"));
        $("#td_formId").attr("width",$("th:eq(3)").attr("width"));
        $("#img_sousuo").click(doSelect);     
      });
      
    </script>
	</head>
	<body class="contentbodymargin" oncontextmenu="return false;"
		onload="initMenuT();">
		<!-- oncontextmenu="return false;" onload="initMenuT();" -->
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td height="8px;"></td>
						</tr>
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
								<strong>标签列表</strong>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
						            <tr>
						            	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="addBookMark();"><img src="<%=root%>/images/operationbtn/add.png"/>&nbsp;新&nbsp;建&nbsp;标&nbsp;签&nbsp;</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
				                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="editBookMark();"><img src="<%=root%>/images/operationbtn/edit.png"/>&nbsp;编&nbsp;辑&nbsp;标&nbsp;签&nbsp;</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
				                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="delBookMark();"><img src="<%=root%>/images/operationbtn/del.png"/>&nbsp;删&nbsp;除&nbsp;标&nbsp;签&nbsp;</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td height="8px;"></td>
						</tr>
							<s:form id="myTableForm" action="/bookmark/bookMark.action"
								method="post">
								<webflex:flexTable name="myTable" width="100%" height="200px"
									wholeCss="table1" property="id" isCanDrag="true"
									isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
									getValueType="getValueByProperty" collection="${page.result}"
									page="${page}">
									<table width="100%" border="0" cellpadding="0" cellspacing="0"
										class="table1_search">
										<tr>
											<td>
									       		&nbsp;&nbsp;标签名称：&nbsp;<input name="model.name" id="model.name" type="text"  class="search" title="请您输入标签名称" value="${model.name }">
									       		&nbsp;&nbsp;标签说明：&nbsp;<input name="model.desc" id="model.desc" type="text"  class="search" title="请您输入标签说明" value="${model.desc }">
									       		&nbsp;&nbsp;表单：&nbsp;<s:select onchange="doSelect();" name="formId"  id="formId" list="eforms" listKey="id" listValue="title" />
									       		&nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo" type="button" />
									       	</td>
										</tr>
									</table>

									<webflex:flexCheckBoxCol caption="选择" property="id"
										showValue="desc" width="5%" isCheckAll="true"
										isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
									<webflex:flexTextCol caption="标签名称" property="name"
										showValue="name" showsize="50" width="30%" isCanDrag="true"
										isCanSort="true"></webflex:flexTextCol>
									<webflex:flexTextCol caption="标签说明" property="desc"
										showValue="desc" showsize="25" width="30%" isCanDrag="true"
										isCanSort="true"></webflex:flexTextCol>
									<webflex:flexTextCol caption="字段映射" property="" showValue="rest5"
										showsize="50000" width="35%" isCanDrag="true"  isCanSort="true"></webflex:flexTextCol>
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
	        item = new MenuItem("<%=root%>/images/operationbtn/add.png","新建标签","addBookMark",1,"ChangeWidthTable","checkMoreDis");
	        sMenu.addItem(item);
	        item = new MenuItem("<%=root%>/images/operationbtn/edit.png","编辑标签","editBookMark",1,"ChangeWidthTable","checkMoreDis");
	        sMenu.addItem(item);
	        item = new MenuItem("<%=root%>/images/operationbtn/del.png","删除标签","delBookMark",1,"ChangeWidthTable","checkOneDis");
	        sMenu.addItem(item);
        sMenu.addShowType("ChangeWidthTable");
        registerMenu(sMenu);
      }
    </script>
	</body>
</html>
