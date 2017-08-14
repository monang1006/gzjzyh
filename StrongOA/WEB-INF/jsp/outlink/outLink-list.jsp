<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="/tags/web-flex" prefix="webflex"%>
<%@taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>外部链接列表</title>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
			<!-- 列表外部样式引用改变 -->
		  <link href="<%=frameroot%>/css/properties_windows_list.css" type="text/css"
			rel="stylesheet">
			
		
		<link href="<%=frameroot%>/css/search.css" type="text/css"
			rel="stylesheet">
		<script language='javascript'
			src='<%=path%>/common/js/grid/ChangeWidthTable.js'
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
		<script language="javascript" src="<%=path%>/common/js/menu/menu.js"
			type="text/javascript"></script>
		<script language="javascript" src="<%=path%>/common/js/common/windowsadaptive.js"></script>
		<script src="<%=path%>/common/js/common/common.js"
			type="text/javascript"></script>
		<!--<script src="<%=path%>/common/js/common/search.js"
			type="text/javascript"></script>-->
		<style media="screen" type="text/css">


.hand {
	cursor: pointer;
}
</style>
		<script type="text/javascript">
			function add(){
				var reValue = OpenWindow('<%=root%>/outlink/outLink!input.action', '500', '180', window);
				if(reValue=="true"){
					window.location.reload();
				}
			}
			
			function chageone(id,type){
	    		if(id==null||id==""){
	    			if(type=="view"){
	    			  alert("请您选择要查看的记录");
	    			}else{
	    				alert("请您选择要编辑的记录");	
	    			}
	    			return false;
	    		}else{
	    			if(id.indexOf(",")!=-1){
	    				if(type=="view"){
	    					alert("只可以查看一条记录。");
	  	    			}else{
	  	    				alert("只可以编辑一条记录。");	
	  	    			}
	    				
	    				return false;
	    			}else{
	    				return true;
	    			}
	    		}
	    	}
			
			function edit(){
				var id = getValue();
				if(chageone(id,"edit")==true){
					var reValue = OpenWindow('<%=root%>/outlink/outLink!edit.action?id='+id, '550', '180', window);
					if(reValue=="true"){
						window.location.reload();
					}
				}
			}
			
			function del(){
				var id = getValue();
				if(id==null||id==""){
	    			alert("请选择要删除的记录。");
	    			return;
	    		}else{
	    		  if(confirm("确定要删除吗？")){
					$.ajax({
						type:"post",
						dataType:"text",
						url:"<%=root%>/outlink/outLink!delete.action",
						data:"id="+id,
						success:function(msg){
							if(msg=="true"){
								window.location.reload();
							}else{
								alert("操作失败，请您重新删除");
							}
						}
					});
					}
	    		}
			}
			
			function typeName(type){
				if(type=='0'){
					return '桌面链接';
				}else if(type=='1'){
					return '首页链接';
				}else if(type=='2'){
					return '处理类链接';
				}else{
					return '无此链接类型';
				}

			}
			
			function viewUrl(id){
				
				var iWidth=650; //弹出窗口的宽度;
				var iHeight=500; //弹出窗口的高度;
				var iTop = (window.screen.availHeight-30-500)/2; //获得窗口的垂直位置;
				var iLeft = (window.screen.availWidth-10-650)/2; //获得窗口的水平位置;
				window.open(id,'预览链接地址','height='+iHeight+',,innerHeight='+iHeight+',width='+iWidth+',innerWidth='+iWidth+',top='+iTop+',left='+iLeft+',toolbar=yes, menubar=yes, scrollbars=yes, resizable=yes,location=no, status=yes');
				//window.open(id, '预览链接地址', ' toolbar=yes, menubar=yes, scrollbars=yes, resizable=yes,location=yes, status=yes');
			}
			
			function view(){
				var iWidth=650; //弹出窗口的宽度;
				var iHeight=500; //弹出窗口的高度;
				var iTop = (window.screen.availHeight-30-500)/2; //获得窗口的垂直位置;
				var iLeft = (window.screen.availWidth-10-650)/2; //获得窗口的水平位置;
				var id = getValue();
				if(chageone(id,"view")==true){
					window.open($("tr[value='"+id+"']").find("td").eq(3).attr("value"), '预览链接地址', 'height='+iHeight+',,innerHeight='+iHeight+',width='+iWidth+',innerWidth='+iWidth+',top='+iTop+',left='+iLeft+',toolbar=yes, menubar=yes, scrollbars=yes, resizable=yes,location=no, status=yes');
				}
			}
			
			$(document).ready(function(){
			    $("#img_sousuo").click(function(){
					$("form").submit();
			    });
			    //获取焦点
				$("input").focus();
			});
	    </script>
	</head>
	<body class="contentbodymargin" oncontextmenu="return false;"
		onload="initMenuT();">
