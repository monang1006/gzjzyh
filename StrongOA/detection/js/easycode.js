/**
 * 此脚本目的为节省代码工作量,能少点是一点
 * @copyright digitalChina 2004
 * @author nie shu
 * @date 2004.10.20
*/
function test(){
    alert("test easycode");
}
/**
 * @parm service
 */
function checkServiceReturn(service){
    if(service.getCode() == "2000")return true;
    else{
        showMessage(service.getMessage());
        return false;
    }
}
/**
 *@parm datawindow
 */
function delete_Row(dw_obj){
    if (dw_obj.rowCount != -1){
        if (confirm("确定删除该行数据吗？"))  {
            dw_obj.deleteRow(dw_obj.getCurRow());
            dw_obj.selectRow(0);
            return true;
            }
        }
    return false;
}