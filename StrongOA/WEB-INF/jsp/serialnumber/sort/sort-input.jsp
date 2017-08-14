<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"  contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
   <%@include file="/common/include/meta.jsp"%>
    
    <title>序列生成器</title>
    
	<link href="<%=frameroot%>/css/properties_windows_add.css" type="text/css" rel="stylesheet">
	<script type="text/javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
	<script src="<%=path%>/common/js/upload/jquery.MultiFile.js" type="text/javascript" language="javascript"></script>
	<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<script src="<%=path%>/common/js/upload/jquery.blockUI.js" type="text/javascript" language="javascript"></script>
			<script src="<%=path%>/common/js/common/common.js"
      type="text/javascript"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/service.js"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></script>
		<script type="text/javascript">
		 var id=0;//代字数量，默认为1
		
		//表单提交
		 var onSub=function(){
		 var sortabb="";
		 var sortName= $.trim($("#sortName").val());
		 if(sortName==""){
		 alert("类型名称不可以为空！");
		 return false;
		 }
		 var sortId=$("#sortId").val();
		 $.post("<%=path%>/serialnumber/sort/sort!IsSortName.action",
	           {"sortName":sortName,"sortId":sortId},
	           function(data){
	           		if(data=="0"){
	           			alert("当前规则名称“"+name+"”已经存在,请重新输入！！");
		       			return;
	           		}	
	           		else{
					 $(".abbre").each(function(){
							sortabb=sortabb+$(this).val()+",";
						});
						sortabb=sortabb.substring(0,sortabb.length-1);
					    $("#sortAbbreviation").val(sortabb);
					    $("#sumit").attr("disabled",true);
					    $("#sort").submit();
					 }
					 
					 
	           	 });	
		 }
		 
		 var del=function(value){
					     var temp="."+value;
					     $("p").remove(temp); 
					      var htm="";
					     id=0;
					      $(".abbre").each(function(){
					      id++;
					      htm=htm+"<p class="+id+"><span id="+id+">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;第"+id+"个代字：</span><input id='abbre' maxlength='10' class='abbre' value='"+$(this).val()+"' type='text'><a href=javascript:del("+id+")><span>删除</span></a></p>"
							
							
						});
					    $("#abbreviation").html(htm);
	           		}
		 
		 $(document).ready(function(){
		    var value="${model.sortAbbreviation}";
		    if(value!=null&&value!=""){
		         var htmlvalue="";
		         var upvalue=value.split(",");
		         id=upvalue.length;
		         for(var i=0;i<id;i++){
		            htmlvalue=$("<p class="+(i+1)+"><span id="+(i+1)+">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;第"+(i+1)+"个代字：</span><input id='abbre' class='abbre' type='text' maxlength='10' value='"+upvalue[i]+"'><a href=javascript:del("+(i+1)+")><span>删除</span></a></p>");
		             $("#abbreviation").append(htmlvalue);
		         }
		         
		    }
		 
		   $("#button").click(function(){//单击添加代字按钮事件
<%--		     if(id<10){--%>
		     id++;
		      var invalue=$("<p class="+id+"><span id="+id+">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;第"+id+"个代字：</span><input id='abbre' maxlength='10' class='abbre' type='text'><a href=javascript:del("+id+")><span>删除</span></a></p>");
		      $("#abbreviation").append(invalue);
<%--		      }else{--%>
<%--		        alert("同一个序列号类型文号代字不可以超过10个！");--%>
<%--		      }--%>
		   
		   });
		  
		 });
		  function close(){//返回按钮单击事件
		window.history.go(-1);
		}
		</script>
  </head>

  <body class="contentbodymargin">
 	<form action="<%=path %>/serialnumber/sort/sort!save.action" method="post"  id="sort" name="sort">
 	<input type="hidden" name="model.sortAbbreviation" id="sortAbbreviation">
 	<input type="hidden" name="sortId" id="sortId" value="${model.sortId }">
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
								<strong>文件类型</strong>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
					                <tr>
										<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onclick="onSub();">&nbsp;确&nbsp;定&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
				                  		<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
					                 	<td class="Operation_input1" onclick="javascript:history.back(-1);">&nbsp;返&nbsp;回&nbsp;</td>
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
            文件类型
          </legend>
          <div style="padding: 15px 15px 15px 15px;text-align: left;">
                        
              <div style="padding: 15px 15px 15px 15px; text-align: left;">
                <div>
                  请填写文件类型：&nbsp;&nbsp;&nbsp;&nbsp;
                  
                 <input type="text" name="model.sortName" maxlength="25" value="${model.sortName }" id="sortName">
                  
                </div>
                
                <div id="nextstep">
                &nbsp;
                </div>
              </div>         
           
           
              <div  style="padding: 15px 15px 15px 15px;">
              <table ><tr><td width="30%" valign="top">
               <input type="button" id="button" name="button"  value="添加一个文号代字"></td><td width="70%">
               <div id="abbreviation"></div></td></tr></table>
              </div>
       
          </div>
        <!--  <div style="width: 100%;" align="center">
     
         
        <span><input type="button"  value="确   定" id="sumit" 
            onclick="onSub()" class="input_bg"></span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <span><input type="button" id="cancel" value="返   回" class="input_bg"></span>
      </div>-->
      </fieldset>
    </div>
   

      </div>
      

   <form>
  </body>
</html>
