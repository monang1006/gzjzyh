<meta http-equiv="X-UA-Compatible" content="IE=edge">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/include/rootPath3.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<title>时效统计</title>
<%@include file="/common/include/meta.jsp"%>
<LINK type=text/css rel=stylesheet
	href="<%=frameroot%>/css/properties_windows_add.css">
<script type="text/javascript" language="javascript"
	src="<%=path%>/common/js/jquery/jquery-1.8.3.min.js"></script>
<LINK href="<%=path%>/css/properties_windows_list.css" type=text/css
	rel=stylesheet>
<script language="javascript" src="<%=path%>/common/js/echarts/echarts.min.new.js"></script>
</head>
<base target="_self" />
<body class=contentbodymargin oncontextmenu="return false;">
	<!-- 为ECharts准备一个具备大小（宽高）的Dom -->
    <div id="main" style="width: 100%;height:100%;"></div>
    <script type="text/javascript">
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('main'));

     	// 指定图表的配置项和数据
        option = {
		    title: {
		        text: '时效统计',
		        textStyle: {
		        	fontSize: 20
		        }
		    },
		    tooltip: {
		        trigger: 'axis',
		        axisPointer: {
		            type: 'shadow'
		        }
		    },
		    legend: {
		        data: ['执行任务最短时间', '执行任务最长时间', '执行任务平均时间']
		    },
		    grid: {
		        left: '3%',
		        right: '4%',
		        bottom: '3%',
		        containLabel: true
		    },
		    xAxis: {
		    	type: 'category',
		        data: ${bankNamesJson}
		    },
		    yAxis: {
		    	type: 'value',
		    	name: '单位（秒）',
		        boundaryGap: [0, 0.01],
		        minInterval: 1
		    },
		    series: [
		        {
		            name: '执行任务最短时间',
		            type: 'bar',
		            data: ${minTimesJson}
		        },
		        {
		            name: '执行任务最长时间',
		            type: 'bar',
		            data: ${maxTimesJson}
		        },
		        {
		            name: '执行任务平均时间',
		            type: 'bar',
		            data: ${avgTimesJson}
		        }
		    ]
		};
        
        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
    </script>
</body>
</html>