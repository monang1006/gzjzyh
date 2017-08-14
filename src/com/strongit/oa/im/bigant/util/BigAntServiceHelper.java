package com.strongit.oa.im.bigant.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.strongit.oa.bo.ToaBigAntUser;
import com.strongit.oa.bo.ToaGroup;
import com.strongit.oa.bo.ToaView;
import com.strongmvc.exception.SystemException;


/**
 * 大蚂蚁查询辅助类.
 * @author 邓志城
 * @company  Strongit Ltd. (C) copyright
 * @date 2010-6-12 下午03:08:30
 * @version  2.0.2.3
 * @classpath com.strongit.oa.im.bigant.util.BigAntServiceHelper
 * @comment
 * @email dengzc@strongit.com.cn
 */
public class BigAntServiceHelper {

	private static Logger logger = Logger.getLogger(BigAntServiceHelper.class);
	
	PreparedStatement ps = null;
	ResultSet rs = null;
	Connection con = null;
	SQlServerHelper helper = SQlServerHelper.getInstance();

	private boolean needCloseConnection = true; // 是否在执行完SQL操作后关闭连接

	/**
	 * 得到所有机构.
	 * @author:邓志城
	 * @date:2010-6-12 下午03:26:46
	 * @return	机构列表
	 * @throws SystemException
	 */
	public List<ToaView> getAllDeparments() throws SystemException { 
		List<ToaView> result = new ArrayList<ToaView>();
		try {
			con = helper.getConnection();
			ps = con.prepareStatement("SELECT Col_ID,Col_Name,Col_Type FROM hs_View WHERE Col_Type=1 order by Col_ItemIndex");
			rs = ps.executeQuery();
			while(rs.next()){
				ToaView model = new ToaView();
				int Col_ID = rs.getInt("Col_ID");
				model.setCol_ID(Col_ID);
				model.setCol_Name(rs.getString("Col_Name"));
				model.setCol_Type(rs.getInt("Col_Type"));
				result.add(model);
				model = null;
			}
		} catch (Exception e) {
			logger.error("com.strongit.oa.im.bigant.util.BigAntServiceHelper.getAllDeparments()", e);
			throw new SystemException(e);
		}finally{
			if(this.isNeedCloseConnection()){
				helper.close(ps, rs, con);				
			}
		}
		return result ;
	}
	
	/**
	 * 验证是否存在机构.
	 * @author:邓志城
	 * @date:2010-6-12 下午03:11:30
	 * @param model	机构对象
	 * @return	操作结果.
	 * @throws SystemException
	 */
	public ToaView isHasView(String name) throws SystemException {
		ToaView view = null;
		try {
			con = helper.getConnection();
			ps = con.prepareStatement("SELECT Col_ID, Col_Name FROM hs_View WHERE hs_View.Col_Name = ?");
			ps.setString(1, name);
			rs = ps.executeQuery();
			if(rs.next()){
				view = new ToaView();
				view.setCol_ID(rs.getInt("Col_ID"));
				view.setCol_Name(rs.getString("Col_Name"));
			}
		} catch (SQLException e) {
			logger.error("com.strongit.oa.im.bigant.util.BigAntServiceHelper.isHasView()", e);
			throw new SystemException(e);
		}finally{
			if(this.isNeedCloseConnection()){
				helper.close(ps, rs, con);				
			}
		}
		return view;
	}

	/**
	 * 根据OA中的机构id得到大蚂蚁中的机构对象
	 * @param orgId			OA中的机构id
	 * @return				大蚂蚁中的机构对象
	 * @throws SystemException
	 */
	public ToaView findViewByOAOrgId(String orgId) throws SystemException {
		ToaView view = null;
		try {
			con = helper.getConnection();
			ps = con.prepareStatement("SELECT Col_ID, Col_Name FROM hs_View WHERE hs_View.Col_Creator_Name = ?");
			ps.setString(1, orgId);
			rs = ps.executeQuery();
			if(rs.next()){
				view = new ToaView();
				view.setCol_ID(rs.getInt("Col_ID"));
				view.setCol_Name(rs.getString("Col_Name"));
			}
		} catch (SQLException e) {
			logger.error("com.strongit.oa.im.bigant.util.BigAntServiceHelper.findViewByOAOrgId()", e);
			throw new SystemException(e);
		}finally{
			if(this.isNeedCloseConnection()){
				helper.close(ps, rs, con);				
			}
		}
		return view;
	}

