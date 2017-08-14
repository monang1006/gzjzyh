
/*------------------------------------------------------------------------------
+                        图形绘制  定义了状态接口DrawState，环境角色，DrawContext  +
+                        和实现状态接口的子类 SelectDrawState，BeginDrawState    +
+						 EndDrawState，ProcessDrawState，LineDrawState         +
-------------------------------------------------------------------------------*/
/*--------画图状态-----------*/
	//画图状态接口
function DrawState() {
		//interface
	this.draw = draw;
	function draw(canvas, clickEvent) {
	}
}
	//选择
function SelectDrawState() {
		//inhert
	this.base = DrawState;
	this.base();
		//override
	this.draw = draw;
	function draw(canvas, clickEvent) {
		
		//zw
		if(clickEvent.srcElement == null || clickEvent.srcElement == undefined){
			var obj;
			var nodeType = clickEvent.ctlNodeType;
			if (nodeType == "node") {
				obj = clickEvent;
			}
			else{
				return;
			}

		}
		else{
			//是否选择的是节点
			var nodeType = clickEvent.srcElement.ctlNodeType;
			/*
			if(nodeType == undefined)
				return;
				*/
			if (!(nodeType == "node")) {
				if (!(clickEvent.srcElement.parentElement.ctlNodeType == "node")) {
					return;
				}
			}
				//获得节点
			var obj;
			if (nodeType == "node") {
				obj = clickEvent.srcElement;
			} else {
				obj = clickEvent.srcElement.parentElement;
			}
		}
		
		
		if (obj.ctlType == CNST_CTLTYPE_MOVEPOINT) {
			if (obj.movetype == "1") {
				obj.nodeclass.vmlobj.movetype = "1";
			} else {
				if (obj.movetype == "2") {
					obj.nodeclass.vmlobj.movetype = "2";
				}
			}
			obj = obj.nodeclass.vmlobj;
			fDragApproved = 3;//折线拖动
		}
		fSelectedObj = obj;
		
		//zw
		if(isCtrlKeyDown || isSelRect){
			fSelectedObjArray.push(fSelectedObj);
		}
		else{
			fSelectedObjArray = [];
			fSelectedObjCopyArray = [];
			fSelectedObjArray.push(fSelectedObj);
		}
		
            //保存坐标
		fx = fSelectedObj.style.pixelLeft - event.clientX;
		fy = fSelectedObj.style.pixelTop - event.clientY;


			//如果是线条
		if (obj.ctlType == CNST_CTLTYPE_LINE && obj.isStraint == "0") {
			obj.nodeclass.movepoint1.style.display = "block";
			obj.nodeclass.movepoint2.style.display = "block";
				//fDragApproved=3;//折线拖动
		} else {
			if (obj.ctlType == CNST_CTLTYPE_LINE && obj.isStraint == "1") {
				fDragApproved = false;
			} else {
				fDragApproved = true;//允许拖动
			}
		}
		return true;
	}
}

	//任务
