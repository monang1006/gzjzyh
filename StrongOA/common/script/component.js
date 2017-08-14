/** web组件 * */

/** 级联选择组件 * */
(function($) {

	// 创建级联组件
	$.fn.Cascade = function(setting) {
		var ps = $.extend({
			// content holder(Object || css Selector)
			sel : "",
			// whether the slider can be dragged
			type : ""
		}, setting);

		ps.sel = (typeof ps.sel == 'string' ? $(ps.sel) : ps.sel);
		// 初始化第一级
		var init = function() {

			var root = ps.sel[0];

			var data = getData(root, 0);
			// 初始化并填充第一个选择框
			if (data) {
				for (i = 0; i < data.length; i = i + 1) {
					$('<option value="' + data[i].value + '">' + data[i].text
							+ '</option>').appendTo(root);
				}
			}
			// 为第一个框添加事件
			for (i = 0; i < ps.sel.length; i++) {
				$(ps.sel[i]).bind("change", selchange);
			}

		},
		/**
		 * 得到本节点的数据
		 */
		getData = function(obj, index, value) {
			var srcdata, data;
			if (value == null) {
				srcdata = $(ps.sel[index]).attr('srcdata');
			} else {
				var srcdata = $(ps.sel[index + 1]).attr('srcdata');
			}

			// 从本地读取数据
			if (srcdata != null) {
				if (ps.type == 'fun') {

					srcdata = srcdata + "(" + value + ");";
					data = eval(srcdata);
				} else if (ps.type == "local") {

					data = eval("(" + srcdata + ")");
					if (value != null) {
						data = getNextData(index, value, data);
					}
				} else if (ps.type == "remote") {
					$.post(srcdata, {
						value : value
					}, function(data1) {

						data = $.parseJSON(data1);
						if (data) {
							if (value == null) {

								for (i = 0; i < data.length; i = i + 1) {
									$('<option value="' + data[i].value + '">'
											+ data[i].text + '</option>')
											.appendTo(ps.sel[index]);
								}

							} else {
								for (i = 0; i < data.length; i = i + 1) {
									$('<option value="' + data[i].value + '">'
											+ data[i].text + '</option>')
											.appendTo(ps.sel[index + 1]);
								}
							}
						}
					});

				}
			} else {
				data = [];
			}
			return data;
		},
		/**
		 * 得到子节点的数据
		 */
		getNextData = function(index, value, data) {
			var i, arr = [];
			if (data) {
				for (i = 0; i < data.length; i = i + 1) {
					if (data[i].fk == value) {
						arr.push(data[i]);
					}
				}
			}
			return arr;
		},
		/**
		 * 移除value ！= -1的option 参数sel为jquery对象
		 */
		removeop = function(sel) {
			sel.find('option[value!=-1]').remove();
		},
		/**
		 * 得到选中的值 参数sel为jquery对象
		 */
		getSelVal = function(sel) {
			return sel.find('option:selected').val();
		},
		/**
		 * 移除第几个选择框以后的option,option value = -1的不会被移除 移除第0个以后，则移除第1个，第2个，第3个
		 */

		/**
		 * 单选框变化事件
		 */
		selchange = function(e) {

			that = $(this), selVal = getSelVal(that), // 当前输入框选择的值
			index = ps.sel.index(that), // 当前输入框是第几个 从 0 开始
			data = [], // 目标结果集
			i;// 操作选择框的索引
			removeOpAfter(index); // 移除index以后所有选择框的数据,除option 为-1的除外
			data = getData(that, index, selVal);//
			if (data) {
				for (i = 0; i < data.length; i = i + 1) {
					$('<option value="' + data[i].value + '">' + data[i].text
							+ '</option>').appendTo(ps.sel[index + 1]);
				}
			}
		}, removeOpAfter = function(i) {
			var ii;
			for (ii = 0; ii < ps.sel.length; ii = ii + 1) {
				if (ii > i) {
					removeop($(ps.sel[ii]));
				}
			}
		};
		init();
	}
})(jQuery);

/** 菜单组件 * */
(function($) {
	function returnfalse() {
		return false;
	};

	$.fn.contextmenu = function(option) {

		option = $.extend({
			alias : "cmroot",
			width : 150,
			theme : 'b-m-mpanel'
		}, option);
		var ruleName = null, target = null, groups = {}, mitems = {}, actions = {}, showGroups = [], itemTpl = "<div class='b-m-$[type]' unselectable=on><nobr unselectable='on'><img src='$[icon]' align='absmiddle'/><span class='contextmenu' unselectable='on'>$[text]</span></nobr></div>";
		// var gTemplet =
		// $("<div/>").addClass("b-m-mpanel").attr("unselectable",
		// "on").css("display", "none");
		var gTemplet = $("<div/>").addClass(option.theme).attr("unselectable",
				"on").css("display", "none");
		var iTemplet = $("<div/>").addClass("b-m-item").attr("unselectable",
				"on");
		var sTemplet = $("<div/>").addClass("b-m-split");
		// 创建菜单组
		var buildGroup = function(obj) {
			groups[obj.alias] = this;
			this.gidx = obj.alias;
			this.id = obj.alias;
			if (obj.disable) {
				this.disable = obj.disable;
				this.className = "b-m-idisable";
			}
			// $(this).width(obj.width).click(returnfalse).mousedown(returnfalse).appendTo($("body"));
			$(this).width(obj.width).mousedown(returnfalse).appendTo($("body"));
			obj = null;
			return this;
		};
		var buildItem = function(obj) {
			var T = this;
			T.title = obj.text;
			T.idx = obj.alias;
			T.gidx = obj.gidx;
			T.data = obj;
			T.innerHTML = itemTpl.replace(/\$\[([^\]]+)\]/g, function() {
				return obj[arguments[1]];
			});
			if (obj.disable) {
				T.disable = obj.disable;
				T.className = "b-m-idisable";
			}
			obj.items && (T.group = true);
			obj.action && (actions[obj.alias] = obj.action);
			mitems[obj.alias] = T;
			T = obj = null;
			return this;
		};
		// 添加菜单项
		var addItems = function(gidx, items) {
			var tmp = null;
			for (var i = 0; i < items.length; i++) {
				if (items[i].type == "splitLine") {
					// 菜单分隔线
					tmp = sTemplet.clone()[0];
				} else {
					items[i].gidx = gidx;
					if (items[i].type == "group") {
						// 菜单组
						buildGroup.apply(gTemplet.clone()[0], [items[i]]);
						arguments.callee(items[i].alias, items[i].items);
						items[i].type = "arrow";
						tmp = buildItem.apply(iTemplet.clone()[0], [items[i]]);
					} else {
						// 菜单项
						items[i].type = "ibody";
						tmp = buildItem.apply(iTemplet.clone()[0], [items[i]]);
						$(tmp).click(function(e) {
							if (!this.disable) {
								hideMenuPane();
								if ($.isFunction(actions[this.idx])) {
									actions[this.idx].call(this, target);
								}
							}
								// return false;
							});

					} // Endif
					$(tmp).bind("contextmenu", returnfalse).hover(overItem,
							outItem);
				} // Endif
				groups[gidx].appendChild(tmp);
				tmp = items[i] = items[i].items = null;
			} // Endfor
			gidx = items = null;
		};
		var overItem = function(e) {
			// 如果菜单项不可用
			if (this.disable)
				return false;
			hideMenuPane.call(groups[this.gidx]);
			// 如果是菜单组
			if (this.group) {
				var pos = $(this).offset();
				var width = $(this).outerWidth();
				showMenuGroup.apply(groups[this.idx], [pos, width]);
			}
			this.className = "b-m-ifocus";
			return false;
		};
		// 菜单项失去焦点
		var outItem = function(e) {
			// 如果菜单项不可用
			if (this.disable)
				return false;
			if (!this.group) {
				// 菜单项
				this.className = "b-m-item";
			} // Endif
			return false;
		};
		// 在指定位置显示指定的菜单组
		var showMenuGroup = function(pos, width) {
			var bwidth = $("body").width();
			var bheight = document.documentElement.clientHeight;
			var mwidth = $(this).outerWidth();
			var mheight = $(this).outerHeight();
			pos.left = (pos.left + width + mwidth > bwidth) ? (pos.left
					- mwidth < 0 ? 0 : pos.left - mwidth) : pos.left + width;
			pos.top = (pos.top + mheight > bheight) ? (pos.top - mheight
					+ (width > 0 ? 25 : 0) < 0 ? 0 : pos.top - mheight
					+ (width > 0 ? 25 : 0)) : pos.top;
			$(this).css(pos).show();
			showGroups.push(this.gidx);
		};
		// 隐藏菜单组
		var hideMenuPane = function() {
			var alias = null;
			for (var i = showGroups.length - 1; i >= 0; i--) {
				if (showGroups[i] == this.gidx)
					break;
				alias = showGroups.pop();
				groups[alias].style.display = "none";
				mitems[alias] && (mitems[alias].className = "b-m-item");
			} // Endfor
				// CollectGarbage();
		};
		// 隐藏所有菜单菜单组
		var hideAllMenuPane = function() {
			var alias = null;
			for (var i = showGroups.length - 1; i >= 0; i--) {
				alias = showGroups.pop();
				groups[alias].style.display = "none";
				mitems[alias] && (mitems[alias].className = "b-m-item");
			} // Endfor
				// CollectGarbage();
		};
		function applyRule(rule) {
			if (ruleName && ruleName == rule.name)
				return false;
			for (var i in mitems)
				disable(i, !rule.disable);
			for (var i = 0; i < rule.items.length; i++)
				disable(rule.items[i], rule.disable);
			ruleName = rule.name;
		};
		function disable(alias, disabled) {
			var item = mitems[alias];
			item.className = (item.disable = item.lastChild.disabled = disabled)
					? "b-m-idisable"
					: "b-m-item";
		};

		/** 右键菜单显示 */
		function showMenu(e, menutarget) {
			target = menutarget;
			showMenuGroup.call(groups[option.alias], {
				left : e.pageX,
				top : e.pageY
			}, 0);
			$(document).one('mousedown', hideMenuPane);
		}

		/**
		 * 按用户指定的坐标展现菜单
		 * 
		 * @param e
		 *            -点击事件
		 * @param menutarget
		 *            -触发菜单的dom对象
		 * @param position
		 *            -菜单展现的坐标
		 * @return
		 */
		function showMenuByPos(e, menutarget, position) {
			target = menutarget;
			showMenuGroup.call(groups[option.alias], position, 0);
			$(document).one('mousedown', hideMenuPane);
		}

		/**
		 * 改变右键菜单的生成方式，修改成右键触发事件由使用者决定
		 * 
		 * @param e
		 *            -右键事件
		 * @param menutarget
		 *            -触发右键菜单的dom对象
		 * @return
		 */
		function displayMenu(e, menutarget) {
			var bShowContext = (option.onContextMenu && $
					.isFunction(option.onContextMenu)) ? option.onContextMenu
					.call(menutarget, e) : true;
			if (bShowContext) {
				if (option.onShow && $.isFunction(option.onShow)) {
					option.onShow.call(menutarget, this);
				}
				this.showMenu(e, menutarget);
			}
		}

		/**
		 * 按用户指定的坐标展现菜单
		 * 
		 * @param e
		 *            -点击事件
		 * @param menutarget
		 *            -触发右键菜单的dom对象
		 * @param position
		 *            -菜单展现的坐标
		 * @return
		 */
		function displayMenuByPos(e, menutarget, position) {
			var bShowContext = (option.onContextMenu && $
					.isFunction(option.onContextMenu)) ? option.onContextMenu
					.call(menutarget, e) : true;
			if (bShowContext) {
				if (option.onShow && $.isFunction(option.onShow)) {
					option.onShow.call(menutarget, this);
				}
				this.showMenuByPos(e, menutarget, position);
			}
		}

		var $root = $("#" + option.alias);
		var root = null;
		if ($root.length == 0) {
			root = buildGroup.apply(gTemplet.clone()[0], [option]);
			root.applyrule = applyRule;
			root.showMenu = showMenu;
			root.displayMenu = displayMenu;
			root.showMenuByPos = showMenuByPos;// 按用户指定坐标显示
			root.displayMenuByPos = displayMenuByPos;// 按用户指定坐标显示
			root.hideMenuPane = hideAllMenuPane;// 隐藏菜单
			addItems(option.alias, option.items);
		} else {
			root = $root[0];
		}

		// 设置显示规则
		if (option.rule) {
			applyRule(option.rule);
		}
		gTemplet = iTemplet = sTemplet = itemTpl = buildGroup = buildItem = null;
		addItems = overItem = outItem = null;
		// CollectGarbage();
		// return me;
		return root;
	}
})(jQuery);


/** 等待提示组件 * */
;
(function($) {

	if (/1\.(0|1|2)\.(0|1|2)/.test($.fn.jquery) || /^1.1/.test($.fn.jquery)) {
		alert('blockUI requires jQuery v1.2.3 or later!  You are using v'
				+ $.fn.jquery);
		return;
	}

	$.fn._fadeIn = $.fn.fadeIn;

	var noOp = function() {
	};

	// this bit is to ensure we don't call setExpression when we shouldn't (with
	// extra muscle to handle
	// retarded userAgent strings on Vista)
	var mode = document.documentMode || 0;
	var setExpr = $.browser.msie
			&& (($.browser.version < 8 && !mode) || mode < 8);
	var ie6 = $.browser.msie && /MSIE 6.0/.test(navigator.userAgent) && !mode;

	// global $ methods for blocking/unblocking the entire page
	$.blockUI = function(opts) {
		install(window, opts);
	};
	$.unblockUI = function(opts) {
		remove(window, opts);
	};

	// convenience method for quick growl-like notifications
	// (http://www.google.com/search?q=growl)
	$.growlUI = function(title, message, timeout, onClose) {
		var $m = $('<div class="growlUI"></div>');
		if (title)
			$m.append('<h1>' + title + '</h1>');
		if (message)
			$m.append('<h2>' + message + '</h2>');
		if (timeout == undefined)
			timeout = 3000;
		$.blockUI({
			message : $m,
			fadeIn : 700,
			fadeOut : 1000,
			centerY : false,
			timeout : timeout,
			showOverlay : false,
			onUnblock : onClose,
			css : $.blockUI.defaults.growlCSS
		});
	};

	// plugin method for blocking element content
	$.fn.block = function(opts) {
		return this.unblock({
			fadeOut : 0
		}).each(function() {
			if ($.css(this, 'position') == 'static')
				this.style.position = 'relative';
			if ($.browser.msie)
				this.style.zoom = 1; // force 'hasLayout'
			install(this, opts);
		});
	};

	// plugin method for unblocking element content
	$.fn.unblock = function(opts) {
		return this.each(function() {
			remove(this, opts);
		});
	};

	$.blockUI.version = 2.31; // 2nd generation blocking at no extra cost!

	// override these in your code to change the default behavior and style
	$.blockUI.defaults = {
		// message displayed when blocking (use null for no message)
		message : '<h1>Please wait...</h1>',

		title : null, // title string; only used when theme == true
		draggable : true, // only used when theme == true (requires
		// jquery-ui.js to be loaded)

		theme : false, // set to true to use with jQuery UI themes

		// styles for the message when blocking; if you wish to disable
		// these and use an external stylesheet then do this in your code:
		// $.blockUI.defaults.css = {};
		css : {
			padding : 0,
			margin : 0,
			width : '30%',
			top : '40%',
			left : '35%',
			textAlign : 'center',
			color : '#000',
			border : '1px solid red',
			backgroundColor : 'yellow',
			cursor : 'wait'
		},

		// minimal style set used when themes are used
		themedCSS : {
			width : '30%',
			top : '40%',
			left : '35%'
		},

		// styles for the overlay
		overlayCSS : {
			backgroundColor : '#fff',
			opacity : 0.1,
			cursor : 'wait'
		},

		// styles applied when using $.growlUI
		growlCSS : {
			width : '350px',
			top : '10px',
			left : '',
			right : '10px',
			border : 'none',
			padding : '5px',
			opacity : 0.6,
			cursor : 'default',
			color : '#fff',
			backgroundColor : '#000',
			'-webkit-border-radius' : '10px',
			'-moz-border-radius' : '10px'
		},

		// IE issues: 'about:blank' fails on HTTPS and javascript:false is
		// s-l-o-w
		// (hat tip to Jorge H. N. de Vasconcelos)
		iframeSrc : /^https/i.test(window.location.href || '')
				? 'javascript:false'
				: 'about:blank',

		// force usage of iframe in non-IE browsers (handy for blocking applets)
		forceIframe : false,

		// z-index for the blocking overlay
		baseZ : 1000,

		// set these to true to have the message automatically centered
		centerX : true, // <-- only effects element blocking (page block
		// controlled via css above)
		centerY : true,

		// allow body element to be stetched in ie6; this makes blocking look
		// better
		// on "short" pages. disable if you wish to prevent changes to the body
		// height
		allowBodyStretch : true,

		// enable if you want key and mouse events to be disabled for content
		// that is blocked
		bindEvents : true,

		// be default blockUI will supress tab navigation from leaving blocking
		// content
		// (if bindEvents is true)
		constrainTabKey : true,

		// fadeIn time in millis; set to 0 to disable fadeIn on block
		fadeIn : 200,

		// fadeOut time in millis; set to 0 to disable fadeOut on unblock
		fadeOut : 400,

		// time in millis to wait before auto-unblocking; set to 0 to disable
		// auto-unblock
		timeout : 0,

		// disable if you don't want to show the overlay
		showOverlay : true,

		// if true, focus will be placed in the first available input field when
		// page blocking
		focusInput : true,

		// suppresses the use of overlay styles on FF/Linux (due to performance
		// issues with opacity)
		applyPlatformOpacityRules : true,

		// callback method invoked when fadeIn has completed and blocking
		// message is visible
		onBlock : null,

		// callback method invoked when unblocking has completed; the callback
		// is
		// passed the element that has been unblocked (which is the window
		// object for page
		// blocks) and the options that were passed to the unblock call:
		// onUnblock(element, options)
		onUnblock : null,

		// don't ask; if you really must know:
		// http://groups.google.com/group/jquery-en/browse_thread/thread/36640a8730503595/2f6a79a77a78e493#2f6a79a77a78e493
		quirksmodeOffsetHack : 4
	};

	// private data and functions follow...

	var pageBlock = null;
	var pageBlockEls = [];

	function install(el, opts) {
		var full = (el == window);
		var msg = opts && opts.message !== undefined ? opts.message : undefined;
		opts = $.extend({}, $.blockUI.defaults, opts || {});
		opts.overlayCSS = $.extend({}, $.blockUI.defaults.overlayCSS,
				opts.overlayCSS || {});
		var css = $.extend({}, $.blockUI.defaults.css, opts.css || {});
		var themedCSS = $.extend({}, $.blockUI.defaults.themedCSS,
				opts.themedCSS || {});
		msg = msg === undefined ? opts.message : msg;

		// remove the current block (if there is one)
		if (full && pageBlock)
			remove(window, {
				fadeOut : 0
			});

		// if an existing element is being used as the blocking content then we
		// capture
		// its current place in the DOM (and current display style) so we can
		// restore
		// it when we unblock
		if (msg && typeof msg != 'string' && (msg.parentNode || msg.jquery)) {
			var node = msg.jquery ? msg[0] : msg;
			var data = {};
			$(el).data('blockUI.history', data);
			data.el = node;
			data.parent = node.parentNode;
			data.display = node.style.display;
			data.position = node.style.position;
			if (data.parent)
				data.parent.removeChild(node);
		}

		var z = opts.baseZ;

		// blockUI uses 3 layers for blocking, for simplicity they are all used
		// on every platform;
		// layer1 is the iframe layer which is used to supress bleed through of
		// underlying content
		// layer2 is the overlay layer which has opacity and a wait cursor (by
		// default)
		// layer3 is the message content that is displayed while blocking

		var lyr1 = ($.browser.msie || opts.forceIframe)
				? $('<iframe class="blockUI" style="z-index:'
						+ (z++)
						+ ';display:none;border:none;margin:0;padding:0;position:absolute;width:100%;height:100%;top:0;left:0" src="'
						+ opts.iframeSrc + '"></iframe>')
				: $('<div class="blockUI" style="display:none"></div>');
		var lyr2 = $('<div class="blockUI blockOverlay" style="z-index:'
				+ (z++)
				+ ';display:none;border:none;margin:0;padding:0;width:100%;height:100%;top:0;left:0"></div>');

		var lyr3;
		if (opts.theme && full) {
			var s = '<div class="blockUI blockMsg blockPage ui-dialog ui-widget ui-corner-all" style="z-index:'
					+ z
					+ ';display:none;position:fixed">'
					+ '<div class="ui-widget-header ui-dialog-titlebar blockTitle">'
					+ (opts.title || '&nbsp;')
					+ '</div>'
					+ '<div class="ui-widget-content ui-dialog-content"></div>'
					+ '</div>';
			lyr3 = $(s);
		} else {
			lyr3 = full
					? $('<div class="blockUI blockMsg blockPage" style="z-index:'
							+ z + ';display:none;position:fixed"></div>')
					: $('<div class="blockUI blockMsg blockElement" style="z-index:'
							+ z + ';display:none;position:absolute"></div>');
		}

		// if we have a message, style it
		if (msg) {
			if (opts.theme) {
				lyr3.css(themedCSS);
				lyr3.addClass('ui-widget-content');
			} else
				lyr3.css(css);
		}

		// style the overlay
		if (!opts.applyPlatformOpacityRules
				|| !($.browser.mozilla && /Linux/.test(navigator.platform)))
			lyr2.css(opts.overlayCSS);
		lyr2.css('position', full ? 'fixed' : 'absolute');

		// make iframe layer transparent in IE
		if ($.browser.msie || opts.forceIframe)
			lyr1.css('opacity', 0.0);

		// $([lyr1[0],lyr2[0],lyr3[0]]).appendTo(full ? 'body' : el);
		var layers = [lyr1, lyr2, lyr3], $par = full ? $('body') : $(el);
		$.each(layers, function() {
			this.appendTo($par);
		});

		if (opts.theme && opts.draggable && $.fn.draggable) {
			lyr3.draggable({
				handle : '.ui-dialog-titlebar',
				cancel : 'li'
			});
		}

		// ie7 must use absolute positioning in quirks mode and to account for
		// activex issues (when scrolling)
		var expr = setExpr
				&& (!$.boxModel || $('object,embed', full ? null : el).length > 0);
		if (ie6 || expr) {
			// give body 100% height
			if (full && opts.allowBodyStretch && $.boxModel)
				$('html,body').css('height', '100%');

			// fix ie6 issue when blocked element has a border width
			if ((ie6 || !$.boxModel) && !full) {
				var t = sz(el, 'borderTopWidth'), l = sz(el, 'borderLeftWidth');
				var fixT = t ? '(0 - ' + t + ')' : 0;
				var fixL = l ? '(0 - ' + l + ')' : 0;
			}

			// simulate fixed position
			$.each([lyr1, lyr2, lyr3], function(i, o) {
				var s = o[0].style;
				s.position = 'absolute';
				if (i < 2) {
					full
							? s
									.setExpression(
											'height',
											'Math.max(document.body.scrollHeight, document.body.offsetHeight) - (jQuery.boxModel?0:'
													+ opts.quirksmodeOffsetHack
													+ ') + "px"')
							: s.setExpression('height',
									'this.parentNode.offsetHeight + "px"');
					full
							? s
									.setExpression(
											'width',
											'jQuery.boxModel && document.documentElement.clientWidth || document.body.clientWidth + "px"')
							: s.setExpression('width',
									'this.parentNode.offsetWidth + "px"');
					if (fixL)
						s.setExpression('left', fixL);
					if (fixT)
						s.setExpression('top', fixT);
				} else if (opts.centerY) {
					if (full)
						s
								.setExpression(
										'top',
										'(document.documentElement.clientHeight || document.body.clientHeight) / 2 - (this.offsetHeight / 2) + (blah = document.documentElement.scrollTop ? document.documentElement.scrollTop : document.body.scrollTop) + "px"');
					s.marginTop = 0;
				} else if (!opts.centerY && full) {
					var top = (opts.css && opts.css.top)
							? parseInt(opts.css.top)
							: 0;
					var expression = '((document.documentElement.scrollTop ? document.documentElement.scrollTop : document.body.scrollTop) + '
							+ top + ') + "px"';
					s.setExpression('top', expression);
				}
			});
		}

		// show the message
		if (msg) {
			if (opts.theme)
				lyr3.find('.ui-widget-content').append(msg);
			else
				lyr3.append(msg);
			if (msg.jquery || msg.nodeType)
				$(msg).show();
		}

		if (($.browser.msie || opts.forceIframe) && opts.showOverlay)
			lyr1.show(); // opacity is zero
		if (opts.fadeIn) {
			var cb = opts.onBlock ? opts.onBlock : noOp;
			var cb1 = (opts.showOverlay && !msg) ? cb : noOp;
			var cb2 = msg ? cb : noOp;
			if (opts.showOverlay)
				lyr2._fadeIn(opts.fadeIn, cb1);
			if (msg)
				lyr3._fadeIn(opts.fadeIn, cb2);
		} else {
			if (opts.showOverlay)
				lyr2.show();
			if (msg)
				lyr3.show();
			if (opts.onBlock)
				opts.onBlock();
		}

		// bind key and mouse events
		bind(1, el, opts);

		if (full) {
			pageBlock = lyr3[0];
			pageBlockEls = $(':input:enabled:visible', pageBlock);
			if (opts.focusInput)
				setTimeout(focus, 20);
		} else
			center(lyr3[0], opts.centerX, opts.centerY);

		if (opts.timeout) {
			// auto-unblock
			var to = setTimeout(function() {
				full ? $.unblockUI(opts) : $(el).unblock(opts);
			}, opts.timeout);
			$(el).data('blockUI.timeout', to);
		}
	};

	// remove the block
	function remove(el, opts) {
		var full = (el == window);
		var $el = $(el);
		var data = $el.data('blockUI.history');
		var to = $el.data('blockUI.timeout');
		if (to) {
			clearTimeout(to);
			$el.removeData('blockUI.timeout');
		}
		opts = $.extend({}, $.blockUI.defaults, opts || {});
		bind(0, el, opts); // unbind events

		var els;
		if (full) // crazy selector to handle odd field errors in ie6/7
			els = $('body').children().filter('.blockUI')
					.add('body > .blockUI');
		else
			els = $('.blockUI', el);

		if (full)
			pageBlock = pageBlockEls = null;

		if (opts.fadeOut) {
			els.fadeOut(opts.fadeOut);
			setTimeout(function() {
				reset(els, data, opts, el);
			}, opts.fadeOut);
		} else
			reset(els, data, opts, el);
	};

	// move blocking element back into the DOM where it started
	function reset(els, data, opts, el) {
		els.each(function(i, o) {
			// remove via DOM calls so we don't lose event handlers
			if (this.parentNode)
				this.parentNode.removeChild(this);
		});

		if (data && data.el) {
			data.el.style.display = data.display;
			data.el.style.position = data.position;
			if (data.parent)
				data.parent.appendChild(data.el);
			$(el).removeData('blockUI.history');
		}

		if (typeof opts.onUnblock == 'function')
			opts.onUnblock(el, opts);
	};

	// bind/unbind the handler
	function bind(b, el, opts) {
		var full = el == window, $el = $(el);

		// don't bother unbinding if there is nothing to unbind
		if (!b
				&& (full && !pageBlock || !full
						&& !$el.data('blockUI.isBlocked')))
			return;
		if (!full)
			$el.data('blockUI.isBlocked', b);

		// don't bind events when overlay is not in use or if bindEvents is
		// false
		if (!opts.bindEvents || (b && !opts.showOverlay))
			return;

		// bind anchors and inputs for mouse and key events
		var events = 'mousedown mouseup keydown keypress';
		b ? $(document).bind(events, opts, handler) : $(document).unbind(
				events, handler);

		// former impl...
		// var $e = $('a,:input');
		// b ? $e.bind(events, opts, handler) : $e.unbind(events, handler);
	};

	// event handler to suppress keyboard/mouse events when blocking
	function handler(e) {
		// allow tab navigation (conditionally)
		if (e.keyCode && e.keyCode == 9) {
			if (pageBlock && e.data.constrainTabKey) {
				var els = pageBlockEls;
				var fwd = !e.shiftKey && e.target == els[els.length - 1];
				var back = e.shiftKey && e.target == els[0];
				if (fwd || back) {
					setTimeout(function() {
						focus(back)
					}, 10);
					return false;
				}
			}
		}
		// allow events within the message content
		if ($(e.target).parents('div.blockMsg').length > 0)
			return true;

		// allow events for content that is not being blocked
		return $(e.target).parents().children().filter('div.blockUI').length == 0;
	};

	function focus(back) {
		if (!pageBlockEls)
			return;
		var e = pageBlockEls[back === true ? pageBlockEls.length - 1 : 0];
		if (e)
			e.focus();
	};

	function center(el, x, y) {
		var p = el.parentNode, s = el.style;
		var l = ((p.offsetWidth - el.offsetWidth) / 2)
				- sz(p, 'borderLeftWidth');
		var t = ((p.offsetHeight - el.offsetHeight) / 2)
				- sz(p, 'borderTopWidth');
		if (x)
			s.left = l > 0 ? (l + 'px') : '0';
		if (y)
			s.top = t > 0 ? (t + 'px') : '0';
	};

	function sz(el, p) {
		return parseInt($.css(el, p)) || 0;
	};

})(jQuery);

