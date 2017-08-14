<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!--  引用标签处-->
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<!--  引用公共文件,以下两个文件是建议包含-->
<%@include file="/common/include/rootPath.jsp" %>
<% 
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragrma","no-cache");
	response.setDateHeader("Expires",0);
%>
<html>
	<head>
	<%@include file="/common/include/meta.jsp" %>
	<!--  引用公共样式文件,建议所有样式都以文件方式定义在jsp文件外部,通常定义在WebRoot目录下的CSS文件夹下-->
	<LINK href="<%=frameroot%>/css/properties_windows_add.css" type=text/css rel=stylesheet>
	<!--  引用公共及自定义js文件,建议js都以文件方式定义在jsp文件外部,通常定义在WebRoot目录下的js文件夹下-->
	<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
	<script src="<%=path%>/common/js/validate/jquery.validate.js" type="text/javascript"></script>
	<script src="<%=path%>/common/js/validate/formvalidate.js" type="text/javascript"></script>
	<script src="<%=path%>/common/js/upload/jquery.MultiFile.js" type="text/javascript"></script>
	<script src="<%=path%>/common/js/upload/jquery.blockUI.js" language="javascript"></script>
	<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>	
		<title>保存通知公告</title>
		
		<style type="text/css">
		body{
			width:100%;
			margin : 0px;
			height: 100%
		}
		#inHtml{
			CURSOR: default; 
			width: 300px;
			height: 100px;
		}
	
		</style>
		<script language="javascript">
		var recvuserId = "${msgReceiverIds}";
		$(document).ready(function(){
		if(notifyForm.affReceiverName.value.trim().length==0){
		//当为新建时则默认勾选所有人
		$("#setAllUser").attr("checked","true");
		$("#orguserid").val("alluser");
		$("#orgusername").val("所有人");
		
		$("#addUser").attr("disabled","disabled");
		$("#clearUser").attr("disabled","disabled");
		}
		 //选中所有人
		 $("#setAllUser").click(function(){
			if($("#setAllUser").attr("checked")){
				if(confirm("您确定发给所有人吗？")==true){
					$("#orguserid").val("alluser");
					$("#orgusername").val("所有人");
					$("#addUser").attr("disabled","disabled");
					$("#clearUser").attr("disabled","disabled");
				}else{
					$("#addUser").attr("disabled","");
					$("#clearUser").attr("disabled","");
					$("#setAllUser").attr("checked",false);
				}
			}else{
				$("#orguserid").val("");
				$("#orgusername").val("");
				$("#addUser").attr("disabled","");
				$("#clearUser").attr("disabled","");
			}
		 });
		});
		String.prototype.trim = function() {
                var strTrim = this.replace(/(^\s*)|(\s*$)/g, "");
                strTrim = strTrim.replace(/^[\s　\t]+|[\s　\t]+$/, ""); 
                return strTrim;
            }
		
		function validateTitle(){
			var title = notifyForm.afficheTitle.value;
			if(title.trim().length==0){
		 	 		alert("请填写标题！");
                    notifyForm.afficheTitle.focus();
                    return false;
		 	}else{
				 title = title.replace(new RegExp("\"","gm"),"“");
				 title = title.replace(/[\']/gm,"’");
				 title = title.replace(new RegExp("\n","gm")," ");
				 title = title.replace(new RegExp("\r","gm"),"");
				 title = title.replace(new RegExp("<","gm"),"＜");
				 title = title.replace(new RegExp(">","gm"),"＞");
				 title = title.replace(/[\\]/gm, "＼");
				 
				 notifyForm.afficheTitle.value = title;
				 
		 	return true;
		 	}
		}
		
		
		 
		//转换时间格式(yyyy-MM-dd)--->(yyyyMMdd)
        function date2string(stime){
         	var arrsDate1=stime.split('-');
         	stime=arrsDate1[0]+""+arrsDate1[1]+""+arrsDate1[2];
         	//var arrsDate2=stime.split(' ');
         	//stime=arrsDate2[0]+""+arrsDate2[1];
         	//var arrsDate3=stime.split(':');
         	//stime=arrsDate3[0]+""+arrsDate3[1]+""+arrsDate3[2];
         	return stime;
         }
		
		function CheckSend(){
		 	saveFrom("1");
		 }
		 function CheckSave(){
			saveFrom("0");
		 }
		 
		 function saveFrom(inputType){
		 	
		 	if(!validateTitle()){
		 		return;
		 	}
			var notifyForm = document.getElementById("notifyForm");
		 	document.getElementById("inputType").value = inputType;
		 	if(!getNotifyContent(notifyForm)){
		 		alert("内容不能为空。");
			 	return false;
		 	}
		 	
		 	//时间验证
			var stime = $("#afficheTime").val();
			var etime = $("#afficheUsefulLife").val();
			if(stime==null|stime==""|etime==null|etime==""){
				alert("请输入公告的有效期。");
				return;
			}else{
				if(date2string(stime)>=date2string(etime)){
					alert("失效日期要在生效日期之后。");
					return;
				}
			}
		 	
		 	if($("#public2IPP").attr("checked")==true){
		 		if($("#lanmuId").val()==null){
		 			alert("请选择要发布到的栏目。");
		 			return;
		 		}
		 	}
		 	
			var submit = document.getElementById("submit");
		 	submit.click();
		 }
		 $(document).ready(function() {
		 	$("#titlecolor").val("${model.afficheTitleColour}");
			var message = $(".actionMessage").text();
			if(message!=null && message!=""){
				if(message.indexOf("error")>-1){
					message = message.replace("error","");
					alert(message);
				}else{
					//alert("新闻公告的ID ："+"${model.afficheId}");
					//alert("是否发布："+"${model.afficheState}");
					if("${model.afficheState}"=="0"){	//保存公告
						window.returnValue = "reload";
						window.close();
						return;
					}
					//选择是否发布到外网
					if(""!='${lanmuId}'){
						sendtoIpp('${lanmuId}');
					}else{
						window.returnValue = "reload";
						window.close();
					}
				}
			}
			
			 //选择是否是粗体
			 $("#afficheTitleBold").change(function(){
			 	
			 	if($(this).val()=="1"){
				 	$("#bold").html("<B>选择标题是否以粗体显示</B>");
			 	}else{
				 	$("#bold").html("选择标题是否以粗体显示");
			 	}
			 	
			 });
	
			//初始化标题颜色
			selcolor();
		 });
		 
		 //获取栏目信息 （勾选框方式）
		 function selectlanmu(){
		 	if($("#public2IPP").attr("checked")==true){
		 		$.ajax({
						type:"post",
						url:"<%=path%>/notify/notify!getLanmuList.action",
						data:'',
						success:function(data){
								if(data!="" && data!=null){
									try{
									var JSONobj = eval('('+data+')');
									var inHtml ="<select id=\"lanmuId\" name=\"lanmuId\" >";
									for(var i=0;i<JSONobj.length;i++){
										inHtml += "<option value=\""+JSONobj[i][0]+"\">"+JSONobj[i][1]+"</option>"
									}
									inHtml += "</select>";
									
									$("#lanmuInfo").html(inHtml);			
									}catch(e){
										alert("获取栏目信息出错。");
										$("#public2IPP").attr("checked",false)
									}
								}else{
									 alert("返回数据为空。");
									 $("#public2IPP").attr("checked",false)
								}
							},
						error:function(data){
							alert("对不起，获取栏目信息出错。"+data);
							$("#public2IPP").attr("checked",false)
						}
				   });
 			 	
		 	}else{
			 	$("#lanmuInfo").html("");
			 	$("#lanmuId").val("");
		 	}
		 	

		 }
		 
		 //获取栏目信息 （blockUI方式）
		 function getLanmuInfo(){
			$.blockUI({ message: "<font color='#008000'><b>正在获取栏目信息...</b><div></font>" });
				$.ajax({
						type:"post",
						url:"<%=path%>/notify/notify!getLanmuList.action",
						data:'',
						success:function(data){
								if(data!="" && data!=null){
									try{
									var JSONobj = eval('('+data+')');
									var inHtml ="";
									for(var i=0;i<JSONobj.length;i++){
										inHtml += "<option value=\""+JSONobj[i][0]+"\">"+JSONobj[i][1]+"</option>"
									}
									
									$.blockUI({ overlayCSS: { backgroundColor: '#B3B3B3' }});
									$.blockUI({ message: "<div id=inHtml ><font color='#008000'><b>请选择要发布到的栏目</b><div></font><br><div>栏目：<select id=\"lanmuId\" >"
												+inHtml
												+"</select></div><br><div><input class=\"input_bg\" type=button value='确定' onclick=sendtoIpp($('#lanmuId').val());>&nbsp;&nbsp;<input class=\"input_bg\" type=button value='取消' onclick='window.returnValue = \"reload\";window.close();'></div></div>" });
												
									}catch(e){
										$.blockUI({ message: "<div id=inHtml ><font color='#008000'><b>获取栏目信息出错</b><div></font><br>"+
																"<input class=\"input_bg\" type=button value='重试' onclick=getLanmuInfo();>&nbsp;&nbsp;"+
																"<input class=\"input_bg\" type=button value='取消' onclick='window.returnValue = \"reload\";window.close();'></div>" });
									}
								}else{
									 alert("返回数据为空。");
									 window.returnValue = "reload";
									 window.close();
								}
							},
						error:function(data){
							blockUI:$.unblockUI();
							alert("对不起，获取栏目信息出错。"+data);
							window.returnValue = "reload";
							window.close();
						}
				   });
		 }
		 
		 //发布到IPP
		 function sendtoIpp(id){
			var afficheId = "${model.afficheId}";
		 	$.blockUI({ message: "<div id=inHtml ><font color='#008000'><b>正在发布到外网...</b><div></font></div>" });
		 	$.ajax({
					type:"post",
					url:"<%=path%>/notify/notify!SendToIpp.action",
					data:'afficheId='+afficheId+'&lanmuId='+id,
					success:function(data){
							if(data!="" && data!=null){
								if("success"==data){
									$.blockUI({ message: "<div id=inHtml ><font color='#008000'><b>发布成功</b><div></font><br><input class=\"input_bg\" type=button value='确定' onclick='window.returnValue = \"reload\";window.close();'></div>" });
								}else{
									$.blockUI({ message: "<div id=inHtml ><font color='#008000'><b>发布失败</b><div></font><br><input class=\"input_bg\" type=button value='确定' onclick='window.returnValue = \"reload\";window.close();'></div>" });
								}
								//$.unblockUI();
							}else{
								$.blockUI({ message: "<div id=inHtml ><font color='#008000'><b>返回数据为空</b><div></font></div>" });
								 window.returnValue = "reload";
								 window.close();
							}
						},
					error:function(data){
						$.blockUI({ message: "<div id=inHtml ><font color='#008000'><b>返回数据为空</b><div></font></div>" });
						alert("对不起，获取公告发布出错。"+data);
						window.returnValue = "reload";
					    window.close();
					}
			   });
		 }
		 
		 //选择颜色
		 function selcolor(){
		 	var colortext = document.getElementById("colortext");
		 	var titlecolor = document.getElementById("titlecolor");
		 	var afficheTitleColour = document.getElementById("afficheTitleColour");
		 	colortext.color=(titlecolor.value);
		 	afficheTitleColour.value = titlecolor.value;
		 }
		 //选择是否置顶
		 function seltop(){
		 	var titletop = document.getElementById("titletop");
		 	var afficheTop = document.getElementById("afficheTop");
		 	afficheTop.value = titletop.value;
		 }
		 //获取通知内容
         function getNotifyContent(form) {
             var oEditor = FCKeditorAPI.GetInstance('content');
             var acontent = oEditor.GetXHTML();
             if(acontent.trim()==""|acontent.trim()==null){
             	return false;
             }else{
				 form.afficheDesc.value = acontent;
				 return true;
             }
         }
		 function sd_operate(dp){
		 	alert(dp.cal.getDateStr());
		 }
		 //页面取消返回
		 function backToList(){
			//var inputType = document.getElementById("inputType");
			//location = "<%=path%>/notify/notify!mylist.action?inputType="+inputType.value;
			returnValue = "close";
			window.close();
		 }
		 
		 //删除附件
		 function delAttach(id){
		 	if(id!=null&&id!=""){
			 	var delattIds = $("#delAttIds").val();
			 	delattIds += id+",";
			 	var divId = "att"+id;
			 	$("#"+divId).hide();
			 	
			 	$("#delAttIds").val(delattIds);
		 	}
			/* 
			 	var notifyForm = document.getElementById("notifyForm");
			 	var attachId = document.getElementById("attachId");
			 	attachId.value=id;
			 	notifyForm.action = "<%=path%>/notify/notify!delAttach.action";
			 	var inputType = document.getElementById("inputType");
			 	inputType.value="1";
			 	getNotifyContent(notifyForm);
			 	var submit = document.getElementById("submit");
			 	submit.click();
			 */
		 }
		 
		 //失效通知
		 function setUsefulLife(){
			 alert("失效后此公告将过期。");
		 	var afficheUsefulLife = document.getElementById("afficheUsefulLife");
		 	var afficheTime = document.getElementById("afficheTime");
		 	//var nowdate = new Date();
		 	//afficheTime.value = nowdate.getYear()+"-"+(nowdate.getMonth()+1)+"-"+(nowdate.getDate()-2);
		 	//afficheUsefulLife.value = nowdate.getYear()+"-"+(nowdate.getMonth()+1)+"-"+(nowdate.getDate()-1);
		 	$.ajax({
						type:"post",
						url:"<%=path%>/notify/notify!orderDate.action",
						success:function(data){	
							if(data!=null){
							//返回的值 data包含当前日期的前一天和当前日期的前两天
							afficheTime.value=data.split(";")[1];
							afficheUsefulLife.value=data.split(";")[0];
						
							}
						},
						error:function(data){
							alert("对不起，操作异常。"+data);
						}
					});
		 }
		 //清空联系人
		 function clickClear(){
		 if($("#setAllUser").attr("checked")){
				$("#setAllUser").attr("checked",false);
			}
			$("#orgusername").val("");
			$("#orguserid").val("");
		 }
		 //添加人员
		 function addPerson(){
		 var url="<%=root%>/address/addressOrg!tree.action?isShowAllUser=1";
		 if($("#setAllUser").attr("checked")){
				alert("您已选择所有人，如需重新选择，请先取消选择所有人。");
				return false;
			}
			var ret=OpenWindow(url,"600","400",window);
		 }
		 
		/*
		jQuery控制生效日期不可以为过去的日期，需要高版本的jQuery，暂时屏蔽
		
		 jQuery(function(){
        	jQuery.noConflict();
            var startEnd =  function(str){
	        	var $Date = jQuery(str);
	        	$Date.each(function(i){
	        		var that = this;
	        		jQuery(this)[0].onclick = null;
	        		if(i == 0)
		        		jQuery(this).bind('click',function(){
						        					WdatePicker({
							        					dateFmt:'yyyy-MM-dd',
							        					skin:'whyGreen',
							        					maxDate:$Date.not(that).val()
						        					});
		        							 	 });
		 	 		else
		 	 			jQuery(this).bind('click',function(){
						        					WdatePicker({
							        					dateFmt:'yyyy-MM-dd',
							        					skin:'whyGreen',
							        					minDate:$Date.not(that).val()
						        					});
		        							 	 });
	        	});
        	};
        	startEnd('#afficheTime,#afficheUsefulLife');
        });
		 */
		</script>
	</head>
	<base target="_self"/>
	<body>
	<DIV id=contentborder align=center>
	<s:form action="/notify/notify!save.action" name="notifyForm" id="notifyForm" method="post" enctype="multipart/form-data">
	<input type="submit" id="submit" name="submit" value="" style="display: none"/>
	<input type="hidden" id="afficheId" name="afficheId" value="${model.afficheId}"/>
	<input type="hidden" id="attachId" name="attachId" value="${attachId}"/>
	<input type="hidden" id="delAttIds" name="delAttIds" value="${delAttIds}"/>
	<input type="hidden" id="inputType" name="inputType" value="${inputType}"/>
	<input type="hidden" id="afficheTitleColour" name="model.afficheTitleColour" value="${model.afficheTitleColour}"/>
	<input type="hidden" id="afficheTop" name="model.afficheTop" value="${model.afficheTop}"/>
<%--<input type="hidden"  id="afficheDesc" name="model.afficheDesc" value='${model.afficheDesc}'/>--%>
	<textarea   id="afficheDesc" name="model.afficheDesc" style="display:none">${model.afficheDesc}</textarea>
	<textarea   id="desc"  style="display:none">${model.afficheDesc}</textarea>
<%--<div id="desc" style="display: none">${model.afficheDesc}</div>--%>
	<label id="l_actionMessage" style="display:none;"><s:actionmessage/></label>
<!--<script type="text/javascript" src="<%=path %>/common/js/newdate/WdatePicker.js"></script> -->
	<script type="text/javascript" src="<%=path%>/common/script/My97DatePicker/WdatePicker.js"></script>
		
			<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
				<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
								<script>
											var id = "${model.afficheId}";
											if(id==null|id==""){
												window.document.write("<strong>新建通知公告</strong>");
											}else{
												window.document.write("<strong>编辑通知公告</strong>");
											}
											</script>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
					                <tr>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onclick="CheckSend();">&nbsp;发&nbsp;布&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onclick="CheckSave();">&nbsp;保&nbsp;存&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
					                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
					                 	<td class="Operation_input1" onclick="backToList();">&nbsp;取&nbsp;消&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
				                  		<td width="6"></td>
					                </tr>
					            </table>
							</td>
						</tr>
					</table>
				</td>
				</tr>
				<tr>
					<td>
					<table  border="0" cellpadding="0" cellspacing="0" class="table1" width="100%" >
						<tr>
							<td class="biao_bg1" align="right">
								<span class="wz"><font color=red>*</font>&nbsp;标题：&nbsp;</span>
							</td>
							<td class="td1" style="padding-left:5px;">
								&nbsp;<input id="afficheTitle" name="model.afficheTitle" type="text"  value="${model.afficheTitle}"
											 size="95" maxlength="65">
											 
								&nbsp;
								<%--  IPP 发布调试代码
									<input type="checkbox" id="public2IPP" onclick="selectlanmu()">发布到IPP
									<span id="lanmuInfo"></span>
								--%>
							</td>
						</tr>
						<tr>
						<td class="biao_bg1" align="right" valign="top" >
							<span class="wz">发布范围：&nbsp;</span>
						</td>
						<td colspan="3" class="td1">&nbsp;
							<s:textarea title="双击选择收件人"  cols="90%" id="orgusername" name="affReceiverName" ondblclick="addPerson();"  rows="4" readonly="true" ></s:textarea>
							<input  type="hidden" size="65" id="orguserid" name="affReceiverId" value="${affReceiverId}" ></input>
							<table >
								<tr>
									<td align="right" width="100%" style="padding-left: 5px;">
										&nbsp;&nbsp;<input type="checkbox" name="setAllUser" id="setAllUser" value="1">所有人&nbsp;
										<a id="addPerson"  href="#" class="button" onclick="addPerson(this.url)">添加</a>&nbsp;
										<a id="clearPerson" href="#" class="button" onclick="clickClear()">清空</a>
									</td>
								</tr>
								<tr></tr>
								<tr></tr>
							</table>
						</td>
					</tr>
					<tr>
							<td nowrap class="biao_bg1" align="right">
								<span class="wz">是否登入时弹出：&nbsp;</span>
							</td>
							<td class="td1" >
								&nbsp;
								<s:select list="#{'0':'否','1':'是'}"  id="viewAfterLogin" name="model.viewAfterLogin" cssClass="s_select"/>
								
								<span class="wz" ><font id="boldtext"  color="gray">
											选择公告是否在登入时自动弹出
										</font></span>
							</td>
					</tr>
						<tr>
							<td nowrap class="biao_bg1" align="right">
								<span class="wz">标题颜色：&nbsp;</span>
							</td>
							<td class="td1">
								&nbsp;
								<select name="title_color" onchange="selcolor();"  id="titlecolor" class="s_select">
											<option value="black">
												<font color="black">黑色</font>
											</option>
											<option value="red" >
												<font color="red">红色</font>
											</option>
											<option value="green">
												<font color="green">绿色</font>
											</option>
											<option value="yellow">
												<font color="yellow">黄色</font>
											</option>
											<option value="orange">
												<font color="orange">橙色</font>
											</option>
											<option value="blue">
												蓝色
											</option>
											<option value="purple">
												紫色
											</option>
									   </select>
									   <span class="wz"><font id=colortext color="black" >
											选择标题的显示颜色
										</font></span>
							</td>
						</tr>
						<tr>
							<td nowrap class="biao_bg1" align="right">
								<span class="wz">是否为粗体：&nbsp;</span>
							</td>
							<td class="td1" >
								&nbsp;
								<s:select list="#{'0':'否','1':'是'}"  id="afficheTitleBold" name="model.afficheTitleBold" cssClass="s_select"/>
								
								<span class="wz" ><font id="bold"  color="gray">
											选择标题是否以粗体显示
										</font></span>
							</td>
						</tr>
						<%--<tr>
							<td nowrap class="biao_bg1" align="right">
								<span class="wz">置顶：</span>
							</td>
							<td class="td1">
								&nbsp;<select name="titletop" onchange="seltop();">
									<option value="0">
										否
									</option>
									<option value="1">
										是
									</option>
								</select>
								<span class="wz"><font id=titletop >
											选择是否使通知公告置顶
										</font></span>
							</td>
						</tr>
						
						--%><tr>
							<td nowrap class="biao_bg1" align="right">
								<span class="wz"><font color=red>*</font>&nbsp;有效期：&nbsp;</span>
							</td>
							<td class="td1">
								&nbsp;<span class="wz">生效日期:</span>
								<strong:newdate name="model.afficheTime" id="afficheTime" dateform="yyyy-MM-dd"  dateobj="${model.afficheTime}"
									width="150px" skin="whyGreen" isicon="true" title=" 为空为立即生效"></strong:newdate>
							<br>&nbsp;<span class="wz">失效日期:</span>
								<strong:newdate name="model.afficheUsefulLife" id="afficheUsefulLife" dateform="yyyy-MM-dd" dateobj="${model.afficheUsefulLife}"
									width="150px" skin="whyGreen" isicon="true" title=" 为空为手动失效 "></strong:newdate>
								<s:if  test="model.afficheId!=null&&model.afficheId!=''&&model.afficheState!=2">
								<a id="clearPerson" href="#" class="button"  title="点击则即刻失效"  onclick="setUsefulLife();">失效</a>
								</s:if>
							</td>
						</tr>
						<tr>
							<td nowrap class="biao_bg1" align="right">
								<span class="wz">附件：&nbsp;</span>
							</td>
							<td class="td1" title="每次上传附件总大小不能超过${defAttSize/1024/1024 }M">
								<div style="margin-left: 8px;">			
								<span class="wz" ><font color="gray">每次上传附件大小不能超过${defAttSize/1024/1024 }M</font></span>													
									<input type="file" style="width: 36%;" onkeydown="return false;" class="multi" name="file"/>
									${attachFiles}
								</div>
							</td>
						</tr>
						<tr>
							<td class="biao_bg1" align="right"  valign="top">
								<span class="wz"><font color=red>*</font>&nbsp;内容：&nbsp;</span>
							</td>
							<td align="left" style="margin-left: 5px;">
 		 						<script type="text/javascript" src="<%=path%>/common/js/fckeditor2/fckeditor.js"></script>
								<script type="text/javascript">
									var oFCKeditor = new FCKeditor( 'content' );
									oFCKeditor.BasePath	= '<%=path%>/common/js/fckeditor2/'
									oFCKeditor.ToolbarSet = "FutureDefaultSet" ;
									oFCKeditor.Width = '100%' ;
                                    oFCKeditor.Height = '460' ;
									oFCKeditor.Value = document.getElementById("desc").innerText;
									oFCKeditor.Create() ;
								</script>                              
     						</td>
						</tr>
						<tr>
							<td class="table1_td"></td>
							<td></td>
						</tr>
					</table>
					</td>
				</tr>
				
			</table>
		</s:form>
		</DIV>
	</body>

</html>
