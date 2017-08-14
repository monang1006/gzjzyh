/*------------------------------------------------------------------------------
+                        系统常量的定义			          					   +
+                            												   +
+						        											   +
-------------------------------------------------------------------------------*/

	//流程定义节点插件属性名称前缀
	var pluginNamePrefix = "plugins_";

	//按钮显示名称数组
	var buttonname = [];
	buttonname['sel']='选择';
	buttonname['stat']='状态节点';
	buttonname['node']='自动节点';
	buttonname['superstate']='动态子流程节点';
	buttonname['processstate']='子流程节点';
	buttonname['task']='任务节点';
	buttonname['condition']='条件分支';
	buttonname['splits']='并发节点';
	buttonname['joins']='汇聚节点';
	buttonname['end']='结束节点';
	buttonname['line']='流程转移';
	
	//流程当前状态
	var currentStatus = "non-edit";
		
	//系统目录
	var systemroot = scriptroot+"/workflow/designer";
	//流程类型
	var processType="公共流程,0"
	//流程名称
	var processName="新建流程";
	//流程主表单
	var processForm="未指定表单,0";
	
	//是否启用流程定时器
	var doc_isTimer = "0";
	//流程定时器设置
	var doc_timerSet = "0,day,0,day,0,day,0,0,0,0,0,0";
	
	/*
	* 流程辅助类定义
	*/
	//流程定时器触发Action
	var timerAction = "com.strongit.workflow.workflowDesign.helper.TimerOutAction";	
	
	var preTranTypeName = "com.strongit.task.preTranTypeAction";
	var preTranTypeClass = "com.strongit.workflow.workflowDesign.helper.TaskPreTranType";
	
	var nextTranTypeName = "com.strongit.task.nextTranTypeAction";
	var nextTranTypeClass = "com.strongit.workflow.workflowDesign.helper.TaskNextTranType";
	
	var taskAssignClass = "com.strongit.workflow.workflowDesign.helper.ActorsHandel";
	
	var taskStartName = "com.strongit.taskStartDefaultAction";
	var taskStartClass = "com.strongit.workflow.workflowDesign.helper.TaskStartDefaultAction";
	
	var taskEndName = "com.strongit.taskEndDefaultAction";
	var taskEndClass = "com.strongit.workflow.workflowDesign.helper.TaskEndDefaultAction";
	
	var processEndClass = "com.strongit.workflow.workflowDesign.helper.SubProcessEnd";
	
	var subProcessStartName = "com.strongit.SubProcessStart";
	var subProcessStartClass = "com.strongit.workflow.workflowDesign.helper.SubProcessStart";
	
	var createTimerName = "com.strongit.TimerCreate";
	var createTimerClass = "com.strongit.workflow.workflowDesign.helper.ActiveSetTimer";
	
	var decideHandleName = "com.strongit.handledecision";
	var decideHandleClass = "com.strongit.workflow.workflowDesign.helper.DecisionHandle";
	
	var counterSignName = "com.strongit.counterSignAction";
	var counterSignClass = "com.strongit.workflow.workflowDesign.helper.CounterSignAction";
	
	var initCounterSignName = "com.strongit.initCounterSignAction";
	var initCounterSignClass = "com.strongit.workflow.workflowDesign.helper.InitCounterSignAction";
	
	var nodeEnterName = "com.strongit.nodeEnterAction";
	
	var nodeLeaveName = "com.strongit.nodeLeaveAction";
	
	var taskStartCustomName = "com.strongit.taskStartAction";
	
	var taskEndCustomName = "com.strongit.taskEndAction";
	
	var nodeDelegationName = "com.strongit.nodeDelAction";
	/*
	 * 在流程迁移线上保存流程轨迹信息
	 */
	var flowTrackName = "com.strongit.flowTrackAction";
	var flowTrackClass = "com.strongit.workflow.workflowDesign.helper.FlowTrackAction";
	/*
	* 流程辅助类定义结束
	*/	
	
	//是否可以拖动
	var fDragApproved=false;
	var fSelectedObj,fx,fy;    //当前所选择的对象
	
	//zw
	//已选择的对象组
	var fSelectedObjArray = [];
	//已选择的对象的复制组
	var fSelectedObjCopyArray = [];

	
	//选择的按钮
    var fLiftButton="sel";
	//是否在画线 0,不在画线；1,起点；2,终点
	var fAddLineFlag = 0;
	var fLineStartObj = null;
	var fLineEndObj = null;
	//控件的数量
	var fCtlNum;
	
	//JBPM流程文件各节点的标识ID
	var nCtlNum = 1;
	
	//表格宽度和高度
	var fGridNo=10;
	
	//节点高度
	var nodeHeight=40;
	//节点宽度
	var nodeWidth=100;
	//线段转折点圆宽
	var linePointWidth = 4;
	//线段转折点圆高
	var linePointHeight = 4;
	//线段文本框宽
	var lineTextWidth = 60;
	//线段文本框高
	var lineTextHeight = 20;

	//fCtlNum最大值,加载时使用
	var maxfCtlNum=-1;
	
	//生成xml时检测数组
	var testArray=new Array();
	var testElement="process-definition,swimlane,start-state,end-state,decision,fork,join,node,process-state,state,super-state,task-node,transition,action,cancel-timer,create-timer,script,event,exception-handler,task,controller,timer,variable,handler,sub-process,assignment,expression";	
	var tempArray=testElement.split(",");
	for(var i=0;i<tempArray.length;i++){
		testArray[tempArray[i]]="1";
	}
	

	//类型
	var CNST_CTLTYPE_INITIAL   = "initial";  //启动
	var CNST_CTLTYPE_NODE      = "node";     //节点
	var CNST_CTLTYPE_SUPERSTATE= "superstate";  //超状态
	var CNST_CTLTYPE_PROSTATE  = "prostate";  //子流程			
	var CNST_CTLTYPE_STAT      = "stat";    //状态
	var CNST_CTLTYPE_JOIN      = "join";    //聚合
	var CNST_CTLTYPE_SPLIT     = "split";   //分支
	var CNST_CTLTYPE_LINE	   = "line";    //线段
	var CNST_CTLTYPE_CONDITION = "condition";    //条件
	var CNST_CTLTYPE_TASK      = "task";    //任务
	var CNST_CTLTYPE_END       = "end";    //结束
	var CNST_LINE_NAME         = "linecontent" //转移名称
	var CNST_CTLTYPE_MOVEPOINT = "movepoint";//转移移动端点


	// 流程图所在矩形范围
	var processTop = 0;
	var processLeft = 0;
	var processRight = 0;
	var processBottom = 0;
	
	
	// 流程图形移动（上、下、左、右）
	function moveGraph(point){
		switch(point){
			case "top":
				countMaxpoint();
				var index = fGridNo;
				if(parseInt(processTop) < parseInt(fGridNo)){
					index = processTop;
					alert("不能继续向上移动");
					return false;
				}
				for(var i = 0; i < sltObjArray.options.length; i++){
					var obj = document.getElementById(sltObjArray.options[i].value);
					if(obj.ctlType == CNST_CTLTYPE_LINE){
						var line = obj.nodeclass;
						var posArray = obj.posArray;
						for(var j = 1; j < posArray.length; j = j + 2){
							obj.posArray[j] = parseInt(obj.posArray[j]) - parseInt(index);				
						}
						if(obj.isStraint == "1"){
							var str=obj.id+'.Points.value="'+obj.posArray[0]+','+obj.posArray[1]+' '+obj.posArray[4]+','+obj.posArray[5]+'"';
							eval(str);
						}else if(obj.isStraint == "0"){
							var str=obj.id+'.Points.value="'+obj.posArray[0]+','+obj.posArray[1]+' '+obj.posArray[6]+','+obj.posArray[7]+' '+obj.posArray[8]+','+obj.posArray[9]+' '+obj.posArray[4]+','+obj.posArray[5]+'"';
							eval(str);
						}							
						line.textobj.style.top = parseInt(line.textobj.style.top) - parseInt(index);
						line.movepoint1.style.top = parseInt(line.movepoint1.style.top) - parseInt(index);
						line.movepoint2.style.top = parseInt(line.movepoint2.style.top) - parseInt(index);
							
					}else{
						var node = obj.nodeclass;
						obj.style.top = parseInt(obj.style.top) - parseInt(index);			
					}
				}
				break;				
			case "right":
				var index = fGridNo;
				for(var i = 0; i < sltObjArray.options.length; i++){
					var obj = document.getElementById(sltObjArray.options[i].value);
					if(obj.ctlType == CNST_CTLTYPE_LINE){
						var line = obj.nodeclass;
						var posArray = obj.posArray;
						for(var j = 0; j < posArray.length; j = j + 2){
							obj.posArray[j] = parseInt(obj.posArray[j]) + parseInt(index);				
						}
						if(obj.isStraint == "1"){
							var str=obj.id+'.Points.value="'+obj.posArray[0]+','+obj.posArray[1]+' '+obj.posArray[4]+','+obj.posArray[5]+'"';
							eval(str);
						}else if(obj.isStraint == "0"){
							var str=obj.id+'.Points.value="'+obj.posArray[0]+','+obj.posArray[1]+' '+obj.posArray[6]+','+obj.posArray[7]+' '+obj.posArray[8]+','+obj.posArray[9]+' '+obj.posArray[4]+','+obj.posArray[5]+'"';
							eval(str);
						}							
						line.textobj.style.left = parseInt(line.textobj.style.left) + parseInt(index);
						line.movepoint1.style.left = parseInt(line.movepoint1.style.left) + parseInt(index);
						line.movepoint2.style.left = parseInt(line.movepoint2.style.left) + parseInt(index);
							
					}else{
						var node = obj.nodeclass;
						obj.style.left = parseInt(obj.style.left) + parseInt(index);			
					}
				}
				break;
			case "bottom":
				var index = fGridNo;
				for(var i = 0; i < sltObjArray.options.length; i++){
					var obj = document.getElementById(sltObjArray.options[i].value);
					if(obj.ctlType == CNST_CTLTYPE_LINE){
						var line = obj.nodeclass;
						var posArray = obj.posArray;
						for(var j = 1; j < posArray.length; j = j + 2){
							obj.posArray[j] = parseInt(obj.posArray[j]) + parseInt(index);				
						}
						if(obj.isStraint == "1"){
							var str=obj.id+'.Points.value="'+obj.posArray[0]+','+obj.posArray[1]+' '+obj.posArray[4]+','+obj.posArray[5]+'"';
							eval(str);
						}else if(obj.isStraint == "0"){
							var str=obj.id+'.Points.value="'+obj.posArray[0]+','+obj.posArray[1]+' '+obj.posArray[6]+','+obj.posArray[7]+' '+obj.posArray[8]+','+obj.posArray[9]+' '+obj.posArray[4]+','+obj.posArray[5]+'"';
							eval(str);
						}							
						line.textobj.style.top = parseInt(line.textobj.style.top) + parseInt(index);
						line.movepoint1.style.top = parseInt(line.movepoint1.style.top) + parseInt(index);
						line.movepoint2.style.top = parseInt(line.movepoint2.style.top) + parseInt(index);
							
					}else{
						var node = obj.nodeclass;
						obj.style.top = parseInt(obj.style.top) + parseInt(index);			
					}
				}
				break;								
			case "left":
				countMaxpoint();
				var index = fGridNo;
				if(parseInt(processLeft) < parseInt(fGridNo)){
					index = processLeft;
					alert("不能继续向左移动");
					return false;
				}			
				for(var i = 0; i < sltObjArray.options.length; i++){
					var obj = document.getElementById(sltObjArray.options[i].value);
					if(obj.ctlType == CNST_CTLTYPE_LINE){
						var line = obj.nodeclass;
						var posArray = obj.posArray;
						for(var j = 0; j < posArray.length; j = j + 2){
							obj.posArray[j] = parseInt(obj.posArray[j]) - parseInt(index);				
						}
						if(obj.isStraint == "1"){
							var str=obj.id+'.Points.value="'+obj.posArray[0]+','+obj.posArray[1]+' '+obj.posArray[4]+','+obj.posArray[5]+'"';
							eval(str);
						}else if(obj.isStraint == "0"){
							var str=obj.id+'.Points.value="'+obj.posArray[0]+','+obj.posArray[1]+' '+obj.posArray[6]+','+obj.posArray[7]+' '+obj.posArray[8]+','+obj.posArray[9]+' '+obj.posArray[4]+','+obj.posArray[5]+'"';
							eval(str);
						}							
						line.textobj.style.left = parseInt(line.textobj.style.left) - parseInt(index);
						line.movepoint1.style.left = parseInt(line.movepoint1.style.left) - parseInt(index);
						line.movepoint2.style.left = parseInt(line.movepoint2.style.left) - parseInt(index);
							
					}else{
						var node = obj.nodeclass;
						obj.style.left = parseInt(obj.style.left) - parseInt(index);			
					}
				}
				break;			
		}
	}
	
	// 计算矩形边界点
	function countMaxpoint(){
		processTop = 100000;
		processLeft = 100000;
		processRight = 0;
		processBottom = 0;	
		var movepointIndex = parseInt(lineTextWidth * Math.sqrt(2) / 2) + parseInt(lineTextWidth) / 2;
		for(var i = 0; i < sltObjArray.options.length; i++){
			var obj = document.getElementById(sltObjArray.options[i].value);
			if(obj.ctlType == CNST_CTLTYPE_LINE){
				var line = obj.nodeclass;			
				var posArray = obj.posArray;
				for(var j = 0; j < posArray.length;){
					if(parseInt(posArray[j]) < parseInt(processLeft)){
						processLeft = posArray[j];
					}
					if(parseInt(posArray[j]) > parseInt(processRight)){
						processRight = posArray[j];
					}
					j++;
					if(parseInt(posArray[j]) < parseInt(processTop)){
						processTop = posArray[j];
					}
					if(parseInt(posArray[j]) > parseInt(processBottom)){
						processBottom = posArray[j];
					}
					j++;					
				}				
				if(parseInt(line.textobj.style.left) < parseInt(processLeft)){
					processLeft = line.textobj.style.left;
				}
				if((parseInt(line.textobj.style.left) + parseInt(lineTextWidth)) > parseInt(processRight)){
					processRight = parseInt(line.textobj.style.left) + parseInt(lineTextWidth);
				}
				if(parseInt(line.textobj.style.top) < parseInt(processTop)){
					processTop = line.textobj.style.top;
				}
				if((parseInt(line.textobj.style.top) + parseInt(lineTextHeight)) > parseInt(processBottom)){
					processBottom = parseInt(line.textobj.style.top) + parseInt(lineTextHeight);
				}
				if(parseInt(line.movepoint1.style.left) < parseInt(processLeft)){
					processLeft = line.movepoint1.style.left;
				}
				if((parseInt(line.movepoint1.style.left) + movepointIndex) > parseInt(processRight)){
					processRight = parseInt(line.movepoint1.style.left) + movepointIndex;
				}
				if(parseInt(line.movepoint1.style.top) < parseInt(processTop)){
					processTop = line.movepoint1.style.top;
				}
				if((parseInt(line.movepoint1.style.top) + movepointIndex) > parseInt(processBottom)){
					processBottom = parseInt(line.movepoint1.style.top) + movepointIndex;
				}
				if(parseInt(line.movepoint2.style.left) < parseInt(processLeft)){
					processLeft = line.movepoint2.style.left;
				}
				if((parseInt(line.movepoint2.style.left) + movepointIndex) > parseInt(processRight)){
					processRight = parseInt(line.movepoint2.style.left) + movepointIndex;
				}
				if(parseInt(line.movepoint2.style.top) < parseInt(processTop)){
					processTop = line.movepoint2.style.top;
				}
				if((parseInt(line.movepoint2.style.top) + movepointIndex) > parseInt(processBottom)){
					processBottom = parseInt(line.movepoint2.style.top) + movepointIndex;
				}								
			}else{
				var node = obj.nodeclass;
				if(parseInt(obj.style.left) < parseInt(processLeft)){
					processLeft = obj.style.left;
				}
				if((parseInt(obj.style.left) + parseInt(nodeWidth)) > parseInt(processRight)){
					processRight = parseInt(obj.style.left) + parseInt(nodeWidth);
				}
				if(parseInt(obj.style.top) < parseInt(processTop)){
					processTop = obj.style.top;
				}
				if((parseInt(obj.style.top) + parseInt(nodeHeight)) > parseInt(processBottom)){
					processBottom = parseInt(obj.style.top) + parseInt(nodeHeight);
				}				
			}
		}
	}
	

	//增加到控件列表中
	function addToObjArray (obj,toLine) {
		var oOption = document.createElement("OPTION");
		sltObjArray.options.add(oOption);
		oOption.innerText = obj;
		oOption.value = obj;
		fSelectedObj = document.getElementById(obj);
		if (toLine==true) {
        	moveObjToline(fSelectedObj);
		}
		fCtlNum++; //对象计数
		iptCtlNum.value=fCtlNum; //控件中计数同步
	}

	//从控件列表删除
	function removeFromObjArray (obj) {
		for (var i=0;i<sltObjArray.options.length;i++) {
			if (obj.id==sltObjArray.options[i].value) {
				sltObjArray.options[i] = null; //删除
				break;
			}
		}
	}


	//四舍五入
	function floatRound(myFloat) {
		var cutNumber = Math.pow(10,0);
		return Math.round(myFloat * cutNumber)/cutNumber;
	}

	//适应表格
	function moveObjToline(aObj) {
		if(aObj.ctlType!=CNST_CTLTYPE_LINE && aObj.ctlType!=CNST_CTLTYPE_MOVEPOINT){
			aObj.style.left = Math.abs(floatRound((parseInt(aObj.style.left)+parseInt(aObj.style.width)/2)/fGridNo)*fGridNo - parseInt(aObj.style.width)/2);
			aObj.style.top  = Math.abs(floatRound((parseInt(aObj.style.top)+parseInt(aObj.style.height)/2)/fGridNo)*fGridNo - parseInt(aObj.style.height)/2);
		}
	}
	
	//节点与连接点交点生成策略
	function cal_node_point(startleft,starttop,pointx,pointy){
		var posArray=new Array();
		//var startleft,starttop;
		var sourcex,sourcey;
//		with (aStart.style) {
//			startleft = pixelLeft;  //Left
//			starttop = pixelTop;   //Top
//		}
		sourcex = startleft+nodeWidth/2;
		sourcey = starttop+nodeHeight/2;

		var xielu=nodeHeight/nodeWidth;
		var y1=starttop-(pointx-startleft-nodeWidth)*xielu;
		var y2=starttop+(pointx-startleft)*xielu;

		if(pointy>=y1 && pointy>=y2){
			//alert("下面");
			var dtos;
			if(pointx==sourcex){
				dtos=0;
			}else{
				dtos=(pointx-sourcex)/(pointy-sourcey);
			}
			posArray["y"]=starttop+nodeHeight;
			posArray["x"]=(posArray["y"]-sourcey)*dtos+sourcex;
		}else if(pointy>=y2 && pointy<=y1){
						//alert("左面");
			var dtos;
			if(pointy==sourcey){
				dtos=0;
			}else{
				dtos=(pointy-sourcey)/(pointx-sourcex);
			}
			posArray["x"]=startleft;
			posArray["y"]=(posArray["x"]-sourcex)*dtos+sourcey;

		}else if(pointy>=y1 && pointy<=y2){
						//alert("右面");
			var dtos;
			if(pointy==sourcey){
				dtos=0;
			}else{
				dtos=(pointy-sourcey)/(pointx-sourcex);
			}
			posArray["x"]=startleft+nodeWidth;
			posArray["y"]=(posArray["x"]-sourcex)*dtos+sourcey;

		}else if(pointy<=y1 && pointy<=y2){
						//alert("上面");
			var dtos;
			if(pointx==sourcex){
				dtos=0;
			}else{
				dtos=(pointx-sourcex)/(pointy-sourcey);
			}
			posArray["y"]=starttop;
			posArray["x"]=(posArray["y"]-sourcey)*dtos+sourcex;
		}
		return posArray;

	}


	function cf_CalculateLinePos (aStart,aEnd,midx,midy) {

    	var posArray = new Array();
		var x1,y1,x2,y3,x3,y3,x4,y4;
		var sl,st,sw,sh,el,et,ew,eh;
		var spacing;
		//开始
		with (aStart.style) {

			sl=pixelLeft;
			st=pixelTop;

			sw = 100;//parseInt(width);      //width
			sh = 40;//parseInt(height);     //height
		}
		//结束
		with (aEnd.style) {
			el=pixelLeft;
			et=pixelTop;

			ew = 100;//parseInt(width);      //width
			eh = 40;//parseInt(height);     //height
		}

		//计算路线
		x1 = sl+sw/2;
		y1 = st+sh/2;

		x4 = el+ew/2;
		y4 = et+eh/2;

		var tempArray;

		if(arguments.length==4){
			//放到数组里面
			tempArray=cal_node_point(sl,st,midx,midy);
			posArray["x1"] = parseInt(tempArray["x"]);
			posArray["y1"] = parseInt(tempArray["y"]);

			tempArray=cal_node_point(el,et,midx,midy);
			posArray["x4"] = parseInt(tempArray["x"]);
			posArray["y4"] = parseInt(tempArray["y"]);

			posArray["x2"]=(posArray["x1"]+posArray["x4"])/2;
			posArray["y2"]=(posArray["y1"]+posArray["y4"])/2;

			var tempx=(posArray["x4"]-posArray["x1"])/4;
			var tempy=(posArray["y4"]-posArray["y1"])/4;

			posArray["x5"]=posArray["x1"]+tempx;
			posArray["x6"]=posArray["x4"]-tempx;
			posArray["y5"]=posArray["y1"]+tempy;
			posArray["y6"]=posArray["y4"]-tempy;
		}else if(arguments.length==2){
			//放到数组里面
			tempArray=cal_node_point(sl,st,x4,y4);
			posArray["x1"] = parseInt(tempArray["x"]);
			posArray["y1"] = parseInt(tempArray["y"]);

			tempArray=cal_node_point(el,et,x1,y1);
			posArray["x4"] = parseInt(tempArray["x"]);
			posArray["y4"] = parseInt(tempArray["y"]);

			posArray["x2"]=(posArray["x1"]+posArray["x4"])/2;
			posArray["y2"]=(posArray["y1"]+posArray["y4"])/2;

			var tempx=(posArray["x4"]-posArray["x1"])/4;
			var tempy=(posArray["y4"]-posArray["y1"])/4;

			posArray["x5"]=posArray["x1"]+tempx;
			posArray["x6"]=posArray["x4"]-tempx;
			posArray["y5"]=posArray["y1"]+tempy;
			posArray["y6"]=posArray["y4"]-tempy;
		}

		return posArray;

	}


