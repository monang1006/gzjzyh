<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML><HEAD><TITLE>个人通讯录-联系人列表</TITLE>
<%@include file="/common/include/meta.jsp" %>
<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet"><!--右键菜单样式 -->
<LINK href="<%=frameroot%>/css/properties_windows_list.css" type=text/css  rel=stylesheet>
<LINK href="<%=frameroot%>/css/search.css" type=text/css  rel=stylesheet>
<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT><!--右键菜单脚本 -->
<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
<script src="<%=path%>/common/js/validate/checkboxvalidate.js" type="text/javascript"></script>
<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
<!--<script src="<%=path%>/common/js/common/search.js" type="text/javascript"></script>-->
<script src="<%=path%>/oa/js/address/address.js" type="text/javascript"></script>
<script language="javascript" src="<%=path%>/common/js/common/windowsadaptive.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
		$("#hrfWriteMail").click(gotoWriterMail);
	});
	//添加组
	function addGroup(){
		var ret=OpenWindow("<%=root%>/fileNameRedirectAction.action?toPage=address/addressGroup-add.jsp","300","200",window);
		if("suc"==ret && ret!=undefined){
				parent.project_work_tree.location="<%=root %>/address/addressGroup.action";
		}
	}
	//导入系统人员
	function importPublic(){
		var ret=OpenWindow("<%=root%>/address/addressOrg!userImport.action?groupId=${groupId}","750","400",window);
		//只有真正执行了导入动作才刷新当前页面
		if("yes" == ret){
			parent.document.location="<%=root%>/fileNameRedirectAction.action?toPage=address/address-personal.jsp" ;
		}
	}
	function exp(){
		var groupId=$("#groupId").val();
		var ret=OpenWindow("<%=root%>/address/address!initExport.action?groupId="+groupId,"400","370",window);
		}
