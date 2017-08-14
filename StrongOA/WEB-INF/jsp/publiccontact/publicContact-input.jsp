<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>公共联系人管理</title>
		<LINK type=text/css rel=stylesheet href="<%=frameroot%>/css/properties_windows_add.css">
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/upload/jquery.MultiFile.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/upload/jquery.blockUI.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/validate/jquery.validate.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/validate/formvalidate.js" type="text/javascript" language="javascript"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/service.js"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<SCRIPT>
		function save(){
			var strTemp;
			//var t = document.getElementById("xiha").value;
			//alert(t);
			//document.getElementById("myForm"),submit();
			var pcName = trim($("#pcName").val());
			var pcTell = trim($("#pcTell").val());
			var pcPhone = trim($("#pcPhone").val());
			var pcEmail = trim($("#pcEmail").val());
			var pcPost = trim($("#pcPost").val());
			var pcOther = trim($("#pcOther").val());
			var temp = "";
			if(pcName==null||pcName==""){
				temp = "姓名为必填项。\n";
				strTemp="pcName";
			}else{
				if(pcName.length>20){
					temp = temp + "姓名输入长度不能大于20。\n";
					strTemp="pcName";
				}
			}
			if(pcTell!=null&&pcTell!=""){
				if(pcTell.length>20){
					temp = temp + "电话输入长度不能大于20。\n";
					strTemp="pcTell";
				}else if(!IsTelephone(pcTell)){
					temp = temp + "电话输入不符合电话格式,前面请加区号如：0791-8888888\n";
					strTemp="pcTell";
				}
			}
			if(pcPhone!=null&&pcPhone!=""){
				if(pcPhone.length>20){
					temp = temp + "手机号码输入长度不能大于20。\n";
					strTemp="pcPhone";
				}else if(!testPhone(pcPhone)){
					temp = temp + "手机号码输入不符合手机号码格式。\n";
					strTemp="pcPhone";
				}
			}
			if(pcEmail!=null&&pcEmail!=""){
				if(pcEmail.length>32){
					temp = temp + "Email输入长度不能大于32。\n";
					strTemp="pcEmail";
				}else if(!testEmail(pcEmail)){
					temp = temp + "Email输入不符合Email格式。\n";
					strTemp="pcEmail";
				}
			}
			if(pcPost!=null&&pcPost!=""){
				if(pcName.length>20){
					temp = temp + "职务输入长度不能大于20。\n";
					strTemp="pcPost";
				}
			}
			if(pcOther!=null&&pcOther!=""){
				if(pcName.length>100){
					temp = temp + "其他输入长度不能大于20。\n";
					strTemp="pcPost";
				}
			}
			if(temp!=""){
				alert(temp);
				//定位光标
				document.getElementById(strTemp).focus();
				return;
			}
			$("#myForm").submit();
		}
		//验证是否是手机格式
		function testPhone(tel){ 
			var reg = new RegExp("^[0-9]*$");
			if(!reg.test(tel)){
		        return false;
		    }
			if(tel.length!=11){
				return false;
			}else{
				var t = tel.substr(0,3);
				var mmm = "133,153,180,181,189,134,130,131,132,145,155,156,185,186,139,138,137,136,135,159,158,152,151,150,182,183,184,157,188,187,147";
				if(mmm.indexOf(t)==-1){
					return false;
				}
				return true;
			}
		} 
		function testEmail(str){
		  	var reg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
			if(reg.test(str)){
				return true;
			}else{
				return false;
			}
		}
		function IsTelephone(obj)// 正则判断
		{ 
			return (/^(([0\+]\d{2,3}-)?(0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/.test(obj));
		} 
		function trim(str){ //删除左右两端的空格
	　　     return str.replace(/(^\s*)|(\s*$)/g, "");
	　　 }
	　　 function ltrim(str){ //删除左边的空格
	　　     return str.replace(/(^\s*)/g,"");
	　　 }
	　　 function rtrim(str){ //删除右边的空格
	　　     return str.replace(/(\s*$)/g,"");
	　　 }
//特殊字符过滤
//匹配中文 数字 字母 下划线       
function checkInput(vvv) {
	var s = vvv.value;
	if(s==""||s==null) return;
	//var pattern = new RegExp("[`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）&mdash;—|{}【】‘；：”“'。，、？]")
    var pattern = new RegExp("[`~!@#$^&*()=|{}':;'\\[\\].<>/?~！@#￥……&*（）&mdash;—|{}【】‘；：”“'。？]")
    var rs = "";
	var ttt =/^[A-Za-z]+$/;//英文字母
	var k;
    for (var i = 0; i < s.length; i++) {
        //rs = rs + s.substr(i, 1).replace(pattern, '');
        //alert(pattern.test(s.substr(i,i+1)))
        var mmm = s.substr(i,1);
        if(ttt.test(mmm)){
        	continue;
        }
        if(pattern.test(mmm)){
        	//alert();
        	k=i;
        	rs="……";
        	break;
        }
    }
    if(rs!=""){
    	alert("不允许输入特殊字符。");
    	vvv.value = vvv.value.substring(0,k);
    }else{
    	return  true;
    }
}
 		</SCRIPT>
	</head>
	<base target="_self"/>
	<body class=contentbodymargin  oncontextmenu="return false;">
		<DIV id=contentborder align=center>
		<s:form id="myForm" theme="simple"  action="/publiccontact/publicContact!save.action" method="post">
			<input type="hidden" id="pcId" name="pcId" value="${model.pcId}">
			<input type="hidden" id="pcCreateDate" name="model.pcCreateDate" value="${model.pcCreateDate}">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
				<tr>
					<td height="100%">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td colspan="3" class="table_headtd">
										<table width="100%" border="0" cellspacing="0" cellpadding="00">
											<tr>
												<td class="table_headtd_img" >
													<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
												</td>
												<td align="left">
													<s:if test="#request.addoredit!=1"><strong>新建人员</strong></s:if>
													<s:else><strong>编辑人员</strong></s:else>
												</td>
												<td align="right">
													<table border="0" align="right" cellpadding="00" cellspacing="0">
														<tr>
															<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
										                 	<td class="Operation_input" onclick="save();">&nbsp;保&nbsp;存&nbsp;</td>
										                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
									                  		<td width="5"></td>
										                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
										                 	<td class="Operation_input1" onclick="window.close()">&nbsp;取&nbsp;消&nbsp;</td>
										                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
									                  		<td width="6"></td>
														</tr>
													</table>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
							<table  border="0" cellpadding="0" cellspacing="1" class="table1" width="100%" >
								<tr>
									<td  class="biao_bg1" align="right">
										<span class="wz" ><font color="red">*</font>&nbsp;姓名：</span>
                                    </td>
                                    <td class="td1">
										<input type="text" id="pcName" name="model.pcName" value="${model.pcName}"
										maxlength="20" onkeyup="checkInput(this);">
								  	</td>
								</tr>
								<tr>
									<td class="biao_bg1" align="right">
										<span class="wz">电话：</span>	
									</td>
									<td class="td1">
										<input type="text" id="pcTell" name="model.pcTell" value="${model.pcTell}"
										maxlength="20">
								  	</td>
								</tr>
								<tr>
									<td  class="biao_bg1" align="right">
										<span class="wz">手机号码：</span>
									</td>
									<td class="td1">
										<input type="text" id="pcPhone" name="model.pcPhone" value="${model.pcPhone}"
										maxlength="20">
								  	</td>
								</tr>
								<tr>
									<td  class="biao_bg1" align="right">
										<span class="wz">Email：</span>
									</td>
									<td class="td1">
										<input type="text" id="pcEmail" name="model.pcEmail" value="${model.pcEmail}"
										maxlength="32">
								  	</td>
								</tr>
								<tr>
									<td  class="biao_bg1" align="right">
										<span class="wz">职务：</span>
									</td>
									<td class="td1">
										<input type="text" id="pcPost" name="model.pcPost" value="${model.pcPost}"
										maxlength="16" onkeyup="checkInput(this);">
								  	</td>
								</tr>
								<tr>
									<td  class="biao_bg1" align="right">
										<span class="wz">其他：</span>
									</td>
									<td class="td1">
										<input type="text" id="pcOther" name="model.pcOther" value="${model.pcOther}"
										maxlength="20" onkeyup="checkInput(this);">
								  	</td>
								</tr>
								<tr>
									<td  class="biao_bg1" align="right">
										<span class="wz">类别：</span>
									</td>
									<td class="td1">
										<%--<s:select list="typeMap" listKey="key" listValue="value" id="typeId" name="typeId"
										 cssStyle=" width:150px;"></s:select>--%>
										<s:select list="pccList" listKey="pccId" listValue="pccName" id="typeId" name="typeId"
										 cssStyle=" width:150px;"></s:select>
								  	</td>
								</tr>
							</table>
							<div style="height: 20px;"></div>
					</td>
				</tr>
			</table>
						</s:form>
		</DIV>
	</body>
</html>
