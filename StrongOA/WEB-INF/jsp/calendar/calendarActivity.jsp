<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!--  引用标签处-->
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<!--  引用公共文件,以下两个文件是建议包含-->
<%@include file="/common/include/rootPath.jsp" %>
<% 
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragrma","no-cache");
	response.setDateHeader("Expires",0);
%>
<HTML>
	<HEAD>
		<%@include file="/common/include/meta.jsp" %>
		<link rel="stylesheet" type="text/css" href="<%=frameroot%>/css/properties_windows_list.css"/>
			<LINK href="<%=frameroot%>/css/properties_windows_add.css" type=text/css rel=stylesheet>
		<script src="<%=path%>/oa/js/calendar/spinelz_lib/prototype.js" type="text/javascript"></script>
		<script language='javascript' src='<%=path%>/common/js/jquery/jquery-1.2.6.js'></script>
		<% 
		String ifLeader=request.getParameter("ifLeader");//是否领导日程
		%>
		<TITLE>日程查看——日视图</TITLE>
		<script type="text/javascript">
		   var scriptroot='<%=path%>';
		   
		   $(function(){
			  $("body").bind("selectstart",function(){return false;}); 
		   });
		   
		   function hideAdd(){
				var addWindow = window.parent.daycontent.addwindow;
				if("undefined"!=(typeof addWindow)){
					addWindow.style.display='none';
				}
			}
		   
			//跳转页面 查看某个活动分类的视图
			function viewOfType(){
				var str="";   
				 $("input[name='activityId']:checkbox").each(function(){ 
		                if($(this).attr("checked")){
		                    str += $(this).val()+",";
		                }
		            });
				 if(str!=""){
					 str =str.substring(0,str.length-1);
				 }
				 if(str==""||str==undefined){
						alert("请选择要活动的活动分类！");
						return;
					}
				 var flag;
				 if(str!=""&&str!=undefined){
					  flag = str.split(",");
					 if(flag.length>1){
						 alert("只能选择一项")
						 return false;
					 }
					}
				var activ = flag[0].split("#");
				var activityId = activ[0];
				var activityName = activ[1];
				if(activityId==null|activityId==null){
					return false;
				}
		//		var daycontent = window.parent.document.getElementById("daycontent");
		//		daycontent.src = "<%=path%>/calendar/calendar!viewpage.action?activityId="+activityId;
				
				var title = window.parent.document.getElementById("cal_page_title");
				var cal_page_title = $("#cal_page_title",window.parent.document);
				cal_page_title.text("日程安排 （"+ activityName+"）");
				//title.innerText ="日程安排 （"+ activityName+"）";
			
				
				parent.changeActivity(activityId);
			}
			
			//跳转页面活动分类的视图
			function viewOfTypeOf(activityId){
				$(':checkbox').not(
									$(event.srcElement).parent().parent()
												   .find(':checkbox')
												   .attr('checked','checked')
								   ).attr('checked','');
				parent.changeActivity(activityId);
			}
			
			
			//gotoAllType 查看所有分类的活动
			function gotoAllType(){
				var title = window.parent.document.getElementById("cal_page_title");
				title.innerText ="日程安排";
				parent.changeActivity("");
	//			var daycontent = window.parent.document.getElementById("daycontent");
	//			daycontent.src = "<%=path%>/calendar/calendar!viewpage.action?ifleader=<%=ifLeader%>";
			}
			
			//添加新活动分类
			function addActivity(){
				var url = "<%=path%>/calendar/calendarActivity!input.action";
				var a = window.showModalDialog(url,window,'dialogWidth:450px;dialogHeight:190px;help:no;status:no;scroll:no');
				if(undefined==a){
					return ;
				}
				if(a.indexOf("reload")==0){
					a = a.replace("reload","");
					var str = a.split(",");
					
					viewOfTypeOf(str[0]);
					document.location.reload();
				} 
			}
			//编辑新活动分类
			function editActivity(){
				var str="";   
				 $("input[name='activityId']:checkbox").each(function(){ 
		                if($(this).attr("checked")){
		                    str += $(this).val()+",";
		                }
		            });
				 if(str!=""){
					 str =str.substring(0,str.length-1);
				 }
				 var flag;
				 if(str!=""&&str!=undefined){
					  flag = str.split(",");
					 if(flag.length>1){
						 alert("只能选择一项")
						 return false;
					 }
					}
				 if(str==""||str==undefined){
						alert("请选择要编辑的活动分类！");
						return;
					}
				var activ = flag[0].split("#");
				var activityId = activ[0];
				var activityName = activ[1];
				if(activityName=="默认分类"){
					alert("不能编辑默认分类！");
					return;
				}	
				
				var url = "<%=path%>/calendar/calendarActivity!input.action?activityId="+activityId;
				var a = window.showModalDialog(url,window,'dialogWidth:450px;dialogHeight:190px;help:no;status:no;scroll:no');
				if(undefined==a){
					return ;
				}
				
				if(a.indexOf("reload")==0){
					a = a.replace("reload","");
					viewOfTypeOf(activityId);
					document.location.reload();
				} 
			}
			
			//删除新活动分类
			function del(){
				var str="";   
				 $("input[name='activityId']:checkbox").each(function(){ 
		                if($(this).attr("checked")){
		                    str += $(this).val()+",";
		                }
		            });
				if(str==""||str==undefined){
					alert("请选择要删除的活动分类。");
					return;
				}
				var flag = str.split(",");
				
				var sid ="";
				if(flag.length==2){
					var activ = flag[0].split("#");
					var activityId = activ[0];
					var activityName = activ[1];
					if(activityName!=="默认分类"){
						sid += activityId +",";
					}
					else{
						alert("不能删除默认分类。");
						return;
					}
				}
				else{
				for(var i=0;i<flag.length;i++){
					var activ = flag[i].split("#");
					var activityId = activ[0];
					var activityName = activ[1];
					if(activityName!=="默认分类"){
						sid += activityId +",";
					}
					else{
						alert("默认分类不能删除，将删除其他分类。");
						
					}
				}
				}
				if(confirm("如果该分类下存在活动，将一并删除。确定要删除吗？")){
					var myAjax = new Ajax.Request(
		                 '<%=path%>/calendar/calendarActivity!delete.action', // 请求的URL
		                {
		                    //参数
		                    parameters : 'activityId='+sid,
		                   
		                    // 使用GET方式发送HTTP请求
		                    method:  'post', 
		                    // 指定请求成功完成时需要执行的js方法
		                    onComplete: function(response){
			                    	var activityId = response.responseText||"no response text";
			                    	if(activityId!="no response text"){
			                    		//location = "<%=path%>/calendar/calendar.action?ifLeader=<%=ifLeader%>";
			                    		window.parent.location.reload();
			                    		//document.location.reload();
			                    	}else if(activityId.indexOf("false")>-1){
			                    		alert("不能删除默认分类。");
			                    	}else if(activityId=="false"){
			                    		alert("不能删除默认分类。");
			                    	}else{alert("不能删除分类。");}
		                    	}
		                }
		            )
				}
			};
			
		</script>

		 <style type="text/css">
		 body, table, tr, td,div{
		    margin:0px;
		}
		#cal,#activity{
			background-color:rgb(255, 255, 255);
		}
		 </style>
	</HEAD>
	<BODY class="contentbodymargin" style="background-color:#eeeff3">
		<script type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script> 
		<DIV >
			<table  width="100%" border="0" cellpadding="0"
					cellspacing="0">
					<tr>
						<td height="20"	style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,
								startColorStr=#ededed,endColorStr=#ffffff);" >
						&nbsp;
						<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
						<a href="javascript:gotoAllType();" title="点击查看所有分类下的活动"><strong>所有活动分类</strong></a>
						</td>
					</tr>
					<tr >
						<td  align="left" height="200">
						<div style="height: 200px;overflow: auto">
						<table>
							<s:iterator value="activityList" var="ids">
								<tr>
									<td><input type="checkbox" value="<s:property value="activityId"/>#<s:property value="activityName"/>" name="activityId"/></td>
									<td><span onclick="viewOfTypeOf('<s:property value="activityId"/>')" style="cursor: pointer;"><s:property value="activityName"/></span></td>
								</tr>
							</s:iterator>
						</table>
						</div>
							<table>
								<tr><td>
								<a  class="button" onclick="addActivity()"  onmouseover="this.style.cursor='hand'">新建</a>&nbsp;
								<a  class="button" onclick="editActivity()" onmouseover="this.style.cursor='hand'">编辑</a>&nbsp;
								</td></tr>	
								<tr><td>
								<a   class="button" onclick="del()" onmouseover="this.style.cursor='hand'">删除</a>&nbsp;
							<a  class="button" onclick="viewOfType()" onmouseover="this.style.cursor='hand'">活动</a>&nbsp;
								</td></tr>	
							</table>
						</td>
					</tr>
					<tr><td></td></tr>
			</table>
		</DIV>
	</BODY>
</HTML>
