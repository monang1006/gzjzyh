<%@ page language="java" pageEncoding="utf-8"%>
<%@include file="/common/include/rootPath.jsp" %>
<html>
  <head>
    <title>导出CSV、vCard文件</title>
    <%@include file="/common/include/meta.jsp" %>
	<LINK href="<%=frameroot%>/css/properties_windows_add.css" type=text/css rel=stylesheet>
	<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
	<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
	<script src="<%=path%>/oa/js/address/address.js" type="text/javascript"></script>
  	<script src="<%=path%>/common/js/common/btn.js" type="text/javascript"></script>
  </head>
  <body style="background-color:#ffffff">
     <table border="0" width="100%" cellspacing="0" cellpadding="3" >
		<tr>
			<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
							<strong>联系组【${groupName }】导出</strong>
							</td>
					</tr>
				</table>
			</td>
		</tr>
  </table>
  
  <div id="exportcontent" style="height: 60%;background-color:#ffffff" align="center">
  	<span class="wz">请选择导出文件格式：
  <br><br>
  	<!-- &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  	 	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  	<input id="path" type="file" style="width: 50%;"/><br>
  	<span><br>
  		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  		<input type="button" id="btnPrew" style="display: none;"  class="input_bg" icoPath="<%=root%>/images/ico/queding.gif" value="  浏览..."/>&nbsp;&nbsp;
  	</span>-->
     <input id="radio1" value="csv" name="select_role" type="radio" checked="checked"/>CSV&nbsp;&nbsp;&nbsp;&nbsp;
     <!--<input id="radio2" value="vard" name="select_role" type="radio"/>VCARD--></span>
  </div>
	<div id="nextContent"  style="display: none;" style='background:#fff; overflow:auto;height:60%;border-left:1px solid #404040;border-top:1px solid #404040;border-bottom:1px solid #d4d0c8;border-right:1px solid #d4d0c8;'>
	<input name='header' value='姓名' checked="checked" type='checkbox' id='cb1'><span class="wz">姓名</span><br>
	<input name='header' value='职务' type='checkbox' id='cb2'><span class="wz">职务</span><br>
	<input name='header' value='电子邮件' checked="checked" type='checkbox' id='cb3'><span class="wz">Email</span><br>
	<input name='header' value='手机1' checked="checked" type='checkbox' id='cb4'><span class="wz">手机1</span><br>
	<input name='header' value='手机2' type='checkbox' id='cb5'><span class="wz">手机2</span><br>
	
	<%--<input name='header' value='性别' type='checkbox' id='cb6'><span class="wz">性别</span><br>
	--%><input name='header' value='生日' type='checkbox' id='cb7'><span class="wz">生日</span><br>
	<input name='header' value='QQ' type='checkbox' id='cb8'><span class="wz">QQ</span><br>
	<input name='header' value='MSN' type='checkbox' id='cb9'><span class="wz">MSN</span><br>
	<input name='header' value='主页' type='checkbox' id='cb10'><span class="wz">主页</span><br>
	<input name='header' value='爱好' type='checkbox' id='cb11'><span class="wz">爱好</span><br>
	
	<input name='header' value='国家' type='checkbox' id='cb12'><span class="wz">国家</span><br>
	<input name='header' value='省' type='checkbox' id='cb14'><span class="wz">省份</span><br>
	<input name='header' value='城市' type='checkbox' id='cb16'><span class="wz">城市</span><br>
	<input name='header' value='家庭电话1' type='checkbox' id='cb13'><span class="wz">家庭电话1</span><br>
	<input name='header' value='家庭电话2' type='checkbox' id='cb15'><span class="wz">家庭电话2</span><br>
	
	<input name='header' value='家庭地址' type='checkbox' id='cb18'><span class="wz">家庭地址</span><br>
	<input name='header' value='传真' type='checkbox' id='cb17'><span class="wz">传真</span><br>
	<input name='header' value='公司' type='checkbox' id='cb19'><span class="wz">公司</span><br>
	<input name='header' value='职位' type='checkbox' id='cb20'><span class="wz">职位</span><br>
	
	<input name='header' value='部门' type='checkbox' id='cb21'><span class="wz">部门</span><br>
	<input name='header' value='公司电话1' type='checkbox' id='cb22'><span class="wz">公司电话1</span><br>
	<input name='header' value='公司电话2' type='checkbox' id='cb23'><span class="wz">公司电话2</span><br>
	<input name='header' value='邮编' type='checkbox' id='cb24'><span class="wz">邮编</span><br>
	<input name='header' value='附注信息' type='checkbox' id='cb5'><span class="wz">附注信息</span><br>
	</div>
		<div id="div_select" style="display: none;" align="right"><br>
		<a id="btnSelectAll"  href="#" class="button" >全 选</a>&nbsp;
		<a id="btnClear"  href="#" class="button" >清 空</a>&nbsp;
		</div>
  <br>
  <div align="center" style="background-color:#ffffff">
	  <form name="form1" method="post" action="<%=root%>/address/address!export.action" target="exportFrame">
	  	<input id="headerColumn" name="headerColumn" type="hidden" />
	  	<input name="groupId" type="hidden" value="${groupId }">
	  	<input id="groupName" name="groupName" type="hidden" value="${groupName }">
	  	<input id="exportExt" type="hidden" name="exportExt"/>
	  	<a id="btnPrev"  href="#" class="button" >上一步</a>&nbsp;
	  	<a id="btnNext"  href="#" class="button" >下一步</a>&nbsp;
	  	<a id="btnCancel"  href="#" class="button" >取 消</a>&nbsp;
	  </form>
  </div>
  <iframe name="exportFrame" style="display: none ;"></iframe>
  </body>
</html>
