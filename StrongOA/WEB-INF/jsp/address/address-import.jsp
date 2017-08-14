<%@ page language="java" pageEncoding="utf-8"%>
<%@include file="/common/include/rootPath.jsp" %>
<html>
  <head>
    <title>导入CSV、vCard文件</title>
    <%@include file="/common/include/meta.jsp" %>
	<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
	<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
	<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
  	<script src="<%=path%>/common/js/common/btn.js" type="text/javascript"></script>
  	<script type="text/javascript">
  		$(document).ready(function(){
  			var btnPrev = $("#btnPrev");
			var btnNext = $("#btnNext");
			var btnMap = $("#btnMap");
			var nextContent = $("#nextContent");
			var exportcontent = $("#exportcontent");
			var div_select = $("#div_select");
			var l_title = $("#l_title");
			$(":file").keydown(function(){return false;});
			//上一步
			if(btnPrev){
				btnPrev.attr("disabled","disabled");
				btnPrev.click(function(){
					$(this).attr("disabled","disabled");
					btnNext.get(0).value='  下一步';
					nextContent.hide();
					exportcontent.show();
					div_select.hide();
					l_title.text("导入CSV、vCard文件");
				});
			}
			
			//下一步
			if(btnNext){
				btnNext.click(function(){
					if(btnNext.get(0).value=='  完  成'){
						$("#btnNext").attr("disabled",true);
						var params = "";
						$("td>input:not(:checkbox)").each(function(){
							var ad_content = $(this).next().text();
							if($.trim(ad_content) != ""){
								params += ad_content+"="+$(this).attr("value")+"Γ";
							}
						});
						if(params.length>0){
							params = params.substring(0,params.length-1);
						}
						var parentWindow = window.dialogArguments;
						var groupId = parentWindow.document.getElementById("groupId");
						$.ajax({
							type:"post",
							url:"<%=root%>/address/address!doImportFile.action",
							data:{
								params:params,
								groupId:groupId.value
							},
							success:function(data){
								alert(data);
								window.returnValue = "suc";
								window.close();
							},
							error:function(){
								alert("对不起，导入失败。请重试或与管理员联系。");
								window.returnValue = "fail";
								window.close();
							}
						});
					}else{
						if($("#path").val() == ""){
							alert("未找到文件！");
							return false;
						}else{
							var path = $("#path").val();
							var ext = path.substring(path.lastIndexOf(".")+1,path.length);
							if(ext!="csv"){
								alert("文件类型不对，请选择csv文件！");
								return false;
							}
						}
						$("form").submit();
						$("#btnNext").attr("disabled",true);
					}
				});
			}
			
			//映射
			btnMap.click(function(){
				var count = $(":checkbox:checked").size();
				if(count>1){
					alert("对不起，一次只能映射一个字段！");
				}else if(count==0){
					alert("请至少选择一项！");
				}else{
					var plaintext = $(":checked").next().text();
					var addresstext = $(":checked").parent().next();
					var ad_span = addresstext.find("span");
					$("#plaintext").val(plaintext);
					var ret=OpenWindow("<%=path%>/fileNameRedirectAction.action?toPage=address/address-changemap.jsp","250","150",window);
					if(null!=ret && ""!=ret){
						if(ad_span.html() == null){
							addresstext.append("<span class=\"wz\">"+ret+"</span>");
						}else{
							ad_span.text(ret);
						}
					}
				}
			});
			
			var allContent = $("#allContent").val();
			if(""!=allContent){
								var jsons = allContent.split("Γ");
								var strHtml = "";
								if(jsons.length>0){
									var header = jsons[0];//头部
									var headers = header.split(",");
									
									strHtml += "<table style=\"vertical-align: top;\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"00\">";
									strHtml += "<thead><tr style=\"position:relative;top:expression(this.parentElement.offsetParent.parentElement.scrollTop);z-index:1;\">";
									strHtml +="<th width=\"50%\" height=\"22\" class='biao_bg2'>文本字段</th>";
									strHtml +="<th width=\"50%\" height=\"22\" class=\"biao_bg2\">通讯录字段</th>";
									strHtml +="<th class=\"biao_bg2\" style=\"text-indent: 0px;\"></th>";
									strHtml +="</tr></thead>";
									for(var j=0;j<headers.length;j++){
										strHtml+="<tr>";
													strHtml+="<td width='60%' id='cb"+j+"'><input name='header' type='checkbox' />";
													//strHtml+="<td>";
													var content = "";
													for(var i=1;i<jsons.length;i++){
														var tempjson = jsons[i].split(",");
														content+=tempjson[j]+",";
													}
													content = content.substring(0,content.length-1);
													//strHtml+=content;
														
													strHtml+="<span class='wz'>"+headers[j]+"</span>"+"</td>";
													if("姓名"==headers[j]){
														strHtml+="<td><input name='header' value='"+content+"' type='hidden' /><span class=\"wz\">姓名</span></td>";
													}else if("职务"==headers[j]){
														strHtml+="<td><input name='header' value='"+content+"' type='hidden' /><span class=\"wz\">职务</span></td>";
													}else if("电子邮件"==headers[j]){
														strHtml+="<td><input name='header' value='"+content+"' type='hidden' /><span class=\"wz\">电子邮件</span></td>";
													}else if("手机1"==headers[j]){
														strHtml+="<td><input name='header' value='"+content+"' type='hidden' /><span class=\"wz\">手机1</span></td>";
													}else if("手机2"==headers[j]){
														strHtml+="<td><input name='header' value='"+content+"' type='hidden' /><span class=\"wz\">手机2</span></td>";
													}else if("性别"==headers[j]){
														strHtml+="<td><input name='header' value='"+content+"' type='hidden' /><span class=\"wz\">性别</span></td>";
													}else if("生日"==headers[j]){
														strHtml+="<td><input name='header' value='"+content+"' type='hidden' /><span class=\"wz\">生日</span></td>";
													}else if("QQ"==headers[j]){
														strHtml+="<td><input name='header' value='"+content+"' type='hidden' /><span class=\"wz\">QQ</span></td>";
													}else if("MSN"==headers[j]){
														strHtml+="<td><input name='header' value='"+content+"' type='hidden' /><span class=\"wz\">MSN</span></td>";
													}else if("主页"==headers[j]){
														strHtml+="<td><input name='header' value='"+content+"' type='hidden' /><span class=\"wz\">主页</span></td>";
													}else if("爱好"==headers[j]){
														strHtml+="<td><input name='header' value='"+content+"' type='hidden' /><span class=\"wz\">爱好</span></td>";
													}else if("国家"==headers[j]){
														strHtml+="<td><input name='header' value='"+content+"' type='hidden' /><span class=\"wz\">国家</span></td>";
													}else if("家庭电话1"==headers[j]){
														strHtml+="<td><input name='header' value='"+content+"' type='hidden' /><span class=\"wz\">家庭电话1</span></td>";
													}else if("家庭电话2"==headers[j]){
														strHtml+="<td><input name='header' value='"+content+"' type='hidden' /><span class=\"wz\">家庭电话2</span></td>";
													}else if("省"==headers[j]){
														strHtml+="<td><input name='header' value='"+content+"' type='hidden' /><span class=\"wz\">省</span></td>";
													}else if("城市"==headers[j]){
														strHtml+="<td><input name='header' value='"+content+"' type='hidden' /><span class=\"wz\">城市</span></td>";
													}else if("传真"==headers[j]){
														strHtml+="<td><input name='header' value='"+content+"' type='hidden' /><span class=\"wz\">传真</span></td>";
													}else if("家庭地址"==headers[j]){
														strHtml+="<td><input name='header' value='"+content+"' type='hidden' /><span class=\"wz\">家庭地址</span></td>";
													}else if("公司"==headers[j]){
														strHtml+="<td><input name='header' value='"+content+"' type='hidden' /><span class=\"wz\">公司</span></td>";
													}else if("职位"==headers[j]){
														strHtml+="<td><input name='header' value='"+content+"' type='hidden' /><span class=\"wz\">职位</span></td>";
													}else if("部门"==headers[j]){
														strHtml+="<td><input name='header' value='"+content+"' type='hidden' /><span class=\"wz\">部门</span></td>";
													}else if("公司电话1"==headers[j]){
														strHtml+="<td><input name='header' value='"+content+"' type='hidden' /><span class=\"wz\">公司电话1</span></td>";
													}else if("公司电话2"==headers[j]){
														strHtml+="<td><input name='header' value='"+content+"' type='hidden' /><span class=\"wz\">公司电话2</span></td>";
													}else if("邮编"==headers[j]){
														strHtml+="<td><input name='header' value='"+content+"' type='hidden' /><span class=\"wz\">邮编</span></td>";
													}else if("附注信息"==headers[j]){
														strHtml+="<td><input name='header' value='"+content+"' type='hidden' /><span class=\"wz\">附注信息</span></td>";
													}else{
														strHtml+="<td><input name='header' value='"+content+"' type='hidden' /></td>";
													}
													
										strHtml+="</tr>";
									}
									
									strHtml+="</table>";
									
								}
								
								$("#nextContent").html(strHtml);
								btnPrev.attr("disabled",'');
								btnNext.get(0).value='  完  成';
								nextContent.show();
								exportcontent.hide();
								div_select.show();
								l_title.text("映射输入字段：");
			}
			
			//取消
			$("#btnCancel").click(function(){
				window.close();
			});
  		});
  	</script>
  </head>
  <base target="_self"/>
  <body>
     <table border="0" width="100%" cellspacing="0" cellpadding="3" >
		<tr>
			<td height="45" colspan="2"
				style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
				<table width="100%" border="0" cellspacing="0" cellpadding="00">
					<tr>
						<td width="34" align="center">
							<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
								height="9">
						</td>
						<td width="227">
							<label id="l_title">导入CSV、vCard文件</label>
						</td>
						<td>
						</td>
						<td width="290">
						</td>
					</tr>
				</table>
			</td>
		</tr>
  </table>
  <div id="exportcontent" style="height: 60%;" >
  		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  	<span class="wz">请选择导入的文件：</span>
  <br><br>
  	 <form name="form1" method="post"  enctype="multipart/form-data" action="<%=root%>/address/address!importFile.action">
  	 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  	 	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  	<input id="path" name="upload" type="file" style="width: 50%;"/>
  	</form>
  	<br>
  	<span><br>
  		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  		<input type="button" id="btnPrew" style="display: none;"  class="input_bg" icoPath="<%=root%>/images/ico/queding.gif" value="  浏览..."/>&nbsp;&nbsp;
  	</span>
  </div>
	<div id="nextContent"  style="display: none;" style='background:#fff; overflow:auto;height:60%;border-left:1px solid #404040;border-top:1px solid #404040;border-bottom:1px solid #d4d0c8;border-right:1px solid #d4d0c8;'>
		
	
	</div>
		<div id="div_select" style="display: none;" align="right"><br>
			<input type="button" id="btnMap" class="input_bg" icoPath="<%=root%>/images/ico/queding.gif" value="  映射"/>&nbsp;&nbsp;
		</div>
  <br>
  <div align="center">
	  	<input id="plaintext" type="hidden" />
	  	<input id="allContent" type="hidden" value='${allContent }'/>
		<input type="button" id="btnPrev"  class="input_bg" icoPath="<%=root%>/images/ico/queding.gif" value="  上一步"/>&nbsp;&nbsp;
		<input type="button" id="btnNext"  class="input_bg" icoPath="<%=root%>/images/ico/queding.gif" value="  下一步"/>&nbsp;&nbsp;
		<input type="button" id="btnCancel"  class="input_bg" icoPath="<%=root%>/images/ico/queding.gif" value="  取  消"/>
  </div>
  </body>
</html>
