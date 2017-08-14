package com.strongit.oa.personnel.personorg;

import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.context.SecurityContext;
import org.springframework.security.Authentication;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.IGenericDAO;
import com.strongit.oa.common.user.util.PropertiesUtil;
import com.strongit.oa.common.user.util.Const;
import com.strongit.uums.security.UserInfo;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * <p>Title: StrongUUMS</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) Strong 2008</p>
 * <p>Company: </p>
 * @author minhongbin@hotmail.com
 * @version 1.0
 */
public class BaseOrgManager {

  /**
   * 构造方法。
   */
  public BaseOrgManager() {
  }

  /**
   * 得到当前登录用户。
   * @return User - 当前登录用户VO
   */
  public UserInfo getCurrentUserInfo() {
    SecurityContext context = SecurityContextHolder.getContext();
    Authentication authentication = context.getAuthentication();
    return (UserInfo) authentication.getPrincipal();
  }

  /**
   * 校验代码编码规则。
   * @param codeRule - 编码规则
   * @param code - 代码
   * @throws SystemException
   * @throws DAOException
   * @throws ServiceException
   */
  protected void checkCodeRule(String codeRule, String code) throws SystemException, DAOException, ServiceException {
    //空值或空串处理
    if (code != null && code.trim().length() > 0) {
      int lenCode = code.length();
      //分析编码规则
      int len = 0;
      for (int i = 0; i < codeRule.length(); i++) {
        String str = codeRule.substring(i, i + 1);
        len += Integer.parseInt(str);
        if (len == lenCode) {
          break;
        }
        else if (len > lenCode) {
          throw new ServiceException(code + "不符合代码编码规范。");
        }
      }
    }
  }

  /**
   * 得到父级代码。
   * @param codeRule - 编码规则
   * @param code - 代码
   * @return String - 父级代码
   * @throws SystemException
   * @throws DAOException
   * @throws ServiceException
   */
  public String getParentCode(String codeRule, String code) throws SystemException, DAOException, ServiceException {
    String parentCode = null;
    if (code != null && codeRule != null) {
      //校验代码编码规则
      this.checkCodeRule(codeRule, code);
      //分析编码规则
      List<Integer> lstCodeRule = new ArrayList<Integer> ();
      for (int i = 0; i < codeRule.length(); i++) {
        String str = codeRule.substring(i, i + 1);
        lstCodeRule.add(new Integer(str));
      }
      int codeLength = code.length();
      int count = 0;
      for (Iterator<Integer> iter = lstCodeRule.iterator(); iter.hasNext(); ) {
        Integer len = iter.next();
        count += len.intValue();
        if (count == codeLength) {
          count -= len.intValue();
          parentCode = code.substring(0, count);
        }
      }
    }
    return parentCode;
  }

  public static void main(String[] args) {
	  BaseOrgManager manager = new BaseOrgManager();
    String code = manager.getParentCode("4444444444444", "4444");
    System.out.println(code);
  }

