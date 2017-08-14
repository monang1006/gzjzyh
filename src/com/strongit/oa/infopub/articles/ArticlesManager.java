/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-2-12
 * Autour: qindh
 * Version: V1.0
 * Description： 信息发布manager
 */
package com.strongit.oa.infopub.articles;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import jxl.write.DateTime;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaInfopublishArticle;
import com.strongit.oa.bo.ToaInfopublishColumn;
import com.strongit.oa.bo.ToaInfopublishColumnArticl;
import com.strongit.oa.bo.ToaInfopublishComment;
import com.strongit.oa.bo.ToaMeetingTopic;
import com.strongit.oa.common.service.BaseManager;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.infopub.articlesappro.ArticlesApproManager;
import com.strongit.oa.infopub.statistic.ArticleStatistic;
import com.strongit.oa.meetingmanage.util.MeetingmanageConst;
import com.strongit.oa.search.SearchManager;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.OALogInfo;
import com.strongit.oa.webservice.iphone.server.pushNotify.PushNotifyManager;
import com.strongit.plugin.search.index.IIndesOperator;
import com.strongit.plugin.search.index.IndexFactory;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

@Service
@Transactional
public class ArticlesManager extends BaseManager{

	IUserService userService;
	
	private ArticlesApproManager appManager;

	public static final String YES = "1"; // 是
	public static final String NO = "0"; // 否
	public static final String ARTICLETYPE = "2"; // 稿件已上栏
	public static final String ISC = "1";// 状态上栏
	public static final String ISA = "2";// 审核
	public static final String ISP = "3";// 已发布
	public static final String ALL = "0"; // 全部
	public static final String DAODU = "1"; // 导读
	public static final String REDAIN = "2"; // 热点
	public static final String GUDIN = "3"; // 固顶
	public static final String PINR = "4"; // 评论
	/** articlesDAO* */
	private GenericDAOHibernate<ToaInfopublishArticle, String> articlesDAO;
	/** columnDAO* */
	private GenericDAOHibernate<ToaInfopublishColumn, String> columnDAO;
	/** articlesColumnDAO* */
	private GenericDAOHibernate<ToaInfopublishColumnArticl, String> articlesColumnDAO;
	/** commentsDAO* */
	private GenericDAOHibernate<ToaInfopublishComment, String> commentsDAO;

	// 全文检索
	private SearchManager searchManager;
	//推送给ios的manager类
	@Autowired
	private PushNotifyManager pushManager;

	/**
	 * 构造方法
	 * 
	 */
	public ArticlesManager() {
	}

