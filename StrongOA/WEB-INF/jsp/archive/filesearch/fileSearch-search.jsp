<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<title>档案搜索</title>
		<%@include file="/common/include/meta.jsp"%>
		<link href="<%=frameroot%>/css/properties_windows_add.css" type="text/css" rel="stylesheet">
		<script type="text/javascript" language="javascript" src="<%=jsroot%>/jquery/jquery-1.2.6.js"></script>
		<script type="text/javascript" src="<%=jsroot%>/validate/jquery.validate.js"></script>
		<script type="text/javascript" src="<%=jsroot%>/validate/formvalidate.js"></script>
		<script type="text/javascript" src="<%=jsroot%>/common/common.js"></script>
		<script type="text/javascript" language="javascript" src="<%=jsroot%>/upload/jquery.MultiFile.js"></script>
		<script type="text/javascript" language="javascript" src="<%=jsroot%>/upload/jquery.blockUI.js"></script>
		<style type="text/css">
			.number {
				font-size: 3;
				font-family: 宋体;
			}
		</style>
		<script type="text/javascript">
			$(function(){
			  $("#nomSearchBTN").show();
 				$("#nomSearch").show();
 				$("#keySearch").hide();
 				$("#keySearchBTN").hide();
			});
   			
   			function gogo(){
   		  //   document.getElementById("myTableForm").action="<%=path%>/archive/fileSearch/fileSearch.action";
   		//	 $("#myTableForm").attr("action","<%=path%>/archive/fileSearch/fileSearch.action");
   			var year=$("#year").val();
   			var month=$("#month").val();
   			var patrnYear=/^(?!0000)[0-9]{4}$/;
   			var patrn=/^(0?[1-9]|1[0-2])$/;
   			if(year!=""&&!patrnYear.test(year)){
   				alert("输入的年份格式不正确!!");
   				return false ;
   			}
   			if(month!=""&&!patrn.test(month)){
   				alert("输入的月份不正确!!")
   				return false;
   			}
   			var tempfileDeadline=$("#tempfileDeadline1").find("option:selected").text();
   			if("请选择保管期限"==tempfileDeadline){
   			  tempfileDeadline="";
   			}
   			document.getElementById("tempfileDeadline").value = tempfileDeadline;
   			 $("#myTableForm").submit();
   			///fileNameRedirectAction.action?toPage=archive/filesearch/fileSearch-search.jsp
   		//	window.location="<%=path%>/fileNameRedirectAction.action?toPage=/archive/filesearch/fileSearch-tempContent.jsp?treeType=4";
   				}
			var deptbudget_basedata_width = 500;
			var deptbudget_basedata_height = 360;
			function departscelet(){			
 				var objId = "orgId";
 				var objName = "orgIdName";
 				var URLStr = scriptroot+"/archive/archivefolder/archiveFolder!orgtree.action?objId="+objId+"&objName="+objName+"&moduletype=pige";
	 			var sty="dialogWidth:"+deptbudget_basedata_width+"px;dialogHeight:"+deptbudget_basedata_height+"px;status:no";
				showModalDialog(URLStr, window, sty);
 			}
 			function selectfolder(){
 			 var returnValue= window.showModalDialog("<%=path%>/fileNameRedirectAction.action?toPage=archive/tempfile/tempFilePortalContent.jsp?forward=selected&moduletype=pige1",window,"help:no;status:no;scroll:no;dialogWidth:950px; dialogHeight:500px");
 			 if(returnValue){
 			// var returnValue1=returnValue.split(";");
 			  //document.getElementById("fileFolderName").value=returnValue1[0];
 			  document.getElementById("fileFolder").value=returnValue;
 			  var fileFolder = returnValue;
 			  $.post("<%=path%>/archive/archivefolder/archiveFolder!findFloderNo.action",
                 {"folderId":fileFolder},
                   function(data){
                     document.getElementById("fileFolderName").value=data;
                      
                   });
 			  }
 			}
 			function keywordSearch(){
 			    
 				$("#nomSearchBTN").hide();
 				$("#nomSearch").hide();
 				$("#keySearch").show();
 				$("#keySearchBTN").show();
 			}
 			
 			function nomwordSearch(){
 			   $("#nomSearchBTN").show();
 				$("#nomSearch").show();
 				$("#keySearch").hide();
 				$("#keySearchBTN").hide();
 			}
 			
			function selectCommpass(){//全文检索
				var value=document.getElementById("textfield").value;
				value=ltrim(value);
				if(value=="" || value=="全文检索"){
					alert("请输入检索内容");
					return false;
				}
				
				value = encodeURIComponent(value);
				$.post('<%=path%>/viewmodel/modelPrival!isNotDoor.action',
							 function(data){			
						            if(data=="sucess"){
						              	 getSysConsole().refreshWorkByTitle("<%=basePath%>/search/search!searchContent1.action?searchContent="+value,"全文检索");
						              	}else {
										 getSysConsole().refreshWorkByTitle("<%=basePath%>/search/search!searchContent1.action?searchContent="+value,"全文检索");
										 }
									});
			}
			 	//去掉左边空格
			function ltrim(s){ 
			    return s.replace( /^\s*/,""); 
			} 		
		</script>
	</head>
	<body class=contentbodymargin oncontextmenu="">
		<script type="text/javascript" language="javascript" src="<%=jsroot%>/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
				<tr>
					<td height="100%">
						<s:form id="myTableForm" action="/archive/filesearch/fileSearch.action" theme="simple">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td height="40" class="table_headtd">
										<table width="100%" border="0" cellspacing="0" cellpadding="00">
											<tr>
												<td class="table_headtd_img" >
													<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
												</td>
												<td align="left">
													<strong>档案高级查询</strong>
												</td>
												<td align="right">
													<table border="0" align="center" cellpadding="00" cellspacing="0" width="80%">
										                <tr>
										                	<td id="nomSearchBTN" name="save">
										                		<table border="0" align="right" cellpadding="00" cellspacing="0">
										                			<tr>
													                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
													                 	<td class="Operation_input" onclick="gogo();">&nbsp;查&nbsp;询&nbsp;</td>
													                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
												                  		<td width="5"></td>
										                			</tr>
										                		</table>
										                	</td>
										                	<td id="keySearchBTN" name="save" style="display:none">
										                		<table border="0" align="right" cellpadding="00" cellspacing="0">
										                			<tr>
													                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
													                 	<td class="Operation_input" onclick="selectCommpass();">&nbsp;查&nbsp;询&nbsp;</td>
													                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
												                  		<td width="5"></td>
										                			</tr>
										                		</table>
										                	</td>
										                </tr>
										            </table>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
							<div id="step1" align=center>
								<fieldset>
									<div style=" text-align: center;">
										<div >
											<div>
												<table width="100%" border="0" cellpadding="0" cellspacing="0" align="center" class="table1">
													<tr align="center">
														<td class="biao_bg1" style="width:430px;align:right">
															<input type="radio" onclick="nomwordSearch();"
																checked="checked" name="disLogo" value="tempfile">
															<span class="wz">资料中心</span>
															
															
															<input type="radio" onclick="nomwordSearch();"
																name="disLogo" value="file">
															<span class="wz">档案室</span>
															
															
															<input type="radio" onclick="keywordSearch();"
																name="disLogo" id="keyword" value="fileContent">
															<span class="wz">关键字</span>
														</td>
												</table>
											</div>
										</div>
										<div id="nomSearch" align="center">
											<table width="100%" border="0" cellpadding="0" cellspacing="0"
												 class="table1" align="center" valign="middle">
												<tr align="center">
													<td  class="biao_bg1" align="right">
														<span class="wz">文号：</span>
													</td>
													<td class="td1" colspan="3" align="left">
														<input id="fileNo" name="fileNo" type="text" size="30"
															value="${fileNo}" maxlength="25">

													</td>
												</tr>
												<%--												<tr>
													<td width="20%" height="21" class="biao_bg1" align="right">
														<span class="wz">作者名：</span>
													</td>
													<td class="td1" colspan="3" align="left">
														<input id="fileAuthor" name="fileAuthor" type="text"
															size="30" value="${fileAuthor}" maxlength="40">
													</td>
												</tr>--%>
												<tr align="center"> 
													<td  class="biao_bg1" align="right">
														<span class="wz">题名：</span>
													</td>
													<td class="td1" colspan="3" align="left">
														<input id="fileTitle" name="fileTitle" type="text"
															size="30" value="${fileTitle}" maxlength="80">
													</td>
												</tr>
												<tr align="center">
													<td  class="biao_bg1" align="right">
														<span class="wz">保管期限：</span>
														<input id="tempfileDeadline" name="tempfileDeadline" type="hidden" size="30"
															value="${tempfileDeadline}" maxlength="80">
													</td>
													<td class="td1" align="left">
												      <SELECT id="tempfileDeadline1" name="tempfileDeadline1"
															STYLE="width: 192px">
															<option value="">请选择保管期限</option>
															<option value="0">永久</option>
															<option value="4">定期10年</option>
															<option value="3">定期30年</option>
															
													  </SELECT>
													</td>
												</tr>
												<tr align="center">
													<td class="biao_bg1" align="right">
														<span class="wz">年度：</span>
													</td>
													<td class="td1" align="left">
														<strong:newdate id="year" name="year" cantinput="true"
															dateform="yyyy" width="70" dateobj="${fileDate}"
															isicon="true" />
														<span class="wz">月份：</span>
														<strong:newdate id="month" name="month" cantinput="true"
															dateform="MM" width="60" dateobj="${fileDate}"
															isicon="true" />
													</td>
												</tr>
												<tr align="center">
													<td  class="biao_bg1" align="right">
														<span class="wz">文件所属案卷：</span>
													</td>
													<td class="td1" colspan="3" align="left">
														<input id="fileFolderName" name="fileFolderName"
															type="text" size="22" readonly="readonly" value="${fileFolder}"
															>
															<a  href="#" class="button" onclick="selectfolder()">选择</a>
														
														<input id="fileFolder" name="fileFolder" type="hidden"
															value="${fileFolder}">
													</td>
												</tr>
												<tr align="center">
													<td  class="biao_bg1" align="right">
														<span class="wz">机构：</span>
													</td>
													<td class="td1" colspan="3" align="left">
														<input id="orgIdName" type="text" size="22"
															name="orgIdName" readonly="readonly" value="${orgId}">
														
															<a  href="#" class="button" onclick="departscelet()">选择</a>
														<input id="orgId" name="orgId" type="hidden" size="30"
															value="${orgId}" maxlength="80">
													</td>
												</tr>
												<%--												<tr>
													<td width="20%" height="21" class="biao_bg1" align="right">
														<span class="wz">来文类型：</span>
													</td>
													<td class="td1" colspan="3" align="left">
															<SELECT id="fileType" name="fileType">
															<option value="">请选择类型</option>
															<option value="0">档案中心文件</option>
															<option value="1">发文文件</option>
															<option value="2">收文文件</option>
															<option value="3">会议纪要文件</option>
															</SELECT>
													</td>
												</tr>
												<tr>
													<td width="20%" height="21" class="biao_bg1" align="right">
														<span class="wz">请选择分组方式：</span>
													</td>
													<td class="td1" colspan="3" align="left">
														<select id="groupType" name="groupType">
															<option value="">
																不分组
															</option>
															<option value="1">
																文件类型
															</option>
															<option value="4">
																创建年份
															</option>
															<option value="2">
																所属案卷
															</option>
														</select>
													</td>
												</tr>--%>
												<tr>
							                         <td class="table1_td"></td>
							                         <td></td>
						                         </tr>
											</table>
										</div>
										<div id="keySearch" align="center">
											<table width="100%" border="0" cellpadding="0" cellspacing="0" align="center" class="table1">
												<tr>
													<td class="biao_bg1" align="right">
														<span class="wz">关键字：</span>
													</td>
													<td class="td1" colspan="3" align="left">
														<input type="text" id="textfield" name="textfield" value=""
															onkeypress="if(event.keyCode==13) selectCommpass();"
															class="keyword" style="width: 50%">
													</td>
												</tr>
												<tr>
							                         <td class="table1_td"></td>
							                         <td></td>
						                         </tr>
											</table>
											<br>
										</div>
										<br>
										<div>
										</div>
								</fieldset>
							</div>
						</s:form>
					</td>
				</tr>
			</table>
		</DIV>
	</body>
</html>