  /**
   * 得到下一个代码编码。
   * @param dao - DAO对象
   * @param tableName - 表名(BO对象名称)
   * @param fieldName - 字段名(BO对象属性名)
   * @param ruleType - 规则类型(com.strongit.uums.util.Const.RULE_TYPE_PARALLEL - 同级增加 / com.strongit.uums.util.Const.RULE_TYPE_NEXT - 下级增加)
   * @param ruleCode - 规则代码,如com.strongit.uums.util.Const.orgRule、com.strongit.uums.util.Const.privil等等
   * @param code - 代码
   * @return String
   */
  protected String getNextCode(IGenericDAO dao, String tableName, String fieldName, int ruleType, String ruleCode,
                               String code) throws SystemException, DAOException,
      ServiceException {
    String nextCode = null;
    try {
      //空值或空串处理
      if (code == null) {
        code = "";
      }
      else {
        code = code.trim();
      }
      //得到代码编码规则字符串
      String codeRule = PropertiesUtil.getCodeRule(ruleCode);
      //校验代码编码规则
      this.checkCodeRule(codeRule, code);
      //分析编码规则
      List lstCodeRule = new ArrayList();
      for (int i = 0; i < codeRule.length(); i++) {
        String str = codeRule.substring(i, i + 1);
        lstCodeRule.add(new Integer(str));
      }
      int lenAll = 0, level = 0;
      for (int i = 0; i < lstCodeRule.size(); i++) {
        Integer len = (Integer) lstCodeRule.get(i);
        lenAll += len.intValue();
        if (lenAll <= code.length()) {
          level++;
        }
      }
      //同级增加
      if (ruleType == Const.RULE_TYPE_PARALLEL) {
        Integer len = (Integer) lstCodeRule.get(level-1);
        String str = "1" + code.substring(code.length() - len.intValue());
        Integer next = new Integer(Integer.valueOf(str).intValue() + 1);
        str = next.toString();
        if ("1".equals(str.substring(0, 1))) {
          nextCode = code.substring(0, code.length() - len.intValue()) + str.substring(1);
        }
        else {
          nextCode = code;
        }
      }
      //下级增加
      else {
        //得到数据库最大代码值
        StringBuffer sql = new StringBuffer();
        sql.append("select max(bo.")
            .append(fieldName)
            .append(") from ")
            .append(tableName)
            .append(" as bo where bo.")
            .append(fieldName)
            .append(" like '")
            .append(code)
            .append("%'");
        String hql = sql.toString();
        List lst = dao.find(hql);
        String newCode = null;
        if (lst != null && lst.size() > 0) {
          newCode = (String) lst.get(0);
          if (newCode != null) {
            newCode = newCode.trim();
            //校验代码编码规则
            this.checkCodeRule(codeRule, newCode);
          }
        }
        if(newCode == null || "".equals(newCode)){
        	newCode = "0000000000".substring(0, (Integer)lstCodeRule.get(level));
        }
        if (newCode != null && newCode.length() > 0 && lenAll > code.length()) {
          Integer len = (Integer) lstCodeRule.get(level);
          String str = null;
          if (newCode.length() > code.length()) {
            str = "1" + newCode.substring(code.length(), code.length() + len.intValue());
          }
          else {
            str = "1000000000".substring(0, len.intValue() + 1);
          }
          Integer next = new Integer(Integer.valueOf(str).intValue() + 1);
          str = next.toString();
          if ("1".equals(str.substring(0, 1))) {
            nextCode = code + str.substring(1);
          }
          else {
            nextCode = newCode.substring(0, code.length() + len.intValue());
          }
        }
        else {
          nextCode = code;
        }
      }
    }
    catch (ServiceException ex) {
      throw ex;
    }
    catch (DAOException ex) {
      throw ex;
    }
    catch (SystemException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new SystemException(ex);
    }
    return nextCode;
  }
  
  /**
   * 根据传入的表名和字段名查找下一个排序号
   * @author  喻斌
   * @date    Feb 10, 2009  4:07:36 PM
   * @param dao -dao对象
   * @param tableName -表名
   * @param fieldName -字段名
   * @return String 下一个排序号
   * @throws DAOException
   * @throws SystemException
   * @throws ServiceException
   */
  public String getNextSequence(IGenericDAO dao, String tableName
		  , String fieldName) throws DAOException, SystemException, ServiceException {
      String nextSequence;
	  StringBuffer sql = new StringBuffer("")
      	  .append("select max(bo.")
          .append(fieldName)
          .append(") from ")
          .append(tableName)
          .append(" as bo");
      
      String maxSequence = String.valueOf(dao
    		  	.findUnique(sql.toString(), new Object[]{}));
      if(maxSequence == null || "".equals(maxSequence) || 
    		  "null".equals(maxSequence)){
    	  maxSequence = "0";
/*    	  for(int i= 0; i< Const.INFO_SEQUENCE_LENGTH; i++){
    		  maxSequence = maxSequence + "0";
    	  }*/
      }
	  nextSequence = String.valueOf(Long.valueOf(maxSequence) + 1);
	  if(nextSequence.length() > Const.INFO_SEQUENCE_LENGTH){
		  throw new SystemException("排序字段超过最大值!");
	  }else{
		  int index = nextSequence.length();
		  for(int i = Const.INFO_SEQUENCE_LENGTH; i > index; i--){
			  nextSequence = "0" + nextSequence;
		  }
	  }
      return nextSequence;
  }
  
