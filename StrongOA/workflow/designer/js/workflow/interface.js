/**
 * 流程设计器公共接口，主要提供给业务系统自定义验证使用
 */

/**
 * 得到指定节点/迁移线上扩展属性值
 * 
 * @param nodeId -节点/迁移线Id
 * @param pluginAttrName -扩展属性名称
 * @param designMainWindow -设计器主窗口引用，若当前已是主窗口，则为null
 * @returns 扩展属性值
 */
function getPluginAttrValue(nodeId, pluginAttrName, designMainWindow){
	var pluginAttrValue = null;
	var currDocument = document;
	if(designMainWindow != null){
		currDocument = designMainWindow.document;
	}
	if(nodeId != undefined && nodeId != null && nodeId != ""){
		var nodeElement = currDocument.getElementById(nodeId);
		if(nodeElement != undefined && nodeElement != null){
			pluginAttrValue = nodeElement.getAttribute("plugins")[pluginAttrName];
		}
	}
	return pluginAttrValue;
}

/**
 * 得到子流程节点上设置的子流程和表单信息
 * 
 * @param nodeId -子流程节点Id
 * @param designMainWindow -设计器主窗口引用，若当前已是主窗口，则为null
 * @returns 子流程和表单设置信息<br>
 * <P>
 * 返回信息对象为：<br>
 * {<br>
 * 		subProcessName："子流程名称",<br>
 * 		subProcessFormId："子流程对应表单Id，0表示对应子流程启动表单",<br>
 * 		subProcessFormName："子流程对应表单名称"<br>
 * }
 * </P>
 */
function getSubprocessSetting(nodeId, designMainWindow){
	var subProcessSetting = null;
	var currDocument = document;
	if(designMainWindow != null){
		currDocument = designMainWindow.document;
	}
	if(nodeId != undefined && nodeId != null && nodeId != ""){
		var nodeElement = currDocument.getElementById(nodeId);
		if(nodeElement != undefined && nodeElement != null){
			var node = nodeElement.nodeclass;
			// 该节点是子流程节点
			if(node.nodetype == CNST_CTLTYPE_PROSTATE){
				subProcessSetting = {};
				subProcessSetting['subProcessName'] = nodeElement.getAttribute("subProcessName");
				var subProcessForm = nodeElement.getAttribute("subProcessForm");
				if(subProcessForm != undefined && subProcessForm != null && subProcessForm != ""){
					var formSplit = subProcessForm.split(",");
					if(formSplit.length > 1){
						subProcessSetting['subProcessFormId'] = formSplit[1];
						subProcessSetting['subProcessFormName'] = formSplit[0];
					}
				}
			}
		}
	}
	return subProcessSetting;
}

/**
 * 得到任务节点上设置的任务处理人信息
 * 
 * @param nodeId -任务节点Id
 * @param designMainWindow -设计器主窗口引用，若当前已是主窗口，则为null
 * @returns 任务处理人信息
 */
function getTaskActorSetting(nodeId, designMainWindow){
	var taskActorSetting = null;
	var currDocument = document;
	if(designMainWindow != null){
		currDocument = designMainWindow.document;
	}
	if(nodeId != undefined && nodeId != null && nodeId != ""){
		var nodeElement = currDocument.getElementById(nodeId);
		if(nodeElement != undefined && nodeElement != null){
			var node = nodeElement.nodeclass;
			// 该节点是任务节点
			if(node.nodetype == CNST_CTLTYPE_TASK){
				// 任务节点处理人设置
				var setting = nodeElement.getAttribute("handleActor");
				if(setting != undefined && setting != null && setting != ""){
					taskActorSetting = setting.split("\\|");
				}
			}
		}
	}
	return taskActorSetting;
}

/**
 * 得到指定节点的表单设置信息
 * 
 * @param nodeId -节点ID
 * @param designMainWindow -设计器主窗口引用，若当前已是主窗口，则为null
 * @returns 节点设置的表单信息<br>
 * <P>
 * 返回信息对象为：<br>
 * {<br>
 * 		formId："表单Id，0表示对应流程启动表单",<br>
 * 		formName："表单名称"<br>
 * }
 * </P>
 */
function getNodeForm(nodeId, designMainWindow){
	var nodeForm = null;
	var currDocument = document;
	if(designMainWindow != null){
		currDocument = designMainWindow.document;
	}
	if(nodeId != undefined && nodeId != null && nodeId != ""){
		var nodeElement = currDocument.getElementById(nodeId);
		if(nodeElement != undefined && nodeElement != null){
			var form = nodeElement.getAttribute("nodeForm");
			if(form != undefined && form != null && form != ""){
				var formSplit = form.split(",");
				if(formSplit.length > 1){
					nodeForm = {};
					nodeForm['formId'] = formSplit[1];
					nodeForm['formName'] = formSplit[0];
				}
			}
		}
	}
	return nodeForm;
}

/**
 * 得到流程启动表单信息
 * 
 * @param designMainWindow -设计器主窗口引用，若当前已是主窗口，则为null
 * @returns 节点设置的表单信息<br>
 * <P>
 * 返回信息对象为：<br>
 * {<br>
 * 		formId："表单Id，0表示流程不需设置启动表单",<br>
 * 		formName："表单名称"<br>
 * }
 * </P>
 */