/** JqueryUI公共部分 * */
/*
 * ! jQuery UI 1.8.1
 * 
 * Copyright (c) 2010 AUTHORS.txt (http://jqueryui.com/about) Dual licensed
 * under the MIT (MIT-LICENSE.txt) and GPL (GPL-LICENSE.txt) licenses.
 * 
 * http://docs.jquery.com/UI
 */
;
jQuery.ui || (function($) {

	// Helper functions and ui object
	$.ui = {
		version : "1.8.1",

		// $.ui.plugin is deprecated. Use the proxy pattern instead.
		plugin : {
			add : function(module, option, set) {
				var proto = $.ui[module].prototype;
				for (var i in set) {
					proto.plugins[i] = proto.plugins[i] || [];
					proto.plugins[i].push([option, set[i]]);
				}
			},
			call : function(instance, name, args) {
				var set = instance.plugins[name];
				if (!set || !instance.element[0].parentNode) {
					return;
				}

				for (var i = 0; i < set.length; i++) {
					if (instance.options[set[i][0]]) {
						set[i][1].apply(instance.element, args);
					}
				}
			}
		},
		// 判断节点a是否包含节点b （关于compareDocumentPosition和contains 用法见
		// http://www.cnblogs.com/siceblue/archive/2010/02/02/1661833.html）
		// 与16按位于的原因是，如果包含 值为20 即返回true; 如果不包含值为 0 即返回false
		contains : function(a, b) {
			return document.compareDocumentPosition ? a
					.compareDocumentPosition(b)
					& 16 : a !== b && a.contains(b);
		},
		// 判断元素的sroll 在top或者left方向是是否有滚动
		hasScroll : function(el, a) {

			// If overflow is hidden, the element might have extra content, but
			// the user wants to hide it
			if ($(el).css('overflow') == 'hidden') {
				return false;
			}

			var scroll = (a && a == 'left') ? 'scrollLeft' : 'scrollTop', has = false;

			if (el[scroll] > 0) {
				return true;
			}

			// TODO: determine which cases actually cause this to happen
			// if the element doesn't have the scroll set, see if it's possible
			// to
			// set the scroll
			el[scroll] = 1;
			has = (el[scroll] > 0);
			el[scroll] = 0;
			return has;
		},
		// 确定x坐标是否在元素内部
		// x：要确认的坐标；reference：参考坐标；size：元素宽度
		isOverAxis : function(x, reference, size) {
			// Determines when x coordinate is over "b" element axis
			return (x > reference) && (x < (reference + size));
		},
		// 确定x、y坐标是否同事在元素内部
		// x、y：坐标；top、left：元素坐标；height、width：元素宽高
		isOver : function(y, x, top, left, height, width) {
			// Determines when x, y coordinates is over "b" element
			return $.ui.isOverAxis(y, top, height)
					&& $.ui.isOverAxis(x, left, width);
		},

		keyCode : {
			ALT : 18,
			BACKSPACE : 8,
			CAPS_LOCK : 20,
			COMMA : 188,
			CONTROL : 17,
			DELETE : 46,
			DOWN : 40,
			END : 35,
			ENTER : 13,
			ESCAPE : 27,
			HOME : 36,
			INSERT : 45,
			LEFT : 37,
			NUMPAD_ADD : 107,
			NUMPAD_DECIMAL : 110,
			NUMPAD_DIVIDE : 111,
			NUMPAD_ENTER : 108,
			NUMPAD_MULTIPLY : 106,
			NUMPAD_SUBTRACT : 109,
			PAGE_DOWN : 34,
			PAGE_UP : 33,
			PERIOD : 190,
			RIGHT : 39,
			SHIFT : 16,
			SPACE : 32,
			TAB : 9,
			UP : 38
		}
	};

	// jQuery plugins
	$.fn.extend({
		_focus : $.fn.focus,
		// 设置元素焦点（delay：延迟时间）
		focus : function(delay, fn) {
			return typeof delay === 'number' ? this.each(function() {
				var elem = this;
				setTimeout(function() {
					$(elem).focus();
					(fn && fn.call(elem));
				}, delay);
			}) : this._focus.apply(this, arguments);
		},
		// 设置元素支持被选择
		enableSelection : function() {
			return this.attr('unselectable', 'off').css('MozUserSelect', '');
		},
		// 设置元素不支持被选择
		disableSelection : function() {
			return this.attr('unselectable', 'on').css('MozUserSelect', 'none');
		},
		// 获取设置滚动属性的 父元素
		scrollParent : function() {
			var scrollParent;
			if (($.browser.msie && (/(static|relative)/).test(this
					.css('position')))
					|| (/absolute/).test(this.css('position'))) {
				scrollParent = this.parents().filter(function() {
					return (/(relative|absolute|fixed)/).test($.curCSS(this,
							'position', 1))
							&& (/(auto|scroll)/).test($.curCSS(this,
									'overflow', 1)
									+ $.curCSS(this, 'overflow-y', 1)
									+ $.curCSS(this, 'overflow-x', 1));
				}).eq(0);
			} else {
				scrollParent = this.parents().filter(function() {
					return (/(auto|scroll)/).test($.curCSS(this, 'overflow', 1)
							+ $.curCSS(this, 'overflow-y', 1)
							+ $.curCSS(this, 'overflow-x', 1));
				}).eq(0);
			}

			return (/fixed/).test(this.css('position')) || !scrollParent.length
					? $(document)
					: scrollParent;
		},
		// 设置或获取元素的垂直坐标
		zIndex : function(zIndex) {
			if (zIndex !== undefined) {
				return this.css('zIndex', zIndex);
			}

			if (this.length) {
				var elem = $(this[0]), position, value;
				while (elem.length && elem[0] !== document) {
					// Ignore z-index if position is set to a value where
					// z-index is ignored by the browser
					// This makes behavior of this function consistent across
					// browsers
					// WebKit always returns auto if the element is positioned
					position = elem.css('position');
					if (position == 'absolute' || position == 'relative'
							|| position == 'fixed') {
						// IE returns 0 when zIndex is not specified
						// other browsers return a string
						// we ignore the case of nested elements with an
						// explicit value of 0
						// <div style="z-index: -10;"><div style="z-index:
						// 0;"></div></div>
						value = parseInt(elem.css('zIndex'));
						if (!isNaN(value) && value != 0) {
							return value;
						}
					}
					elem = elem.parent();
				}
			}

			return 0;
		}
	});

	// Additional selectors
	// jQuery.expr[":"] = jQuery.expr.filters; 扩展jQuery.expr.filters
	// 的筛选方法，在jquery-1.4.1.js中有其他方法
	$.extend($.expr[':'], {
		data : function(elem, i, match) {
			return !!$.data(elem, match[3]);
		},

		focusable : function(element) {
			var nodeName = element.nodeName.toLowerCase(), tabIndex = $.attr(
					element, 'tabindex');
			return (/input|select|textarea|button|object/.test(nodeName)
					? !element.disabled
					: 'a' == nodeName || 'area' == nodeName ? element.href
							|| !isNaN(tabIndex) : !isNaN(tabIndex))
					// the element and all of its ancestors must be visible
					// the browser may report that the area is hidden
					&& !$(element)['area' == nodeName ? 'parents' : 'closest'](':hidden').length;
		},

		tabbable : function(element) {
			var tabIndex = $.attr(element, 'tabindex');
			return (isNaN(tabIndex) || tabIndex >= 0)
					&& $(element).is(':focusable');
		}
	});

})(jQuery);

/*
 * ! jQuery UI Widget 1.8.1
 * 
 * Copyright (c) 2010 AUTHORS.txt (http://jqueryui.com/about) Dual licensed
 * under the MIT (MIT-LICENSE.txt) and GPL (GPL-LICENSE.txt) licenses.
 * 
 * http://docs.jquery.com/UI/Widget
 */
(function($) {

	var _remove = $.fn.remove;

	$.fn.remove = function(selector, keepData) {
		return this.each(function() {
			if (!keepData) {
				if (!selector || $.filter(selector, [this]).length) {
					$("*", this).add(this).each(function() {
						$(this).triggerHandler("remove");
					});
				}
			}
			return _remove.call($(this), selector, keepData);
		});
	};

	$.widget = function(name, base, prototype) {
		var namespace = name.split(".")[0], fullName;
		name = name.split(".")[1];
		fullName = namespace + "-" + name;

		if (!prototype) {
			prototype = base;
			base = $.Widget;
		}

		// create selector for plugin
		$.expr[":"][fullName] = function(elem) {
			return !!$.data(elem, name);
		};

		$[namespace] = $[namespace] || {};
		$[namespace][name] = function(options, element) {
			// allow instantiation without initializing for simple inheritance
			if (arguments.length) {
				this._createWidget(options, element);
			}
		};

		var basePrototype = new base();
		// we need to make the options hash a property directly on the new
		// instance
		// otherwise we'll modify the options hash on the prototype that we're
		// inheriting from
		// $.each( basePrototype, function( key, val ) {
		// if ( $.isPlainObject(val) ) {
		// basePrototype[ key ] = $.extend( {}, val );
		// }
		// });
		basePrototype.options = $.extend({}, basePrototype.options);
		$[namespace][name].prototype = $.extend(true, basePrototype, {
			namespace : namespace,
			widgetName : name,
			widgetEventPrefix : $[namespace][name].prototype.widgetEventPrefix
					|| name,
			widgetBaseClass : fullName
		}, prototype);

		$.widget.bridge(name, $[namespace][name]);
	};

	$.widget.bridge = function(name, object) {
		$.fn[name] = function(options) {
			var isMethodCall = typeof options === "string", args = Array.prototype.slice
					.call(arguments, 1), returnValue = this;

			// allow multiple hashes to be passed on init
			options = !isMethodCall && args.length ? $.extend.apply(null, [
					true, options].concat(args)) : options;

			// prevent calls to internal methods
			if (isMethodCall && options.substring(0, 1) === "_") {
				return returnValue;
			}

			if (isMethodCall) {
				this.each(function() {
					var instance = $.data(this, name), methodValue = instance
							&& $.isFunction(instance[options])
							? instance[options].apply(instance, args)
							: instance;
					if (methodValue !== instance && methodValue !== undefined) {
						returnValue = methodValue;
						return false;
					}
				});
			} else {
				this.each(function() {
					var instance = $.data(this, name);
					if (instance) {
						if (options) {
							instance.option(options);
						}
						instance._init();
					} else {
						$.data(this, name, new object(options, this));
					}
				});
			}

			return returnValue;
		};
	};

	$.Widget = function(options, element) {
		// allow instantiation without initializing for simple inheritance
		if (arguments.length) {
			this._createWidget(options, element);
		}
	};

	$.Widget.prototype = {
		widgetName : "widget",
		widgetEventPrefix : "",
		options : {
			disabled : false
		},
		_createWidget : function(options, element) {
			// $.widget.bridge stores the plugin instance, but we do it anyway
			// so that it's stored even before the _create function runs
			this.element = $(element).data(this.widgetName, this);
			this.options = $.extend(true, {}, this.options, $.metadata
					&& $.metadata.get(element)[this.widgetName], options);

			var self = this;
			this.element.bind("remove." + this.widgetName, function() {
				self.destroy();
			});

			this._create();
			this._init();
		},
		_create : function() {
		},
		_init : function() {
		},

		destroy : function() {
			this.element.unbind("." + this.widgetName)
					.removeData(this.widgetName);
			this.widget().unbind("." + this.widgetName)
					.removeAttr("aria-disabled")
					.removeClass(this.widgetBaseClass + "-disabled "
							+ "ui-state-disabled");
		},

		widget : function() {
			return this.element;
		},

		option : function(key, value) {
			var options = key, self = this;

			if (arguments.length === 0) {
				// don't return a reference to the internal hash
				return $.extend({}, self.options);
			}

			if (typeof key === "string") {
				if (value === undefined) {
					return this.options[key];
				}
				options = {};
				options[key] = value;
			}

			$.each(options, function(key, value) {
				self._setOption(key, value);
			});

			return self;
		},
		_setOption : function(key, value) {
			this.options[key] = value;

			if (key === "disabled") {
				this.widget()[value ? "addClass" : "removeClass"](this.widgetBaseClass
						+ "-disabled" + " " + "ui-state-disabled").attr(
						"aria-disabled", value);
			}

			return this;
		},

		enable : function() {
			return this._setOption("disabled", false);
		},
		disable : function() {
			return this._setOption("disabled", true);
		},

		_trigger : function(type, event, data) {
			var callback = this.options[type];

			event = $.Event(event);
			event.type = (type === this.widgetEventPrefix
					? type
					: this.widgetEventPrefix + type).toLowerCase();
			data = data || {};

			// copy original event properties over to the new event
			// this would happen if we could call $.event.fix instead of $.Event
			// but we don't have a way to force an event to be fixed multiple
			// times
			if (event.originalEvent) {
				for (var i = $.event.props.length, prop; i;) {
					prop = $.event.props[--i];
					event[prop] = event.originalEvent[prop];
				}
			}

			this.element.trigger(event, data);

			return !($.isFunction(callback)
					&& callback.call(this.element[0], event, data) === false || event
					.isDefaultPrevented());
		}
	};

})(jQuery);

/*
 * jQuery UI Accordion 1.8.1
 * 
 * Copyright (c) 2010 AUTHORS.txt (http://jqueryui.com/about) Dual licensed
 * under the MIT (MIT-LICENSE.txt) and GPL (GPL-LICENSE.txt) licenses.
 * 
 * http://docs.jquery.com/UI/Accordion
 * 
 * Depends: jquery.ui.core.js jquery.ui.widget.js
 */
(function($) {

	$.widget("ui.accordion", {
		options : {
			active : 0,
			animated : 'slide',
			autoHeight : true,
			clearStyle : false,
			collapsible : true,
			data : false,// 要展现的导航菜单数据，格式为[{id:"",pid:"",name:"",url:""}]
			wholeCss : "accor",// 导航菜单整体样式，组件的样式切换由此样式控制
			event : "click",
			fillSpace : false,
			header : "> li > :first-child,> :not(li):even",
			icons : {
				header : "ui-icon-triangle-1-e",
				headerSelected : "ui-icon-triangle-1-s"
			},
			hideIcons: false,//当header下没有子级菜单时是否隐藏header前面的图标，当icons不为false时有效
			hideDiv: false,//当header下没有子级菜单时点击header时是否展现空白的子级div，当fillSpace为true时有效
			headerNavigation : false,//点击header是否能触发链接事件
			navigation : false,
			navigationFilter : function() {
				return this.href.toLowerCase() == location.href.toLowerCase();
			}
		},
		_create : function() {
			var o = this.options, self = this;
			this.running = 0;
						if (o.data) {// 如果需要根据options.data进行初始化数据构造
				var fData, sData, appendHtml = "", ulAppendHtml = "", liAppendHtml, classAppendHtml, hasChild;
				for (var i = 0; i < o.data.length; i++) {
					hasChild = false;//标志该header是否有子级菜单
					liAppendHtml = ">";
					classAppendHtml = "";
					fData = o.data[i];
					if (fData.pid == "-1") {
						appendHtml = appendHtml + "<h3";
						ulAppendHtml = "><a onfocus='this.blur()';  href=\"" + fData.url + "\">" + fData.name + "</a></h3><div><ul";
						for (var j = 0; j < o.data.length; j++) {
							sData = o.data[j];
							if (sData.pid == fData.id) {
								hasChild = true;//具有子级菜单
								liAppendHtml = liAppendHtml + "<li><a onfocus='this.blur()' href=\""
										+ sData.url + "\">" + sData.name
										+ "</a></li>";
							}
						}
						
						if(!hasChild && o.hideIcons){//没有子级菜单并且需要隐藏图标时
							classAppendHtml = classAppendHtml + " header-noicon ";
						}
						
						if(!hasChild && o.hideDiv){//没有子级菜单并且需要隐藏div时
							classAppendHtml = classAppendHtml + " header-nodiv ";
						}
						
						if(fData.url == null || fData.url == "" || fData.url == "null"){
							classAppendHtml = classAppendHtml + " header-nourl ";
						}
						
						appendHtml = appendHtml + " class='" + classAppendHtml + "'" + ulAppendHtml + liAppendHtml;
						appendHtml = appendHtml + "</ul></div>";
					}
				}
				this.element.append($(appendHtml));
			}
			// this.element.addClass("ui-accordion ui-widget ui-helper-reset");
			this.element.addClass(o.wholeCss);

			// in lack of child-selectors in CSS we need to mark top-LIs in a
			// UL-accordion for some IE-fix
			if (this.element[0].nodeName == "UL") {
				// this.element.children("li").addClass("ui-accordion-li-fix");
				this.element.children("li").addClass("li-fix");
			}

			// this.headers =
			// this.element.find(o.header).addClass("ui-accordion-header
			// ui-helper-reset ui-state-default ui-corner-all")
			this.headers = this.element.find(o.header)
					.addClass("accordion-header")
					// .bind("mouseenter.accordion", function(){
					// $(this).addClass('ui-state-hover'); })
					// .bind("mouseleave.accordion", function(){
					// $(this).removeClass('ui-state-hover'); })
					// .bind("focus.accordion", function(){
					// $(this).addClass('ui-state-focus'); })
					// .bind("blur.accordion", function(){
					// $(this).removeClass('ui-state-focus'); });
					.bind("mouseenter.accordion", function() {
						$(this).addClass('state-hover');
					}).bind("mouseleave.accordion", function() {
						$(this).removeClass('state-hover');
					}).bind("focus.accordion", function() {
						$(this).addClass('state-focus');
					}).bind("blur.accordion", function() {
						$(this).removeClass('state-focus');
					});

			this.headers.next()
					// .addClass("ui-accordion-content ui-helper-reset
					// ui-widget-content ui-corner-bottom");
					.addClass("accordion-content").css('overflow', 'auto');
			
			this.subs = this.headers.next().find("li");//找到所有子级菜单
			this.subs.bind("click", {subs: this.subs}, function(event){
				var oldActive = event.data.subs.filter(function(){
					return $(this).hasClass("active");
				});
				oldActive.removeClass("active");
				$(this).addClass("active");
			});

			if (o.navigation) {
				var current = this.element.find("a").filter(o.navigationFilter);
				if (current.length) {
					var header = current.closest(".ui-accordion-header");
					if (header.length) {
						// anchor within header
						this.active = header;
					} else {
						// anchor within content
						this.active = current.closest(".ui-accordion-content")
								.prev();
					}
				}
			}

			// this.active = this._findActive(this.active ||
			// o.active).toggleClass("ui-state-default").toggleClass("ui-state-active").toggleClass("ui-corner-all").toggleClass("ui-corner-top");
			this.active = this._findActive(this.active || o.active)
					.toggleClass("state-active");
			// this.active.next().addClass('ui-accordion-content-active');
			this.active.next().addClass('accordion-content-active');

			// Append icon elements
			this._createIcons();

			this.resize();

			// ARIA
			this.element.attr('role', 'tablist');

			this.headers.attr('role', 'tab').bind('keydown', function(event) {
				return self._keydown(event);
			}).next().attr('role', 'tabpanel');

			this.headers.not(this.active || "").attr('aria-expanded', 'false')
					.attr("tabIndex", "-1").next().hide();

			// make sure at least one header is in the tab order
			if (!this.active.length) {
				this.headers.eq(0).attr('tabIndex', '0');
			} else {
				this.active.attr('aria-expanded', 'true').attr('tabIndex', '0');
			}

			// only need links in taborder for Safari
			if (!$.browser.safari)
				this.headers.find('a').attr('tabIndex', '-1');

			if (o.event) {
				this.headers.bind((o.event) + ".accordion", function(event) {
					self._clickHandler.call(self, event, this);
					if(!o.headerNavigation || $(this).hasClass("header-nourl")){//header无法触发链接事件
						event.preventDefault();
					}
				});
			}

		},

		_createIcons : function() {
			var o = this.options;
			if (o.icons) {
				// $("<span/>").addClass("ui-icon " +
				// o.icons.header).prependTo(this.headers);
				// this.active.find(".ui-icon").toggleClass(o.icons.header).toggleClass(o.icons.headerSelected);
				// this.element.addClass("ui-accordion-icons");
				$("<span/>").addClass("icon_tree_menu").prependTo(this.headers);
			}
		},

		_destroyIcons : function() {
			// this.headers.children(".ui-icon").remove();
			this.headers.children(".icon_tree_menu").remove();
			// this.element.removeClass("ui-accordion-icons");
		},

		destroy : function() {
			var o = this.options;

			this.element
					// .removeClass("ui-accordion ui-widget ui-helper-reset")
					.removeClass(o.wholeCss).removeAttr("role")
					.unbind('.accordion').removeData('accordion');

			this.headers
					.unbind(".accordion")
					// .removeClass("ui-accordion-header ui-helper-reset
					// ui-state-default ui-corner-all ui-state-active
					// ui-corner-top")
					.removeClass("accordion-header state-active")
					.removeAttr("role").removeAttr("aria-expanded")
					.removeAttr("tabIndex");

			this.headers.find("a").removeAttr("tabIndex");
			this._destroyIcons();
			// var contents = this.headers.next().css("display",
			// "").removeAttr("role").removeClass("ui-helper-reset
			// ui-widget-content ui-corner-bottom ui-accordion-content
			// ui-accordion-content-active");
			var contents = this.headers.next().css("display", "")
					.removeAttr("role")
					.removeClass("accordion-content accordion-content-active");
			if (o.autoHeight || o.fillHeight) {
				contents.css("height", "");
			}

			return this;
		},

		_setOption : function(key, value) {
			$.Widget.prototype._setOption.apply(this, arguments);

			if (key == "active") {
				this.activate(value);
			}
			if (key == "icons") {
				this._destroyIcons();
				if (value) {
					this._createIcons();
				}
			}

		},

		_keydown : function(event) {

			var o = this.options, keyCode = $.ui.keyCode;

			if (o.disabled || event.altKey || event.ctrlKey)
				return;

			var length = this.headers.length;
			var currentIndex = this.headers.index(event.target);
			var toFocus = false;

			switch (event.keyCode) {
				case keyCode.RIGHT :
				case keyCode.DOWN :
					toFocus = this.headers[(currentIndex + 1) % length];
					break;
				case keyCode.LEFT :
				case keyCode.UP :
					toFocus = this.headers[(currentIndex - 1 + length) % length];
					break;
				case keyCode.SPACE :
				case keyCode.ENTER :
					this._clickHandler({
						target : event.target
					}, event.target);
					event.preventDefault();
			}

			if (toFocus) {
				$(event.target).attr('tabIndex', '-1');
				$(toFocus).attr('tabIndex', '0');
				toFocus.focus();
				return false;
			}

			return true;

		},

		resize : function() {

			var o = this.options, maxHeight;

			if (o.fillSpace) {

				if ($.browser.msie) {
					var defOverflow = this.element.parent().css('overflow');
					this.element.parent().css('overflow', 'hidden');
				}
				maxHeight = this.element.parent().height();
				if ($.browser.msie) {
					this.element.parent().css('overflow', defOverflow);
				}

				this.headers.each(function() {
					maxHeight -= $(this).outerHeight(true);
				});
				
				this.headers.next().each(function() {
					if($(this).prev().hasClass("header-nodiv")){//没有子级菜单
						
					}else{
						$(this).height(Math.max(0, maxHeight
								- $(this).innerHeight() + $(this).height()));
					}
				}).css('overflow', 'auto');

			} else if (o.autoHeight) {
				maxHeight = 0;
				this.headers.next().each(function() {
					maxHeight = Math.max(maxHeight, $(this).height());
				}).height(maxHeight);
			}

			return this;
		},

		activate : function(index) {
			// TODO this gets called on init, changing the option without an
			// explicit call for that
			this.options.active = index;
			// call clickHandler with custom event
			var active = this._findActive(index)[0];
			this._clickHandler({
				target : active
			}, active);

			return this;
		},

		_findActive : function(selector) {
			return selector ? typeof selector == "number" ? this.headers
					.filter(":eq(" + selector + ")") : this.headers
					.not(this.headers.not(selector)) : selector === false
					? $([])
					: this.headers.filter(":eq(0)");
		},

		// TODO isn't event.target enough? why the seperate target argument?
		_clickHandler : function(event, target) {

			var o = this.options;
			if (o.disabled)
				return;

			// called only when using activate(false) to close all parts
			// programmatically
			if (!event.target) {
				if (!o.collapsible)
					return;
				// this.active.removeClass("ui-state-active
				// ui-corner-top").addClass("ui-state-default ui-corner-all")
				// .find(".ui-icon").removeClass(o.icons.headerSelected).addClass(o.icons.header);
				this.active.removeClass("state-active");
				// this.active.next().addClass('ui-accordion-content-active');
				this.active.next().addClass('accordion-content-active');
				var toHide = this.active.next(), data = {
					options : o,
					newHeader : $([]),
					oldHeader : o.active,
					newContent : $([]),
					oldContent : toHide
				}, toShow = (this.active = $([]));
				this._toggle(toShow, toHide, data);
				return;
			}

			// get the click target
			var clicked = $(event.currentTarget || target);
			var clickedIsActive = clicked[0] == this.active[0];

			// TODO the option is changed, is that correct?
			// TODO if it is correct, shouldn't that happen after determining
			// that the click is valid?
			// o.active = o.collapsible && clickedIsActive ? false :
			// $('.ui-accordion-header', this.element).index(clicked);
			o.active = o.collapsible && clickedIsActive ? false : $(
					'.accordion-header', this.element).index(clicked);

			// if animations are still active, or the active header is the
			// target, ignore click
			if (this.running || (!o.collapsible && clickedIsActive)) {
				return;
			}

			// switch classes
			// this.active.removeClass("ui-state-active
			// ui-corner-top").addClass("ui-state-default ui-corner-all")
			// .find(".ui-icon").removeClass(o.icons.headerSelected).addClass(o.icons.header);
			this.active.removeClass("state-active");
			if (!clickedIsActive) {
				// clicked.removeClass("ui-state-default
				// ui-corner-all").addClass("ui-state-active ui-corner-top")
				// .find(".ui-icon").removeClass(o.icons.header).addClass(o.icons.headerSelected);
				// clicked.next().addClass('ui-accordion-content-active');
				clicked.addClass("state-active");
				clicked.next().addClass('accordion-content-active');
			}

			// find elements to show and hide
			var toShow = clicked.next(), toHide = this.active.next(), data = {
				options : o,
				newHeader : clickedIsActive && o.collapsible ? $([]) : clicked,
				oldHeader : this.active,
				newContent : clickedIsActive && o.collapsible ? $([]) : toShow,
				oldContent : toHide
			}, down = this.headers.index(this.active[0]) > this.headers
					.index(clicked[0]);

			this.active = clickedIsActive ? $([]) : clicked;
			this._toggle(toShow, toHide, data, clickedIsActive, down);

			return;

		},

		_toggle : function(toShow, toHide, data, clickedIsActive, down) {

			var o = this.options, self = this;

			this.toShow = toShow;
			this.toHide = toHide;
			this.data = data;

			var complete = function() {
				if (!self)
					return;
				return self._completed.apply(self, arguments);
			};

			// trigger changestart event
			this._trigger("changestart", null, this.data);

			// count elements to animate
			this.running = toHide.size() === 0 ? toShow.size() : toHide.size();

			if (o.animated) {

				var animOptions = {};

				if (o.collapsible && clickedIsActive) {
					animOptions = {
						toShow : $([]),
						toHide : toHide,
						complete : complete,
						down : down,
						autoHeight : o.autoHeight || o.fillSpace
					};
				} else {
					animOptions = {
						toShow : toShow,
						toHide : toHide,
						complete : complete,
						down : down,
						autoHeight : o.autoHeight || o.fillSpace
					};
				}

				if (!o.proxied) {
					o.proxied = o.animated;
				}

				if (!o.proxiedDuration) {
					o.proxiedDuration = o.duration;
				}

				o.animated = $.isFunction(o.proxied)
						? o.proxied(animOptions)
						: o.proxied;

				o.duration = $.isFunction(o.proxiedDuration) ? o
						.proxiedDuration(animOptions) : o.proxiedDuration;

				var animations = $.ui.accordion.animations, duration = o.duration, easing = o.animated;

				if (easing && !animations[easing] && !$.easing[easing]) {
					easing = 'slide';
				}
				if (!animations[easing]) {
					animations[easing] = function(options) {
						this.slide(options, {
							easing : easing,
							duration : duration || 700
						});
					};
				}

				animations[easing](animOptions);

			} else {

				if (o.collapsible && clickedIsActive) {
					toShow.toggle();
				} else {
					toHide.hide();
					toShow.show();
				}

				complete(true);

			}

			// TODO assert that the blur and focus triggers are really
			// necessary, remove otherwise
			toHide.prev().attr('aria-expanded', 'false').attr("tabIndex", "-1")
					.blur();
			toShow.prev().attr('aria-expanded', 'true').attr("tabIndex", "0")
					.focus();

		},

		_completed : function(cancel) {

			var o = this.options;

			this.running = cancel ? 0 : --this.running;
			if (this.running)
				return;

			if (o.clearStyle) {
				this.toShow.add(this.toHide).css({
					height : "",
					overflow : ""
				});
			}

			// other classes are removed before the animation; this one needs to
			// stay until completed
			this.toHide.removeClass("accordion-content-active");

			this._trigger('change', null, this.data);
		}

	});

	$.extend($.ui.accordion, {
		version : "1.8.1",
		animations : {
			slide : function(options, additions) {
				options = $.extend({
					easing : "swing",
					duration : 300
				}, options, additions);
				if (!options.toHide.size()) {
					options.toShow.animate({
						height : "show"
					}, options);
					return;
				}
				if (!options.toShow.size()) {
					options.toHide.animate({
						height : "hide"
					}, options);
					return;
				}
				var overflow = options.toShow.css('overflow'), percentDone = 0, showProps = {}, hideProps = {}, fxAttrs = [
						"height", "paddingTop", "paddingBottom"], originalWidth;
				// fix width before calculating height of hidden element
				var s = options.toShow;
				originalWidth = s[0].style.width;
				s.width(parseInt(s.parent().width(), 10)
						- parseInt(s.css("paddingLeft"), 10)
						- parseInt(s.css("paddingRight"), 10)
						- (parseInt(s.css("borderLeftWidth"), 10) || 0)
						- (parseInt(s.css("borderRightWidth"), 10) || 0));
				$.each(fxAttrs, function(i, prop) {
					hideProps[prop] = 'hide';

					var parts = ('' + $.css(options.toShow[0], prop))
							.match(/^([\d+-.]+)(.*)$/);
					showProps[prop] = {
						value : parts[1],
						unit : parts[2] || 'px'
					};
				});
				options.toShow.css({
					height : 0,
					overflow : 'hidden'
				}).show();
				options.toHide.filter(":hidden").each(options.complete).end()
						.filter(":visible").animate(hideProps, {
							step : function(now, settings) {
								// only calculate the percent when animating
								// height
								// IE gets very inconsistent results when
								// animating elements
								// with small values, which is common for
								// padding
								if (settings.prop == 'height') {
									percentDone = (settings.end
											- settings.start === 0)
											? 0
											: (settings.now - settings.start)
													/ (settings.end - settings.start);
									if(options.toHide.prev().hasClass("header-nodiv")){//当header下没有子级菜单时
										percentDone = 1;
									}
								}
								
								options.toShow[0].style[settings.prop] = (percentDone * showProps[settings.prop].value)
										+ showProps[settings.prop].unit;
							},
							duration : options.duration,
							easing : options.easing,
							complete : function() {
								if (!options.autoHeight || options.toShow.prev().hasClass("header-nodiv")) {
									options.toShow.css("height", "");
								}
								options.toShow.css("width", originalWidth);
								options.toShow.css({
									overflow : overflow
								});
								options.complete();
							}
						});
			},
			bounceslide : function(options) {
				this.slide(options, {
					easing : options.down ? "easeOutBounce" : "swing",
					duration : options.down ? 1000 : 200
				});
			}
		}
	});

})(jQuery);