<!--		<script type="text/javascript"-->
<!--			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>-->
<script type="text/javascript" src="<%=path%>/common/script/My97DatePicker/WdatePicker.js"></script>
		<div id="contentborder" align="center">
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
				   <td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
								<strong>外部链接列表</strong>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
						            <tr>
						            	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="add();"><img src="<%=root%>/images/operationbtn/add.png"/>&nbsp;新&nbsp;建&nbsp;</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
				                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="edit();"><img src="<%=root%>/images/operationbtn/edit.png"/>&nbsp;编&nbsp;辑&nbsp;</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
				                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="view();"><img src="<%=root%>/images/operationbtn/view.png"/>&nbsp;查&nbsp;看&nbsp;</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
				                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="del();"><img src="<%=root%>/images/operationbtn/del.png"/>&nbsp;删&nbsp;除&nbsp;</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
				                  		
				                  		<td width="2%"></td>
					                 	<%--<security:authorize ifAllGranted="001-0003000600020004">
					                 	<td width="50"><a class="Operation" href="javascript:searchNotify();"><img src="<%=root%>/images/ico/sousuo.gif" width="15" height="15" class="img_s">查找</a></td>
					                 	<td width="5"></td>
					                 	</security:authorize>
					                --%></tr>
					            </table>
							</td>
						</tr>
					</table>
					  </td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td height="100%">
						<s:form id="myTableForm" action="/outlink/outLink.action"
							method="post">
							<webflex:flexTable name="myTable" width="100%" height="200px"
								wholeCss="table1" property="outlinkId" isCanDrag="true"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByProperty" collection="${page.result}"
								page="${page}">
								<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table1_search">
							     <tr>
							       <td>
							       		<div style="float: left;">&nbsp;&nbsp;描述：&nbsp;<s:textfield name="des" cssClass="search"  title="请输入外部链接描述"></s:textfield></div>
							       		<div style="float: left;">&nbsp;&nbsp;起始日期：&nbsp;<strong:newdate name="beginDate" id="beginDate"  skin="whyGreen" isicon="true" dateobj="${beginDate}" classtyle="search" title="请输入起始日期" dateform="yyyy-MM-dd"></strong:newdate></div>
							       		<div style="float: left;width:450px;">&nbsp;&nbsp;结束日期：&nbsp;<strong:newdate name="endDate" id="endDate" skin="whyGreen" isicon="true" dateobj="${endDate}" classtyle="search" title="请输入结束日期" dateform="yyyy-MM-dd"></strong:newdate>
							       		&nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo" type="button" /></div>
							       	</td>
							     </tr>
							</table> 
								
								<webflex:flexCheckBoxCol caption="选择" property="outlinkId"
									showValue="outlinkDes" width="5%" isCheckAll="true"
									isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexTextCol caption="描述" property="outlinkDes"
									showValue="outlinkDes" width="25%" isCanDrag="true"
									isCanSort="true" showsize="30"></webflex:flexTextCol>
								<webflex:flexTextCol caption="外部链接地址" property="outlinkAddress"
									showValue="outlinkAddress" onclick="viewUrl(this.value)"
									width="15%" isCanDrag="true" isCanSort="true" align="center"></webflex:flexTextCol>
									<!--请勿删除，“预留接口 不启用”-->
								<%--<webflex:flexTextCol caption="外部链接类" property="outlinkClass"
									showValue="outlinkClass" width="15%" isCanDrag="true"
									isCanSort="true" align="center"></webflex:flexTextCol>--%>
								<webflex:flexTextCol caption="链接类型" property="outlinkType"
									showValue="javascript:typeName(outlinkType)" width="20%"
									isCanDrag="true" isCanDrag="true" isCanSort="true" align="center"></webflex:flexTextCol>
								<webflex:flexDateCol caption="日期" property="outlinkDate"
									showValue="outlinkDate" width="20%" isCanDrag="true"
									isCanSort="true" showsize="20" dateFormat="yyyy-MM-dd HH:mm"></webflex:flexDateCol>
							</webflex:flexTable>
						</s:form>
					</td>
				</tr>
			</table>
		</div>
		<script language="javascript">
	    var sMenu = new Menu();
		function initMenuT(){
			sMenu.registerToDoc(sMenu);
			var item = null;
			item = new MenuItem("<%=root%>/images/operationbtn/add.png","新建","add",1,"ChangeWidthTable","checkMoreDis");
			sMenu.addItem(item);  
			item = new MenuItem("<%=root%>/images/operationbtn/edit.png","编辑","edit",1,"ChangeWidthTable","checkMoreDis");
			sMenu.addItem(item);
			item = new MenuItem("<%=root%>/images/operationbtn/view.png","查看","view",1,"ChangeWidthTable","checkMoreDis");
			sMenu.addItem(item);
			item = new MenuItem("<%=root%>/images/operationbtn/del.png","删除","del",1,"ChangeWidthTable","checkMoreDis");
			sMenu.addItem(item);
			
			sMenu.addShowType("ChangeWidthTable");
			registerMenu(sMenu);
		}
		
	
	  </script>
	</body>
</html>