	/**
	 * setSessionFactory
	 * 
	 * @param sessionFactory
	 */
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		articlesDAO = new GenericDAOHibernate<ToaInfopublishArticle, String>(
				sessionFactory, ToaInfopublishArticle.class);
		columnDAO = new GenericDAOHibernate<ToaInfopublishColumn, String>(
				sessionFactory, ToaInfopublishColumn.class);
		articlesColumnDAO = new GenericDAOHibernate<ToaInfopublishColumnArticl, String>(
				sessionFactory, ToaInfopublishColumnArticl.class);
		commentsDAO = new GenericDAOHibernate<ToaInfopublishComment, String>(
				sessionFactory, ToaInfopublishComment.class);
	}

	/**
	 * 获取未上栏稿件page
	 * 
	 * @param page
	 * @return 稿件的page对象
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<ToaInfopublishArticle> getAricesPages(
			Page<ToaInfopublishArticle> page,OALogInfo...infos) throws SystemException,
			ServiceException {
		String hql = "from ToaInfopublishArticle as t where t.articlesAticlestate='1' order by t.articlesCreatedate DESC ";// 1为稿件状态为未上栏
		return articlesDAO.find(page, hql);
	}

	/**
	 * 获取稿件BO
	 * 
	 * @param articlesId
	 *            稿件ID
	 * @return BO
	 */
	public ToaInfopublishArticle getArticle(String articlesId,OALogInfo...infos)
			throws SystemException, ServiceException {
		try {
			return articlesDAO.get(articlesId);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "信息发布稿件" });
		}

	}

	/**
	 * 根据新闻标题来查找所有符合的新闻，主要用于android
	 * @author hecj
	 * @date   2012-04-10 11:01
	 * @param  title
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<ToaInfopublishColumnArticl> getColumnArticleTitleList(String title)throws SystemException, ServiceException {
		Date date=new Date();
		String hql = "from ToaInfopublishColumnArticl as t where t.columnArticleState='9' "+
			" and (t.toaInfopublishArticle.articlesAutopublishtime is null or t.toaInfopublishArticle.articlesAutopublishtime<?)"+
			"  and (t.toaInfopublishArticle.articlesAutocancletime is null or t.toaInfopublishArticle.articlesAutocancletime>? )  " ;
		if(title!=null&&!"".equals(title)){
			hql+=" and t.toaInfopublishArticle.articlesTitle like "+"'%"+title+"%' ";
		}
		hql+="order by t.columnArticleIsstandtop DESC, t.columnArticleAddtime DESC ";
		try {
			
				return articlesColumnDAO.find(hql,date,date);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "信息发布列表" });
		}
	}
	
	/**
	 * 获取所有上栏的稿件,android使用
	 * @author hecj
	 * @date 2012-4-9 11:02
	 * @param page
	 * @param columnId 分组id
	 */
	public Page<ToaInfopublishColumnArticl> getAndroidColumnArticlPagesByProcess(
			Page<ToaInfopublishColumnArticl> page, String columnId,OALogInfo...infos)
			throws SystemException, ServiceException {
		String hql = "from ToaInfopublishColumnArticl t where t.columnArticleState ='5' "
				+ " or t.columnArticleState='9' "
				+"  order by  t.columnArticleIsstandtop DESC, t.columnArticleAddtime DESC";
		try {
			return articlesColumnDAO.find(page, hql);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "信息发布稿件" });
		}
	}
	
	/**
	 * 保存稿件
	 * 
	 * @param model
	 * @param columnId
	 * @param columnArticleId
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void saveArticle(ToaInfopublishArticle model, String columnId,
			String columnArticleId,OALogInfo...infos) throws SystemException, ServiceException {
		String type = null;
		if (model.getArticlesId() != null && !"".equals(model.getArticlesId())) {
			type = YES;
		}
		if(columnId != null && !"".equals(columnId)&&type!=null){
			model.setArticlesAticlestate(ARTICLETYPE);
		}
		articlesDAO.save(model);
		if (columnId != null && !"".equals(columnId)) {// 如果是已上栏稿件保存
			ToaInfopublishColumnArticl cabo = null;
			ToaInfopublishColumn cbo = null;
			if (type == null) {// 新建上栏稿件
				cabo = new ToaInfopublishColumnArticl();
				model.setArticlesAticlestate(ARTICLETYPE);
				cbo = columnDAO.get(columnId);
				cabo.setColumnArticleState(ARTICLETYPE);
				cabo.setColumnArticleIsstandtop(model.getArticlesIsstandtop());
				cabo.setColumnArticleGuidetype(model.getArticlesGuidetype());
				cabo.setColumnArticleAddtime(new Date());
				cabo.setColumnArticleAdduser(userService.getCurrentUser()
						.getUserName());
				cabo.setColumnArticleOldaticlestate(model
						.getArticlesAticlestate());
				cabo.setColumnArticleLatestchangtime(model
						.getArticlesLatestchangtime());
				cabo.setColumnArticleLatestuser(model.getArticlesLatestuser());
				cabo.setToaInfopublishArticle(model);
				cabo.setToaInfopublishColumn(cbo);
				try {
					articlesDAO.update(model);
					articlesColumnDAO.save(cabo);
					searchManager.saveIndex(cabo);
					/**
					 * 在这里增加推送
					 */
					/**
					 * 新增操作时往推送设置里面新增记录或者修改推送数
					 */
					if(pushManager.getPushState(userService.getCurrentUser().getUserId(),PushNotifyManager.PUSH_MODULE_NO_ARTICLES)){
						pushManager.saveNotity(userService.getCurrentUser().getUserId(),PushNotifyManager.PUSH_MODULE_NO_ARTICLES, 1);
					}
				} catch (ServiceException e) {
					throw new ServiceException(MessagesConst.save_error,
							new Object[] { "信息发布稿件" });
				}
			} else {// 修改上栏稿件
				cabo = articlesColumnDAO.get(columnArticleId);
				cabo.setColumnArticleIsstandtop(model.getArticlesIsstandtop());
				cabo.setColumnArticleGuidetype(model.getArticlesGuidetype());
				cabo.setColumnArticleLatestchangtime(new Date());
				cabo.setColumnArticleLatestuser(userService.getCurrentUser()
						.getUserName());
				try {
					articlesColumnDAO.update(cabo);
					searchManager.updetIndex(cabo);
				} catch (ServiceException e) {
					throw new ServiceException(MessagesConst.save_error,
							new Object[] { "信息发布稿件" });
				}
			}
		}

	}
	
	/**
	 * 保存发文归档的政务信息的稿件
	 * 
	 * @param model
	 * @param columnId
	 * @param columnArticleId
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void saveZWGKArticle(ToaInfopublishArticle model, String columnId,
			String columnArticleId,OALogInfo...infos) throws SystemException, ServiceException {
		String type = null;
		if (model.getArticlesId() != null && !"".equals(model.getArticlesId())) {
			type = YES;
		}
		if(columnId != null && !"".equals(columnId)&&type!=null){
			model.setArticlesAticlestate(ARTICLETYPE);
		}
		articlesDAO.save(model);
		if (columnId != null && !"".equals(columnId)) {// 如果是已上栏稿件保存
			ToaInfopublishColumnArticl cabo = null;
			ToaInfopublishColumn cbo = null;
			if (type == null) {// 新建政务信息上栏稿件
				cabo = new ToaInfopublishColumnArticl();
				model.setArticlesAticlestate(ARTICLETYPE);
				cbo = columnDAO.get(columnId);
				cabo.setColumnArticleState("9");//发文归档的信息存入政务信息就设置为发布的状态
				cabo.setColumnArticleIsstandtop(model.getArticlesIsstandtop());
				cabo.setColumnArticleGuidetype(model.getArticlesGuidetype());
				cabo.setColumnArticleAddtime(new Date());
				cabo.setColumnArticleAdduser(userService.getCurrentUser()
						.getUserName());
				cabo.setColumnArticleOldaticlestate(model
						.getArticlesAticlestate());
				cabo.setColumnArticleLatestchangtime(model
						.getArticlesLatestchangtime());
				cabo.setColumnArticleLatestuser(model.getArticlesLatestuser());
				cabo.setToaInfopublishArticle(model);
				cabo.setToaInfopublishColumn(cbo);
				cabo.setColumnArticleLatestchangtime(new Date());
				try {
					articlesDAO.update(model);
					articlesColumnDAO.save(cabo);
					searchManager.saveIndex(cabo);
				} catch (ServiceException e) {
					throw new ServiceException(MessagesConst.save_error,
							new Object[] { "信息发布稿件" });
				}
			} else {// 修改上栏稿件
				cabo = articlesColumnDAO.get(columnArticleId);
				cabo.setColumnArticleIsstandtop(model.getArticlesIsstandtop());
				cabo.setColumnArticleGuidetype(model.getArticlesGuidetype());
				cabo.setColumnArticleLatestchangtime(new Date());
				cabo.setColumnArticleLatestuser(userService.getCurrentUser()
						.getUserName());
				try {
					articlesColumnDAO.update(cabo);
					searchManager.updetIndex(cabo);
				} catch (ServiceException e) {
					throw new ServiceException(MessagesConst.save_error,
							new Object[] { "信息发布稿件" });
				}
			}
		}

	}

	/**
	 * 获取刚刚添加稿件ID
	 * 
	 * @return
	 */
	public String getArticleByAdd(String name,OALogInfo...infos) throws SystemException,
			ServiceException {
		try {
			String sql = "from ToaInfopublishArticle as t where t.articlesTitle=? order by articlesLatestchangtime DESC";
			List<ToaInfopublishArticle> list = articlesDAO.find(sql, name);
			return list.get(0).getArticlesId();
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.save_error,
					new Object[] { "获取刚添加的稿件ID" });
		}

	}

	/**
	 * 保存稿件
	 * 
	 * @param model
	 */
	public void saveArticle(ToaInfopublishArticle model,OALogInfo...infos)
			throws SystemException, ServiceException {
		try {
			articlesDAO.save(model);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.save_error,
					new Object[] { "信息发布稿件" });
		}
	}

	/**
	 * 逻辑删除稿件,将Aticlestate改为0
	 * 
	 * @param articlesId
	 *            稿件ID
	 */
	public void deleteOrgs(String articlesId ,OALogInfo...infos) throws SystemException,
			ServiceException {
		String[] ids = articlesId.split(",");
		for (int i = 0; i < ids.length; i++) {
			ToaInfopublishArticle boOrg = articlesDAO.get(ids[i]);
			boOrg.setArticlesAticlestate(NO);
			try {
				articlesDAO.update(boOrg);
			} catch (ServiceException e) {
				throw new ServiceException(MessagesConst.del_error,
						new Object[] { "信息发布稿件" });
			}
		}
	}

	/**
	 * 稿件上栏
	 * 
	 * @param clumnId
	 *            栏目ID
	 * @param articlesId
	 *            稿件ID
	 */
	public void setArticlesClumn(String clumnsId, String articlesId,OALogInfo...infos)
	 throws SystemException, ServiceException {
		IIndesOperator indexFactory = IndexFactory.getInstance().getOperator();
		if(clumnsId!=null&&clumnsId!=""){
			//删除该信息原有上拦记录
			String[] clumnIds = clumnsId.split(",");
			String[] articlesIds = articlesId.split(",");
			ToaInfopublishColumnArticl cabo;
			for (int i = 0; i < articlesIds.length; i++) {
				this.deleteColumnArticlByID(articlesIds[i]);
				for (int j = 0; j < clumnIds.length; j++) {
					cabo = new ToaInfopublishColumnArticl();
					ToaInfopublishArticle abo = articlesDAO.get(articlesIds[i]);
					abo.setArticlesAticlestate(ARTICLETYPE);
					ToaInfopublishColumn cbo = columnDAO.get(clumnIds[j]);
					cabo.setColumnArticleState(abo.getArticlesAticlestate());
					cabo.setColumnArticleIsstandtop(abo.getArticlesIsstandtop());
					cabo.setColumnArticleGuidetype(abo.getArticlesGuidetype());
					cabo.setColumnArticleAddtime(new Date());
					cabo.setColumnArticleAdduser(userService.getCurrentUser()
							.getUserName());
					cabo.setColumnArticleOldaticlestate(abo
							.getArticlesAticlestate());
					cabo.setColumnArticleLatestchangtime(abo
							.getArticlesLatestchangtime());
					cabo.setColumnArticleLatestuser(abo.getArticlesLatestuser());
					cabo.setToaInfopublishArticle(abo);
					cabo.setToaInfopublishColumn(cbo);
					try {
						articlesDAO.update(abo);
						articlesColumnDAO.save(cabo);
						searchManager.saveIndex(cabo);// 以下为增加全文条检索
					} catch (ServiceException e) {
						throw new ServiceException(MessagesConst.save_error,
								new Object[] { "信息发布稿件" });
					}

				}
			}
		}
	}

	
	@Autowired
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	/**
	 * 获取栏目下稿件
	 * 
	 * @param page
	 * @param columnId
	 *            栏目ID
	 * @return
	 */
	public Page<ToaInfopublishColumnArticl> getColumnArticlPages(
			Page<ToaInfopublishColumnArticl> page, String columnId,OALogInfo...infos)
			throws SystemException, ServiceException {
		String hql = "from ToaInfopublishColumnArticl as t where t.toaInfopublishColumn.clumnId=? and t.columnArticleState <> '0' "+
		       " and t.columnArticleState<>'9'"
				+ " and t.columnArticleState <> '1' order by t.columnArticleState DESC, t.columnArticleIsstandtop DESC, t.columnArticleAddtime DESC";
		try {
			return articlesColumnDAO.find(page, hql, columnId);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "信息发布稿件" });
		}
	}
	
	
	/**
	* @method getColumnArticlsBycId
	* @author 申仪玲
	* @created 2011-12-2 上午10:05:37
	* @description 通过栏目ID获取栏目文章列表
	* @return Page<ToaInfopublishColumnArticl> 返回类型
	*/
	public Page<ToaInfopublishColumnArticl> getColumnArticlsBycId(
			Page<ToaInfopublishColumnArticl> page, String columnId,OALogInfo...infos)
			throws SystemException, ServiceException {
		StringBuilder hql=new StringBuilder(" from ToaInfopublishColumnArticl as t where 1=1");
		if(columnId!=null && !"".equals(columnId)){
			hql.append("  and t.toaInfopublishColumn.clumnId=?");
		}
		hql.append("and t.columnArticleState <> '0'  order by t.columnArticleState, t.columnArticleIsstandtop DESC, t.columnArticleAddtime DESC");
//		String hql = "from ToaInfopublishColumnArticl as t where t.toaInfopublishColumn.clumnId=? and t.columnArticleState <> '0' "
//				+ " order by t.columnArticleState, t.columnArticleIsstandtop DESC, t.columnArticleAddtime DESC";
		try {
			if(columnId!=null && !"".equals(columnId)){
				return articlesColumnDAO.find(page, hql.toString(), columnId);
			}else{
				return articlesColumnDAO.find(page, hql.toString());
			}
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "信息发布稿件" });
		}
	}
	
	/**
	 * 获取栏目下走完工作流和不要走工作流的稿件
	 * 
	 * @param page
	 * @param columnId
	 *            栏目ID
	 * @return
	 */
	public Page<ToaInfopublishColumnArticl> getColumnArticlPagesByState(
			Page<ToaInfopublishColumnArticl> page, String columnId,OALogInfo...infos)
			throws SystemException, ServiceException {
		String hql = "from ToaInfopublishColumnArticl t where t.toaInfopublishColumn.clumnId=? and t.columnArticleState <> '0' "
				+ " and t.columnArticleState <> '1'  and t.columnArticleState<>'3' and t.columnArticleState<>'4'  "
				+"  order by  t.columnArticleIsstandtop DESC, t.columnArticleAddtime DESC";
		try {
			return articlesColumnDAO.find(page, hql, columnId);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "信息发布稿件" });
		}
	}
	/**
	 * 获取栏目下走完工作流的稿件
	 * 
	 * @param page
	 * @param columnId
	 *            栏目ID
	 * @return
	 */
	public Page<ToaInfopublishColumnArticl> getColumnArticlPagesByProcess(
			Page<ToaInfopublishColumnArticl> page, String columnId,OALogInfo...infos)
			throws SystemException, ServiceException {
		String hql = "from ToaInfopublishColumnArticl t where t.toaInfopublishColumn.clumnId=? and (t.columnArticleState ='5' "
				+ " or t.columnArticleState='9' ) "
				+"  order by  t.columnArticleIsstandtop DESC, t.columnArticleAddtime DESC";
		try {
			return articlesColumnDAO.find(page, hql, columnId);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "信息发布稿件" });
		}
	}

	/**
	 * 查看栏目下稿件
	 * 
	 * @param page
	 * @param columnId
	 *            栏目ID
	 * @return
	 */
	public Page<ToaInfopublishColumnArticl> getColumnArticlPagesVeiw(
			Page<ToaInfopublishColumnArticl> page, String articlesTitle,
			String articlesAuthor, String hits,String disLogo,String ArcticlesType,OALogInfo...infos)
			throws SystemException, ServiceException {
		List list = new ArrayList();
	
//		String hql = "from ToaInfopublishColumnArticl as t where t.toaInfopublishColumn.clumnId=? and t.columnArticleState = '9' "+
//		    " and (t.toaInfopublishArticle.articlesAutopublishtime is null or t.toaInfopublishArticle.articlesAutopublishtime<=?)"+
//		"  and (t.toaInfopublishArticle.articlesAutocancletime is null or t.toaInfopublishArticle.articlesAutocancletime>=? ) order by  t.columnArticleIsstandtop DESC, t.columnArticleAddtime DESC ";
		String hql = "";
		
		if (ALL.equals(disLogo)) {
			hql = "from ToaInfopublishColumnArticl t where 1=1 ";
		} else if (DAODU.equals(disLogo)) {
			hql = "from ToaInfopublishColumnArticl t where t.columnArticleGuidetype = '1' ";
		} else if (REDAIN.equals(disLogo)) {
			hql = "from ToaInfopublishColumnArticl t where t.toaInfopublishArticle.articlesIshot = '1'  ";
		} else if (GUDIN.equals(disLogo)) {
			hql = "from ToaInfopublishColumnArticl t where t.columnArticleIsstandtop = '1' ";
		} else if (PINR.equals(disLogo)) {
			hql = "from ToaInfopublishColumnArticl t where t.toaInfopublishArticle.articlesIscancomment = '1' ";
		}else{
			hql = "from ToaInfopublishColumnArticl as t where  t.columnArticleState = '9'";
		}
		// 稿件名称
		if (articlesTitle != null && !"".equals(articlesTitle)
				&& !"null".equals(articlesTitle)) {
			hql += " and t.toaInfopublishArticle.articlesTitle like ?";
			list.add("%" + articlesTitle + "%");
		}
		// 稿件作者
		if (articlesAuthor != null && !"".equals(articlesAuthor)
				&& !"null".equals(articlesAuthor)) {
			hql += " and t.toaInfopublishArticle.articlesAuthor like ?";
			list.add("%" + articlesAuthor + "%");
		}
		// 栏目名称
		if (hits != null && !"".equals(hits) && !"null".equals(hits)) {
			hql += " and t.toaInfopublishArticle.articlesHits=?";
			list.add(Long.parseLong(hits));
		} 
	
		hql = hql
		+ " and t.columnArticleState = '9' order by t.toaInfopublishArticle.articlesAutopublishtime desc "; //按发布时间的降序排序
		Object[] param = list.toArray();
		try {
			return articlesColumnDAO.find(page,hql,param);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "信息发布稿件" });
		}
	}

	/**
	 * 获取以上栏的稿件BO
	 * 
	 * @param columnArticleId
	 *            已上栏稿件ID
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public ToaInfopublishColumnArticl getColumnArticl(String columnArticleId,OALogInfo...infos)
			throws SystemException, ServiceException {
		try {
			return articlesColumnDAO.get(columnArticleId);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "信息发布稿件" });
		}
	}

	/**
	 * 逻辑删除已上栏稿件
	 * 
	 * @param columnArticleId
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void colAtrdelete(String columnArticleId,OALogInfo...infos) throws SystemException,
			ServiceException {
		ToaInfopublishColumnArticl bo = articlesColumnDAO.get(columnArticleId);
		bo.setColumnArticleOldaticlestate(bo.getColumnArticleState());
		bo.setColumnArticleState(NO);// 删除
		bo.setColumnArticleRemovetime(new Date());
		bo.setColumnArticleRemoveuser(userService.getCurrentUser()
				.getUserName());
		try {
			articlesColumnDAO.update(bo);
			searchManager.updetIndex(bo);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.save_error,
					new Object[] { "信息发布稿件" });
		}

	}

	/**
	 * 获取已删除page
	 * 
	 * @param page
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page getRecPages(Page page,OALogInfo...infos) throws SystemException, ServiceException {
		String hql = " from ToaInfopublishColumnArticl as t where t.columnArticleState='0' order by t.columnArticleRemovetime DESC ";
		try {
			return articlesColumnDAO.find(page, hql);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "删除稿件" });
		}
	}
	public Page getRecPagesByUserId(Page page,String userId ,OALogInfo...infos) throws SystemException, ServiceException {
		String hql = "select t from ToaInfopublishColumnArticl t,ToaInfopublishColumnPrivil m where t.toaInfopublishColumn.clumnId=m.toaInfopublishColumn.clumnId" +
				" and m.columnPrivilUserid = '"+userId+"' and m.columnPrivilType ='1' and t.columnArticleState='0' order by t.columnArticleLatestchangtime DESC ";
		try {
			return articlesColumnDAO.find(page, hql);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "删除稿件" });
		}
	}

	/*
	 * 还原已删除的稿件
	 * 
	 * @param articlesId
	 *            稿件ID
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void revert(String columnArticleId,OALogInfo...infos) throws SystemException,
			ServiceException {
		String[] id = columnArticleId.split(",");
		for (int i = 0; i < id.length; i++) {
			ToaInfopublishColumnArticl abo = articlesColumnDAO.get(id[i]);
			if(abo.getColumnArticleOldaticlestate().equals("9")){
				abo.setColumnArticleState("2");
			}else{
				abo.setColumnArticleState(abo.getColumnArticleOldaticlestate());
			}
			try {
				articlesColumnDAO.update(abo);
				searchManager.updetIndex(abo);
			} catch (ServiceException e) {
				throw new ServiceException(MessagesConst.save_error,
						new Object[] { "信息发布稿件" });
			}
		}

	}

	/**
	 * 彻底删除稿件
	 * 
	 * @param articlesId
	 *            稿件ID
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void truedelete(String articlesId,OALogInfo...infos) throws SystemException,
			ServiceException {
		String[] id = articlesId.split(",");
		try {
			for (int i = 0; id.length > i; i++) {
				articlesColumnDAO.delete(id[i]);
				searchManager.delIndex(id[i]);
			}
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.del_error,
					new Object[] { "信息发布稿件" });
		}
	}

	/**
	 * 清空所以稿件
	 * 
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void clear(OALogInfo...infos) throws SystemException, ServiceException {
		try {
			//String hql="delete from ToaInfopublishColumnArticl  where columnArticleState = '0' ";
			String hql = "FROM ToaInfopublishColumnArticl where columnArticleState = '0' ";
			List lst = articlesDAO.find(hql);
			articlesDAO.delete(lst);
			
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.del_error,
					new Object[] { "信息发布稿件" });
		}
	}

	/**
	 * 获取上栏的栏目下文件 前台显示
	 * 
	 * @param columnId
	 *            栏目ID
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<ToaInfopublishColumnArticl> getColumnArticleList(Page<ToaInfopublishColumnArticl> page,String columnId,OALogInfo...infos)
			throws SystemException, ServiceException {
		Date date=new Date();
		StringBuilder hql = new StringBuilder("from ToaInfopublishColumnArticl as t where t.columnArticleState='9' "+
		" and (t.toaInfopublishArticle.articlesAutopublishtime is null or t.toaInfopublishArticle.articlesAutopublishtime<?)"+
	"  and (t.toaInfopublishArticle.articlesAutocancletime is null or t.toaInfopublishArticle.articlesAutocancletime>? )  ");
		try {
			if(!"".equals(columnId)&&columnId!=null){
				hql.append(" and t.toaInfopublishColumn.clumnId=? ");
				hql.append(" order by t.columnArticleIsstandtop DESC, t.columnArticleAddtime DESC");
				return articlesColumnDAO.find(page,hql.toString(),date,date, columnId);
			}else{
				hql.append(" order by t.columnArticleIsstandtop DESC, t.columnArticleAddtime DESC");
				return articlesColumnDAO.find(page,hql.toString(),date,date);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "信息发布稿件" });
		}
	}
	
	/**
	 * 获取上栏的栏目以及其子栏目下文件 前台显示
	 * 
	 * @param columnId
	 *            栏目ID
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<ToaInfopublishColumnArticl> getAllColumnArticleList(Page<ToaInfopublishColumnArticl> page,String columnId,OALogInfo...infos)
			throws SystemException, ServiceException {
		Date date=new Date();
		StringBuilder hql = new StringBuilder("from ToaInfopublishColumnArticl as t where t.columnArticleState='9' "+
		" and (t.toaInfopublishArticle.articlesAutopublishtime is null or t.toaInfopublishArticle.articlesAutopublishtime<?)"+
	"  and (t.toaInfopublishArticle.articlesAutocancletime is null or t.toaInfopublishArticle.articlesAutocancletime>? )  ");
		try {
			if(!"".equals(columnId)&&columnId!=null){
				hql.append(" and t.toaInfopublishColumn.clumnId in (").append(columnId).append(") ");
				hql.append(" order by t.columnArticleIsstandtop DESC, t.columnArticleAddtime DESC");
				return articlesColumnDAO.find(page,hql.toString(),date,date);
			}else{
				hql.append(" order by t.columnArticleIsstandtop DESC, t.columnArticleAddtime DESC");
				return articlesColumnDAO.find(page,hql.toString(),date,date);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "信息发布稿件" });
		}
	}
	
	
	/**
	 * 只查询标题和时间的属于每日要情栏目下文件 前台显示
	 * 
	 * @param columnId
	 *            栏目ID
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<ToaInfopublishColumnArticl> getYQColumnArticleList(Page<ToaInfopublishColumnArticl> page,String columnId,OALogInfo...infos)
			throws SystemException, ServiceException {
		Date date=new Date();
		StringBuilder hql = new StringBuilder("select t.toaInfopublishArticle.articlesIssId,t.toaInfopublishArticle.articlesTitle,t.toaInfopublishArticle.articlesCreatedate from ToaInfopublishColumnArticl as t where t.columnArticleState='9' "+
		" and (t.toaInfopublishArticle.articlesAutopublishtime is null or t.toaInfopublishArticle.articlesAutopublishtime<?)"+
	"  and (t.toaInfopublishArticle.articlesAutocancletime is null or t.toaInfopublishArticle.articlesAutocancletime>? )  ");
		try {
			if(!"".equals(columnId)&&columnId!=null){
				hql.append(" and t.toaInfopublishColumn.clumnId in (").append(columnId).append(") ");
				hql.append(" order by t.columnArticleIsstandtop DESC, t.columnArticleAddtime DESC");
				return articlesColumnDAO.find(page,hql.toString(),date,date);
			}else{
				hql.append(" order by t.columnArticleIsstandtop DESC, t.columnArticleAddtime DESC");
				return articlesColumnDAO.find(page,hql.toString(),date,date);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "信息发布稿件" });
		}
	}

	/**
	 * 获取评论信息
	 * 
	 * @param articlesId
	 *            稿件ID
	 * @return
	 */
	public List<ToaInfopublishComment> getComments(String articlesId,OALogInfo...infos)
			throws SystemException, ServiceException {
		String hql = "from ToaInfopublishComment as t where t.toaInfopublishArticle.articlesId=? order by t.commentAddtime DESC ";
		try {
			return commentsDAO.find(hql, new Object[] { articlesId });
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "信息发布评论" });
		}
	}

	/**
	 * 保存评论
	 * 
	 * @param articlesId
	 *            稿件ID
	 * @param commentText
	 * @param commentName
	 */
	public void comments(String articlesId, String commentText,
			String commentName,OALogInfo...infos) throws SystemException, ServiceException {
		ToaInfopublishComment bo = new ToaInfopublishComment();
		bo.setCommentId(null);
		if (commentName == null || "".equals(commentName)) {
			bo.setCommentTitle("评论");
		} else {
			bo.setCommentTitle(commentName);
		}
		bo.setCommentUser(userService.getCurrentUser().getUserName());
		bo.setCommentUserip("");
		bo.setCommentContent(commentText);
		bo.setCommentAddtime(new Date());
		bo.setToaInfopublishArticle(articlesDAO.get(articlesId));
		commentsDAO.save(bo);
	}

	/**
	 * 删除评论
	 * 
	 * @param id
	 */
	public void deleteComment(String id,OALogInfo...infos) {
		commentsDAO.delete(id);
	}

	/**
	 * 更新ToaInfopublishColumnArticl
	 * 
	 * @param ca
	 *            ToaInfopublishColumnArticl对象
	 */
	public void updataColumnArtile(ToaInfopublishColumnArticl ca,OALogInfo...infos)
			throws SystemException, ServiceException {
		articlesColumnDAO.update(ca);
		searchManager.updetIndex(ca);
	}

	/**
	 * 查询
	 * 
	 * @param page
	 * @param articlesTitle
	 * @param articlesAuthor
	 * @param articlesLatestuser
	 * @param articlesLatestchangtime
	 * @param disLogo
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<ToaInfopublishArticle> getSelectAricesPages(
			Page<ToaInfopublishArticle> page, String articlesTitle,
			String articlesAuthor, String articlesLatestuser,
			Date articlesLatestchangtime, String disLogo,OALogInfo...infos)
			throws SystemException, ServiceException {
		Object[] obj = new Object[4];
		int i = 0;
		String hql = "";
		if (ALL.equals(disLogo)) {
			hql = "from ToaInfopublishArticle t where 1=1 ";
		} else if (DAODU.equals(disLogo)) {
			hql = "from ToaInfopublishArticle t where t.articlesGuidetype = '1' ";
		} else if (REDAIN.equals(disLogo)) {
			hql = "from ToaInfopublishArticle t where t.articlesIshot = '1'  ";
		} else if (GUDIN.equals(disLogo)) {
			hql = "from ToaInfopublishArticle t where t.articlesIsstandtop = '1' ";
		} else if (PINR.equals(disLogo)) {
			hql = "from ToaInfopublishArticle t where t.articlesIscancomment = '1' ";
		}
		// 稿件名称
		if (articlesTitle != null && !"".equals(articlesTitle)
				&& !"null".equals(articlesTitle)) {
			hql += " and t.articlesTitle like ?";
			obj[i] = "%" + articlesTitle + "%";
			i++;
		}
		// 稿件作者
		if (articlesAuthor != null && !"".equals(articlesAuthor)
				&& !"null".equals(articlesAuthor)) {
			hql += " and t.articlesAuthor like ?";
			obj[i] = "%" + articlesAuthor + "%";
			i++;
		}
		// 修改人
		if (articlesLatestuser != null && !"".equals(articlesLatestuser)
				&& !"null".equals(articlesLatestuser)) {
			hql += " and t.articlesLatestuser like ?";
			obj[i] = "%" + articlesLatestuser + "%";
			i++;
		}
		// 时间
		if (articlesLatestchangtime != null
				&& !"".equals(articlesLatestchangtime)
				&& !"null".equals(articlesLatestchangtime)) {
			hql += " and t.articlesLatestchangtime >=?";
			obj[i] = articlesLatestchangtime;
			i++;
		}
		hql = hql
				+ " and t.articlesAticlestate='1' order by t.articlesCreatedate DESC ";
		Object[] param = new Object[i];
		for (int k = 0, t = 0; k < obj.length; k++) {
			if (obj[k] != null) {
				param[t] = obj[k];
				t++;
			}
		}
		return page = articlesDAO.find(page, hql, param);
	}

	/**
	 * 财富库查询上栏稿件
	 * 
	 * @param page
	 * @param model
	 * @param disLogo
	 * @param arcticlesType
	 * @param columnId
	 * @return
	 */
	public Page<ToaInfopublishColumnArticl> getSelectColumnAricesPagesWealth(
			Page<ToaInfopublishColumnArticl> page, String articlesTitle,
			String articlesAuthor, String hits,
			Date columnArticleLatestchangtime, String disLogo,
			String arcticlesType, String columnId,OALogInfo...infos) throws SystemException,
			ServiceException {
		Object[] obj = new Object[7];
		int i = 0;
		String hql = "";
		if (ALL.equals(disLogo)) {
			hql = "from ToaInfopublishColumnArticl t where 1=1 ";
		} else if (DAODU.equals(disLogo)) {
			hql = "from ToaInfopublishColumnArticl t where t.columnArticleGuidetype = '1' ";
		} else if (REDAIN.equals(disLogo)) {
			hql = "from ToaInfopublishColumnArticl t where t.toaInfopublishArticle.articlesIshot = '1'  ";
		} else if (GUDIN.equals(disLogo)) {
			hql = "from ToaInfopublishColumnArticl t where t.columnArticleIsstandtop = '1' ";
		} else if (PINR.equals(disLogo)) {
			hql = "from ToaInfopublishColumnArticl t where t.toaInfopublishArticle.articlesIscancomment = '1' ";
		}
		// 稿件名称
		if (articlesTitle != null && !"".equals(articlesTitle)
				&& !"null".equals(articlesTitle)) {
			hql += " and t.toaInfopublishArticle.articlesTitle like ?";
			obj[i] = "%" + articlesTitle + "%";
			i++;
		}
		// 稿件作者
		if (articlesAuthor != null && !"".equals(articlesAuthor)
				&& !"null".equals(articlesAuthor)) {
			hql += " and t.toaInfopublishArticle.articlesAuthor like ?";
			obj[i] = "%" + articlesAuthor + "%";
			i++;
		}
		// 点击次数
		if (hits != null && !"".equals(hits) && !"null".equals(hits)) {
			hql += " and t.toaInfopublishArticle.articlesHits=?";
			obj[i] = Integer.parseInt(hits);
			i++;
		} 
		if(columnId!=null &&!"".equals(columnId)){
				hql += " and t.toaInfopublishColumn.clumnId=? ";
				obj[i] = columnId;
				i++;
		}
		// 稿件状态
		if (ALL.equals(arcticlesType)) {// 全部
			hql += " and t.columnArticleState <> '0' and t.columnArticleState <> '1' ";
		} else if (ISC.equals(arcticlesType)) {// 已上栏
			hql += " and t.columnArticleState = '2' ";
		} else if (ISA.equals(arcticlesType)) {// 审核中
			hql += " and t.columnArticleState = '3' ";
		} else if(ISP.equals(arcticlesType)){
			hql+=" and t.columnArticleState = '9' ";
		}
		// 修改人
		// if(model.getColumnArticleLatestuser()!=null&&!"".equals(model.getColumnArticleLatestuser())&&!"null".equals(model.getColumnArticleLatestuser())){
		// hql+=" and t.columnArticleLatestuser like ?";
		// obj[i]="%"+model.getColumnArticleLatestuser()+"%";
		// i++;
		// }
		// 修改时间
		if (columnArticleLatestchangtime != null
				&& !"".equals(columnArticleLatestchangtime)
				&& !"null".equals(columnArticleLatestchangtime)) {
				Date sdate=columnArticleLatestchangtime;	//开始时间
				Calendar cal=Calendar.getInstance();
				cal.setTime(columnArticleLatestchangtime);//结束时间
				cal.set(Calendar.HOUR_OF_DAY, 23);
				cal.set(Calendar.MINUTE, 59);
				cal.set(Calendar.SECOND, 59);
				Date edate=cal.getTime();
			hql += " and t.columnArticleLatestchangtime >? and t.columnArticleLatestchangtime<?  ";
			obj[i] = sdate;
			i++;
			obj[i] = edate;
			i++;
		}
		hql = hql
				+ "order by t.columnArticleState, t.columnArticleIsstandtop DESC, t.columnArticleAddtime DESC";
		Object[] param = new Object[i];
		for (int k = 0, t = 0; k < obj.length; k++) {
			if (obj[k] != null) {
				param[t] = obj[k];
				t++;
			}
		}
		return page = articlesColumnDAO.find(page, hql, param);
	}

	/**
	 * 查询
	 * 
	 * @param page
	 * @param articlesTitle
	 * @param articlesAuthor
	 * @param clumnName
	 * @param columnArticleLatestchangtime
	 * @param disLogo
	 * @param arcticlesType
	 * @param columnId
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<ToaInfopublishColumnArticl> getSelectColumnAricesPages(
			Page<ToaInfopublishColumnArticl> page, String articlesTitle,
			String articlesAuthor, String clumnName,
			Date columnArticleLatestchangtime, String disLogo,
			String arcticlesType, String columnId,OALogInfo...infos) throws SystemException,
			ServiceException {
		Object[] obj = new Object[6];
		int i = 0;
		String hql = "";
		if (ALL.equals(disLogo)) {
			hql = "from ToaInfopublishColumnArticl t where 1=1 ";
		} else if (DAODU.equals(disLogo)) {
			hql = "from ToaInfopublishColumnArticl t where t.columnArticleGuidetype = '1' ";
		} else if (REDAIN.equals(disLogo)) {
			hql = "from ToaInfopublishColumnArticl t where t.toaInfopublishArticle.articlesIshot = '1'  ";
		} else if (GUDIN.equals(disLogo)) {
			hql = "from ToaInfopublishColumnArticl t where t.columnArticleIsstandtop = '1' ";
		} else if (PINR.equals(disLogo)) {
			hql = "from ToaInfopublishColumnArticl t where t.toaInfopublishArticle.articlesIscancomment = '1' ";
		}
		// 稿件名称
		if (articlesTitle != null && !"".equals(articlesTitle)
				&& !"null".equals(articlesTitle)) {
			hql += " and t.toaInfopublishArticle.articlesTitle like ?";
			obj[i] = "%" + articlesTitle + "%";
			i++;
		}
		// 稿件作者
		if (articlesAuthor != null && !"".equals(articlesAuthor)
				&& !"null".equals(articlesAuthor)) {
			hql += " and t.toaInfopublishArticle.articlesAuthor like ?";
			obj[i] = "%" + articlesAuthor + "%";
			i++;
		}
		// 栏目名称
		if (clumnName != null && !"".equals(clumnName)
				&& !"null".equals(clumnName)) {
			hql += " and t.toaInfopublishColumn.clumnName like ?";
			obj[i] = "%" + clumnName + "%";
			i++;
		} else {
			hql += " and t.toaInfopublishColumn.clumnId=? ";
			obj[i] = columnId;
			i++;
		}
		// 稿件状态
		if (ALL.equals(arcticlesType)) {// 全部
			hql += " and t.columnArticleState <> '0' and t.columnArticleState <> '1' and t.columnArticleState<>'9'";
		} else if (ISC.equals(arcticlesType)) {// 已上栏
			hql += " and t.columnArticleState = '2' ";
		} else if (ISA.equals(arcticlesType)) {// 审核中
			hql += " and t.columnArticleState = '3' ";
		} else if (ISP.equals(arcticlesType)) {// 已发布
			hql += " and t.columnArticleState = '9' ";
		}
		// 修改人
		// if(model.getColumnArticleLatestuser()!=null&&!"".equals(model.getColumnArticleLatestuser())&&!"null".equals(model.getColumnArticleLatestuser())){
		// hql+=" and t.columnArticleLatestuser like ?";
		// obj[i]="%"+model.getColumnArticleLatestuser()+"%";
		// i++;
		// }
		// 修改时间
		if (columnArticleLatestchangtime != null
				&& !"".equals(columnArticleLatestchangtime)
				&& !"null".equals(columnArticleLatestchangtime)) {
			hql += " and t.columnArticleLatestchangtime >=?";
			obj[i] = columnArticleLatestchangtime;
			i++;
		}
		hql = hql
				+ " order by t.columnArticleState DESC, t.columnArticleIsstandtop DESC, t.columnArticleAddtime DESC";
		Object[] param = new Object[i];
		for (int k = 0, t = 0; k < obj.length; k++) {
			if (obj[k] != null) {
				param[t] = obj[k];
				t++;
			}
		}
		return page = articlesColumnDAO.find(page, hql, param);
	}

	/**
	 * 查询上栏稿件
	 * 
	 * @param page
	 * @param model
	 * @param disLogo
	 * @param arcticlesType
	 * @param columnId
	 * @return
	 */
	public Page<ToaInfopublishColumnArticl> getSelectColumnAricesPagesVeiw(
			Page<ToaInfopublishColumnArticl> page, String articlesTitle,
			String articlesAuthor, String clumnName,
			Date columnArticleLatestchangtime, String disLogo,
			String arcticlesType, String columnId,OALogInfo...infos) throws SystemException,
			ServiceException {
		Object[] obj = new Object[6];
		int i = 0;
		String hql = "";
		if (ALL.equals(disLogo)) {
			hql = "from ToaInfopublishColumnArticl t where 1=1 ";
		} else if (DAODU.equals(disLogo)) {
			hql = "from ToaInfopublishColumnArticl t where t.columnArticleGuidetype = '1' ";
		} else if (REDAIN.equals(disLogo)) {
			hql = "from ToaInfopublishColumnArticl t where t.toaInfopublishArticle.articlesIshot = '1'  ";
		} else if (GUDIN.equals(disLogo)) {
			hql = "from ToaInfopublishColumnArticl t where t.columnArticleIsstandtop = '1' ";
		} else if (PINR.equals(disLogo)) {
			hql = "from ToaInfopublishColumnArticl t where t.toaInfopublishArticle.articlesIscancomment = '1' ";
		}
		// 稿件名称
		if (articlesTitle != null && !"".equals(articlesTitle)
				&& !"null".equals(articlesTitle)) {
			hql += " and t.toaInfopublishArticle.articlesTitle like ?";
			obj[i] = "%" + articlesTitle + "%";
			i++;
		}
		// 稿件作者
		if (articlesAuthor != null && !"".equals(articlesAuthor)
				&& !"null".equals(articlesAuthor)) {
			hql += " and t.toaInfopublishArticle.articlesAuthor like ?";
			obj[i] = "%" + articlesAuthor + "%";
			i++;
		}
		// 栏目名称
		if (clumnName != null && !"".equals(clumnName)
				&& !"null".equals(clumnName)) {
			hql += " and t.toaInfopublishColumn.clumnName like ?";
			obj[i] = "%" + clumnName + "%";
			i++;
		} else {
			hql += " and t.toaInfopublishColumn.clumnId=? ";
			obj[i] = columnId;
			i++;
		}
		// 稿件状态
		/*
		 * if(ALL.equals(arcticlesType)){//全部 hql+= " and t.columnArticleState <>
		 * '0' and t.columnArticleState <> '1' "; }else
		 * if(ISC.equals(arcticlesType)){//已上栏 hql+= " and t.columnArticleState =
		 * '2' "; }else if(ISA.equals(arcticlesType)){//审核中 hql+= " and
		 * t.columnArticleState = '3' "; }else
		 * if(ISP.equals(arcticlesType)){//已发布 hql+= " and t.columnArticleState =
		 * '9' "; }
		 */
		// 修改人
		// if(model.getColumnArticleLatestuser()!=null&&!"".equals(model.getColumnArticleLatestuser())&&!"null".equals(model.getColumnArticleLatestuser())){
		// hql+=" and t.columnArticleLatestuser like ?";
		// obj[i]="%"+model.getColumnArticleLatestuser()+"%";
		// i++;
		// }
		// 修改时间
		if (columnArticleLatestchangtime != null
				&& !"".equals(columnArticleLatestchangtime)
				&& !"null".equals(columnArticleLatestchangtime)) {
			hql += " and t.columnArticleLatestchangtime >=?";
			obj[i] = columnArticleLatestchangtime;
			i++;
		}
		hql = hql
				+ " and t.columnArticleState = '9' order by  t.columnArticleIsstandtop DESC, t.columnArticleAddtime DESC";
		Object[] param = new Object[i];
		for (int k = 0, t = 0; k < obj.length; k++) {
			if (obj[k] != null) {
				param[t] = obj[k];
				t++;
			}
		}
		return page = articlesColumnDAO.find(page, hql, param);
	}

	/**
	 * 财富库查询上拦稿件
	 * 
	 * @param page
	 * @param articlesTitle
	 * @param articlesAuthor
	 * @param hits
	 * @param columnArticleLatestchangtime
	 * @param disLogo
	 * @param arcticlesType
	 * @param columnId
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<ToaInfopublishColumnArticl> getSelectColumnAricesPagesVeiwWealth(
			Page<ToaInfopublishColumnArticl> page, String articlesTitle,
			String articlesAuthor, String hits,
			Date columnArticleLatestchangtime, String disLogo,
			String arcticlesType, String columnId,OALogInfo...infos) throws SystemException,
			ServiceException {
		Object[] obj = new Object[6];
		int i = 0;
		String hql = "";
		if (ALL.equals(disLogo) || disLogo == null) {
			hql = "from ToaInfopublishColumnArticl t where 1=1 ";
		} else if (DAODU.equals(disLogo)) {
			hql = "from ToaInfopublishColumnArticl t where t.columnArticleGuidetype = '1'  ";
		} else if (REDAIN.equals(disLogo)) {
			hql = "from ToaInfopublishColumnArticl t where t.toaInfopublishArticle.articlesIshot = '1'  ";
		} else if (GUDIN.equals(disLogo)) {
			hql = "from ToaInfopublishColumnArticl t where t.columnArticleIsstandtop = '1' ";
		} else if (PINR.equals(disLogo)) {
			hql = "from ToaInfopublishColumnArticl t where t.toaInfopublishArticle.articlesIscancomment = '1' ";
		}
		// 稿件名称
		if (articlesTitle != null && !"".equals(articlesTitle)
				&& !"null".equals(articlesTitle)) {
			hql += " and t.toaInfopublishArticle.articlesTitle like ?";
			obj[i] = "%" + articlesTitle + "%";
			i++;
		}
		// 稿件作者
		if (articlesAuthor != null && !"".equals(articlesAuthor)
				&& !"null".equals(articlesAuthor)) {
			hql += " and t.toaInfopublishArticle.articlesAuthor like ?";
			obj[i] = "%" + articlesAuthor + "%";
			i++;
		}
		// 栏目名称
		if (hits != null && !"".equals(hits) && !"null".equals(hits)) {
			hql += " and t.toaInfopublishArticle.articlesHits=?";

			obj[i] = Long.parseLong(hits);
			i++;
		} 
		if(columnId != null && !"".equals(columnId) && !"null".equals(columnId) ){
			hql += " and t.toaInfopublishColumn.clumnId=? ";
			obj[i] = columnId;
			i++;
		}
		// 稿件状态
		/*
		 * if(ALL.equals(arcticlesType)){//全部 hql+= " and t.columnArticleState <>
		 * '0' and t.columnArticleState <> '1' "; }else
		 * if(ISC.equals(arcticlesType)){//已上栏 hql+= " and t.columnArticleState =
		 * '2' "; }else if(ISA.equals(arcticlesType)){//审核中 hql+= " and
		 * t.columnArticleState = '3' "; }else
		 * if(ISP.equals(arcticlesType)){//已发布 hql+= " and t.columnArticleState =
		 * '9' "; }
		 */
		// 修改人
		// if(model.getColumnArticleLatestuser()!=null&&!"".equals(model.getColumnArticleLatestuser())&&!"null".equals(model.getColumnArticleLatestuser())){
		// hql+=" and t.columnArticleLatestuser like ?";
		// obj[i]="%"+model.getColumnArticleLatestuser()+"%";
		// i++;
		// }
		// 修改时间
		if (columnArticleLatestchangtime != null
				&& !"".equals(columnArticleLatestchangtime)
				&& !"null".equals(columnArticleLatestchangtime)) {
			hql += " and t.columnArticleLatestchangtime  >= ?";
			obj[i] = columnArticleLatestchangtime;
			i++;
		}
		hql = hql
		+ " and t.columnArticleState = '9' order by t.toaInfopublishArticle.articlesAutopublishtime desc "; //按发布时间的降序排序
