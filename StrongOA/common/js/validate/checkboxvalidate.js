	//点击“全选”链接
function checkedAll() {
	var check = document.getElementsByName("checkall")[0];
	if (check.checked == false) {
		check.checked = true;
	} else {
		check.checked = false;
	}
	checkAll(checkall, document.getElementById("titleTable_td"), "#A9B2CA", true);
}
	//获取“chkbox”值	
	function getvalue() {
		var values = "";
		$("input:checked").each(function () {
			if (this.name == "chkButton") {
				values += "," + $(this).val();
			}
		});
		if (values.length > 1) {
			values = values.substring(1);
		}
		return values;
	}
	
	//验证复选框选择情况（没选/多选）
	function validateCheck() {
		var value = getvalue();
		if (value == "") {
			alert("请在列表中选择一项！");
			return false;
		} else {
			var values = value.split(",");
			if (values.length > 1) {
				alert("对不起，您只能选择一项！");
				return false;
			}
		}
		location=this.url+"?id="+value;
	}
	//仅仅验证选择框，不涉及逻辑
	function merelyValidateCheck(){
		var value = getvalue();
		if (value == "") {
			alert("请在列表中选择一项！");
			return false;
		} else {
			var values = value.split(",");
			if (values.length > 1) {
				alert("对不起，您只能选择一项！");
				return false;
			}
		}
		return true;
	}
	//验证弹出窗口
	function validateOpenWindow(url){
		var value = getvalue();
		if (value == "") {
			alert("请在列表中选择一项！");
			return false;
		} else {
			var values = value.split(",");
			if (values.length > 1) {
				alert("对不起，您只能选择一项！");
				return false;
			}
		}
		var ret=OpenWindow(url+"?id="+value,"400","300",window);
		return ret;
	}