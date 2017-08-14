<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>首页</title>
<script type="text/javascript" src="<%=scriptPath%>/global.js"></script>
<style type="text/css">
body,h1,h2,h3,h4,h5,h6,hr,p,blockquote,dl,dt,dd,ul,ol,li,pre,form,fieldset,legend,button,input,textarea,th,td{margin:0;padding:0;}body,button,input,select,textarea{font:16px/1.5 tahoma,arial,simsun,sans-serif;}h1,h2,h3,h4,h5,h6{font-size:100%;}address,cite,dfn,em,var{font-style:normal;}code,kbd,pre,samp{font-family:courier new,courier,monospace;}small{font-size:12px;}ul,ol,li{list-style:none;}a{text-decoration:none;}a:hover{text-decoration:underline;}sup{vertical-align:text-top;}sub{vertical-align:text-bottom;}legend{color:#000;}fieldset,img{border:0;}button,input,select,textarea{font-size:100%;}table{border-collapse:collapse;border-spacing:0;}
html{ color: #000; background:#fff; }
body{ font:16px/1.5 tahoma,arial,simsun,sans-serif; font-family:"\5b8b\4f53", sans-serif; }
a{ color: #333; text-decoration:none; cursor:pointer; }
a:hover{ color:#0f67ab; text-decoration:underline; }
.inline-block{ display: inline-block; zoom: 1; *display: inline; vertical-align: middle; }
.clearfix:after{ content: '\0020'; display: block; height: 0; clear: both; }
.clearfix{ *zoom: 1; }
.clear{ clear:both; height:0; line-height:0; font-size:0; width:100%; overflow:hidden; }
.fri{ float: right; }
.fle{ float: left; }
.tecen{ text-align:center; }
.tit{ font-size:16px; font-weight:bold; }
.dtmore{ font-size:16px; font-weight:normal; float:right; }
.dlef{ width:274px; border:1px solid #ccc; border-right:none; background:url(<%=themePath%>/images/dx01.gif) #f6f6f6 repeat-y right center; vertical-align:top; }
.dlefd01 dt{ height:35px; line-height:35px; background:url(<%=themePath%>/images/dx02.gif) no-repeat left top; padding:0 8px 0 86px; } 
.dlefd01 dt a,.driglis dt a{ color:#0f67ab; }
.dlefd01 dt a:hover,.driglis dt a:hover{ color:#000; text-decoration:none; }
.dlefd01 dt span a,.driglis dt span a{ color:#999; }
.dlefd01 dt span a:hover,.driglis dt span a:hover{ color:#000; text-decoration:underline; }
.dlefd01 dd{ overflow:hidden; height:240px; padding:18px 24px 0 36px; }
.dlefd01 dd td{ line-height:21px; height:21px; }
.dlefd02 dt{ height:37px; line-height:37px; background:url(<%=themePath%>/images/dx02.gif) no-repeat left top; padding:0 8px 0 86px; } 
.dlefd02 dd{ background:none; padding:10px 24px 10px 36px; }
.drig{ width:222px; padding-left:8px; vertical-align:top; }
.driglis{ background:url(<%=themePath%>/images/dx07.gif) #fbfbfb repeat-x center top; border:1px solid #dedede; }
.driglis dt{ height:31px; border-bottom:1px solid #dedede; line-height:31px; background:url(<%=themePath%>/images/dx08.gif) no-repeat left top; padding:0 8px 0 38px; }
.drigli02 dt{ background-image:url(<%=themePath%>/images/dx09.gif); }
.drigli03 dt{ background-image:url(<%=themePath%>/images/dx10.gif); }
.driglis dd{ padding:10px; }
.drigli01,.drigli02{ margin-bottom:8px; }
.drigli02 dd th,.drigli02 dd td{ border:1px solid #c5d9e8; padding:3px 5px; background-color:#f7f9fb; } 
.drigli02 dd th{ font-weight:normal; color:#4d5e6a; background-color:#dbe9f5; }
.drigli03 dd td,.drigli01 dd td{ height:21px; line-height:21px; }
.dcen{ border:1px solid #ccc; padding:10px 10px 0; border-left:none; vertical-align:top; }
.dcenlis{ background:url(<%=themePath%>/images/dx06.gif) repeat-x center top; }
.dcenlis dt{ height:33px; background:url(<%=themePath%>/images/dx05.gif) no-repeat left top; padding:7px 8px 0 50px; }
.dcenlis dt a{ color:#fff; }
.dcenlis dt a:hover{ color:#000; text-decoration:none; }
.dcenlis dt span a{ color:#134680; }
.dcenlis dt span a:hover{ color:#000; text-decoration:none; text-decoration:underline; }
.dcenlis dd{ padding:6px 12px 18px; }
.dcenlis dd td{ line-height:30px; border-bottom:1px dashed #ccc; }
</style>
</head>

<body>
<div id="dcon" style="padding:20px;">
  <table width="100%" style="min-width: 1200px">
  	<tr>
  	  <td class="dlef">
        <dl class="dlefd01">
          <dt class="tit">
           <a href="javascript:redirect('<%=root%>/xxbs/action/bulletin.action?submitStatus=1','通知公告','00130007');">通知公告</a>
           <span class="dtmore">
           <a href="javascript:redirect('<%=root%>/xxbs/action/bulletin.action?submitStatus=1','通知公告','00130007');">更多&gt;&gt;</a>
           </span>
          </dt>
          <dd id="bulletin">
          </dd>
        </dl>
        <dl class="dlefd01 dlefd02">
          <dt class="tit">
          <a href="javascript:redirect('<%=root%>/xxbs/action/invitation.action?submitStatus=1&isSinceToday=true','约稿管理','00130001');">约稿信息</a>
          <span class="dtmore">
           <a href="javascript:redirect('<%=root%>/xxbs/action/invitation.action?submitStatus=1&isSinceToday=true','约稿管理','00130001');">更多&gt;&gt;</a>
           </span>
          </dt>
          <dd style="overflow:hidden; height:168px;" id="invitation">
          </dd>
        </dl>
       <!--   <dl class="dlefd01 dlefd02">
          <dt class="tit"><span class="dtmore"><a href="#">更多&gt;&gt;</a></span><a href="#">最新帖子</a></dt>
          <dd style="overflow:hidden; height:168px;">
            <table width="100%">
              <tr>
                <td><img src="<%=themePath%>/images/dx12.gif" /> <a href="#">信息采编与报送系编与报送系统</a></td>
              </tr>
              <tr>
                <td><img src="<%=themePath%>/images/dx12.gif" /> <a href="#">信息采编与报送系编与报送系统</a></td>
              </tr>
              <tr>
                <td><img src="<%=themePath%>/images/dx12.gif" /> <a href="#">信息采编与报送系编与报送系统</a></td>
              </tr>
              <tr>
                <td><img src="<%=themePath%>/images/dx12.gif" /> <a href="#">信息采编与报送系编与报送系统</a></td>
              </tr>
              <tr>
                <td><img src="<%=themePath%>/images/dx12.gif" /> <a href="#">信息采编与报送系编与报送系统</a></td>
              </tr>
              <tr>
                <td><img src="<%=themePath%>/images/dx12.gif" /> <a href="#">信息采编与报送系编与报送系统</a></td>
              </tr>
              <tr>
                <td><img src="<%=themePath%>/images/dx12.gif" /> <a href="#">信息采编与报送系编与报送系统</a></td>
              </tr>
              <tr>
                <td><img src="<%=themePath%>/images/dx12.gif" /> <a href="#">信息采编与报送系编与报送系统</a></td>
              </tr>
            </table>
          </dd>
        </dl>-->
        
         <dl class="dlefd01 dlefd02">
          <dt class="tit"><span class="dtmore">
          <a href="javascript:redirect('<%=root%>/xxbs/action/infoReport.action?submitStatus=1','通报管理','00130010');">更多&gt;&gt;</a></span>
          <a href="javascript:redirect('<%=root%>/xxbs/action/infoReport.action?submitStatus=1','通报管理','00130010');">采用通报</a></dt>
          <dd style="overflow:hidden; height:168px;" id="report">
            
          </dd>
        </dl>
      </td>
      <td class="dcen">
        <dl class="dcenlis dcenli01">
          <dt class="tit"><span class="dtmore">
          <a href="javascript:redirect('<%=root%>/xxbs/action/submit.action?submitStatus=1','报送信息','00140004');">更多&gt;&gt;</a>
          </span>
          <a href="javascript:redirect('<%=root%>/xxbs/action/submit.action?submitStatus=1','报送信息','00140004');">报送信息</a>
          </dt>
          <dd style="height:200px; overflow:hidden;" id="submitted">
          </dd>
        </dl>
        <dl class="dcenlis dcenli02">
          <dt class="tit">
          <a href="javascript:redirect('<%=root%>/xxbs/action/submit.action?submitStatus=1','报送信息','00140004');">采用信息</a>
          <span class="dtmore">
          <a href="javascript:redirect('<%=root%>/xxbs/action/submit.action?submitStatus=1','报送信息','00140004');">更多&gt;&gt;</a>
          </span>
          </dt>
          <dd style="height:175px; overflow:hidden;" id="used">
         </dd>
        </dl>
        <dl class="dcenlis dcenli03">
          <dt class="tit">
          <a href="javascript:redirect('<%=root%>/xxbs/action/submit.action?submitStatus=2&isShared=1','报送信息','00140004');">共享信息</a>
          <span class="dtmore">
          <a href="javascript:redirect('<%=root%>/xxbs/action/submit.action?submitStatus=2&isShared=1','报送信息','00140004');">更多&gt;&gt;</a>
          </span>
          </dt>
          <dd style="height:175px; overflow:hidden;" id="shared">
          </dd>
        </dl>
      </td>
      <td class="drig">
        <dl class="driglis drigli01">
          <dt class="tit">
          <a href="javascript:redirect('<%=root%>/xxbs/action/release!issueView.action','最新刊物','00130003');">最新刊物</a>
          <span class="dtmore">
          <a href="javascript:redirect('<%=root%>/xxbs/action/release!issueView.action','最新刊物','00130003');">更多&gt;&gt;</a>
          </span>
          </dt>
          <dd style="overflow:hidden; height:210px;" id="issue">
          </dd>
        </dl>
        <!-- <dl class="driglis drigli02">
          <dt class="tit"><span class="dtmore">
           <a href="javascript:redirect('<%=root%>/xxbs/action/statistics!point.action','采用统计','00130004');">更多&gt;&gt;</a></span><a href="javascript:redirect('<%=root%>/xxbs/action/statistics!point.action','采用统计','00130004');">采用排名</a></dt>
          <dd id="rank" style="height:200px">
          </dd>
        </dl> -->
        <dl class="driglis drigli03">
          <dt class="tit"><span class="dtmore"></span><a href="#">资源下载</a></dt>
          <dd style="overflow:hidden; height:168px;">
            <table width="100%">
              <tr>
                <td></td>
              </tr>
            </table>
          </dd>
        </dl>
      </td>
  	</tr>
  </table>
</div>

<script type="text/javascript">
var w = gl.windowWidth();
var h = $(window).height();

function invitation(id){
	var url = "<%=root%>/xxbs/action/invitation!input.action?op=view&toId="+id;
	gl.showDialog(url,800,600);
}

function bulletin(id){
	var url = "<%=root%>/xxbs/action/bulletin!view.action?toId="+id;
	gl.showDialog(url,800,600);
	getBulletin();
}

function publish(id){
	var url = "<%=root%>/xxbs/action/submit!view.action?toId="+id;
	gl.showDialog(url,1000,800);
}

function report(id){
	var url = "<%=root%>/xxbs/action/infoReport!view.action?toId="+id;
	gl.showDialog(url,650,480);
}

function journal(id){
	var url = "<%=root%>/xxbs/action/release!content.action?selView=1&toId="+id;
	gl.showDialog(url,790,h);
}
function journal1(id){
	var url = "<%=root%>/xxbs/action/release!content1.action?selView=1&toId="+id;
	gl.showDialog(url,990,h);
}

function getBulletin(){
	$.get("<%=root%>/xxbs/action/index!bulletin.action", function(data){
		$("#bulletin").html(data);
	}, 'html');
}

function pubReort(pubSubmitDate){
	var url = "<%=root%>/xxbs/action/handling!usedReport.action?toId="+pubSubmitDate;
	gl.showDialog(url,790,h);		
}


$(function(){
	getBulletin();
	
	$.ajax({
		url: "<%=root%>/xxbs/action/index!JdcbBulletin.action",
		success: function(data){
			$("#bulletin").html(data);
		},
		cache: false,
		dataType: 'html'
	});
	
	$.ajax({
		url: "<%=root%>/xxbs/action/index!submitted.action",
		success: function(data){
			$("#submitted").html(data);
		},
		cache: false,
		dataType: 'html'
	});
	
	$.ajax({
		url: "<%=root%>/xxbs/action/index!used.action",
		success: function(data){
			$("#used").html(data);
		},
		cache: false,
		dataType: 'html'
	});
	
	//$.ajax({
	//	url: "<%=root%>/xxbs/action/index!rank.action",
	//	success: function(data){
	//		$("#rank").html(data);
	//	},
	//	cache: false,
	//	dataType: 'html'
	//});
	
	$.ajax({
		url: "<%=root%>/xxbs/action/index!shared.action",
		success: function(data){
			$("#shared").html(data);
		},
		cache: false,
		dataType: 'html'
	});
	
	$.ajax({
		url: "<%=root%>/xxbs/action/index!usedReport.action",
		success: function(data){
			$("#report").html(data);
		},
		cache: false,
		dataType: 'html'
	});
	
	$.ajax({
		url: "<%=root%>/xxbs/action/index!invitation.action",
		success: function(data){
			$("#invitation").html(data);
		},
		cache: false,
		dataType: 'html'
	});
	
	$.ajax({
		url: "<%=root%>/xxbs/action/index!issue.action",
		success: function(data){
			$("#issue").html(data);
		},
		cache: false,
		dataType: 'html'
	});
});



function redirect(url, name, id){

	if(parent.contentWindow == undefined)
 		parent.addTab(id, name, url);
 	else
 		parent.contentWindow.addTab(id, name, url);

}


</script>
</body>
</html>