function TaskDrawState() {
		//inhert
	this.base = DrawState;
	this.base();
		//override
	this.draw = draw;
	function draw(canvas, clickEvent) {
		var tasks = new tasknode(clickEvent);

			//添加根节点
		var n = doc.createNode(1, "task-node", "");
			//创建属性 
		var r = doc.createAttribute("name");
		r.value = tasks.textobj.innerText; 

			//添加属性 
		n.setAttributeNode(r); 
			//创建属性 
		var r = doc.createAttribute("atnode");
		r.value = tasks.vmlobj.id; 

			//添加属性 
		n.setAttributeNode(r);
		
		//添加xml节点标识属性
		var r = doc.createAttribute("xmlflag");
		r.value = nCtlNum ++; 

			//添加属性 
		n.setAttributeNode(r);
		
		// 增加判断后置连接类型
		var nodeLeaveEvent = doc.createNode(1, "event", "");
		
		//添加xml节点标识属性
		var r = doc.createAttribute("xmlflag");
		r.value = nCtlNum ++; 

			//添加属性 
		nodeLeaveEvent.setAttributeNode(r);		
		
		var r = doc.createAttribute("type");
		r.value = "node-leave";
		nodeLeaveEvent.setAttributeNode(r);
		var n1 = doc.createNode(1, "action", "");
		
		//添加xml节点标识属性
		var r = doc.createAttribute("xmlflag");
		r.value = nCtlNum ++; 

			//添加属性 
		n1.setAttributeNode(r);			
		
		var r = doc.createAttribute("name");
		r.value = nextTranTypeName;
		n1.setAttributeNode(r);
		var r = doc.createAttribute("class");
		r.value = nextTranTypeClass;
		n1.setAttributeNode(r);
		nodeLeaveEvent.appendChild(n1);
		n.appendChild(nodeLeaveEvent);		
				
		
		var nn = doc.createNode(1, "task", "");
			//创建属性 
		var r = doc.createAttribute("name");
		r.value = tasks.vmlobj.taskName; 

			//添加属性 
		nn.setAttributeNode(r);
		
		//添加xml节点标识属性
		var r = doc.createAttribute("xmlflag");
		r.value = nCtlNum ++; 

			//添加属性 
		nn.setAttributeNode(r);		
		
		n.appendChild(nn);
		var n1 = doc.createNode(1, "assignment", "");
		
		//添加xml节点标识属性
		var r = doc.createAttribute("xmlflag");
		r.value = nCtlNum ++; 

			//添加属性 
		n1.setAttributeNode(r);				
		
		var r = doc.createAttribute("class");
		r.value = taskAssignClass;
		n1.setAttributeNode(r);
		nn.appendChild(n1);
		var taskStartEvent = doc.createNode(1, "event", "");
		
		//添加xml节点标识属性
		var r = doc.createAttribute("xmlflag");
		r.value = nCtlNum ++; 

			//添加属性 
		taskStartEvent.setAttributeNode(r);		
		
		var r = doc.createAttribute("type");
		r.value = "task-start";
		taskStartEvent.setAttributeNode(r);
		var n1 = doc.createNode(1, "action", "");
		
		//添加xml节点标识属性
		var r = doc.createAttribute("xmlflag");
		r.value = nCtlNum ++; 

			//添加属性 
		n1.setAttributeNode(r);			
		
		var r = doc.createAttribute("name");
		r.value = taskStartName;
		n1.setAttributeNode(r);
		var r = doc.createAttribute("class");
		r.value = taskStartClass;
		n1.setAttributeNode(r);
		taskStartEvent.appendChild(n1);
		nn.appendChild(taskStartEvent);
		var taskEndEvent = doc.createNode(1, "event", "");
		
		//添加xml节点标识属性
		var r = doc.createAttribute("xmlflag");
		r.value = nCtlNum ++; 

			//添加属性 
		taskEndEvent.setAttributeNode(r);			
		
		var r = doc.createAttribute("type");
		r.value = "task-end";
		taskEndEvent.setAttributeNode(r);
		var n1 = doc.createNode(1, "action", "");
		
		//添加xml节点标识属性
		var r = doc.createAttribute("xmlflag");
		r.value = nCtlNum ++; 

			//添加属性 
		n1.setAttributeNode(r);		
		
		var r = doc.createAttribute("name");
		r.value = taskEndName;
		n1.setAttributeNode(r);
		var r = doc.createAttribute("class");
		r.value = taskEndClass;
		n1.setAttributeNode(r);
		taskEndEvent.appendChild(n1);
		nn.appendChild(taskEndEvent);
		root.appendChild(n);
		propertyListSubject.notifyObservers();
		return true;
	}
}

	//条件
