var RoomView = Class.create();
RoomView.className = {
	container : 'calendar',
	header : 'calendar_header',
	preYears : 'calendar_preYears',
	nextYears : 'calendar_nextYears',
	years : 'calendar_years',
	mark : 'calendar_mark',
	ym : 'calendar_ym',
	table : 'calendar_table',
	thRight : 'right',
	tdRight : 'right',
	tdBottom : 'bottom',
	date : 'calendar_date',
	holiday : 'calendar_holiday',
	regularHoliday : 'calendar_regularHoliday',
	schedule : 'calendar_schedule',
	highlightDay : 'calendar_highlightDay',
	scheduleListContainer : 'calendar_scheduleListContainer',
	scheduleItem : 'calendar_scheduleItem',
	scheduleTimeArea : 'calendar_scheduleItemTimeArea',
	scheduleHandler : 'calendar_scheduleHandler',
	holidayName : 'calendar_holidayName',
	// holidayContainer: 'calendar_holidayContainer',
	dateContainer : 'calendar_dateContainer',
	tableHeader : 'calendar_tableHeader',
	rowContent : 'calendar_rowContent',
	selected : 'calendar_selected',

	nextYearMark : 'calendar_nextYearMark',
	nextMonthMark : 'calendar_nextMonthMark',
	nextWeekMark : 'calendar_nextWeekMark',
	preYearMark : 'calendar_preYearMark',
	preMonthMark : 'calendar_preMonthMark',
	preWeekMark : 'calendar_preWeekMark',

	weekTable : 'calendar_weekContainerTable',
	weekMainTable : 'calendar_weekMainTable',
	timeLine : 'calendar_timeline',
	timeLineTimeTop : 'calendar_timelineTimeTop',
	timeLineTime : 'calendar_timelineTime',
	headerColumn : 'calendar_headerColumn',
	columnTopDate : 'calendar_columnTopDate',
	columnDate : 'calendar_columnDate',
	columnDateOdd : 'calendar_columnOddDate',
	scheduleItemSamll : 'calendar_scheduleItemSmall',
	scheduleItemLarge : 'calendar_scheduleItemLarge',
	scheduleItemNoBorder : 'calendar_scheduleItemNoBorder',
	scheduleItemSelect : 'calendar_scheduleItemSelect',
	deleteImg : 'calendar_deleteImage',
	privateImg : 'calendar_privateImage',
	scheduleContainer : 'calendar_weekScheduleContainer',
	selector : 'calendar_selector',
	cover : 'calendar_cover'
}

RoomView.smallClassName = {
	container : 'calendar_small',
	header : 'calendar_header_small',
	calendar : 'calendar_calendar_small',
	table : 'calendar_tableSmall'
}

RoomView.size = {
	large : 'large',
	small : 'small'
}

/**
 * RoomView Class
 */
