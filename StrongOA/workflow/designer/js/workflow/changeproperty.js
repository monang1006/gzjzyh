/*------------------------------------------------------------------------------
+                        属性的改变 					                           +
+                            						                           +
+						          		       							       +
-------------------------------------------------------------------------------*/

	//改变属性策略角色接口
	function ChangePropertyStrategy (aSObj) {
		//interface
		this.sObj = aSObj;         //当前所选控件
		this.change = change;
		function change () {
		}
	}
	//改变 name
	function ChangeNameSty (aSObj) {
		//inhert
    	this.base=ChangePropertyStrategy;
       	this.base(aSObj);
		//property
		this.inputValue;
		//override
		this.change = change;
		function change () {

			this.sObj.lastChild.innerText = this.inputValue;
		}
	}
	//改变 left
	function ChangeLeftSty (aSObj) {
		//inhert
    	this.base=ChangePropertyStrategy;
       	this.base(aSObj);
		//property
		this.inputValue;
		this.canvasObj;
		//override
		this.change = change;
		function change () {
			var myLeft = this.inputValue;
			this.sObj.style.pixelLeft=myLeft;
            var rx = this.sObj.style.pixelLeft;
			var sx = this.canvasObj.clientWidth-(this.sObj.style.pixelLeft+this.sObj.clientWidth);
			if(	rx<0 ) this.sObj.style.left = 0;
			if(	sx<0 ) this.sObj.style.left = this.canvasObj.clientWidth-this.sObj.clientWidth;
		}
	}
	//改变 Top
	function ChangeTopSty (aSObj) {
		//inhert
    	this.base=ChangePropertyStrategy;
       	this.base(aSObj);
		//property
		this.inputValue;
		this.canvasObj;
		//override
		this.change = change;
		function change () {
			var myTop = this.inputValue;
			this.sObj.style.pixelTop=myTop;
            var ry = this.sObj.style.pixelTop;
			var sy = this.canvasObj.clientHeight-(this.sObj.style.pixelTop+this.sObj.clientHeight);
			if(	ry<0 ) this.sObj.style.top = 0;
			if(	sy<0 ) this.sObj.style.top = this.canvasObj.clientHeight-this.sObj.clientHeight;
		}
	}

	//改变 view
	function ChangeViewSty (aSObj) {
		//inhert
    	this.base=ChangePropertyStrategy;
       	this.base(aSObj);
		//property
		this.inputValue;
		//override
		this.change = change;
		function change () {
			this.sObj.view = this.inputValue;
		}
	}

	//改变 auto
	function ChangeAutoSty (aSObj) {
		//inhert
    	this.base=ChangePropertyStrategy;
       	this.base(aSObj);
		//property
		this.inputValue;
		//override
		this.change = change;
		function change () {
			this.sObj.auto = this.inputValue;
		}
	}
	//改变 ConditionType
	function ChangeConditionTypeSty (aSObj) {
		//inhert
    	this.base=ChangePropertyStrategy;
       	this.base(aSObj);
		//property
		this.inputValue;
		//override
		this.change = change;
		function change () {
			this.sObj.conditionType = this.inputValue;
		}
	}
	//改变 Owner
	function ChangeOwnerSty (aSObj) {
		//inhert
    	this.base=ChangePropertyStrategy;
       	this.base(aSObj);
		//property
		this.inputValue;
		//override
		this.change = change;
		function change () {
			this.sObj.owner = this.inputValue;
		}
	}

	//改变 status
	function ChangeStatusSty (aSObj) {
		//inhert
    	this.base=ChangePropertyStrategy;
       	this.base(aSObj);
		//property
		this.inputValue;
		//override
		this.change = change;
		function change () {
			this.sObj.status = this.inputValue;
		}
	}

	//改变 old-status
	function ChangeOldStatusSty (aSObj) {
		//inhert
    	this.base=ChangePropertyStrategy;
       	this.base(aSObj);
		//property
		this.inputValue;
		//override
		this.change = change;
		function change () {
			this.sObj.oldStatus = this.inputValue;
		}
	}


	//环境角色 改变对象属性
	function ChangePropertyContext () {
		//property
		this.changePropertyStrategy;
		//method
		this.setStrategy = setStrategy;
		this.changeProperty = changeProperty;
		function setStrategy (aChangePropertyStrategy) {
			this.changePropertyStrategy = aChangePropertyStrategy;
		}
		function changeProperty () {
			return this.changePropertyStrategy.change();
		}
	}




