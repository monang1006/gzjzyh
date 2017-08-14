package com.strongit.oa.archive.archivefolder;

import com.strongit.oa.bo.ToaArchiveFolder;
import com.strongit.oa.util.TempPo;
import com.strongit.tag.web.tree.IDealTreeNode;
/**
 * 案卷树
 * @author 胡丽丽
 * @date 2010-01-18
 *
 */
public class DealForderTreeNode extends IDealTreeNode{

	@Override
	public String getClick() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNodeImg() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTagNodeId() {
		// TODO Auto-generated method stub
		return ((ToaArchiveFolder) obj).getFolderId();
	}

	@Override
	public String getTagNodeName() {
		// TODO Auto-generated method stub
		return ((ToaArchiveFolder) obj).getFolderName();
	}

	@Override
	public String getTagParentId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUrl() {
		// TODO Auto-generated method stub
		return null;
	}

}