/** jquery UI position * */
/**
 * jQuery UI Position 1.8
 * 
 * Copyright (c) 2010 AUTHORS.txt (http://jqueryui.com/about) Dual licensed
 * under the MIT (MIT-LICENSE.txt) and GPL (GPL-LICENSE.txt) licenses.
 * 
 * http://docs.jquery.com/UI/Position
 */
(function(f) {
	f.ui = f.ui || {};
	var c = /left|center|right/, e = "center", d = /top|center|bottom/, g = "center", a = f.fn.position, b = f.fn.offset;
	f.fn.position = function(i) {
		if (!i || !i.of) {
			return a.apply(this, arguments)
		}
		i = f.extend({}, i);
		var l = f(i.of), n = (i.collision || "flip").split(" "), m = i.offset
				? i.offset.split(" ")
				: [0, 0], k, h, j;
		if (i.of.nodeType === 9) {
			k = l.width();
			h = l.height();
			j = {
				top : 0,
				left : 0
			}
		} else {
			if (i.of.scrollTo && i.of.document) {
				k = l.width();
				h = l.height();
				j = {
					top : l.scrollTop(),
					left : l.scrollLeft()
				}
			} else {
				if (i.of.preventDefault) {
					i.at = "left top";
					k = h = 0;
					j = {
						top : i.of.pageY,
						left : i.of.pageX
					}
				} else {
					k = l.outerWidth();
					h = l.outerHeight();
					j = l.offset()
				}
			}
		}
		f.each(["my", "at"], function() {
			var o = (i[this] || "").split(" ");
			if (o.length === 1) {
				o = c.test(o[0]) ? o.concat([g]) : d.test(o[0])
						? [e].concat(o)
						: [e, g]
			}
			o[0] = c.test(o[0]) ? o[0] : e;
			o[1] = d.test(o[1]) ? o[1] : g;
			i[this] = o
		});
		if (n.length === 1) {
			n[1] = n[0]
		}
		m[0] = parseInt(m[0], 10) || 0;
		if (m.length === 1) {
			m[1] = m[0]
		}
		m[1] = parseInt(m[1], 10) || 0;
		if (i.at[0] === "right") {
			j.left += k
		} else {
			if (i.at[0] === e) {
				j.left += k / 2
			}
		}
		if (i.at[1] === "bottom") {
			j.top += h
		} else {
			if (i.at[1] === g) {
				j.top += h / 2
			}
		}
		j.left += m[0];
		j.top += m[1];
		return this.each(function() {
			var r = f(this), q = r.outerWidth(), p = r.outerHeight(), o = f
					.extend({}, j);
			if (i.my[0] === "right") {
				o.left -= q
			} else {
				if (i.my[0] === e) {
					o.left -= q / 2
				}
			}
			if (i.my[1] === "bottom") {
				o.top -= p
			} else {
				if (i.my[1] === g) {
					o.top -= p / 2
				}
			}
			f.each(["left", "top"], function(t, s) {
				if (f.ui.position[n[t]]) {
					f.ui.position[n[t]][s](o, {
						targetWidth : k,
						targetHeight : h,
						elemWidth : q,
						elemHeight : p,
						offset : m,
						my : i.my,
						at : i.at
					})
				}
			});
			if (f.fn.bgiframe) {
				r.bgiframe()
			}
			r.offset(f.extend(o, {
				using : i.using
			}))
		})
	};
	f.ui.position = {
		fit : {
			left : function(h, i) {
				var k = f(window), j = h.left + i.elemWidth - k.width()
						- k.scrollLeft();
				h.left = j > 0 ? h.left - j : Math.max(0, h.left)
			},
			top : function(h, i) {
				var k = f(window), j = h.top + i.elemHeight - k.height()
						- k.scrollTop();
				h.top = j > 0 ? h.top - j : Math.max(0, h.top)
			}
		},
		flip : {
			left : function(i, j) {
				if (j.at[0] === "center") {
					return
				}
				var l = f(window), k = i.left + j.elemWidth - l.width()
						- l.scrollLeft(), h = j.my[0] === "left"
						? -j.elemWidth
						: j.my[0] === "right" ? j.elemWidth : 0, m = -2
						* j.offset[0];
				i.left += i.left < 0 ? h + j.targetWidth + m : k > 0 ? h
						- j.targetWidth + m : 0
			},
			top : function(i, k) {
				if (k.at[1] === "center") {
					return
				}
				var m = f(window), l = i.top + k.elemHeight - m.height()
						- m.scrollTop(), h = k.my[1] === "top"
						? -k.elemHeight
						: k.my[1] === "bottom" ? k.elemHeight : 0, j = k.at[1] === "top"
						? k.targetHeight
						: -k.targetHeight, n = -2 * k.offset[1];
				i.top += i.top < 0 ? h + k.targetHeight + n : l > 0
						? h + j + n
						: 0
			}
		}
	};
	if (!f.offset.setOffset) {
		f.offset.setOffset = function(l, i) {
			if (/static/.test(f.curCSS(l, "position"))) {
				l.style.position = "relative"
			}
			var k = f(l), n = k.offset(), h = parseInt(
					f.curCSS(l, "top", true), 10)
					|| 0, m = parseInt(f.curCSS(l, "left", true), 10) || 0, j = {
				top : (i.top - n.top) + h,
				left : (i.left - n.left) + m
			};
			if ("using" in i) {
				i.using.call(l, j)
			} else {
				k.css(j)
			}
		};
		f.fn.offset = function(h) {
			var i = this[0];
			if (!i || !i.ownerDocument) {
				return null
			}
			if (h) {
				return this.each(function() {
					f.offset.setOffset(this, h)
				})
			}
			return b.call(this)
		}
	}
}(jQuery));;

/** jquery UI autocomplete * */
/**
 * jQuery UI Autocomplete 1.8
 * 
 * Copyright (c) 2010 AUTHORS.txt (http://jqueryui.com/about) Dual licensed
 * under the MIT (MIT-LICENSE.txt) and GPL (GPL-LICENSE.txt) licenses.
 * 
 * http://docs.jquery.com/UI/Autocomplete
 * 
 * Depends: jquery.ui.core.js jquery.ui.widget.js jquery.ui.position.js
 */
(function(a) {
	a.widget("ui.autocomplete", {
		options : {
			minLength : 1,
			delay : 300
		},
		_create : function() {
			var b = this, c = this.element[0].ownerDocument;
			this.element.addClass("ui-autocomplete-input").attr("autocomplete",
					"off").attr({
				role : "textbox",
				"aria-autocomplete" : "list",
				"aria-haspopup" : "true"
			}).bind("keydown.autocomplete", function(d) {
				var e = a.ui.keyCode;
				switch (d.keyCode) {
					case e.PAGE_UP :
						b._move("previousPage", d);
						break;
					case e.PAGE_DOWN :
						b._move("nextPage", d);
						break;
					case e.UP :
						b._move("previous", d);
						d.preventDefault();
						break;
					case e.DOWN :
						b._move("next", d);
						d.preventDefault();
						break;
					case e.ENTER :
						if (b.menu.active) {
							d.preventDefault()
						}
					case e.TAB :
						if (!b.menu.active) {
							return
						}
						b.menu.select();
						break;
					case e.ESCAPE :
						b.element.val(b.term);
						b.close(d);
						break;
					case e.SHIFT :
					case e.CONTROL :
					case 18 :
						break;
					default :
						clearTimeout(b.searching);
						b.searching = setTimeout(function() {
							b.search(null, d)
						}, b.options.delay);
						break
				}
			}).bind("focus.autocomplete", function() {
				b.previous = b.element.val()
			}).bind("blur.autocomplete", function(d) {
				clearTimeout(b.searching);
				b.closing = setTimeout(function() {
					b.close(d)
				}, 150)
			});
			this._initSource();
			this.response = function() {
				return b._response.apply(b, arguments)
			};
			this.menu = a("<ul></ul>").addClass("ui-autocomplete").appendTo(
					"body", c).menu({
				focus : function(e, f) {
					var d = f.item.data("item.autocomplete");
					if (false !== b._trigger("focus", null, {
						item : d
					})) {
						b.element.val(d.value)
					}
				},
				selected : function(e, f) {
					var d = f.item.data("item.autocomplete");
					if (false !== b._trigger("select", e, {
						item : d
					})) {
						b.element.val(d.value)
					}
					b.close(e);
					b.previous = b.element.val();
					if (b.element[0] !== c.activeElement) {
						b.element.focus()
					}
				},
				blur : function(d, e) {
					if (b.menu.element.is(":visible")) {
						b.element.val(b.term)
					}
				}
			}).zIndex(this.element.zIndex() + 1).css({
				top : 0,
				left : 0
			}).hide().data("menu");
			if (a.fn.bgiframe) {
				this.menu.element.bgiframe()
			}
		},
		destroy : function() {
			this.element
					.removeClass("ui-autocomplete-input ui-widget ui-widget-content")
					.removeAttr("autocomplete").removeAttr("role")
					.removeAttr("aria-autocomplete")
					.removeAttr("aria-haspopup");
			this.menu.element.remove();
			a.Widget.prototype.destroy.call(this)
		},
		_setOption : function(b) {
			a.Widget.prototype._setOption.apply(this, arguments);
			if (b === "source") {
				this._initSource()
			}
		},
		_initSource : function() {
			var c, b;
			if (a.isArray(this.options.source)) {
				c = this.options.source;
				this.source = function(e, d) {
					var f = new RegExp(a.ui.autocomplete.escapeRegex(e.term),
							"i");
					d(a.grep(c, function(g) {
						return f.test(g.label || g.value || g)
					}))
				}
			} else {
				if (typeof this.options.source === "string") {
					b = this.options.source;
					this.source = function(e, d) {
						a.getJSON(b, e, d)
					}
				} else {
					this.source = this.options.source
				}
			}
		},
		search : function(c, b) {
			c = c != null ? c : this.element.val();
			if (c.length < this.options.minLength) {
				return this.close(b)
			}
			clearTimeout(this.closing);
			if (this._trigger("search") === false) {
				return
			}
			return this._search(c)
		},
		_search : function(b) {
			this.term = this.element.addClass("ui-autocomplete-loading").val();
			this.source({
				term : b
			}, this.response)
		},
		_response : function(b) {
			if (b.length) {
				b = this._normalize(b);
				this._suggest(b);
				this._trigger("open")
			} else {
				this.close()
			}
			this.element.removeClass("ui-autocomplete-loading")
		},
		close : function(b) {
			clearTimeout(this.closing);
			if (this.menu.element.is(":visible")) {
				this._trigger("close", b);
				this.menu.element.hide();
				this.menu.deactivate()
			}
			if (this.previous !== this.element.val()) {
				this._trigger("change", b)
			}
		},
		_normalize : function(b) {
			if (b.length && b[0].label && b[0].value) {
				return b
			}
			return a.map(b, function(c) {
				if (typeof c === "string") {
					return {
						label : c,
						value : c
					}
				}
				return a.extend({
					label : c.label || c.value,
					value : c.value || c.label
				}, c)
			})
		},
		_suggest : function(b) {
			var c = this.menu.element.empty().zIndex(this.element.zIndex() + 1), d, e;
			this._renderMenu(c, b);
			this.menu.deactivate();
			this.menu.refresh();
			this.menu.element.show().position({
				my : "left top",
				at : "left bottom",
				of : this.element,
				collision : "none"
			});
			d = c.width("").width();
			e = this.element.width();
			c.width(Math.max(d, e))
		},
		_renderMenu : function(d, c) {
			var b = this;
			a.each(c, function(e, f) {
				b._renderItem(d, f)
			})
		},
		_renderItem : function(b, c) {
			return a("<li></li>").data("item.autocomplete", c).append("<a>"
					+ c.label + "</a>").appendTo(b)
		},
		_move : function(c, b) {
			if (!this.menu.element.is(":visible")) {
				this.search(null, b);
				return
			}
			if (this.menu.first() && /^previous/.test(c) || this.menu.last()
					&& /^next/.test(c)) {
				this.element.val(this.term);
				this.menu.deactivate();
				return
			}
			this.menu[c]()
		},
		widget : function() {
			return this.menu.element
		}
	});
	a.extend(a.ui.autocomplete, {
		escapeRegex : function(b) {
			return b.replace(/([\^\$\(\)\[\]\{\}\*\.\+\?\|\\])/gi, "\\$1")
		}
	})
}(jQuery));
(function(a) {
	a.widget("ui.menu", {
		_create : function() {
			var b = this;
			this.element
					.addClass("ui-menu ui-widget ui-widget-content ui-corner-all")
					.attr({
						role : "listbox",
						"aria-activedescendant" : "ui-active-menuitem"
					}).click(function(c) {
						c.preventDefault();
						b.select()
					});
			this.refresh()
		},
		refresh : function() {
			var c = this;
			var b = this.element.children("li:not(.ui-menu-item):has(a)")
					.addClass("ui-menu-item").attr("role", "menuitem");
			b.children("a").addClass("ui-corner-all").attr("tabindex", -1)
					.mouseenter(function() {
						c.activate(a(this).parent())
					}).mouseleave(function() {
						c.deactivate()
					})
		},
		activate : function(d) {
			this.deactivate();
			if (this.hasScroll()) {
				var e = d.offset().top - this.element.offset().top, b = this.element
						.attr("scrollTop"), c = this.element.height();
				if (e < 0) {
					this.element.attr("scrollTop", b + e)
				} else {
					if (e > c) {
						this.element.attr("scrollTop", b + e - c + d.height())
					}
				}
			}
			this.active = d.eq(0).children("a").addClass("ui-state-hover")
					.attr("id", "ui-active-menuitem").end();
			this._trigger("focus", null, {
				item : d
			})
		},
		deactivate : function() {
			if (!this.active) {
				return
			}
			this.active.children("a").removeClass("ui-state-hover")
					.removeAttr("id");
			this._trigger("blur");
			this.active = null
		},
		next : function() {
			this.move("next", "li:first")
		},
		previous : function() {
			this.move("prev", "li:last")
		},
		first : function() {
			return this.active && !this.active.prev().length
		},
		last : function() {
			return this.active && !this.active.next().length
		},
		move : function(d, c) {
			if (!this.active) {
				this.activate(this.element.children(c));
				return
			}
			var b = this.active[d]();
			if (b.length) {
				this.activate(b)
			} else {
				this.activate(this.element.children(c))
			}
		},
		nextPage : function() {
			if (this.hasScroll()) {
				if (!this.active || this.last()) {
					this.activate(this.element.children(":first"));
					return
				}
				var d = this.active.offset().top, c = this.element.height(), b = this.element
						.children("li").filter(function() {
							var e = a(this).offset().top - d - c
									+ a(this).height();
							return e < 10 && e > -10
						});
				if (!b.length) {
					b = this.element.children(":last")
				}
				this.activate(b)
			} else {
				this.activate(this.element.children(!this.active || this.last()
						? ":first"
						: ":last"))
			}
		},
		previousPage : function() {
			if (this.hasScroll()) {
				if (!this.active || this.first()) {
					this.activate(this.element.children(":last"));
					return
				}
				var c = this.active.offset().top, b = this.element.height();
				result = this.element.children("li").filter(function() {
					var d = a(this).offset().top - c + b - a(this).height();
					return d < 10 && d > -10
				});
				if (!result.length) {
					result = this.element.children(":first")
				}
				this.activate(result)
			} else {
				this.activate(this.element.children(!this.active
						|| this.first() ? ":last" : ":first"))
			}
		},
		hasScroll : function() {
			return this.element.height() < this.element.attr("scrollHeight")
		},
		select : function() {
			this._trigger("selected", null, {
				item : this.active
			})
		}
	})
}(jQuery));;

/** 导航栏组件 * */
/**
 * 依赖 juqery1.4.1 contextmenu.js
 */
(function($) {

	var ps = {
		menu : [],
		item1 : [],
		item2 : [],
		clickedFlag : false

	};

	var reset = function() {
		var i;
		for (i = 0; i < ps.menu.length; i = i + 1) {
			ps.menu[i].removeClass("clicked");
		}
	}, /**
		 * 事件处理
		 */
	menuClickHandler = function(e) {
		var that = $(this);

		// alert(1);

		if (that.hasClass('clicked')) {
			that.removeClass("clicked");
			ps.clickedFlag = false;
		} else {
			reset();
			that.addClass('clicked');
			ps.clickedFlag = true;
		}

		showMenuItem(e, that);

		e.stopPropagation();
	}, /**
		 * 事件处理
		 */
	menuMouseOver = function(e) {
		var that = $(this);

		// alert(2);

		if (ps.clickedFlag) {
			reset();
			that.addClass('clicked');

			showMenuItem(e, that);
			e.stopPropagation();
		}
	},

	/**
	 * 有clicked的就显示菜单，没有就隐藏
	 * 
	 * @return
	 */
	showMenuItem = function(e, theli) {
		var i, i1, height = theli.height(), width = theli.width(), top = theli
				.offset().top, left = theli.offset().left, showtop = top
				+ height + 2, showleft = left;

		for (i = 0; i < ps.menu.length; i = i + 1) {
			if (ps.menu[i].hasClass("clicked")) {
				ps.item1[i].displayMenuByPos(e, e.target, {
					left : showleft,
					top : showtop
				});
				for (i1 = 0; i1 < ps.item2.length; i1 = i1 + 1) {
					if (i1 !== i) {
						ps.item2[i1][0].hideMenuPane();
					}
				}
				break;
			}
		}
	};

	$.extend({
		/**
		 * 初始化
		 */
		menu : function() {

			$(document).click(function(e) {
				if (e.target.toString().indexOf('HTMLLIElement') === -1) {
					reset();
					ps.clickedFlag = false;
				}
			});
		},
		/**
		 * liId items 菜单项
		 */
		bindMenuItem : function(liId, item) {

			var ali = $('#' + liId), // 得到页面相应的菜单元素
			item1 = $.fn.contextmenu(item), // 实例化下拉菜单到界面
			item2 = $('#' + item.alias); // 更具id得到下拉菜单div

			ps.menu.push(ali);
			ps.item1.push(item1);
			ps.item2.push(item2);

			// 为菜单绑定click事件
			ali.bind('click', menuClickHandler);

			ali.bind('mouseover', menuMouseOver);

		}
	});
})(jQuery);

