<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/include/nrootPath.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>单位通知上报功能</title>
		<link rel="stylesheet" type="text/css"
			href="<%=themePath%>/css/global.css" />
		<link rel="stylesheet" type="text/css"
			href="<%=themePath%>/css/component.css" />
		<link rel="stylesheet" type="text/css"
			href="<%=themePath%>/css/dxxk.css" />

		<script type="text/javascript"
			src="<%=request.getContextPath()%>/scripts/easyui-1.3/jquery.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/scripts/easyui-1.3/jquery.easyui.min.js"></script>
		<script language="javascript"
			src="<%=path%>/common/js/common/common.js"></script>
		<!--右键菜单脚本 -->

		<script language="javascript"
			src="<%=path%>/common/js/upload/jquery.MultiFile.js"></script>
		<script src="<%=path%>/common/js/upload/jquery.blockUI.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/btn.js" type="text/javascript"></script>


		<style type="text/css">
html {
	-webkit-box-sizing: border-box;
	-moz-box-sizing: border-box;
	box-sizing: border-box;
	padding: 40px 0px 40px 0px;
	overflow: hidden;
}

html,body {
	font-size: 12px;
	width: 100%;
	height: 100%;
	overflow: hidden;
}

* {
	margin: 0;
	padding: 0;
}

.dmalb {
	padding: 0px 0px 0;
	font-size: 12px;
	height: 160px;
	overflow: auto;
}

.lbtab {
	border-collapse: collapse;
	border-spacing: 0;
	background-color: #fff;
}

.lbtab th {
	background: url(<%=root%>/frame/theme_gray_12/images/biao_bg3.jpg)
		repeat-x left top;
	font-size: 13px;
}

.lbtab th,.lbtab td {
	padding: 5px;
	border: 1px solid #d4d0c8;
	text-align: center;
}

.lbtab td input {
	background: #fff;
}

.inputbutd {
	border-top: 1px solid #ccc;
	text-align: center;
	height: 20px;
	padding: 6px 0;
	position: absolute;
	bottom: 0;
	width: 100%;
	background: #fff;
}

#nobr br {
	display: none;
}

.information_list_choose_pagedownnew {
	position: absolute;
	width: 100%;
	height: 80px;
	text-align: right;
	left: 0;
	right: 0;
	bottom: 0;
	overflow: hidden;
	background: url(../image/wzfbbbg.gif) repeat-x center bottom;
}

.information_list span {
	display: inline;
	float: none;
	text-align: left;
	padding-right: 0px;
}

.information_list {
	border-collapse: collapse;
	border-spacing: 0;
}

.pnlSuggest {
	border: #000000 1px solid;
	background-color: #FFFFFF;
	z-index: 9527;
	position: absolute;
	overflow-y: auto;
	overflow-x: hidden;
	text-overflow: clip;
}

.pnlSuggest table {
	width: 100%;
}

.pnlSuggest tr {
	width: 100%;
}

.trmouseover {
	width: 100%;
	background-color: #397CC3;
}

.trmouseover td {
	text-align: left;
	overflow: hidden;
	text-overflow: clip;
	background-color: #397CC3;
}

.trmouseout {
	width: 100%;
	background-color: #FFFFFF;
}

.trmouseout td {
	text-align: left;
	overflow: hidden;
	text-overflow: clip;
	background-color: #FFFFFF;
}

.txtValues {
	display: none;
}

.dataSource {
	display: none;
}

.hiddentd {
	display: none;
}

.hiddenValues {
	display: none;
}
</style>
		<script language="javascript" type="text/javascript">
		var   message_user_telephone="请输入正确的电话号码(电话或者手机).";
//为string对象添加一个清除前后空格的属性
String.prototype.trim = function()
{
     return this.replace(new RegExp("(^[\\s]*)|([\\s]*$)", "g"), "");
};
Array.prototype.contains = function (elem) {
	for (var i = 0; i < this.length; i++) {
		if (this[i] == elem) {
			return true;
		}
	}
	return false;
}
 

