<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>

		<!--  引用公共样式文件,建议所有样式都以文件方式定义在jsp文件外部,通常定义在WebRoot目录下的CSS文件夹下-->
		<LINK href="<%=frameroot%>/css/properties_windows_special.css"
			type=text/css rel=stylesheet>
		<script language="javascript"
			src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<title>系统配置</title>
		<script type="text/javascript">
			$(document).ready(function(){
					var shtml="";
					$.getJSON("<%=root%>/systemset/systemset!pushSettingToJson.action?dt="+new Date(),{},function(data){
						$.each(data.rows,function(i,dt){
							shtml+=generateHtml(dt.mName,dt.mCode,dt.mStatus);
						});
						$("#settingHtml").html(shtml);
					});
				
					var jsonHtml="";
					$("#settingHtml").bind("click",function(){
						$("[name=checkedFlag]").each(function(){
								if($(this).attr("checked")){
									$(this).val("1");
								}else{
									$(this).val("0");
								}
						});
					});
			});
 			//验证数字
 			function checkNum(str){
 				var reg=/^[0-9]*$/;
 				var result=str.match(reg);   
			    if(result==null) return false;   
			    return true;   
 			}
 			function isNumber(oNum) {
	          if(!oNum) return false;
			  var strP=/^(([0-9][0-9]*)|([1-9]*))$/;
	          if(!strP.test(oNum)) return false;
	          try{
	            if(parseFloat(oNum)!=oNum) return false;
	          }catch(ex){
	            return false;
	          }
	          return true;
	       }
	       /**
	       	提交保存
	       */
	       function saveSetting(){
	       		var pvalue="";
	       		var pname="";
	       		var p1="pushModuleNo";
	       		var p2="checkedFlag";
	       		$("#settingHtml input").each(function(){
	       			if($(this).attr("name")==p1){
	       				pname+=$(this).attr("value")+";";
	       			}
	       			if($(this).attr("name")==p2){
	       				pvalue+=$(this).attr("value")+";";
	       			}
	       		});
	       		if(pvalue!=""){
	       			pvalue=pvalue.substring(0,pvalue.length-1);
	       		}
	       		$.post("<%=root%>/systemset/systemset!saveSetting.action",{"pushModuleNo":pname,"checkedFlag":pvalue},function(data){
	       			if(data=="OK"){
	       				alert("保存成功!");
	       			}else{
	       				alert("保存失败!");
	       			}
	       		});
	       }
	       function generateHtml(jname,jcode,jchecked){
	       		if(jchecked=="1"){
			       	return "<tr><td><input name='checkedFlag' type='checkbox' checked='checked' value='1'/><input type='hidden' name='pushModuleNo' value='"+jcode+"'></td><td>"+jname+"</td</tr>";
	       		}else{
	       			return "<tr><td><input name='checkedFlag' type='checkbox' value='0'/><input type='hidden' name='pushModuleNo' value='"+jcode+"'></td><td>"+jname+"</td></tr>";
	       		}
	       }
		</script>
	</head>
	<body class=contentbodymargin>
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td colspan="3" class="table_headtd">
						<table width="100%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td class="table_headtd_img">
									<img src="<%=frameroot%>/images/ico/ico.gif">
									&nbsp;
								</td>
								<td align="left">
									<strong>推送设置</strong>
								</td>
								<td align="right">
									<table border="0" align="right" cellpadding="00"
										cellspacing="0">
										<tr>
											<td width="7">
												<img src="<%=frameroot%>/images/ch_h_l.gif" />
											</td>
											<td class="Operation_input" onclick="saveSetting()">
												&nbsp;保&nbsp;存&nbsp;
											</td>
											<td width="7">
												<img src="<%=frameroot%>/images/ch_h_r.gif" />
											</td>
											<td width="5"></td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
			<input type="hidden" id="systemsetId" name="model.systemsetId" value="${model.systemsetId}" />
			<fieldset style="width: 95%">
				<legend>
					<span class="wz">系统全局配置</span>
				</legend>
				<table id="settingHtml">
				</table>
			</fieldset>
		</DIV>
	</body>
	<script type="text/javascript">
		function setTime(t){
			var n = new Date();
			$("#reminderDate").val(n.getYear()+"-01-01 "+t);
		}
		function isUsing(key){
			if(key==1){
				$("#dialogBirth").slideDown("slow");
				$("#dialogBirths").slideDown("slow");
			}else if(key==0){
				$("#dialogBirth").fadeOut("slow");
				$("#dialogBirths").fadeOut("slow");
			}
		}
	</script>
</html>