/** 数值、货币输入组件 * */
(function() {
	var thousandsNotation, thousandsNotation2, trimComma;
	/**
	 * 千分位表示，每个数字用，分开
	 */
	thousandsNotation = function(str) {
		var re = /(\d{1,3})(?=(\d{3})+(?:$|\D))/g;
		return str.replace(re, "$1,");
	};

	thousandsNotation2 = function(str) {
		var arr, zhengshu, xiaoshu;
		// 如果有小数位
		if (str.indexOf('.') > 0) {
			arr = str.split('.');
			zhengshu = arr[0];
			xiaoshu = arr[1];
			zhengshu = thousandsNotation(zhengshu);
			xiaoshu = thousandsNotation(xiaoshu);
			return zhengshu + '.' + xiaoshu;
		}
		// 如果没有小数位
		else {
			return thousandsNotation(str);
		}
	};

	/**
	 * 去掉数字中的逗号
	 */
	trimComma = function(str) {
		return str.replace(/,/g, "");
	};

	/**
	 * 用于输入数字的输入框组件
	 */
	$.NumberInput = function(config) {
		var ps = $.extend({
			inputId : null, // 输入框的id
			mark : '', // 是￥ 或者$或者没有
			accuracy : null
				// 精确到小数点后面的位数

				}, config);
		// 初始化参数

		var that = this, inputElclick, inputElBlur;

		ps.hiddenEl = $('#' + ps.inputId).attr("tagFlag", "numberInput").hide();
		ps.inputEl = $('<input type="text" tagFlag="numberInput" id="_'
				+ ps.inputId + '">');
		ps.hiddenEl.before(ps.inputEl);

		// 验证输入内容是否合法
		inputElclick = function() {
			// 得到隐藏域的值
			var hiddenElValue = ps.hiddenEl.val();
			// 如果隐藏域中有值，将隐藏域的值放在输入框中
			// if(hiddenElValue !== '') {
			ps.inputEl.val(hiddenElValue);
				// }
				// 否则什么都不做
				// else {
				// return;
				// }

		};

		// 当前输入框鼠标离开事件处理
		inputElBlur = function() {
			// 得到输入框的值
			var inputElValue = ps.inputEl.val(), arr, zhengshu, xiaoshu, temp, i;

			// 如果用户没有输入直接返回
			if (inputElValue === '') {
				ps.hiddenEl.val('');
				return;
			}
			// 将输入框中的值处理后，放进隐藏域，在转换成 ￥111,111.0,000的形式

			// 输入的数值没有小数位
			if (inputElValue.indexOf('.') < 1) {
				// 更具精确度用0来填补
				if (ps.accuracy > 0) {
					inputElValue = inputElValue + '.';
				}
				for (i = 1; i <= ps.accuracy; i += 1) {
					inputElValue = inputElValue + '0';
				}
				// 将输入框的值，放进隐藏域中
				ps.hiddenEl.val(inputElValue);
				// 转换成千分位
				inputElValue = thousandsNotation2(inputElValue);
				// 转换成带钞票符号的数字
				inputElValue = ps.mark + inputElValue;
			}
			// 输入的数值有小数位
			else {
				// 将整数部分和小数部分分开
				arr = inputElValue.split('.');
				zhengshu = arr[0];
				xiaoshu = arr[1];
				temp = xiaoshu.length;

				// 判断输入数值的精确度
				// 如果小数长度和精度相同
				if (temp === ps.accuracy) {
					// 将输入框的值，放进隐藏域中
					ps.hiddenEl.val(inputElValue);
					// 转换成千分位
					inputElValue = thousandsNotation2(inputElValue);
					// 转换成带钞票符号的数字
					inputElValue = ps.mark + inputElValue;
				}
				// 小数位长度 大于 精确度，截断
				else if (temp > ps.accuracy) {
					xiaoshu = xiaoshu.slice(0, ps.accuracy);

					if (ps.accuracy > 0) {
						inputElValue = zhengshu + '.' + xiaoshu;
					} else {
						inputElValue = zhengshu;
					}
					// 将输入框的值，放进隐藏域中
					ps.hiddenEl.val(inputElValue);
					// 转换成千分位
					inputElValue = thousandsNotation2(inputElValue);
					// 转换成带钞票符号的数字
					inputElValue = ps.mark + inputElValue;
				} else {
					for (i = 0; i < ps.accuracy - temp; i += 1) {
						xiaoshu = xiaoshu + '0';
					}
					inputElValue = zhengshu + '.' + xiaoshu;
					// 将输入框的值，放进隐藏域中
					ps.hiddenEl.val(inputElValue);
					// 转换成千分位
					inputElValue = thousandsNotation2(inputElValue);
					// 转换成带钞票符号的数字
					inputElValue = ps.mark + inputElValue;
				}

			}
			// 将处理后的值替换原来的值
			ps.inputEl.val(inputElValue);
				// that.inputEl.show();
				// that.inputEl.hide();
		};

		// 注册事件
		ps.inputEl.focusin(inputElclick);
		ps.inputEl.focusout(inputElBlur);
		ps.inputEl.focusin();
		ps.inputEl.focusout();
	};
})();

/** 进度条组件 * */
(function($) {
	$.extend($.fn, {
		// 创建进度条
		progressbar : function(setting) {
			var ps = $.extend({
				// content holder(Object || css Selector)
				renderTo : $(document.body),
				// whether the slider can be dragged
				enable : true,
				// width of bar and slider
				barWidth : 200,

				barHeight : 40,
				// class name of bar
				barCssName : 'progressbar',
				// class name of completed bar
				completedCssName : 'jquery-completed',
				// show or not show percent
				percent : true

			}, setting);

			ps.renderTo = (typeof ps.renderTo == 'string'
					? $(ps.renderTo)
					: ps.renderTo);
			var str = '';
			if (ps.percent) {
				str = '<div style = "z-index:3;"><div style = "z-index:-1;"></div><span>0%</span></div>';
			} else {
				str = '<div><div></div></div>';
			}
			var height = '';

			var sliderbar = $(str).attr('class', ps.barCssName).css('width',
					ps.barWidth).css('Height', ps.barHeight).css("text-align",
					"center").appendTo(ps.renderTo);
			var completedbar = sliderbar.find('div:eq(0)').attr('class',
					ps.completedCssName);

			var bolorTopWidth = sliderbar.css('border-top-width').replace(
					/[^\d]/g, '');
			var bolorBottomWidth = sliderbar.css('border-bottom-width')
					.replace(/[^\d]/g, '');
			var barHeight = sliderbar.css('height').replace(/[^\d]/g, '');
			var comHeight = '';

			if ($.browser.msie) {

				comHeight = barHeight - bolorBottomWidth - bolorTopWidth;

			} else {
				comHeight = barHeight;
			}
			var fontSize = comHeight - 6;

			if (comHeight < 0 || fontSize < 0) {
				throw new Error('请将进度条的高度和线条的宽度设置调整一下，进度条的高度减去两倍的线条宽度加上6不能小于零。');
			}
			completedbar.css('height', comHeight + 'px');
			sliderbar.css('font-size', fontSize);
			// alert(sliderbar.css('height'));
			// alert(completedbar.css('height'));

			return sliderbar;

		},
		// 设置进度条的值
		setSliderValue : function(v, ispercent, callback) {
			try {
				// validate
				if (typeof v == 'undefined' || v < 0 || v > 100) {
					throw new Error('\'v\' must between 0 and 100.');
				}
				var s = this;
				// validate
				if (typeof s == 'undefined') {
					throw new Error('You bound the method to an object that is not a slider!');
				}

				s.find('div:eq(0)').css('width', v + '%');
				var str = s.html();

				if (str != '' && ispercent) {
					s.find("span").remove();
					s.append("<span>" + v + "%</span>");
				}
				if (typeof callback != 'undefined') {
					callback(v);
				}
			} catch (e) {
				alert(e.message);
			}
		},
		isParamNull : function(param) {
			if (param == null) {
				return 0;
			} else {
				return 1;
			}
		},
		// 删除一个进度条
		removePros : function() {
			$(this).remove();
		}
	});

})(jQuery);
// 得到每个参数对应创建进度条时的名称
function getParamName(i) {
	var paramName = ''
	switch (i) {
		case 0 :
			paramName = 'renderTo:renderTo'
			break
		case 1 :
			paramName = 'barWidth:barWidth'
			break
		case 2 :
			paramName = 'barHeight:barHeight'
			break
		case 3 :
			paramName = 'barCssName:barCssName'
			break
		case 4 :
			paramName = 'completedCssName:completedCssName'
			break
		case 5 :
			paramName = 'percent:isPercent'
			break

	}
	return paramName;
};

// 以javascript的方式创建进度条
function createProsBar(renderTo, barWidth, barHeight, barCssName,
		completedCssName, isPercent) {
	// alert("r");

	var rt = new Array(5);
	rt[0] = $.fn.isParamNull(renderTo);
	rt[1] = $.fn.isParamNull(barWidth);
	rt[2] = $.fn.isParamNull(barHeight);
	rt[3] = $.fn.isParamNull(barCssName);
	rt[4] = $.fn.isParamNull(completedCssName);
	rt[5] = $.fn.isParamNull(isPercent);

	var jsonParams = '{';
	for (i = 0; i < 6; i++) {
		if (rt[i] == 1) {
			jsonParams = jsonParams + getParamName(i) + ",";

		}
	}
	jsonParams = jsonParams.substr(0, jsonParams.length - 1);
	jsonParams = jsonParams + "}";
	var myE = eval('(' + jsonParams + ')');
	// alert(myE);
	var s = $.fn.progressbar(myE);
	return s;
};