	public ToaGroup findGroupByOrgId(String orgId) throws SystemException {
		ToaGroup group = null;
		try {
			con = helper.getConnection();
			ps = con.prepareStatement("SELECT Col_ID, Col_Name FROM hs_Group WHERE hs_Group.Col_Creator_Name = ?");
			ps.setString(1, orgId);
			rs = ps.executeQuery();
			if(rs.next()){
				group = new ToaGroup();
				group.setCol_ID(rs.getInt("Col_ID"));
				group.setCol_Name(rs.getString("Col_Name"));
			}
		} catch (SQLException e) {
			logger.error("com.strongit.oa.im.bigant.util.BigAntServiceHelper.findGroupByOrgId()", e);
			throw new SystemException(e);
		}finally{
			if(this.isNeedCloseConnection()){
				helper.close(ps, rs, con);				
			}
		}
		return group;
	}
	
	/**
	 * 校验是否存在子节点
	 * @author:邓志城
	 * @date:2010-6-12 下午04:59:16
	 * @param Col_HsItemID
	 * @param Col_HsItemType
	 * @return
	 * @throws SystemException
	 */
	public boolean isHasChild(int Col_HsItemID,int Col_HsItemType) throws SystemException {
		try {
			con = helper.getConnection();
			ps = con.prepareStatement("SELECT * FROM hs_Relation WHERE Col_HsItemID = ? AND Col_HsItemType = ?");
			ps.setInt(1, Col_HsItemID);
			ps.setInt(2, Col_HsItemType);
			rs = ps.executeQuery();
			if(rs.next()){
				return true;
			}
		} catch (SQLException e) {
			logger.error("com.strongit.oa.im.bigant.util.BigAntServiceHelper.isHasChild()", e);
			throw new SystemException(e);
		}finally{
			if(this.isNeedCloseConnection()){
				helper.close(ps, rs, con);				
			}
		}
		return false;
	}

	/**
	 * 得到机构下的用户集合
	 * @author:邓志城
	 * @date:2010-6-12 下午05:03:00
	 * @param Col_HsItemID
	 * @param Col_HsItemType
	 * @return
	 * @throws SystemException
	 */
	public List<ToaBigAntUser> getUsersByOrgID(int Col_HsItemID,int Col_HsItemType) throws SystemException{
		List<ToaBigAntUser> result = new ArrayList<ToaBigAntUser>();
		try {
			con = helper.getConnection();
			ps = con.prepareStatement("SELECT hs_User.Col_ID,hs_User.Col_LoginName,hs_User.Col_Name,hs_User.Col_PWord FROM hs_User,hs_Relation WHERE hs_User.Col_ID = hs_Relation.Col_DHsItemID " +
									  "AND hs_Relation.Col_DHsItemType = 1 " +
									  "AND hs_Relation.Col_HsItemID = ? " +
									  "AND hs_Relation.Col_HsItemType = ? order by hs_User.Col_ItemIndex ");
				ps.setInt(1, Col_HsItemID);
				ps.setInt(2, Col_HsItemType);
				rs = ps.executeQuery();
				while(rs.next()){
					ToaBigAntUser model = new ToaBigAntUser();
					model.setCol_ID(rs.getInt("Col_ID"));
					model.setCol_LoginName(rs.getString("Col_LoginName"));
					model.setCol_Name(rs.getString("Col_Name"));
					model.setCol_PWord(rs.getString("Col_PWord"));
					result.add(model);
					model = null;
				}
		} catch (SQLException e) {
			logger.error("com.strongit.oa.im.bigant.util.BigAntServiceHelper.getUsersByOrgID()", e);
			throw new SystemException(e);
		}finally{
			if(this.isNeedCloseConnection()){
				helper.close(ps, rs, con);				
			}
		}
		return result;
	}

