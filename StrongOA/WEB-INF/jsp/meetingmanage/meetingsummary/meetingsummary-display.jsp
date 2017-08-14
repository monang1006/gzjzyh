<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>

<%@ taglib uri="/tags/c.tld" prefix="c"%>
<%@taglib uri="/tags/web-newdate" prefix="strong"%>
<%@taglib prefix="s" uri="/struts-tags"%>


<html>
	<head>
		<base target="_self" />
		<title>会议记录</title>
		<%@include file="/common/include/meta.jsp"%>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<link type="text/css" rel="stylesheet"
			href="<%=path%>/common/js/tabpane/css/luna/tab.css" />
		<script type="text/javascript"
			src="<%=path%>/common/js/tabpane/js/tabpane.js"></script>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/btn.js" type="text/javascript"></script>
		<script language="javascript"
			src="<%=path %>/common/js/upload/jquery.MultiFile.js"></script>
		<script src="<%=path%>/common/js/upload/jquery.blockUI.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/btn.js" type="text/javascript"></script>
		<script src="<%=path%>/oa/js/prsnfldr/prsnfldr.js"
			type="text/javascript"></script>
		<style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
	height: 100%
}
</style>
		<script type="text/javascript">

function formsubmit(){
		var r=document.getElementById('works').rows;
		var arr=new Array(0);
		for(var j=1;j<r.length;j++){
		//alert(document.getElementById('works').rows[j].aaa)
		//alert("");
		var d=document.getElementById('works').rows[j].aaa;
		   var t;
		for(var i=1;i<r[j].cells.length;i++){
			var worksid='worksname'+d;
			//alert(worksid);
			var totitle=document.getElementById(worksid).value;
			if(totitle.length>100){
				alert('待办事项字段过多，不能保存！');
				return ;
			}
			t=totitle+'/';
			var snameid='sname'+d;
			var toname=document.getElementById(snameid).value;
			if(toname.length>16){
				alert('处理人名字过长，不能保存！');
				return ;
			}
			t=t+toname+'/';
			var roname='wstate'+d;
			var state=document.getElementsByName(roname);
			 var tostate;
	      for(var k=0;k<state.length;k++){
		   if(state[k].checked){
	       tostate=state[k].value;
	      }
	     }
	     t=t+tostate;
		}
		arr.push(t);
	}
	var todoparams=arr.toString();
	document.getElementById('todoparams').value=todoparams;
	//alert(document.getElementById('todoparams').value);
	
	var f=document.getElementById('family').rows;
	var far=new Array(0);
	for(var j=1;j<f.length;j++){
		   var ft;
	var d=document.getElementById('family').rows[j].ccc;
		for(var i=0;i<f[j].cells.length;i++){
			var worksid='fname'+d;
			var uname=document.getElementById(worksid).value;
			ft=uname+'/';
			var roname='astate'+d;
			var state=document.getElementsByName(roname);
			 var tostate;
	      for(var k=0;k<state.length;k++){
		   if(state[k].checked){
	       tostate=state[k].value;
	      }
	     }
	     ft=ft+tostate;
		}
		far.push(ft);
	}
	var atenparams=far.toString();
	//alert(atenparams);
	document.getElementById('atenparams').value=atenparams;
	
		<%--
		 var inputDocument=document;
    if(inputDocument.getElementById("topicCode").value==""){
    	alert("议题编号不能为空，请输入。");
    	inputDocument.getElementById("topicCode").focus();
    	return false;
    }
   
    if(inputDocument.getElementById("topicsort").value=="123"){
    	alert("请选择议题分类。");
    	inputDocument.getElementById("topicsort").focus();
    	return false;
    }
    if(inputDocument.getElementById("topicEstime").value==""){
    	alert("请选择会议开始时间。");
    	inputDocument.getElementById("topicEstime").focus();
    	return false;
    }
    if(inputDocument.getElementById("topicEndtime").value==""){
    	alert("请选择会议结束时间。");
    	inputDocument.getElementById("topicEndtime").focus();
    	return false;
    }
    if(inputDocument.getElementById("topicAddr").value==""){
    	alert("会议地点不能为空，请输入。");
    	inputDocument.getElementById("topicAddr").focus();
    	return false;
    }
    if(inputDocument.getElementById("topicSubject").value==""){
    	alert("会议主题不能为空，请输入。");
    	inputDocument.getElementById("topicSubject").focus();
    	return false;
    }
		--%>
	var oEditor = FCKeditorAPI.GetInstance('content');
				     var acontent = oEditor.GetXHTML();
		document.getElementById("summaryContent").value=acontent;
		
		//获取被删除的附件id
			var delAttachIds = document.getElementById("delAttachIds").value;
			if(delAttachIds.length>0){
            		delAttachIds = delAttachIds.substring(0,delAttachIds.length-1);
            	}
            document.getElementById("delAttachIds").value = delAttachIds;
			document.forms[0].submit();
		 
		}
		
		//删除附件
 function delAttach(id){
		 	var attach = document.getElementById(id);
		 	var delAttachIds = document.getElementById("delAttachIds").value;
		 	document.getElementById("delAttachIds").value += id + ",";
		 	attach.style.display="none";
		 }
		 //下载附件
 function download(id){
		 var attachDownLoad = document.getElementById("attachDownLoad");
		 	attachDownLoad.src = "<%=path%>/meetingmanage/meetingtopic/meetingtopic!down.action?attachId="+id;
		 }
	function comeback(){
	//window.location="<%=path %>/meetingmanage/meetingsummary/meetingsummary.action";
   window.close();
	}
