package com.strongit.oa.infopub.statistic;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.infopub.articles.ArticlesManager;
import com.strongit.oa.infopub.column.ColumnManager;
import com.strongit.oa.util.UUIDGenerator;
import com.strongmvc.webapp.action.BaseActionSupport;

/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-5-11 
 * Autour: hull
 * Version: V1.0
 * Description：信息统计
 */
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "statistic.action", type = ServletActionRedirectResult.class) })
public class StatisticAction extends BaseActionSupport {
	
	private ArticlesManager articlesManager;//
	private List<ArticleStatistic> list;//统计信息List
	private String jpgtype;//图形类型
	private String columnId;//栏目ID
	private Date beginTime;//开始时间
	private Date endTime;//结束时间
	private int hpsize;//总页数
	private String columnName;
	private int pagenow;
	private static final  int pagesize=10;//
	private ColumnManager columnManager;//栏目DAO
	private String date;
	private String temp;
	private String temp1;
	


	public String getTemp1() {
		return temp1;
	}

	public void setTemp1(String temp1) {
		this.temp1 = temp1;
	}

	/***
	 * 统计信息列表
	 */
	@Override
	public String list() throws Exception {
	//	getResponse().setContentType("image/jpeg");
		list=articlesManager.getColumnArticleCount(beginTime,endTime);
		getRequest().setAttribute("beginTime", beginTime);
		getRequest().setAttribute("endTime",endTime);
		for(ArticleStatistic as:list){//循环信息列表过滤过长的标题
			if(as.getColumnName().length()>=20){
				as.setColumnName(as.getColumnName().substring(0,10)+"...");
			}
		}
		//清理文件夹
		   folder();
		// 1.生成绘图类对象
		
		     brawBar("文章统计柱形图","文章","文章统计");
		
			 brawPie();
	         getTimeDate();
	        
//	         Date date=new Date();
//	         
//	         temp=String.valueOf(date.getYear())+String.valueOf(date.getMonth())+String.valueOf(date.getDay())+String.valueOf(date.getHours())+String.valueOf(date.getMinutes())+String.valueOf(date.getSeconds());
		return SUCCESS;
	}
	
	/***
	 * 格式化时间
	 * @return
	 */
	private String getTimeDate(){
		Date time=new Date();
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss SS");
        date= format.format(time);
        return date;
	}
	
	/***
	 * 饼形图
	 */
	public void brawPie(){
		
		// 清除缓存
		getResponse().setHeader("cache-control", "no-cache");
		getResponse().setHeader("cache-control", "no-store");
		getResponse().setDateHeader("expires", 0);
		getResponse().setHeader("pragma", "no-cache");
		
		getResponse().setContentType("image/jpeg");
		 DrawPie brawpie=new DrawPie();//饼形图

			for(ArticleStatistic as:list){
				if(as.getColumnName()==null)
				{
					as.setColumnName("");
				}
				brawpie.addData(as.getColumnName(), as.getPubCount());
			}
			brawpie.init();
			brawpie.setBgcolor(Color.white);
			brawpie.setTitle("饼型统计图");
			brawpie.setWidth(500);
			brawpie.setHeight(400);
			brawpie.setLabelFontSize(10);
			brawpie.setFactor(0.2);
			//4.保存图片
			String url=getRequest().getContextPath();
			temp=String.valueOf((Math.random()*10));
			brawpie.saveAbs(this.getRequest().getRealPath("/")+"/img/"+temp+".jpg");

	}
	/**
	 * 画柱形图
	 */
	public void brawBar(String title,String xtitle,String ytitle){
		getResponse().setContentType("image/jpeg");
		
		 DrawBar brawbar = DrawBar.getInstance();//柱形图
	      
	     for(ArticleStatistic as:list){
	    	 if(as.getColumnName()==null)
				{
					as.setColumnName("");
				}
		     brawbar.addData(as.getColumnName(),as.getPubCount(),"文章");
	      }
	     brawbar.init();
	     brawbar.setTitle(title);
	     brawbar.setYTitle(ytitle);
	     brawbar.setXTitle(xtitle);
	     brawbar.setBgColor("white");
	     brawbar.setIsV(true);
	     brawbar.setWidth(500);
	     brawbar.setMargin(0.1);
	     brawbar.setHeight(350);
	     brawbar.setXFontSize(10);
	     brawbar.setYFontSize(10);
	     // 4.保存图片
	   
	     String url=getRequest().getContextPath();
			temp1=String.valueOf((Math.random()*10));
			while(true){
				if(temp1.equals(temp)){
					temp1=String.valueOf((Math.random()*10));
				}
				else{
					break;
				}
			}
	     brawbar.saveAbs(this.getRequest().getRealPath("/")+"/img/"+temp1+".jpg");
//	     try {
//		//	brawbar.writeWebChartAsJPEG(getResponse().getOutputStream());
//		} catch (IOException e) {
//			
//			e.printStackTrace();
//		}
	}
	/***
	 * 查询栏目下文章发布状态
	 * @return
	 */
	public String columnState()throws Exception{
		list=articlesManager.getStateArticles(columnId,beginTime,endTime);
		for(ArticleStatistic as:list){
			if("".equals(as.getColumnName())||as.getColumnName()==null){
				as.setColumnName("0");
			}
			
		}
		 List<ArticleStatistic> newlist=new ArrayList<ArticleStatistic>();
		    int sum=0;
		    for(ArticleStatistic ac:list){
		    	if("0".equals(ac.getColumnName())){
		    		ac.setColumnName("已废除");
		    		newlist.add(ac);
		    	}else if("2".equals(ac.getColumnName())){
		    		ac.setColumnName("未发布");
		    		newlist.add(ac);
		    	}else if("9".equals(ac.getColumnName())){
		    		ac.setColumnName("已发布");
		    		newlist.add(ac);
		    	}else{
		    		sum=sum+ac.getPubCount();
		    	}
		    }
		    ArticleStatistic asc=new ArticleStatistic();
		    asc.setColumnName("审核中");
		    asc.setPubCount(sum);
		    newlist.add(asc);
		    list.clear();
		    list=newlist;
		columnName=columnManager.getClumnName(columnId);
		//清理文件夹
		   folder();
		// 1.生成绘图类对象
		
	     brawBar("文章发布量柱形统计图","文章","文章发布数量");//柱形图
	
		 brawPie();//饼形图
		 getTimeDate();
		return "columnState";
		
	}
	
