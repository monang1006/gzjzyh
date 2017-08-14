<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!--  引用标签处-->
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<!--  引用公共文件,以下两个文件是建议包含-->
<%@include file="/common/include/rootPath.jsp" %>
<% 
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragrma","no-cache");
	response.setDateHeader("Expires",0);
%>
<HTML>
	<HEAD>
	<%@include file="/common/include/meta.jsp" %>
	<!--  引用公共样式文件,建议所有样式都以文件方式定义在jsp文件外部,通常定义在WebRoot目录下的CSS文件夹下-->
	<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet"><!--右键菜单样式 -->
	<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
	<LINK href="<%=frameroot%>/css/search.css" type=text/css  rel=stylesheet>
	<!--  引用公共及自定义js文件,建议js都以文件方式定义在jsp文件外部,通常定义在WebRoot目录下的js文件夹下-->
	<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT><!--右键菜单脚本 -->
	<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
	<script language='javascript' src='<%=path%>/common/js/jquery/jquery-1.2.6.js'></script>
	<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
	<script language='javascript' src='<%=path%>/common/js/common/search.js'></script>
		
		<TITLE>公文分发列表</TITLE>
		
		<style type="text/css">
		<!--
		body {
			width:100%;
			margin-left: 0px;
			margin-top: 0px;
			margin-right: 0px;
			margin-bottom: 0px;
			height: 100%
		}
		</style>
		<script type="text/javascript">
		
			//回收公文
			function recycleDoc(){
				var id = getValue();
				if(""==id|null==id){
					alert("请选择一条公文!");
					return;
				}
				
				

				
				$.ajax({
					type:"post",
					dataType:"text",
					data:{ids:id},
					url:"<%=root%>/sends/docSend!batchRecycle.action",
					success:function(data){
						if(data == "-1"){
							alert("只能选择待收状态的公文回收！");
						}
						else{
							alert("回收成功！");
							location = "<%=root%>/sends/docSend!sendslist.action?docId=${docId}";
						}	
					},
					error:function(data){
						alert("对不起，操作异常"+data);
					}
				});

				
<%--			$.post(scriptroot + "/sends/docSend!isHasRecycleDocByOrg.action",--%>
<%--	    			  {"docSendId":id},--%>
<%--	    			  function(ret){--%>
<%--	    				if(ret == "1"){--%>
<%--							alert("该单位公文已回收！");--%>
<%--	    				}else{--%>
<%--	    					--%>
<%--							if(confirm("您确定要回收该单位的公文?")){--%>
<%--								$.post(scriptroot + "/sends/docSend!input.action",--%>
<%--				    			  {"docSendId":id,"model.recvState":"3"},--%>
<%--				    			  function(ret){--%>
<%--				    				if(ret == "0"){--%>
<%--										location = "<%=root%>/sends/docSend!sendslist.action?docId=${docId}";--%>
<%--				    				}else if(ret == "-1"){--%>
<%--				    					alert("对不起，操作失败，请与管理员联系。");--%>
<%--				    					return ;--%>
<%--				    				}else{--%>
<%--				    					alert(ret);--%>
<%--				    					return ;--%>
<%--				    				}--%>
<%--				    			  });--%>
<%--							}--%>
<%--	    				}--%>
<%--	    			  });--%>
			}
			
       
			
			//设置打印份数
			function setPrintNum(){
				var id = getValue();
				var time = new Date();
				
				
				if(""==id|null==id){
					alert("请选择一个分发单位!");
					return;
				}
				
				$.get("<%=root%>/sends/docSend!verifyState3.action?ids="+id+"&time="+time.getTime(), function(response){
					if(response == "-1"){
						alert("只能选择待收状态的公文修改打印份数！！");
						return false;
					}
					else{
						var validate = false;
						var pnum,num;
						(function(){
						 	pnum = $("#chkButton"+id).parent().next().next().next().next().next().text();
							
							 if (typeof(setPnum) != "undefined") {
						       	 pnum = setPnum(pnum);
						        }
							
							num = prompt("请输入打印份数：",pnum);
							if(""==num||null==num){
								return;
							}
							var defPrintNum= /^((1|2|3|4|5|6|7|8|9)\d{0,3})$/;
							if(!defPrintNum.test(num)){
								alert("输入的默认打印份数格式不正确，要求:\n1、只能为数字；\n2、不以0开头；\n3、不能超过四位数字；");
								arguments.callee();
							}
							else
							{
								validate = true;
							}
						})();
		    		  if(!validate)
		    		 	 return;
		   			  $.ajax({
						type:"post",
						dataType:"text",
						data:{"ids":id,"defPrint":num},
						url:"<%=root%>/sends/docSend!batchSetPrintNum.action",
						success:function(ret){
							if(ret == "-1"){
								alert("不是全为待收状态，不能修改打印份数！！");
		    				}else{
		    					alert("设置成功！");	
								location = "<%=root%>/sends/docSend!sendslist.action?docId=${docId}";
							}
						},
						error:function(data){
							alert("对不起，操作异常"+data);
						}
					  });
					}
				});
				
				
			}
			
			$(document).ready(function(){
				$("#img_sousuo").click(function(){
					$("form").submit();
				});
			}); 
			function OpenWindows(danwei,title,docRecvRemark,liyous,qita)
			{
				var laiwenDW = encodeURI(danwei);
				var mytitle = encodeURI(title);
				var liyou2 = encodeURI(liyous);
				var docRecvRemark2=encodeURI(docRecvRemark);
				var qita2 = encodeURI(qita);
				var urls = "<%=path%>/fileNameRedirectAction.action?toPage=sends/tuiwendan.jsp?remark="+docRecvRemark2+"&liyou="+liyou2+"&laiwenDW="+laiwenDW+"&title="+mytitle+"&qita="+qita2;
				OpenWindow(urls,window,'help:no;status:no;scroll:auto;dialogWidth:600px; dialogHeight:760px');
			}
			function showLiyou(danwei,title,docRecvRemark,liyous,qita){
				if(docRecvRemark == 'null')
				{
					docRecvRemark= "";
				}
				if(qita == 'null')
				{
					qita= "";
				}
				if(liyous != 'null')
				{
					
					
					return "<a href=\"#\" onclick=\"javascript:OpenWindows('"+danwei+"','"+title+"','"+docRecvRemark+"','"+liyous+"','"+qita+"');\"  >查看退文单</a>";
					//alert(a);
					//return a;
				}
				
				return "<font>"+docRecvRemark+"</font>";
			}
		</script>
	</HEAD>
	<BODY class=contentbodymargin oncontextmenu="return false;" onload="initMenuT()">
	<script type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
	<input type="hidden" id="inputType" name="inputType" value="${inputType}">
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
          <td height="40" class="tabletitle">
            <table width="100%" border="0" align="right" cellpadding="0" cellspacing="0">
              <tr>
                <td>&nbsp;
                </td>
                <td width="30%">
                 <img src="<%=frameroot%>/images/ico.gif" width="9" height="9"
                    alt="">&nbsp;
                    下发单位以及分发信息
                </td>
                 <td width="70%"><table align="right"><tr>
	                 <td>
	                  <a class="Operation" href="javascript:recycleDoc();"><img
	                                    src="<%=frameroot%>/images/fankui.gif" width="15"
	                                    height="15" class="img_s">回收</a>
	                </td>
	                <td width="5"> </td>
	                 <td>
	                  <a class="Operation" href="javascript:setPrintNum();"><img
	                                    src="<%=frameroot%>/images/bianji.gif" width="15"
	                                    height="15" class="img_s">设置打印份数</a>
	                </td>
	                <td width="5"> </td>
	                </tr></table></td>
              </tr>
            </table>
          </td>
        </tr>
				<tr>
					<td>
					<s:form id="myTableForm" action="docSend!sendslist.action" method="post">
					<input type="hidden" name="docId" id="docId" value="${docId}">
					<input type="hidden" name="title" id="title" value="${title}">
					<input type="hidden" name="danwei" id="danwei" value="${danwei }">
						<webflex:flexTable name="myTable" width="100%" height="365px" wholeCss="table1" property="afficheId" isCanDrag="false"  isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck" getValueType="getValueByProperty" collection="${page.result}" page="${page}">
					        <table width="100%" border="0" cellpadding="0" cellspacing="1" class="table1">
							     <tr>
							       <td width="40px" align="center"  class="biao_bg1"  id="img_sousuo" style="cursor: hand;"><img  src="<%=frameroot%>/images/perspective_leftside/sousuo.gif" width="17" height="16"></td>
							       <td width="8%" align="center" class="biao_bg1"><s:select name="model.recvState" list="#{'':'全部','0':'待收','1':'已收','2':'已拒收','3':'已回收'}" listKey="key" listValue="value" style="width:100%" onchange='$("#img_sousuo").click();'/></td>
							       <td width="14%" align="center" class="biao_bg1"><strong:newdate  name="model.docRecvTime" id="docRecvTime" width="100%" skin="whyGreen" isicon="true"  classtyle="search" title="选择日期"/></td>
							       <td width="26%"  class="biao_bg1"><input name="model.deptName" id="deptName" type="text" style="width=100%" class="search" title="单位名称" value="${model.deptName }"></td>
							       <td width="9%"  class="biao_bg1"><input name="model.docRecvUser" id="docRecvUser" type="text"  style="width=100%" class="search" title="操作用户" value="${model.docRecvUser}"></td>
							       <td width="8%"  class="biao_bg1"><input name="model.docHavePrintSum" id="docHavePrintSum" type="text" style="width=100%" class="search" title="打印份数"  maxLength="4" value="${model.docHavePrintSum }" onkeyup="this.value=this.value.replace(/\D/g,'')"  onafterpaste="this.value=this.value.replace(/\D/g,'')" ></td>
							       <td width="8%"  class="biao_bg1"><input name="model.docHavePrintNum" id="docHavePrintNum" type="text" style="width=100%" class="search" title="已打份数"  maxLength="4" value="${model.docHavePrintNum }" onkeyup="this.value=this.value.replace(/\D/g,'')"  onafterpaste="this.value=this.value.replace(/\D/g,'')" ></td>
							       <td width="23%"  class="biao_bg1"><input name="model.docRecvRemark" id="docRecvRemark" type="text" style="width=100%" class="search" title="签收说明" value="${model.docRecvRemark }" ></td>
							       <td class="biao_bg1">&nbsp;</td>
							     </tr>
							</table> 
							<webflex:flexCheckBoxCol caption="选择" property="senddocId" 
								showValue="deptName" width="3%" isCheckAll="true" isCanDrag="false"
								isCanSort="false"></webflex:flexCheckBoxCol>
							<webflex:flexEnumCol caption="分发状态" mapobj="${typemap}" property="recvState"  align="center" 
								showValue="recvState" width="8%" isCanDrag="true" isCanSort="true"></webflex:flexEnumCol>
							<webflex:flexDateCol caption="操作时间" property="docRecvTime" dateFormat="yyyy-MM-dd hh:mm"
								showValue="docRecvTime" width="14%" isCanDrag="true" isCanSort="true"></webflex:flexDateCol>
							<webflex:flexTextCol caption="单位名称" property="deptName"
								showValue="deptName" width="27%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="操作用户" property="docRecvUser"
								showValue="docRecvUser" width="10%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="打印份数" property="docHavePrintSum"  align="center"
								showValue="docHavePrintSum" width="8%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="已打份数" property="docHavePrintNum"  align="center"
								showValue="docHavePrintNum" width="8%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="说明" property="docRecvRemark" 
								showValue="javascript:showLiyou(${danwei },${title },docRecvRemark,liyous,qita)"  width="22%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
						</webflex:flexTable>
					</s:form>
					</td>
				</tr>
			</table>
		</DIV>
		<script language="javascript">
			var sMenu = new Menu();
			function initMenuT(){
				sMenu.registerToDoc(sMenu);
				var item = null;
				item = new MenuItem("<%=frameroot%>/images/fankui.gif","回收","recycleDoc",3,"ChangeWidthTable","checkOneDis");
				sMenu.addItem(item);
				item = new MenuItem("<%=frameroot%>/images/bianji.gif","设置打印份数","setPrintNum",3,"ChangeWidthTable","checkOneDis");
				sMenu.addItem(item);
				sMenu.addShowType("ChangeWidthTable");
			    registerMenu(sMenu);
			}
		</script>	
	</BODY>
</HTML>