function reloadForm(){
		var params=document.getElementById('todoparams').value;
		if(params!=null && params!=''){
		var a=params.split(',');
		//alert(a);
		for(var i=0;i<a.length;i++){
		var arr=a[i];
		var xx=arr.split('/');
		var title=xx[0];
		var sname=xx[1];
		var state=xx[2];
		//alert(title);
		add_row_works(title,sname,state);
		}
		}
	var par=document.getElementById('atenparams').value;
	if(par!=null && par!=''){
	var ua=par.split(',');
	for(var k=0;k<ua.length;k++){
	var ar=ua[k];
	var sp=ar.split('/');
	var na=sp[0];
	var sa=sp[1];
	add_row_family(na,sa);
	}
	}
	else if(par==''|| par==null){
	var username=document.getElementById('userName').value;
	var un=username.split(',');
	for(var i=0;i<un.length;i++){
		var na=un[i];
		var sa='';
		
	add_row_family(na,sa);
	}
	}
	}	     	
	</script>
	</head>
	<body class="contentbodymargin" onload="reloadForm();">
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder style="overflow: auto;" cellpadding="0">
			<s:form
				action="/meetingmanage/meetingsummary/meetingsummary!save.action"
				method="post" enctype="multipart/form-data">
				<input type="hidden" id="sumId" name="sumId"
					value="${model.summaryId}">
				<input type="hidden" id="delAttachIds" name="delAttachIds" value="">
				<input type="hidden" id="todoparams" name="todoparams"
					value="${todoparams}">
				<input type="hidden" id="atenparams" name="atenparams"
					value="${atenparams}">
				<input type="hidden" id="userName" name="userName"
					value="${userName}">
				<input type="hidden" id="notId" name="notId"
					value="${model.noticeId}">
				<input id=summaryContent name="model.summaryContent"
					value="${model.summaryContent}" type="hidden">
				<div id="con" style="display: none">
					${model.summaryContent}
				</div>
				<label style="display: none;"></label>
				<iframe id="attachDownLoad" src=''
					style="display: none; border: 4px solid #CCCCCC;"></iframe>
				<table border="0" width="100%" bordercolor="#FFFFFF" cellspacing="0"
					cellpadding="0">
					<tr>
						<td>
							<table width="100%"
								style="FILTER: progid :   DXImageTransform.Microsoft.Gradient (   gradientType =   0, startColorStr =   #ededed, endColorStr =   #ffffff );">
								<tr>
									<td>&nbsp;</td>
									<td width="80%">
										<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
										会议记录
									</td>
									<td width="5">
									</td>

									<td >
										<a class="Operation" href="#" onclick="comeback()"> <img
												src="<%=root%>/images/ico/guanbi.gif" width="15"
												height="15" class="img_s"><span id="test"
											style="cursor: hand">关 闭&nbsp;</span> </a>
									</td>

								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td width="100%">
							<DIV class=tab-pane id=tabPane1>
								<SCRIPT type="text/javascript">
								tp1 = new WebFXTabPane( document.getElementById( "tabPane1") ,false );
								</SCRIPT>

								<DIV class=tab-page id=tabPage2>
									<H2 class=tab>
										纪要内容
									</H2>
									<table border="0" width="100%" cellpadding="2" cellspacing="1"
										align="center">

										<tr height="30">
											<img src="<%=path%>/oa/image/address/person.bmp" />
											&nbsp;&nbsp;&nbsp;&nbsp; 纪要情况
											<br>
											<hr />
										</tr>

										<tr>
											<td width="15%" height="21" class="biao_bg1" align="right">
												<span class="wz">纪要名称：</span>
											</td>
											<td class="td1" align="left">

												<input id="summaryTitle" name="model.summaryTitle"
													type="text" value="${model.summaryTitle}" readonly="true" size="30"
													maxlength="100">
											</td>

											<td width="15%" height="21" class="biao_bg1" align="right">
												<span class="wz">会议地点：</span>
											</td>
											<td class="td1" align="left">

												<input id="summaryAddr" name="model.summaryAddr" type="text"
													value="${model.summaryAddr}" readonly="true" size="30" maxlength="100">
											</td>

										</tr>
										<tr>
											<td width="15%" height="21" class="biao_bg1" align="right">
												<span class="wz">主 持 人：</span>
											</td>
											<td class="td1" align="left">
												<input id="summaryUsers" name="model.summaryUsers"
													type="text" value="${model.summaryUsers}" readonly="true" size="30">
											</td>
											<td width="15%" height="21" class="biao_bg1" align="right">
												<span class="wz">记录时间：</span>
											</td>
											<td class="td1" align="left">
												<strong:newdate name="model.summaryTime" id="summaryTime"
													width="175" skin="whyGreen" isicon="true"
													dateobj="${model.summaryTime}" dateform="yyyy-MM-dd"></strong:newdate>
											</td>

										</tr>

										<td height="21" class="biao_bg1" align="right">
											<span class="wz">纪要内容：</span>
										</td>
										<td class="td1" colspan="3" align="left">
											<script type="text/javascript"
												src="<%=request.getContextPath()%>/common/js/fckeditor2/fckeditor.js"></script>
											<script type="text/javascript">
													 
													var oFCKeditor = new FCKeditor( 'content' );
													oFCKeditor.BasePath	= '<%=request.getContextPath()%>/common/js/fckeditor2/'
													oFCKeditor.ToolbarSet = "FutureDefaultSet" ;
													oFCKeditor.Width = '100%' ;
													oFCKeditor.align == 'left';
                                                    oFCKeditor.Height = '250' ;								
													oFCKeditor.Value=document.getElementById("con").innerText;
													oFCKeditor.Create() ;
													 
                                                    </script>
										</td>
										</tr>
										<tr>
											<td height="21" class="biao_bg1" align="right">
												<span class="wz">附 件&nbsp;&nbsp;&nbsp;：</span>
											</td>
											<td class="td1" colspan="3" align="left">
												&nbsp;&nbsp;
												<input type="file" onkeydown="return false;" name="upload"
													class="multi required" style="width: 63%;" />
												${attachFiles}
											</td>
										</tr>

									</table>

									<SCRIPT type="text/javascript">tp1.addTabPage( document.getElementById( "tabPage2" ) );</SCRIPT>

								</DIV>

								<DIV class=tab-page id=tabPage4>
									<H2 class=tab>
										待办事项
									</H2>
									<SCRIPT type="text/javascript">tp1.addTabPage( document.getElementById( "tabPage4" ) );
						</SCRIPT>
									<table border="0" width="100%" cellpadding="2" cellspacing="1"
										align="center">
										<tr height="50">
											<img src="<%=path%>/oa/image/address/company.bmp" />
											&nbsp;&nbsp;&nbsp;&nbsp; 待办工作信息
											<br>
											<hr />
										</tr>
										<tr>
											<td width="100%" height="100" align="center">

												<table>


													<tr>

														<td>

															<table id="works" style="width: 100%" border="1"
																cellspacing="0" cellpadding="1">

																<tr>

																	<td class="td_name" width="200" align="center">
																		工作名称
																	</td>

																	<td class="td_name" width="100" align="center">
																		处 理 人
																	</td>
																	<td class="td_name" width="300" align="center">
																		处理状态
																	</td>
																</tr>

															</table>

														</td>

													</tr>

												</table>

											</td>
										</tr>


									</table>
								</div>

								<DIV class=tab-page id=tabPage3>
									<H2 class=tab>
										考勤情况
									</H2>
									<table border="0" width="100%" cellpadding="2" cellspacing="1"
										align="center">
										<tr height="50">
											<img src="<%=path%>/oa/image/address/home.bmp" />
											&nbsp;&nbsp;&nbsp;&nbsp;考勤情况
											<br>
											<hr />
										</tr>

										<tr>
											<td height="100" width="100%" align="center">
												<table>


													<tr>

														<td>

															<table id="family" style="width: 100%" border="1"
																cellspacing="0" cellpadding="1">

																<tr>

																	<td class="td_name" width="200" align="center">
																		姓 名
																	</td>

																	<td class="td_name" width="400" align="center">
																		参加会议情况
																	</td>

																</tr>

															</table>

														</td>

													</tr>

												</table>


											</td>
										</tr>

									</table>
									<SCRIPT type="text/javascript">tp1.addTabPage( document.getElementById( "tabPage3" ) );</SCRIPT>

								</DIV>
							</DIV>
							</DIV>
						</td>
					</tr>
					<tr height="80">
						<td align="center">
							<input type="button" id="btnNo" class="input_bg"
								icoPath="<%=root%>/images/ico/quxiao.gif" onclick="comeback();"
								value="  关闭" />
						</td>
						<td>
							&nbsp;
						</td>
					</tr>
				</table>
			</s:form>
		</DIV>


		<SCRIPT language="JavaScript">

