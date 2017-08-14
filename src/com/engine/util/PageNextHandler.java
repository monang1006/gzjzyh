package com.engine.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class PageNextHandler {
	
  
  public  String getHTML(int[] page,Map para,String action){
	  //page[0]:页码总数、page[1]:数据总数、page[2]:当前页
	  ThreadLocalUtils.put(Define.WEB_PAGE_COUNT,page[0]);	 	  
	  String splitString="&nbsp;&nbsp;&nbsp;";
	  StringBuffer returnHtml=new StringBuffer();
	  returnHtml.append("\n<table><tr><form name='strongPageNext' method='GET' action='").append(action).append("'>\n")
	  .append("<td>")
      .append("共&nbsp;").append(page[0]).append("&nbsp;页&nbsp;")
	  .append(page[1]).append("&nbsp;条记录").append(splitString).append("当前第&nbsp;").append(page[2]).append("&nbsp;页&nbsp;")
	  .append(splitString);
	  
	  if(page[2]>1){
		returnHtml.append("<A href=\"javascript:gotoCurrentPage('1')\">首页</A>&nbsp;&nbsp;&nbsp;")
		.append("<A href=\"javascript:gotoCurrentPage('").append(page[2]-1).append("')\">上一页</A>&nbsp;&nbsp;&nbsp;");
	  }else{
		  returnHtml.append("首页&nbsp;&nbsp;&nbsp;").append("上一页&nbsp;&nbsp;&nbsp;");
	  }
	returnHtml.append("\n第&nbsp;<select class=\"conditionSelectBox\" onChange='javascript:gotoCurrentPage(this.value)'>");

	if(page[0]>1){
		for(int i=1;i<=page[0];i++){
			if(i==page[2]){
				returnHtml.append("<option value='").append(i).append("' selected='selected' >").append(i).append("/").append(page[0]).append("</option>");
			}else{				
				returnHtml.append("<option value='").append(i).append("'>").append(i).append("/").append(page[0]).append("</option>");
			}			
		}
		
	}else{
		returnHtml.append("<option value='1'>1/1</option>");		
	}
	returnHtml.append("</select>").append("&nbsp;页&nbsp;&nbsp;&nbsp;");

  if(page[2]<page[0]){
	  
	  returnHtml.append("<A href=\"javascript:gotoCurrentPage('").append(page[2]+1).append("')\">下一页</A>&nbsp;&nbsp;&nbsp;")
	  .append("<A href=\"javascript:gotoCurrentPage('").append(page[0]).append("')\">尾页</A>");
	  
  }else{
	  returnHtml.append("下一页&nbsp;&nbsp;&nbsp;").append("尾页");	  
  }
  
  returnHtml.append("\n<input type='hidden' id='").append(Define.WEB_PAGE).append("' name='").append(Define.WEB_PAGE).append("' value='").append(page[2]).append("'>")
    .append(creatPara(para))
    .append("\n<script language=\"javascript\">\n")
	.append("function gotoCurrentPage(pageNo) {\n")
	  .append("var pageE = document.getElementById(\"").append(Define.WEB_PAGE).append("\");\n")
      .append("pageE.value = pageNo;\n")
      .append("document.strongPageNext.submit();\n")
	  .append("}\n")
    .append("</script>");
  returnHtml.append("</td>").append("</form></tr></table>"); 
	  
	  return returnHtml.toString();
  }
  
  private String creatPara(Map para){
	  
	  StringBuffer returnString=new StringBuffer();
	  if(para!=null){
	  if(!para.isEmpty()){
		  Set name=para.keySet();
		  for(Iterator it=name.iterator();it.hasNext();){
			  String paraName=(String)it.next();
			  returnString.append("<input type='hidden' name='").append(paraName).append("' value='").append(para.get(paraName)).append("'>\n");
		  }
	  }	
	  }

	  return returnString.toString();
	  
  }
}