  /**
   * 根据传入的参数查找要与当前节点交换位置的节点信息
   * @author  喻斌
   * @date    Feb 11, 2009  9:00:36 AM
   * @param dao -DAO对象
   * @param moveDownOrUp -上下移动标识<br>
   * 	<p>down:下移</p>
   * 	<p>see com.strongit.uums.util.MOVE_DOWN_IN_TREE</p>
   * 	<P>up:上移</P>
   * 	<P>see com.strongit.uums.util.MOVE_UP_IN_TREE</P>
   * @param tableName -表名
   * @param sequenceFieldName -排序号字段名
   * @param sysCodeFieldName -对象系统编码字段名
   * @param currentSequence -当前节点排序号
   * @param codeRule -编码规则
   * @param currentCode -当前节点系统编码
   * @return Object 要与当前节点交换位置的节点信息<br>
   * 	<p>具体类型转换需要到调用该方法的Service里面</p>
   * 	<p>若已经是最上层或最下层则返回null</p>
   * @throws DAOException
   * @throws SystemException
   * @throws ServiceException
   */
  public Object getObjExchangePositionInTree(IGenericDAO dao
		  , String moveDownOrUp, String tableName, String sequenceFieldName
		  , String sysCodeFieldName, String currentSequence, String codeRule
		  , String currentCode) throws DAOException, SystemException, ServiceException {
	  StringBuffer sql;
	  String parentCode = this.getParentCode(codeRule, currentCode);
	  if(parentCode == null || "".equals(parentCode)){
		  parentCode = currentCode;
	  }
	  /**
	   * 该节点排序字段为空
	   */
	  if(currentSequence == null || "".equals(currentSequence)){
		  throw new SystemException("001");
	  }
	  
	  if(Const.MOVE_DOWN_IN_TREE.equals(moveDownOrUp)){
		  sql = new StringBuffer("")
		  		.append("from ")
		  		.append(tableName)
		  		.append(" as bo")
		  		.append(" where bo.")
		  		.append(sequenceFieldName)
		  		.append(" > ? and bo.")
		  		.append(sysCodeFieldName)
		  		.append(" like '")
		  		.append(parentCode);
		  for(int i = 0; i < currentCode.length()-parentCode.length(); i++){
			  sql.append("_");
		  }
		  sql.append("' order by bo.")
		  		.append(sequenceFieldName)
		  		.append(" asc,")
		  		.append(sysCodeFieldName)
		  		.append(" asc");
		  	
	  }else if(Const.MOVE_UP_IN_TREE.equals(moveDownOrUp)){
		  sql = new StringBuffer("")
		  		.append("from ")
		  		.append(tableName)
		  		.append(" as bo")
		  		.append(" where bo.")
		  		.append(sequenceFieldName)
		  		.append(" < ? and bo.")
		  		.append(sysCodeFieldName)
		  		.append(" like '")
		  		.append(parentCode);
		  for(int i = 0; i < currentCode.length()-parentCode.length(); i++){
			  sql.append("_");
		  }
		  sql.append("' order by bo.")
		  		.append(sequenceFieldName)
		  		.append(" desc,")
		  		.append(sysCodeFieldName)
		  		.append(" desc");		  
	  }else{
		  throw new SystemException("不存在此标识!");
	  }
	  
	  List list = dao.find(sql.toString(), new Object[]{currentSequence});
	  if(list.isEmpty()){
		  return null;
	  }else{
		  return list.get(0);
	  }
  }
  /**
   * 根据输入的编码判断其唯一性 ，统一方法
   *@author 蒋国斌
   *@date 2009-2-25 下午02:10:27 
   * @param dao
   * @param tableName
   * @param sysCodeFieldName
   * @param currentCode
   * @return
   * @throws DAOException
   * @throws SystemException
   * @throws ServiceException
   */
  public boolean getObjBycode(IGenericDAO dao
		  , String tableName, String sysCodeFieldName, 
		   String currentCode) throws DAOException, SystemException, ServiceException {
	  StringBuffer sql;
	  
		  sql = new StringBuffer("")
		  		.append("from ")
		  		.append(tableName)
		  		.append(" as bo")
		  		.append(" where bo.")
		  		.append(sysCodeFieldName)
		  		.append(" like '")
		  		.append(currentCode)
		  		.append("'");
		  
	  List list = dao.find(sql.toString());
	  if(list.size()==0){
		  return true;
	  }else{
		  return false;
	  }
  }
  /**
   * 校验代码编码规则。
   *@author 蒋国斌
   *@date 2009-2-27 上午11:39:19 
   * @param codeRule
   * @param code
   * @return
   * @throws SystemException
   * @throws DAOException
   * @throws ServiceException
   */
  protected boolean checkCode(String codeRule, String code){
	    try
	    {
	    	this.checkCodeRule(codeRule, code);
	    }catch(ServiceException ex){
	    	return false;	
	    }
	    return true;
	  }


}
