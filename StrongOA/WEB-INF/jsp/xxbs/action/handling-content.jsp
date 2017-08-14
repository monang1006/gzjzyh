<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="web" uri="/tags-web/component"%>
<html>
	<head>
		<title>信息处理</title>

		<%@include file="/common/include/meta.jsp"%>

		<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/component.css" />
		<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/global.css" />
		
		<script type="text/javascript" src="<%=scriptPath%>/global1.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/common.js"></script>
		<!--  <script type="text/javascript" src="<%=scriptPath%>/search.js"></script>-->
		<script type="text/javascript" src="<%=scriptPath%>/Message_${locale}.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/jquery.tooltip.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/component-min.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/My97DatePicker/WdatePicker.js"></script>
		
		<script type="text/javascript" src="<%=scriptPath%>/jqgrid/jqgrid.js"></script>
		<style type="text/css">
		.ui-jqgrid-resize-ltr{width: 0px;}
		.ui-jqgrid .ui-jqgrid-htable TH DIV{height:21px}
		</style>
	</head>

	<body>
		<script type="text/javascript" src="<%=scriptPath%>/wait.js"></script>
			<div class="main_up_out">
				<div id="menu_top" style="min-width: 688px;">
					<ul>
						
						<li>
							<button id="add" class="input_button_4"><img src="<%=themePath%>/image/ico_add.gif"/>补录</button>
						</li>
						<li>
							<button id="show" class="input_button_4"><img src="<%=themePath%>/image/ico_view.gif"/>查看</button>
						</li>
						<li>
							<button id="adopt" class="input_button_4"><img src="<%=themePath%>/image/ico_view.gif"/>采用</button>
						</li>
						<li>
							<button id="listadd" class="input_button_6"><img src="<%=themePath%>/image/ico_add.gif"/>批量采用</button>
						</li>
						<li>
							<button id="remark" class="input_button_4"><img src="<%=themePath%>/image/ico_view.gif"/>批示</button>
						</li>
						<li>
							<button id="share" class="input_button_4"><img src="<%=themePath%>/image/ico_view.gif"/>共享</button>
						</li>
						<li>
							<button id="merge" class="input_button_4"><img src="<%=themePath%>/image/ico_view.gif"/>合并</button>
						</li>
						<li>
							<button id="viewmerge" class="input_button_6"><img src="<%=themePath%>/image/ico_view.gif"/>查看合并</button>
						</li>
						<li>
							<button id="closemerge" class="input_button_6"><img src="<%=themePath%>/image/ico_view.gif"/>解除合并</button>
						</li>
						<li>
							<button id="delete" class="input_button_4"><img src="<%=themePath%>/image/ico_del.gif"/>删除</button>
						</li>
						<li>
							<button id="allInfo" class="input_button_6"><img src="<%=themePath%>/image/ico_view.gif"/>所有信息</button>
						</li>
						<!-- <li>
							<button id="comment" class="input_button_4"><img src="<%=themePath%>/image/ico_view.gif"/>点评</button>
						</li> 
						<li>
							<button id="queryTree" class="input_button_6"><img src="<%=themePath%>/image/ico_view.gif"/>查询分类</button>
						</li>-->
						<li>
							<button id="print1" class="input_button_6"><img src="<%=themePath%>/image/ico_view.gif"/>打印预览</button>
						</li>
						<!--<li>
							<button id="isOA" class="input_button_6"><img src="<%=themePath%>/image/ico_view.gif"/>网上发布</button>
						</li>
						  <li>
							<button id="putall" class="input_button_6"><img src="<%=themePath%>/image/ico_view.gif"/>批量处理</button>
						</li>-->
					</ul>

					<br style="clear:both" />
				</div>

				<div id="gridDiv" class="grid">
		<s:form theme="simple" id="myTableForm" action="">
					<table width="100%" id="searchTable" border="0" cellpadding="0"
						cellspacing="1" class="main_search_border">
						<tr>
