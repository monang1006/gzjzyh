package com.strongit.oa.component.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.strongmvc.exception.DAOException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

/**
 * Jdbc查询分页处理类,目前只支持Oracle数据库.
 * @author 		邓志城
 * @company  	Strongit Ltd. (C) copyright
 * @date   		Feb 15, 2012
 * @classpath	com.strongit.oa.component.jdbc.JdbcSplitService
 * @version  	5.0
 * @email		dengzc@strongit.com.cn
 * @tel			0791-8186916
 */
@Repository
public class JdbcSplitService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private GenericDAOHibernate<Object, String> DAO;

	private ResultSetExtractor resultSetExtractor;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		DAO = new GenericDAOHibernate<Object, String>(sessionFactory,
				Object.class);
		resultSetExtractor = new RowMapperResultSetExtractor(
				new SingleColumnRowMapper());
	}

	private Connection getConnection() {
		return DAO.getConnection();
	}

	private String removeSelect(String hql) {
		int beginPos = hql.toLowerCase().indexOf("from");
		return hql.substring(beginPos);
	}

	public List executeSqlForList(String sql,List<Object> paramsLst) throws DAOException {
		Connection co = getConnection();
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			logger.info(sql);	
			st = co.prepareStatement(sql, 1003, 1007);
			if ((paramsLst != null) && (!(paramsLst.isEmpty()))) {
				for (int i = 0; i < paramsLst.size(); ++i) {
					Object param = paramsLst.get(i);
					st.setObject(i + 1, param);
				}
			}
			rs = st.executeQuery();
			return resultSetExtractor.extractData(rs);
		} catch (SQLException ex) {
			throw new DAOException(ex);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
			} catch (SQLException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public Page excuteSqlForPage(Page page, String sql,
			List<Object> paramsLst, String orderBy) throws DAOException {
		Connection co = getConnection();
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			int count = 0;
			if(page.isAutoCount()) {
				String countSql = removeSelect(sql);
				logger.info("select count(1) " + countSql);
				st = co.prepareStatement("select count(1) " + countSql, 1003, 1007);
				if ((paramsLst != null) && (!(paramsLst.isEmpty()))) {
					for (int i = 0; i < paramsLst.size(); ++i) {
						Object param = paramsLst.get(i);
						st.setObject(i + 1, param);
					}
				}
				rs = st.executeQuery();
				if (rs.next()) {
					count = rs.getInt(1);
				}				
			}

			int start = 0;
			int end = start + page.getPageSize();
			if (page.isFirstSetted()) {
				start = page.getFirst();
			}
			sql = "select tt.* from (select row_.*, rownum rownum_ from ("
					+ sql + " " + ((orderBy == null) ? "" : orderBy)
					+ ") row_ where rownum <= " + end + ") tt where rownum_ > "
					+ start;
			logger.info(sql);	
			st = co.prepareStatement(sql, 1003, 1007);
			if (page.isPageSizeSetted()) {
				
				st.setMaxRows(page.getPageSize());
			}
			if ((paramsLst != null) && (!(paramsLst.isEmpty()))) {
				for (int i = 0; i < paramsLst.size(); ++i) {
					Object param = paramsLst.get(i);
					st.setObject(i + 1, param);
				}
			}
			rs = st.executeQuery();
			page.setTotalCount(count);
			page.setResult(resultSetExtractor.extractData(rs));
		} catch (SQLException ex) {
			throw new DAOException(ex);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
			} catch (SQLException e) {
				logger.error(e.getMessage(), e);
			}
		}
		return page;

	}
}
