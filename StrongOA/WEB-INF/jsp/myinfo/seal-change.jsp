<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
<head>
	<title>NTKO印章管理</title>
	<%@include file="/common/include/meta.jsp"%>
	<LINK href="<%=frameroot%>/css/windows.css" type=text/css rel=stylesheet>
	<link href="<%=frameroot%>/css/properties_windows.css" type="text/css" rel=stylesheet>
	<style type="text/css">
		.style2
		{
		    width: 45%;
		}
	</style>
<script language="JavaScript">

 //如果成功状态，显示当前印章信息
function ShowSignInfo()
{
    
    //document.all("SignName").value = ntkosignctl.SignName;
    //document.all("SignUser").value = ntkosignctl.SignUser;
    //document.all("Password").value = ntkosignctl.Password;
    //document.all("SignSN").value = ntkosignctl.SignSN;
    document.all("SignWidth").innerHTML = ntkosignctl.SignWidth;
    document.all("SignHeight").innerHTML = ntkosignctl.SignHeight;
}
//检查用户输入。参数IsNewSign标志是新建还是修改印章。新建的时候需要
//检查用户是否选择了印章原始文件。
function CheckInput(IsNewSign)
{

        
    var password = document.all("Password").value;
    var repassword = document.all("repassword").value;
    if(( password=='') ||( undefined == typeof(password))){     
        alert('请输入印章口令');
        document.getElementById("Password").focus();
        return false;
    }
    if( (password.length<6) || (password.length>32)){     
        alert('印章口令必须是6-32位.');
        document.getElementById("Password").focus();
        return false;
    } 
   	if(password!=repassword){
   		alert("印章口令与确认密码不同，其您重新输入确认口令");
   		document.getElementById("repassword").focus();
   		return false;
   	}
	ntkosignctl.Password = document.all("Password").value;
    return true;
}
//生成新印章文件
function CreateNew()
{
	if(!CheckInput(true))return;
    ntkosignctl.CreateNew(
    			document.all("SignName").value,
    			document.all("SignUser").value,
    			document.all("Password").value,
    			document.all("SignFile").value
    );
    if(0 != ntkosignctl.StatusCode)
    {
	    alert("创建印章文件错误.");
	    return;
    }
    alert("创建印章成功.您现在可以插入EKEY,并点击'保存印章到EKEY'将创建的印章保存到EKEY.");
}
//对话框方式生成新的印章文件
function CreateNewWithDialog()
{
    ntkosignctl.CreateNew();
    if(0 != ntkosignctl.StatusCode)
    {
	    alert("创建印章文件错误.");
	    return;
    }
    //正确，显示印章信息
    ShowSignInfo();
    alert("创建印章成功.您现在可以插入EKEY,并点击'保存印章到EKEY'将创建的印章保存到EKEY.");
}
function OpenFromEkey(pass)
{
	var ifCont = window.confirm("请插入EKEY到您的计算机.然后继续。");
	if(!ifCont)return;
    ntkosignctl.OpenFromEkey(pass);
    if(0 != ntkosignctl.StatusCode)
    {
	    alert("从EKEY打开印章错误.");
	    return;
    }
    //正确，显示印章信息
    ShowSignInfo();
    alert("从EKEY打开印章成功！您现在可以修改印章的相关信息并重新保存到EKEY.此时选择印章原始文件无效.");
}

function SaveToEkey()
{
	if(!CheckInput(false))return;
	var ifCont = window.confirm("请插入EKEY到您的计算机.然后继续。");
	if(!ifCont)return;
	ntkosignctl.SaveToEkey();
	if(0 == ntkosignctl.StatusCode)
	{
		alert("保存印章到EKEY成功!");
	}
	else
	{
		alert("保存印章到EKEY失败！！");
	}
}
function OpenFromLocal()
{
    var test=ntkosignctl.OpenFromLocal('',true);
    document.getElementById("Password").value="";
    document.getElementById("repassword").value="";
    ShowSignInfo();
}
function SaveToLocal()
{
	/**if(!CheckInput(false))return;
	ntkosignctl.SaveToLocal('',true);
	if(0 == ntkosignctl.StatusCode)
	{
		alert("保存印章到本地文件成功!");
	}
	else
	{
		alert("保存印章到本地文件失败！！");
	}*/
	if(!CheckInput(false))return;
	var path = ntkosignctl.LocalFileName;
	var args;
	if(path=='undefined'){
		alert("文件路径错误，不能进行保存！");
		return;
	}else{
		try{
			args=path.split("\\");
		}catch(e){
			alert("提取文件名异常，请您重新操作！");
		}
	}
	if(args.length>0){
		ntkosignctl.SaveToLocal(args[args.length-1],false);
		if(0 == ntkosignctl.StatusCode)
		{
			alert("保存印章到本地文件成功!");
		}
		else
		{
			alert("保存印章到本地文件失败！！");
		}
	}else{
		alert("对不起，出现异常！");
	}
}
function SetEkeyUserName()
{
	var EkeyUser = "";
	EkeyUser = document.all("EkeyUser").value;
    if(( EkeyUser=="") ||( undefined == typeof(EkeyUser)))
    {     
        alert('请输入EKEY用户名称!');
        return false;
    }
    if( (EkeyUser.length>24))
    {     
        alert('KEY用户名称不能超过24个字符.');
        return false;
    } 
	ntkosignctl.SetEkeyUser(EkeyUser);
	if(0 == ntkosignctl.StatusCode)
	{
		alert("设定EKEY用户:"+EkeyUser+"成功!");
	}
	else
	{
		alert("设定EKEY用户:"+EkeyUser+"失败！！");
	}	
}
function GetEkeyUserName()
{
	var EkeyUser = "";
	EkeyUser = ntkosignctl.GetEkeyUser();
	if(0 == ntkosignctl.StatusCode)
	{
		document.all("EkeyUser").value = EkeyUser;
		alert("读取EKEY用户成功！此EKEY用户是："+EkeyUser);
	}
	else
	{
		alert("读取EKEY用户失败！！");
	} 
}

