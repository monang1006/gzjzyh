<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@	include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>增加文件</title>
		<link href="<%=frameroot%>/css/properties_windows_add.css" type="text/css"
			rel="stylesheet">
		<script src="<%=path %>/common/js/jquery/jquery-1.2.6.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/validate/jquery.validate.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/validate/formvalidate.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path %>/common/js/upload/jquery.MultiFile.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path %>/common/js/upload/jquery.blockUI.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/commontab/service.js" language="JavaScript"></script>
		<script src="<%=path%>/common/js/commontab/workservice.js" language="JavaScript"></script>
		<SCRIPT>
 			setPageListenerEnabled(true);//设置页面数据发生改变是否监听
 			function changelimitName(obj){
 				var objvalue = obj.value;
 				for(var i=0;i<obj.options.length;i++){
 					if(obj.options[i].value==objvalue)
 						document.getElementById("folderLimitName").value=obj.options[i].text;
 				}
 				notifyChange(true);
 			}
 			var deptbudget_basedata_width = 300;
			var deptbudget_basedata_height = 360;
 			function orgscelet(){
 				var objId = "folderOrgId";
 				var objName = "folderOrgName";
 				var hascheckedvalues = document.getElementById("folderOrgId").value;
    			var URLStr = scriptroot+"/archive/archivefolder/archiveFolder!dictree.action?objId="+objId+"&objName="+objName+"&dictValue=SSQZ&hascheckedvalues="+hascheckedvalues;
	 			var sty="dialogWidth:"+deptbudget_basedata_width+"px;dialogHeight:"+deptbudget_basedata_height+"px;status:no";
				showModalDialog(URLStr, window, sty);
 			}
 			function departscelet(){
 				var objId = "folderDepartment";
 				var objName = "folderDepartmentName";
 				var moduletype = document.getElementById("moduletype").value;
 				var hascheckedvalues = document.getElementById("folderDepartment").value;
 				var URLStr = scriptroot+"/archive/archivefolder/archiveFolder!orgtree.action?objId="+objId+"&objName="+objName+"&moduletype="+moduletype+"&hascheckedvalues="+hascheckedvalues;
	 			var sty="dialogWidth:"+deptbudget_basedata_width+"px;dialogHeight:"+deptbudget_basedata_height+"px;status:no";
				showModalDialog(URLStr, window, sty);
 			}
 			function userselect(){
 				var orgid = document.getElementById("folderDepartment").value;
 				if(orgid==""){
 					alert("请先选择组织机构！");
 					return ;
 				}
 				var objId = "folderCreaterId";
 				var objName = "folderCreaterName";
 				var hascheckedvalues = document.getElementById("folderCreaterId").value;
 				var URLStr = scriptroot+"/archive/archivefolder/archiveFolder!usertree.action?objId="+objId+"&objName="+objName+"&orgId="+orgid+"&hascheckedvalues="+hascheckedvalues;
	 			var sty="dialogWidth:"+deptbudget_basedata_width+"px;dialogHeight:"+deptbudget_basedata_height+"px;status:no";
				showModalDialog(URLStr, window, sty);
 			}
 			
 			function gogo(){
   				var content = document.getElementById("folderDesc").value;
   				if(content.length > 2){
   					alert("文件备注太长！");
   					document.getElementById("folderDesc").value="";
   					return false;
   				}
   			}
   			
   			function chkNN(obj){ //控制卷内件数为数字
			  	str=obj.value;  	
		      	if(str!=""&&checkNum(str)==false){
		      		alert("只可以输入大于或等于0的整数");
		      	}   	      
   			}
   			function chkShuMing(){
   			var shuming = document.getElementById("folderFormName").value;
   			if(shuming.length>30){
   			alert("书名不能超过30个字");
   			return false;
   				}
   			}
   			function checkNum(str){
 				var reg=/^[0-9]*$/;
 				var result=str.match(reg);   
			    if(result==null) return false;   
			    return true;   
 			}
			function onSub(){
			  var folderName=document.getElementById("folderName").value;//案卷名称
			  var folderNo=document.getElementById("folderNo").value;//案卷编号
			  var folderOrgName=document.getElementById("folderOrgName").value;//全宗名称
			  var folderDate=document.getElementById("folderDate").value;//创建日期
			  var desc=document.getElementById("testearea").value;
			  var str=document.getElementById("folderPage").value;
			  var folderLimitName=document.getElementById("folderLimitName").value;//保管期限
			  var folderLimitId=document.getElementById("folderLimitId").value;
			  var folderDepartment=document.getElementById("folderDepartment").value;//机构Id
			  var folderId=document.getElementById("folderId").value;
			  //var num=document.getElementById("folderFileNum").value;
			  var time=/^((((19|20)\d{2})-(0?[13-9]|1[012])-(0?[1-9]|[12]\d|30))|(((19|20)\d{2})-(0?[13578]|1[02])-31)|(((19|20)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|((((19|20)([13579][26]|[2468][048]|0[48]))|(2000))-0?2-29))$/;
		      var begintime=document.getElementById("folderFromDate").value;
		      var endtime=document.getElementById("folderToDate").value;
			  if(folderName==""||folderName==null||folderName=="null"){
			      alert("题名不可以为空！");
			      return false;
			  }else
			   if(folderNo==null||folderNo==""||folderNo=="null"){
			      alert("卷(盒)号不可以为空！");
			      return false;
			  }else {
			  		
			  }
			  if(folderOrgName==null||folderOrgName==""||folderOrgName=="null"){
			      alert("全宗号不可以为空！");
			      return false;
			  }else if(folderDate==null||folderDate==""||folderDate=="null"){
			      alert("年度不可以为空！");
			      return false;
			  }else if(desc.length>=200){
			     alert("案卷描述请不要超过200字");
			     return false;
			  }
<%--			  if(num!=""&&checkNum(num)==false){--%>
<%--			        alert("档案文件数目只能为整数");--%>
<%--		      		return false;--%>
<%--			  }--%>
			  if(str!=""&&checkNum(str)==false){
		      		alert("卷内件数只能为整数");
		      		return false;
              }  
              if(begintime!=""&&!time.test(begintime)){
		            alert("年度开始格式不对！");
		            return false;    
              }
              
              if(endtime!=""&&!time.test(endtime)){
		            alert("年度结束格式不对！");
		            return false;    
              }
              if(begintime > endtime&&""!=endtime){
        		alert("开始年度晚于结束年度，请重新选择时间范围！");	
        		return;
        	  }
              if(folderLimitId==""||folderLimitId==null){
              		alert("保管期限不能为空");
              		return false;
              }
              if(folderDepartment==""||folderDepartment==null){
              		alert("机构不能为空");
              		return false;
              }
              
                 $.post("<%=path%>/archive/archivefolder/archiveFolder!isHasthesameNoandDate.action",
                 {"model.folderNo":folderNo,"folderId":folderId,"folderDate1":folderDate},
                   function(data){
                      if(data=="0"){
                      	var folderDate=clockon($("#folderDate").val());
                      	$("#folderDate1").val(folderDate);
                      	document.getElementById("archiveFolderForm").submit();
                      }else{
                      		alert("已存在该卷(盒)号，不能进行保存！");
                      		return ;
                      }
                   });
			}
			
			function   clockon(folderDate)   {   
			  var thistime=   new   Date()  
			  var   months=thistime.getMonth()
			  var   month=months+1	
			  var   date=thistime.getDate() 
			  if   (eval(month)   <10)   {month="0"+month}  
			  if   (eval(date)   <10)   {date="0"+date}  
			   folderDate=folderDate+"-"+month+"-"+date;
			   return folderDate;
			  }
			
			function gouppage(){
				//window.history.go(-1);
				var archiveSortId="${archiveSortId}";
				var moduletype="${moduletype}";
				window.location="<%=path%>/archive/archivefolder/archiveFolder.action?archiveSortId="+archiveSortId+"&moduletype="+moduletype;
			}
			function showBorn(name){
			   var date=$("#"+name).val();
   		       if(date.length==4){
   		         $("#"+name).val(date+"-");
   		       }else if(date.length==7){
   		         $("#"+name).val(date+"-");
   		       }else if(date.length>9){
   		         $("#"+name).val(date);
   		       }
   		    }
 		</SCRIPT>
	</head>
	<body class=contentbodymargin oncontextmenu="return false;">
		<script src="<%=path %>/common/js/newdate/WdatePicker.js" type="text/javascript"></script>
		<DIV id=contentborder align=center>
			<s:form id="archiveFolderForm" theme="simple" action="/archive/archivefolder/archiveFolder!save.action">
				<input id="folderId" name="model.folderId" type="hidden" size="22" value="${model.folderId}">
				<input id="moduletype" name="moduletype" type="hidden" size="22" value="${moduletype}">
				<input id="folderCreaterId" name="model.folderCreaterId" type="hidden" value="${model.folderCreaterId}">
				<input id="folderCreaterName" name="model.folderCreaterName" type="hidden" value="${model.folderCreaterName}">
				<table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
					<tr>
						<td height="100%">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td height="40" class="table_headtd">
										<table width="100%" border="0" cellspacing="0" cellpadding="00">
											<tr>
												<td class="table_headtd_img" >
													<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
												</td>
												<td align="left">
													<s:if test="folderId==null">&nbsp;
									                  	<strong>增加案卷</strong>
								                  	</s:if>
													<s:else>
								                    	<strong>编辑案卷</strong>
								                    </s:else>
												</td>
												<td align="right">
													<table border="0" align="right" cellpadding="00" cellspacing="0">
										                <tr>
										                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
										                 	<td class="Operation_input" onclick="onSub();">&nbsp;保&nbsp;存&nbsp;</td>
										                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
									                  		<td width="5"></td>
										                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
										                 	<td class="Operation_input1" onclick="gouppage();">&nbsp;返&nbsp;回&nbsp;</td>
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
							<table  border="0" cellpadding="0" cellspacing="0" class="table1" width="100%" >
							<tr style="display:none"><td>
								<strong:newdate id="folderDate1" name="model.folderDate" dateform="yyyy" width="133" dateobj="${model.folderDate}" classtyle="required" />
								</td></tr>
								<tr>
									<td  height="21" class="biao_bg1" align="right">
										<span class="wz">所属类目：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="archiveSortId" name="archiveSortId" type="hidden" size="22" value="${archiveSortId}">
										<input id="archiveSortId" name="model.toaArchiveSort.sortId" type="hidden" size="22" value="${archiveSortId}">
										${archiveSortName}
									</td>
								</tr>
								<tr>
									<td  height="21" class="biao_bg1" align="right">
										<span class="wz"><FONT color="red">*</FONT>&nbsp;题名：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="folderName" name="model.folderName" type="text" size="22" value="${model.folderName}" class="required" maxlength="60">
									</td>
								</tr>
								<tr>
									<td  height="21" class="biao_bg1" align="right">
										<span class="wz"><FONT color="red">*</FONT>&nbsp;卷(盒)号：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="folderNo" name="model.folderNo" type="text" size="22" value="${model.folderNo}" class="required" maxlength="20">
									</td>
								</tr>
								<!-- 
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">案卷序号：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="folderArrayNo" name="model.folderArrayNo" type="text" size="22" value="${model.folderArrayNo}" maxlength="40">
									</td>
								</tr>
								 -->
								<tr>
									<td  height="21" class="biao_bg1" align="right">
										<span class="wz"><FONT color="red">*</FONT>&nbsp;全宗号：</span>
										<input id="folderOrgId" name="model.folderOrgId" type="hidden" value="${model.folderOrgId}">
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="folderOrgName" name="model.folderOrgName" type="text" size="22" readonly="readonly" value="${model.folderOrgName}" class="required">
										
										<a  href="#" class="button" onclick="orgscelet()">选择</a>
									</td>
								</tr>
								<tr>
									<td height="21"  class="biao_bg1" align="right">
										<span class="wz"><FONT color="red">*</FONT>&nbsp;年度：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<strong:newdate id="folderDate"  dateform="yyyy" width="160" dateobj="${model.folderDate}" classtyle="required"/>
									</td>
								</tr>
								<tr>
									<td height="21" class="biao_bg1" align="right">
										<span class="wz"><FONT color="red">*</FONT>&nbsp;机构：</span>
										<input id="folderDepartment" name="model.folderDepartment" type="hidden" value="${model.folderDepartment}">
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="folderDepartmentName" type="text" size="22" readonly="readonly" value="${model.folderDepartmentName}">
										
										<a  href="#" class="button" onclick="departscelet()">选择</a>
<%--										<span class="wz"><FONT color="red">注：获奖单位（处室）</FONT></span>--%>
									</td>
								</tr>
								<tr>
									<td height="21"  class="biao_bg1" align="right">
										<span class="wz"><FONT color="red">*</FONT>&nbsp;保管期限：</span>
										<input id="folderLimitName" name="model.folderLimitName" type="hidden" value="${model.folderLimitName}">
									</td>
									<td class="td1" colspan="3" align="left">
										<s:select list="limitdictList" listKey="dictItemCode" listValue="dictItemName" headerKey="" headerValue="请选择保管期限" id="folderLimitId" name="model.folderLimitId" onchange="changelimitName(this)" style="width:11.4em"/>
									</td>
								</tr>
								<!-- 
								<tr>
									<td height="21" width="20%" class="biao_bg1" align="right">
										<span class="wz">件（册）数：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="folderFileNum" name="model.folderFileNum" type="text" size="22" value="${model.folderFileNum}" class="number" maxlength="10">
									</td>
								</tr>
								 -->
								<tr>
									<td height="21" class="biao_bg1" align="right">
										<span class="wz">开始年度自：</span>
									</td>
									<td class="td1" align="left">
									<strong:newdate id="folderFromDate" name="folderFromDate1" dateform="yyyy-MM-dd" width="160" dateobj="${folderFromDate1}"/>
									</td>
								</tr>	
								<tr>
									<td height="21" class="biao_bg1" align="right">
										<span class="wz">结束年度至：</span>
									</td>
									<td class="td1" align="left">
									<strong:newdate id="folderToDate" name="folderToDate1" dateform="yyyy-MM-dd" width="160" dateobj="${folderToDate1}"/>
									</td>
								</tr>								
								<tr>
									<td height="21" class="biao_bg1" align="right">
										<span class="wz">件(册)数：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="folderPage" name="model.folderPage" type="text" onblur="" size="22" value="${model.folderPage}" class="number" maxlength="10">
									</td>
								</tr>
								<!-- 
								<tr>
									<td height="21" width="20%" class="biao_bg1" align="right">
										<span class="wz">案卷创建者：</span>
										<input id="folderCreaterId" name="model.folderCreaterId" type="hidden" value="${model.folderCreaterId}">
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="folderCreaterName" name="model.folderCreaterName" type="text" size="22" readonly="readonly" value="${model.folderCreaterName}">
										<input name="userscelet" type="button" class="input_bg" value="选 择" onclick="userselect()">
									</td>
								</tr>
								 -->
								 <tr>
									<td height="21"  class="biao_bg1" align="right">
										<span class="wz">书名：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="folderFormName" name="model.folderFormName" type="text" size="22" value="${model.folderFormName}" class="required"  onblur="chkShuMing(this)" maxlength="60">
									</td>
								</tr>
<%--								<s:if test="model.folderId!=null">
								<s:hidden id="folderAuditing" name="model.folderAuditing"></s:hidden>
								</s:if><s:else>
									<tr>
									<td height="21" width="20%" class="biao_bg1" align="right">
										<span class="wz">是否归档：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<s:select list="#{'未归档':'0','已归档':'1'}" listKey="value"  
												listValue="key" headerKey="0" headerValue="是否归档" id="folderAuditing" name="model.folderAuditing" />
									</td>
								</tr>
								</s:else>--%>
								<tr>
									<td height="21"  class="biao_bg1" align="right" valign="top">
										<span class="wz">卷(盒)描述：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<textarea id="testearea" rows="10" cols="50" name="model.folderDesc">${model.folderDesc}</textarea>
										<br>
										<font color="#999999">请不要超过200字</font>
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