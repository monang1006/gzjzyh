<%@ page contentType="text/html; charset=utf-8"%>
<!-- 新增tab页对应内容区 -->
<!-- <div id="tabs-8" height="100%" width="100%">
</div> -->
<script>
/**
 * 动态添加业务扩展Tab页
 */
$(function() {
	//$("#tabs").tabs('add', '#tabs-8', '测试测试');
});

/**
 * 自定义验证接口，在保存时对扩展页面中扩展的业务属性进行正确性验证
 * 1.如果验证正确，则接口返回true
 * 2.如果验证失败，则接口直接alert错误信息，并返回false
 */
function customValidate(){
	// TODO：扩展属性验证逻辑
	return true;
}

/**
 * 配置页面保存时执行的业务接口
 * 1.如果执行正确，则接口返回true
 * 2.如果执行失败，则接口直接alert错误信息，并返回false
 */
function onSave(){
	// TODO：配置页面保存时执行的业务逻辑
	return true;
}

/**
 * 配置页面取消时执行的业务接口
 * 1.如果执行正确，则接口返回true
 * 2.如果执行失败，则接口直接alert错误信息，并返回false
 */
function onCancel(){
	// TODO：配置页面取消时执行的业务逻辑
	return true;
}
</script>