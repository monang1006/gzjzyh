<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<%
	String nowYear = (String) request.getAttribute("nowYear");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>
				保存编号项
		</title>
		<%@include file="/common/include/meta.jsp"%>
		<link href="<%=frameroot%>/css/properties_windows_add.css" type=text/css
			rel=stylesheet />
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript" language="javascript"></script>
		<script type="text/javascript">
				
			var xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
			var oxs = xmlDoc.createElement("AutoCode");
			var year = <%=nowYear%>;
			function onSelectChange(id){
				var obj = document.getElementById(id);
				var tbid = "";
				for(var i = 0; i < obj.options.length; i ++){
					tbid = obj.options[i].value;
					// 未选择类型
					if (tbid == ""){
						continue;
					}
					// 选择类型对应的表格ID
					if (document.getElementById(tbid) == null){
						if(id=="VariantItem"){
							if(obj.options[i].selected){
								if(obj.options[i].value=='Data LongYear'||obj.options[i].value=='Data ShortYear'){
									document.getElementById("useSame").disabled=false;
								}else{
									document.getElementById("useSame").disabled=true;
									document.getElementById("useSame").checked=false;
								}
							}
						}
						continue;
					}
					//  操作选类型对应择表格地显示/隐藏
					if (obj.options[i].selected){
						if(id=="VariantItem"){
							if(obj.options[i].value=='Array'){
								document.getElementById("useSame").disabled=false;
							}
						}
						document.getElementById(tbid).style.display="block";
					}else{
						document.getElementById(tbid).style.display="none";
					}
				}
			}
			
			// 更新一条规则
			function onUpdateItem(){
				var id = "${id}";
				if (id == ""){
					return;
				}
      			$.ajax({
      					type:"POST",
	      				dataType:"text",
	      				url:"<%=path%>/autoencoder/rule!find.action",
	      				data:"id=" + id,
	      				success:function(msg){
	      					xmlDoc.loadXML(msg);
	      					oxs = xmlDoc.documentElement;
	      					// 刷新XML表
	      					refreshxmltable();
	      					// 设定更新方法
	      					document.getElementById("confirmbtn").onclick = onUpdateConfirmItem;
						}
      			});	
			}
			
			// 确认更新一条规则
			function onUpdateConfirmItem(){
				var id = "${id}";
				var code = document.getElementById("code").value;
				var xml = oxs.xml;
				if (id == ""){
					alert("目前进行的是新增操作，不能进行顺序保存。");
					return;
				}
      			$.ajax({
      					type:"POST",
	      				dataType:"text",
	      				url:"<%=path%>/autoencoder/rule!update.action",
	      				data:"id=" + id + "&type=" + xml + "&code=" + code,
	      				success:function(msg){
	      					if(msg=="updated"){
	      						// 刷新规则列表
 								window.dialogArguments.submitForm();
 								window.close();
	      					}else{
	      						alert("更新数据失败。");
	      					}	      					
						}
      			});	
			}
			
			// 确认添加一条规则
			function onCreateConfirmItem(){
				var xml = oxs.xml;
				var code = document.getElementById("code").value;
				
				if (code == ""){
					alert("请输入编号名称。");
					return;
				}else if (oxs.childNodes.length < 1){
					alert("请输入编号项目。");
					return;
				}
      			$.ajax({
      					type:"POST",
	      				dataType:"text",
	      				url:"<%=path%>/autoencoder/rule!save.action",
	      				data:"type=" + xml + "&code=" + code,
	      				success:function(msg){
	      					if(msg=="seved"){
	      						// 保存成功，刷新编码列表
 								window.dialogArguments.submitForm();
 								window.close();
	      					}else{
	      						alert("保存失败。");
	      					}	      						    				
						}
      			});
			}
			
			// 添加一个子项目
			function onCreateSubitem(){
				subitemVisible(true);
				document.getElementById("confirmSubitem").onclick = onCreateConfirmSubitem;
				document.forms[0].reset();
				// 禁止XML表
				toggleDisabled(document.getElementById("content"), true);
				
			}
			
			// 确认添加一个子项目
			function onCreateConfirmSubitem(){
				// 创建一个xml节点
				var type = document.getElementById("type").value;
				if(checkInfo(type)==false){
					return ;
				}
				createAutoCodeXml(type, null);
				// 刷新XML表
				refreshxmltable();
				// 关闭节点录入
				subitemVisible(false);
				// 激活XML表
				toggleDisabled(document.getElementById("content"), false);
			}
			
			// 删除一个子项目
			function onDeleteSubitem(){
				if(selObj == null){
					return;
				} 
				//删除XML节点
				oxs.removeChild(oxs.childNodes[selObj.rowIndex-1]);
				//刷新XML表
				refreshxmltable();
			}
			function showXml(){
				alert(oxs.xml);
			}
			// 编辑一个子项目
			function onUpdateSubitem(){
				if(selObj == null){
					return;
				} 
				//取得XML节点
				x = oxs.childNodes[selObj.rowIndex-1];
				// 类型
				var type = x.nodeName;
				// 固定
				if (type == "String"){
					document.getElementById("StringItem").value = x.getAttribute("Value");
				}else if (type == "Variant"){
					var vartype = x.getAttribute("Type");
					document.getElementById("VariantItem").value = vartype;
					var varDiffrent = x.getAttribute("NumDiffrent");
					if(varDiffrent=="true"){
						document.getElementById("useSame").checked=true;
					}else{
						document.getElementById("useSame").checked=false;
					}
					if (vartype == "Array"){
						var obj = document.getElementById("ArrayItem");
						// 清空固定变量
						obj.options.length = 0;
						for (var i =0; i < x.childNodes.length; i++){
						   	var optinNode = document.createElement("option")
						   	optinNode.id=x.childNodes[i].getAttribute("Id");
						   	optinNode.mstart=x.childNodes[i].getAttribute("mstart");
						   	optinNode.mstep=x.childNodes[i].getAttribute("mstep");
						   	optinNode.mend=x.childNodes[i].getAttribute("mend");
						   	optinNode.mNowValue=x.childNodes[i].getAttribute("mNowValue");
						   	optinNode.nowYear=x.childNodes[i].getAttribute("nowyear");
							optinNode.innerText = x.childNodes[i].getAttribute("Value"); 
							obj.appendChild(optinNode);  
						}
						//alert(obj.innerHTML);
					}
					if(vartype == "OrgName"){
						var otab = document.getElementById("orgItem");
						for (var i =0; i < x.childNodes.length; i++){
							var orgId = x.childNodes[i].getAttribute("orgId");
							var rname = x.childNodes[i].getAttribute("rname");
							var rstart = x.childNodes[i].getAttribute("rstart");
							var rstep = x.childNodes[i].getAttribute("rstep");
							var rend = x.childNodes[i].getAttribute("rend");
							var rnow = x.childNodes[i].getAttribute("rnow");
							var nowYear = x.childNodes[i].getAttribute("nowYear");
							for ( var j= 0; j < otab.rows.length; j++) {
								if(otab.rows[j].value==orgId){
									if(rname !=null || rname !==""){
										document.getElementById(orgId+"_rname").value=rname;
									}													
									document.getElementById(orgId+"_rstart").value=rstart;
									document.getElementById(orgId+"_rstep").value=rstep;
									document.getElementById(orgId+"_rend").value=rend;
									document.getElementById(orgId+"_rnow").value=rnow;
									document.getElementById(orgId+"_nowYear").value=nowYear;
							}
						}
					
					}
				}	
			}else if (type == "Sequence"){
				for(var i = 0; i < x.attributes.length; i++){
					if (x.attributes[i].name == "Fill"){
						document.getElementById(x.attributes[i].name).checked = x.attributes[i].value == '1'?true:false;
					}else{
						document.getElementById(x.attributes[i].name).value = x.attributes[i].value;
					}
				}
			}
			// 设置初始值
			subitemVisible(true);
			document.getElementById("confirmSubitem").onclick = onUpdateConfirmSubitem;
			document.getElementById("type").value = type;
			document.getElementById("type").fireEvent("onchange");
			document.getElementById("VariantItem").fireEvent("onchange");
			// 禁止XML表
			toggleDisabled(document.getElementById("content"), true);
		}
			
			//判断为小于12位的正整数
			function iszzs(number){
				var r =/^\d+$/;    // /^\d+$/   ///[1-9]d*|0$/;
				if(r.test(number)==false){
					return true;
				}else{
					//alert(number.length)
					if(number.length>12){
						return true;
					}else{
						return false;
					}
				}
			}
			//去掉左空格
			function ltrim(s){ 
				return s.replace(/(^\s*)/g, "");
 			} 
 			
 			//去右空格; 
			function rtrim(s){ 
  				return s.replace(/(\s*$)/g, "");
 			} 
			
			//去掉字符串的左右空格
			function trim(s){ 
    			//s.replace(/(^\s*)|(\s*$)/g, "");
 				return rtrim(ltrim(s)); 
 			}
			function checkInfo(type){
				if(type=="String"){
					var stringItem = document.getElementById("StringItem");
					if(trim(stringItem.value)==""){
						alert("固定字符不能为空，请填写。");
						stringItem.select(); 
						return false;
					}
				}else if(type=="Variant"){
					
				}else if(type=="Sequence"){
					var start = document.getElementById("Start");
					var end = document.getElementById("End");
					var step = document.getElementById("Step");
					var nowvalue = document.getElementById("nowValue");
					if(iszzs(start.value)){
						alert("开始字段不能为空且只能为小于12位的非负整数，请填写开始数值。");
						start.select();
						return false;
					}else if(iszzs(end.value)){
						alert("结束字段不能为空且只能为小于12位的非负整数，请填写结束数值。");
						end.select();
						return false;
					}else if(iszzs(step.value)){
						alert("步长字段不能为空且只能为小于12位的非负整数，请填写步长数值。");
						step.select();
						return false;
					}else if(iszzs(nowvalue.value)){
						alert("当前值字段不能为空且只能为小于12位的非负整数，请填写当前值数值。");
						nowvalue.select();
						return false;
					}
					if(Number(start.value)>Number(end.value)){
						alert("填写的开始值大于结束值,不符合编号生成规则。");
						start.select();
						return false;
					}
					if(Number(nowvalue.value)>Number(end.value)){
						alert("设置的编号当前值已经大于了结束值,不符合编号生成规则。");
						nowvalue.select();
						return false;
					}
					if(Number(start.valuee) + Number(step.value)>Number(end.value)){
						alert("设置的编号开始值加步长值已经大于了结束值,不能生成下一个编号。");
						nowvalue.select();
						return false;
					}
					if(Number(nowvalue.value) + Number(step.value)> Number(end.value)){
						alert("设置的编号当前值加步长值已经大于了结束值,不能生成下一个编号。");
						nowvalue.select();
						return false;
					}
				}
			}
			// 编辑确认一个子项目
			function onUpdateConfirmSubitem(){
				if(selObj == null){
					return;
				}
				// 更新一个xml节点
				var type = document.getElementById("type").value;
				if(checkInfo(type)==false){
					return ;
				}
				var replaceIndex = selObj.rowIndex-1;
				createAutoCodeXml(type, replaceIndex);
				// 刷新XML表
				refreshxmltable();
				// 关闭节点录入
				subitemVisible(false);
				// 激活XML表
				toggleDisabled(document.getElementById("content"), false);
			}
			
			// 取消操作一个子项目
			function onCancelSubitem(){
				// 关闭节点录入
				subitemVisible(false);
				// 激活XML表
				toggleDisabled(document.getElementById("content"), false);
			}
			
			function subitemVisible(displayflag){
				// 是否创建一个标识项目
				document.getElementById("subitem").style.display = displayflag ? "block" : "none";
				// 复位子表示项目
				document.getElementById("type").selectedIndex = 0;
				document.getElementById("type").fireEvent("onchange");
				
				if (displayflag == false){
					document.getElementById("VariantItem").selectedIndex = 0;
					document.getElementById("VariantItem").fireEvent("onchange");
					var obj = document.getElementById("ArrayItem");
					// 清空固定变量
					obj.options.length = 0;
				}
			}
			
			function createAutoCodeXml(type, updateIndex){
				if (type == ""){
					return;
				}

				var oemt = xmlDoc.createElement(type);
				// 固定
				if (type == "String"){
					oemt.setAttribute("Value", document.getElementById("StringItem").value);
				}else if (type == "Variant"){
				// 变量
					var variantItem = document.getElementById("VariantItem").value;
					if (variantItem == "Array"){
						var obj = document.getElementById("ArrayItem");
						for(var i = 0; i < obj.options.length; i ++){
							var ov = xmlDoc.createElement("ArrayItem");
						/**	if(obj.options[i].id==""){							//当ID为空时证明为新添加的内容
								ov.setAttribute("Id",i+1);
								ov.setAttribute("mstart",document.getElementById("mstartinput").value);
								ov.setAttribute("mstep",document.getElementById("mstepinput").value);
								ov.setAttribute("mend",document.getElementById("mendinput").value);
								ov.setAttribute("mNowValue",document.getElementById("mNowValue").value);
							}else{
								ov.setAttribute("Id",obj.options[i].id);		//ID存在的证明为原有的内容
								if(obj.options[i].id==document.getElementById("mid").value){
									ov.setAttribute("mstart",document.getElementById("mstartinput").value);
									ov.setAttribute("mstep",document.getElementById("mstepinput").value);
									ov.setAttribute("mend",document.getElementById("mendinput").value);
									ov.setAttribute("mNowValue",document.getElementById("mNowValue").value);
								}else{
									var nowNode = getNowNodeInfo(obj.options[i].id);*/
									ov.setAttribute("Id",i);
									ov.setAttribute("mstart",obj.options[i].mstart);
									ov.setAttribute("mstep",obj.options[i].mstep);
									ov.setAttribute("mend",obj.options[i].mend);
									ov.setAttribute("mNowValue",obj.options[i].mNowValue);
									ov.setAttribute("nowyear",obj.options[i].nowYear);
					//			}
					//		}
							ov.setAttribute("Value", obj.options[i].text);
							oemt.appendChild(ov);
						}
					}
					if(variantItem == "OrgName"){
						//获取部门所在table的对象
						var otab = document.getElementById("orgItem");
						//alert(otab.rows.length);
						for ( var i= 0; i < otab.rows.length; i++) {
							var orgId= otab.rows[i].value;	
							var rname=document.getElementById(orgId+"_rname").value;												
							var rstart=document.getElementById(orgId+"_rstart").value;
							var rstep=document.getElementById(orgId+"_rstep").value;
							var rend=document.getElementById(orgId+"_rend").value;
							var rnow=document.getElementById(orgId+"_rnow").value;
				    		var nowYear=document.getElementById(orgId+"_nowYear").value;
				    		
				    		if(rstart == null || rstart ==""){
								rstart=1;
							}							
							if(rstep == null || rstep ==""){
								rstep=1;
							}
							if(rend == null || rend ==""){
								rend=999999;
							}
							if(rnow == null || rnow ==""){
								rnow=0;
							}
							if(nowYear == null || nowYear ==""){
								nowYear=<%=nowYear %>;
								//alert(nowYear);
							}	
													
				    		//创建一个orgItem节点
							var ov = xmlDoc.createElement("orgItem");
							ov.setAttribute("orgId",orgId);
							ov.setAttribute("rname",rname);
				    		ov.setAttribute("rstart",rstart);
				    		ov.setAttribute("rstep",rstep);
				    		ov.setAttribute("rend",rend);
				    		ov.setAttribute("rnow",rnow);
				    		ov.setAttribute("nowYear",nowYear);	
				    		oemt.appendChild(ov);			    		
						}										
					}
					
					oemt.setAttribute("Type",variantItem);
					if(document.getElementById("useSame").checked==true){
						oemt.setAttribute("NumDiffrent","true");
					}else{
						oemt.setAttribute("NumDiffrent","false");
					}
			
				}else if (type == "Sequence"){
				// 序列
					oemt.setAttribute("Start",document.getElementById("Start").value);
					oemt.setAttribute("End",document.getElementById("End").value);
					oemt.setAttribute("Step",document.getElementById("Step").value);
					oemt.setAttribute("Display",document.getElementById("Display").value);
					oemt.setAttribute("Fill",document.getElementById("Fill").checked ? '1':'0');
					oemt.setAttribute("FillChar",document.getElementById("FillChar").value);
					oemt.setAttribute("NowValue",document.getElementById("nowvalue").value);
					oemt.setAttribute("year",document.getElementById("year").value);
				}
				
				// 创建或者更新
				if (updateIndex == null){
					oxs.appendChild(oemt);
				}else{
					oxs.replaceChild(oemt, oxs.childNodes[updateIndex]);
				}
				var strXml = oxs.xml;
			}
			
			function getNowNodeInfo(id){
				var arr = xmlDoc.selectSingleNode("/AutoCode/Variant");
				for(var i=0;i<arr.childNodes.length;i++){
					if(arr.childNodes[i].getAttribute("Id")==id){
						return arr.childNodes[i];
					}
				}
				return null;
			}
			
			function displaytbAdd(col1, col2){
			
				//先获取该表格的引用:
				var Container = document.getElementById("displaytb");
				//然后创建行(TR对象)
				var NewTr = document.createElement("<tr class='td1'>");
				
				//填充该表格行
				var NewTd1 = document.createElement("<td align='center'>");
				var NewTd2 = document.createElement("<td>");
			
				NewTd1.innerHTML = col1;
				NewTd2.innerHTML = col2;
				
				NewTr.onclick = function (){selDisplaytb(this)};
				NewTr.appendChild(NewTd1);
				NewTr.appendChild(NewTd2);
				
				//在DOM树中指定NewTr的父节点
				var LastTr = Container.rows[Container.rows.length - 1];
				LastTr.parentNode.appendChild(NewTr); 
			}
			
			function moveUp(){
				var table = document.getElementById("displaytb");
				var row = "";
				var nowid="";
				for(var i=0;i<table.rows.length;i++){
					if(table.rows[i].style.backgroundColor=='#a9b2ca'||table.rows[i].style.backgroundColor=='#A9B2CA'){
						row = table.rows[i];
						nowid = i;
						break;
					}
				}
				if(row==""){
					alert("请选择好对应的行再进行移动。");
					return;
				}
				if(row.previousSibling.id=='head'){
					alert("选择的数据是第一行，已经不能再向上移动了。");
				}else{
					swapNode(row,row.previousSibling,nowid);
				}
			}
			
			function moveDown(){
				var table = document.getElementById("displaytb");
				var row = "";
				var nowid="";
				for(var i=0;i<table.rows.length;i++){
					if(table.rows[i].style.backgroundColor=='#a9b2ca'||table.rows[i].style.backgroundColor=='#A9B2CA'){
						row = table.rows[i];
						nowid = i;
						break;
					}
				}
				if(row==""){
					alert('请选择好对应的行再进行移动。');
					return;
				}
				if(row.nextSibling){
					swapNodeDown(row,row.nextSibling,nowid);
				}else{
					alert("当前是最后一行。");
				}
			}
			
			function copyMyObj(node,copyedNode){
				var type=node.nodeName;
				if (type == "String"){
					node.setAttribute("Value", copyedNode.getAttribute("Value"));
				}else if (type == "Variant"){
					var variantItem = copyedNode.getAttribute("Type");
					if(variantItem=="Array"){
						//alert(copyedNode.childNodes.length);
						for(var i=0;i<copyedNode.childNodes.length;i++){
							var ov = xmlDoc.createElement("ArrayItem"); 
							ov.setAttribute("Value",copyedNode.childNodes[i].getAttribute("Value"));
							ov.setAttribute("Id",copyedNode.childNodes[i].getAttribute("Id"));
							ov.setAttribute("mstart",copyedNode.childNodes[i].getAttribute("mstart"));
							ov.setAttribute("mstep",copyedNode.childNodes[i].getAttribute("mstep"));
							ov.setAttribute("mend",copyedNode.childNodes[i].getAttribute("mend"));
							ov.setAttribute("mNowValue",copyedNode.childNodes[i].getAttribute("mNowValue"));
							ov.setAttribute("nowyear",copyedNode.childNodes[i].getAttribute("nowyear"));
							node.appendChild(ov);
						}
					}
					node.setAttribute("Type",variantItem);
					node.setAttribute("NumDiffrent",copyedNode.getAttribute("NumDiffrent"));
				}else if (type == "Sequence"){
					node.setAttribute("Start",copyedNode.getAttribute("Start"));
					node.setAttribute("End",copyedNode.getAttribute("End"));
					node.setAttribute("Step",copyedNode.getAttribute("Step"));
					node.setAttribute("Display",copyedNode.getAttribute("Display"));
					node.setAttribute("Fill",copyedNode.getAttribute("Fill"));
					node.setAttribute("FillChar",copyedNode.getAttribute("FillChar"));
					node.setAttribute("NowValue",copyedNode.getAttribute("NowValue"));
					node.setAttribute("year",copyedNode.getAttribute("year"));
				}
				return node;
			}
			
			function swapNodeDown(node1,node2,nowid){
				var infoPos = nowid-1;
				var nowNode = oxs.childNodes[infoPos];
				var nextNode = oxs.childNodes[infoPos+1];
				var nowNodeRe = xmlDoc.createElement(nowNode.nodeName);
				var nextNodeRe = xmlDoc.createElement(nextNode.nodeName);
				nowNodeRe=copyMyObj(nowNodeRe,nowNode);
				nextNodeRe=copyMyObj(nextNodeRe,nextNode);
				oxs.appendChild(nowNodeRe);
				oxs.appendChild(nextNodeRe);
				oxs.replaceChild(nowNodeRe, oxs.childNodes[infoPos+1]);
				oxs.replaceChild(nextNodeRe, oxs.childNodes[infoPos]);
				//获取父结点 
				var _parent=node1.parentNode; 
				//获取两个结点的相对位置 
				var _t1=node1.nextSibling; 
				var _t2=node2.nextSibling; 
				//将node2插入到原来node1的位置 
				if(_t1)
					_parent.insertBefore(node2,_t1); 
				else 
					_parent.appendChild(node2); 
				//将node1插入到原来node2的位置 
				if(_t2)
					_parent.insertBefore(node1,_t2); 
				else 
					_parent.appendChild(node1);  
			}
			
			function swapNode(node1,node2,nowid){ 
				var infoPos = nowid-1;
				var nowNode = oxs.childNodes[infoPos];
				var preNode = oxs.childNodes[infoPos-1];
				var nowNodeRe = xmlDoc.createElement(nowNode.nodeName);
				var preNodeRe = xmlDoc.createElement(preNode.nodeName);
				nowNodeRe=copyMyObj(nowNodeRe,nowNode);
				preNodeRe=copyMyObj(preNodeRe,preNode);
				oxs.appendChild(nowNodeRe);
				oxs.appendChild(preNodeRe);
				oxs.replaceChild(nowNodeRe, oxs.childNodes[infoPos-1]);
				oxs.replaceChild(preNodeRe, oxs.childNodes[infoPos]);
				//获取父结点 
				var _parent=node1.parentNode; 
				//获取两个结点的相对位置 
				var _t1=node1.nextSibling; 
				var _t2=node2.nextSibling; 
				//将node2插入到原来node1的位置 
				if(_t1)
					_parent.insertBefore(node2,_t1); 
				else 
					_parent.appendChild(node2); 
				//将node1插入到原来node2的位置 
				if(_t2)
					_parent.insertBefore(node1,_t2); 
				else 
					_parent.appendChild(node1);
			} 
			
			function clearDisplaytb(){
				var Container = document.getElementById("displaytb");
				while (Container.rows.length > 1){
					Container.deleteRow(1);
				}
			}
			
			// 解析XML
			function loadAutoCodeXml(){
				var x = oxs;
				var nodeName = "";
				var col1 = "";
				var col2 = "";
				var arrtkey = "";
				var arrtvar = "";
				for (var i = 0; i < x.childNodes.length; i++){
					nodeName = x.childNodes[i].nodeName;
					col1 = getTextByValue("type", nodeName);
					col2 = "";
					for ( var j = 0; j < x.childNodes[i].attributes.length ;j++){
						arrtkey = x.childNodes[i].attributes[j].name;
						arrtvar = x.childNodes[i].attributes[j].value;
						
						if (nodeName == "Variant"){
							if(arrtkey=="Type"){
								col2 =  col2 + getTextByValue("VariantItem", arrtvar) + " ";
							}else if(arrtkey="NumDiffrent"){
								if(arrtvar=="true"){
									document.getElementById("useSame").checked=true;
								}else{
									document.getElementById("useSame").checked=false;
								}
							}
						}else if (nodeName == "Sequence"){
							if (arrtkey == "Start"){
								col2 = "开始：" + arrtvar + "　" + col2;
							}else if (arrtkey == "End"){
								col2 = "结束：" + arrtvar + "　" + col2;
							}else if (arrtkey == "Step"){
								col2 = "步长：" + arrtvar + "　" + col2;
							}
						}else{
							col2 = col2 + arrtvar + " ";
						}
					}
					displaytbAdd(col1, col2);
				}
			}
			
			var selObj = null;
			function selDisplaytb(obj)
			{
			  if(selObj == null){
			  	selObj = obj;
			  } 
			  selObj.style.backgroundColor = "white";
			  obj.style.backgroundColor = "#A9B2CA";
			  selObj = obj;
			}
			
			// 刷新XML表
			function refreshxmltable(){
				selObj = null;
				clearDisplaytb();
				loadAutoCodeXml();
			
			}
			
			function KeyPress(obj){
				var len=obj.value.length;
				if (len>=100){
					event.keyCode=0;
				}
			}
			
			function getTextByValue(tagName, value){
			 var obj = document.getElementById(tagName);
			 var text = value;
				for(var i = 0; i < obj.options.length; i ++){
					vartmp = obj.options[i].value;
					if (value == vartmp){
						text = obj.options[i].text;
					}	
				}
				return text;
			}
			
			function toggleDisabled(el, state) {
			    try {
			        el.disabled = state;
			    }
			    catch(e){}
			    if (el.childNodes && el.childNodes.length > 0) {
			        for (var x = 0; x < el.childNodes.length; x++) {
			            toggleDisabled(el.childNodes[x], state);
			        }
			    }
			}
			
			function onArrayItemCreate(){
					arrayItemOper(onArrayItemCreateConfirm, "添加变量：", "block", true);
					document.getElementById("mid").value="";													//记录当前进行修改的ID
				    document.getElementById("mstartinput").value="";
				    document.getElementById("mstepinput").value="";
				    document.getElementById("mendinput").value="";
				    document.getElementById("mNowValue").value="";
				    document.getElementById("nowyear").value=year;
			}
			function onArrayItemUpdate(){
				obj = document.getElementById("ArrayItem");
				if (obj.selectedIndex >= 0){
					arrayItemOper(onArrayItemUpdateConfirm, "更新变量：", "block", true);
					document.getElementById("ArrayOperInput").value = obj.options[obj.selectedIndex].text;
					//alert(obj.options[obj.selectedIndex].id);
					document.getElementById("mid").value=obj.options[obj.selectedIndex].id;						//记录当前进行修改的ID
				    document.getElementById("mstartinput").value=obj.options[obj.selectedIndex].mstart;
				    document.getElementById("mstepinput").value=obj.options[obj.selectedIndex].mstep;
				    document.getElementById("mendinput").value=obj.options[obj.selectedIndex].mend;
				    document.getElementById("mNowValue").value=obj.options[obj.selectedIndex].mNowValue;
				    document.getElementById("nowyear").value=obj.options[obj.selectedIndex].nowYear;
				}else{
					alert("请选择要更新的变量。");
				}
			}
			function onArrayItemDelete(){
				obj = document.getElementById("ArrayItem");
				var index = obj.selectedIndex;
				//alert(index);
				if (index >= 0){
					obj.options.remove(index);
					index = index -1;
					if ( index >= 0){
						obj.selectedIndex = index;
					}
				}else{
					alert("请选择要删除的变量。");
				}
			}
			
			function checkNewInfo(){
				var title = document.getElementById("ArrayOperInput");
				var start=document.getElementById("mstartinput");
				var step = document.getElementById("mstepinput");
				var end = document.getElementById("mendinput");
				var nowvalue=document.getElementById("mNowValue");
				if(trim(title.value)==""){
					alert("变量的名称不能为空。");
					title.select();
					return false;
				}else if(iszzs(start.value)){
					alert("开始字段不能为空且只能为小于12位的非负整数，请填写开始数值。");
					start.select();
					return false;
				}else if(iszzs(end.value)){
					alert("开始字段不能为空且只能为小于12位的非负整数，请填写开始数值。");
					end.select();
					return false;
				}else if(iszzs(step.value)){
					alert("开始字段不能为空且只能为小于12位的非负整数，请填写开始数值。");
					step.select();
					return false;
				}else if(iszzs(nowvalue.value)){
					alert("开始字段不能为空且只能为小于12位的非负整数，请填写开始数值。");
					nowvalue.select();
					return false;
				}
				if(Number(start.value)>Number(end.value)){
					alert("开始字段不能为空且只能为小于12位的非负整数，请填写开始数值。");
					start.select();
					return false;
				}
				if(Number(nowvalue.value)>Number(end.value)){
					alert("设置的编号当前值已经大于了结束值,不符合编号生成规则。");
					nowvalue.select();
					return false;
				}
				
				
			}
			
			function onArrayItemCreateConfirm(){
				var title = document.getElementById("ArrayOperInput").value;
				var nowLength = document.getElementById("ArrayItem").options.length;
				var mid = nowLength+1;
				var mstart=document.getElementById("mstartinput").value;
				var mstep = document.getElementById("mstepinput").value;
				var mend = document.getElementById("mendinput").value;
				var mNowValue=document.getElementById("mNowValue").value;
				var nowYear = document.getElementById("nowyear").value;
				
				if (title == ""){
					alert("请输入变量名称。");
					return false;
				}
				
				if(iszzs(mstart)){
						alert("开始字段不能为空且只能为小于12位的非负整数，请填写开始数值。");
						return false;
					}else if(iszzs(mend)){
						alert("结束字段不能为空且只能为小于12位的非负整数，请填写结束数值。");
						return false;
					}else if(iszzs(mstep)){
						alert("步长字段不能为空且只能为小于12位的非负整数，请填写步长数值。");
						return false;
					}else if(iszzs(mNowValue)){
						alert("当前值字段不能为空且只能为小于12位的非负整数，请填写当前值数值。");
						return false;
					}
					if(Number(mstart)>Number(mend)){
						alert("填写的开始值大于结束值,不符合编号生成规则。");
						return false;
					}
					if(Number(mNowValue)>Number(mend)){
						alert("设置的编号当前值已经大于了结束值,不符合编号生成规则。");
						return false;
					}
					if(Number(mstart) + Number(mstep)>= Number(mend)){
						alert("设置的编号开始值加步长值已经大于了结束值,不能生成下一个编号。");
						return false;
					}
					if(Number(mNowValue) + Number(mstep)>= Number(mend)){
						alert("设置的编号当前值加步长值已经大于了结束值,不能生成下一个编号。");
						return false;
					}
					var varItem = new Option(title,title);
					varItem.id=mid;
					varItem.mstart=mstart;
					varItem.mstep=mstep;
					varItem.mend=mend;
					varItem.mNowValue=mNowValue;
					varItem.nowYear = nowYear;
				 	document.getElementById("ArrayItem").options.add(varItem);
					arrayItemOper(null, "", "none", false);

			}
			
			function onArrayItemUpdateConfirm(){
				var obj = document.getElementById("ArrayItem");
				var title = document.getElementById("ArrayOperInput").value;
				var mstart=document.getElementById("mstartinput").value;
				var mstep = document.getElementById("mstepinput").value;
				var mend = document.getElementById("mendinput").value;
				var mNowValue=document.getElementById("mNowValue").value;
				var nowYear=document.getElementById("nowYear").value;
				if(checkNewInfo()==false){
					return;
				}
				
				obj.options[obj.selectedIndex].value = title;
				obj.options[obj.selectedIndex].text = title;
				obj.options[obj.selectedIndex].mstart= mstart;
				obj.options[obj.selectedIndex].mstep= mstep;
				obj.options[obj.selectedIndex].mend= mend;
				obj.options[obj.selectedIndex].mNowValue= mNowValue;
				obj.options[obj.selectedIndex].nowYear=nowYear;
				arrayItemOper(null, "", "none", false);
			}
			function onCancelArrayOperInput(){
				arrayItemOper(null, "", "none", false);
			}
			
			function arrayItemOper(fun, title, operStyle, arrayContentState){
				document.getElementById("ArrayOperBtn").onclick = fun;
				document.getElementById("ArrayOperTitle").innerHTML = title;
				document.getElementById("ArrayOper").style.display = operStyle;
				document.getElementById("ArrayOperInput").value = "";
				// 禁止/激活数组列表
				toggleDisabled(document.getElementById("ArrayContent"), arrayContentState);
			}
			
			function onInit(){
				<s:if test="id==null">
				// 设定添加方法
				document.getElementById("confirmbtn").onclick = onCreateConfirmItem;
				</s:if>
				<s:else>
				// 设定更新方法
				document.getElementById("confirmbtn").onclick = onUpdateConfirmItem;
				onUpdateItem();
				</s:else>
			}
			
			var lastTr = null;
			function setColor(obj){
				color = obj.style.color
				obj.style.color = "#0033CC";
				if(lastTr!=null)
	   			lastTr.style.color = color;								
				lastTr = obj;
			}
			
			function setNum(id){
					var rstart=document.getElementById(id+"_rstart").value;
					var rstep=document.getElementById(id+"_rstep").value;
					var rend=document.getElementById(id+"_rend").value;
					var rnow=document.getElementById(id+"_rnow").value;
					var nowYear=document.getElementById(id+"_nowYear").value;
					var num = rstart+","+rstep+","+rend+","+rnow+","+nowYear;
									
				//alert(id); 
				var json="";
				json=window.showModalDialog("<%=path%>/autoencoder/rule!setNum.action?orgId="+id+"&ruleNum="+num,window, 
					'help:no;status:no;scroll:no;dialogWidth:500px; dialogHeight:300px');
				if(json!=null){
				   	document.getElementById(id+"_rstart").value=json.rstart;	
				    document.getElementById(id+"_rstep").value=json.rstep;
				    document.getElementById(id+"_rend").value=json.rend;
				    document.getElementById(id+"_rnow").value=json.rnow;
				    document.getElementById(id+"_nowYear").value=json.nowYear;
				}							
			}
			
	</script>
	</head>
	<body class=contentbodymargin oncontextmenu="return false;"
		onload="onInit();">
		<div id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td height="8px;"></td>
						</tr>
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
								<strong>保存编号项
								</strong>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
					                <tr>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onclick="$('#confirmbtn').click();">&nbsp;保&nbsp;存&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
					                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
					                 	<td class="Operation_input1" onclick="window.close();">&nbsp;取&nbsp;消&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
				                  		<td width="6"></td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td height="8px;"></td>
							</tr>
						</table>
						
						<table width="100%" border="0" cellpadding="0" cellspacing="1"
							align="center" class="table1">
							<tr>
								<td width="25%" height="21" class="biao_bg1" align="right">
									<span class="wz"><font color='red'>*</font>&nbsp;编号名称：&nbsp;</span>
								</td>
								<td class="td1" align="left">
									<input id="code" type="text" maxlength="100"
										onkeypress="KeyPress(this)" value="${code}"
										style="width: 380px" />
									&nbsp;&nbsp;&nbsp;&nbsp;
									<input type="hidden" id="confirmbtn" class="input_bg"
										value="确认" />
								</td>
							</tr>
						</table>
						<br />
						<form>
							<div id="content">
								<table style="vertical-align: top;" align="left" class="table1"
									cellpadding=0 cellspacing=1 width="100%" id="displaytb">
									<tr id="head">
										<th class="biao_bg2" width="25%" scope="col" >
											类别
										</th>
										<th class="biao_bg2" scope="col">
											设置
										</th>
									</tr>
									<tr>
										<td class="td1">&nbsp;
											
										</td>
										<td class="td1">&nbsp;
											
										</td>
									</tr>
								</table>
								<table width="100%" border="0" cellpadding="0" cellspacing="1"
									align="center">
									<tr>
										<td align="center">
											<a id="addPerson"  href="#" class="button" onclick="showXml();">查看XML</a>&nbsp;
											<a id="addPerson"  href="#" class="button" onclick="moveUp();">向上移动</a>&nbsp;
											<a id="addPerson"  href="#" class="button" onclick="moveDown();">向下移动</a>&nbsp;
											<a id="addPerson"  href="#" class="button" onclick="onCreateSubitem();">增加</a>&nbsp;
											<a id="addPerson"  href="#" class="button" onclick="onUpdateSubitem();">修改</a>&nbsp;
											<a id="addPerson"  href="#" class="button" onclick="onDeleteSubitem();">删除</a>&nbsp;
										</td>
									</tr>
								</table>
								
							</div>
							<br />
							<table width="100%" border="0" cellpadding="0" cellspacing="1"
								align="center" class="table1" id="subitem"
								style="display: none;">
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz">类型：&nbsp;</span>
									</td>
									<td class="td1" align="left" >
										<select id="type" style="width: 380px;"
											onchange="onSelectChange(this.id);">
											<option>
												请选择
											</option>
											<option value="String">
												固定字符
											</option>
											<option value="Variant">
												变&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;量
											</option>
											<option value="Sequence">
												自动序号
											</option>
										</select>
										&nbsp;&nbsp;
										<a id="addPerson"  href="#" class="button" onclick="$('#confirmSubitem').click();">确认</a>&nbsp;
										<a id="addPerson"  href="#" class="button" onclick="onCancelSubitem();">取消</a>&nbsp;
										&nbsp;&nbsp;&nbsp;&nbsp;
										<input type="hidden" id="confirmSubitem" class="input_bg"
											value="确认" />
									</td>
								</tr>
							</table>
							<table width="100%" border="0" cellpadding="0" cellspacing="1"
								align="center" class="table1" id="String" style="display: none;">
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz">字符：&nbsp;</span>
									</td>
									<td class="td1" align="left">
										<input type="text" id="StringItem" />
									</td>
								</tr>
							</table>
							<table width="100%" border="0" cellpadding="0" cellspacing="1"
								align="center" class="table1" id="Variant"
								style="display: none;">
								
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz">变量：&nbsp;</span>
									</td>
									<td class="td1" align="left">
										<select id="VariantItem" onchange="onSelectChange(this.id);">
											<option value="Data LongYear">
												当前时间(年-长格式)
											</option>
											<option value="Data ShortYear">
												当前时间(年-短格式)
											</option>
											<option value="Data Month">
												当前时间(月)
											</option>
											<option value="Data Day">
												当前时间(日)
											</option>

											<option value="OrgName">
												部门文号
											</option>

											<option value="Array">
												固定格式
											</option>
										</select>
										<input id="useSame" type="checkbox" />
										不同变量采用不同序号
									</td>
								</tr>

								<!--  部门文号生成规则，action中取得所有部门	-->
								<tr id="OrgName" style="display: none;">
									<td width="25%" height="21" class="biao_bg1" align="right" valign="top">
										<span class="wz">部门简称：&nbsp;</span>
									</td>
									<td class="td1">
										<div style="overflow: scroll; width: 540px; height: 320px;">
											<table width="100%" id="orgItem">
												<!-- 遍历部门list,，取得所有部门 -->
												<s:iterator value="orgList" status="statu" id="org">
													<!--  点击行变颜色，双击行弹出设置该部门文号生成规则,传参数部门ID-->
													<tr value="${org.orgId}" onclick="setColor(this)"
														ondblclick=setNum("<s:property value='orgId' />")>
														<input type="hidden" id="${org.orgId}_rstart" name="rstart" value="">
														<input type="hidden" id="${org.orgId}_rstep" name="rstep" value="">
														<input type="hidden" id="${org.orgId}_rend" name="rend" value="">
														<input type="hidden" id="${org.orgId}_rnow" name="rnow" value="">
														<input type="hidden" id="${org.orgId}_nowYear" name="nowYear" value="">
														<td align="left">
															<s:if test="#org.isOrg==1">
																机构:
															</s:if>
															<s:else>
																部门:
															</s:else>
															<s:property value="orgName" />
														</td>
														<td align="right">
															简称:
															<input type=text id="${org.orgId}_rname"
																value="${org.orgName}" size="20" maxlength="10" />
														</td>
													</tr>
												</s:iterator>
											</table>
										</div>
									</td>
								</tr>

								<tr id="Array" style="display: none;">
									<td width="25%" height="21" class="biao_bg1" align="right" valign="top">
										<span class="wz">变量选项：&nbsp;</span>
									</td>
									
									<td class="td1" align="left">
										<table border="0">
											<tr id="ArrayContent">
												<td>
													<select id="ArrayItem" size="5" style="width: 200px">
													</select>
												</td>
												<td>
                                                <table>
                                                <tr>
                                                 <td width="10"></td>
                                                  <td>
													<a  href="#" class="button" onclick="onArrayItemCreate();">添加</a>&nbsp;
													
                                                   </td> 
                                                </tr> 
                                                 <tr>
                                                 <td width="10"></td>
                                                  <td>
												
													<a  href="#" class="button" onclick="onArrayItemUpdate();">修改</a>&nbsp;
													
                                                   </td> 
                                                </tr>    
                                                 <tr>
                                                 <td width="10"></td>
                                                  <td>
													
													<a  href="#" class="button" onclick="onArrayItemDelete();">删除</a>&nbsp;
                                                   </td> 
                                                </tr>       
												</table>	
												</td>
												<td>&nbsp;
													
												</td>
											</tr>
                                            
                                        </table>   
                                        
									</td>
								</tr>
								</table>
								<table border="0" cellpadding="0" cellspacing="0" 
								 width="100%">
                                <tr>
                                <td colspan="2" align="center" >
                                <table border="0" cellpadding="0" cellspacing="0" id="ArrayOper" style="display: none;width:90%" > 
											<tr>
												<td>
												<table border="0" cellpadding="0" cellspacing="0" 
								align="" width="100%">
												<tr>
												<td width="20%">
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													<input id="mid" type="hidden" />
													
													<span id="ArrayOperTitle"></span>
													</td>
													<td></td><td></td><td></td>
												</tr>
												<tr>
													
													<td width="20%" align="right">
													   <span id="">变量名称：</span>
													   </td>
													<td width="20%">
														<input id="ArrayOperInput" type="text" style="width: 200px" />
														</td>
													<td align="right" >
														<span id="mstart">开始：</span>
														</td>
													<td width="20%">
														<input id="mstartinput" type="text" style="width: 200px" />
														</td>
												
											    </tr>
												<tr>
												
												<td width="20%" align="right">
													<span id="mstep">步长：</span>
												</td>
												<td width="20%" align="right">	
													<input id="mstepinput" type="text" style="width: 200px" />
												</td>
												<td align="right">
													<span id="mend">结束：</span>
													</td>
												<td>
													<input id="mendinput" type="text" style="width: 200px" />
													</td>
												
												</tr>
												<tr>	
											
												<td width="20%" align="right">
													<span id="mnow">当前值：</span>
												</td>
												<td>	
													<input id="mNowValue" type="text" style="width: 200px" />
												</td>
												<td width="20%" align="right">	
													<span id="mnowyear">当前年份：</span>
												</td>
												<td>	
													<input id="nowyear" type="text"
														style="width: 200px" />
												</td>
											
												</tr>
                                                <tr>
                                                  <td colspan="4"><table border="0" cellpadding="0" cellspacing="0"  width="100%">
                                               <tr>
                                               <td align="center">
													
													<a id="ArrayOperBtn" href="#" class="button" onclick="onArrayItemCreate();">添加</a>&nbsp;
												
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													<a  href="#" class="button" onclick="onCancelArrayOperInput();">取消</a>&nbsp;
												</td>
                                               
                                               
                                               </tr>
                                            
                                            </table></td>
                                                 
                                                </tr>
												
													</table>
												</td>
												
											</tr>
                                            <tr>
                                            
                                            
                                            </tr>
										</table>
                                
                                </td>
                                
                                </tr>
							</table>
							<table width="100%" border="0" cellpadding="0" cellspacing="1"
								align="center" class="table1" id="Sequence"
								style="display: none;">
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz">开始：&nbsp;</span>
									</td>
									<td class="td1" align="left">
										<input type="text" id="Start" />
									</td>
								</tr>
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz">结束：&nbsp;</span>
									</td>
									<td class="td1" align="left">
										<input type="text" id="End" />
									</td>
								</tr>
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz">步长：&nbsp;</span>
									</td>
									<td class="td1" align="left">
										<input type="text" id="Step" />
									</td>
								</tr>
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz">当前值：&nbsp;</span>
									</td>
									<td class="td1" align="left">
										<input type="text" name="nowvalue" />
									</td>
								</tr>
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz">表达方式：&nbsp;</span>
									</td>
									<td class="td1" align="left">
										<select id="Display">
											<option value="数字" selected="selected">
												数字
											</option>
											<option value="中文数字">
												中文数字
											</option>
											<option value="大写中文数字">
												大写中文数字
											</option>
											<option value="小写字母">
												小写字母
											</option>
											<option value="大写字母">
												大写字母
											</option>
											<option value="罗马数字">
												罗马数字
											</option>
										</select>
									</td>
								</tr>
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz">补充字符：&nbsp;</span>
									</td>
									<td class="td1" align="left">
										<input type="text" id="FillChar" />
										<input type="checkbox" id="Fill" />
										不足位时补充字符
									</td>
								</tr>
								<tr>
									<td width="25%" height"21" class="biao_bg1" align="right">
										<span class="wz">当前年份：&nbsp;</span>
									</td>
									<td class="td1" align="left">
										<input type="text" id="year" value="<%=nowYear%>" />
								</td>
							</tr>
						</table>
					</form>
				</td>
			</tr>
		</table>
		</div>
	</body>
</html>
