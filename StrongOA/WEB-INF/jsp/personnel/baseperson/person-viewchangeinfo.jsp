<%@ page contentType="text/html; charset=utf-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
	String oldstruct = (String) request.getAttribute("oldstruct");
	String beforeChangeStr = (String) request
			.getAttribute("beforeChangeStr");
%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>人员调配</title>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<SCRIPT language="javascript"
			src="<%=path%>/oa/js/personnel/common.js"></SCRIPT>
		<script language="javascript" charset="GBK"
			src="<%=root%>/oa/js/personnel/selectbasedata_infoinput.js"></script>
		<SCRIPT language="javascript"
			src="<%=path%>/oa/js/personnel/prototype.js"></SCRIPT>
		<script type="text/javascript" language="javascript"
			src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<SCRIPT language="javascript"
			src="<%=path%>/oa/js/personnel/choosetext.js"></SCRIPT>
		<STYLE>
			@media All{
				input{ 
					behavior: url("<%=root%>/oa/js/personnel/textfield.htc");
				}
			}	
		</STYLE>
		<script language="JavaScript">
		    window.name="MyModalDialog"; 
			var viewchanged=false;
			var even=/^[0,2,4,6,8]+$/; 
		   	var odd=/^[1,3,5,7,9]+$/; 
		   	var flag=1;
			var procount=0;
			var prostr="",comstr="";
			String.prototype.trim = function() {
				var strTrim = this.replace(/(^\s*)|(\s*$)/g, "");
				strTrim = strTrim.replace(/^[\s　\t]+|[\s　\t]+$/, ""); 
			    return strTrim;
			}
   	
		   	function viewtext_Change(){			
				viewchanged=true;
			}
			
		    function getBorn(){//自动获取出生日期
		    	var cardid=document.all.PERSON_CARD_ID.value;
				if(cardid!=""){
					 if(cardid.length==15){
						var bornday=cardid.substring(6,12);
						var sex=cardid.substring(14,15);
						var mon=bornday.substring(2,4);
						if(isNaN(mon)||mon<0||mon>12){
							alert("身份证号中的出生月份错误！");
							document.all.PERSON_CARD_ID.style.color="red";
							document.all.PERSON_CARD_ID.focus();
							return false;
						}
						if(document.all.PERSON_BORN!=undefined){
							document.all.PERSON_BORN.value="19"+bornday.substring(0,2)+"-"+mon+"-"+bornday.substring(4,6);
						}
						getSex(sex);
					}else if(cardid.length==18){
						var bornday=cardid.substring(6,14);
						var sex=cardid.substring(16,17);
						var mon=bornday.substring(4,6);
						if(isNaN(mon)||mon<0||mon>12){
							alert("身份证号中的出生月份错误！");
							document.all.PERSON_CARD_ID.style.color="red";
							document.all.PERSON_CARD_ID.focus();
							return false;
						}
						if(document.all.PERSON_BORN!=undefined){
							document.all.PERSON_BORN.value=bornday.substring(0,4)+"-"+mon+"-"+bornday.substring(6,8);	
						}
						getSex(sex);	
					}else{
						alert("身份证号必须为15位或者18位！");
						document.all.PERSON_CARD_ID.style.color="red";
						document.all.PERSON_CARD_ID.focus();
						return false;					
					}
				}
				return true;
		    }
		    
		    function getSex(sex){//自动获取性别
		    	if(document.all.PERSON_SAX!=undefined){
					if(even.test(sex)){//如果为偶数
						document.all.PERSON_SAX.value=8580;
						document.all.PERSON_SAX_name.value="女性";
					}else if(odd.test(sex)){//如果为奇数						
						document.all.PERSON_SAX.value=8579;
						document.all.PERSON_SAX_name.value="男性";
					}
				}
		    }	
				
			function charge(){
			    submitForm();
			}
			//提交表单
			function submitForm(){	
				var infoSetCode=document.all.infoSetCode.value;	
			    var personId=document.all.personId.value;
			    var orgId=document.getElementById("orgId").value;
				var pros=document.all.propertyList.value;
				var prostr=pros.split(',');
				var flag=false;
				
				//提示必录项
				var checkvalue=document.all.checkvalue;
			    for(var i=0;i<checkvalue.options.length;i++){   
			    	var checkoption=checkvalue.options[i];
			    	if(document.getElementById(checkoption.value).value==""){
			    		alert("["+checkoption.text+"]不能为空！");
			    		return false;
			    	}
			    }
			    
			    //上传文件格式判断   
			  	var fileobjs=$(":file"); 
			    var ispass=true;
			    for(var i=0;i<fileobjs.length;i++){
				    if(fileobjs[i].value!="")
				     	ispass=preview(fileobjs[i],0);
				     if(!ispass){
				     	 return false;
				     }
			    }
			    
			   
			    if(document.getElementById("PERSON_PERSON_KIND")!=undefined){
			         var value=document.getElementById("PERSON_PERSON_KIND").value;
			    	 var personkind=document.getElementById("personkind").value;
			    	 if(personkind==value){
			    	 	alert("人员类别没有变动，不需要调配！");
			    	 	return false;
			    	 }
			    }
	      		//获取编辑后的编制ID
	      		var strucId="";
			    if(document.getElementById("STRUC_ID")!=undefined){
			    	strucId=document.getElementById("STRUC_ID").value;
			    }
			    //如果是人员信息集，并且选择了人员编制类型，判断人员是否已超编	
			    if(infoSetCode=='40288239230c361b01230c7a60f10015'&&strucId!=""&&strucId!=null){
			         //oldStruct存放的是编辑之前的值，存放格式为：编制ID+编制名称	       
			        var oldstruct="<%=oldstruct%>";
			        var flag=false;
			        if(oldstruct!=null&&oldstruct!=""&&oldstruct!="null"){
			        	var values=oldstruct.split(",");
			        	if(strucId==values[0]){	//没有修改人员编制
			        		flag=true;
			        	}
			        }
			        if(!flag){	//修改了人员编制，没有修改编制则不需要判断
				        var strucName=document.getElementById("STRUC_ID_name").value;
				        //判断是否超编
						$.ajax({
							type:"post",
							url:"<%=path%>/personnel/baseperson/person!isOutOfQuata.action",
							data:{
								strucId:strucId,
								orgId:orgId							
							},
							success:function(data){	
							    if(data==null){ //该单位没有编制
							    	alert("该单位没有上编！");
									return false;	
							    }else if(data!=null&&data=="1"){//提示已满编
									if(confirm(strucName+"已满编，是否继续添加该种编制人员？")){
									    document.getElementById("PERSON_IS_OUT_LIMIT").value="1";
		      							document.all.comform.submit(); 	
									}else{
										return false;
									}			
								}else if(data!=null&&data=="2"){//提示已超编
									if(confirm(strucName+"已超编，是否继续添加该种编制人员？")){
									    document.getElementById("PERSON_IS_OUT_LIMIT").value="1";
		      							document.all.comform.submit(); 	
									}else{
										return false;
									}
								}else{	
								    document.getElementById("PERSON_IS_OUT_LIMIT").value="0";
		      						document.all.comform.submit(); 	
								}
							},
							error:function(data){
								alert("对不起，操作异常"+data);
							}
						});
					}else{
						document.all.comform.submit(); 	
					}
				}else{
					//提交表单
					if(strucId==""||strucId==null||strucId=="null"){//如果修改后的编制为空，说明一定不超编
					    if(document.getElementById("PERSON_IS_OUT_LIMIT")!=undefined)
							document.getElementById("PERSON_IS_OUT_LIMIT").value="0";
					}
	      			document.all.comform.submit(); 	
				}	
	   		}
	   			
	   		function cancel(){
	   			window.close();
	   		}   	
	   		
	   		//文件格式判断
			function preview(path,filenum)
			{
				var urlStr = path.value;
				var suburlStr=urlStr.substring(urlStr.lastIndexOf(".")+1);//得到文件类型
				var flag=true;
				switch(suburlStr){
				case "jpg" : flag=true; break;
				case "JPG" : flag=true; break;
				case "jpeg": flag=true; break;
				case "JPEG": flag=true; break;
				case "bmp": flag=true; break;
				case "BMP": flag=true; break;
				case "gif": flag=true; break;
				case "GIF": flag=true; break;
				case "png": flag=true; break;
				case "PNG": flag=true; break;
				case "psd": flag=true; break;
				case "PSD": flag=true; break;
				case "dxf": flag=true; break;
				case "DXF": flag=true; break;
				case "cdr": flag=true; break;
				case "CDR": flag=true; break;
				default: flag=false;break;
				}
				if(flag){	
					return true;
				}else 
				{
					alert("上传的文件中包含非图片文件！请选择图片文件!\n 路径:"+urlStr);
					return false;
				}
		}	
		</script>
	</head>
	<body oncontextmenu="return false;" style="overflow: auto">
		<DIV id=contentborder align=center>
			<form
				action="<%=path%>/personnel/baseperson/person!changePersonInfo.action"
				method="post" name="comform" target="MyModalDialog" enctype="multipart/form-data">
				<input type="hidden" name="propertyList" onmouseout="">
				<input type="hidden" name="defaultshow" value="${defaultshow}">
				<input type="hidden" name="personkind" value="${personkind}">
				<input type="hidden" name="orgId" value="${orgId}">
				<input type="hidden" id="oldInfos" name="deployinfo.oldInfos"
					value="<%=beforeChangeStr%>">
				<input type="hidden" name="personId" value="${personId}">
				<input type="hidden" name="keyid" value="${keyid}">
				<input type="hidden" name="infoSetCode" value="${infoSetCode}">
				<DIV style="display: none">
					<SELECT name="checkvalue">
					</SELECT>
				</DIV>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td height="40"
							style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
							<table width="100%" border="0" cellspacing="0" cellpadding="00">
								<tr>
									<td>&nbsp;</td>
									<td width="20%">
										<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
										人员调配
									</td>
									<td width="*%">
										&nbsp;
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
				<table width="100%" height="10%" border="0" cellpadding="0"
					cellspacing="1" align="center" class="table1">
					<tr>
						<td width="20%" height="21" class="biao_bg1" align="right">
							<span class="wz">调配类别：</span>
						</td>
						<td class="td1" colspan="3" align="left">
							<s:select cssStyle="width: 174px" list="deployTypeList"
								id="deployId" name="deployId" onchange="gochange()"
								listKey="pdepId" listValue="pdepName" theme="simple">
							</s:select>
						</td>
					</tr>
					<s:if test="#request.propertyList!=null">
						<s:iterator id="ite" value="#request.propertyList" status="status">
							<script language="JavaScript">	
							    var field="<s:property value="pro.infoItemField"/>";
							    var defaultshow=document.getElementById("defaultshow").value;	
							    if(defaultshow.indexOf(field)==-1){		 
									prostr+="<s:property value="pro.infoItemField"/>"+",";
								}					
								flag=chooseTextForChange(flag,"<s:property value="pro.infoItemDatatype"/>","<s:property value="pro.infoItemProset"/>","<s:property value="pro.infoItemField"/>","<s:property value="pro.infoItemDictCode"/>","<s:property value="pro.infoItemFlag"/>","<s:property value="pro.infoItemSeconddisplay"/>","<s:property value="pro.infoItemLength"/>","<s:property value="pro.infoItemDecimal"/>","<s:property value="value"/>","<s:property value="pro.properTypeName"/>");																													
							</script>
						</s:iterator>
					</s:if>
					<s:else>
						<tr>
							<td colspan="4">
								该调配类别没有待调配的信息！
							</td>
						</tr>
					</s:else>
					<tr>
						<td width="20%" height="21" class="biao_bg1" align="right">
							<span class="wz">调配原因：</span>
						</td>
						<td class="td1" colspan="3" align="left">
							<textarea id="exchangeWhy" rows="5" cols="58"
								name="deployinfo.exchangeWhy" maxlength="100">${deployinfo.exchangeWhy}</textarea>

						</td>
					</tr>
					<!--机构基本信息结束-->
				</table>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td align="center" height="40" colspan="4">
							<input type="button" class="input_bg" value="保  存"
								onclick="charge()">
							<input type="button" class="input_bg" value="关  闭"
								onclick="javascript:window.close();">
						</td>
					</tr>
				</table>
			</form>
			<script language="JavaScript">
				if(prostr!="")	
					document.all.propertyList.value=prostr;
				if(document.all.PERSON_CARD_ID!=undefined){
					document.all.PERSON_CARD_ID.onblur=getBorn;
				}
				function goback(){
				    var orgId=document.getElementById("orgId").value;
					window.parent.location="<%=path%>/personnel/baseperson/person.action?orgId="+orgId;
				}
				
				function gochange(){
					//document.all.comform.action="<%=path%>/personnel/baseperson/person!viewChangeInfo.action?personId="+personId+"&infoSetCode=40288239230c361b01230c7a60f10015&keyid="+personId+"&deployId="+deployId;    		
					document.all.comform.action="<%=path%>/personnel/baseperson/person!viewChangeInfo.action";
					document.all.comform.submit(); 
				}
			</script>
		</DIV>
	</body>
</html>

