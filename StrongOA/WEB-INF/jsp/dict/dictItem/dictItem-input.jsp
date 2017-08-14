<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<title>保存字典项</title>
		<%@include file="/common/include/meta.jsp"%>
		<link href="<%=frameroot%>/css/properties_windows_add.css" type=text/css rel=stylesheet>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/upload/jquery.MultiFile.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/upload/jquery.blockUI.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/validate/jquery.validate.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/validate/formvalidate.js" type="text/javascript" language="javascript"></script>
		<script language="javascript">
			function getTree()
			{   
    			var dictCode = document.getElementById("dictCode").value;   
    			var dictItemCode = document.getElementById("dictItemCode").value; 
    			var dictItemParentvalue = document.getElementById("dictItemParentvalue").value; 
				window.showModalDialog(scriptroot+"/dict/dictItem/dictItem!tree.action?dictCode="+dictCode+"&model.dictItemCode="+dictItemCode+"&model.dictItemParentvalue="+dictItemParentvalue,window,'dialogWidth:310pt ;dialogHeight:300pt;status:no;help:no;scroll:yes;status:0;help:0;scroll:1;');
				//window.open(scriptroot + "/dict/dictItem/dictItem!tree.action?dictCode="+dictCode+"&model.dictItemCode="+dictItemCode,'uccomwindow','height=300,width=250,top=300,left=200,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
			}
			
			function getvalue(id,value){
				document.getElementById("dictItemParentvalue").value=id;
				document.getElementById("dictItemParentName").value=value;
			}
			
			function getFirstwORD(){
				var SourceStr=document.getElementById("dictItemShortdesc").value;
				document.getElementById("dictItemDescspell").value="";
				for(var i=0;i<SourceStr.length;i++){
					var subStr=SourceStr.substring(i,i+1);
					if(subStr!=""&&isNaN(subStr)){//为空或为数字不处理

						var str=/^[\u0391-\uFFE5]+$/;
						if(str.test(subStr)){//如果为中文

							getpy(subStr);
						}
						else{
							document.getElementById("dictItemDescspell").value+=subStr;
						}
					}
					else
						document.getElementById("dictItemDescspell").value+="";
 				}
			}
			
			function getsubmit(){
				var te = "";
				for(var i=0;i<document.getElementsByName("model.dictItemIsSelect").length;i++){
		           if(document.getElementsByName("model.dictItemIsSelect")[i].checked==true){
		              te = document.getElementsByName("model.dictItemIsSelect")[i].value;
		              break;
		           }
       			}
				
       			if(te==''){
       				alert("请选择可选状态。");
       				return false;
       			}
       			//document.getElementById("DictitemForm").submit();
			}
			
			//清空【父级字典项】
			function empty(){
				$("#dictItemParentName").val("");
				$("#dictItemParentvalue").val("");
			}
			function test(){
				var msg = '';
				//字典项值
				var dictItemValue = document.getElementById("dictItemValue").value;
				//字典项名称
				 var dictItemName = document.getElementById("dictItemName").value;
				 //字典项简称 
				 var dictItemShortdesc = document.getElementById("dictItemShortdesc").value;
				 if($.trim(dictItemValue)==""){
						if(msg==""){
						  document.getElementById("dictItemValue").focus();
						}
						msg="字典项值不能为空。\n";
						
					}
					
					
					if($.trim(dictItemName)==""){
						
						//alert("系统链接不能为空");
						if(msg==""){
						 document.getElementById("dictItemName").focus();
						}
						msg+="字典项名称不能为空。\n";
						//return false;
					}
					if($.trim(dictItemShortdesc)==""){
						
						if(msg==""){
							document.getElementById("dictItemShortdesc").focus();
						}
						msg+="字典项简称不能为空。";
						//return false;
					}
					if(msg!=""){
						alert(msg);
						return false;
					}
				document.getElementById("DictitemForm").submit();
			}
		</script>
		
		<script LANGUAGE=vbscript>
			function getpychar(char)
				tmp=65536+asc(char)
				if(tmp>=45217 and tmp<=45252) then 
					getpychar= "A"
				elseif(tmp>=45253 and tmp<=45760) then
					getpychar= "B"
				elseif(tmp>=45761 and tmp<=46317) then
					getpychar= "C"
				elseif(tmp>=46318 and tmp<=46825) then
				 	getpychar= "D"
				elseif(tmp>=46826 and tmp<=47009) then 
					getpychar= "E"
				elseif(tmp>=47010 and tmp<=47296) then 
					getpychar= "F"
				elseif(tmp>=47297 and tmp<=47613) then 
					getpychar= "G"
				elseif(tmp>=47614 and tmp<=48118) then
					getpychar= "H"
				elseif(tmp>=48119 and tmp<=49061) then
					getpychar= "J"
				elseif(tmp>=49062 and tmp<=49323) then 
					getpychar= "K"
				elseif(tmp>=49324 and tmp<=49895) then 
					getpychar= "L"
				elseif(tmp>=49896 and tmp<=50370) then 
					getpychar= "M"
				elseif(tmp>=50371 and tmp<=50613) then 
					getpychar= "N"
				elseif(tmp>=50614 and tmp<=50621) then 
					getpychar= "O"
				elseif(tmp>=50622 and tmp<=50905) then
					getpychar= "P"
				elseif(tmp>=50906 and tmp<=51386) then 
					getpychar= "Q"
				elseif(tmp>=51387 and tmp<=51445) then 
					getpychar= "R"
				elseif(tmp>=51446 and tmp<=52217) then 
					getpychar= "S"
				elseif(tmp>=52218 and tmp<=52697) then 
					getpychar= "T"
				elseif(tmp>=52698 and tmp<=52979) then 
					getpychar= "W"
				elseif(tmp>=52980 and tmp<=53640) then 
					getpychar= "X"
				elseif(tmp>=53689 and tmp<=54480) then 
					getpychar= "Y"
				elseif(tmp>=54481 and tmp<=62289) then
					getpychar= "Z"
				end if
			end function

			function getpy(str)
					getpy=getpy&getpychar(str)
					DictitemForm.dictItemDescspell.value=DictitemForm.dictItemDescspell.value+getpy
			end function
	</script>
	</head>
	<base target="_self" />
	<body class=contentbodymargin oncontextmenu="return false;">
		<DIV id=contentborder align=center>
			<s:form id="DictitemForm" theme="simple" action="/dict/dictItem/dictItem!save.action">
				<table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
				<tr>
					<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
							   <s:if test="dictItemCode==null">
										<strong>新建字典项</strong>
									</s:if>
									<s:else>
										<strong>编辑字典项</strong>
									</s:else>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
					                <tr>
					                 <s:if test="model.dictItemIsSystem==\"1\"">
												
											</s:if>
											<s:else>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onclick="test();">&nbsp;保&nbsp;存&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
				                  	 </s:else>
				                  		<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
					                 	<td class="Operation_input1" onclick="window.close();">&nbsp;关&nbsp;闭&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
				                  		<td width="6"></td>
					                </tr>
					            </table>
							</td>
							
						</tr>
					</table>
				</td>
				</tr>
				<tr>
					<td>
					
							<table  border="0" cellpadding="0" cellspacing="0" class="table1" width="100%" >
								<tr>
									<td  class="biao_bg1" align="right">
										<span class="wz">所属类名称：</span>
									</td>
									<td class="td1" style="margin-left: 5px;">
										<span class="wz">${dictName}</span>
										<input type="hidden" id="dictItemCode" name="model.dictItemCode" value="${model.dictItemCode}" />
										<input id="dictCode" name="model.toaSysmanageDict.dictCode" value="${dictCode}" type="hidden">
									</td>
								</tr>
								<tr>
									<td  class="biao_bg1" align="right">
										<span class="wz">父级字典项：</span>
									</td>
									<td class="td1" style="margin-left: 5px;">
										<input type="text" id="dictItemParentName" name="dictItemParentName" readonly="readonly" size="27" value="${itemParentName}" />
										<input type="hidden" id="dictItemParentvalue" name="model.dictItemParentvalue" value="${model.dictItemParentvalue}" />
										<a  href="#" class="button" onclick="getTree()">选择</a>&nbsp;
										<a   href="#" class="button" onclick="empty()">清空</a>&nbsp;
										
									</td>
								</tr>
								<tr>
									<td class="biao_bg1" align="right">
										<span class="wz"><FONT color="red">*</FONT>&nbsp;字典项值：</span>
									</td>
									<td class="td1" style="margin-left: 5px;">
										<input id="dictItemValue" name="model.dictItemValue" value="${model.dictItemValue}" type="text" size="27" class="required" maxlength="25">
									</td>
								</tr>
								<tr>
									<td  class="biao_bg1" align="right">
										<span class="wz"><FONT color="red">*</FONT>&nbsp;字典项名称：</span>
									</td>
									<td class="td1" style="margin-left: 5px;">
										<input id="dictItemName" name="model.dictItemName" value="${model.dictItemName}" type="text" size="27" class="required" maxlength="25">
									</td>
								</tr>
								<tr>
									<td  class="biao_bg1" align="right">
										<span class="wz"><FONT color="red">*</FONT>&nbsp;字典项简称：</span>
									</td>
									<td class="td1" style="margin-left: 5px;">
										<input id="dictItemShortdesc" name="model.dictItemShortdesc" value="${model.dictItemShortdesc}" type="text" size="27" onKeyUp="getFirstwORD()" class="required" maxlength="25">
									</td>
								</tr>
								<tr>
									<td  class="biao_bg1" align="right">
										<span class="wz">查询码：</span>
									</td>
									<td class="td1" style="margin-left: 5px;">
										<input id="dictItemDescspell" name="model.dictItemDescspell" value="${model.dictItemDescspell}" type="text" size="27" maxlength="25">
									</td>
								</tr>
								<tr>
									<td  class="biao_bg1" align="right">
										<span class="wz"><FONT color="red">*</FONT>&nbsp;可选状态：</span>
									</td>
									<td class="td1" style="margin-left: 5px;">
										
										<s:select name="model.dictItemIsSelect"
											list="#{'0':'可选','1':'不可选'}"
											listKey="key" listValue="value" />
										
										(可选状态的字典项才可作为具体表的字典值)
									</td>
								</tr>
								<tr>
											<td class="table1_td"></td>
											<td></td>
						               </tr>
							</table>
							<table id="annex" width="90%" height="10%" border="0" cellpadding="0" cellspacing="1" align="center" class="table1">
							</table>
							
						</td>
					</tr>
				</table>
			</s:form>
		</DIV>
	</body>
</html>