function ConditionDrawState() {
		//inhert
	this.base = DrawState;
	this.base();
		//override
	this.draw = draw;
	function draw(canvas, clickEvent) {
		var conditions = new conditionnode(clickEvent);

			//添加根节点
		var n = doc.createNode(1, "decision", "");
		
		//添加xml节点标识属性
		var r = doc.createAttribute("xmlflag");
		r.value = nCtlNum ++; 

			//添加属性 
		n.setAttributeNode(r);			
		
			//创建属性 
		var r = doc.createAttribute("name");
		r.value = conditions.textobj.innerText; 

			//添加属性 
		n.setAttributeNode(r); 
			//创建属性 
		var r = doc.createAttribute("atnode");
		r.value = conditions.vmlobj.id; 

			//添加属性 
		n.setAttributeNode(r); 
		root.appendChild(n);
		propertyListSubject.notifyObservers();
		return true;
	}
}

	//结束
function EndDrawState() {
		//inhert
	this.base = DrawState;
	this.base();
		//override
	this.draw = draw;
	function draw(canvas, clickEvent) {
		var ends = new endnode(clickEvent);

			//添加根节点
		var n = doc.createNode(1, "end-state", "");
		
		//添加xml节点标识属性
		var r = doc.createAttribute("xmlflag");
		r.value = nCtlNum ++; 

			//添加属性 
		n.setAttributeNode(r);		
		
			//创建属性 
		var r = doc.createAttribute("name");
		r.value = ends.textobj.innerText; 

			//添加属性 
		n.setAttributeNode(r); 
			//创建属性 
		var r = doc.createAttribute("atnode");
		r.value = ends.vmlobj.id; 

			//添加属性 
		n.setAttributeNode(r);
		var n1 = doc.createNode(1, "event", "");
		
		//添加xml节点标识属性
		var r = doc.createAttribute("xmlflag");
		r.value = nCtlNum ++; 

			//添加属性 
		n1.setAttributeNode(r);		
		
		var r = doc.createAttribute("type");
		r.value = "node-enter";
		n1.setAttributeNode(r);
		var n2 = doc.createNode(1, "action", "");
		
		//添加xml节点标识属性
		var r = doc.createAttribute("xmlflag");
		r.value = nCtlNum ++; 

			//添加属性 
		n2.setAttributeNode(r);			
		
		var r = doc.createAttribute("class");
		r.value = processEndClass;
		n2.setAttributeNode(r);
		n1.appendChild(n2);
		n.appendChild(n1);
		root.appendChild(n);
		propertyListSubject.notifyObservers();
		return true;
	}
}



	//合并
function JoinDrawState() {
		//inhert
	this.base = DrawState;
	this.base();
		//override
	this.draw = draw;
	function draw(canvas, clickEvent) {
		var joins = new joinnode(clickEvent);

			//添加根节点
		var n = doc.createNode(1, "join", "");
			//创建属性 
		var r = doc.createAttribute("name");
		r.value = joins.textobj.innerText; 

			//添加属性 
		n.setAttributeNode(r); 
			//创建属性 
		var r = doc.createAttribute("atnode");
		r.value = joins.vmlobj.id; 

			//添加属性 
		n.setAttributeNode(r); 
		root.appendChild(n);
		propertyListSubject.notifyObservers();
		return true;
	}
}

    //node
function NodeDrawState() {
		//inhert
	this.base = DrawState;
	this.base();
		//override
	this.draw = draw;
	function draw(canvas, clickEvent) {
		var nodes = new nodenode(clickEvent);

			//添加根节点
		var n = doc.createNode(1, "node", "");
		
		//添加xml节点标识属性
		var r = doc.createAttribute("xmlflag");
		r.value = nCtlNum ++; 

			//添加属性 
		n.setAttributeNode(r);		
		
			//创建属性 
		var r = doc.createAttribute("name");
		r.value = nodes.textobj.innerText; 

			//添加属性 
		n.setAttributeNode(r); 
			//创建属性 
		var r = doc.createAttribute("atnode");
		r.value = nodes.vmlobj.id; 

			//添加属性 
		n.setAttributeNode(r); 
		
		// 增加判断后置连接类型
		var nodeLeaveEvent = doc.createNode(1, "event", "");
		
		//添加xml节点标识属性
		var r = doc.createAttribute("xmlflag");
		r.value = nCtlNum ++; 

			//添加属性 
		nodeLeaveEvent.setAttributeNode(r);		
		
		var r = doc.createAttribute("type");
		r.value = "node-leave";
		nodeLeaveEvent.setAttributeNode(r);
		var n1 = doc.createNode(1, "action", "");
		
		//添加xml节点标识属性
		var r = doc.createAttribute("xmlflag");
		r.value = nCtlNum ++; 

			//添加属性 
		n1.setAttributeNode(r);			
		
		var r = doc.createAttribute("name");
		r.value = nextTranTypeName;
		n1.setAttributeNode(r);
		var r = doc.createAttribute("class");
		r.value = nextTranTypeClass;
		n1.setAttributeNode(r);
		nodeLeaveEvent.appendChild(n1);
		n.appendChild(nodeLeaveEvent);
		
		root.appendChild(n);
		propertyListSubject.notifyObservers();
		return true;
	}
}
	
    //超状态