	/**
	 * 栏目下文章发布统计
	 * @return
	 * @throws Exception
	 */
	public String column()throws Exception{
		
		list=articlesManager.getStateArticles(columnId,beginTime,endTime);
		// 1.生成绘图类对象
	    List<ArticleStatistic> newlist=new ArrayList<ArticleStatistic>();
	    int sum=0;
	    for(ArticleStatistic ac:list){
	    	if("0".equals(ac.getColumnName())){
	    		ac.setColumnName("已废除");
	    		newlist.add(ac);
	    	}else if("2".equals(ac.getColumnName())){
	    		ac.setColumnName("未发布");
	    		newlist.add(ac);
	    	}else if("9".equals(ac.getColumnName())){
	    		ac.setColumnName("已发布");
	    		newlist.add(ac);
	    	}else{
	    		sum=sum+ac.getPubCount();
	    	}
	    }
	    ArticleStatistic asc=new ArticleStatistic();
	    asc.setColumnName("审核中");
	    asc.setPubCount(sum);
	    newlist.add(asc);
	    list.clear();
	    list=newlist;
	  //清理文件夹
		   folder();
		     brawBar("文章发布量柱形统计图","文章","文章发布数量");//柱形图
		
			 brawPie();//饼形图
			 getTimeDate();
		return "columnState";
	}
	/***
	 * 显示栏目列表
	 * @return
	 * @throws Exception
	 */
	public String statisticList() throws Exception{
		list=articlesManager.getColumnArticleCount(beginTime,endTime);
		StringBuffer str=new StringBuffer();
		str.append("<table border='1' width='80%'><td>栏目名称</td><td>文章数量</td>");
		for(ArticleStatistic as:list){
			if(as.getColumnName().length()>=20)
			{
				str.append("<tr><td width='50%'><a href='"+getRequest().getContextPath()+"/infopub/statistic/statistic!columnState.action?columnId="+as.getColumnId()+"'>"+as.getColumnName().substring(0,10)+"...</a></td><td>"+as.getPubCount()+"</td></tr>");
			}
			else{
				str.append("<tr><td width='50%'><a href='"+getRequest().getContextPath()+"/infopub/statistic/statistic!columnState.action?columnId="+as.getColumnId()+"'>"+as.getColumnName()+"</a></td><td>"+as.getPubCount()+"</td></tr>");
			}
			
		}
		str.append("</table>");
		return renderHtml(str.toString());
	}
	/**
	 * 统计发布信息
	 * @return
	 */
	public String statisticPub() throws Exception{
		list=articlesManager.getColumnState(beginTime, endTime);
		for(ArticleStatistic as:list){
			if(as.getColumnName().length()>=20){
				as.setColumnName(as.getColumnName().substring(0,10)+"...");
			}
		}
		//清理文件夹
		   folder();
		
		// 1.生成绘图类对象
		 brawBar("文章发布量柱形统计图","文章","文章发布数量");//柱形图
			
		 brawPie();//饼形图
		 getTimeDate();
		return "statisticPub";
	}
	/***
	 * 统计点击量
	 * @return
	 * @throws Exception
	 */
	public String statisticHits() throws Exception{
		list=articlesManager.getHitsArticles();
		for(ArticleStatistic as:list){
			if(as.getColumnName().length()>=20){
				as.setColumnName(as.getColumnName().substring(0,10)+"...");
			}
		}
		if(pagenow==0){
			pagenow=1;
		}
		hpsize=list.size()%pagesize==0?list.size()/pagesize:list.size()/pagesize+1;
		getRequest().setAttribute("hpsize",hpsize);
		getRequest().setAttribute("pagesize",pagesize);
		getRequest().setAttribute("pagenow",pagenow);
		// 1.生成绘图类对象
		if(jpgtype!=null&&"bar".equals(jpgtype))//判断显示图形
		{
		     getResponse().setContentType("image/jpeg");
			 DrawBar brawbar = DrawBar.getInstance();//柱形图
		      
		     for(ArticleStatistic as:list){
		    	 if(as.getColumnName()==null)
					{
						as.setColumnName("");
					}
			     brawbar.addData(as.getColumnName(),as.getPubCount(),"文章");
		      }
		     brawbar.init();
		     brawbar.setTitle("文章点击量柱形统计图");
		     brawbar.setYTitle("文章点击数量");
		     brawbar.setXTitle("文章");
		     brawbar.setBgColor("white");
		     brawbar.setIsV(true);
		     brawbar.setWidth(600);
		     brawbar.setMargin(0.1);
		     brawbar.setHeight(400);
		     brawbar.setXFontSize(10);
		     brawbar.setYFontSize(10);
		     // 4.保存图片
		     try {
				brawbar.writeWebChartAsJPEG(getResponse().getOutputStream());
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}else if("pie".equals(jpgtype)){
			 brawPie();
		}
		
		return "statisticHits";
	}
	
	/***
	 * 统计评论量
	 * @return
	 * @throws Exception
	 */
	public String statisticComment() throws Exception{
		list=articlesManager.getCommentArticles();
		for(ArticleStatistic as:list){//过滤过长的名称
			if(as.getColumnName().length()>=20){
				as.setColumnName(as.getColumnName().substring(0,10)+"...");
			}
		}
		//清理文件夹
		   folder();
		
		// 1.生成绘图类对象
		 brawBar("文章评论量柱形统计图","文章","文章评论数量");//柱形图
			
		 brawPie();//饼形图
		return "statisticComment";
	}
	/**
	 * 根据人员统计
	 * @return
	 * @throws Exception
	 */
	public String statisticUser() throws Exception{
		list=articlesManager.getUserArticles();
		//清理文件夹
		   folder();
		
		// 1.生成绘图类对象
		 brawBar("人员编辑量柱形统计图","文章","文章数量");//柱形图
			
		 brawPie();//饼形图
		 getTimeDate();
		return "statisticUser";
	}
	
	/***
	 * 判断文件夹里文件的数量
	 */
	private void folder(){
		
		String path = this.getRequest().getRealPath("/")+"/img/";
		int fileCount = 0;
		File d = new File(path);
		File list[] = d.listFiles();
		for(int i = 0; i < list.length; i++){
		    if(list[i].isFile()){
		        fileCount++;
		    }
		}
		if(fileCount>=10){
			delAllFile(this.getRequest().getRealPath("/")+"/img/");
		}

	}
	
    /***
     * 清楚文件夹里的文件
     * @param path
     * @return
     */
	public static boolean delAllFile(String path) {
	       boolean flag = false;
	       File file = new File(path);
	       if (!file.exists()) {
	         return flag;
	       }
	       if (!file.isDirectory()) {
	         return flag;
	       }
	       String[] tempList = file.list();
	       File temp = null;
	       for (int i = 0; i < tempList.length; i++) {
	          if (path.endsWith(File.separator)) {
	             temp = new File(path + tempList[i]);
	          } else {
	              temp = new File(path + File.separator + tempList[i]);
	          }
	          if (temp.isFile()) {
	             temp.delete();
	          }
	          if (temp.isDirectory()) {
	             delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
	            
	             flag = true;
	          }
	       }
	       return flag;
	     }
	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public String save() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getHpsize() {
		return hpsize;
	}

	public void setHpsize(int hpsize) {
		this.hpsize = hpsize;
	}

	
	public int getPagenow() {
		return pagenow;
	}

	public void setPagenow(int pagenow) {
		this.pagenow = pagenow;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	

	@Autowired
	public void setColumnManager(ColumnManager columnManager) {
		this.columnManager = columnManager;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getColumnId() {
		return columnId;
	}

	public void setColumnId(String columnId) {
		this.columnId = columnId;
	}

	public String getJpgtype() {
		return jpgtype;
	}

	public void setJpgtype(String jpgtype) {
		this.jpgtype = jpgtype;
	}

	public List<ArticleStatistic> getList() {
		return list;
	}

	public void setList(List<ArticleStatistic> list) {
		this.list = list;
	}

	@Autowired
	public void setArticlesManager(ArticlesManager articlesManager) {
		this.articlesManager = articlesManager;
	}

	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String input() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public String getTemp() {
		return temp;
	}

	public void setTemp(String temp) {
		this.temp = temp;
	}

}
