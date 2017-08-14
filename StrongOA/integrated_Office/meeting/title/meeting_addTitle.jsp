<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@taglib uri="/tags/web-newdate" prefix="strong"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>添加议题</title>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
			   <script type="text/javascript"
      src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<base target="_self" />
		<%
		String param = request.getParameter("param");
		%>
		<script type="text/javascript">
	var param="<%=param%>";	
	function goback(){
		var form=document.getElementById("meetform");
		if(param=='selectTitle'){		
			form.action="../meetingPlan/selectTitle.jsp";
			form.submit();
		}else{
			form.action="meeting_titleList.jsp";
			form.submit();
		}
	}	
	var num=0;
	function createTable(str){
		var ft=document.getElementById("annex");
		var length=ft.rows.length;
		if(str=='add')
		 	num=1;
  		for(var j=1;j<=num;j++){
  			var i;
  			if(str=='add')
  				i=length+j;
  			else
  				i=j;
  			var hanghao=i-1;
  			newRow=ft.insertRow(hanghao);//插入一行
  			newRow.id="url"+i;
  			newRow.ln=i;
  
  			c1=newRow.insertCell(0);//插入一列
  			c1.id="td1"+i;
			c1.align="left";
			c1.width="15%";
			c1.height="21";
			document.getElementById("td1"+i).className="biao_bg1";
			c1.align="right";
			c1.innerHTML="<span class='wz'>附件"+i+":</span>";		
			
			c2=newRow.insertCell(1);//插入一列
  			c2.id="td2"+i;
			c2.align="left";
			c2.width="7%";
			c1.height="21";
			document.getElementById("td2"+i).className="biao_bg1";
			c2.align="right";
			c2.innerHTML="<span class='wz'>&nbsp;</span>";	
			
			
			c3=newRow.insertCell(2);//插入一列
			c3.id="td3"+i;
			c3.align="left";
			c3.height="21";
			document.getElementById("td3"+i).className="td1";
			c3.innerHTML="<input id='liulan"+i+"' name='liulan"+i+"' type='file' size='45'> <a href='#' onclick='deleteRow();' id='delete"+i+"' ln='"+i+"'>删除</a>";			
		 	<%--
		 	c4=newRow.insertCell(3);//插入一列
  			c4.id="td4"+i;
			c4.align="left";
			c4.width="7%";
			c4.height="21";
			document.getElementById("td4"+i).className="biao_bg1";
			c4.align="right";
			c4.innerHTML="<span class='wz'>文件：</span>";	
		 	
		 	c5=newRow.insertCell(4);//插入一列
			c5.id="td5"+i;
			c5.height="21";
			c5.align="left";
			document.getElementById("td5"+i).className="td1";
			c5.innerHTML=" <input id='liulan"+i+"' name='liulan"+i+"' type='file' size='45'>  <a href='#' onclick='deleteRow();' id='delete"+i+"' ln='"+i+"'>删除</a>";		
		--%>
		}
		document.getElementById("length").value=ft.rows.length;
	}

	function deleteRow(){
		var ft=document.getElementById("annex");
		line=parseInt(event.srcElement.ln,10);
		if (line>=1)
			for (var i=0; i<ft.rows.length ; i++)
			{
			    if (ft.rows[i].ln==line ){
			    	if (!confirm("确认删除?")) return;
			   		 ft.deleteRow(i);
			   		 for(var j=i+2;j<ft.rows.length+2;j++){	 	
			   		 	document.getElementById("td1"+j).innerHTML="<span class='wz'>附件"+(j-1)+":</span>";
			   		 	document.getElementById("td3"+j).innerHTML="<input id='annexId"+(j-1)+"' name='annexId"+(j-1)+"' type='text' size='20'>";
			   		 	document.getElementById("td5"+j).innerHTML="<input id='liulan"+(j-1)+"' name='liulan"+(j-1)+"' type='file' size='45'>  <a href='#' onclick='deleteRow();' id='delete"+(j-1)+"' ln='"+(j-1)+"'>删除</a>";	 
			   		 	document.getElementById("td1"+j).id="td1"+(j-1);
			   		 	document.getElementById("td2"+j).id="td2"+(j-1);
			   		 	document.getElementById("td3"+j).id="td3"+(j-1);
			   		 	document.getElementById("td4"+j).id="td4"+(j-1);
			   		 	document.getElementById("td5"+j).id="td5"+(j-1);
			   		 	document.getElementById("url"+j).ln=(j-1); 
						document.getElementById("url"+j).id="url"+(j-1); 		
			   		 }
				}
			}
			
	}
	
	function editAnnex(){
		window.showModalDialog("editAnnex.jsp","editAnnexWindow","dialogWidth:450pt;dialogHeight:400pt;status:no;help:no;scroll:no;status:0;help:0;scroll:0;")
	}			     	
