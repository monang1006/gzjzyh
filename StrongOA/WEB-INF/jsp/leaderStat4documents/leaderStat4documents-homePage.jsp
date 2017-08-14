<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>无标题文档gfgf</title>
<link href="css/dmain.css" type="text/css" rel="stylesheet" />
<script language="javascript" src="css/jquery.js"></script>
<script language="javascript">
$(function(){
alert();
    var Mtop = $(".mtop dt span");
	Mtop.mouseover(function(){
	  var wi = Mtop.index(this);
	  $(this).addClass("mtopsping").siblings("span").removeClass("mtopsping");
	  $(".mtop dd div").eq(wi).show().siblings("div").hide();
	});	   
  });
</script>
</head>

<body>
<div id="dcon">
  <div class="mtop">
    <dl>
      <dt><span class="mtopsping">[信息处]公文办理情况统计</span><span>[督查处]公文办理情况统计</span><span>全厅公文办理情况统计</span></dt>
      <dd>
        <div>
          <p>1、本周收文登记文件总数<span>649</span>件，已分办文件<span>49</span>件，退回外单位文件20件；</p>
          <p>2、本周办结文件总数500件，其中自办文200件，收文100件，发文100件，呈阅件100件；</p>
          <p>3、本周发文登记文件总数<span>320</span>件，其中自办文220件，收文50件，发文50件，呈阅件0件；</p>
          <p>4、当前在办文件总数281件，其中自办文<span>220</span>件，收文10件，发文20件，呈阅件31件。</p>
        </div>
        <div style="display:none">
          <p>1、本周收文登记文件总数<span>649</span>件，已分办文件<span>49</span>件，退回外单位文件20件；</p>
          <p>2、本周办结文 文100件，发文100件，呈阅件100件；</p>
          <p>3、本周发文登记文件总数<span>320</span>件，其中自办文220件，收文50件，发文50件，呈阅件0件；</p>
          <p>4、当前在办文件总数 an>件，收文10件，发文20件，呈阅件31件。</p>
        </div>
        <div style="display:none">
          <p>1、本周收文登sdfsfsdfsd件20件；</p>
          <p>2、本周办结文件总sdfsdf100件，呈阅件100件；</p>
          <p>3、本周发文登记文件总数sdfsdf，其中自办文220件，收文50件，发文50件，呈阅件0件；</p>
          <p>4、当前在办文件总数2sdf阅件31件。</p>
        </div>
      </dd>
    </dl>
  </div>
  <div class="dmain clearfix">
    <div class="dmlef fle">
      <div class="dm01 clearfix">
        <dl>
          <dt><b class="dtmore"><a href="#">更多&gt;&gt;</a></b><span>待办提醒</span></dt>
          <dd>
            <table width="100%">
              <tr>
                <td><a href="#">关于呈报《中共江西省委江西省人民政府关于构建和谐劳....</a></td>
                <td>11-27</td>
              </tr>
              <tr>
                <td><a href="#">关于呈报《中共江西省委江西省人民政府关于构建和谐劳....</a></td>
                <td>11-27</td>
              </tr>
              <tr>
                <td><a href="#">关于呈报《中共江西省委江西省人民政府关于构建和谐劳....</a></td>
                <td>11-27</td>
              </tr>
              <tr>
                <td><a href="#">关于呈报《中共江西省委江西省人民政府关于构建和谐劳....</a></td>
                <td>11-27</td>
              </tr>
              <tr>
                <td><a href="#">关于呈报《中共江西省委江西省人民政府关于构建和谐劳....</a></td>
                <td>11-27</td>
              </tr>
              <tr>
                <td><a href="#">关于呈报《中共江西省委江西省人民政府关于构建和谐劳....</a></td>
                <td>11-27</td>
              </tr>
              <tr>
                <td><a href="#">关于呈报《中共江西省委江西省人民政府关于构建和谐劳....</a></td>
                <td>11-27</td>
              </tr>
              <tr>
                <td><a href="#">关于呈报《中共江西省委江西省人民政府关于构建和谐劳....</a></td>
                <td>11-27</td>
              </tr>
            </table>
          </dd>
        </dl>
      </div>
      <div class="dm02">
        <dl class="dmdls">
          <dt><div><span>本日</span><span class="dm02sping">本周</span><span>本月</span><span>本年</span></div><b>办结文件统计</b></dt>
          <dd><img src="images/dzfimg01.jpg" /></dd>
        </dl>
      </div>
      <div class="dm03">
        <dl class="dmdls">
          <dt><div><span>本日</span><span class="dm02sping">本周</span><span>本月</span><span>本年</span></div><b>发文登记统计</b></dt>
          <dd><img src="images/dzfimg01.jpg" /></dd>
        </dl>
      </div>
      <div class="dm04">
        <dl class="dmdls">
          <dt><div><span>本日</span><span class="dm02sping">本周</span><span>本月</span><span>本年</span></div><b>在办文件统计</b></dt>
          <dd><img src="images/dzfimg01.jpg" /></dd>
        </dl>
      </div>
    </div>
    <div class="dmrig fri">
      <div class="dmrid01"><a href="#"><img src="images/dzfimg02.jpg" /></a></div>
      <div class="dmrid02">
        <dl>
          <dt>日程安排</dt>
          <dd><img src="images/dzfimg03.jpg" /></dd>
        </dl>
      </div>
      <div class="dmrid03">
        <dl class="dmridls">
          <dt>收文登记统计</dt>
          <dd>
            <table width="100%">
              <tr>
                <th><p>本日</p></th>
                <th><p class="dmridping">本周</p></th>
                <th><p>本月</p></th>
                <th><p>本年</p></th>
              </tr>
            </table>
            <div>
              <table width="100%">
                <tr>
                  <td><p><img src="images/dicon01.gif" /> 已登记的文件<span>260</span></p></td>
                </tr>
                <tr>
                  <td><p><img src="images/dicon02.gif" /> 已登记的文件<span>260</span></p></td>
                </tr>
                <tr>
                  <td><p><img src="images/dicon03.gif" /> 已登记的文件<span>260</span></p></td>
                </tr>
                <tr>
                  <td><p><img src="images/dicon04.gif" /> 已登记的文件<span>260</span></p></td>
                </tr>
                <tr>
                  <td><p class="dmrdsplast">总计：<span>260</span></p></td>
                </tr>
              </table>
            </div>
          </dd>
        </dl>
      </div>
      <div class="dmrid04">
        <dl class="dmridls">
          <dt>办结文件统计</dt>
          <dd>
            <table width="100%">
              <tr>
                <th><p>本日</p></th>
                <th><p class="dmridping">本周</p></th>
                <th><p>本月</p></th>
                <th><p>本年</p></th>
              </tr>
            </table>
            <div>
              <table width="100%">
                <tr>
                  <td><p><img src="images/dicon01.gif" /> 已登记的文件<span>260</span></p></td>
                </tr>
                <tr>
                  <td><p><img src="images/dicon02.gif" /> 已登记的文件<span>260</span></p></td>
                </tr>
                <tr>
                  <td><p><img src="images/dicon03.gif" /> 已登记的文件<span>260</span></p></td>
                </tr>
                <tr>
                  <td><p><img src="images/dicon04.gif" /> 已登记的文件<span>260</span></p></td>
                </tr>
                <tr>
                  <td><p class="dmrdsplast">总计：<span>260</span></p></td>
                </tr>
              </table>
            </div>
          </dd>
        </dl>
      </div>
      <div class="dmrid05">
        <dl class="dmridls">
          <dt>发文登记统计</dt>
          <dd>
            <table width="100%">
              <tr>
                <th><p>本日</p></th>
                <th><p class="dmridping">本周</p></th>
                <th><p>本月</p></th>
                <th><p>本年</p></th>
              </tr>
            </table>
            <div>
              <table width="100%">
                <tr>
                  <td><p><img src="images/dicon01.gif" /> 已登记的文件<span>260</span></p></td>
                </tr>
                <tr>
                  <td><p><img src="images/dicon02.gif" /> 已登记的文件<span>260</span></p></td>
                </tr>
                <tr>
                  <td><p><img src="images/dicon03.gif" /> 已登记的文件<span>260</span></p></td>
                </tr>
                <tr>
                  <td><p><img src="images/dicon04.gif" /> 已登记的文件<span>260</span></p></td>
                </tr>
                <tr>
                  <td><p class="dmrdsplast">总计：<span>260</span></p></td>
                </tr>
              </table>
            </div>
          </dd>
        </dl>
      </div>
      <div class="dmrid06">
        <dl class="dmridls">
          <dt>在办文件统计</dt>
          <dd>
            <table width="100%">
              <tr>
                <th><p>本日</p></th>
                <th><p class="dmridping">本周</p></th>
                <th><p>本月</p></th>
                <th><p>本年</p></th>
              </tr>
            </table>
            <div>
              <table width="100%">
                <tr>
                  <td><p><img src="images/dicon01.gif" /> 已登记的文件<span>260</span></p></td>
                </tr>
                <tr>
                  <td><p><img src="images/dicon02.gif" /> 已登记的文件<span>260</span></p></td>
                </tr>
                <tr>
                  <td><p><img src="images/dicon03.gif" /> 已登记的文件<span>260</span></p></td>
                </tr>
                <tr>
                  <td><p><img src="images/dicon04.gif" /> 已登记的文件<span>260</span></p></td>
                </tr>
                <tr>
                  <td><p class="dmrdsplast">总计：<span>260</span></p></td>
                </tr>
              </table>
            </div>
          </dd>
        </dl>
      </div>
    </div>
  </div>
</div>
</body>
</html>