<td width="73%" align="left" class="biao_bg1"
								style="padding-right:8px;padding-left:8px;">
								标题：
								<s:textfield name="pubTitle" id="pubTitle"
									title="请输入信息标题"
									theme="simple" size="20"></s:textfield>
								&nbsp;&nbsp;时间：
								<s:textfield name="startDate" id="startDate"
									title="请输入开始时间"
									theme="simple" size="7"></s:textfield>-
								<s:textfield name="endDate" id="endDate"
									title="请输入结束时间"
									theme="simple" size="7"></s:textfield>
								<web:datetime format="yyyy-MM-dd HH:mm" readOnly="false" id="startDate" />
								<web:datetime format="yyyy-MM-dd HH:mm" readOnly="false" id="endDate" />
								 &nbsp;&nbsp;单位：
								 <s:textfield name="orgId" id="orgId"
									title="请输入单位"
									theme="simple" size="7"></s:textfield>
								<input type="button" value="搜索"  style="width: 50px" id="img_sousuo1">
							</td>							<td width="27%" align="right">
								过滤：<s:select id="useStatus" name="useStatus" list="#{\"2\":'预采用',\"0\":'未采用',\"1\":'已采用', \"3\":'已点评', \"4\":'已共享',\"all\":'所有'}" value="%{useStatus}"></s:select>
							</td>
							<td>&nbsp;</td>
						</tr>
					</table>
					
					<table id="list" minWidth="689"></table>
					<div id="pager"></div>
		</s:form>
				</div>
				</div>


	<input type="hidden" value="" id="toId" name="toId"/>
	</body>
</html>
<script type="text/javascript">
var w;
var h;

var add = function(){
	var url = "<%=root%>/xxbs/action/handling!entry.action";
	var ret = gl.showDialog(url,1000,800);
	gl.msg(ret, "保存成功");
};
$("#add").click(add);


var listadd = function(){
	var id = $("#list").jqDeleteId();
	if(id.length<1){
		alert("最少选择一项!");
		return false;
	}
	$.get("<%=root%>/xxbs/action/handling!isSubmit.action?toId="+id, function(response){
		 if(response == "nosuccess"){
			alert("只能选择未采用和预采用的信息批量采用!");
		}
		 else{
			 var url = "<%=root%>/xxbs/action/handling!listadd.action?toId="+id;
				var ret = gl.showDialog(url,570,300);
				gl.msg(ret, "批量采用成功");
		 }
	});
};
$("#listadd").click(listadd);

var del = function(){
	var id = $("#list").jqDeleteId();
	if(id==''){
		alert("最少选择一项！");
		return false;
	}
	if(id != false && confirm("确定要删除吗？")){
		$.get("<%=root%>/xxbs/action/submit!deleteSubmitted.action?toId="+id, function(response){
			if(response == "success"){
				gl.msg(response, "删除成功");
			}
			else if(response == "notDelete"){
				alert("已被采用的报送信息不允许删除。");
			}
		});
	}
};
$("#delete").click(del);

var adoptDialog = function(id){
	//var url = "<%=root%>/xxbs/action/adoption!input.action?toId="+id;
	var url = "<%=root%>/xxbs/action/handling!view.action?op=adopt&toId="+id;
	var ret = gl.showDialog(url,1000,800);
	gl.msg(ret, "更新采用成功");	
};
var adopt = function(){
	var id = $("#list").jqSelectedId();
	if(id != false){
		adoptDialog(id);
	}
};
$("#adopt").click(adopt);

var remark = function(){
	var id = $("#list").jqSelectedId();
	$.get("<%=root%>/xxbs/action/handling!isPublish.action?toId="+id, function(response){
		if(response == "nosuccess"){
			alert("未成刊的文章不能批示!");
			return false;
		}
		else{
			if(id != false){
				var url = "<%=root%>/xxbs/action/handling!viewRemark.action?toId="+id;
				var ret = gl.showDialog(url,600,350);
				gl.msg(ret, "更新批示成功");
			}
		}
	});
	
};
$("#remark").click(remark);

