/*******************************************************************************
 * 
 * mask页面遮蔽组件
 * 
 ******************************************************************************/
(function($) {
	$.extend({
		documentMask : function(options) {
			// 扩展参数
			var op = $.extend({
				opacity : 1,
				z : 999,
				icon : scriptroot + "/images/public/mask_loading.gif",
				maskObj : $(document.body),
				message : "<font color=#16387C><strong>数据加载中...</strong></font>"
			}, options);
			
			//创建一个 Mask 层，追加到 mask对象
			var obj = $("<div></div>");
			obj.appendTo(op.maskObj).addClass("mask").css({'z-index' : op.z});
			obj.attr('align', 'center');
			var obj2 = $("<DIV style='position:absolute;display:none' align='center'></div>");
			obj2.append("<IMG src='" + op.icon + "'>&nbsp;&nbsp;" + op.message);
			obj2.appendTo(obj[0]);
			var mleft = (obj.width() - obj2.width()) / 2;
			var mtop = (obj.height() - obj2.height()) / 2;
			obj2.css('left', mleft);
			obj2.css('top', mtop);
			obj2.show();
			obj.fadeIn('slow', function() {
				// 淡入淡出效果
				$(this).fadeTo('fast', op.opacity);
			});
			obj.close = function(){//Mask 被销毁
				$(this).fadeTo('fast', 0, function() {
					$(this).remove();
				});
			};
			return obj;
		}
	});
})(jQuery);