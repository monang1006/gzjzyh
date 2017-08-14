<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
  <head>
    <%@include file="/common/include/meta.jsp" %>
    <title>发文审批查看</title>
    <link
      href="<%=frameroot%>/css/properties_windows.css"
      type=text/css rel=stylesheet>
    <link type="text/css" rel="stylesheet"
      href="<%=root%>/common/js/tabpane/css/luna/tab.css" />
    <script type="text/javascript"
      src="<%=root%>/common/js/tabpane/js/tabpane.js"></script>
<style type="text/css">
<!--
body {
  margin-left: 0px;
  margin-top: 0px;
  margin-right: 0px;
  margin-bottom: 0px;
}
.titlefont {
  font-size: 36px;
  font-weight: bold;
  color: #FF0000;
  line-height:60px;
  height:60px;
  font-family: "楷体_GB2312";
  text-align:center;
}
.tdfont {color: #FF0000; font-family: "华文仿宋",宋体; font-size: 20px; }
.tdd{ border:1.5px solid red;}
.td2{ border-bottom:1.5px solid red; border-right:1.5px solid red;}
.td3{ border-bottom:1.5px solid red;}
.td4{ border-right:1.5px solid red;}
.suggestfont { text-indent:2em; font-size: 20px;
font-family: "华文仿宋",宋体;}
-->
</style>

  </head>
  <body class="contentbodymargin">

    <DIV id=contentborder align="center" cellpadding="0">
      <form>
        <table border="0" width="98%" bordercolor="#FFFFFF" cellspacing="0"
          cellpadding="0">
          <tr>
            <td width="100%">
              &nbsp;
            </td>
          </tr>
          <tr>
            <td width="100%">
              <DIV class=tab-pane id=tabPane1>
                <SCRIPT type="text/javascript">
                tp1 = new WebFXTabPane( document.getElementById( "tabPane1") ,false );
                </SCRIPT>
                <DIV class=tab-page id=tabPage1>
                  <H2 class=tab>
                    公文处理单
                  </H2>
                    <table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
                      <tr>
                      <td colspan="6" height="50" align="center"><span class="titlefont">* * * 发 文 稿 纸</span></td>
                      </tr>
                      <tr>
                        <td><table width="100%" border="0" cellpadding="0" cellspacing="0" class="tdd">
                          <tr>
                            <td width="10%" height="80" align="center" class="td2"><span class="tdfont">签<br/>发</span></td>
                            <td class="td3"><div class="suggestfont">&nbsp;测试</div></td>
                          </tr>
                          <tr>
                            <td height="60" align="center" class="td2"><span class="tdfont">标<br/>题</span></td>
                            <td class="td3"><div class="suggestfont">&nbsp;测试</div></td>
                          </tr>
                          <tr>
                            <td height="80" colspan="2" align="center"><table width="100%" height="80" border="0" cellspacing="0" cellpadding="0">
                              <tr>
                                <td style=" text-indent:2em;" width="45%" align="left" class="td2"><span class="tdfont">拟稿部门：</span><span class="suggestfont">测试</span></td>
                                <td width="15%" rowspan="2" align="center" class="td2"><span class="tdfont">拟稿部门<br/>
                                  领导审核</span></td>
                                <td width="15%" rowspan="2" align="center" class="td2"><span class="suggestfont">测试</span></td>
                                <td width="15%" rowspan="2" align="center" class="td2"><span class="tdfont">是否网<br/>
                                  上公开</span></td>
                                <td width="10%" rowspan="2" align="center" class="td3"><span class="suggestfont">测试</span></td>
                              </tr>
                              <tr>
                                <td style=" text-indent:2em;" align="left" class="td2"><span class="tdfont">拟稿人：</span><span class="suggestfont">测试</span></td>
                                </tr>
                            </table>
                     <table width="100%" height="80" border="0" cellspacing="0" cellpadding="0">
                      <tr>
                        <td width="20%" align="center" class="td2"><span class="tdfont">秘书部<br/>
                          门审核</span></td>
                        <td width="30%" align="left" class="td2"><div class="suggestfont">&nbsp;测试</div></td>
                        <td width="20%" align="center" class="td2"><span class="tdfont">有关部<br/>门审核</span></td>
                        <td width="30%" align="left" class="td3"><div class="suggestfont">&nbsp;测试</div></td>
                      </tr>
                    </table>
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr>
                        <td width="20%" height="40" align="center" class="td2"><span class="tdfont">主&nbsp;送</span></td>
                        <td width="80%" align="left" class="td3"><div class="suggestfont">&nbsp;测试</div></td>
                      </tr>
                      <tr>
                        <td width="20%" height="40" align="center" class="td2"><span class="tdfont">抄&nbsp;报</span></td>
                        <td width="80%" align="left" class="td3"><div class="suggestfont">&nbsp;测试</div></td>
                      </tr>
                      <tr>
                        <td width="20%" height="40" align="center" class="td2"><span class="tdfont">抄&nbsp;送</span></td>
                        <td width="80%" align="left" class="td3"><div class="suggestfont">&nbsp;测试</div></td>
                      </tr>
                      <tr>
                        <td width="20%" height="80" align="center" class="td2"><span class="tdfont">附&nbsp;件</span></td>
                        <td width="80%" align="left" class="td3"><div class="suggestfont">&nbsp;测试</div></td>
                      </tr>
                      <tr>
                        <td width="20%" height="40" align="center" class="td2"><span class="tdfont">主题词</span></td>
                        <td width="80%" align="left" class="td3"><div class="suggestfont">&nbsp;测试</div></td>
                      </tr>
                    </table>
                    <table width="100%" height="40" border="0" cellspacing="0" cellpadding="0">
                      <tr>
                        <td width="40%" align="left" class="td2" style=" text-indent:2em;"><span class="tdfont">文号：</span><span class="suggestfont">测试</span></td>
                        <td width="30%" align="left" class="td2" style=" text-indent:2em;"><span class="tdfont">密级：</span><span class="suggestfont">测试</span></td>
                        <td width="30%" align="left" class="td2" style=" text-indent:2em;"><span class="tdfont">缓急：</span><span class="suggestfont">测试</span></td>
                      </tr>
                    </table>
                    <table width="100%" height="40" border="0" cellspacing="0" cellpadding="0">
                      <tr>
                        <td width="70%" align="left" class="td2"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                          <tr>
                            <td style=" text-indent:2em;"><span class="tdfont">封发时间：</span></td>
                            <td>&nbsp;</td>
                            <td><span class="suggestfont">测试</span><span class="tdfont">年</span></td>
                            <td>&nbsp;</td>
                            <td><span class="suggestfont">测试</span><span class="tdfont">月</span></td>
                            <td>&nbsp;</td>
                            <td><span class="suggestfont">测试</span><span class="tdfont">日</span></td>
                          </tr>
                        </table></td>
                        <td width="30%" align="left" class="td2"  style=" text-indent:2em;"><span class="tdfont">份数：</span><span class="suggestfont">测试</span></td>
                      </tr>
                    </table>
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr>
                        <td width="50%" height="40" align="left" class="td4" style=" text-indent:2em;"><span class="tdfont">打字：</span><span class="suggestfont">测试</span></td>
                        <td style=" text-indent:2em;" width="50%" align="left"><span class="tdfont">校对：</span><span class="suggestfont">测试</span></td>
                      </tr>
                    </table>
                     
                      </td>
                      </tr>
                  </table></td>
                </tr>
              </table>
                  <SCRIPT type="text/javascript">tp1.addTabPage( document.getElementById( "tabPage1" ) );</SCRIPT>

                </DIV>
                <DIV class=tab-page id=tabPage2>
                  <H2 class=tab >
                    处理记录
                  </H2>
                  <SCRIPT type="text/javascript">tp1.addTabPage( document.getElementById( "tabPage2" ) );</SCRIPT>
                  测试2
                </DIV>
                <DIV class=tab-page id=tabPage3>
                  <H2 class=tab >
                    处理状态
                  </H2>
                  <SCRIPT type="text/javascript">tp1.addTabPage( document.getElementById( "tabPage3" ) );</SCRIPT>
                  测试3
                </div>
              </DIV>
            </td>
          </tr>
        </table>
      </form>
    </DIV>
  </body>

</html>
