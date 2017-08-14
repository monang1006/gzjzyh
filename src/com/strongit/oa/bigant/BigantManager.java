package com.strongit.oa.bigant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaBigAntUser;
import com.strongit.oa.bo.ToaGroup;
import com.strongit.oa.bo.ToaView;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
@org.springframework.stereotype.Service
@Transactional
public class BigantManager implements IBigantService {

	
	private Connection con;
	/**
	 * 获取数据库中所有机构
	 *	@author lee李俊勇
	 *	@date Apr 19, 2010 3:02:44 PM 
	 * 	@return
	 * 	@throws SystemException
	 * 	@throws ServiceException
	 */
	public List<ToaView> getAllDeparments() throws SystemException,
			ServiceException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<ToaView> result = new ArrayList<ToaView>();
		con = SQlServerHelper.getConnection();
		if(con != null){
			try
			{
				ps = con.prepareStatement("SELECT * FROM hs_View WHERE Col_Type=1");
				rs = ps.executeQuery();
				while(rs.next())
				{
					ToaView model = new ToaView();
					int Col_ID = rs.getInt("Col_ID");
					model.setCol_ID(Col_ID);
					model.setCol_Name(rs.getString("Col_Name"));
					model.setCol_Type(rs.getInt("Col_Type"));
					result.add(model);
					model = null;
				}
				SQlServerHelper.close(con, rs);
				return result;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		SQlServerHelper.close(con, rs);
		return null;
	}
	public boolean isHasChild(int Col_HsItemID,int Col_HsItemType)
	{
		PreparedStatement ps = null;
		ResultSet rs = null;
		con = SQlServerHelper.getConnection();
		if(con != null){
			try 
			{
				ps = con.prepareStatement("SELECT * FROM hs_Relation WHERE Col_HsItemID = ? AND Col_HsItemType = ?");
				ps.setInt(1, Col_HsItemID);
				ps.setInt(2, Col_HsItemType);
				rs = ps.executeQuery();
				if(rs.next())
				{
					SQlServerHelper.close(con, rs);
					return true;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		SQlServerHelper.close(con, rs);
		return false;
	}
	/**
	 * 获取部门下的所有人员
	 *	@author lee李俊勇
	 *	@date Apr 21, 2010 10:10:43 AM 
	 * 	@param Col_HsItemID
	 * 	@param Col_HsItemType
	 * 	@return
	 * 	@throws SystemException
	 * 	@throws ServiceException
	 */
	public List<ToaBigAntUser> getUsersByOrgID(int Col_HsItemID,int Col_HsItemType)
			throws SystemException, ServiceException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<ToaBigAntUser> result = new ArrayList<ToaBigAntUser>();
		con = SQlServerHelper.getConnection();
		if(con != null){
		try 
		{
			ps = con.prepareStatement("SELECT hs_User.* FROM hs_User,hs_Relation WHERE hs_User.Col_ID = hs_Relation.Col_DHsItemID " +
									  "AND hs_Relation.Col_DHsItemType = 1 " +
									  "AND hs_Relation.Col_HsItemID = ? " +
									  "AND hs_Relation.Col_HsItemType = ?");
			ps.setInt(1, Col_HsItemID);
			ps.setInt(2, Col_HsItemType);
			rs = ps.executeQuery();
			while(rs.next())
			{
				ToaBigAntUser model = new ToaBigAntUser();
				model.setCol_ID(rs.getInt("Col_ID"));
				model.setCol_LoginName(rs.getString("Col_LoginName"));
				model.setCol_Name(rs.getString("Col_Name"));
				model.setCol_PWord(rs.getString("Col_PWord"));
				result.add(model);
				model = null;
			}
			SQlServerHelper.close(con, rs);
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		}
		SQlServerHelper.close(con, rs);
		return null;
	}
	/**
	 * 获取部门下的子部门
	 *	@author lee李俊勇
	 *	@date Apr 21, 2010 10:11:04 AM 
	 * 	@param Col_HsItemID  
	 * 	@param Col_HsItemType
	 * 	@return
	 * 	@throws SystemException
	 * 	@throws ServiceException
	 */
	public List<ToaGroup> getGroupById(int Col_HsItemID, int Col_HsItemType)
			throws SystemException, ServiceException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<ToaGroup> result = new ArrayList<ToaGroup>();
		con = SQlServerHelper.getConnection();
		if(con != null){
			try 
			{
				ps = con.prepareStatement("SELECT hs_Group.* FROM hs_Group,hs_Relation WHERE hs_Group.Col_ID = hs_Relation.Col_DHsItemID " +
						  "AND hs_Relation.Col_DHsItemType = 2 " +
						  "AND hs_Relation.Col_HsItemID = ? " +
						  "AND hs_Relation.Col_HsItemType = ?");
				ps.setInt(1, Col_HsItemID);
				ps.setInt(2, Col_HsItemType);
				rs = ps.executeQuery();
				while(rs.next())
				{
					ToaGroup model = new ToaGroup();
					model.setCol_ID(rs.getInt("Col_ID"));
					model.setCol_Name(rs.getString("Col_Name"));
					result.add(model);
					model = null;
				}
				SQlServerHelper.close(con, rs);
				return result;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		SQlServerHelper.close(con, rs);
		return null;
	}
	public boolean isHasGroup(ToaGroup model) throws SystemException,
			ServiceException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		con = SQlServerHelper.getConnection();
		if(con != null){
			try 
			{
				ps = con.prepareStatement("SELECT * FROM hs_Group WHERE hs_Group.Col_Name = ?");
				ps.setString(1, model.getCol_Name());
				rs = ps.executeQuery();
				if(rs.next()){
					return true;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	public boolean isHasUser(ToaBigAntUser model) throws SystemException,
			ServiceException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		con = SQlServerHelper.getConnection();
		if(con != null){
			try 
			{
				ps = con.prepareStatement("SELECT * FROM hs_User WHERE hs_User.Col_LoginName = ?");
				ps.setString(1, model.getCol_LoginName());
				rs = ps.executeQuery();
				if(rs.next()){
					return true;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	public boolean isHasView(ToaView model) throws SystemException,
			ServiceException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		con = SQlServerHelper.getConnection();
		if(con != null){
			try 
			{
				ps = con.prepareStatement("SELECT * FROM hs_View WHERE hs_View.Col_Name = ?");
				ps.setString(1, model.getCol_Name());
				rs = ps.executeQuery();
				if(rs.next()){
					return true;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	/**
	 * 保存部门
	 *	@author lee李俊勇
	 *	@date Apr 23, 2010 8:56:16 AM 
	 * 	@param model
	 * 	@return
	 * 	@throws SystemException
	 * 	@throws ServiceException
	 */
	public boolean saveGroup(ToaGroup model) throws SystemException,
			ServiceException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		con = SQlServerHelper.getConnection();
		if(con != null){
			try 
			{
				if(!isHasGroup(model))
				{
					
						ps = con.prepareStatement("INSERT INTO hs_Group(Col_Name,Col_Dt_Create) VALUES (?,?)");
						ps.setString(1, model.getCol_Name());
						ps.setString(2, new Date().toLocaleString());
						int count = ps.executeUpdate();
						if(count != 0)
						{
							ps = null;
							ps = con.prepareStatement("SELECT * FROM hs_Group WHERE hs_Group.Col_Name = ?");
							ps.setString(1, model.getCol_Name());
							rs = ps.executeQuery();
							if(rs.next())
							{
								model.setCol_ID(rs.getInt("Col_ID"));
								SQlServerHelper.close(con, rs);
								return true;
							}
						}
					
				}else{
					ps = con.prepareStatement("SELECT * FROM hs_Group WHERE hs_Group.Col_Name = ?");
					ps.setString(1, model.getCol_Name());
					rs = ps.executeQuery();
					if(rs.next())
					{
						model.setCol_ID(rs.getInt("Col_ID"));
						SQlServerHelper.close(con, rs);
						return true;
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		SQlServerHelper.close(con, rs);
		return false;
	}
	/**
	 * 保存人员
	 *	@author lee李俊勇
	 *	@date Apr 23, 2010 8:56:31 AM 
	 * 	@param model
	 * 	@return
	 * 	@throws SystemException
	 * 	@throws ServiceException
	 */
	public boolean saveUser(ToaBigAntUser model) throws SystemException,
			ServiceException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		con = SQlServerHelper.getConnection();
		if(con != null){
			try 
			{
				if(!isHasUser(model))
				{
					
						if(!"".equals(model.getCol_IsSuper()) && model.getCol_IsSuper() != null&&model.getCol_IsSuper().equals("1")){
							ps = con.prepareStatement("INSERT INTO hs_User(Col_LoginName,Col_Name," +
									"Col_PWord,Col_IsSuper,Col_EnType,Col_DeptInfo) VALUES (?,?,?,1,1,?)");
						}else{
							ps = con.prepareStatement("INSERT INTO hs_User(Col_LoginName,Col_Name," +
									"Col_PWord,Col_EnType,Col_DeptInfo) VALUES (?,?,?,1,?)");
						}
						ps.setString(1, model.getCol_LoginName());
						ps.setString(2, model.getCol_Name());
						ps.setString(3, model.getCol_PWord());
						ps.setString(4, model.getCol_DeptInfo());
						int count = ps.executeUpdate();
						if(count != 0){
							ps = null;
							ps = con.prepareStatement("SELECT * FROM hs_User WHERE hs_User.Col_LoginName = ?");
							ps.setString(1, model.getCol_LoginName());
							rs = ps.executeQuery();
							if(rs.next()){
								model.setCol_ID(rs.getInt("Col_ID"));
								SQlServerHelper.close(con, rs);
								return true;
							}
						}
					
				}else{
					
						ps = con.prepareStatement("SELECT * FROM hs_User WHERE hs_User.Col_LoginName = ?");
						ps.setString(1, model.getCol_LoginName());
						rs = ps.executeQuery();
						if(rs.next()){
							model.setCol_ID(rs.getInt("Col_ID"));
							SQlServerHelper.close(con, rs);
							return true;
						}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		SQlServerHelper.close(con, rs);
		return false;
	}
	/**
	 * 保存机构
	 *	@author lee李俊勇
	 *	@date Apr 23, 2010 8:56:51 AM 
	 * 	@param model
	 * 	@return
	 * 	@throws SystemException
	 * 	@throws ServiceException
	 */
	public boolean saveView(ToaView model) throws SystemException,
			ServiceException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		con = SQlServerHelper.getConnection();
		if(con != null){
			try 
			{
				if(!isHasView(model))
				{
						ps = con.prepareStatement("INSERT INTO hs_View(Col_Name,Col_Type) VALUES (?,1)");
						ps.setString(1, model.getCol_Name());
						int count = ps.executeUpdate();
						if(count != 0){
							ps = null;
							ps = con.prepareStatement("SELECT * FROM hs_View WHERE hs_View.Col_Name = ?");
							ps.setString(1, model.getCol_Name());
							rs = ps.executeQuery();
							if(rs.next()){
								model.setCol_ID(rs.getInt("Col_ID"));
								SQlServerHelper.close(con, rs);
								return true;
							}
						}
					
				}else{
					ps = con.prepareStatement("SELECT * FROM hs_View WHERE hs_View.Col_Name = ?");
					ps.setString(1, model.getCol_Name());
					rs = ps.executeQuery();
					if(rs.next()){
						model.setCol_ID(rs.getInt("Col_ID"));
						SQlServerHelper.close(con, rs);
						return true;
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		SQlServerHelper.close(con, rs);
		return false;
	}
	/**
	 * 
	 *	@author lee李俊勇
	 *	@date Apr 21, 2010 4:10:17 PM 
	 *  @desc 
	 * 	@param parent
	 * 	@param child
	 * 	@return
	 * 	@throws SystemException
	 * 	@throws ServiceException
	 */
	public boolean saveGroupAndGroup(ToaGroup parent, ToaGroup child)
			throws SystemException, ServiceException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		con = SQlServerHelper.getConnection();
		if(con != null){
			try 
			{
				ps = con.prepareStatement("SELECT * FROM hs_Relation WHERE Col_HsItemID = "+parent.getCol_ID()+
										  " AND Col_HsItemType = 2 " +
										  " AND Col_DHsItemID = "+child.getCol_ID()+
										  " AND Col_DHsItemType = 2");
				rs = ps.executeQuery();
				if(!rs.next()){
					ps = null;
					ps = con.prepareStatement("INSERT INTO hs_Relation(Col_HsItemID,Col_HsItemType," +
							"Col_DHsItemID,Col_DHsItemType,Col_RelType,Col_ViewID)" +
							"VALUES(?,2,?,2,1,0)");
					ps.setInt(1, parent.getCol_ID());
					ps.setInt(2, child.getCol_ID());
					int count = ps.executeUpdate();
					SQlServerHelper.close(con, rs);
					if(count != 0)
						return true;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		SQlServerHelper.close(con, rs);
		return false;
	}
	/**
	 * 保存部门与人员之间的关系
	 *	@author lee李俊勇
	 *	@date Apr 21, 2010 4:10:51 PM 
	 * 	@param parent  部门
	 * 	@param child   人员
	 * 	@return
	 * 	@throws SystemException
	 * 	@throws ServiceException
	 */
	public boolean saveGroupAndUser(ToaGroup parent, ToaBigAntUser child)
			throws SystemException, ServiceException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		con = SQlServerHelper.getConnection();
		if(con != null){
			try 
			{
				ps = con.prepareStatement("SELECT * FROM hs_Relation WHERE Col_HsItemID = "+parent.getCol_ID()+
										  " AND Col_HsItemType = 2 " +
										  " AND Col_DHsItemID = "+child.getCol_ID()+
										  " AND Col_DHsItemType = 1");
				rs = ps.executeQuery();
				if(!rs.next()){
					ps = null;
					ps = con.prepareStatement("INSERT INTO hs_Relation(Col_HsItemID,Col_HsItemType," +
							"Col_DHsItemID,Col_DHsItemType,Col_RelType,Col_ViewID)" +
							"VALUES(?,2,?,1,1,1)");
					ps.setInt(1, parent.getCol_ID());
					ps.setInt(2, child.getCol_ID());
					int count = ps.executeUpdate();
					SQlServerHelper.close(con, rs);
					if(count != 0)
						return true;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		SQlServerHelper.close(con, rs);
		return false;
	}
	/**
	 * 保存机构与部门之间的关系
	 *	@author lee李俊勇
	 *	@date Apr 21, 2010 4:11:10 PM 
	 * 	@param parent  机构
	 * 	@param child   部门
	 * 	@return
	 * 	@throws SystemException
	 * 	@throws ServiceException
	 */
	public boolean saveViewAndGroup(ToaView parent, ToaGroup child)
			throws SystemException, ServiceException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		con = SQlServerHelper.getConnection();
		if(con != null){
			try 
			{
				ps = con.prepareStatement("SELECT * FROM hs_Relation WHERE Col_HsItemID = "+parent.getCol_ID()+
										  " AND Col_HsItemType = 4 " +
										  " AND Col_DHsItemID = "+child.getCol_ID()+
										  " AND Col_DHsItemType = 2");
				rs = ps.executeQuery();
				if(!rs.next()){
					ps = null;
					ps = con.prepareStatement("INSERT INTO hs_Relation(Col_HsItemID,Col_HsItemType," +
							"Col_DHsItemID,Col_DHsItemType,Col_RelType,Col_ViewID)" +
							"VALUES(?,4,?,2,1,0)");
					ps.setInt(1, parent.getCol_ID());
					ps.setInt(2, child.getCol_ID());
					int count = ps.executeUpdate();
					SQlServerHelper.close(con, rs);
					if(count != 0)
						return true;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		SQlServerHelper.close(con, rs);
		return false;
	}
	/**
	 * 保存机构与人员之间的关系
	 *	@author lee李俊勇
	 *	@date Apr 21, 2010 4:11:28 PM 
	 * 	@param parent
	 * 	@param child
	 * 	@return
	 * 	@throws SystemException
	 * 	@throws ServiceException
	 */
	public boolean saveViewAndUser(ToaView parent, ToaBigAntUser child)
			throws SystemException, ServiceException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		con = SQlServerHelper.getConnection();
		if(con != null){
			try 
			{
				ps = con.prepareStatement("SELECT * FROM hs_Relation WHERE Col_HsItemID = "+parent.getCol_ID()+
										  " AND Col_HsItemType = 4 " +
										  " AND Col_DHsItemID = "+child.getCol_ID()+
										  " AND Col_DHsItemType = 1");
				rs = ps.executeQuery();
				if(!rs.next()){
					ps = null;
					ps = con.prepareStatement("INSERT INTO hs_Relation(Col_HsItemID,Col_HsItemType," +
							"Col_DHsItemID,Col_DHsItemType,Col_RelType,Col_ViewID)" +
							"VALUES(?,4,?,1,1,1)");
					ps.setInt(1, parent.getCol_ID());
					ps.setInt(2, child.getCol_ID());
					int count = ps.executeUpdate();
					SQlServerHelper.close(con, rs);
					if(count != 0)
						return true;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		SQlServerHelper.close(con, rs);
		return false;
	}
	public boolean deleteAll() throws SystemException, ServiceException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		con = SQlServerHelper.getConnection();
		if(con != null){
			try 
			{
				ps = con.prepareStatement("DELETE FROM hs_Relation");
				ps.executeUpdate();
				ps = null;
				ps = con.prepareStatement("DELETE FROM hs_Group");
				ps.executeUpdate();
				ps = null;
				ps = con.prepareStatement("DELETE FROM hs_User");
				ps.executeUpdate();
				ps = null;
				ps = con.prepareStatement("DELETE FROM hs_View");
				ps.executeUpdate();
				ps = null;
				SQlServerHelper.close(con, rs);
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
}
