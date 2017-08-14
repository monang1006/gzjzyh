/*------------------------------------------------------------------------------
+                         控件数据显示										   +
+						观察者接口 Observer  主题接口	 Subject
+
-------------------------------------------------------------------------------*/

	//定义观察者接口
	function Observer () {
		//interface
		this.update = update;
		function update () {
		}
	}
	//定义主题接口
	function Subject () {
		//interface
		this.attach = attach; //登记一个新的观察者
		this.detach = detach; //删除一个的观察者
		this.notifyObservers = notifyObservers; //通知所有登记的观察者
		function attach (observer) {
		}
		function detach (observer) {
		}
		function notifyObservers () {
		}
	}
/*-----------定义具体的观察者--------*/
	//右边panel属性标签
	function PropertyLabelObserver () {
		//property
		this.object = null;
		//inhert
    	this.base=Observer;
       	this.base();
		//override
		this.update = update;
		//method
		this.setObject = setObject;
		function setObject (obj) {
			this.object = obj;
		}
		function update () {
			this.object.innerText =fSelectedObj.nodeclass.thisCaption;
		}
	}
	//获得焦点的控件改变颜色
	function focusCtlObserver () {
		//inhert
    	this.base=Observer;
       	this.base();
		//override
		this.update = update;
		function update () {
            for (var i=0;i<sltObjArray.options.length;i++) {
				var obj = document.getElementById(sltObjArray.options[i].value);
				
				//zw
				var isMulti = false;
				for(var one=0;one<fSelectedObjArray.length;one++){
					if(obj.id == fSelectedObjArray[one].id){
						isMulti = true;
					}
				}
					
                if (isMulti || obj.id==fSelectedObj.id) {
                	obj.StrokeColor='green';
                    obj.strokeweight=2;
					if(fSelectedObj.ctlType==CNST_CTLTYPE_LINE && fSelectedObj.isStraint=="0"){
						fSelectedObj.nodeclass.movepoint1.style.display="block";
						fSelectedObj.nodeclass.movepoint2.style.display="block";
					}
                } else {
                	obj.StrokeColor='#000000';
                    obj.strokeweight=1;
					if(obj.ctlType==CNST_CTLTYPE_LINE){
						obj.nodeclass.movepoint1.style.display="none";
						obj.nodeclass.movepoint2.style.display="none";
					}
                } //end if
			} // end for
		}
	}
	//右边panel属性列表
	function PropertyListObserver () {
		//inhert
    	this.base=Observer;
       	this.base();
		//override
		this.update = update;
		function update () {
			propertyContext.setPropertyState();
			propertyContext.setProperty();
		}
	}
	//线条的重绘
	function LinesObserver () {
		//inhert
    	this.base=Observer;
       	this.base();
		//override
		this.update = update;
		function update () {
			if(fSelectedObj != null) {
            	if (fSelectedObj.ctlType!=CNST_CTLTYPE_LINE) {
					var aLines = fSelectedObj.line.split(";");
					for( var i = 0; i < aLines.length - 1; i ++ ){

						var lineObjStr = (aLines[i].split("TO")[0]).split(":")[0];

						var oLineStart = document.getElementById((aLines[i].split("TO")[0]).split(":")[1]);
						var oLineEnd = document.getElementById(aLines[i].split("TO")[1]);
						
						//add yubin start
						var line=document.getElementById(lineObjStr);
						if(line.isStraint=="1"){
							var posArray = cf_CalculateLinePos(oLineStart,oLineEnd);
							line.posArray[0]=posArray["x1"];
							line.posArray[1]=posArray["y1"];
							line.posArray[2]=posArray["x2"];
							line.posArray[3]=posArray["y2"];
							line.posArray[4]=posArray["x4"];
							line.posArray[5]=posArray["y4"];
							line.posArray[6]=posArray["x5"];
							line.posArray[7]=posArray["y5"];
							line.posArray[8]=posArray["x6"];
							line.posArray[9]=posArray["y6"];
							line.nodeclass.textobj.style.top=posArray["y2"];
							line.nodeclass.textobj.style.left=posArray["x2"];
							//add yubin end
							var str = lineObjStr + '.Points.value="'+line.posArray[0]+','+line.posArray[1]+' '+line.posArray[4]+','+line.posArray[5]+'"';
							eval(str);
							line.nodeclass.movepoint1.style.top=line.posArray[7]-2;
							line.nodeclass.movepoint1.style.left=line.posArray[6]-2;
							line.nodeclass.movepoint2.style.top=line.posArray[9]-2;
							line.nodeclass.movepoint2.style.left=line.posArray[8]-2;
						}else if(line.isStraint=="0"){
							if(oLineStart.id==fSelectedObj.id){
								var posArray = cal_node_point(oLineStart.style.pixelLeft,oLineStart.style.pixelTop,line.posArray[6],line.posArray[7]);
								line.posArray[0]=posArray["x"];
								line.posArray[1]=posArray["y"];
							}else if(oLineEnd.id==fSelectedObj.id){
								var posArray = cal_node_point(oLineEnd.style.pixelLeft,oLineEnd.style.pixelTop,line.posArray[8],line.posArray[9]);
								line.posArray[4]=posArray["x"];
								line.posArray[5]=posArray["y"];
							}
							//add yubin end
							var str = lineObjStr + '.Points.value="'+line.posArray[0]+','+line.posArray[1]+' '+line.posArray[6]+','+line.posArray[7]+' '+line.posArray[8]+','+line.posArray[9]+' '+line.posArray[4]+','+line.posArray[5]+'"';
							eval(str);
						}
					}
            	}//end if
            	// add yubin
            	else if(fSelectedObj.ctlType==CNST_CTLTYPE_LINE){
					if(fSelectedObj.isStraint=="0"){
						if(fSelectedObj.movetype=="1"){
							var posArray = cal_node_point(fSelectedObj.fromelement.style.pixelLeft,fSelectedObj.fromelement.style.pixelTop,fSelectedObj.posArray[6],fSelectedObj.posArray[7]);
							//add yubin start
							fSelectedObj.posArray[0]=posArray["x"];
							fSelectedObj.posArray[1]=posArray["y"];
							fSelectedObj.nodeclass.movepoint1.style.top=fSelectedObj.posArray[7]-2;
							fSelectedObj.nodeclass.movepoint1.style.left=fSelectedObj.posArray[6]-2;
						}else if(fSelectedObj.movetype=="2"){
							var posArray = cal_node_point(fSelectedObj.toelement.style.pixelLeft,fSelectedObj.toelement.style.pixelTop,fSelectedObj.posArray[8],fSelectedObj.posArray[9]);
							//add yubin start
							fSelectedObj.posArray[4]=posArray["x"];
							fSelectedObj.posArray[5]=posArray["y"];
							fSelectedObj.nodeclass.movepoint2.style.top=fSelectedObj.posArray[9]-2;
							fSelectedObj.nodeclass.movepoint2.style.left=fSelectedObj.posArray[8]-2;
						}
						fSelectedObj.posArray[2]=(parseFloat(fSelectedObj.posArray[6])+parseFloat(fSelectedObj.posArray[8]))/2;
						fSelectedObj.posArray[3]=(parseFloat(fSelectedObj.posArray[7])+parseFloat(fSelectedObj.posArray[9]))/2;
						fSelectedObj.nodeclass.textobj.style.top=fSelectedObj.posArray[3];
						fSelectedObj.nodeclass.textobj.style.left=fSelectedObj.posArray[2];

						var str = fSelectedObj.id + '.Points.value="'+fSelectedObj.posArray[0]+','+fSelectedObj.posArray[1]+' '+fSelectedObj.posArray[6]+','+fSelectedObj.posArray[7]+' '+fSelectedObj.posArray[8]+','+fSelectedObj.posArray[9]+' '+fSelectedObj.posArray[4]+','+fSelectedObj.posArray[5]+'"';
						eval(str);
					}else{
						var posArray = cf_CalculateLinePos(fSelectedObj.fromelement,fSelectedObj.toelement);
						//add yubin start
						
						fSelectedObj.posArray[0]=posArray["x1"];
						fSelectedObj.posArray[1]=posArray["y1"];
						fSelectedObj.posArray[2]=posArray["x2"];
						fSelectedObj.posArray[3]=posArray["y2"];
						fSelectedObj.posArray[4]=posArray["x4"];
						fSelectedObj.posArray[5]=posArray["y4"];
						fSelectedObj.posArray[6]=posArray["x5"];
						fSelectedObj.posArray[7]=posArray["y5"];
						fSelectedObj.posArray[8]=posArray["x6"];
						fSelectedObj.posArray[9]=posArray["y6"];
						fSelectedObj.nodeclass.textobj.style.top=fSelectedObj.posArray[3];
						fSelectedObj.nodeclass.textobj.style.left=fSelectedObj.posArray[2];
						//fSelectedObj.isStraint="0";
						var str = fSelectedObj.id + '.Points.value="'+fSelectedObj.posArray[0]+','+fSelectedObj.posArray[1]+' '+fSelectedObj.posArray[4]+','+fSelectedObj.posArray[5]+'"';
						eval(str);
						fSelectedObj.nodeclass.movepoint1.style.top=fSelectedObj.posArray[7]-2;
						fSelectedObj.nodeclass.movepoint1.style.left=fSelectedObj.posArray[6]-2;
						fSelectedObj.nodeclass.movepoint2.style.top=fSelectedObj.posArray[9]-2;
						fSelectedObj.nodeclass.movepoint2.style.left=fSelectedObj.posArray[8]-2;
					}
            	}
            	// add yubin end
			}//end if
		}
	}
	//设置left+top属性
	function leftTopPropertyObserver () {
		//inhert
    	this.base=Observer;
       	this.base();
		//override
		this.update = update;
		function update () {
			for(var i=0;i<noderoot.childNodes.length;i++){
				var tempnode=noderoot.childNodes[i];
				if(tempnode.getAttributeNode("thisIDName").value==fSelectedObj.id){
					tempnode.getAttributeNode("leftpix").value=fSelectedObj.style.posLeft;
					tempnode.getAttributeNode("toppix").value=fSelectedObj.style.posTop;
					break;
				}
			}
		}
	}


	//设置与节点相连的连接变化属性
	function nodeLinePointsObserver () {
		//inhert
    	this.base=Observer;
       	this.base();
		//override
		this.update = update;
		function update () {
			var aLines = fSelectedObj.line.split(";");
			for( var i = 0; i < aLines.length - 1; i ++ ){
				var lineObj = eval((aLines[i].split("TO")[0]).split(":")[0]);
				for(var j=0;j<lineroot.childNodes.length;j++){
					var templine=lineroot.childNodes[j];
					if(templine.getAttributeNode("thisIDName").value==lineObj.id){
						templine.getAttributeNode("points").value=lineObj.posArray[0]+","+lineObj.posArray[1]+","+lineObj.posArray[2]+","+lineObj.posArray[3]+","+lineObj.posArray[4]+","+lineObj.posArray[5];
						break;
					}
				}

			}
		}
	}

	//设置移动线段属性
	function moveLinePointsObserver () {
		//inhert
    	this.base=Observer;
       	this.base();
		//override
		this.update = update;
		function update () {
			for(var j=0;j<lineroot.childNodes.length;j++){
				var templine=lineroot.childNodes[j];
				if(templine.getAttributeNode("thisIDName").value==fSelectedObj.id){
					templine.getAttributeNode("points").value=fSelectedObj.posArray[0]+","+fSelectedObj.posArray[1]+","+fSelectedObj.posArray[2]+","+fSelectedObj.posArray[3]+","+fSelectedObj.posArray[4]+","+fSelectedObj.posArray[5];
					break;
				}
			}
		}
	}

/*--------end-具体的观察者-------*/

	//定义控件主题类
	function ControlSubject () {
		//property
		this.observerList = new Array(); //用来存储观察者
		//inhert
    	this.base=Subject;
       	this.base();
		//override
		this.attach = attach; //登记一个新的观察者
		this.detach = detach; //删除一个的观察者
		this.notifyObservers = notifyObservers; //通知所有登记的观察者
		function attach (observer) {
			var i = this.observerList.length;
			this.observerList[i] = observer;
		}
		function detach (observer) {
		}
		function notifyObservers () {
            for (var i=0;i<this.observerList.length;i++) {
				this.observerList[i].update();
			}
		}
	}
