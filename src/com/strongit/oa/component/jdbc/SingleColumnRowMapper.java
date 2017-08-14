package com.strongit.oa.component.jdbc;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.springframework.jdbc.support.JdbcUtils;

import com.strongmvc.exception.DAOException;

public class SingleColumnRowMapper implements RowMapper {

	public Object[] mapRow(ResultSet rs, int paramInt) throws DAOException {
		try {
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount()-1;
			Object[] result = new Object[columnCount];
			for (int i = 1; i <= columnCount; ++i) {
				result[i-1] = JdbcUtils.getResultSetValue(rs, i);
			}
			return result;
		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}

}
