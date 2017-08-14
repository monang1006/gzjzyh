package com.strongit.oa.dataprivil;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaPostProperty;
import com.strongit.oa.bo.ToaPostStructure;
import com.strongit.oa.bo.ToaSysmanageStructure;
import com.strongit.oa.common.user.model.Position;
import com.strongit.oa.common.user.service.UserService;
import com.strongit.oa.infotable.infoset.InfoSetManager;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * 
 * @company  Strongit
 * @author 	 彭小青
 * @date 	 Apr 1, 2010 3:13:46 PM
 * @version  2.0.4
 * @comment  设置岗位的信息集信息项权限
 */
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "/postDataPrivil.action") })
public class PostDataPrivilAction extends BaseActionSupport{

	@Autowired private PostDataPrivilManager privilManager;
	
	@Autowired private InfoSetManager infoSetManager;
	
	@Autowired private UserService userService;
	
	private Page<ToaPostStructure> page = new Page<ToaPostStructure>(FlexTableTag.MAX_ROWS, true);
	
	private List<ToaSysmanageStructure> infoSet=new ArrayList<ToaSysmanageStructure>();
	
	private List<ToaPostStructure> infoSetList=new ArrayList<ToaPostStructure>();
	
	private List<ToaPostProperty> infoItemList=new ArrayList<ToaPostProperty>();
	
	private ToaPostStructure postStructure;
	
	private ToaPostProperty postProperty;
	
	private String postId;
	
	private String postName;
	
	private String infoSetCode;
	
	private String infoItemCode;
	
	private String status;
	
	private final static String PERSON_TABLE_NAME="T_OA_BASE_PERSON";
	
	@Override
	public String delete() throws Exception {
		return null;
	}

	@Override
	public String input() throws Exception {
		return null;
	}

	@Override
	public String list() throws Exception {
		if(postName!=null&&!"".equals(postName)){
			
		}else{
			Position post=userService.getPostInfoByPostId(postId);
			postName=post.getPostName();
		}
		infoSet=infoSetManager.getChildCreatedInfoSet2(PERSON_TABLE_NAME, true);
		page=privilManager.getPagePostStructure(page,postId,infoSetCode,status);
		return SUCCESS;
	}
	
	public String getInfoItems() throws Exception{
		infoItemList=privilManager.getPostPropertyList(postId, infoSetCode);
		return "infoitem";
	}
	
	public String getInfoSets() throws Exception{
		infoSetList=privilManager.getPostStructureList(postId);
		return "infoset";
	}
	
	/*
	 * 
	 * Description:设置该岗位的人员对该信息集为只读权限
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Apr 1, 2010 8:30:09 PM
	 */
	public String readSet() throws Exception{
		this.renderText(privilManager.readSet(postId, infoSetCode));
		return null;
	}
	
	/*
	 * 
	 * Description:设置该岗位的人员对该信息集为读写权限
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Apr 1, 2010 8:30:09 PM
	 */
	public String readWriteSet() throws Exception{
		this.renderText(privilManager.readWriteSet(postId, infoSetCode));
		return null;
	}
	
	/*
	 * 
	 * Description:设置该岗位的人员不能查看到该信息集
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Apr 1, 2010 8:30:09 PM
	 */
	public String hiddenSet() throws Exception{
		this.renderText(privilManager.hiddenSet(postId, infoSetCode));
		return null;
	}
	
	/*
	 * 
	 * Description:设置该岗位的人员对该信息项为只读权限
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Apr 1, 2010 8:30:09 PM
	 */
	public String itemReadSet() throws Exception{
		this.renderText(privilManager.itemReadSet(postId, infoItemCode));
		return null;
	}
	
	/*
	 * 
	 * Description:设置该岗位的人员对该信息项为读写权限
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Apr 1, 2010 8:30:09 PM
	 */
	public String itemReadWriteSet() throws Exception{
		this.renderText(privilManager.itemReadWriteSet(postId, infoItemCode));
		return null;
	}
	
	/*
	 * 
	 * Description:设置该岗位的人员不能查看到该信息项
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Apr 1, 2010 8:30:09 PM
	 */
	public String itemHiddenSet() throws Exception{
		this.renderText(privilManager.itemHiddenSet(postId, infoItemCode));
		return null;
	}

	@Override
	protected void prepareModel() throws Exception {
		
	}

	@Override
	public String save() throws Exception {
		return null;
	}

	public ToaPostStructure getPostStructure() {
		return postStructure;
	}

	public void setPostStructure(ToaPostStructure postStructure) {
		this.postStructure = postStructure;
	}

	public ToaPostProperty getPostProperty() {
		return postProperty;
	}

	public void setPostProperty(ToaPostProperty postProperty) {
		this.postProperty = postProperty;
	}

	public Object getModel() {
		return null;
	}

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public Page<ToaPostStructure> getPage() {
		return page;
	}

	public void setPage(Page<ToaPostStructure> page) {
		this.page = page;
	}

	public List<ToaPostStructure> getInfoSetList() {
		return infoSetList;
	}

	public void setInfoSetList(List<ToaPostStructure> infoSetList) {
		this.infoSetList = infoSetList;
	}

	public List<ToaPostProperty> getInfoItemList() {
		return infoItemList;
	}

	public void setInfoItemList(List<ToaPostProperty> infoItemList) {
		this.infoItemList = infoItemList;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getInfoSetCode() {
		return infoSetCode;
	}

	public void setInfoSetCode(String infoSetCode) {
		this.infoSetCode = infoSetCode;
	}

	public List<ToaSysmanageStructure> getInfoSet() {
		return infoSet;
	}

	public String getInfoItemCode() {
		return infoItemCode;
	}

	public void setInfoItemCode(String infoItemCode) {
		this.infoItemCode = infoItemCode;
	}

	public String getPostName() {
		return postName;
	}

	public void setPostName(String postName) {
		this.postName = postName;
	}

}
