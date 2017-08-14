<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<%-- 郑志斌 2010-5-9 15：22 添加修改   把“div3”和“div4”中的“第一段”，提出放入为“div5”中显示， 把“daizi4”统一改为“daizi3” --%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>

		<title>序列生成器</title>

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
		
		//选择段落
		var duanluo=function(value){
		    if(value=='2'){
		      document.getElementById("div2").style.display="block";
		      document.getElementById("div3").style.display="none";
		      document.getElementById("div4").style.display="none";
		       document.getElementById("div5").style.display="none";
		     
		    }else if(value=='3'){
		      document.getElementById("div2").style.display="none";
		      document.getElementById("div3").style.display="block";
		      document.getElementById("div4").style.display="none";
		      document.getElementById("div5").style.display="block";
		    }else if(value=='4'){
		      document.getElementById("div2").style.display="none";
		      document.getElementById("div3").style.display="none";
		      document.getElementById("div4").style.display="block";
		       document.getElementById("div5").style.display="block";
		    }
		}
	
		function numToYear(value){
		  if(value=="1"){
		  return "当年";
		  }else if(value=="2"){
		  return "去年";
		  }else if(value=="3"){
		  return "前年";
		  }
		}
		function numToNum(value){
		   if(value=="1"){
		   return "数字递增";
		   }else if(value=="2"){
		   return "字母递增";
		   }else if(value=="3"){
		   return '数字递增+"号"';
		   }else if(value=="4"){
		   return '字母递增+"号"';
		   }
		}
		function edit(value){
		  var id="${model.regulationId}";
		  var rule="${model.regulationRule}";
		  var rules=rule.split(",");
		  if(id!=""){
		    if(value=="2"){
		     $("#year2").val(numToYear(rules[0]));  
		      $("#number2").val(numToNum(rules[1]));
		    }else if(value=="3"){
		     $("#daizi3").val(rules[0]);
		    $("#year3").val(numToYear(rules[1]));  
		    $("#number3").val(numToNum(rules[2]));
		    }else if(value=="4"){
<%--		     $("#daizi4").val(rules[0]);--%>
		     $("#daizi3").val(rules[0]);
		     $("#letter").val(rules[1]);
		    $("#year4").val(numToYear(rules[2]));  
		    $("#number4").val(numToNum(rules[3]));
		    }
		  }
		}
		$(document).ready(function(){
	      var paragraph=$("#paragraph").val();
	      edit(paragraph);
	      var htmldiv=duanluo(paragraph);
	      //段落的改变时间
	      $("#paragraph").change(function(){
	          var value=$("#paragraph").val();
	           duanluo(value);
	           $("#label").empty();
	           });
	           var sort=$("#sortSelect").val();
	           if(sort!=""){
	           		sortSelectChange();
	           }
       });

		var cancel=function(){
		    window.history.go(-1);
		}
		
		var sortSelectChange=function(){
			var rule="${model.regulationRule}";
		    var rules=rule.split(",");
		    var id="";
		     var paragraph=$("#paragraph").val();
		      if(paragraph=='3'){
		      id=$("#sortSelect").val();
		      }else if(paragraph=='4'){
		      	id=$("#sortSelect").val();
<%--		      id=$("#sortSelect1").val();--%>
		      }
		 //   alert(id);
		    $.post('<%=path%>/serialnumber/sort/sort!select.action',
		             { 'sortId':id,
		                'sortName':encodeURI(rules[0])},
		              function(data){
		              if(paragraph=='3'){
		              $("#abbSelect").html(data);
		              }else if(paragraph=='4'){
		              		 $("#abbSelect").html(data);
<%--		                $("#abbSelect1").html(data);--%>
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
		    id=$("#abbSelect").val();
		    $("#daizi3").val(id); 
<%--		     $("#daizi4").val(id);--%>
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
		//获取年份
		var getyear=function(value){
		      var myDate = new Date();
		     var ye= myDate.getYear(); 
		  if(value=='1'){
		     var myDate = new Date();
		     return ye;
		  }else if(value=='2'){
		     ye=ye-1;
		     return ye;
		  }else{
		     ye=ye-2;
		     return ye;
		  }
		}
		
		var getNumber=function(value){
		    if(value=='1'){
		       return "1";
		    }else if(value=='2'){
		       return "ABTR";
		    }else if(value=='3'){
		    return "1号";
		    }else if(value=='4'){
		    return "ABTR号";
		    }
		}
		//测试
		var ceshi=function(){
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
		      var jieguo="["+getyear(getNum(year))+"]"+getNumber(getNum(number));
		        $("#label").text(jieguo);
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
		           var jieguo=abb+"〔"+getyear(getNum(year))+"〕"+getNumber(getNum(number));
		           $("#label").text(jieguo);
		      }
		   
		   }else if(paragraph=='4'){
		   	    abb=$("#daizi3").val();//郑志斌修改
<%--		        abb=$("#daizi4").val();--%>
		        year=$("#year4").val();
		        number=$("#number4").val();
		        letter=$("#letter").val();
		         sortId=$("#sortSelect").val();//郑志斌修改
<%--		         sortId=$("#sortSelect1").val();--%>
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
		          var jieguo=abb+letter+"〔"+getyear(getNum(year))+"〕"+getNumber(getNum(number));
		           $("#label").text(jieguo);
		      }
		   }
		}
		
		var onSub=function(){
		     var paragraph=$("#paragraph").val();
		     var year="";
		     var number="";
		     var abb="";
		     var letter="";
		     var sortId="";
		     var name= $.trim($("#regulationName").val());
		     var regulationId=$("#regulationId").val();		     
		     if(name==null||name==""){
		       alert("请输入规则名称！");
		       return;
		     }
		     if(name==null||name==""){
		       alert("请输入规则名称！");
		       return;
		     }
		     $.post("<%=path%>/serialnumber/regulation/regulation!IsRegulationName.action",
	           {"regulationName":name,"regulationId":regulationId},
	           function(data){
	           		if(data=="0"){
	           			alert("当前规则名称“"+name+"”已经存在,请重新输入！！");
		       			return;
	           		}	
	           		else{
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
					         $("#ruleYear").val(getNum(year));
					         $("#ruleNumber").val(getNum(number));
					         $("#regulationForm").submit();
					      }
					    
					   }else if(paragraph=='3'){
					        abb=$("#daizi3").val();
					        year=$("#year3").val();
					        number=$("#number3").val();
					        sortId=$("#sortSelect").val();
					        if(abb==""){
					           alert("请选择文号代字！");
					           return;
					        }else if(year==""){
					          alert("请选择年份！");
					          return;
					      }else if(number==""){
					          alert("请选择文号类型！");
					          return;
					      }else{
					      
					          $("#sortId").val(sortId);
					          $("#abbreviate").val(abb);
					          $("#ruleYear").val(getNum(year));
					          $("#ruleNumber").val(getNum(number));
					          $("#regulationForm").submit();
					      }
					   
					   }else if(paragraph=='4'){
			<%--		        abb=$("#daizi4").val();--%>
					         abb=$("#daizi3").val();//郑志斌修改
					        year=$("#year4").val();
					        number=$("#number4").val();
					        letter=$("#letter").val();
					        sortId=$("#sortSelect").val();//郑志斌修改
			<%--		         sortId=$("#sortSelect1").val();--%>
					        if(abb==""){
					           alert("请选择文号代字！");
					           return;
					        }else if(letter==""){
					            alert("请填写缩写内容！");
					            return;
					        }else if(year==""){
					          alert("请选择年份！");
					          return;
					      }else if(number==""){
					          alert("请选择文号类型！");
					          return;
					      }else{
					          $("#sortId").val(sortId);
					          $("#abbreviate").val(abb);
					          $("#ruleYear").val(getNum(year));
					          $("#ruleNumber").val(getNum(number));
					          $("#regulationForm").submit();
					      }
					   }
	           		}
		     });
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
				for(var i=0;i<SourceStr.length;i++)
				{
					var subStr=SourceStr.substring(i,i+1);
					if(subStr!=""&&isNaN(subStr))//为空或为数字不处理
					{
					    var str=/^[\u0391-\uFFE5]+$/;//字母 正则表达式
<%--						var str=/^[a-z\u4E00-\u9FA5]+$/;--%>//字母和中文 正规则表达式
						var partarn=/^[%_']+$/;
						if(str.test(subStr)){//如果为中文
								getpy(subStr);
						
						}else if(partarn.test(subStr)){ 
							 alert("请不要输入“%”,“_”和“ ’”特殊字符！");
							 document.getElementById("hanzi").value=SourceStr.substring(0,i);
							 return;
						}
						else{
								document.getElementById("letter").value+=subStr;
						}
						
					}
					else{
					     if(!isNaN(subStr)){
					       alert("请不要输入数字！");
					      document.getElementById("hanzi").value=SourceStr.substring(0,i);
					       return;
					     }
						document.getElementById("letter").value+="";
						}
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
		<form id="regulationForm" theme="simple"
			action="<%=path%>/serialnumber/regulation/regulation!save.action"
			method="post">
			<input type="hidden" id="ruleYear" name="ruleYear">
			<input type="hidden" id="ruleNumber" name="ruleNumber">
			<input type="hidden" id="abbreviate" name="abbreviate">
			<input type="hidden" id="sortId" name="sortId">
			<input type="hidden" id="regulationId" name="regulationId" value="${model.regulationId }">
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
								<strong>序列生成规则</strong>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
					                <tr>
										<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onclick="onSub();">&nbsp;确&nbsp;定&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
				                  		<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
					                 	<td class="Operation_input1" onclick="cancel();">&nbsp;返&nbsp;回&nbsp;</td>
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
					style="padding: 15px 15px 15px 15px; font-size: 12px;">
					<fieldset>
						<legend>
							序列生成规则
						</legend>
						<div id="step2"
							style="padding: 15px 15px 15px 15px; font-size: 12px;">
							
								<div style="padding: 15px 15px 15px 15px; text-align: left;">
									<div>
										<span>规则名称：&nbsp;</span>
										<input type="text" id="regulationName" maxlength="50"
											value="${model. regulationName}" name="model.regulationName">
										(例如:某某单位序列生成规则)
									</div>
								</div>
						</div>
						<div style="padding: 15px 15px 15px 15px; text-align: left;">
							
								<div style="padding: 15px 15px 15px 15px; text-align: left;">
									<div>
										规则类型：&nbsp;

										<s:select id="regulationSort" style="width: 110px;"
											name="regulationSort" list="#{'发文规则':'/senddoc/sendDoc','收文规则':'/recvdoc/recvDoc'}"
											listKey="value" headerKey="0" headerValue="多者规则" listValue="key"></s:select>

									</div>
									<div style="padding: 5px 5px 5px 5px; display: block;"></div>
									<div>
										选择段落：&nbsp;

										<s:select id="paragraph" style="width: 110px;"
											name="paragraph" list="#{'两段':'2','三段':'3','四段':'4'}"
											listKey="value" listValue="key"></s:select>

									</div>
									
									<div id="nextstep">
										&nbsp;
									</div>
								</div>
							
							<br>
							
								<div id="div2"
									style="padding: 15px 15px 15px 15px; display: block;">
									<table>
										<tr>
											<td>
												<span>第一段：&nbsp;</span>
												<input type='text' id='year2' value='当年'
													onkeydown="return false;" name='year2'>
											</td>
											<td align='right' width='150'>
												<span>选择年份：&nbsp;</span>
											</td>
											<td>
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
											<td>
												<span>第二段：&nbsp;</span>
												<input type='text' id='number2' value='数字递增'
													onkeydown="return false;" name='number2'>
											</td>
											<td align='right'>
												<span>选择序号生成类型：&nbsp;</span>
											</td>
											<td>
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
								
								<!-- 郑志斌 2010-5-9 15：22 添加修改   把“div3”和“div4”中的“第一段”，提出放入为“div5”中显示，  -->
								<div id="div5"
									style="padding: 15px 15px 15px 15px; display: none;">
									<table>
										<tr>
											<td>
												<span>第一段：</span>
												<input type='text' id='daizi3' onkeydown="return false;"
													name='daizi3'>
											</td>
											<td align='center' width='150'>
												<span>文号类型</span>
											</td>
											<td>
										
												<s:select list="sortList" id="sortSelect" name="model.sortId"  listKey="sortId" listValue="sortName" headerKey="" headerValue="请选择文号类型" onchange="sortSelectChange()"/>

											&nbsp; &nbsp; &nbsp; &nbsp;代字 &nbsp; &nbsp; &nbsp;
											<select id="abbSelect" onchange="addselectChange()"
												style="width: 80px;" class="search">
												<option value="">
													选择代字
												</option>
											</select>
										</td>
										</tr>
									</table>
									
								</div>																

								<div id="div3"
									style="padding: 15px 15px 15px 15px; display: none;">
									<table>
<%--									<tr>
											<td>
												<span>第一段：</span>
												<input type='text' id='daizi3' onkeydown="return false;"
													name='daizi3'>
											</td>
											<td align='center' width='150'>
												<span>文号类型</span>
											</td>
											<td>
												<select id="sortSelect" onchange="sortSelectChange()">
													<option>
														选择文号类型
													</option>
													<s:iterator id="vo" value="sortList">
														<option value="${vo.sortId }">
															${vo.sortName }
														</option>
													</s:iterator>
												</select>
												&nbsp; &nbsp; &nbsp; &nbsp;代字 &nbsp; &nbsp; &nbsp;
												<select id="abbSelect" onchange="addselectChange()"
													style="width: 80px;" class="search"><option value="">
													选择代字
												</option></select>
											</td>
										</tr>--%>
										<tr>
											<td>
												<span>第二段：</span>
												<input type='text' value='当年' onkeydown="return false;"
													id='year3' name='year3'>
											</td>
											<td align='center' width='150'>
												<span>选择年份</span>
											</td>
											<td>
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
											<td>
												<span>第三段：</span>
												<input type='text' value='数字递增' onkeydown="return false;"
													id='number3' name='number3'>
											</td>
											<td align='center'>
												<span>选择序号生成类型</span>
											</td>
											<td>
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
									style="padding: 15px 15px 15px 15px; display: none;">
									<table>
<%--										<tr>
											<td>
												<span>第一段：</span>
												<input onkeydown="return false;" type='text' id='daizi4'
													name='daizi4'>
											</td>
											<td align='center' width='150'>
												<span>文号类型</span>
											</td>
											<td>
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
												&nbsp; &nbsp; &nbsp; &nbsp;代字 &nbsp; &nbsp; &nbsp;
												<select id="abbSelect1" onchange="addselectChange()"
													style="width: 80px;" class="search">
													<option>
														请选择代字
													</option>
												</select>
											</td>
										</tr>--%>
										<tr>
											<td>
												<span>第二段：</span>
												<input onkeydown="return false;" type='text' id='letter'
													name='letter'>
											</td>
											<td align='center' width='150'>
												<span>字母缩写</span>
											</td>
											<td>
												<input type="text" id="hanzi" name="hanzi"
													style="width: 110px;" onkeyup="getFirstwORD()">
											</td>
										</tr>
										<tr>
											<td>
												<span>第三段：</span>
												<input type='text' onkeydown="return false;" id='year4'
													value='当年' name='year4'>
											</td>
											<td align='center' width='150'>
												<span>选择年份</span>
											</td>
											<td>
												<select id="selectYear4" style="width: 110px;"
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
											<td>
												<span>第四段：</span>
												<input type='text' onkeydown="return false;" value='数字递增'
													id='number4' name='number4'>
											</td>
											<td align='center'>
												<span>选择序号生成类型</span>
											</td>
											<td>
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

								<div style="float: right; padding: 15px 15px 15px 15px;">
									<input type="button" value="测  试" onclick="ceshi()">
									<label id="label"></label>
								</div>
						
						</div>
						<!-- <div style="width: 100%;" align="center">


							<span><input type="button" id="finish" value="确  定"
									onclick="onSub()" class="input_bg">
							</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<span><input type="button" value="返  回" class="input_bg"
									onclick="cancel();">
							</span>
						</div> -->
					</fieldset>
				</div>



			</div>
		</form>
	</body>
</html>