/** 树型组件 * */
(function($) {
    $.fn.swapClass = function(c1, c2) {
        return this.removeClass(c1).addClass(c2);
    };
    $.fn.switchClass = function(c1, c2) {
        if (this.hasClass(c1)) {
            return this.swapClass(c1, c2);
        }
        else {
            return this.swapClass(c2, c1);
        }
    };
    $.fn.treeview = function(settings) {
        var dfop =
            {
                method: "POST",
                datatype: "json",
                url: false,
                cbiconpath: "tree/images/icons/",// 图片路径
                icons: ["checkbox_0.gif", "checkbox_1.gif", "checkbox_2.gif"],// 多选图片
                radioIcons:["radio_0.gif", "radio_1.gif"],// 单选图片
                showcheck: false, // 是否显示多选
                showradio: false, // 是否显示单选
                oncheckboxclick: false, // 当checkstate状态变化时所触发的事件，但是不会触发因级联选择而引起的变化
                onselect: false,// 当节点checkstate状态变化时所触发的事件，会触发因级联而引起的变化
                onnodeclick: false,// 节点单击事件
                onnodedblclick: false,// 节点双击事件
                cascadecheck: true,// 节点选中时级联选中父节点和子节点
                parentCascadecheck: true,// 节点选中时向父节点回溯
                data: null,// 待加载数据
                // clicktoggle: true, //点击节点展开和收缩子节点
                theme: "bbit-tree-arrows", // bbit-tree-lines
											// ,bbit-tree-no-lines,bbit-tree-arrows
                className: "bbit-tree",// 树主体样式名称
                aHasSelected: "",// 已经选中的节点项id值
                aNeedLazy: false,// 该树是否延迟加载
                contextMenu: false,// 该树是否显示右键菜单
                title: 'root',// 树标题
                displayDeep: false,// 要显示的级别,默认为false
                error: false// 获取数据错误时的处理函数，由用户自定义
            };
        $.extend(dfop, settings);
        var idPathMap = [];// 保存节点Id和对应path的map表，用于快速查询节点
        // dfop.data = initTreeData(dfop.data);//初始化数据
        var treenodes = dfop.data;
        var me = $(this);
        var id = me.attr("id");
        if (id == null || id == "") {
            id = "bbtree" + new Date().getTime();
            me.attr("id", id);
        }

        // 预加载图片
        if (dfop.showcheck) {// 加载复选框图片
            for (var i = 0; i < 3; i++) {
                var im = new Image();
                im.src = dfop.cbiconpath + dfop.icons[i];
            }
        }
        if(dfop.showradio){// 加载单选框图片
        	for(var i=0; i<2; i++){
        		var im = new Image();
        		im.src = dfop.cbiconpath + dfop.radioIcons[i];
        	}
        }
        
        /**
		 * 根据传入的数据进行父子节点的匹配（顶级节点的父节点默认为“-1”）
		 * 
		 * @param data
		 *            树型数据
		 * @return data 父子节点匹配好的树型数据
		 */
        function initTreeData(data) {
        	var orderedData = [];
        	if(data && data.length > 0){
        		var l = data.length;
        		
        		for(var i=0; i<l; i++){
        			orderedData['' + data[i].id] = i;
        		}
        		var pid;
        		for(var i=0; i<l; i++){
        			pid = data[i].pid;
        			if(pid != '-1'){
        				data[i].parent = data[orderedData['' + pid]];// 设置父节点
        			    data[orderedData['' + pid]].ChildNodes.push(data[i]);
        			    data[orderedData['' + pid]].hasChildren = true;
        			}
        		}
        		orderedData.length = 0;
        		for(var i=0; i<l; i++){
        			pid = data[i].pid;
        			if(pid == '-1'){
        				orderedData.push(data[i]);
        			}
        		}
        		
        		buildIdPathMap(orderedData, "");// 构造节点Id和Path的Map数组，用与快速节点查询
        	}
        	return orderedData;
        }
        
        /**
		 * 构造节点Id和Path的Map数组，用与快速节点查询
		 * 
		 * @param orderedData
		 *            -构造好的树节点数据
		 * @param path
		 *            -树节点路径
		 * @return
		 */
        function buildIdPathMap(orderedData, path){
        	var l = orderedData.length;
        	var data;
        	var tempPath;
        	for(var i=0; i<l; i++){
        		data = orderedData[i];
        		tempPath = path === "" ? i : path + "." + i;
        		data.path = tempPath;
        		idPathMap['' + data.id] = data.path;
        		if(data.ChildNodes && data.ChildNodes.length > 0){
        			buildIdPathMap(data.ChildNodes, data.path);
        		}
        	}
        }
        
        // region
        function buildtree(data, ht) {
            ht.push("<div class='bbit-tree-bwrap'>"); // Wrap ;
            ht.push("<div class='bbit-tree-body'>"); // body ;
            ht.push("<ul class='bbit-tree-root ", dfop.theme, "'>"); // root
            
            ht.push("<li class='bbit-tree-node-root'>");
           	ht.push("<div unselectable='on'");
            ht.push("<span class='bbit-tree-node-indent'>");
            ht.push("</span>");
            ht.push("<img class='bbit-tree-ec-icon", "' src='" + dfop.cbiconpath + "base.gif'/>");
            ht.push("<a hideFocus class='bbit-tree-node-anchor' tabIndex=1 href='javascript:void(0);'>");
            ht.push("<span unselectable='on'>", dfop.title, "</span>");
            ht.push("</a>");
            ht.push("</div>");
            ht.push("</li>");
            if (data && data.length > 0) {
                var l = data.length;
                for (var i = 0; i < l; i++) {
                    buildnode(data[i], ht, 0, i, i == l - 1);
                }
            }
            else {
                asnyloadc(null, false, function(data) {
                    if (data && data.length > 0) {
                    	data = initTreeData(data);// 初始化数据
                        treenodes = data;
                        dfop.data = data;
                        initSelectedNodes(dfop.data, null, 0);// 初始化树节点的选中状态
                        var l = data.length;
                        for (var i = 0; i < l; i++) {
                            buildnode(data[i], ht, 0, i, i == l - 1);
                        }
                    }
                });
            }
            ht.push("</ul>"); // root and;
            ht.push("</div>"); // body end;
            ht.push("</div>"); // Wrap end;
        }
        // endregion
        function buildnode(nd, ht, deep, path, isend) {
        	if(dfop.displayDeep && deep >= dfop.displayDeep){// 如果设置了要显示到第几级并且deep已经超过该级别
        		return false;
        	}
            var nid = nd.id.replace(/[^\w]/gi, "_");
            ht.push("<li class='bbit-tree-node'>");
            ht.push("<div id='", id, "_", nid, "' tpath='", path, "' unselectable='on' title='", nd.text, "'");
            var cs = [];
            cs.push("bbit-tree-node-el");
            if (nd.hasChildren && (!dfop.displayDeep || deep < dfop.displayDeep - 1)) {// 增加显示级别限制
                cs.push(nd.isexpand ? "bbit-tree-node-expanded" : "bbit-tree-node-collapsed");
            }
            else {
                cs.push("bbit-tree-node-leaf");
            }
            if (nd.classes) { cs.push(nd.classes); }

            ht.push(" class='", cs.join(" "), "'>");
            // span indent
            ht.push("<span class='bbit-tree-node-indent'>");
            if(deep == 0){// 如果当前节点是最外层节点，则记录该节点子节点是否需要展现deepline
            	if(isend){// 如果该节点是最后一个节点，则不需要使用deepline
            	    nd.deepline = "0";
            	}else{
            		nd.deepline = "1";
            	}
            }else if(deep > 0){
            	var parentDeepline = nd.parent.deepline;
            	var deeplineLength = parentDeepline.length;
            	var singleDeepline;
            	for(var i=0; i<deeplineLength; i++){
            		singleDeepline = parentDeepline.substring(i, i+1);
            		if(singleDeepline == "0"){
            		    ht.push("<img class='bbit-tree-icon' src='" + dfop.cbiconpath + "s.gif'/>");
            		}else if(singleDeepline == "1"){
            		    ht.push("<img class='bbit-tree-elbow-line' src='" + dfop.cbiconpath + "s.gif'/>");
            		}
            	}
            	// 记录节点deepline信息
            	if(isend){
            		nd.deepline = parentDeepline + "0";
            	}else{
            		nd.deepline = parentDeepline + "1";
            	}
            }

            ht.push("</span>");
            // img
            cs.length = 0;
            if (nd.hasChildren && (!dfop.displayDeep || deep < dfop.displayDeep - 1)) {// 增加显示级别限制
                if (nd.isexpand) {
                    cs.push(isend ? "bbit-tree-elbow-end-minus" : "bbit-tree-elbow-minus");
                }
                else {
                    cs.push(isend ? "bbit-tree-elbow-end-plus" : "bbit-tree-elbow-plus");
                }
            }
            else {
                cs.push(isend ? "bbit-tree-elbow-end" : "bbit-tree-elbow");
            }
            ht.push("<img class='bbit-tree-ec-icon ", cs.join(" "), "' src='" + dfop.cbiconpath + "s.gif'/>");
            
            if(nd.imgPath && nd.imgPath.length > 0){// 设置了节点自定义图片
            	ht.push("<img class='bbit-tree-node-icon' style='background-image:url(", nd.imgPath, ");' src='" + dfop.cbiconpath + "s.gif'/>");
            }else{// 使用默认节点图片
            	ht.push("<img class='bbit-tree-node-icon' src='" + dfop.cbiconpath + "s.gif'/>");
            }
            
            // radio
            if(dfop.showradio && nd.showcheck){
            	if(nd.checkstate == null || nd.checkstate == undefined){
            		nd.checkstate = 0;
            	}else if(nd.checkstate != 0){
            		nd.checkstate = 1;
            	}
            	ht.push("<img  id='", id, "_", nid, "_cb' class='bbit-tree-node-ra' src='", dfop.cbiconpath, dfop.radioIcons[nd.checkstate], "'/>");
            }
            // checkbox
            if (dfop.showcheck && nd.showcheck) {
                if (nd.checkstate == null || nd.checkstate == undefined) {
                    nd.checkstate = 0;
                }
                ht.push("<img  id='", id, "_", nid, "_cb' class='bbit-tree-node-cb' src='", dfop.cbiconpath, dfop.icons[nd.checkstate], "'/>");
            }
            // a
            ht.push("<a hideFocus class='bbit-tree-node-anchor' tabIndex=1 href='javascript:void(0);'>");
            ht.push("<span unselectable='on'>", nd.text, "</span>");
            ht.push("</a>");
            ht.push("</div>");
            // Child
            if (nd.hasChildren && (!dfop.displayDeep || deep < dfop.displayDeep - 1)) {// 增加显示级别限制
                if (nd.isexpand) {
                    ht.push("<ul  class='bbit-tree-node-ct'  style='z-index: 0; position: static; visibility: visible; top: auto; left: auto;'>");
                    if (nd.ChildNodes) {
                        var l = nd.ChildNodes.length;
                        for (var k = 0; k < l; k++) {
                            nd.ChildNodes[k].parent = nd;
                            buildnode(nd.ChildNodes[k], ht, deep + 1, path + "." + k, k == l - 1);
                        }
                    }
                    ht.push("</ul>");
                }
                else {
                    ht.push("<ul style='display:none;'></ul>");
                }
            }
            ht.push("</li>");
            nd.render = true;
        }
        function getItem(path) {
            ap = path.toString().split(".");
            var t = treenodes;
            for (var i = 0; i < ap.length; i++) {
                if (i == 0) {
                    t = t[ap[i]];
                }
                else {
                    t = t.ChildNodes[ap[i]];
                }
            }
            return t;
        }
        
        /**
		 * 点击单选框时节点选中事件
		 * 
		 * @param item
		 *            -树节点
		 * @param state
		 *            -树节点选中状态
		 */
        function checkRadio(item, state){
        	var nid;
        	var et;
        	if(dfop.ritem){// 已经存在选择了的节点
        		if(dfop.ritem.id != item.id){// 已经选择的节点不是本节点
        			dfop.ritem.checkstate = 0;
        			nid = dfop.ritem.id.replace(/[^\w]/gi, "_");
	                et = $("#" + id + "_" + nid + "_cb");
	                if (et.length == 1) {
	                    et.attr("src", dfop.cbiconpath + dfop.radioIcons[dfop.ritem.checkstate]);
	                    if(dfop.onselect){
	                    	dfop.onselect.call(et, dfop.ritem, dfop.ritem.checkstate);
	                    }
	                }
        		}
        	}
        	if(state == 1){
	        	dfop.ritem = item;// 更新最后一个选中节点为当前节点
		        dfop.ritem.checkstate = state;
		        if(dfop.onselect){
		        	dfop.onselect.call(this, dfop.ritem, dfop.ritem.checkstate);
		        }
				nid = dfop.ritem.id.replace(/[^\w]/gi, "_");
	            et = $("#" + id + "_" + nid + "_cb");
	            if (et.length == 1) {
	                et.attr("src", dfop.cbiconpath + dfop.radioIcons[dfop.ritem.checkstate]);
	            }
        	}
        }
        
        /**
		 * 区分当前选中节点的半选和全选状态，使用情况： 1.当树节点选中不需要进行回溯和遍历时，该节点视子节点是否全部选中来决定是否使用半选状态
		 * 2.当树节点初始化时，该节点视子节点是否全部选中来决定是否使用半选状态
		 * 3.当item为父级节点并且当前节点的选中状态为0时，则item为半选状态
		 * 
		 * @param item
		 *            -树节点
		 * @param state
		 *            -选择状态
		 * @param type
		 *            -选择类型：1.item为当前选中节点 0.item为父级节点
		 * @return
		 */
        function check2(item, state, type) {
            var pstate = item.checkstate;
            var state2 = state;
            if (state == 1 && item.ChildNodes){// 若某个节点下面有子节点并展开，则根据子节点是否全部选中来确定是半选状态还是全选状态
           		var cs = item.ChildNodes;
            	var l = cs.length;
           		var ch = true;
                for (var i = 0; i < l; i++) {
                    if (state == 1 && cs[i].checkstate != 1) {
                        ch = false;
                        break;
                    }
                }
                if(!ch){
                	state2 = 2;
                }
           	}else if(state != 0 && item.hasChildren){// 若某个节点下面有子节点但又没有展开，则选中该节点时为半选状态
           		state2 = 2;
           	}
            if(type == 0 && pstate != 0 && state == 0){// 当item为父级节点并且当前节点的选择状态为0时，则item为半选状态
            	state2 = 2;
            }
            
            item.checkstate = state2;
            if(pstate != item.checkstate){
            	if(dfop.onselect){
            		dfop.onselect.call(this, item, item.checkstate);
            	}
	            if (item.render) {
	                var nid = item.id.replace(/[^\w]/gi, "_");
	                var et = $("#" + id + "_" + nid + "_cb");
	                if (et.length == 1) {
	                    et.attr("src", dfop.cbiconpath + dfop.icons[item.checkstate]);
	                }
	            }
            }
        }
        
        /**
		 * 树节点向上回溯，直到父级节点未被选中且该树不允许向上回溯时取消回溯
		 * 
		 * @param fn
		 *            -选中处理函数
		 * @param item
		 *            -树节点
		 * @param args
		 *            -选中状态
		 * @return
		 */
        function bubble2(fn, item, args) {
            var p = item.parent;
            while (p) {
            	if((p.checkstate == 0 && !dfop.parentCascadecheck) || fn(p, args, 0) === false){
            		break;
            	}
                p = p.parent;
            }
        }
        
        function check(item, state, type) {
            var pstate = item.checkstate;
            if (type == 1) {
                item.checkstate = state;
            }
            else {// 上溯
                var cs = item.ChildNodes;
                var l = cs.length;
                var ch = true;
                for (var i = 0; i < l; i++) {
                    if ((state == 1 && cs[i].checkstate != 1) || state == 0 && cs[i].checkstate != 0) {
                        ch = false;
                        break;
                    }
                }
                if (ch) {
                    item.checkstate = state;
                }
                else {
                    item.checkstate = 2;
                }
            }
            if(pstate != item.checkstate){
            	if(dfop.onselect){
            		dfop.onselect.call(this, item, item.checkstate);
            	}
	            if (item.render) {
	                var nid = item.id.replace(/[^\w]/gi, "_");
	                var et = $("#" + id + "_" + nid + "_cb");
	                if (et.length == 1) {
	                    et.attr("src", dfop.cbiconpath + dfop.icons[item.checkstate]);
	                }
	            }
            }
        }
        // 遍历子节点
        function cascade(fn, item, args) {
            if (fn(item, args, 1) != false) {
            	item.isSelectedAll = true;// 若该节点选中则该节点的子节点应该全部选中
                if (item.ChildNodes != null && item.ChildNodes.length > 0) {
                    var cs = item.ChildNodes;
                    for (var i = 0, len = cs.length; i < len; i++) {
                        cascade(fn, cs[i], args);
                    }
                }
            }
        }
        // 冒泡的祖先
        function bubble(fn, item, args) {
            var p = item.parent;
            while (p) {
                if (fn(p, args, 0) === false) {
                    break;
                }
                p = p.parent;
            }
        }
        function nodeclick(e) {
            var path = $(this).attr("tpath");
            var et = e.target || e.srcElement;
            var item = getItem(path);
            if (et.tagName == "IMG") {
                // +号需要展开
                if ($(et).hasClass("bbit-tree-elbow-plus") || $(et).hasClass("bbit-tree-elbow-end-plus")) {
                    var ul = $(this).next(); // "bbit-tree-node-ct"
                    if (ul.hasClass("bbit-tree-node-ct")) {
                        ul.show();
                    }
                    else {
                        var deep = path.split(".").length;
                        if (item.complete) {
                            item.ChildNodes != null && asnybuild(item.ChildNodes, deep, path, ul, item);
                        }
                        else {
                            $(this).addClass("bbit-tree-node-loading");
                            asnyloadc(item, true, function(data) {
                                item.complete = true;
                                item.ChildNodes = data;
                                
                                if(data && data.length > 0){// 如果data不为空则初始化data的path值，并将其加入idPathMap中
                                	var l = data.length;
                                	for(var i=0; i<l; i++){
                                		data[i].path = item.path + "." + i;
                                		idPathMap['' + data[i].id] = data[i].path;
                                	}
                                }
                                
                                asnybuild(data, deep, path, ul, item);
                            });
                        }
                    }
                    if ($(et).hasClass("bbit-tree-elbow-plus")) {
                        $(et).swapClass("bbit-tree-elbow-plus", "bbit-tree-elbow-minus");
                    }
                    else {
                        $(et).swapClass("bbit-tree-elbow-end-plus", "bbit-tree-elbow-end-minus");
                    }
                    $(this).swapClass("bbit-tree-node-collapsed", "bbit-tree-node-expanded");
                }
                else if ($(et).hasClass("bbit-tree-elbow-minus") || $(et).hasClass("bbit-tree-elbow-end-minus")) {  // -
																													// 号需要收缩
                    $(this).next().hide();
                    if ($(et).hasClass("bbit-tree-elbow-minus")) {
                        $(et).swapClass("bbit-tree-elbow-minus", "bbit-tree-elbow-plus");
                    }
                    else {
                        $(et).swapClass("bbit-tree-elbow-end-minus", "bbit-tree-elbow-end-plus");
                    }
                    $(this).swapClass("bbit-tree-node-expanded", "bbit-tree-node-collapsed");
                }
                else if ($(et).hasClass("bbit-tree-node-ra")) // 点击了radio
                {
                    var s = 1;
                    var r = true;
                    if (dfop.oncheckboxclick) {
                        r = dfop.oncheckboxclick.call(et, item, s);
                    }
                    if (r != false) {
                    	clearSeleted(me[0].t);// 删除自己和子级节点在初始化选中节点Id中的值
                    	checkRadio(item, s);// 点击单选框
                    }
                }
                else if ($(et).hasClass("bbit-tree-node-cb")) // 点击了Checkbox
                {
                    var s;
                    
                    if(dfop.cascadecheck){// 如果设置回溯遍历，则checkstate 2会转变为1
                    	s = item.checkstate != 1 ? 1 : 0;
                    }else{// 如果没有设置回溯遍历，则checkstate 2会转变为0
                    	s = item.checkstate != 0 ? 0 : 1;
                    }
                    
                    var r = true;
                    if (dfop.oncheckboxclick) {
                        r = dfop.oncheckboxclick.call(et, item, s);
                    }
                    if (r != false) {
                        if (dfop.cascadecheck) {
                        	item.isSelectedAll = true;// 设置该节点的下级子节点应该全部选中
                    		removeSelfOrChildrenSeleted(me[0].t, item.id);// 删除自己和子级节点在初始化选中节点Id中的值
                            // 遍历
                            cascade(check, item, s);
                    		if(dfop.parentCascadecheck){
                            	// 上溯
                            	bubble(check, item, s);
                    		}
                        }
                        else {
                            check2(item, s, 1);// 改变节点选中状态
                            bubble2(check2, item, s);// 向上回溯父节点
                        }
                    }
                }
            }
            else {
                if (dfop.citem) {
                    var nid = dfop.citem.id.replace(/[^\w]/gi, "_");
                    $("#" + id + "_" + nid).removeClass("bbit-tree-selected");
                }
                dfop.citem = item;
                $(this).addClass("bbit-tree-selected");
                if (dfop.onnodeclick) {
                    if (!item.expand) {
                        item.expand = function() { expandnode.call(item); };
                    }
                    dfop.onnodeclick.call(this, item);
                }
            }
        }
        function expandnode() {
            var item = this;
            var nid = item.id.replace(/[^\w]/gi, "_");
            var img = $("#" + id + "_" + nid + " img.bbit-tree-ec-icon");
            if (img.length > 0) {
                img.click();
            }
        }
        
        /**
		 * 节点双击触发事件
		 * 
		 * @param e
		 *            双击事件
		 * @return
		 */
        function nodeDblclick(e) {
            var path = $(this).attr("tpath");
            var et = e.target || e.srcElement;
            var item = getItem(path);
            if (et.tagName != "IMG") {// 点击图片时双击无效
	            if (dfop.onnodedblclick) {
	                dfop.onnodedblclick.call(this, item);
	            }
            }
        }
        
        function asnybuild(nodes, deep, path, ul, pnode) {
            var l = nodes.length;
            if (l > 0) {
            	initSelectedNodes(nodes, pnode, 1);// 初始化树节点的选中状态
                var ht = [];
                for (var i = 0; i < l; i++) {
                    nodes[i].parent = pnode;
                    buildnode(nodes[i], ht, deep, path + "." + i, i == l - 1);
                }
                ul.html(ht.join(""));
                ht = null;
                InitEvent(ul);
            }
            ul.addClass("bbit-tree-node-ct").css({ "z-index": 0, position: "static", visibility: "visible", top: "auto", left: "auto", display: "" });
            ul.prev().removeClass("bbit-tree-node-loading");
        }
        
        function asnyloadc(pnode, isAsync, callback) {
            if (dfop.url) {
                if (pnode && pnode != null)
                    var param = builparam(pnode);
                $.ajax({
                    type: dfop.method,
                    url: dfop.url,
                    data: param,
                    async: isAsync,
                    dataType: dfop.datatype,
                    success: callback,
                    error: function(e) { 
                    	if(dfop.error){// 用户定义了错误处理函数
                    		dfop.error.call(this, e);
                    	}else{// 默认错误处理机制
                    		alert("error occur!");
                    	}
                    }
                });
            }
        }
        function builparam(node) {
            var p = [{ name: "id", value: encodeURIComponent(node.id) }
                    , { name: "text", value: encodeURIComponent(node.text) }
                    , { name: "value", value: encodeURIComponent(node.value) }
                    , { name: "checkstate", value: node.checkstate}];
            return p;
        }
        function bindevent() {
            $(this).hover(function() {
                $(this).addClass("bbit-tree-node-over");
            }, function() {
                $(this).removeClass("bbit-tree-node-over");
            }).click(nodeclick)
              .dblclick(nodeDblclick)// 双击事件
             .find("img.bbit-tree-ec-icon").each(function(e) {
                 if (!$(this).hasClass("bbit-tree-elbow")) {
                     $(this).hover(function() {
                         $(this).parent().addClass("bbit-tree-ec-over");
                     }, function() {
                         $(this).parent().removeClass("bbit-tree-ec-over");
                     });
                 }
             });
        }
        function InitEvent(parent) {
            var nodes = $("li.bbit-tree-node>div", parent);
            nodes.each(bindevent);
            nodes.bind("contextmenu", function(e){// 添加右键功能
            	var et = e.target || e.srcElement;
            	if (et.tagName != "IMG") {
	                if (dfop.citem) {
	                    var nid = dfop.citem.id.replace(/[^\w]/gi, "_");
	                    $("#" + id + "_" + nid).removeClass("bbit-tree-selected");
	                }
	                var path = $(this).attr("tpath");
		            var item = getItem(path);
	                dfop.citem = item;
	                $(this).addClass("bbit-tree-selected");
			        if(dfop.contextMenu){// 需要展示右键菜单
		            	eval(dfop.contextMenu).displayMenu(e, this);
		            	return false;
		            }
            	}
            });
        }
        function reflash(itemId) {
            var nid = itemId.replace(/[^\w-]/gi, "_");
            var node = $("#" + id + "_" + nid);
            if (node.length > 0) {
                node.addClass("bbit-tree-node-loading");
                var isend = node.hasClass("bbit-tree-elbow-end") || node.hasClass("bbit-tree-elbow-end-plus") || node.hasClass("bbit-tree-elbow-end-minus");
                var path = node.attr("tpath");
                var deep = path.split(".").length;
                var item = getItem(path);
                if (item) {
                    asnyloadc(item, true, function(data) {
                        item.complete = true;
                        item.ChildNodes = data;
                        item.isexpand = true;
                        if (data && data.length > 0) {
                            item.hasChildren = true;
                        }
                        else {
                            item.hasChildren = false;
                        }
                        var ht = [];
                        buildnode(item, ht, deep - 1, path, isend);
                        ht.shift();
                        ht.pop();
                        var li = node.parent();
                        li.html(ht.join(""));
                        ht = null;
                        InitEvent(li);
                        bindevent.call(li.find(">div"));
                    });
                }
            }
            else {
                alert("该节点还没有生成");
            }
        }
        /**
		 * 重新加载树节点数据
		 * 
		 * @param data
		 *            -要重新加载树节点数据，json格式
		 * @param aHasSelected
		 *            -初始化选中的节点Id集
		 * @return
		 */
        function reLoadData(data, aHasSelected){
        	dfop.citem = null;
        	dfop.ritem = null;
        	me.html("");
        	dfop.data = data;
        	dfop.aHasSelected = aHasSelected;
        	var idPathMap = [];// 保存节点Id和对应path的map表，用于快速查询节点
	        dfop.data = initTreeData(dfop.data);// 初始化数据
	        treenodes = dfop.data;
	        
	        var html = [];
	        initSelectedNodes(dfop.data, null, 0);// 初始化树节点的选中状态
	        buildtree(dfop.data, html);
	        me.addClass(dfop.className).html(html.join(""));
	        InitEvent(me);
	        html = null;
        }
        
        function getck(items, c, fn, deep) {
        	if(dfop.displayDeep && deep >= dfop.displayDeep){// 增加显示级别设置的节点过滤
        		return false;
        	}
            for (var i = 0, l = items.length; i < l; i++) {
                (items[i].showcheck == true && items[i].checkstate == 1) && c.push(fn(items[i]));
                if (items[i].ChildNodes != null && items[i].ChildNodes.length > 0) {
                    getck(items[i].ChildNodes, c, fn, deep + 1);
                }
            }
        }
        
        /**
		 * 得到已经选中的节点的Id值集合
		 * 
		 * @param items
		 *            -节点集合
		 * @param c
		 *            -返回结果集合
		 * @param fn
		 *            -返回处理函数
		 * @param deep
		 *            -目前树的层次深度，主要在处理级别设置时使用
		 * @return
		 */
        function getCkAndHalfCkIds(items, c, fn, deep){
        	if(dfop.displayDeep && deep >= dfop.displayDeep){// 增加显示级别设置的节点过滤
        		return false;
        	}
        	var item;
        	for (var i = 0, l = items.length; i < l; i++) {
        		item = items[i];
        		if (item.showcheck == true && (item.checkstate == 1 || item.checkstate == 2)){
        			if(!dfop.cascadecheck || item.complete){
        				c.singleNode.push(fn(item));
        			}else{
        				if(checkSelfOrChildrenHasSelected(me[0].t, item.id)){
        					c.singleNode.push(fn(item));
        				}else{
        					c.multiNode.push(fn(item));
        				}
        			}
        			removeSelfSeleted(me[0].t, item.id);
        		}
        		if (items[i].ChildNodes != null && items[i].ChildNodes.length > 0) {
        			getCkAndHalfCkIds(items[i].ChildNodes, c, fn);
        		}
        	}
        }
        
        function getCkAndHalfCk(items, c, fn, deep) {
        	if(dfop.displayDeep && deep >= dfop.displayDeep){// 增加显示级别设置的节点过滤
        		return false;
        	}
            for (var i = 0, l = items.length; i < l; i++) {
               (items[i].showcheck == true && (items[i].checkstate == 1 || items[i].checkstate == 2)) && c.push(fn(items[i]));
                if (items[i].ChildNodes != null && items[i].ChildNodes.length > 0) {
                    getCkAndHalfCk(items[i].ChildNodes, c, fn, deep + 1);
                }
            }
        }
        
        /**
		 * 初始化选中的树节点
		 * 
		 * @param items
		 *            -树节点集
		 * @param parentNode
		 *            -items的父节点
		 * @param type
		 *            -初始化类型；type=0 树初始化时的节点选中；type=1 树延迟加载时的节点选中
		 * @return
		 */
        function initSelectedNodes(items, parentNode, type){
        	if(dfop.aNeedLazy){
        		if(dfop.showradio){
        			initRadioSelectedNodes(items);
        		}else if(dfop.showcheck){
        			initLazySelectedNodes(items, parentNode, type);
        		}
        	}else{
        		if(type == 0){
        			if(dfop.showradio){
        				initRadioSelectedNodes(items);
        			}else if(dfop.showcheck){
        				initStaticSelectedNodes(items);
        			}
        		}
        	}
        }
        
        /**
		 * 单选框时初始化选中的树节点
		 * 
		 * @param items
		 *            -树节点集
		 * @return
		 */
        function initRadioSelectedNodes(items){
        	var item;
    		for (var i = 0, l = items.length; i < l; i++) {
    			item = items[i];
    			if(item.showcheck == true && checkSelfHasSelected(me[0].t, item.id)){
    				checkRadio(item, 1);
    				removeSelfSeleted(me[0].t, item.id);
    			}
                if (item.ChildNodes && item.ChildNodes.length > 0) {
                    initRadioSelectedNodes(item.ChildNodes);
                }
            }
        }
        
        /**
		 * 复选框时初始化选中的树节点
		 * 
		 * @param items
		 *            -树节点集
		 * @param parentNode
		 *            -items的父节点
		 * @param type
		 *            -初始化类型；type=0 树初始化时的节点选中；type=1 树延迟加载时的节点选中
		 * @return
		 */
        function initLazySelectedNodes(items, parentNode, type){
    		var item;
    		var state = 0;
    		if(type == 1){
    			state = (parentNode.checkstate && parentNode.isSelectedAll && !checkSelectedNode(items)) ? 1 : 0; 
    		}
    		for (var i = 0, l = items.length; i < l; i++) {
    			item = items[i];
    			if(item.showcheck == true){
    				if(dfop.cascadecheck){// 节点选中时需要遍历选中子节点
	        			if(checkSelfOrChildrenHasSelected(me[0].t, item.id)){// 如果是本节点的子节点被初始化选中
                    		check2(item, 1, 1);
	        				bubble2(check2, item, 1);
	        			}else if(state){
	        				cascade(check, item, 1);
                            bubble2(check, item, 1);
	        			}
    				}else{
    					if(checkSelfHasSelected(me[0].t, item.id)){// 如果是本节点被初始化选中
	        				check2(item, 1, 1);
	        				bubble2(check2, item, 1);
	        			}
    				}
    			}
                if (items[i].ChildNodes && items[i].ChildNodes.length > 0) {
                    initLazySelectedNodes(items[i].ChildNodes, items[i], type);
                }
            }
        }
        
        /**
		 * 判断节点集里的某个节点或者其子节点是否有被选中的
		 * 
		 * @param items
		 *            -节点集
		 * @return true/false 是否被选中
		 */
        function checkSelectedNode(items){
        	var item;
        	var hasSelected = false;
    		for (var i = 0, l = items.length; i < l; i++) {
    			item = items[i];
    			if(item.showcheck == true){
        			if(checkSelfOrChildrenHasSelected(me[0].t, item.id)){// 如果是本节点的子节点被初始化选中
        				hasSelected = true;
        				break;
        			}
    			}
            }
            return hasSelected;
        }
        
        /**
		 * 静态树情况下的初始化选中的树节点
		 * 
		 * @param items
		 *            -树节点集
		 * @return
		 */
        function initStaticSelectedNodes(items){
        	if(items && items.length > 0){
        		var l = items.length;
        		for(var i=0; i<l; i++){
        			initStaticSelectedNode(items[i]);
        		}
        	}
        }
        
        /**
		 * 静态树情况下的初始化选中的树节点
		 * 
		 * @param item
		 *            -树节点
		 * @return
		 */
        function initStaticSelectedNode(item){
    		var state = checkSelfHasSelected(me[0].t, item.id)?1:0;
    		removeSelfSeleted(me[0].t, item.id);
            if (item.ChildNodes && item.ChildNodes.length > 0) {
            	var l = item.ChildNodes.length;
            	var childState;
            	var allChildSelected = 1;// 记录子节点是否全部被选中
            	for(var i=0; i<l; i++){
                	childState = initStaticSelectedNode(item.ChildNodes[i]);
                	if((state == 1 && childState != 1) ||
                			(dfop.cascadecheck && dfop.parentCascadecheck && state == 0 && childState != 0)){
                		state = 2;
                	}
                	if(childState != 1){
                		allChildSelected = 0;
                	}
            	}
            	if(state == 2 && allChildSelected == 1){
                	state = 1;
                }
            }
            item.checkstate = state;
            return state;
        }
        
        me[0].t = {
        	/**
			 * 得到树的所有节点
			 * 
			 * @return 树的所有节点（需要递归遍历）
			 */
        	getAllTreeNodes: function() {
        		return treenodes;
        	},
            getSelectedNodes: function(gethalfchecknode) {
            	var s = [];
                if (gethalfchecknode) {
                    getCkAndHalfCk(treenodes, s, function(item) { return item; }, 0);
                }
                else {
                    getck(treenodes, s, function(item) { return item; }, 0);
                }
                return s;
            },
            getSelectedIds: function() {
            	var initSelectedNode = dfop.aHasSelected;
                var s = {singleNode:[], multiNode:[]};
                if(dfop.aNeedLazy){// 延迟加载数据
	                try{
	                	getCkAndHalfCkIds(treenodes, s, function(item) { return item.id; }, 0);
	                	if(dfop.aHasSelected && dfop.aHasSelected != "" && dfop.aHasSelected != ","){// 还有初始化选中的节点Id
	                		var aHasSelected = dfop.aHasSelected;
	                		aHasSelected = aHasSelected.substring(1, aHasSelected.length - 1);
	                		var aHasSelectedIds = aHasSelected.split(",");
	                		for(var j=0; j<aHasSelectedIds.length; j++){
	                			s.singleNode.push(aHasSelectedIds[j]);
	                		}
	                	}
	               	}catch(e){
	               		alert(e);
	                }finally{
	                	dfop.aHasSelected = initSelectedNode;
	                }
	                return s;
                }else{// 一次性加载数据
                	getCkAndHalfCk(treenodes, s.singleNode, function(item) { return item.id; }, 0);
                	return s.singleNode;
                }
            },
            getCurrentItem: function() {
                return dfop.citem;
            },
            reflash: function(itemOrItemId) {
                var id;
                if (typeof (itemOrItemId) == "string") {
                    id = itemOrItemId;
                }
                else {
                    id = itemOrItemId.id;
                }
                reflash(id);
            },
			/**
			 * 根据节点Id得到节点信息
			 * 
			 * @return 给定Id的树节点信息
			 */
            getNodeById: function(nodeId){
            	var path = idPathMap['' + nodeId];
                var item = getItem(path);
                if (item) {
                	return item;
                }
	            return null;
            },
            /**
			 * 由用户直接选中树节点中某个节点
			 * 
			 * @param nodeId
			 *            -树节点Id
			 * @param checkState
			 *            -选中状态 0或1
			 * @param cascadeState
			 *            -是否需要级联 true或者false
			 * @return true或者false
			 */
	        checkItem: function(nodeId, checkState, cascadeState){
	        	var path = idPathMap['' + nodeId];
                if(path || path === 0){
                	var item = getItem(path);
		        	if (dfop.showradio) // 点击了radio
		            {
		            	if(cascadeState){
		                	clearSeleted(me[0].t);// 删除自己和子级节点在初始化选中节点Id中的值
		            	}
		                checkRadio(item, checkState);// 点击单选框
		            }
		            else if (dfop.showcheck) // 点击了Checkbox
		            {
		                var s = checkState;
		                if (cascadeState && dfop.cascadecheck) {
		                	item.isSelectedAll = true;// 设置该节点的下级子节点应该全部选中
		            		removeSelfOrChildrenSeleted(me[0].t, item.id);// 删除自己和子级节点在初始化选中节点Id中的值
		                    // 遍历
		                    cascade(check, item, s);
		            		if(dfop.parentCascadecheck){
		                    	// 上溯
		                    	bubble(check, item, s);
		            		}
		                }else if(dfop.cascadecheck && dfop.parentCascadecheck){
		                	check2(item, s, 1);// 改变节点选中状态
		                    bubble(check2, item, s);// 向上回溯父节点
		                }else {
		                    check2(item, s, 1);// 改变节点选中状态
		                    bubble2(check2, item, s);// 向上回溯父节点
		                }
		            }
		            return true;
                }
                return false;
	        },
			/**
			 * 得到初始化选中节点Id集
			 * 
			 * @return 初始化选中节点Id集
			 */
			getHasSelected: function(){
		    	return dfop.aHasSelected;
		    },
		    
		    /**
			 * 设置初始化选中节点Id集
			 * 
			 * @param hasSelected
			 *            -初始化选中节点Id集
			 * @return
			 */
		    setHasSelected: function(hasSelected){
		        dfop.aHasSelected = hasSelected;
		    },
		    /**
			 * 重新加载树节点数据
			 * 
			 * @param data
			 *            -要重新加载的树节点数据，json格式
			 * @param aHasSelected
			 *            -初始化选中的树节点Id集
			 * @return
			 */
		    reLoadData: function(data, aHasSelected){
		    	reLoadData(data, aHasSelected);
		    }
        };
        reLoadData(dfop.data, dfop.aHasSelected);
        return me;
    };
    // 获取所有选中的节点的Ids数组
    $.fn.getTSIds = function() {
        if (this[0].t) {
            return this[0].t.getSelectedIds();
        }
        return null;
    };
    // 获取所有选中的节点的Item数组
    $.fn.getTSNs = function(gethalfchecknode) {
        if (this[0].t) {
            return this[0].t.getSelectedNodes(gethalfchecknode);
        }
        return null;
    };
    $.fn.getTCT = function() {
        if (this[0].t) {
            return this[0].t.getCurrentItem();
        }
        return null;
    };
    $.fn.reflash = function(ItemOrItemId) {
        if (this[0].t) {
            return this[0].t.reflash(ItemOrItemId);
        }
    };
	/**
	 * 得到树的所有节点
	 * 
	 * @return 树的所有节点（需要递归遍历）
	 */
    $.fn.getAllTreeNodes = function() {
    	return this[0].t.getAllTreeNodes();
    }
    /**
	 * 根据节点Id得到节点信息
	 * 
	 * @return 给定Id的树节点信息
	 */
    $.fn.getNodeById = function(nodeId){
    	return this[0].t.getNodeById(nodeId);
    }
    /**
	 * 由用户直接选中树节点中某个节点
	 * 
	 * @param nodeId
	 *            -树节点Id
	 * @param checkState
	 *            -选中状态 0或1
	 * @param cascade
	 *            -是否需要级联 true或者false
	 * @return
	 */
    $.fn.checkItem = function(nodeId, checkState, cascade){
    	return this[0].t.checkItem(nodeId, checkState, cascade);
    }
    /**
	 * 重新加载树节点数据
	 * 
	 * @param data
	 *            -要重新加载的树节点数据，json格式
	 * @param aHasSelected
	 *            -初始化选中的节点Id集
	 * @return
	 */
    $.fn.reLoadData = function(data, aHasSelected){
    	this[0].t.reLoadData(data, aHasSelected);
    }
})(jQuery);

/**
 * 检测初始化选中的节点中是否有给定节点的子节点，默认实现，可由用户重载
 * 
 * @param {}
 *            aThis -当前树对象
 * @param {}
 *            nodeId -节点Id
 * @return true或者false
 */
function checkChildrenHasSelected(aThis, nodeId){
	pattern = new RegExp("\\," + nodeId + "\\S+\\,");
	return aThis.getHasSelected() ? pattern.test(aThis.getHasSelected()) : false;
}

/**
 * 检测初始化选中的节点中是否有给定节点，默认实现，可由用户重载
 * 
 * @param {}
 *            aThis -当前树对象
 * @param {}
 *            nodeId -节点Id
 * @return true或者false
 */
function checkSelfHasSelected(aThis, nodeId){
	pattern = new RegExp("\\," + nodeId + "\\,");
	return aThis.getHasSelected() ? pattern.test(aThis.getHasSelected()) : false;
}

/**
 * 检测初始化选中的节点中是否有给定节点或者给定节点的子节点，默认实现，可由用户重载
 * 
 * @param {}
 *            aThis -当前树对象
 * @param {}
 *            nodeId -节点Id
 * @return true或者false
 */
function checkSelfOrChildrenHasSelected(aThis, nodeId){
	pattern = new RegExp("\\," + nodeId + "\\S*\\,");
	return aThis.getHasSelected() ? pattern.test(aThis.getHasSelected()) : false;
}

/**
 * 删除初始化选中的节点中包含的给定节点或者给定节点的子节点，默认实现，可由用户重载
 * 
 * @param {}
 *            aThis -当前树对象
 * @param {}
 *            nodeId -节点Id
 * @return
 */
function removeSelfOrChildrenSeleted(aThis, nodeId){
	pattern = new RegExp("\\," + nodeId + "\\S*\\,");
	if(aThis.getHasSelected()){
		aThis.setHasSelected(aThis.getHasSelected().replace(pattern, ","));
	}
}

/**
 * 删除初始化选中的节点中包含的给定节点，默认实现，可由用户重载
 * 
 * @param {}
 *            aThis -当前树对象
 * @param {}
 *            nodeId -节点Id
 * @return
 */
function removeSelfSeleted(aThis, nodeId){
	pattern = new RegExp("\\," + nodeId + "\\,");
	if(aThis.getHasSelected()){
		aThis.setHasSelected(aThis.getHasSelected().replace(pattern, ","));
	}
}

/**
 * 清空初始化选中的节点数据
 * 
 * @param {}
 *            aThis -当前树对象
 * @return
 */
function clearSeleted(aThis){
	if(aThis.getHasSelected()){
		aThis.setHasSelected(null);
	}
}

