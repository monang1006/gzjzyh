package com.strongit.oa.publiccontact;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaPcCategory;
import com.strongit.oa.bo.ToaPubliccontact;
import com.strongmvc.exception.DAOException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

@Service
@Transactional
public class PublicContactManage {
	private GenericDAOHibernate<ToaPubliccontact, java.lang.String> pcdao;
	private GenericDAOHibernate<ToaPcCategory, java.lang.String> pccdao;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		pcdao = new GenericDAOHibernate<ToaPubliccontact, String>(
				sessionFactory, ToaPubliccontact.class);
		pccdao = new GenericDAOHibernate<ToaPcCategory, String>(sessionFactory,
				ToaPcCategory.class);
	}

	/**
	 * 获取公共联系人类型 
	 * author taoji
	 * 
	 * @return
	 * @date 2013-12-31 上午09:19:13
	 */
	public Page<ToaPcCategory> getContactsType(Page<ToaPcCategory> pccpage) {
		StringBuffer sb = new StringBuffer();
		sb.append("from ToaPcCategory t where 1=1 order by t.pccNum asc ");
		pccpage = pccdao.find(pccpage,sb.toString());
		return pccpage;
	}
	/**
	 * 获取类别实体 根据类别id
	 * author  taoji
	 * @param typeId
	 * @return
	 * @date 2014-1-2 下午02:21:01
	 */
	public ToaPcCategory getType(String typeId){
		return pccdao.get(typeId);
	}
	/**
	 * 删除分类    批量删除  并且删除分类下的所有人员
	 * author  taoji
	 * @param typeId
	 * @date 2013-12-31 下午03:16:53
	 */
	public void delType(String typeId){
		try {
			String[] id =  typeId.split(",");
			for(String m : id){
				if(m!=null&&!"".equals(m)){
					pccdao.delete(m);
				}
			}
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 查询分类排序号 是否有重复   重复 返回  true
	 * author  taoji
	 * @param pccNum
	 * @return
	 * @date 2014-1-3 上午11:38:02
	 */
	@SuppressWarnings("unchecked")
	public boolean isRepeat(String pccNum){
		StringBuffer sb = new StringBuffer();
		sb.append("from ToaPcCategory t where t.pccNum = '"+pccNum+"'");
		List<ToaPcCategory> t = pccdao.find(sb.toString());
		if(t!=null&&t.size()>0){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 查询分类名称 是否有重复   重复 返回  true
	 * author  taoji
	 * @param pccNum
	 * @return
	 * @date 2014-1-3 上午11:38:02
	 */
	@SuppressWarnings("unchecked")
	public boolean isRepeatName(String pccName){
		StringBuffer sb = new StringBuffer();
		sb.append("from ToaPcCategory t where t.pccName = '"+pccName+"'");
		List<ToaPcCategory> t = pccdao.find(sb.toString());
		if(t!=null&&t.size()>0){
			return true;
		}else{
			return false;
		}
	}
	public int getMaxNumType(){
		StringBuffer sb = new StringBuffer();
		sb.append(" select max(t.pccNum) from ToaPcCategory t where 1=1 ");
		List<Integer> t = pccdao.find(sb.toString());
		if(t!=null&&t.size()>0){
			if(null!=t.get(0)){
				Integer i = t.get(0)+1;
				return i;
			}
		}
		return 0;
	}
	/**
	 * 根据类型显示公共联系人
	 * author  taoji
	 * @param page
	 * @param typeId
	 * @return
	 * @date 2013-12-31 上午09:27:22
	 */
	public Page<ToaPubliccontact> getContactsBytype(
			Page<ToaPubliccontact> page, String typeId) {
		StringBuffer sb = new StringBuffer();
		sb.append("from ToaPubliccontact t where 1=1 ");
		if(typeId!=null&&!"".equals(typeId)){
			sb.append(" and t.TOaPcCategory.pccId='"+typeId+"'");
		}
//		sb.append(" order by t.pcCreateDate desc");
		sb.append(" order by t.pcCreateDate desc");
		page = pcdao.find(page, sb.toString());
		return page;
	}
	/**
	 * 根据查询条件查询公共联系人
	 * author  taoji
	 * @param page
	 * @param pcName
	 * @param pcTell
	 * @param pcEmail
	 * @return
	 * @date 2013-12-31 上午09:26:55
	 */
	public Page<ToaPubliccontact> getContactsBycondition(
			Page<ToaPubliccontact> page,String typeId, String pcName,String pcTell,String pcEmail) {
		StringBuffer sb = new StringBuffer();
		Object[] obj=new Object[5];
		sb.append(" from ToaPubliccontact t where 1=1 ");
		int i=0;
		if(typeId!=null&&!"".equals(typeId)){
			sb.append(" and t.TOaPcCategory.pccId=?");
			obj[i]=typeId;
			i++;
		}else{
			sb.append(" and 1=?");
			obj[i]=1;
			i++;
		}
		if(pcName!=null&&!"".equals(pcName)){
			sb.append(" and t.pcName like ");
			if("%".equals(pcName)){
				pcName = pcName.replaceAll("%", "/%");
				sb.append(" '%"+pcName+"%' ESCAPE '/' ");
			}else{
				sb.append(" ? ");
				obj[i]="%"+pcName+"%";
				i++;
			}
		}
		if(pcTell!=null&&!"".equals(pcTell)){
			sb.append(" and t.pcTell like ");
			if("%".equals(pcTell)){
				pcTell = pcTell.replaceAll("%", "/%");
				sb.append(" '%"+pcTell+"%' ESCAPE '/' ");
			}else{
				sb.append("? ");
				obj[i]="%"+pcTell+"%";
				i++;
			}
		}
		if(pcEmail!=null&&!"".equals(pcEmail)){
			sb.append(" and t.pcEmail like ");
			if("%".equals(pcEmail)){
				pcEmail = pcEmail.replaceAll("%", "/%");
				sb.append(" '%"+pcEmail+"%' ESCAPE '/' ");
			}else{
				sb.append("? ");
				obj[i]="%"+pcEmail+"%";
				i++;
			}
		}
//		sb.append(" order by t.pcCreateDate desc");
		sb.append(" order by t.pcCreateDate desc");
		Object[] objss=new Object[i];
		for(int j=0;j<i;j++ ){
			objss[j]=obj[j];
		}
		page = pcdao.find(page, sb.toString(), objss);
		return page;
	}
	/**
	 * 
	 * author  taoji
	 * @param t
	 * @date 2014-1-3 下午02:37:36
	 */
	public void saveType(ToaPcCategory t ){
		try {
			if(t.getPccId()==null||"".equals(t.getPccId())){
				t.setPccId(null);
				pccdao.save(t);
			}else{
				pccdao.update(t);
			}
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * author  taoji
	 * @param pcId
	 * @return
	 * @date 2013-12-31 下午04:29:14
	 */
	public ToaPubliccontact get(String pcId){
		return pcdao.get(pcId);
	}
	public void save(ToaPubliccontact t ){
		try {
			if(t.getPcId()==null||"".equals(t.getPcId())){
				t.setPcId(null);
				t.setPcCreateDate(new Date());
				pcdao.save(t);
			}else{
				pcdao.update(t);
			}
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 删除联系人
	 * 删除人员   支持多个
	 * author  taoji
	 * @param id
	 * @date 2014-1-2 上午11:24:35
	 */
	public void delContacts(String id){
		String[] ids = id.split(",");
		for(String idid : ids ){
			if(idid!=null&&!"".equals(idid)){
				try {
					pcdao.delete(idid);
				} catch (DAOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	public List<ToaPubliccontact> findToaPubliccontactByIds(String ids){
		StringBuffer sb = new StringBuffer();
		sb.append("from ToaPubliccontact t where 1 = 1 ");
		String[] pcids = ids.split(",");
		sb.append(" and t.pcId in(");
		for(String pcId : pcids){
			sb.append("'"+pcId+"',");
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append(")");
		sb.append(" order by t.pcCreateDate desc");
		return pcdao.find(sb.toString());
	}
	/**
	 * 导出Excel
	 * author  taoji
	 * @return
	 * @date 2014-1-2 上午11:34:49
	 */
	public String exportContacts(){
		return null;
	}
	/**
	 * 根据 类别id  获取 该类别下人的总数
	 * author  taoji
	 * @param pccId
	 * @return
	 * @date 2014-3-24 上午10:58:53
	 */
	public String getCounts(String pccId){
		StringBuffer sb = new StringBuffer();
		sb.append(" select count(*) from ToaPubliccontact t where t.TOaPcCategory.pccId=?");
		List<Long> t = new ArrayList<Long>();
		t = pcdao.find(sb.toString(),pccId);
		if(t!=null&&t.size()>0){
			String ttt = String.valueOf(t.get(0));
			return ttt;
		}
		return null;
	}
}
