<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<% 
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragrma","no-cache");
	response.setDateHeader("Expires",0);
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>添加车辆</title>
		<%@include file="/common/include/meta.jsp" %>
		<script language='javascript'
			src='<%=path%>/common/js/jquery/jquery-1.2.6.js'></script>
		<script language='javascript'
			src='<%=path%>/common/js/upload/jquery.MultiFile.js'></script>
		<script language='javascript'
			src='<%=path%>/common/js/upload/jquery.blockUI.js'></script>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<style type="text/css">
		#imageSrc
		{
			margin-left:5px; 
		    filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale,src='<%=path%>/oa/image/meetingroom/LOGO.jpg');
		}
		</style>
		<base target="_self">
	</head>
	<body oncontextmenu="return false;">
	<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
		<s:form action="/car/car!save.action" id="carform" name="carform" method="post" enctype="multipart/form-data">
		<input type="hidden" id="carId" name="model.carId"
								value="${model.carId}">
			<table width="100%"
				style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
				<tr>
					<td>&nbsp;</td>
					<td width="30%">
						<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
						添加车辆
					</td>
					<td width="70%">
						&nbsp;
					</td>
				</tr>
			</table>
            <table width="100%">
				<tr>
					<td height="10">	
					  
					</td>
				</tr>
			</table>
			<table align="center" width="90%" border="0" cellpadding="0"
				cellspacing="1" class="table1">
				<tr>
					<td rowspan="1" colspan="4"  height="40" class="td1" align="center">
						<span class="wz">车辆信息</span>
					</td>
					
				</tr>
				<tr>
					<td colspan="1" width="20%" height="21" class="biao_bg1" align="right">
						<span class="wz">车牌号(<font color="#FF6600">*</font>)：&nbsp;</span>
					</td>
					<td colspan="1" width="30%" class="td1" align="left">
						<input id="carno"  name="model.carno" value="${model.carno}" type="text" style="width:100%;">
					</td>
					<td  colspan="2" width="50%" class="td1">
					</td>
					
				</tr>
				<tr>
				    <td colspan="1" height="21" class="biao_bg1" align="right">
						<span class="wz">可乘人数(<font color="#FF6600">*</font>)：&nbsp;</span>
					</td>
					<td colspan="1" class="td1" align="left">
						<input id="takenumber"  name="model.takenumber" value="${model.takenumber}" type="text" style="width:100%;">
					</td>
					<td width="20%" colspan="1" height="21" class="biao_bg1" align="right">
						<span class="wz">购置日期(<font color="#FF6600">*</font>)：&nbsp;</span>
					</td>
					<td  width="30%" colspan="1" class="td1">
						<strong:newdate id="buydate" name="model.buydate" width="100%" dateobj="${model.buydate}"
							dateform="yyyy-MM-dd"></strong:newdate>
					</td>
					
				</tr>
				<tr>
				   
				   <td colspan="1"  height="21" class="biao_bg1" align="right">
						<span class="wz">车  型(<font color="#FF6600">*</font>)：&nbsp;</span>
					</td>
					<td colspan="1"  class="td1" align="left">
					<s:select list="carTypeList" listKey="dictItemValue" listValue="dictItemName"  headerKey="" headerValue="请选择车型"
							id="cartpye" name="model.cartype" />
					</td>	
					<td  height="21" class="biao_bg1" align="right">
						<span class="wz">状  态(<font color="#FF6600">*</font>)：&nbsp;</span>
					</td>
					<td  width="30%" class="td1" align="left">
					<s:select list="carStatusList" listKey="dictItemValue" listValue="dictItemName"  headerKey="" headerValue="请选择状态"
							id="status" name="model.status" />
					</td>
					
				</tr>
				<tr>
				    <td colspan="1" height="21" class="biao_bg1" align="right">
						<span class="wz">品  牌：&nbsp;</span>
					</td>
					<td colspan="1" class="td1" align="left">
						<input id="carbrand"  name="model.carbrand" value="${model.carbrand}" type="text" style="width:100%;">
					</td>
			    	<td  height="21" class="biao_bg1" align="right">
						<span class="wz">司  机：&nbsp;</span>
					</td>
					<td   class="td1" align="left">
					<input id="driver"  name="model.driver" value="${model.driver}" type="text" style="width:100%;">
					</td>
				</tr>
				<tr>
					<td rowspan="1" colspan="1"  height="21" class="biao_bg1" align="right">
						<span class="wz">车辆图片：&nbsp;</span>
					</td>
					<td rowspan="1" colspan="3" align="left" class="td1">
						
					     <div style="margin-left:5px;margin-top:5px" id="fileinput">
						<INPUT id="uploads" class="upFileBtn"  onkeydown="return false;" name=uploads TYPE="file" onchange="showimg()" style="display: block;">
						</div>
						<div id="delpic" style="display:none">
						<img src="<%=root%>/images/ico/shanchu.gif" width="15"
														height="15" class="img_s" onclick="del_pic();" style="cursor:hand">
									<span id="test"	style="cursor:hand" onclick="del_pic();">删除</span> 
						</div>
						<%-- <div style="margin-left:5px;margin-right:5px;margin-top:5px;margin-bottom:5px">
						   <IMG id="imageSrc" name=imageSrc src="F:\我的文档\My Pictures\011.jpg"   BORDER="0"> 
						--%>
						<div id="imageSrc"></div>
					</td>
				</tr>
            
				<tr>
					<td height="21" class="biao_bg1" align="right">
						<span class="wz">车辆描述：&nbsp;</span>
					</td>
					<td class="td1" colspan="3" align="left">
						<textarea  id="cardescription" name="model.cardescription" 
							             style="width:464px;height:150px;" >${model.cardescription}</textarea>
				    </td>
				</tr>
				<tr>
					<td class="td1" colspan="4" align="center" height="21">
					    <input name="Submit" type="button" class="input_bg" value="保 存" onclick="save();">
						<input name="Submit2" type="button" class="input_bg" value="取 消"
							onclick="window.close();">
					</td>
				</tr>
			</table>
		</s:form>
		</DIV>
