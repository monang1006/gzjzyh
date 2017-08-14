/*
 * jqGrid4
 */

document.write('<script type="text/javascript" src="' + scriptPath + '/jqgrid/grid.locale-cn.js"></script>');

document.write('<script type="text/javascript" src="' + scriptPath + '/jqgrid/jquery.jqGrid.min.js"></script>');

document.write('<link rel="stylesheet" type="text/css" href="' + scriptPath + '/jqgrid/ui.jqgrid.css" />');



$(function(){
	$.extend(jQuery.jgrid.defaults, {
	    datatype: 'json',
	    mtype: 'POST',
	    prmNames:{page:'curpage',rows:'unitpage'},
	    jsonReader : {
	    	root: "rows",
	        page: "curpage", 
	        total: "totalpages", 
	        records: "totalrecords", 
	        repeatitems: false,
	        id: "0"
	     },
	    multiselect:true,
	    rownumbers:true,
	    pager: '#pager',
	    pagerpos: 'left',
	    viewrecords:true,
	    rowNum:10,
	    rowList:[5,10,20,30],
	    autowidth: true,
	    height:'100%',
	    sortorder: 'desc',
	    altRows:true,
	    altclass:'altRow',
	    gridview: true,
	    isTips:false
	});
	
});
