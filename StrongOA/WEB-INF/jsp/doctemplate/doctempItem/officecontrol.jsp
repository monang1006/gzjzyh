<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp" %>
<%@include file="/common/OfficeControl/version.jsp"%>
<%@ page import="java.util.*"%>
<HTML>
	<HEAD>
		<TITLE>操作内容</TITLE>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<script src="<%=path%>/common/OfficeControl/officecontrol.js"
			type="text/javascript"></script>
		<script type="text/javascript">
			 //打开文档
      function openFromURL() {
          var doctemplateId=window.parent.document.getElementById("doctemplateId").value;
          var TANGER_OCX_OBJ = document.getElementById("TANGER_OCX_OBJ");
          TANGER_OCX_OBJ.OpenFromURL("<%=root%>/doctemplate/doctempItem/docTempItem!opendoc.action?doctemplateId="+doctemplateId);
      }            	
      
      //插入模板
      function TANGER_OCX_AddTemplateFromURL(){
		  var TANGER_OCX_OBJ = document.getElementById("TANGER_OCX_OBJ");
		  //待实现url
		  var ReturnStr=OpenWindow("<%=root%>/doctemplate/doctempType/docTempType!officetree.action", 
                                   "200", "250", window);
          if(ReturnStr!=null){
          	TANGER_OCX_OBJ.AddTemplateFromURL("<%=root%>/doctemplate/doctempItem/docTempItem!opendoc.action?doctemplateId="+ReturnStr,true); 
          }
      }
      
      //插入套红
      function TANGER_OCX_AddDocRedFromURL(){
      	  var TANGER_OCX_OBJ = document.getElementById("TANGER_OCX_OBJ");
      	  //待实现url
		  var ReturnStr=OpenWindow("<%=root%>/docredtemplate/docredtype/docRedType!officetree.action", 
                                   "200", "250", window);
          if(ReturnStr!=null){
          	TANGER_OCX_OBJ.AddTemplateFromURL("<%=root%>/docredtemplate/docreditem/docRedItem!opendoc.action?redstempId="+ReturnStr,true);
          }                         
      }
      
      //从服务器插入印章
      function TANGER_OCX_AddSignFromURL(){
      	  var TANGER_OCX_OBJ = document.getElementById("TANGER_OCX_OBJ");
      	  //待实现url
      	  var ReturnStr=OpenWindow("<%=root%>", 
                                   "200", "300", window);
      }    
		</script>
	</HEAD>
	<BODY style="margin:0; padding:0;" onload="openFromURL()">
		<table width="100%" height="100%">
			<tr height="100%" valign="top">
				<td valign="top" width="10%" height="100%">
					<table width="100%" align="center" border="0" cellpadding="0"
						cellspacing="1" class="table1">
						<tr>
							<td nowrap align="center" class="biao_bg1">
								文件操作
							</td>
						</tr>
						<tr onclick="TANGER_OCX_ShowDialog(1)"
							style="cursor: pointer; line-height: 20px;">
							<td nowrap class="td1">
								打开本地文件
							</td>
						</tr>
						<tr onclick="TANGER_OCX_ShowDialog(5);"
							style="cursor: pointer; line-height: 20px;">
							<td nowrap class="td1">
								设置页面布局
							</td>
						</tr>
						<tr onclick="TANGER_OCX_ShowDialog(6);"
							style="cursor: pointer; line-height: 20px;">
							<td nowrap class="td1">
								设置文件属性
							</td>
						</tr>
						<tr onclick="TANGER_OCX_PrintDoc(true);"
							style="cursor: pointer; line-height: 20px;">
							<td nowrap class="td1">
								打印控制
							</td>
						</tr>
						<tr onclick="saveDoc();"
							style="cursor: pointer; line-height: 20px;" title="保存为草稿">
							<td nowrap class="td1">
								<img src="<%=root%>/images/ico/baocun.gif" width="14"
									height="14" alt="">
								保存至服务器
							</td>
						</tr>
						<tr onclick="TANGER_OCX_ShowDialog(2);"
							style="cursor: pointer; line-height: 20px;">
							<td nowrap class="td1">
								保存到本地
							</td>
						</tr>
						<tr onclick="closeDoc();"
							style="cursor: pointer; line-height: 20px;">
							<td nowrap class="td1">
								<img src="<%=root%>/images/ico/guanbi.gif" width="14"
									height="14" alt="">
								关闭
							</td>
						</tr>
						<tr>
							<td nowrap align="center" class="biao_bg1">
								文件编辑
							</td>
						</tr>
						<tr onclick="TANGER_OCX_SetMarkModify(true);"
							style="cursor: pointer; line-height: 20px;">
							<td nowrap class="td1">
								保留痕迹
							</td>
						</tr>
						<tr onclick="TANGER_OCX_SetMarkModify(false);"
							style="cursor: pointer; line-height: 20px;">
							<td nowrap class="td1">
								不留痕迹
							</td>
						</tr>
						<tr onclick="TANGER_OCX_ShowRevisions(true);"
							style="cursor: pointer; line-height: 20px;">
							<td nowrap class="td1">
								显示痕迹
							</td>
						</tr>
						<tr onclick="TANGER_OCX_ShowRevisions(false);"
							style="cursor: pointer; line-height: 20px;">
							<td nowrap class="td1">
								隐藏痕迹
							</td>
						</tr>
						<tr onclick="TANGER_OCX_AddPicFromLocal(false);"
							style="cursor: pointer; line-height: 20px;">
							<td nowrap class="td1">
								插入图片
							</td>
						</tr>
						<tr onclick="TANGER_OCX_AddTemplateFromURL();"
							style="cursor: pointer; line-height: 20px;">
							<td nowrap class="td1">
								插入模板
							</td>
						</tr>
						<tr onclick="TANGER_OCX_AddDocRedFromURL();"
							style="cursor: pointer; line-height: 20px;">
							<td nowrap class="td1">
								插入套红
							</td>
						</tr>
						<tr>
							<td nowrap align="center" class="biao_bg1">
								电子认证
							</td>
						</tr>
						<tr onclick="TANGER_OCX_DoHandSign2();"
							style="cursor: pointer; line-height: 20px;">
							<td nowrap class="td1">
								全屏手写签名
							</td>
						</tr>
						<tr onclick="TANGER_OCX_DoHandDraw2();"
							style="cursor: pointer; line-height: 20px;">
							<td nowrap class="td1">
								全屏手工绘图
							</td>
						</tr>
						<tr onclick="TANGER_OCX_DoCheckSign();"
							style="cursor: pointer; line-height: 20px;">
							<td nowrap class="td1">
								签名验证信息
							</td>
						</tr>
						<tr onclick="TANGER_OCX_AddSignFromLocal();"
							style="cursor: pointer; line-height: 20px;">
							<td nowrap class="td1">
								加盖电子印章
							</td>
						</tr>
					</table>
				</td>
				<td colspan="3" valign="top" height="100%" >
					<%--<object id="TANGER_OCX_OBJ"
						classid="clsid:C9BC4DFF-4248-4a3c-8A49-63A7D317F404"
						codebase="<%=root%>/common/OfficeControl/OfficeControl.cab#Version=5,0,1,8"
						width="100%" height="850">
						<param name="ProductCaption" value="思创数码科技股份有限公司">
						<param name="ProductKey" value="B339688E6F68EAC253B323D8016C169362B3E12C">
						<param name="BorderStyle" value="1">
						<param name="TitlebarColor" value="42768">
						<param name="TitlebarTextColor" value="0">
						<param name="TitleBar" value="false">
						<param name="MenuBar" value="false">
						<param name="Toolbars" value="true">
						<param name="IsResetToolbarsOnOpen" value="true">
						<param name="IsUseUTF8URL" value="true">
						<param name="IsUseUTF8Data" value="true">
						<span style="color: red">不能装载文档控件。请在检查浏览器的选项中检查浏览器的安全设置。</span>
					</object>
					--%>
					<script type="text/javascript">
						document.write(OfficeTabContent);
					</script>
				</td>
			</tr>
		</table>
	</BODY>
</HTML>