<script language="javascript">


      function showimg(){
				var img=$("#uploads").val();//document.carform.uploads.value;
				if(img!=null&&img!=""){
				   $("#delpic").css("display","block");
					if(validimg(img)){
					    
						var image = document.getElementById("imageSrc");
						image.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = img;
					    image.style.width = "455px";
					    image.style.height = "179px";
                       $("#imageSrc").css("display","");
					//  document.carform.imageSrc.src=img;
					//  document.carform.imageSrc.width=100%;   
			        //  document.carform.imageSrc.height=179;  
			  	  //   }else{
			  	       
			  	   // 	var newPreview = document.getElementById("imageSrc");
					//    newPreview.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = "<%=path%>/oa/image/meetingroom/LOGO.jpg";
					//    newPreview.style.width = "140px";
					//    newPreview.style.height = "47px";
						
		  		 // 	   document.carform.uploads.value=null;
		  	  	//	   document.carform.imageSrc.src='<%=path%>/oa/image/car/LOGO.jpg';
		  	  	//	   document.carform.imageSrc.width=140;   
			     //      document.carform.imageSrc.height=47;
		     	 	  }
		      	  }
			}

      //验证图片
			function validimg(img){
				var ext = img.substring(img.lastIndexOf(".") + 1,img.length);
				if(ext ==("jpg")|ext=="gif"|ext=="JPG"|ext=="GIF"){
						return true;
					}else{
						alert("请输入正确的图片格式(jpg或gif)或删除！");
						return false;
						
						}
			}

    function del_pic(){
        document.getElementById("fileinput").innerHTML='<INPUT id="uploads" onkeydown="return false;" name=uploads TYPE="file" onchange="showimg()" style="display: block;">';
                        $("#delpic").css("display","none");
					    $("#imageSrc").css("display","none");
       }

	function isDigits(port){
  					if($.trim(port) == ""){
  						return false;
  					}else{
	  					var exp = /^\d+$/;
	  					return exp.test(port);
  					}
  				}
	//取出数字前的0
		function formatNumZore(num){
			if(num.indexOf("0")==0){
				num = num.replace("0","");
				return formatNumZore(num);
			}else{
				return num;
			}
		}  	

