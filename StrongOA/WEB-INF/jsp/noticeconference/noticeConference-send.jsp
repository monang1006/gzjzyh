<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@taglib uri="/tags/web-newdate" prefix="strong"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<jsp:directive.page import="com.strongit.oa.util.GlobalBaseData" />
<%@taglib uri="/tags/web-remind" prefix="stron"%>
<%--<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">--%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>单位通知上报功能</title>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type="text/css "/>

		<link href="<%=frameroot%>/css/properties_windows.css" type="text/css"
			rel=stylesheet>
		<script language='javascript'
			src='<%=path%>/common/js/jquery/jquery-1.2.6.js'></script>
		<script language="javascript"
			src="<%=path%>/common/js/common/common.js"></script>
		<!--右键菜单脚本 -->
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<script language="javascript"
			src="<%=path%>/common/js/upload/jquery.MultiFile.js"></script>
		<script src="<%=path%>/common/js/upload/jquery.blockUI.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/btn.js" type="text/javascript"></script>



		<style type="text/css">
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
		 

//为string对象添加一个清除前后空格的属性
String.prototype.trim = function()
{
     return this.replace(new RegExp("(^[\\s]*)|([\\s]*$)", "g"), "");
};

//显示下拉信息
function ShowSuggest(objInputText)
{
     objInputText.onkeyup = ControlSuggest;
     
     objInputText.onblur = RemoveSuggest;

     var oldValue = objInputText.parentElement.getElementsByTagName("h6")[0];
     var olValue=
     
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
    
}

