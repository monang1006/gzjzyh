package com.strongit.oa.infotable;

import com.strongit.oa.bo.ToaSysmanageProperty;
import com.strongit.oa.bo.ToaSysmanageStructure;
import com.strongit.oa.infotable.infoitem.InfoItemManager;
import com.strongit.oa.infotable.infoset.InfoSetManager;
import com.strongmvc.exception.SystemException;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service("TranslateService") //服务名必须是"TranslateService"
@Transactional(readOnly = true)
public class TranslateServiceImpl {

	private InfoItemManager itemanager;

	private InfoSetManager infosetmanager;
	
	@Autowired
	public void setInfosetmanager(InfoSetManager infosetmanager) {
		this.infosetmanager = infosetmanager;
	}

	@Autowired
	public void setItemanager(InfoItemManager itemanager) {
		this.itemanager = itemanager;
	}

	/**
	   * 得到所有表的中文表名。
	   * @return Map - 其中key为表名，value为中文表名。
	   * @throws SystemException
	   * @throws DAOException
	   * @throws ServiceException
	   */
	public Map GetColumnNames(String arg0) throws SystemException, DAOException, ServiceException {
		// TODO Auto-generated method stub
		Map<String,String> tables = new HashMap<String,String>();
		List list = itemanager.getAllCreatedItemsByValue(arg0);
		for(int i=0;i<list.size();i++){
			ToaSysmanageProperty infoitem = (ToaSysmanageProperty)list.get(i);
			tables.put(infoitem.getInfoItemField(), infoitem.getInfoItemSeconddisplay());
		}
		return tables;
	}

	/**
	   * 得到指定表的表列中文名。
	   * @param tablename - 表名
	   * @return Map - 其中key为列名，value为中文列名。
	   * @throws SystemException
	   * @throws DAOException
	   * @throws ServiceException
	   */
	public Map GetSequenceNames() throws SystemException, DAOException, ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	   * 得到所有序列的中文序列名。
	   * @return Map - 其中key为序列名，value为中文序列名。
	   * @throws SystemException
	   * @throws DAOException
	   * @throws ServiceException
	   */
	public Map GetTableNames() throws SystemException, DAOException, ServiceException {
		// TODO Auto-generated method stub
		Map<String,String> tables = new HashMap<String,String>();
		List<ToaSysmanageStructure> list = infosetmanager.getAllSets();
		for(int i=0;i<list.size();i++){
			ToaSysmanageStructure infoset = list.get(i);
			tables.put(infoset.getInfoSetValue(), infoset.getInfoSetName());
		}
		return tables;
	}
	
}
