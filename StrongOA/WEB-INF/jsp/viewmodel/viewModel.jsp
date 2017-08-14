<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>视图模型管理</title>
		<LINK href="<%=frameroot%>/css/properties_windows_list.css" type=text/css rel=stylesheet>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<script language="javascript"
			src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script src="<%=path%>/common/js/common/search.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/upload/jquery.blockUI.js"
			language="javascript"></script>
		<script language='javascript'
			src='<%=root%>/common/js/grid/ChangeWidthTable.js'></script>
		<script src="<%=path%>/common/js/common/common.js"
			type="text/javascript"></script>
				
		<script src="<%=path%>/common/js/validate/checkboxvalidate.js"
			type="text/javascript"></script>
		<script src="js/work.js" type="text/javascript"></script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
		<!--右键菜单脚本 -->
		<script type="text/javascript">
			function createModel(){
				msg = OpenWindow('<%=root%>/viewmodel/viewModel!input.action', '350', '150', window);
				if(msg == "true"){
					window.location.reload();
				}
			}
			function chageone(id,type){
	    		if(id==null||id==""){
	    			 if(type=="yemian"){
	    				alert("请选择要查看页面模型的记录。"); 
	    			 }else{
	    			   alert("请选择要编辑的记录。");
	    			 }
	    			return false;
	    		}else{
	    			if(id.indexOf(",")!=-1){
	    				if(type=="yemian"){
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
			
			function modifyModel(){
				var id = getValue();
				if(chageone(id,"edit")==true){
					var reValue = OpenWindow('<%=root%>/viewmodel/viewModel!edit.action?id='+id, '350', '150', window);
					if(reValue=="true"){
						window.location.reload();
					}
				}
			}
			
			function chargeIds(id){
				var arr = id.split(",");
				for(i=0;i<arr.length;i++){	
					//alert($("tr[value='"+arr[i]+"']").find("td").eq(4).attr("value"));
					if($("tr[value='"+arr[i]+"']").find("td").eq(4).attr("value")=="1")
						return true;
				}
				return false;
			}
			
		function preview(){				
				var id = getValue();
				if(id==null||id==""){
	    			alert("请选择要页面预览的记录。");
	    			return;
	    		}else{
	    			if(id.indexOf(",")!=-1){
	    				alert("只可以页面预览一条记录。");
	    				return;
	    			}else{
						OpenWindow('<%=root%>/theme/theme!RefreshTop.action?modelId='+id, '1000', '800', window);
	    		}
			  }
			}
			function delModel(){
				var id = getValue();
				if(id==null||id==""){
					alert("请选择要删除的记录。");
				}else if(chargeIds(id)){
					alert("您删除的选项中存在默认页面模型，默认页面模型不允许删除，请您重新选择。");
				}else{
					if(confirm("确定要删除吗？")==true){
						$.ajax({
							type:"post",
							dataType:"text",
							url:"<%=root%>/viewmodel/viewModel!delete.action",
							data:"id="+id,
							success:function(msg){
								if(msg=="true"){
									window.location.reload();
								}else if(msg == "has"){
									alert("您删除的选项中存在默认页面模型，默认页面模型不允许删除，请您重新选择。");
								}else{
									alert("删除失败，请您重新操作。");
								}
							}
						});
					}
				}
			}
			function createPage(){
				alert("生成页面");
			}
			
			function pageModel(){
				var id = getValue();
				if(chageone(id,"yemian")==true){
					window.location = "<%=root%>/viewmodel/viewModelPage.action?modelId="+id;
				}
			}
			
			function chargeDefault(charge){
				if(charge=="1"){
					return "是";
				}else{
					return "否";
				}
			}
			
			function createPrival(){				
				var id = getValue();
				if(id==null||id==""){
	    			alert("请选择要设置的记录。");
	    			return;
	    		}else{
	    			if(id.indexOf(",")!=-1){
	    				alert("只可以设置一条记录。");
	    				return;
	    			}else{
						 $.post('<%=path%>/viewmodel/modelPrival!isHasCreatPage.action',
				             { 'foramulaId':id},
				              function(data){			              
					          //    if(data=="sucess"){
					              	
									var reValue = OpenWindow('<%=root%>/viewmodel/modelPrival!selectPerson.action?foramulaId='+id, '400', '520', window);
									 if(reValue=="suc"){
											window.location.reload();
										}
					          //    }else{
					         //        	alert("当前选择的【"+data+"】还没有生成页面，不能设置权限！");
					          //       	return ;
				             // 		}
				       });
						
	    			}
	    		}
			}
		</script>
	</head>
	<body class="contentbodymargin" oncontextmenu="return false;"
		onload="initMenuT();">
		<div id="contentborder" align="center">
				<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
				<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
								<strong>首页管理-视图模型列表</strong>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
						            <tr>
						            	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="createModel();"><img src="<%=root%>/images/operationbtn/add.png"/>新建模型</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
				                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="modifyModel();"><img src="<%=root%>/images/operationbtn/edit.png"/>编辑模型</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
				                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="delModel();"><img src="<%=root%>/images/operationbtn/del.png"/>删除模型</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
				                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="pageModel();"><img src="<%=root%>/images/operationbtn/Page_template.png"/>页面模板</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
				                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="preview();"><img src="<%=root%>/images/operationbtn/Page_preview.png"/>页面预览</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
				                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="createPrival();"><img src="<%=root%>/images/operationbtn/Permissions_settings.png"/>权限设置</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
				                  		<td width="2%"></td>
					                 </tr>
					            </table>
							</td>
						</tr>
					</table>
				</td>
				
				</tr>
				
				
				<tr>
					<td height="100%">
						<s:form id="myTableForm" action="/viewmodel/viewModel.action"
							method="get">
							<webflex:flexTable name="myTable" width="100%" height="200px"
								wholeCss="table1" property="foramulaId" isCanDrag="true"
								showSearch="false" isCanFixUpCol="true" clickColor="#A9B2CA"
								footShow="showCheck" getValueType="getValueByProperty"
								collection="${page.result}" page="${page}">
								<webflex:flexCheckBoxCol caption="选择" property="foramulaId"
									showValue="foramulaDec" width="5%" isCheckAll="true"
									isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexTextCol caption="视图描述" property="foramulaDec"
									showValue="foramulaDec" width="65%" isCanDrag="true"
									isCanSort="true" showsize="30" align="center"></webflex:flexTextCol>
								<webflex:flexDateCol caption="修改日期" property="foramulaDate"
									showValue="foramulaDate" width="20%" isCanDrag="true"
									dateFormat="yyyy-MM-dd HH:mm:ss" isCanSort="true"
									showsize="100"></webflex:flexDateCol>
								<webflex:flexTextCol caption="是否为默认" property="foramulaDefault"
									showValue="javascript:chargeDefault(foramulaDefault)"
									width="10%" isCanDrag="true" isCanSort="true" align="center"></webflex:flexTextCol>
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
				item = new MenuItem("<%=root%>/images/operationbtn/add.png","新建模型","createModel",1,"ChangeWidthTable","checkMoreDis");
				sMenu.addItem(item);    
				item = new MenuItem("<%=root%>/images/operationbtn/edit.png","编辑模型","modifyModel",1,"ChangeWidthTable","checkOneDis");
				sMenu.addItem(item);
				item = new MenuItem("<%=root%>/images/operationbtn/del.png","删除模型","delModel",1,"ChangeWidthTable","checkOneDis");
				sMenu.addItem(item); 
				item = new MenuItem("<%=root%>/images/operationbtn/Page_template.png","页面模板","pageModel",1,"ChangeWidthTable","checkOneDis");
				sMenu.addItem(item); 
				//item = new MenuItem("<%=root%>/images/ico/tianjia.gif","生成模型页面","createPage",1,"ChangeWidthTable","checkOneDis");
				//sMenu.addItem(item); 
				item = new MenuItem("<%=root%>/images/operationbtn/Page_preview.png","页面预览","preview",1,"ChangeWidthTable","checkOneDis");
				sMenu.addItem(item); 
				item = new MenuItem("<%=root%>/images/operationbtn/Permissions_settings.png","权限设置","createPrival",1,"ChangeWidthTable","checkOneDis");
				sMenu.addItem(item); 
				sMenu.addShowType("ChangeWidthTable");
				registerMenu(sMenu);
			}
	  </script>
	</body>
</html>
