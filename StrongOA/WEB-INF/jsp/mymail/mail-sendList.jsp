<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp" %>
<html>
	<head>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet"><!--右键菜单样式 -->
		<LINK href="<%=frameroot%>/css/search.css" type=text/css  rel=stylesheet>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css  rel=stylesheet>
		<style type="text/css">
			a.{text-decoration:none;}
		</style>
		<style type="text/css">
		.tbaCss tr td {
			padding:3px 0;
		}
		.myshow{
			width:50px;
		}
		.myhidden{
			width:40px;
			padding-left:10px;
			text-align:center;
			background:#ACCDF1;
		}
		</style>
		<title>已发邮件列表</title>
		<script language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script src="<%=path%>/common/js/common/search.js" type="text/javascript"></script>
		<script language="javascript" src="<%=path%>/common/js/upload/jquery.blockUI.js"></script>
		<script language='javascript' src='<%=root%>/common/js/grid/ChangeWidthTable.js'></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/validate/checkboxvalidate.js" type="text/javascript"></script>
		<script src="js/work.js" type="text/javascript"></script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT><!--右键菜单脚本 -->
		<script type="text/javascript">
			function show(i){
				$.blockUI({ overlayCSS: { backgroundColor: '#B3B3B3' }});
				$.blockUI({ message: '<font color="#008000"><b>'+i+'</b></font>' });
			}
			function hidden(){
				$.unblockUI();
			}
			function viewState(state){
				if(state=="1")
					return "<img src='<%=path%>/oa/image/mymail/read.gif'>";
				else
					return "<img src='<%=path%>/oa/image/mymail/unread.gif'>";
			}
			
			function viewPri(pri){
				if(pri=="0"){
					return "<img src='<%=path%>/oa/image/mymail/mark1.gif'>"
				}else{
					return "<img src='<%=path%>/oa/image/mymail/mark2.gif'>"
				}
			}
			
			function isAtt(hasAtt){
				if(hasAtt=="1"){
					return "<img src='<%=path%>/oa/image/mymail/yes.gif'>";
				}else{
					return "<img src='<%=path%>/oa/image/mymail/no.gif'>";
				}
			}
			
			
			$(document).ready(function(){
			  var d1x_click = false;
			  $("body").not($("#d1x")).click(function(){
				  if(!d1x_click)
				  {
				  	$("#d1x").css("display","none");
				  }

			  	  d1x_click = false;
			  });
			  $("#d1x").click(function(){
			  	  d1x_click = true;
			  });

			  $("#test").click(function(){
			  	var offset = $("#test").offset();
				var d1x = $("#d1x");
				if(d1x.css("display") == "block")
				{
					d1x.css("display","none");
				}
				else
				{
					d1x.css("display","block");
				}
				d1x.css("position","absolute");
				d1x.css("left",offset.left);
				d1x.css("top",offset.top+$("#test").height()+3);
				d1x_click = true;
				return false;
			  });
			  
				$("#img_sousuo").click(function(){
					$("#searchName").val(encodeURI($("#textfield").val()));
					$("#beginDate").val($("#starttime").val());
					$("#endDate").val($("#endtime").val());
					$("form").submit();
				});
			}); 
			function changeFolder(folderid){
				mailid=getValue();
				if(mailid==null||mailid==""){
					alert("请选择要移动的邮件!");
				}else{
				  	$.ajax({
				  		type:"post",
				  		dataType:"text",
				  		url:"<%=root%>/mymail/mail!changeFolder.action",
				  		data:"sendid="+mailid+"&folderId="+folderid,
				  		success:function(msg){
				  			if(msg=="true"){
				  			//	alert("文件夹更换成功！");
				  				window.location.reload();
							}else if(msg=="false"){
								alert("文件夹更换失败");
				  			}else if(msg=="nofolder"){
				  				alert("文件夹不存在");
				  			}else{
				  				alert("文件夹更换出现异常！");
				  			}
				  		}
				  	});
				}
				$("#d1x").css("display","none");
			}
			function ddd(){
				$("#d1x").css("display","none");
				$("#d2x").css("display","none");
			}
		</script>
		
	</head>
