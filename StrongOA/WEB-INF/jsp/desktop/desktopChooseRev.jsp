<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/common/include/rootPath.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head> 
    <title>人员选择</title>
    
<style type="text/css">
*{ padding:0; marrgin:0; }
html{ color: #000; background-color:#f4f4f4; }
body{ font:12px/1.5 tahoma,arial,simsun,sans-serif; }
ul{ list-style:none; }
#tcmain{ padding:20px; font-family: Tahoma, Geneva, sans-serif; }
.tcm{ border:1px solid #d8d8d8; background-color:#fff; padding:12px; }
.tcmtx{ overflow:hidden; height:2px; background:url(<%=root %>/frame/theme-web/webmodel/images/dxhtimg16.gif) #dfdfdf no-repeat left top; }
.tcmtit{ font-size:14px; font-weight:bold; color:#333; height:26px; line-height:26px; padding-left:20px; background:url(<%=root %>/frame/theme-web/webmodel/images/dxhtimg15.gif) no-repeat left center; }
.tcmcont{ padding:14px 20px 4px; font-size:14px; }
.tcmcont ul{ height:180px; overflow:auto; }
.tcmcont li{ line-height:29px; border-bottom:1px dashed #d8d8d8; }
.tcmbutqr{ width:49px; height:23px; border:none; background:url(<%=root %>/frame/theme-web/webmodel/images/tcmqr.gif) no-repeat center center; }
.tcmbutqx{ width:49px; height:23px; border:none; background:url(<%=root %>/frame/theme-web/webmodel/images/tcmqx.gif) no-repeat center center; }
.tcminp{ padding:14px 8px 0; text-align:center; }
</style>
    
    <script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
			
    <script type="text/javascript">
   
    	$(document).ready(function(){
    		var params = window.dialogArguments;//得到父窗口传来的参数
    		var userNames = params[0];
    		//alert(userNames);
    		if(userNames != ""){
				var userArr = userNames.split(",");
				var str ="";
				for(var i=0;i<userArr.length;i++){					
					var userName = userArr[i].split("_")[0];
					var userLoginName = userArr[i].split("_")[1];										
					if(i == 0){
						str += "<li><input name='tradio' checked='checked' type='radio' value='"+userArr[i]+"'  />"+userName+"</li>";
					}else{
						str += "<li><input name='tradio' type='radio' value='"+userArr[i]+"'  />"+userName+"</li>";
					}
				}
				$("#ul").html(str);
			}
    	});
    	
    	function selected(){
    		var v = document.getElementsByName("tradio")
    		for (var i=0;i<v.length;i++){	
	 			if(v.item(i).checked){
	   					//alert(v.item(i).value);
	   					window.returnValue = v.item(i).value;
	   					window.close();
	 			}
			}
    		
    	}
    
    </script>
   
  </head>
  
  <body>
	<div id="tcmain">
	  <div class="tcm">
	    <h3 class="tcmtit">请选择人员</h3>
	    <div class="tcmtx"></div>
	    <div class="tcmcont">
	      <ul id="ul">
	      </ul>
	      <div class="tcminp"><input type="button" class="tcmbutqr" onclick="selected();" />&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" class="tcmbutqx" onclick="window.close();" /></p>
	    </div>
	  </div>
	</div>
  </body>
</html>