	/**
	 * 获取部门下的子部门
	 * @author:邓志城
	 * @date:2010-6-12 下午05:06:49
	 * @param Col_HsItemID
	 * @param Col_HsItemType
	 * @return
	 * @throws SystemException
	 */
	public List<ToaGroup> getGroupById(int Col_HsItemID, int Col_HsItemType) throws SystemException {
		List<ToaGroup> result = new ArrayList<ToaGroup>();
		try {
			con = helper.getConnection();
			ps = con.prepareStatement("SELECT hs_Group.* FROM hs_Group,hs_Relation WHERE hs_Group.Col_ID = hs_Relation.Col_DHsItemID " +
									  "AND hs_Relation.Col_DHsItemType = 2 " +
									  "AND hs_Relation.Col_HsItemID = ? " +
									  "AND hs_Relation.Col_HsItemType = ? order by hs_Group.Col_ItemIndex");
			ps.setInt(1, Col_HsItemID);
			ps.setInt(2, Col_HsItemType);
			rs = ps.executeQuery();
			while(rs.next()){
				ToaGroup model = new ToaGroup();
				model.setCol_ID(rs.getInt("Col_ID"));
				model.setCol_Name(rs.getString("Col_Name"));
				result.add(model);
				model = null;
			}
		} catch (SQLException e) {
			logger.error("com.strongit.oa.im.bigant.util.BigAntServiceHelper.getGroupById()", e);
			throw new SystemException(e);
		}finally{
			if(this.isNeedCloseConnection()){
				helper.close(ps, rs, con);				
			}
		}
		return result;
	}

	/**
	 * 校验部门是否存在
	 * @author:邓志城
	 * @date:2010-6-12 下午05:07:51
	 * @param model
	 * @return
	 * @throws SystemException
	 */
	public ToaGroup getGroupByName(int parent,String name) throws SystemException {
		ToaGroup group = null;
		try {
			con = helper.getConnection();
			String sql =  " SELECT hs_Group.Col_ID,hs_Group.Col_Name FROM hs_Relation,hs_Group WHERE Col_HsItemID = "+parent+
						  " AND Col_HsItemType = 4 " +
						  " AND Col_DHsItemID = hs_Group.Col_ID"+
						  " AND Col_DHsItemType = 2"+
						  " AND hs_Group.Col_Name=?";
			ps = con.prepareStatement(sql);
			ps.setString(1, name);
			rs = ps.executeQuery();
			if(rs.next()){
				group = new ToaGroup();
				group.setCol_ID(rs.getInt("Col_ID"));
				group.setCol_Name(rs.getString("Col_Name")); 
			}
		} catch (SQLException e) {
			logger.error("com.strongit.oa.im.bigant.util.BigAntServiceHelper.isHasUser()", e);
			throw new SystemException(e);
		}finally{
			if(this.isNeedCloseConnection()){
				helper.close(ps, rs, con);
			}	
		}
		return group;
	}

	/**
	 * 保存部门信息
	 * @author:邓志城
	 * @date:2010-6-12 下午05:12:40
	 * @param model
	 * @return
	 * @throws SystemException
	 */
	@SuppressWarnings("deprecation")
	public ToaGroup saveGroup(ToaGroup model) throws SystemException {
		try {
			con = helper.getConnection();
			ps = con.prepareStatement("INSERT INTO hs_Group(Col_Name,Col_Dt_Create,Col_ItemIndex,Col_Creator_Name) VALUES (?,?,?,?)",new String[]{"Col_ID"});//增加字段Col_Creator_Name 存储OA中的部门id
			ps.setString(1, model.getCol_Name());
			//ps.setString(2, new Date().toLocaleString());
			ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
			ps.setLong(3, model.getCol_ItemIndex());
			ps.setString(4, model.getCol_Creator_Name());//OA中的部门id
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			if(rs.next()){
				model.setCol_ID(rs.getInt(1));
			}
		} catch (SQLException e) {
			logger.error("com.strongit.oa.im.bigant.util.BigAntServiceHelper.saveGroup()", e);
			throw new SystemException(e);
		}finally{
			if(this.isNeedCloseConnection()){
				helper.close(ps, rs, con);
			}	
		}
		return model;
	}

	/**
	 * 保存部门与部门的关系.
	 * @author:邓志城
	 * @date:2010-6-17 上午10:57:56
	 * @param parent
	 * @param child
	 * @throws SystemException
	 */
	public void saveGroupAndGroup(int parent, int child)
		throws SystemException{
		try{
			con = helper.getConnection();
			ps = con.prepareStatement("INSERT INTO hs_Relation(Col_HsItemID,Col_HsItemType," +
					"Col_DHsItemID,Col_DHsItemType,Col_RelType,Col_ViewID)" +
					"VALUES(?,2,?,2,1,0)");
			ps.setInt(1, parent);
			ps.setInt(2, child);
			ps.executeUpdate();
		}catch(SQLException e){
			logger.error("com.strongit.oa.im.bigant.util.BigAntServiceHelper.saveGroupAndGroup()", e);
			throw new SystemException(e);
		}finally{
			if(this.isNeedCloseConnection()){
				helper.close(ps, rs, con);
			}	
		}
	}

