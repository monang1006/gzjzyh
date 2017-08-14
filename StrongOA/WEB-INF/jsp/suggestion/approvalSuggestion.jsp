<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/security" prefix="security"%>

<HTML>
	<HEAD>
		<TITLE>操作内容</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<!--右键菜单样式 -->
		<LINK type=text/css rel=stylesheet href="<%=frameroot%>/css/properties_windows_list.css">
		<LINK type=text/css rel=stylesheet href="<%=frameroot%>/css/search.css">
		<script language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script language="javascript" src="<%=path%>/common/js/common/search.js"></script>
		<SCRIPT language="javascript" type="text/javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT><!--右键菜单脚本 -->
		<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js' type="text/javascript"></script>
		<script language="javascript" src="<%=path%>/common/js/common/common.js"></script>
			<SCRIPT LANGUAGE="JavaScript" src="<%=path%>/common/js/commontab/service.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></SCRIPT>
		<script type="text/javascript">

         var addAppSuggestion=function(){
			 var ret=  OpenWindow("<%=path%>/suggestion/approvalSuggestion!input.action", "441","343",window);
	          if(ret!=null&&ret!=""&&ret=="ok"){
	          		document.getElementById("myTableForm").submit();
	            		
				}    
            //window.location.href="<%=path%>/suggestion/approvalSuggestion!input.action";
         }
         var editAppSuggestion=function(){
               var id=getValue();
               if(id==null||id==""){
                  alert("请选择要编辑的记录。");   
               }else if(id.length>32){
                  alert("只可以编辑一条记录。");
               }else{
               		  var ret=  OpenWindow("<%=path%>/suggestion/approvalSuggestion!input.action?suggestionCode="+id, "441","343",window);
			          if(ret!=null&&ret!=""&&ret=="ok"){
			          		document.getElementById("myTableForm").submit();
			            		
						}    
               		
                 // window.location.href="<%=path%>/suggestion/approvalSuggestion!input.action?suggestionCode="+id;
               }
         }
         var delAppSuggestion=function(){
          var id=getValue();
               if(id==null||id==""){
                  alert("请选择要删除的记录。");   
               }else{
               	  
                 	// window.location.href="<%=path%>/suggestion/approvalSuggestion!delete.action?suggestionCode="+id;
                  if(confirm("确定要删除吗？")){
			
					 $.post("<%=root%>/suggestion/approvalSuggestion!delete.action",
					           {"suggestionCode":id},
					           function(data){			         
							    if(data=="success"){
							     alert("删除成功。");	
							 	  document.getElementById("myTableForm").submit();								
							    }else{
									alert('删除失败。');
								}
							});
					}
               }
         }
      //上移    
      function upMove(){
      	 var id=getValue();
               if(id==null||id==""){
                  alert("请选择要上移的记录。");   
               }else if(id.length>32){
                  alert("只可以上移一条记录。");
               }else{
               		var chkAll = document.getElementsByName("checkall"); 
			       if(chkAll[0].checked){
			           chkAll[0].checked = false;
			       }
			       var chkButtons=document.getElementsByName("chkButton");
			       if(chkButtons[0].value==id){
			       		alert("已到当前页最顶项，不允许上移。");	
			       		
			       }else{
	               		var objCurrent = $("input:checkbox:checked").eq(0);
	               		var preid = objCurrent.parent().parent().prev().find("input:checkbox").eq(0).val();
	               		if(preid!=""&&preid!=null){
	               			var suggestionCode=id+","+preid;
		               		$.post("<%=root%>/suggestion/approvalSuggestion!moveSeqUp.action",
		               			{"suggestionCode":suggestionCode},
		               			function(data){
		               				if(data=="OK"){
		               					/*
		               					var objSeq=objCurrent.parent().parent().children().eq(4).html();
		               					var preSeq=objCurrent.parent().parent().prev().children().eq(4).html();
		               					if(objSeq!=preSeq){
			               					objCurrent.parent().parent().children().eq(4).html(preSeq);
			               					objCurrent.parent().parent().prev().children().eq(4).html(objSeq);
			               				 	var temp=objCurrent.parent().parent().prev().html();
			               				 	
			               					objCurrent.parent().parent().prev().html(objCurrent.parent().parent().html());
			               					objCurrent.parent().parent().html(temp);
		               					}
		               					
		               					
		               					var objSeq=objCurrent.parent().parent().children().eq(4).html();
		               					var preSeq=objCurrent.parent().parent().prev().children().eq(4).html();
		               					objCurrent.parent().parent().children().eq(4).html(preSeq);
		               					objCurrent.parent().parent().prev().children().eq(4).html(objSeq);
		               					
		               				 	var temp=objCurrent.parent().parent().prev().html();
		               					objCurrent.parent().parent().prev().html(objCurrent.parent().parent().html());
		               					objCurrent.parent().parent().html(temp);
		               					*/

		               					//location="<%=root%>/suggestion/approvalSuggestion.action?state=state";
		               					//location.reload();
		               					//document.all.myTableForm.submit();
		               					this.target="_self"; 
		               					//var reload=document.getElementById('reload'); 
		               					//reload.href =  window.location.href; 
		               					//reload.click();
		               					document.getElementById("myTableForm").submit();
		               				}else if(data=="error"){
		               					alert("上移失败。");
		               				}
		               			} 
		               		)
	               		}
			       
			       }
			       
			       
               }
      }
      
      //下移
      function downMove(){
      	 var id=getValue();
               if(id==null||id==""){
                  alert("请选择要下移的记录。");   
               }else if(id.length>32){
                  alert("只可以下移一条记录。");
               }else{
               		var chkAll = document.getElementsByName("checkall"); 
			       if(chkAll[0].checked){
			           chkAll[0].checked = false;
			       }
			       var chkButtons=document.getElementsByName("chkButton");
			       var n=chkButtons.length-1;
			       if(chkButtons[n].value==id){
			       		alert("已到当前页最底项，不允许下移。");	
			       		
			       }else{
	               		var objCurrent = $("input:checkbox:checked").eq(0);
	               		var nextid = objCurrent.parent().parent().next().find("input:checkbox").eq(0).val();
	               		if(nextid!=""&&nextid!=null){
	               			var suggestionCode=id+","+nextid;
		               		$.post("<%=root%>/suggestion/approvalSuggestion!moveSeqDown.action",
		               			{"suggestionCode":suggestionCode},
		               			function(data){
		               				if(data=="OK"){
		               					/*var objSeq=objCurrent.parent().parent().children().eq(4).html();
		               					var preSeq=objCurrent.parent().parent().next().children().eq(4).html();
		               					if(objSeq!=preSeq){
			               					objCurrent.parent().parent().children().eq(4).html(preSeq);
			               					objCurrent.parent().parent().next().children().eq(4).html(objSeq);
			               					
			               					var temp=objCurrent.parent().parent().next().html();
			               					objCurrent.parent().parent().next().html(objCurrent.parent().parent().html());
			               					objCurrent.parent().parent().html(temp);
		               					}
		               					
		               					var objSeq=objCurrent.parent().parent().children().eq(4).html();
		               					var preSeq=objCurrent.parent().parent().next().children().eq(4).html();
		               					objCurrent.parent().parent().children().eq(4).html(preSeq);
		               					objCurrent.parent().parent().next().children().eq(4).html(objSeq);
		               				
		               				
		               					var temp=objCurrent.parent().parent().next().html();
		               					objCurrent.parent().parent().next().html(objCurrent.parent().parent().html());
		               					objCurrent.parent().parent().html(temp);
		               					*/
		               					//location="<%=root%>/suggestion/approvalSuggestion.action?state=state";
		               					//location.reload();

		               					this.target="_self"; 
		               					//var reload=document.getElementById('reload'); 
		               					//reload.href =  window.location.href; 
		               					//reload.click();
		               					document.getElementById("myTableForm").submit();
		               				}else if(data=="error"){
		               					alert("下移失败。");
		               				}
		               			} 
		               		)
	               		}  
			       
			       }
               }
      }
         
      //关闭窗口  
       function closeSuggestion(){
       		
       		//document.getElementById("myTableForm").submit();
       		window.returnValue="ok";
       		window.close();
       }  
       
       function onunLoadClick(){
       		window.returnValue="ok";
       }
       
       $(function(){
       		$('#myTable_div tbody tr:first').remove();
       });
       