function getProcessForm(designMainWindow){
	var processFormSetting = null;
	if(designMainWindow == null){
		designMainWindow = window;
	}
	var form = window.processForm;
	if(form != undefined && form != null && form != ""){
		var forms = form.split(",");
		if(forms.length > 1){
			processFormSetting = {};
			processFormSetting["formId"] = forms[1];
			processFormSetting["formName"] = forms[0];
		}
	}
	return processFormSetting;
}

/**
 * 判断指定节点是否是并发节点
 * 
 * @param nodeId -节点ID
 * @param designMainWindow -设计器主窗口引用，若当前已是主窗口，则为null
 * @returns true|false
 */
function isForkNode(nodeId, designMainWindow){
	var isFork = false;
	var currDocument = document;
	if(designMainWindow != null){
		currDocument = designMainWindow.document;
	}
	if(nodeId != undefined && nodeId != null && nodeId != ""){
		var nodeElement = currDocument.getElementById(nodeId);
		if(nodeElement != undefined && nodeElement != null){
			var nextTranType = nodeElement.getAttribute("nextTranType");
			if(nextTranType != undefined && nextTranType != null && nextTranType == "1"){
				isFork = true;
			}
		}
	}
	return isFork;
}

/**
 * 判断指定节点是否是汇聚节点
 * 
 * @param nodeId -节点ID
 * @param designMainWindow -设计器主窗口引用，若当前已是主窗口，则为null
 * @returns true|false
 */
function isJoinNode(nodeId, designMainWindow){
	var isJoin = false;
	var currDocument = document;
	if(designMainWindow != null){
		currDocument = designMainWindow.document;
	}
	if(nodeId != undefined && nodeId != null && nodeId != ""){
		var nodeElement = currDocument.getElementById(nodeId);
		if(nodeElement != undefined && nodeElement != null){
			var preTranType = nodeElement.getAttribute("preTranType");
			if(preTranType != undefined && preTranType != null && preTranType == "1"){
				isJoin = true;
			}
		}
	}
	return isJoin;
}

/**
 * 获取某节点之后的节点信息
 * by zw
 * 
 * 方法一：getPaths(nodeId)
 * 		获取给定节点nodeId后的路径信息；
 *     返回值：路径对象，包含两个属性：fullPaths和partPaths。
 *              fullPaths是全路径，从给定节点到结束节点的路径；
 *              partPaths是部分路径，从给定节点到回路重复节点的路径，结合fullPaths可以求出全路径。
 *              fullPaths和partPaths可以求出所有路径。
 *              
 * 方法二：getAllNextNodes(nodeId)
 *     获取给定节点nodeId后的所有节点；
 *     返回值：节点数组。
 * 
 * 
 * 使用方法：
 * +++++++++++++++++++++++++++++++++++++
 * +
 * + var nextNode = nodePathTraversal();
 * +
 * + var paths = nextNode.getPaths(nodeId);
 * + alert(paths.fullPaths);
 * + alert(paths.partPaths);
 * +
 * + var nodes = nextNode.getAllNextNodes(nodeId);
 * + alert(nodes);
 * +
 * +++++++++++++++++++++++++++++++++++++
 * 
 */
var nodePathTraversal = (function(){
	
	var nodeScanned = function(id){
		var isScanned = false;
		for(var i=0;i<path.length;i++){
			if(id == path[i]){
				isScanned = true;
				break;
			}
		}
		return isScanned;
	};
	
	var fullPaths = [];
	var partPaths = [];
	var path = [];
	var nodeRecursion = function(nodeId){
		var node = $("#"+nodeId);
		path.push(nodeId);
		if(node.attr("ctlType") != "end"){
			var strLine = node.attr("line");
			var lines = strLine.split(";") || [];
			for(var i=0;i<lines.length;i++){
				if(lines[i].indexOf(nodeId+"TO") != -1){
					var line = lines[i].split("TO");
					if(line.length > 0){
						var to = line[1];
						if(nodeScanned(to)){
							path.push(to);
							partPaths[partPaths.length] = path.slice(0);
							path.pop();
						}
						else{
							nodeRecursion(to);
						}
					}
				}
			}
		}
		else if(node.attr("ctlType") == "end"){
			fullPaths[fullPaths.length] = path.slice(0);
		}
		path.pop();
	};
	
	var idSet = (function(){
		var prefix = "id_";
		var ids = {};
		return{
			"putId": function(id){
				if(id.length > 0){
					if(id instanceof String){
						ids[prefix+id] = id;
					}
					else if(id instanceof Array){
						var oneIds = [];
						for(var j=0;j<id.length;j++){
							oneIds = oneIds.concat(id[j]);
						}
						for(var i=0;i<oneIds.length;i++){
							ids[prefix+oneIds[i]] = oneIds[i];
						}
					}
				}				
			},
			"delId": function(id){
				delete ids[prefix+id];
			},
			"toArray": function(){
				var arrIds = [];
				for(var one in ids){
					if(ids.hasOwnProperty(one)){
						arrIds[arrIds.length] = ids[one];
					}
				}
				return arrIds;
			}
		};
	});
	
	return {
		"getPaths": function(nodeId){
			nodeRecursion(nodeId);
			return {
				"fullPaths": fullPaths,
				"partPaths": partPaths
			};
		},
		"getAllNextNodes": function(nodeId){
			nodeRecursion(nodeId);
			var nextIds = idSet();
			nextIds.putId(partPaths);
			nextIds.putId(fullPaths);
			nextIds.delId(nodeId);
			var nodes = nextIds.toArray();
			return nodes;
		}
	};
	
});

