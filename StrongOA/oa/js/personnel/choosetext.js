var tryoutstate="0";
var pro_type="";
var num=6;
var k=num;

function checkPhoto(obj){
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

function getImage(obj,type,len){
	doTextFocus(obj)
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

function doSelChange(obj){
	if(obj==null || obj.tagName!="SELECT")
		return;
	var nameobj=obj.parentElement.parentElement.children[0];
	var idobj=document.getElementById(obj.id.substring(0,obj.id.length-11));
	obj.parentElement.parentElement.children[2];
	if(nameobj==null || nameobj.tagName!="INPUT") return;
	nameobj.value=obj.options[obj.selectedIndex].text;
	idobj.value=obj.options[obj.selectedIndex].value;
	//alert(idobj.name+":"+idobj.value)//北京
	if(idobj.name=='PERSON_SALARY_KIND'&&window.parent.document.all.newInfoAdd!=undefined){
	if(idobj.value==9424||idobj.value==9431){
		window.parent.setVisible('T_GS_ADMINDUTY',true);
		window.parent.setVisible('T_GS_TECH_DUTY',false);
		window.parent.setVisible('T_GS_TECHNIC_LEVEL',false);
	}else if(idobj.value==9425||idobj.value==9426||idobj.value==9432||idobj.value==9433||idobj.value==9434){
		window.parent.setVisible('T_GS_ADMINDUTY',false);
		window.parent.setVisible('T_GS_TECH_DUTY',true);
		window.parent.setVisible('T_GS_TECHNIC_LEVEL',true);
	}
	}
	obj.parentElement.style.display="none";
	nameobj.style.display="block";
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

function checkinput(obj,type,len,dec) {
	viewtext_Change();
    var str = obj.value, str1, str2;
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
    }else if(type==14){
    	if(str!=null&&str!=""){
    	    var vlen=obj.value.length;
    		if(vlen*2>len){
    			alert(obj.label+"请不要超过" + len/2+"字");
    			obj.focus();
    		}
    	}
    } 
}


function setTGSJTLX(objId,objName,dictValue){
	viewtext_Change();
	if(document.getElementById('LX')!=undefined){
		var lx=document.getElementById('LX').value;
		if(lx=="")
			alert("请先选择类型！");
		else
			selectJTLXBaseArea(objId,objName,dictValue,lx);
	}
}
						
function showFile(){
   var personPhoto=document.getElementById("file").value;
   var img=document.getElementById("personImg");
   img.src=personPhoto;
   $("#photo").val(personPhoto);
}

//说明：当有照片时，一定得将照片放在第二列，并且和照片同一排的其它信息项不能是文本域，因为文本域要独自占一行
function chooseText(flag, type, proset, id, dataid, itemflag,label, len, dec,value,protype) {
	viewchanged=false;
    var id1 = id;
    var str1;
    var checkselect=document.all.checkvalue;
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
    	document.write("</table></fieldset>");
    	document.write("<table>");
    }
    if(proset == 1) { //不可读
    	document.write("<input type='hidden' name='" + id1 + "' value='"+value+"'>");
        if(flag == 2){
        	//document.write("<input type='hidden' name='" + id1 + "' value='"+value+"'>");
         	flag = 2;
        }else{
        	//document.write("<tr><td colspan='4' align='left'><input type='hidden' name='" + id1 + "' value='"+value+"'></td></tr>");
         	flag = 1;
        }
         return flag;
    }else {    
        if (flag == 1) { //换下一行  
            document.write("<tr><td align='right' width='20%' class='titleTD' id='label"+id1+"' label='"+label+"'>" + label);
            if(itemflag==1){	//必填
            	 document.write("<font color='red'>*</font>");
            	 var opt=new Option(label,id,true,true)  
				 checkselect.options[checkselect.options.length]=opt; 
            }
            document.write("</td>");
            if (type == 14) { //备注、文本域
                document.write("<td colspan='3' align='left'><textarea name='" + id1 + "' cols=65 rows=10 label='"+label+"' maxlength='"+len+"'");
                if (proset == 0) {	//可读
                    document.write(" onkeyup='checkinput(this,\"" + type + "\",\"" + len + "\",\"" + dec + "\")' ");
                } else {			//不可读
                    document.write("style='background-color: #F0F0E8' readonly ");
                }
                document.write(">"+value+"</textarea></td></tr>");
                flag = 1;
            }else {
                chooseText1(type, proset, id1, dataid, itemflag,len, dec,value,protype);
                flag = 2;
            } 
            if(k!=0&&k!=num){//说明有为照片的信息项
                document.write("</tr>");
                k=k-1;
            	flag=1;
            }
            return flag;         
        } else 
            if (flag == 2) {   //继续该行，换下一列
	            if(id == "PERSON_PHOTO"){//为图片，图片放在第二列，并且占k行
	            	 document.write("<td rowspan='"+num+"' align='right' width='20%' class='titleTD' id='label"+id1+"' label='"+label+"'>" + label);
	            	 document.write("</td>");
	            	 document.write("<td rowspan='"+num+"' align='left' width='25%'>");
	            	 document.write("<div>");
	            	 var personId=document.getElementById("personId").value;
	            	 if(personId==null||personId==""){
	            	 	 personId=window.parent.getPersonId();
	            	 }
	                 if(personId!=null&&personId!=""&&personId!="null"){//编辑时从数据库里读取照片
	                 	 document.write("<img src='"+scriptroot+"/personnel/baseperson/person!viewPersonPhoto.action?personId="+personId+"' id='personImg' style='width: 115px; height:135px;align:top;'>");	
	                 }else{
		            	 document.write("<img src='"+scriptroot+"${photo}' id='personImg' style='width: 115px; height:135px;align:top;'>");	
	            	 }	
	            	 document.write("</div>");
	            	 document.write("<div>");        	 
	            	 document.write("<input type='file' name='file' id='file' size=17  maxlength='"+len+"' onchange='showFile();viewtext_Change()'>");
	            	 document.write("<input type='hidden' name='photo' id='photo' value='${photo}'>");
 					 document.write("</div>");      	
	            	 document.write("</td>"); 
	            	 k=k-1;
	            	 flag=1;
	            	 return flag;
	            }else{
	                if (type == 14) {//文本域
	                    document.write("<td colspan='2'></td>");	//结束第二列，换一行
	                    document.write("<tr><td align='right' width='20%' class='titleTD' id='label"+id1+"' label='"+label+"'>" + label);
	                    if(itemflag==1){//必填
	            	 		document.write("<font color='red'>*</font>");
	            	 		var opt=new Option(label,id,true,true)  
					 		checkselect.options[checkselect.options.length]=opt; 
	           			}
	           			document.write("</td>");
	                    document.write("<td colspan='3' align='left'><textarea name='" + id1 + "' cols=65 rows=10 label='"+label+"' maxlength="+len  );
	                    if (proset == 0) {//可读写
	                        document.write(" onkeyup='checkinput(this,\"" + type + "\",\"" + len + "\",\"" + dec + "\")' ");
	                    } else {		//只读
	                        document.write("style='background-color: #F0F0E8' readonly ");
	                    }
	                    document.write(">"+value+"</textarea></td></tr>");
	                } else {
	                    document.write("<td align='right' width='20%' class='titleTD' id='label"+id1+"' label='"+label+"'>" + label);         
	                    if(itemflag==1){//必填
	            	 		document.write("<font color='red'>*</font>");
	            	 		var opt=new Option(label,id,true,true)  
					 		checkselect.options[checkselect.options.length]=opt; 
	           			}
	           			document.write("</td>");
	                    chooseText1(type, proset, id1, dataid, itemflag,len, dec,value,protype);
	                    document.write("</tr>");
	                }
	                flag = 1;	
	                return flag;
	            }
	         }
        }
 }
 
 function chooseText1(type, proset, id1, dataid, itemflag,len, dec, value,protype) {
    document.write("<td align='left' width='25%'>");
    if (type == 0 || type == 2) {//类型为字符或数字
        document.write("<input type='text' id='"+id1+"' name='" + id1 + "' size=30  maxlength='"+len+"' value='"+value+"'" );
        if (proset == 0) {//可读写  	
            document.write(" onkeyup=checkinput(this,'" + type + "','" + len + "','" + dec + "') ");
        } else {
            document.write("style='background-color: #F0F0E8' readonly ");
         
        }
        document.write(">");
    }
    else if (type == 1) {//类型为代码
    	var value1="",value2=value;
    	if(value.indexOf(",")!=-1){
    		var vstr=value.split(",");
    		value1=vstr[0];
    		value2=vstr[1];
    	}
        document.write("<input type='text' readonly id='" + id1 + "_name' name='" + id1 + "_name' size=30  maxlength='"+len+"'  style='background: #D8E5F7' value='"+value2+"'" );
        if(proset==2){ //只读
        	 document.write(" style='background-color: #F0F0E8' readonly ");
        }
        else if (proset == 0) {  //可读写        
            if(id1 == 'RZJB'){
        		document.write("ondblclick=setTGSJTLX('"+id1+"','"+id1+"_name','"+dataid+"');viewtext_Change()");
        	}else{ 
            	document.write("ondblclick=selectBaseData('"+id1+"','"+id1+"_name','"+dataid+"');viewtext_Change()");
            }
        }
        document.write(">");
        document.write("<div style='margin:1px;width:100%; overflow:hidden;display:none'><select id='"+id1+"_dictselect' name='"+id1+"_dictselect' style='margin-left:-3px;margin-top:-2px;margin-bottom:-2px;width:100%;' onblur='doSelChange(this)'></div>");
        document.write("<input type='hidden' id='" + id1 + "' name='" + id1 + "'  maxlength='"+len+"'  value='"+value1+"'>" );
    }
    else if (type == 3){//类型为年
    	document.write("<input type='text' name='" + id1 + "' preset='YEAR' size=30 maxlength='"+len+"' value='"+value+"'" );
        if (proset != 0) {
            document.write("style='background-color: #F0F0E8' readonly ");
        }
        document.write(" onkeyup='viewtext_Change()'>");
    }
    else if(type == 4){//类型为年月
    	document.write("<input type='text' id='"+id1+"' name='" + id1 + "' preset='MONTH' size=30 maxlength='"+len+"' value='"+value+"'" );
        if (proset != 0) {
            document.write("style='background-color: #F0F0E8' readonly ");
        }
        document.write(" onkeyup='viewtext_Change()'>");
    } 
	else if(type == 5) {//类型为年月日
        document.write("<input type='text' name='" + id1 + "' preset='DATE' size=30 maxlength='"+len+"' value='"+value+"'" );
        if (proset != 0) {
            document.write("style='background-color: #F0F0E8' readonly ");
        }
        document.write(" onkeyup='viewtext_Change()'>");
    }else if(type == 12) {//类型为电话
       document.write("<input type='text' id='"+id1+"' name='" + id1 + "' size=30  maxlength='"+len+"' value='"+value+"'" );
        if (proset == 0) {//可读写  	
            document.write(" onblur=checkPhoto(this) ");
        } else {
            document.write("style='background-color: #F0F0E8' readonly ");
         
        }
        document.write(">");
    }else if (type == 10) {//类型为图片
        document.write("<input type='file' name='" + id1 + "' id='"+id1+"' size=20 maxlength='"+len+"'  value='"+value+"'");
        document.write(">");
    }
    document.write("</td>");
}

//说明：当有照片时，一定得将照片放在第二列，并且和照片同一排的其它信息项不能是文本域，因为文本域要独自占一行,所以要注意信息项的排序字段的值
//查看人员方法
function chooseTextForReadOnly(flag, type, proset, id, dataid, itemflag,label, len, dec,value,protype) {
	viewchanged=false;
    var id1 = id;
    var str1;
    var checkselect=document.all.checkvalue;
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
    }
    if (proset == 1) { //不可读
    	document.write("<input type='hidden' name='" + id1 + "' value='"+value+"'>");
        if(flag == 2){
        	//document.write("<input type='hidden' name='" + id1 + "' value='"+value+"'>");
         	flag = 2;
        }else{
        	//document.write("<tr><td colspan='4' align='left'><input type='hidden' name='" + id1 + "' value='"+value+"'></td></tr>");
         	flag = 1;
        }
         return flag;
    } else {
        if (flag == 1) { //换下一行  
            document.write("<tr><td align='right' width='20%' class='titleTD' id='label"+id1+"' label='"+label+"'>" + label);
            if(itemflag==1){	//必填
            	 document.write("<font color='red'>*</font>");
            	 var opt=new Option(label,id,true,true)  
				 checkselect.options[checkselect.options.length]=opt; 
            }
            document.write("</td>");
            if (type == 14) { //备注、文本域
                document.write("<td colspan='3' align='left'><textarea name='" + id1 + "' cols=65 rows=10  label='"+label+"' maxlength='"+len+"'");
                document.write("style='background-color: #F0F0E8' readonly ");
                document.write(">"+value+"</textarea></td></tr>");
                flag = 1;
            }else {
                chooseTextForReadOnly1(type, proset, id1, dataid, itemflag,len, dec,value,protype);
                flag = 2;
            } 
            if(k!=0&&k!=num){//说明有为照片的信息项
                document.write("</tr>");
                k=k-1;
            	flag=1;
            }
            return flag;         
        } else 
            if (flag == 2) {   //继续该行，换下一列
	            if(id == "PERSON_PHOTO"){//为图片，图片放在第二列，并且占k行
	            	 document.write("<td rowspan='"+num+"' align='right' width='20%' class='titleTD' id='label"+id1+"' label='"+label+"'>" + label);
	            	 document.write("</td>");
	            	 document.write("<td rowspan='"+num+"' align='left' width='25%'>");
	            	 document.write("<div>");
	            	 var personId=document.getElementById("personId").value;
	            	 if(personId==null||personId==""){
	            	 	 personId=window.parent.getPersonId();
	            	 }
	                 if(personId!=null&&personId!=""&&personId!="null"){//编辑时从数据库里读取照片
	                 	 document.write("<img src='"+scriptroot+"/personnel/baseperson/person!viewPersonPhoto.action?personId="+personId+"' id='personImg' style='width: 115px; height:135px;align:top;'>");	
	                 }else{
		            	 document.write("<img src='"+scriptroot+"${photo}' id='personImg' style='width: 115px; height:135px;align:top;'>");	
	            	 }	
	            	 document.write("</div>");
	            	 document.write("</td>"); 
	            	 k=k-1;
	            	 flag=1;
	            	 return flag;
	            }else{
	                if (type == 14) {//文本域
	                    document.write("<td colspan='2'></td>");	//结束第二列，换一行
	                    document.write("<tr><td align='right' width='20%' class='titleTD' id='label"+id1+"' label='"+label+"'>" + label);
	                    if(itemflag==1){//必填
	            	 		document.write("<font color='red'>*</font>");
	            	 		var opt=new Option(label,id,true,true)  
					 		checkselect.options[checkselect.options.length]=opt; 
	           			}
	           			document.write("</td>");
	                    document.write("<td colspan='3' align='left'><textarea name='" + id1 + "' cols=65 rows=10 label='"+label+"' maxlength="+len );
	                    document.write("style='background-color: #F0F0E8' readonly ");
	                    document.write(">"+value+"</textarea></td></tr>");
	                } else {
	                    document.write("<td align='right' width='20%' class='titleTD' id='label"+id1+"' label='"+label+"'>" + label);         
	                    if(itemflag==1){//必填
	            	 		document.write("<font color='red'>*</font>");
	            	 		var opt=new Option(label,id,true,true)  
					 		checkselect.options[checkselect.options.length]=opt; 
	           			}
	           			document.write("</td>");
	                    chooseTextForReadOnly1(type, proset, id1, dataid, itemflag,len, dec,value,protype);
	                    document.write("</tr>");
	                }
	                flag = 1;	
	                return flag;
	            }
	         }
        }
 }

