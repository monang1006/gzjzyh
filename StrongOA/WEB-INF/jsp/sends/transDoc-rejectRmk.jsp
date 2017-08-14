<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:directive.page import="java.text.SimpleDateFormat,com.strongit.oa.util.GlobalBaseData"/>
<%@ page language="java" import="java.net.URLDecoder"  %>

<%@ taglib uri="/struts-tags" prefix="s"%>
<%@taglib uri="/tags/web-remind" prefix="strong"%>
<%@include file="/common/include/rootPath1.jsp"%>


<html>

<head>


<title>南昌市人民政府办公厅退文告知单</title>
<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="<%=frameroot%>/css/properties_windows.css">
<style>
<!--
 /* Font Definitions */
 @font-face
	{font-family:宋体;
	panose-1:2 1 6 0 3 1 1 1 1 1;
	mso-font-alt:SimSun;
	mso-font-charset:134;
	mso-generic-font-family:auto;
	mso-font-pitch:variable;
	mso-font-signature:3 680460288 22 0 262145 0;}
@font-face
	{font-family:"\@宋体";
	panose-1:2 1 6 0 3 1 1 1 1 1;
	mso-font-charset:134;
	mso-generic-font-family:auto;
	mso-font-pitch:variable;
	mso-font-signature:3 680460288 22 0 262145 0;}
 /* Style Definitions */
 p.MsoNormal, li.MsoNormal, div.MsoNormal
	{mso-style-parent:"";
	margin:0cm;
	margin-bottom:.0001pt;
	text-align:justify;
	text-justify:inter-ideograph;
	mso-pagination:none;
	font-size:10.5pt;
	mso-bidi-font-size:12.0pt;
	font-family:"Times New Roman";
	mso-fareast-font-family:宋体;
	mso-font-kerning:1.0pt;}
span.GramE
	{mso-style-name:"";
	mso-gram-e:yes;}
 /* Page Definitions */
 @page
	{mso-page-border-surround-header:no;
	mso-page-border-surround-footer:no;}
@page Section1
	{size:595.3pt 841.9pt;
	margin:72.0pt 90.0pt 72.0pt 90.0pt;
	mso-header-margin:42.55pt;
	mso-footer-margin:49.6pt;
	mso-paper-source:0;
	layout-grid:15.6pt;}
div.Section1
	{page:Section1;}
 /* List Definitions */
 @list l0
	{mso-list-id:675886213;
	mso-list-type:hybrid;
	mso-list-template-ids:246552058 -1138092626 67698713 67698715 67698703 67698713 67698715 67698703 67698713 67698715;}
@list l0:level1
	{mso-level-tab-stop:18.0pt;
	mso-level-number-position:left;
	margin-left:18.0pt;
	text-indent:-18.0pt;
	mso-ansi-font-size:10.5pt;
	color:windowtext;}
ol
	{margin-bottom:0cm;}
ul
	{margin-bottom:0cm;}
-->
</style>

<script type="text/javascript">	
//提交
		function save(){ 
			var groupName = $.trim($("#docRecvRemark").val()); 
			var qita = $.trim($("#qita").val()); 
			var flag =false;
			for(i =0;i<13;i++)
			{
				
				if(myTable.liyou[i].checked)
				{
					flag = true;
					
				}
			}
			if(!flag)
			{
				alert("请选择退文理由！");
				return false;
			}
			if(myTable.liyou[12].checked)
			{
				if(qita=="")
				{
					alert("其他原因不能为空！");
					$("#qita").focus();
					return false;
				}
				
			}
			if(!myTable.liyou[12].checked&&qita!="")
			{
				alert("填写了其他原因，请勾选！");
				return false;
			}
			if($("#qita").val().length>200)
			{
				alert("其他原因过长！");
				return false;
			}
			
			/*
			if(groupName == ""){
				alert("拒收原因不能为空！");
				$("#docRecvRemark").focus();
				return false;
			}*/
			if(groupName.length > 100)
			 {
			  alert("输入的拒收原因长度不能超过100个字符!");
			  $("#docRecvRemark").focus();
			  return false;
			 }
			var taskId = $("#taskId").val();
			if(taskId==""|taskId==null){
				alert();
				return;
			}
			document.getElementById("recvState").value = "2";
			document.getElementById("myTable").submit();
		}