//		hql = hql
//				+ " and t.columnArticleState = '9' order by  t.columnArticleIsstandtop DESC, t.columnArticleAddtime DESC ";
		Object[] param = new Object[i];
		for (int k = 0, t = 0; k < obj.length; k++) {
			if (obj[k] != null) {
				param[t] = obj[k];
				t++;
			}
		}
		return page = articlesColumnDAO.find(page, hql, param);
	}


	@Autowired
	public void setSearchManager(SearchManager searchManager) {
		this.searchManager = searchManager;
	}

	/**
	 * 根据条件搜索已删除的导读稿件
	 * 
	 * @param page
	 * @param articlesTitle
	 * @param articlesAuthor
	 * @param clumnName
	 * @param articlesLatestuser
	 * @param articlesLatestchangtime
	 * @param disLogo
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page getSelectRecPages(Page page, String articlesTitle,
			String articlesAuthor, String clumnName, String articlesLatestuser,
			Date articlesLatestchangtime, String disLogo,OALogInfo...infos) throws SystemException, ServiceException {
		// String hql = "from ToaInfopublishColumnArticl as t where
		// t.columnArticleState='0' order by t.columnArticleId DESC " ;
		Object[] obj = new Object[6];
		int i = 0;
		String hql = "";
		if (ALL.equals(disLogo)) {
			hql = "from ToaInfopublishColumnArticl as t where t.columnArticleState='0' ";
		} else if (DAODU.equals(disLogo)) {
			hql = "from ToaInfopublishColumnArticl as t where t.columnArticleState='0' and t.columnArticleGuidetype = '1' ";
		} else if (REDAIN.equals(disLogo)) {
			hql = "from ToaInfopublishColumnArticl as t where t.columnArticleState='0' and t.toaInfopublishArticle.articlesIshot = '1'  ";
		} else if (GUDIN.equals(disLogo)) {
			hql = "from ToaInfopublishColumnArticl as t where t.columnArticleState='0' and t.columnArticleIsstandtop = '1' ";
		} else if (PINR.equals(disLogo)) {
			hql = "from ToaInfopublishColumnArticl as t where t.columnArticleState='0' and t.toaInfopublishArticle.articlesIscancomment = '1' ";
		}
		// 稿件名称
		if (articlesTitle != null && !"".equals(articlesTitle)
				&& !"null".equals(articlesTitle)) {
			hql += " and t.toaInfopublishArticle.articlesTitle like ?";
			obj[i] = "%" + articlesTitle + "%";
			i++;
		}
		// 稿件作者
		if (articlesAuthor != null && !"".equals(articlesAuthor)
				&& !"null".equals(articlesAuthor)) {
			hql += " and t.toaInfopublishArticle.articlesAuthor like ?";
			obj[i] = "%" + articlesAuthor + "%";
			i++;
		}
		// 栏目名称
		if (clumnName != null && !"".equals(clumnName)
				&& !"null".equals(clumnName)) {
			hql += " and t.toaInfopublishColumn.clumnName like ?";
			obj[i] = "%" + clumnName + "%";
			i++;
		}
		// 修改人
		if (articlesLatestuser != null && !"".equals(articlesLatestuser)
				&& !"null".equals(articlesLatestuser)) {
			hql += " and t.columnArticleLatestuser like ?";
			obj[i] = "%" + articlesLatestuser + "%";
			i++;
		}
		// 修改时间
		if (articlesLatestchangtime != null
				&& !"".equals(articlesLatestchangtime)
				&& !"null".equals(articlesLatestchangtime)) {
			hql += " and t.columnArticleLatestchangtime >=?";
			obj[i] = articlesLatestchangtime;
			i++;
		}
		hql = hql
				+ "  order by  t.columnArticleIsstandtop DESC, t.columnArticleAddtime DESC ";
		Object[] param = new Object[i];
		for (int k = 0, t = 0; k < obj.length; k++) {
			if (obj[k] != null) {
				param[t] = obj[k];
				t++;
			}
		}
		return page = articlesColumnDAO.find(page, hql, param);
	}
	public Page getSelectRecPagesByUserId(Page page,String userId, String articlesTitle,
			String articlesAuthor, String clumnName, String articlesLatestuser,
			Date articlesLatestchangtime, String disLogo,OALogInfo...infos) throws SystemException, ServiceException {
		// String hql = "from ToaInfopublishColumnArticl as t where
		// t.columnArticleState='0' order by t.columnArticleId DESC " ;
		Object[] obj = new Object[6];
		int i = 0;
		String hql = "";
		if (ALL.equals(disLogo)) {
			hql = "select t from ToaInfopublishColumnArticl t,ToaInfopublishColumnPrivil m where t.toaInfopublishColumn.clumnId=m.toaInfopublishColumn.clumnId" +
		" and m.columnPrivilUserid = '"+userId+"' and m.columnPrivilType ='1' where t.columnArticleState='0' ";
		} else if (DAODU.equals(disLogo)) {
			hql = "select t from ToaInfopublishColumnArticl t,ToaInfopublishColumnPrivil m where t.toaInfopublishColumn.clumnId=m.toaInfopublishColumn.clumnId" +
		" and m.columnPrivilUserid = '"+userId+"' and m.columnPrivilType ='1' where t.columnArticleState='0' and t.columnArticleGuidetype = '1' ";
		} else if (REDAIN.equals(disLogo)) {
			hql = "select t from ToaInfopublishColumnArticl t,ToaInfopublishColumnPrivil m where t.toaInfopublishColumn.clumnId=m.toaInfopublishColumn.clumnId" +
		" and m.columnPrivilUserid = '"+userId+"' and m.columnPrivilType ='1' where t.columnArticleState='0' and t.toaInfopublishArticle.articlesIshot = '1'  ";
		} else if (GUDIN.equals(disLogo)) {
			hql = "select t from ToaInfopublishColumnArticl t,ToaInfopublishColumnPrivil m where t.toaInfopublishColumn.clumnId=m.toaInfopublishColumn.clumnId" +
		" and m.columnPrivilUserid = '"+userId+"' and m.columnPrivilType ='1' where t.columnArticleState='0' and t.columnArticleIsstandtop = '1' ";
		} else if (PINR.equals(disLogo)) {
			hql = "select t from ToaInfopublishColumnArticl t,ToaInfopublishColumnPrivil m where t.toaInfopublishColumn.clumnId=m.toaInfopublishColumn.clumnId" +
		" and m.columnPrivilUserid = '"+userId+"' and m.columnPrivilType ='1' where t.columnArticleState='0' and t.toaInfopublishArticle.articlesIscancomment = '1' ";
		}
		// 稿件名称
		if (articlesTitle != null && !"".equals(articlesTitle)
				&& !"null".equals(articlesTitle)) {
			hql += " and t.toaInfopublishArticle.articlesTitle like ?";
			obj[i] = "%" + articlesTitle + "%";
			i++;
		}
		// 稿件作者
		if (articlesAuthor != null && !"".equals(articlesAuthor)
				&& !"null".equals(articlesAuthor)) {
			hql += " and t.toaInfopublishArticle.articlesAuthor like ?";
			obj[i] = "%" + articlesAuthor + "%";
			i++;
		}
		// 栏目名称
		if (clumnName != null && !"".equals(clumnName)
				&& !"null".equals(clumnName)) {
			hql += " and t.toaInfopublishColumn.clumnName like ?";
			obj[i] = "%" + clumnName + "%";
			i++;
		}
		// 修改人
		if (articlesLatestuser != null && !"".equals(articlesLatestuser)
				&& !"null".equals(articlesLatestuser)) {
			hql += " and t.columnArticleLatestuser like ?";
			obj[i] = "%" + articlesLatestuser + "%";
			i++;
		}
		// 修改时间
		if (articlesLatestchangtime != null
				&& !"".equals(articlesLatestchangtime)
				&& !"null".equals(articlesLatestchangtime)) {
			hql += " and t.columnArticleLatestchangtime >=?";
			obj[i] = articlesLatestchangtime;
			i++;
		}
		hql = hql
		+ "  order by  t.columnArticleIsstandtop DESC, t.columnArticleAddtime DESC ";
		Object[] param = new Object[i];
		for (int k = 0, t = 0; k < obj.length; k++) {
			if (obj[k] != null) {
				param[t] = obj[k];
				t++;
			}
		}
		return page = articlesColumnDAO.find(page, hql, param);
	}

	/**
	 * 获取所有已发布新闻
	 * 
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<ToaInfopublishColumnArticl> getColumnArticleAllList(OALogInfo...infos)
			throws SystemException, ServiceException {
		String hql = "from ToaInfopublishColumnArticl as t where t.columnArticleState='9' ";
		try {
			return articlesColumnDAO.find(hql);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "信息发布稿件" });
		}
	}
	
	/**
	* @method getColumnArticleAll
	* @author 申仪玲
	* @created 2011-11-9 上午10:20:58
	* @description 获取所有已发布新闻的分页对象
	* @return Page<ToaInfopublishColumnArticl> 返回类型
	*/
	public Page<ToaInfopublishColumnArticl> getColumnArticleAll(Page<ToaInfopublishColumnArticl> page){
		
		String hql = "from ToaInfopublishColumnArticl as t " +
				"where t.columnArticleState='9' and ( (t.toaInfopublishArticle.articlesAutopublishtime < ? and t.toaInfopublishArticle.articlesAutocancletime > ? )or t.toaInfopublishArticle.articlesAutocancletime is null) order by t.toaInfopublishColumn.clumnName DESC, t.columnArticleIsstandtop DESC ,t.toaInfopublishArticle.articlesAutopublishtime DESC" ;
		
		return this.articlesColumnDAO.find(page,hql,new Object[]{new Date(),new Date()});
		
	}
	
	/**
	* @method getColumnArticleAll
	* @author 申仪玲
	* @created 2012-11-6 上午11:20:58
	* @description 获取所传用户有权限的栏目下的文章的分页对象
	* @return Page<ToaInfopublishColumnArticl> 返回类型
	*/
	public Page<ToaInfopublishColumnArticl> getColumnArticleAll(String userId, Page<ToaInfopublishColumnArticl> page){
		
		String hql = "from ToaInfopublishColumnArticl as t " +
				"where" +
				"( (t.toaInfopublishColumn.clumnIsprivate = '1') or (t.toaInfopublishColumn.clumnId in (select t2.toaInfopublishColumn.clumnId from ToaInfopublishColumnPrivil t2  where t2.columnPrivilUserid= ? ))) " +
				"and t.columnArticleState='9' " +
				"and ( (t.toaInfopublishArticle.articlesAutopublishtime < ? and t.toaInfopublishArticle.articlesAutocancletime > ? )or t.toaInfopublishArticle.articlesAutocancletime is null) " +
				"order by t.toaInfopublishColumn.clumnName DESC, t.columnArticleIsstandtop DESC ,t.toaInfopublishArticle.articlesAutopublishtime DESC" ;
		
		return this.articlesColumnDAO.find(page,hql,new Object[]{userId,new Date(),new Date()});
		
	}
	
	/**
	* @method getColumnArticleAll
	* @author 申仪玲
	* @created 2012-11-9 上午10:20:58
	* @description 根据栏目Id获取已发布新闻(当栏目Id为空时，获取所有发布文章)
	* @return Page<ToaInfopublishColumnArticl> 返回类型
	*/
	public Page<ToaInfopublishColumnArticl> getColumnArticles(String columnId, Page<ToaInfopublishColumnArticl> page){
		String hql = "";
		if(columnId != null && !"".equals(columnId)){
			hql = "from ToaInfopublishColumnArticl as t where t.columnArticleState='9' and t.toaInfopublishColumn.clumnId= ? and" +
			"( (t.toaInfopublishArticle.articlesAutopublishtime < ? and t.toaInfopublishArticle.articlesAutocancletime > ? )or t.toaInfopublishArticle.articlesAutocancletime is null) " +
			"order by t.toaInfopublishColumn.clumnName DESC, t.columnArticleIsstandtop DESC ,t.columnArticleAddtime DESC" ;
			return this.articlesColumnDAO.find(page,hql,new Object[]{columnId, new Date(),new Date()});
		}else{
			hql = "from ToaInfopublishColumnArticl as t where t.columnArticleState='9' and " +
			"( (t.toaInfopublishArticle.articlesAutopublishtime < ? and t.toaInfopublishArticle.articlesAutocancletime > ? )or t.toaInfopublishArticle.articlesAutocancletime is null) " +
			"order by t.toaInfopublishColumn.clumnName DESC, t.columnArticleIsstandtop DESC ,t.columnArticleAddtime DESC" ;
			
			return this.articlesColumnDAO.find(page,hql,new Object[]{new Date(),new Date()});
		}					
	}
	/**移动webservice  调用
	* @method getColumnArticleAll
	* @author 申仪玲
	* @created 2012-11-9 上午10:20:58
	* @description 根据栏目Id获取已发布新闻(当栏目Id为空时，获取所有发布文章)
	* @return Page<ToaInfopublishColumnArticl> 返回类型
	*/
	public Page<ToaInfopublishColumnArticl> getColumnArticles(String columnId,String articlesTitle, Page<ToaInfopublishColumnArticl> page){
		Object[] obj = new Object[4];
		int i = 2;
		String hql = "from ToaInfopublishColumnArticl as t where (t.columnArticleState='9') and " +
				"( (t.toaInfopublishArticle.articlesAutopublishtime < ? and t.toaInfopublishArticle.articlesAutocancletime > ? ) or" +
				" t.toaInfopublishArticle.articlesAutocancletime is null)";
		obj[0] = new Date();
		obj[1] = new Date();
	
		if(columnId != null && !"".equals(columnId) && !"null".equals(columnId)){
			hql += "and t.toaInfopublishColumn.clumnId = ?";
			obj[i] = columnId;
			i++;
		}
		if(articlesTitle != null && !"".equals(articlesTitle) && !"null".equals(articlesTitle)){
			hql += " and t.toaInfopublishArticle.articlesTitle like ?";
			obj[i] = "%" + articlesTitle + "%";
			i++;
		}
		
		//hql += "order by t.toaInfopublishColumn.clumnName DESC, t.columnArticleIsstandtop DESC ,t.columnArticleAddtime DESC";
		hql += "order by t.columnArticleState, t.columnArticleIsstandtop DESC, t.columnArticleAddtime DESC";
		
		
		Object[] param = new Object[i];
		for (int k = 0, t = 0; k < obj.length; k++) {
			if (obj[k] != null) {
				param[t] = obj[k];
				t++;
			}
		}
			
		return this.articlesColumnDAO.find(page,hql,param);
					
	}
	/**
	 * 格式化时间
	 */
	public String getDateTime(Date time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String date = format.format(time);
		return date;
	}

	/**
	 * 统计栏目下的信息数量
	 * 
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<ArticleStatistic> getColumnArticleCount(Date beginTime,
			Date endTime,OALogInfo...infos) throws SystemException, ServiceException {
		try {
			Calendar cal = Calendar.getInstance();
			String hql = "select t.toaInfopublishColumn.clumnId,count(t.columnArticleId) from ToaInfopublishColumnArticl  t ";
			List<Date> datelist=new ArrayList<Date>();
			if (beginTime != null && !"".equals(beginTime)) {
				hql = hql + " where t.columnArticleAddtime> ?";
				datelist.add(beginTime);
				if ( endTime != null&& !"".equals(endTime)) {
					hql = hql + " and  t.columnArticleAddtime< ?";
					cal.setTime(endTime);
					cal.add(Calendar.DAY_OF_MONTH, 1); // 加一天
					datelist.add(cal.getTime());}				
			} else if (endTime != null && !"".equals(endTime)) {
				hql = hql + " where t.columnArticleAddtime< ?";
				cal.setTime(endTime);
				cal.add(Calendar.DAY_OF_MONTH, 1); // 加一天
				datelist.add(cal.getTime());
			}
			hql = hql + "  group by t.toaInfopublishColumn.clumnId";
			Query query=null;
			if(datelist.size()>0){
				Date[] datetime=new Date[datelist.size()];
				for(int i=0;i<datelist.size();i++){
					datetime[i]=datelist.get(i);
				}
				query=articlesColumnDAO.createQuery(hql, datetime);
			}else{
				query=articlesColumnDAO.createQuery(hql);
			}
			List<Object> rt=new ArrayList<Object>();
			rt=query.list();
			List<ArticleStatistic> list = new ArrayList<ArticleStatistic>();
			for(Object stat:rt){
				Object[] obj=(Object[])stat;
				ArticleStatistic as = new ArticleStatistic();
				ToaInfopublishColumn column = new ToaInfopublishColumn();
				column=columnDAO.get(obj[0].toString());
				as.setColumnName(column.getClumnName());
				as.setPubCount(Integer.parseInt(obj[1].toString()));
				as.setColumnId(obj[0].toString());
				list.add(as);
			}

			return list;
		} catch (DAOException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "信息统计数据访问" });
		} 
	}

	/**
	 * 统计信息发布情况
	 * 
	 * @param beginTime
	 * @param endTime
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<ArticleStatistic> getColumnState(Date beginTime, Date endTime,OALogInfo...infos)
			throws SystemException, ServiceException {
		try {
			String time = "";
			List<Date> datelist = new ArrayList<Date>();
			String hql = "select t.toaInfopublishColumn.clumnId,count(t.columnArticleId) from ToaInfopublishColumnArticl  t  where t.columnArticleState='9' ";
			if (beginTime != null && !"".equals(beginTime)) {// 判断开始时间是否为空
				time = getDateTime(beginTime);
				hql = hql + " and t.columnArticleAddtime>?";
				datelist.add(beginTime);

				if (endTime != null && !"".equals(endTime)) {// 判断结束时间是否为空
					time = getDateTime(endTime);
					hql = hql + " and  t.columnArticleAddtime< ?";
					datelist.add(endTime);
				}
			} else if (endTime != null && !"".equals(endTime)) {// 判断开始和结束时间是否同时为空
				time = getDateTime(endTime);
				hql = hql + " and t.columnArticleAddtime< ?";
				datelist.add(endTime);
			}
			hql = hql + "  group by t.toaInfopublishColumn.clumnId";
			Query query = null;
			if (datelist.size() > 0) {// 判断是否有参数
				Date[] datetime = new Date[datelist.size()];
				for (int i = 0; i < datelist.size(); i++) {
					datetime[i] = datelist.get(i);
				}
				query = articlesColumnDAO.createQuery(hql, datetime);
			} else {
				query = articlesColumnDAO.createQuery(hql);
			}
			List<Object> objlist = query.list();

			// 统计未发布的信息
			List<ArticleStatistic> notlist = this.getColumnNoState(beginTime,
					endTime);
			boolean faly = true;
			for (Object obj : objlist) {
				Object[] str = (Object[]) obj;
				for (int i = 0; i < notlist.size(); i++) {
					if (str[0].toString() == notlist.get(i).getColumnId()
							|| str[0].toString().equals(
									notlist.get(i).getColumnId())) {
						notlist.get(i).setPubCount(
								Integer.parseInt(str[1].toString()));
						faly = false;
					}
				}
				if (faly) {
					ArticleStatistic as = new ArticleStatistic();
					ToaInfopublishColumn column = new ToaInfopublishColumn();
					as.setColumnId(str[0].toString());
					as.setPubCount(Integer.parseInt(str[1].toString()));
					column = columnDAO.get(str[0].toString());
					as.setColumnName(column.getClumnName());
					notlist.add(as);
				} else {
					faly = true;
				}
			}

			return notlist;
		} catch (DAOException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "发布的信息统计数据访问" });
		}

	}

	/**
	 * 未发布的信息统计
	 * 
	 * @param beginTime
	 * @param endTime
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<ArticleStatistic> getColumnNoState(Date beginTime, Date endTime,OALogInfo...infos)
			throws SystemException, ServiceException {
		try {
			String time = "";
			List<Date> datelist=new ArrayList<Date>();
			String hql = "select t.toaInfopublishColumn.clumnId,count(t.columnArticleId) from ToaInfopublishColumnArticl  t  where t.columnArticleState<>'9' ";
			if (beginTime != null && !"".equals(beginTime)) {//判断开始时间
				time = getDateTime(beginTime);
				hql = hql + " and t.columnArticleAddtime> ?";
				datelist.add(beginTime);
			
			if (endTime != null&& !"".equals(endTime)) {//判断结束时间
				time = getDateTime(endTime);
				hql = hql + " and  t.columnArticleAddtime< ?";
				datelist.add(endTime);
			}
			} else if (endTime != null && !"".equals(endTime)) {//开始和结束时间是否同时为空
				time = getDateTime(endTime);
				hql = hql + " and t.columnArticleAddtime< ?";
				datelist.add(endTime);
			}
			hql = hql + "  group by t.toaInfopublishColumn.clumnId";
			Query query=null;
			if(datelist.size()>0){//判断是否有参数
				Date[] datetime=new Date[datelist.size()];
				for(int i=0;i<datelist.size();i++){
					datetime[i]=datelist.get(i);
				}
				query=articlesColumnDAO.createQuery(hql, datetime);
			}else{
			    query=articlesColumnDAO.createQuery(hql);
			}
			List<Object> objlist=query.list();
			List<ArticleStatistic> list = new ArrayList<ArticleStatistic>();
			for(Object obj:objlist){
				Object[] str=(Object[])obj;
				ArticleStatistic as = new ArticleStatistic();
				ToaInfopublishColumn column = new ToaInfopublishColumn();
				as.setColumnId(str[0].toString());
				as.setArticleCount(Integer.parseInt(str[1].toString()));
				column=columnDAO.get(str[0].toString());
				as.setColumnName(column.getClumnName());
				list.add(as);
			}
			
			return list;
		} catch (DAOException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "没发布的信息统计数据访问" });
		} 

	}

	/**
	 * 根据栏目统计文章状态
	 * 
	 * @param columnId
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<ArticleStatistic> getStateArticles(String columnId,Date beginTime,Date endTime)
			throws SystemException, ServiceException {
		try {
			List<Object> finelist=new ArrayList<Object>();
			String hql = "select t.columnArticleState,count(t.columnArticleId)  from ToaInfopublishColumnArticl t"
					+ " where t.toaInfopublishColumn.clumnId=? ";
			finelist.add(columnId);
			if(beginTime!=null){
				hql=hql+" and t.columnArticleAddtime>?";
				finelist.add(beginTime);
			}
			if(endTime!=null){
				hql=hql+"  and  t.columnArticleAddtime<?";
				finelist.add(endTime);
			}
			
			hql=hql+"  group by t.columnArticleState ";
			Object[] values=new Object[finelist.size()];
			for(int i=0;i<finelist.size();i++){
				values[i]=finelist.get(i);
			}
			Query query=articlesColumnDAO.createQuery(hql,values);
			List<Object> objlist=query.list();
			List<ArticleStatistic> list = new ArrayList<ArticleStatistic>();
			for(Object obj:objlist){
				Object[] str=(Object[])obj;
				ArticleStatistic as = new ArticleStatistic();
				if(str[0]!=null){
                as.setColumnName(str[0].toString());
				}else{
					as.setColumnName("");
				}
                as.setPubCount(Integer.parseInt(str[1].toString()));
                as.setColumnId(columnId);
                list.add(as);
			}
			
			return list;
		} catch (DAOException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "根据栏目统计文章状态数据访问" });
		} 
	}

	/**
	 * 统计文章的点击数
	 * 
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<ArticleStatistic> getHitsArticles() throws SystemException,
			ServiceException {
		try {
//			String hql = "select articlesId, articlesTitle, articlesHits from (select *from ToaInfopublishArticle t "
//					+ " where t.articlesHits<>0  order by articlesHits desc) where rownum <= 10";
//			
//			Query query=articlesColumnDAO.createQuery(hql);
//			List<Object> objlist=query.list();
//			List<ArticleStatistic> list = new ArrayList<ArticleStatistic>();
//			for(Object obj:objlist){
//				Object[] str=(Object[])obj;
//				ArticleStatistic as = new ArticleStatistic();
//                as.setColumnId(str[0].toString());
//                as.setPubCount(Integer.parseInt(str[2].toString()));
//                as.setColumnName(str[1].toString());
//                list.add(as);
//			}
			String hql="select t.articlesId, t.articlesTitle, t.articlesHits from ToaInfopublishArticle t  where  t.articlesHits<>0  order by articlesHits desc";
			Query query=articlesColumnDAO.createQuery(hql);
			List<Object> objlist=query.list();
			List<ArticleStatistic> list = new ArrayList<ArticleStatistic>();
			if(objlist.size()>0){//
				for(int i=0;i<objlist.size();i++){
					if(i<10){
					Object[] str=(Object[])objlist.get(i);
					ArticleStatistic as = new ArticleStatistic();
	                as.setColumnId(str[0].toString());
	                as.setPubCount(Integer.parseInt(str[2].toString()));
	                as.setColumnName(str[1].toString());
	                list.add(as);
					}else{
						break;
					}
				}
			}
			return list;
		} catch (DAOException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "统计文章的点击数数据访问" });
		} 
	}

	/**
	 * 统计评论量
	 * 
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<ArticleStatistic> getCommentArticles() throws SystemException,
			ServiceException {
		try {
			String sql = "from ToaInfopublishArticle t where t.articlesIscancomment='1'";
			List<ToaInfopublishArticle> articleList = articlesDAO.find(sql);
			List<ArticleStatistic> list = new ArrayList<ArticleStatistic>();
			for (ToaInfopublishArticle ta : articleList) {
				if (ta.getToaInfopublishComments().size() > 0) {
					ArticleStatistic as = new ArticleStatistic();
					as.setColumnId(ta.getArticlesId());
					as.setColumnName(ta.getArticlesTitle());
					as.setPubCount(ta.getToaInfopublishComments().size());

					list.add(as);
				}
			}
			return list;
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "统计评论量数据访问" });
		}

	}

	/**
	 * 根据编辑人统计文章量
	 * 
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<ArticleStatistic> getUserArticles() throws SystemException,
			ServiceException {
		try {
			String hql = "select t.articlesLatestuser,count(t.articlesId) from ToaInfopublishArticle t group by t.articlesLatestuser  order by count(t.articlesId) desc";
			
			Query query=articlesColumnDAO.createQuery(hql);
			List<Object> objlist=query.list();
			List<ArticleStatistic> list = new ArrayList<ArticleStatistic>();
			for(Object obj:objlist){
				Object[] str=(Object[])obj;
				ArticleStatistic as = new ArticleStatistic();
				if(str[0]!=null){
                as.setColumnName(str[0].toString());
                as.setColumnId(str[0].toString());
				}else{
					as.setColumnName("");
					 as.setColumnId("");
				}
                as.setPubCount(Integer.parseInt(str[1].toString()));
                list.add(as);
			}
			
			return list;
		} catch (DAOException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "根据编辑人统计文章量数据访问" });
		} 
	}

	/**
	 * 查询文章上了那些栏目
	 * 
	 * @param id
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<ToaInfopublishColumnArticl> getColumnArticlByArticleId(String id)
			throws SystemException, ServiceException {
		try {
			String hql = "from ToaInfopublishColumnArticl as t where t.toaInfopublishArticle.articlesId=? and t.columnArticleState <> '0'";
			return articlesColumnDAO.find(hql, id);
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "查询文章上了那些栏目" });
		}
	}
	
	/**
	 * 根据文章ID删除所有栏目里的该信息
	 * @param id
	 */
	public void deleteColumnArticlByID(String id){
		String hql="delete ToaInfopublishColumnArticl t where t.toaInfopublishArticle.articlesId=?";
	       Query query=articlesColumnDAO.createQuery(hql,id);
	       query.executeUpdate();
	}
	@Autowired
	public void setAppManager(ArticlesApproManager appManager) {
		this.appManager = appManager;
	}
	/**
	 * 取消固顶
	 * @author 胡丽丽
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void notStandtop(OALogInfo...infos)throws SystemException,ServiceException{
		try {
			String hql="select t.articlesId from ToaInfopublishArticle t where (t.articlesStandtopstart is not null and t.articlesStandtopstart>? and  t.articlesIsstandtop='1' ) or ( t.articlesStandtopend is not null and t.articlesStandtopend<? and  t.articlesIsstandtop='1' )";
			Date time=new Date();
			List<String> list=articlesDAO.find(hql, time,time);
			if(list!=null&&list.size()>0){
				notStand(list,"0");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 设置固顶
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void isStendTop(OALogInfo...infos)throws SystemException,ServiceException{
		String hql="select t.articlesId from ToaInfopublishArticle t where t.articlesStandtopstart is not null and t.articlesStandtopstart<=? and  t.articlesStandtopend is not null and t.articlesStandtopend>=? and  t.articlesIsstandtop='1' ";
		Date time=new Date();
		List<String> list=articlesDAO.find(hql, time,time);
		if(list!=null&&list.size()>0){
			notStand(list,"1");
		}
	}
	/**
	 * @author 胡丽丽
	 * @param list
	 */
	public void notStand(List<String> list,String top){
		String ids="";
		for(String st:list){
            ids=ids+",'"+st+"'";
		}
		ids=ids.substring(1);
		String hql="update ToaInfopublishColumnArticl t set t.columnArticleIsstandtop=? where t.toaInfopublishArticle.articlesId in ("+ids+")";
		 Query query=articlesColumnDAO.createQuery(hql,top);
	       query.executeUpdate();
	}
	/**
	 * 提交工作流处理
	 * 这里覆盖父类的方法，将保存电子表单数据和提交工作流处理一并处理,处理完成之后修改会议状态
	 * @param formId 表单ID
	 * @param workflowName 流程名称
	 * @param businessId 业务数据id
	 * @param businessName 标题
	 * @param taskActors 下一步处理人([人员ID|节点ID，人员ID|节点ID……])
	 * @param transitionName 迁移线名称
	 */
	public String handleWorkflow(String formId, String workflowName,
			String businessId, String businessName, String[] taskActors,
			String tansitionName, String concurrentTrans, String sugguestion,String formData)throws SystemException,ServiceException {
		String processInstance="";
		try {
			User curUser = userService.getCurrentUser();
			if(concurrentTrans==null){
				concurrentTrans="";
			}
			processInstance=workflow.startWorkflow("0", workflowName, curUser.getUserId(),
					businessId, businessName, taskActors, tansitionName, "",
					sugguestion);
			ToaInfopublishColumnArticl	ca=this.getColumnArticl(businessId);
			ca.setProcessInstanceId(processInstance);
			ca.setColumnArticleState("3");//改变会议状态为审核中
			this.updataColumnArtile(ca, new OALogInfo("送审信息稿件!"));
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.operation_error,new Object[] {"工作流处理"});
		}
		return processInstance;
	}
	public void delete(List<ToaInfopublishColumnArticl> t){
		try {
			articlesColumnDAO.delete(t);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