	/**
	 * 保存部门与人员的关系.
	 * @author:邓志城
	 * @date:2010-6-17 上午11:52:32
	 * @param parent
	 * @param child
	 * @throws SystemException
	 */
	public void saveGroupAndUser(int parent, int child, int viewId)
			throws SystemException	{
		try{
			con = helper.getConnection();
			ps = con.prepareStatement("INSERT INTO hs_Relation(Col_HsItemID,Col_HsItemType," +
					"Col_DHsItemID,Col_DHsItemType,Col_RelType,Col_ViewID)" +
					"VALUES(?,2,?,1,1,?)");
			ps.setInt(1, parent);
			ps.setInt(2, child);
			ps.setInt(3, viewId);
			ps.executeUpdate();
		}catch(SQLException e){
			logger.error("com.strongit.oa.im.bigant.util.BigAntServiceHelper.saveGroupAndUser()", e);
			throw new SystemException(e);
		}finally{
			if(this.isNeedCloseConnection()){
				helper.close(ps, rs, con);
			}	
		}
	}
	
	/**
	 * 保存机构与部门的关系
	 * @author:邓志城
	 * @date:2010-6-17 上午10:36:13
	 * @param parent
	 * @param child
	 * @throws SystemException
	 */
	public void saveViewAndGroup(int parent, int child)
			throws SystemException{
		try{
			con = helper.getConnection();
			ps = con.prepareStatement("INSERT INTO hs_Relation(Col_HsItemID,Col_HsItemType," +
					"Col_DHsItemID,Col_DHsItemType,Col_RelType,Col_ViewID)" +
					"VALUES(?,4,?,2,1,0)");
			ps.setInt(1, parent);
			ps.setInt(2, child);
			ps.executeUpdate();
		}catch(SQLException e){
			logger.error("com.strongit.oa.im.bigant.util.BigAntServiceHelper.saveViewAndGroup()", e);
			throw new SystemException(e);
		}finally{
			if(this.isNeedCloseConnection()){
				helper.close(ps, rs, con);
			}	
		}
	}
	
	/**
	 * 校验用户是否存在.
	 * @author:邓志城
	 * @date:2010-6-12 下午03:16:52
	 * @param model	用户对象
	 * @return 操作结果
	 * @throws SystemException
	 */
	public boolean isHasUser(ToaBigAntUser model) throws SystemException {
		try {
			con = helper.getConnection();
			ps = con.prepareStatement("SELECT Col_LoginName FROM hs_User WHERE hs_User.Col_LoginName = ?");
			ps.setString(1, model.getCol_LoginName().toLowerCase());
			rs = ps.executeQuery();
			if(rs.next()){
				return true;
			}
		} catch (SQLException e) {
			logger.error("com.strongit.oa.im.bigant.util.BigAntServiceHelper.isHasUser()", e);
			throw new SystemException(e);
		}finally{
			if(this.isNeedCloseConnection()){
				helper.close(ps, rs, con);
			}	
		}
		return false;
	}

	/**
	 * 根据查询条件查询用户信息.
	 * 支持登录名，姓名，ID，手机号码查询
	 * @author:邓志城
	 * @date:2010-6-28 上午10:37:33
	 * @param model
	 * @return
	 * @throws SystemException
	 */
	public ToaBigAntUser getUserInfo(ToaBigAntUser model) throws SystemException {
		ToaBigAntUser user = new ToaBigAntUser();
		try{
			con = helper.getConnection();
			StringBuilder sql = new StringBuilder("SELECT * FROM hs_User WHERE 1=1 ");
			ps = con.prepareStatement(sql.toString());
			List<Object> params = new ArrayList<Object>(1);
			if(model.getCol_LoginName() != null){
				sql.append(" AND hs_User.Col_LoginName = ?");
				params.add(model.getCol_LoginName().toLowerCase());
			}
			if(model.getCol_Name() != null){
				sql.append(" AND hs_User.Col_Name = ?");
				params.add(model.getCol_Name());
			}
			if(model.getCol_Mobile() != null){
				sql.append(" AND hs_User.Col_Mobile = ?");
				params.add(model.getCol_Mobile());
			}
			for(int i=1;i<=params.size();i++){
				Object param = params.get(i-1);
				ps.setObject(i, param);
			}
			rs = ps.executeQuery();
			if(rs.next()){
				user.setCol_LoginName(rs.getString("Col_LoginName"));
				user.setCol_Name(rs.getString("Col_Name"));
				user.setCol_ID(rs.getInt("Col_ID"));
				user.setCol_Mobile(rs.getString("Col_Mobile")); 
			}
		} catch (SQLException e){
			logger.error("com.strongit.oa.im.bigant.util.BigAntServiceHelper.getUserInfo()", e);
			throw new SystemException(e);
		}finally{
			if(this.isNeedCloseConnection()){
				helper.close(ps, rs, con);
			}
		}
		return user ;
	}
	