function save(){
    var inputDocument=document;
    var carNo = $.trim(inputDocument.getElementById("carno").value);
    inputDocument.getElementById("carno").value = carNo;
     if(""==carNo|null==carNo){
    	alert("车牌号不能为空！");
    	inputDocument.getElementById("carno").focus();
    	return false;
    }
      if(inputDocument.getElementById("carno").value.length>32){
    	alert("车牌号不能超过32个字符！");
    	inputDocument.getElementById("carno").focus();
    	return false;
    }
    var re1 = new RegExp("^([\u4E00-\uFA29]|[\uE7C7-\uE7F3]|[a-zA-Z0-9])*$");
	 if (!re1.test(carNo)){
	 	 alert("车牌号只能输入汉字，字母或数字！");
	      $('carno').focus();
	      return false;
	 }
    
    
    var url = "<%=path%>/car/car!isExistCarno.action";
		 	$.ajax({
						type:"post",
						url:url,
						data:{
							'carno':encodeURI(carNo),
							'carId':$("#carId").val()
						},
						success:function(data){
		                    	if(data=="succ"){
								 	  if(inputDocument.getElementById("cartpye").value.length==null || inputDocument.getElementById("cartpye").value.length==""){
									   	alert("请选择车型！");
									    	inputDocument.getElementById("cartpye").focus();
									   	return false;
									   }
									      
									 	var numb = $("#takenumber").val();
										 numb = formatNumZore(numb);
										 inputDocument.getElementById("takenumber").value = numb
									   if(inputDocument.getElementById("takenumber").value.length>3){
									    	alert("可乘人数不能超过3位数！");
									    	inputDocument.getElementById("takenumber").focus();
									   	return false;
									    }
									    
									   	if(!isDigits($("#takenumber").val())){
														alert("可乘人数不能为空且只能为数字！");
														inputDocument.getElementById("takenumber").focus();
														return ;
										}
									    if(inputDocument.getElementById("status").value.length==null || inputDocument.getElementById("status").value.length==""){
									    	alert("请选择状态！");
									    	inputDocument.getElementById("status").focus();
									    	return false;
									    }
									    
									     if(inputDocument.getElementById("buydate").value.length==null || inputDocument.getElementById("buydate").value.length==""){
									    	alert("请选择购置日期！");
									    	inputDocument.getElementById("buydate").focus();
									    	return false;
									    }
									    if(inputDocument.getElementById("cardescription").value.length>1000){
									    	alert("车辆描述不能超过1000个字符！");
									    	inputDocument.getElementById("cardescription").focus();
									    	return false;
									    }
									    
									     if(inputDocument.getElementById("carbrand").value.length>32){
									    	alert("品牌不能超过32个字符！");
									    	inputDocument.getElementById("carbrand").focus();
									    	return false;
									    }
									    
									      if(inputDocument.getElementById("driver").value.length>32){
									    	alert("司机不能超过32个字符！");
									    	inputDocument.getElementById("driver").focus();
									    	return false;
									    }
									    
									    var img=$("#uploads").val();
									    if (img!="" && img!=null){
										    if(!validimg(img)){
										      return false;
										    }
									    }
									   carform.submit();
		                    	}else{
		                    		alert("车牌号重复，不能添加！");
		                    		$('carno').focus();
		                    	}
	                    	},
						error:function(data){
							alert("对不起，操作异常"+data);
						}
				   });
				   

    

}

</script>
	</body>
</html>
