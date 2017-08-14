<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp" %>
<html>
	<head>
	<%@include file="/common/include/meta.jsp" %>
	<LINK href="<%=frameroot%>/css/properties_windows_add.css" type=text/css rel=stylesheet>
	<script language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
	<title>
			<strong>保存标签</strong>
	</title>
		<script>
				//拦截特殊字符  
			function CheckCode(t) {  
			    var Error = "";  
			    var re = /^[a-z\d\u4E00-\u9FA5]+$/i;  
			    if (!re.test(t)) {  
			        Error = "中含有特殊字符，拒绝输入。";  
			    }  
			    return Error;  
			}  
			
			//保存标签,需要校验书签名称的唯一性
			function doSave(){
				var name = $.trim($("#name").val());		//标签名称
				//name = name.replace(/\s+/g,""); 			//去除标签中的空格
				var desc = $.trim($("#desc").val());		//标签说明
				var remark = $.trim($("#remark").val());	//标签备注
				if(name == ""){
					alert("标签名称不能为空。");
					return ;
				}else{
				   var ret=CheckCode(name);
		             if(ret!=null && ret!=""){
		                 alert("标签名称"+ret);
		                 return;
		             }
				}		
				if(desc == ""){
					alert("标签说明不能为空。");
					return ;
				}
				var partn = /^[\S]*$/;
				if(!partn.exec(name))  {  
            		alert("标签名称中间不能有空格。"); 
            		return ; 
        		}  
				
				var regex = /^([\d])/;
				if(regex.test(name)){
					alert("标签名称不能以数字开头。")
                    return ; 
        		}
			
				//校验标签名称的唯一性
				$.post(scriptroot+"/bookmark/bookMark!checkName.action",{"model.id":'${model.id}',"model.name":name},function(ret){
					if(ret == "-2"){
						alert("对不起，校验标签名称时发生异常。请与管理员联系。");
						return ;
					}else if(ret == "-1"){
						alert("标签名称传递错误，请检查您的输入是否正确。");
						return ;
					}else if(ret == "1"){
						alert("标签名称【"+name+"】，已存在，请重新输入。");
						return ;
					}else{
						$("form").submit();
					}
				});
			}
			
			//返回
			function doReturn(){
				location = scriptroot + "/bookmark/bookMark.action";
			}	
		</script>
	</head>
	<body class="contentbodymargin">

		<DIV id=contentborder cellpadding="0" >
			<s:form action="/bookmark/bookMark!save.action" theme="simple" name="bookMarkForm" id="bookMarkForm">
			<s:hidden name="model.id"></s:hidden>
			<table width="100%" border="0" cellspacing="0" cellpadding="0" >
				<tr>
					<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left"><strong>
								保存标签
								</strong>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
					                <tr>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onclick="doSave();">&nbsp;保&nbsp;存&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
					                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
					                 	<td class="Operation_input1" onclick="doReturn();">&nbsp;取&nbsp;消&nbsp;</td>
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
					<table width="100%" height="100%" border="0" cellpadding="0"
				cellspacing="0" align="center" class="table1">
					<tr>
						<td>
							<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="1" align="center" >
								<tr id="tr1">
									<td width="10%" height="21" class="biao_bg1"  align="right">
										<span class="wz"><font color=red>*</font>&nbsp;标签名称：</span>
									</td>
									<td class="td1" valign="top">
										<s:textfield id="name" name="model.name" size="60"  maxlength="50"></s:textfield>
									</td>
								</tr>
								<tr id="tr1">
									<td width="10%" height="21" class="biao_bg1" align="right">
										<span class="wz"><font color=red>*</font>&nbsp;标签说明：</span>
									</td>
									<td class="td1" valign="top">
										<s:textfield id="desc" size="60"  maxlength="50" name="model.desc"></s:textfield>
									</td>
								</tr>
								<tr id="tr1">
									<td width="10%" height="21" class="biao_bg1" align="right">
										<span class="wz">标签备注：</span>
									</td>
									<td class="td1" valign="top">
										<s:textfield id="remark"  size="60" maxlength="50" name="model.remark"></s:textfield>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
					</td>
				</tr>
			</table>
			
			</s:form>
		</DIV>
	</body>

</html>
