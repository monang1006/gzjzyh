/*
 * 常用工具集
 * Author:钟伟
 * Date:2012-2-8
 * 
 */
   var   xMax   =   screen.width;
             var   yMax   =   screen.height;  
             var xx=xMax/2-250;
             var yy=yMax/2+50;

var gl = {};

gl.resize = function(){
	resize();
	$(window).bind("resize", resize);
	
	function resize(){
 		var width = $(window).width()-4;
		$("#list").setGridWidth(width, true);
	}
};

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
	var mask0=window.top.frames['perspective_toolbar'].document.getElementById("mask_top");
	var mask1=window.top.frames['perspective_content'].document.getElementById("mask_content");
	mask0.style.visibility='visible';
	mask1.style.visibility='visible';
	var ret = showModalDialog(url, "", param);
	mask0.style.visibility='hidden';
	mask1.style.visibility='hidden';
	return ret;
};
gl.showmyDialog = function(url,width, height){
		
    var param = "dialogWidth:"+width+"px;dialogHeight:"+height+"px;";
    if($.browser.msie != true){
	    param = param +"dialogTop: "+xx+"; dialogLeft: "+yy+"; edge: Raised; center: Yes; help: Yes; resizable: Yes; status: Yes;";
}
	var ret=showModalDialog(url,window,param);
	
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
