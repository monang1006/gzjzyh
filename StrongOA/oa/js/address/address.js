$(document).ready(function(){
	var td_title = $("#title");
	if(null!=td_title && $("#groupId").val()!=""){
		td_title.text("重命名联系组");
		//document.title="个人通讯录-重命名联系组";
	}
	
	//添加分组
	$("#btnOK").click(function(){
		if($.trim($("#groupName").val()) == ""){
			alert("分组名称不能为空！");
			$("#groupName").focus();
			return false;
		}
		var url = $("form").attr("action");
		var id= $("#groupId").val();
		if(id != ""){
			url += "?model.addrGroupId="+id;
		}
		$.ajax({
			type:"post",
			url:url,
			data:{
				"model.addrGroupName":$("#groupName").val(),
				"model.addrGroupRemark":$("#groupDesc").val()
			},
			success:function(data){
			//	alert(data);
				window.returnValue="suc";
				window.close();
			},
			error:function(){
				alert("对不起，操作出错！");
				window.returnValue="fail";
				window.close();
			}
		});
	});
	//关闭窗口
	$("#btnCancel").click(function(){window.close();});
	//添加联系人
	$("#hrfNewPerson").click(function(){
		var groupName = $("#groupName").val();
		if(groupName.indexOf("addGroup()")!=-1){//没有组
			alert("添加联系人之前请先建立组！");
			return ;
		}
		var ret=OpenWindow(this.url,"450","295",window);
		if("sucess"==ret && ret!=undefined){
			parent.document.location=scriptroot+"/fileNameRedirectAction.action?toPage=address/address-personal.jsp" ;
		}
	});
	//查看联系人
	$("#hrfView").click(function(){
		var id = getValue();
		if(id == ""){
			alert("请选择要查看的联系人！");
			return ;
		}else{
			var ids = id.split(",");
			if(ids.length>1){
				alert("一次只能查看一个联系人详细信息！");
				return ;
			}
		}
		var ret=OpenWindow(this.url+"?id="+getValue(),"450","295",window);
		if(ret!=undefined && "sucess"==ret){
			parent.document.location=scriptroot+"/fileNameRedirectAction.action?toPage=address/address-personal.jsp" ;
		}
	});
	//创建分组
	$("#hrfNewGroup").click(function(){
		var ret=OpenWindow(this.url,"300","170",window);
		if(ret!=undefined && "suc"==ret){
			parent.project_work_tree.location=scriptroot+"/address/addressGroup.action";
		}
	});
	//删除
	$("#hrfDel").click(function(){
		var id = getValue();
		if(id == ""){
			alert("请选择要删除的联系人！");
		}else{
			if(confirm("确定要删除选定的联系人吗？")){
				$.ajax({
					type:"post",
					url:this.url,
					data:{id:getValue()},
					success:function(data){
					//	alert(data);
						//parent.project_work_tree.location=scriptroot+"/address/addressGroup.action";
                        //document.location.reload();
						parent.document.location=scriptroot+"/fileNameRedirectAction.action?toPage=address/address-personal.jsp" ;
					},
					error:function(){
						alert("对不起，操作出错！");
					}
				});
			}
		}

	});
	//导入导出
	$("#hrfExport").click(function(){
		var groupName = $("#groupName").val();
		if(groupName.indexOf("addGroup()")!=-1){//没有组
			alert("对不起，您的联系人列表中无人员。操作无效！");
			return ;
		}
		var ret=OpenWindow(this.url+$("#groupId").val()+"&groupName="+encodeURI(encodeURI(groupName)),"400","275",window);
	});
	$("#hrfImport").click(function(){
		var groupName = $("#groupName").val();
		if(groupName.indexOf("addGroup()")!=-1){//没有组
			alert("对不起，导入人员之前请先建立组！");
			return ;
		}
		var ret=OpenWindow(this.url,"400","275",window);
		if(ret!=undefined && "suc" == ret){
			parent.document.location=scriptroot+"/fileNameRedirectAction.action?toPage=address/address-personal.jsp" ;
		}
	});
	//导入导出页面功能
	var btnPrev = $("#btnPrev");
	var btnNext = $("#btnNext");
	var nextContent = $("#nextContent");
	var exportcontent = $("#exportcontent");
	var div_select = $("#div_select");
	var btnSelectAll = $("#btnSelectAll");
	var btnClear = $("#btnClear");
	var l_title = $("#l_title");
	//上一步
	if(btnPrev){
		btnPrev.attr("disabled","disabled");
		btnPrev.click(function(){
			$(this).attr("disabled","disabled");
			btnNext.get(0).value='  下一步';
			nextContent.hide();
			exportcontent.show();
			div_select.hide();
			l_title.text("联系组【"+$("#groupName").val()+"】导出CSV文件格式");
		});
	}
	//下一步
	if(btnNext){
		btnNext.click(function(){
			if(btnNext.get(0).value=='  完  成'){
				var column = "";
				var ext = "";
				ext = $(":radio:checked").val();
				$(":checkbox:checked").each(function(){
					column += this.value +",";
				});
				if(column.length>0){
					column = column.substring(0,column.length-1);
				}else{
					alert("请至少勾选一项！");
					return false;
				}
				$("#headerColumn").val(column);
				$("#exportExt").val(ext);
				$("form").submit();
				//window.close();
				/*$.ajax({
					type:"post",
					url:$("form").attr("action"),
					data:{
						headerColumn:column,
						exportExt:ext
					},
					success:function(data){
						if(data.indexOf("成功")!=-1){
							alert(data);
							window.close();
						}else{
							alert(data);
						}
						
					},
					error:function(){
						alert("对不起,导出失败.请重试或与管理员联系.");
						window.close();
					}
				});*/
			}else{
				btnPrev.attr("disabled",'');
				btnNext.get(0).value='  完  成';
				nextContent.show();
				exportcontent.hide();
				div_select.show();
				l_title.text("请选择输出字段：");
				
			}
		});
	}
	//全选
	btnSelectAll.click(function(){
		$(":checkbox").each(function(){
			$(this).attr("checked","checked");
		});
	});
	//清空
	btnClear.click(function(){
		$(":checkbox").each(function(){
			$(this).attr("checked","");
		});
	});
	//搜索
	$("#img_search").click(function(){
		$("form").submit();
	});
});