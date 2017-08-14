/*
 * 常用工具集
 * Author:钟伟
 * Date:2012-2-8
 * 
 */



var gl = {};

gl.resize = function(){
	var width = $(document).width()-4;
	if (document.all){
		if(document.documentElement.scrollHeight > document.documentElement.clientHeight){
			width = width - 16;
		}
	}
	if($("#list")[0] != undefined){
		$("#list").jqGrid("setGridWidth", width, true);
	}
};
$(window).bind("resize", gl.resize);

gl.msg = function(ret, msg){
	if(ret == "success"){
		showActionTip(msg);
		reloadData();
		setTimeout(function(){hideActionTip();}, 3000);
	}
};

$.fn.extend({
	
	jqDeleteId : function(){
		var id = $(this).getGridParam('selarrrow');		
		/*if(id.length < 1){
			Error('请选择一项进行操作。');
			return false;
		}	
		else if(id.length >1){
			Error('一次只能选择一项。');
			return false;
		}*/
		return id;
	},
	
	jqSelectedId : function(){
		var id = $(this).getGridParam('selarrrow');		
		if(id.length < 1){
			Error('请选择一项进行操作。');
			return false;
		}	
		else if(id.length >1){
			Error('一次只能选择一项。');
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
	}
});

gl.showDialog = function(url, width, height){
	var param = "dialogWidth="+width+"px;dialogHeight="+height+"px;";
	if($.browser.msie != true){
		var top = (window.screen.availHeight - 20 - height) / 2;
		var left = (window.screen.availWidth - 10 - width) / 2;
		param = param + "dialogTop:"+ top + "px;dialogLeft:"+left+"px;";
	}
	/*var mask0=null;
	var mask1 = null;
	if(window.top.frames['perspective_toolbar']){
	 mask0=window.top.frames['perspective_toolbar'].document.getElementById("mask_top");
	 mask1=window.top.frames['perspective_content'].document.getElementById("mask_content");
	mask0.style.visibility='visible';
	mask1.style.visibility='visible';
	}*/
	
	var ret = showModalDialog(url, "", param);
	/*if(mask0){
	mask0.style.visibility='hidden';
	mask1.style.visibility='hidden';
	}*/
	return ret;
};

gl.showSubDialog = function(url, width, height){
	var param = "dialogWidth="+width+"px;dialogHeight="+height+"px;";
	if($.browser.msie != true){
		var top = (window.screen.availHeight - 20 - height) / 2;
		var left = (window.screen.availWidth - 10 - width) / 2;
		param = param + "dialogTop:"+ top + "px;dialogLeft:"+left+"px;";
	}
	var mask=document.getElementById("mask");
	mask.style.visibility='visible';
	var ret = showModalDialog(url, "", param);
	mask.style.visibility='hidden';
	return ret;
};

gl.showmyDialog = function(url, width, height){
	gl.showDialog(url, width, height);
};

gl.showsmgwDialog = function(url, width, height){
	gl.showDialog(url, width, height);
};

gl.windowWidth = function(){
	var w =	$(window).width();
	if(w > 800){
		w = 800;
	}
	else{
		w = w - 50;
	}
	return w;
};

gl.windowHeight = function(){
	var h = $(window).height();
	return h;
};

