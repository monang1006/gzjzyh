/*
 * jqGrid4
 */

document.write('<script type="text/javascript" src="' + jsroot + '/jqgrid/grid.locale-cn.js"></script>');

document.write('<script type="text/javascript" src="' + jsroot + '/jqgrid/jquery.jqGrid.min.js"></script>');

document.write('<link rel="stylesheet" type="text/css" href="' + jsroot + '/jqgrid/ui.jqgrid.css" />');

var globalJqgrid = {};

globalJqgrid.goPager = function(_this){
	var page = $(_this).parent().find("input[class=ui-pg-input]").val();
	var pg_totalpage = $("#pg_totalpage").text();
	if(parseInt(page) > parseInt(pg_totalpage)){
		alert("设置的页数超出总页数范围，请重新设置！");
		return;
	}
	var id = $(_this).parents("div[id^=gbox_]").attr("id");
	if(id){
		id = id.replace("gbox_", "");
		$("#"+id).setGridParam({page:page}).trigger("reloadGrid");
	}
};

globalJqgrid.goPageUnit = function(_this){
	var page = $(_this).parent().find("input[class=ui-pg-input]").val();
	var id = $(_this).parents("div[id^=gbox_]").attr("id");
	if(id){
		id = id.replace("gbox_", "");
		$("#"+id).setGridParam({rowNum:page,page:1}).trigger("reloadGrid");
	}
};

globalJqgrid.fillRows = function(_this){
	var tds = $(_this).parent().find(".jqgfirstrow").html();
	tds = tds.replace(/HEIGHT: 0px;/g, "");			
	var tr = $("<tr class='ui-widget-content jqgrow ui-row-ltr'>"+tds+"</tr>");			
	var col = $(_this).getCol(0);
	var curRowNum = col.length;			
	for(var i=0;i<(10-curRowNum);i++){
		$(_this).append(tr.clone());
	}
	
	var pg_info = $("#pg_info_go");
	var pg_sel = $("#pg_sel_go");
	var pg_go = $("#pg_go_into");
	var pg_totalpage = $("#pg_totalpage").text();
	
	pg_sel.parent().parent().parent().parent().attr("style", "width:590px");
	pg_sel.parent().parent().parent().parent().next().attr("style", "width:0px");
	
	pg_sel.parent().append(pg_go);
	if($("#pg_tp_number").length < 1){
		pg_sel.parent().prepend("<td id='pg_tp_number'></td>");
		pg_go.append("<span id='pg_tp_info'></span>");
	}
	var curpage = pg_go.find("input[class=ui-pg-input]").val();
	$("#pg_tp_number").html("&nbsp;&nbsp;&nbsp;当前 "+curpage+"/"+pg_totalpage + "页 ");
	
	var tNum = pg_info.find("#pg_totalrow").text();
	if(!tNum){
		tNum = 0;
	}
	$("#pg_tp_info").html("共"+tNum+"条数据");
	
	pg_sel.hide();
	
	$("#pg_unit_input").val($(_this).getGridParam("rowNum"));
	
	//pg_info.parent().prev().remove();
	//pg_info.parent().attr("colspan", 2);
	
	$("input[class=ui-pg-input]").keyup(function(){
        var tmptxt=$(this).val();     
        $(this).val(tmptxt.replace(/\D|^0/g,''));     
    }).bind("paste",function(){    
        var tmptxt=$(this).val();     
        $(this).val(tmptxt.replace(/\D|^0/g,''));     
    }).css("ime-mode", "disabled");
	
	pg_sel.parent().find(".ui-pg-button").show();
	pg_sel.parent().find(".ui-state-disabled").hide();
	
};

globalJqgrid.propName = function(rows){
	var ret = "";
	var one = 0;
	for(var prop in rows){
		ret = prop;
		if(++one == 1) break;
	}
	return ret;
};

globalJqgrid.processData = function(data){
	var rows = data.rows;
	var kIdName = globalJqgrid.propName(rows[0]);
	for(var i=0;i<rows.length;i++){	
		var kId = rows[i][kIdName];
		rows[i].treeChildsId = [];
		
		for(var j=0;j<rows.length;j++){
			if(kId == rows[j].parent){
				rows[i].treeChildsId.push(rows[j][kIdName]);
			}
		}
		
		if(rows[i].treeChildsId.length > 0){
			rows[i].isLeaf = false;
		}
		else{
			rows[i].isLeaf = true;
		}
	}
	
	
	function rowById(id){
		for(var ic=0;ic<rows.length;ic++){
			if(id == rows[ic][kIdName]){
				return rows[ic];
			}
		}
	}
	
	var rowList = [];
	var level = 0;
	
	function buildLevel(ro){
		++level;
		var childs = ro.treeChildsId;
		if(childs != undefined){
			for(var ib=0;ib<childs.length;ib++){
				var row = rowById(childs[ib]);
				row.level = level;
				rowList.push(row);
				arguments.callee(row);
				--level;
			}
		}
	}
	
	for(var ia=0;ia<rows.length;ia++){
		if(rows[ia].parent == "" || rows[ia].parent == null || rows[ia].parent == "0"){
			rows[ia].level = 0;
			rowList.push(rows[ia]);
			buildLevel(rows[ia]);
		}
	}
	
	data.rows = rowList;
};

$(function(){
		
	$.extend(jQuery.jgrid.defaults, {
	    datatype: 'json',
	    mtype: 'POST',
	    prmNames:{page:'curpage',rows:'unitpage'},
	    jsonReader : {
	    	root: "rows",
	        page: "curpage", 
	        total: "totalpages", 
	        records: "totalrecords", 
	        repeatitems: false,
	        id: "0"
	     },
	    multiselect:true,
	    rownumbers:false,
	    pager: '#pager',
	    pagerpos: 'left',
	    recordpos: 'right',
	    viewrecords:true,
	    rowNum:10,
	    rowList:[5,10,20,30],
	    autowidth: true,
	    height:'301',
	    sortorder: 'desc',
	    altRows:true,
	    altclass:'altRow',
	    hoverrows:false,
	    gridview: true,
	    isTips:false
	});
	
});
