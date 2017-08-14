package com.strongit.oa.util;

/**
 * 办理模式Const
 * 
 * @author 严建
 * @company Strongit Ltd. (C) copyright
 * @date Jul 1, 2012 4:09:27 PM
 * @version 1.0.0.0
 * @classpath com.strongit.oa.util.DoNextModelConst
 */
public class DoNextModelConst {
	/**
	 * @field ISPOP 弹出模式
	 */
	public static final String ISPOP = "isPop";

	/**
	 * @field ISMENUBUTTON 菜单模式
	 */
	public static final String ISMENUBUTTON = "isMenuButton";

	/**
	 * @field ISOPEN 展开模式
	 */
	public static final String ISOPEN = "isOpen";

	/**
	 * @field ISSUGGESTIONBUTTON 快捷模式
	 */
	public static final String ISSUGGESTIONBUTTON = "isSuggestionButton";

	/**
	 * @field ISNOMAL 正常模式
	 */
	public static final String ISNOMAL = "isNomal";

	/**
	 * 是否为弹出模式
	 * 
	 * @author 严建
	 * @param doNextModel
	 * @return
	 * @createTime Jul 1, 2012 4:24:16 PM
	 */
	public static boolean isPop(String doNextModel) {
		return ISPOP.equals(doNextModel);
	}

	/**
	 * 是否为菜单模式
	 * 
	 * @author 严建
	 * @param doNextModel
	 * @return
	 * @createTime Jul 1, 2012 4:24:16 PM
	 */
	public static boolean isMenuButton(String doNextModel) {
		return ISMENUBUTTON.equals(doNextModel);
	}

	/**
	 * 是否为展开模式
	 * 
	 * @author 严建
	 * @param doNextModel
	 * @return
	 * @createTime Jul 1, 2012 4:24:16 PM
	 */
	public static boolean isOpen(String doNextModel) {
		return ISOPEN.equals(doNextModel);
	}

	/**
	 * 是否为快捷模式
	 * 
	 * @author 严建
	 * @param doNextModel
	 * @return
	 * @createTime Jul 1, 2012 4:24:16 PM
	 */
	public static boolean isSuggestionButton(String doNextModel) {
		return ISSUGGESTIONBUTTON.equals(doNextModel);
	}

	/**
	 * 是否为正常模式
	 * 
	 * @author 严建
	 * @param doNextModel
	 * @return
	 * @createTime Jul 1, 2012 4:24:16 PM
	 */
	public static boolean isNomal(String doNextModel) {
		return ISNOMAL.equals(doNextModel);
	}
}