</script>
	</head>
	<base target="_self" />
	<body>
		<table width="100%" border="0" cellspacing="0" cellpadding="0"
			style="vertical-align: top;">

			<td height="90%">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td
							style="FILTER: progid :         DXImageTransform .         Microsoft .         Gradient(gradientType =         0, startColorStr =         #ededed, endColorStr =         #ffffff);">
							<table width="100%" border="0" cellspacing="0" cellpadding="0"
								style="vertical-align: top;">
								<tr>
									<td align='center'>
										<strong>会议通知</strong>
									</td>
								</tr>
								<tr>
								<tr>
									<td height="100%">
										<DIV class=tab-pane id=tabParent style="width: 100%">
											<DIV class=tab-page id=tabPage1 style="width: 100%">
												<table width="100%" class="table1" border="0"
													cellpadding="0" cellspacing="1" align="center">
													<tr>
														<td class="biao_bg1" width="20%" align="right">
															会议标题(
															<font color="red">*</font>)：
														</td>
														<td class="td1" width="80%" colspan="5">
															<input type="text" name="model.conferenceTitle"
																id="conferenceTitle" style="width: 100%;">
														</td>
													</tr>
													<tr>
														<td class="biao_bg1" width="20%" align="right">
															会议地址(
															<font color="red">*</font>)：
														</td>
														<td class="td1" width="30%" colspan="2">
															<input type="text" name="model.conferenceAddr"
																id="conferenceAddr" style="width: 100%;">
														</td>

														<td class="biao_bg1" width="20%" align="right">
															报名截至时间(
															<font color="red">*</font>)：
														</td>
														<td class="td1" width="30%" colspan="2">
															<input type="text" name="model.conferenceAddr"
																id="conferenceAddr" style="width: 100%;">
														</td>
													</tr>

													<tr>
														<td class="biao_bg1" width="20%" align="right">
															会议开始时间(
															<font color="red">*</font>)：
														</td>
														<td class="td1" width="30%" colspan="2">
															<input readonly type="text" id="conferenceStime"
																name="model.conferenceStime"
																onclick="WdatePicker({dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
																class="Wdate" style="width: 100%" />
														</td>

														<td class="biao_bg1" width="20%" align="right">
															会议结束时间(
															<font color="red">*</font>)：
														</td>
														<td class="td1" width="30%" colspan="2">
															<input type="text" name="model.conferenceAddr"
																id="conferenceAddr" style="width: 100%;">
														</td>
													</tr>
													<tr>
														<td class="biao_bg1" width="20%" align="right">
															主持人(
															<font color="red">*</font>)：
														</td>
														<td class="td1" width="30%" colspan="2">
															<input type="text" name="model.conferenceAddr"
																id="conferenceAddr" style="width: 100%;">
														</td>

														<td class="biao_bg1" width="20%" align="right">
															会议类型(
															<font color="red">*</font>)：
														</td>
														<td class="td1" width="30%" colspan="2">
															<select name="model.conferenceType"
																id="myform_model_conferenceType" style="width: 100%">
																<option value="BGHY">
																	办公会议
																</option>
																<option value="BMLH">
																	部门例会
																</option>

															</select>
														</td>
													</tr>
													<tr>
														<td class="biao_bg1" width="16%" align="right">
															出席领导：
														</td>
														<td class="td1" width="80%" colspan="5">
															<input type="text" name="model.conferenceAttenleaders"
																id="conferenceAttenleaders" style="width: 100%;">
														</td>
													</tr>

													<tr>
														<td class="biao_bg1" width="20%" align="right">
															是否重报(
															<font color="red">*</font>)：
														</td>
														<td class="td1" width="30%" colspan="2">
															<input type="text" name="model.conferenceAddr"
																id="conferenceAddr" style="width: 100%;">
														</td>

														<td class="biao_bg1" width="20%" align="right">
															处理状态(
															<font color="red">*</font>)：
														</td>
														<td class="td1" width="30%" colspan="2">
															<input type="text" name="model.conferenceAddr"
																id="conferenceAddr" style="width: 100%;">
														</td>
													</tr>
												</table>
												<br>

												<table width="70%" class="table1" border="0" cellpadding="0"
													cellspacing="1" align="center">
													<tr>
														<td>
															<input type="button" name="addMember" value="新增上报人员" />
														</td>
													</tr>
													<tr>
														<td>
															<table width="100%" class="table1" border="0"
																cellpadding="0" cellspacing="1" align="center">
																<tr>
																	<th>
																		姓名
																	</th>
																	<th>
																		职务
																	</th>
																	<th>
																		性别
																	</th>
																	<th>
																		民族
																	</th>
																	<th>
																		手机号
																	</th>
																	<th>
																		办公电话
																	</th>
																	<th>
																	  操作
																	</th>
																</tr>
																<tr>
																	<td>
																		<input type="text"   id="txtInput11" name="name" onkeydown="ShowSuggest(this);" style="width: 320px" autocomplete="off" />			
																		 <h6 class="hiddenValues" style="display: none;"></h6>															</td>
																	<td>
																		<input type='text' name="name" id="zw" />
																	</td>
																	<td>
																		<input type='text' name="name" id="xb" />
																	</td>
																	<td>
																		<input type='text' name="name" id="mz" />
																	</td>
																	<td>
																		<input type='text' name="name" id="tel" />
																	</td>
																	<td>
																		<input type='text' name="name" id="mobail" />
																	</td>
																	<td>
																	   <input type="button" name="del" id="del" value ="删除" onclick=""/>
																	</td>
																</tr>
															</table>
														</td>
													</tr>
													<tr>
														<td>
															&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
															备注：&nbsp;&nbsp;&nbsp; &nbsp;
															<input type='text' name="name" id="name"
																style="width: 70%;" />
														</td>
													</tr>
												</table>
												<br>
												<table width="100%" class="table1" border="0"
													cellpadding="0" cellspacing="1" align="center">
													<tr>
														<td>
															会议材料
														</td>
													</tr>
													<tr>
														<td>
															<input type='text' name="name" id="name"
																style="width: 100%;" />
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





		<div align='center'>
			<input type="text" runat="server" id="txtInput" name="txtInput"
				onkeydown="ShowSuggest(this);" style="width: 320px"
				autocomplete="off" />
			<input type="text" id="name" name="name" style="width: 320px" />
			<input type="text" runat="server" id="txtInput2" name="txtInput"
				onkeydown="ShowSuggest(this);" style="width: 320px"
				autocomplete="off" />

			<h6 class="hiddenValues" style="display: none;"></h6>
		</div>
	</body>
</html>
