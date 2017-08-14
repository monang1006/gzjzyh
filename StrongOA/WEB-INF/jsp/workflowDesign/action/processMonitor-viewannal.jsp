<%@ page language="java" import="java.util.*,java.text.SimpleDateFormat" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<%@include file="/common/include/rootPath.jsp"%>
<html>
  <head>
    <title>办理记录查看</title>
    	<%@include file="/common/include/meta.jsp" %>
    	<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
		<link type="text/css" rel="stylesheet" href="<%=path%>/common/js/tabpane/css/luna/tab.css" />
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script type="text/javascript">
			var flag = false;
			//修改
			function edit(id){
				$("#lable_"+id).hide();
				$("#input_"+id).show();
				$("#btn_save_"+id).attr("disabled",false);
			}
			//保存
			function save(id) {
				var oldContent = $("#lable_"+id).text();
				var newConent  = $("#input_"+id).val();
				/*if(oldContent == newConent){
					//alert("内容未发生变化，操作结束。");
					//return ;
				}*/
				oldContent= filter(oldContent);
				newConent = filter(newConent);
				if(newConent.length > 100){
					alert("处理意见字数不能超过100个。");
					return;
				}
				$("#btn_save_"+id).attr("disabled",true);
				$.post("<%=path%>/workflowDesign/action/processMonitor!saveApproveInfo.action",{approveId:id,oldContent:oldContent,content:newConent},
					function(ret){
						if(ret == "0"){//修改成功
							$("#lable_"+id).show();
							$("#input_"+id).hide();
							$("#td_"+id).attr("title",newConent);
							if(newConent!=""&&newConent.length>30){
								newConent = newConent.substring(0,30)+"...";
							}
							$("#lable_"+id).text(newConent);
							flag = true;//修改成功
						} else if(ret == "-1"){//发生异常
							alert("对不起，操作失败，请与管理员联系。");
						} else if(ret == "-2"){//记录不存在
							alert("记录不存在或已删除，操作失败。");
						}
					});
			}
			
			//关闭
			function onReturn(){
				window.returnValue = flag;
			}
			String.prototype.replaceAll = function(s1,s2) { 
			    return this.replace(new RegExp(s1,"gm"),s2); 
			}
			
			/*2011-11-07 yanjian 处理一些特殊字符，这些字符导致无法正常查看流程图*/
			function filter(suggestionValue){
				if(suggestionValue.indexOf("\r")!=-1){				//处理回车
					suggestionValue = suggestionValue.replaceAll("\r", "");
				}
				if(suggestionValue.indexOf("\n")!=-1){				//处理换行
					suggestionValue = suggestionValue.replaceAll("\n", " ");
				}
				
				if(suggestionValue.indexOf("\"")!=-1){				//处理英文形式的双引号
					suggestionValue = suggestionValue.replaceAll("\"", "“");
				}
				if(suggestionValue.indexOf("\'")!=-1){				//处理英文形式的单引号
					suggestionValue = suggestionValue.replaceAll("\'", "’");
				}
				if(suggestionValue.indexOf("<")!=-1){				//处理英文形式的<
					suggestionValue = suggestionValue.replaceAll("<", "＜");
				}
				if(suggestionValue.indexOf(">")!=-1){				//处理英文形式的>
					suggestionValue = suggestionValue.replaceAll(">", "＞");
				}
				if(suggestionValue.indexOf("\\")!=-1){				//处理英文形式的\
					suggestionValue = suggestionValue.replace(/[\\]/gm,"＼");
				}
				return suggestionValue;
			}
//特殊字符过滤
//匹配中文 数字 字母 下划线       
function checkInput(vvv) {
	var s = vvv.value;
	if(s==""||s==null) return;
	//var pattern = new RegExp("[`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）&mdash;—|{}【】‘；：”“'。，、？]")
//    var pattern = new RegExp("[`~!@#$^&*()=|{}':;'\\[\\].<>/?~！@#￥……&*（）&mdash;—|{}【】‘；：”“'。？]")
    var rs = "";
	var ttt =/^[A-Za-z]+$/;//英文字母
	var k;
    for (var i = 0; i < s.length; i++) {
        //rs = rs + s.substr(i, 1).replace(pattern, '');
        //alert(pattern.test(s.substr(i,i+1)))
        var mmm = s.substr(i,1);
        if(ttt.test(mmm)){
        	continue;
        }
//      if(pattern.test(mmm)){
//        	k=i;
//        	rs="……";
//        	break;
//      }
    }
    if(rs!=""){
    	alert("不允许输入特殊字符。");
    	vvv.value = vvv.value.substring(0,k);
    }else{
    	return  true;
    }
}
		</script>
  </head>
  <body onunload="onReturn();">
  <div id="contentborder" align="center">
		<div>
		    <table cellSpacing=1 cellPadding=1 width="100%" border="0" class="table1" >
		    	<tr class="biao_bg2">
		    		<td align="center" width="20%" >
		    			<strong>处理人</strong>
		    		</td> 
					<td align="center" width="20%" >
						<strong>处理时间</strong>
					</td>
		    		<td align="center" width="50%" >
		    			<strong>处理意见</strong>
		    		</td>
		    		<td align="center" width="10%" >
						<strong>操作</strong>
					</td>
				</tr>
				<%
					List annalList = (List)request.getAttribute("annalList");
		
					if(annalList != null && !annalList.isEmpty()) {
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						for(int i=0;i<annalList.size();i++) {
							Object[] objs = (Object[])annalList.get(i);
							String content = (String)objs[3];
							if(content == null){
								content = "";
							}
							//开始
							String subContent = "";
							if(content.length() > 30){
								subContent = content.substring(0,30) + "...";
							}else{
								subContent = content;
							}
							System.out.println("********************" + content);
							
							out.println("<tr class=\"biao_bg1\">");
							
							out.println("<td align=\"center\">");
							out.println(objs[2]);
							out.println("</td>");
							
							out.println("<td align=\"center\">");
							out.println(format.format((Date)objs[4]));
							out.println("</td>");
							
							out.println("<td align=\"left\" id=td_"+objs[6]+" title="+content+">");
							out.println("<span id=lable_"+objs[6]+">");
							out.print(subContent);
							out.println("</span>");
							out.println("<input style=\"display:none;width:100%;\" content=\""+content+"\" onkeyup=\"checkInput(this);\" id=input_"+objs[6]+" value=\""+content+"\">");
							out.println("</td>");
							
							out.println("<td align=\"center\">");
							out.println("<input id=btn_edit_"+objs[6]+" type=\"button\" onclick=\"edit("+objs[6]+");\" class=\"input_bg\" value=\"修改\" />");
							out.println("<input id=btn_save_"+objs[6]+" type=\"button\" onclick=\"save("+objs[6]+");\" disabled class=\"input_bg\" value=\"确定\" />");
							out.println("</td>");
							
							out.println("</tr>");
						}
					}
				%>
			</table>
		</div>
	</body>
</html>
