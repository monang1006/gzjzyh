	$(document).ready(function(){
		/**
		 * 处理按钮上的图片
		 */
		$(".input_bg").each(function(){
			if(this.icoPath !=null && this.icoPath !=""){
				var btnCss = {"background-image": "url('"+this.icoPath+"')","background-repeat":"no-repeat"};
				$(this).css(btnCss);
			}
		});
		
	});
	
	