	/**
	 * 清空大蚂蚁中的数据.
	 * @author:邓志城
	 * @date:2010-6-12 下午05:56:34
	 * @return
	 * @throws SystemException
	 */
	public boolean deleteAll() throws SystemException {
		try {
			con = helper.getConnection();
			ps = con.prepareStatement("DELETE FROM hs_Relation WHERE (Col_ViewID NOT IN (SELECT Col_ID FROM hs_View WHERE Col_OwnerID !='0')) AND Col_ViewID!='0'");
			ps.executeUpdate();
			ps = null;
			ps = con.prepareStatement("DELETE FROM hs_Group WHERE Col_ItemIndex !='10000'");
			ps.executeUpdate();
			/*ps = null;
			ps = con.prepareStatement("DELETE FROM hs_User");
			ps.executeUpdate();*/
			ps = null;
			ps = con.prepareStatement("DELETE FROM hs_View WHERE Col_OwnerID = '0'");
			ps.executeUpdate();
			ps = null;
		} catch (SQLException e) {
			logger.error("com.strongit.oa.im.bigant.util.BigAntServiceHelper.deleteAll()", e);
			throw new SystemException(e);
		}finally{
			if(this.isNeedCloseConnection()){
				helper.close(ps, rs, con);
			}	
		}
		return true;
	}
	
	/**
	 * 删除机构与人员之间的关系.
	 * @author:刘皙
	 * @date:2012年8月16日 上午 10:33:24
	 * @param bigantUserId		人员Id
	 */
	public void deleteViewAndUser(int bigantUserId) {
		try{
			con = helper.getConnection();
			ps = con.prepareStatement("DELETE FROM hs_Relation WHERE" + 
				" Col_HsItemType = '4' and Col_DHsItemID =? and Col_DHsItemType = '1' and Col_RelType = '1'");
			ps.setInt(1, bigantUserId);
			ps.executeUpdate();
		}catch(SQLException e){
			logger.error("com.strongit.oa.im.bigant.util.BigAntServiceHelper.deleteViewAndUser(int)", e);
			throw new SystemException(e);
		}finally{
			if(this.isNeedCloseConnection()){
				helper.close(ps, rs, con);
			}
		}
	}
	
	/**
	 * 删除部门与人员的关系.
	 * @author:刘皙
	 * @date:2012年8月16日 上午 10:02:38
	 * @param bigantUserId
	 * @throws SystemException
	 */
	public void deleteGroupAndUser(int bigantUserId)
			throws SystemException	{
		try{
			con = helper.getConnection();
			ps = con.prepareStatement("DELETE FROM hs_Relation WHERE" + 
				" Col_HsItemType = '2' and Col_DHsItemID =? and Col_DHsItemType = '1' and Col_RelType = '1'");
			ps.setInt(1, bigantUserId);
			ps.executeUpdate();
		}catch(SQLException e){
			logger.error("com.strongit.oa.im.bigant.util.BigAntServiceHelper.deleteGroupAndUser(int,int,int)", e);
			throw new SystemException(e);
		}finally{
			if(this.isNeedCloseConnection()){
				helper.close(ps, rs, con);
			}	
		}
	}
	
	/**
	 * 得到大蚂蚁中所有用户信息.
	 * @author:邓志城
	 * @date:2010-9-19 上午10:42:43
	 * @return
	 * @throws SystemException
	 */
	public List<ToaBigAntUser> getAllUserInfo() throws SystemException {
		List<ToaBigAntUser> userList = new ArrayList<ToaBigAntUser>();
		try {
			con = helper.getConnection();
			ps = con.prepareStatement("SELECT Col_ID, Col_LoginName,Col_Name,Col_Mobile, Col_PWord FROM hs_User");
			rs = ps.executeQuery();
			while (rs.next()) {
				ToaBigAntUser user = new ToaBigAntUser();
				user.setCol_LoginName(rs.getString("Col_LoginName"));
				user.setCol_Name(rs.getString("Col_Name"));
				user.setCol_ID(rs.getInt("Col_ID"));
				user.setCol_Mobile(rs.getString("Col_Mobile")); 
				user.setCol_PWord(rs.getString("Col_PWord"));
				userList.add(user);
			}
		} catch (SQLException e) {
			logger.error("com.strongit.oa.im.bigant.util.BigAntServiceHelper.getAllUserInfo()", e);
			throw new SystemException(e);
		}finally{
			if(this.isNeedCloseConnection()){
				helper.close(ps, rs, con);
			}	
		}
		return userList;
	}

