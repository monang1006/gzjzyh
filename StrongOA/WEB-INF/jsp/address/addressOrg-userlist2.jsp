<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML><HEAD><TITLE>系统通讯录-用户列表</TITLE>
<%@include file="/common/include/meta.jsp" %>
<link href="<%=path%>/common/css/strongitmenu.css" type="text/css" rel="stylesheet"><!--右键菜单样式 -->
<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css  rel=stylesheet>
<LINK href="<%=frameroot%>/css/search.css" type=text/css  rel=stylesheet>
<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT><!--右键菜单脚本 -->
<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
<script src="<%=path%>/common/js/validate/checkboxvalidate.js" type="text/javascript"></script>
<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
<script src="<%=path%>/common/js/common/search.js" type="text/javascript"></script>
<script src="<%=path%>/oa/js/address/address.js" type="text/javascript"></script>
<script type="text/javascript">
	$(document).ready(function(){
		//搜索
		$("#img_search").click(function(){
			$("form").submit();
		});
		var objSelect = parent.window.document.getElementById("sel_accept");
		var sel = ($(objSelect));
		$("input:checkbox").each(function(){
			$(this).click(function(){
				if($(this).attr("checked")){
					if($(this).attr("name")!="checkall"){
						var td_text = $(this).parent().next().attr("value");//name
						var td_val = $(this).parent().next().next().attr("value");//email
						if(!hasSelect(sel,td_text,td_val)){
							sel.append("<option value="+td_val+">"+td_text+"</option>");
						}
					}else{
						addAllOpt(this);
						return ;
					}
				}
				if(!$(this).attr("checked")){//取消
					if($(this).attr("name")!="checkall"){
					    var td_text = $(this).parent().next().attr("value");//name
					    var td_val = $(this).parent().next().next().attr("value");//email
					    var opt = findSelectOpt(td_text,td_val);
					    if(opt!=null){
					    	objSelect.removeChild(opt);
					    }
					}else{
						deleteAllOpt();
						return;
					}
				}
			});
		});
	
		initCheck();
		//获取选中的元素
		function findSelectOpt(txt,val){
			var opt = null;
			sel.find("option").each(function(){
				if(this.text == txt && this.value == val){
					opt = this;
				}
			});
			return opt;
		}
		//删除所有元素
		function deleteAllOpt(){
			sel.find("option").each(function(){
			 	sel.get(0).removeChild(this);
			});
			/*for(var i=0;i<objSelect.options.length;i++){
				objSelect.removeChild(objSelect.options[i]);
			}*/
		}
		//添加所有元素
		function addAllOpt(all){
			$("input:checkbox:checked").each(function(){
				if(this!=all){
					var td_text = $(this).parent().next().attr("value");//name
					var td_val = $(this).parent().next().next().attr("value");//email
					if(!hasSelect(sel,td_text,td_val)){
						sel.append("<option value="+td_val+">"+td_text+"</option>");
					}
				}
			});
		}
		
	});
	
	//父窗口调用全选项
     function allSelect(sel_accept){
	              $("input:checkbox").each(function(){
	              if($(this).attr("name")!="checkall"){
					var td_text = $(this).parent().next().attr("value");//name
					var td_val = $(this).parent().next().next().attr("value");//email
					if(!hasSelect(sel_accept,td_text,td_val)){
						//sel.append("<option value="+td_val+">"+td_text+"</option>");
						sel_accept.append("<option value="+td_val+">"+td_text+"</option>");
					}
					}
			});
	 }
		//父窗口调用删除所有元素
		function deleteAllSelect(sel_accept){
			sel_accept.find("option").each(function(){
			 	sel_accept.get(0).removeChild(this);
			});
		}
		//判断是否已经选择
		function hasSelect(sel,txt,val){
			var isExist = false;
			sel.find("option").each(function(){
				if(this.text == txt && this.value == val){
					isExist = true;
				}
			});
			return isExist;
		}
	//初始化
	function initCheck(){
		var sel = parent.window.document.getElementById("sel_accept");
		$("input:checkbox").each(function(){
			var name = $(this).parent().next().attr("value");
			var email = $(this).parent().next().next().attr("value");
			if (sel.length != 0) {
                 for(var j=0; j<sel.options.length; j++){
                      if (email == sel.options[j].value && name == sel.options[j].text) {
                          $(this).attr("checked",true);
                      }
                 }
             }    
		});
	}
</script>
</HEAD>
<BODY class=contentbodymargin oncontextmenu="return false;"  >
<script type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
<label id="l_actionMessage" style="display: none;"><s:actionmessage/></label>
<DIV id=contentborder align=center>
  <table width="100%" border="0" cellspacing="0" cellpadding="00">
  <tr>
    <td height="100%">
    <table width="100%" border="0" cellspacing="0" cellpadding="00">
	<s:form id="myTableForm" action="/address/addressOrg!orguserlist2.action">
		 <input id="orgName" type="hidden" name="orgName" value="${orgName }"/><!-- 用于将文件名传到后台然后传回此页面显示在<label> -->
		 <s:hidden name="orgId"></s:hidden>
	     <webflex:flexTable name="myTable" width="500px" height="370px" wholeCss="table1" property="fileId" 
	     isCanDrag="true" isCanFixUpCol="true" clickColor="#A9B2CA" 
	     footShow="showCheck" getValueType="getValueByProperty" 
	     collection="${page.result}" page="${page}">
	     <table width="100%" border="0" cellpadding="0" cellspacing="1" class="table1">
		 <tr>
          <td width="40px" align="center"  class="biao_bg1"><img style="cursor: hand;" id="img_search"  title="单击搜索" src="<%=root%>/images/ico/sousuo.gif" width="15" height="15"></td>
          <td width="69%" align="center"  class="biao_bg1"><input id="userName" name="userName" type="text" class="search" value="${userName }" title="输入姓名" onkeyup="this.value=this.value.replace(/\s/,'')" onaftepaste="this.value=this.value.replace(/\s/,'')"></td>
          <td class="biao_bg1">&nbsp;</td>
         </tr>
		 </table>
		<webflex:flexCheckBoxCol caption="选择" property="userId" 
			showValue="name" width="40px" isCheckAll="true" isCanDrag="false"
			isCanSort="userName"></webflex:flexCheckBoxCol>
		<webflex:flexTextCol caption="姓名" property="userName" 
			showValue="userName" showsize="50"  width="70%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
		<webflex:flexTextCol caption="Email" property="userEmail"
			showValue="userEmail" width="30%" showsize="50" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>	
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

	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}


</script>
</BODY></HTML>