//获取元素的纵坐标
function getTop(e){
	var offset=getToploop(e)
	offset-=document.getElementById("drawCanvas").scrollTop;
	return offset;
}
function getToploop(e){
	var offset=e.offsetTop;
	if(e.offsetParent!=null && e.offsetParent!=document.body) offset+=getToploop(e.offsetParent);
	else if(e.offsetParent==document.body) offset-=document.body.scrollTop;
	return offset;
}


//获取元素的横坐标
function getLeft(e){
	var offset=getLeftloop(e)
	offset-=document.getElementById("drawCanvas").scrollLeft;
	return offset;
}

function getLeftloop(e){
	var offset=e.offsetLeft;
	if(e.offsetParent!=null && e.offsetParent!=document.body) offset+=getLeftloop(e.offsetParent);
	else if(e.offsetParent==document.body) offset-=document.body.scrollLeft;
	return offset;
}

/**
 * 得到现有流程节点信息集合数组
 * @return 节点信息集合数组，数据格式为[{(0)节点编码，(1)节点名称}]
 */
function getProcessNodesList(){
	var processNodes = [];
	for (var i=0;i<sltObjArray.options.length;i++) {
		var value = sltObjArray.options[i].value;
		var node = document.getElementById(value);
		if(node.ctlType != CNST_CTLTYPE_LINE){
			processNodes.push([value, node.thisCaption]);
		}
	}
	return processNodes;
}

