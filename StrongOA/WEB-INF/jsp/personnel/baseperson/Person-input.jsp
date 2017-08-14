<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<title>编辑人员基本信息</title>
		<%@include file="/common/include/meta.jsp"%>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<link href="<%=frameroot%>/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<script language='javascript'
			src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>

		<!--右键菜单样式 -->
		<script language='javascript'
			src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
		<!--右键菜单脚本 -->
		<script language='javascript'
			src='<%=path%>/common/js/jquery/jquery-1.2.6.js'></script>
		<script language="javascript"
			src="<%=path%>/common/js/common/common.js"></script>
		<!--右键菜单脚本 -->
		<script language="javascript"
			src="<%=path%>/common/js/common/search.js"></script>
	</head>
	<body class=contentbodymargin oncontextmenu="return false;">
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<!-- 增加form    -->
		<DIV id=contentborder>
			<s:form action="" id="veteranform" name="veteranform" method="post"
				enctype="multipart/form-data">
				<input type="hidden" id="personid" name="model.personid"
					value="${model.personid }">
				<input type="hidden" id="orgId" name="orgId" value="${orgId }">
				<table width="100%" border="0" cellspacing="0" cellpadding="00"
					style="FILTER: progid :   DXImageTransform .   Microsoft .   Gradient(gradientType =   0, startColorStr =   #ededed, endColorStr =   #ffffff);">
					<br>
					<tr>
						<td>&nbsp;</td>
						<td width="12%">
							<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
							人员基本信息编辑
						</td>
						<td width="1%">
							&nbsp;
						</td>
						<td width="87%">


						</td>
					</tr>
				</table>
				<table width="100%">
					<tr>
						<td height="10">

						</td>
					</tr>
				</table>
				<table width="100%" border="0" cellpadding="0" cellspacing="1"
					class="table1">

					<tr colspan="1" height="30" class="biao_bg1" align="right">
						<td height="30" class="biao_bg1" align="right">
							<span class="wz">工号(<font color="#FF6600">*</font>)：&nbsp;</span>
						</td>
						<td colspan="" class="td1" align="left">
							<input id="personLabourno" 
								name="model.personLabourno" type="text" size="22"
								value="${model.personLabourno }">
							<input type="button" class="input_bg" value="生成工号"
								onclick="createNumber();">

						</td>
						<td rowspan="6" width="15%" class="td1" align="left">
							<img src="<%=root%>${photo}" id="personImg"
								style="width: 125px; height: 155px;">
							<br>

							<input type="file" id="personPhoto" onkeydown="return false;"
								size="10" onchange="showFile();" name="file" class="multi" />
							<input type="hidden" name="photo" id="photo" value="${photo }">
						</td>
					</tr>
					<tr colspan="1" height="30" class="biao_bg1" align="right">
						<td height="30" class="biao_bg1" align="right">
							<span class="wz">姓名(<font color="#FF6600">*</font>)：&nbsp;</span>


						</td>
						<td colspan="" class="td1" align="left">
							<input id="personName" name="model.personName"
								value="${model.personName }" type="text" style="">
							&nbsp;&nbsp;&nbsp;&nbsp;
							<span class="wz">曾用名：&nbsp;</span>
							<input id="personNamed" name="model.personNamed"
								value="${model.personNamed }" type="text" style="">


						</td>
					</tr>

					<tr colspan="1" height="30" class="biao_bg1" align="right">
						<td height="21" class="biao_bg1" align="right">
							<span class="wz">性别(<font color="#FF6600">*</font>)：&nbsp;</span>
						</td>
						<td colspan="" class="td1" align="left">
							<input type="radio" id="personSax1" name="personSax" value="0">
							男
							<input type="radio" id="personSax2" name="personSax" value="1">
							女&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<span class="wz">出生日期(<font color="#FF6600">*</font>)：&nbsp;</span>
							<input id="personBorn" name="model.personBorn" onkeyup="showBorn();"
								value="${model.personBorn }" type="text" style="">
						</td>
					</tr>


					<tr colspan="1" height="30" class="biao_bg1" align="right">
						<td colspan="1" height="30" class="biao_bg1" align="right">
							<span class="wz">身份证号(<font color="#FF6600">*</font>)：&nbsp;</span>
						</td>
						<td colspan="1" class="td1" align="left">
							<input id="personCardId" name="model.personCardId"
								value="${model.personCardId }" type="text" style="width: 80%;"
								class="required number">
						</td>
					</tr>
					<tr colspan="1" height="30" class="biao_bg1" align="right">
						<td height="30" class="biao_bg1" align="right">
							<span class="wz">籍贯(<font color="#FF6600">*</font>)：&nbsp;</span>
						</td>
						<td colspan="" class="td1" align="left">
							<input id="personNativePlace" name="model.personNativePlace"
								value="${model.personNativePlace }" type="text"
								style="width: 80%;" class="required number">
						</td>
					</tr>

					<tr>
						<td colspan="1" height="30" width="21%" class="biao_bg1"
							align="right">
							<span class="wz">出生地：&nbsp;</span>
						</td>
						<td colspan="" class="td1" align="left">
							<input id="personBornPlace" name="model.personBornPlace"
								value="${model.personBornPlace }" type="text"
								style="width: 80%;" class="required number">

						</td>
					</tr>
				</table>
				<table width="100%" border="0" cellpadding="0" cellspacing="1"
					class="table1">

					<tr>

						<td height="21" class="biao_bg1" align="right">
							<span class="wz">学历(<font color="#FF6600">*</font>)：&nbsp;</span>
						</td>
						<td colspan="" class="td1" align="left">
							<select id="personEducation" style="width: 80%"
								name="model.personEducation">
								<s:iterator id="vo" value="eduList">
									<option value="${vo.dictItemCode }" id="${vo.dictItemCode }">
										${vo.dictItemName }
									</option>
								</s:iterator>
							</select>

						</td>
						<td width="25%" colspan="1" height="30" class="biao_bg1"
							align="right">
							<span class="wz">民族：&nbsp;</span>
						</td>
						<td width="25%" colspan="1" class="td1">
							<input id="personNation" name="model.personNation"
								value="${model.personNation }" type="text" style="width: 80%;"
								class="required number">
						</td>


					</tr>
					<tr>
						<td width="20%" colspan="1" height="30" class="biao_bg1"
							align="right">
							<span class="wz">毕业院校：&nbsp;</span>
						</td>
						<td width="30%" colspan="1" class="td1">
							<input id="personEducoll" name="model.personEducoll"
								value="${model.personEducoll }" type="text" style="width: 80%;"
								class="required number">
						</td>
						<td height="30" class="biao_bg1" align="right">
							<span class="wz">政治面貌：&nbsp;</span>
						</td>
						<td class="td1" align="left">
							<select id="personPolical" style="width: 80%"
								name="model.personPolical">
								<s:iterator id="vo" value="policitalList">
									<option value="${vo.dictItemCode }" id="${vo.dictItemCode }">
										${vo.dictItemName }
									</option>
								</s:iterator>
							</select>

						</td>

					</tr>


					<tr>
						<td colspan="1" height="30" class="biao_bg1" align="right">
							<span class="wz">机构(<font color="#FF6600">*</font>)：&nbsp;</span>

						</td>
						<td colspan="1" class="td1" align="left">
							<input type="text" id="orgname" name="org.orgName"
								value="${org.orgName }" style="width: 80%" />
							<input type="hidden" id="orgId" name="org.orgid"
								value="${org.orgid }">
						</td>

						<td height="30" class="biao_bg1" align="right">
							<span class="wz">编制：&nbsp;</span>
						</td>
						<td class="td1" align="left">
							<select id="personStructId" style="width: 80%"
								name="model.personStructId">
								<s:iterator id="vo" value="strucList">
									<option value="${vo.strucId }" id="${vo.strucId }">
										${vo.strucTypeName }
									</option>
								</s:iterator>
							</select>

						</td>
					</tr>
					<tr>



						<td width="20%" colspan="1" height="30" class="biao_bg1"
							align="right">
							<span class="wz">个人身份：&nbsp;</span>
						</td>
						<td width="30%" colspan="1" class="td1">
							<s:select name="" list="#{'0':'领导','1':'非领导'}" listKey="key"
								listValue="value" onchange='$("#img_sousuo").click();'
								style="width:80%" />

						</td>
						<td colspan="1" height="30" class="biao_bg1" align="right">
							<span class="wz">职务：&nbsp;</span>
						</td>
						<td colspan="1" class="td1" align="left">


							<s:select name="model.personPset"
								list="#{'':'职位','0':'职位一','1':'职位二'}" listKey="key"
								listValue="value" onchange='$("#img_sousuo").click();'
								style="width:80%" />
						</td>
					</tr>
					<tr>
						<td height="30" class="biao_bg1" align="right">
							<span class="wz">待遇级别：&nbsp;</span>
						</td>
						<td class="td1" align="left">

							<select id="personTreatmentLevel" style="width: 80%"
								name="model.personTreatmentLevel">
								<s:iterator id="vo" value="daiList">
									<option value="${vo.dictItemCode }" id="${vo.dictItemCode }">
										${vo.dictItemName }
									</option>
								</s:iterator>
							</select>

						</td>
						<td width="20%" colspan="1" height="30" class="biao_bg1"
							align="right">
							<span class="wz">入职日期：&nbsp;</span>
						</td>
						<td width="30%" colspan="1" class="td1">
							<input id="prsonComeDate" name="model.prsonComeDate"
								value="${model.prsonComeDate }" type="text" style="width: 80%;"
								class="required number">
						</td>

					</tr>



				</table>
				<br>
				<table width="100%">
					<tr>
						<td class="td1" colspan="4" align="center" height="21">
							<input name="Submit" type="button" class="input_bg" value="保  存"
								onclick="save();">
							&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
							&nbsp; &nbsp; &nbsp; &nbsp;
							<input name="Submit2" type="button" class="input_bg" value="返  回"
								onclick="go();">
						</td>
					</tr>
				</table>

			</s:form>

			
		</DIV>
		<SCRIPT type="text/javascript">
			    //保存
			function save(){   
			   var personLabourno=$("#personLabourno").val();//工号
			   var personName=$("#personName").val();//姓名
			   var personBorn=$("#personBorn").val();//出生日期
			   var personCardId=$("#personCardId").val();//身份证号码
			   var personNativePlace=$("#personNativePlace").val();//籍贯
			   var orgname=$("#orgname").val();//部门机构
			   var prsonComeDate=$("#prsonComeDate").val();//入职日期
			   var personStructId=document.getElementById("personStructId").value;//编制ID
			   //日期格式正则表达式
			   var time=/^((((19|20)\d{2})-(0?[13-9]|1[012])-(0?[1-9]|[12]\d|30))|(((19|20)\d{2})-(0?[13578]|1[02])-31)|(((19|20)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|((((19|20)([13579][26]|[2468][048]|0[48]))|(2000))-0?2-29))$/;
		       if(personLabourno==""){
			      alert("工号不可以为空！");
			      return false;
			   }else if(personName==""){
			      alert("姓名不可以为空！");
			      return false;
			   }else if(personBorn==""){
			      alert("出生日期不可以为空！");
			      return false;
			   }else if(!time.test(personBorn)){
			      alert("出生日期格式不正确！");
			      return false;
			   }else if(personCardId==""){
			      alert("身份证号码不可以为空！");
			      return false;
			   }else if(personNativePlace==""){
			      alert("籍贯不可以为空！");
                  return false;
			   }else if(orgname==""){
			      alert("机构不可以为空！");
			      return false;
			   }else if(prsonComeDate!=""&&!time.test(prsonComeDate)){
			      alert("入职日期格式不正确！");
			      return false;
			   }else if(personStructId!=""){
			      $.post("<%=path%>/personnel/structure/personStructure!structureState.action",
			      {"structureId":personStructId},
			        function(data){
			       
			        if(data=="1"){
			           if(!confirm("该编制已经满编，是否继续添加?")){
			              return false;
			           }
			        }else if(data=="2"){
			           if(!confirm("该编制已经超编，是否继续添加?")){
			              return false;
			           }
			        }
			        });
			   }
		       var veteranform=document.getElementById("veteranform");//from表单
			   veteranform.action="<%=path%>/personnel/baseperson/person!save.action";
			   $("#veteranform").submit();
			}
			//上传图片
			function showFile(){
			   var personPhoto=document.getElementById("personPhoto").value;
			   var img=document.getElementById("personImg");
			   img.src=personPhoto;
			   $("#photo").val(personPhoto);
			}
			//生成工号
		    function createNumber(){
   		       var ret=  OpenWindow("<%=root%>/serialnumber/number/number!input.action", 
                                   		700, 400, window);
   		     }
   		   //返回
   		    function go(){
   		      window.history.go(-1);
   		    }
   		    
   		    function showBorn(){
   		       var personBorn=$("#personBorn").val();//出生日期
   		       if(personBorn.length==4){
   		         $("#personBorn").val(personBorn+"-");
   		       }else if(personBorn.length==7){
   		         $("#personBorn").val(personBorn+"-");
   		       }else if(personBorn.length>9){
   		         $("#personBorn").val(personBorn);
   		       }
   		    }
		</SCRIPT>
	</body>
</html>
