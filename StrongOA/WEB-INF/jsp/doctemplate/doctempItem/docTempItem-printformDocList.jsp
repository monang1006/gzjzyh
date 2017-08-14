<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>选择打印模板</title>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet">
		<LINK href="<%=frameroot%>/css/properties_windows_list.css" type=text/css rel=stylesheet>
		<LINK href="<%=frameroot%>/css/search.css" type=text/css rel=stylesheet>
		<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/search.js" type="text/javascript"></script>

		<SCRIPT LANGUAGE="JavaScript" src="<%=path%>/common/js/commontab/service.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></SCRIPT>
		<script type="text/javascript">
			function showLogo(type,id,IsHasImage){
				var path=document.getElementById("path").value;	
				var url=path+"view.jpg";
				var	imageUrl= "<img src='"+url+"' width='100px' height='70px' style='border:1px solid #404040;'>";
				if(type!=null&&type!=""&&type!="null"&&IsHasImage!=null&&IsHasImage!=""&&IsHasImage=="1"){
					
			    	url=path+id+"."+type;
					imageUrl= "<img src='"+url+"' width='100px' height='70px' style='border:1px solid #404040;'>";  								
				}
				
				return imageUrl;
			}
			//将模板内容放入文本
			function setContent(templateId){
				$.post("<%=path%>/doctemplate/doctempItem/docTempItem!opentxt.action",
      				{"doctemplateId":templateId},
      				function (data){
      						window.parent.SelectTemplate(data); 

      			}); 
      						    		
			}
			
			
			function selectTemplate(value){
				returnValue=value;
				window.close();
			}
		</script>
	</head>
	<body class=contentbodymargin oncontextmenu="return false;">
		<DIV id=contentborder align=center>
			<s:hidden id="path" name="path"></s:hidden>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td colspan="3" class="table_headtd">
						<table width="100%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td class="table_headtd_img" >
									<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
								</td>
								<td height="40" align="left">
									<strong>双击选择模板</strong>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td height="100%" align="center">
						<s:if test="list.size>0">
							<webflex:flexTable name="myTable" width="100%" height="370px"
								wholeCss="table1" property="doctemplateId" isCanDrag="true"
								isCanFixUpCol="true" showSearch="false" clickColor="#A9B2CA"
								footShow="111" getValueType="getValueByProperty" ondblclick="selectTemplate(this.value);"
								collection='${list}'>
								<webflex:flexTextCol caption="模板名称" property="doctemplateTitle"
									showValue="doctemplateTitle" width="50%" isCanDrag="true"
									showsize="50" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="模板简介" property="doctemplateRemark"
									showValue="doctemplateRemark" width="50%" isCanDrag="true"
									showsize="50" isCanSort="true"></webflex:flexTextCol>
							</webflex:flexTable>
						</s:if>
						<s:else>
							<br>
							<br>
							<br>
							<br>
							暂时没有模板！
						</s:else>
					</td>
				</tr>
			</table>
		</DIV>
		<script>
		var sMenu = new Menu();
		function initMenuT(){
			sMenu.registerToDoc(sMenu);
			var item = null;
			sMenu.addShowType("ChangeWidthTable");
		    registerMenu(sMenu);
		}
		</script>
	</body>
</html>
