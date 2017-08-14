<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="web" uri="/tags-web/component"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>通报管理</title>

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
	</head>

	<body>
		<script type="text/javascript" src="<%=scriptPath%>/wait.js"></script>
			<div class="main_up_out">
				<div class="main_up_out">
				<s:if test="%{submitStatus=='all'}">
				<div id="menu_top" style="width:auto;">
					<ul>
						<li>
							统计年份：<select name="year" id="year">
        		<option value="<s:property value="years[0]"/>"><s:property value="years[0]"/></option>
        		<option value="<s:property value="years[1]"/>"><s:property value="years[1]"/></option>
        		<option value="<s:property value="years[2]"/>"><s:property value="years[2]"/></option>
        		<option value="<s:property value="years[3]"/>"><s:property value="years[3]"/></option>
        		<option value="<s:property value="years[4]"/>"><s:property value="years[4]"/></option>
        		<option value="<s:property value="years[5]"/>" selected="selected"><s:property value="years[5]"/></option>
        		<option value="<s:property value="years[6]"/>"><s:property value="years[6]"/></option>
        		<option value="<s:property value="years[7]"/>"><s:property value="years[7]"/></option>
        		<option value="<s:property value="years[8]"/>"><s:property value="years[8]"/></option>
        		<option value="<s:property value="years[9]"/>"><s:property value="years[9]"/></option>
        		<option value="<s:property value="years[10]"/>"><s:property value="years[10]"/></option>
       	   </select>	
								
								
								
						</li>
						<li>
							统计月份：<select name="month" id="month">
        		<option value="01">1月</option>
        		<option value="02">2月</option>
        		<option value="03">3月</option>
        		<option value="04">4月</option>
        		<option value="05">5月</option>
        		<option value="06">6月</option>
        		<option value="07">7月</option>
        		<option value="08">8月</option>
        		<option value="09">9月</option>
        		<option value="10">10月</option>
        		<option value="11">11月</option>
        		<option value="12">12月</option>
       	   </select>
						</li>
						<li>
							<input type="button" name="report" id="report" value="生成通报"/>
						</li>
						<!-- <li>
							<button id="comment" class="input_button_4"><img src="<%=themePath%>/image/ico_view.gif"/>点评</button>
						</li> -->
						<li>
							<button id="edit" class="input_button_4"><img src="<%=themePath%>/image/ico_edit.gif"/>修改</button>
						</li>
						<li>
							<button id="delete" class="input_button_4"><img src="<%=themePath%>/image/ico_del.gif"/>删除</button>
						</li>
					</ul>

					<br style="clear:both" />
				</div>
				</s:if>
				<div id="gridDiv" class="grid" style="width:auto; display:block; padding: 1px;">
				<s:if test="%{submitStatus=='all'}">
					<!--<form theme="simple" id="myTableForm" onsubmit="return checkTD();" action="<%=root%>/xxbs/action/infoReport!word.action" method="post">
					<table width="100%" id="searchTable" border="0" cellpadding="0"
						cellspacing="1" class="main_search_border">
						<tr style="height:50px">
							<td width="100%" align="left" class="biao_bg1"
								style="padding-right:8px;padding-left:8px;">
								通报标题：
								<s:textfield name="tbTitle" id="tbTitle" style="width:276px"
									cssClass="main_search_input search" title="请输入通报标题"
									theme="simple"></s:textfield><br />
								开始时间：
								<s:textfield name="startDate" id="startDate"
									cssClass="main_search_input search" title="请输开始布时间"
									theme="simple"></s:textfield>
								<web:datetime format="yyyy-MM-dd" readOnly="true" id="startDate" />
								结束时间：
								<s:textfield name="endDate" id="endDate"
									cssClass="main_search_input search" title="请输入结束时间"
									theme="simple"></s:textfield>
								<web:datetime format="yyyy-MM-dd" readOnly="true" id="endDate" />		
								
								<button type="submit" style="float:none" class="input_button_6"><img src="<%=themePath%>/image/ico_add.gif"/>生成通报</button>
							</td>
						</tr>
					</table>-->
					</s:if>
					<s:else>
					<div id="menu_top" style="width:auto;">
					<ul>
						<li>
							<button id="view" class="input_button_4"><img src="<%=themePath%>/image/ico_edit.gif"/>查看</button>
						</li>
					</ul>
					
					<br style="clear:both" />
				</div>
					</s:else>
					<div id="gridDiv" class="grid" style="width:auto; display:block; padding: 1px;">
		<s:form theme="simple" id="myTableForm" action="">
					<table width="100%" id="searchTable" border="0" cellpadding="0"
						cellspacing="1" class="main_search_border">
						<tr>
							<td width="100%" align="left" class="biao_bg1"
								style="padding-right:8px;padding-left:8px;">
								通报标题：
								<s:textfield name="reTitle" id="reTitle"
									cssClass="main_search_input search" title="请输入通报标题"
									theme="simple"></s:textfield>
								<input type="button" value="搜索"  style="width: 50px" id="img_sousuo1">
							</td>
						</tr>
					</table>
					<div id="load" style="display: none" align="center"><img src="<%=themePath%>/image/tjtc.gif"/></div>
					<table id="list"></table>
					<div id="pager"></div>
		</s:form>
				</div>
					</form>
					
					<table id="list"></table>
					<div id="pager"></div>
				</div>
				</div>
				</div>

