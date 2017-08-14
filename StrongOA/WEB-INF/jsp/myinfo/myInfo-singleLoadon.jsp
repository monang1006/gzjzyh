<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<html>
<head>
<%@include file="/common/include/meta.jsp"%>
<LINK href="<%=frameroot%>/css/windows.css" type=text/css rel=stylesheet>
<link href="<%=frameroot%>/css/properties_windows_add.css"
	type="text/css" rel=stylesheet>
<script language="javascript"
	src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
<script type="text/javascript" src="<%=path%>/oa/js/myinfo/md5.js"></script>
<script src="<%=path%>/common/js/common/common.js"
	type="text/javascript"></script>
<title>个人账号单点登录设置</title>
<style type="text/css">
input,select,button{ vertical-align:middle;}
.login_set{
	 border:1px solid #cdcdcd;
	 border-collapse:collapse;
	 background-color:#ffffff;
	 font-family:"宋体"；
	 font-size:14px;
	 min-width:1024px;}
.login_set td{ 
     height:56px; 
	  background-color:#f3f3f3; 
	  text-align:center;
	   border-bottom:1px solid #cdcdcd;
	   border-top:1px solid ;}
.login_set_in{ border-left:1px solid #ffffff;
               border-right:1px solid #ffffff;}
.login_set_in td{ border-top:1px solid #ffffff;
                   border-bottom:1px solid #ffffff;
				   padding:3px;
				    }
.center{ margin-left:auto; margin-right:auto;}
.login_sel{ height:21px; 
            width:266px; 
            border:1px solid #b3bcc3;
			vertical-align:middle;
			margin-bottom:2px;
			*margin-bottom:0px;}
.login_xx{ padding-left:8px;
           padding-right:8px;
		   color:#e90000; 
		   }
.login_text{ height:21px; line-height:21px; border:1px solid #b3bcc3;}
.login_but_out{ background:url(../frame/theme_blue/images/login_set_01.gif) no-repeat left center;}
.login_but{ height:22px; line-height:22px; width:53px; border:none; background: url(<%=frameroot%>/images/login_icon_01.gif) no-repeat; color:#ffffff !important;}

</style>
<script type="text/javascript">
			Array.prototype.contains = function (elem) {
				for (var i = 0; i < this.length; i++) {
					if (this[i] == elem) {
						return true;
					}
				}
				return false;
			}
			function submitForm(){
				var namesame=false;
				var names= new Array();
				var systemName=false;
				var nums=false;
				var psw=false;
				var checkNum=false;
				var checkPsw=false;
				$("[name^='list1'][name$='linkId']").each(function(i){
			    if(names.contains(this.value)){	        
				     namesame=true;
				  } 
			    	names.push(this.value);
  				});
				$("[name^='list1'][name$='SYS_ID']").each(function(i){
  					$(this).attr({name:"list1["+i+"].SYS_ID"});
  				});
				$("[name^='list1'][name$='linkId']").each(function(i){
  					$(this).attr({name:"list1["+i+"].linkId"});
  					if(this.value==""){ 
  						systemName=true;
  						this.size=2;
  						return false;
  						//alert(systemName);
  					}
  				});
		  		$("[name^='list1'][name$='SYS_NAME']").each(function(i){
		  			
  					$(this).attr({name:"list1["+i+"].SYS_NAME"});
  					
  				});
  				$("[name^='list1'][name$='SYS_NUM']").each(function(i){
		  		  
  					$(this).attr({name:"list1["+i+"].SYS_NUM"});
  					if(this.value==""){
  						nums=true;
  						this.focus();	
  						return false;
  					//	：{}|:"?[]\;',./，
  						//alert(nums);
  					}else if(this.value.indexOf("?") != -1 || this.value.indexOf("[")!= -1 || this.value.indexOf("]")!= -1 || this.value.indexOf(";")!= -1 || this.value.indexOf("'")!= -1
  							|| this.value.indexOf(",") != -1 || this.value.indexOf(".") != -1|| this.value.indexOf("/") != -1 || this.value.indexOf("，") != -1 || this.value.indexOf("|") != -1){
  						checkNum=true;
  					}
  					
  				});
  				$("[name^='list1'][name$='SYS_PASSWORD']").each(function(i){
  					$(this).attr({name:"list1["+i+"].SYS_PASSWORD"});
  					if(this.value==""){
  						psw=true;
  						if(!nums){
  						this.focus();	
  						}
  						return false;
  					}
  				});
  				if(systemName){   //验证系统名称非空
  					alert("系统名称为空请输入。");
  					return false;
  				}else if(nums){	//系统账号验证非空
					alert("系统账号为空请输入。");
					return false;

				}else if(psw){	//系统密码验证非空
					alert("密码为空请输入。");
					return false;
				}else if(checkNum){	//系统账号验证特殊字符
					alert("账号不能包含特殊字符，请检查输入。");
					return false;
				}else if(checkPsw){	//系统密码验证特殊字符
					alert("密码不能包含特殊字符，请检查输入。");
					return false;
				}else if(namesame){
					 alert("一个系统只能设置一个账号和密码。");
					 return false;
				}else{
					$.ajax({
						type:"post",
						url:"<%=path%>/myinfo/myInfo!saveSystem.action",
						data:$('#myForm1').serialize(),
						success:function(data){
								if(data!="ok" ){
									alert(data);					
								}else{
									location.reload() ;
								}
							},
						error:function(data){
							alert("对不起，操作异常"+data);
						}
				   });
			}
			
			}
					
					
					
<%--				$("#myForm1").attr("action","/oa/myinfo/myInfo!saveSystem.action");--%>
<%--				//alert(	$("#myForm1").attr("action"));--%>
<%--					document.getElementById("myForm1").submit();--%>
<%--					//window.location.reload();--%>
<%--					window.location.href= "<%=root%>/myinfo/myInfo!singleLoadon.action";--%>
<%--				}--%>
<%--			}--%>
			
			
		
			function add(obj){
				//新增信息的内容的tbody
				var tbody =  $("#userInfo");
				//tbody下存在tr的数量
				var childCount = tbody.children("tr").length;
				//tbody下最后一个td
				var lastTr = tbody.children("tr").eq(childCount-1);
				var newHtml = lastTr.html();
				if(newHtml!=null && newHtml!=""){
				//获取最后一个tr的html
	  			tbody.append("<tr>"+newHtml+"</tr>");
	  			 childCount = tbody.children("tr").length;         
				//tbody下最后一个td
				 lastTr = tbody.children("tr").eq(childCount-1);
	  			var tds = lastTr.children("td");
	  		
	  			tds.eq(1).children("select").eq(0).val("");	//下拉框
	  			tds.eq(3).children("input").eq(0).val("");	//账号
	  			tds.eq(5).children("input").eq(0).val("");	//密码
	  			tds.eq(6).children("input").eq(0).val("");	//id
				}else{
					window.location.reload();
				}
				$("[name^='list1'][name$='SYS_ID']").each(function(i){
  					$(this).attr({name:"list1["+i+"].SYS_ID"});
  				});
				$("[name^='list1'][name$='linkId']").each(function(i){
  					$(this).attr({name:"list1["+i+"].linkId"});
  				});
		  		$("[name^='list1'][name$='SYS_NAME']").each(function(i){
		  			
  					$(this).attr({name:"list1["+i+"].SYS_NAME"});
  				});
  				$("[name^='list1'][name$='SYS_NUM']").each(function(i){
		  		  
  					$(this).attr({name:"list1["+i+"].SYS_NUM"});
  				});
  				$("[name^='list1'][name$='SYS_PASSWORD']").each(function(i){

  					$(this).attr({name:"list1["+i+"].SYS_PASSWORD"});
  				});
				} 
			
			function deltr(obj){
			//alert(sysId);
			//var sysId = "";
			var delA = $(obj);
			var id = delA.next().val();
			if(id==""){
				delA.parent().parent().remove();
			}else{
					//ajax调用删除数据，需要补充
			var url = "<%=root%>/myinfo/myInfo!deleteSys.action";
				//var input = document.getElementById("SYS_ID").value;
			if(confirm("确定要删除吗？")){
				$.ajax({
					type:"post",
					url:url,
					data:{
						SYS_ID:id
					},
					success:function(data){
							if(data!="ok" ){
								alert(data);					
							}else{
								 //alert("删除成功");
								location.reload() ;
							}
						},
					error:function(data){
						alert("对不起，操作异常"+data);
					}
			   });
				//url = "notify!delete.action?afficheId="+id+"&inputType="+input;
				}
			}
		}
	
	
		$(document).ready(function(){
			/*初始化客户经理信息*/

		});
		function showTemp(value){
			value.size=1;
		}		
		</script>
		<style type="text/css">
		input,select,button{
		background-color:"#ffffff";
		}
		</style>
</head>
<body class=contentbodymargin>
	<DIV id=contentborder align=center>
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<s:form id="myForm1" name="myForm1"
			action="/myinfo/myInfo!singleLoadon.action"
			enctype="multipart/form-data" method="post" >
			<div align=left style="width: 95%;padding:5px;">
				<tr>
					<td colspan="3" class="table_headtd">
						<table border="0" width="100%" cellpadding="0" cellspacing="0">
							<tr>
								<td class="table_headtd_img"><img
									src="<%=frameroot%>/images/ico/ico.gif">&nbsp;</td>
								<td align="left"><strong>个人账户单点登录设置</strong></td>
								<td align="right">
									<table border="0" align="right" cellpadding="00"
										cellspacing="0">
										<tr>
										<td width="7"><img
												src="<%=frameroot%>/images/ch_h_l.gif" /></td>
											<td class="Operation_input" onclick="add(this);">&nbsp;新&nbsp;增&nbsp;</td>
											<td width="7"><img
												src="<%=frameroot%>/images/ch_h_r.gif" /></td>
											<td width="5"></td>
											<td width="7"><img
												src="<%=frameroot%>/images/ch_h_l.gif" /></td>
											<td class="Operation_input" onclick="submitForm();">&nbsp;保&nbsp;存&nbsp;</td>
											<td width="7"><img
												src="<%=frameroot%>/images/ch_h_r.gif" /></td>
											<td width="5"></td>
										</tr>
									</table></td>
							</tr>
						</table></td>
				</tr>
			</div>
			<fieldset style="width: 95%">
				<%--<legend>
					<span class="wz">个人账号单点登录设置 </span>
				</legend>

				--%><table width="100%" border="0" cellpadding="0" class="login_set center" cellspacing="1" id="tab">
					<tr>
<%--						<td><input type="button"--%>
<%--							class="information_list_choose_button9" id="addMember"--%>
<%--							value="新增登录系统" onclick="add(this);" />--%>
<%--						</td>--%>
					</tr>
					<tr>
						<td>
							<div class="dmalb">
								<table width="100%" border="0" class="lbtab" width="99%"
									cellpadding="0" cellspacing="0" align="center"><%--
									<thead>
										<tr>
										    <td class="login_xx">系统名称</td>
											<td class="login_xx">账号</td>
											<td class="login_xx">密码</td>
											<td class="login_xx">操作</td>
										</tr>
									</thead>
									--%><tbody id="userInfo">
									<s:if test="list1!=null">
										<s:iterator value="list1" id="u" status="stt">
										<tr>
<%--											<input  type="text"  value="<s:property value='#stt.index' /> " />--%>
<%--											<td class="login_xx">系统名称</td>--%>
											<td ><font color=red>*</font>&nbsp;系统名称</td>
											<td>
<%--												<input type="hidden"  name="list1[${stt.index}].SYS_ID" value="${u.SYS_ID}">--%>
											<s:if test="list2==null">
											<select  name="list1[#stt.index].linkId">
												<option>请选择第三方系统<option>
											</select>
											</s:if>
											<s:else>
												<s:select list="list2" listKey="linkId"  
												listValue="systemName" headerKey="" 
												 headerValue="请选择第三方系统" id="linkId"   onclick="showTemp(this);" onmousemove="showTemp(this);" 
												  name="list1[#stt.index].linkId" class="login_sel" style="width:280px;"/>
											</s:else>
											</td>
											<td ><font color=red>*</font>&nbsp;账号：</td>
											<td><input type='text' name="list1[${stt.index}].SYS_NUM"
												 style="width: 160px" class="login_text"
												value="${u.SYS_NUM}" maxlength="20" /></td>
											<td><font color=red>*</font>&nbsp;密码：</td>
											<td><input type='password'
												name="list1[${stt.index}].SYS_PASSWORD" class="login_text"
												value="${u.SYS_PASSWORD}" maxlength="20" /></td>

											<td ><a href=# onclick="deltr(this)"
												class="login_but" ></a>&nbsp;
											<input type="hidden" class="login_but" name="list1[${stt.index}].SYS_ID" value="${u.SYS_ID}">
											</td>
										</tr>
									</s:iterator>
									</s:if>
									<s:else>
										<tr>
										<td ><font color=red>*</font>系统名称</td>
											<td>
											<s:if test="list2==null">
											<select  name="list1[#stt.index].linkId">
												<option>请选择第三方系统<option>
											</select>
											</s:if>
											<s:else>
												<s:select list="list2" listKey="linkId" 
												listValue="systemName" headerKey=""
												 headerValue="请选择第三方系统" id="linkId"   
												  name="list1[0].linkId"  style="width:280px;"/>
											</s:else>
											</td>
											<td><font color=red>*</font>账号</td>
											<td><input type='text' name="list1[0].SYS_NUM"
												 style="width: 150px"
												value="${u.SYS_NUM}" maxlength="20" /></td>
												<td><font color=red>*</font>密码</td>
											<td><input type='password'
												name="list1[0].SYS_PASSWORD" 
												value="${u.SYS_PASSWORD}" maxlength="20" /></td>

											<td ><a href=# onclick="deltr(this)"
												class="login_but"></a>&nbsp;
											<input type="hidden"  name="list1[0].SYS_ID" value="${u.SYS_ID}">
											</td>
										</tr>
									</s:else>
									</tbody>
								</table>
							</div></td>
					</tr>
				</table>
			</fieldset>
			<br>
		</s:form>
	</DIV>
</body>
</html>
