$(function(){

	$(".select-event").click(function(){
		var ret = gl.showDialog(scriptroot+"/eventActions/action/event!tree.action", 300, 400);
		if(ret != undefined){
			var td = $(this).parent();
			td.children("input[event-attr=event-name]").val(ret.eventName);
			td.children("input[event-attr=event-class]").val(ret.eventClass);
		}
	});
	
	$(".clear-event").click(function(){
		var td = $(this).parent();
		td.children("input[event-attr=event-name]").val("");
		td.children("input[event-attr=event-class]").val("");
	});
	

	if(currentStatus == "edit"){
		$(".select-event").attr("disabled", true);
		$(".clear-event").attr("disabled", true);
	}
	else{
		$(".select-event").attr("disabled", false);
		$(".clear-event").attr("disabled", false);
	}
	
});
