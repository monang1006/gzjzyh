package com.strongit.oa.component.jdbc;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;

import org.springframework.core.CollectionFactory;
import org.springframework.jdbc.support.JdbcUtils;

import com.strongmvc.exception.DAOException;

public class ColumnMapRowMapper implements RowMapper {

	public Map<String,Object> mapRow(ResultSet rs, int paramInt) throws DAOException {
		try {
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			Map<String,Object> mapOfColValues = createColumnMap(columnCount);
			for (int i = 1; i <= columnCount; ++i) {
				String key = getColumnKey(JdbcUtils.lookupColumnName(rsmd, i));
				Object obj = getColumnValue(rs, i);
				mapOfColValues.put(key, obj);
			}
			return mapOfColValues;
		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}

	@SuppressWarnings("unchecked")
	protected Map<String,Object> createColumnMap(int columnCount) {
		return CollectionFactory
				.createLinkedCaseInsensitiveMapIfPossible(columnCount);
	}

	protected String getColumnKey(String columnName) {
		return columnName;
	}

	protected Object getColumnValue(ResultSet rs, int index)
			throws SQLException {
		return JdbcUtils.getResultSetValue(rs, index);
	}
}