var j_1 = 1;

function add_row_family(sname,state){

 newRow=document.all.family.insertRow(-1); 

 newRow.ccc=j_1;

 newcell=newRow.insertCell();

 newRow.bgColor='#FFFFFF';

 newcell.className='STYLE3';

 newcell.align='center';

 //newcell.innerHTML="<input type='text' name='familyname"+j_1+"' style='WIDTH: 60px; font-size:9pt; color:#000000' />";

 newcell.innerHTML="<input type='text' name='fname"+j_1+"' value='"+sname+"' style='WIDTH: 190px; font-size:9pt; color:#000000' />";


 for(var i = 0;i<1;i++){

 newcell=newRow.insertCell() ;

 newRow.bgColor='#FFFFFF';

 newcell.className='STYLE3';

 newcell.align='center';
if(state=='1')
{
 newcell.innerHTML="<input type='radio' name='astate"+j_1+"' value='1' style='WIDTH: 20px; font-size:9pt; color:#000000' checked/>&nbsp;出席 <input type='radio' name='astate"+j_1+"' value='2' style='WIDTH: 20px; font-size:9pt; color:#000000' />&nbsp;缺席 <input type='radio' name='astate"+j_1+"' value='3' style='WIDTH: 20px; font-size:9pt; color:#000000' />&nbsp;请假 <input type='radio' name='astate"+j_1+"' value='4' style='WIDTH: 20px; font-size:9pt; color:#000000' />&nbsp;迟到";
}
if(state=='2'){
 newcell.innerHTML="<input type='radio' name='astate"+j_1+"' value='1' style='WIDTH: 20px; font-size:9pt; color:#000000' />&nbsp;出席 <input type='radio' name='astate"+j_1+"' value='2' style='WIDTH: 20px; font-size:9pt; color:#000000'checked />&nbsp;缺席 <input type='radio' name='astate"+j_1+"' value='3' style='WIDTH: 20px; font-size:9pt; color:#000000' />&nbsp;请假 <input type='radio' name='astate"+j_1+"' value='4' style='WIDTH: 20px; font-size:9pt; color:#000000' />&nbsp;迟到";
 }
 if(state=='3'){
  newcell.innerHTML="<input type='radio' name='astate"+j_1+"' value='1' style='WIDTH: 20px; font-size:9pt; color:#000000' />&nbsp;出席 <input type='radio' name='astate"+j_1+"' value='2' style='WIDTH: 20px; font-size:9pt; color:#000000' />&nbsp;缺席 <input type='radio' name='astate"+j_1+"' value='3' style='WIDTH: 20px; font-size:9pt; color:#000000' checked />&nbsp;请假 <input type='radio' name='astate"+j_1+"' value='4' style='WIDTH: 20px; font-size:9pt; color:#000000' />&nbsp;迟到";
 }
  if(state=='4'){
   newcell.innerHTML="<input type='radio' name='astate"+j_1+"' value='1' style='WIDTH: 20px; font-size:9pt; color:#000000' />&nbsp;出席 <input type='radio' name='astate"+j_1+"' value='2' style='WIDTH: 20px; font-size:9pt; color:#000000' />&nbsp;缺席 <input type='radio' name='astate"+j_1+"' value='3' style='WIDTH: 20px; font-size:9pt; color:#000000' />&nbsp;请假 <input type='radio' name='astate"+j_1+"' value='4' style='WIDTH: 20px; font-size:9pt; color:#000000' checked />&nbsp;迟到";
 }
 else if(state=='0'|| state==''){
 newcell.innerHTML="<input type='radio' name='astate"+j_1+"' value='1' style='WIDTH: 20px; font-size:9pt; color:#000000' />&nbsp;出席 <input type='radio' name='astate"+j_1+"' value='2' style='WIDTH: 20px; font-size:9pt; color:#000000' />&nbsp;缺席 <input type='radio' name='astate"+j_1+"' value='3' style='WIDTH: 20px; font-size:9pt; color:#000000' />&nbsp;请假 <input type='radio' name='astate"+j_1+"' value='4' style='WIDTH: 20px; font-size:9pt; color:#000000' />&nbsp;迟到";
}
}

 

 newcell=newRow.insertCell() ;

 newRow.bgColor='#FFFFFF';

 newcell.className='STYLE3';

 newcell.align='center';

 //newcell.innerHTML="<a href='javascript:delTableRow(\""+1+"\")'>删除</a>";


 j_1++;

// document.all.j_1.value=j_1;

// document.all.family.focus();

}

 

 

 function deleteCurrentRow()//刪除當前行