RoomView.prototype = {

	initialize : function(element) {
		this.building = true;
		this.element = $(element);
		Element.setStyle(this.element, {
					visibility : 'hidden'
				});
		Element.hide(this.element);
		alert("element:"+this.element.outerHTML);
		this.options = Object.extend({
					initDate : new Date(),
					cssPrefix : 'custom_',
					holidays : [],
					schedules : [],
					size : RoomView.size.large,
					regularHoliday : [0, 6],
					displayIndexes : [0, 1, 2, 3, 4, 5, 6],
					displayTime : [{
								hour : 0,
								min : 0
							}, {
								hour : 24,
								min : 0
							}],
					weekIndex : 0,
					dblclickListener : null,
					afterSelect : function(x,y){},//选择弹出添加提示框
					beforeRefresh : Prototype.emptyFunction,
					changeSchedule : function(term){},//拖动事务活动
					changeCalendar : function(date, oldDate,type){},//查看下月/下年日历
					displayType : 'month',
					highlightDay : true,
					beforeRemoveSchedule : function() {
						return true;
					},
					dblclickSchedule : null,
					updateTirm : function(term){},//拖动修改事务的时间
					displayTimeLine : true,
					clickDateText : null,
					monthHeaderFormat : null,
					weekHeaderFormat : null,
					weekSubHeaderFormat : null,
					dayHeaderFormat : null,
					dayOfWeek : DateUtil.dayOfWeek
				}, arguments[1] || {});

		this.options.holidays = this.toHolidayHash(this.options.holidays);
		// this.options.schedules = this.toScheduleHash(this.options.schedules);

		this.setIndex();

		this.classNames = null;
		if (this.options.size == RoomView.size.large) {
			this.classNames = RoomView.className;
		} else {
			this.classNames = $H({}).merge(RoomView.className);
			this.classNames = this.classNames.merge(RoomView.smallClassName);
		}
		var customCss = CssUtil.appendPrefix(this.options.cssPrefix,
				this.classNames);
		this.classNames = new CssUtil([this.classNames, customCss]);

		this.date = this.options.initDate;

		this.roomview = this.build();
		alert("this.build(): "+this.build());
		this.element.appendChild(this.roomview);

		Event.observe(document, "mouseup", this.onMouseUp
						.bindAsEventListener(this));
		Element.setStyle(this.element, {
					visibility : 'visible'
				});
		Element.show(this.element);
		this.builder.afterBuild();
		this.windowResize = this.onResize.bind(this);
		if (this.options.size != 'small')
			Event.observe(window, "resize", this.windowResize);
		this.building = false;
	},

	onResize : function() {
		try {
			var oldDimentions = this.builder.containerDimensions;
			var dimentions = Element.getDimensions(this.builder.container);
			if (dimentions.height != oldDimentions.height
					|| dimentions.width != oldDimentions.width) {
				this.refresh();
			}
		} catch (e) {
		}
	},

	destroy : function() {
		Event.stopObserving(window, 'resize', this.windowResize);
	},

	setIndex : function() {
		var options = this.options;
		var bottom = [];
		var up = [];
		var index = null;
		options.displayIndexes.sort();
		options.displayIndexes.each(function(i) {
					if (index == null) {
						if (options.weekIndex <= i) {
							index = i;
							up.push(i);
						} else {
							bottom.push(i);
						}
					} else {
						up.push(i);
					}
				});
		options.displayIndexes = up.concat(bottom);
		this.setLastWday();
	},

	setLastWday : function() {
		var firstIndex = this.options.weekIndex;
		var sat = 6;
		var sun = 0;
		var week = $R(firstIndex, sat, false).toArray();
		if (firstIndex != sun) {
			week = week.concat($R(sun, firstIndex - 1, false).toArray());
		}
		this.lastWday = week.last();
		this.wdays = week;
	},

	build : function() {
		var s,e;//开始时间
		if (this.builder) {
			this.builder.destroy();
		}
		
		if (this.options.displayType == 'week') {
			this.builder = new CalendarWeek(this);
			s = this.builder.getWeek().first();
			e = this.builder.getWeek().last();
		} else if (this.options.displayType == 'day') {
//			this.builder = new CalendarDay(this);
			this.builder = new RoomViewTable(this);
			alert("this.innerHTML:"+this.innerHTML);
			s = this.date;
			e = this.date;
		} else {
			this.builder = new CalendarMonth(this);
			var year = this.date.getFullYear();
			var month = this.date.getMonth();
			s = DateUtil.getFirstDate(year, month);
			e = DateUtil.getLastDate(year, month);
		}
		
		//调用页面ajax脚本 填充日程事务
		var startD = s.getYear()+"-"+s.getMonth()+"-"+s.getDate();
		var endD = e.getYear()+"-"+e.getMonth()+"-"+e.getDate();
		//alert("开始时间为:"+startD+"\n结束时间为:"+endD);
//        setSchedules(startD,endD);
            
//		this.builder.beforeBuild();
//		return this.builder.build();
	},

	/** * Calendar ** */
	/**
	 * ******************************** public method
	 * *********************************
	 */
	undo : function() {
		if (this.cached) {
			this.cached.start = this.cached.start_old;
			this.cached.finish = this.cached.finish_old;
			this.cached = null;
			this.refreshSchedule();
		}
	},

	hideSatSun : function() {
		var sun = 0;
		var sat = 6;
		this.options.displayIndexes = this.options.displayIndexes.without(sun,
				sat);
		this.setIndex();
		this.refresh();
	},

	showSatSun : function() {
		var sun = 0;
		var sat = 6;
		var indexes = this.options.displayIndexes;
		if (!indexes.include(sun)) {
			indexes.push(sun);
		}
		if (!indexes.include(sat)) {
			indexes.push(sat);
		}
		this.setIndex();
		this.refresh();
	},

	changeDisplayIndexes : function(indexes) {
		this.options.displayIndexes = indexes;
		this.setIndex();
		this.refresh();
	},

	changeDisplayTime : function(time) {
		this.options.displayTime = time;
		this.refresh();
	},

	refresh : function() {
		try {
			if (!this.building) {
				this.building = true;
				this.options.beforeRefresh(this);
				this.destroy();
				this.selectedBase = null;
				Element.remove(this.calendar);
				this.calendar = this.build();
				this.element.appendChild(this.calendar);
				this.builder.afterBuild();
				if (this.options.size != 'small')
					Event.observe(window, 'resize', this.windowResize);
				this.building = false;
			}
		} catch (e) {
		}
	},

	changeCalendar : function(event) {
		this.builder.changeCalendar(event);
	},

	changeDisplayType : function(type) {
		this.options.displayType = type;
		this.refresh();
	},

	showDetail : function(event) {
		var calendar = this;
		this.abstractSelect(event, function(date, element) {
					if (calendar.selectedBase || calendar.hasSelectedDate()) {
						if (event.ctrlKey) {
							if (Element.hasClassName(element,
									Calendar.className.selected)) {
								calendar.addSelectedClass(element);
								return;
							}
						} else if (calendar.selectedBase) {
							var selectedId = calendar.selectedBase.id;
							$(selectedId).className = calendar.selectedBase.className;
							calendar.clearSelected();
							if (selectedId == element.id) {
								calendar.selectedBase = null;
								return;
							}
						}
					}

					calendar.selectedBase = {
						id : element.id,
						date : date,
						className : element.className
					};
					calendar.addSelectedClass(element);
					//小视图的情况
					if (date && calendar.options.displayType == 'month'
							&& calendar.options.size == Calendar.size.small) {
						//调用页面显示当日事务详情
						
						var d = date.getYear()+"-"+(date.getMonth()+1)+"-"+date.getDate()+" 00:00"
						mouseoverToShow(d,element,event);
						
//						new Balloon(element.id, 'Hello Balloon!');
					}
				});

		if (calendar.options.displayType != 'month'
				|| calendar.options.size != Calendar.size.small) {
			this.mouseDown = true;
		}
	},
	
	selectDate : function(event) {
		var calendar = this;
		// 选中单元格
		// testSelected();
		this.abstractSelect(event, function(date, element) {
					if (calendar.selectedBase || calendar.hasSelectedDate()) {
						if (event.ctrlKey) {
							if (Element.hasClassName(element,
									Calendar.className.selected)) {
								calendar.addSelectedClass(element);
								return;
							}
						} else if (calendar.selectedBase) {
							var selectedId = calendar.selectedBase.id;
							$(selectedId).className = calendar.selectedBase.className;
							calendar.clearSelected();
							if (selectedId == element.id) {
								calendar.selectedBase = null;
								return;
							}
						}
					}

					calendar.selectedBase = {
						id : element.id,
						date : date,
						className : element.className
					};
					calendar.addSelectedClass(element);
					//小视图的情况
					if (date && calendar.options.displayType == 'month'
							&& calendar.options.size == Calendar.size.small) {
//						alert("public method");
						calendar.options.afterSelect(date, calendar);
						selectInSmall(date);
					}
				});

		if (calendar.options.displayType != 'month'
				|| calendar.options.size != Calendar.size.small) {
			this.mouseDown = true;
		}
	},

	clearSelect : function() {
		// BETA
		this.selectedBase = null;
		this.clearSelected();
	},

	showDayOfWeek : function(dayOfWeek) {
		var indexes = this.options.displayIndexes;
		this.recurrence(dayOfWeek, function(d) {
					if (!indexes.include(d)) {
						indexes.push(d);
					}
				});
		this.setIndex();
		this.refresh();
	},

	hideDayOfWeek : function(dayOfWeek) {
		var indexes = this.options.displayIndexes;
		var self = this;
		this.recurrence(dayOfWeek, function(d) {
					var index = self.findIndex(indexes, d);
					if (index) {
						indexes.remove(index);
					} else if (!index.isNaN) {
						indexes.shift();
					}
				});
		this.refresh();
	},

	addHoliday : function(object) {
		object = this.inspectArgument(object);
		var newHash = this.toHolidayHash(object);
		this.options.holidays = this.options.holidays.merge(newHash);
		this.refresh();
	},

	removeHoliday : function(date) {
		var calendar = this;
		date = calendar.inspectDateArgument(date);
		if (!date)
			return;

		this.recurrence(date, function(d) {
					var key = d.toDateString();
					if (calendar.options.holidays[key])
						delete calendar.options.holidays[key];
				});
		this.refresh();
	},

	refreshHoliday : function(object, rebuild) {
		object = this.inspectArgument(object);
		this.options.holidays = this.toHolidayHash(object);
		if (rebuild)
			this.refresh();
	},

	clearHoliday : function() {
		this.refreshHoliday([], true);
	},

	getHoliday : function(date) {
		date = this.inspectDateArgument(date);
		if (!date)
			return;

		var calendar = this;
		var holidays = [];
		this.recurrence(date, function(o) {
					var h = calendar.options.holidays[o.toDateString()];
					if (h)
						holidays.push(h);
				});

		return holidays;
	},

	// 添加视图中的的事务对象
	addSchedule : function(schedule) {
		var schedules = this.options.schedules;
		if (schedule.constructor == Array) {
			schedule.each(function(s) {
						var find = schedules.detect(function(tmp) {
									return s.id == tmp.id
								});
						if (!find)
							schedules.push(s);
					});
		} else {
			var find = schedules.detect(function(tmp) {
						return tmp.id == schedule.id
					});
			if (!find)
				schedules.push(schedule);
		}
		this.refreshSchedule();
	},

	replaceSchedule : function(schedules) {
		this.options.schedules = schedules;
		this.refreshSchedule();
	},

	// 删除视图中的的事务对象
	removeSchedule : function(ids, refresh) {
		if (ids.constructor != Array)
			ids = [ids];
		var self = this;
		ids.each(function(id) {
					var index = null;
					self.options.schedules.each(function(s, i) {
								if (s.id == id) {
									index = i;
									throw $break;
								}
							});

					if (index != null) {
						var schedule = self.options.schedules[index];
						if (schedule) {
							self.options.schedules.remove(index);
						}
					}
				});
		if (refresh)
			this.refreshSchedule();
	},

	// 更新试图中的所有事务对象
	refreshSchedule : function() {
		this.builder.scheduleNodes.each(function(node) {
					Element.remove(node);
				});
		this.builder.afterBuild();
	},
//合并事务
	mergeSchedule : function(schedule) {
		var index = -1;
//		alert("mergeSchedule: function"),
		this.options.schedules.each(function(s, i) {
					if (s.id == schedule.id) {
						index = i;
						throw $break;
					}
				});
		if (index != -1) {
			this.options.schedules[index] = schedule;
			this.refreshSchedule();
		} else {
			this.addSchedule(schedule);
		}
	},

	clearSchedule : function() {
		this.options.schedules = [];
		this.refreshSchedule();
	},

	getSchedule : function(object) {
		var result = [];
		var calendar = this;
		object = this.inspectArgument(object || {});

		this.recurrence(object, function(o) {
			var schedule = calendar.options.schedules[o.date.toDateString()];
			if (!schedule)
				return;

			if (o.start) {
				schedule = schedule.detect(function(s) {
					return ((s.start.hour == o.start.hour) && (s.start.min == o.start.min));
				});
				if (schedule)
					result.push(schedule);
			} else if (o.number) {
				schedule = schedule[o.number];
				if (schedule)
					result.push(schedule);
			} else {
				result = result.concat(schedule);
			}
		});

		return result;
	},

	getSelected : function() {
		return this.element.getElementsByClassName(Calendar.className.selected,
				this.element);
	},

	changeSchedule : function() {
//		alert("changeSchedule : function()");
		var calendar = this;
		return function(drag, drop) {
			var array = drag.id.split('_');
			var i = array.pop();
			var date = array.pop();

			date = calendar.getDate(date);
			var newDate = calendar.getDate(drop);

			var schedule = calendar.getSchedule({
						date : date,
						number : i
					});
			if (schedule.length != 1)
				return;
			schedule = schedule.pop();
			schedule.date = newDate;
			calendar.removeSchedule({
						date : date,
						number : i
					}, false);
			calendar.addSchedule(schedule);

			calendar.options.changeSchedule(schedule);
		}
	},

	getSelectedDates : function() {
		return this.builder.getSelectedDates();
	},

	getSelectedTerm : function() {
		return this.builder.getSelectedTerm();
	},

	/**
	 * ******************************** private method
	 * *********************************
	 */
	abstractSelect : function(event, method) {
		this.builder.abstractSelect(event, method);
	},

	createRange : function(a, b) {
		var range = null;
		if (a <= b)
			range = $R(a, b);
		else
			range = $R(b, a);
		return range;
	},

	formatTime : function(time) {
		var hour = (time.hour < 10) ? '0' + time.hour : time.hour;
		var min = (time.min < 10) ? '0' + time.min : time.min;
		return hour + ':' + min;
	},

	clearSelected : function() {
		var elements = this.getSelected();
		var self = this;
		elements.each(function(e) {
					if (Element.hasClassName(e, Calendar.className.selected))
						self.removeSelectedClass(e);
				});
	},

	onDblClick : function(event) {
		this.abstractSelect(event, this.options.dblclickListener);
	},

	onMouseUp : function(event) {
		var e = event || window.event;
		var calendar = this;
		//当前坐标
		var x = e.clientX;
		var y = e.clientY;
		if (calendar.mouseDown) {
			setTimeout(function() {
						calendar.mouseDown = false;
						calendar.options.afterSelect(x,y);
					}, 50);
		}
	},

	setRegularHolidayClass : function(node) {
		this.classNames.refreshClassNames(node, 'regularHoliday');
	},

	setHolidayClass : function(node) {
		this.classNames.refreshClassNames(node, 'holiday');
	},

	setWorkdayClass : function(node) {
		this.classNames.refreshClassNames(node, 'date');
	},

	setScheduleClass : function(node) {
		this.classNames.refreshClassNames(node, 'schedule');
	},

	addHighlightClass : function(node) {
		Element.addClassName(node, Calendar.className.highlightDay);
	},

	addSelectedClass : function(node) {
		Element.addClassName(node, Calendar.className.selected);
	},

	removeSelectedClass : function(node) {
		Element.removeClassName(node, Calendar.className.selected);
	},

	getDatasWithMonthAndYear : function(array) {
		var calendar = this;
		var result = array.findAll(function(h) {
					return calendar.isSameYearAndMonth(h.date);
				});

		return result;
	},

	isSameYearAndMonth : function(a, b) {
		if (a.constructor == Date) {
			if (!b)
				b = this.date;
			return ((a.getYear() == b.getYear()) && (a.getMonth() == b
					.getMonth()));
		} else {
			return (a.year == b.year && a.month == b.month && a.day == a.day);
		}
	},

	isSameDate : function(a, b) {
		if (a.constructor == Date) {
			if (!b)
				b = this.date;
			return (this.isSameYearAndMonth(a, b) && (a.getDate() == b
					.getDate()));
		} else {
			return (this.isSameYearAndMonth(a, b) && a.day == b.day);
		}
	},

	isSameTime : function(a, b) {
		return ((a.hour == b.hour) && (a.min == b.min));
	},

	betweenDate : function(schedule, date) {
		var start = this.toDateNumber(schedule.start);
		var finish = this.toDateNumber(schedule.finish);
		date = this.toDateNumber(date);
		return start <= date && date <= finish;
	},

	toDateNumber : function(date) {
		if (date.constructor == Date) {
			return date.getFullYear() * 10000 + date.getMonth() * 100
					+ date.getDate();
		} else {
			return date.year * 10000 + date.month * 100 + date.day;
		}
	},

	getTimeDiff : function(a, b) {
		var time = {
			hour : b.hour - a.hour,
			min : b.min - a.min
		};
		if (time.min >= 60) {
			time.hour++;
			time.min -= 60;
		} else if (time.min < 0) {
			time.hour--;
			time.min += 60;
		}
		return time;
	},

	findIndex : function(array, value) {
		var index = null;
		array.each(function(v, i) {
					if (v == value) {
						index = i;
						throw $break;
					}
				});
		return index;
	},

	recurrence : function(object, method) {
		var calendar = this;
		if (object.constructor == Array) {
			object.each(function(o) {
						calendar.recurrence(o, method)
					});
		} else if (object.keys) {
			object.each(function(pair) {
						calendar.recurrence(pair[1], method)
					});
		} else {
			method(object);
		}
	},

	toHolidayHash : function(object) {
		var calendar = this;
		var hash = {};

		this.recurrence(object, function(o) {
					if (!o.name)
						return;
					if (o.date.constructor == Object)
						o.date = new Date(o.date.year, o.date.month, o.date.day);

					hash[o.date.toDateString()] = o;
				});
		return $H(hash);
	},

	inspectArgument : function(object, time) {
		return this.builder.inspectArgument(object, time);
	},

	inspectDateArgument : function(date) {
		return this.builder.inspectDateArgument(date);
	},

	sortSchedule : function(a, b) {
		if (a.start.hour == b.start.hour) {
			if (a.start.min == b.start.min)
				return 0;
			if (a.start.min < b.start.min)
				return -1;
			return 1;
		}
		if (a.start.hour < b.start.hour)
			return -1;

		return 1;
	},

	hasSelectedDate : function() {
		return (this.getSelected().length != 0);
	},

	getDate : function(element) {
		return this.builder.getDate(element);
	},

	isRegularHoliday : function(day) {
		return this.options.regularHoliday.include(day);
	},

	isHoliday : function(date) {
		return this.options.holidays[date.toDateString()];
	},

	isScheduleDay : function(date) {
		return this.options.schedules[date.toDateString()];
	},

	cacheSchedule : function(schedule) {
		this.cached = schedule;
		schedule.start_old = Object.clone(schedule.start);
		schedule.finish_old = Object.clone(schedule.finish);
	}
}