</script>
	</HEAD>
	<base target="_self"/>
	<BODY class=contentbodymargin oncontextmenu="return false;"
		onload="initMenuT()" onunload="onunLoadClick()">
		<script language="javascript" type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
						<td colspan="3" class="table_headtd">
						<s:form theme="simple" id="myTableForm"
							action="/suggestion/approvalSuggestion.action">
							<input type="hidden" name="state" id="state" value="${state}">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
										<table width="100%" height="40" border="0" cellspacing="0" cellpadding="00">
											<tr>
												<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
								<strong>常用意见</strong>
							</td>
							<td align="right">
													<table  border="0" align="right"
														cellpadding="0" cellspacing="0">
														<tr>
															<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
										                 	<td class="Operation_list" onclick="addAppSuggestion();"><img src="<%=root%>/images/operationbtn/add.png"/>&nbsp;新&nbsp;建&nbsp;</td>
										                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
									                  		<td width="5"></td>
									                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
										                 	<td class="Operation_list" onclick="editAppSuggestion();"><img src="<%=root%>/images/operationbtn/edit.png"/>&nbsp;编&nbsp;辑&nbsp;</td>
										                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
									                  		<td width="5"></td>
									                  		
									                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
										                 	<td class="Operation_list" onclick="delAppSuggestion();"><img src="<%=root%>/images/operationbtn/del.png"/>&nbsp;删&nbsp;除&nbsp;</td>
										                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
									                  		<td width="5"></td>
									                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
										                 	<td class="Operation_list" onclick="upMove();"><img src="<%=root%>/images/operationbtn/Move.png"/>&nbsp;上&nbsp;移&nbsp;</td>
										                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
									                  		<td width="5"></td>
															<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
										                 	<td class="Operation_list" onclick="downMove();"><img src="<%=root%>/images/operationbtn/Down.png"/>&nbsp;下&nbsp;移&nbsp;</td>
										                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
									                  		<td width="5"></td>
															<td width="5">&nbsp;</td>
															<s:if test="state!=null&&state!=\"\"">
																<%--<td>
																	<a class="Operation" href="javascript:closeSuggestion()"><img src="<%=root%>/images/ico/guanbi.gif" width="15"
																			height="15" class="img_s">关闭&nbsp;</a>
																</td>
																--%>
																
																<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
										                 	<td class="Operation_list" onclick="closeSuggestion();"><img src="<%=root%>/images/operationbtn/close.png"/>&nbsp;关&nbsp;闭&nbsp;</td>
										                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
									                  		<td width="5"></td>
																
																<td width="5">&nbsp;</td>
															</s:if>
															</tr>
														</table>
													</td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
							
							<webflex:flexTable name="myTable" width="100%" height="370px"
								wholeCss="table1" property="suggestionCode" isCanDrag="true"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByProperty" collection="${page.result}"
								page="${page}">
								<%-- <table width="100%" border="0" cellpadding="0" cellspacing="1"
									class="table1">
									<tr>
										<td width="5%" align="center" class="biao_bg1">
											<img src="<%=root%>/images/ico/sousuo.gif" width="17"
												height="16" onclick="getListBySta()" style="cursor: hand;">
										</td>
										
										<td width="65%" align="center" class="biao_bg1">
											<input  name="model.suggestionContent" type="text"
												style="width:100%" maxlength="25" value="${model.suggestionContent}" class="search" title="请输入意见名称">
										</td>
									
								
										<td width="15%" align="center" class="biao_bg1">
											  <strong:newdate name="startDate" id="startDate" width="98%"
						                      skin="whyGreen" isicon="true" dateform="yyyy-MM-dd" dateobj="${startDate}"></strong:newdate>
										
											<strong:newdate id="startDate" name="startDate"
													dateform="yyyy-MM-dd" isicon="true" width="100%"
													dateobj="${startDate}" classtyle="search" title="起始日期"/>
										</td>
										<td width="15%" align="center" class="biao_bg1">
											  <strong:newdate name="endDate" id="endDate" width="98%"
						                     	 skin="whyGreen" isicon="true" dateform="yyyy-MM-dd" dateobj="${endDate}"></strong:newdate>
										
											<strong:newdate id="endDate" name="endDate"
													dateform="yyyy-MM-dd" isicon="true" width="100%"
													dateobj="${endDate}" classtyle="search" title="结止日期"/>
										</td>
										<td class="biao_bg1">
											&nbsp;
										</td>
									</tr>
								</table>--%>
								<webflex:flexCheckBoxCol caption="选择" property="suggestionCode"
									showValue="suggestionContent" width="5%" isCheckAll="true"
									isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								
								<webflex:flexTextCol caption="意见内容" property="suggestionContent"
									showValue="suggestionContent" width="80%" isCanDrag="true"  showsize="35"
									isCanSort="true"></webflex:flexTextCol>
								
							<%--<webflex:flexDateCol caption="创建日期" property="suggestionDate"
									showValue="suggestionDate" width="20%" isCanDrag="true"
									isCanSort="true" dateFormat="yyyy-MM-dd"></webflex:flexDateCol>--%>
								<webflex:flexTextCol align="center" caption="排序序号" property="suggestionSeq"
									showValue="suggestionSeq" width="15%" isCanDrag="true"  
									isCanSort="true"></webflex:flexTextCol>
							</webflex:flexTable>
						</s:form>
					</td>
				</tr>
			</table>
			
		</DIV>
		<script language="javascript">
         var sMenu = new Menu();
          function initMenuT(){
	        sMenu.registerToDoc(sMenu);
	        var item = null;
	        
		        item = new MenuItem("<%=root%>/images/operationbtn/add.png","新建","addAppSuggestion",1,"ChangeWidthTable","checkMoreDis");
		        sMenu.addItem(item);
		    
		        item = new MenuItem("<%=root%>/images/operationbtn/edit.png","编辑","editAppSuggestion",1,"ChangeWidthTable","checkOneDis");
		        sMenu.addItem(item);

		        item = new MenuItem("<%=root%>/images/operationbtn/del.png","删除","delAppSuggestion",1,"ChangeWidthTable","checkOneDis");
		        sMenu.addItem(item);
		        
		        item = new MenuItem("<%=root%>/images/operationbtn/Move.png","上移","upMove",1,"ChangeWidthTable","checkOneDis");
		        sMenu.addItem(item);

		        item = new MenuItem("<%=root%>/images/operationbtn/Down.png","下移","downMove",1,"ChangeWidthTable","checkOneDis");
		        sMenu.addItem(item);
		        
		     var state = "${state}";    
	        if(state!=null&&state!=""){
			
		        item = new MenuItem("<%=root%>/images/operationbtn/close.png","关闭","closeSuggestion",1,"ChangeWidthTable","checkOneDis");
		        sMenu.addItem(item);
			}  
			
	
	        sMenu.addShowType("ChangeWidthTable");
            registerMenu(sMenu);
         }
function getListBySta(){	//根据属性查询
	//document.getElementById("myTableForm").action="<%=path%>/suggestion/approvalSuggestion.action";
	document.getElementById("myTableForm").submit();
}
</script>
<a id="reload" href="" style="display:none"></a>
	</BODY>
</HTML>
