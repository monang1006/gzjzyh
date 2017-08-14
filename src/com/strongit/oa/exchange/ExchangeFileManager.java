package com.strongit.oa.exchange;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.archive.ITempFileService;
import com.strongit.oa.bo.ToaArchiveTempfile;
import com.strongit.oa.bo.ToaArchiveTfileAppend;
import com.strongit.oa.bo.ToaExchange;
import com.strongit.oa.infotable.TreeHelp;
import com.strongit.oa.util.MessagesConst;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

@Service
@Transactional
public class ExchangeFileManager {

	private GenericDAOHibernate<ToaExchange,java.lang.String> exchangeDao;
	
	private ITempFileService tempFileService;
	
	@Autowired
	public void setTempFileService(ITempFileService tempFileService) {
		this.tempFileService = tempFileService;
	}
	
	/**
	 * author:luosy
	 * description:注入sessionFactory
	 * modifyer:
	 * @param sessionFactory
	 */
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		exchangeDao = new GenericDAOHibernate<ToaExchange,java.lang.String>(sessionFactory,ToaExchange.class);
	}
	
	/**
	 * author:luosy
	 * description:调用归档接口
	 * modifyer:
	 * description:
	 */
	public void saveFileToArch(String columnArticleId) throws SystemException,ServiceException{
		try{
			ToaArchiveTempfile arch= new ToaArchiveTempfile();
			ToaArchiveTfileAppend archApp = new ToaArchiveTfileAppend();
			String[] str = columnArticleId.split(";");
				String sql = "SELECT * FROM " +str[0]+" WHERE "+str[1]+" = '"+str[2]+"'";
				ResultSet rs = exchangeDao.executeJdbcQuery(sql);
				if("T_OA_SENDDOC".equals(str[0])){
					while (rs.next()) {
						arch.setTempfileDocId(rs.getString(1));
						arch.setTempfileAuthor(rs.getString(24));
						arch.setTempfileTitle(rs.getString(3));
						arch.setTempfileDate(rs.getDate(28));
//						exchange.setDocNum(rs.getString(23));
//						exchange.setDocContent(rs.getBytes(17));
						
						archApp.setTempappendContent(rs.getBytes(17));
						archApp.setTempappendName(rs.getString(3));
					}
				}
			
			ArrayList<ToaArchiveTfileAppend> list = new ArrayList<ToaArchiveTfileAppend>();
			list.add(archApp);
			HashSet<ToaArchiveTfileAppend> hs = new HashSet<ToaArchiveTfileAppend>(list);
			Set<ToaArchiveTfileAppend> sw = hs;
			
			archApp.setToaArchiveTempfile(arch);
			arch.setToaArchiveTfileAppends(sw);
			
				tempFileService.saveTempfile(arch);
			
		} catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error);
		}catch (SQLException e) {
			throw new DAOException();
		}
	}
	
	
	/**
	 * author:luosy
	 * description: 分发公文 by 保存到 ToaExchange
	 * modifyer:
	 * description:
	 * @param columnArticleId
	 */
	public void tranFile(String columnArticleId) throws SystemException,ServiceException{
		try{
			ToaExchange exchange = new ToaExchange();
			String[] str = columnArticleId.split(";");
			String sql = "SELECT * FROM " +str[0]+" WHERE "+str[1]+" = '"+str[2]+"'";
			ResultSet rs = exchangeDao.executeJdbcQuery(sql);
			if("T_OA_SENDDOC".equals(str[0])){
				while (rs.next()) {
					exchange.setDocAuthor(rs.getString(24));
					exchange.setDocNum(rs.getString(23));
					exchange.setDocTitle(rs.getString(3));
					exchange.setSendDate(rs.getDate(28));
					exchange.setDocContent(rs.getBytes(17));
					exchange.setSendEnterprise(rs.getString(13));
					exchangeDao.save(exchange);
				}
			}
		}catch (SQLException e) {
			throw new DAOException();
		} catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error);
		}
	}
	
}