<BODY class=contentbodymargin oncontextmenu="return false;" onload=initMenuT()>
<script type="text/javascript" src="<%=path %>/common/js/newdate/WdatePicker.js"></script>
<DIV id=contentborder onmousewheel="ddd()" onscroll="ddd()" align=center>
    <s:form id="myTableForm" action="/mymail/mail.action" method="get">
  <table width="100%" border="0" cellspacing="0" cellpadding="00">
  <tr>
    <td height="100%">
    <table width="100%" border="0" cellspacing="0" cellpadding="00">
      <tr>
        <td height="40" style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
        <table width="100%" border="0" cellspacing="0" cellpadding="00">
          <tr>
            <td>&nbsp;</td>
            <td width="30%">
            <img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
            ${boxName }：${folderName }
            </td>
            <td>&nbsp;</td>
            <td width="70%">
            <table border="0" align="right" cellpadding="00" cellspacing="0">
                <tr>
                  <td width="*">&nbsp;</td>
                  <td><a class="Operation" id=write href="#" onclick="gotoWrite()"><img src="<%=root%>/images/ico/tijiao.gif" width="15" height="15" class="img_s">写信</a></td>
                  <td width="5"></td>
                  <td><a class="Operation" href="#" onclick="resend()"><img src="<%=root%>/images/ico/fasong.gif" width="15" height="15" class="img_s">转发</a></td>
                  <td width="5"></td>
                  <td><a class="Operation" href="#" onclick="readmail()"><img src="<%=root%>/images/ico/page.gif" width="15" height="15" class="img_s">阅读</a></td>
                  <td width="5"></td>
                  <td><span class="Operation" id="test" style="cursor:hand"><img src="<%=root%>/images/ico/zhuanjiaoxiayibu.gif" width="15" height="15" class="img_s">转移</span></td>
                  <!--<td width="8%"><a href="#" onclick="gotoSetPro()">属性</a></td>  -->
                  <td width="5"></td>
                  <td><a class="Operation" href="#" onclick="del()"><img src="<%=frameroot%>/images/perspective_leftside/shanchu.gif" width="15" height="15" class="img_s">删除</a></td>
                  <td width="5"></td>
                  <td><a class="Operation" href="#" onclick="realDel()"><img src="<%=root%>/images/ico/xiaohui.gif" width="15" height="15" class="img_s">彻底删除</a></td>
                  <td width="5"></td>
                </tr>
            </table>
            </td>
          </tr>
        </table>
        </td>
      </tr>
    </table>
    	 <input type="hidden" name="sendid" id="sendid" value="${sendid }">
    	 <input type="hidden" name="type" id="type" value="${returnType }">
    	 <input type="hidden" name="searchName" id="searchName" value="${searchName }">
    	 <input type="hidden" name="beginDate" id="beginDate" value="${firstDate }">
    	 <input type="hidden" name="endDate" id="endDate" value="${otherDate }">
	     <webflex:flexTable name="myTable" width="100%" height="100%" wholeCss="table1" property="0" isCanDrag="true" isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck" getValueType="getValueByArray" onclick="quicklyVeiwMail(this.value)" collection="${page.result}" page="${page}">
			<table width="100%" border="0" cellpadding="0" cellspacing="1" class="table1">
			     <tr>
			       <td width="5%" align="center"  class="biao_bg1"><img id="img_sousuo" style="cursor: hand;" src="<%=frameroot%>/images/perspective_leftside/sousuo.gif" width="15" height="15" ></td>
			       <td width="35%"  class="biao_bg1"><input name="textfield" id="textfield" type="text" style="width=100%" class="search" title="请您输入邮件主题"></td>
			       <td width="30%" align="center" class="biao_bg1"><strong:newdate  name="starttime" id="starttime" width="100%" skin="whyGreen" isicon="true"  classtyle="search" title="请输入起始日期"/></td>
			       <td width="30%" align="center" class="biao_bg1"><strong:newdate  name="endtime" id="endtime" width="100%" skin="whyGreen" isicon="true"  classtyle="search" title="请输入截止日期"/></td>
			       <td class="biao_bg1">&nbsp;</td>
			     </tr>
			</table> 
			<webflex:flexCheckBoxCol caption="选择" valuepos="0" valueshowpos="1" width="5%" isCheckAll="true" isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
			<webflex:flexTextCol caption="状态" valuepos="2" valueshowpos="javascript:viewState(2)" width="5%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
			<webflex:flexTextCol caption="附件" valuepos="3" valueshowpos="javascript:isAtt(3)" width="5%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
			<webflex:flexTextCol caption="类型" valuepos="4" valueshowpos="javascript:viewPri(4)" width="5%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
			<webflex:flexTextCol caption="收件人"  valuepos="8" valueshowpos="8" width="20%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
			<webflex:flexTextCol caption="主题" valuepos="0" valueshowpos="1" width="30%" isCanDrag="true" isCanSort="true" onclick="toViewMail(this.value)"></webflex:flexTextCol>
			<webflex:flexDateCol caption="日期" valuepos="6" valueshowpos="6" width="20%" isCanDrag="true" isCanSort="true"  showsize="20" dateFormat="yyyy-MM-dd hh:mm"></webflex:flexDateCol>
			<webflex:flexTextCol caption="大小(KB)" valuepos="7" valueshowpos="7" width="10%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
			
		</webflex:flexTable>
      </td>
  </tr>
