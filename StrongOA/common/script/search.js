$(document).ready(function() {
	$(document).keydown(function(event){ 
		if(event.keyCode == 13 && $(document.activeElement).hasClass("search")){
			search();
		}
	});
});	
