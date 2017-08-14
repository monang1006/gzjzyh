//Source file: D:\\workspace\\StrongOA2.0_DEV\\src\\com\\strongit\\oa\\archive\\tempfile\\TempFileAction.java

package com.strongit.uums.postmanage;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.springframework.beans.factory.annotation.Autowired;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.dataprivil.PostDataPrivilManager;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongit.uums.bo.TUumsBasePost;
import com.strongit.uums.bo.TUumsBasePostUser;
import com.strongit.uums.bo.TUumsBaseUser;
import com.strongit.oa.common.user.util.Const;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

@ParentPackage("default")
@Results( { 
	@Result(name = BaseActionSupport.RELOAD , value = "/WEB-INF/jsp/postmanage/postmanage-list.jsp"),
	@Result(name = BaseActionSupport.INPUT, value = "/WEB-INF/jsp/postmanage/postmanage-input.jsp"),
	@Result(name ="temp", value = "/WEB-INF/jsp/postmanage/postmanage-temp.jsp")
	})
public class PostContentAction extends BaseActionSupport {

	private static final long serialVersionUID = 1L;

	private Page<TUumsBasePost> page = new Page<TUumsBasePost>(
			FlexTableTag.MAX_ROWS, true);

	private String wpId;

	private List<TUumsBasePost> listWholepost = null;

	private TUumsBasePost boWholepost = new TUumsBasePost();
	
	@Autowired PostDataPrivilManager privilManager;
	
	@Autowired IUserService userService;
	/*
	 * 查询相关属性
	 */
	private String postname;//岗位名称
	private String postdesc;//岗位说明
	
	
	public PostContentAction() {
	}
	public Page<TUumsBasePost> getPage() {
		return page;
	}


	public String getWpId() {
		return wpId;
	}

	public void setWpId(String wpId) {
		this.wpId = wpId;
	}

	/**
	 * Access method for the tempFileList property.
	 * 
	 * @return the current value of the tempFileList property
	 */
	public List<TUumsBasePost> getListWholepost() {
		return listWholepost;
	}

	public TUumsBasePost getBoWholepost() {
		return boWholepost;
	}

	/**
	 * @return java.lang.String
	 * @roseuid 493F83C20148
	 */
	@Override
	public String list() {
		page = userService.queryPosts(page, postname, postdesc);
		List<TUumsBasePost> list = page.getResult();
		if(list!=null){
			for(TUumsBasePost role:list){
				Set<TUumsBasePostUser> set = role.getBasePostUsers();
				StringBuilder userName = new StringBuilder("");
				for(Iterator<TUumsBasePostUser> it=set.iterator();it.hasNext();){
					TUumsBasePostUser ru = it.next();
					TUumsBaseUser tUser=userService.getUserInfoByUserId(ru.getUserId());
					if(Const.IS_YES.equals(tUser.getUserIsdel())){   //判断用户是否删除，删除了的用户不显示在列表中
						continue;
					}
					userName.append(userService.getUserNameByUserId(ru.getUserId()));
					if(it.hasNext()){
						userName.append("、");
					}
				}
				role.setRest4(userName.toString());
			}
		}
		return RELOAD;
	}

	/**
	 * @return java.lang.String
	 * @roseuid 493F83C20167
	 */
	@Override
	public String save() {
		if (boWholepost.getPostId() != null
				&& boWholepost.getPostId().length() == 0) {
			boWholepost.setPostId(null);
		}
		userService.savePost(boWholepost);
		addActionMessage("岗位信息保存成功。");
//		return list();
		//return renderHtml("<script>window.dialogArguments.location='"+getRequest().getContextPath()+"/postmanage/postContent!list.action'; window.close();</script>");
		  return "temp";
	}


	/**
	 * @param wholepostId
	 * @return java.lang.String
	 * @roseuid 493F83C20196
	 */
	@Override
	public String delete() {
		try {
			privilManager.deletePostDataPrivil(wpId);
			userService.deletePosts(wpId);
			addActionMessage("全局岗位信息删除成功。");
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			addActionMessage(e.getMessage());
		}
//		return list();
		return renderHtml("<script>window.location='"+getRequest().getContextPath()+"/postmanage/postContent!list.action';</script>");
	}


	@Override
	protected void prepareModel() {
		if (this.wpId != null) {
			boWholepost = userService.getPostInfoByPostId(this.wpId);
		} else {
			boWholepost = new TUumsBasePost();
			Long sequence = userService.getNextPostSequenceCode();
			boWholepost.setPostSequence(sequence);
		}
	}

	public TUumsBasePost getModel() {
		return this.boWholepost;
	}

	public void setBoWholepost(TUumsBasePost boWholepost) {
		this.boWholepost = boWholepost;
	}
	@Override
	public String input() {
		prepareModel();
		return INPUT;
	}
	public String getPostname() {
		return postname;
	}
	public void setPostname(String postname) {
		this.postname = postname;
	}
	public String getPostdesc() {
		return postdesc;
	}
	public void setPostdesc(String postdesc) {
		this.postdesc = postdesc;
	}
}
