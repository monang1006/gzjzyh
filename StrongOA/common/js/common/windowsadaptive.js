$(document).ready(function() {
	
	  var id=$("#contentborder table:first");
	  var width=$(window).width();
	  if(width<765){
		  id.css("width","805px");
	  }
});

$(window).resize(function(){
	 var id=$("#contentborder table:first");
	  var width=$(window).width();
	  if(width<765){
		  id.css("width","805px");
	  }else{
		  id.css("width","100%");
	  }
});