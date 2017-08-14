<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
 <head>
  <title> 很抱歉，系统出现错误，请联系管理员 </title>
   <link href="${base}/oa/image/msg/msg.css" type="text/css" rel="Stylesheet" />
   <script type="text/javascript" src="${base}/oa/js/msg/msg.js"></script>
	<script>
		function test(){
			var html = "很抱歉，系统出现错误，请联系管理员。";
			html += "<div id='trace' style='font-size: 9pt; display: none'>";
			html += "<textarea id='errormessage' name='textarea' rows='10' readonly wrap=off class=input-area style='WIDTH: 97%; HEIGHT: 100%'>";
			html += document.getElementById("errorinput").value;
			html += "</textarea></div>";
			Showbo.Msg.show({buttons:{yes:'详细信息',no:'确定'},msg:html,title:'出错啦',icon:'error',width:400,fn:fn});
		}
		function fn(a){
			if(a == "no"){
				Showbo.Msg.hide();
				 var parWin = window.dialogArguments || window.opener;
			    if(parWin != null){
			    	window.close();
			    } else {
			    	//history.back();
			    }
			} else {
				var trace = document.getElementById("trace");
				if(trace.style.display=="none"){
					trace.style.display="block";
				}else{
					trace.style.display="none";
				}	
			}
		}
	</script>
 </head>

 <body onload="test()">
  	<textarea id="errorinput" rows="12" cols="25" style="display: none;">${Request['detailMessage']}</textarea>
 </body>
</html>