<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<jsp:directive.page import="com.strongmvc.exception.SystemException"/>
<%@ taglib uri="/tags/web-bigtree" prefix="tree"%>
<%@	include file="/common/include/rootPath.jsp"%>
<%
	List data = (List)request.getAttribute("data");
	if(data == null){
		throw new SystemException("数据为空！");
	}
	String title = (String)request.getAttribute("title");
	if(title == null || "null".equals(title) || "".equals(title)){
		title = request.getParameter("title");
		if(title == null || "null".equals(title) || "".equals(title)){
			title = "组织机构";
		}
	}
	String check = (String)request.getAttribute("check");
	if(check == null || "null".equals(check) || "".equals(check)){
		check = request.getParameter("check");
		if(check == null || "null".equals(check) || "".equals(check)){
			check = "true";
		}
	}
	boolean chk = Boolean.parseBoolean(check);
	String hasSelected = (String)request.getAttribute("id");
	if(hasSelected == null || "null".equals(hasSelected) || "".equals(hasSelected)) {
		hasSelected = request.getParameter("id");
		if(hasSelected == null || "null".equals(hasSelected) || "".equals(hasSelected)) {
			hasSelected = "";
		}
	}
%>
<html>
  <head>
  		<title><%=title%></title>
  		<LINK type=text/css rel=stylesheet href="<%=frameroot%>/css/properties_windows_list.css">
		<LINK href="<%=path%>/common/css/tree.css" type=text/css rel=stylesheet>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<SCRIPT src="<%=root%>/common/bigtree/js/tree.js"></SCRIPT>
		<script type="text/javascript">
			var treeObj = null;
			//初始化
			function init(o){
				var aHasSelected = '<%=hasSelected%>';
				if(aHasSelected != ""){
	                o.aHasSelected = ","+aHasSelected+",";   
				}	
				treeObj = o;
			}
			//得到选中的项
			function doSubmit() {
				var id = "";
				if(treeObj == null) {
					alert("树节点未生成。");
					return ;
				}
				var ids = $("#bigTreeDiv").getTSIds();
				$.each(ids,function(i,item){
					if(item != ''){
						id += item;
					}
				});
				if(id != ""){
					id = id.substring(2);//去掉//根节点 根节点id为1
				}
				$("#privilCode").val(id);
				document.getElementById("form").submit();
			}
			//伪CHECKBOX的单击事件.
			function chkclick(item){
				
			}
		</script>
  </head>
  <base target=_self>
  <body>
  	<form id="form" action="<%=path%>/usermanage/usermanage!saveUserPrivil.action" method="post">
		<input type="hidden" id="privilCode" name="privilCode" value="">
		<input type="hidden" name="userId" id="userId" value="${userId }">
		<input type="hidden" id="extOrgId" name="extOrgId" value="${extOrgId}">
	</form>
 	<DIV id=contentborder cellpadding="0" style="overflow-y:hidden;">
		<div align="center">
		<table width="100%" border="0" cellspacing="0" cellpadding="00">
			<tr>
				<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
								<strong>资源设置</strong>
							</td>
							<td>
								<table border="0" cellpadding="00" cellspacing="0" align="right">
									<tr>
										<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
										<td class="Operation_input" onClick="doSubmit()">&nbsp;确&nbsp;定&nbsp;</td>
										<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
										<td width="5"></td>
										<td width="7"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
										<td class="Operation_input1" onClick="window.close()">&nbsp;取&nbsp;消&nbsp;</td>
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
		</div>
        
        <div style="height:460px; padding-top:10px; overflow-y:auto;" >
        
          <div style="padding-left:20px;">
           
	
		  			<tree:strongbigtree title="<%=title%>" data="<%=data%>"  dealclass="com.strongit.oa.common.tree.TreeImpl" oncheckboxclick="chkclick" check="<%=chk %>"/>
		</div>	
        </div>
        
	</DIV>

  </body>
</html>
