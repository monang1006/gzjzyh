/*
 * jQuery validation plug-in 1.5.5
 *
 * http://bassistance.de/jquery-plugins/jquery-plugin-validation/
 * http://docs.jquery.com/Plugins/Validation
 *
 * Copyright (c) 2006 - 2008 Jörn Zaefferer
 *
 * $Id: jquery.validate.js 6403 2009-06-17 14:27:16Z joern.zaefferer $
 *
 * Dual licensed under the MIT and GPL licenses:
 *   http://www.opensource.org/licenses/mit-license.php
 *   http://www.gnu.org/licenses/gpl.html
 */

(function($) {

$.extend($.fn, {
	// http://docs.jquery.com/Plugins/Validation/validate
	validate: function( options ) {
		// if nothing is selected, return nothing; can't chain anyway
		if (!this.length) {
			options && options.debug && window.console && console.warn( "nothing selected, can't validate, returning nothing" );
			return;
		}

		// check if a validator for this form was already created
		var validator = $.data(this[0], 'validator');
		if ( validator ) {
			return validator;
		}
		
		validator = new $.validator( options, this[0] );
		$.data(this[0], 'validator', validator); 
		//表单提交时进行验证设置为有效
		if ( validator.settings.onsubmit ) {
		
			// allow suppresing validation by adding a cancel class to the submit button
			//.cancel样式的按钮点击事件触发
			this.find("input, button").filter(".cancel").click(function() {
				validator.cancelSubmit = true;
			});
			
			// when a submitHandler is used, capture the submitting button
			//当submitHandler设置为有效时，type为submit的按钮为提交按钮
			if (validator.settings.submitHandler) {
				this.find("input, button").filter(":submit").click(function() {
					validator.submitButton = this;
				});
			}
		
			// validate the form on submit
			this.submit( function( event ) {
				if ( validator.settings.debug )
					// prevent form submit to be able to see console output
					event.preventDefault();
					
				function handle() {
					if ( validator.settings.submitHandler ) {
						if (validator.submitButton) {
							// insert a hidden input as a replacement for the missing submit button
							var hidden = $("<input type='hidden'/>").attr("name", validator.submitButton.name).val(validator.submitButton.value).appendTo(validator.currentForm);
						}
						//submitHandler函数输入参数为当前验证的表单对象
						validator.settings.submitHandler.call( validator, validator.currentForm );
						if (validator.submitButton) {
							// and clean up afterwards; thanks to no-block-scope, hidden can be referenced
							hidden.remove();
						}
						return false;
					}
					return true;
				}
					
				// prevent submit for invalid forms or custom submit handlers
				if ( validator.cancelSubmit ) {
					validator.cancelSubmit = false;
					return handle();
				}
				//验证表单方法
				if ( validator.form() ) {
					if ( validator.pendingRequest ) {
						validator.formSubmitted = true;
						return false;
					}
					return handle();
				} else {
					validator.focusInvalid();
					return false;
				}
			});
		}
		
		return validator;
	},
	// http://docs.jquery.com/Plugins/Validation/valid
	valid: function() {
        if ( $(this[0]).is('form')) {
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
	removeAttrs: function(attributes) {
		var result = {},
			$element = this;
		$.each(attributes.split(/\s/), function(index, value) {
			result[value] = $element.attr(value);
			$element.removeAttr(value);
		});
		return result;
	},
	// http://docs.jquery.com/Plugins/Validation/rules
	rules: function(command, argument) {
		var element = this[0];
		
		if (command) {
			var settings = $.data(element.form, 'validator').settings;
			var staticRules = settings.rules;
			var existingRules = $.validator.staticRules(element);
			switch(command) {
			case "add":
				$.extend(existingRules, $.validator.normalizeRule(argument));
				staticRules[element.name] = existingRules;
				if (argument.messages)
					settings.messages[element.name] = $.extend( settings.messages[element.name], argument.messages );
				break;
			case "remove":
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
		
		var data = $.validator.normalizeRules(
		$.extend(
			{},
			$.validator.metadataRules(element),
			$.validator.classRules(element),
			$.validator.attributeRules(element),
			$.validator.staticRules(element)
		), element);
		
		// make sure required is at front
		if (data.required) {
			var param = data.required;
			delete data.required;
			data = $.extend({required: param}, data);
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
	$.each(params, function(i, n) {
		source = source.replace(new RegExp("\\{" + i + "\\}", "g"), n);
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
		validClass: "valid",
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
		onfocusin: function(element) {
			this.lastActive = element;
				
			// hide error label and remove error class on focus if enabled
			if ( this.settings.focusCleanup && !this.blockFocusCleanup ) {
				this.settings.unhighlight && this.settings.unhighlight.call( this, element, this.settings.errorClass, this.settings.validClass );
				this.errorsFor(element).hide();
			}
		},
		onfocusout: function(element) {
			if ( !this.checkable(element) && (element.name in this.submitted || !this.optional(element)) ) {
				this.element(element);
			}
		},
		onkeyup: function(element) {
			if ( element.name in this.submitted || element == this.lastElement ) {
				this.element(element);
			}
		},
		onclick: function(element) {
			if ( element.name in this.submitted )
				this.element(element);
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
		required: "This field is required.",
		remote: "Please fix this field.",
		email: "Please enter a valid email address.",
		url: "Please enter a valid URL.",
		date: "Please enter a valid date.",
		dateISO: "Please enter a valid date (ISO).",
		dateDE: "Bitte geben Sie ein gültiges Datum ein.",
		number: "Please enter a valid number.",
		numberDE: "Bitte geben Sie eine Nummer ein.",
		digits: "Please enter only digits",
		creditcard: "Please enter a valid credit card number.",
		equalTo: "Please enter the same value again.",
		accept: "Please enter a value with a valid extension.",
		maxlength: $.validator.format("Please enter no more than {0} characters."),
		minlength: $.validator.format("Please enter at least {0} characters."),
		rangelength: $.validator.format("Please enter a value between {0} and {1} characters long."),
		range: $.validator.format("Please enter a value between {0} and {1}."),
		max: $.validator.format("Please enter a value less than or equal to {0}."),
		min: $.validator.format("Please enter a value greater than or equal to {0}.")
	},
	
	autoCreateRanges: false,
	
	prototype: {
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
			for ( var i in obj )
				count++;
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
		errors: function() {
			return $( this.settings.errorElement + "." + this.settings.errorClass, this.errorContext );
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
				return;
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
			for(var i = 0; i < arguments.length; i++) {
				if (arguments[i] !== undefined)
					return arguments[i];
			}
			return undefined;
		},
		
		//获取验证错误提示信息
		//1.用户自定义信息
		//2.用户自定义meta信息
		//3.元素的title属性信息
		//4.validator默认提供的信息
		//5.提示没有为该属性配置错误提示信息
		defaultMessage: function( element, method) {
			return this.findDefined(
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
		
		//展现表单验证出错信息
		showLabel: function(element, message) {
			var label = this.errorsFor( element );
			if ( label.length ) {
				// refresh error/success class
				label.removeClass().addClass( this.settings.errorClass );
			
				// check if we have a generated label, replace the message then
				label.attr("generated") && label.html(message);
			} else {
				// create label
				label = $("<" + this.settings.errorElement + "/>")
					.attr({"for":  this.idOrName(element), generated: true})
					.addClass(this.settings.errorClass)
					.html(message || "");
				if ( this.settings.wrapper ) {
					// make sure the element is visible, even in IE
					// actually showing the wrapped element is handled elsewhere
					label = label.hide().show().wrap("<" + this.settings.wrapper + "/>").parent();
				}
				//如果没有设置labelContainer则要做如下动作：
				if ( !this.labelContainer.append(label).length )
					this.settings.errorPlacement
						//调用errorPlacement方法进行label展现,errorPlacement参数为label对象和当前验证元素对象
						? this.settings.errorPlacement(label, $(element) )
						//在元素最后加入label
						: label.insertAfter(element);
			}
			if ( !message && this.settings.success ) {
				label.text("");
				typeof this.settings.success == "string"
					? label.addClass( this.settings.success )
					: this.settings.success( label );
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
				rules[method] = value;
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
					return handler.apply(target, arguments);
				}
			});
		},
		triggerEvent: function(type, target) {
			return this.triggerHandler(type, [$.event.fix({ type: type, target: target })]);
		}
	})
})(jQuery);


// 将表单中input和textarea等页面元素的值的前后空格去掉
// form为jquery对象，而非DOM对象
function trimForm(form) {
	var value = "";
	$("input,textarea", form).not(":button").each(function(i, element) {
		value = $(element).val();
		if (value != null) {
			$(element).val($.trim(value));
		}
	});
}

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

$.extend($.validator.prototype, {
		//初始化错误页面中的html
		errorLabelInitHtml: function(message){
			var arr = [];
			arr.push("	<div class='tooltipfang'></div>");
			arr.push("  <table border='0' cellspacing='0' cellpadding='0' class='tooltiptable'>");
			arr.push("  	<tr><td class='cornerTop topleft'> </td><td class='topcenter'> </td>");
			arr.push("  			<td class='cornerTop topright'> </td></tr><tr><td class='corner bodyleft'> </td>");
			arr.push("				<td class='tooltipcontent'>"+message+"</td>");
			arr.push("				<td class='bodyright'> </td></tr>");
			arr.push("		<tr><td class='cornerTop footerleft'> </td><td class='footercenter'> </td>");
			arr.push("				<td class='cornerTop footerright'> </td></tr></table>");
			return arr.join('');
		},
		
		// 展现表单验证出错信息,修改成提示页面以div展现
		showLabel: function(element, message) {
			var label = this.errorsFor(element);
			//如果label已经存在
			if (label.length) {
				// refresh error/success class
				label.removeClass().addClass(this.settings.errorClass);
				//重新定位error label的位置
				var element1 = element;
				//如果element后面是input、a、img元素则error label放在这些元素后面
				while($(element1).next()[0] && /input|a|img/i.test($(element1).next()[0].tagName)){
					element1 = $(element1).next();
				}
				var pos = $(element1).offset();//element1相对于视窗的位置
				var dim = {width:$(element1).outerWidth(), height:$(element1).outerHeight()};//element1的实际高度和宽度
				var dim2 = {width:$(label).width(), height:$(label).height()};//error label的高度和宽度
				var scrolltop = 0;
				var scrollleft = 0;
				var parentElement = $(element1);
				while(parentElement[0]){//element1的滚动宽度和高度
					scrolltop = scrolltop + parentElement[0].scrollTop;
					scrollleft = scrollleft + parentElement[0].scrollLeft;
					if(parentElement[0].tagName == "BODY"){
						break;
					}
					parentElement = $(parentElement.parent());
				}
				//先设置css一次，使得能够获取到准确的高度和宽度
				label.css({'left':"" + (pos.left + dim.width + scrollleft), 'top':"" + (pos.top + scrolltop + dim.height/2 - dim2.height/2) });
				dim2 = {width:$(label).width(), height:$(label).height()};//重新获取error label的高度和宽度
				label.css({'left':"" + (pos.left + dim.width + scrollleft), 'top':"" + (pos.top + scrolltop + dim.height/2 - dim2.height/2) });
				label.show();
	
				// check if we have a generated label, replace the message then
				label.attr("generated") && label.html(this.errorLabelInitHtml(message));//html代码为<Table>出错信息代码
			} else {
				// create label
				label = $("<" + this.settings.errorElement + "/>").attr({
					"for" : this.idOrName(element),
					generated : true
				}).addClass(this.settings.errorClass).css({//增加div样式
					//'position':"absolute",
					'left':"0px",
					'top':"0px",
					//'width':"50%",
					'z-index':"999",
					'display': "none"
				}).click(function(){//点击提示信息使其隐藏
					$(this).hide();
				}).html(this.errorLabelInitHtml(message) || "");//html代码为<Table>出错信息代码
				if (this.settings.wrapper) {
					// make sure the element is visible, even in IE
					// actually showing the wrapped element is handled elsewhere
					label = label.hide().show().wrap("<" + this.settings.wrapper
							+ "/>").parent();
				}
				
				var element1 = element;
				//如果element后面是input、a、img元素则error label放在这些元素后面
				while($(element1).next()[0] && /INPUT|A|IMG/i.test($(element1).next()[0].tagName)){
					element1 = $(element1).next();
				}
				// 如果没有设置labelContainer则要做如下动作：
				if (!this.labelContainer.append(label).length){
					this.settings.errorPlacement
							// 调用errorPlacement方法进行label展现,errorPlacement参数为label对象和当前验证元素对象
							? this.settings.errorPlacement(label, $(element))
							// 在element后面的input、a、img元素最后加入label
							: label.appendTo(this.container);//label.insertAfter(element1);//
				}
				var pos = $(element1).offset();//element1相对于视窗的位置
				var dim = {width:$(element1).outerWidth(), height:$(element1).outerHeight()};//element1的实际高度和宽度
				var dim2 = {width:$(label).width(), height:$(label).height()};//error label的高度和宽度
				var scrolltop = 0;
				var scrollleft = 0;
				var parentElement = $(element1);
				while(parentElement[0]){//element1的滚动宽度和高度
					scrolltop = scrolltop + parentElement[0].scrollTop;
					scrollleft = scrollleft + parentElement[0].scrollLeft;
					if(parentElement[0].tagName == "BODY"){
						break;
					}
					parentElement = $(parentElement.parent());
				}
				//先设置css一次，使得能够获取到准确的高度和宽度
				label.css({'left':"" + (pos.left + dim.width + scrollleft), 'top':"" + (pos.top + scrolltop + dim.height/2 - dim2.height/2) });
				dim2 = {width:$(label).width(), height:$(label).height()};//重新获取error label的高度和宽度
				label.css({'left':"" + (pos.left + dim.width + scrollleft), 'top':"" + (pos.top + scrolltop + dim.height/2 - dim2.height/2) });
				label.show();
			}
			if (!message && this.settings.success) {
				label.text("");
				typeof this.settings.success == "string" ? label
						.addClass(this.settings.success) : this.settings
						.success(label);
			}
			this.toShow = this.toShow.add(label);
		},
		
		//所有error label
		errors: function() {
			return $( this.settings.errorElement + "." + this.settings.errorClass.split(" ")[0]);
		}
});

$(document).ready(function() {
	window.onresize = function(){//当前窗口大小变化时改变error label的相对视窗的位置
		setTimeout("windowResize()", 100);
	};
	
	// 处理range等验证方法的错误信息，覆盖原方法，直接使用‘，’分隔而不采用数组形式
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
		$.each(params, function(i, n) {
			source = source.replace(new RegExp("\\{" + i + "\\}", "g"), n);
		});
		return source;
	};
	// 覆盖默认的出错信息，采用国际化形式
	$.validator.messages = {
		required : "输入不能为空.",
		remote : "Please fix this field.",
		email : "请输入合法的邮件地址.",
		url : "请输入合法的URL.",
		date : "请输入合法的日期.",
		dateISO : "Please enter a valid date (ISO).",
		dateDE : "Bitte geben Sie ein gültiges Datum ein.",
		number : "请输入一个有效数字.",
		numberDE : "Bitte geben Sie eine Nummer ein.",
		digits : "只能输入整数.",
		creditcard : "请输入合法的信用卡号.",
		equalTo : "两次输入不一致.",
		accept : "Please enter a value with a valid extension.",
		maxlength : $.validator.formatString("输入不能超过 {0} 个字符."),
		minlength : $.validator.formatString("请至少输入 {0} 个字符."),
		rangelength : $.validator.formatString("只能输入 {0} 至 {1} 个字符."),
		range : $.validator.formatString("输入的值只能在 {0} 至 {1}."),
		max : $.validator.formatString("请输入一个小于或等于{0}的值."),
		min : $.validator.formatString("请输入一个大于 {0} 的值.")
	};

	// 覆盖默认设置
	$.validator.setDefaults({
		//验证错误的展现样式名称，高亮有效时使用
		errorClass: "tooltip callout9",
		//验证错误的展现样式名称，高亮无效时使用
		validClass: "tooltip callout9",
		errorElement: "DIV",
		//设置label放置在哪个容器上，现在放置在body的Div上
		//errorPlacement: function(label, element1){
		//	label.appendTo($("#contentborder"));
		//},
		// 取消onkeyup验证事件
		onkeyup : function(element) {
		},
		// onfocus事件中取消验证元素depending，否则必须提交一遍表单才能实现
		onfocusout : function(element) {
			if (!this.checkable(element)) {
				this.element(element);
			}
		},
		onfocusin : function(element) {
			//this.lastActive = element;
			// hide error label and remove error class on focus if enabled
			//if (this.settings.focusCleanup && !this.blockFocusCleanup) {
				this.settings.unhighlight
						&& this.settings.unhighlight.call(this, element,
								this.settings.errorClass,
								this.settings.validClass);
				this.errorsFor(element).hide();
			//}
		},
		onclick: function(element) {
			if (this.checkable(element)) {
				this.element(element);
			}
		}
	});
	
	// 用户自定义函数验证，主要用于ajax验证
	$.validator.addMethod("customFunction", function ajaxFunction(value, element,
			param) {
		//var returnValue = eval(param + "()");
		var returnValue = eval(param).call(element);
		if (typeof returnValue == "string") {
			$.validator.messages["customFunction"] = returnValue;
			return false;
		} else if (typeof returnValue == "boolean") {
			return returnValue;
		} else {
			return false;
		}
	}, "测试错误信息");

	// 字符串类型验证
	$.validator.addMethod("string", function(value, element) {
		return this.optional(element) || /^[\u0391-\uFFE5\w]+$/.test(value);
	}, "只能包括中文字、英文字母、数字和下划线");

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
	}, $.validator.formatString("请确保输入的值在{0}-{1}个字节之间(一个中文字算2个字节)!"));

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
	}, $.validator.formatString("输入的值不能小于{0}位(一个中文字算2位)!"));

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
	}, $.validator.formatString("输入的值不能大于{0}位(一个中文字算2位)!"));

	// 验证用户自定义正则表达式
	$.validator.addMethod("customReg", function(value, element, param) {
		if (param == "") {
			return true;
		}
		var reg = new RegExp(param);
		return reg.test(value);
	}, "输入的信息格式不符合要求!");

	// 验证IP信息格式是否合法
	$.validator.addMethod("ip", function(value, element) {
		return this.optional(element)
				|| (/^(\d+)\.(\d+)\.(\d+)\.(\d+)$/.test(value) && (RegExp.$1 < 256
						&& RegExp.$2 < 256 && RegExp.$3 < 256 && RegExp.$4 < 256));
	}, "请输入正确的IP信息!");

	// 手机号码验证
	$.validator.addMethod("mobile", function(value, element) {
		var length = value.length;
		var mobile = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/;
		return this.optional(element) || (length == 11 && mobile.test(value));
	}, "请正确填写您的手机号码");

	// 电话号码验证
	$.validator.addMethod("telephone", function(value, element) {
		var tel = /^\d{3,4}-?\d{7,9}$/; // 电话号码格式010-12345678
		return this.optional(element) || (tel.test(value));
	}, "请正确填写您的电话号码，号码前需要加区号");

	// 联系电话(手机/电话皆可)验证
	$.validator.addMethod("phone", function(value, element) {
		var length = value.length;
		var mobile = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/;
		var tel = /^\d{3,4}-?\d{7,9}$/;
		return this.optional(element)
				|| (tel.test(value) || mobile.test(value));
	}, "请正确填写您的联系电话");

	// 邮政编码验证
	$.validator.addMethod("zipCode", function(value, element) {
		var tel = /^[0-9]{6}$/;
		return this.optional(element) || (tel.test(value));
	}, "请正确填写您的邮政编码");
});

/**
 * 当前窗口大小变化时改变error label的相对视窗的位置
 */
function windowResize(){
	var errorLabels = $($.validator.defaults.errorElement + "." + $.validator.defaults.errorClass.split(" ")[0]);
	$.each(errorLabels, function(index, errorLabel){
		var idOrName = $(errorLabel).attr("for");
		//id为idOrName
		var element = $("#" + idOrName);
		if(!element[0]){//如果idOrName不是id值，则是name值
			element = $("*[name='" + idOrName + "']");
		}
		if(!element[0]){//如果Id和name都不存在，则该元素不存在
			return;
		}
		var element1 = element;
		//如果element后面是input、a、img元素则error label放在这些元素后面
		while($(element1).next()[0] && /input|a|img/i.test($(element1).next()[0].tagName)){
			element1 = $(element1).next();
		}
		var pos = $(element1).offset();//element1相对于视窗的位置
		var dim = {width:$(element1).outerWidth(), height:$(element1).outerHeight()};//element1的实际高度和宽度
		var dim2 = {width:$(errorLabel).width(), height:$(errorLabel).height()};//error label的高度和宽度
		var scrolltop = 0;
		var scrollleft = 0;
		var parentElement = $(element1);
		while(parentElement[0]){//element1的滚动宽度和高度
			scrolltop = scrolltop + parentElement[0].scrollTop;
			scrollleft = scrollleft + parentElement[0].scrollLeft;
			if(parentElement[0].tagName == "BODY"){
				break;
			}
			parentElement = $(parentElement.parent());
		}
		$(errorLabel).css({'left':"" + (pos.left + dim.width + scrollleft), 'top':"" + (pos.top + scrolltop + dim.height/2 - dim2.height/2) });
	});
};