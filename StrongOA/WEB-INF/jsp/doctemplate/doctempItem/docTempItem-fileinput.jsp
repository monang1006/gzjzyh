<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>公文模板上传</title>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<LINK href="<%=frameroot%>/css/properties_windows_add.css" type=text/css
			rel=stylesheet>
		<LINK href="<%=frameroot%>/css/search.css" type=text/css
			rel=stylesheet>
		<script language='javascript'
			src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/upload/jquery.MultiFile.js" type="text/javascript"></script>
		<script type="text/javascript" language="javascript"
			src="<%=jsroot%>/upload/jquery.MultiFile.js"></script>
		
			
			<script type="text/javascript">
			
			function onSub(){
				var file=document.getElementsByName("file");
				var msg1='true';
			  if($("#file").val()=="" || $("#file").val() == null){
			     alert("请选择模版文档。");
			     return;
			   }
			  
			  if(0!=file.length-1){
            	 	for(var i=0;i<file.length-1;i++){
            	 		 var file1=file[i].value.split("\\");
            	 		 var file2=file1[file1.length-1].split(".");
            	 		 //alert(file2);
            	 		 if(file2[0].length>25){
            	 			alert("文档的标题长度不能超过25个字。");
    						return; 
            	 		 }
						}
					}
                $("#doctempForm").submit();
					
				}
			</script>
			</head>
  
	<base target="_self"/>
  <body class=contentbodymargin >
		<DIV id=contentborder align=center>
           <s:form id="doctempForm" name="doctempForm" 	enctype="multipart/form-data" theme="simple" action="/doctemplate/doctempItem/docTempItem!fileSave.action" method="post">
		 <input type="hidden" name="docgroupId" id="docgroupId" value="${docgroupId}"/>
			<table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
				<tr>
				<td colspan="3" class="table_headtd">
							
						<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
								<strong>公文模板上传</strong>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
					                <tr>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onclick="onSub();">&nbsp;保&nbsp;存&nbsp;</td>
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
					</td>
				</tr>				
							<table width="100%" height="10%" border="0" cellpadding="0" cellspacing="1" align="center" class="table1">
								
								<tr>
								<td width="25%" height="21" class="biao_bg1" align="right">
									<span>	上传模板：&nbsp;</span>
									</td>
									<td class="td1"  align="left">
										<s:if test="type!=null&&type.equals(\"1\")">
											<input type="file" id="file" name="file" size="50" onkeydown="return false;" class="multi" accept="txt"/>
											
										</s:if>
										<s:else>
											<input type="file" id="file" name="file" size="50" onkeydown="return false;" class="multi" accept="doc/docx"/>
										</s:else>
                                	</td>
								</tr>
								<tr>
									<td></td>
									<td align="left">
										<s:if test="type!=null&&type.equals(\"1\")">
											<span class="wz"><font color="#999999">当前为编辑器模板，只能上传".txt"文件</span></span>
										</s:if>
										<s:else>
											<span class="wz"><font color="#999999">当前为word控件模板，只能上传".doc或者.docx"文件</span></span>
										</s:else>
									</td>
								</tr>
							</table>
						
					</td>
				</tr>
			</table>
						</s:form>
		</DIV>
  </body>
</html>
