<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>

		<title>序列生成器</title>

		<link href="<%=frameroot%>/css/properties_windows.css" type="text/css"
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
		if(returnValue==""||returnValue==null){
		   returnValue="";
		}
		    window.close();
		}
		
		var gonext=function(){
		window.history.go(-1);
		}
		var chooseregulation=function(){
		$("#result").empty();
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
		   $("#result").empty();
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
		//选择是否使用默认代字单选按钮
		var isChoose=function(){
		     var regulationId=$("#regulation").val();
		     var value=$("input[name=abb]:checked").val();
		     $("#result").empty();
		     if(value=='1'){
		        $("#chooseAbb").empty(); 
		     }else if(value=='2'){
		         $.post('<%=path%>/serialnumber/regulation/regulation!chooseAbbreviate.action',
		             { 'regulationId':regulationId},
		              function(data){
		             
		                 $("#chooseAbb").html(data);
		       });
		     }
		}
		
		var chooseabb=function(){
		     var value= $("#abbreviate").val();
		     $("#text1").val(value);
		}
		var saveSub=function(){
		    var value=$("#chooseregulation").val();
		    if(value=='1'){
		      saveNumber(value);
		    }else if(value=='2'){
		       var abb=$("#text1").val();
		       var regulation=$("#regulation").val();
		       if(regulation==null||regulation==""){
		          alert("请选择文号规则！");
		          return;
		       }else{
		          saveNumber(value,abb,regulation);
		          }
		    }else if(value=='3'){
		        var paragraph=$("#paragraph").val();
		        var rule=onSub();
		        if(rule==null||rule==""){
		        return ;
		        }else{
		        saveNumber(value,"","",rule,paragraph);
		        }
		    }
		}
		var saveNumber=function(chooseregulation,abbreviation,regulation,rule,paragraph){
		     var division=$("#division").val();
		     $.post("<%=path%>/serialnumber/number/number!save.action",
		           {"chooseRegulation":chooseregulation,
		             "abbreviation":abbreviation,
		             "regulationId":regulation,
		             "rule":rule,
		             "paragraph":paragraph,
		             "division":division},
		           function(data){
		               $("#result").html("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;文号： <input style='color: red; font-size: 16;'  onkeydown='return false;'   name='txt' id='txt' value='"+data+
		                    "'> <span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span><input type='button' value='复制' onclick='setTxt()'>");
		             returnValue=data;
		        //     window.close();
		           });
		}
		
		
     function setTxt() 
     { 
       var t=document.getElementById("txt"); 
        t.select(); 
       window.clipboardData.setData('text',t.createTextRange().text); 
      } 

		</script>
		<script type="text/javascript">
		
		
		var duanluo=function(value){
		    if(value=='2'){
		      document.getElementById("div2").style.display="block";
		      document.getElementById("div3").style.display="none";
		      document.getElementById("div4").style.display="none";
		     
		    }else if(value=='3'){
		      document.getElementById("div2").style.display="none";
		      document.getElementById("div3").style.display="block";
		      document.getElementById("div4").style.display="none";
		    }else if(value=='4'){
		      document.getElementById("div2").style.display="none";
		      document.getElementById("div3").style.display="none";
		      document.getElementById("div4").style.display="block";
		    }
		}
		$(document).ready(function(){
	      var paragraph=$("#paragraph").val();
	      
	      var htmldiv=duanluo(paragraph);
	      $("#paragraph").change(function(){
	          var value=$("#paragraph").val();
	           duanluo(value);
	           });
       });

		
		var sortSelectChange=function(){
		    var id="";
		     var paragraph=$("#paragraph").val();
		      if(paragraph=='3'){
		      id=$("#sortSelect").val();
		      }else if(paragraph=='4'){
		      id=$("#sortSelect1").val();
		      }
		 //   alert(id);
		    $.post('<%=path%>/serialnumber/sort/sort!select.action',
		             { 'sortId':id},
		              function(data){
		              if(paragraph=='3'){
		              $("#abbSelect").html(data);
		              }else if(paragraph=='4'){
		                $("#abbSelect1").html(data);
		              }
		       });
		}
		var addselectChange=function(){
		     var id="";
		     var paragraph=$("#paragraph").val();
		     if(paragraph=='3'){
		     id=$("#abbSelect").val();
		     $("#daizi3").val(id);
		    } else if(paragraph=='4'){
		    id=$("#abbSelect1").val();
		     $("#daizi4").val(id);
		     }
		}
		
		var addyear=function(){
		   var paragraph=$("#paragraph").val();
		   if(paragraph=='2'){
		   
		     $("#year2").val($("#selectYear2").val());
		   }else if(paragraph=='3'){
		    $("#year3").val($("#selectYear3").val());
		   }else if(paragraph=='4'){
		    $("#year4").val($("#selectYear4").val());
		   }
		}
		
		var addNumber=function(){
		    var paragraph=$("#paragraph").val();
		   if(paragraph=='2'){
		   
		     $("#number2").val($("#selectNumber2").val());
		   }else if(paragraph=='3'){
		    $("#number3").val($("#selectNumber3").val());
		   }else if(paragraph=='4'){
		    $("#number4").val($("#selectNumber4").val());
		   }
		}
		var onSub=function(){
		     var paragraph=$("#paragraph").val();
		     var year="";
		     var number="";
		     var abb="";
		     var letter="";
		     var sortId="";
		   if(paragraph=='2'){
		      year=$("#year2").val();
		      number=$("#number2").val();
		      if(year==""){
		      alert("请选择年份！");
		      return;
		      }else if(number==""){
		          alert("请选择文号类型！");
		          return;
		      }else{
		      var num=getNum(year)+","+getNum(number)
		         return num;
		         
		      }
		    
		   }else if(paragraph=='3'){
		        abb=$("#daizi3").val();
		        year=$("#year3").val();
		        number=$("#number3").val();
		        sortId=$("#sortSelect").val();
		        if(abb==""){
		           alert("请选择单位代字！");
		           return;
		        }else if(year==""){
		          alert("请选择年份！");
		          return;
		      }else if(number==""){
		          alert("请选择文号类型！");
		          return;
		      }else{
		        var num=abb+","+getNum(year)+","+getNum(number);
		         return num;
		      }
		   
		   }else if(paragraph=='4'){
		        abb=$("#daizi4").val();
		        year=$("#year4").val();
		        number=$("#number4").val();
		        letter=$("#letter").val();
		         sortId=$("#sortSelect1").val();
		        if(abb==""){
		           alert("请选择单位代字！");
		           return;
		        }else if(letter==""){
		            alert("请填写字母缩写！");
		            return;
		        }else if(year==""){
		          alert("请选择年份！");
		          return;
		      }else if(number==""){
		          alert("请选择文号类型！");
		          return;
		      }else{
		        var num=abb+","+letter+","+getNum(year)+","+getNum(number);
		          return num;
		      }
		   }
		}
		
		var getNum=function(value){
		     if(value=='当年'||value=='数字递增'){
		        return '1';
		     }else if(value=='去年'||value=='字母递增'){
		         return '2';
		     }else if(value=='前年'||value=='数字递增+"号"'){
		         return '3';
		         
		     }else if(value=='字母递增+"号"'){
		       return "4"
		     }
		}
		function getFirstwORD(){
				var SourceStr=document.getElementById("hanzi").value;
				document.getElementById("letter").value="";
				for(var i=0;i<SourceStr.length;i++){
					var subStr=SourceStr.substring(i,i+1);
					if(subStr!=""&&isNaN(subStr)){//为空或为数字不处理

						var str=/^[\u0391-\uFFE5]+$/;
						if(str.test(subStr)){//如果为中文

							getpy(subStr);
						}
						else{
							document.getElementById("letter").value+=subStr;
						}
					}
					else
						document.getElementById("letter").value+="";
 				}
			}
			
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
			<div id="step1"
				style="padding: 10px 10px 10px 10px; font-size: 12px;">
				<fieldset>
					<legend>
						序列生成器
					</legend>
					<div style="padding: 10px 10px 10px 10px; text-align: left;">


						<div style="padding: 10px 10px 10px 10px; text-align: left;">
							<div>
								生成规则：&nbsp;&nbsp;&nbsp;&nbsp;

								<select id="chooseregulation" style="width: 110px;"
									onchange="chooseregulation()">
									<option value="1">
										系统规则
									</option>
									<option value="2">
										单位规则
									</option>
									<option value="3">
										自定义规则
									</option>
								</select>
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 分隔符：&nbsp;&nbsp;&nbsp;&nbsp;
								<select id='division' style="width: 110px;" name='division'>
									<option value="">
										不使用分隔符
									</option>
									<option value="-">
										下划线分割
									</option>
									<option value="/">
										斜杠分割
									</option>
								</select>
							</div>

							<div id="nextstep" style="float: right;">

							</div>
						</div>

						<br>
						<fieldset id="isRegulation" style="display: none; border: 0">

							<div id="div1" style="padding: 10px 10px 10px 10px;">
								<div>
									<table>
										<tr>
											<td>
												请选择：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<select onchange="selectRegulation()" style="width: 110px;"
													id="regulation">
													<s:iterator id="vo" value="regulationList">
														<option value="${vo.regulationId }">
															${vo.regulationName }
														</option>
													</s:iterator>
												</select>
											</td>
											<td id="isradio">
											</td>
										</tr>
									</table>
								</div>
								<div id="chooseAbb">

								</div>
							</div>
						</fieldset>
					</div>
					<div id="zidingyi1"
						style="padding: 0px 10px 0px 10px; display: none;">
						<form action="<%=path%>/serialnumber/number/number!save.action"
							method="post" name="regulationForm" id="regulationForm">

							<div style="padding: 0px 10px 0px 10px; text-align: left;">
								<div>
									段落：&nbsp;&nbsp;&nbsp;&nbsp;

									<s:select id="paragraph" style="width: 110px;" name="paragraph"
										list="#{'两段':'2','三段':'3','四段':'4'}" listKey="value"
										listValue="key"></s:select>

								</div>
								
							</div>

							<div id="div2"
								style="padding: 10px 10px 10px 10px; display: block;">
								<table>
									<tr>
										<td nowrap>
											<span>第一段：</span>
											<input type='text' id='year2' onkeydown="return false;"
												value='当年' name='year2'>
										</td>
										<td nowrap align='right' width='150'>
											<span>选择年份--></span>
										</td>
										<td nowrap>
											<select id="selectYear2" style="width: 110px;"
												onchange="addyear()" style="width:80px;" class="search">
												<option value='当年'>
													当年
												</option>
												<option value='去年'>
													去年
												</option>
												<option value='前年'>
													前年
												</option>
											</select>
										</td>
									</tr>
									<tr>
										<td nowrap>
											<span>第二段：</span>
											<input type='text' value='数字递增' onkeydown="return false;"
												id='number2' name='number2'>
										</td>
										<td nowrap align='right'>
											<span>选择序号生成类型--></span>
										</td>
										<td nowrap>
											<select id="selectNumber2" style="width: 110px;"
												onchange="addNumber()">
												<option value='数字递增'>
														数字递增
													</option>
													<option value='数字递增+"号"'>
														数字递增+"号"
													</option>
													<option value='字母递增'>
														字母递增
													</option>
													<option value='字母递增+"号"'>
														字母递增+"号"
													</option>
											</select>
										</td>
									</tr>
								</table>
							</div>

							<div id="div3"
								style="padding: 10px 10px 10px 10px; display: none;">
								<table>
									<tr>
										<td nowrap>
											<span>第一段：</span>
											<input type='text' onkeydown="return false;" id='daizi3'
												name='daizi3'>
										</td>
										<td nowrap align="right" width='150'>
											<span>文号类型--></span>
										</td>
										<td nowrap>
											<select id="sortSelect" onchange="sortSelectChange()">
												<option value="">
													选择文号类型
												</option>
												<s:iterator id="vo" value="sortList">
													<option value="${vo.sortId }">
														${vo.sortName }
													</option>
												</s:iterator>
											</select>
											&nbsp; &nbsp;代字 &nbsp; &nbsp;
											<select id="abbSelect" onchange="addselectChange()"
												style="width: 80px;" class="search"><option value="">
													选择代字
												</option></select>
										</td>
									</tr>
									<tr>
										<td nowrap>
											<span>第二段：</span>
											<input type='text' value='当年' onkeydown="return false;"
												id='year3' name='year3'>
										</td>
										<td nowrap align='right' width='150'>
											<span>选择年份--></span>
										</td>
										<td nowrap>
											<select id="selectYear3" style="width: 110px;"
												onchange="addyear()" style="width:80px;" class="search">
												<option value='当年'>
													当年
												</option>
												<option value='去年'>
													去年
												</option>
												<option value='前年'>
													前年
												</option>
											</select>
										</td>
									</tr>
									<tr>
										<td nowrap>
											<span>第三段：</span>
											<input type='text' value='数字递增' onkeydown="return false;"
												id='number3' name='number3'>
										</td>
										<td nowrap align='right'>
											<span>选择序号生成类型--></span>
										</td>
										<td nowrap>
											<select id="selectNumber3" style="width: 110px;"
												onchange="addNumber()">
												<option value='数字递增'>
														数字递增
													</option>
													<option value='数字递增+"号"'>
														数字递增+"号"
													</option>
													<option value='字母递增'>
														字母递增
													</option>
													<option value='字母递增+"号"'>
														字母递增+"号"
													</option>
											</select>
										</td>
									</tr>
								</table>
							</div>
							<div id="div4"
								style="padding: 10px 10px 10px 10px; display: none;">
								<table>
									<tr>
										<td nowrap>
											<span>第一段：</span>
											<input onkeydown="return false;" type='text' id='daizi4'
												name='daizi4'>
										</td>
										<td nowrap align="right" width='150'>
											<span>文号类型--></span>
										</td>
										<td nowrap>
											<select id="sortSelect1" onchange="sortSelectChange()">
												<option>
													选择文号类型
												</option>
												<s:iterator id="vo" value="sortList">
													<option value="${vo.sortId }">
														${vo.sortName }
													</option>
												</s:iterator>
											</select>
											&nbsp; &nbsp;代字 &nbsp; &nbsp;
											<select id="abbSelect1" onchange="addselectChange()"
												style="width: 80px;" class="search">
												<option value="">
													选择代字
												</option>
											</select>
										</td>
									</tr>
									<tr>
										<td nowrap>
											<span>第二段：</span>
											<input onkeydown="return false;" type='text' id='letter'
												name='letter'>
										</td>
										<td nowrap align='right' width='150'>
											<span>字母缩写--></span>
										</td>
										<td nowrap>
											<input type="text" id="hanzi" style="width: 110px;"
												name="hanzi" onkeyup="getFirstwORD()">
										</td>
									</tr>
									<tr>
										<td nowrap>
											<span>第三段：</span>
											<input type='text' value='当年' onkeydown="return false;"
												id='year4' name='year4'>
										</td>
										<td nowrap align='right' width='150'>
											<span>选择年份--></span>
										</td>
										<td nowrap>
											<select id="selectYear4" onchange="addyear()"
												style="width: 110px;" class="search">
												<option value='当年'>
													当年
												</option>
												<option value='去年'>
													去年
												</option>
												<option value='前年'>
													前年
												</option>
											</select>
										</td>
									</tr>
									<tr>
										<td>
											<span>第四段：</span>
											<input type='text' value='数字递增' onkeydown="return false;"
												id='number4' name='number4'>
										</td>
										<td nowrap align='right'>
											<span>选择序号生成类型--></span>
										</td>
										<td nowrap>
											<select id="selectNumber4" style="width: 110px;"
												onchange="addNumber()">
												<option value='数字递增'>
														数字递增
													</option>
													<option value='数字递增+"号"'>
														数字递增+"号"
													</option>
													<option value='字母递增'>
														字母递增
													</option>
													<option value='字母递增+"号"'>
														字母递增+"号"
													</option>
											</select>
										</td>
									</tr>
								</table>
							</div>
						</form>
					</div>
					<div id="step2">
						<div >
							<div id="result" style="color: red; font-size: 16; border: 0">
							</div>
						</div>
					</div>
					<div style="width: 100%;" align="center">
					<span><input type="button" id="finish" value="生   成"
								onclick="saveSub()" class="input_bg"> </span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						
						<span><input type="button" id="finish" value="确   定"
								onclick="cancel()" class="input_bg"> </span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					&nbsp;
						<s:if test="doinput!=null&&doinput!=''">
							<span><input type="button" value="返   回" class="input_bg"
									onclick="gonext()"> </span>
						</s:if>
						<s:else>
							<span><input type="button" value="关   闭" class="input_bg"
									onclick="cancel()"> </span>
						</s:else>
					</div>
				</fieldset>
			</div>



		</div>

	</body>
</html>
