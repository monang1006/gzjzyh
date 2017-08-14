<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="web" uri="/tags-web/component"%>
<html>
	<head>
		<title>约稿信息</title>

		<%@include file="/common/include/meta.jsp"%>

		<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/component.css" />
		<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/global.css" />
		
		<script type="text/javascript" src="<%=scriptPath%>/common.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/search.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/Message_${locale}.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/jquery.tooltip.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/component-min.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/global1.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/jqgrid/jqgrid.js"></script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT><!--右键菜单脚本 -->
		<style type="text/css">
			html { -webkit-box-sizing:border-box; -moz-box-sizing:border-box; box-sizing:border-box; padding:40px 0px 40px 0px; overflow:hidden;}
			html,body { height:100%;}
		</style>
	</head>

	<body>
		<script type="text/javascript" src="<%=scriptPath%>/wait.js"></script>
		<div class="windows_title">
			请选择约稿单位
		</div>
		<div class="information_out" id="information_out">
			<div id="gridDiv" class="grid" style="width:auto; display:block; padding: 0px;">
				<s:form theme="simple" id="myTableForm" action="">
					<table width="100%" id="searchTable" border="0" cellpadding="0"
						cellspacing="1" class="main_search_border">
						<tr>
							<td width="100%" align="left" class="biao_bg1"
								style="padding-right:8px;padding-left:8px;">
								机构名称：
								<s:textfield name="orgName" id="orgName"
									cssClass="main_search_input search" title="请输入机构名称"
									theme="simple"></s:textfield>
								<input type="button" value="搜索"  style="width: 50px" id="img_sousuo">
							</td>
						</tr>
					</table>
					<table id="list"></table>
					<div id="pager"></div>
		</s:form>
			</div>
		</div>

		<div class="information_list_choose_pagedown">
			<input type="button" class="information_list_choose_button9"
				value="保存机构" name="save" id="save" />
			<input type="button" class="information_list_choose_button9"
				value="取消机构" name="delete" id="delete" />
			<input type="button" class="information_list_choose_button9"
				value="关闭" name="cancel" id="cancel" />
		</div>

		<input type="hidden" value="${checkId}" id="checkId" name="checkId"/>
		<input type="hidden" value="${loadTitle}" id="loadTitle" name="loadTitle"/>
	</body>
</html>
<script type="text/javascript">
function add(){
	gl.resize();
	var ids = obj.checkId;
	var idArr = new Array();
	idArr = ids.split(",");
	for(var i=0;i<idArr.length;i++){
		$("#list").setSelection(idArr[i],true);
	}
	
}
var obj = window.dialogArguments;
var check = obj.checkId;
var loadTitle = obj.loadTitle;

function distinctArray(arr){
	var obj={},temp=[];
	for(var i=0;i<arr.length;i++){
	if(!obj[arr[i]]){
	temp.push(arr[i]);
	obj[arr[i]] =true;
	}
	}
	return temp;
	   }

$(function(){
	
	var idall = "";

	var onCellSelect = function(rowid, iCol ,el,index, contents, event){
		if(iCol==0||iCol==2){
			var title = $("#list").jqGrid("getCell", rowid, "orgName");
			if(check.indexOf(rowid)>-1){
				check = check.replace(rowid,"");
				loadTitle = loadTitle.replace(title,"");
			}else{
				check=check+","+rowid;
				loadTitle = loadTitle+","+title;
			}
			
		}
		$("#checkId").val(check);
		$("#loadTitle").val(loadTitle);
	};
	
	var all = function(aRowids,status){
		if(status==true){
		for(var i=0;i<aRowids.length;i++){
			//得到列表机构名称
			var title = $("#list").jqGrid("getCell", aRowids[i], "orgName");
			//if(check.indexOf(aRowids[i])>-1){
			//	check = check.replace(aRowids[i],"");
			//	loadTitle = loadTitle.replace(title,"");
			//}
			//else{
				check=check+","+aRowids[i];
				loadTitle = loadTitle+","+title;
				
			//}
		}
		if(check.indexOf(",")==0){
			check =check.substring(1);
		}
		var cid = new Array();
		var ctitle = new Array();
		cid = check.split(",");
		ctitle = loadTitle.split(",");
		cid = distinctArray(cid);
		ctitle = distinctArray(ctitle);
		check=cid.join(",");
		loadTitle=ctitle.join(",");
		$("#checkId").val(check);
		$("#loadTitle").val(loadTitle);
		}
	};
	
	$("#list").jqGrid({
	    url:'${root}/xxbs/action/invitation!findOrg.action',
	    colModel :[ 
	      {label:'orgId',name:'orgId', hidden:true}, 
	      {label:'机构名称',name:'orgName'}, 
	    ],
	    gridComplete: add,
	    onCellSelect: onCellSelect,
	    onSelectAll : all,
	    grouping:true
	});
	

	$("#img_sousuo").click(function(){
  	 	var orgName = $("#orgName").val();
       	var searchParam = {};
	 	searchParam.orgName = $.trim(orgName);
       	searchParam.isSearch = true;
       	$("#list").setGridParam({postData: searchParam}).trigger("reloadGrid");		
	});
	
	
	$("#save").click(function(){
		
		var ret = {};
		ret.status = "success";
		/*ret.id = $("#list").jqDeleteId();
		var ids = ret.id;
		var titleArray =new Array();
		var idArray = new Array();
		for(var i=0;i<ids.length;i++){
			var title = $("#list").jqGrid("getCell", ids[i], "orgName");
			titleArray.push(title);
			var ide = ret.id[i];
			idArray.push(ide);
		}
		//ret.title = $("#list").jqGrid("getCell", ret.id, "orgName");
		ret.title=titleArray.join(",");
		ret.id = idArray.join(",");*/
		var titage = new Array();
		var tig = new Array();
		var idage = new Array();
		var idg =new Array();
		titage = loadTitle.split(",");
		idage = check.split(",");
		for(var i=0;i<titage.length;i++){
			if(titage[i]!=""){
				tig.push(titage[i]);
			}
			if(idage[i]!=""){
				idg.push(idage[i]);
			}
		}
		
		ret.title=tig.join(",");
		ret.id = idg.join(",");
		window.returnValue = ret;
		window.close();
	});
	
	$("#delete").click(function(){
		var ret = {};
		ret.status = "success";
		ret.id = "";
		ret.title = "";
		window.returnValue = ret;
		window.close();
	});
	
	$("#cancel").click(function(){
		window.close();
	});
	$("#jqgh_list_cb").hide();
});





</script>
<script type="text/javascript" src="<%=scriptPath%>/stopwait.js"></script>