</script>
	</head>
	<body class=contentbodymargin oncontextmenu="return false;"
		onload="createTable('add')">
		<DIV id=contentborder align=center>
			<form id="meetform" action="">
				<input type="hidden" id="length" name="length">
				<table width="100%"
					style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
					<tr>
						<td width="5%">
							<img src="<%=frameroot%>/images/ico.gif" width="9" height="9">
						</td>
						<td width="20%">
							添加会议议题
						</td>
						<td width="*">
							&nbsp;
						</td>
					</tr>
				</table>
				<table width="100%" height="10%" border="0" cellpadding="0"
					cellspacing="1" align="center" class="table1">
					<tr>
						<td width="15%" height="21" class="biao_bg1" align="right">
							<span class="wz">议题编号：</span>
						</td>
						<td class="td1" colspan="3" align="left">
							<input id="id" name="id" type="text" size="30">
						</td>
					</tr>
					
					<tr>
						<td width="15%" height="21" class="biao_bg1" align="right">
							<span class="wz">议题分类：</span>
						</td>
						<td class="td1" colspan="3" align="left">
							<select id="classific" name="classific" style="width:27%">
								<option value="议题分类(1)">
									议题分类(1)
								</option>
								<option value="议题分类(2)">
									议题分类(2)
								</option>
								<option value="议题分类(3)">
									议题分类(3)
								</option>
							</select>
						</td>
					</tr>
					
				<tr>
					<td colspan="1" height="21" class="biao_bg1" align="right">
						<span class="wz">会议时间：</span>
					</td>
					<td  colspan="3" class="td1" align="left">
					<strong:newdate name="startDate" id="startDate"  width="233"
                      skin="whyGreen" isicon="true" dateobj="${startDate}" dateform="yyyy-MM-dd HH:mm"></strong:newdate>
						
					&nbsp;&nbsp;&nbsp;&nbsp;--&nbsp;&nbsp;&nbsp;	<strong:newdate name="endDate" id="endDate"  width="233"
                      skin="whyGreen" isicon="true" dateobj="${endDate}" dateform="yyyy-MM-dd HH:mm"></strong:newdate>
					</td>

				</tr>
			
				<tr>
					<td colspan="1" height="21" class="biao_bg1" align="right">
						<span class="wz">会议地点：</span>
					</td>
					<td colspan="3" class="td1" align="left">
						<input id="place" name="place" type="text" size="97%">
					</td>
				</tr>
					<tr>
						<td width="15%" height="21" class="biao_bg1" align="right">
							<span class="wz">议题主题：</span>
						</td>
						<td class="td1" colspan="3" align="left">
							<input id="name" name="name" type="text" size="97">
						</td>
					</tr>
					<tr>
						<td width="15%" height="21" class="biao_bg1" align="right">
							<span class="wz">议题范围：</span>
						</td>
						<td class="td1" colspan="3" align="left">
							<input id="name" name="name" type="text" size="97">
						</td>
					</tr>
				
					<tr>
						<td height="21" class="biao_bg1" align="right">
							<span class="wz">议题内容：</span>
						</td>
						<td class="td1" colspan="3" align="left">
						<script type="text/javascript"
							src="<%=request.getContextPath()%>/common/js/fckeditor2/fckeditor.js"></script>
						<script type="text/javascript">
													 
													var oFCKeditor = new FCKeditor( 'content' );
													oFCKeditor.BasePath	= '<%=request.getContextPath()%>/common/js/fckeditor2/'
													oFCKeditor.ToolbarSet = "FutureDefaultSet" ;
													oFCKeditor.Width = '80%' ;
													oFCKeditor.align == 'left';
                                                    oFCKeditor.Height = '300' ;								
													oFCKeditor.Value	= '\n          \t\t' ;
													oFCKeditor.Create() ;
													 
                                                    </script>
					</td>
					</tr>
					<%-- 
					<tr>
						<td height="21" class="biao_bg1" align="right">
							<span class="wz">议题描述：</span>
						</td>
						<td class="td1" colspan="3" align="left">
							<textarea rows="8" cols="61" id="description" name="description"
								style="overflow:auto"></textarea>
						</td>
					</tr>
				--%>
				</table>
				<table id="annex" width="100%" height="10%" border="0"
					cellpadding="0" cellspacing="1" align="center" class="table1">
				</table>
			</form>
			<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td align="center" valign="middle">
						<table width="27%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td width="10%" height="34">
									&nbsp;
								</td>
								<td width="40%">
									<input name="Submit" type="submit" class="input_bg"
										value="添加附件" onclick="createTable('add')">
								</td>
								<td width="25%">
									<input name="Submit" type="submit" class="input_bg" value="保 存">
								</td>
								<td width="25%">
									<input name="Submit2" type="submit" class="input_bg"
										value="返 回" onclick="goback()">
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</DIV>
	</body>
</html>