function SuperStateDrawState() {
		//inhert
	this.base = DrawState;
	this.base();
		//override
	this.draw = draw;
	function draw(canvas, clickEvent) {
		var nodes = new superstatenode(clickEvent);

			//添加根节点
		var n = doc.createNode(1, "task-node", "");
		
		//添加xml节点标识属性
		var r = doc.createAttribute("xmlflag");
		r.value = nCtlNum ++; 

			//添加属性 
		n.setAttributeNode(r);		
		
			//创建属性 
		var r = doc.createAttribute("name");
		r.value = nodes.textobj.innerText; 

			//添加属性 
		n.setAttributeNode(r); 
			//创建属性 
		var r = doc.createAttribute("atnode");
		r.value = nodes.vmlobj.id; 

			//添加属性 
		n.setAttributeNode(r);
		var nn = doc.createNode(1, "task", "");
		
		//添加xml节点标识属性
		var r = doc.createAttribute("xmlflag");
		r.value = nCtlNum ++; 

			//添加属性 
		nn.setAttributeNode(r);		
		
			//创建属性 
		var r = doc.createAttribute("name");
		r.value = nodes.vmlobj.taskName; 

			//添加属性 
		nn.setAttributeNode(r);
		n.appendChild(nn);
		var n1 = doc.createNode(1, "assignment", "");
		
		//添加xml节点标识属性
		var r = doc.createAttribute("xmlflag");
		r.value = nCtlNum ++; 

			//添加属性 
		n1.setAttributeNode(r);			
		
		var r = doc.createAttribute("class");
		r.value = taskAssignClass;
		n1.setAttributeNode(r);
		nn.appendChild(n1);
		var taskStartEvent = doc.createNode(1, "event", "");
		
		//添加xml节点标识属性
		var r = doc.createAttribute("xmlflag");
		r.value = nCtlNum ++; 

			//添加属性 
		taskStartEvent.setAttributeNode(r);			
		
		var r = doc.createAttribute("type");
		r.value = "task-start";
		taskStartEvent.setAttributeNode(r);
		var n1 = doc.createNode(1, "action", "");
		
		//添加xml节点标识属性
		var r = doc.createAttribute("xmlflag");
		r.value = nCtlNum ++; 

			//添加属性 
		n1.setAttributeNode(r);		
		
		var r = doc.createAttribute("name");
		r.value = taskStartName;
		n1.setAttributeNode(r);
		var r = doc.createAttribute("class");
		r.value = taskStartClass;
		n1.setAttributeNode(r);
		taskStartEvent.appendChild(n1);
		nn.appendChild(taskStartEvent);
		var taskEndEvent = doc.createNode(1, "event", "");
		
		//添加xml节点标识属性
		var r = doc.createAttribute("xmlflag");
		r.value = nCtlNum ++; 

			//添加属性 
		taskEndEvent.setAttributeNode(r);		
		
		var r = doc.createAttribute("type");
		r.value = "task-end";
		taskEndEvent.setAttributeNode(r);
		var n1 = doc.createNode(1, "action", "");
		
		//添加xml节点标识属性
		var r = doc.createAttribute("xmlflag");
		r.value = nCtlNum ++; 

			//添加属性 
		n1.setAttributeNode(r);		
		
		var r = doc.createAttribute("name");
		r.value = taskEndName;
		n1.setAttributeNode(r);
		var r = doc.createAttribute("class");
		r.value = taskEndClass;
		n1.setAttributeNode(r);
		taskEndEvent.appendChild(n1);
		nn.appendChild(taskEndEvent);
		root.appendChild(n);
		propertyListSubject.notifyObservers();
		return true;
	}
}
	
	
    //子流程
