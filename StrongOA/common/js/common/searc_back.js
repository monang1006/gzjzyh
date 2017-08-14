/**
	 * ????????????????????????
	 */
	function clearTooltip(){
		$("input:text").each(function(){
			if($(this).val() == this.title){
				$(this).val("");
			}
		});
	}
$(document).ready(function() {
	$(".search").each(function(){
	
	
		if($(this).attr('onclick') != null && $(this).attr("class")=="Wdate search"){
			var clickStr = $(this).attr('onclick').toString();
			var obj = eval('(' +  clickStr.substring(clickStr.indexOf('WdatePicker(') + 'WdatePicker('.length,clickStr.indexOf(')\n}')) + ')');
			var that = this;
			obj.onpicked  = obj.oncleared = function(){
				
				if($(that).val()==""){
					$(that).hide();
					$(that).next().show();
				}
			};
			
//			$(this).attr('onclick','').bind('click',function(){
//				WdatePicker(obj);
//			}) ;
//			$(this).attr('onclick','');
			
			$(this)[0].onclick = function(){
					WdatePicker(obj);
					if($(this).attr("class")=="Wdate search"){
						var that = this;
						setTimeout(function(){
							if($('div[lang][skin]').css('display') != "none"){
								setTimeout(arguments.callee,500);
							}else
								if($(that).val()==""){
									$(that).hide();
									$(that).next().show();
							}
						},500);
					}
			};
			
		}
		$(this).hide();
		//this.title=$(this).val();
		//$(this).val("");
		var htmltxt = $(this).parent().html();
		var divhtml = "<div class='searchinputdiv' align='left'>"+this.title+"</div>";
		$(this).parent().append(divhtml);
		//alert($(this).parent().html());
		if($(this).val()!=""){
			$(this).show();
			$(this).next().hide();
		}
		
	});
	
	  $(".search").keypress(function(){
		    if(event.keyCode==13) {
		      $("#img_sousuo").click();
		      $("#img_search").click();
		    }
		  });
	
	//???????????????????????????
	$(".searchinputdiv").focus(function(){
		$(this).hide();
		var searchtxt = $(this).prev();
		searchtxt.show();
		searchtxt.focus();
		if(searchtxt.attr("class")=="Wdate search"){
			searchtxt.click();
		}
		if(searchtxt.val() == searchtxt.title){
			searchtxt.val("");
		}
	});
	$(".search").blur(function(){		
		if($(this).val()==""){
			if($(this).attr("class")=="Wdate search"){
				
			}else{
				$(this).hide();
				$(this).next().show();	
			}
		}
	});
	$(".Wdate").change(function(){
		if($(this).attr("class")=="Wdate search"&&$(this).val()==""){
			$(this).hide();
			$(this).next().show();	
		}else{
			$(this).show();
			$(this).prev().hide();	
		}
	});
});	
