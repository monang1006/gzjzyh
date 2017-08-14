package com.strongit.oa.util;

import java.util.LinkedList;
import java.util.List;

public class NodeNameConst {
	public static final String BAN_WEN = "0";// 办文

	public static final String SONG_LING_DAO = "1";// 送领导

	public static final String HUI_WEN = "2";// 会文

	public static final String YI_JIAN_ZHENG_XUN = "3";// 意见征询

	public static final String BAN_JIE = "4";// 办结

	/**
	 * 返回对应的节点描绘信息
	 * 
	 * @author 严建
	 * @param nodenameconst
	 * @return
	 * @createTime Jul 2, 2012 10:42:44 AM
	 */
	public static List<String> getNodeName(String nodenameconst) {
		List<String> results = new LinkedList<String>();
		if (BAN_WEN.equals(nodenameconst)) {
			results.add("主办人员办理");
			results.add("主办人员");
			results.add("拟稿");
			results.add("拟稿人呈领导");
		} else if (SONG_LING_DAO.equals(nodenameconst)) {
			results.add("处领导审核");
			results.add("市长审签");
			results.add("分管副市长审签");
			results.add("分管厅领导、副秘书长、秘书长审签");

			results.add("分管处领导审核");
			results.add("处室负责人审核");
			results.add("林书记核稿");
			results.add("市长签发");
			results.add("其他市领导审签");
			results.add(" 林书记审核");
			results.add("副秘书长、秘书长审签");
			results.add("厅领导审签");
			results.add("处室领导复审");
		} else if (HUI_WEN.equals(nodenameconst)) {
			results.add("处室会文");
		} else if (YI_JIAN_ZHENG_XUN.equals(nodenameconst)) {
			results.add("转征求意见");
		} else if (BAN_JIE.equals(nodenameconst)) {
			results.add("BAN_JIE");
			results.add("结束");
		}
		return results;
	}
}
