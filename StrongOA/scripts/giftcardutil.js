/**
 * 格式化面额
 * @param decimal 面额
 */
function formatDecimal(decimal){
	if(decimal != null && decimal != "" && decimal != "null"){
		var index = decimal.indexOf(".");
		if(index == -1){//没有精确位
			return decimal + ".00";
		}else if(decimal.length - index -1 == 0){//如XXX.
			return decimal + "00";
		}else if(decimal.length - index -1 == 1){//如XXX.X
			return decimal + "0";
		}else if(decimal.length - index -1 > 2){//如XXX.XXX
			return decimal.substring(0, index + 3);
		}else{//如XXX.XX
			return decimal;
		}
	}
	return "";
}

	
/** 
 * 显示/隐藏加载信息
 * @param bl true/false
 */
function showLoading(bl){
	var state = bl ? "block" : "none";
	$("#LOADING")[0].style.display = state;
}

/**
 * 改变展示消息
 * @param message 展示消息
 */
function changeMessage(message){
	$("#showMessage").html(message);
}