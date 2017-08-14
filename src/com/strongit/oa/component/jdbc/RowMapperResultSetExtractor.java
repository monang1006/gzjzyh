package com.strongit.oa.component.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.strongmvc.exception.DAOException;

public class RowMapperResultSetExtractor implements ResultSetExtractor {

	private final RowMapper rowMapper;

	public RowMapperResultSetExtractor(RowMapper rowMapper) {
		this.rowMapper = rowMapper;
	}

	@SuppressWarnings("unchecked")
	public List extractData(ResultSet rs) throws DAOException {
		List results = new ArrayList();
		int rowNum = 0;
		try {
			while(rs.next()) {
				results.add(rowMapper.mapRow(rs, rowNum++));
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		}
		return results;
	}

}
