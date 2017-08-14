/***********************************************************************************/
/****   TAB控件前台JS类   ***********************************************************/
/****   主要功能：初始化TAB标签，响应card点击事件等×××××××××××/
/****   作者：刘彦峰******************************************************************/
//   类名：TabTag
function Tab(){

var objTab=null;	//整个tab页
var titles=null;	//标题数组
var cards=null;		//卡片数组
var cardNum=null;	//卡片数量
var mBtn=null;		//标题移动按钮
var tabType="top";	//标题位置
var first=null;			//显示在最左的卡片
var last=null;		//当last在最左时，则不能继续左移了，first的最大值
var limit=null;		//tab页总宽度
var currTabNum=null;
var infoSetCode=null;	//当前tab页的信息集编号
var currentcard=null; //当前TAB页对象

//初始化tab页
this.initTab=initTab;
function initTab(tabId,index){
	objTab=document.all(tabId);
	first=null;
	last=null;
	if(objTab.width!="100%")
		limit=parseInt(objTab.width);
	else
		limit=parseInt(objTab.offsetWidth);
	if(titles==null||cards==null){
		if(objTab.cells[0].children[0].children[0].tagName=="TABLE"){
			titles=objTab.cells[0].children[0].children[0].tBodies[0].children[0].children;
			cards=objTab.cells[0].children[1].children;
			objTab.cells[0].children[0].children[0].onmousedown=this.doMouseDown;
			tabType="top";
			var tidiv=objTab.cells[0].children[0];
			tidiv.style.width=limit;
			tidiv.style.display="";
		}else{
			titles=objTab.cells[0].children[1].children[0].tBodies[0].children[0].children;
			cards=objTab.cells[0].children[0].children;
			objTab.cells[0].children[1].children[0].onmousedown=this.doMouseDown;
			tabType="bottom";
			var tidiv=objTab.cells[0].children[1];
			tidiv.style.width=limit;
			tidiv.style.display="";
		}
	}
	cardNum=titles.length-2;
	mBtn=titles[titles.length-1];
	mBtn.style.display="none";
	var sum=0;
	for(var i=titles.length-3;i>=0;i--){
		if(titles[i].style.display!="none"){
			sum+=parseInt(titles[i].offsetWidth);
			first=i;
		}
		if(last==null&&sum>(limit-32))
			last=++i;
		if(mBtn.style.display=="none"&&sum>limit){
			mBtn.style.display="inline";
			mBtn.style.left=(limit-32);
		}
	}
	if(index!=null&&index==-1){//处理selected缺省的情况
		cards[0].style.display="";
		showCard(0);
	}
	//currentcard=cards[0];
	
}

this.getCurrentCard=getCurrentCard;
function getCurrentCard()
{
	if(currentcard==null)
		currentcard=cards[0];
	return currentcard;
}

this.getAllCards=getAllCards;
function getAllCards()
{
	return cards;
}

this.getFirstCard=getFirstCard;
function getFirstCard()
{
	return cards[0];
}

this.getCurrInfoSetCode=getCurrInfoSetCode;
function getCurrInfoSetCode()
{
	if(infoSetCode==null)
		return "2"
	else
	    return infoSetCode
}

this.getCurrTabNum=getCurrTabNum;
function getCurrTabNum()
{
	if(currTabNum==null)
		return "1"
	else
	    return currTabNum
}
//相应鼠标点击事件
this.doMouseDown=doMouseDown;
function doMouseDown()
{  
	var obj=event.srcElement;
	var cur=null;
	if(obj.tagName=="SPAN"){
		if(tabType=="top"){
			for(var i=0;i<cardNum;i++){
				if(titles[i].style.display!="none"){
    			var test=titles[i].children[0].children[0].children[2];
	    			if(test.children[2].children[0]==obj){
	    				cur=i;
	    				if(cards[i].children[0].src=="")
	    					doCardClick1(cards[i]);		
	    				eval("test.children[2].style.backgroundColor='#70A9CA';");
	    				eval("obj.parentElement.style.height='20';");
	    				eval("test.nextSibling.children[1].style.backgroundColor='transparent';");
	    				eval("cards[i].style.display=''");
	    				eval(objTab.id+".doCardClick(cards[i]);");  				
	    				//currentcard=cards[i];
	    			}
	    			else{
	    			    eval("test.children[2].style.backgroundColor='';");
	    				eval("test.children[2].style.height='18';");
	    				eval("test.nextSibling.children[1].style.backgroundColor='white';");
	    				eval("cards[i].style.display='none'");
	    			}
			}	
		}
    }
    else if(tabType=="bottom")
		for(var i=0;i<cardNum;i++){
			if(titles[i].style.display!="none"){
				var test=titles[i].children[0].children[0].children[1];
 					if(test.children[2].children[0]==obj){
						cur=i;	
	 				 	if(cards[i].children[0].src=="")   //判断是否已经加载tab页的链接路径
	 						doCardClick1(cards[i]);	 					
	 					eval("obj.parentElement.style.height='22';");
	 					eval("test.previousSibling.children[0].style.backgroundColor='transparent';");
	 					eval("cards[i].style.display=''");
	 					eval(objTab.id+".doCardClick(cards[i]);");
	 						//currentcard=cards[i];
 					}
 					else{
 						eval("test.children[2].style.height='20';");
 						if(frameroot==scriptroot+"/frame/theme_blue"){
 							eval("test.previousSibling.children[0].style.backgroundColor='#506eaa';");
 						}else if(frameroot==scriptroot+"/frame/theme_red"){
 							eval("test.previousSibling.children[0].style.backgroundColor='#dbdbdb';");
 						}else{
 							eval("test.previousSibling.children[0].style.backgroundColor='#848284';");
 						}
 						eval("cards[i].style.display='none'");
					}
			}		
		}			
		if(titles[titles.length-1].style.display=="inline"){
			var len=limit-32;
			var sum=null;
			while(true){
				sum=0;
				if(cur==first)
					break;
				for(var j=first;j<=cur;j++)
					if(titles[j].style.diaplay!="none")
						sum+=parseInt(titles[j].offsetWidth);
				if(sum>len)
					moveLeft1();
				else
					break;
			}
		}
	}
  return ;
}

//显示某卡片
this.showCard=showCard;
function showCard(index){
	if(index<0||index>=cardNum)
		return;
	if(tabType=="top")
		for(var i=0;i<cardNum;i++){
			if(titles[i].style.display!="none"){
				var test=titles[i].children[0].children[0].children[2];
    		if(i==index){
    		    eval("test.children[2].style.backgroundColor='#70A9CA';");
    			eval("test.children[2].style.height='20';");
    			eval("test.nextSibling.children[1].style.backgroundColor='transparent';");
    			eval("cards[i].style.display=''");
    			//eval(objTab.id+".doCardClick(cards[i]);");
    		}
    		else{
    		    eval("test.children[2].style.backgroundColor='';");  
    			eval("test.children[2].style.height='18';");
    			eval("test.nextSibling.children[1].style.backgroundColor='white';");
    			eval("cards[i].style.display='none'");
    		}
			}
  	}
	else if(tabType=="bottom")
  			for(var i=0;i<cardNum;i++){
					if(titles[i].style.display!="none"){
    				var test=titles[i].children[0].children[0].children[1];
    				if(i==index){
    					eval("test.children[2].style.height='22';");
    					eval("test.previousSibling.children[0].style.backgroundColor='transparent';");
    					eval("cards[i].style.display=''");
    					//eval(objTab.id+".doCardClick(cards[i]);");
    				}
    				else{
    					eval("test.children[2].style.height='20';");
    					if(frameroot==scriptroot+"/frame/theme_blue"){
   							eval("test.previousSibling.children[0].style.backgroundColor='#506eaa';");
   						}else if(frameroot==scriptroot+"/frame/theme_red"){
    						eval("test.previousSibling.children[0].style.backgroundColor='#dbdbdb';");
    					}else{
   							eval("test.previousSibling.children[0].style.backgroundColor='#848284';");
   						}
    					eval("cards[i].style.display='none'");
  					}
					}
				}	
}
//获得某卡片的标题
this.getTitle=getTitle;
function getTitle(index){
	if(index<0||index>=cardNum||titles[index].style.display=="none")
		return;
	if(tabType=="top")
		return titles[index].children[0].children[0].children[2].children[2].children[0].innerText;
	else if(tabType=="bottom")
				return titles[index].children[0].children[0].children[1].children[2].children[0].innerText;
}
//卡片标题左移一个标题的距离
this.moveLeft1=moveLeft1;
function moveLeft1(){
	var lp=0;
	if(first==last)
		return;
	var ml=parseInt(titles[first].offsetWidth);
	while(titles[++first].style.display=="none"&&first<last);
	for(var i=0;i<titles.length-1;i++){
		if(titles[i].style.left!=""&&titles[i].style.left!=null){
			lp=parseInt(titles[i].style.left)-ml;
			titles[i].style.left=lp;
		}
	}
}

//卡片标题左移到完整显示当前最左的标题
this.moveLeft=moveLeft;
function moveLeft(){
		var sum=0;
		var len=limit-32;
		for(var i=first;i<titles.length-2;i++)
			if(titles[i].style.display!="none"&&(sum+=titles[i].offsetWidth)>len){
				var curl=i;
				while(true){
					sum=0;
					if(first==curl)
						break;
					moveLeft1();
					for(var j=first;j<=curl;j++)
						if(titles[j].style.display!="none")
							sum+=titles[j].offsetWidth;
						if(sum<=len)
							break;
				}
				break;
			}
}
//卡片标题右移一个标题的距离
this.moveRight=moveRight;
function moveRight(){
	var lp=0;
	if(first==0)
		return;
	else
		while(first>0&&titles[--first].style.display=="none");
	if(titles[first].style.display=="none")
		return;
	var ml=parseInt(titles[first].offsetWidth);
	for(var i=0;i<titles.length-1;i++){
		if(titles[i].style.left!=""&&titles[i].style.left!=null){
			lp=parseInt(titles[i].style.left)+ml;
			titles[i].style.left=lp;
		}
	}
}

//设置某个卡片是否可见，目前只能用于标题较短、标题不移动的情况
this.setVisible=setVisible;
function setVisible(cardId,visible){
	var index=null;
	var ocur=null;
	this.resetCards();
	if(visible){
		for(var i=0;i<cardNum;i++){
			if(cards[i].id==cardId)
				index=i;
			if(cards[i].style.display!="none")
				ocur=i;
			if(ocur!=null&&index!=null)
				break;
		}
		if(index==null)
			return;
		titles[index].style.display="";
		this.showCard(ocur);
		if(index<first)
			--first;
		if(cardNum==1){
			this.initTab(objTab.id,-1);
			return;
		}
	}
	else{
		for(var i=0;i<cardNum;i++)
			if(cards[i].id==cardId){
				index=i;
				break;
			}
			if(index==null)
				return;	
			var cur=cards[index];
			titles[index].style.display="none";
			if(cur.style.display!="none"){
				cur.style.display="none";
			var j=index;
			while(++index<cardNum||--j>=0)		
				if(index<cardNum&&titles[index].style.display!="none"){
					this.showCard(index);
					break;
				}else if(j>=0&&titles[j].style.display!="none"){
								this.showCard(j);
								break;
				}
			}

			if(index<=first)
				first++;	
	}
	this.initTab(objTab.id);
}
//复位
this.nextCards=nextCards;
function nextCards(){
    var carnum;
	if(currentcard==null)
		currentcard=cards[0];
	carnum=currentcard.cardnum;	
   	while(titles[carnum].style.display=="none"){
   		if(carnum<cardNum-1){
   			carnum=carnum*1+1;
   		}else{
   			carnum=0;
   		}	
   	}	
   	currentcard=cards[carnum-1];
	if(currentcard.cardnum<cardNum){						
   		if(cards[currentcard.cardnum].children[0].src==""||cards[currentcard.cardnum].children[0].changeLogo=="changed"){ 						   							
   			doCardClick1(cards[currentcard.cardnum]);
   			cards[currentcard.cardnum].children[0].changeLogo=="nochange";     							    						   						    											
   		} 
		this.showCard(currentcard.cardnum);
		eval(objTab.id+".doCardClick(cards[currentcard.cardnum]);");		
	}		
}
//复位
this.resetCards=resetCards;
function resetCards(){
	for(var i=0;i<=cardNum;i++)
		titles[i].style.left="0";	
}
//卡片点击接口函数
this.doCardClick=doCardClick;
function doCardClick(card){
	if(document.all.personId!=undefined){
		var tableName=card.id;
		var personId=document.all.personId.value;
		if((personId==null||personId=='null'||personId=='')&&tableName!='T_OA_BASE_PERSON'){
			companytab.showCard(0);
			currentcard=cards[0];
			return false;
		}
	}
	if(currentcard==null)
		currentcard=cards[0];
	var if_frame=window.frames[currentcard.cardnum];
	var if_frame2=window.frames[currentcard.cardnum-1];
	if(if_frame!=undefined){
       	if(if_frame.changed==true){
       		card.children[0].src="";
			if(confirm("要保存所做的修改吗？")){
			  	this.showCard(currentcard.cardnum-1);
			  	if(currentcard.cardnum==1)				  
					if_frame.submitForm();
			  	else
			  		if_frame.save();					 	
			}else{
				this.showCard(currentcard.cardnum-1);
				if(currentcard.cardnum!=1)
				{
					var newRowKeyValues=if_frame.document.getElementById("newRowKeyValue").value;
					if_frame.gotoRemove2(newRowKeyValues);
				}
				if_frame.location.reload();
			}
			return;
		}
	}else if(if_frame2!=undefined){
			/*if(if_frame2.viewchanged==true){
				if(confirm("当前信息未保存，是否保存（是/否）？")){
					this.showCard(currentcard.cardnum-1);
					if_frame2.viewchanged=false;
					if_frame2.submitForm();	
					return;
				}else{
					this.showCard(currentcard.cardnum);
					//addnewInfo();
				}				
			}*/
			this.showCard(currentcard.cardnum);
	}
   
	currentcard=card;
	var id=card.children[0].id;
	if(document.getElementById("newInfoAdd")!=undefined){
		if(id!='40288239230c361b01230c7a60f10015')
			document.getElementById("newInfoAdd").innerHTML="<INPUT type='button' class='input_bg' value='新增当前子集' onclick='addnewInfo()'>&nbsp;";
		else
			document.getElementById("newInfoAdd").innerHTML="";
	}
}

//将所有卡片的changLogo属性设置为'changed'
this.setNotChanged=setNotChanged;
function setNotChanged(){
for(var i=0;i<cardNum;i++)
	cards[i].children[0].changeLogo="changed";
}

//将年度考核子集卡片的changLogo属性设置为'changed'
this.setExamNotChanged=setExamNotChanged;
function setExamNotChanged(num){
	cards[num].children[0].changeLogo="changed";
}
}