</table>
</s:form>
</DIV>
<div id="d1x" class="Operation">
<div style="border-top:1px solid #fff; border-left:1px solid #fff; border-right:1px solid #B4C5E0; border-bottom:1px solid #B4C5E0; padding:5px;">
    <table width="50%" border="0" cellspacing="0" class="tbaCss" cellpadding="0">
      <s:iterator value="#request.folderList" status="statu" id="mailFolder">
      	<tr>
      		<td onclick="changeFolder('<s:property value="#mailFolder.mailfolderId"/>')" onmouseout="this.className='myshow'" onmouseover="this.className='myhidden'"><s:property value="#mailFolder.mailfolderName"/></td>
      	</tr>
      </s:iterator>
    </table>
</div>
</div>
<script language="javascript">
var sMenu = new Menu();
function initMenuT(){
	sMenu.registerToDoc(sMenu);
	var item = null;
	item = new MenuItem("<%=root%>/images/ico/fankui.gif","写信","gotoWrite",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/fankui.gif","转发","resend",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/page.gif","阅读","readmail",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=frameroot%>/images/perspective_leftside/shanchu.gif","删除","del",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/xiaohui.gif","彻底删除","realDel",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}
//主办工作(办理待办工作)
function readmail(){
	var redId=getValue();
	if(redId==null||redId==""){
		alert("请选择要阅读的邮件！");
	}else{
		if(redId.indexOf(",")!=-1){
			alert("一次只能阅读一封邮件！");
		}else{
			msg=OpenWindow('<%=root%>/mymail/mail!view.action?sendid='+redId, '700', '400', window);
			if(msg=="reply"){
				retell();
			}else if(msg=="del"){
				window.location.reload();
			}else if(msg=="resend"){
				resend();
			}
		}
	}
}


function resend(){
	var reId=getValue();
	if(reId==null||reId==""){
		alert("请选择要转发的邮件！");
	}else{
		if(reId.indexOf(",")!=-1){
			alert("一次只能转发一封邮件！");
		}else{
			boo=OpenWindow('<%=root%>/mymail/mail!tran.action?boxId=${boxId}&sendid='+reId, '700', '400', window);
			if(boo=="true"){
				window.location.reload();
			}
		}
	}
}

function del(){
	var delId=getValue();
	if(delId==null||delId==""){
		alert("请选择要删除的邮件！");
	}else{
		if(confirm("删除此邮件，确定？")==true){
	  	$.ajax({
	  		type:"post",
	  		dataType:"text",
	  		url:"<%=root%>/mymail/mail!delete.action",
	  		data:"sendid="+delId+"&type=notreal",
	  		success:function(msg){
	  			if(msg=="true"){
	  				//alert("删除成功！");
	  				window.location.reload();
				}else{
					alert("删除失败请您重新删除！");
	  			}
	  		}
	  	});
	  	}
	 }
}

function realDel(){
	var delId=getValue();
	if(delId==null||delId==""){
		alert("请选择要彻底删除的邮件！");
	}else{
		if(confirm("彻底删除此邮件，确定？")==true){
	  	$.ajax({
	  		type:"post",
	  		dataType:"text",
	  		url:"<%=root%>/mymail/mail!delete.action",
	  		data:"sendid="+delId+"&type=real",
	  		success:function(msg){
	  			if(msg=="true"){
	  				//alert("彻底删除成功！");
	  				window.location.reload();
	  				
				}else{
					alert("彻底删除失败请您重新删除！");
	  			}
	  		}
	  	});
		}
	 }
}
function gotoHtml(mailid){
	window.parent.mail_main_content.personal_status_content.location="status_content.htm";//?mailid="+mailid;
}

function toViewMail(mailid){
	msg=OpenWindow('<%=root%>/mymail/mail!view.action?sendid='+mailid, '700', '400', window);
	if(msg=="reply"){
		OpenWindow('<%=root%>/mymail/mail!reply.action?boxId=${boxId}&sendid='+mailid, '700', '400', window);
	}else if(msg=="del"){
		window.location.reload();
	}else if(msg=="resend"){
		OpenWindow('<%=root%>/mymail/mail!tran.action?boxId=${boxId}&sendid='+mailid, '700', '400', window);
	}
}

function quicklyVeiwMail(mailid){
	window.parent.mail_main_content.personal_status_content.location='<%=root%>/mymail/mail!quicklyview.action?sendid='+mailid
}

function gotoWrite(){
	//window.parent.location="writemail.jsp";
	var boo=OpenWindow('<%=root%>/mymail/mail!write.action?boxId=${boxId}', '700', '400', window);				//fileNameRedirectAction.action?toPage=mymail/writemail.jsp
	if(boo=="true"){
		window.location.reload();
	}else if(boo=="false"){
	}
}

</script>
</BODY>
</HTML>