function ProcessStateDrawState() {
		//inhert
	this.base = DrawState;
	this.base();
		//override
	this.draw = draw;
	function draw(canvas, clickEvent) {
		var nodes = new processstatenode(clickEvent);
			//添加根节点
		var n = doc.createNode(1, "task-node", "");
		
		//添加xml节点标识属性
		var r = doc.createAttribute("xmlflag");
		r.value = nCtlNum ++; 

			//添加属性 
		n.setAttributeNode(r);			
		
			//创建属性 
		var r = doc.createAttribute("name");
		r.value = nodes.textobj.innerText; 

			//添加属性 
		n.setAttributeNode(r); 
			//创建属性 
		var r = doc.createAttribute("atnode");
		r.value = nodes.vmlobj.id;
			//添加属性 
		n.setAttributeNode(r);
		var r = doc.createAttribute("signal");
		r.value = "never";
		n.setAttributeNode(r);
		var r = doc.createAttribute("create-tasks");
		r.value = "false";
		n.setAttributeNode(r);
		var nn = doc.createNode(1, "task", "");
		
		//添加xml节点标识属性
		var r = doc.createAttribute("xmlflag");
		r.value = nCtlNum ++; 

			//添加属性 
		nn.setAttributeNode(r);		
		
			//创建属性 
		var r = doc.createAttribute("name");
		r.value = "\u9759\u6001\u5b50\u6d41\u7a0b";
		nn.setAttributeNode(r);
		n.appendChild(nn);
			
			//nodeenter
		var enterevent = doc.createNode(1, "event", "");
		
		//添加xml节点标识属性
		var r = doc.createAttribute("xmlflag");
		r.value = nCtlNum ++; 

			//添加属性 
		enterevent.setAttributeNode(r);			
		
			//创建属性 
		var r = doc.createAttribute("type");
		r.value = "node-enter"; 
			//添加属性 
		enterevent.setAttributeNode(r);
		n.appendChild(enterevent);
		var action = doc.createNode(1, "action", "");
		
		//添加xml节点标识属性
		var r = doc.createAttribute("xmlflag");
		r.value = nCtlNum ++; 

			//添加属性 
		action.setAttributeNode(r);			
		
			//创建属性 
		var r = doc.createAttribute("name");
		r.value = subProcessStartName; 
			//添加属性 
		action.setAttributeNode(r);	
			//创建属性 
		var r = doc.createAttribute("class");
		r.value = subProcessStartClass; 
			//添加属性 
		action.setAttributeNode(r);
		enterevent.appendChild(action);
		n.appendChild(enterevent);
		root.appendChild(n);
		propertyListSubject.notifyObservers();
		return true;
	}
}		


    //分支