	/**
	 * 得到大蚂蚁中用户表中存储的对应OA中的用户id列表.
	 * @return
	 * @throws SystemException
	 */
	public List<String> getAllUserIds() throws SystemException {
		List<String> ids = new LinkedList<String>();
		try {
			con = helper.getConnection();
			ps = con.prepareStatement("SELECT Col_HomePage FROM hs_User");
			rs = ps.executeQuery();
			while (rs.next()) {
				ids.add(rs.getString("Col_HomePage"));
			}
		} catch (SQLException e) {
			logger.error("com.strongit.oa.im.bigant.util.BigAntServiceHelper.getAllUserInfo()", e);
			throw new SystemException(e);
		}finally{
			if(this.isNeedCloseConnection()){
				helper.close(ps, rs, con);
			}	
		}
		return ids;
	}

	/**
	 * 根据OA中的人员id删除大蚂蚁中的用户
	 * @param userId
	 */
	public void delete(String userId) {
		try {
			con = helper.getConnection();
			ps = con.prepareStatement("DELETE FROM hs_User WHERE Col_HomePage = '"+userId+"'");
			ps.executeUpdate();
		} catch (SQLException e) {
			logger.error("com.strongit.oa.im.bigant.util.BigAntServiceHelper.getAllUserInfo()", e);
			throw new SystemException(e);
		}finally{
			if(this.isNeedCloseConnection()){
				helper.close(ps, rs, con);
			}	
		}
	}
	
	/**
	 * 更新用户信息
	 * @author:邓志城
	 * @date:2010-9-19 上午11:07:54
	 * @param id
	 * @param model
	 * @throws SystemException
	 */
	public void updateUserInfo(int id,ToaBigAntUser model) throws SystemException {
		try {
			if(model == null){
				return ;
			}
			con = helper.getConnection();
			StringBuilder sql = new StringBuilder();
			sql.append("UPDATE hs_User set ");
			if(model.getCol_PWord() != null && !"".equals(model.getCol_PWord())) {
				sql.append("hs_User.Col_PWord = '").append(model.getCol_PWord()).append("',");
			}
			if(model.getCol_Name() != null) {
				sql.append(" hs_User.Col_Name = '").append(model.getCol_Name()).append("',");
			}
			if(model.getCol_Mobile() != null) {
				sql.append(" hs_User.Col_Mobile = '").append(model.getCol_Mobile()).append("',");
			}
			if(model.getCol_LoginName() != null) {
				sql.append(" hs_User.Col_LoginName = '").append(model.getCol_LoginName().toLowerCase()).append("',");
			}
			if(model.getCol_o_Phone() != null) {
				sql.append(" hs_User.Col_o_Phone = '").append(model.getCol_o_Phone()).append("',");
			}
			if(model.getCol_EMail() != null) {
				sql.append(" hs_User.Col_EMail = '").append(model.getCol_EMail().toLowerCase()).append("',");
			}
			if(model.getCol_DeptInfo() != null) {
				sql.append(" hs_User.Col_DeptInfo = '").append(model.getCol_DeptInfo()).append("',");
			}
			if(model.getCol_HomePage() != null) {
				sql.append(" hs_User.Col_HomePage = '").append(model.getCol_HomePage()).append("',");
			}
			if(model.getCol_o_JobTitle() != null) {
				sql.append(" hs_User.Col_o_JobTitle = '").append(model.getCol_o_JobTitle()).append("',");
			}
			sql.append(" hs_User.Col_ItemIndex = ").append(model.getCol_ItemIndex()).append(",");
			sql.deleteCharAt(sql.length() - 1);
			sql.append(" WHERE hs_User.Col_ID = ").append(id);
			ps = con.prepareStatement(sql.toString());
			ps.executeUpdate();
			logger.error("更新大蚂蚁用户信息SQL:" + sql.toString());
		} catch (SQLException e) {
			logger.error("com.strongit.oa.im.bigant.util.BigAntServiceHelper.getAllUserInfo()", e);
			throw new SystemException(e);
		}finally{
			if(this.isNeedCloseConnection()){
				helper.close(ps, rs, con);
			}	
		}
	}
	
