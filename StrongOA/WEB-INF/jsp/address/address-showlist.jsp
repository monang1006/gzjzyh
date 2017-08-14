<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>工作流选择人员-发文通讯录</TITLE>
		<%@include file="/common/include/meta.jsp" %>
	<LINK href="<%=frameroot%>/css/properties_windows_list.css" type=text/css  rel=stylesheet>
		<LINK href="<%=frameroot%>/css/search.css" type=text/css  rel=stylesheet>
		<STYLE>
			#Mycontentborder {
				BORDER-RIGHT: #848284 1px solid;
				PADDING-RIGHT: 3px;
				BORDER-TOP: #848284 0px solid;
				PADDING-LEFT: 3px;
				BACKGROUND: white;
				PADDING-BOTTOM: 10px;
				OVERFLOW: no;
				BORDER-LEFT: #848284 1px solid;
				WIDTH: 100%;
				PADDING-TOP: 0px;
				BORDER-BOTTOM: #848284 1px solid;
				POSITION: absolute;
				HEIGHT: 100%
			}
		</STYLE>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/upload/jquery.blockUI.js" language="javascript"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<!--<script src="<%=path%>/common/js/common/search.js" type="text/javascript"></script>-->
		<script type="text/javascript">
			$(document).ready(function(){
				//搜索
				$("#img_sousuo").click(function(){
					$("form").submit();
				});
				var objSelect = parent.parent.window.document.getElementById("sel_person");
				var sel = ($(objSelect));
				$("input:checkbox").each(function(){
					$(this).click(function(){
						if($(this).attr("checked")){
							if($(this).attr("name")!=""){
								var td_text = $(this).parent().next().text();//name
								var userId = $(this).parent().next().attr("value");
								
								if(!hasSelect(sel,userId)){
									if(userId!=null&&''!=userId){
									parent.parent.addOpt(td_text,userId);}
<%--									sel.append("<option value="+userId+">"+td_text+"</option>");--%>

								}
							}else{
								addAllOpt(this);
								return ;
							}
						}
						if(!$(this).attr("checked")){//取消
							if($(this).attr("name")!="checkall"){
							    var td_text = $(this).parent().next().text();//name
							    var userId=$(this).parent().next().attr("value");
							    var opt = findSelectOpt(sel,userId);
							    if(opt!=null){
							    	objSelect.removeChild(opt);
							    }
							}else{
<%--								deleteAllOpt();--%>
								deleteAllChangeOpt(this)
								return;
							}
						}
					});
				});
			
				initCheck();
				
				//删除所有元素
				function deleteAllOpt(){
					sel.find("option").each(function(){
					 	sel.get(0).removeChild(this);
					});
				}
				//添加所有元素
				function addAllOpt(all){
					show("请稍后，加载数据中...");
					$.getJSON("<%=root%>/address/address!doSelectAllUser.action",
							  {groupId:$("#groupId").val(),type:$("#type").val(),"model.name":encodeURI($("#userName").val())},
							  function(users){
							  	if(users == "-1"){
							  		show("对不起，加载数据异常！");
							  	}else{
								  	$.each(users,function(i,user){
								  		if(!hasSelect(sel,user.userId)){
								  			parent.parent.addOpt(user.userName,user.userId);
										}
								  	});
							  	}
							  });
					hidden();
				}
				
				//点击两次全选后，删除全选项 元素
				function deleteAllChangeOpt(all){
					show("请稍后，加载数据中...");
					$.getJSON("<%=root%>/address/address!doSelectAllUser.action",
							  {groupId:$("#groupId").val(),type:$("#type").val(),"model.name":encodeURI($("#userName").val())},
							  function(users){
							  	if(users == "-1"){
							  		show("对不起，加载数据异常！");
							  	}else{
								  	$.each(users,function(i,user){
										 var opt = findSelectOpt(sel,user.userId);
										    if(opt!=null){
										    	objSelect.removeChild(opt);
										    }
										
								  	});
							  	}
							  });
					hidden();
				}
				
			});
			function show(i){
				$.blockUI({ overlayCSS: { backgroundColor: '#B3B3B3'}});
				$.blockUI({ message: '<span class="wz">'+i+'</span>',css:{width:'160px',height:'25px'}});
				
			}
			function hidden(){
				$.unblockUI();
			}
			//父窗口调用全选项
		    function allSelect(sel_person){
		        $("input:checkbox").each(function(){
		            if($(this).attr("name")!="checkall"){
						var td_text = $(this).parent().next().text();//name						
						var userId = $(this).parent().next().attr("value");					
						if(!hasSelect(sel_person,userId)){
							
							parent.parent.addOpt(td_text,userId);
<%--							sel_person.append("<option value="+userId+">"+td_text+"</option>");--%>
						}
					}
				});
			 }
			 
			 //取消选择项
			 function removeSelected(objSelectValue){
				 var typewei="${typewei}";
				 var sel = parent.parent.window.document.getElementById("sel_person");
				if(typewei=='weipai'){
					$("input:radio").each(function(){
						var userId = $(this).parent().next().attr("value");	//获取用户ID
						if(userId==objSelectValue){
							 $(this).attr("checked",false);
							 $("td.td1").css("background-color","");
						}
	   
					});
						if(sel.options.length<1){
							$("input:radio").attr("checked",false);
							$("td.td1").css("background-color","");
						}
				}else{

				 	$("input:checkbox").each(function(){
						var userId = $(this).parent().next().attr("value");	//获取用户ID
						if(userId==objSelectValue){
							 $(this).attr("checked",false);
						}
	   
					});
						if(sel.options.length<=1){
							$("input:checkbox").attr("checked",false);
						}
				}
				
			 }
			 
			//父窗口调用删除所有元素
			function deleteAllSelect(sel_person){
				sel_person.find("option").each(function(){
				 	sel_person.get(0).removeChild(this);
				});
			}
			//判断是否已经选择
			function hasSelect(sel,userId){
				var isExist = false;
				sel.find("option").each(function(){
					if(this.value == userId){
						isExist = true;
					}
				});
				return isExist;
			}
			//获取选中的元素
			function findSelectOpt(sel,userId){
				var opt = null;
				sel.find("option").each(function(){
					if(this.value == userId){
						opt = this;
					}
				});
				return opt;
			}
			//初始化
			function initCheck(){
				var sel = parent.parent.window.document.getElementById("sel_person");
				var typewei="${typewei}";
				if(typewei=='weipai'){
					$("input:radio").each(function(index){
						var name = $(this).parent().next().text();
						var userId = $(this).parent().next().attr("value");
						if (sel.length != 0) {
			                   if (userId == sel.options[0].value) {
			                          $(this).attr("checked",true);
			                          $("#tr"+(index)+">td").css("background-color","#A9B2CA");
			                      }
			                 
			             }    
					});
				}else{
					$("input:checkbox").each(function(index){
						var name = $(this).parent().next().text();
						var userId = $(this).parent().next().attr("value");
						if (sel.length != 0) {
			                 for(var j=0; j<sel.options.length; j++){
			                      if (userId == sel.options[j].value) {
			                          $(this).attr("checked",true);
			                          $("#tr"+(index-1)+">td").css("background-color","#A9B2CA");
			                      }
			                 }
			             }    
					});
				}
				
			}
			//全选
			function checkAll(chkAll){//this,document.getElementById('myTable_td'),'#A9B2CA',true
				var checked = chkAll.checked;
				$("input:checkbox").attr("checked",checked);
				if(checked){
					$(".td1").css("background-color","#A9B2CA");
					parent.parent.allSelect();
					var sel = parent.parent.window.document.getElementById("sel_person");
					//sel.removeChild(sel.options[sel.options.length-3]);
				}else{
					$(".td1").css("background-color","");
					parent.parent.removeSelect();
				}
			}
			//单击某行时修改其背景颜色
			function changeColor(currentTr){
				var objSelect = parent.parent.window.document.getElementById("sel_person");
				var sel_person = ($(objSelect));
				var chk = $("#"+currentTr+" td:first-child>input").attr("checked");
				var tagName = event.srcElement.tagName;//事件源,因为checkbox也在此tr中
				var td_text = $("#"+currentTr+" td:first-child").next().text();
				var userId = $("#"+currentTr+" td:first-child").next().attr("value");
				if(chk){
					if(tagName!="INPUT"){//如果单击的是tr中的某个TD，不是checkbox
						$("#"+currentTr+" td:first-child>input").attr("checked",false);
						$("#"+currentTr+">td").css("background-color","");
						var opt = findSelectOpt(sel_person,userId);
					    if(opt!=null){
					    	objSelect.removeChild(opt);
					    }
					}else{
						$("#"+currentTr+">td").css("background-color","#A9B2CA");
					}	
				}else{
					if(tagName!="INPUT"){
						$("#"+currentTr+" td:first-child>input").attr("checked",true);
						$("#"+currentTr+">td").css("background-color","#A9B2CA");
						if(!hasSelect(sel_person,userId)){
							parent.parent.addOpt(td_text,userId);
<%--							sel_person.append("<option value="+userId+">"+td_text+"</option>");--%>
						}
					}else{
						$("#"+currentTr+">td").css("background-color","");
					}	
					
				}
			}
			//单击某行时修改其背景颜色
			function changeColor1(currentTr){
				var objSelect = parent.parent.window.document.getElementById("sel_person");
				var sel_person = ($(objSelect));
				var chk = $("#"+currentTr+" td:first-child>input").attr("checked");
				var tagName = event.srcElement.tagName;//事件源,因为checkbox也在此tr中
				var td_text = $("#"+currentTr+" td:first-child").next().text();
				var userId = $("#"+currentTr+" td:first-child").next().attr("value");
				
				if(chk){
					if(tagName!="INPUT"){//如果单击的是tr中的某个TD，不是checkbox
						if(objSelect.options[0]!=null&&''!=objSelect.options[0]){
							objSelect.removeChild(objSelect.options[0]); }
					    parent.parent.addOpt(td_text,userId);
						$("td:not('#'"+currentTr+">td)").css("background-color","");
						$("#"+currentTr+">td").css("background-color","#A9B2CA");
					}else{
						if(objSelect.options[0]!=null&&''!=objSelect.options[0]){
						objSelect.removeChild(objSelect.options[0]); }
					    parent.parent.addOpt(td_text,userId);
						$("td:not('#'"+currentTr+">td)").css("background-color","");
						$("#"+currentTr+">td").css("background-color","#A9B2CA");
					}	
				}else{
					if(tagName!="INPUT"){
						$("#"+currentTr+" td:first-child>input").attr("checked",true);
						$("td:not('#'"+currentTr+">td)").css("background-color","");
						$("#"+currentTr+">td").css("background-color","#A9B2CA");
						if(!hasSelect(sel_person,userId)){
							
							if(objSelect.options[0]!=null&&''!=objSelect.options[0]){
								objSelect.removeChild(objSelect.options[0]); }
						    
							parent.parent.addOpt(td_text,userId);
<%--							sel_person.append("<option value="+userId+">"+td_text+"</option>");--%>
						}
					}else{
						
						$("#"+currentTr+">td").css("background-color","");
					}	
					
				}
			}
			//页面跳转
			function gotoPage(no){
				if(no!=null&&no!="")
					document.getElementById('pageNo').value=no;
					document.getElementById('myTableForm').submit();
				}
		</script>
	</HEAD>
	<BODY class=contentbodymargin oncontextmenu="return false;">
		<DIV id=Mycontentborder align=center style="overflow-x: hidden;overflow-y: auto;"  >
	    	<s:form id="myTableForm" action="/address/address!showlist.action">
	    	<s:hidden name="groupName" id="groupName"></s:hidden>
	    	<s:hidden id="pageNo" name="pageUser.pageNo"></s:hidden>
	    	<s:hidden id="type" name="type"></s:hidden>
			<s:hidden id="groupId" name="groupId"></s:hidden>
			<s:hidden id="typewei" name="typewei"></s:hidden>
			<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table1_search">
			 <tr>
			     <td>
		          <div style="float:left;width:180px;"> 姓名：&nbsp;<input name="name" id="userName"  maxlength="50" size="16" type="text" style="background-color:#ffffff" class="search" title="请您输入姓名" value="${model.name }"></div>
		          <div style="float:left; padding-top:3px; padding-left:3px;width:80px;"> <input id="img_sousuo" type="button" /></div>
				 </td>
	         </tr>
			 </table>
	  		<table width="100%" border="0" cellspacing="0" cellpadding="00">
	  			<tr>
	    			<td height="100%">
	    				<table width="100%" border="0" cellpadding="0" cellspacing="1" class="table1">
							<thead>
								<tr>
									<th width="3%" height="22" class="biao_bg2">
									<s:if test="typewei=='weipai'">
									 &nbsp;
									</s:if>
									<s:else>
										<input id="checkall" type="checkbox" name="checkall" onClick="checkAll(this);" > 
									</s:else>
									</th>
									<th align="center" class='biao_bg2'>姓名</th>
								</tr>
							</thead>
							<tbody>
							<s:if test="typewei=='weipai'">
							   <s:iterator value="pageUser.result" status="index">
									 <tr id="tr<s:property value='#index.index'/>" onClick="changeColor1(this.id);">
										<td width="3%" id="chkButtonTd" class="td1" style="text-indent: 0px;" align="center">
											<input type="radio" name="radButton">
										</td>
										<td class="td1" value="<s:property value="pageUser.result[#index.index][0]" />"><s:property value="pageUser.result[#index.index][1]" /></td>
									</tr>
										</s:iterator>
							</s:if>
							<s:else>
								<s:iterator value="pageUser.result" status="index">
									 <tr id="tr<s:property value='#index.index'/>" onClick="changeColor(this.id);">
										<td width="3%" id="chkButtonTd" class="td1" style="text-indent: 0px;" align="center">
											<input type="checkbox" name="chkButton">
										</td>
										<td class="td1" value="<s:property value="pageUser.result[#index.index][0]" />"><s:property value="pageUser.result[#index.index][1]" /></td>
									</tr>
								</s:iterator>
								</s:else>
								<s:if test="pageUser.totalCount>0">
									<tr class="biao_bg2">
										<td colspan="7">
											&nbsp;&nbsp;
											<!-- <input type="checkbox" name="checkall" onclick="checkAll(this);">全选&nbsp;&nbsp;-->
											<s:if test="pageUser.totalCount==-1">
												<s:set value="1" name="totalpages" />
											</s:if>
											<s:else>
												<s:set value="pageUser.totalCount/pageUser.pageSize"
													name="totalpages" />
												<s:if test="pageUser.totalCount%pageUser.pageSize>0">
													<s:set value="pageUser.totalCount/pageUser.pageSize+1"
														name="totalpages" />
												</s:if>
											</s:else>
											当前&nbsp;&nbsp;
											${pageUser.pageNo }/<s:if test="#totalpages==0">1</s:if><s:else><s:property value="#totalpages"/></s:else>
											&nbsp;&nbsp;
												共<b><s:property value="pageUser.totalCount"/></b>条&nbsp;&nbsp;
											<s:if test="pageUser.pageNo>1">
													<img style="cursor: hand;" src="<%=root%>/images/ico/first.gif" onClick="gotoPage(1)"/>
													<img style="cursor: hand;" src="<%=root%>/images/ico/prev.gif" onClick="gotoPage(${pageUser.pageNo-1})"/>
												</s:if>
												<s:if test="pageUser.pageNo<=#totalpages-1">
													<img style="cursor: hand;" src="<%=root%>/images/ico/next.gif" onClick="gotoPage(${pageUser.pageNo+1})"/>
													<img style="cursor: hand;" src="<%=root%>/images/ico/last.gif" onClick="gotoPage(<s:property value="#totalpages"/>)"/>
												</s:if>
										</td>
									</tr>	
								</s:if>
							</tbody>
						</table>
	      			</td>
	  			</tr>
			</table>
   			</s:form>
		</DIV>
	</BODY>
</HTML>