/**
 * 根据节点编码得到节点名称
 * @param nodeCode -节点编码
 * @return 节点名称，若返回null，则表示该节点不存在或者类型为迁移线
 */
function getNodeNameByNodeCode(nodeCode){
	var nodeName = null;
	if(nodeCode != null && nodeCode != ""){
		var node = document.getElementById(nodeCode);
		if(node != undefined && node != null && node.ctlType != CNST_CTLTYPE_LINE){
			nodeName = node.nodeclass.thisCaption;
		}
	}
	return nodeName;
}

/**
 * 验证流程节点名称是否合法<br>
 * <P>
 * 1. 节点名称是否为空；
 * 2. 节点名称是否重复；
 * </P>
 * @param nodename -当前节点名称
 * @param mainDesignWin -设计器主窗口引用
 * @returns {Boolean}
 */
function validateNodeName(nodename, mainDesignWin){
	var isValidated = true;
	nodename = nodename.replace(/[ 　]/g, "");
	// 节点名称为空
	if (nodename == null || nodename == "") {
		alert("请输入节点名称。");
		isValidated = false;
	// 判断节点名称是否与其他节点重复
	} else{
		//校验合法字符
		isValidated = checkIllegalCharacters(nodename);
		if(isValidated && nodename != mainDesignWin.fSelectedObj.nodeclass.thisCaption){
			for (var i = 0; i < mainDesignWin.root.childNodes.length; i++) {
				var node = mainDesignWin.root.childNodes[i];
				if (node.getAttributeNode("atnode").value != mainDesignWin.fSelectedObj.id
						&& node.getAttributeNode("name").value == nodename) {
					alert("节点名称与模型中其他节点相同，请重新输入。");
					isValidated = false;
					break;
				}
			}
		}
	}
	
	
	return isValidated;
}