<div id="mask"></div>
	</body>
</html>
<script type="text/javascript">

var view = function(){
	var id = $("#list").jqSelectedId();
	if(id != false){
		var url = "<%=root%>/xxbs/action/infoReport!view.action?toId="+id;
		gl.showDialog(url,650,480);
	}
};

var edit = function(){
	var id = $("#list").jqSelectedId();
	if(id != false){
		location = "<%=root%>/xxbs/action/infoReport!input.action?toId="+id;
	}
};
$("#edit").click(edit);

var del = function(){
	var id = $("#list").jqSelectedId();
	if(id != false && confirm("确定要删除吗？")){
		$.get("<%=root%>/xxbs/action/infoReport!delete.action?toId="+id, function(response){
			if(response == "success"){
				gl.msg(response, "删除成功");
			}
		});
	}
};

$("#view").click(view);

$("#delete").click(del);

function checkTD(){
 		var tbTitle = $("#tbTitle").val();
 	 	var startDate = $("#startDate").val();
 	 	var endDate = $("#endDate").val();
 	 	if(tbTitle == ""){
 	 		alert("请输入标题。");
 	 		return false;
 	 	}
 	 	if(startDate == ""){
 	 		alert("请选择开始时间。");
 	 		return false;
 	 	}
 	 	if(endDate ==""){
 	 		alert("请选择结束时间。");
 	 		return false;
	 	}
 	 	if(startDate > endDate){
 	 		alert("开始时间不能大于结束时间。");
 	 		return false;
 	 	}

}


var option = {
	width : 150,
	items : [	{
		text : "修改通报",
		icon : "<%=themePath%>/image/ico_edit.gif",
		alias : "1-3",
		action : edit
	}, 
	{
		text : "删除通报",
		icon : "<%=themePath%>/image/ico_del.gif",
		alias : "1-2",
		action : del
	},
	{
		text : "查看通报",
		icon : "<%=themePath%>/image/ico_view.gif",
		alias : "1-1",
		action : view
	}
	]
};

var option1 = {
		width : 150,
		items : [	
		{
			text : "查看通报",
			icon : "<%=themePath%>/image/ico_view.gif",
			alias : "1-1",
			action : view
		}
		]
	};

$(function(){
	var month = "${month1}";
	 $("#month").find("option[text='"+month+"月']").attr("selected",true);
	
	var s = "${submitStatus}";
	if(s!=1){
	var rightMenu = function(rowid, iRow, iCol, e){
		$(this).resetSelection();
		$(this).setSelection(rowid);
		var menu = $.fn.contextmenu(option);
		menu.displayMenu(e, e.target);
	};
	}
	else{
	var rightMenu = function(rowid, iRow, iCol, e){
		$(this).resetSelection();
		$(this).setSelection(rowid);
		var menu = $.fn.contextmenu(option1);
		menu.displayMenu(e, e.target);
	};
	}
	$("#list").jqGrid({
		url:'${root}/xxbs/action/infoReport!showList.action',
		colModel :[ 
			{label:'rpId',name:'rpId', hidden:true}, 
			{label:'通报标题',name:'rpTitle'},
			{label:'时间',name:'rpDate',align:'center',width:30}
			],
		onRightClickRow: rightMenu,
		gridComplete: gl.resize,
		sortname: 'rpDate'
	});
			
	$("#img_sousuo1").click(function(){
  	 	var reTitle = $("#reTitle").val();
       	var searchParam = {};
	 	searchParam.reTitle = $.trim(reTitle);
       	searchParam.isSearch = true;
       	$("#list").setGridParam({postData: searchParam,page:1}).trigger("reloadGrid");		
	});
});



function reloadData(){
	$('#list').trigger("reloadGrid");
}


$("#report").click(function(){
	$("#load").css("display","block");
	$("#report").css("display","none");
	$("#edit").css("display","none");
	$("#delete").css("display","none");
	var year = $("#year option:selected").val();
	var month = $("#month option:selected").val();
	location.href = "<%=root%>/xxbs/action/infoReport!report.action?year="+year+"&month="+month;
	//gl.showDialog(url,650,800);
});


</script>
<script type="text/javascript" src="<%=scriptPath%>/stopwait.js"></script>
