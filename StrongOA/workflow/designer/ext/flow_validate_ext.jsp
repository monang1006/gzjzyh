<%@ page contentType="text/html; charset=utf-8"%>
<script>
/**
 * 添加流程模型的自定义验证Filter
 * 1.如果验证正确，则接口返回true
 * 2.如果验证失败，则接口直接alert错误信息，并返回false
 */
processModel.prototype.validateFilters.push(function(){
	// TODO：流程模型自定义验证逻辑
	// 其中this获取的是流程中的节点和迁移线ID集合
	var nodeIds = this;
	var tips = "";
	var isOk = true;
	for (var i=0; i<nodeIds.length; i++) {
		var nodeId = nodeIds[i].value;
		var node = $("#"+nodeId);
		var nc = node.attr("nodeclass");
		var title = nc.thisCaption;
		
		if("task" == node.attr("ctlType")){
			var actors = node.attr("handleactor");
			$.ajax({
				type: 'POST',
				async:false,
				url: contextPath+"/workflowvalidate/workflowValidate!actorValidate.action",
				data: {strActor: actors},
				success: function(res){
					if(res){
						tips = tips + "\r\n" + title + "的处理人验证结果：\r\n\r\n" + res;
						isOk = false;
					}
				}
			});
			
			var plugins = node.attr("plugins");
			var pis = "";
			var forms = "";
			for(var f=1;f<=6;f++){
				var piId = plugins["plugins_flowId"+f];
				var formId = plugins["plugins_formId"+f];
				var piName = plugins["plugins_flowName"+f];
				var formName = plugins["plugins_formName"+f];
				
				if(piId && piName){
					pis = pis + piId + "," + piName + ",";
				}
				
				
				if(formId != 0 && formId && formName){
					forms = forms + formId + "," + formName + ",";
				}
			}
			
			if(pis){
				$.ajax({
					type: 'POST',
					async:false,
					url: contextPath+"/workflowvalidate/workflowValidate!processDefValidate.action",
					data: {strActor: pis},
					success: function(res){
						if(res){
							tips = tips + "\r\n" + title + "的流程验证结果：\r\n\r\n" + res;
						}
					}
				});
			}
			
			if(forms){
				$.ajax({
					type: 'POST',
					async:false,
					url: contextPath+"/workflowvalidate/workflowValidate!eformValidate.action",
					data: {strActor: forms},
					success: function(res){
						if(res){
							tips = tips + "\r\n" + title + "的表单验证结果：\r\n\r\n" + res;
						}
					}
				});
			}			
			
		}
		
		if("line" == node.attr("ctlType")){
			var plugins = node.attr("plugins");
			var actors = plugins.plugins_handleactor;
			$.ajax({
				type: 'POST',
				async:false,
				url: contextPath+"/workflowvalidate/workflowValidate!actorValidate.action",
				data: {strActor: actors},
				success: function(res){
					if(res){
						tips = tips + "\r\n" + title + "的验证结果：\r\n\r\n" + res;
						isOk = false;
					}
				}
			});
		}

		if("prostate" == node.attr("ctlType")){
			var plugins = node.attr("plugins");
			var orgId = plugins.plugins_NodeOrgId;
			var orgName = plugins.plugins_nodeDePartName;
			if(orgId && orgName){
				$.ajax({
					type: 'POST',
					async:false,
					url: contextPath+"/workflowvalidate/workflowValidate!orgValidate.action",
					data: {orgId: orgId,orgName: orgName},
					success: function(res){
						if(res){
							tips = tips + "\r\n" + title + "的验证结果：\r\n\r\n" + res;
							isOk = false;
						}
					}
				});
				
			}
		}
		
		if("initial" == node.attr("ctlType")){			
		}
		
		var isFork = isForkNode(nodeId);
		if(isFork){
			var existJoinNode = false;			
			var nextNode = nodePathTraversal();
			var nextIds = nextNode.getAllNextNodes(nodeId);
			for(var ni=0;ni<nextIds.length;ni++){
				if(isJoinNode(nextIds[ni])){
					existJoinNode = true;
					break;
				}
			}
			if(existJoinNode == false){
				tips = tips + "\r\n" + title + "是并发节点，所以该节点之后至少要有一个汇聚节点。\r\n\r\n";
			}
		}
	}
	if(tips){
		alert(tips);
	}
	return true;
/*
	if(isOk){
		return true;
	}
	else{
		alert(tips);
		return false;
	}*/
});