</script>

</head>
<base target="_self">
<body lang=ZH-CN style='tab-interval:21.0pt;text-justify-trim:punctuation;overflow: auto;'>
<s:form action="/receives/recvDoc!mysave.action" id="myTable" enctype="multipart/form-data" theme="simple">
<div class=Section1 style='layout-grid:15.6pt'>

<%--<p class=MsoNormal><span lang=EN-US style='font-size:12.0pt'><o:p>&nbsp;</o:p></span></p>--%>
<div style="margin-top: 20px"></div>

<p class=MsoNormal align=center style='margin-left:18.0pt;text-align:center'><b
style='mso-bidi-font-weight:normal'><span style='font-size:16.0pt;font-family:
宋体;mso-ascii-font-family:"Times New Roman";mso-hansi-font-family:"Times New Roman"'>南昌市人民政府办公厅退文告知单</span></b><b
style='mso-bidi-font-weight:normal'><span lang=EN-US style='font-size:16.0pt'><o:p></o:p></span></b></p>

<p class=MsoNormal align=center style='margin-left:18.0pt;text-align:center'><span
lang=EN-US><o:p>&nbsp;</o:p></span></p>

<div align=center>

<table class=MsoTableGrid border=1 cellspacing=0 cellpadding=0
 style='margin-left:5.4pt;border-collapse:collapse;border:none;mso-border-alt:
 solid windowtext .5pt;mso-yfti-tbllook:480;mso-padding-alt:0cm 5.4pt 0cm 5.4pt;
 mso-border-insideh:.5pt solid windowtext;mso-border-insidev:.5pt solid windowtext'>
 <tr style='mso-yfti-irow:0;mso-yfti-firstrow:yes;height:18.45pt'>
  <td width=36 rowspan=6 valign=top style='width:27.0pt;border:solid windowtext 1.0pt;
  mso-border-alt:solid windowtext .5pt;padding:0cm 5.4pt 0cm 5.4pt;height:18.45pt'>
  <p class=MsoNormal align=center style='text-align:center'><span lang=EN-US><o:p>&nbsp;</o:p></span></p>
  <p class=MsoNormal align=center style='text-align:center'><span lang=EN-US><o:p>&nbsp;</o:p></span></p>
  <p class=MsoNormal align=center style='text-align:center'><span
  style='font-family:宋体;mso-ascii-font-family:"Times New Roman";mso-hansi-font-family:
  "Times New Roman"'>格</span></p>
  <p class=MsoNormal align=center style='text-align:center'><span
  style='font-family:宋体;mso-ascii-font-family:"Times New Roman";mso-hansi-font-family:
  "Times New Roman"'>式</span></p>
  <p class=MsoNormal align=center style='text-align:center'><span
  style='font-family:宋体;mso-ascii-font-family:"Times New Roman";mso-hansi-font-family:
  "Times New Roman"'>方</span></p>
  <p class=MsoNormal align=center style='text-align:center'><span
  style='font-family:宋体;mso-ascii-font-family:"Times New Roman";mso-hansi-font-family:
  "Times New Roman"'>面</span></p>
  </td>
  <td width=432 valign=top style='width:324.0pt;border:solid windowtext 1.0pt;
  border-left:none;mso-border-left-alt:solid windowtext .5pt;mso-border-alt:
  solid windowtext .5pt;padding:0cm 5.4pt 0cm 5.4pt;height:18.45pt'>
  <p class=MsoNormal style='text-indent:31.5pt;mso-char-indent-count:3.5'><span
  lang=EN-US style='font-size:9.0pt'>1</span><span style='font-size:9.0pt;
  font-family:宋体;mso-ascii-font-family:"Times New Roman";mso-hansi-font-family:
  "Times New Roman"'>、请示文件只能一文一事，<span class=GramE>不能主</span>送多个机关</span><span
  lang=EN-US style='font-size:9.0pt'><o:p></o:p></span></p>
  </td>
  <td width=24 valign=top style='width:18.0pt;border:solid windowtext 1.0pt;
  border-left:none;mso-border-left-alt:solid windowtext .5pt;mso-border-alt:
  solid windowtext .5pt;padding:0cm 5.4pt 0cm 5.4pt;height:18.45pt'>
  <p class=MsoNormal><span lang=EN-US><o:p><input id="liyou" value="11" name="liyou" type="checkbox" /></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:1;height:18.45pt'>
  <td width=432 valign=top style='width:324.0pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0cm 5.4pt 0cm 5.4pt;height:18.45pt'>
  <p class=MsoNormal style='text-indent:31.5pt;mso-char-indent-count:3.5'><span
  lang=EN-US style='font-size:9.0pt'>2</span><span style='font-size:9.0pt;
  font-family:宋体;mso-ascii-font-family:"Times New Roman";mso-hansi-font-family:
  "Times New Roman"'>、请示文件要有签发人、发文号、印章、联系人、联系电话，联合上报公文要注明会签人姓名</span><span
  lang=EN-US style='font-size:9.0pt'><o:p></o:p></span></p>
  </td>
  <td width=24 valign=top style='width:18.0pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0cm 5.4pt 0cm 5.4pt;height:18.45pt'>
  <p class=MsoNormal><span lang=EN-US><o:p><input id="liyou" value="12" name="liyou" type="checkbox" /></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:2;height:18.45pt'>
  <td width=432 valign=top style='width:324.0pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0cm 5.4pt 0cm 5.4pt;height:18.45pt'>
  <p class=MsoNormal style='text-indent:31.5pt;mso-char-indent-count:3.5'><span
  lang=EN-US style='font-size:9.0pt'>3</span><span style='font-size:9.0pt;
  font-family:宋体;mso-ascii-font-family:"Times New Roman";mso-hansi-font-family:
  "Times New Roman"'>、公文文种要使用得当，“报告”和“意见”不能夹带请示事项</span><span lang=EN-US
  style='font-size:9.0pt'><o:p></o:p></span></p>
  </td>
  <td width=24 valign=top style='width:18.0pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0cm 5.4pt 0cm 5.4pt;height:18.45pt'>
  <p class=MsoNormal><span lang=EN-US><o:p><input id="liyou" value="13" name="liyou" type="checkbox" /></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:3;height:18.45pt'>
  <td width=432 valign=top style='width:324.0pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0cm 5.4pt 0cm 5.4pt;height:18.45pt'>
  <p class=MsoNormal style='text-indent:31.5pt;mso-char-indent-count:3.5'><span
  lang=EN-US style='font-size:9.0pt'>4</span><span style='font-size:9.0pt;
  font-family:宋体;mso-ascii-font-family:"Times New Roman";mso-hansi-font-family:
  "Times New Roman"'>、公文中附件资料要齐全</span><span lang=EN-US style='font-size:9.0pt'><o:p></o:p></span></p>
  </td>
  <td width=24 valign=top style='width:18.0pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0cm 5.4pt 0cm 5.4pt;height:18.45pt'>
  <p class=MsoNormal><span lang=EN-US><o:p><input id="liyou" value="14" name="liyou" type="checkbox" /></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:4;height:18.45pt'>
  <td width=432 valign=top style='width:324.0pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0cm 5.4pt 0cm 5.4pt;height:18.45pt'>
  <p class=MsoNormal style='text-indent:31.5pt;mso-char-indent-count:3.5'><span
  lang=EN-US style='font-size:9.0pt'>5</span><span style='font-size:9.0pt;
  font-family:宋体;mso-ascii-font-family:"Times New Roman";mso-hansi-font-family:
  "Times New Roman"'>、有主管部门的单位、企业，应由主管部门行文</span><span lang=EN-US
  style='font-size:9.0pt'><o:p></o:p></span></p>
  </td>
  <td width=24 valign=top style='width:18.0pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0cm 5.4pt 0cm 5.4pt;height:18.45pt'>
  <p class=MsoNormal><span lang=EN-US><o:p><input id="liyou" value="15" name="liyou" type="checkbox" /></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:5;height:18.45pt'>
  <td width=432 valign=top style='width:324.0pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0cm 5.4pt 0cm 5.4pt;height:18.45pt'>
  <p class=MsoNormal style='text-indent:31.5pt;mso-char-indent-count:3.5'><span
  lang=EN-US style='font-size:9.0pt'>6</span><span style='font-size:9.0pt;
  font-family:宋体;mso-ascii-font-family:"Times New Roman";mso-hansi-font-family:
  "Times New Roman"'>、无主管部门的企业按属地管理原则，由县区政府、开发区（新区）管委会行文</span><span
  lang=EN-US style='font-size:9.0pt'><o:p></o:p></span></p>
  </td>
  <td width=24 valign=top style='width:18.0pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0cm 5.4pt 0cm 5.4pt;height:18.45pt'>
  <p class=MsoNormal><span lang=EN-US><o:p><input id="liyou" value="16" name="liyou" type="checkbox" /></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:6;height:18.45pt'>
  <td width=36 rowspan=6 valign=top style='width:27.0pt;border:solid windowtext 1.0pt;
  border-top:none;mso-border-top-alt:solid windowtext .5pt;mso-border-alt:solid windowtext .5pt;
  padding:0cm 5.4pt 0cm 5.4pt;height:18.45pt'>
  <p class=MsoNormal><span lang=EN-US><o:p>&nbsp;</o:p></span></p>
  <p class=MsoNormal align=center style='text-align:center'><span lang=EN-US><o:p>&nbsp;</o:p></span></p>
  <p class=MsoNormal align=center style='text-align:center'><span lang=EN-US><o:p>&nbsp;</o:p></span></p>
  <p class=MsoNormal align=center style='text-align:center'><span
  style='font-family:宋体;mso-ascii-font-family:"Times New Roman";mso-hansi-font-family:
  "Times New Roman"'>内</span></p>
  <p class=MsoNormal align=center style='text-align:center'><span
  style='font-family:宋体;mso-ascii-font-family:"Times New Roman";mso-hansi-font-family:
  "Times New Roman"'>容</span></p>
  <p class=MsoNormal align=center style='text-align:center'><span
  style='font-family:宋体;mso-ascii-font-family:"Times New Roman";mso-hansi-font-family:
  "Times New Roman"'>方</span></p>
  <p class=MsoNormal align=center style='text-align:center'><span
  style='font-family:宋体;mso-ascii-font-family:"Times New Roman";mso-hansi-font-family:
  "Times New Roman"'>面</span></p>
  </td>
  <td width=432 valign=top style='width:324.0pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0cm 5.4pt 0cm 5.4pt;height:18.45pt'>
  <p class=MsoNormal style='text-indent:31.5pt;mso-char-indent-count:3.5'><span
  lang=EN-US style='font-size:9.0pt'>1</span><span style='font-size:9.0pt;
  font-family:宋体;mso-ascii-font-family:"Times New Roman";mso-hansi-font-family:
  "Times New Roman"'>、属于部门职权范围内的事项应报部门办理</span><span lang=EN-US
  style='font-size:9.0pt'><o:p></o:p></span></p>
  </td>
  <td width=24 valign=top style='width:18.0pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0cm 5.4pt 0cm 5.4pt;height:18.45pt'>
  <p class=MsoNormal><span lang=EN-US><o:p><input id="liyou" value="21" name="liyou" type="checkbox" /></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:7;height:18.45pt'>
  <td width=432 valign=top style='width:324.0pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0cm 5.4pt 0cm 5.4pt;height:18.45pt'>
  <p class=MsoNormal style='text-indent:31.5pt;mso-char-indent-count:3.5'><span
  lang=EN-US style='font-size:9.0pt'>2</span><span style='font-size:9.0pt;
  font-family:宋体;mso-ascii-font-family:"Times New Roman";mso-hansi-font-family:
  "Times New Roman"'>、公文内容涉及其他地区或部门职权范围内的事项，主办单位应事先征求相关部门意见，并附上征求意见情况说明、相关单位盖章同意的正式意见</span><span
  lang=EN-US style='font-size:9.0pt'><o:p></o:p></span></p>
  </td>
  <td width=24 valign=top style='width:18.0pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0cm 5.4pt 0cm 5.4pt;height:18.45pt'>
  <p class=MsoNormal><span lang=EN-US><o:p><input id="liyou" value="22" name="liyou" type="checkbox" /></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:8;height:18.45pt'>
  <td width=432 valign=top style='width:324.0pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0cm 5.4pt 0cm 5.4pt;height:18.45pt'>
  <p class=MsoNormal style='text-indent:31.5pt;mso-char-indent-count:3.5'><span
  lang=EN-US style='font-size:9.0pt'>3</span><span style='font-size:9.0pt;
  font-family:宋体;mso-ascii-font-family:"Times New Roman";mso-hansi-font-family:
  "Times New Roman"'>、公文反映的问题要清晰，提出的要求要明确</span><span lang=EN-US
  style='font-size:9.0pt'><o:p></o:p></span></p>
  </td>
  <td width=24 valign=top style='width:18.0pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0cm 5.4pt 0cm 5.4pt;height:18.45pt'>
  <p class=MsoNormal><span lang=EN-US><o:p><input id="liyou" value="23" name="liyou" type="checkbox" /></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:9;height:18.45pt'>
  <td width=432 valign=top style='width:324.0pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0cm 5.4pt 0cm 5.4pt;height:18.45pt'>
  <p class=MsoNormal style='text-indent:31.5pt;mso-char-indent-count:3.5'><span
  lang=EN-US style='font-size:9.0pt'>4</span><span style='font-size:9.0pt;
  font-family:宋体;mso-ascii-font-family:"Times New Roman";mso-hansi-font-family:
  "Times New Roman"'>、应由部门发文的不能报请市政府转发（上级有明确要求的除外）</span><span lang=EN-US
  style='font-size:9.0pt'><o:p></o:p></span></p>
  </td>
  <td width=24 valign=top style='width:18.0pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0cm 5.4pt 0cm 5.4pt;height:18.45pt'>
  <p class=MsoNormal><span lang=EN-US><o:p><input id="liyou" value="24" name="liyou" type="checkbox" /></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:10;height:18.45pt'>
  <td width=432 valign=top style='width:324.0pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0cm 5.4pt 0cm 5.4pt;height:18.45pt'>
  <p class=MsoNormal style='text-indent:31.5pt;mso-char-indent-count:3.5'><span
  lang=EN-US style='font-size:9.0pt'>5</span><span style='font-size:9.0pt;
  font-family:宋体;mso-ascii-font-family:"Times New Roman";mso-hansi-font-family:
  "Times New Roman"'>、除上级明文规定和涉及全市的、综合性的重大工作外，设立和调整（除非常设机构正副职调整外）市政府非常设机构不要请报市政府印发</span><span
  lang=EN-US style='font-size:9.0pt'><o:p></o:p></span></p>
  </td>
  <td width=24 valign=top style='width:18.0pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0cm 5.4pt 0cm 5.4pt;height:18.45pt'>
  <p class=MsoNormal><span lang=EN-US><o:p><input id="liyou" value="25" name="liyou" type="checkbox" /></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:11;height:18.45pt'>
  <td width=432 valign=top style='width:324.0pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0cm 5.4pt 0cm 5.4pt;height:18.45pt'>
  <p class=MsoNormal style='text-indent:31.5pt;mso-char-indent-count:3.5'><span
  lang=EN-US style='font-size:9.0pt'>6</span><span style='font-size:9.0pt;
  font-family:宋体;mso-ascii-font-family:"Times New Roman";mso-hansi-font-family:
  "Times New Roman"'>、非常设机构确需报请市政府设立和调整的，该机构办公室应事先组织研究、协调并明确提出意见</span><span
  lang=EN-US style='font-size:9.0pt'><o:p></o:p></span></p>
  </td>
  <td width=24 valign=top style='width:18.0pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0cm 5.4pt 0cm 5.4pt;height:18.45pt'>
  <p class=MsoNormal><span lang=EN-US><o:p><input id="liyou" value="26" name="liyou" type="checkbox" /></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:12;mso-yfti-lastrow:yes;height:35.4pt'>
  <td width=36 valign=top style='width:27.0pt;border:solid windowtext 1.0pt;
  border-top:none;mso-border-top-alt:solid windowtext .5pt;mso-border-alt:solid windowtext .5pt;
  padding:0cm 5.4pt 0cm 5.4pt;height:35.4pt'>
  <p class=MsoNormal align=center style='text-align:center'><span
  style='font-family:宋体;mso-ascii-font-family:"Times New Roman";mso-hansi-font-family:
  "Times New Roman"'>其</span></p>
  <p class=MsoNormal align=center style='text-align:center'><span
  style='font-family:宋体;mso-ascii-font-family:"Times New Roman";mso-hansi-font-family:
  "Times New Roman"'>他</span></p>
  </td>
  <td width=432 valign=top style='width:324.0pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0cm 5.4pt 0cm 5.4pt;height:35.4pt'>
  <p class=MsoNormal><span lang=EN-US><o:p>&nbsp;&nbsp;&nbsp;&nbsp;<s:textarea id="qita" name="qita" style="width:380px;height:45px;font-size: 16px;"></s:textarea></o:p></span></p>
  </td>
  <td width=24 valign=top style='width:18.0pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0cm 5.4pt 0cm 5.4pt;height:35.4pt'>
  <p class=MsoNormal><span lang=EN-US><o:p><input id="liyou" value="33" name="liyou" type="checkbox" /></o:p></span></p>
  </td>
 </tr>
