package com.strongit.oa.component.jdbc;

import java.sql.ResultSet;

import com.strongmvc.exception.DAOException;

public interface RowMapper {

	public Object mapRow(ResultSet rs,int paramInt) throws DAOException;
}