function chooseTextForReadOnly1(type, proset, id1, dataid, itemflag,len, dec, value,protype) {
    document.write("<td  class='td1' align='left' width='25%'>");
    if (type == 0 || type == 2||type==12) {//类型为字符或数字
        document.write("<input type='text' id='"+id1+"' name='" + id1 + "' size=30  maxlength='"+len+"' value='"+value+"'" );
        document.write("style='background-color: #F0F0E8' readonly ");
        document.write(">");
    }
    else if (type == 1) {//类型为代码
    	var value1="",value2=value;
    	if(value.indexOf(",")!=-1){
    		var vstr=value.split(",");
    		value1=vstr[0];
    		value2=vstr[1];
    	}
        document.write("<input type='text' readonly id='" + id1 + "_name' name='" + id1 + "_name' size=30  maxlength='"+len+"'  style='background: #D8E5F7' value='"+value2+"'" );
        document.write(" style='background-color: #F0F0E8' readonly ");
        document.write(">");
        document.write("<div style='margin:1px;width:100%; overflow:hidden;display:none'><select id='"+id1+"_dictselect' name='"+id1+"_dictselect' style='margin-left:-3px;margin-top:-2px;margin-bottom:-2px;width:100%;' onblur='doSelChange(this)'></div>");
        document.write("<input type='hidden' id='" + id1 + "' name='" + id1 + "'  maxlength='"+len+"'  value='"+value1+"'>" );
    }
    else if (type == 3){//类型为年
    	document.write("<input type='text' name='" + id1 + "' preset='YEAR' size=30 maxlength='"+len+"' value='"+value+"'" );
        document.write("style='background-color: #F0F0E8' readonly ");
        document.write(" onkeyup='viewtext_Change()'>");
    }
    else if(type == 4){//类型为年月
    	document.write("<input type='text' id='"+id1+"' name='" + id1 + "' preset='MONTH' size=30 maxlength='"+len+"' value='"+value+"'" );
        document.write("style='background-color: #F0F0E8' readonly ");
        document.write(" onkeyup='viewtext_Change()'>");
    } 
	else if(type == 5) {//类型为年月日
        document.write("<input type='text' name='" + id1 + "' preset='DATE' size=30 maxlength='"+len+"' value='"+value+"'" );
        document.write("style='background-color: #F0F0E8' readonly ");
        document.write(" onkeyup='viewtext_Change()'>");
    }
    document.write("</td>");
}

