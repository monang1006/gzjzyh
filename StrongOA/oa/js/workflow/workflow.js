/*
	抽取工作流中一些共性操作，主要从工作处理的提交流程和审批流程中抽取。
*/
  //提交流程时的一些共性操作
  var selectedWorkflowName = "";//用于控制选择了一次流程后再次选择此流程
  var hasLoaded = false;//用于控制选择了一次迁移线以后不用重新加载,即控制loadTransInfo()只执行一次
  var inputCount = 125;//默认的审批意见长度
  var maxActors = "";//节点上设置的最大处理人数
  var selectCount = "";//选择的人员数
  var contextPath = "";//定义获取调用此JS的上下文路径,从form的action属性获取
  
  $(document).ready(function(){
  		$("#chooseUser").hide();
  		$("#userFieldset").hide();
  		
	    var fullContextPath = $("form").attr("action");
  		contextPath = fullContextPath.substring(0,fullContextPath.indexOf("!"));
	    dispalyStep(step);
	     $("#yj").change(function(){
        	var value = $(this).val();
        	if(value != "0"){
        		$("#suggestion").val(value);
        	}
        });
  }); 
		
  /**
	预览流程图
	@param workflowId 流程定义id
  */
  function workFlowView(workflowId) {
  	if(!workflowId){//当流程id定义不存在时，获取已选择的流程
  		workflowId = $("input:radio[name=workflowName]:checked").attr("id");
  	}
  	location = scriptroot + "/fileNameRedirectAction.action?toPage=workflow/pdimageprew.jsp&workflowId="+workflowId;
  }

  /**
   * 选择了工作流程,点击下一步
   * @param {} workflowName 已选择的流程
   * @return {}
   */
  function nextStepForChooseWorkflow(){
	 	var workflowName = $("input:radio[name=workflowName]:checked").val();//流程名称
	 	if(workflowName){//说明是在第一次提交流程页面
		 	if(workflowName == selectedWorkflowName){
		 		//do nothing 解决BUG -- bug-1658 
		 		//return ;
		 	}
		 	initialDefaultInfo(workflowName);
		 	var fullContextPath = $("form").attr("action");
		 	var contextPath = fullContextPath.substring(0,fullContextPath.indexOf("!"));
		 	$.getJSON(contextPath+"!getNextTransitions.action",
		 	          {workflowName:encodeURI(workflowName)},
		 	          function(trans){
		 	            selectedWorkflowName = workflowName;
				 		var transHtml = trans.transHtml;
				 		var usersHtml = trans.usersHtml;
				 		var menuButtonTableHtml = trans.menuButtonTableHtml;
				 		$("#nextstep").html(transHtml);
				 		$("#users").html(usersHtml);
				 		if($(window.parent.document)){
					 		var menu = $(window.parent.document).find("#menu");
					 		menu.html("");//清楚按钮信息，防止页面刷新导致重复按钮出现
					 		if($(window.parent.document).find("#toNext").attr("isMenuButton")=="1"){
						 		menu.html(menuButtonTableHtml+menu.html());
						 		$(window.parent.document).find("#toNext").hide();//隐藏办理按钮
					 			if(typeof(window.parent.unmaskPrivilege) != "undefined"){
							 		window.parent.unmaskPrivilege();
						 		}
					 		}else{
						 		if($(window.parent.document).find("#toNext").attr("isOpen")=="1"){//展开模式
						 			$(window.parent.document).find("#toNext").hide();//隐藏办理按钮
						 			$(window.parent.document).find("#Open_Submit").show();//展开模式显示“提交”按钮
						 		}else{
						 		}
					 		}
					 		$(window.parent.document).find("#privilege").show();//显示节点设置的按钮
				 		}
				 		doSelectPersonWithOneLine();
		 	});
	 	}else{
	 		loadTransInfo();
	 	}
	 	var taskId = $("#taskId").val();
		  if(!taskId || taskId == ""){
		  	taskId = 0;
		  }
	 }
	 
	 function chooseUserUrl(nodeId,taskId){
	 }

	 //空函数，用于初始化
	 function initialDefaultInfo(){}
	 
	 /**
	  * 审批流程时,加载迁移线信息
	  * @param hasLoaded 是否已经加载过了
	  * @see work-nextstep.jsp
	  */
	function loadTransInfo(){
			if(hasLoaded){
				return ;
			}
			$("#nextstep").mask("加载中...");
		 	$.getJSON(contextPath+"!getNextTransitions.action",
		 	          {taskId:$("#taskId").val(),
		 				srand:(new Date()).getTime(),
		 	          instanceId:$("#instanceId").val(),
		 	          workflowName:encodeURI($("#workflowName").val()),
		 	          //id="toNext"存在于父页面(sendDoc-input.jsp)中
		 	          isMenuWin:$("#toNext",window.parent.document).attr("isMenuButton")},
		 	          function(trans){
				 		var transHtml = trans.transHtml;
				 		var usersHtml = trans.usersHtml;
				 		var menuButtonTableHtml = trans.menuButtonTableHtml;
				 		$("#nextstep").html(transHtml);
				 		
				 		//草拟收文登记时隐藏退文按钮  modify by luosy 2013-05-21
				 		//alert("${workflowName}"+$("#workflowName").val()+"\n ${currentNodeName}"+$("#toNext",parent.document).attr("nodeName")+"\n ${bussinessId}"+$("#bussinessId").val());// == '收文办件登记' && nodeName == '公文分发' && bussinessId != null")
				 		//alert(transHtml);
				 		//alert($("#nextstep").children("div").next().next().html() )
				 		//$("#nextstep").children("div").next().next().hide();
				 		try{
				 			if("收文办件登记"==$("#workflowName").val() 
				 					&& "公文分发" == $("#toNext",parent.document).attr("nodeName")
				 					&& ("" == $("#bussinessId").val()) || ("null" == $("#bussinessId").val())||(null == $("#bussinessId").val())){
				 				//$("#nextstep,div").next().next().hide();
				 				$("#nextstep").children("div").next().next().hide();
				 			}
				 		}catch(e){ }
				 		//草拟收文登记时隐藏退文按钮  modify by luosy 2013-05-21
				 		$("#users").html(usersHtml);
				 		if($(window.parent.document)){
					 		var menu = $(window.parent.document).find("#menu");
					 		menu.html("");//清楚按钮信息，防止页面刷新导致重复按钮出现
					 		if($(window.parent.document).find("#toNext").attr("isMenuButton")=="1"){
						 		menu.html(menuButtonTableHtml+menu.html());
						 		$(window.parent.document).find("#toNext").hide();//隐藏办理按钮
						 		if(typeof(window.parent.unmaskPrivilege) != "undefined"){
							 		window.parent.unmaskPrivilege();
						 		}
					 		}else{
						 		if($(window.parent.document).find("#toNext").attr("isOpen")=="1"){//展开模式
						 			$(window.parent.document).find("#toNext").hide();//隐藏办理按钮
						 			$(window.parent.document).find("#Open_Submit").show();//展开模式显示“提交”按钮
						 		}else{
						 		}
					 		}
					 		$(window.parent.document).find("#privilege").show();//显示节点设置的按钮
				 		}
				 		doSelectPersonWithOneLine();
				 		hasLoaded = true;
				 		$("#nextstep").unmask();
		 	});
	} 

	/**
	 * 只有一条迁移线时需要默认展现人员选择树，并选择迁移线。
	 */
	function doSelectPersonWithOneLine(){
		var transitions = document.getElementsByName("transition");
		if(transitions.length == 1){
			var taskId = $("#taskId").val();
			if(transitions[0].checked == true){
				//获取当前节点设置信息
				$.getJSON(contextPath+"!getNodeSettingInfo.action",
				{taskId:taskId,timestamp:new Date()},function(result){
	           		chooseNextStep(transitions[0].id, transitions[0].concurrentFlag,transitions[0].nodeid,transitions[0].value,transitions[0].state,result)
				});
           	}
		}
	}
	
	/**
	 * 显示界面,整个页面分为3个界面
	 * 1、选择流程阶段
	 * 2、选择迁移线和选择任务处理人阶段
	 * 3、提交表单阶段
	 * @param {} step 标示当前所处阶段
	 */
	function dispalyStep(step) {
	   switch(step) {
             case 1:               
               $("#btn_first").show();
               $("#btn_second").hide();
               $("#btn_three").hide();
               $("#step1").show();
               $("#step2").hide();
               $("#step3").hide();
               $("#msg").hide();
               break;
             case 2:
               nextStepForChooseWorkflow();
               $("#btn_first").hide();
               $("#btn_second").show();
               $("#btn_three").hide();
               $("#step1").hide();
               $("#step2").show();
               $("#step3").hide();
               $("#msg").show();
               break;
             case 3:
               $("#btn_first").hide();
               $("#btn_second").hide();
               $("#btn_three").show();
               $("#step1").hide();
               $("#step2").hide();
               $("#step3").show();
               $("#msg").hide();
               break;
         }
	}      	 
	 
	/**
  	选择下一步按钮之前的一些验证操作：
  	1、验证是否选择了流程,通过是否勾选了某个流程验证。
  	2、验证是否选择了下一步步骤,通过是否选择了某条迁移线。
  	3、验证是否选择了任务处理人,因为存在并发情况,这里需要验证每个任务是否选择了处理人
  	@param step 当前处于的步骤
  		step=1:选择流程的界面
  		step=2:选择下一步步骤,选择任务处理人
  	@author yanjian 2011-11-03 17:09
  	  */
  function processStep(step) {
	   var ret = true;//定义函数返回值
	   switch(step) {
         case 1:
           if (!$("input:radio[name=workflowName]:checked").val()) {//不存在已经选择的名为“workflowName”的单选按钮
               ret = false;
               alert("请选择流程！");                   
           }
           break;
     	  case 2:
     	   var hasSelectTransition = false;
           var hasSelectActors = true;
           var transitions = $("input[name='transition']");
           for(var i=0; i<transitions.length; i++){
           		if(transitions[i].checked == true){
					hasSelectTransition = true;
	       			var flag = transitions[i].concurrentFlag;
	       			var nodeid = transitions[i].nodeid;
	       			var transid = transitions[i].id;
					$("input:hidden[id^='strTaskActors_'][transId='"+transid+"']").each(function(){
				   		if($(this).val() == ""){
				   			hasSelectActors = false;   
				   		}else{
				   			hasSelectActors = true;
				   		}
			  		 });       			
           		}
           }
           if (!hasSelectTransition) {
               ret = false;
               alert("请选择下一步骤！");
           } else if (!hasSelectActors) {
               ret = false;
               alert("请选择下一步处理人！");
           }
           break;
         }
         return ret;
	}
	
	/**
	 * 点击上一步按钮
	 * @param step 标示当前所处阶段
	 */
	function lastStep() {
         step -= 1;
         dispalyStep(step);         
	}

	/**
	 * 点击下一步按钮
	 * 操作之前会做些选择信息进行验证
	 * @see processStep()
	 * @param step 标示当前所处阶段
	 */
	function nextStep() {
		if (processStep(step)) {
			step += 1;
			dispalyStep(step);
		}
  	}
  	
  	//当点击上一步的过程中要进行对IFRAME中的内容进行清空Dony
		function changeIframe(){
			$("#chooseUser").attr({src:""});
			$("#chooseUser").hide();
  			$("#userFieldset").hide();
		}

	//隐藏或显示选择人员控件,依据node_DIV下是否有内容判断
	function showOrHideNodeDiv(obj){
		var flag = null;
		$("div[id^=nodeDiv_]").each(function(){
			if($(this).html() != "" && $(this).is(":visible")){
				if(flag == null){
					flag = true;
				}
			}
		});
		if(flag == true){
			$("#userFieldset").show();
		} else {
			$("#userFieldset").hide();
		}
	}
  	
  	//校验是否选择了不同分组下的迁移线
  	function checkChooseOtherTranstion(group){
  		var flag = false;
  		$("input:checked").each(function(){
  			var g = $(this).attr("group");
  			if(g){
  				if(g != group){
  					flag = true;
  				}
  			}
  		});
  		return flag;
  	}
  	
  	  /**
	 *  点击某一条迁移线,用于显示迁移线对应的目标任务处理人选择界面
	 *  @param id 选择的迁移线id
	 *  @param concurrentFlag 是否是并发模式： “0”：非并发；“1”：并发;"2"：子流程并且父节点需要选择人员;"3"：非动态选择处理人(包括结束节点)应该隐藏树
	 *  @modify yanjian 2011-11-08 14:00 
	 */
	function chooseNextStep(id, concurrentFlag,nodeId,nodeName,endNodeState,result) {  
		try{
			//刷新清空人员选中的人员信息 added by yanjian 2010年11月28日 18:44	
			if($("input:hidden[id ='strTaskActors_'"+ nodeId+"][transId='"+id+"']")!=undefined){
				$("input:hidden[id ='strTaskActors_'"+ nodeId+"][transId='"+id+"']").val("");
			}
		  //处理是否直接返回经办人的情况 added by dengzc 2010年10月9日13:56:27	
		  if(result && result != ""){
			  var returnTaskActor = result.returnTaskActor;
			  if(returnTaskActor != "" && returnTaskActor == "1"){//直接返回经办人
			  	document.getElementById("strTaskActors_" + nodeId).value = "p0|" + nodeId;
			  	$("input:radio[name='transition']").attr("disabled",true);
			  	return ;
			  }
		  }	
		  //end
          var transitionId = id;
          var group = $("#"+id).attr("group");
		  var tranlineType = $("input[id='"+id+"'][nodeid='"+nodeId+"']").attr("type");
		  //-statr yanjian 2011-12-20 20:04 获取该条迁移线上要验证的信息(省办公厅需求)
		  var tranlineValidate = $("input[id='"+id+"'][nodeid='"+nodeId+"']").attr("tranlineValidate");
		  var pdfValidate = $("input[id='"+id+"'][nodeid='"+nodeId+"']").attr("pdfValidate");
		  if($(window.parent.document)){
		      	var isValidate =  $(window.parent.document).find("#isValidate").val();
		  		if($(window.parent.document).find("#isValidate")){
		  			if(tranlineValidate != ""){
		  				 $(window.parent.document).find("#isValidate").val(id+"|"+nodeId);
		  			}else{
		  				$(window.parent.document).find("#isValidate").val("");
		  			}
		  		}
		  		var transpdf =  $(window.parent.document).find("#transpdf").val();
		  		if($(window.parent.document).find("#transpdf")){
		  			if(pdfValidate != ""&&pdfValidate!=null){
		  				 $(window.parent.document).find("#transpdf").val(id+"|"+nodeId);
		  			}else{
		  				$(window.parent.document).find("#transpdf").val("");
		  			}
		  		}
		  	}
		  	
		  //-end
		  	
          if(checkChooseOtherTranstion(group)){
          	//alert("不能选择不同分组下的路径.");
          	showTip('<div class="tip" id="loading">不能选择不同分组下的路径.</div>');
          	$("#"+id).attr("checked",false);
          	return ;
          }
          if($("input[id='"+id+"']").attr("selectPersonType") == "buttonType"){//按钮方式选择人员
          	$("SPAN[id^='users_']").html("");//清楚用户名显示
      		$("input[id^=strTaskActors_]").val("");//清楚用户id
      		$("input[id^=taskActor_]").val("");
      		$("div[id^=nodeDiv_]").hide();//隐藏所有div
      		$("#chooseUser").attr({src:""});
          	$("#chooseUser").hide();
          	$("#userFieldset").show();
          	$("#nodeDiv_" + id).show();//和目标迁移线不一致的隐藏目标迁移线对应任务处理人选择界面
          }else{
          	  if(concurrentFlag == 1){
	          		if(tranlineType=="radio"){//迁移线类型设置为单选时
	          			var transitions = document.getElementsByName("transition");//得到所有的迁移线
			      		for(var i=0; i<transitions.length; i++){
			      			$("#nodeDiv_" + transitions[i].id).hide();//和目标迁移线不一致的隐藏目标迁移线对应任务处理人选择界面
			      		}
			      		if(endNodeState==0){
			  				maxActors = "";//切换了迁移线后清空数据
							var taskId = $("#taskId").val();
							if(!taskId || taskId == ""){
								taskId = 0;
							}
							if(nodeId != null){
								$("#chooseUser").show();
				  				$("#userFieldset").show();
								var url = contextPath + "!chooseForWorkflow.action?nodeId="+nodeId+"&transitionId="+transitionId+"&taskId="+taskId+"&timestamp="+new Date()+"&nodeName="+encodeURI(encodeURI(nodeName));
									$("#chooseUser").attr({src:url});
									$("#userFieldset").mask("正在加载,请稍后...");
								}
							}
	          		}else{
	          			$("#chooseUser").hide();
			          	//针对并发节点修改
			          	var spanId = $("#nodeDiv_" + transitionId).children("span").attr("id");
		          		$("div[id^=nodeDiv_]").each(function(){
							if($(this).children("span").attr("id") == spanId && $(this).is(":visible")){
								$(this).hide();
							}
						});
		          		if(document.getElementById(transitionId).checked == true){
			          		$("#nodeDiv_" + transitionId).show();
		          		}else{
			          		$("input[nodeid="+spanId.split("_")[1]+"]").each(function(){
			          			if($(this).is(":checked")){
			          				$("#nodeDiv_" + transitionId).show();
			          			}
			          		});
		          		}
		          		showOrHideNodeDiv($("#nodeDiv_" + transitionId));
	          		}
	          }else if(concurrentFlag == 2){
	          	  $("#chooseUser").hide();
	  			  $("#userFieldset").show();
		          var transitions = document.getElementsByName("transition");//得到所有的迁移线
		          		for(var i=0; i<transitions.length; i++){
		          			if(transitions[i].id == transitionId){
		          				$("#nodeDiv_" + transitions[i].id).show();//和目标迁移线一致的显示目标迁移线对应任务处理人选择界面
		          			}else{
		          				$("#nodeDiv_" + transitions[i].id).hide();//和目标迁移线不一致的隐藏目标迁移线对应任务处理人选择界面
		          			}
		          		}
	          }else if(concurrentFlag == 3){////add by dengzc 2010年3月30日20:33:06//非动态选择处理人应该隐藏树
	          	$("#chooseUser").hide();
	  			$("#userFieldset").hide();
	          	var transitions = document.getElementsByName("transition");//得到所有的迁移线
	      		for(var i=0; i<transitions.length; i++){
	      			$("#nodeDiv_" + transitions[i].id).hide();//和目标迁移线不一致的隐藏目标迁移线对应任务处理人选择界面
	      		}
	          }else{
	          	var transitions = document.getElementsByName("transition");//得到所有的迁移线
	      		for(var i=0; i<transitions.length; i++){
	      			$("#nodeDiv_" + transitions[i].id).hide();//和目标迁移线不一致的隐藏目标迁移线对应任务处理人选择界面
	      		}
	      		if(endNodeState==0){
		          	$("#chooseUser").show();
	  				$("#userFieldset").show();
	  				maxActors = "";//切换了迁移线后清空数据
					var taskId = $("#taskId").val();
					if(!taskId || taskId == ""){
						taskId = 0;
					}
					var url = contextPath + "!chooseForWorkflow.action?nodeId="+nodeId+"&transitionId="+transitionId+"&taskId="+taskId+"&timestamp="+new Date()+"&nodeName="+encodeURI(encodeURI(nodeName));
						$("#chooseUser").attr({src:url});
						$("#userFieldset").mask("正在加载,请稍后...");
					}
	          }
          }
		}catch(e){}
      }
  	
      function changeChoose(nodeId){

      }
      
   	 /**
     * 选择任务处理人
     * @param nodeId 任务节点id
     * @param transId 迁移线id(实际的迁移线id)
     * @param selectTransId (用于控制选人显示的标识id)用于解决多个子流程同时为父流程选人时无法将选中的人取消的情况 bug-2656
     * @author yanjian 2011-11-06 13:03
     */
   	function chooseActors(nodeId,transId,selectTransId) {
   		  var taskActors=$("input[id=taskActor_"+nodeId+"][transId='"+selectTransId+"']").val();//此节点已选的人员
		  if(taskActors == null){//第一次选择人员
		  	taskActors = "";
		  }
		  var taskId = $("#taskId").val();
		  if(!taskId || taskId == ""){
		  	taskId = 0;
		  }
          var url =scriptroot+"/workflowRun/action/runUserSelect!input.action?dispatch=assign&taskId="+taskId+"&nodeId=" + nodeId +
                     "&taskActors="+encodeURI(encodeURI(taskActors))+"&transitionId="+transId+"&workflowName="+encodeURI(encodeURI($("#workflowName").val()));
          var str = OpenWindow(url, 400, 400, window);          
          //用户ID|用户名称,用户ID|用户名称
          if(str!=null && str!=''){
          	  
          	  //回传选人动作
          	 $("input[id=taskActor_"+nodeId+"]").val(str);
	          var usestr = str.split(",");
	          var userinfo="";
	          var username="";
	          var userids="";
	          for(var i=0;i<usestr.length;i++){
	          	var userinfo=usestr[i].split("|");
	          	userids+=","+userinfo[0];
	          	username+=","+userinfo[1];
	          }
	          //获取用户ID
	          var userid = userids.split(",");
	          var strTaskActors="";
	          //组合用户ID和节点ID传给后台处理
	          for(var j=0;j<userid.length;j++){
	          	if(userid[j]!=null && userid[j]!=''){
	          		strTaskActors+=","+userid[j].substring(1)+"|"+nodeId;
	          	}
	          }
	          $("span[id=users_"+nodeId+"]").html(username.substring(1));
          	  $("input[id=strTaskActors_"+nodeId+"]").val(strTaskActors.substring(1));
          	  // ----------- End -----------------------
          }else{
          	if(str!=null && str == ''){
          		$("SPAN[id='users_"+nodeId+"'][transId='"+selectTransId+"']").html("");
          		$("input[id=strTaskActors_"+nodeId+"][transId='"+selectTransId+"']").val("");
          		$("input[id=taskActor_"+nodeId+"][transId='"+selectTransId+"']").val("");
          	}
          }
   	}  
   	
   	/**
   	 * 获取提醒方式
   	 * 1、消息提醒,默认在标签中提供了
   	 * 2、邮件提醒,需要打开了开关,并且当前用户有默认邮箱才会显示
   	 * 3、手机短信提醒,需要短信开关开启
   	 * 4、Rtx提醒,需要开关开启
   	 */	
    function getRemindValue(){
    	if(!checkRemindCont()){
    		return false;
    	}
		var returnValue = "";
		$("#msg").find("input:checkbox:checked").each(function(){//id为msg的容器中查找选中的checkbox
			returnValue = returnValue + $(this).val() + ",";//多个提醒类型以‘,’隔开
		});
		if(returnValue!=""){
			returnValue = returnValue.substring(0,returnValue.length-1);
		}
		$("#remindType").val(returnValue);
		return true;
	}
	
	//数据校验 yanjian 2011-11-08 10:35
	function doPreSubmit(){
		if(!processStep(step)){
			return false;
		} 
  	  	if(maxActors != "" && selectCount != ""){
  	  		if(selectCount > maxActors){
		  	  	alert("允许最大参与人数为" + maxActors + "人，您选择了" + selectCount + "人，请重新选择！");
		  	  	return false;
  	  		}
  	  	}
  	  	var transitionNames = "";
  	  	var transitionId = "";
        var taskActors = "";
        var returnDeptFlag = "";//协办处室标志
        var transitions = $("input[name='transition']");
        for(var i=0; i<transitions.length; i++){
        	if(transitions[i].checked == true){
	           transitionNames += "," + transitions[i].value;//迁移线名称
	           transitionId += "," + transitions[i].id;//迁移线名称
	           if(transitions[i].transDept && transitions[i].transDept == "1"){
	           	returnDeptFlag += "," + transitions[i].transDept;//协办处室标志
	           }
	           var flag = transitions[i].concurrentFlag;
	           var nodeid = transitions[i].nodeid;
	       	   var transid = transitions[i].id;
	       	   
	           $("input:hidden[id^='strTaskActors_'][transId='"+transid+"']").each(function(){
			   		if($(this).val() != ""){
			   			taskActors += "," + $(this).val(); 
			   		}
			  	});     
        	}
        }
  	  	if(taskActors != ""){
  	  		$("#strTaskActors").val(taskActors.substring(1));//任务处理人
  	  	}
  	  	if(returnDeptFlag != ""){
  	  		returnDeptFlag = returnDeptFlag.substring(1);
  	  	}
  	  	$("#returnFlag").val(returnDeptFlag);//协办处室标志,多个协办处室格式为:1,1,1
  	  	$("#transitionName").val(transitionNames.substring(1));//迁移线
  	  	$("#transitionId").val(transitionId.substring(1));//迁移线id
  	  	if(!getRemindValue()){
  	  		return false;
  	  	}
  	  	var parWin = parent;
  	  	if(parWin){
  	  		if(parWin.document.getElementById("handlerMes")){
		  	  	parWin.document.getElementById("handlerMes").value = document.getElementById("handlerMes").value;
		  	  	var suggestionStyle = $(window.parent.document).find("#suggestionStyle").val();
		  	  	//alert(suggestionStyle);
		  	  	if(suggestionStyle == "1"){
		  	  		parWin.document.getElementById("suggestion").value = document.getElementById("suggestion").value;		  	  		
		  	  	}
		  	  	parWin.document.getElementById("transitionName").value = document.getElementById("transitionName").value;
		  	  	parWin.document.getElementById("transitionId").value = document.getElementById("transitionId").value;
		  	  	parWin.document.getElementById("strTaskActors").value = document.getElementById("strTaskActors").value;
		  	  	parWin.document.getElementById("remindType").value = document.getElementById("remindType").value;
		  	  	parWin.document.getElementById("remindSet").value = document.getElementById("remindSet").value;
		  	  	parWin.document.getElementById("returnFlag").value = document.getElementById("returnFlag").value;
		  	  	parWin.document.getElementById("isGenzong").value = document.getElementById("isGenzong").value;
  	  		} 
  	  	} 
  	  	return true;
	}
	
	function doMask(){
		$("body").mask("正在提交流程,请稍后...");
	}
   		
   	function doCancelMask(){
   		$("body").unmask();
   	}	
   		
	/**
	 * 提交表单
	 */
	function doSubmit(){
	//当流程节点设置为审批意见为必填时  此处做限制
	var suggestionrequired = $("#suggestionrequired").val();
	var suggestionStyle =0;
	if(window.dialogArguments.document.getElementById("suggestionStyle")){
		suggestionStyle = window.dialogArguments.document.getElementById("suggestionStyle").value;
	}  	
	if(suggestionrequired==1){
		if(suggestionStyle==0){
			var tempsuggestion = $.trim(window.dialogArguments.document.getElementById("suggestion").value);
			if(tempsuggestion==""||tempsuggestion==null){
				alert("审批意见不能为空。");
				return;
			}
		}else{
			var tempsuggestion = $.trim($("#suggestion").val());
			if(tempsuggestion==""||tempsuggestion==null){
				alert("审批意见不能为空。");
				return;
			}
		}
		
	}
 	 var genzon=document.getElementById("genzong");
	 if(genzon!=null){
         if(genzon.checked==true){
          $("#isGenzong").val('1');
         }else
         $("#isGenzong").val('0');
       }  
		if(!processStep(step)) return ;
        var transitionNames = "";
        var transitionId = "";
        var taskActors = "";
        var returnDeptFlag = "";//协办处室标志
        var transitions = document.getElementsByName("transition");//得到所有迁移线集合
        for(var i=0; i<transitions.length; i++){
           if(transitions[i].checked == true){//得到选中的迁移线
               transitionNames += "," + transitions[i].value;//迁移线名称
               transitionId += "," + transitions[i].id;//迁移线名称
               if(transitions[i].transDept && transitions[i].transDept == "1"){
               	returnDeptFlag += "," + transitions[i].transDept;//协办处室标志
               }
               var flag = transitions[i].concurrentFlag;
               if(flag == 2){//子流程并且父流程需要选择人员
				   $("input:hidden[id^='strTaskActors_']").each(function(){
				   		if($(this).val()!=""){
					   		taskActors += "," + $(this).val();
				   		}
				   });
               }else{
               		if($("#strTaskActors_" + transitions[i].nodeid).val() != "" && 
	               			$("#strTaskActors_" + transitions[i].nodeid).val() != undefined){
				   		taskActors += "," + $("#strTaskActors_" + transitions[i].nodeid).val();
				   }
               }
            }
        }
  	  	if(taskActors != ""){
  	  		$("#strTaskActors").val(taskActors.substring(1));//任务处理人
  	  	}
  	  	if(returnDeptFlag != ""){
  	  		returnDeptFlag = returnDeptFlag.substring(1);
  	  	}
  	  	$("#returnFlag").val(returnDeptFlag);//协办处室标志,多个协办处室格式为:1,1,1
  	  	$("#transitionName").val(transitionNames.substring(1));//迁移线
  	  	$("#transitionId").val(transitionId.substring(1));//迁移线id
  	  	getRemindValue();//读取提醒方式
  	  	var businessName = $("#businessName").val();
  	  	businessName = businessName.replace(/\+/g, "%2B"); 
  	  	$("#businessName").val(businessName);
  	  	if(maxActors != "" && selectCount != ""){
  	  		if(selectCount > maxActors){
		  	  	alert("允许最大参与人数为" + maxActors + "人，您选择了" + selectCount + "人，请重新选择！");
		  	  	return ;
  	  		}
  	  	}
  	  	var parWin = window.dialogArguments;
  	  	if(parWin){
  	  		if(parWin.document.getElementById("handlerMes")){
  	  		 $("#handlerMes").val(encodeURIComponent($("#handlerMes").val()));
		  	  	parWin.document.getElementById("handlerMes").value = document.getElementById("handlerMes").value;
		  	  	var suggestionStyle = $(window.parent.document).find("#suggestionStyle").val();
		  	  	if(suggestionStyle != "0"){
		  	  		parWin.document.getElementById("suggestion").value = document.getElementById("suggestion").value;		  	  		
		  	  	}
		  	  	//weblogic环境下 标题，迁移线名称 机构名称  流程流水号 用户名乱码的问题 
		  	    $("#businessName").val(encodeURIComponent($("#businessName").val()));
				$("#transitionName").val(encodeURIComponent($("#transitionName").val()));
				$("#orgName").val(encodeURIComponent($("#orgName").val()));
				$("#workflowCode").val(encodeURIComponent($("#workflowCode").val()));
				$("#userName").val(encodeURIComponent($("#userName").val()));
				parWin.document.getElementById("orgName").value = document.getElementById("orgName").value;
				parWin.document.getElementById("workflowCode").value = document.getElementById("workflowCode").value;
				parWin.document.getElementById("userName").value = document.getElementById("userName").value;
				
		  	    parWin.document.getElementById("businessName").value = document.getElementById("businessName").value;
		  	  	parWin.document.getElementById("transitionName").value = document.getElementById("transitionName").value;
		  	  	parWin.document.getElementById("transitionId").value = document.getElementById("transitionId").value;
		  	  	parWin.document.getElementById("strTaskActors").value = document.getElementById("strTaskActors").value;
		  	  	parWin.document.getElementById("remindType").value = document.getElementById("remindType").value;
		  	  	parWin.document.getElementById("remindSet").value = document.getElementById("remindSet").value;
		  	  	parWin.document.getElementById("returnFlag").value = document.getElementById("returnFlag").value;
		  	  	parWin.document.getElementById("isGenzong").value = document.getElementById("isGenzong").value;
		  	  	parWin.saveFormDataAndWorkflow(window);
		  	  	window.returnValue = "OK";
  	  		} else {
  	  			$("form").submit();//提交表单
  	  		}
  	  	} else {
  	  		$("form").submit();//提交表单,在流程草稿中直接提交流程
  	  	}
        $("body").mask("正在提交流程,请稍后...");
   }
   
   /**
    * 查看流程图
    * @param contextPath 上下文路径 返回值如：/StrongOA/work/work
    * @param instanceId 流程实例id
    */
   function viewPDImage(){
   	  var width=screen.availWidth-10;
   	  var height=screen.availHeight-30;
   	  var prewUrl = scriptroot + "/fileNameRedirectAction.action?toPage=workflow/pdimageprew.jsp&workflowName="+encodeURI(encodeURI($("#workflowName").val()));
   	  var viewUrl = contextPath + "!PDImageView.action?instanceId="+$("#instanceId").val();
   	  if($("#instanceId").val() != ""){
	   	  var ReturnStr=OpenWindow(viewUrl,width, height, window);
   	  }else{
   	  	if($("#workflowName").val() != ""){
   	  		var ReturnStr=OpenWindow(prewUrl,width, height, window);
   	  	}
   	  }
   }

   /**
    * 查看办理记录
    * @param contextPath 上下文路径 返回值如：/StrongOA/work/work
    * @param taskId 任务id
    */
   function annal(){
   	 OpenWindow(contextPath + "!annallist.action?taskId="+$("#taskId").val(),500, 300, window);
   }
   		
	//增加列单击事件,方便勾选操作.
	function doSelect(id){
		document.getElementById(id).checked = true;
	}	
	
   /**
    * @author yanjian 2012-06-22 00:35
    * 选择处理人【迁移线设置按钮型选人模式】
    */
      function chooseButtonActors() {
      	var tranObj = $("input:radio:checked");
      	var nodeId = tranObj.attr("nodeid");
      	var selectTransId = tranObj.attr("id");
      	var transId = selectTransId;
      	 var taskActors=$("input[id=taskActor_"+nodeId+"][transId='"+selectTransId+"']").val();//此节点已选的人员
		  if(taskActors == null){//第一次选择人员
		  	taskActors = "";
		  }
		  var taskId = $("#taskId").val();
		  if(!taskId || taskId == ""){
		  	taskId = 0;
		  }
		   var url = scriptroot + "/workflowRun/action/runUserSelect!input.action?selectPersonType=buttonType&taskId="+taskId+"&nodeId=" + nodeId +
                     "&taskActors="+encodeURI(encodeURI(taskActors))+"&transitionId="+transId+"&workflowName="+encodeURI(encodeURI($("#workflowName").val()));
      		var str=OpenWindow(url,"600","400",window);
      }