	/**
	 * 关闭连接
	 * @author:邓志城
	 * @date:2010-6-12 下午03:00:53
	 * @param pstm
	 * @param rs 记录集对象
	 * @param con	连接
	 * @throws SQLException
	 */
	public synchronized void close() throws SystemException {
		helper.close(ps, rs, con);
	}
	
	/**
	 * 保存组织机构
	 * @author:邓志城
	 * @date:2010-6-17 上午09:29:26
	 * @param model
	 * @return
	 * @throws SystemException
	 */
	public ToaView saveView(ToaView model) throws SystemException {
		try{
			con = helper.getConnection();
			ps = con.prepareStatement("INSERT INTO hs_View(Col_Name,Col_Type,Col_ItemIndex,Col_Creator_Name) VALUES (?,1,?,?)",new String[]{"Col_ID"});//增加字段Col_Creator_Name 存储OA中的机构id
			ps.setString(1, model.getCol_Name());
			ps.setLong(2, model.getCol_ItemIndex());
			ps.setString(3, model.getCol_Creator_Name());//OA中的机构id
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			if(rs.next()){
				model.setCol_ID(rs.getInt(1));
			}
		}catch(SQLException ex){
			logger.error("com.strongit.oa.im.bigant.util.BigAntServiceHelper.saveView()", ex);
			throw new SystemException(ex);
		}finally{
			if(this.isNeedCloseConnection()){
				helper.close(ps, rs, con);
			}
		}
		return model;
	}

	/**
	 * 根据大蚂蚁中存储的OA中的用户id得到大蚂蚁中的用户id
	 * @param oaUserId			OA中的用户id
	 * @return					大蚂蚁中的用户id
	 * @throws SystemException
	 */
	public Integer getBigAntUserIdFromOAUserId(String oaUserId) throws SystemException {
		try{
			con = helper.getConnection();
			ps = con.prepareStatement("SELECT Col_ID FROM hs_User WHERE hs_User.Col_HomePage = ?");
			ps.setString(1, oaUserId);
			rs = ps.executeQuery();
			if(rs.next()) {//找到这样的用户
				int id = rs.getInt("Col_ID");
				return id;
			}
		}catch(SQLException ex){
			logger.error("com.strongit.oa.im.bigant.util.BigAntServiceHelper.saveView()", ex);
			throw new SystemException(ex);
		}finally{
			if(this.isNeedCloseConnection()){
				helper.close(ps, rs, con);
			}
		}
		return null;
	}
	
	/**
	 * 根据大蚂蚁中存储的OA中的机构id得到大蚂蚁中的机构id
	 * @param oaOrgId			OA中的机构id
	 * @return					大蚂蚁中的机构id
	 * @throws SystemException
	 */
	public String getBigAntOrgIdFromOAOrgId(String oaOrgId) throws SystemException {
		try{
			con = helper.getConnection();
			ps = con.prepareStatement("SELECT Col_ID FROM hs_Group WHERE Col_Creator_Name = ?");
			ps.setString(1, oaOrgId);
			rs = ps.executeQuery();
			if(rs.next()) {
				int id = rs.getInt("Col_ID");
				return id + "," + "1";//返回部门
			} else {
				ps = con.prepareStatement("SELECT Col_ID FROM hs_View WHERE Col_Creator_Name = ?");
				ps.setString(1, oaOrgId);
				rs = ps.executeQuery();
				if(rs.next()) {
					int id = rs.getInt("Col_ID");
					return id + "," + "0";//返回机构
				}
			}
		}catch(SQLException ex){
			logger.error("com.strongit.oa.im.bigant.util.BigAntServiceHelper.saveView()", ex);
			throw new SystemException(ex);
		}finally{
			if(this.isNeedCloseConnection()){
				helper.close(ps, rs, con);
			}
		}
		return null;
	}
	
