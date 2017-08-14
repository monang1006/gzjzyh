<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@	include file="/common/include/rootPath.jsp"%>
<%request.setCharacterEncoding("utf-8");%>
<HTML>
	<HEAD>
		<TITLE>生成件号</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<link href="<%=frameroot%>/css/properties_windows_list.css" type="text/css"
			rel="stylesheet">
		<link type=text/css rel=stylesheet
			href="<%=frameroot%>/css/strongitmenu.css">
		<LINK type=text/css rel=stylesheet
			href="<%=frameroot%>/css/search.css">
		<!--右键菜单样式 -->
		<script language="javascript"
			src="<%=request.getContextPath()%>/common/js/grid/ChangeWidthTable.js"></script>
		<script language="javascript"
			src="<%=request.getContextPath()%>/common/js/menu/menu.js"></script>
		<script language="javascript"
			src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script language="javascript"
			src="<%=path%>/common/js/common/search.js"></script>
		<script language="JavaScript"
			src="<%=path%>/common/js/commontab/service.js"></script>
		<script language="JavaScript"
			src="<%=path%>/common/js/commontab/workservice.js"></script>
		<!--右键菜单脚本 -->
		<SCRIPT type="text/javascript">
			function baocun(){
			 var tempfPieceNo=document.getElementsByName("tempfPieceNo");
			 var s="";
			 if("1"==tempfPieceNo.length){
			     if(tempfPieceNo[0].value==""){
			       s="null"+",";
			     }else{
			       s=tempfPieceNo[0].value+",";
			      }
			 }else{
			     if(tempfPieceNo[0].value==""){
			       s="null"+",";
			     }else{
			       s=tempfPieceNo[0].value+",";
			      }
			     for(var i=1;i<tempfPieceNo.length;i++){
                    if(i==tempfPieceNo.length-1){
                      if(tempfPieceNo[i].value==""){
			               s=s+"null";
			          }else{
			                s=s+tempfPieceNo[i].value;
			          }
                    }else{
                       if(tempfPieceNo[i].value==""){
			               s=s+"null"+",";
			          }else{
			               s=s+tempfPieceNo[i].value+",";
			          }					
                       }	
               }
             }
            var folderId = "<%=request.getAttribute("folderId")%>";
               $.ajax({
						type:"post",
						url:"<%=path%>/archive/archivefolder/archiveFolder!savePieceNo.action",
						data:{
							folderId:folderId,tempfPieceNo:s				
						},
						success:function(data){	
							alert("件号保存成功！");
							//myTableForm.submit();
							//window.location.reload();
							window.close();
						},
						error:function(data){
							alert("对不起，操作异常"+data);
						}
					});
  }
	function cc(item){
			  var folderId = "<%=request.getAttribute("folderId")%>";
			  if(isNaN(item.value)){
		         alert("件号不能输入非数字！");
		         myTableForm.submit();
	            }
			    //alert(item.rowid);
			  //alert(item.value);
			    $.ajax({
						type:"post",
						url:"<%=path%>/archive/archivefolder/archiveFolder!changePieceNo.action",
						data:{
							tempfileId:item.rowid,
							folderId:folderId,
							tempPieceNo:item.value				
						},
						success:function(data){	
						
						 if(data=="1"){
						  alert("已存在相同的件号，修改不成功！");
						  myTableForm.submit();
						 }
							//myTableForm.submit();
							//window.location.reload();
						},
						error:function(data){
							alert("对不起，操作异常"+data);
						}
					});
			}
			
			function editPieceNo(value,id){
			   if(value=="null"){
			    value="";
			   }
			   return "<input type='text' name='tempfPieceNo' onchange='cc(this)' rowid="+id+" value="+value+" ></input>";
			}  
		</SCRIPT>
		<base target="_self">
	</HEAD>
	<BODY class=contentbodymargin  onload="initMenuT()">
		<script src="<%=path%>/common/js/newdate/WdatePicker.js" type="text/javascript"></script>
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
				<tr>
					<td height="100%">
						<s:form action="/archive/archivefolder/archiveFolder!getArchiveFile.action" id="myTableForm" theme="simple">
                          <input id="folderId" name="folderId" type="hidden" size="30" value="${folderId}">
                          <table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td height="40" class="table_headtd">
										<table width="100%" border="0" cellspacing="0" cellpadding="00">
											<tr>
												<td class="table_headtd_img" >
													<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
												</td>
												<td align="left">
													<strong>卷(盒)文件列表</strong>
												</td>
												<td align="right">
													<table width="100%" border="0" align="right" cellpadding="0" cellspacing="0">
														<tr>
															<td>
																<table border="0" align="right" cellpadding="0" cellspacing="0">
																	<tr>
																		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
													                 	<td class="Operation_list" onclick="createPieceNo();"><img src="<%=root%>/images/operationbtn/Formation_No.png"/>&nbsp;生&nbsp;成&nbsp;</td>
													                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
												                  		<td width="5"></td>
												                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
													                 	<td class="Operation_list" onclick="windowclose();"><img src="<%=root%>/images/operationbtn/close.png"/>&nbsp;关&nbsp;闭&nbsp;</td>
													                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
												                  		<td width="5"></td>
												                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
													                 	<td class="Operation_list" onclick="baocun();"><img src="<%=root%>/images/operationbtn/preserve.png"/>&nbsp;保&nbsp;存&nbsp;</td>
													                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
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
				<webflex:flexTable name="myTable" width="100%" wholeCss="table1"
					property="tempfileId" isCanDrag="true" isCanFixUpCol="true"
					clickColor="#A9B2CA" showSearch="false" footShow="showCheck"
					getValueType="getValueByProperty"  collection="${tempfileList}">
					<webflex:flexTextCol caption="件号" property="tempfilePieceNo"  onclick=""
									showValue="javascript:editPieceNo(tempfilePieceNo,tempfileId);" width="21%" isCanDrag="true" showsize="5"
									isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="卷(盒)号" property="toaArchiveFolder.folderId"
									showsize="30" showValue="toaArchiveFolder.folderNo" width="10%" showsize="10"
									isCanDrag="true" isCanSort="true">
								</webflex:flexTextCol>
								<webflex:flexTextCol caption="责任者" property="tempfileAuthor"
									showsize="30" showValue="tempfileAuthor" width="17%" showsize="10"
									isCanDrag="true" isCanSort="true">
								</webflex:flexTextCol>
									
								<webflex:flexTextCol caption="文号" property="tempfileNo"
									showValue="tempfileNo" width="16%" isCanDrag="true" 
									showsize="9" isCanSort="true"></webflex:flexTextCol>
									
								<webflex:flexTextCol caption="题名" property="tempfileId"
									showsize="50" showValue="tempfileTitle" width="27%" showsize="14"
									isCanDrag="true" isCanSort="true"></webflex:flexTextCol>

								<webflex:flexDateCol caption="日期" property="tempfileDate"
									showValue="tempfileDate" width="15%" isCanDrag="true"
									isCanSort="true" dateFormat="yyyy-MM-dd"></webflex:flexDateCol>
							
								<webflex:flexTextCol caption="页数" showsize="50"
									property="tempfilePage" showValue="tempfilePage" width="8%"
									isCanDrag="true" isCanSort="true" showsize="8"></webflex:flexTextCol>
				</webflex:flexTable>
						</s:form>
					</td>
				</tr>
			</table>
		</DIV>
		<script language="javascript">
