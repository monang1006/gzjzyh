<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<%@include file="/common/include/rootPath.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
  <head>
    <%@include file="/common/include/meta.jsp" %>
    <title>打印配置</title>
    <link href="<%=frameroot%>/css/properties_windows.css" type="text/css" rel="stylesheet">
    <script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
    <script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
    <script type="text/javascript">
    	$(document).ready(function(){
    		$("#cbtn").click(function(){
    			window.returnValue="false";
    			window.close();
    		});
    		
    		$("#subbtn").click(function(){
    			var arr=new Array();
    			arr[0]=$("#PrintSum").val();				//打印总份数
    			arr[1]=$("#needprint").val();					//需要打印份数
    			arr[2]=$("#docHavePrintNum").val();				//已打印份数
    			arr[3]=$("#printNum").val();					//可以打印的份数
    			if(arr[1]!=""&&checkNum(arr[1])==false){
					alert("打印份数，只能输入数字！");
					document.getElementById("needprint").focus();
					return false;
				}				
				if(parseInt(arr[1])>arr[3]){
					alert("输入的打印份数超出可打印份数，请重新输入！");
					return ;
				}
    			window.returnValue=arr[0]+","+arr[1]+","+arr[2]+","+arr[3];
    			window.close();
    		}
    		);
    	});
    	
    		function checkNum(str){
 				var reg=/^[0-9]*$/;
 				var result=str.match(reg);   
			    if(result==null) return false;   
			    return true;   
 			}
 			
    </script>
  </head>
  <body class="contentbodymargin">
    <div id="contentborder">
          <div id="concurrentStep1" style="padding: 15px 15px 15px 15px; font-size: 12px">
			<fieldset>&nbsp; 
				<legend> 
					公文打印 
				</legend>
				<div style="padding: 15px 15px 15px 15px;text-align: left;">
					<fieldset>
						<legend>
							公文打印信息
						</legend>
						<div style="padding: 15px 15px 15px 15px; text-align: left;">
							<div>
								
								打印总份数：&nbsp;&nbsp;&nbsp;&nbsp;<span>${PrintSum}</span><input type="hidden" name=PrintSum id=PrintSum value="${PrintSum}"/><br>
								已打印份数：&nbsp;&nbsp;&nbsp;&nbsp;<span>${docHavePrintNum}</span><input type="hidden" name=docHavePrintNum id=docHavePrintNum value="${docHavePrintNum }"/><br>
								<input type="hidden" name=printNum id=printNum value="${printNum}"/>
							</div>
						</div>
					</fieldset>
					<br>
					<fieldset>
						<legend>
							打印配置 （可打印：<font color="red"> ${printNum}</font> 份）
						</legend>
						<div style="padding: 15px 15px 15px 15px; text-align: left;">
<%--							<div>--%>
<%--								<input name="printsing" id="print" type="radio" value="1" checked>打印公章&nbsp;&nbsp;<input name="printsing" id="noprint" value="0" type="radio">不打印公章<br>--%>
<%--								--%>
<%--							</div>--%>
							<div>
								
							</div>
							<div>
								
							</div>
							<div>
								打印份数：<input type="text" name=needprint id=needprint value="1">
							</div>
						</div>
					</fieldset>
				</div>
			</fieldset>
		</div>
		<div align=center>
			<input type="button" class="input_bg" name="subbtn" id="subbtn" value="打印"> &nbsp;&nbsp;&nbsp;<input type="button" class="input_bg" name="cbtn" id="cbtn" value="取消">
		</div>
    </div>
  </body>
</html>