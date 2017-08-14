<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/security" prefix="security"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>

		<title>文号生成器</title>

		<link href="<%=frameroot%>/css/properties_windows_add.css" type="text/css"
			rel="stylesheet">
		<script type="text/javascript"
			src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script src="<%=path%>/common/js/upload/jquery.MultiFile.js"
			type="text/javascript" language="javascript"></script>
		<script language='javascript'
			src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<script src="<%=path%>/common/js/upload/jquery.blockUI.js"
			type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/common/common.js"
			type="text/javascript"></script>
		<script language="JavaScript"
			src="<%=path%>/common/js/commontab/service.js"></script>
		<script language="JavaScript"
			src="<%=path%>/common/js/commontab/workservice.js"></script>
		<script type="text/javascript">
		var showparagraph=function(){
		    var value= $("#paragraph").val();
		    if(value=="2"){
		    }
		}
		var cancel=function(){
		   returnValue="";
		    window.close();
		}
		
		var gonext=function(){
			window.location="<%=path%>/serialnumber/number/number.action";
			//window.history.go(-1);
		}
		var chooseregulation=function(){
		$("#result").html("<font color='#999999'>（请点击生成按钮）</font>");
		      var value=$("#chooseregulation").val();
		      if(value=='1'){
		           document.getElementById("isRegulation").style.display="none";
		           document.getElementById("zidingyi1").style.display="none";
		      }else if(value=='2'){
		         document.getElementById("isRegulation").style.display="block";
		         document.getElementById("zidingyi1").style.display="none";
		         selectRegulation();
		      }else if(value=='3'){
		      document.getElementById("zidingyi1").style.display="block";
		      document.getElementById("isRegulation").style.display="none";
		      }
		}
		//选择是否引用单位代字
		var selectRegulation=function(){
		   var regulationId=$("#regulation").val();
		   $("#result").html("<font color='#999999'>（请点击生成按钮）</font>");
		   if(regulationId==null||regulationId==""){
		   return ;
		   }
		     $.post('<%=path%>/serialnumber/regulation/regulation!selectSort.action',
		             { 'regulationId':regulationId},
		              function(data){
		              
		              if(data==null||data=='null'||data==''){
		                $("#isradio").empty(); 
		              }else{
		                $("#isradio").empty();
		                $("#isradio").html(data);
		                 
		                }
		       });
		}
		
		var chooseabb=function(){
		     var value= $("#abbreviate").val();
		     $("#text1").val(value);
		}
		//生成文号
		var toSub=function(){
		    
		       var abb=$("#abbreviate").val();
		       if(abb==undefined){
		       		abb="";
		       }
		       var symbol=$("#symbol").val();
		       if(symbol==null||symbol=="null"||symbol==""||symbol==undefined){
		           symbol="";
		       }else{
		          abb=abb+symbol;
		       }
		       var regulation=$("#regulation").val();
		       if(regulation==null||regulation==""){
		          alert("请选择文号规则！");
		          return;
		       }else{
		         //调用生成文号
		          toNumber("2",abb,regulation,"","",symbol);
		          }
		 }
		 //连接后台生成文号
		var toNumber=function(chooseregulation,abbreviation,regulation,rule,paragraph,dictcode){
		     $.post("<%=path%>/serialnumber/number/number!save.action",
		           {"chooseRegulation":chooseregulation,
		             "abbreviation":abbreviation,
		             "regulationId":regulation,
		             "rule":rule,
		             "paragraph":paragraph,
		             "dictcode":dictcode,
		             "division":""},
		           function(data){
		               $("#result").html("&nbsp;&nbsp;&nbsp;&nbsp; <input id='number' style='color: red;'  name='number' value='"+data+
		                    "'> <span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type='button' value='预留当前号' onclick='makeNumber()'></span>");
		           //  returnValue=data;
		       //      window.close();
		           });
		}
		//保存已经生成的文号
		function tosave(){
		   var abb=$("#abbreviate").val();
	       var symbol=$("#symbol").val();
	       if(symbol==null||symbol=="null"||symbol==""){
	           symbol="";
	       }else{
	          abb=abb+symbol;
	       }
	       var regulation=$("#regulation").val();
	       if($("input[name=radio]:checked").val()=='0'){
		   	   	returnValue=$("#number").val();
		   }else if($("input[name=radio]:checked").val()=='1'){
		        returnValue=$("#yuliuNumber").val();
		   }else{
		        returnValue=$("#recycleNumber").val();
		   }
		   $.post("<%=path%>/serialnumber/number/number!saveNumber.action",
		           {"chooseRegulation":"2",
		             "abbreviation":abb,
		             "regulationId":regulation,
		             "rule":"",
		             "paragraph":"",
		             "dictcode":symbol,
		             "model.numberNo":returnValue,
		             "division":""},
		           function(data){
		               
		       });
		 window.close();
		}
		//预留文号
		function makeNumber(){
		   var abb=$("#abbreviate").val();
	       var symbol=$("#symbol").val();
	       if(symbol==null||symbol=="null"||symbol==""){
	           symbol="";
	       }else{
	          abb=abb+symbol;
	       }
	       var regulation=$("#regulation").val();
	       var num=$("#number").val();
			   $.post("<%=path%>/serialnumber/number/number!makeNumber.action",
			           {"chooseRegulation":"2",
			             "abbreviation":abb,
			             "regulationId":regulation,
			             "rule":"",
			             "paragraph":"",
			             "dictcode":symbol,
			             "model.numberNo":num,
			             "division":""},
			           function(data){
			               alert(data);
			         });
		}
		
		//回收文号
		function toRecycle(){
		   var recy=$("#recycle").val();
		   if(recy==""){
		   alert("请输入要回收的文号！");
		   }else{
		   $.post("<%=path%>/serialnumber/number/number!recycleNumber.action",
		          {"model.numberNo":recy},
		          function(data){
		              if(data=="0"){
		                 alert("你当前所在机构部门不存在该文号,无法回收！");
		              }else if(data=="false"){
		                 alert("该文号已经预留或回收了，不需要再回收！");
		              }else{
		                 alert("回收成功！");
		                // document.getElementById("radio")[2].checked="checked";
		                 document.getElementsByName("radio")[2].checked="checkde";
		                 document.getElementById("dangqian").style.display="none";
						 document.getElementById("yuliu").style.display="none";
						 document.getElementById("recycleid").style.display="block";
		                 $("#recycleNumber").append("<option value='"+recy+"'>"+recy+"</option>");
		              }
		          });
		    }
		}
		</script>
		<script type="text/javascript">
		function show(){
		
			if($("input[name=radio]:checked").val()=='0'){
				 document.getElementById("dangqian").style.display="block";
				 document.getElementById("yuliu").style.display="none";
				 document.getElementById("recycleid").style.display="none";
			}else if($("input[name=radio]:checked").val()=='1'){
			     document.getElementById("dangqian").style.display="none";
				 document.getElementById("yuliu").style.display="block";
				 document.getElementById("recycleid").style.display="none";
			}else{
			     document.getElementById("dangqian").style.display="none";
				 document.getElementById("yuliu").style.display="none";
				 document.getElementById("recycleid").style.display="block";
				 
			}
		}
		//页面加载时调用
		$(document).ready(function(){
		$("input[name=radio]:eq(0)").attr("checked",true);
		    //选择是否引用单位代字
            selectRegulation();
        });
		</script>
		<script LANGUAGE=vbscript>
			function getpychar(char)
				tmp=65536+asc(char)
				if(tmp>=45217 and tmp<=45252) then 
					getpychar= "A"
				elseif(tmp>=45253 and tmp<=45760) then
					getpychar= "B"
				elseif(tmp>=45761 and tmp<=46317) then
					getpychar= "C"
				elseif(tmp>=46318 and tmp<=46825) then
				 	getpychar= "D"
				elseif(tmp>=46826 and tmp<=47009) then 
					getpychar= "E"
				elseif(tmp>=47010 and tmp<=47296) then 
					getpychar= "F"
				elseif(tmp>=47297 and tmp<=47613) then 
					getpychar= "G"
				elseif(tmp>=47614 and tmp<=48118) then
					getpychar= "H"
				elseif(tmp>=48119 and tmp<=49061) then
					getpychar= "J"
				elseif(tmp>=49062 and tmp<=49323) then 
					getpychar= "K"
				elseif(tmp>=49324 and tmp<=49895) then 
					getpychar= "L"
				elseif(tmp>=49896 and tmp<=50370) then 
					getpychar= "M"
				elseif(tmp>=50371 and tmp<=50613) then 
					getpychar= "N"
				elseif(tmp>=50614 and tmp<=50621) then 
					getpychar= "O"
				elseif(tmp>=50622 and tmp<=50905) then
					getpychar= "P"
				elseif(tmp>=50906 and tmp<=51386) then 
					getpychar= "Q"
				elseif(tmp>=51387 and tmp<=51445) then 
					getpychar= "R"
				elseif(tmp>=51446 and tmp<=52217) then 
					getpychar= "S"
				elseif(tmp>=52218 and tmp<=52697) then 
					getpychar= "T"
				elseif(tmp>=52698 and tmp<=52979) then 
					getpychar= "W"
				elseif(tmp>=52980 and tmp<=53640) then 
					getpychar= "X"
				elseif(tmp>=53689 and tmp<=54480) then 
					getpychar= "Y"
				elseif(tmp>=54481 and tmp<=62289) then
					getpychar= "Z"
				end if
			end function

			function getpy(str)
					getpy=getpy&getpychar(str)
					regulationForm.letter.value=regulationForm.letter.value+getpy
			end function
	</script>
	</head>
	<base target="_self">
	<body class="contentbodymargin">
		<input type="hidden" name="text1" id="text1">
		<div id="contentborder">
		<div align=left style="width:100%;padding:5px;">
					<tr>
					<td colspan="3" class="table_headtd">
							<table border="0" width="100%" cellpadding="0" cellspacing="0">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
								<strong>文号生成器</strong>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
					                <tr>
					                	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onclick="toSub();">&nbsp;生&nbsp;成&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
										<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onclick="tosave();">&nbsp;确&nbsp;定&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
				                  		<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
					                 	<td class="Operation_input1" onclick="gonext();">&nbsp;返&nbsp;回&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
				                  		<td width="6"></td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
					</td>
				</tr>
				</div>
			<div id="step1"
				style="padding: 10px 10px 10px 10px; font-size: 12px;">
				<fieldset>
					<legend>
						文号生成器
					</legend>
					<div style="padding: 10px 10px 10px 10px; text-align: left;">
						<fieldset id="isRegulation" style="border: 0">

							<div id="div1" style="padding: 10px 10px 10px 10px;">
								<div>
									<s:radio id="radio" name="radio" list="#{'0':'使用当前文号','1':'使用预留文号','2':'使用回收文号'}" onclick="show()"  onselect="0"/>
								</div>
								<br>
								<div id="dangqian" style="display: block;">
									<table>
										<tr>
											<td nowrap>
												请选择规则：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<select onchange="selectRegulation()" style="width: 110px;"
													id="regulation">
													<s:iterator id="vo" value="regulationList">
														<option value="${vo.regulationId }">
															${vo.regulationName }
														</option>
													</s:iterator>
												</select>
											</td>
											<td nowrap id="isradio">
											</td>
										</tr>
									</table>
									<br>
									<div id="step2">
										<br>
										<div>
											&nbsp; 当前文号：
											<span id="result"> </span>
										</div>

									</div>
									
								</div>
									<div id="yuliu"  style=" display: none;">
									<table><tr><td valign="top">
										预留文号：</td><td valign="top">
										<s:select id="yuliuNumber" list="makeList"
											cssStyle="width:200px;" size="7" listKey="numberNo"
											listValue="numberNo"></s:select></td></tr></table>
									</div>
									<div id="recycleid" style=" display: none;">
									<table><tr><td valign="top">
										回收文号：</td><td valign="top">
										<s:select id="recycleNumber" list="recycleList"
											cssStyle="width:200px;" listKey="numberNo" size="7"
											listValue="numberNo"></s:select></td></tr></table>
									</div>
									
							</div>
						</fieldset>
			</div>
			</fieldset></div>
			<security:authorize ifAllGranted="001-0002000700030003">
			<div>
				&nbsp;&nbsp;&nbsp;&nbsp; 回收文号：
				<input name="recycle" id="recycle">
				<!-- <input type="button" value="回收文号" onclick="toRecycle()"> -->
				<a   href="#" class="button" onclick="toRecycle();">回收文号</a>&nbsp;
			</div>
			</security:authorize>
			<br>
			<!-- <div style="width: 100%;" align="center">
				<span><input type="button" id="finish" value="生   成"
						onclick="toSub()" class="input_bg"> </span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

				<span><input type="button" id="finish" value="确   定"
						onclick="tosave()" class="input_bg"> </span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				&nbsp;
				<s:if test="doinput!=null&&doinput!=''">
					<span><input type="button" value="返   回" class="input_bg"
							onclick="gonext()"> </span>
				</s:if>
				<s:else>
					<span><input type="button" value="关   闭" class="input_bg"
							onclick="cancel()"> </span>
				</s:else>
			</div> -->

		</div>



	</body>
</html>
