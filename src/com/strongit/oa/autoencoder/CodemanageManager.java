package com.strongit.oa.autoencoder;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.autoencoder.util.NumberAnalysis;
import com.strongit.oa.bo.ToaCodemanage;
import com.strongit.oa.bo.ToaRule;
import com.strongit.oa.common.user.IUserService;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;
/**
 * <p>Title: CodemanageManager.java</p>
 * <p>Description: 编码管理类</p>
 * <p>Strongit Ltd. (C) copyright 2009</p>
 * <p>Company: Strong</p>
 * @author 	 于宏洲
 * @date 	 Oct 19, 2010
 * @version  1.0
 */
@Service
@Transactional
public class CodemanageManager {
	
	private GenericDAOHibernate<ToaCodemanage, String> codemanageDao;
	
	@Autowired private IRuleService ruleService;
	
	@Autowired private IUserService userService;
	
	private static Logger log = Logger.getLogger(CodemanageManager.class);
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		codemanageDao = new GenericDAOHibernate<ToaCodemanage,String>(sessionFactory,ToaCodemanage.class);
	}
	
	public boolean delObj(String ids){
		String[] idArray = ids.split(",");
		try{
			codemanageDao.delete(idArray);
			return true;
		}catch(Exception e){
			log.error(e);
			return false;
		}
	}
	
	public synchronized String getCodeToFlow(String id) {
		try{
			ToaRule rule = ruleService.getRuleById(id);
			String xml = rule.getRule();
			//获取当前用户所在部门ID
			String userId = userService.getCurrentUser().getUserId();
			TUumsBaseOrg org=userService.getUserDepartmentByUserId(userId);
			String orgId = org.getOrgId();
			//System.out.println(orgId);
			if(xml==null||"".equals(xml)){
				log.error("获取的编号规则有误，XML信息为空");
				return null;
			}else{
				NumberAnalysis analysis = new NumberAnalysis(xml);
				String val;
				val = analysis.getMyNumber(orgId);
				String updatedXml = analysis.updateXmlByInfo(orgId);
				if(updatedXml==null){
					return null;
				}else{
					rule.setRule(updatedXml);
					this.ruleService.save(rule);
					ToaCodemanage obj = new ToaCodemanage();
					obj.setCodeInfo(val);
					obj.setCoderuleId(id);
					obj.setCodeStatus("0");
					obj.setCodeUsername(userService.getCurrentUser().getUserName());
					obj.setCodeCreatetime(new Date());
					saveObj(obj);
					return val;
				}
			}
		}catch(Exception e){
			log.error(e);
			return null;
		}
	}
	
	public boolean saveObj(ToaCodemanage obj){
		try{
			ToaRule rule = ruleService.getRuleById(obj.getCoderuleId());
			if(rule==null){
				return false;
			}else{
				obj.setOrgId(rule.getOrgId());
				obj.setOrgCode(rule.getOrgCode());
				codemanageDao.save(obj);
				return true;
			}
		}catch(Exception e){
			log.error(e);
			return false;
		}
	}
	
	public ToaCodemanage getObjById(String id){
		ToaCodemanage obj= codemanageDao.get(id);
		return obj;
	}
	
	public ToaCodemanage getObjByName(String name){
		String query = "from ToaCodemanage t where t.codeInfo=?";
		List<ToaCodemanage> list = codemanageDao.find(query, name);
		if(list.size()==1){
			return list.get(0);
		}else{
			return null;
		}
	}
	
	public Page<ToaCodemanage> getReservedCodePageList(Page<ToaCodemanage> page,String codeName,Date startDate,Date endDate,String type,String selcode){
		Object[] values = new Object[3];
		String queryStr = "from ToaCodemanage t where t.codeStatus='"+type+"'";
		if(codeName!=null&&!"".equals(codeName)){
			queryStr = queryStr+" and t.codeInfo like ?";
			values[0] = "%"+codeName+"%";
		}else{
			queryStr = queryStr+"and 1=?";
			values[0] = 1;
		}
		
		if(selcode!=null){
			queryStr=queryStr+" and t.coderuleId='"+selcode+"'";
		}
		
		if(startDate!=null){
			queryStr = queryStr + " and t.codeCreatetime>=?";
			values[1]=startDate;
		}else{
			queryStr = queryStr + " and 1=?";
			values[1]=1;
		}
		
		if(endDate!=null){
			queryStr = queryStr + " and t.codeCreatetime<=?";
			values[2]=endDate;
		}else{
			queryStr = queryStr + " and 1=?";
			values[2] = 1;
		}
		String userId = userService.getCurrentUser().getUserId();
		TUumsBaseOrg org=userService.getSupOrgByUserIdByHa(userId);
		if(!userService.isSystemDataManager(userId)){					//如果是非分级授权管理员
			if(userService.isViewChildOrganizationEnabeld()){			//是否允许看到下级机构
				if(org!=null){
					queryStr = queryStr + " and t.orgCode like '"+org.getSupOrgCode()+"%'";
				}
			}else {
				if(org!=null){
					queryStr = queryStr + " and t.orgId = '"+org.getOrgId()+"'";
				}
			}
		}
		queryStr = queryStr + " order by t.codeCreatetime desc";
		page = codemanageDao.find(page, queryStr, values);
		return page;
	}
	
	public Page<ToaCodemanage> getPageList(Page<ToaCodemanage> page,String codeName,Date startDate,Date endDate,String state,String selcode){
		Object[] values = new Object[4];
		String queryStr = "from ToaCodemanage t where 1=1";
		if(codeName!=null&&!"".equals(codeName)){
			queryStr = queryStr+" and t.codeInfo like ?";
			values[0] = "%"+codeName+"%";
		}else{
			queryStr = queryStr+"and 1=?";
			values[0] = 1;
		}
		
		if(selcode!=null){
			queryStr=queryStr+" and t.coderuleId='"+selcode+"'";
		}
		
		if(startDate!=null){
			queryStr = queryStr + " and t.codeCreatetime>=?";
			values[1]=startDate;
		}else{
			queryStr = queryStr + " and 1=?";
			values[1]=1;
		}
		
		if(endDate!=null){
			queryStr = queryStr + " and t.codeCreatetime<=?";
			values[2]=endDate;
		}else{
			queryStr = queryStr + " and 1=?";
			values[2] = 1;
		}
		
		if(state!=null&&!"".equals(state)&&!"-1".equals(state)){
			queryStr = queryStr + " and t.codeStatus=?";
			values[3] = state;
		}else{
			queryStr = queryStr + " and 1=?";
			values[3] = 1;
		}
		
		queryStr = queryStr + " order by t.codeCreatetime desc,t.codeInfo desc";
		
		page = codemanageDao.find(page, queryStr, values);
		
		return page;
	}
}
