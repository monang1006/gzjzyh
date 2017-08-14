<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp" %>

<%@ taglib uri="/tags/c.tld" prefix="c" %>
<%@ page import="com.strongit.bo.ListTest"%>

<html>
	<head>
		<title>会议记录</title>
		<%@include file="/common/include/meta.jsp" %>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
		<link type="text/css" rel="stylesheet" href="<%=path%>/common/js/tabpane/css/luna/tab.css" />
		<script type="text/javascript" src="<%=path%>/common/js/tabpane/js/tabpane.js"></script>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/btn.js" type="text/javascript"></script>
<style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
	height: 100%
}
</style>
<%
		List testList = new ArrayList();
		for (int i = 1; i <= 5; i++) {
			ListTest test = new ListTest();
			test.setId(new Long(i));
			test.setName("张三(" + i + ")");
			test.setMoney("紧急事件...");
			test.setItem("李四(" + i + ")");
			test.setImg1("1");
			testList.add(test);
		}
		request.setAttribute("testList", testList);
%>
<script type="text/javascript">
var num=2;
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
			c3.innerHTML="<input id='liulan"+i+"' name='liulan"+i+"' type='file' size='45'>";			
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

	
	</script>
	</head>
	<base target="_self">
	<body class="contentbodymargin" onload="createTable('edit')">
		<script type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder style="overflow: auto;" cellpadding="0">
		<label style="display: none;"></label>
			<form action="<%=root %>/address/address!save.action" method="post">
				
				<table border="0" width="100%" bordercolor="#FFFFFF" cellspacing="0"
					cellpadding="0">
					<tr>
						<td colspan="2" style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);"><img src="<%=frameroot%>/images/ico.gif" width="9" height="9">&nbsp;&nbsp;&nbsp;&nbsp;会议记录</td>
					</tr>
					<tr >
						<td width="100%">
							<DIV class=tab-pane id=tabPane1>
								<SCRIPT type="text/javascript">
								tp1 = new WebFXTabPane( document.getElementById( "tabPane1") ,false );
								</SCRIPT>
								
								<DIV class=tab-page id=tabPage2>
									<H2 class=tab>
										纪要内容
									</H2>
									<table border="0" width="100%" cellpadding="2" cellspacing="1" align="center">
									
									<tr height="30">
											<img src="<%=path%>/oa/image/address/person.bmp"/>&nbsp;&nbsp;&nbsp;&nbsp;
											在这里输入纪要情况<br>
											 <hr/>
										</tr>
										
								   <tr>
					<td width="15%" height="21" class="biao_bg1" align="right">
						<span class="wz">纪要编号：</span>
					</td>
					<td class="td1"  align="left">
						<input id="id" name="id" type="text" size="30" readonly="readonly">
						<input id="length" name="length" type="hidden" size="25"
							readonly="readonly">
					</td>
					<td width="15%" height="21" class="biao_bg1" align="right">
						<span class="wz">纪要名称：</span>
					</td>
					<td class="td1" align="left">
						<input id="id" name="id" type="text" size="30" readonly="readonly">
						<input id="length" name="length" type="hidden" size="25"
							readonly="readonly">
					</td>
				</tr>
				<tr>
					<td width="15%" height="21" class="biao_bg1" align="right">
						<span class="wz">主 持 人：</span>
					</td>
					<td class="td1"  align="left">
						<input id="name" name="name" type="text" size="30">
					</td>
					<td width="15%" height="21" class="biao_bg1" align="right">
						<span class="wz">记 录 人：</span>
					</td>
					<td class="td1"  align="left">
						<input id="type" name="type" type="text" size="30">
					</td>
					
				</tr>
				<tr>
					<td height="21" class="biao_bg1" align="right">
						<span class="wz">纪要内容：</span>
					</td>
					<td class="td1" colspan="3" align="left">
						<script type="text/javascript"
							src="<%=request.getContextPath()%>/common/js/fckeditor2/fckeditor.js"></script>
						<script type="text/javascript">
													 
													var oFCKeditor = new FCKeditor( 'content' );
													oFCKeditor.BasePath	= '<%=request.getContextPath()%>/common/js/fckeditor2/'
													oFCKeditor.ToolbarSet = "FutureDefaultSet" ;
													oFCKeditor.Width = '100%' ;
													oFCKeditor.align == 'left';
                                                    oFCKeditor.Height = '300' ;								
													oFCKeditor.Value	= '\n          \t\t' ;
													oFCKeditor.Create() ;
													 
                                                    </script>
					</td>
				</tr>
								        
								    </table>
								    <table id="annex" width="100%" height="10%" border="0" cellpadding="0"
				cellspacing="1" align="center" class="table1">
			</table>
								<SCRIPT type="text/javascript">tp1.addTabPage( document.getElementById( "tabPage2" ) );</SCRIPT>

								</DIV>
								
								<DIV class=tab-page id=tabPage4>
									<H2 class=tab >
										待办事项
									</H2>
									<SCRIPT type="text/javascript">tp1.addTabPage( document.getElementById( "tabPage4" ) );</SCRIPT>
									<table border="0" width="100%" cellpadding="2" cellspacing="1" align="center">
								    <tr height="50">
											<img src="<%=path%>/oa/image/address/company.bmp"/>&nbsp;&nbsp;&nbsp;&nbsp;
											在这里输入待办工作信息<br>
											 <hr/>
										</tr>
								  <tr>
						<td width="100%" height="100" align="center">
							
												<table>


