<script type="text/javascript">
	if("${errorMsg}" != ""){
		alert("${errorMsg}");
	}else{
		var accId = parent.document.getElementById("attrId").value;
		parent.window.dialogArguments.impForm('${accountStr}', accId);
		parent.window.close();
	}
</script>