function SplitDrawState() {
		//inhert
	this.base = DrawState;
	this.base();
		//override
	this.draw = draw;
	function draw(canvas, clickEvent) {
		var splits = new splitnode(clickEvent);

			//添加根节点
		var n = doc.createNode(1, "fork", "");
			//创建属性 
		var r = doc.createAttribute("name");
		r.value = splits.textobj.innerText; 

			//添加属性 
		n.setAttributeNode(r); 
			//创建属性 
		var r = doc.createAttribute("atnode");
		r.value = splits.vmlobj.id; 

			//添加属性 
		n.setAttributeNode(r); 
			
			//nodeenter
		var enterevent = doc.createNode(1, "event", "");
			//创建属性 
		var r = doc.createAttribute("type");
		r.value = "node-enter"; 
			//添加属性 
		enterevent.setAttributeNode(r);
		n.appendChild(enterevent);
		var action = doc.createNode(1, "action", "");
			//创建属性 
		var r = doc.createAttribute("name");
		r.value = "com.strongit.ForkStartHandle"; 
			//添加属性 
		action.setAttributeNode(r);	
			//创建属性 
		var r = doc.createAttribute("class");
		r.value = "com.strongit.workflowmanager.helper.ForkStartHandle"; 
			//添加属性 
		action.setAttributeNode(r);
		enterevent.appendChild(action);
			//script
		var script = doc.createNode(1, "script", "");
			//创建属性 
		var r = doc.createAttribute("name");
		r.value = "com.strongit.indecision"; 
			//添加属性 
		script.setAttributeNode(r);
		n.appendChild(script);
		var variable = doc.createNode(1, "variable", "");
			//创建属性 
		var r = doc.createAttribute("name");
		r.value = "forktokentransitions"; 
			//添加属性 
		variable.setAttributeNode(r);	
			//创建属性 
		var r = doc.createAttribute("access");
		r.value = "write"; 
			//添加属性 
		variable.setAttributeNode(r);
		script.appendChild(variable);
		var expression = doc.createNode(1, "expression", "");
		expression.text = "forktokentransitions=new ArrayList();Collection tran=new ArrayList();if(forktransitions instanceof Collection){tran= (Collection) forktransitions;}Iterator iter = tran.iterator();while(iter.hasNext()){String temp=(String)iter.next();forktokentransitions.add(temp);}";
		script.appendChild(expression);						
			
			//nodeleave						
		var leaveevent = doc.createNode(1, "event", "");
			//创建属性 
		var r = doc.createAttribute("type");
		r.value = "node-leave"; 
			//添加属性 
		leaveevent.setAttributeNode(r);
		n.appendChild(leaveevent);
		var action = doc.createNode(1, "action", "");
			//创建属性 
		var r = doc.createAttribute("name");
		r.value = "com.strongit.ForkLeaveHandle"; 
			//添加属性 
		action.setAttributeNode(r);	
			//创建属性 
		var r = doc.createAttribute("class");
		r.value = "com.strongit.workflowmanager.helper.ForkLeaveHandle"; 
			//添加属性 
		action.setAttributeNode(r);
		leaveevent.appendChild(action);
		root.appendChild(n);
		propertyListSubject.notifyObservers();
		return true;
	}
}

	//状态
function StatDrawState() {
		//inhert
	this.base = DrawState;
	this.base();
		//override
	this.draw = draw;
	function draw(canvas, clickEvent) {
		var stats = new statnode(clickEvent);

				//添加根节点
		var n = doc.createNode(1, "state", "");
					//创建属性 
		var r = doc.createAttribute("name");
		r.value = stats.textobj.innerText; 

						//添加属性 
		n.setAttributeNode(r); 
					//创建属性 
		var r = doc.createAttribute("atnode");
		r.value = stats.vmlobj.id; 

						//添加属性 
		n.setAttributeNode(r); 
		root.appendChild(n);
		propertyListSubject.notifyObservers();
		return true;
	}
}
	//线条
function LineDrawState() {
		//inhert
	this.base = DrawState;
	this.base();
		//override
	this.draw = draw;
	function draw(canvas, clickEvent) {
		var lines = new line(clickEvent);
		return false;
	}//end func
}
/*-----------end-画图状态--------*/
/*-----------画图环境角色--------*/
function DrawContext() {
		//property
	this.drawState = new SelectDrawState(); //默认为选择状态
		//method
	this.setDrawState = setDrawState;
	this.draw = draw;
	function setDrawState(aDrawState) {
		this.drawState = aDrawState;
	}
	function draw(canvas, clickEvent) {
		return this.drawState.draw(canvas, clickEvent);
	}
}
/*---end--画图环境角色--------*/