<tr>

     <td>

 <table id="works" style="width:100%" border="1" cellspacing="0" cellpadding="1" >

        <tr>

   <td class="td_name" width="300" align="center">工作名称</td>

   <td class="td_name" width="100" align="center">处 理 人</td>
   <td class="td_name" width="300" align="center">处理状态</td>
   <td class="td_name" width="50">&nbsp;操 作</td>
     </tr>
     <c:forEach items="${testList}" var="apply" varStatus="status">
     <tr>
    <td class="td_name" width="300" align="center"> <c:out value="${apply.money}"/></td>
   <td class="td_name" width="100" align="center"> <c:out value="${apply.item}"/></td>
   <td class="td_name" width="300" align="center">
<input type='radio' name='worksr' value='1' style='WIDTH: 20px; font-size:9pt; color:#000000'/>&nbsp;未处理 
<input type='radio' name='worksr' value='2' style='WIDTH: 20px; font-size:9pt; color:#000000' />&nbsp;正在处理 
<input type='radio' name='worksr' value='3' style='WIDTH: 20px; font-size:9pt; color:#000000' />&nbsp;处理完


	
</td>
   <td class="td_name" width="50">&nbsp;</td>
     
     </tr>
    
      </c:forEach>
    </table>
    <tr>

</tr>

    </td>

 </tr>

</table>
							
						</td>
					</tr>
				
								    
								    </table>
								</div>
								
								<DIV class=tab-page id=tabPage3>
									<H2 class=tab>
										考勤情况
									</H2>
									<table border="0" width="100%" cellpadding="2" cellspacing="1" align="center">
								    <tr height="50">
											<img src="<%=path%>/oa/image/address/home.bmp"/>&nbsp;&nbsp;&nbsp;&nbsp;
											在这里输入考勤情况<br>
											 <hr/>
										</tr>
										
				<tr>
					<td height="100" width="100%" align="center">
						<table>


<tr>

     <td>

 <table id="family" style="width:100%" border="1" cellspacing="0" cellpadding="1" >

        <tr>

   <td class="td_name" width="200" align="center"> 姓 名 </td>

   <td class="td_name" width="400" align="center">参加会议情况</td>
   <td class="td_name" width="50">&nbsp;操 作</td>

     </tr>
         <c:forEach items="${testList}" var="apply" varStatus="status">
					<tr>
         <td class="td_name" width="190" align="center">
             <c:out value="${apply.name}"/>
                </td>
                <td class="td_name" width="400" align="center">
  <input type='radio' name='chuqin' value='1' style='WIDTH: 20px; font-size:9pt; color:#000000' />&nbsp;出席 
      <input type='radio' name='chuqin' value='2' style='WIDTH: 20px; font-size:9pt; color:#000000' />&nbsp;缺席
         <input type='radio' name='chuqin' value='3' style='WIDTH: 20px; font-size:9pt; color:#000000' />&nbsp;请假 
            <input type='radio' name='chuqin' value='4' style='WIDTH: 20px; font-size:9pt; color:#000000' />&nbsp;迟到 
   </td>
<td class="td_name">&nbsp;</td>
         </tr>
	   </c:forEach>

    </table>
    <tr>

     

</tr>

    </td>

 </tr>

</table>
						
				
					</td>
				</tr>
								        
								    </table>
								<SCRIPT type="text/javascript">tp1.addTabPage( document.getElementById( "tabPage3" ) );</SCRIPT>

								</DIV>
								</DIV>
							</DIV>
						</td>
					</tr>
					<tr height="80">
						<td align="center">
							<input type="button" id="btn_addaddress"  class="input_bg" icoPath="<%=frameroot%>/images/queding.gif" value="  确定"/>&nbsp;&nbsp;
							<input type="button" id="btnNo"  class="input_bg" icoPath="<%=frameroot%>/images/quxiao.gif"  onclick="history.go(-1)" value="  返回"/>
						</td>
						<td>&nbsp;</td>
					</tr>
				</table>
			</form>
		</DIV>
		
		
	</body>

</html>
