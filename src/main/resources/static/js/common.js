/**
 * 关闭Modal
 */
function hideModal(id){
    $("#"+id ).modal('hide');
}

/**
 * 显示Modal
 * @param id
 */
function showModal(id){
    $("#"+id ).modal('show');
}

/**
 * 弹出提示框
 * @param msg
 * @param type  success,error
 */
function showAlert(msg,type){
    swal({
        title: msg,
        text: "",
        type: type
    });
}