/*
 * 常用工具集
 * Author:钟伟
 * Date:2012-2-8
 * 
 */

var gl = {};

gl.resize = function(_this){
	resize();
	$(window).bind("resize", resize);
	
	function resize(){
 		var width = $(window).width();
 		if(width <= 0){
 			width = $(document).width();
 		}
 		width = width - 2;
		$(_this).setGridWidth(width, true);
	}
};

gl.msg = function(ret, msg){
	if(ret == "success"){
		if(msg){
			alert(msg);
		}
		reloadData();
	}
};

jQuery.fn.extend({
	jqSelectedId : function(){
		var id = $(this).getGridParam('selarrrow');		
		if(id.length < 1){
			alert('请选择要编辑的记录。');
			return false;
		}	
		else if(id.length >1){
			alert('只可以编辑一条记录。');
			return false;
		}
		return id;
	},
	jqSelectedIds : function(){
		var id = $(this).getGridParam('selarrrow');		
		if(id.length < 1){
			alert('请选择要删除的记录。');
			return false;
		}	
		return id;
	},
	jqLastId : function(){
		var id = $(this).getGridParam('selarrrow');
		if(id.length > 1){
			id = id[id.length-1];
		}
		return id;
	},
	jqHideTopbar:function(){
		var id = $(this).attr("id");
		$("#gbox_"+id).find(".ui-jqgrid-hdiv").hide();
	},
	jqShowTopbar:function(){
		var id = $(this).attr("id");
		$("#gbox_"+id).find(".ui-jqgrid-hdiv").show();
	}
});

gl.showDialog = function(url, width, height, args){
	var param = "dialogWidth="+width+"px;dialogHeight="+height+"px;";
	if($.browser.msie != true){
		var top = (window.screen.availHeight - 20 - height) / 2;
		var left = (window.screen.availWidth - 10 - width) / 2;
		param = param + "dialogTop:"+ top + "px;dialogLeft:"+left+"px;";
	}
	var ret = showModalDialog(url, args, param);
	return ret;
};

gl.fmtSize = function(val, opts, obj){
	if(val == null || val == undefined ){
		return "";
	}
	if(val.length > opts.colModel.maxlen){
		return "<div title=\""+val+"\">"+val.substring(0, opts.colModel.maxlen)+"...</div>";
	}
	return val;
};

gl.filterInput = function(formId){
	var filter = function(str){
		str = str.replace(/</g, "《");
		str = str.replace(/>/g, "》");
		str = str.replace(/'/g, "‘");
		str = str.replace(/\"/g, "‘");
		str = str.replace(/\\/g, "＼");
		str = str.replace(/\//g, "／");
		str = str.replace(/&/g, "﹠");
		str = str.replace(/;/g, "；");
		return str;
	};
	$("#"+formId).find("input[filtered=true], textarea[filtered=true]").each(function(){
		$(this).val(filter($(this).val()));
	});
};