var comment = function(){
	var id = $("#list").jqSelectedId();
	if(id != false){
		var url = "<%=root%>/xxbs/action/handling!viewComment.action?toId="+id;
		var ret = gl.showDialog(url,570,300);
		gl.msg(ret, "更新点评成功");
	}
};
$("#comment").click(comment);

var share = function(){
	var id = $("#list").jqSelectedId();
	if(id != false){
		var url = "<%=root%>/xxbs/action/handling!viewShare.action?toId="+id;
		var ret = gl.showDialog(url,500,280);
		gl.msg(ret, "更新共享成功");
	}
};
$("#share").click(share);

var showDia = function(id){
	var url = "<%=root%>/xxbs/action/handling!view.action?toId="+id;
	gl.showDialog(url,1000,800);
	reloadData();
};

var show = function(){
	var id = $("#list").jqSelectedId();
	if(id != false){
		showDia(id);
	}
};
$("#show").click(show);

var queryTree = function(){
	var isP = parent.location.href;
	if(isP.indexOf("handling.action") == -1){
		url = root + "/xxbs/action/handling.action";
		location.href= url;
	}
	else{
		url = root + "/xxbs/action/handling!content.action";
		parent.location.href= url;
	}
};
$("#queryTree").click(queryTree);

var option = {
	width : 150,
	items : [	{
		text : "查看",
		icon : "<%=themePath%>/image/ico_view.gif",
		alias : "1-2",
		action : show
	}, 
	{
		text : "采用",
		icon : "<%=themePath%>/image/ico_view.gif",
		alias : "1-4",
		action : adopt
	}, 
	{
		text : "批示",
		icon : "<%=themePath%>/image/ico_view.gif",
		alias : "1-6",
		action : remark
	}, 
	/*{
		text : "点评",
		icon : "<%=themePath%>/image/ico_view.gif",
		alias : "1-7",
		action : comment
	},*/ 
	{
		text : "共享",
		icon : "<%=themePath%>/image/ico_view.gif",
		alias : "1-5",
		action : share
	}, 
	{
		text : "新建报送信息",
		icon : "<%=themePath%>/image/ico_add.gif",
		alias : "1-1",
		action : add
	}, 
	{
		text : "删除报送信息",
		icon : "<%=themePath%>/image/ico_del.gif",
		alias : "1-3",
		action : del
	}
	]
};

	
$(function(){
	w = gl.windowWidth();
	h = gl.windowHeight();

	var rightMenu = function(rowid, iRow, iCol, e){
		$(this).resetSelection();
		$(this).setSelection(rowid);
		var menu = $.fn.contextmenu(option);
		menu.displayMenu(e, e.target);
	};
	
	var onCellSelect = function(rowid, iCol ,el){
		var time = new Date();
		//预采用
		if(iCol==7){
			if(el == " "){
					$.get("<%=root%>/xxbs/action/handling!preUse.action?toId="+rowid+"&time="+time.getTime(),function(ret){
						if(ret == "success"){
							reloadData();
						}
					});	
			    }
			//取消预采用
			if(el == "  "){
				$.get("<%=root%>/xxbs/action/handling!preUse.action?toId="+rowid+"&flag=0&time="+time.getTime(),function(ret){
					if(ret == "success"){
						reloadData();
					}
				});	
		    }
		}
		if(iCol == 4){
			var url = "<%=root%>/xxbs/action/handling!view.action?toId="+rowid;
			gl.showDialog(url,1000,800);
			reloadData();
		}
	};
	
	//if("${qs}" == ""){
		//var url = '${root}/xxbs/action/handling!showListByjdbc.action?useStatus=${useStatus}&qs=${qs}';		
	//}
	//else{
		var url = '${root}/xxbs/action/handling!showList.action?useStatus=${useStatus}&qs=${qs}';
	//}
	
	$("#list").jqGrid({
		url:url,
		datatype:'json',
		colModel :[
			{label:'pubId',name:'pubId', hidden:true,width:1},
			{label:'pubIsRead',name:'pubIsRead', hidden:true,width:1},
			{label:'onlyDate',name:'onlyDate', formatter: dateFormat, hidden:true,width:1},
			{label:'信息标题',name:'pubTitle', formatter:isRead,sortable:false}, 
			{label:'报送单位',name:'orgName',sortable:false,width:40}, 
			
			{label:"报送时间", name:"pubDate", align:"center", sortable:false,  formatter:fPubDate,width:40},
			{label:"采用", name:"pubUseStatus", formatter:fUseStatus, align:"center", sortable:false,width:40},
			{label:"批示",name:'pubIsInstruction', formatter:isYes,  align:'center',sortable:false,width:40}, 
			{label:"共享", name:"pubIsShare", formatter:isYes, align:"center", sortable:false,width:40},
			{label:"点评", name:"pubIsComment", formatter:isYes, align:"center", sortable:false,width:40},
			{label:"合并", name:"pubMergeOrg", formatter:isYes, align:"center", sortable:false,width:40}
			],
		onRightClickRow: rightMenu,
		onCellSelect: onCellSelect,
		gridComplete: gl.resize,
		sortname:'pubDate',
	    grouping:true, 
	    groupingView : { 
	       groupField : ['onlyDate'],
	       groupColumnShow : [false],
	       groupOrder: ['desc'],
	       groupText:['<strong>{0}</strong>'],
	       groupDataSorted : false
	    }
	});
	
	
	
	$("#img_sousuo1").click(function(){
  	 	var startDate = $("#startDate").val();
  	 	var endDate = $("#endDate").val();
  	 	if(startDate == "" && endDate !=""){
  	 		alert("请选择开始时间。");
  	 		return;
  	 	}
  	 	if(startDate != "" && endDate ==""){
  	 		alert("请选择结束时间。");
  	 		return;
 	 	}
  	 	if(startDate > endDate){
  	 		alert("开始时间不能大于结束时间。");
  	 		return;
  	 	}
 	 	var pubTitle = $("#pubTitle").val();
   	 	var orgId = $("#orgId").val();
       	var searchParam = {};
	 	searchParam.pubTitle = $.trim(pubTitle);
	 	searchParam.startDate = startDate;
	 	searchParam.endDate = endDate;
	 	searchParam.orgId = $.trim(orgId);
      	searchParam.isSearch = true;
       	$("#list").setGridParam({postData: searchParam,page:1}).trigger("reloadGrid");	
	});
});