</table>

</div>

<p class=MsoNormal><span lang=EN-US><o:p>&nbsp;</o:p></span></p>

</div>
	<div class=Section1 style='layout-grid:5.6pt'>
			<input type="hidden" id="docId" name="docId" value="${model.docId}" >
			<input type="hidden" id="recvState" name="model.recvState" value="4">
			<input type="hidden" id="laiwenDW" name="laiwenDW" value="${laiwenDW }" >
			<input type="hidden" id="laiwentitle" name="laiwentitle" value="${laiwentitle }">
			<input type="hidden" id="docSendId" name="docSendId" value="${docSendId}" >
			<% 	String taskId = request.getParameter("taskId"); 
			 	if(!"".equals(taskId)&&null!=taskId){%>
			<input type="hidden" id="taskId" name="taskId" value="<%=taskId%>" >
			<%} %>
			<table align="left">
				<tr>
					<td colspan="3" align="left">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						${laiwenDW }:
					</td>
				</tr>
				<tr>
					<td align="left" colspan="3">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<s:label id="lab3" name="lab3" value="你单位来文"></s:label>(${laiwentitle })
					</td>
					
				</tr>
				<tr>
					<td align="left" width="75%" colspan="2">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<s:textarea id="docRecvRemark" name="docRecvRemark" style="width:500px;height:20px;font-size: 16px;"
						onKeyDown='if (this.value.length>=100){event.returnValue=false}'></s:textarea>
					</td>
					<td align="left" width="25%">
						<s:label id="lab" name="lab" value="因不"></s:label>
					</td>				
				</tr>
				<tr>
					<td colspan="3" align="left">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<s:label id="lab2" name="lab2" value="符合上述告知单：列明的要求事项，现予以退文。"></s:label>
					</td>
				</tr>
				<tr>
					<td colspan="3" align="center">
						<input type="button" name="submit2"  value="确 定" class="input_bg" style="width:60px;height:21px;font-size:15px" onclick="save();">&nbsp;&nbsp;&nbsp;
						<input type="button" class="input_bg" style="width:60px;height:21px;font-size:15px" value="取 消" onclick="window.close();">
					</td>
				</tr>
			</table>
		</s:form>
	</div>
</body>

</html>