function add(){
   var html=appendHtml();
   $("#userInfo").append(html);
}
function appendHtml(){
  var html="<tr><td>";
  html+="<input type=\"text\"   id=\"txtInput11\" name=\"personName\" onkeydown=\"ShowSuggest(this);\" style=\"width: 100px\" autocomplete=\"off\" />	";
  html+=" <h6 class=\"hiddenValues\" style=\"display: none;\"></h6>";
  html+="</td>"
  
  html+="<td>";
  html+="<input type=\"text\" name=\"personPost\" id=\"zw\" />";
  html+="</td>";
  
   html+="<td>";
 // html+="<input type=\"text\" name=\"personSax\" id=\"xb\" style=\"width: 50px\" />";
   html+="<select name=\"personSax\" id=\"xb\" style=\"width: 50px\" > <option value=\"男\">男</option> <option value=\"女\">女</option></select>";
  html+="</td>";
  
  html+="<td>";
  html+="<input type=\"text\" name=\"personNation\" id=\"mz\" style=\"width: 50px\"/>";
  html+="</td>";
  
  html+="<td>";
  html+="<input type=\"text\" name=\"personMobile\" id=\"tel\"/>";
  html+="</td>";
  
  html+="<td>";
  html+="<input type=\"text\" name=\"personPhone\" id=\"mobail\" />";
  html+="</td>";
  
  html+="<td >";
  html+="<a href=# onclick='deltr(this)' style='color: blue;font-size: 15px'>删除</a>&nbsp;";
  
  html+="</td>";
  
  html+="</tr>";
  return html;
}

function deltr(obj){
  $(obj).parent().parent().remove();
}