function reloadData(){
	$('#list').trigger("reloadGrid");
}

var infotype = function(el, cellval, opts) {
	temp = "";
	if(el == 0){
		temp = "普通信息";
	}
	else if (el == 1) {
		temp = "涉密信息";
	}
	return temp;
};

var fUseStatus = function(el, cellval, opts) {
	temp = "";
	if (el == "1") {
		temp = "<img src='<%=themePath%>/image/ico_hook.gif' title='已采用'>";
	}
	else if (el == "2") {
		temp = "<div title='预采用' style=' background: url(<%=themePath%>/image/ico_set.gif);width:16px; height:16px;margin:0 auto; cursor: pointer;'>  </div>";
	}
	else if(el == "0"){
		temp = "<div title='未采用' style=' background: url(<%=themePath%>/image/yucaiyong.gif);width:60px; height:20px;margin:0 auto; cursor: pointer;'> </div>";
	}
	return temp;
};


var fPubDate = function(el, cellval, opts) {
	var dtitle = el.substring(0,10);
	var temp="<span title='"+el+"'>"+dtitle+"</span>"
	return temp;
};


var isYes = function(el, cellval, opts) {
	temp = "";
	if (el == "1") {
		temp = "<img src='<%=themePath%>/image/ico_hook.gif' title='是'>";
	}
	return temp;
};

var isRead = function(val, opts, obj) {
	if(obj.pubIsRead == "0"){
		val = "<strong>"+val+"</strong>";
	}
	return val;
};

