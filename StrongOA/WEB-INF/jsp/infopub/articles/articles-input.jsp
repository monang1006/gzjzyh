<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@include file="/common/include/rootPath.jsp" %>
<html>
	<head>
		<title>保存稿件</title>
		<%@include file="/common/include/meta.jsp"%>
		<link type="text/css" rel="stylesheet" href="<%=path%>/common/js/tabpane/css/luna/tab.css" />
		<script type="text/javascript" src="<%=path%>/common/js/tabpane/js/tabpane.js"></script>
		<LINK href="<%=frameroot%>/css/properties_windows_add.css" type=text/css rel=stylesheet>
		<script type="text/javascript" language="javascript" src="<%=jsroot%>/jquery/jquery-1.2.6.js"></script>
		<script type="text/javascript" src="<%=jsroot%>/validate/jquery.validate.js"></script>
		<script type="text/javascript" src="<%=jsroot%>/validate/formvalidate.js"></script>
		<script type="text/javascript" src="<%=jsroot%>/common/common.js"></script>
		<script type="text/javascript" language="javascript" src="<%=jsroot%>/upload/jquery.MultiFile.js"></script>
		<script type="text/javascript" language="javascript" src="<%=jsroot%>/upload/jquery.blockUI.js"></script>
		
		<SCRIPT LANGUAGE="JavaScript" src="<%=path%>/common/js/commontab/service.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></SCRIPT>
		<script type="text/javascript">
		//验证url  
		function chargeUrl(url){
			var reg = "^http[s]?:\\/\\/([\\w-]+\\.)+[\\w-]+([\\w-./?:%&=]*)?$";
			var regExp = new RegExp(reg);
			if(regExp.test(url)){
				return true;
			}else{
				alert('您输入的链接地址有误，请重新输入!链接地址格式如:"http://www.baidu.com/"。');
				return false;
			}
		}
			var i=0;
			function onsubmitform(){  //提交表单
<%--			i++;--%>
<%--			if(i>1){--%>
<%--				alert("请勿重复提交！");--%>
<%--				return;--%>
				var filter = "jpg,gif,png,bmp";
				var filters = filter.split(",");
				var file = $("#file").val();
				
				if(file!=null&&file !=""){
					var index = file.lastIndexOf(".");
					var ext = file.substring(index+1, file.length);
					ext = ext.toLowerCase();
					var istrue = false;
					for(var ii=0;ii<filters.length;ii++){
						if(ext == filters[ii]){
							istrue = true;
						}
					}
					if(!istrue){
						alert("只能上传图片类型，请重新选择。");
						return false;
					}
				}

			var valueint = /^([0-9.]+)$/; 
			var title = document.getElementById("articlesTitle").value;
				if($.trim($("#articlesTitle").val()) == ''){
					alert("主标题不能为空。");
					//document.getElementById("articlesTitle").focus();
			  		return;
				}
				if(document.getElementById("articlesTitle").value.length >100){
					alert("主标题不能超过100个字。");
					//document.getElementById("articlesTitle").focus();
			  		return;
				}
				if(document.getElementById("articlesSource").value.length >20){
					alert("稿件来源不能超过20个字。");
					//document.getElementById("articlesSource").focus();
			  		return;
				}
				if(document.getElementById("articlesAuthor").value.length >10){
					alert("稿件作者名不能超过10个字。");
					//document.getElementById("articlesAuthor").focus();
			  		return;
				}
				if(document.getElementById("articlesCreatedate").value == ""){
					alert("请输入创建时间。");
					//document.getElementById("articlesAuthor").focus();
			  		return;
				}
				if(document.getElementById("articlesSubtitle").value.length > 100){
					alert("稿件副标题名不能超过100个字。");
					//document.getElementById("articlesSubtitle").focus();
			  		return;
				}
				if(document.getElementById("articlesKeyword").value.length > 25){
					alert("稿件关健字不能超过25个字。");
					//document.getElementById("articlesKeyword").focus();
			  		return;
				}
				if(document.getElementsByName("likeBoolean")[0].checked==true){
					if(document.getElementById("articlesRedirecturl").value!=""){
					var t=chargeUrl(document.getElementById("articlesRedirecturl").value);
					if(t==false){
							return ;
						}
					}
				}
				if(document.getElementById("articlesRedirecturl").value.length > 100){
					alert("外部链接不能超过100个字。");
					//document.getElementById("articlesRedirecturl").focus();
			  		return;
				}
				if(document.getElementById("articlesEditor").value.length > 20){
					alert("责任编辑不能超过20个字。");
					//document.getElementById("articlesEditor").focus();
			  		return;
				}
				if(document.getElementById("articlesDesc").value.length > 75){
					alert("摘要信息不能超过75个字。");
					//document.getElementById("articlesDesc").focus();
			  		return;
				}
				if(document.getElementById("articlesPaginationnum").value!=""&&!valueint.test(document.getElementById("articlesPaginationnum").value)){
				
				   alert("分页大小只能为整数。");
				   return;
				}
				
				if(document.getElementById("articlesHits").value != ''){
					var pattern = /^([0-9.]+)$/; 
					var nub = document.getElementById("articlesHits").value;
					if (!pattern.test(nub)) { 
						alert("初始点击数只能为0或正整数。");
						
				  		return;					
					}else{
						if(document.getElementById("articlesHits").value.length > 4){
						alert("初始点击数不能大于9999。");
						//document.getElementById("articlesHits").focus();
				  		return;
						}
					}
				}
				if(document.getElementsByName("likeBoolean")[0].checked==true){
					if($.trim(document.getElementById("articlesRedirecturl").value)==""){
						alert("您如果选择了外部链接就一定要对其进行填写，请您填写外部链接地址。");
						document.getElementById("articlesRedirecturl").focus();
						return;
					}
				}
				
				title = title.replace(new RegExp("\"","gm"),"“");
				title = title.replace(/[\']/gm,"’");
				title = title.replace(new RegExp("\n","gm")," ");
				title = title.replace(new RegExp("\r","gm"),"");
				title = title.replace(new RegExp("<","gm"),"＜");
				title = title.replace(new RegExp(">","gm"),"＞");
				title = title.replace(/[\\]/gm, "＼");
				document.getElementById("articlesTitle").value = title;
				var begintime=$("#articlesAutopublishtime").val();
	            var endtime=$("#articlesAutocancletime").val();
	             if(endtime!=""&&begintime>endtime){
	               alert("上线时间不能晚于下线时间。");
	               return;
	             }
	             var stime = $("#articlesStandtopstart").val();
	 				var etime = $("#articlesStandtopend").val();
	 				if(etime!="" && stime>etime){
<%--	 				if(etime!="" ){--%>
<%--	 				if(date2string(stime)>=date2string(etime)){--%>
	 					alert("固顶开始时间不能晚于固顶结束时间。");
	 					return;
	 			}
				document.getElementById("artsave").submit();
			}
//转换时间格式(yyyy-MM-dd)--->(yyyyMMdd)
function date2string(stime){
 	var arrsDate1=stime.split('-');
 	stime=arrsDate1[0]+""+arrsDate1[1]+""+arrsDate1[2];
 	var arrsDate2=stime.split(' ');
 	stime=arrsDate2[0]+""+arrsDate2[1];
 	var arrsDate3=stime.split(':');
 	stime=arrsDate3[0]+""+arrsDate3[1]+""+arrsDate3[2];
 	return stime;
 }
			function show(){
				if(document.getElementById("articlesRedirecturl").disabled){
					document.getElementById("articlesRedirecturl").disabled ="";
				}else{
					document.getElementById("articlesRedirecturl").disabled ="disabled";
				}
				
			}
<%--			function sohwtime() { //显示隐藏组选项--%>
<%--				var objDiv = document.getElementById ("sohwtime"); --%>
<%--				if(document.getElementById("gudingBoolean").checked){--%>
<%--					objDiv.style.display = "block"; --%>
<%--				}else --%>
<%--					objDiv.style.display = "none"; --%>
<%--			}--%>
			
			function selectCheckbox(){//选择复选框
				var objDiv = document.getElementById ("sohwtime"); 
				if(document.getElementById("likeBoolean").checked){
					document.getElementById("articlesRedirecturl").disabled ="";
				}
<%--				if(document.getElementById("gudingBoolean").checked){--%>
<%--					objDiv.style.display = "block"; --%>
<%--				}--%>
				titlecolor();
			} 
			function goColumn(){//获取机构树
			  var ret=OpenWindow("<%=root%>/infopub/articles/articles!tree.action?treeType=1","400","350",window);
			}
			function deletePic(){
			   $("#divpic").empty();
			   $("#divpic").append("<input type='file' id='file' name='file' size='60' >"); 
			   $("#delAttachIds").val("shanchu");
          	}
			function titlecolor(){
			   var titcolor='${model.articlesTitlecolor}';
			   if(titcolor!=""&&titcolor!="null"){
			   if(document.getElementById(titcolor)!=undefined&&document.getElementById(titcolor)!=null)
			     document.getElementById(titcolor).selected=true;	
			   }		
			 }
			 
			
		</script>
	</head>
	<base target="_self"/>
	<body class=contentbodymargin oncontextmenu="return false;" onload="selectCheckbox();" >
	<script type="text/javascript" language="javascript" src="<%=jsroot%>/newdate/WdatePicker.js"></script>
<DIV id=contentborder align=center>
    <table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
  <tr>
    <td height="100%">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="40" style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
        <table width="100%" border="0" cellspacing="0" cellpadding="00">
          <tr>
           <td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
								<script>
								var id = "${model.articlesId}";
								if(id==null|id==""){
									window.document.write("<strong>新建稿件</strong>");
								}else{
									window.document.write("<strong>编辑稿件</strong>");
								}
								</script>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
					                <tr>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onclick="onsubmitform();">&nbsp;保&nbsp;存&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
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
        </table>
        </td>
      </tr>
    </table>
    <s:form id="artsave" action="/infopub/articles/articles!save.action" theme="simple" enctype="multipart/form-data" >
			<input type="hidden" id="articlesId" name="articlesId" value="${model.articlesId}">
			<input type="hidden" id="columnId" name="columnId" value="${columnId}">
			<input type="hidden" id="columnArticleId" name="columnArticleId" value="${columnArticleId}">
			<input type="hidden" name="isPromulgate" value="${isPromulgate }">
			<input type="hidden" name="delAttachIds" id="delAttachIds">
			<table  border="0" cellpadding="0" cellspacing="0" class="table1" width="100%" >
			<tr >
				<td  valign="top">
							<DIV class=tab-pane id=tabPane1>
								<SCRIPT type="text/javascript">
								tp1 = new WebFXTabPane( document.getElementById("tabPane1") ,false );
								</SCRIPT>
								<DIV class=tab-page id=tabPage1  style="border: 0px">
									<H2 class=tab  >
										基本信息
									</H2>
									<table border="0" width="100%" cellpadding="0" cellspacing="0"   >
										<tr>
											<td  class="biao_bg1" align="right">
												<span class="wz" ><font color=red>*</font>&nbsp;主标题：&nbsp;</span>
											</td>
											<td  class="td1" style="padding-left:5px;">
												<input id="articlesTitle" name="model.articlesTitle" maxlength="100" type="text" size="40" value="${model.articlesTitle}" >&nbsp;
												<select id="articlesTitlecolor" name='model.articlesTitlecolor'  onchange="this.blur();">
								                      <option value="0" >颜色</option>
								                      <option value='#000000' id='#000000' style='background-color:#000000'></option>
								                      <option value='#FFFFFF' id='#FFFFFF' style='background-color:#FFFFFF'></option>
								                      <option value='#008000' id='#008000' style='background-color:#008000'></option>
								                      <option value='#800000' id='#800000' style='background-color:#800000'></option>
								                      <option value='#808000' id='#808000' style='background-color:#808000'></option>
								                      <option value='#000080' id='#000080' style='background-color:#000080'></option>
								                      <option value='#800080' id='#800080'  style='background-color:#800080'></option>
								                      <option value='#808080' id='#808080' style='background-color:#808080'></option>
								                      <option value='#FFFF00' id='#FFFF00'  style='background-color:#FFFF00'></option>
								                      <option value='#00FF00' id='#00FF00' style='background-color:#00FF00'></option>
								                      <option value='#00FFFF' id='#00FFFF' style='background-color:#00FFFF'></option>
								                      <option value='#FF00FF' id='#FF00FF' style='background-color:#FF00FF'></option>
								                      <option value='#FF0000' id='#FF0000' style='background-color:#FF0000'></option>
								                      <option value='#0000FF' id='#0000FF' style='background-color:#0000FF'></option>
								                      <option value='#008080' id='#008080' style='background-color:#008080'></option>
								                </select>&nbsp;
<%--								              <s:select name="model.articlesTitlefont"  list="#{'0':'字体','1':'粗体','2':'斜体','3':'粗+斜'}" 	listKey="key" listValue="value" style="width:5em"/>--%>
											</td>
										</tr>
										<tr>
											<td  class="biao_bg1" align="right">
												<span id="addressspan" class="wz" >副标题：&nbsp;</span>
											</td>
											<td  class="td1" style="padding-left:5px;">
												<input id="articlesSubtitle" name="model.articlesSubtitle"  maxlength="50" type="text" size="40" value="${model.articlesSubtitle}" > 
											
											</td>
										</tr>
										<tr>
											<td class="biao_bg1" align="right">
												<span id="addressspan" class="wz" >来源：&nbsp;</span></td>
													<td class="td1" style="padding-left:5px;">
												<input id="articlesSource" maxlength="20" name="model.articlesSource"  type="text" size="40" value="${model.articlesSource}" >
											
										</td>
										</tr>
										<tr>
											<td  class="biao_bg1" align="right">
												<span id="addressspan" class="wz" >作者：&nbsp;</span></td>
													<td  class="td1" style="padding-left:5px;">
												<input id="articlesAuthor" name="model.articlesAuthor"  maxlength="10" type="text" size="40" value="${model.articlesAuthor}" >
													
												</td>
										</tr>
										<tr>
											<td  class="biao_bg1" align="right">
												<span id="addressspan" class="wz" ><font color=red>*</font>&nbsp;创建时间：&nbsp;</span>
												</td>
											<td  class="td1" style="padding-left:5px;">
												<strong:newdate id="articlesCreatedate" name="model.articlesCreatedate"
												dateform="yyyy-MM-dd HH:mm:ss" width="200" dateobj="${model.articlesCreatedate}" isicon="true"/>
													
											</td>
										</tr>
										<tr><td  valign="top" class="biao_bg1" align="right"><span id="addressspan" class="wz" >选择栏目：&nbsp;</span></td>
										<td class="td1" style="padding-left:5px;"> 
										<s:textarea cols="25" id="columnNames" name="columnNames"  rows="3" readonly="true"></s:textarea>
											<a href="JavaScript:goColumn();" class="button" >栏目</a>
											<input type="hidden" id="columnIds" name="columnIds" value="${columnIds }">
											
										</td></tr>
										<tr>
											<td class="biao_bg1" align="right">
												<span id="addressspan" class="wz" >关键字：&nbsp;</span></span>
											</td>
											<td  class="td1" style="padding-left:5px;">
												<input id="articlesKeyword" name="model.articlesKeyword" type="text" size="20" value="${model.articlesKeyword}" > 
												编辑：&nbsp;<input id="articlesLatestuser" maxlength="50" name="model.articlesLatestuser"  type="text" size="10" readonly="readonly" value="${model.articlesLatestuser}"  > 
											</td>
										</tr>
										<tr>
											<td  class="biao_bg1" align="right">
												<span id="addressspan" class="wz" >外部链接：&nbsp;</span>
											</td>
											<td  class="td1" style="padding-left:5px;">
												<input id="articlesRedirecturl"  maxlength="100" name="model.articlesRedirecturl" type="text" size="40" disabled="disabled" value="${model.articlesRedirecturl}" >  
												<s:checkbox name="likeBoolean" theme="simple" onclick="show()" readonly="readonly"/>
												<span class="wz"><font id=colortext color="gray" >
												 勾选后稿件可查看外部链接
												</font></span>
											</td>
										</tr>
										<tr>
											<td  class="biao_bg1" align="right">
												<span id="addressspan" class="wz" >导读图片：&nbsp;</span>
											</td>
											<td class="td1" style="padding-left:5px;">
											<div id="divpic">
											  <s:if test="model.articlesPic!=null">
											<a href="javascript:deletePic()" class="button">删除</a>
												${model.articlesPic}
											
												</s:if>
												<s:else>
												<input type="file" id="file" class="upFileBtn" onkeydown="return false;" name="file" size="40"  />
												</s:else>
											</div>
												
											</td>
										</tr>
										<tr>
											<td  class="biao_bg1" align="right">
												<span id="addressspan" class="wz" >基本属性：&nbsp;</span>
											</td>
											<td  class="td1" style="padding-left:5px;">
												<!-- 											
												<s:checkbox name="daoduBoolean" theme="simple" />导读&nbsp;
												<s:checkbox name="redianBoolean" theme="simple" />热点&nbsp;
												 -->
												<s:checkbox name="gudingBoolean" theme="simple"/>固顶&nbsp;
												<s:checkbox name="yunxuprBoolean" theme="simple" />允许评论&nbsp;
												
<%--											<div id="sohwtime" style="display:none; ">--%>
<%--											固顶开始时间：<strong:newdate id="articlesStandtopstart" name="model.articlesStandtopstart"--%>
<%--											dateform="yyyy-MM-dd HH:mm:ss" width="120"--%>
<%--											dateobj="${model.articlesStandtopstart}" isicon="true"/>--%>
<%--											固顶结束时间：<strong:newdate id="articlesStandtopend" name="model.articlesStandtopend"--%>
<%--											dateform="yyyy-MM-dd HH:mm:ss" width="120"--%>
<%--											dateobj="${model.articlesStandtopend}" isicon="true"/>--%>
<%--											</div>--%>
											</td>
										</tr>									
										<tr >
											<td colspan="2" style="padding-left: 40px">
											  <textarea id="articlesArticlecontent" name="model.articlesArticlecontent" style="display:none"><font size="4" >${model.articlesArticlecontent }</font></textarea>
											  <IFRAME ID="eWebEditor1" src="<%=path%>/common/ewebeditor/ewebeditor.htm?id=articlesArticlecontent&style=coolblue&extcss=_example/myeditorarea.css" frameborder="0" scrolling="no" width="100%" height="650"></IFRAME>
											</td>
										</tr>
										<tr>
										<td class="table1_td"></td>
										<td></td>
										</tr>
									</table>
								<SCRIPT type="text/javascript">tp1.addTabPage( document.getElementById( "tabPage1" ) );</SCRIPT>

								</DIV>
								<DIV class=tab-page id=tabPage2  style="border: 0px;">
									<H2 class=tab>
										文章属性
									</H2>
									<table border="0"  cellpadding="0" cellspacing="0" >
										<tr >
											<td  class="biao_bg1" align="right">
												<span id="addressspan" class="wz" >责任编辑：&nbsp;</span>
											</td>
											<td  class="td1" style="padding-left:5px;">
												<input id="articlesEditor" style="width:220;"  maxlength="12" name="model.articlesEditor" type="text" size="27" value="${model.articlesEditor}" > 
											</td>
										</tr>
										<tr >
											<td  class="biao_bg1" align="right">
												<span id="addressspan" class="wz" >摘要信息：&nbsp;</span>
											</td>
											<td  class="td1" style="padding-left:5px;">
												<textarea id="articlesDesc"  name="model.articlesDesc" onkeyup="this.value = this.value.substring(0, 75)" rows="4" cols="25" >${model.articlesDesc}</textarea>
											
											</td>
										</tr>
										
										<tr  style="display:none">
											<td  class="biao_bg1" align="right">
												<span id="addressspan" class="wz" >内容分页：&nbsp;</span>
											</td>
											<td  class="td1" style="padding-left:5px;">
												 <s:select name="model.articlesPagination"  list="#{'1':'自动分页','0':'不分页'}" listKey="key" listValue="value" style="width:5em;" disabled="true"/>
												 自动分页大小<input id="articlesPaginationnum" name="model.articlesPaginationnum" maxlength="6" type="text" size="27" value="${model.articlesPaginationnum}" > 
											</td>
										</tr>
										
										<tr >
											<td  class="biao_bg1" align="right">
												<span id="addressspan" class="wz" >上线时间：&nbsp;</span>
											</td>
											<td  class="td1" style="padding-left:5px;">
												<strong:newdate id="articlesAutopublishtime" name="model.articlesAutopublishtime"
												dateform="yyyy-MM-dd HH:mm:ss" width="220"
												dateobj="${model.articlesAutopublishtime}" isicon="true"/>
											
											</td>
										</tr>
										
										<tr >
											<td  class="biao_bg1" align="right">
												<span id="addressspan" class="wz" >下线时间：&nbsp;</span>
											</td>
											<td  class="td1" style="padding-left:5px;">
												<strong:newdate id="articlesAutocancletime" name="model.articlesAutocancletime"
												dateform="yyyy-MM-dd HH:mm:ss" width="220"
												dateobj="${model.articlesAutocancletime}" isicon="true"/>
											
											</td>
										</tr>

										<tr >
											<td  class="biao_bg1" align="right">
												<span id="addressspan" class="wz" >初始点击数：&nbsp;</span>
											</td>
											<td  class="td1" style="padding-left:5px;">
												<input id="articlesHits" name="model.articlesHits" maxlength="4" type="text" size="27" value="${model.articlesHits}"  style="width:220;"> 
											</td>
										</tr>
										<tr>
										<td class="table1_td"></td>
										<td></td>
										</tr>
								    </table>
								<SCRIPT type="text/javascript">tp1.addTabPage( document.getElementById( "tabPage2" ) );</SCRIPT>
							</DIV>
						</td>
					</tr>
			</table>
</s:form>	
			</td></tr></table>
		</DIV>
	</body>
</html>
