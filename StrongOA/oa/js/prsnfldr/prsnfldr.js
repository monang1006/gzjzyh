$(document).ready(function() {

	//验证
	function validateOpt(buttonTitle,onlyOne){
		//alert(1);
		if(buttonTitle.indexOf("除")!=-1){
			buttonTitle="删除";
		}
		var id = getValue();
		if(id == ""){
			alert("请选择要"+buttonTitle+"的记录。");
			return false;
		}else{
			if(onlyOne){
				var ids = id.split(",");
				if(ids.length>1){
					alert("一次只能查阅一个记录。");
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 删除文件
	 */
	$("#hrfDel").click(function(){
		var id = getValue();
		if(validateOpt($(this).text(),false)){
			if(confirm("确定要删除吗？")){
				$.ajax({
					type:"post",
					url:this.url,
					data:{
						id:id				
					},
					success:function(data){
						if(data!=""){
							alert(data);
						}else{
						//	alert("删除成功!");
							parent.project_work_content.document.location.reload() ;
						}
					},
					error:function(data){
						alert("对不起，操作异常"+data);
					}
			   });
			}
		}
	});

	/**
	 * 复制文件
	 */
	$("#hrfCopy").click(function(){
		copyFile(this.url);
	});
	
	/**
	 * 剪切文件
	 */
	$("#hrfCut").click(function(){
		cutFile(this.url);
	});
	
	//~~~~~~~~~~~~~~~~~~prsnfldrFolder-init.jsp~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	/**
	 * 处理Action的返回消息
	 */
	var message = $(".actionMessage").text();
	if(message!=null && message!=""){
		//alert(message);
		if(message == "ok"){
			window.returnValue = "sucess";
			window.close();
		}else{
			alert(message);
			window.returnValue = "None";
			//window.close();
		}
	}
	/**
	 * 点击取消按钮
	 */
	$("#btnCancel").click(function(){window.close();});
	
	/**
	 * 判断选择框是否需要选中
	 */
	var isShare = $("#isShare").val();
	if(isShare!=null && isShare!=""){
		if(isShare == "YES"){
			$("#chk_isShare").attr("checked","checked");
			if($("#orguserid").val() == ""){
				$("#orgusername").val("");
			}
		}
	}
	

	/**
	 * 共享文件夹
	 */
	$("#btnOK").click(function(){
		var chk_isShare = $("#chk_isShare").attr("checked");
		var url = $("form").attr("action");
		if(isShare == "YES"){//已经是共享状态
			if(chk_isShare){
				$.post(
					url+"?chkIsShare="+$("#chk_isShare").val(),
					{
						folderId:$("#folderId").val(),
						userId:$("#orguserid").val(),
						folderProp:getProp()
					},
					function(data){
						if(data == "0"){
							window.returnValue = "donothing";//直接关闭窗口
							window.close();
						}else{
							alert("非法操作。");
							window.returnValue = "fail";
							window.close();
						}
					}
				);
				
			}else{//取消共享
				$.post(
					url,
					{
						folderId:$("#folderId").val()
					},
					function(data){
						if(data == "1"){
							window.returnValue = "cancelShare";
							window.close();
						}else{
							alert("非法操作。");
							window.returnValue = "fail";
							window.close();
						}
					}
				);
			}
		}else{//非共享状态
			if(chk_isShare){//共享文件夹
				$.post(
					url+"?chkIsShare="+$("#chk_isShare").val(),
					{
						folderId:$("#folderId").val(),
						userId:$("#orguserid").val(),
						userName:encodeURI($("#orgusername").val()),
						folderProp:getProp()
					},
					function(data){
						if(data == "2"){
							window.returnValue="share";
							window.close();
						}
					}
				);
			}else{
				window.returnValue = "donothing";//直接关闭窗口
				window.close();
			}
		}
	});
	
	//搜索--自动完成
	var search = $("#search1");
	if(search && undefined!=search.val()){
		if($("#folderName").val() != "/" && $("#folderName").val().indexOf("addFolder()")==-1){
			search.autocomplete(search.attr("url")+"?folderId="+$("#folderId").val(), {
				width: 200,
				max:10,
				selectFirst: true,
				scroll:true
			});
		}	
	}
	
	//共享文件时选择接收人
	$("#addPerson").click(function(){
		if($("#chk_isShare").attr("checked")){
			if($("#chk_isShareAllPeople").attr("checked")||$("#orguserid").val()=='allPeople'){
				alert("您已选择所有人，如需重新选择，请先取消选择所有人。");
				return;
			}else{
				var ret=OpenWindow(this.url,"600","400",window);
			}
		}
		else{
			alert("需先选择[共享此文件夹]才能选择人员。");
			return;
		}
	 });
	
});

/**
 * 文件复制
 */
function copyFile(url){
	var id = getValue();
		if(id!=""){
			
			$.ajax({
					type:"post",
					url:url,
					data:{
						id:id				
					},
					success:function(data){
						showTip("<div class=\"tip\" id=\"loading\">操作成功</div>");
						if(data == null || data == ""){
						//	alert("文件复制成功！您可以执行粘贴操作了！"); 					
						}else{
							alert(data);
						}
					},
					error:function(data){
						alert("对不起，操作异常"+data);
					}
			   });
		}else{
			alert("请选择要复制的记录。");
		}
}
/**
 * 文件剪切
 * @param {} url
 */
function cutFile(url){
	var id = getValue();
		if(id!=""){
			$.ajax({
					type:"post",
					url:url,
					data:{
						id:id				
					},
					success:function(data){
						showTip("<div class=\"tip\" id=\"loading\">操作成功</div>");
						if(data!=""){
							alert(data);
						}else{
						//	alert("文件剪切成功！您可以执行粘贴操作了！"); 
						}
					},
					error:function(data){
						alert("对不起，操作异常"+data);
					}
			   });
		}else{
			alert("请选择要剪切的记录。");
		}
}
/**
 * 文件粘贴
 * @param {} url
 */
function parseFile(url){
	show("正在复制文件...");
	$.ajax({
			type:"post",
			url:url,
			data:{
				folderId:$("#folderId").val()			
			},
			success:function(msg){
				hidden();
				if(msg!=""){
						alert(msg); 
				}else{
						parent.project_work_content.document.location.reload() ;
				}
			},
			error:function(data){
				alert("对不起，操作异常"+data);
			}
	   });
}
//获取共享文件夹属性
function getProp(){
	var prop = "";
	if($("#chk_readonly").attr("checked") == true){
		prop += $("#chk_readonly").val();
	}
	if($("#chk_readwrite").attr("checked") == true){
		prop += $("#chk_readwrite").val();
	}
	return prop;
}