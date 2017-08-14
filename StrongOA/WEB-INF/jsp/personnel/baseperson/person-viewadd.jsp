<%@ page contentType="text/html; charset=utf-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>
				添加人员子集信息
		</title>
		<link type="text/css" rel="stylesheet"
			href="<%=path%>/oa/css/personnel/windows.css">
		<LINK href="<%=frameroot%>/css/windows.css" type=text/css rel=stylesheet>
		<link href="<%=frameroot%>/css/properties_windows.css" type="text/css" rel=stylesheet>
		<SCRIPT language="javascript"
			src="<%=path%>/oa/js/personnel/common.js"></SCRIPT>
		<script language="javascript" charset="GBK"
			src="<%=root%>/oa/js/personnel/selectbasedata_infoinput.js"></script>
		<SCRIPT language="javascript"
			src="<%=path%>/oa/js/personnel/choosetext.js"></SCRIPT>
		<SCRIPT language="javascript"
			src="<%=path%>/oa/js/personnel/prototype.js"></SCRIPT>
		<script type="text/javascript" language="javascript"
			src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<STYLE>
			@media All{
				input{ 
					behavior: url("<%=root%>/oa/js/personnel/textfield.htc");
				}
			}
			TBODY {
				HEIGHT: 0px
			}	
		</STYLE>
	<script language="JavaScript">
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
					document.all.PERSON_SAX.value="4028822723fa40580123fa76c50c0003";
					document.all.PERSON_SAX_name.value="女";
				}else if(odd.test(sex)){//如果为奇数						
					document.all.PERSON_SAX.value="4028822723fa40580123fa768e4c0002";
					document.all.PERSON_SAX_name.value="男";
				}
			}
	    }	
	   		
	   	//判断是否有可提交的数据
   		function checkIsChange(){
   			var pros=document.all.propertyList.value;
			var prostr=pros.split(',');
			var flag=false;
			for(var i=0;i<prostr.length;i++){
				var proid=document.getElementById(prostr[i]);
				var proname=document.getElementById(prostr[i]+"_name");
				if(prostr[i]!=null&&prostr[i]!=""&&((proid!=undefined&&proid.type!="hidden")||(proname!=undefined&&proname.value!=""))){
					var value=document.getElementById(prostr[i]).value;
					if(value!=null&&value!=""){
						flag=true;
					}
				}
			}
			if(!flag){
				alert("没有可提交的数据！");
				return false;
			}
   		}
   		
   		//提示必录项
   		function checkMustInput(){
			var checkvalue=document.all.checkvalue;
		    for(var i=0;i<checkvalue.options.length;i++){
		    	var checkoption=checkvalue.options[i];
		    	if(document.getElementById(checkoption.value).value==""){
		    		alert("["+checkoption.text+"]不能为空！");
		    		return false;
		    	}
		    }
   		} 
   		
   		//上传文件格式判断   
   		function checkFile(){	
		  	var fileobjs=$(":file"); 
		    var ispass=true;
		    for(var i=0;i<fileobjs.length;i++){
			    if(fileobjs[i].value!="")
			     	ispass=preview(fileobjs[i],0);
			     if(!ispass){
			     	 return false;
			     }
		    }
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
		
		//文本域大小控制
		function checkTexteara(){	 
		    var textobj=$("textarea");
		    for(var i=0;i<textobj.length;i++){
			    if(textobj[i].value!=null&&textobj[i].value!="")
			    	var len=textobj[i].maxlength;
			     	var vlen=textobj[i].value.length;
			    	if(vlen*2>len){
		    			alert(textobj[i].label+"请不要超过" + len/2+"字");
		    			textobj[i].focus();
		    			return false;
		    	}
		    }
		}
		
		
		//取消	
   		function cancel(){
   			window.close();
   		} 
   		
   		//判断职数是否超编
   		function checkPerStatus(){
   			var orgId=document.getElementById("orgId").value;
		    var personId=document.getElementById("personId").value;
			var personstatus=document.getElementById("PERSON_STATUS").value; //人员身份  
			var statusName=document.getElementById("PERSON_STATUS_name").value;
			$.ajax({ 	//判断是否超编
				type:"post",
				url:"<%=path%>/personnel/baseperson/person!isOutOfNum.action",
				data:{
					orgId:orgId,
					personId:personId,
					personstatus:personstatus				
				},
				success:function(data){	
				    if(data==null){ //该单位没有编制
				    	alert("该单位没有上编！");
						return false;	
				    }else if(data=="3"){//提示职数已超编
					    alert(statusName+"已满编,不能再添加该种身份的人员！");
					    return false;
					}else{
						document.all.comform.submit(); 	
					}
				},
				error:function(data){
					alert("对不起，操作异常"+data);
					return false;
				}
			});
   		}
   		
   		//判断编制是否超编
   		function checkBianzhi(){
  			var strucId=document.getElementById("STRUC_ID").value;			//获取编辑后的编制ID
	        if(strucId!=""&&strucId!=null){	//编制不为空
	        	var orgId=document.getElementById("orgId").value;
	        	var personId=document.getElementById("personId").value;
	        	var strucName=document.getElementById("STRUC_ID_name").value;  
				var personstatus=document.getElementById("PERSON_STATUS").value; //人员身份  
				var statusName=document.getElementById("PERSON_STATUS_name").value;
				$.ajax({ 	//判断是否超编
					type:"post",
					url:"<%=path%>/personnel/baseperson/person!isOutOfQuata.action",
					data:{
						strucId:strucId,
						orgId:orgId,
						personstatus:personstatus					
					},
					success:function(data){	
					    if(data==null){ //该单位没有编制
					    	alert("该单位没有上编！");
							return false;	
					    }else if(data=="1"){//提示已满编
							if(confirm(strucName+"已满编，是否继续添加该种编制人员？")){
							    document.getElementById("PERSON_IS_OUT_LIMIT").value="1";
							    document.all.comform.submit(); 	
							}else{
								return false;
							}			
						}else if(data=="2"){//提示已超编
							if(confirm(strucName+"已超编，是否继续添加该种编制人员？")){
							    document.getElementById("PERSON_IS_OUT_LIMIT").value="1";
							    document.all.comform.submit(); 	
							}else{
								return false;
							}
						}else if(data=="3"){
							  alert(statusName+"已满编,不能再添加该种身份的人员！");
				    		  return false;
						}else{	
							document.getElementById("PERSON_IS_OUT_LIMIT").value="0";
							document.all.comform.submit(); 	
						}
					},
					error:function(data){
						alert("对不起，操作异常"+data);
						return false;
					}
				});			
			}else{	//编辑后编制为空，说明一定不超编
				document.getElementById("PERSON_IS_OUT_LIMIT").value="0";
				checkPerStatus()
			}	
   		}
   		
   		//提交表单
		function submitForm(value){
			document.getElementById("forward").value=value;
		    var infoSetCode=document.all.infoSetCode.value;	
			if(checkIsChange()==false)	return false;		//判断是否有可提交的数据
			if(checkMustInput()==false)	return false;		//提示必录项
		    if(checkFile()==false)		return false;		//上传文件格式判断   
			if(checkTexteara()==false)  return false; 		//文本域大小控制 
		    if(infoSetCode=='40288239230c361b01230c7a60f10015'){//如果是人员信息集
		        checkBianzhi();//是否超编判断
			}else{
				document.all.comform.submit(); 	
			}
		}  
	   		
	   		
		</script>
	</head>
	<body class="contentbodymargin"
		oncontextmenu="return false;">
		<DIV id=contentborder cellpadding="0">
		 <s:if test="forward==\"addRelationInfo\"">
			<form
				action="<%=path%>/personnel/baseperson/person!viewAddPerson.action"
				method="post" name="comform" target="hidden_frame" enctype="multipart/form-data">
		</s:if>
		<s:else>
			<form
				action="<%=path%>/personnel/baseperson/person!viewAddPerson.action"
				method="post" name="comform" enctype="multipart/form-data">
		</s:else>
				<input type="hidden" name="propertyList" onmouseout="">
				<input type="hidden" name="orgId" value="${orgId}">
				<input type="hidden" name="personId" value="${personId}">
				<input type="hidden" name="infoSetCode" value="${infoSetCode}">
				<input type="hidden" name="forward" value="${forward}">
				<DIV style="display: none">
					<SELECT name="checkvalue">
					</SELECT>
				</DIV>
				   <s:if test="forward==\"addRelationInfo\"">
				   	<table width="100%" border="0" cellspacing="0" cellpadding="0"
					style="vertical-align: top;">
					<tr>
						<td height="100%">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td height="40"
										style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
										<table width="100%" border="0" cellspacing="0"
											cellpadding="00">
											<tr>
												<td>&nbsp;</td>
												<td width="50%">
													<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
														height="9">&nbsp;
													添加人员子集信息
												</td>
												<td width="10%">
													&nbsp;

												</td>
												<td width="35%">
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
				   </s:if>
					<table border="0" width="95%" bordercolor="#FFFFFF" align="center" cellspacing="0"
						cellpadding="0">
						<tr>
							<td width="100%">
								<table width="100%">
								    <s:if test="#request.proList!=null">
										<tr>
											<td colspan="4">
												<s:iterator id="ite" value="#request.proList" status="status">    							
													<s:if test="infoItemField==\"ORG_ID\"">
														<script language="JavaScript">
															prostr+="<s:property value="infoItemField"/>"+",";
															flag=chooseText(flag,"<s:property value="infoItemDatatype"/>","<s:property value="infoItemProset"/>","<s:property value="infoItemField"/>","<s:property value="infoItemDictCode"/>","<s:property value="infoItemFlag"/>","<s:property value="infoItemSeconddisplay"/>","<s:property value="infoItemLength"/>","<s:property value="infoItemDecimal"/>","<s:property value="orgId"/>","<s:property value="properTypeName"/>");																																														
														</script>
													</s:if>	
													<s:elseif test="infoItemField==\"PERSON_IS_OUT_LIMIT\"">		
														<script language="JavaScript">				 
															prostr+="<s:property value="infoItemField"/>"+",";
															flag=chooseText(flag,"<s:property value="infoItemDatatype"/>","1","<s:property value="infoItemField"/>","<s:property value="infoItemDictCode"/>","<s:property value="infoItemFlag"/>","<s:property value="infoItemSeconddisplay"/>","<s:property value="infoItemLength"/>","<s:property value="infoItemDecimal"/>","<s:property value="infoItemDefaultvalue"/>","<s:property value="properTypeName"/>");																					
														</script>
													</s:elseif>			
													<s:elseif test="infoItemField==pkey">		
														<script language="JavaScript">
															flag=chooseText(flag,"<s:property value="infoItemDatatype"/>","<s:property value="infoItemProset"/>","<s:property value="infoItemField"/>","<s:property value="infoItemDictCode"/>","<s:property value="infoItemFlag"/>","<s:property value="infoItemSeconddisplay"/>","<s:property value="infoItemLength"/>","<s:property value="infoItemDecimal"/>","","<s:property value="properTypeName"/>");																										
														</script>
													</s:elseif>
													<s:elseif test="infoItemField ==\"PERSON_PERSON_KIND\"">
														<script language="JavaScript">
															prostr+="<s:property value="infoItemField"/>"+",";
															flag=chooseText(flag,"<s:property value="infoItemDatatype"/>","<s:property value="infoItemProset"/>","<s:property value="infoItemField"/>","<s:property value="infoItemDictCode"/>","<s:property value="infoItemFlag"/>","<s:property value="infoItemSeconddisplay"/>","<s:property value="infoItemLength"/>","<s:property value="infoItemDecimal"/>","<s:property value="persontype"/>,<s:property value="typename"/>","<s:property value="properTypeName"/>");																										
														</script>
													</s:elseif>
													<s:elseif test="infoItemField ==\"PERSONID\"&&infoSetValue!=\"T_OA_BASE_PERSON\"">
														<script language="JavaScript">
															prostr+="<s:property value="infoItemField"/>"+",";
															flag=chooseText(flag,"<s:property value="infoItemDatatype"/>","<s:property value="infoItemProset"/>","<s:property value="infoItemField"/>","<s:property value="infoItemDictCode"/>","<s:property value="infoItemFlag"/>","<s:property value="infoItemSeconddisplay"/>","<s:property value="infoItemLength"/>","<s:property value="infoItemDecimal"/>","<s:property value="personId"/>","<s:property value="properTypeName"/>");																										
														</script>
													</s:elseif>				
													<s:else>
														<script language="JavaScript">				 
															prostr+="<s:property value="infoItemField"/>"+",";
															flag=chooseText(flag,"<s:property value="infoItemDatatype"/>","<s:property value="infoItemProset"/>","<s:property value="infoItemField"/>","<s:property value="infoItemDictCode"/>","<s:property value="infoItemFlag"/>","<s:property value="infoItemSeconddisplay"/>","<s:property value="infoItemLength"/>","<s:property value="infoItemDecimal"/>","<s:property value="infoItemDefaultvalue"/>","<s:property value="properTypeName"/>");																					
														</script>
													</s:else>		
												</s:iterator>
											</td>
										</tr>
									</s:if>
									<s:else>
								  		<tr>
											<td colspan="4">
												没有结构
											</td>
										</tr>
									</s:else>
									<!--机构基本信息结束-->
								</table>
								<script language="JavaScript">
									if(prostr!="")	
										document.all.propertyList.value=prostr;
									if(document.all.PERSON_CARD_ID!=undefined){
										document.all.PERSON_CARD_ID.onblur=getBorn;
									}
								</script>
							</td>
						</tr>
					</table>
					<br>
					<table border="0" width="95%" cellspacing="0"
						 cellpadding="00">
						<tr>
							<td align="center" colspan="4">			
									<s:if test="forward==\"addRelationInfo\"">
										<INPUT type="button" class="input_bg" value="保  存"
										onclick="submitForm('addRelationInfo')">
										<INPUT type="button" class="input_bg" value="关  闭"
										onclick="window.close()">
									</s:if>
									<s:else>
										<INPUT type="button" class="input_bg" value="保  存"
										onclick="submitForm()">
										<INPUT type="button" class="input_bg" value="保存并关闭"
										onclick="submitForm('saveAndClose')">
										<INPUT type="button" class="input_bg" value="保存并新增"
										onclick="submitForm('saveAndAdd')">		
									</s:else>
							</td>
						</tr>
					</table>
					<br>
					<br>
					<table width="98%" border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td></td>
							<td>
								<font color="gray">提示：1、标识为淡蓝色的输入项需双击，弹出选项树</font>
							</td>
						</tr>
					</table>
		</DIV>  
		<iframe name='hidden_frame' id="hidden_frame" style='display:none'></iframe>
		</form>	
		
	</body>
</html>

