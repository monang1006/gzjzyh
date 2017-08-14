<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>报表定义权限设置</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<link href="<%=frameroot%>/css/properties_windows_add.css" type=text/css
			rel=stylesheet>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>

		<script type="text/javascript">
		var reg_num= /^\d+$/;
		$(document).ready(function(){
			
			setSelectTr($("#type").val());
			$("#type").change(function(){
				var type = this.value;
				setSelectTr(type);
				$("#orgNameInput").val("");
				$("#orgIdInput").val("");
				$("#roleNameInput").val("");
				$("#roleIdInput").val("");
				$("#seclevel").val("");
			});
			
			$("#subPriv").click(function(){
				var type = $("#type").val();
				var typeName = "";
				var seclevel = $.trim($("#seclevel").val());
				var privilsetTypeId=""
				
				if(type=="0"){
					typeName="所有人";
				}else if(type=="1"){//部门
					privilsetTypeId = $.trim($("#orgIdInput").val());
					if(privilsetTypeId==""){
						alert("请选择部门后保存!");
						return;
					}
					
					typeName=$("#orgNameInput").val();
				}else if(type=="2"){//角色
					privilsetTypeId = $("#roleIdInput").val();
					if(privilsetTypeId==""){
						alert("请选择角色后保存!");
						return;
					}
					typeName=$("#roleNameInput").val();
				}
				
				if(seclevel==""){
					alert("安全级别不能为空，请确认输入！");
					$("#seclevel").focus();
					return;
				}else if(!reg_num.test(seclevel)){
					alert("安全级别只能输入数字！请重新输入！");
					$("#seclevel").focus();
					return;
				}else if(seclevel>100){
					alert("安全级别不能大于100！请重新输入！");
					$("#seclevel").focus();
					return;
				}
				
				
				$.ajax({
			  		type:"post",
			  		dataType:"text",
			  		url:"<%=path%>/report/reportPrivilset!save.action",
			  		data:"definitionId=${definitionId}&privilsetTypeName="+typeName+"&privilsetTypeId="+
							privilsetTypeId+"&privilsetTypeFlag="+type+"&privilsetLevel="+seclevel,
			  		success:function(msg){
				  			if(""==msg){
								alert("保存出错");
				  			}else{
				  				insertTr(msg,typeName,seclevel);
				  			}
			  		}
			  	});
			
			});
		});
		
		function chooseOrg(){
			var url = "<%=path%>/report/reportPrivilset!showTreeWithCheckbox.action?privilsetTypeFlag=1";
			var a = window.showModalDialog(url,window,'dialogWidth:350px;dialogHeight:480px;help:no;status:no;scroll:no');
			if(a!=""&&a!="undefined"&&a!=undefined&&a.indexOf("，")>0){
				var org = a.split("，");
				$("#orgIdInput").val(org[0]);
				$("#orgNameInput").val(org[1]);
			}
		
		}
		
		function chooseRole(){
			var url = "<%=path%>/report/reportPrivilset!showTreeWithCheckbox.action?privilsetTypeFlag=2";
			var a = window.showModalDialog(url,window,'dialogWidth:350px;dialogHeight:480px;help:no;status:no;scroll:no');
			if(a!=""&&a!="undefined"&&a!=undefined&&a.indexOf("，")>0){
				var org = a.split("，");
				$("#roleIdInput").val(org[0]);
				$("#roleNameInput").val(org[1]);
			}
		}
		
		function setSelectTr(type){
			if("1"==type){
					$("#bumen_tr").show();
					$("#juese_tr").hide();
				}else if("2"==type){
					$("#bumen_tr").hide();
					$("#juese_tr").show();
				}else{
					$("#bumen_tr").hide();
					$("#juese_tr").hide();
				}
		}
		
		function deleterow(id){
			if(id!=""){
				$.ajax({
			  		type:"post",
			  		dataType:"text",
			  		url:"<%=path%>/report/reportPrivilset!delete.action",
			  		data:"privilsetId="+id,
			  		success:function(msg){
				  			if("succ"==msg){
								deleteTr(id);
				  			}else{
				  				alert("对不起，删除出错！");
				  			}
				  			
			  		}
			  	});
			}
		}


		function insertTr(id,typeName,seclevel){
			var addHtml="<TR id=\"tr_"+id+"\" style=\"BACKGROUND: #b0cbef\" >";
				addHtml += "<TD class=\"td1\" nowrap=\"nowrap\" align=\"center\">"+typeName+"</TD>";
				addHtml += "<TD class=\"td1\" nowrap=\"nowrap\" align=\"center\">"+seclevel+"</TD>";
				addHtml += "<TD class=\"td1\" nowrap=\"nowrap\" align=\"center\">";
				addHtml += "<input type=\"button\" value=\"删除\" id=\"btnDel\" onclick=\"deleterow('"+id+"');\">";
				addHtml += "</TD>";
				addHtml += "<TD class=\"td1\" style=\"TEXT-INDENT: 0px\" nowrap=\"nowrap\"> &nbsp; </TD> </TR>";
			$("#list_table").html($("#list_table").html()+addHtml);
		}
		
		function deleteTr(id){
			$("#tr_"+id).hide();
		}
		
		</script>
	</HEAD>

	<BODY class=contentbodymargin oncontextmenu="return false;">
		<DIV id=contentborder align=center>
		<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
				<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
								<strong>报表定义权限设置（
												<b>${definitionName}<b>）</strong>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
					                
					            </table>
							</td>
						</tr>
					</table>
				</td>
				</tr>
				<tr>
					<td>
			
						<fieldset style="width:100%;">
						<legend>
						<span class="wz" style='height:25px'>添加报表权限 </span>
					</legend>
							
							<table width="100%" border="0" cellpadding="0" cellspacing="0"
						class="table1">
								<tr>
									<td  class="biao_bg1" align="right">
										<span class="wz" style="width:190px"><font color=red>*</font>&nbsp;权限设置类别名称：</span>
									</td>
									<td class="td1" title="（权限设置类别名称）">
										<s:select id="type" name="type"
											list="#{'1':'部门','2':'角色','0':'所有人'}" listKey="key"
											listValue="value" style="width:41%" />
									</td>
								</tr>
								<tr id="bumen_tr">
									<td  class="biao_bg1" align="right">
										<span class="wz" style="width:190px"><font color=red>*</font>&nbsp;选择部门：</span>
									</td>
									<td class="td1">
										<input id="orgNameInput" value="" type="text" size="23" readonly="readonly">
										<a  href="#" class="button" onclick="chooseOrg()" id="btnDel">选择部门</a>
										
										<input id="orgIdInput" value="" type="hidden">
									</td>
								</tr>
								<tr id="juese_tr">
									<td  class="biao_bg1" align="right">
										<span class="wz" style="width:190px"><font color=red>*</font>&nbsp;选择角色：</span>
									</td>
									<td class="td1">
										<input id="roleNameInput" value="" type="text" size="23" readonly="readonly">
										<a  href="#" class="button" onclick="chooseRole()" id="btnDel">选择角色</a>
										
										<input id="roleIdInput" value="" type="hidden">
									</td>
								</tr>
								<tr>
									<td  class="biao_bg1" align="right">
										<span class="wz" style="width:190px"><font color=red>*</font>&nbsp;安全级别：</span>
									</td>
									<td class="td1">
										<input id="seclevel" name="seclevel" value="" type="text" size="23">
									</td>
								</tr>
								<tr class="td1">

								</tr>
								
								
								<tr>
							<td class="table1_td"></td>
							<td></td>
						</tr>
							</table>
							<table width="100%" border="0" cellpadding="0" cellspacing="0"
						class="table1">
							<td align="center">
								<table border="0" align="center" cellpadding="00" cellspacing="0">
					                <tr>
					                   <td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" id="subPriv">&nbsp;确&nbsp;定&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
					                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
					                 	<td class="Operation_input1"  onclick="window.close()">&nbsp;关&nbsp;闭&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
				                  		<td width="6"></td>
					              </tr>
					            </table>
							</td>
					</table>
							 </tr>
						</fieldset>
					</td>
				</tr>
			</table>
			<table width="100%" cellSpacing=0 cellPadding=0>
				<tr>
					<td height="28"
						style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
						<table width="100%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td width="5%" align="center">
								</td>
								<td width="95%">
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
			<TABLE class="table1" id="myTable"
				style="VERTICAL-ALIGN: top; CURSOR: default" 
				cellSpacing="1" cellPadding="0" width="100%" align="left">
				<THEAD>
					<TR>
						<TH class="biao_bg2" width="45%" height="22" nowrap="nowrap">
							权限设置类别名称
						</TH>
						<TH class="biao_bg2" width="40%" height="22" nowrap="nowrap">
							安全级别
						</TH>
						<TH class="biao_bg2" width="15%" height="22" nowrap="nowrap">
							操作
						</TH>
						<TH class="biao_bg2" style="TEXT-INDENT: 0px" nowrap="nowrap">
							&nbsp;
						</TH>
					</TR>
				</THEAD>
				<TBODY id="list_table">
				<%
				List list = (List) request.getAttribute("psList");
					if (list != null && list.size() > 0) {
						for (int i = 0; i < list.size(); i++) {
						com.strongit.oa.bo.ToaReportPrivilset priv = (com.strongit.oa.bo.ToaReportPrivilset) list.get(i);
				%>
					<TR id="tr_<%=priv.getPrivilsetId()%>" style="BACKGROUND: #b0cbef" >
						<TD class="td1" nowrap="nowrap" align="center">
							<%=priv.getPrivilsetTypename()%>
						</TD>
						<TD class="td1" nowrap="nowrap" align="center">
							<%=priv.getPrivilsetLevel()%>
						</TD>
						<TD class="td1" nowrap="nowrap" align="center">
							<input type="button" value="删除" id="btnDel" onclick="deleterow('<%=priv.getPrivilsetId()%>');">
						</TD>
						<TD class="td1" style="TEXT-INDENT: 0px"
							nowrap="nowrap">
							&nbsp;
						</TD>
					</TR>
				<%
					}
					}
				%>
				</TBODY>
				
				<TFOOT>
					<TR>
						<TD class="td1" id="myTable_td" align="left" colSpan="5"
							nowrap="nowrap">
							&nbsp;
						</TD>
					</TR>
				</TFOOT>
			</TABLE>

		</DIV>
	</BODY>
</HTML>
