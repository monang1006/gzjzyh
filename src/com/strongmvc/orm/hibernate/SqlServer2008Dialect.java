package com.strongmvc.orm.hibernate;

import java.sql.Types;  
  
import org.hibernate.Hibernate;  
import org.hibernate.dialect.SQLServerDialect;  
import org.hibernate.dialect.function.NoArgSQLFunction;


public class SqlServer2008Dialect extends SQLServerDialect
{
  public SqlServer2008Dialect()
  {
	super();
    registerColumnType(91, "date");
    registerColumnType(92, "time");
    registerColumnType(93, "datetime2");
    
    registerHibernateType(1, "string");   
    registerHibernateType(-9, "string");   
    registerHibernateType(-16, "string");   
    registerHibernateType(3, "double");
    
    registerHibernateType(Types.CHAR, Hibernate.STRING.getName());   
	registerHibernateType(Types.VARCHAR, Hibernate.STRING.getName());   
	//registerHibernateType(Types.DECIMAL, Hibernate.DOUBLE.getName());
	
	//将text改为clob
	registerHibernateType(Types.LONGVARCHAR, Hibernate.CLOB.getName());
	//将decimal转为numberic
	registerHibernateType(Types.DECIMAL, Hibernate.BIG_DECIMAL.getName());

    registerFunction("current_timestamp", new NoArgSQLFunction("current_timestamp", Hibernate.TIMESTAMP, false));
  }
}