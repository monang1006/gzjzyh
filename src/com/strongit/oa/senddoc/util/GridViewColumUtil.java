package com.strongit.oa.senddoc.util;

import java.util.List;
import java.util.Map;

/**
 * 列表工具类
 * 
 * @author yanjian
 * 
 * Sep 11, 2012 9:08:55 PM
 */
public class GridViewColumUtil {
	private static final String PNG_COLUM_NAME = "png";

	private static final String PNG_COLUM_TYPE = "PNG";

	/**
	 * 图片列添加图片
	 * 
	 * @author yanjian
	 * @param map
	 * @param picImage
	 *            Sep 11, 2012 9:08:52 PM
	 */
	@SuppressWarnings("unchecked")
	public static void addPngColumICO(Map map, StringBuilder picImage) {
		if (!map.containsKey(PNG_COLUM_NAME)) {
			map.put(PNG_COLUM_NAME, picImage.toString());
		}
	}

	/**
	 * 添加图片列
	 * 
	 * @author yanjian
	 * @param list
	 *            Sep 11, 2012 9:18:54 PM
	 */
	@SuppressWarnings("unchecked")
	public static void addPngColum(List showColumnList) {
		if (showColumnList != null) {
			showColumnList.add(new String[] {
					GridViewColumUtil.getPNGColumName(), "",
					GridViewColumUtil.getPNGColumType(),
					GridViewColumUtil.getPNGColumName() });// 显示红黄警示牌
		}
	}

	/**
	 * 返回图片列名称
	 * 
	 * @author yanjian
	 * @return Sep 11, 2012 9:14:29 PM
	 */
	private static String getPNGColumName() {
		return PNG_COLUM_NAME;
	}

	/**
	 * 返回图片列类型
	 * 
	 * @author yanjian
	 * @return Sep 11, 2012 9:15:24 PM
	 */
	private static String getPNGColumType() {
		return PNG_COLUM_TYPE;
	}

}