//说明：当有照片时，一定得将照片放在第二列，并且和照片同一排的其它信息项不能是文本域，因为文本域要独自占一行
//人员调配专用方法
function chooseTextForChange(flag, type, proset, id, dataid, itemflag,label, len, dec,value,protype) {
	viewchanged=false;
    var id1 = id;
    var str1;
    var checkselect=document.all.checkvalue;
    var defaultshow=document.getElementById("defaultshow").value;
    if (proset == 1&&id!="ORG_ID") { //不可读
         document.write("<input type='hidden' name='" + id1 + "' value='"+value+"'>");
         if(flag==2){
         	flag = 2;
         }else{
         	 flag = 1;
         }
         return flag;
    } else {	//可读写或只读
        if(defaultshow.indexOf(id)==-1){//需要调配的字段展现格式
        	var personId=document.getElementById("personId").value;
        	document.write("<tr><td align='right' width='20%' height='21' class='biao_bg1' id='label"+id1+"' label='"+label+"'>" + "原"+label);
        	if(itemflag==1){	//必填
            	 document.write("(<font color='red'>*</font>)");	
	        }
	       	document.write("：</td>");
           	if(type=="14"){//为文本域
            	document.write("<td class='td1' align='left' width='25%'>");
            	document.write("<textarea cols=20 rows=5 label='"+label+"' ");
			    if (proset == 0) {	//可读
			        document.write(" onkeyup='checkinput(this,\"" + type + "\",\"" + len + "\",\"" + dec + "\")' ");
			    }else {			//不可读
			        document.write("style='background-color: #F0F0E8' readonly ");
			    }
			    document.write(">"+value+"</textarea>");
			    document.write("</td>");
            }else if(type=="11"){//为照片
         		document.write("<td class='td1' align='left' width='25%'>");
         		document.write("<div>");  
                if(personId!=null&&personId!=""&&personId!="null"){//编辑时从数据库里读取照片
                 	document.write("<img src='"+scriptroot+"/personnel/baseperson/person!viewPersonPhoto.action?personId="+personId+"' style='width: 115px; height:135px;align:top;'>");	
                }else{
	            	document.write("<img src='' style='width: 115px; height:135px;align:top;'>");	
            	}	
		        document.write("</div>");
		        document.write("</td>");
            }else{
         		chooseTextForView(type, proset, id1, dataid, itemflag,len, dec,value,protype);
         	}
         	document.write("<td align='right' width='20%' height='21' class='biao_bg1' id='label"+id1+"' label='"+label+"'>" + "新"+label);    
         	if(itemflag==1){//必填
          		document.write("(<font color='red'>*</font>)");	
          		var opt=new Option(label,id,true,true)  
	 			checkselect.options[checkselect.options.length]=opt; 
        	}
        	document.write("：</td>");
        	if(type=="11"){
        		document.write("<td  align='left' style='background-color: #FFFFFF' width='25%'>");
         		document.write("<div>"); 
         		document.write("<img src='"+scriptroot+"${photo}' id='personImg' style='width: 115px; height:135px;align:top;'>");	
				document.write("</div>");
				document.write("<div>");        	 
		        document.write("<input type='file' name='file' id='file' size=18  maxlength='"+len+"' onchange='showFile();viewtext_Change()' >");
		        document.write("<input type='hidden' name='photo' id='photo' value='${photo}'>");
	 			document.write("</div>"); 
	 			document.write("</td>");
			}else{
				chooseTextForChange1(type, proset, id1, dataid, itemflag,len, dec,value,protype);
			}  		
            document.write("</tr>");
         
        }else{//规定展现的一些字段展现格式
        	if(flag == 1) { //换下一行  
	            document.write("<tr><td align='right' width='20%' height='21' class='biao_bg1' id='label"+id1+"' label='"+label+"'>" + label);
	            if(itemflag==1){	//必填
	            	 document.write("(<font color='red'>*</font>)");    
	            }
	            document.write("：</td>");
	            if (type == 14) { //备注、文本域
	                document.write("<td colspan='3' align='left'><textarea name='" + id1 + "' cols=65 rows=10 label='"+label+"' maxlength='"+len+"'");
	                if (proset == 0) {	//可读
	                    document.write(" onkeyup='checkinput(this,\"" + type + "\",\"" + len + "\",\"" + dec + "\")' ");
	                } else {			//不可读
	                    document.write("style='background-color: #F0F0E8' readonly ");
	                }
	                document.write(">"+value+"</textarea></td></tr>");
	                flag = 1;
	            }else {
	                chooseTextForView(type, proset, id1, dataid, itemflag,len, dec,value,protype);
	                flag = 2;
	            } 
	            if(k!=0&&k!=num){//说明有为照片的信息项
	                document.write("</tr>");
	                k=k-1;
	            	flag=1;
	            }
	            return flag;         
	        } else if (flag == 2) {   //继续该行，换下一列
		            if(id == "PERSON_PHOTO"){//为图片，图片放在第二列，并且占k行
		            	 document.write("<td rowspan='"+num+"' align='right' width='20%' height='21' class='biao_bg1' id='label"+id1+"' label='"+label+"'>" + label);
		            	 document.write("：</td>");
		            	 document.write("<td rowspan='"+num+"' align='left' width='25%'>");
		            	 document.write("<div>");
		            	 var personId=document.getElementById("personId").value;
		            	 if(personId==null||personId==""){
		            	 	 personId=window.parent.getPersonId();
		            	 }
		                 if(personId!=null&&personId!=""&&personId!="null"){//编辑时从数据库里读取照片
		                 	 document.write("<img src='"+scriptroot+"/personnel/baseperson/person!viewPersonPhoto.action?personId="+personId+"' id='personImg' style='width: 115px; height:135px;align:top;'>");	
		                 }else{
			            	 document.write("<img src='"+scriptroot+"${photo}' id='personImg' style='width: 115px; height:135px;align:top;'>");	
		            	 }	
		            	 document.write("</div>");
		            	 document.write("<div>");        	 
		            	 document.write("<input type='file' name='file' id='file' size=17  maxlength='"+len+"' onchange='showFile();viewtext_Change()'>");
		            	 document.write("<input type='hidden' name='photo' id='photo' value='${photo}'>");
	 					 document.write("</div>");      	
		            	 document.write("</td>"); 
		            	 k=k-1;
		            	 flag=1;
		            	 return flag;
		            }else{
		                if (type == 14) {//文本域
		                    document.write("<td colspan='2'></td>");	//结束第二列，换一行
		                    document.write("<tr><td align='right' width='20%' height='21' class='biao_bg1' id='label"+id1+"' label='"+label+"'>" + label);
		                    if(itemflag==1){//必填
		            	 		document.write("(<font color='red'>*</font>)");       
		           			}
		           			document.write("：</td>");
		                    document.write("<td colspan='3' align='left'><textarea name='" + id1 + "' cols=65 rows=10 label='"+label+"' maxlength="+len );
		                    if (proset == 0) {//可读写
		                        document.write(" onkeyup='checkinput(this,\"" + type + "\",\"" + len + "\",\"" + dec + "\")' ");
		                    } else {		//只读
		                        document.write("style='background-color: #F0F0E8' readonly ");
		                    }
		                    document.write(">"+value+"</textarea></td></tr>");
		                } else {
		                    document.write("<td align='right' width='20%' height='21' class='biao_bg1' id='label"+id1+"' label='"+label+"'>" + label);         
		                    if(itemflag==1){//必填
		            	 		document.write("(<font color='red'>*</font>)");
		           			}
		           			document.write("：</td>");
		                    chooseTextForView(type, proset, id1, dataid, itemflag,len, dec,value,protype);
		                    document.write("</tr>");
		                }
		                flag = 1;	
		                return flag;
		            }
		         }
	        }
        }   
 }
 
 //人员调配专用方法