	/**
	 * 保存用户信息
	 * @author:邓志城
	 * @date:2010-6-17 上午09:29:26
	 * @param model
	 * @return
	 * @throws SystemException
	 */
	public ToaBigAntUser saveUser(ToaBigAntUser model) throws SystemException {
		try{
			con = helper.getConnection();
			ps = con.prepareStatement("SELECT Col_ID FROM hs_User WHERE hs_User.Col_HomePage = ?");//or Col_LoginName = '"+model.getCol_LoginName().toLowerCase()+"'
			ps.setString(1, model.getCol_HomePage());//model.getCol_HomePage()存储用户id
			rs = ps.executeQuery();
			if(rs.next()) {//找到这样的用户
				int id = rs.getInt("Col_ID");
				model.setCol_ID(id);
				this.updateUserInfo(id, model);
				return model;
			}
			if(!"".equals(model.getCol_IsSuper()) && model.getCol_IsSuper() != null&&model.getCol_IsSuper().equals("1")){
				ps = con.prepareStatement("INSERT INTO hs_User(Col_LoginName,Col_Name," +
						"Col_PWord,Col_IsSuper,Col_EnType,Col_DeptInfo,Col_Mobile,Col_ItemIndex,Col_HomePage,Col_o_JobTitle,Col_o_Phone,Col_EMail) VALUES (?,?,?,1,1,?,?,?,?,?,?,?)",new String[]{"Col_ID"});
			}else{
				ps = con.prepareStatement("INSERT INTO hs_User(Col_LoginName,Col_Name," +
						"Col_PWord,Col_EnType,Col_DeptInfo,Col_Mobile,Col_ItemIndex,Col_HomePage,Col_o_JobTitle,Col_o_Phone,Col_EMail) VALUES (?,?,?,1,?,?,?,?,?,?,?)",new String[]{"Col_ID"});//添加之后返回生成的主键
			}
			ps.setString(1, model.getCol_LoginName().toLowerCase());
			ps.setString(2, model.getCol_Name());
			ps.setString(3, model.getCol_PWord());
			ps.setString(4, model.getCol_DeptInfo() == null ? "" : model.getCol_DeptInfo());
			ps.setString(5, model.getCol_Mobile() == null ? "" : model.getCol_Mobile());
			ps.setLong(6, model.getCol_ItemIndex() == null ? Long.MAX_VALUE :  model.getCol_ItemIndex());
			ps.setString(7, model.getCol_HomePage());
			ps.setString(8, model.getCol_o_JobTitle() == null ? "" : model.getCol_o_JobTitle());//职务
			ps.setString(9, model.getCol_o_Phone() == null ? "" : model.getCol_o_Phone());//单位电话
			ps.setString(10, model.getCol_EMail() == null ? "" : model.getCol_EMail());//Email
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			if(rs.next()) {
				model.setCol_ID(rs.getInt(1));
			}
			/*if(count != 0){
				ps = null;
				ps = con.prepareStatement("SELECT * FROM hs_User WHERE hs_User.Col_LoginName = ?");
				ps.setString(1, model.getCol_LoginName());
				rs = ps.executeQuery();
				if(rs.next()){
					model.setCol_ID(rs.getInt("Col_ID"));
				}
			}*/	
		}catch(SQLException e){
			logger.error("com.strongit.oa.im.bigant.util.BigAntServiceHelper.saveUser()", e);
			throw new SystemException(e);
		}finally{
			if(this.isNeedCloseConnection()){
				helper.close(ps, rs, con);
			}
		}
		return model;
	}

	/**
	 * 保存机构与人员之间的关系.
	 * @author:邓志城
	 * @date:2010-6-17 上午09:51:07
	 * @param parent	机构Id
	 * @param child		人员Id
	 */
	public void saveViewAndUser(int parentId, int userId, int viewId) {
		try{
			con = helper.getConnection();
			ps = con.prepareStatement("INSERT INTO hs_Relation(Col_HsItemID,Col_HsItemType," +
									  "Col_DHsItemID,Col_DHsItemType,Col_RelType,Col_ViewID)" +
									  "VALUES(?,4,?,1,1,?)");
			ps.setInt(1, parentId);
			ps.setInt(2, userId);
			ps.setInt(3, viewId);
			ps.executeUpdate();
		}catch(SQLException e){
			logger.error("com.strongit.oa.im.bigant.util.BigAntServiceHelper.saveViewAndUser()", e);
			throw new SystemException(e);
		}finally{
			if(this.isNeedCloseConnection()){
				helper.close(ps, rs, con);
			}
		}
	}
	
	public boolean isNeedCloseConnection() {
		return needCloseConnection;
	}

	public void setNeedCloseConnection(boolean needCloseConnection) {
		this.needCloseConnection = needCloseConnection;
	}
	
}
