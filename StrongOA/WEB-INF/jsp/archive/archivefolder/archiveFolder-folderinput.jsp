<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>增加文件</title>
		<link href="<%=frameroot%>/css/properties_windows_add.css" type="text/css"
			rel="stylesheet">
		<script type="text/javascript" language="javascript"
			src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script type="text/javascript" language="javascript"
			src="<%=path%>/common/js/validate/jquery.validate.js"></script>
		<script type="text/javascript" language="javascript"
			src="<%=path%>/common/js/validate/formvalidate.js"></script>
		<script type="text/javascript" language="javascript"
			src="<%=path%>/common/js/upload/jquery.MultiFile.js"></script>
		<script type="text/javascript" language="javascript"
			src="<%=path%>/common/js/upload/jquery.blockUI.js"></script>
		<SCRIPT type="text/javascript" language="javascript"
			src="<%=path%>/common/js/commontab/service.js"></SCRIPT>
		<SCRIPT type="text/javascript" language="javascript"
			src="<%=path%>/common/js/commontab/workservice.js"></SCRIPT>
		<SCRIPT>
 			setPageListenerEnabled(true);//设置页面数据发生改变是否监听
 			function changelimitName(obj){//保管期限
 				var objvalue = obj.value;
 				for(var i=0;i<obj.options.length;i++){
 					if(obj.options[i].value==objvalue)
 						document.getElementById("folderLimitName").value=obj.options[i].text;
 				}
 				notifyChange(true);
 			}
 			
 			var deptbudget_basedata_width = 360;
			var deptbudget_basedata_height = 380;
 			function orgscelet(){	//所属全宗名称
 				var objId = "folderOrgId";
 				var objName = "folderOrgName";
    			var URLStr = scriptroot+"/archive/archivefolder/archiveFolder!dictree.action?objId="+objId+"&objName="+objName+"&dictValue=SSQZ";
	 			var sty="dialogWidth:"+deptbudget_basedata_width+"px;dialogHeight:"+deptbudget_basedata_height+"px;status:no";
				showModalDialog(URLStr, window, sty);
 			}
 			
 			function departscelet(){//所属处室
 				var objId = "folderDepartment";
 				var objName = "folderDepartmentName";
 				//var moduletype = document.getElementById("moduletype").value;
 				var URLStr = scriptroot+"/archive/archivefolder/archiveFolder!orgtree.action?objId="+objId+"&objName="+objName+"&moduletype=pig";
	 			var sty="dialogWidth:"+deptbudget_basedata_width+"px;dialogHeight:"+deptbudget_basedata_height+"px;status:no";
				showModalDialog(URLStr, window, sty);
 			}
 			
 			function uselect(){	//选择用户
 				//alert("选择用户！");
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
 				
   			function chkNN(obj){ //控制卷内件数为数字
			  	str=obj.value;  	
		      	if(str!=""&&checkNum(str)==false){
		      		alert("只可以输入大于或等于0的整数");
		      	}   	      
   			}
   			function checkNum(str){
 				var reg=/^[0-9]*$/;
 				var result=str.match(reg);   
			    if(result==null) return false;   
			    return true;   
 			}
 			//ction submitForm(){	//表单提交
 			function onSub(){
 			  var archiveSortName=document.getElementById("archiveSortName").value;//所属类目
 			  var folderFormName=document.getElementById("folderFormName").value;//书名
			  var folderNo=document.getElementById("folderNo").value;//案卷编号
			  var folderName=document.getElementById("folderName").value;//案卷题名
			  var folderOrgName=document.getElementById("folderOrgName").value;//全宗名称
			  var folderDepartmentName=document.getElementById("folderDepartmentName").value;//全宗名称
			  var folderDate=document.getElementById("folderDate1").value;//创建日期
			  var desc=document.getElementById("testearea").value;
			  var str=document.getElementById("folderPage").value;
			  var folderLimitName=document.getElementById("folderLimitName").value;//保管期限
			   var folderId=document.getElementById("folderId").value;
			  //var num=document.getElementById("folderFileNum").value;
			  var time=/^((((19|20)\d{2})-(0?[13-9]|1[012])-(0?[1-9]|[12]\d|30))|(((19|20)\d{2})-(0?[13578]|1[02])-31)|(((19|20)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|((((19|20)([13579][26]|[2468][048]|0[48]))|(2000))-0?2-29))$/;
		      var begintime=document.getElementById("folderFromDate").value;
		      var endtime=document.getElementById("folderToDate").value;
		      var str1 = new RegExp("^([\u4E00-\uFA29]|[\uE7C7-\uE7F3]|[a-zA-Z0-9])*$"); 
		       if(archiveSortName=null||archiveSortName==""||archiveSortName=="null"){
		       alert("请选择所属类目！");
		       return false;
		       }
			   if(folderNo==null||folderNo==""||folderNo=="null"){
			      alert("卷（盒）号不可以为空！");
			      return false;
			  }
			  if(!str1.test($.trim(folderNo)) && $.trim(folderNo) !=""){
			    alert("卷（盒）号包含特殊字符！\n请输入汉字，数字或字母");
  		        return; 
			  }
			  else if(folderName==null||folderName==""||folderName=="null"){
			      alert("题名不可以为空！");
			      return false;
			  }else if(folderOrgName==null||folderOrgName==""||folderOrgName=="null"){
			      alert("全宗号不可以为空！");
			      return false;
			  }else if(folderDepartmentName==null||folderDepartmentName==""||folderDepartmentName=="null"){
			      alert("机构不可以为空！");
			      return false;
			  }else if(folderDate==null||folderDate==""||folderDate=="null"){
			      alert("年度不可以为空！");
			      return false;
			  }else if(desc.length>200){
			     alert("卷（盒）描述请不要超过200字");
			     return false;
			  }
			  if(!str1.test($.trim(folderName)) && $.trim(folderName) !=""){
			    alert("题名包含特殊字符！\n请输入汉字，数字或字母");
  		        return; 
			  }
			  if(!str1.test($.trim(folderFormName)) && $.trim(folderFormName) !=""){
			    alert("书名包含特殊字符！\n请输入汉字，数字或字母");
  		        return; 
			  }
			  if(str!=""&&checkNum(str)==false){
		      		alert("件(册)数只能为正整数");
		      		return false;
              }  
              if(begintime!=""&&!time.test(begintime)){
		            alert("开始年度格式不对！");
		            return false;    
              }
              
              if(endtime!=""&&!time.test(endtime)){
		            alert("结束年度格式不对！");
		            return false;    
              }
              if(begintime > endtime&&""!=endtime){
        		alert("开始年度晚于结束年度，请重新选择时间范围！");	
        		return;
        	  }
              if(folderLimitName==""||folderLimitName==null){
              		alert("保管期限不能为空");
              		return false;
              }
              	if("${model.folderId}"==""||"${model.folderId}"==null||"${model.folderId}"=="null"){
                 $.post("<%=path%>/archive/archivefolder/archiveFolder!isHasthesameNo.action",
                 {"model.folderNo":folderNo,"folderId":folderId},
                   function(data){
                      if(data=="0"){
                      	$("#folderDate").val($("#folderDate1").val()+"-01-01");
                      	document.getElementById("archiveFolderForm").submit();
                      }else{
                      alert("已存在该卷（盒）号，不能进行保存！");
                      return ;
                      }
                   });
              	}else{
              		$("#folderDate").val($("#folderDate1").val()+"-01-01");
			  		document.getElementById("archiveFolderForm").submit();
			    }
			}
 			function selectSort(){	//选择类目
				var objId = "archiveSortId";
				var objName="archiveSortName";
    			var URLStr = scriptroot+"/archive/sort/archiveSort!tree.action?forwardStr=sorttree&objId="+objId+"&objName="+objName;
	 			var sty="dialogWidth:"+deptbudget_basedata_width+"px;dialogHeight:"+deptbudget_basedata_height+"px;status:no";
				showModalDialog(URLStr, window, sty);
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
		<script src="<%=path%>/common/js/newdate/WdatePicker.js" type="text/javascript"></script>
		<DIV id=contentborder align=center>
			<s:form id="archiveFolderForm" theme="simple" action="/archive/archivefolder/archiveFolder!gropFile.action">
				<input id="fileIds" name="fileIds" type="hidden" size="22" value="${fileIds}">
				<input type="hidden" id="depLogo" name="depLogo" value="${depLogo }">	
				<input id="folderId" name="model.folderId" type="hidden" size="22" value="${model.folderId}">
				<input id="folderCreaterId" name="model.folderCreaterId" type="hidden" value="${model.folderCreaterId}">
				<input id="folderCreaterName" name="model.folderCreaterName" type="hidden" value="${model.folderCreaterName}">
				<!-- 在添加和编辑档案中心文件时，设置返回参数 -->
				<input type="hidden" id="treeValue" name="treeValue" value="${treeValue }">
				<input type="hidden" id="treeType" name="treeType" value="${treeType }">
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
													<strong>组卷</strong>
												</td>
												<td align="right">
													<table border="0" align="right" cellpadding="00" cellspacing="0">
										                <tr>
										                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
										                 	<td class="Operation_input" onclick="onSub();">&nbsp;创&nbsp;建&nbsp;</td>
										                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
									                  		<td width="5"></td>
										                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
										                 	<td class="Operation_input1" onclick="history.go(-1)">&nbsp;返&nbsp;回&nbsp;</td>
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
							<table width="100%" height="10%" border="0" cellpadding="0"
								cellspacing="1" align="center" class="table1">
								<tr style="display:none"><td>
								<strong:newdate id="folderDate" name="model.folderDate" dateform="yyyy" width="133" dateobj="${model.folderDate}" classtyle="required" />
								</td></tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz"><FONT color="red">*</FONT>&nbsp;所属类目：</span>
										<input id="archiveSortId" name="model.toaArchiveSort.sortId"
											type="hidden" size="22"
											value="$(model.toaArchiveSort.sortId)">
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="archiveSortName" name="archiveSortName" type="text"
											size="22" readonly="readonly" >
											<a  href="#" class="button" onclick="selectSort()">选择</a>
										
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz"><FONT color="red">*</FONT>&nbsp;题名：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="folderName" name="model.folderName" type="text" size="22" value="${model.folderName}" class="required" maxlength="60">
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
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
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz"><FONT color="red">*</FONT>&nbsp;全宗号：</span>
										<input id="folderOrgId" name="model.folderOrgId" type="hidden" value="${model.folderOrgId}">
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="folderOrgName" name="model.folderOrgName" type="text" size="22" readonly="readonly" value="${model.folderOrgName}" class="required">
									
										<a  href="#" class="button" onclick="orgscelet()">选择</a>
									</td>
								</tr>
								<tr>
									<td height="21" width="20%" class="biao_bg1" align="right">
										<span class="wz"><FONT color="red">*</FONT>&nbsp;年度：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<strong:newdate id="folderDate1"  dateform="yyyy" width="160" dateobj="${model.folderDate}" classtyle="required" />
									</td>
								</tr>
								<tr>
									<td height="21" width="20%" class="biao_bg1" align="right">
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
									<td height="21" width="20%" class="biao_bg1" align="right">
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
									<td height="21" width="20%" class="biao_bg1" align="right">
										<span class="wz">开始年度自：</span>
									</td>
									<td class="td1" colspan="3" align="left">
									<strong:newdate id="folderFromDate" name="model.folderFromDate" dateform="yyyy-MM-dd" width="160" dateobj="${model.folderFromDate}"/>
										<!--<input id="folderFromDate" onkeyup="showBorn('folderFromDate');" name="model.folderFromDate" type="text" size="22" value="${model.folderFromDate}" maxlength="30">-->
									</td>
								</tr>
								<tr>
									<td height="21" width="20%"  class="biao_bg1" align="right">
										<span class="wz">结束年度到：</span>
									</td>
									<td class="td1" colspan="3" align="left">
									<strong:newdate id="folderToDate" name="model.folderToDate" dateform="yyyy-MM-dd" width="160" dateobj="${model.folderToDate}"/>
										<!--<input id="folderToDate" onkeyup="showBorn('folderToDate');" name="model.folderToDate" type="text" size="22" value="${model.folderToDate}" maxlength="30">-->
									</td>
								</tr>
								
								<tr>
									<td height="21" width="20%" class="biao_bg1" align="right">
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
									<td height="21" width="20%" class="biao_bg1" align="right">
										<span class="wz">书名：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="folderFormName" name="model.folderFormName" type="text" size="22" value="${model.folderFormName}" class="required" maxlength="50">
									</td>
								</tr>
								<%--	<s:if test="model.folderId!=null">
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
									<td height="21" width="20%" class="biao_bg1" align="right" valign="top">
										<span class="wz">卷(盒)描述：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<textarea  id="testearea" rows="10"   cols="50" name="model.folderDesc">${model.folderDesc}</textarea>
										<br><font color="#999999">( 请不要超过200字 )</font>
									</td>
								</tr>
								<tr>
									<td class="table1_td"></td>
									<td></td>
								</tr>
							</table>
							<table width="90%" border="0" cellspacing="0" cellpadding="00">
								<tr>
									<td align="center" valign="middle">
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
