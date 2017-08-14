package com.strongit.oa.component.jdbc;

import java.sql.ResultSet;
import java.util.List;

import com.strongmvc.exception.DAOException;

public interface ResultSetExtractor {

	public List extractData(ResultSet rs) throws DAOException;
}