function chooseTextForView(type, proset, id1, dataid, itemflag,len, dec, value,protype) {
    document.write("<td class='td1' align='left' width='25%'>");
    if (type == 1) {//类型为代码
    	var value1="",value2=value;
    	if(value.indexOf(",")!=-1){
    		var vstr=value.split(",");
    		value1=vstr[0];
    		value2=vstr[1];
    	}
    	document.write(value2);
    }else{
    	 document.write(value);
    }
    document.write("</td>");
}
 
 //人员调配专用方法
 function chooseTextForChange1(type, proset, id1, dataid, itemflag,len, dec, value,protype) {
    document.write("<td class='td1' align='left' width='25%'>");
    if (type == 0 || type == 2) {//类型为字符或数字
        document.write("<input type='text' id='"+id1+"' name='" + id1 + "' size=30  maxlength='"+len+"' value=''" );
        if (proset == 0) {//可读写  	
            document.write(" onkeyup=checkinput(this,'" + type + "','" + len + "','" + dec + "') ");
        } else {
            document.write("style='background-color: #F0F0E8' readonly ");
         
        }
        document.write(">");
    }else if (type == 1||type==15) {//类型为代码或机构ID
    	if(id1=="PERSON_IS_OUT_LIMIT"){
       		document.write("<input type='hidden' id='" + id1 + "' name='" + id1 + "'  maxlength='"+len+"' value='"+value1+"'>" );
       	}else{
       		var value1="",value2=value;
	        document.write("<input type='text' readonly id='" + id1 + "_name' name='" + id1 + "_name' size=30  maxlength='"+len+"'  style='background: #D8E5F7' value=''" );
	        if(type==15){
	        	document.write("ondblclick=selectBaseCom('"+id1+"','"+id1+"_name');viewtext_Change()");    
	        }else{
	        	if(proset==0||id1=="PERSON_PERSON_KIND"){ //读写
	        		document.write("ondblclick=selectBaseData('"+id1+"','"+id1+"_name','"+dataid+"');viewtext_Change()");
	        	}else{
	        		document.write(" style='background-color: #F0F0E8' readonly ");
	        	}
	        }	
	        document.write(">");
	        document.write("<div style='margin:1px;width:100%; overflow:hidden;display:none'><select id='"+id1+"_dictselect' name='"+id1+"_dictselect' style='margin-left:-3px;margin-top:-2px;margin-bottom:-2px;width:100%;' onblur='doSelChange(this)'></div>");
	       	document.write("<input type='hidden' id='" + id1 + "' name='" + id1 + "'  maxlength='"+len+"'  value=''>" );
       	}
    }else if (type == 3){//类型为年
    	document.write("<input type='text' name='" + id1 + "' preset='YEAR' size=30 maxlength='"+len+"' value=''" );
        if (proset != 0) {
            document.write("style='background-color: #F0F0E8' readonly ");
        }
        document.write(" onkeyup='viewtext_Change()'>");
    }else if(type == 4){//类型为年月
    	document.write("<input type='text' id='"+id1+"' name='" + id1 + "' preset='MONTH' size=30 maxlength='"+len+"' value=''" );
        if (proset != 0) {
            document.write("style='background-color: #F0F0E8' readonly ");
        }
        document.write(" onkeyup='viewtext_Change()'>");
    }else if(type == 5) {//类型为年月日
        document.write("<input type='text' name='" + id1 + "' preset='DATE' size=30 maxlength='"+len+"' value=''" );
        if (proset != 0) {
            document.write("style='background-color: #F0F0E8' readonly ");
        }
        document.write(" onkeyup='viewtext_Change()'>");
    }else if (type == 14) { //备注、文本域
       document.write("<textarea name='" + id1 + "' cols='20' rows='5' label='"+label+"' maxlength='"+len+"'");
       if (proset == 0) {	//可读
           document.write(" onkeyup='checkinput(this,\"" + type + "\",\"" + len + "\",\"" + dec + "\")' ");
       } else {			//不可读
           document.write("style='background-color: #F0F0E8' readonly='readonly' ");
       }
       document.write("></textarea>");
    }else if(type == 12) {//类型为电话
       document.write("<input type='text' id='"+id1+"' name='" + id1 + "' size=30  maxlength='"+len+"' value='"+value+"'" );
        if (proset == 0) {//可读写  	
            document.write(" onblur=checkPhoto(this) ");
        } else {
            document.write("style='background-color: #F0F0E8' readonly ");
         
        }
        document.write(">");
    }
    document.write("</td>");
}