var dateFormat = function(val, opts, obj){
	//Date中，月是0-11
	
	var today = new Date();
	today.setHours(0, 0, 0, 0);
	var day = today.getDate();
	
	var yesterday = new Date();
	yesterday.setHours(0, 0, 0, 0);
	yesterday.setDate(day-1);

	var yyday = new Date();
	yyday.setHours(0, 0, 0, 0);
	yyday.setDate(day-2);
	
	//alert(today+"\n"+yesterday+"\n"+yyday);
	
	var valArr = val.split("-");
	var valDate = new Date(valArr[0], valArr[1]-1, valArr[2]);
	valDate.setHours(0, 0, 0, 0);

	if(valDate.toString() == today.toString()){
		val = "今日";
	}
	else if(valDate.toString() == yesterday.toString()){
		val = "昨日";
	}
	else if(valDate.toString() == yyday.toString()){
		val = "前日";
	}
	
	return val;
};


$("#useStatus").change(function(){
	location = "${root}/xxbs/action/handling!content.action?useStatus="+$(this).val();
});

$("#allInfo").click(function(){
	location = "${root}/xxbs/action/handling!content.action?useStatus=all";
});

$("#print1").click(function(){
	var id = $("#list").jqDeleteId();
	if(id.length==0){
		alert("最少选择一项!");
		return;
	}
	var obj = new Object();
	obj.checkId=id;
	var ret = window.showModalDialog("<%=root%>/fileNameRedirectAction.action?toPage=/xxbs/action/handling-print1.jsp",obj,"dialogWidth=800px;dialogHeight=700px");
	gl.msg(ret, "更新采用成功");	
});

$("#putall").click(function(){
	$.get("<%=root%>/xxbs/action/handling!putall.action", function(response){
			gl.msg(ret, "处理成功");	
		});
	});


$("#merge").click(function(){
	var id = $("#list").jqDeleteId();
	if((id=="")||(id.length<=1)){
		alert("最少选择两项!");
		return false;
	}
	$.get("<%=root%>/xxbs/action/handling!isHand.action?toId="+id, function(response){
		if(response == "nosuccess"){
			alert("已采用的文章不能合并!");
			return false;
		}
		//if(response == "nosuccess1"){
		//	alert("不能选择两个机构相同的文章合并!");
		//	return false;
		//}
		else{
			var url = "<%=root%>/xxbs/action/handling!merge.action?toId="+id;
			var ret = gl.showDialog(url,1000,800);
			gl.msg(ret, "合并成功");	
		}
	});
});

$("#closemerge").click(function(){
	var id = $("#list").jqSelectedId();
	if(id != false){
	var time = new Date();
	if(id != false && confirm("确定要解除合并吗？")){
		$.get("<%=root%>/xxbs/action/handling!closemerge.action?toId="+id+"&time="+time.getTime(), function(response){
			if(response == "success"){
				gl.msg(response, "解除合并成功");
			}
			else if (response == "nosuccess1"){
				alert("不是合并的文章不能解除绑定!");
			}
			else if (response == "nosuccess"){
				alert("已成刊的文章不能解除合并!");
			}
		});
	}
	}
});

$("#viewmerge").click(function(){
	var id = $("#list").jqSelectedId();
	if(id != false){
	var url = "<%=root%>/xxbs/action/handling!viewmerge.action?toId="+id;
	var ret = gl.showDialog(url,w,h);
	gl.msg(ret, "保存成功");
	}
});

$("#isOA").click(function(){
	var id = $("#list").jqSelectedId();
	$.get("<%=root%>/xxbs/action/handling!isPublish.action?toId="+id, function(response){
		if(response == "nosuccess"){
			alert("未成刊的文章不能网上发布!");
			return false;
		}
		else{
			if(id != false){
				var url = "<%=root%>/xxbs/action/handling!isOA.action?toId="+id;
				var ret = gl.showDialog(url,600,350);
				gl.msg(ret, "操作成功");
			}
		}
	});
});


</script>
<script type="text/javascript" src="<%=scriptPath%>/stopwait.js"></script>