</script>
</HEAD>
<BODY class=contentbodymargin  oncontextmenu="return false;" onload=initMenuT()><!-- oncontextmenu="return false;" onload=initMenuT()-->
<label id="l_actionMessage" style="display: none;"><s:actionmessage/></label>
<DIV id=contentborder align=center>
  <table width="100%" border="0" cellspacing="0" cellpadding="00">
  <tr>
    <td colspan="3" class="table_headtd">
    <table width="100%" border="0" cellspacing="0" cellpadding="00">
      <tr>
      		<td class="table_headtd_img" >
					<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
			</td>
			<td align="left">
				<strong>${groupName }</strong>
			</td>
            <td align="right">
				<table border="0" align="right" cellpadding="00" cellspacing="0">
		            <tr>
		            	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
	                 	<td class="Operation_list" onclick="gotoDel();" ><img src="<%=root%>/images/operationbtn/del.png"/>&nbsp;删&nbsp;除&nbsp;</td>
	                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
                  		<td width="5"></td>
                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
	                 	<td class="Operation_list" onclick="exp();"><img src="<%=root%>/images/operationbtn/daochu.png"/>&nbsp;导&nbsp;出&nbsp;列&nbsp;表&nbsp;人&nbsp;员&nbsp;</td>
	                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
                  		<td width="5"></td>
                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
	                 	<td class="Operation_list" onclick="importPublic();"><img src="<%=root%>/images/operationbtn/daoru.png"/>&nbsp;导&nbsp;入&nbsp;系&nbsp;统&nbsp;人&nbsp;员&nbsp;</td>
	                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
                  		<td width="5"></td>		
            
                
                
	                  <%--<td width="50"><a class="Operation" href="#" url="<%=root%>/address/address!input.action" id="hrfNewPerson"><img class="img_s" src="<%=root%>/images/ico/tianjia.gif" width="15" height="15" />添加</a></td>
	                  <td width="5">&nbsp;</td>
	                  <td width="50"><a class="Operation" href="#" url="<%=root%>/address/address!initEdit.action"  id="hrfView"><img class="img_s" src="<%=root%>/images/ico/chakan.gif" width="15" height="15" />查看</a></td>
	                  <td width="5">&nbsp;</td>
	                  <td><a class="Operation" href="#" url="<%=root%>/address/address!delete.action" id="hrfDel"><img class="img_s" src="<%=root%>/images/ico/shanchu.gif" width="15" height="15">删除&nbsp;</a></td>
	                  <td width="5">&nbsp;</td>
	                  <td width="50"><a class="Operation" href="#" url="<%=root%>/fileNameRedirectAction.action?toPage=address/address-import.jsp" id="hrfImport"><img class="img_s" src="<%=root%>/images/ico/daoru.gif" width="15" height="15">导入</a></td>
	                  <td width="5">&nbsp;</td>
	                  <td><a class="Operation" href="#" url="<%=root%>/address/address!initExport.action?groupId=" id="hrfExport"><img class="img_s" src="<%=root%>/images/ico/daochu.gif" width="15" height="15">导出&nbsp;</a></td>
	                  <td width="5">&nbsp;</td>
	                  <%--<td width="68"><a class="Operation" href="#" url="<%=root%>/address/addressGroup!chooseperson.action" id="hrfWriteMail"><img class="img_s" src="<%=root%>/images/ico/tijiao.gif" width="15" height="15">写邮件</a></td>
					  <td width="5">&nbsp;</td>
					  <td><a class="Operation" href="#" onclick="JavaScript:importPublic();"><img class="img_s" src="<%=root%>/images/ico/daoru.gif" width="15" height="15">导入系统人员&nbsp;</a></td>
					  <td width="5">&nbsp;</td>	--%>	
                </tr>
            </table>
            </td>
          </tr>
        </table>
        </td>
      </tr>
	<s:form id="myTableForm" action="/address/address.action">
		<input id="groupId" type="hidden" name="groupId" value="${groupId }"/>
		 <input id="groupName" type="hidden" name="groupName" value="${groupName }"/><!-- 用于将文件名传到后台然后传回此页面显示在<label> -->
	     <webflex:flexTable name="myTable" width="100%" height="370px" wholeCss="table1" property="fileId" 
	     isCanDrag="true" isCanFixUpCol="true" clickColor="#A9B2CA" 
	     footShow="showCheck" getValueType="getValueByProperty" 
	     collection="${page.result}" page="${page}">
	     <table width="100%" border="0" cellpadding="0" cellspacing="0" class="table1_search">
		 <tr>
		 		 <td>
		 		   <div style="float: left;">
		            &nbsp;&nbsp;姓名：&nbsp;<input name="model.name"  style="width: 140px;"  id="model.name" type="text" class="search" title="请您输入姓名" value="${model.name}">
		       		</div>
		       		<div style="float: left;">
		       		&nbsp;&nbsp;电话：&nbsp;<input name="model.tel1"  style="width: 140px;" id="model.tel1" type="text" class="search" title="请您输入电话" value="${model.tel1 }">
		       		</div>
		       		<div style="float: left;width:316px">
		       		&nbsp;&nbsp;手机号码：&nbsp;<input  name="model.mobile1"  style="width: 120px;" id="model.mobile1" type="text" class="search" title="请您输入手机号码" value="${model.mobile1 }">
		       		&nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo" type="button" onclick="$('#img_sousuo').click();"/>
		       		</div>
		       	</td>
		 
          <!--<td width="4%" align="center"  class="biao_bg1"><img style="cursor: hand;" id="img_search" src="<%=root%>/images/ico/sousuo.gif" title="单击搜索" width="15" height="15"></td>
          <td width="20%" align="center"  class="biao_bg1"><s:textfield name="model.name" cssClass="search" title="输入姓名"></s:textfield></td>
          <td width="45%" align="center" class="biao_bg1"><s:textfield name="email" cssClass="search" title="输入电子邮件"></s:textfield></td>
          <td width="23%" align="center" class="biao_bg1"><s:textfield name="model.tel1" cssClass="search" title="输入电话"></s:textfield></td>
          <td width="18%" align="center" class="biao_bg1"><s:textfield name="model.mobile1" cssClass="search" title="输入手机号码"></s:textfield></td>
          <td class="biao_bg1">&nbsp;</td>-->
         </tr>
		 </table>
		<webflex:flexCheckBoxCol caption="选择" property="addrId" 
			showValue="name" width="4%" isCheckAll="true" isCanDrag="false"
			isCanSort="false"></webflex:flexCheckBoxCol>
		<webflex:flexTextCol caption="姓名" property="name" 
			showValue="name" showsize="50"  width="20%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
		<webflex:flexNumricCol caption="电话" property="tel1" 
			showValue="tel1" width="23%" showsize="50" isCanDrag="true" isCanSort="true"></webflex:flexNumricCol>
		<webflex:flexNumricCol caption="手机号码" showsize="50" property="mobile1" 
			showValue="mobile1" width="18%" isCanDrag="true" isCanSort="true"></webflex:flexNumricCol>
		<webflex:flexTextCol caption="Email" property="defaultEmail"
			showValue="defaultEmail" width="20%" showsize="50" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
		<webflex:flexTextCol caption="职务" property="position" 
			showValue="position" showsize="50"  width="15%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>	
	  </webflex:flexTable>
	</s:form>
	</table>
      </td>
  </tr>