/**
 * 在节点属性配置界面保存时统一处理节点名称属性<br>
 * <P>
 * 1. 如果节点名称未发生改变，则不作处理；
 * 2. 如果节点名称发生改变，则需要同步修改对应node对象和迁移线中的节点名称；
 * </P>
 * @param nodeName -节点名称
 * @returns 该节点对应的JBPM模型Dom
 */
function handNodeNameOnSave(nodeName){
	var thisNode = null;
	var opener = window.dialogArguments;
	//zw
	var nodename = nodeName.replace(/[ 　]/g, "");
	
	for (var i = 0; i < opener.root.childNodes.length; i++) {
		var node = opener.root.childNodes[i];
		if (node.getAttributeNode("atnode").value == opener.fSelectedObj.id){
			thisNode = node;
		}
	}

	if (nodename != opener.fSelectedObj.nodeclass.thisCaption) {
		var transitions = opener.doc.getElementsByTagName("transition");
		for (var i = 0; i < transitions.length; i++) {
			if (transitions[i].getAttributeNode("to").value == opener.fSelectedObj.nodeclass.thisCaption) {
				transitions[i].getAttributeNode("to").value = nodename;
			}
		}
		opener.fSelectedObj.nodeclass.thisCaption = nodename;
		opener.fSelectedObj.nodeclass.textobj.innerText = nodename;
		opener.fSelectedObj.title = nodename;
		thisNode.getAttributeNode("name").value = nodename;
	}
	
	return thisNode;
}

function checkIllegalCharacters(str){
	var strRegex = "^(\\w*";//字母，下划线，数字
		strRegex +="[\\u4e00-\\u9fa5]*";//汉字
		strRegex +="[";
		strRegex +="\\(（\\)）";//小括号（中英）
		strRegex +="\\[【\\]】";//中括号（中英）
		strRegex +="{｛}｝";//大括号（半角圆角）
		strRegex +="\\.。,，";
		
		strRegex +="]*)*$";
	var errorMessage = "字符“ "+ str +" ”不合法，合法字符只能包含：“字母”、“数字”、“汉字”、“下划线”、“括号”、“句号”、“逗号”";
	var re = new RegExp(strRegex);
	if (re.test(str)){
		return true; 
	}else{
		alert(errorMessage);
		return false;
	}
	
}


