/**
 * 定义办公厅意见征询操作
 * 
 */
var yjzxGroupid = ("${yjzxGroupId}"==""?"8a818b89335858c2013358b359cc0040":"${yjzxGroupId}");// 设置意见征询分组信息id
var enableYjzx = false;// 是否启用了办公厅意见征询机制，默认是不启用
/*
 * 设置是否启用办公厅意见征询机制 @param groupid 分组迁移线id
 */

function setEnableYjzx(groupid) {
	if (groupid == "8a818b89335858c2013358b359cc0040") {
		enableYjzx = true;
	} else {
		enableYjzx = false;
	}
}
/*
 * 获取是否启用办公厅意见征询机制 @param groupid 分组迁移线id
 */
function isEnableYjzx() {
	return enableYjzx;
}