/**
 * AbstractCalendar Class
 */
var AbstractCalendar = Class.create();
AbstractCalendar.id = {
	container : 'container',
	scheduleContainer : 'scheduleContainer',
	selector : 'selector'
}
AbstractCalendar.prototype = {
	destroy : Prototype.emptyFunction,
	beforeBuild : Prototype.emptyFunction,

	build : function() {
		this.header = this.buildHeader();
		var node = Builder.node('DIV', {
					id : this.getContainerId(),
					className : this.calendar.classNames
							.joinClassNames('container')
				}, [this.header, this.buildCalendar()]);

		return node;
	},

	buildHeader : function() {
		var headerNodes = Builder.node('TR');
		headerNodes.appendChild(this.buildHeaderLeft());
		headerNodes.appendChild(this.buildHeaderCenter());
		headerNodes.appendChild(this.buildHeaderRight());

		className = this.calendar.classNames.joinClassNames('header');
		var tbody = Builder.node('TBODY', [headerNodes]);
		return Builder.node('TABLE', {
					className : className
				}, [tbody]);
	},

	buildSelector : function() {
		// create selector
		var selector = Builder.node('DIV', {
					id : this.getSelectorId()
				});
		this.calendar.classNames.addClassNames(selector, 'selector');
		Element.setOpacity(selector, 0.6);
		Element.hide(selector);
		Element.setStyle(selector, {
					zIndex : ZindexManager.getIndex()
				})
		return selector;
	},

	buildCover : function() {
		this.cover = Builder.node('div', {
					id : this.calendar.element.id.appendSuffix('cover')
				});
		this.calendar.classNames.addClassNames(this.cover, 'cover');
		Event.observe(this.cover, 'mousedown', this.calendar.selectDate
						.bindAsEventListener(this.calendar));
		if (this.calendar.options.displayType != 'month'
				|| this.calendar.options.size != 'samll') {
			Event.observe(this.cover, 'mousemove', this.multipleSelection
							.bindAsEventListener(this));
		}
		return this.cover;
	},

	buildScheduleContainer : function() {
		this.container = Builder.node('DIV', {
					style : 'position: relative',
					id : this.getScheduleContainerId()
				});
		return this.container;
	},

	// the default is empty.
	// overwride this method.
	setScheduleContainerEvent : Prototype.emptyFunction,

	changeCalendar : function(event) {
		var element = Event.element(event);
		var date = this.calendar.date;
		var oldDate = new Date(date.toDateString());

		if (Element.hasClassName(element, Calendar.className.preYearMark)) {
			date.setFullYear(date.getFullYear() - 1);
		} else if (Element.hasClassName(element,
				Calendar.className.preMonthMark)) {
			date.setDate(1);
			date.setMonth(date.getMonth() - 1);
		} else if (Element
				.hasClassName(element, Calendar.className.preWeekMark)) {
			date.setDate(date.getDate() - 7);
		} else if (Element.hasClassName(element,
				Calendar.className.nextYearMark)) {
			date.setFullYear(date.getFullYear() + 1);
		} else if (Element.hasClassName(element,
				Calendar.className.nextMonthMark)) {
			date.setDate(1);
			date.setMonth(date.getMonth() + 1);
		} else if (Element.hasClassName(element,
				Calendar.className.nextWeekMark)) {
			date.setDate(date.getDate() + 7);
		}

		var type = this.calendar.options.displayType;
		this.calendar.options.changeCalendar(date, oldDate,type);
		this.calendar.refresh();
	},

	// 单击删除图标
	clickDeleteImage : function(schedule) {
		if (this.calendar.options.beforeRemoveSchedule(schedule)) {
			// 调用删除方法
			var calId = schedule.id;
			if(calId.indexOf("repeatBy")>-1){
				if(confirm("该操作将删除所有相关循环日程,确定执行操作？")){
					calId = calId.replace("repeatBy","");
					deleteSchedule(calId);
					this.calendar.removeSchedule(calId, true);
					window.location.reload();
				}else{
					return false;
				}
			}else{
				deleteSchedule(schedule.id);
				this.calendar.removeSchedule(schedule.id, true);
			}
		}
	},

	showDeleteImage : function(img) {
		Element.show(img);
	},

	hideDeleteImage : function(img) {
		Element.hide(img);
	},

	_constrain : function(n, lower, upper) {
		if (n > upper)
			return upper;
		else if (n < lower)
			return lower;
		else
			return n;
	},

	getContainerId : function() {
		return this.calendar.element.id
				.appendSuffix(AbstractCalendar.id.container);
	},

	getScheduleContainerId : function() {
		return this.calendar.element.id
				.appendSuffix(AbstractCalendar.id.scheduleContainer);
	},

	setColumnWidth : function() {
		var adjustSize = this.getAdjustSize();
		var container = $(this.getScheduleContainerId()) || this.container;
		var indexes = this.calendar.options.displayIndexes;
		this.column.width = container.offsetWidth / indexes.length - adjustSize;
		if (this.column.width < 0)
			this.column.width = 0;
	},

	setCover : function() {
		var container = $(this.getScheduleContainerId()) || this.container;
		if (!this.cover) {
			container.appendChild(this.buildCover());
		}
		Element.setStyle(this.cover, {
					height : Element.getHeight(container) + 'px'
				});
	},

	getDragDistance : function() {
		var adjustSize = this.getAdjustSize();
		// return [this.column.width + adjustSize, this.column.height];
		return [this.column.width + adjustSize, this.column.height / 2];
	},

	getWeek : function() {
		var date = this.calendar.date;
		var baseDay = date.getDay();
		var baseDayIndex = date.getDay();
		var findBaseDay = false;

		return this.calendar.options.displayIndexes.map(function(week, i) {
					var diff = week - baseDay;
					if (!findBaseDay && (diff > 0))
						diff -= 7;
					if (baseDayIndex == week)
						findBaseDay = true;
					return DateUtil.afterDays(date, diff);
				});
	},

	isSameStartDate : function(schedule, date) {
		return ((date.getFullYear() == schedule.start.year)
				&& (date.getMonth() == schedule.start.month) && (date.getDate() == schedule.start.day));
	},

	isSameFinishDate : function(schedule, date) {
		return ((date.getFullYear() == schedule.finish.year)
				&& (date.getMonth() == schedule.finish.month) && (date
				.getDate() == schedule.finish.day));
	},

	getSelectorId : function() {
		return this.calendar.element.id
				.appendSuffix(AbstractCalendar.id.selector);
	},

	clickDateText : function(event, date) {
		Event.stop(event);
		this.calendar.date = date;
		this.calendar.options.displayType = 'day';
		this.calendar.refresh();
	},

	getAdjustSize : function() {
		return UserAgent.isIE() ? 3 : 3;
	},

	setContainerInfo : function() {
		this.container = $(this.getScheduleContainerId());
		this.containerDimensions = Element.getDimensions(this.container);
		this.containerOffset = Position.cumulativeOffset(this.container);
	},

	//鼠标滑过事务
	mouseOverSubSchedule : function(items) {
		items.each(function(item) {
					Element.addClassName(item,
							Calendar.className.scheduleItemSelect);
				});
	},

	mouseOutSubSchedule : function(items) {
		items.each(function(item) {
					Element.removeClassName(item,
							Calendar.className.scheduleItemSelect);
				});
	},

	toDate : function(hash) {
		return DateUtil.toDate(hash);
	}
}




