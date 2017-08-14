/**
 * 一些其他的公用JS操作
 */
 var contextPath = "";//定义获取调用此JS的上下文路径,从form的action属性获取
$(document).ready(function(){
	var fullContextPath = $("form").attr("action");
  	contextPath = fullContextPath.substring(0,fullContextPath.indexOf("!"));//得到上下文路径
  	 //搜索
   $("#img_search").click(function(){
   	$("form").submit();
   });
});

/**
 * 根据提醒内容催办
 *  @param urgencytype 催办类型
 *  @param instanceId 流程实例id
 *  @param handdlerMes 催办内容
 *  @return 催办结果
 *  	1、催办成功返回0；
 * 		2、流程实例不存在返回-1；
 * 		3、抛出异常返回-2；
 */
function urgencyProcessByPerson(){
	var returnValue = "";
	var feedBack=$("#feedBack").val();
	$("#msg").find("input:checkbox:checked").each(function(){
		returnValue = returnValue + $(this).val() + ",";
	});
   if(returnValue == ""){
	   alert("请选择提醒方式。");
    	return ;
    }
	if(returnValue!=""){
		returnValue = returnValue.substring(0,returnValue.length-1);
	}
    var content = $.trim($("#handlerMes").val());
    var orguserid="";
    if(feedBack!="true"){ 
    	if(content == ""){
    	alert("请输入提醒内容。");
    	return ;
      }else{
    	  if(content.length>100){
      		alert("对不起，您输入的内容过长,最多100个字符。");
      		return ;
      	}
      }
    }else{
        orguserid=document.getElementById("orguserid").value;//收件人id
        if(orguserid=="" ){
        	alert("请选择收件人。");
        	return;
        }
    	if(content == ""){
        	alert("请输入反馈内容。");
        	return ;
          }
    	if(content != ""&&content.length>1000){
    		alert("对不起，您输入的内容过长,最多1000个字符。");
    		return ;
    	}
    }
    
    var url="";
    var taskId=document.getElementById("taskId");
    var nodeId=document.getElementById("nodeId");
    if(taskId && taskId.value!=""){
    	url=contextPath + "!urgencyTaskInstanceByPerson.action";
    	taskId = taskId.value;
    	nodeId = nodeId.value;
    }else{
    	if(feedBack=="true"){
    		url=contextPath + "!feedBackProcessByPerson.action";
   		}else{
   			url=contextPath + "!urgencyProcessByPerson.action";}
    	
    }
   // alert(url)
    var xiha =  $("#xiha1").val();
    if(typeof($("body").mask) != "undefined"){
	    $("body").mask("系统正在提交数据。。。");
    }
	    $.post(url,
	    	   {urgencytype:returnValue,instanceId:$("#instanceId").val(),handlerMes:content,nodeId:nodeId,taskId:taskId,orguserid:orguserid,xiha1:xiha},
	    	   function(retCode){
	    	   	if(retCode == "0"){
	    	   		if(feedBack=="true"){
	    	   			alert("反馈成功。");
	    	   		}else{
	    	   			alert("催办成功。");
	    	   			}
	    	   		window.close();
	    	   	}else if(retCode == "-1"){
	    	   		if(feedBack=="true"){
	    	   			alert("流程实例不存在或已删除，反馈失败。");
	    	   		}else{
	    	   			
	    	   		alert("流程实例不存在或已删除，催办失败。");}
	    	   	}else if(retCode == "-2"){
	    	   		if(feedBack=="true"){
	    	   			alert("反馈失败。");
	    	   		}else{
	    	   			
	    	   			alert("催办失败。");}
	    	   		
	    	   	}else if(retCode=="-3"){
	    	   		if(feedBack=="true"){
	    	   			alert("只能反馈当前任务。");
	    	   		}else{
	    	   			
	    	   			alert("只能催办当前任务。");}
	    	   		
	    	   	}
	    	   	 if(typeof($("body").unmask) != "undefined"){
		    	   	$("body").unmask();
	    	   	 }
	    	   }
	   	);
}

/**
 * 任务取回
 * @param taskId 任务id
 * @return retCode
 * 	1、流程实例已结束 0；
 *  2、任务已被签收处理1；
 * 	3、任务取回成功2；
 * 	4、任务实例不存在返回-1
 * 	5、抛出异常返回-2
 */
function doFetchTask(taskId){
	$.post(contextPath +　"!fetchTask.action",
	      　{taskId:taskId},
	      　function(retCode){
	      		if(retCode == "-1"){
	      			alert("该任务不存在或已删除。");
	      		}else if(retCode == "0"){
	      			alert('该流程实例已经结束，无法取回。');	
	      		}else if(retCode == "1"){
	      			alert('该任务的后续任务已经被处理或正在处理，不能取回。');
	      		}else if(retCode == "2"){
	      			alert('任务已成功取回。');
	      			goBack();
	      			window.location.reload();
	      		}else if(retCode == "-2"){
	      			alert("该任务不能取回。");
	      		}	
	      }		
	);
}
/**
 * 查看流程图
 */
function workflowView(){      
  var width=screen.availWidth-10;;
  var height=screen.availHeight-30;
  var ReturnStr=OpenWindow(contextPath + "!PDImageView.action?instanceId="+$("#instanceId").val(), 
                               width, height, window);
}
/**
 * 查看办理记录
 */
function annal(){
 OpenWindow(contextPath + "!annallist.action?taskId="+$("#taskId").val(), 500, 300, window);
}