{

 var currndex=event.srcElement.parentNode.parentNode.rowIndex;

  document.all.family.deleteRow(currndex);//table10--表格id

}


var k_1 = 1;

function add_row_works(title,sname,state){

 newRow=document.all.works.insertRow(-1);
 newRow.aaa = k_1;

 newcell=newRow.insertCell();

 newRow.bgColor='#FFFFFF';

 newcell.className='STYLE3';

 newcell.align='center';

 //newcell.innerHTML="<input type='text' name='familyname"+j_1+"' style='WIDTH: 60px; font-size:9pt; color:#000000' />";

 newcell.innerHTML="<input type='text' id='worksname"+k_1+"' value='"+title+"' style='WIDTH: 200px; font-size:9pt; color:#000000' />";
 
 newcell=newRow.insertCell() 

 newRow.bgColor='#FFFFFF';

 newcell.className='STYLE3';

 newcell.align='center';

 //newcell.innerHTML="<input type='text' name='familyname"+j_1+"' style='WIDTH: 60px; font-size:9pt; color:#000000' />";

 newcell.innerHTML="<input type='text' id='sname"+k_1+"' value='"+sname+"' style='WIDTH: 100px; font-size:9pt; color:#000000' />";
 
 

 newcell=newRow.insertCell() ;

 newRow.bgColor='#FFFFFF';

 newcell.className='STYLE3';

 newcell.align='center';

	if(state=='1'){
	newcell.innerHTML="<input type='radio' name='wstate"+k_1+"' value='1' style='WIDTH: 20px; font-size:9pt; color:#000000' checked />&nbsp;未处理 <input type='radio' name='wstate"+k_1+"' value='2' style='WIDTH: 20px; font-size:9pt; color:#000000' />&nbsp;正在处理 <input type='radio' name='wstate"+k_1+"' value='3' style='WIDTH: 20px; font-size:9pt; color:#000000' />&nbsp;处理完";
	}
	else if(state=='2'){
     newcell.innerHTML="<input type='radio' name='wstate"+k_1+"' value='1' style='WIDTH: 20px; font-size:9pt; color:#000000' />&nbsp;未处理 <input type='radio' name='wstate"+k_1+"' value='2' style='WIDTH: 20px; font-size:9pt; color:#000000' checked />&nbsp;正在处理 <input type='radio' name='wstate"+k_1+"' value='3' style='WIDTH: 20px; font-size:9pt; color:#000000' />&nbsp;处理完";
}
  else if(state=='3'){
       newcell.innerHTML="<input type='radio' name='wstate"+k_1+"' value='1' style='WIDTH: 20px; font-size:9pt; color:#000000' />&nbsp;未处理 <input type='radio' name='wstate"+k_1+"' value='2' style='WIDTH: 20px; font-size:9pt; color:#000000' />&nbsp;正在处理 <input type='radio' name='wstate"+k_1+"' value='3' style='WIDTH: 20px; font-size:9pt; color:#000000' checked/>&nbsp;处理完";
}
else if(state=='0'|| state=='')
	{
	       newcell.innerHTML="<input type='radio' name='wstate"+k_1+"' value='1' style='WIDTH: 20px; font-size:9pt; color:#000000' />&nbsp;未处理 <input type='radio' name='wstate"+k_1+"' value='2' style='WIDTH: 20px; font-size:9pt; color:#000000' />&nbsp;正在处理 <input type='radio' name='wstate"+k_1+"' value='3' style='WIDTH: 20px; font-size:9pt; color:#000000'/>&nbsp;处理完";
	}


 newcell=newRow.insertCell() ;

 newRow.bgColor='#FFFFFF';

 newcell.className='STYLE3';

 newcell.align='center';

 //newcell.innerHTML="<a href='javascript:delTableRow(\""+1+"\")'>删除</a>";


   k_1++;

  //document.all. k_1.value= k_1;

 //document.all.works.focus();

}

 

 

 function deleteworksRow()//刪除當前行

{

  var currRowIndex=event.srcElement.parentNode.parentNode.rowIndex;

  document.all.works.deleteRow(currRowIndex);//table10--表格id

}


</script>

	</body>

</html>
