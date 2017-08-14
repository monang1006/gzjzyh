<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>复合查询</title>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<SCRIPT language="javascript"
			src="<%=root%>/common/scripts/validator.js"></SCRIPT>
		<SCRIPT language="javascript"
			src="<%=root%>/common/scripts/handleEnter.js"></SCRIPT>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
			<script src="<%=path%>/common/js/common/common.js"
			type="text/javascript"></script>
		<SCRIPT language="javascript"
			src="<%=path%>/oa/js/personnel/choosetext.js"></SCRIPT>
		<STYLE>
			@media All{
				input{ 
					behavior: url("<%=root%>/oa/js/personnel/textfield.htc");
				}
			}
		</STYLE>
		<SCRIPT>
		window.name="targetForm";
		var isOpenThisWindow=true;
    	var currentline=0;//记录当前行

		String.prototype.trim = function() {
				var strTrim = this.replace(/(^\s*)|(\s*$)/g, "");
				strTrim = strTrim.replace(/^[\s　\t]+|[\s　\t]+$/, ""); 
			    return strTrim;
		}
		
		function isMatch(str){   
          $.ajax({
			type:"post",
			data:{
				bds:str			
			},
			url:"<%=path%>/historyquery/query!isRegulate.action",
			success:function(data){	
			    return data;
			},
			error:function(data){
				alert("对不起，操作异常"+data);
			}
		}); 
      	}   
		
		
	  	function isExpression(str){  
			var   re=/^[\d+*()]+$/g; 
			var   re2=/\d\(/g; 
			var   re3=/\(/g; 
			var   re4=/\)/g; 
			var   re5=/[+*][+*]/g; 
			var l=str.match(re3);
			var r=str.match(re4);
			var length1=0;
			var length2=0;
			if(l!=null&&l!=""){
				length1=l.length;
			}
			if(r!=null&&r!=""){
				length2=r.length;
			}
			var index1=str.indexOf("(");
			var index2=str.indexOf(")");
			if(index2<index1){
				return "no";
			}
			if(re.test(str)&&(!re2.test(str))&&((l==r)||(length1==length2))&&(!re5.test(str))){
				return "ok";
			} else {
				return "no";
			} 
	 	}
		
		function setQueryDes(){
			var bdsvalue=document.getElementById("bds").value;
    		var operatorvalue;
    		if(document.getElementById("name5")!=undefined){
    			var operatorobj=document.getElementById("operator5");
    			for(var i=0;i<operatorobj.length;i++){
    				if(operatorobj.options[i].value==operatorobj.value)
    					operatorvalue=operatorobj.options[i].text;
    			}
    			bdsvalue=bdsvalue.replace("6",document.getElementById("name5").value+operatorvalue+document.getElementById("infoItemValue6").value);
    		}
    		if(document.getElementById("name4")!=undefined){
    			var operatorobj=document.getElementById("operator4");
    			for(var i=0;i<operatorobj.length;i++){
    				if(operatorobj.options[i].value==operatorobj.value)
    					operatorvalue=operatorobj.options[i].text;
    			}
    			bdsvalue=bdsvalue.replace("5",document.getElementById("name4").value+operatorvalue+document.getElementById("infoItemValue5").value);
    		}
    		if(document.getElementById("name3")!=undefined){
    			var operatorobj=document.getElementById("operator3");
    			for(var i=0;i<operatorobj.length;i++){
    				if(operatorobj.options[i].value==operatorobj.value)
    					operatorvalue=operatorobj.options[i].text;
    			}
    			bdsvalue=bdsvalue.replace("4",document.getElementById("name3").value+operatorvalue+document.getElementById("infoItemValue4").value);
    		}
    		if(document.getElementById("name2")!=undefined){
    			var operatorobj=document.getElementById("operator2");
    			for(var i=0;i<operatorobj.length;i++){
    				if(operatorobj.options[i].value==operatorobj.value)
    					operatorvalue=operatorobj.options[i].text;
    			}
    			bdsvalue=bdsvalue.replace("3",document.getElementById("name2").value+operatorvalue+document.getElementById("infoItemValue3").value);
    		}
    		if(document.getElementById("name1")!=undefined){
    			var operatorobj=document.getElementById("operator1");
    			for(var i=0;i<operatorobj.length;i++){
    				if(operatorobj.options[i].value==operatorobj.value)
    					operatorvalue=operatorobj.options[i].text;
    			}
    			bdsvalue=bdsvalue.replace("2",document.getElementById("name1").value+operatorvalue+document.getElementById("infoItemValue2").value);
    		}
    		if(document.getElementById("name0")!=undefined){
    			var operatorobj=document.getElementById("operator0");
    			for(var i=0;i<operatorobj.length;i++){
    				if(operatorobj.options[i].value==operatorobj.value)
    					operatorvalue=operatorobj.options[i].text;
    			}
    			bdsvalue=bdsvalue.replace("1",document.getElementById("name0").value+operatorvalue+document.getElementById("infoItemValue1").value);
    		}
    		bdsvalue=bdsvalue.replace(/\*/g," 并且 ");//new RegExp("infoItemValue"+(num+1),"gi")
    		bdsvalue=bdsvalue.replace(/\+/g," 或者 ");
    		document.getElementById("querydescript").value=bdsvalue;
		}
		
		function cancelRecomm(){
			window.close();
		}
		
		function getFields(value){
			$.ajax({
				type:"post",
				data:{
					infoSetValue:value		
				},
				url:"<%=path%>/historyquery/query!getFields.action",
				success:function(data){	
					if(data==null){
						alert("该信息集没有可操作的信息项");
					}else{	
	                   	var JSONobj = eval('('+data+')');
	                   	document.getElementById("infoSetValue").value=value;
						createFields(JSONobj);
					}
				},
				error:function(data){
					alert("对不起，操作异常"+data);
				}
			});  
		}
		
		function createFields(field){
			var checkField=document.all.item;
			var module="${module}";
			if(module!=null&&module=="recordQuery"){	//综合查询模块
				var groupByFiled=document.all.groupByFiled;	
			}
			while(length!=0){  
       			var length=checkField.options.length;
     			for(var i=0;i<length;i++)
           			checkField.options.remove(i);
           		if(module!=null&&module=="recordQuery"){//综合查询模块
	           		for(var i=0;i<length;i++)
	           			groupByFiled.options.remove(i);

	           	}
       			length=length/2;
 			}
 			if(module!=null&&module=="recordQuery"){	//综合查询模块
	 			var opt=new Option("请选择分组信息项","",true,true);  
				groupByFiled.options[groupByFiled.options.length]=opt; 
			}
			for(var i=0;i<field.length;i++){
				var opt=new Option(field[i]["CHECK_FIELD_NAME"],field[i]["CHECK_FIELD_ID"],true,true)  
				checkField.options[checkField.options.length]=opt; 
				if(module!=null&&module=="recordQuery"){	//综合查询模块
					var opt=new Option(field[i]["CHECK_FIELD_NAME"],field[i]["CHECK_FIELD_VALUE"],true,false)  
					groupByFiled.options[groupByFiled.options.length]=opt; 
				}
			}
			checkField.selectedIndex=0;	
		}
		
		function getOtherFields(){ 
			var ft=document.getElementById("addTable");
		    var infoItemId=document.getElementById("item").value;
		    if(infoItemId==""){
				alert("请选择信息项！");
				return;
			}
			var ft=document.getElementById("addTable");
		    var len=ft.rows.length;//得到表的总行数
		    if(len==0){
		    	// alert("请添加条件显示框");
		    }else{	
				var infoSetValue=document.getElementById("infoSetValue").value
				len=currentline+1;
				var length=String(len);	
				$.ajax({
					type:"post",
					data:{
						infoSetValue:infoSetValue,
						infoItemId:infoItemId,
						length:length				
					},
					url:"<%=path%>/historyquery/query!getOtherCheckFields.action",
					success:function(data){	
						var arr= new Array()
						arr=data.split("|");		
	                   	var JSONobj = eval('('+arr[0]+')');
						createOperator(JSONobj);
						createObject(arr[1]);	
				   		createText(arr[2]);
				   		createObject2(arr[3])
					},
					error:function(data){
						alert("对不起，操作异常"+data);
					}
				});  
			}
		}
		
		function createObject(name)
		{
			var Name=document.getElementById("name"+currentline);
			Name.value=name;
		}
		
		function createObject2(values)
		{
			var Name=document.getElementById("values"+currentline);
			Name.value=values;
		}
		
		function createOperator(field){
			var operator=document.getElementById("operator"+currentline);
			while(length!=0){  
      			var length=operator.options.length;
    			for(var i=0;i<length;i++)
          			operator.options.remove(i);
      			length=length/2;
			}
			for(var i=0;i<field.length;i++){
				var opt=new Option(field[i]["CHECK_OPERATOR_NAME"],field[i]["CHECK_OPERATOR_ID"],true,true)  
				operator.options[operator.options.length]=opt; 
			}
			operator.selectedIndex=0;
		}
		
		function createText(testtext){
			var testtd=document.getElementById("texttd"+currentline);
			testtd.innerHTML=testtext;
	   	}
	   	
	   function addConditon(){
 			var ft=document.getElementById("addTable");
 			var len=ft.rows.length;//得到表的总行数
			if(len>"5")
				alert("表达式不能超过6条！！");
			else{
 				document.all.length.value=len;
 				var flag=1;
 				//for(var i=0;i<len;i++)
		    	//{
		    	//	var namevalue = document.getElementById("name"+i).value;
		    	//	if(namevalue=null||namevalue==''){
		    	//		alert("请输入查询条件或查询条件无值！！");
		    	//		flag=0;
		    	//	}
		    	//}		
			   if(flag==1){
	 				newRow=ft.insertRow(len);//插入一行
	 				newRow.id="url"+len;
	 				newRow.ln=len;
	 				newRow.valign="top"
	
					c1=newRow.insertCell(0);//插入一列
		
					c1.align="left";
					c1.width="80%";
					c1.valign="top";
					c1.innerHTML="<input type='text' readonly='readonly' name='name"+len+"' id='name"+len+"' style='width:150'><input type='hidden' name='values"+len+"' id='values"+len+"'>";
		
					c2=newRow.insertCell(1);
					c2.align="left";
					c2.width="20%";
					c2.valign="top";
					c2.innerHTML="<select name='operator"+len+"' id='operator"+len+"' style='width:50'></select>";	
		
					c3=newRow.insertCell(2);
					c3.align="left";
					c3.width="20%";
					c3.valign="top";
					c3.innerHTML="<DIV id='texttd"+len+"'>&nbsp;</DIV>";	
					
					currentline=len;//把当前行值记录位新增的行值 
					c4=newRow.insertCell(3);
					c4.align="left";
					c4.width="20%";
					c4.valign="top";
					c4.innerHTML="<a href='#' onclick='deleteCondition();' id='ahref"+len+"' ln="+len+"/>删除</a>";	
					if(len==0){
						document.all.bds.value=len+1;
					}else if(len>0)
						document.all.bds.value=document.all.bds.value+"*"+(len+1);			
				}	
				var len=ft.rows.length;//得到表的总行数

 				document.all.length.value=len;
			}
		}
		
		function deleteCondition(){
	       var ft=document.getElementById("addTable");
	       var bds = document.getElementById("bds");
	       var bdsvalue = bds.value;
	       var  pre;
	       var  back;
	       
		   line=parseInt(event.srcElement.ln,10);
		   if (line>=0)
		     	for (var i=0; i<ft.rows.length ; i++){
		       		if (ft.rows[i].ln==line ){
		         		if (!confirm("确认删除?")) return;
		          		ft.deleteRow(i);
		          		if(i==0){
		          			pre="";
		          			back=bdsvalue.substring(2,bdsvalue.length);
		          		}else if(i==ft.rows.length){
		          			pre=bdsvalue.substring(0,bdsvalue.length-2);
		          			back="";
		          		}else{
				          	pre=bdsvalue.substring(0,i*2-1);
				          	back=bdsvalue.substring(i*2+1,bdsvalue.length);
				        }
		          		
	   				  	//将删除行后的数字-1替换 函数
	   				 	var j=1;
	   				  	if(i==0)
   				  			j=0;
   				  		for(j;j<back.length;j=j+2){
	   				  	  	var num=parseInt(back.substring(j,j+1))-1;
	   				  	   	back=back.substring(0,j)+num+back.substring(j + 1, back.length);
	   				  	   	var namevalue=document.getElementById("name"+num).value;
	   				  	   	var values=document.getElementById("values"+num).value;
	   				  	   	var optionvalue=document.getElementById("operator"+num).innerHTML;
	   				  	   	var texttdvalue=document.getElementById("texttd"+num).innerHTML.toString();
	   				  	   	document.getElementById("url"+num).children[0].innerHTML="<input type='text' readonly='readonly' name='name"+(num-1)+"' id='name"+(num-1)+"' value='"+namevalue+"'style='width:150'><input type='hidden' name='values"+(num-1)+"' id='values"+(num-1)+"' value='"+values+"'>";  	 	 
	   				  	   	document.getElementById("url"+num).children[1].innerHTML="<select name='operator"+(num-1)+"' id='operator"+(num-1)+"' style='width:50'>"+optionvalue+"</select>";  	   
	   				  	   	document.getElementById("url"+num).children[3].innerHTML="<a href='#' onclick='deleteCondition();' id='ahref"+(num-1)+"' ln="+(num-1)+"/>删除</a>";
	   				  	   	texttdvalue=texttdvalue.replace(new RegExp("infoItemValue"+(num+1),"gi"),"infoItemValue"+num);
	   				  	   	texttdvalue=texttdvalue.replace(new RegExp("infoItemValue"+(num+1),"gi"),"infoItemId"+num);
	   				  	   	document.getElementById("texttd"+num).id="texttd"+(num-1);
	   				  	   	document.getElementById("texttd"+(num-1)).innerHTML=texttdvalue;  	   
	   				  	   	document.getElementById("url"+num).ln=(num-1);
	   				  	   	document.getElementById("url"+num).id="url"+(num-1);
   				 		}
   				  		bds.value=pre+back;
   				  		var bdsv = bds.value;
   				  		currentline=bdsv.substring(bdsv.length-1,bdsv.length)-1;//把当前行值记录位新增的行值 
		          	}
		     	}
		    var ft=document.getElementById("addTable");
 			var len=ft.rows.length;//得到表的总行数
 			document.all.length.value=len;
		}
			     
  		function changeLine(){
  			var crrentTr = event.srcElement.parentElement;//获取当前点击的行
    		while (crrentTr.tagName != "TR") {
        		if (crrentTr == null || crrentTr == "undefined" || crrentTr.tagName == "BODY") {
           		 	return;
        		}
       			 crrentTr = crrentTr.parentElement;
   			 }
    		currentline = crrentTr.rowIndex; 
    	}
    	
    	function selectBaseData(objId,objName,dictValue){
		    var URLStr = contextPath+"/personnel/baseperson/person!infoItemTree.action?objId="+objId+"&objName="+objName+"&dictCode="+dictValue;
		    OpenWindow(URLStr,200,200,window);	
		}
		
		function checkBDS(value){
          	if ((event.keyCode>=48&&event.keyCode<=57)||event.keyCode==16||event.keyCode==187||event.keyCode==13||event.keyCode==8){ 
			}else{
				event.keyCode=0;
				alert("请输入1~9的数字，或者* + （ ）运算符");
				event.returnvalue=false;
			}	
		}
		
		function saveButton(){	
	        var ft=document.getElementById("addTable");
	        var module=document.getElementById("module").value;
	        var bds=document.getElementById("bds").value;
	        var item =document.all.item.value;
		    var len=ft.rows.length;//得到表的总行数
		    document.getElementById("length").value=len;
		    if(len==0){
		    	if(module=="setCon"){
		    		document.getElementById("myForm").action="<%=path%>/historyquery/query!setCon.action";
		    		document.getElementById("myForm").submit();
		    	}else if(module=="personQuery"){
		    		window.dialogArguments.document.getElementById("queryContent").value=document.getElementById("queryContent").value;
    				document.getElementById("myForm").action="<%=path%>/historyquery/query!multipleTableQuery.action";
    				document.getElementById("myForm").submit();
		    	}else if(module=="recordQuery"){
		    		document.getElementById("myForm").submit();
		    	}else{
		    		alert("请添加条件表达式")
		    	}
		    }else{
		     	for(var i=0;i<len;i++){
		     		var k=i+1;
		     		var infoItemValue="";
		     		if(document.getElementById("infoItemValue"+k)!=undefined){
			    		infoItemValue=document.getElementById("infoItemValue"+k).value;
			    	}else{
			    		alert("请设置查询条件！！");
			    		return;
			    	}
		    		//if(infoItemValue=null||infoItemValue==''){
		    		//	alert("查询条件无值！！");
		    		//	return;
		    		//}	
		    		if(module!="personQuery"){//人事信息查询模块可以设置多表查询
			    		var tempValue=document.getElementById("values"+0).value;
			    		tempValue=tempValue.substring(0,tempValue.indexOf("."))
			    		var values=document.getElementById("values"+i).value;
			     		if(values.substring(0,values.indexOf("."))!=tempValue){
			     			alert("只能设置单表查询条件！");
			     			return;
			     		}
		     		}
		    	}	
		    	
	    		//验证条件表达式
	    		var msg=isExpression(bds);
	    		if(msg=="no"){
	    			alert("条件表达式不正确！");
	    			return;
	    		}else{
	    			for(var i=0;i<bds.length;i++){
	    				var temp=bds.substring(i,i+1);
	    				if(temp!="*"&&temp!="+"&&temp!="("&&temp!=")"&&temp!=" "){
	    					if(temp*1>len*1){
	    						alert("条件超出最大值！");
	    						return;
	    					}else if(temp*1==0){
	    						alert("请输入大于0的数据！");
	    						return;
	    					}
	    				}
	    			}	
	    		}	   				
    			if(module=="setCon"){		//内部分发
    				setQueryDes();
    				document.getElementById("myForm").action="<%=path%>/historyquery/query!setCon.action";
    			}else if(module=="personQuery"){	//人事人员管理
    				window.dialogArguments.document.getElementById("queryContent").value=document.getElementById("queryContent").value;
    				document.getElementById("myForm").action="<%=path%>/historyquery/query!multipleTableQuery.action";
    			}
    			document.getElementById("myForm").submit();
		    }
		}
		
		//设置列
		function searchRow(){
			var url= '<%=path%>/historyquery/query!rowlist.action?infoSetValue='+document.getElementById("structValue").value;
  			OpenWindow(url,250,330,window);
		}
		
		</SCRIPT>
	</head>
	<body class=contentbodymargin oncontextmenu="return false;">
		<script language="javascript" type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
			<form
				action="<%=path%>/historyquery/query!complexQuery.action"
				method="post" id="myForm" target="targetForm">
				<input type="hidden" id="infoSetValue" name="infoSetValue"
					value="${infoSetValue}">
				<input type="hidden" id="flowId" name="flowId" value="${flowId}">
				<input type="hidden" id="module" name="module" value="${module}">
				<input type="hidden" id="groupFiled" name="groupFiled" value="${groupFiled}">
				<input type="hidden" id="length" name="length">
				<table width="100%" border="0" cellspacing="0" cellpadding="0"
					style="vertical-align: top;">
					<tr>
						<td height="100%">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td height="40"
										style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
										<table width="100%" border="0" cellspacing="0"
											cellpadding="00">
											<tr>
												<td>&nbsp;</td>
												<td width="50%">
													<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
														height="9">&nbsp;
													<s:if test="module!=null&&(module==\"setCon\"||module==\"recordQuery\"||module==\"personQuery\")">
														<s:select id="structValue" list="structureList" listKey="infoSetValue" listValue="infoSetName" onchange="getFields(this.value)">
														</s:select>
													</s:if>
													<s:else>
														【<s:property value="objName" />】复合查询
													</s:else>
												</td>
												<td width="35%" align="left">
													<s:if test="module==\"personQuery\"">
														查询内容：
														<s:select id="queryContent" name="queryContent"  list="#{'0':'一般查询','1':'休假情况统计','2':'满足年限任职统计'}" onselect="0">
														</s:select>
													</s:if>
												</td>
												<td width="10%" align="left">
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
							<table width="95%" height="10%" border="0" cellpadding="0"
								cellspacing="1" align="center" class="table1">
								<tr>
									<td class="td1"> 
										<select id="item" name="item" onclick="getOtherFields()" size="21"
											style="width:160">
											<s:iterator value="columnList">
												<option value="<s:property value="infoItemCode"/>">
													<s:property value="infoItemSeconddisplay" />
													
												</option>
											</s:iterator>
										</select>
									</td>
									<td width="78%" valign="top" class="td1">
										<s:if test="module!=null&&module==\"setCon\"">
											<DIV style="width:99%;height:200;overflow:scroll">
												<TABLE id="addTable" onclick="changeLine()" border="1"
													cellspacing="0" width="100" 
													cellpadding="2">
												</TABLE>
											</DIV>
											<DIV style="width:99%;height:105">
												<TABLE  border="1"
													cellspacing="0" width="105" 
													cellpadding="2">
													<tr valign="top">
														<td align="left">
															<textarea rows="6" cols="50" id="querydescript" name="querydescript">${querydescript}</textarea>
														</td>
													</tr>	
												</TABLE>
											</DIV>
										</s:if>
										<s:else>
											<DIV style="width:99%;height:300;overflow:scroll">
												<TABLE id="addTable" onclick="changeLine()" border="1"
													cellspacing="0" width="100" 
													cellpadding="2">
												</TABLE>
											</DIV>
										</s:else>
									</td>
								</tr>
								<tr>
									<td colspan="2" class="td1">
										<INPUT TYPE="BUTTON" NAME="" VALUE="选择添加" class="anniu"
											OnClick="addConditon()">
										条件表达式：
										<INPUT TYPE="Text" NAME="bds" id="bds" 
											style="border:1px solid #999999 " VALUE="" width="80%">&nbsp;&nbsp;<font color="gray">"+"表示或者，"*"表示并且。例如：(1+2)*3</font>
									</td>
								</tr>
								<s:if test="module!=null&&module==\"recordQuery\"">
									<tr>
										<td colspan="2" class="td1">
											分组：
											<select id="groupByFiled" name="groupByFiled">
												<option value=''>请选择分组信息项</option>
												<s:iterator value="columnList">
													<option value="<s:property value="infoItemField"/>">
														<s:property value="infoItemSeconddisplay" />
													</option>
												</s:iterator>
											</select>
										</td>
									</tr>
								</s:if>
							</TABLE>
						</td>
					</tr>
				</table>
				<br>
				<table width="95%" border="0" cellspacing="0" cellpadding="00">
					<tr>
						<td align="center" valign="middle">
							<table width="30%" border="0" cellspacing="0" cellpadding="00">
								<tr>
									<td width="15%" >
										<input type="button" class="anniu" value="确定"
											onclick="saveButton();" />
									</td>
									<td width="15%">
										<input type="button" class="anniu" value="取消"
											onclick="cancelRecomm();" />
									</td>
									<s:if test="module!=null&&module==\"recordQuery\"">
										<td width="15%">
											<input type="button" class="anniu" value="设置展现列"
												onclick="searchRow();" />
										</td>
									</s:if>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</form>
		</DIV>
	</body>
</html>