</table>
</DIV>
<script language="javascript">
var sMenu = new Menu();

function initMenuT(){
	sMenu.registerToDoc(sMenu);
	var item = null;
		//item = new MenuItem("<%=root%>/images/ico/tianjia.gif","添加","gotoNew",1,"ChangeWidthTable","checkOneDis");
		//sMenu.addItem(item);
		//item = new MenuItem("<%=root%>/images/ico/chakan.gif","查看","gotoView",1,"ChangeWidthTable","checkOneDis");
		//sMenu.addItem(item);
		item = new MenuItem("<%=root%>/images/operationbtn/del.png","删除","gotoDel",1,"ChangeWidthTable","checkOneDis");
		sMenu.addItem(item);
		//item = new MenuItem("<%=root%>/images/ico/tijiao.gif","写邮件","gotoWriterMail",1,"ChangeWidthTable","checkOneDis");
		//sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}
//搜索
$(document).ready(function(){
        $("#img_sousuo").click(function(){
        	$("form").submit();
        });     
      });
//查看
function gotoView(){
	var url = $("#hrfView").attr("url");
	var ret=OpenWindow(url+"?id="+getValue(),"450","295",window);
	if("sucess"==ret && ret!=undefined){
		parent.project_work_content.document.location="<%=root %>/address/address.action?groupId=${groupId}" ;
	}
}
//新建
function gotoNew(){
	var url = $("#hrfNewPerson").attr("url");
	var ret=OpenWindow(url,"450","295",window);
	if("sucess"==ret && ret!=undefined){
		parent.document.location="<%=root%>/fileNameRedirectAction.action?toPage=address/address-personal.jsp" ;
	}
}
//删除
function gotoDel(){
	//var name = $(":checked").parent().next().attr("value");
	var url = "<%=root%>/address/address!delete.action";
	var id=getValue();
	if(id==null|id==""){
		alert("请选择要删除的记录。");
	}else{
		if(confirm("确定要删除吗？")){
		$.ajax({
			type:"post",
			url:url,
			data:{id:getValue()},
			success:function(data){
				parent.document.location="<%=root%>/fileNameRedirectAction.action?toPage=address/address-personal.jsp" ;
			},
			error:function(){
				alert("对不起，操作出错。");
			}
		});
		}
	}
}
//写邮件
function gotoWriterMail(){
	//验证是否勾选
	if(getValue() == ""){
		alert("请选择要写邮件的联系人。");
		return ;
	}
	//验证用户是否有邮箱
	var noEmail = new Array();
	var userEmail = "";
	var k=0;
	$(":checked").each(function(i){
	   	if($(this).attr("name")!='checkall'){
			var email = $(this).parent().parent().children().next().next().next().next().next().attr("value");
			var name =  $(this).parent().parent().children().next().next().attr("value");
			if(email!=""){
				userEmail = userEmail + name+"<"+email+">"+",";
			}else{
				noEmail[k] = name;
				k++;
			}
	   	}	
	});

	if(userEmail.length>0){
		userEmail = userEmail.substring(0,userEmail.length-1);
		if(noEmail.length>0){
			var userName = "";
			for(var j=0;j<noEmail.length;j++){
				userName += noEmail[j]+",";
			}
			if(userName.length>0){
				userName = userName.substring(0,userName.length-1);
			}
			alert("提示:以下人员["+userName+"]邮箱未设置，系统发送邮件过程中将忽略这些人员。");
		}
		$.post(
			"<%=root%>/address/address!getUserDefaultEmail.action",
			function(data){
				if("" == data){
					alert("对不起，您未配置默认邮箱，不能发送邮件，请先配置默认邮箱。");
				}else if(data == "error"){
					alert("对不起，获取默认邮箱出错。请与管理员联系。");
				}else{
					OpenWindow('<%=root%>/mymail/mail!otherModel.action?receiver='+encodeURI(encodeURI(userEmail)), '700', '450', window);
				}
			}
		);
	}else{
		alert("提示:您所选择的人员未设置邮箱，无法发送邮件。");
	}
	
}
</script>
</BODY></HTML>