function ChangeEkeyPin()
{
	var flags = document.all("forWho");
	var oldpass = document.all("oldPassword").value;
	var newpass1 = document.all("newPassword1").value;
	var newpass2 = document.all("newPassword2").value;
	if( (newpass1.length<4) || (newpass1.length>16) )
	{
        alert('EKEY访问口令必须是4-16位.');
        return false;	
	}
	if( newpass1 != newpass2)
	{
		alert('两次新口令不符合，请重新输入.');
        return false;
	}
    var isAdmin = true;
    if(flags[0].checked)
    {
    	isAdmin = false;
    }
    else
    {
    	isAdmin = true;
    }
    ntkosignctl.ChangeEkeyPassword(oldpass,newpass1,isAdmin);
    if(0 == ntkosignctl.StatusCode)
	{
		if(isAdmin)
		{
			alert("改变EKEY管理员口令成功!");
		}
		else
		{
			alert("改变EKEY用户口令成功!");
		}
	}
	else
	{
		if(isAdmin)
		{
			alert("改变EKEY管理员口令失败!");
		}
		else
		{
			alert("改变EKEY用户口令失败!");
		}
	}
}
function ResetEkeyUserPin()
{
	var adminPassword = document.all("adminPassword").value;
	var newUserPassword1 = document.all("newUserPassword1").value;
	var newUserPassword2 = document.all("newUserPassword2").value;
	if( (newUserPassword1.length<4) || (newUserPassword1.length>16) )
	{
        alert('EKEY访问口令必须是4-16位.');
        return false;	
	}
	if( newUserPassword1 != newUserPassword2)
	{
		alert('两次新口令不符合，请重新输入.');
        return false;
	}
    ntkosignctl.ResetEkeyUserPassword(adminPassword,newUserPassword1);
    if(0 == ntkosignctl.StatusCode)
	{
		alert("重设EKEY用户口令成功!");
	}
	else
	{
		alert("重设EKEY用户口令失败!");
	}
}
function ResetEkeySigns()
{
    ntkosignctl.ResetEkeySigns();
    if(0 == ntkosignctl.StatusCode)
	{
		alert("重设EKEY所有印章成功!");
	}
	else
	{
		alert("用户取消,或者重设EKEY所有印章失败!");
	}
}
//定位整个页面距中
function init()
{
	document.body.style.marginLeft=document.body.clientWidth/2-400;
}
</script>
</head>
<body class=contentbodymargin>
	<div id=contentborder align=center>
		<table class="table1">
		<tr>
			<td width="45%" height="50" class="biao_bg1">
				<div style="width:300px;height:100px"><br><!-- 以下为了适应微软新的ActiveX机制,将<object代阿放到外部,避免点击激活 -->
					<!--<script type="text/javascript" src="<%=path %>/common/OfficeControl/ntkoGenOcxObj.js"></script><br/> -->
					<object id="ntkosignctl" classid="clsid:3DFFFCF3-D78C-42b8-B5ED-44DC57B6FD4E" codebase="<%=path %>/common/OfficeControl/ntkosigntool.cab#version=3,0,5,0" width="100%" height="260px">
						<param name="BackColor" value="16777210">
						<param name="ForeColor" value="16744576">
						<SPAN STYLE="color:red">不能装载印章管理控件。请在检查浏览器的选项中检查浏览器的安全设置。</SPAN>
					</Object>
				</div>		
			</td>
		  <td valign="top" width="55%" class="td1">
			
					<table width="100%" style="vertical-align:middle; text-align:center;">
						<tr>
							<td class="biao_bg1">印章信息:</td>
							<td align="left" class="td1">宽度:<span id="SignWidth">0</span>&nbsp;&nbsp;高度:<span id="SignHeight">0</span></td>
						</tr>
						<tr>
							<td class="biao_bg1">印章口令:</td>
							<td align="left" class="td1"><input type="password" name="Input" id="Password" value="" size="25" maxlength="32"></td>
						</tr>
						<tr>
							<td class="biao_bg1">确认口令:</td>
							<td align="left" class="td1"><input type="password" name="reinput" id="repassword" value="" size="25" maxlength="32"></td>
						</tr>
						<tr>
							<td colspan="2"><hr></td>
						</tr>
						<tr>
							<td class="td1"><input type="button" name="openBtn" id="openBtn" value="打开本地印章" onClick="OpenFromLocal()" class="input_bg"></td>
							<td class="td1"><input type="button" name="saveBtn" id="saveBtn" value="保存印章到本地" onClick="SaveToLocal()" class="input_bg"></td>
						</tr>
				  </table>


		  <br/></td>
		</tr>
		<tr>
		</tr>
		</table>
	</div>
</body>
</html>
