<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-tree" prefix="tree"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>${privilsetTypeName}选择树</TITLE>
		<%@include file="/common/include/meta.jsp" %>
		<link href="<%=frameroot%>/css/properties_windows_add.css" type=text/css rel=stylesheet>
		<LINK href="<%=path%>/common/css/tree.css" type=text/css rel=stylesheet>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<SCRIPT src="<%=root%>/common/js/mztree_check/mztreeview_check.js"></SCRIPT>
		<script type="text/javascript">
			var imageRootPath='<%=path%>/common/frame';
			var ret = "";
			function doSubmit(){
				$(":checked").each(function(){
					ret = $(this).val()+"，" + $(this).next().text();;
				});			
				if(ret == ""){
					alert("请选择一个的${privilsetTypeName}！");
					return ;
				}
				window.returnValue = ret;
				window.close();
			}
		</script>
	</HEAD>
	<BODY >
		
		 <DIV cellpadding="0" style="overflow:hidden;">
          <div align="center">
		<table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
				<tr>
					<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
							   <strong>选择${privilsetTypeName}</strong>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
					                <tr>
					                   <td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onclick="doSubmit();">&nbsp;确&nbsp;定&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
				                  	   <td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
					                 	<td class="Operation_input1" onclick="window.close();">&nbsp;取&nbsp;消&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
				                  		<td width="6"></td>
					                </tr>
					            </table>
							 </td>
              </tr>
            </table>
            <div style="height:458px;  overflow-y:auto; overflow-x:hidden; widows:100%">
              <div style="padding:0 20px; text-align: left;" >
                <tree:strongtree title="${root}"  check="true" chooseType="signle" dealclass="com.strongit.oa.report.ChooseTreeDeal" data="${list}" target="project_work_content"  />	
              </div>
            </div>
          </div>
        </DIV>
		</BODY>
</HTML>
