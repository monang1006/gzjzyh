var tryoutstate="0";
var pro_type="";
var imageflag=0;
var imagealgin="";
setPageListenerEnabled(false);//设置页面数据发生改变是否监听
var flag=1;
var procount=0;
var prostr="",comstr="";
var viewchanged=false;
			
function viewtext_Change(){			
	viewchanged=true;
}
String.prototype.trim = function() {
	var strTrim = this.replace(/(^\s*)|(\s*$)/g, "");
	strTrim = strTrim.replace(/^[\s　\t]+|[\s　\t]+$/, ""); 
	return strTrim;
}
			
//提交表单
function submitForm(){
				
	var pros=document.all.propertyList.value;
	var prostr=pros.split(',');
	var sflag=false;
	var contb=document.all.textvalue;
	for(var k=0;k<contb.options.length;k++){
		var id1=contb.options[k].value;
		var oEditor = FCKeditorAPI.GetInstance('content'+id1);
        var acontent = oEditor.GetXHTML();
        document.getElementById(id1).value = acontent;
    }
	for(var i=0;i<prostr.length;i++){
		var proid=document.getElementById(prostr[i]);
		var proname=document.getElementById(prostr[i]+"_name");
		if(prostr[i]!=null&&prostr[i]!=""&&((proid!=undefined&&proid.type!="hidden")||(proname!=undefined&&proname.value!=""))){
			var value=document.getElementById(prostr[i]).value;
			if(value!=null&&value!=""){
				sflag=true;
			}
		}
	}
	if(!sflag){
		alert("没有可提交的数据！");
			return false;
	}
	var checkvalue=document.all.checkvalue;
	for(var i=0;i<checkvalue.options.length;i++){
		var checkoption=checkvalue.options[i];
		if(document.getElementById(checkoption.value).value==""){
			alert("["+checkoption.text+"]不能为空！");
			return false;
		}
	}
	document.getElementById("comform").submit();
	   		
}	
function window.onbeforeunload(){
	window.dialogArguments.submitForm();
}
function cancel(){
	window.close();
}
$(document).ready(function() {
	$(".dictrefrence").dblclick(function(){
		var id = this.pro;
		var dataid = this.refrence;
		if(dataid==null||dataid=="null"||dataid==""){
			alert("该信息项未设置字典，请设置相应字典！");
			return false;
		}
		selectBaseData(id,id+"_name",dataid);
		notifyChange(true);
	})
	
	$(".dictrefrence").keyup(function(){
		var p=new Array();
		var id = this.pro;
		var dataid = this.refrence;
		p[0]=$('#'+id+'_name').val();//$(id+"_name").value;
		if(p[0]!=null&&p[0]!=""){
		p[1]=dataid;
		$.getJSON(scriptroot+"/infotable/infoTable!findictitem.action",{"inputname":p[0],"dataid":p[1]},function(json){
	    	if(json==null||json.length==0){
	    		alert("不存在该名称,请重新输入");
	    		$('#'+id+'_name').val("");
	    		$('#'+id).val("");
	    		$('#'+id+'_name').focus();
	    	}
	    	else{
	    		var checkField=$('#'+id+'_dictselect');
	    		while(length!=0){  
        			var length=checkField.options.length;
       				for(var i=0;i<length;i++)
             			checkField.options.remove(i);
         				length=length/2;
   				}
   				$.each(json,function(index,jsons){
   					var opt=new Option(jsons.dictItemShortdesc,jsons.dictItemCode,true,true)  
					checkField.options[checkField.options.length]=opt; 
   				});
				checkField.selectedIndex=0;
				doTextFocus($('#'+id+'_name'));
	    	}
	    }
	    );
		notifyChange(true);
	}
	});
});
function checkPhone(obj){
	var Phone =/^((\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/;
	if(obj.value!=""&&!Phone.test(obj.value)){
		alert("格式错误！");
		obj.style.color="red";
		obj.focus();
	}else{
		obj.style.color="black";
	}
}
function getDate(obj,type,len){
	if(type==3){
		setyear(obj);
	}
	else if(type==4){
		setday(obj);
	}
	else{
		setday(obj);
	}
}
function setyear(obj){
	if(obj==null || obj.tagName!="INPUT")
		return;
	var pobj=obj.parentElement.children[1];
	if(pobj==null && pobj.tagName!="DIV") return;
  	obj.style.display="none";
  	pobj.style.display="block";
  	pobj.children[0].focus();
}
function getyear(obj){
	if(obj==null || obj.tagName!="SELECT" || obj.options.length==0)
	return;
	var nobj=obj.parentElement.parentElement.children[0];
	if(nobj==null || nobj.tagName!="INPUT") return;
	var opt=obj.options[obj.selectedIndex];
	nobj.value=opt.innerText;
	obj.parentElement.style.display="none";
	nobj.style.display="block";
}
function createFields(id,field){
	var checkField=document.getElementById(id+"_dictselect");
	while(length!=0){  
        var length=checkField.options.length;
       	for(var i=0;i<length;i++)
             checkField.options.remove(i);
         	length=length/2;
   	}
   	var i;
	for(i=0;i<field.length;i++){
		var opt=new Option(field[i]["CHECK_FIELD_NAME"],field[i]["CHECK_FIELD_ID"],true,true)  
		checkField.options[checkField.options.length]=opt; 
	}
	//checkField.size=checkField.options.length;
	checkField.selectedIndex=0;
	doTextFocus(document.getElementById(id+"_name"));
}
function doSelChange(obj){
	if(obj==null || obj.tagName!="SELECT")
		return;
	var nameobj=obj.parentElement.parentElement.children[0];
	var idobj=document.getElementById(obj.id.substring(0,obj.id.length-11));
	obj.parentElement.parentElement.children[2];
	if(nameobj==null || nameobj.tagName!="INPUT") return;
	nameobj.value=obj.options[obj.selectedIndex].text;
	idobj.value=obj.options[obj.selectedIndex].value;
	
	obj.parentElement.style.display="none";
	nameobj.style.display="block";
	
	getDictOtherValue(idobj);
}
function getImage(obj,type,len){
	doTextFocus(obj)
}
function resizepic(id,name){
 picname = contextPath+"/"+document.getElementById(id).value;
 document.getElementById("imgoo"+id).src = picname;
 if((190/190)>(document.getElementById("imgoo"+id).width/document.getElementById("imgoo"+id).height))
 document.getElementById("imgstr"+id).innerHTML = "<img  width='190' height='180' src='"+picname+"' />";
 else
 document.getElementById("imgstr"+id).innerHTML = "<img width='190' height='180' src='"+picname+"' />";
 document.getElementById("label"+id).innerHTML = name+"：<br>"+"<INPUT type='button' class='anniu' value='修改' onclick=editimg('"+id+"','"+name+"')>"
}
function editimg(id,name){
document.getElementById(id).value = "";
document.getElementById("imgstr"+id).innerHTML = "<iframe id='"+id+"frame' name='"+id+"frame' frameborder=0 scrolling=no src='"+scriptroot+"/fileNameRedirectAction.action?toPage=infotable/upfile.jsp?id="+id+"&name="+name+"' width='250' height='180'></iframe>"
document.getElementById("label"+id).innerHTML = name+"：";
}
function doTextFocus(obj){
	if(obj==null || obj.tagName!="INPUT")
		return;
	var pobj=obj.parentElement.children[1];
	if(pobj==null && pobj.tagName!="DIV") return;
	obj.style.display="none";
	pobj.style.display="block";
	pobj.children[0].focus();
	
}

function doSelBlur(obj){	
	if(obj==null || obj.tagName!="INPUT")
		return;
	var nobj=obj.parentElement.parentElement.children[0];
	if(nobj==null || nobj.tagName!="INPUT") return;
	nobj.value=obj.value;
	obj.parentElement.style.display="none";
	nobj.style.display="block";
}

function doFileFocus(obj){
	if(obj==null || obj.tagName!="INPUT")
		return;
	var pobj=obj.parentElement.children[1];
	if(pobj==null && pobj.tagName!="IFRAME") return;
	obj.style.display="none";
	pobj.style.display="block";
	pobj.focus();
	
}

function doFileBlur(obj){	
	if(obj==null || obj.tagName!="IFRAME")
		return;
	var nobj=obj.parentElement.children[0];
	if(nobj==null || nobj.tagName!="INPUT") return;
	//nobj.value=obj.getValue();
	obj.style.display="none";
	nobj.style.display="block";
}

function checkinput(obj,type,len,dec) {
    var str = obj.value, str1, str2;
    //alert(str);
    if(type==2){//为数字
    	if (isNaN(str)) {//判断是否包含字母
        	alert("\u4e0d\u80fd\u8f93\u5165\u5b57\u6bcd!");
        	obj.value = 0;
        	return false;
    	}
    	 var pos = str.indexOf(".");//判断小数点后位数
    	 if(dec==0&&pos!=-1){
    	 	alert("格式错误!");
        	str = str.substring(0,  str.length-1);
    	 }
    	 else{
    	 	str2 = str.substring(pos);
    	 	if (pos!=-1&&str2.length - 1 > dec) {
        		alert("\u4e0d\u80fd\u8d85\u8fc7\u6307\u5b9a\u5c0f\u6570\u70b9\u4f4d\u6570" + dec);
        		str = str.substring(0, dec + pos);
        	}
    		if (str.substring(0, 1) == "0"&&str.substring(1, 2) != ".") {//去除不够成数字的首位零
        		str = str.substring(1);
    		}
    	}
    	obj.value = str;    	
    }   
}
function chooseText1(type, proset, id1, dataid, itemflag,len, dec, value,label) {
	if(type==11){
		document.write("<input type='hidden' name='" + id1 + "' id='"+id1+"' value='"+value+"'");
		if(itemflag==1)
        	document.write("  class='required'");
		document.write("><img id='imgoo" + id1 + "' style='display: none'  src='' />");   	
		document.write("<td align='left' width='250' height='180' rowspan='6' align='right' id='imgstr"+id1+"'>");
	}
	else
    	document.write("<td align='left' width='25%'>");
    if (type == 0) {//类型为字符或数字
        document.write("<input type='text' id='"+id1+"' name='" + id1 + "' size=30  maxlength='"+(len*1+dec*1)+"' value='"+value+"'" );
        if (proset == 0) {     	
            document.write(" onkeyup=checkinput(this,'" + type + "','" + len + "','" + dec + "') ");
        } else {
            document.write("style='background-color: #F0F0E8' readonly ");
        }
        if(itemflag==1)
        	document.write(" class='required'");
        document.write(">");
    }else if(type == 2){
    	document.write("<input type='text' id='"+id1+"' name='" + id1 + "' size=30  maxlength='"+(len*1+dec*1)+"' value='"+value+"'" );
        if (proset == 0) {     	
            document.write(" onkeyup=checkinput(this,'" + type + "','" + len + "','" + dec + "') ");
        } else {
            document.write("style='background-color: #F0F0E8' readonly ");
        }
        document.write(" class='number");
        if(itemflag==1)
        	document.write(" required");
        document.write("'>");
    }else if(type == 12){//类型为电话
    	document.write("<input type='text' id='"+id1+"' name='" + id1 + "' size=30  maxlength='"+len+"' value='"+value+"'" );
    	 if (proset == 0) {
    		document.write(" onkeyup='viewtext_Change()' dataType='Phone' onblur=checkPhone(this)");
    	}else {
            document.write("style='background-color: #F0F0E8' readonly ");
        }
        if(itemflag==1)
        	document.write("  class='required'");
    	document.write(">");
    }
    else if (type == 1) {//类型为代码
    	var value1="",value2=value;
    	if(value.indexOf(",")!=-1){
    		var vstr=value.split(",");
    		value1=vstr[0];
    		value2=vstr[1];
    	}
        document.write("<input type='text' id='" + id1 + "_name' name='" + id1 + "_name' size=30  maxlength='"+len+"'  value='"+value2+"' readonly" );
        if(proset==2){
        	 document.write("style='background-color: #F0F0E8'  ");
        }
        else if (proset == 0) {
        	
            	document.write(" style='background: #D8E5F7' class='dictrefrence' refrence='"+dataid+"' pro='"+id1+"'");
  
        }
         document.write(">");
         document.write("<div style='margin:1px;width:100%; overflow:hidden;display:none'><select id='"+id1+"_dictselect' name='"+id1+"_dictselect' style='margin-left:-3px;margin-top:-2px;margin-bottom:-2px;width:100%;' onblur='doSelChange(this)'></div>");
        document.write("<input type='hidden' id='" + id1 + "' name='" + id1 + "'  maxlength='"+len+"'  value='"+value1+"' onchange='getDictOtherValue(this)'" );
        if(itemflag==1)
        	document.write("  class='required'");
        document.write(">");	
    }
    else if (type == 3){//类型为年
    	var dateobj=document.getElementById("nowdate");
    	if(value==""&&dateobj!=undefined&&dateobj.value!="")
    		value=dateobj.value.substring(4);
    	document.write("<input type='text' name='" + id1 + "' preset='YEAR' size=30  maxlength='"+len+"' value='"+value+"'" );
        if (proset != 0) {
            document.write("style='background-color: #F0F0E8' readonly ");
        }
        if(itemflag==1)
        	document.write("  class='required'");
        document.write(" onkeyup='viewtext_Change()'>");
    }
    else if(type == 4){//类型为年月
    	var dateobj=document.getElementById("nowdate");
    	if(value==""&&dateobj!=undefined&&dateobj.value!="")
    		value=dateobj.value.substring(7);
    	document.write("<input type='text' id='"+id1+"' name='" + id1 + "' preset='MONTH' size=30 maxlength='"+len+"' value='"+value+"'" );
        if (proset != 0) {
            document.write("style='background-color: #F0F0E8' readonly ");
        }
        if(itemflag==1)
        	document.write("  class='required'");
        document.write(" onkeyup='viewtext_Change()'>");
    } 
	else if(type == 5) {//类型为年月日
		var dateobj=document.getElementById("nowdate");
    	if(value==""&&dateobj!=undefined&&dateobj.value!="")
    		value=dateobj.value;
        document.write("<input type='text' name='" + id1 + "' preset='DATE' size=30 maxlength='"+len+"' value='"+value+"'" );
        if (proset != 0) {
            document.write("style='background-color: #F0F0E8' readonly ");
        }
        if(itemflag==1)
        	document.write("  class='required'");
        document.write(" onkeyup='viewtext_Change()'>");
    }
    else if(type == 6) {//类型为年月日时间
		var dateobj=document.getElementById("nowdate");
    	if(value==""&&dateobj!=undefined&&dateobj.value!="")
    		value=dateobj.value;
        document.write("<input type='text' name='" + id1 + "' preset='DATE' size=30 maxlength='"+len+"' value='"+value+"'" );
        if (proset != 0) {
            document.write("style='background-color: #F0F0E8' readonly ");
        }
        if(itemflag==1)
        	document.write("  class='required'");
        document.write(" onkeyup='viewtext_Change()'>");
    }
    else if (type == 11) {//类型为图片
    	if(imageflag==0)
    		imageflag=6;
    	else if(imageflag>0)
    		imageflag=0;
        //document.write("<input type='hidden' name='" + id1 + "' id='"+id1+"' size=30 maxlength='"+len+"'  value='"+value+"'");
        //if (proset == 0) {
        //    document.write(" onclick='doFileFocus(this);viewtext_Change()' ");
        //}
        //document.write(">");
        //document.write("<iframe id='" + id1 + "frame' name='" + id1 + "frame' frameborder=0 scrolling=no src='"+scriptroot+"/sysbuild/tablecommon/upfile.jsp?id="+id1+"' width='' height='40' style='display:none' onmouseout='doFileBlur(this)'></iframe>");
        //document.write("<div style='margin:1px;width:100%; overflow:hidden;display:none'><input type='file' id='" + id1 + "_file' name='" + id1 + "_file' style='margin-left:-3px;margin-top:-2px;margin-bottom:-2px;width:100%;' contentEditable='false' accept='image/gif' onchange='preview(this,0);' onmouseout='doFileBlur(this)'></div>");
    	document.write("<iframe id='" + id1 + "frame' name='" + id1 + "frame' frameborder=0 scrolling=no src='"+scriptroot+"/fileNameRedirectAction.action?toPage=infotable/upfile.jsp?id="+id1+"&name="+label+"&type=img' width='250' height='180'></iframe>");
        if(value!=null&&value!=""&&value!="null")
        	resizepic(id1,label);
    }
    else if (type == 10) {//类型为文件
        document.write("<input type='hidden' name='" + id1 + "' id='"+id1+"' size=30 value='"+value+"'");
        if(itemflag==1)
        	document.write(" class='required");
        document.write("'>");	
        if (value !=null) {
            document.write("<a href='contextPath/"+value+"'>查看文件</a>");
        }
        document.write("<br>");
        
        document.write("<iframe id='" + id1 + "frame' name='" + id1 + "frame' frameborder=0 scrolling=no src='"+scriptroot+"/company/upfile.jsp?id="+id1+"&type=file' width='' height='40'></iframe>");      
    }
    document.write("</td>");
}
function chooseText(flag, type, proset, id, dataid, itemflag,label, len, dec,value,protype) {
	viewchanged=false;
    var id1 = id;
    var str1;
    var checkselect=document.all.checkvalue;
    var textvalue=document.all.textvalue;
    if(protype!=null&&protype!=""&&protype!="null"){
    	if(pro_type==""){
    		pro_type=protype;
    		document.write("<fieldset id='Demo"+id+"'><legend>"+protype+"</legend><table>");
    	}
    	else if(pro_type!=protype){
    		pro_type=protype;
    		if(flag==2){
    			document.write("<td></td></tr>");
    			flag=1;
    		}	
    		document.write("</table></fieldset><br>");
    		document.write("<fieldset id='Demo"+id+"'><legend>"+protype+"</legend><table>");
    	}
    }else if(pro_type!=""&&protype==""){
    	pro_type="";
    	if(flag==2){
    			document.write("<td></td></tr>");
    			flag=1;
    	}	
    	document.write("</table></fieldset><br>");
    	document.write("<table>");
    }else if(pro_type==""&&protype==""){
    	
    }
    if (proset == 1) {
        if(flag == 2){
        	document.write("<input type='hidden' name='" + id1 + "' value='"+value+"'>");
         	flag = 2;
        }else{
        	document.write("<input type='hidden' name='" + id1 + "' value='"+value+"'>");
         	flag = 1;
        }
         return flag;
    } else {
    	if(imageflag>0){
    		if(imagealgin=="left"){
    			flag=2;
    		}else if(imagealgin=="right"){
    			flag=1;
    		}
    		imageflag--;
    	}
        if (flag == 1) {
        	if(type == 11){//类型为图片
        		document.write("<tr><td align='right' width='20%' class='biao_bg1' id='label"+id1+"' label='"+label+"' rowspan='6' align='right'><span class='wz'>" + label);        		
        		imagealgin="left";
        	}
        	else
            	document.write("<tr><td align='right' width='20%' class='biao_bg1' id='label"+id1+"' label='"+label+"'><span class='wz'>" + label);
            if(itemflag==1){
            	 		document.write("(<font color='red'>*</font>)");
            	 		var opt=new Option(label,id,true,true)  
				 		checkselect.options[checkselect.options.length]=opt; 
           	}
            document.write("：</span></td>");
            if (type == 14) { //类型为备注
                document.write("<td class='td1' colspan='3' align='left'><textarea name='" + id1 + "' cols=53 rows=10 maxlength='"+len+"'");
                if (proset == 0) {
                    document.write(" onkeyup=checkinput(this,'" + type + "','" + len + "','" + dec + "') ");
                } else {
                    document.write("style='background-color: #F0F0E8' readonly ");
                }
                document.write(">"+value+"</textarea></td></tr>");
                flag = 1;
                return flag;
            }if (type == 13) { //类型为文本
                document.write("<td class='td1' colspan='3' align='left'><input type='hidden' name='" + id1 + "' id='" + id1 + "' value='"+value+"' maxlength='"+len+"'>");
                document.write("<table border='0' cellspacing='0' width='100%' height='100%' id='contenttable"+id1+"' name='contenttable'><tr><td><script type='text/javascript'>var oFCKeditor = new FCKeditor('content"+id1+"');oFCKeditor.BasePath = '"+jsroot+"/fckeditor2/';oFCKeditor.ToolbarSet = 'FutureDefaultSet';oFCKeditor.Width = '100%';oFCKeditor.Height = '400'; oFCKeditor.Value = '"+document.getElementById(id1).value+"';oFCKeditor.Create();</script></td></tr></table>");
                document.write("</td></tr>");
                flag = 1;
                var opt=new Option(label,id,true,true)  
				textvalue.options[textvalue.options.length]=opt; 
                return flag;
            }  else {
                chooseText1(type, proset, id1, dataid, itemflag,len, dec,value,label);
                flag = 2;
                return flag;
            }
            if(imageflag>-1){
            	 document.write("</tr>");
            }
        } else {
            if (flag == 2) {
                if (type == 14) {//类型为备注
                    document.write("<td colspan='2'></td>");
                    document.write("<tr><td align='right' width='20%' class='biao_bg1' id='label"+id1+"' label='"+label+"'><span class='wz'>" + label);
                    
                    if(itemflag==1){
            	 		document.write("(<font color='red'>*</font>)");
            	 		var opt=new Option(label,id,true,true)  
				 		checkselect.options[checkselect.options.length]=opt; 
           			}
           			document.write("：</span></td>");
                    document.write("<td colspan='3' align='left'><textarea name='" + id1 + "' cols=53 rows=10 maxlength="+len );
                    if (proset == 0) {
                        document.write(" onkeyup='checkinput(this," + type + "," + len + "," + dec + ")' ");
                    } else {
                        document.write("style='background-color: #F0F0E8' readonly ");
                    }
                    document.write(">"+value+"</textarea></td></tr>");
                }else if (type == 13) {//类型为文本
                	document.write("<td colspan='2'></td></tr>");
                	document.write("<tr><td align='right' width='20%' class='biao_bg1' id='label"+id1+"' label='"+label+"'><span class='wz'>" + label);
                	if(itemflag==1){
            	 		document.write("(<font color='red'>*</font>)");
            	 		var opt=new Option(label,id,true,true)  
				 		checkselect.options[checkselect.options.length]=opt; 
           			}
           			document.write("：</span></td>");
                    document.write("<td colspan='3' align='left'><input type='hidden' name='" + id1 + "' id='" + id1 + "' value='"+value+"' maxlength='"+len+"'>");
               		document.write("<table border='0' cellspacing='0' width='100%' height='100%' id='contenttable"+id1+"' name='contenttable'><tr><td><script type='text/javascript'>var oFCKeditor = new FCKeditor('content"+id1+"');oFCKeditor.BasePath = '"+jsroot+"/fckeditor2/';oFCKeditor.ToolbarSet = 'FutureDefaultSet';oFCKeditor.Width = '100%';oFCKeditor.Height = '400'; oFCKeditor.Value = '"+document.getElementById(id1).value+"';oFCKeditor.Create();</script></td></tr></table>");
                	document.write("</td></tr>");
                	var opt=new Option(label,id,true,true)  
				 	textvalue.options[textvalue.options.length]=opt; 
                }else if(type == 11){//类型为图片
                	imagealgin="right";
        			document.write("<td align='right' width='20%' class='biao_bg1' id='label"+id1+"' label='"+label+"' rowspan='6' align='right'>" + label);
        			if(itemflag==1){
            	 		document.write("<font color='red'>*</font>");
            	 		var opt=new Option(label,id,true,true)  
				 		checkselect.options[checkselect.options.length]=opt; 
           			}
           			document.write("</td>");
                    chooseText1(type, proset, id1, dataid, itemflag,len, dec,value,label);
                    document.write("</tr>");
        	   }else {
                    document.write("<td align='right' width='20%' class='biao_bg1' id='label"+id1+"' label='"+label+"'><span class='wz'>" + label);
                    
                    if(itemflag==1){
            	 		document.write("(<font color='red'>*</font>)");
            	 		var opt=new Option(label,id,true,true)  
				 		checkselect.options[checkselect.options.length]=opt; 
           			}
           			document.write("：</span></td>");
                    chooseText1(type, proset, id1, dataid, itemflag,len, dec,value,label);
                    document.write("</tr>");
                }
                flag = 1;
                return flag;
            }
        }
    }
}