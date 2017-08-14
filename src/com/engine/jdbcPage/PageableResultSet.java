/**
 * CopyRight © 2008 Jiang Xi Strong Co. Ltd.
 * All rights reserved. 
 * @FileName: PageableResultSet.java
 * @Description: 实现的分页的主要方法 implement Rageable
 * @author: Jia Dongsheng
 * @JDKVersion: 1.4.2
 * @date: 2008-7-30
 * 
 */
package com.engine.jdbcPage;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

public class PageableResultSet implements Pageable {

	protected ResultSet rs = null;

	protected int rowsCount;

	protected int pageSize;

	protected int curPage;

	protected String command = "";

	public PageableResultSet(ResultSet rs)throws java.sql.SQLException {
		if (rs == null)throw new SQLException("given ResultSet is NULL", "user");
		rs.last();
		rowsCount = rs.getRow();
		rs.beforeFirst();
		this.rs = rs;
	}
	//得到分页总页数
	public int getPageCount() {
		if (rowsCount == 0)
			return 0;
		if (pageSize == 0)
			return 1;
		double tmpD = (double) rowsCount / pageSize;
		int tmpI = (int) tmpD;
		if (tmpD > tmpI)
			tmpI++;
		return tmpI;
	}
	//得到一页记录数
	public int getPageRowsCount() {
		if (pageSize == 0)
			return rowsCount;
		if (getRowsCount() == 0)
			return 0;
		if (curPage != getPageCount())
			return pageSize;
		return rowsCount - (getPageCount() - 1) * pageSize;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getRowsCount() {
		return rowsCount;
	}
	//转到第几页
	public void gotoPage(int page) {
		if (rs == null)
			return;
		if (page < 1)
			page = 1;
		if (page > getPageCount())
			page = getPageCount();
		int row = (page - 1) * pageSize + 1;
		try {
			rs.absolute(row);
			curPage = page;
		} catch (java.sql.SQLException e) {
		}
	}
	//前一页
	public void pageFirst() throws java.sql.SQLException {
		int row = (curPage - 1) * pageSize + 1;
		rs.absolute(row);
	}

	public void pageLast() throws java.sql.SQLException {
		int row = (curPage - 1) * pageSize + getPageRowsCount();
		rs.absolute(row);
	}

	public void setPageSize(int pageSize) {
		if (pageSize >= 0) {
			this.pageSize = pageSize;
			curPage = 1;
		}
	}
	
	public int getCurPage() {
		return curPage;
	}
	
	public int getConcurrency() throws SQLException {
		return rs.getConcurrency();
	}

	public int getFetchDirection() throws SQLException {
		return rs.getFetchDirection();
	}

	public int getFetchSize() throws SQLException {
		return rs.getFetchSize();
	}

	public int getRow() throws SQLException {
		return rs.getRow();
	}

	public int getType() throws SQLException {
		return rs.getType();
	}

	public void afterLast() throws SQLException {
		rs.afterLast();
	}

	public void beforeFirst() throws SQLException {
		rs.beforeFirst();

	}

	public void cancelRowUpdates() throws SQLException {
		rs.cancelRowUpdates();
	}

	public void clearWarnings() throws SQLException {
		rs.clearWarnings();
	}

	public void close() throws SQLException {
		rs.close();
	}

	public void deleteRow() throws SQLException {
		// TODO Auto-generated method stub

	}

	public void insertRow() throws SQLException {
		rs.insertRow();
	}

	public void moveToCurrentRow() throws SQLException {
		rs.moveToCurrentRow();
	}

	public void moveToInsertRow() throws SQLException {
		rs.moveToInsertRow();
	}

	public void refreshRow() throws SQLException {
		rs.refreshRow();
	}

	public void updateRow() throws SQLException {
		rs.updateRow();
	}

	public boolean first() throws SQLException {
		return rs.first();
	}

	public boolean isAfterLast() throws SQLException {
		return rs.isAfterLast();
	}

	public boolean isBeforeFirst() throws SQLException {
		return rs.isBeforeFirst();
	}

	public boolean isFirst() throws SQLException {
		return rs.isFirst();
	}

	public boolean isLast() throws SQLException {
		return rs.isLast();
	}

	public boolean last() throws SQLException {
		return rs.last();
	}

	public boolean next() throws SQLException {
		return rs.next();
	}

	public boolean previous() throws SQLException {
		return rs.previous();
	}

	public boolean rowDeleted() throws SQLException {
		return rs.rowDeleted();
	}

	public boolean rowInserted() throws SQLException {
		return rs.rowInserted();
	}

	public boolean rowUpdated() throws SQLException {
		return rs.rowUpdated();
	}

	public boolean wasNull() throws SQLException {
		return false;
	}

	public byte getByte(int columnIndex) throws SQLException {
		return rs.getByte(columnIndex);
	}

	public double getDouble(int columnIndex) throws SQLException {
		return rs.getDouble(columnIndex);
	}

	public float getFloat(int columnIndex) throws SQLException {
		return rs.getFloat(columnIndex);
	}

	public int getInt(int columnIndex) throws SQLException {
		return rs.getInt(columnIndex);
	}

	public long getLong(int columnIndex) throws SQLException {
		return rs.getLong(columnIndex);
	}

	public short getShort(int columnIndex) throws SQLException {
		return rs.getShort(columnIndex);
	}

	public void setFetchDirection(int direction) throws SQLException {
		rs.setFetchDirection(direction);
	}

	public void setFetchSize(int rows) throws SQLException {
		rs.setFetchSize(rows);
	}

	public void updateNull(int columnIndex) throws SQLException {
		rs.updateNull(columnIndex);
	}

	public boolean absolute(int row) throws SQLException {
		return rs.absolute(row);
	}

	public boolean getBoolean(int columnIndex) throws SQLException {
		return rs.getBoolean(columnIndex);
	}

	public boolean relative(int rows) throws SQLException {
		return rs.relative(rows);
	}

	public byte[] getBytes(int columnIndex) throws SQLException {
		return rs.getBytes(columnIndex);
	}

	public void updateByte(int columnIndex, byte x) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateDouble(int columnIndex, double x) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateFloat(int columnIndex, float x) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateInt(int columnIndex, int x) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateLong(int columnIndex, long x) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateShort(int columnIndex, short x) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateBoolean(int columnIndex, boolean x) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateBytes(int columnIndex, byte[] x) throws SQLException {
		// TODO Auto-generated method stub

	}

	public InputStream getAsciiStream(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public InputStream getBinaryStream(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public InputStream getUnicodeStream(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public void updateAsciiStream(int columnIndex, InputStream x, int length)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateBinaryStream(int columnIndex, InputStream x, int length)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public Reader getCharacterStream(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public void updateCharacterStream(int columnIndex, Reader x, int length)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public Object getObject(int columnIndex) throws SQLException {
		return rs.getObject(columnIndex);
	}

	public void updateObject(int columnIndex, Object x) throws SQLException {
		rs.updateObject(columnIndex,x);
	}

	public void updateObject(int columnIndex, Object x, int scale)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public String getCursorName() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getString(int columnIndex) throws SQLException {
		return rs.getString(columnIndex);
	}

	public void updateString(int columnIndex, String x) throws SQLException {
		rs.updateString(columnIndex,x);
	}

	public byte getByte(String columnName) throws SQLException {
		return rs.getByte(columnName);
	}

	public double getDouble(String columnName) throws SQLException {
		return rs.getDouble(columnName);
	}

	public float getFloat(String columnName) throws SQLException {
		return rs.getFloat(columnName);
	}

	public int findColumn(String columnName) throws SQLException {
		return rs.findColumn(columnName);
	}

	public int getInt(String columnName) throws SQLException {
		return rs.getInt(columnName);
	}

	public long getLong(String columnName) throws SQLException {
		return rs.getLong(columnName);
	}

	public short getShort(String columnName) throws SQLException {
		return rs.getShort(columnName);
	}

	public void updateNull(String columnName) throws SQLException {
		rs.updateNull(columnName);
	}

	public boolean getBoolean(String columnName) throws SQLException {
		return rs.getBoolean(columnName);
	}

	public byte[] getBytes(String columnName) throws SQLException {
		return rs.getBytes(columnName);
	}

	public void updateByte(String columnName, byte x) throws SQLException {
		rs.updateByte(columnName,x);
	}

	public void updateDouble(String columnName, double x) throws SQLException {
		rs.updateDouble(columnName,x);
	}

	public void updateFloat(String columnName, float x) throws SQLException {
		rs.updateFloat(columnName,x);
	}

	public void updateInt(String columnName, int x) throws SQLException {
		rs.updateInt(columnName,x);
	}

	public void updateLong(String columnName, long x) throws SQLException {
		rs.updateLong(columnName,x);
	}

	public void updateShort(String columnName, short x) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateBoolean(String columnName, boolean x) throws SQLException {
		rs.updateBoolean(columnName,x);
	}

	public void updateBytes(String columnName, byte[] x) throws SQLException {
		rs.updateBytes(columnName,x);
	}

	public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public BigDecimal getBigDecimal(int columnIndex, int scale)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public void updateBigDecimal(int columnIndex, BigDecimal x)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public URL getURL(int columnIndex) throws SQLException {
		return rs.getURL(columnIndex);
	}

	public Array getArray(int i) throws SQLException {
		return rs.getArray(i);
	}

	public void updateArray(int columnIndex, Array x) throws SQLException {
		// TODO Auto-generated method stub

	}

	public Blob getBlob(int i) throws SQLException {
		return rs.getBlob(i);
	}

	public void updateBlob(int columnIndex, Blob x) throws SQLException {

	}

	public Clob getClob(int i) throws SQLException {
		return rs.getClob(i);
	}

	public void updateClob(int columnIndex, Clob x) throws SQLException {
		// TODO Auto-generated method stub

	}

	public Date getDate(int columnIndex) throws SQLException {
		return rs.getDate(columnIndex);
	}

	public void updateDate(int columnIndex, Date x) throws SQLException {
		rs.updateDate(columnIndex,x);
	}

	public Ref getRef(int i) throws SQLException {
		return rs.getRef(i);
	}

	public void updateRef(int columnIndex, Ref x) throws SQLException {
		rs.updateRef(columnIndex,x);
	}

	public ResultSetMetaData getMetaData() throws SQLException {
		return null;
	}

	public SQLWarning getWarnings() throws SQLException {
		return null;
	}

	public Statement getStatement() throws SQLException {
		return rs.getStatement();
	}

	public Time getTime(int columnIndex) throws SQLException {
		return rs.getTime(columnIndex);
	}

	public void updateTime(int columnIndex, Time x) throws SQLException {
		rs.updateTime(columnIndex,x);
	}

	public Timestamp getTimestamp(int columnIndex) throws SQLException {
		return rs.getTimestamp(columnIndex);
	}

	public void updateTimestamp(int columnIndex, Timestamp x)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public InputStream getAsciiStream(String columnName) throws SQLException {
		return rs.getAsciiStream(columnName);
	}

	public InputStream getBinaryStream(String columnName) throws SQLException {
		return rs.getBinaryStream(columnName);
	}

	public InputStream getUnicodeStream(String columnName) throws SQLException {
		return null;
	}

	public void updateAsciiStream(String columnName, InputStream x, int length)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateBinaryStream(String columnName, InputStream x, int length)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public Reader getCharacterStream(String columnName) throws SQLException {
		return rs.getCharacterStream(columnName);
	}

	public void updateCharacterStream(String columnName, Reader reader,
			int length) throws SQLException {

	}

	public Object getObject(String columnName) throws SQLException {
		return rs.getObject(columnName);
	}

	public void updateObject(String columnName, Object x) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateObject(String columnName, Object x, int scale)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public Object getObject(int i, Map map) throws SQLException {
		return rs.getObject(i,map);
	}

	public String getString(String columnName) throws SQLException {
		try {
			return rs.getString(columnName);
		} catch (SQLException e) {// 这里是为了增加一些出错信息的内容便于调试
			throw new SQLException(e.toString() + " columnName=" + columnName);
		}
	}

	public void updateString(String columnName, String x) throws SQLException {
		rs.updateString(columnName,x);
	}

	public BigDecimal getBigDecimal(String columnName) throws SQLException {
		return rs.getBigDecimal(columnName);
	}

	public BigDecimal getBigDecimal(String columnName, int scale)
			throws SQLException {
		return null;
	}

	public void updateBigDecimal(String columnName, BigDecimal x)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public URL getURL(String columnName) throws SQLException {
		return rs.getURL(columnName);
	}

	public Array getArray(String colName) throws SQLException {
		return rs.getArray(colName);
	}

	public void updateArray(String columnName, Array x) throws SQLException {
		rs.updateArray(columnName,x);
	}

	public Blob getBlob(String colName) throws SQLException {
		return rs.getBlob(colName);
	}

	public void updateBlob(String columnName, Blob x) throws SQLException {
		// TODO Auto-generated method stub

	}

	public Clob getClob(String colName) throws SQLException {
		return rs.getClob(colName);
	}

	public void updateClob(String columnName, Clob x) throws SQLException {
		rs.updateClob(columnName,x);
	}

	public Date getDate(String columnName) throws SQLException {
		return rs.getDate(columnName);
	}

	public void updateDate(String columnName, Date x) throws SQLException {
		rs.updateDate(columnName,x);
	}

	public Date getDate(int columnIndex, Calendar cal) throws SQLException {
		return rs.getDate(columnIndex,cal);
	}

	public Ref getRef(String colName) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public void updateRef(String columnName, Ref x) throws SQLException {
		// TODO Auto-generated method stub

	}

	public Time getTime(String columnName) throws SQLException {
		return rs.getTime(columnName);
	}

	public void updateTime(String columnName, Time x) throws SQLException {
		rs.updateTime(columnName,x);
	}

	public Time getTime(int columnIndex, Calendar cal) throws SQLException {
		return rs.getTime(columnIndex,cal);
	}

	public Timestamp getTimestamp(String columnName) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public void updateTimestamp(String columnName, Timestamp x)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public Timestamp getTimestamp(int columnIndex, Calendar cal)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getObject(String colName, Map map) throws SQLException {
		return null;
	}

	public Date getDate(String columnName, Calendar cal) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Time getTime(String columnName, Calendar cal) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Timestamp getTimestamp(String columnName, Calendar cal)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