/** 表单验证组件 * */
(function($) {
	
$.extend($.fn, {
	
	// http://docs.jquery.com/Plugins/Validation/validate
	validate : function(options) {
		// if nothing is selected, return nothing; can't chain anyway
		if (!this.length) {
			options
					&& options.debug
					&& window.console
					&& console
							.warn("nothing selected, can't validate, returning nothing");
			return;
		}

		// check if a validator for this form was already created
		var validator = $.data(this[0], 'validator');
		if (validator) {
			return validator;
		}

		validator = new $.validator(options, this[0]);
		$.data(this[0], 'validator', validator);
		// 表单提交时进行验证设置为有效
		if (validator.settings.onsubmit) {
			// allow suppresing validation by adding a cancel class to the
			// submit button
			// .cancel样式的按钮点击事件触发
			this.find("input, button").filter(".cancel").click(function() {
				validator.cancelSubmit = true;
			});

			// when a submitHandler is used, capture the submitting button
			// 当submitHandler设置为有效时，type为submit的按钮为提交按钮
			if (validator.settings.submitHandler) {
				this.find("input, button").filter(":submit").click(function() {
					validator.submitButton = this;
				});
			}

			// validate the form on submit
			this.submit(function(event) {
				if (validator.settings.debug)
					// prevent form submit to be able to see console output
					event.preventDefault();

				function handle() {
					// 去除表单中录入数据的前后空白字符
					trimForm(validator.currentForm);
					if (validator.settings.submitHandler) {
						if (validator.submitButton) {
							// insert a hidden input as a replacement for the
							// missing submit button
							var hidden = $("<input type='hidden'/>").attr(
									"name", validator.submitButton.name)
									.val(validator.submitButton.value)
									.appendTo(validator.currentForm);
						}
						// submitHandler函数输入参数为当前验证的表单对象
						validator.settings.submitHandler.call(validator,
								validator.currentForm);
						if (validator.submitButton) {
							// and clean up afterwards; thanks to
							// no-block-scope, hidden can be referenced
							hidden.remove();
						}
						//去除submitHandler固定返回false约束，返回true
						return false;
					}
					if(validator.settings.submitTip){//表单提交时使用提示信息
						showLoadingTip("数据正在提交，请稍候...")
					}
					
					return true;
				}

				// prevent submit for invalid forms or custom submit handlers
				if (validator.cancelSubmit) {
					validator.cancelSubmit = false;
					return handle();
				}
				// 验证表单方法
				if (validator.form()) {
					if (validator.pendingRequest) {
						validator.formSubmitted = true;
						return false;
					}
					return handle();
				} else {
					validator.focusInvalid();
					return false;
				}
			});
			$(this)[0].onSubmit = function(event){
				if (validator.settings.debug)
					// prevent form submit to be able to see console output
					event.preventDefault();

				function handle() {
					// 去除表单中录入数据的前后空白字符
					trimForm(validator.currentForm);
					if (validator.settings.submitHandler) {
						if (validator.submitButton) {
							// insert a hidden input as a replacement for the
							// missing submit button
							var hidden = $("<input type='hidden'/>").attr(
									"name", validator.submitButton.name)
									.val(validator.submitButton.value)
									.appendTo(validator.currentForm);
						}
						// submitHandler函数输入参数为当前验证的表单对象
						validator.settings.submitHandler.call(validator,
								validator.currentForm);
						if (validator.submitButton) {
							// and clean up afterwards; thanks to
							// no-block-scope, hidden can be referenced
							hidden.remove();
						}
						//去除submitHandler固定返回false约束，返回true
						//return false;
					}
					return true;
				}

				// prevent submit for invalid forms or custom submit handlers
				if (validator.cancelSubmit) {
					validator.cancelSubmit = false;
					return handle();
				}
				// 验证表单方法
				if (validator.form()) {
					if (validator.pendingRequest) {
						validator.formSubmitted = true;
						return false;
					}
					return handle();
				} else {
					validator.focusInvalid();
					return false;
				}
			};
		}
		return validator;
	},
	// http://docs.jquery.com/Plugins/Validation/valid
	valid : function() {
		if ($(this[0]).is('form')) {
			return this.validate().form();
		} else {
			var valid = true;
			var validator = $(this[0].form).validate();
			this.each(function() {
				valid &= validator.element(this);
			});
			return valid;
		}
	},
	// attributes: space seperated list of attributes to retrieve and remove
	removeAttrs : function(attributes) {
		var result = {}, $element = this;
		$.each(attributes.split(/\s/), function(index, value) {
			result[value] = $element.attr(value);
			$element.removeAttr(value);
		});
		return result;
	},
	// http://docs.jquery.com/Plugins/Validation/rules
	rules : function(command, argument) {
		var element = this[0];

		if (command) {
			var settings = $.data(element.form, 'validator').settings;
			var staticRules = settings.rules;
			var existingRules = $.validator.staticRules(element);
			switch (command) {
				case "add" :
					$
							.extend(existingRules, $.validator
									.normalizeRule(argument));
					staticRules[element.name] = existingRules;
					if (argument.messages)
						settings.messages[element.name] = $.extend(
								settings.messages[element.name],
								argument.messages);
					break;
				case "remove" :
					if (!argument) {
						delete staticRules[element.name];
						return existingRules;
					}
					var filtered = {};
					$.each(argument.split(/\s/), function(index, method) {
						filtered[method] = existingRules[method];
						delete existingRules[method];
					});
					return filtered;
			}
		}

		var data = $.validator.normalizeRules($.extend({}, $.validator
				.metadataRules(element), $.validator.classRules(element),
				$.validator.attributeRules(element), $.validator
						.staticRules(element)), element);

		// make sure required is at front
		if (data.required) {
			var param = data.required;
			delete data.required;
			data = $.extend({
				required : param
			}, data);
		}

		return data;
	}
});

// Custom selectors
$.extend($.expr[":"], {
	// http://docs.jquery.com/Plugins/Validation/blank
	blank: function(a) {return !$.trim(a.value);},
	// http://docs.jquery.com/Plugins/Validation/filled
	filled: function(a) {return !!$.trim(a.value);},
	// http://docs.jquery.com/Plugins/Validation/unchecked
	unchecked: function(a) {return !a.checked;}
});

// constructor for validator
$.validator = function( options, form ) {
	this.settings = $.extend( {}, $.validator.defaults, options );
	this.currentForm = form;
	this.init();
};

//处理range等验证方法的错误信息
$.validator.format = function(source, params) {
	if ( arguments.length == 1 )
		//返回给message一个function
		return function() {
			var args = $.makeArray(arguments);
			args.unshift(source);
			//重新调用format方法
			return $.validator.format.apply( this, args );
		};
	if ( arguments.length > 2 && params.constructor != Array  ) {
		params = $.makeArray(arguments).slice(1);
	}
	if ( params.constructor != Array ) {
		params = [ params ];
	}
	var reg = new RegExp("\\{\\d+\\}");
	$.each(params, function(i, n) {
		//source = source.replace(new RegExp("\\{" + i + "\\}", "g"), n);
		source = source.replace(reg, n);
	});
	return source;
};

// 处理range等验证方法的错误信息，取代$.validator.format()方法，直接使用‘，’分隔而不采用数组形式
$.validator.formatString = function(source, params) {
	if (arguments.length == 1){
		// 返回给message一个function
		return function() {
			var args = $.makeArray(arguments);
			args.unshift(source);
			// 重新调用formatString方法
			return $.validator.formatString.apply(this, args);
		};
	}
	if (arguments.length > 2 && params.constructor != Array) {
		params = params.toString();//在某些情况下将params从Number转换为String类型
		params = $.makeArray(params.split(","));
	}
	if (params.constructor != Array) {
		params = [params];
	}
	var reg = new RegExp("\\{\\d+\\}");
	$.each(params, function(i, n) {
		//source = source.replace(new RegExp("\\{" + i + "\\}", "g"), n);
		source = source.replace(reg, n);
	});
	return source;
};

$.extend($.validator, {
	
	defaults: {
		//错误提示信息
		messages: {},
		groups: {},
		//表单验证规则
		rules: {},
		//验证错误的展现样式名称，高亮有效时使用
		errorClass: "error",
		//验证错误的展现样式名称，高亮无效时使用
		validClass: "",
		tipClass: "tooltip",//采用tip信息展现方式时，验证错误提示信息主体样式名称
		errorElement: "label",
		//聚焦验证未通过的元素
		focusInvalid: true,
		errorContainer: $( [] ),
		errorLabelContainer: $( [] ),
		//是否在表单提交时进行验证
		onsubmit: true,
		//忽略掉的表单元素，即不进行表单验证
		ignore: [],
		ignoreTitle: false,
		submitTip: true,//表单提交时是否展示提示信息,依赖于common.js
		onfocusin : function(element) {
			//this.lastActive = element;
			// hide error label and remove error class on focus if enabled
			//if (this.settings.focusCleanup && !this.blockFocusCleanup) {
				this.settings.unhighlight
						&& this.settings.unhighlight.call(this, element,
								this.settings.errorClass,
								this.settings.validClass);
				//this.errorsFor(element).hide();
			//}
		},
		// 取消onkeyup验证事件
		onkeyup : function(element) {
		},
		// onfocus事件中取消验证元素depending，否则必须提交一遍表单才能实现
		onfocusout : function(element) {
			if($(element).attr("tagFlag") == "numberInput"){//numberInput组件
				element = $("#" + element.id.substring(1));
				this.element(element);
			}else if (($(element).attr("tagFlag") != "dateTime") && !this.checkable(element)) {//排除日期组件
				this.element(element);
			}
		},
		onclick: function(element) {
			if (this.checkable(element)) {
				this.element(element);
			}
		},
		//高亮验证错误的表单元素
		highlight: function( element, errorClass, validClass ) {
			$(element).addClass(errorClass).removeClass(validClass);
		},
		//不高亮验证错误的表单元素
		unhighlight: function( element, errorClass, validClass ) {
			$(element).removeClass(errorClass).addClass(validClass);
		}
	},

	// http://docs.jquery.com/Plugins/Validation/Validator/setDefaults
	setDefaults: function(settings) {
		$.extend( $.validator.defaults, settings );
	},
	//默认的验证错误提示信息
	messages: {
		required : "errors_required",
		remote : "errors_remote",
		email : "errors_email",
		url : "errors_url",
		date : "errors_date",
		dateISO : "errors_dateISO",
		dateDE : "errors_dateDE",
		number : "errors_number",
		numberDE : "errors_numberDE",
		digits : "errors_digits",
		creditcard : "errors_creditcard",
		equalTo : "errors_equalTo",
		accept : "errors_accept",
		maxlength : "errors_maxlength",
		minlength : "errors_minlength",
		rangelength : "errors_rangelength",
		range : "errors_range",
		max : "errors_max",
		min : "errors_min"
	},
	
	autoCreateRanges: false,
	
	prototype: {
		
		/**
		 * 根据errorLabel得到对应的控件元素的Jquery对象
		 * @param {} $error -错误页面
		 * @return 对应的控件元素Jquery对象
		 */
		_getElementByError: function($error){
			var idOrName = $error.attr("for");
			//id为idOrName
			var element = $("#" + idOrName);
			if(!element[0]){//如果idOrName不是id值，则是name值
				element = $("*[name='" + idOrName + "']");
			}
			if(!element[0]){//如果Id和name都不存在，则该元素不存在
				return null;
			}
			return element;
		},
		
		/**
		 * 根据页面控件元素找到该元素同一行最后一个Input、a或img元素
		 * @param {} $element -页面控件Jquery对象
		 * @return 对应的控件元素Jquery对象
		 */
		_getLastElement: function($element){
			var element1 = $element;
			
			if($element.attr("tagFlag") && $element.attr("tagFlag") == "numberInput"){//如果是页面的标签控件，如货币、数量标签
				var idOrName = this.idOrName($element[0]);
				idOrName = "_" + idOrName;//查找货币、数量标签的展现控件
				//id为idOrName
				var element1 = $("#" + idOrName);
				if(!element1[0]){//如果idOrName不是id值，则是name值
					element1 = $("*[name='" + idOrName + "']");
				}
			}
			
			//如果element后面是input、a、img元素则error label放在这些元素后面
			while($(element1).next()[0] && /input|a|img/i.test($(element1).next()[0].tagName) && "block" == $(element1).next().attr("display")){
				element1 = $(element1).next();
			}
			return element1;
		},
			
		/**
		 * 设置errorLabel的大小和位置
		 * @param {} $element -在该元素后面放置errorLabel
		 * @param {} $errorLabel -要放置的errorLabel
		 */
		_placeErrorLabel: function($element, $errorLabel){
			var pos = $element.position();//element1相对于视窗的位置
			var dim = {width:$element.outerWidth(), height:$element.outerHeight()};//element1的实际高度和宽度
			
			var dim2 = {width:$errorLabel.width(), height:$errorLabel.height()};//error label的高度和宽度
			var scrolltop = 0;
			var scrollleft = 0;
			var parentElement = $element;
			parentElement = $(parentElement.parent());
			while(parentElement[0]){//element1的滚动宽度和高度
				if(parentElement[0].tagName && parentElement[0].tagName.toLowerCase() == "body"){
					if($errorLabel.parent()[0].tagName && $errorLabel.parent()[0].tagName.toLowerCase() == "body"){//如果label的父容器就是body，则需要考虑body的滚动距离
						scrolltop = scrolltop + parentElement[0].scrollTop;
						scrollleft = scrollleft + parentElement[0].scrollLeft;
					}
					break;
				}
				scrolltop = scrolltop + parentElement[0].scrollTop;
				scrollleft = scrollleft + parentElement[0].scrollLeft;
				
				parentElement = $(parentElement.parent());
			}
			//先设置css一次，使得能够获取到准确的高度和宽度
			$errorLabel.css({"left":"" + (pos.left + dim.width + scrollleft), "top":"" + (pos.top + scrolltop + dim.height/2 - dim2.height/2) });
			dim2 = {width:$errorLabel.width(), height:$errorLabel.height()};//重新获取error label的高度和宽度
			$errorLabel.css({"left":"" + (pos.left + dim.width + scrollleft) + "px", "top":"" + (pos.top + scrolltop + dim.height/2 - dim2.height/2) + "px" });
			$errorLabel.css({"width":"" + dim2.width, "height":"" + dim2.height});
		},
		
		/**
		 * 当前窗口大小变化时改变error label的相对视窗的位置
		 */
		windowResize: function(){
			var errorLabels = $($.validator.defaults.errorElement + "." + $.validator.defaults.errorClass.split(" ")[0]);
			$.each(errorLabels, function(index, errorLabel){
				var width = $(errorLabel).width();//保存窗口变换之前的宽度
				var height = $(errorLabel).height();//保存窗口变换之前的高度
				
				$(errorLabel).css({"width":"","height":""});
				var element = this._getElementByError($(errorLabel));
				if(!element || !element[0]){//如果Id和name都不存在，则该元素不存在
					return;
				}
				//如果element后面是input、a、img元素则error label放在这些元素后面
				var element1 = this._getLastElement(element);
				
				var pos = $(element1).position();//element1相对于视窗的位置
				var dim = {width:$(element1).outerWidth(), height:$(element1).outerHeight()};//element1的实际高度和宽度
				
				if($(errorLabel).width() < width){//如果变换之后的label宽度小于之前的宽度，则label保持之前的宽度和高度不变
					$(errorLabel).css({"width":width,"height":height});
				}else{
					$(errorLabel).css({"width":"" + $(errorLabel).width(), "height":"" + $(errorLabel).height()});
				}
				
				this._placeErrorLabel(element1, $(errorLabel));
			});
		},
		
		//初始化函数
		init: function() {
			this.labelContainer = $(this.settings.errorLabelContainer);
			this.container = $(this.settings.container);
			//若设置了错误标签展现容器则返回错误标签展现容器，否则返回当前表单
			this.errorContext = this.labelContainer.length && this.labelContainer || $(this.currentForm);
			//将错误标签展现容器加入到错误展现容器中
			this.containers = $(this.settings.errorContainer).add( this.settings.errorLabelContainer );
			//已经处理过的页面元素，为对象
			this.submitted = {};
			this.valueCache = {};
			this.pendingRequest = 0;
			this.pending = {};
			this.invalid = {};
			//表单重置函数
			this.reset();
			
			var groups = (this.groups = {});
			$.each(this.settings.groups, function(key, value) {
				$.each(value.split(/\s/), function(index, name) {
					groups[name] = key;
				});
			});
			//表单验证策略，key为表单元素Id，value为表单元素验证策略
			var rules = this.settings.rules;
			$.each(rules, function(key, value) {
				rules[key] = $.validator.normalizeRule(value);
			});
			//处理设置的代理事件，当相应时间发生时回调用户自定义的事件处理函数function
			function delegate(event) {
				var validator = $.data(this[0].form, "validator");
				validator.settings["on" + event.type] && validator.settings["on" + event.type].call(validator, this[0] );
			}
			$(this.currentForm)
				.delegate("focusin focusout keyup", ":text, :password, :file, select, textarea", delegate)
				.delegate("click", ":radio, :checkbox", delegate);

			if (this.settings.invalidHandler)
				$(this.currentForm).bind("invalid-form.validate", this.settings.invalidHandler);
		},

		// http://docs.jquery.com/Plugins/Validation/Validator/form
		//表单元素验证，通过则返回true，不通过则返回false
		form: function() {
			this.checkForm();
			$.extend(this.submitted, this.errorMap);
			this.invalid = $.extend({}, this.errorMap);
			if (!this.valid())
				$(this.currentForm).triggerHandler("invalid-form", [this]);
			this.showErrors();
			return this.valid();
		},
		//对表单中每个要验证的元素进行验证
		checkForm: function() {
			this.prepareForm();
			for ( var i = 0, elements = (this.currentElements = this.elements()); elements[i]; i++ ) {
				this.check( elements[i] );
			}
			return this.valid(); 
		},
		
		// http://docs.jquery.com/Plugins/Validation/Validator/element
		//验证单个表单元素，通过则返回true，未通过则返回false
		//这个函数行为类似validation的on blur 和on keyup事件，但是会返回结果result
		element: function( element ) {
			element = this.clean( element );
			this.lastElement = element;
			this.prepareElement( element );
			this.currentElements = $(element);
			var result = this.check( element );
			if ( result ) {
				delete this.invalid[element.name];
			} else {
				this.invalid[element.name] = true;
			}
			if ( !this.numberOfInvalids() ) {
				// Hide error containers on last error
				this.toHide = this.toHide.add( this.containers );
			}
			this.showErrors();
			return result;
		},

		// http://docs.jquery.com/Plugins/Validation/Validator/showErrors
		// Show the specified messages.
		// Keys have to refer to the names of elements, values are displayed for those elements, using the configured error placement.
		// var validator = $("#myform").validate();
		// validator.showErrors({"firstname": "I know that your firstname is Pete, Pete!"});
		showErrors: function(errors) {
			if(errors) {
				// add items to error list and map
				$.extend( this.errorMap, errors );
				this.errorList = [];
				for ( var name in errors ) {
					//errorList中保存的message为字符串，element为页面元素类型即Element
					this.errorList.push({
						message: errors[name],
						element: this.findByName(name)[0]
					});
				}
				// remove items from success list
				// 从successList中移除验证错误的元素
				this.successList = $.grep( this.successList, function(element) {
					return !(element.name in errors);
				});
			}
			//如果设置了showErrors处理函数，则执行该处理函数，该函数输入参数为validator、errorMap和errorList
			//否则执行默认defaultShowErrors()方法
			this.settings.showErrors
				? this.settings.showErrors.call( this, this.errorMap, this.errorList )
				: this.defaultShowErrors();
		},
		
		// http://docs.jquery.com/Plugins/Validation/Validator/resetForm
		//重置整个表单，消除表单验证错误的提示信息
		//已提交元素置为空
		resetForm: function() {
			if ( $.fn.resetForm )
				$( this.currentForm ).resetForm();
			this.submitted = {};
			this.prepareForm();
			this.hideErrors();
			this.elements().removeClass( this.settings.errorClass );
		},
		
		//验证通过的元素个数
		numberOfInvalids: function() {
			return this.objectLength(this.invalid);
		},
		
		//计算对象属性个数
		objectLength: function( obj ) {
			var count = 0;
			for ( var i in obj ){
				count++;
			}
			return count;
		},
		
		hideErrors: function() {
			this.addWrapper( this.toHide ).hide();
		},
		//表单验证错误元素长度是否等于0，即验证是否成功
		valid: function() {
			return this.size() == 0;
		},
		//表单验证错误元素长度
		size: function() {
			return this.errorList.length;
		},
		
		focusInvalid: function() {
			if( this.settings.focusInvalid ) {
				try {
					$(this.findLastActive() || this.errorList.length && this.errorList[0].element || []).filter(":visible").focus();
				} catch(e) {
					// ignore IE throwing errors when focusing hidden elements
				}
			}
		},
		
		findLastActive: function() {
			var lastActive = this.lastActive;
			return lastActive && $.grep(this.errorList, function(n) {
				return n.element.name == lastActive.name;
			}).length == 1 && lastActive;
		},
		
		//返回一个数组，该数组中是需要验证的表单元素，元素类型为“input”并且验证策略不为空
		elements: function() {
			var validator = this,
				rulesCache = {};
			
			// select all valid inputs inside the form (no submit or reset buttons)
			// workaround $Query([]).add until http://dev.jquery.com/ticket/2114 is solved
			return $([]).add(this.currentForm.elements)
			.filter(":input")
			.not(":submit, :reset, :image, [disabled]")
			.not( this.settings.ignore )
			.filter(function() {
				!this.name && validator.settings.debug && window.console && console.error( "%o has no name assigned", this);
			
				// select only the first element for each name, and only those with rules specified
				//每个名字的元素只选择一个并且该元素的验证策略不为空
				//修改成非选择性元素可以重名
				if ( (this.name in rulesCache && /radio|checkbox/i.test(this.type)) || !validator.objectLength($(this).rules()) )
					return false;
				
				rulesCache[this.name] = true;
				return true;
			});
		},
		//获取dom对象
		clean: function( selector ) {
			return $( selector )[0];
		},
		
		//所有error label
		//errors: function() {
		//	return $( this.settings.errorElement + "." + this.settings.errorClass, this.errorContext );
		//},
		
		//所有error label
		errors: function() {
			return $( this.settings.errorElement + "." + this.settings.errorClass.split(" ")[0]);
		},
		
		reset: function() {
			this.successList = [];
			this.errorList = [];
			this.errorMap = {};
			this.toShow = $([]);
			this.toHide = $([]);
			this.formSubmitted = false;
			this.currentElements = $([]);
		},
		//预备表单
		prepareForm: function() {
			this.reset();
			this.toHide = this.errors().add( this.containers );
		},
		//预备表单元素
		prepareElement: function( element ) {
			this.reset();
			this.toHide = this.errorsFor(element);
		},
		
		check: function( element ) {
			element = this.clean( element );
			
			// if radio/checkbox, validate first element in group instead
			//如果是radio或checkbox类型的表单元素，则验证相同Name的一个元素
			if (this.checkable(element)) {
				element = this.findByName( element.name )[0];
			}
			//获取到该元素下的验证策略信息
			var rules = $(element).rules();
			var dependencyMismatch = false;
			//循环验证策略中的每一个策略
			for( method in rules ) {
				var rule = { method: method, parameters: rules[method] };
				try {
					//验证函数，输入参数为validator、元素值、元素及验证策略参数
					var result = $.validator.methods[method].call( this, element.value.replace(/\r/g, ""), element, rule.parameters );
					// if a method indicates that the field is optional and therefore valid,
					// don't mark it as valid when there are no other rules
					if ( result == "dependency-mismatch" ) {
						dependencyMismatch = true;
						continue;
					}
					dependencyMismatch = false;
					if ( result == "pending" ) {
						this.toHide = this.toHide.not( this.errorsFor(element) );
						return;
					}
					
					if( !result ) {
						this.formatAndAdd( element, rule );
						return false;
					}
				} catch(e) {
					this.settings.debug && window.console && console.log("exception occured when checking element " + element.id
						 + ", check the '" + rule.method + "' method");
					throw e;
				}
			}
			if (dependencyMismatch)
				//return;
				return true;//若dependency-mismatch,则默认返回true
			if ( this.objectLength(rules) )
				this.successList.push(element);
			return true;
		},
		
		// return the custom message for the given element and validation method
		// specified in the element's "messages" metadata
		customMetaMessage: function(element, method) {
			if (!$.metadata)
				return;
			
			var meta = this.settings.meta
				? $(element).metadata()[this.settings.meta]
				: $(element).metadata();
			
			return meta && meta.messages && meta.messages[method];
		},
		
		// return the custom message for the given element name and validation method
		customMessage: function( name, method ) {
			var m = this.settings.messages[name];
			return m && (m.constructor == String
				? m
				: m[method]);
		},
		
		// return the first defined argument, allowing empty strings
		//找到第一个传入的有效参数
		findDefined: function() {
			var index = new RegExp("\\{\\d+\\}");
			for(var i = 0; i < arguments.length; i++) {
				if (arguments[i] !== undefined){
					var argumentsI = eval(arguments[i]);
					if(index.test(argumentsI)){//若message中包含{0}的参数传入格式，则作为函数处理
						return $.validator.formatString(argumentsI);
					}else{
						return argumentsI;
					}
				}
			}
			return undefined;
		},
		
		//获取验证错误提示信息
		//1.用户在Html控件中自定义的验证错误信息
		//2.用户自定义信息
		//3.用户自定义meta信息
		//4.元素的title属性信息
		//5.validator默认提供的信息
		//6.提示没有为该属性配置错误提示信息
		defaultMessage: function( element, method) {
			return this.findDefined(
				$(element).attr(method + "Msg")?$(element).attr(method + "Msg"):undefined,//添加Html控件中的自定义验证错误信息
				this.customMessage( element.name, method ),
				this.customMetaMessage( element, method ),
				// title is never undefined, so handle empty string as undefined
				!this.settings.ignoreTitle && element.title || undefined,
				$.validator.messages[method],
				"<strong>Warning: No message defined for " + element.name + "</strong>"
			);
		},
		
		//表单元素验证出错后的处理，并将相应出错信息加入errorList、errorMap、submitted中去
		formatAndAdd: function( element, rule ) {
			var message = this.defaultMessage( element, rule.method );
			//表单验证错误的处理信息可以是回调函数
			if ( typeof message == "function" ){
				//该回调函数的输入参数为 验证策略的参数 和 验证的表单元素，同时需要返回验证错误信息
				//用于$.validator.format处
				message = message.call(this, rule.parameters, element);
			}
			this.errorList.push({
				message: message,
				element: element
			});
			//出错map，存储内容为 key为表单元素Id值，value为出错提示信息
			this.errorMap[element.name] = message;
			//已经验证过的元素，存储内容为 key为表单元素Id值，value为出错提示信息
			this.submitted[element.name] = message;
		},
		
		//利用设置中的元素包裹toToggle元素
		addWrapper: function(toToggle) {
			if ( this.settings.wrapper )
				toToggle = toToggle.add( toToggle.parent( this.settings.wrapper ) );
			return toToggle;
		},
		
		//默认错误显示函数
		defaultShowErrors: function() {
			for ( var i = 0; this.errorList[i]; i++ ) {
				var error = this.errorList[i];
				//高亮操作可以调用回调函数，该函数的输入参数为validator、表单元素、出错样式、验证样式
				this.settings.highlight && this.settings.highlight.call( this, error.element, this.settings.errorClass, this.settings.validClass );
				this.showLabel( error.element, error.message );
			}
			if( this.errorList.length ) {
				this.toShow = this.toShow.add( this.containers );
			}
			if (this.settings.success) {
				for ( var i = 0; this.successList[i]; i++ ) {
					this.showLabel( this.successList[i] );
				}
			}
			if (this.settings.unhighlight) {
				for ( var i = 0, elements = this.validElements(); elements[i]; i++ ) {
					this.settings.unhighlight.call( this, elements[i], this.settings.errorClass, this.settings.validClass );
				}
			}
			this.toHide = this.toHide.not( this.toShow );
			this.hideErrors();
			this.addWrapper( this.toShow ).show();
		},
		
		//返回验证正确的表单元素
		validElements: function() {
			return this.currentElements.not(this.invalidElements());
		},
		
		//返回验证错误的表单元素
		invalidElements: function() {
			return $(this.errorList).map(function() {
				return this.element;
			});
		},
		
		//初始化错误页面中的html
		errorLabelInitHtml: function(message){
			var arr = [];
			if(!this.labelContainer || this.labelContainer.length == 0){//采用tips显示错误信息
				arr.push("  <table border='0' cellspacing='0' cellpadding='0' class='tooltiptable'>");
				arr.push("  	<tr><td class='cornerTop topleft'></td>");
				arr.push("          <td class='topcenter'></td>");
				arr.push("          <td class='cornerTop topright'></td></tr>");
				arr.push("  	<tr><td class='corner bodyleft'></td>");
				arr.push("			<td class='tooltipcontent'>"+message+"</td>");
				arr.push("			<td class='corner bodyright'></td></tr>");
				arr.push("		<tr><td class='cornerTop footerleft'></td>")
				arr.push("          <td class='footercenter'></td>");
				arr.push("			<td class='cornerTop footerright'></td></tr>");
				arr.push("  </table>");
			}else{
				arr.push(message);
			}
			return arr.join('');
		},
		
		// 展现表单验证出错信息,修改成提示页面以div展现
		showLabel: function(element, message) {
			var label = this.errorsFor(element);
			//如果label已经存在
			if (label && label.length > 0) {
				// check if we have a generated label, replace the message then
				label.attr("generated") && label.html(this.errorLabelInitHtml(message));//html代码为<Table>出错信息代码
				
				// refresh error/success class
				label.css({"width":"","height":""});//去掉label样式中宽度和高度的设定
				label.removeClass().addClass(this.settings.errorClass);
				
				if(!this.labelContainer || this.labelContainer.length == 0){//采用tips显示错误信息
					label.addClass(this.settings.tipClass);
					var element1 = this._getLastElement($(element));
					this._placeErrorLabel(element1, label);
					label.show();
				}
			} else {
				// create label
				label = $("<" + this.settings.errorElement + "/>").attr({
					"for" : this.idOrName(element),
					generated : true
				}).addClass(this.settings.errorClass).css({//增加div样式
					//'position':"absolute",
					"left":"0px",
					"top":"0px",
					//'width':"50%",
					"z-index":"999",
					"display": "none"
				}).html(this.errorLabelInitHtml(message) || "");//html代码为<Table>出错信息代码
				if(!this.labelContainer || this.labelContainer.length == 0){//采用tips显示错误信息
					label.addClass(this.settings.tipClass).click(function(){//点击提示信息使其隐藏
						$(this).hide();
					});
				}
				if (this.settings.wrapper) {
					// make sure the element is visible, even in IE
					// actually showing the wrapped element is handled elsewhere
					label = label.hide().show().wrap("<" + this.settings.wrapper
							+ "/>").parent();
				}
				
				var element1 = this._getLastElement($(element));
				// 如果没有设置labelContainer则要做如下动作：
				if (!this.labelContainer.append(label).length){
					this.settings.errorPlacement
							// 调用errorPlacement方法进行label展现,errorPlacement参数为label对象和当前验证元素对象
							? this.settings.errorPlacement(label, $(element))
							// 在element后面的input、a、img元素最后加入label
							: label.appendTo(this.container);//label.insertAfter(element1);//
					this._placeErrorLabel(element1, label);
					label.show();
				}
				
				//_placeErrorLabel(element1, label);
				//label.show();
			}
			if (!message && this.settings.success) {
				label.text("");
				typeof this.settings.success == "string" ? label
						.addClass(this.settings.success) : this.settings
						.success(label);
			}
			this.toShow = this.toShow.add(label);
		},
		
		//根据error label的for属性寻找element的error label
		errorsFor: function(element) {
			return this.errors().filter("[for='" + this.idOrName(element) + "']");
		},
		
		idOrName: function(element) {
			return this.groups[element.name] || (this.checkable(element) ? element.name : element.id || element.name);
		},
		
		//判断元素是否是radio或checkbox类型
		checkable: function( element ) {
			return /radio|checkbox/i.test(element.type);
		},
		
		//从表单中查找符合条件的name = ‘name’的元素集合
		findByName: function( name ) {
			// select by name and filter by form for performance over form.find("[name=...]")
			//从表单中查找name = ‘name’的元素集合
			var form = this.currentForm;
			return $(document.getElementsByName(name)).map(function(index, element) {
				//该判断返回element或null
				return element.form == form && element.name == name && element  || null;
			});
		},
		
		//1. 返回select元素选中的项的个数
		//2. 返回radio或checkbox元素选中的项的个数
		//3. 返回元素值的长度
		getLength: function(value, element) {
			switch( element.nodeName.toLowerCase() ) {
			case 'select':
				return $("option:selected", element).length;
			case 'input':
				if( this.checkable( element) ){
					return this.findByName(element.name).filter(':checked').length;
				}
			}
			return value.length;
		},
	
		//返回true 或者 false
		depend: function(param, element) {
			return this.dependTypes[typeof param]
				? this.dependTypes[typeof param](param, element)
				: true;
		},
	
		//返回true 或者 false
		dependTypes: {
			"boolean": function(param, element) {
				return param;
			},
			"string": function(param, element) {
				return !!$(param, element.form).length;
			},
			"function": function(param, element) {
				return param(element);
			}
		},
		
		//判断该元素是否可选的，若是可选则不要求必填
		optional: function(element) {
			return !$.validator.methods.required.call(this, $.trim(element.value), element) && "dependency-mismatch";
		},
		
		//
		startRequest: function(element) {
			if (!this.pending[element.name]) {
				this.pendingRequest++;
				this.pending[element.name] = true;
			}
		},
		
		//
		stopRequest: function(element, valid) {
			this.pendingRequest--;
			// sometimes synchronization fails, make sure pendingRequest is never < 0
			if (this.pendingRequest < 0)
				this.pendingRequest = 0;
			delete this.pending[element.name];
			if ( valid && this.pendingRequest == 0 && this.formSubmitted && this.form() ) {
				$(this.currentForm).submit();
			} else if (!valid && this.pendingRequest == 0 && this.formSubmitted) {
				$(this.currentForm).triggerHandler("invalid-form", [this]);
			}
		},
		
		previousValue: function(element) {
			return $.data(element, "previousValue") || $.data(element, "previousValue", previous = {
				old: null,
				valid: true,
				message: this.defaultMessage( element, "remote" )
			});
		}
		
	},
	
	//经典验证策略设置
	classRuleSettings: {
		required: {required: true},
		email: {email: true},
		url: {url: true},
		date: {date: true},
		dateISO: {dateISO: true},
		dateDE: {dateDE: true},
		number: {number: true},
		numberDE: {numberDE: true},
		digits: {digits: true},
		creditcard: {creditcard: true}
	},
	
	//添加验证策略，className为String时取经典验证策略；不为String时添加策略到经典策略中
	addClassRules: function(className, rules) {
		className.constructor == String ?
			this.classRuleSettings[className] = rules :
			$.extend(this.classRuleSettings, className);
	},
	
	//取element中的class属性，并将以' '分隔的验证属性加入到rules中
	classRules: function(element) {
		var rules = {};
		var classes = $(element).attr('class');
		classes && $.each(classes.split(' '), function() {
			if (this in $.validator.classRuleSettings) {
				$.extend(rules, $.validator.classRuleSettings[this]);
			}
		});
		return rules;
	},
	
	//利用class外的属性来设置验证策略，并将其值读到rules中
	attributeRules: function(element) {
		var rules = {};
		var $element = $(element);
		
		for (method in $.validator.methods) {
			var value = $element.attr(method);
			if (value) {
			    if(value === "true"){//如果值为"true"，设置required的值为true
			    	rules[method] = true;
			    }else if(value === "false"){
			        rules[method] = false;
			    }else{
			    	rules[method] = value;
			    }
			}
		}
		
		// maxlength may be returned as -1, 2147483647 (IE) and 524288 (safari) for text inputs
		if (rules.maxlength && /-1|2147483647|524288/.test(rules.maxlength)) {
			delete rules.maxlength;
		}
		
		return rules;
	},
	
	metadataRules: function(element) {
		if (!$.metadata) return {};
		
		var meta = $.data(element.form, 'validator').settings.meta;
		return meta ?
			$(element).metadata()[meta] :
			$(element).metadata();
	},
	
	staticRules: function(element) {
		var rules = {};
		var validator = $.data(element.form, 'validator');
		if (validator.settings.rules) {
			rules = $.validator.normalizeRule(validator.settings.rules[element.name]) || {};
		}
		return rules;
	},
	
	normalizeRules: function(rules, element) {
		// handle dependency check
		$.each(rules, function(prop, val) {
			// ignore rule when param is explicitly false, eg. required:false
			if (val === false) {
				delete rules[prop];
				return;
			}
			if (val.param || val.depends) {
				var keepRule = true;
				switch (typeof val.depends) {
					case "string":
						keepRule = !!$(val.depends, element.form).length;
						break;
					case "function":
						keepRule = val.depends.call(element, element);
						break;
				}
				if (keepRule) {
					rules[prop] = val.param !== undefined ? val.param : true;
				} else {
					delete rules[prop];
				}
			}
		});
		
		// evaluate parameters
		$.each(rules, function(rule, parameter) {
			rules[rule] = $.isFunction(parameter) ? parameter(element) : parameter;
		});
		
		// clean number parameters
		$.each(['minlength', 'maxlength', 'min', 'max'], function() {
			if (rules[this]) {
				rules[this] = Number(rules[this]);
			}
		});
		$.each(['rangelength', 'range'], function() {
			if (rules[this]) {
				rules[this] = [Number(rules[this][0]), Number(rules[this][1])];
			}
		});
		
		if ($.validator.autoCreateRanges) {
			// auto-create ranges
			if (rules.min && rules.max) {
				rules.range = [rules.min, rules.max];
				delete rules.min;
				delete rules.max;
			}
			if (rules.minlength && rules.maxlength) {
				rules.rangelength = [rules.minlength, rules.maxlength];
				delete rules.minlength;
				delete rules.maxlength;
			}
		}
		
		// To support custom messages in metadata ignore rule methods titled "messages"
		if (rules.messages) {
			delete rules.messages
		}
		
		return rules;
	},
	
	// Converts a simple string to a {string: true} rule, e.g., "required" to {required:true}
	normalizeRule: function(data) {
		if( typeof data == "string" ) {
			var transformed = {};
			$.each(data.split(/\s/), function() {
				transformed[this] = true;
			});
			data = transformed;
		}
		return data;
	},
	
	// http://docs.jquery.com/Plugins/Validation/Validator/addMethod
	//添加验证策略实现方法
	//method函数的输入参数为：value、element、param
	addMethod: function(name, method, message) {
		$.validator.methods[name] = method;
		$.validator.messages[name] = message || $.validator.messages[name];
		if (method.length < 3) {
			$.validator.addClassRules(name, $.validator.normalizeRule(name));
		}
	},

	methods: {

		// http://docs.jquery.com/Plugins/Validation/Methods/required
		required: function(value, element, param) {
			// check if dependency is met
			if ( !this.depend(param, element) )
				return "dependency-mismatch";
			switch( element.nodeName.toLowerCase() ) {
			case 'select':
				var options = $("option:selected", element);
				return options.length > 0 && ( element.type == "select-multiple" || ($.browser.msie && !(options[0].attributes['value'].specified) ? options[0].text : options[0].value).length > 0);
			case 'input':
				if ( this.checkable(element) )
					return this.getLength(value, element) > 0;
			default:
				return $.trim(value).length > 0;
			}
		},
		
		// http://docs.jquery.com/Plugins/Validation/Methods/remote
		remote: function(value, element, param) {
			if ( this.optional(element) )
				return "dependency-mismatch";
			
			var previous = this.previousValue(element);
			
			if (!this.settings.messages[element.name] )
				this.settings.messages[element.name] = {};
			this.settings.messages[element.name].remote = typeof previous.message == "function" ? previous.message(value) : previous.message;
			//该条件表达式返回{url:param}或param
			param = typeof param == "string" && {url:param} || param; 
			
			if ( previous.old !== value ) {
				previous.old = value;
				var validator = this;
				this.startRequest(element);
				var data = {};
				data[element.name] = value;
				$.ajax($.extend(true, {
					url: param,
					mode: "abort",
					port: "validate" + element.name,
					dataType: "json",
					data: data,
					success: function(response) {
						var valid = response === true;
						if ( valid ) {
							var submitted = validator.formSubmitted;
							validator.prepareElement(element);
							validator.formSubmitted = submitted;
							validator.successList.push(element);
							validator.showErrors();
						} else {
							var errors = {};
							errors[element.name] = previous.message = response || validator.defaultMessage( element, "remote" );
							validator.showErrors(errors);
						}
						previous.valid = valid;
						validator.stopRequest(element, valid);
					}
				}, param));
				return "pending";
			} else if( this.pending[element.name] ) {
				return "pending";
			}
			return previous.valid;
		},

		// http://docs.jquery.com/Plugins/Validation/Methods/minlength
		minlength: function(value, element, param) {
			return this.optional(element) || this.getLength($.trim(value), element) >= param;
		},
		
		// http://docs.jquery.com/Plugins/Validation/Methods/maxlength
		maxlength: function(value, element, param) {
			return this.optional(element) || this.getLength($.trim(value), element) <= param;
		},
		
		// http://docs.jquery.com/Plugins/Validation/Methods/rangelength
		rangelength: function(value, element, param) {
			var length = this.getLength($.trim(value), element);
			return this.optional(element) || ( length >= param[0] && length <= param[1] );
		},
		
		// http://docs.jquery.com/Plugins/Validation/Methods/min
		min: function( value, element, param ) {
			return this.optional(element) || value >= param;
		},
		
		// http://docs.jquery.com/Plugins/Validation/Methods/max
		max: function( value, element, param ) {
			return this.optional(element) || value <= param;
		},
		
		// http://docs.jquery.com/Plugins/Validation/Methods/range
		range: function( value, element, param ) {
			return this.optional(element) || ( value >= param[0] && value <= param[1] );
		},
		
		// http://docs.jquery.com/Plugins/Validation/Methods/email
		email: function(value, element) {
			// contributed by Scott Gonzalez: http://projects.scottsplayground.com/email_address_validation/
			return this.optional(element) || /^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?$/i.test(value);
		},
	
		// http://docs.jquery.com/Plugins/Validation/Methods/url
		url: function(value, element) {
			// contributed by Scott Gonzalez: http://projects.scottsplayground.com/iri/
			return this.optional(element) || /^(https?|ftp):\/\/(((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:)*@)?(((\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5]))|((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?)(:\d*)?)(\/((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)+(\/(([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)*)*)?)?(\?((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|[\uE000-\uF8FF]|\/|\?)*)?(\#((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|\/|\?)*)?$/i.test(value);
		},
        
		// http://docs.jquery.com/Plugins/Validation/Methods/date
		date: function(value, element) {
			return this.optional(element) || !/Invalid|NaN/.test(new Date(value));
		},
	
		// http://docs.jquery.com/Plugins/Validation/Methods/dateISO
		dateISO: function(value, element) {
			return this.optional(element) || /^\d{4}[\/-]\d{1,2}[\/-]\d{1,2}$/.test(value);
		},
	
		// http://docs.jquery.com/Plugins/Validation/Methods/dateDE
		dateDE: function(value, element) {
			return this.optional(element) || /^\d\d?\.\d\d?\.\d\d\d?\d?$/.test(value);
		},
	
		// http://docs.jquery.com/Plugins/Validation/Methods/number
		number: function(value, element) {
			return this.optional(element) || /^-?(?:\d+|\d{1,3}(?:,\d{3})+)(?:\.\d+)?$/.test(value);
		},
	
		// http://docs.jquery.com/Plugins/Validation/Methods/numberDE
		numberDE: function(value, element) {
			return this.optional(element) || /^-?(?:\d+|\d{1,3}(?:\.\d{3})+)(?:,\d+)?$/.test(value);
		},
		
		// http://docs.jquery.com/Plugins/Validation/Methods/digits
		digits: function(value, element) {
			return this.optional(element) || /^\d+$/.test(value);
		},
		
		// http://docs.jquery.com/Plugins/Validation/Methods/creditcard
		// based on http://en.wikipedia.org/wiki/Luhn
		creditcard: function(value, element) {
			if ( this.optional(element) )
				return "dependency-mismatch";
			// accept only digits and dashes
			if (/[^0-9-]+/.test(value))
				return false;
			var nCheck = 0,
				nDigit = 0,
				bEven = false;

			value = value.replace(/\D/g, "");

			for (n = value.length - 1; n >= 0; n--) {
				var cDigit = value.charAt(n);
				var nDigit = parseInt(cDigit, 10);
				if (bEven) {
					if ((nDigit *= 2) > 9)
						nDigit -= 9;
				}
				nCheck += nDigit;
				bEven = !bEven;
			}

			return (nCheck % 10) == 0;
		},
		
		// http://docs.jquery.com/Plugins/Validation/Methods/accept
		accept: function(value, element, param) {
			param = typeof param == "string" ? param.replace(/,/g, '|') : "png|jpe?g|gif";
			return this.optional(element) || value.match(new RegExp(".(" + param + ")$", "i")); 
		},
		
		// http://docs.jquery.com/Plugins/Validation/Methods/equalTo
		equalTo: function(value, element, param) {
			return value == $(param).val();
		}
		
	}
	
});

// deprecated, use $.validator.format instead
$.format = $.validator.format;

})(jQuery);

// ajax mode: abort
// usage: $.ajax({ mode: "abort"[, port: "uniqueport"]});
// if mode:"abort" is used, the previous request on that port (port can be undefined) is aborted via XMLHttpRequest.abort() 
;(function($) {
	var ajax = $.ajax;
	var pendingRequests = {};
	$.ajax = function(settings) {
		// create settings for compatibility with ajaxSetup
		settings = $.extend(settings, $.extend({}, $.ajaxSettings, settings));
		var port = settings.port;
		if (settings.mode == "abort") {
			if ( pendingRequests[port] ) {
				pendingRequests[port].abort();
			}
			return (pendingRequests[port] = ajax.apply(this, arguments));
		}
		return ajax.apply(this, arguments);
	};
})(jQuery);

// provides cross-browser focusin and focusout events
// IE has native support, in other browsers, use event caputuring (neither bubbles)

// provides delegate(type: String, delegate: Selector, handler: Callback) plugin for easier event delegation
// handler is only called when $(event.target).is(delegate), in the scope of the jquery-object for event.target 

// provides triggerEvent(type: String, target: Element) to trigger delegated events
;(function($) {
	$.each({
		focus: 'focusin',
		blur: 'focusout'
	}, function( original, fix ){
		$.event.special[fix] = {
			setup:function() {
				if ( $.browser.msie ) return false;
				this.addEventListener( original, $.event.special[fix].handler, true );
			},
			teardown:function() {
				if ( $.browser.msie ) return false;
				this.removeEventListener( original,
				$.event.special[fix].handler, true );
			},
			handler: function(e) {
				arguments[0] = $.event.fix(e);
				arguments[0].type = fix;
				return $.event.handle.apply(this, arguments);
			}
		};
	});
	$.extend($.fn, {
		delegate: function(type, delegate, handler) {
			return this.bind(type, function(event) {
				var target = $(event.target);
				if (target.is(delegate)) {
					if(!$.browser.msie && event.type == "focusout"){//如果浏览器不是IE类型的，则需要在触发form的focusout事件前将触发事件元素的focusout事件触发，兼容数值和货币输入框验证
						if($.data(target[0], "events") && $.data(target[0], "events")["focusout"]){//该元素定义了focusout事件
							target.triggerHandler("focusout");//触发focusout事件
							event.stopPropagation();//阻止事件冒泡
						}
					}
					return handler.apply(target, arguments);
				}
			});
		},
		triggerEvent: function(type, target) {
			return this.triggerHandler(type, [$.event.fix({ type: type, target: target })]);
		}
	})
})(jQuery);

// 用户自定义函数验证，主要用于ajax验证
$.validator.addMethod("customFunction", function ajaxFunction(value, element,
		param) {
	//var returnValue = eval(param + "()");
	var returnValue = eval(param).call(element);
	if (typeof returnValue == "string") {
		$.validator.messages["customFunction"] = "\"" + returnValue + "\"";
		return false;
	} else if (typeof returnValue == "boolean") {
		return returnValue;
	} else {
		return false;
	}
}, "errors_customFunction");

// 字符串类型验证
$.validator.addMethod("string", function(value, element) {
	return this.optional(element) || /^[\u0391-\uFFE5\w]+$/.test(value);
}, "errors_string");

// 验证字符串的长度范围，中文字算两个字节
$.validator.addMethod("byteRangeLength", function(value, element, param) {
	var length = value.length;
	var params = param.split(",");
	for (var i = 0; i < value.length; i++) {
		if (value.charCodeAt(i) > 127) {
			length++;
		}
	}
	return this.optional(element)
			|| (length >= params[0] && length <= params[1]);
}, "errors_byteRangeLength");

// 验证字符串的最小长度，中文字算两个字节
$.validator.addMethod("byteMinLength", function(value, element, param) {
	var length = value.length;
	var params = param.split(",");
	for (var i = 0; i < value.length; i++) {
		if (value.charCodeAt(i) > 127) {
			length++;
		}
	}
	return this.optional(element) || (length >= params);
}, "errors_byteMinLength");

// 验证字符串的最大长度，中文字算两个字节
$.validator.addMethod("byteMaxLength", function(value, element, param) {
	var length = value.length;
	var params = param.split(",");
	for (var i = 0; i < value.length; i++) {
		if (value.charCodeAt(i) > 127) {
			length++;
		}
	}
	return this.optional(element) || (length <= params);
}, "errors_byteMaxLength");

// 验证用户自定义正则表达式
$.validator.addMethod("customReg", function(value, element, param) {
	if (param == "") {
		return true;
	}
	var reg = new RegExp(param);
	return reg.test(value);
}, "errors_customReg");

// 验证IP信息格式是否合法
$.validator.addMethod("ip", function(value, element) {
	return this.optional(element)
			|| (/^(\d+)\.(\d+)\.(\d+)\.(\d+)$/.test(value) && (RegExp.$1 < 256
					&& RegExp.$2 < 256 && RegExp.$3 < 256 && RegExp.$4 < 256));
}, "errors_ip");

// 手机号码验证
$.validator.addMethod("mobile", function(value, element) {
	var length = value.length;
	var mobile = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/;
	return this.optional(element) || (length == 11 && mobile.test(value));
}, "errors_mobile");

// 电话号码验证
$.validator.addMethod("telephone", function(value, element) {
	var tel = /^\d{3,4}-?\d{7,9}$/; // 电话号码格式010-12345678
	return this.optional(element) || (tel.test(value));
}, "errors_telephone");

// 联系电话(手机/电话皆可)验证
$.validator.addMethod("phone", function(value, element) {
	var length = value.length;
	var mobile = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/;
	var tel = /^\d{3,4}-?\d{7,9}$/;
	return this.optional(element)
			|| (tel.test(value) || mobile.test(value));
}, "errors_phone");

// 邮政编码验证
$.validator.addMethod("zipCode", function(value, element) {
	var tel = /^[0-9]{6}$/;
	return this.optional(element) || (tel.test(value));
}, "errors_zipCode");


/** 横向tab页组件 * */
(function(){
	var HTabPanel = function (config) {
		var tabHeadRenderer = $('#' + config.tabHeadRenderer),
			tabBodyRenderer	= $('#' + config.tabBodyRenderer),
			tabHeadTemplate = $(config.tabHeadTemplate),// 高亮显示的
			tabHeadWidth	= config.tabHeadWidth, // 标签头 宽度
			// tabWidth = config.tabWidth, // 整个标签页宽度
			autoAdaptFunc	= config.autoAdaptFunc;// 窗口变化时的tab容器宽度获取自定义函数
			that = this;
		
		this.tabHeadTemplate = tabHeadTemplate;
		this.tabHeadWidth 	 = tabHeadWidth;
		this.autoAdaptFunc   = autoAdaptFunc;// 窗口变化时的tab容器宽度获取自定义函数
		// this.tabWidth = tabWidth;
		this.showMenu 		 = config.showMenu;
		
		this.tabHeadRenderer  = tabHeadRenderer;
		this.tabBodyRenderer  = tabBodyRenderer;
		this.tabHeadContainer = $('<div class="tabHeadContainer"></div>');
		this.tabHeadSlider 	  = $('<div class="tabHeadSlider"></div>');
		this.tabHeadWraper 	  = $('<ul class="tabHeadWraper"></ul>');
		this.tabHeaddown 	  = $('<div class="tabHeaddown"></div>').hide();
		// this.tabBodyContainer = $('<div class="tabBodyContainer"
		// style="width:100%;height:100%;"></div>');
		this.init();
		
		this.tabHeaddown.click(function(e) {
			var tabHeadSlider = that.tabHeadSlider, hidetabs = tabHeadSlider.find('.tabHead:hidden'), hidetabssize = hidetabs.size(), hidetab, hidetabtext, hidetabid, i, menu = [],mm, xx;
			var menuclick = function(e) {
				var id = this.data.alias.slice(10),thetab = $('#'+id),headtabs = that.tabHeadWraper.find('.tabHead'), curActiveTab = that.tabHeadWraper.find('.menubg'), index;
				if(curActiveTab.css('display') === 'none') {
					// alert(that.tabHeadWraper.find('li:visible').size());
					that.tabHeadWraper.find('li:visible').first().hide();
					thetab.show();
					thetab.find('.tabHeadText').click();
				}
				curActiveTab.hide();
				thetab.show();
				thetab.find('.tabHeadText').click();
			};
			
			if(hidetabssize > 0) {
				for(i = 0; i < hidetabssize; i += 1) {
					hidetab = $(hidetabs[i]);
					hidetabtext = hidetab.find('.tabHeadText').html();
					hidetabid = hidetab.attr('id');
					
					menu[i] = {text:hidetabtext, alias:'menuoption'+hidetabid, icon: imgPath+"/contextmenu/page_go.png", action:menuclick};
				}
				
				mm = {alias:'aaaaa' + new Date(), items:menu};
				xx = $.fn.contextmenu(mm);
				xx.displayMenuByPos(e, this, {top:e.pageY, left:e.pageX});
			}
			
		});
	};

	HTabPanel.prototype = {
		init:function() {
			var that = this;
			
			that.hasResize = false;// 判断是否已经响应了窗口resize事件，IE6 sp3版本之前使用
			
			this.tabHeadRenderer.append(this.tabHeadContainer);
			
			this.tabHeadContainer.append(this.tabHeadSlider);
			this.tabHeadSlider.append(this.tabHeadWraper);
			this.tabHeadContainer.append(this.tabHeaddown);
			
			this.tabWidth 	 = this._autoAdaptWidth();
			
			this.tabHeadContainer.width(this.tabWidth);
			this.tabHeadSlider.width(this.tabWidth - this.tabHeaddown.outerWidth());
			// this.tabHeadSlider.width("95%");
			
			// this.tabHeaddown.css('float', 'right');
			// this.tabBodyRenderer.append(this.tabBodyContainer);
			$(window).bind('resize', function() {
				that.tabWidth = that._autoAdaptWidth();
				
				if($.browser.msie && ($.browser.version == "6.0") && !$.support.style){// 处理IE6浏览器
					if(!that.hasResize){
						that.hasResize = true;
			            that.tabHeadContainer.width(that.tabWidth);
						that.tabHeadSlider.width(that.tabWidth - that.tabHeaddown.outerWidth());
						that.showhide();
					}
					setTimeout(function(){
						that.hasResize = false;
					}, 1000);
				}else{// 其它浏览器
					that.tabHeadContainer.width(that.tabWidth);
					that.tabHeadSlider.width(that.tabWidth - that.tabHeaddown.outerWidth());
					that.showhide();
				}				
				
				if(that.tabHeadWraper.find("li.menubg").css('display') === 'none') {
					that.tabHeadWraper.find('li').first().hide();
					that.tabHeadWraper.find("li.menubg").show();
					that.tabHeadWraper.find("li.menubg").find('.tabHeadText').click();
				}
			});
		},
		
		/**
		 * 获取tab容器宽度，若定义了自定义函数则通过自定义函数获取，否则直接获取tab容器宽度
		 */
		_autoAdaptWidth: function(){
            var funcBtnWidth = 60;
			if(this.autoAdaptFunc && typeof(this.autoAdaptFunc) == "function"){
				return (parseInt(this.autoAdaptFunc.call(this),10)-funcBtnWidth);
			}
			return parseInt(this.tabHeadRenderer.outerWidth(),10)-funcBtnWidth;
		},
		
		/**
		 * 添加tab页
		 */
		add : function(config) {
			var that	 = this, 
				id		 = config.id, // tab页 id，
										// tabHead的id和此id相同，tabBody的id为_id
				text	 = config.text, // tab页标题
				closeAble= config.closeAble, // 是否可以关闭
				url		 = config.url, // tabHead接受URL，
				content	 = config.content,// tabHead接受content，
				tabHead		    = this.tabHeadTemplate.clone().attr('id', id),
				tabHeadText		= tabHead.find('.tabHeadText').append(text),
				tabHeadClose	= tabHead.find('.tabHeadClose'),
				tabHeadWidth	= this.tabHeadWidth,
				tabWidth        = this.tabWidth,
				tabHeadWraper	= this.tabHeadWraper,
				// tabBodyContainer= this.tabBodyContainer,
				tabBodyRenderer = this.tabBodyRenderer,
				tabBodyWraper	= $('<div class="tabBodyWraper" id="_'+id+'" style="width:100%;height:100%;"></div>'), 
				tabHeaddown		= this.tabHeaddown,
				lazy			= config.lazy != null ? config.lazy : false,
				highlight       = config.highlight != null ? config.highlight : true,
				cb = config.callback || function(){},
				cbargs = config.callbackargs,
				close			= function() {
					tabHeadClose.click();
				},
				closeOthers		= function() {
					var tabheads = tabHeadWraper.find('.tabHead').not(tabHead),i;
					for(i = 0; i < tabheads.size(); i += 1) {
						$(tabheads[i]).find('.tabHeadClose').click();
					}
				},
				closeAll		= function() {
					 tabHeadWraper.find('.tabHead').find('.tabHeadClose').click();
				},
				menuOption 		= {alias:"menu"+id, items: [{text:"关闭当前",icon: imgPath+"/contextmenu/page_delete.png", alias:"menuOptiona", action:close},
			         		                        	  {text:"关闭其他",icon:imgPath+"/contextmenu/page_delete.png",alias:"menuOptionb",action:closeOthers},
			         		                              {text:"关闭全部",icon:imgPath+"/contextmenu/page_delete.png",alias:"menuOptionc",action:closeAll}]},
			    menu 			= $.fn.contextmenu(menuOption);
				
			// 添加tabHead
			this.reset();
//			tabHeadWraper.prepend(tabHead);//在前面新开页面
            tabHeadWraper.append(tabHead);//在后面新开页面
			tabHeadText.width(tabHeadWidth);
			
			// tabBody 内容添加
			if((!lazy) && url) {
				tabBodyWraper.append('<iframe src="'+url+'" width="100%" height="100%" frameborder="0"></iframe>');
				tabBodyRenderer.append(tabBodyWraper);
			}
			else if(content != null && content != undefined && content != 'null') {
				tabBodyWraper.append(content);
				tabBodyRenderer.append(tabBodyWraper);
			}
			// tabBodyContainer.append(tabBodyWraper);
			
			// 单击事件
			tabHead.click(function() {
				if(url && lazy) {
					tabBodyWraper.append('<iframe src="'+url+'" width="100%" height="100%" frameborder="0"></iframe>');
					tabBodyRenderer.append(tabBodyWraper);
					lazy=false;
				}
				that.reset();
				that.active(tabHead);
				cb.call(null, that.tabHeadWraper.find('.tabHead').index(tabHead), text, '_'+id, cbargs);
				
				//点击table页   刷新页面  此方法在 nav_right.jsp
				activeRefresh();
			});
			
			if(!highlight) {
				// alert('#' + tabHead.attr('id'));
				tabHead.removeClass('menubg').addClass('menubg2');
				$('#_' + tabHead.attr('id')).hide();
			}
			
			// 显示菜单
			if(this.showMenu) {
				tabHeadText.bind('contextmenu', function(e) {
					menu.displayMenuByPos(e, this, {top:e.pageY, left:e.pageX});
					e.preventDefault();
				});
			}
			
			// 关闭按钮
			if(closeAble) {
				tabHeadClose.show();
				tabHeadClose.click(function() { // 关闭
					var heads = tabHeadWraper.find('.tabHead'),index = heads.index(tabHead),count = heads.size(),vtabs,w = 0, i;
					
					tabHead.remove();
					// tabBodyWraper.remove();
					
					var id = tabHead.attr('id');
					$('#_' + id).remove();
					
					if(that.isActive(tabHead)) {
						if(index + 1 === count) {
							that.active($(heads[index - 1]));
						}
						else {
							that.active($(heads[index + 1]));
						}
					}
					
					if(tabHeadWraper.find('.menubg:hidden').size() > 0) {
						tabHeadWraper.find('.menubg').show();
					}
					else {
						tabHeadWraper.find('.tabHead:hidden').first().show();
					}
					
					if(tabHeadWraper.find('.tabHead:hidden').size() < 1) {
						that.tabHeaddown.hide();
					}
				});
			}
			
			else {
				tabHeadClose.hide();
			}
			
			
// //tabBody 内容添加
// if(url) {
// tabBodyWraper.append('<iframe src="'+url+'" width="100%" height="100%"
// frameborder="0"></iframe>');
// tabBodyRenderer.append(tabBodyWraper);
// }
// else if(content != null && content != undefined && content != 'null') {
// tabBodyWraper.append(content);
// tabBodyRenderer.append(tabBodyWraper);
// }
// //tabBodyContainer.append(tabBodyWraper);
			
			
			this.showhide();
			if(highlight) {
				// this.active(tabHead);
				tabHead.click();
			}
			// this.active(tabHeadWraper.find('.tabHead').first());
			// cb.call(null, this.tabHeadWraper.find('.tabHead').index(tabHead),
			// text, '_'+id, cbargs);
		},
		
		showhide : function() {
			var tabHeadWraper = this.tabHeadWraper, tabheads = tabHeadWraper.find('.tabHead'), i, w = 0, tabWidth = this.tabWidth - that.tabHeaddown.outerWidth(), tabHeaddown = this.tabHeaddown;
			// alert(this.tabHeadSlider.width());
			// tabWidth = this.tabHeadWidth;
			for(i = 0; i < tabheads.size(); i+=1) {
				w = w + this.getHSpace($(tabheads[i]));
				if(w > tabWidth) {
					$(tabheads[i]).hide();
					w = w - this.getHSpace($(tabheads[i]));
					tabHeaddown.show();
				}
				else {
					$(tabheads[i]).show();
					tabHeaddown.hide();
				}
			}
		},
		
		/**
		 * 将所有tab页 变成非高亮状态
		 */
		reset : function() {
			var tabHeadWraper = this.tabHeadWraper,tabBodyRenderer = this.tabBodyRenderer;
			tabHeadWraper.find('.menubg').removeClass('menubg').addClass('menubg2');
			tabBodyRenderer.children().hide();
		},
		
		active : function(tabhead) {
			var id = tabhead.attr('id'), tabBodyWraper = $('#_' + id);
			tabhead.removeClass('menubg2').addClass('menubg'); // 换高亮样式
			tabBodyWraper.show(); // 显示对应的内容
		},
		
		getHSpace : function(tabHead) {
			return tabHead.outerWidth(true);
		},
		
		/**
		 * 判断 传入id 的tab页是否存在
		 */
		isExist : function(id) {
			if(this.tabHeadWraper.find("#"+id).size() === 1) {
				return true;
			}
			else {
				return false;
			}
		},
		
		/**
		 * 高亮传入id的tab页
		 */
		activeId : function(id) {
			this.reset();
			this.tabHeadWraper.find("#"+id).find('.tabHeadText').click();
		},
		
		isActive : function(tabHead) {
			if(tabHead.hasClass('menubg')) {
				return true;
			}
			else {
				return false;
			}
		}
		
	};
	window.HTabPanel = HTabPanel;
	
})();;

/** 纵向tab页组件 * */
(function(){
	var VTabPanel = function (config) {
		
		var tabHeadRenderer = $('#' + config.tabHeadRenderer),
			tabBodyRenderer	= $('#' + config.tabBodyRenderer),
			tabHeadTemplate = $('<li class="tabHead"><div class="container"><span class="tabHeadLeft"></span><span class="tabHeadLeft_left"></span><span class="tabHeadText"></span><span class="tabHeadClose"></span><span class="tabHeadright"></span></div></li>'),
			tabHeadHeight	= config.tabHeadHeight,
			// tabHeight = config.tabHeight,
			showMenu		= config.showMenu,
			autoAdaptFunc	= config.autoAdaptFunc;// 窗口变化时的tab容器高度获取自定义函数
			that			= this;
		
		this.tabHeadRenderer = tabHeadRenderer;
		this.tabBodyRenderer = tabBodyRenderer;
		this.tabHeadTemplate = tabHeadTemplate;
		this.tabHeadHeight 	 = tabHeadHeight;
		this.autoAdaptFunc   = autoAdaptFunc;// 窗口变化时的tab容器高度获取自定义函数
		// this.tabHeight = tabHeight;
		this.showMenu 		 = showMenu;
		
		this.tabHeadContainer = $('<div class="tabHeadContainer"></div>');
		this.tabHeadSlider 	  = $('<div class="tabHeadSlider"></div>');
		this.tabHeadWraper 	  = $('<ul class="tabHeadWraper"></ul>');
		this.tabHeaddown 	  = $('<div class="tabHeaddown"></div>').hide();
		// this.tabBodyContainer = $('<div class="tabBodyContainer"
		// style="width:100%;height:100%;"></div>');
		this.tabBodyWraperTemplate = $('<div class="tabBodyWraper" style="width:100%;height:100%;"></div>');
		
		this.init();
		
		// 弹出菜单
		this.tabHeaddown.click(function(e) {
			var tabHeadSlider = that.tabHeadSlider, hidetabs = tabHeadSlider.find('.tabHead:hidden'), hidetabssize = hidetabs.size(), 
				hidetab, hidetabtext, hidetabid, i, menu = [], mm, xx;
			
			var menuclick = function(e) {
				var id = this.data.alias.slice(10),thetab = $('#'+id),headtabs = that.tabHeadWraper.find('.tabHead'), curActiveTab = that.tabHeadWraper.find('.highLight').parent('.tabHead'), index;
				if(curActiveTab.size() != 0) {
				curActiveTab.hide();
				}
				else {
					headtabs.first().hide();
				}
				
				thetab.show();
				thetab.click();
			};
			
			if(hidetabssize > 0) {
// menuOption = {alias:"menu"+id, items: [{text:"关闭当前",icon:
// "http://jscs.cloudapp.net/images/icons/ico1.gif", alias:"menuOptiona",
// action:close},
// {text:"关闭其他",icon:"http://jscs.cloudapp.net/images/icons/ico2.gif",alias:"menuOptionb",action:closeOthers},
// {text:"关闭全部",icon:"http://jscs.cloudapp.net/images/icons/ico3.gif",alias:"menuOptionc",action:closeAll}]},
				for(i = 0; i < hidetabssize; i += 1) {
					hidetab = $(hidetabs[i]);
					hidetabtext = hidetab.find('.tabHeadText').html();
					hidetabid = hidetab.attr('id');
					
					menu[i] = {text:hidetabtext, alias:'menuoption'+hidetabid, icon: imgPath+"/contextmenu/view.png", action:menuclick};
				}
				
				mm = {alias:'aaaaa' + new Date(), items:menu};
				xx = $.fn.contextmenu(mm);
				xx.displayMenuByPos(e, this, {top:e.pageY, left:e.pageX});
			}
			
		});
	};

	VTabPanel.prototype = {
			
		init : function() {
			var that = this;
			that.hasResize = false;// 判断是否已经响应了窗口resize事件，IE6 sp3版本之前使用
			this.tabHeadSlider.append(this.tabHeadWraper);
			this.tabHeadContainer.append(this.tabHeadSlider);
			this.tabHeadContainer.append(this.tabHeaddown);
			this.tabHeadContainer.appendTo(this.tabHeadRenderer);
			// this.tabHeadSlider.height(this.tabHeight);
			// this.tabBodyContainer.appendTo(this.tabBodyRenderer);
			
			this.tabHeight 	 = this._autoAdaptHeight();
			
			// this.tabHeadRenderer
			// that.tabHeadSlider.height($(window).height() - 20);
			that.tabHeadContainer.height(this.tabHeight);
			that.tabHeadSlider.height(this.tabHeight - this.tabHeaddown.outerHeight());
			
			$(window).bind('resize', function() {
				that.tabHeight 	 = that._autoAdaptHeight();
				if($.browser.msie && ($.browser.version == "6.0") && !$.support.style){// 处理IE6浏览器
					if(!that.hasResize){
						that.hasResize = true;
			            that.tabHeadContainer.height(that.tabHeight);
						that.tabHeadSlider.height(that.tabHeight - that.tabHeaddown.outerHeight());
						that.setHidden();
					}
					setTimeout(function(){
						that.hasResize = false;
					}, 1000);
				}else{// 其它浏览器
					that.tabHeadContainer.height(that.tabHeight);
					that.tabHeadSlider.height(that.tabHeight - that.tabHeaddown.outerHeight());
					that.setHidden();
				}				
			});
		},
		
		/**
		 * 获取tab容器高度，若定义了自定义函数则通过自定义函数获取，否则直接获取tab容器高度
		 */
		_autoAdaptHeight: function(){
			if(this.autoAdaptFunc && typeof(this.autoAdaptFunc) == "function"){
				return this.autoAdaptFunc.call(this);
			}
			return this.tabHeadRenderer.outerHeight();
		},
		
		initTabHeadTemplate : function(id, tabHeadText, tabHeadClose) {
			var tabHead = this.tabHeadTemplate.clone();
			tabHead.attr('id', id);
			if(tabHeadClose) {
				tabHead.find('.tabHeadClose').show();
			}
			else {
				tabHead.find('.tabHeadClose').hide();
			}
			tabHead.find('.tabHeadText').append(tabHeadText);			
			return tabHead;
		},
		
		/**
		 * 重置
		 */
		reset : function() {
			var tabHeads, i;
			
			// 重置所有
			this.tabHeadWraper.find('.tabHeadCss').removeClass('tabHeadCss');
			this.tabHeadWraper.find('.tabMidCss').removeClass('tabMidCss');
			this.tabHeadWraper.find('.tabTailCss').removeClass('tabTailCss');
			this.tabHeadWraper.find('.nhighLight').removeClass('nhighLight');
			this.tabHeadWraper.find('.highLight').removeClass('highLight');
			this.tabHeadWraper.find('.downHighLight').removeClass('downHighLight');
			this.tabHeadWraper.find('.upHighLight').removeClass('upHighLight');
			
			tabHeads = this.tabHeadWraper.find('li.tabHead:visible');// 非隐藏的
			// 对非隐藏的tab，添加些默认的class
			for(i = 0; i < tabHeads.size(); i += 1) {
				if(i === 0) {
					$(tabHeads[i]).addClass('tabHeadCss'); // 优先加tabHeadCss
					$(tabHeads[i]).find('div.container').addClass('nhighLight');
				}
				else if(i === tabHeads.size() - 1) {
					$(tabHeads[i]).addClass('tabTailCss');
					$(tabHeads[i]).find('.container').addClass('nhighLight');
				}
				else {
					$(tabHeads[i]).addClass('tabMidCss');
					$(tabHeads[i]).find('.container').addClass('nhighLight');
				}
			}
			// 隐藏所有tabBody
			this.tabBodyRenderer.children().hide();
		},
		
		/**
		 * 高亮tab ，用于单击高亮显示
		 */
		active : function(tabHead) {
			var next, prev, tabHeads = this.tabHeadWraper.find('li.tabHead:visible'), index = tabHeads.index(tabHead);
			
			// 重置
			this.reset();
			
			// 如果只有一个
			if(tabHeads.size() === 1) {
				tabHead.addClass('tabTailCss');
			    tabHead.find('div.nhighLight').removeClass('nhighLight').addClass('highLight');
			}else{
				if(index === 0) {
					tabHead.find('div.nhighLight').removeClass('nhighLight').addClass('highLight');
					$(tabHeads[index + 1]).find('.nhighLight').addClass('upHighLight');
					
				}
				else if(index === tabHeads.size() - 1) {
					tabHead.find('div.nhighLight').removeClass('nhighLight').addClass('highLight');
					$(tabHeads[index - 1]).find('.nhighLight').addClass('downHighLight');
				}
				
				else {
					tabHead.find('.nhighLight').removeClass('nhighLight').addClass('highLight');
					// prev = tabHead.prev();
					// next = tabHead.next();
					$(tabHeads[index + 1]).find('.nhighLight').addClass('upHighLight');
					$(tabHeads[index - 1]).find('.nhighLight').addClass('downHighLight');
				}
			}
			// 显示 tabBody
			$('#_'+tabHead.attr('id')).show();
		},
		
		/**
		 * 
		 */
		getIndex : function(tabHead) {
			return this.tabHeadWraper.find('.tabHead').index(tabHead);
		},
		
		/**
		 * 高亮
		 */
		isActive : function(tabHead) {
			if(tabHead.find('.highLight').size() === 0) {
				return false;
			}
			else {
				return true;
			}
		},
		
		/**
		 * 移除
		 */
		remove : function(tabHead) {
			var id = tabHead.attr('id');
			$('#_' + id).remove();
			tabHead.remove();
		},
		
		/**
		 * 
		 * @return
		 */
		setHidden : function() {

			var tabHeads = this.tabHeadWraper.find('.tabHead'), i, h = 0;
			var htab = tabHeads.find('div.highLight').parent();
			
			// !!!!!!!!!!!!!!!
			// var height = $(window).height() - 20;
			var height = this.tabHeight - this.tabHeaddown.outerHeight();
			
			for(i = 0; i < tabHeads.size(); i+=1) {
				h = h + $(tabHeads[i]).outerHeight(true);
				if(h > height) {
					h = h - $(tabHeads[i]).outerHeight(true);
					$(tabHeads[i]).hide();
					this.tabHeaddown.show();
					this.active(htab);
				}
				else {
					$(tabHeads[i]).show();
					this.tabHeaddown.hide();
					this.active(htab);
				}
			}
			if(htab.is(':hidden')) {
				this.tabHeadWraper.find('.tabHead:visible').first().hide();
				htab.show();
				this.active(htab);
			}
		},
		
		/**
		 * 
		 */
		isExist : function(id) {
			if(this.tabHeadWraper.find('#' + id).size()) {
				return true;
			}
			else {
				return false;
			}
		},
		
		/**
		 * 
		 */
		activeId : function(id) {
			// this.reset();
			// this.active(this.tabHeadWraper.find('#' + id));
			this.tabHeadWraper.find('#' + id).click();
		},
		
		/**
		 * 添加tab
		 */
		add : function(config) {
			var id = config.id, 
				tabHeadText = config.text, 
				close = config.closeAble, 
				url = config.url, 
				content = config.content,
				that = this, 
				cb = config.callback || function(){}, 
				cbargs = config.callbackargs,
				tabHead = this.initTabHeadTemplate(id, tabHeadText, close), // tabhead
				tabBodyWraper = this.tabBodyWraperTemplate.clone(),
				lazy = config.lazy,
				highlight = config.highlight;
			
			// tabhead
			this.tabHeadWraper.prepend(tabHead);
			tabHead.height(this.tabHeadHeight);
			tabHead.find('div.container').height(this.tabHeadHeight);
			
			// tabbody
			tabBodyWraper.attr('id', '_'+id);
			if((!lazy) && url) {
			// if(url && lazy) {
				tabBodyWraper.append('<iframe width="100%" frameborder="0" height="100%" src="'+url+'"></iframe>');
				this.tabBodyRenderer.append(tabBodyWraper);
				// lazy = false;
			}
			else if(content != undefined && content != null && content != 'null') {
				tabBodyWraper.append(content);
				this.tabBodyRenderer.append(tabBodyWraper);
			}
			
			this.setHidden();
			// this.reset();
			
			
			
			if(this.tabHeadWraper.find('.tabHead:hidden').size() !== 0) {
				that.tabHeaddown.show();
			}
			
			// 弹出菜单初始化
			var close = function() {
				tabHead.find('.tabHeadClose').click();
			},
			closeOthers	= function() {
				var tabheads = that.tabHeadWraper.find('.tabHead').not(tabHead),i;
				for(i = 0; i < tabheads.size(); i += 1) {
					$(tabheads[i]).find('.tabHeadClose').click();
				}
			},
			closeAll = function() {
				 that.tabHeadWraper.find('.tabHead').find('.tabHeadClose').click();
			},
			menuOption = {alias:"menu"+id, items: [{text:"关闭当前",icon: "http://jscs.cloudapp.net/images/icons/ico1.gif", alias:"menuOptiona", action:close},
		         		                           {text:"关闭其他",icon:"http://jscs.cloudapp.net/images/icons/ico2.gif",alias:"menuOptionb",action:closeOthers},
		         		                           {text:"关闭全部",icon:"http://jscs.cloudapp.net/images/icons/ico3.gif",alias:"menuOptionc",action:closeAll}
		         		                           ]},
		    menu = $.fn.contextmenu(menuOption);
			
			if(this.showMenu) {
				that.tabHeadWraper.find('.tabHead').bind('contextmenu', function(e) {
					menu.displayMenuByPos(e, this, {top:e.pageY, left:e.pageX});
					e.stopImmediatePropagation();
					e.preventDefault();
				});
			}
			
			// tabHead单击事件
			tabHead.click(function(e) {
				if(url && lazy) {
					tabBodyWraper.append('<iframe width="100%" frameborder="0" height="100%" src="'+url+'"></iframe>');
					that.tabBodyRenderer.append(tabBodyWraper);
					lazy = false;
				}
				that.reset();
				that.active(tabHead);
				cb.call(null, that.getIndex(tabHead), tabHeadText, '_'+id, cbargs);
			});
			
			// tabHead 关闭事件
			tabHead.find('.tabHeadClose').click(function(e) {
				var prev = tabHead.prev(), next = tabHead.next(), curActive = that.tabHeadWraper.find('.highLight').parent('.tabHead');
				if(that.isActive(tabHead)) {
					if(tabHead.hasClass('tabTailCss')) {
						that.remove(tabHead);
						var aa = that.tabHeadWraper.find('.tabHead:hidden').first();
						if(aa.size()) {
							aa.show();
							that.reset();
							that.active(aa);
						} 
						else {
							that.reset();
							that.active(prev);
						}
					}
					else {
						that.remove(tabHead);
						that.tabHeadWraper.find('.tabHead:hidden').first().show();
						that.reset();
						that.active(next);
					}
				}
				else {
					that.remove(tabHead);
					that.tabHeadWraper.find('.tabHead:hidden').first().show();
					that.reset();
					that.active(curActive);
				}
				
				if(that.tabHeadWraper.find('.tabHead:hidden').size() === 0) {
					that.tabHeaddown.hide();
				}
				
				e.stopImmediatePropagation();
			});
			
			var tabHeads = this.tabHeadWraper.find('li.tabHead:visible');
			if(tabHeads.size() === 1){// 若选项卡只有一个，则为该选项卡添加tabTailCss样式
				tabHead.addClass("tabTailCss");
			}
			
			if(highlight) {
				// this.active(tabHead);
				tabHead.click();
			}
			// cb.call(null, this.getIndex(tabHead), tabHeadText, '_'+id,
			// cbargs);
		}
	};
	
	window.VTabPanel = VTabPanel;
	
})();;

/** 日期时间组件 **/
(function() {
	$.datetime = function(setting) {
		 var ps = $.extend({
                //content holder(Object || css Selector)
                elId:"",
                format:"",
                readOnly:false,
                //whether the slider can be dragged
                val: null   
            }, setting);
		
		var el = $('#'+ps.elId);
		el.attr('tagFlag', 'dateTime');
		el.focus(function() {
			var picking = function(dp) {
				var newVal = dp.cal.getNewDateStr(), oldVal = dp.cal.getDateStr();
				el.val(newVal);
				//验证
				if(ps.val) {
					ps.val.element($('#' + ps.elId));
				}
				//
			};
			WdatePicker({onpicking:picking, el:ps.elId, readOnly:ps.readOnly, dateFmt:ps.format,errDealMode:1});
		});
	};
})();