/**
 * 新建会议室表格
 */
var RoomViewTable = Class.create();
RoomViewTable.id = {
	header : 'dayHeader'
}
Object.extend(RoomViewTable.prototype);
Object.extend(RoomViewTable.prototype, {

	initialize : function(roomview) {
		var day = roomview.date.getDay();
		this.roomview = roomview;
//		this.setDisplayTime();
		alert("roomview.element.outerHTML : "+ roomview.element.outerHTML);
	
		alert("this.roomview.options.displayIndexes: "+this.roomview.options.displayIndexes);
		alert("[day]: "+[day]);
//		this.roomview.options.displayIndexesOld = this.roomview.options.displayIndexes;
//		this.roomview.options.displayIndexes = [day];
//		this.roomview.options.weekIndexOld = this.roomview.options.weekIndex;
//		this.roomview.options.weekIndex = day;
//		this.week = this.getWeek();
	},

	destroy : function() {
		this.roomview.options.displayIndexes = this.roomview.options.displayIndexesOld;
		this.roomview.options.weekIndex = this.roomview.options.weekIndexOld;
		delete this.roomview.options.displayIndexesOld;
		delete this.roomview.options.weekIndexOld;
	},

	/**
	 * ******************************** build functions
	 * *********************************
	 */
	buildHeaderCenter : function() {
		var headerTaxt = "buildHeaderCenter___建立自动"
		
//		var headerText = replaceHeadtext(this.calendar.date.toDateString());
//		if (this.calendar.options.dayHeaderFormat) {
//			var date = this.calendar.date;
//			headerText = new Template(this.calendar.options.dayHeaderFormat)
//					.evaluate({
//								year : date.getFullYear(),
//								month : date.getMonth() + 1,
//								day : date.getDate()
//							});
//		}
		var container = Builder.node('TD');
		this.calendar.classNames.addClassNames(container, 'years');

		var id = this.roomview.element.id.appendSuffix(RoomViewTable.id.header);
		var node = Builder.node('SPAN', {
					id : id
				}, [headerText]);
		this.calendar.classNames.addClassNames(node, 'ym');
		container.appendChild(node);

		return container;
	}

});