/**
 * 添加流程开始节点的自定义验证Filter
 * 1.如果验证正确，则接口返回true
 * 2.如果验证失败，则接口直接alert错误信息，并返回false
 */
startnode.prototype.validateFilters.push(function(){
	// TODO：流程开始节点的自定义验证Filter
	// 其中this获取的是开始节点ID
	var nodeId = this;
	var node = $("#"+nodeId);
	var line = node.attr("line");
	var nextNodeId = line.substring(line.indexOf("TO")+2);
	nextNodeId = nextNodeId.substring(0, nextNodeId.length-1);
	var nextNode = $("#"+nextNodeId);
	if("condition" == nextNode.attr("ctlType")){
		alert("开始节点之后不能是条件节点。");
		return false;
	}/*
	else if("task" == nextNode.attr("ctlType")){
		alert("第一个任务节点【草稿必填】功能是无效的。");
		return true;
	}*/
	else{
		return true;
	}
});

/**
 * 添加流程任务节点的自定义验证Filter
 * 1.如果验证正确，则接口返回true
 * 2.如果验证失败，则接口直接alert错误信息，并返回false
 */
tasknode.prototype.validateFilters.push(function(){
	// TODO：流程任务节点的自定义验证Filter
	// 其中this获取的是任务节点ID
	var nodeId = this;
	return true;
});

/**
 * 添加流程条件节点的自定义验证Filter
 * 1.如果验证正确，则接口返回true
 * 2.如果验证失败，则接口直接alert错误信息，并返回false
 */
conditionnode.prototype.validateFilters.push(function(){
	// TODO：流程条件节点的自定义验证Filter
	// 其中this获取的是条件节点ID
	var nodeId = this;
	//alert("流程条件节点的自定义验证Filter。");
	return true;
});

/**
 * 添加流程自定义节点的自定义验证Filter
 * 1.如果验证正确，则接口返回true
 * 2.如果验证失败，则接口直接alert错误信息，并返回false
 */
nodenode.prototype.validateFilters.push(function(){
	// TODO：流程自定义节点的自定义验证Filter
	// 其中this获取的是自定义节点ID
	var nodeId = this;
	//alert("流程自定义节点的自定义验证Filter。");
	return true;
});

/**
 * 添加流程子流程节点的自定义验证Filter
 * 1.如果验证正确，则接口返回true
 * 2.如果验证失败，则接口直接alert错误信息，并返回false
 */
processstatenode.prototype.validateFilters.push(function(){
	// TODO：流程子流程节点的自定义验证Filter
	// 其中this获取的是子流程节点ID
	var nodeId = this;
	//alert("流程子流程节点的自定义验证Filter。");
	return true;
});

/**
 * 添加流程结束节点的自定义验证Filter
 * 1.如果验证正确，则接口返回true
 * 2.如果验证失败，则接口直接alert错误信息，并返回false
 */
endnode.prototype.validateFilters.push(function(){
	// TODO：流程结束节点的自定义验证Filter
	// 其中this获取的是结束节点ID
	var nodeId = this;
	//alert("流程结束节点的自定义验证Filter。");
	return true;
});

/**
 * 添加流程迁移线的自定义验证Filter
 * 1.如果验证正确，则接口返回true
 * 2.如果验证失败，则接口直接alert错误信息，并返回false
 */
line.prototype.validateFilters.push(function(){
	// TODO：流程迁移线的自定义验证Filter
	// 其中this获取的是迁移线ID
	var transitionId = this;

	//alert("流程迁移线的自定义验证Filter。");
	return true;
});

</script>