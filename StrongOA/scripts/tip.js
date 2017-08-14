/*******************************************************************************
 * 
 * 提示信息组件
 * 
 ******************************************************************************/

$.tip = function(element, message){
	this.element = element;
	this.message = message;
	this.$label = this.init();
}

$.extend($.tip, {
	prototype: {
		init: function(){
			$label = $("<DIV/>").addClass("tooltip callout9").css({//增加div样式
				//'position':"absolute",
				'left':"0px",
				'top':"0px",
				'display': "block"
			}).html(this.tipInitHtml(this.message) || "");//html代码为<Table>出错信息代码
			
			var element1 = this.element;
			//如果element后面是input、a、img元素则error label放在这些元素后面
			while($(element1).next()[0] && /INPUT|A|IMG/i.test($(element1).next()[0].tagName)){
				element1 = $(element1).next();
			}
			
			$label.appendTo($(document.body));
			
			var pos = $(element1).offset();//element1相对于视窗的位置
			var dim = {width:$(element1).outerWidth(), height:$(element1).outerHeight()};//element1的实际高度和宽度
			var dim2 = {width:$($label).width(), height:$($label).height()};//error label的高度和宽度
			var scrolltop = 0;
			var scrollleft = 0;
			var parentElement = $(element1);
			while(parentElement[0]){//element1的滚动宽度和高度
				scrolltop = scrolltop + parentElement[0].scrollTop;
				scrollleft = scrollleft + parentElement[0].scrollLeft;
				if(parentElement[0].tagName == "BODY"){
					break;
				}
				parentElement = $(parentElement.parent());
			}
			//先设置css一次，使得能够获取到准确的高度和宽度
			$label.css({'left':"" + (pos.left + dim.width + scrollleft), 'top':"" + (pos.top + scrolltop + dim.height/2 - dim2.height/2) });
			dim2 = {width:$($label).width(), height:$($label).height()};//重新获取error label的高度和宽度
			$label.css({'left':"" + (pos.left + dim.width + scrollleft), 'top':"" + (pos.top + scrolltop + dim.height/2 - dim2.height/2) });
			
			$label.show();
			
			alert($label[0].outerHTML);
			
			return $label;
		},
		
		tipInitHtml: function(message){
			var arr = [];
			arr.push("	<div class='tooltipfang'></div>");
			arr.push("  <table border='0' cellspacing='0' cellpadding='0' class='tooltiptable'>");
			arr.push("  	<tr><td class='cornerTop topleft'> </td><td class='topcenter'> </td>");
			arr.push("  			<td class='cornerTop topright'> </td></tr><tr><td class='corner bodyleft'> </td>");
			arr.push("				<td class='tooltipcontent'>" + message + "</td>");
			arr.push("				<td class='bodyright'> </td></tr>");
			arr.push("		<tr><td class='cornerTop footerleft'> </td><td class='footercenter'> </td>");
			arr.push("				<td class='cornerTop footerright'> </td></tr></table>");
			return arr.join('');
		},
		
		hide: function(){
			this.$label.hide();
		},
		
		show: function(){
			this.$label.show();
		},
		
		changeMessage: function(message){
			this.$label.html(this.tipInitHtml(this.message) || "");
		},
		
		remove: function(){
			this.$label.remove();
		}
	}
});