//显示下拉信息
function ShowSuggest(objInputText)
{
    // objInputText.onkeyup = ControlSuggest;
      objInputText.onFocus = ControlSuggest;
      objInputText.onkeyup = ControlSuggest;
     objInputText.onblur = RemoveSuggest;

     var oldValue = objInputText.parentElement.getElementsByTagName("h6")[0];
     //对提示框的控制
     function ControlSuggest()
     {
         var ie = (document.all)?true:false;
         if(ie)
         {
             var keycode = event.keyCode;
            var txtvalues = objInputText.value.trim().split(";");
            
            if( !CheckSuggest() && txtvalues[txtvalues.length-1] != oldValue.innerText.trim())
            {
                CreateSuggest();
                return ;
            }
            
            if(keycode == 32 && txtvalues[txtvalues.length-1] != oldValue.innerText.trim())
            {
                CreateSuggest();
                return ;
            }
            
            //按向下创建提示
            if(!CheckSuggest() && txtvalues[txtvalues.length-1].length !=0 && keycode == 40)
            {//文本框有内容,提示不存在,向下
                CreateSuggest();
                return;
            }
            
           //当删除的时候，参量要初始化
            if(keycode == 8 && txtvalues[txtvalues.length-1].length == 0)
            {
                DeleteSuggest();
                oldValue.innerText = "";
                return ;
            }
            
            if(CheckSuggest())
            {
                var inputIndex = document.getElementById("inputIndex");
                var selectIndex = Number(inputIndex.value);
                
                //排除上下控制的值操作外，其他任何值改变都要去创建提示框
                if( selectIndex < 0 && txtvalues[txtvalues.length-1] != oldValue.innerText)
                {
                    CreateSuggest();
                    return ;
                }
                
                if(keycode == 40)
                {//向下
                    ChangeSelection(false);
                    return ;
                }

               if(keycode == 38)
                {//向上
                    ChangeSelection(true);
                    return ;
                }
                
                if(keycode == 46 || keycode == 27)
                {//del
                    DeleteSuggest();
                    oldValue.innerText = "";
                    return ;
                }
                
                var panelSuggest = document.getElementById("divSuggestPanel");
                var tb = panelSuggest.getElementsByTagName("table")[0];

                if(keycode == 13)
                {//回车
                    if(selectIndex > -1 && txtvalues[txtvalues.length-1] != oldValue.innerText)
                    {
                        CreateSuggest();
                        return ;
                    }
                    RemoveSuggest();
                    return ;
                }

            }
            
            if(txtvalues[txtvalues.length-1] != oldValue.innerText)
            {//当上面的条件都筛选后，只要值发生改变就创建下拉提示
                CreateSuggest();
                return ;
            }
        }
    }
    
    //删除提示前对文本做相关操作
    function RemoveSuggest()
    {
        if(CheckSuggest())
        {
            var panelSuggest = document.getElementById("divSuggestPanel");
            var inputIndex = document.getElementById("inputIndex");
            
            var txtvalues = objInputText.value.trim().split(";");
            
            if( CheckActiveElement(panelSuggest) || event.keyCode == 13)
            {
                //做个判断，判断当前活动对象 是不是TD,是的话就执行下面的操作，不是的话就不做操作，或者把文本框作为当前活动
                if(CheckActiveElement(panelSuggest) && document.activeElement.tagName != "TD")
                {
                    objInputText.focus();
                    return ;
                }

                //得到选定的值
                var selectIndex = Number(inputIndex.value);
                if(selectIndex >= 0)
                {
                    objInputText.value ="";
                    var tb = panelSuggest.getElementsByTagName("table")[0];
                    txtvalues[txtvalues.length-1] = tb.rows[selectIndex].cells[0].innerHTML;
                    $txtVal=$(txtvalues);
                    var selMsgs=$($txtVal[0]).val();
                    var msgs=selMsgs.split("#");
                    objInputText.value =msgs[0];
                    $tb=$(objInputText).parent().siblings('td');
                    $tb.find('#zw').val(msgs[1]);
                    $tb.find('#xb').val(msgs[2]);
                    $tb.find('#mz').val(msgs[3]);
                    $tb.find('#tel').val(msgs[4]);
                }
            }
            
            document.body.removeChild(inputIndex);
            document.body.removeChild(panelSuggest);
            oldValue.innerText = "";
        }
        else
        {
            return ;
        }
    }

    //删除提示的方法，不对文本做任何操作
    function DeleteSuggest()
    {
        if(CheckSuggest())
        {
            var panelSuggest = document.getElementById("divSuggestPanel");
            var inputIndex = document.getElementById("inputIndex");
            document.body.removeChild(inputIndex);
            document.body.removeChild(panelSuggest);
        }
    }

    //加载提示框
    function CreateSuggest()
    {
        var txtvalues = objInputText.value.trim().split(";");
        
        //提示框存在，而且文本框值与上次的输入不同时，才进行下面的加载工作
        if(CheckSuggest())
        {
            if( oldValue.innerText.trim() == txtvalues[txtvalues.length-1].trim())
            {
                return ;
            }
            else
            {
                DeleteSuggest();
            }
        }
        
        if(CheckSuggest() && txtvalues[txtvalues.length-1].trim().length ==0)
        {//提示框存在,但是文本框没有内容,这时删除提示框
            DeleteSuggest();
            oldValue.innerText = "";
            return ;
        }
        
        //如果输入为空格，就退出
        if(txtvalues[txtvalues.length-1].trim().length == 0)
        {
            return ;
        } 
        
        //从数据源中取数据
        var suggestList = GetSuggestList();
        
        if(suggestList == null||suggestList.length < 1)
        {//对传入的数组进行判断，为空或者列表为0就退出
            DeleteSuggest();                                  //开始的输入有提示，后面的输入可能没有提示，所以数据源为空时要尝试删除提示
            oldValue.innerText = "";
            return ;
        }
        
        oldValue.innerText = txtvalues[txtvalues.length-1];              //以上条件都符合，根据数据源来创建数据
        
        var inputIndex = document.createElement("input");     //用隐藏控件来做索引的保存
        inputIndex.type = "hidden";
        inputIndex.id = "inputIndex";
        inputIndex.value = -1;

        var suggest = "";                                     //根据数据源来写div提示信息
        suggest += "<table>";
        for(var nIndex = 0; nIndex < suggestList.length; nIndex++)
        {
            suggest += "<tr onmouseover=\" for(var n=0;n<this.parentElement.rows.length;n++){this.parentElement.rows[n].className='trmouseout';};this.className='trmouseover';var inputIndex = document.getElementById('inputIndex');inputIndex.value = this.rowIndex; \" onmouseout=\"this.className='trmouseout';\"  >";
            suggest += suggestList[nIndex];
            suggest += "</tr>";
        }
        suggest += "</table>";
        
        var panelSuggest = document.createElement("div");                //创建装提示框的容器div

        panelSuggest.id = "divSuggestPanel";
        panelSuggest.className = "pnlSuggest";                           //设置对象的类
        panelSuggest.style.width = objInputText.clientWidth + "px";      //设置对象的宽度，与文本框宽度相同
        panelSuggest.style.top = (GetPosition()[0] + objInputText.offsetHeight + 1) + "px";
        panelSuggest.style.left = GetPosition()[1] + "px";
        panelSuggest.innerHTML = suggest;

        document.body.appendChild(panelSuggest);                         //把提示框和索引控件添加进来
        document.body.appendChild(inputIndex);

        //判断显示条数的多少,多于10条就用滚动条操作
        if(suggestList.length > 10)
        {
            var h = panelSuggest.getElementsByTagName("tr")[1].offsetHeight;
            panelSuggest.style.height = (h * 10) + "px";
            panelSuggest.style.width = (objInputText.clientWidth + 20) + "px";
        }
        
    }

    //更换选项
    function ChangeSelection(isup)
    {

        if(CheckSuggest())
        {
            var txtvalues = objInputText.value.trim().split(";");

            var inputIndex = document.getElementById("inputIndex");                 //得到索引的值
            var selectIndex = Number(inputIndex.value);
            
            var panelSuggest = document.getElementById("divSuggestPanel");          //得到提示框
            var tb = panelSuggest.getElementsByTagName("table")[0];
            var maxIndex = tb.rows.length - 1;                                      //提示信息的最大索引

            if(isup)
            {//向上
                if(selectIndex >= 0)                                                //索引不能为负
                {
                    tb.rows[selectIndex].className = "trmouseout";
                    selectIndex--;
                    if(selectIndex >= 0)
                    {
                        tb.rows[selectIndex].className = "trmouseover";
                    }
                }
            }
            else
            {
                if(selectIndex < maxIndex)                                          //大于等于最大索引就不做任何操作
                {
                    if(selectIndex >= 0)
                    {
                        tb.rows[selectIndex].className = "trmouseout";
                    }
                    selectIndex++;
                    tb.rows[selectIndex].className = "trmouseover";
                }
            }

            inputIndex.value = selectIndex;
            //控制滚动条的上下
            if(selectIndex >= 0)
            {
                if(tb.rows[selectIndex].offsetTop < panelSuggest.scrollTop)
                {
                    panelSuggest.scrollTop = tb.rows[selectIndex].offsetTop;
                }
                if(tb.rows[selectIndex].offsetTop + tb.rows[selectIndex].offsetHeight > panelSuggest.scrollTop + panelSuggest.offsetHeight)
                {
                    panelSuggest.scrollTop = tb.rows[selectIndex].offsetTop + tb.rows[selectIndex].offsetHeight - panelSuggest.offsetHeight;
                }
            }
            
        }

    }
    
    //判断活动对象是否为obj对象的从属对象
    function CheckActiveElement(obj)
    {
        var isAe = false;
        var objtemp = document.activeElement;
        while(objtemp != null)
        {
            if(objtemp == obj)
            {
                isAe = true;
                break;
            }
            objtemp = objtemp.parentElement;
        }
        return isAe;
    }
    
    //检查提示框是否存在
    function CheckSuggest()
    {
        var panelSuggest = document.getElementById("divSuggestPanel");
        if(panelSuggest == null)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    
    //获取文本框的位置
    function GetPosition()
    {
        var top = 0,left = 0;
        var obj = objInputText;
        do 
        {
            top += obj.offsetTop;         //距离顶部
            left += obj.offsetLeft;       //距离左边
        }
        while (obj = obj.offsetParent);
        
        var arr = new Array();
        arr[0] = top;
        arr[1] = left;
        return arr; 
    }
    
    //得到提示数据
    function GetSuggestList()
    {
       var suggestlist=new Array();
       var strValues="${hval}";
       if(strValues!=''){
       var vals=strValues.split("@");
           for(var i=0;i<vals.length;i++){              
              suggestlist.push(vals[i]);
           }
       } 
       /* for(var i=0;i<12;i++){
       	   var tb="<td>sfxlf"+i+" <input type='hidden'  id='name' name='hvalue'  style='width: 320px'  value='wanjunlong#zhid#1#汉族#23456' /></td>";       	
       	        suggestlist.push(tb);
       }*/
       return suggestlist;


    }
    
    //得到文本框的显示值
    function GetValues(values)
    {
        var txtvalue="";
        for(var n=0;n<values.length;n++)
        {
            if(values[n].trim().length==0)
            {
                continue;
            }
            txtvalue+=values[n]+";";
        }      
      
        return txtvalue;
    }
    
    function setValues(values){
       var strValues=values.split("#");
       $obj=$(objInputText);
    }
};

 
function selLeaderMember(){              
		 
		var url="<%=root%>/noticeconference/clientConference!selMemberForReport.action?initleader="+$("#initleader").val();
		var ret=OpenWindow(url,"700","400",window);
		if(ret != null && ret!=''){
			 var infos=ret.split('@');
			 for(var i=0;i<infos.length;i++){
			    if(infos[i]!=''){
			         var initleader=$("#initleader").val();
			         delAllLeaderForReLoad(initleader);//清空客户端 领导选择项
			         var itemDails=infos[i].split('#');
			         var html=selLeaderToHtml(itemDails[0],itemDails[1],itemDails[2],itemDails[3].trim(),itemDails[4]);
			         var length=$("#userInfo tr").length;
			       
			     
			         initleader+=","+itemDails[0];
			         $("#initleader").val(initleader);			         
			         
			         if(length<=1){
			            $("#userInfo").prepend(html);
			         }else{
			            $("#userInfo tr:eq("+(length-1)+")").parent().prepend(html);
			         }			      
			    }
			 }
			
		}

}

function selLeaderToHtml(id,name,zw,xb,mz,tel,mobil){
  var html="<tr id='"+id+"'><td>";
  html+="<input type=\"text\"   id=\"txtInput11\" name=\"personName\" onkeydown=\"ShowSuggest(this);\" style=\"width: 100px\" autocomplete=\"off\" value=\""+name+"\"/>	";
  html+=" <h6 class=\"hiddenValues\" style=\"display: none;\"></h6>";
  html+="</td>"
  
  html+="<td>";
  html+="<input type=\"text\" name=\"personPost\" id=\"zw\" value=\""+zw+"\"/>	";
  html+="</td>";
  
   html+="<td>";
 // html+="<input type=\"text\" name=\"personSax\" id=\"xb\" style=\"width: 50px\" />";
  html+="<select name=\"personSax\" id=\"xb\" style=\"width: 50px\" >";
  if(xb=="男"){
      html+="<option value=\"男\" selected>男</option> <option value=\"女\">女</option>";
  }else{
      html+="<option value=\"男\" >男</option> <option value=\"女\" selected>女</option>";
  }
  html+="</select>";
  
  html+="</td>";
  
  html+="<td>";
  if(mz!=undefined){
      html+="<input type=\"text\" name=\"personNation\" id=\"mz\" style=\"width: 50px\" value=\""+mz+"\"/>	";
  }else{
      html+="<input type=\"text\" name=\"personNation\" id=\"mz\" style=\"width: 50px\" />	";
  }
  html+="</td>";
  
  html+="<td>";
  
  if(tel!=undefined){
      html+="<input type=\"text\" name=\"personMobile\" id=\"tel\"   value=\""+tel+"\"/>	";
  }else{
      html+="<input type=\"text\" name=\"personMobile\" id=\"tel\"  />	";
  }
  
  html+="</td>";
  
  html+="<td>";
  
  if(mobil!=undefined){
     html+="<input type=\"text\" name=\"personPhone\" id=\"mobail\" value=\""+mobil+"\"/>	";
  }else{
     html+="<input type=\"text\" name=\"personPhone\" id=\"mobail\"/>	";
  }
 
  html+="</td>";
  
  html+="<td >";
  html+="<a href=# onclick='delLeaderTr(\""+id+"\")' style='color: blue;font-size: 15px'>删除</a>&nbsp;";
  
  html+="</td>";
  
  html+="</tr>";
  return html;
}

function delAllLeaderForReLoad(initleader){
   if( initleader!=''){
     var leaders=initleader.split(',');
     for(var i=0;i<leaders.length;i++){
      //  delLeaderTr(leaders[i]);
     }
   } 
}

function delLeaderTr(id){  
  $("#"+id).remove(); 
  var initleader=$("#initleader").val();
  var filterLeader="";
  if( initleader!=''){
     var leaders=initleader.split(',');
     for(var i=0;i<leaders.length;i++){
        if(id!=leaders[i]){
           filterLeader+=leaders[i]+",";
        }
     }
  } 
  $("#initleader").val(filterLeader);
  
}

function checkMobile(str){ 
    if(!(/^1[3|5][0-9]\d{4,8}$/.test(str))){ 
        return false; 
    } 
} 

function checkPhone(str){ 
    if(!(/^\d+$/.test(str))){ 
        return false; 
    } 
} 

function validate(){
  var flag=false;
  var pName= 0;
  var pPost= 0;
  var pNation= 0;
  var pMobile= 0;
  var pPhone= 0;
  var cMobile = true;
  var cPhone = true;
  var sameFlag=false;
  $tr=$("#userInfo tr ");
  if($tr.length<1){
     alert("请添加上报人员！！");
     $("#save").attr("disabled",false);
     return false;
  }
  var names=new Array();
   $tr.each(function(i){
       var personName=$(this).find("input[name='personName']").val();
       var personPost=$(this).find("input[name='personPost']").val();
       var personNation=$(this).find("input[name='personNation']").val();
       var personMobile=$(this).find("input[name='personMobile']").val();
       var personPhone=$(this).find("input[name='personPhone']").val();
       if(personName!="undefined" && personName!=''){
           flag=true;
       }
       else{
    	   flag=false;
    	   return false; 
       }
       if(personName.length>50){
    	   pName= 1;
    	   return false;
       }
       if(personPost=="undefined" || personPost==''){
    	   pPost= 2;
    	   return false;
       }
       if(personPost.length>50){
    	   pPost= 1;
    	   return false;
       }
       if(personNation=="undefined" || personNation==''){
    	   pNation= 2;
    	   return false;
       }
       if(personNation.length>50){
    	   pNation= 1;
    	   return false;
       }
       if(personMobile.length>50){
    	   pMobile= 1;
    	   return false;
       }
       if(personMobile!=""){
    	   cMobile =  checkMobile(personMobile);
       }
       if(personPhone.length>50){
    	   pPhone= 1;
    	   return false;
       }
       if((personPhone.length!=0)&&(personPhone.length<4)){
    	   pPhone= 2;
    	   return false;
       }
       if(personPhone!=""){
    	   cPhone = checkPhone(personPhone);
       }
       var items=$(this).find("input[name='personName']").val()+"|"+$(this).find("input[name='personPost']").val()+"|"+$(this).find("select[name='personSax']").val();
        if(names.contains(items)){	        
	         sameFlag=true;
	         return;
	      } 
	   names.push(items);
       
     
  });
  if(!flag){     
	  alert("姓名不能为空！！");
    $("#save").attr("disabled",false);
     return false;
  }
  //判断姓名
  if(pName==1){
	     alert("姓名长度超过50！");
	    $("#save").attr("disabled",false);
	     return false;
	  }
  //判断职务
  if(pPost===2){
	     alert("职务不能为空！");
	    $("#save").attr("disabled",false);
	     return false;
	  }
  if(pPost===1){
	     alert("职务长度不能超过50！");
	    $("#save").attr("disabled",false);
	     return false;
	  }
  
//判断民族
  if(pNation==2){
	     alert("民族不能为空！");
	    $("#save").attr("disabled",false);
	     return false;
	  }
  if(pNation==1){
	     alert("民族长度不能超过50！");
	    $("#save").attr("disabled",false);
	     return false;
	  }
	  
//判断手机号
  if(cMobile==false){
	   alert("手机号不合法！"); 
	   $("#save").attr("disabled",false);
	   return false;
  }
  if(pMobile){
	     alert("手机号长度不能超过50！");
	    $("#save").attr("disabled",false);
	     return false;
	  }
  
//判断办公电话
   if(cPhone==false){
		  alert("办公电话不合法"); 
		  $("#save").attr("disabled",false);
			return false;
		}
  if(pPhone==1){
	     alert("办公电话长度不能超过50！！");
	    $("#save").attr("disabled",false);
	     return false;
	  }
  if(pPhone==2){
	     alert("办公电话最少为4位！！");
	    $("#save").attr("disabled",false);
	     return false;
	  }  

  
  if(sameFlag){   
    alert("存在相同上报人员,请确认！");
      $("#save").attr("disabled",false); 
    return false;
  }
  return true;

}

$(function(){
   //$("#userInfo").prepend('${initdatas}');
   $("#userInfo").html('${initdatas}');
   $("#save").click(function(){
        $(this).attr("disabled","true");
        $('#myform').form('submit',{
						   url: '<%=root%>/noticeconference/clientConference!saveToApply.action',
						   onSubmit: function(){
							 return validate();
						},
						success: function(data){
						   	 // alert("会议通知草稿保存成功！！！");  
						   	  window.close();
							  window.dialogArguments.callback(); 
						}						
			 });  
     });
});

</script>
	</head>
	<base target="_self" />
	<body>
		<div class="information_top">
			<div class="windows_title">
				会议通知报名
				<div class="information_list_choose_pagedownnew" align="right">
					<tr align="right">
						<td align="right" width="10%">
							<input type="button" class="information_list_choose_button9"
								value="提交" id="save" />

						</td>
					</tr>
				</div>
			</div>

		</div>
		<div class="information_out" id="information_out">


			<s:form id="myform" name="myform" theme="simple"
				enctype="multipart/form-data">
				<input type="hidden" name="depId" value="${model.deptCode}" />
				<input type="hidden" name="ids" value="${ids }" />
			 
				<input type="hidden" name="initleader" id="initleader" value="${initleader }"/>
				<table width="100%" border="0" cellspacing="0" cellpadding="0"
					style="vertical-align: top;">

					<td height="90%">
						<table style="width: 99%" border="1" cellspacing="0"
							cellpadding="0">
							<tr>
								<td
									style="FILTER: progid :             DXImageTransform .             Microsoft .             Gradient(gradientType =             0, startColorStr =             #ededed, endColorStr =             #ffffff);">
									<table width="100%" border="0" cellspacing="0" cellpadding="0"
										style="vertical-align: top;">

										<tr>
										<tr>
											<td height="100%">
												<DIV class=tab-pane id=tabParent style="width: 100%">
													<DIV class=tab-page id=tabPage1 style="width: 100%">
														<table class="information_list"
															style="width: 99%; height: 100px;" cellspacing="0"
															cellpadding="0" style="vertical-align: top;">
															<tr>
																<td align="right">
																	会议标题:
																</td>
																<td class="dxktslx">
																	${model.TOmConference.conferenceTitle}
																</td>

																<td align="right">
																	会议类型:
																</td>
																<td class="dxktslx">
																	${sendType}
																</td>
															</tr>
															<tr>
																<td align="right">
																	会议地址:
																</td>
																<td class="dxktslx">
																	${model.TOmConference.conferenceAddr}
																</td>

																<td align="right">
																	报名截至时间
																</td>
																<td class="dxktslx">
																 <s:date name="model.TOmConference.conferenceRegendtime" format="yyyy-MM-dd" />
																 
																</td>
															</tr>

															<tr>
																<td align="right">
																	会议开始时间:
																</td>
																<td class="dxktslx">
																	 
 <s:date name="model.TOmConference.conferenceStime" format="yyyy-MM-dd" />
																</td>

																<td align="right">
																	会议结束时间:

																</td>
																<td class="dxktslx">
																	 
 <s:date name="model.TOmConference.conferenceEtime" format="yyyy-MM-dd" />
																</td>
															</tr>
															<tr>
																<td align="right">
																	联 系 人:
																</td>
																<td class="dxktslx">
																${model.TOmConference.conferenceUser}
																</td>
																
																<td align="right">
																	联系电话:
																</td>
																<td class="dxktslx">
																${model.TOmConference.conferenceUsertel}
																</td>
															</tr>
														</table>
														<br>


														<table width="90%" border="0" cellpadding="0"
															cellspacing="1" align="center">
															<tr>
																<td>
																	<input type="button"
																		class="information_list_choose_button9" id="addMember"
																		value="新增上报人员" onclick="add();" />																  
																	<!--  <input type="button"
																		class="information_list_choose_button9" id="selMember"
																		value="选择领导名册" onclick="selLeaderMember();" />-->

																</td>
															</tr>
															<tr>
																<td>
																	<div class="dmalb">
																		<table width="100%" border="0" class="lbtab"
																			width="99%" cellpadding="0" cellspacing="0"
																			align="center">
																			<thead>
																				<tr>
																					<th width="88">
																						姓名
																					</th>
																					<th width="25%">
																						职务
																					</th>
																					<th width="70">
																						性别
																					</th>
																					<th width="70">
																						民族
																					</th>
																					<th width="100">
																						手机号
																					</th>
																					<th>
																						办公电话
																					</th>
																					<th width="88">
																						操作
																					</th>
																				</tr>
																			</thead>
																			<tbody id="userInfo">
																				<tr>
																					<td>
																						<input type="text" id="txtInput11"
																							name="personName" onkeydown="ShowSuggest(this);"
																							style="width: 100px" autocomplete="off" />
																						<h6 class="hiddenValues" style="display: none;"></h6>
																					</td>
																					<td>
																						<input type='text' name="personPost" id="zw" />
																					</td>
																					<td>
																						 
																						<select name="personSax" id="xb"
																							style="width: 50px" >
																							 <option value="男">男</option>
																							 <option value="女">女</option>
																							</select>
																					</td>
																					<td>
																						<input type='text' name="personNation" id="mz"
																							style="width: 50px" />
																					</td>
																					<td>
																						<input type='text' name="personMobile" id="tel" />
																					</td>
																					<td>
																						<input type='text' name="personPhone" id="mobail" />
																					</td>
																					<td style="color: blue">
																						<a href=# onclick='deltr(this)'
																							style='color: blue; font-size: 15px'>删除</a>&nbsp;
																					</td>

																				</tr>
																			</tbody>
																		</table>
																	</div>

																</td>
															</tr>
															<!--  <tr>
												<td>
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 备注：&nbsp;&nbsp;&nbsp;
													&nbsp;
													<input type='text' name="name" id="name"
														style="width: 70%;" />
												</td>
											</tr>-->
														</table>
														<br>
														<s:if test="%{attachHtml}"></s:if>

														<table class="information_list"
															style="width: 99%; height: 100px;" cellspacing="0"
															cellpadding="0" style="vertical-align: bottom;">
															<tr>

																<td valign="middle" align="right" width="15%">
																	会议材料：
																</td>
																<td colspan="3"
																	style="font-size: 12px; font-weight: bolder;">
																	<div style="float: left;">
																		<s:iterator value="attachs">
																		<s:if test="%{attachs!=null}">
																		<img src="<%=path%>/oa/image/mymail/yes.gif">
																		<a href="#" onclick="down('<s:property value="attachId"/>')"><font color="blue"><s:property value="attachFileName"/></font></a>
																		</s:if>
																		</s:iterator>
																	</div>

																</td>

															</tr>
														</table>

													</DIV>
												</DIV>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
				</table>

			</s:form>
		</div>
	</body>
</html>