var sMenu = new Menu();
function initMenuT(){
	sMenu.registerToDoc(sMenu);
	var item = null;
	item = new MenuItem("<%=root%>/images/ico/tb-add.gif","生成","createPieceNo",1,"ChangeWidthTable","checkOneDis");
	//sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/bianji.gif","保存","baocun",1,"ChangeWidthTable","checkOneDis");
	//sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}	
//生成件号
function createPieceNo(){
  var folderId = "<%=request.getAttribute("folderId")%>";
  var msg="";
           //$.ajax({
						//type:"post",
						//url:"<%=path%>/archive/archivefolder/archiveFolder!createPieceNo.action",
						//data:{
							//folderId:folderId				
						//},
						//success:function(data){	
							//alert("件号生成成功,请保存！");
							//myTableForm.submit();
							//window.location.reload();

						//},
						//error:function(data){
							//alert("对不起，操作异常"+data);
						//}
					//});
					var tempfPieceNo=document.getElementsByName("tempfPieceNo");
					if(tempfPieceNo.length=="0"){
					  alert("没有文件！");
					  return;
					}
					for(var i=0;i<tempfPieceNo.length;i++){
					   tempfPieceNo[i].value=i+1;
					   msg="1";
					}
					if(msg=="1"){
					 alert("件号生成成功,请保存！");
					 }
					 else{
					 alert("件号生成失败！");
					 }
              }
  
           
        
//关闭窗口
function windowclose(){
		 window.close();
	}
</script>
	</BODY>
</HTML>
