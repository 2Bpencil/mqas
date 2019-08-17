//父id
// paid = "";
// meid = "";
// mename = "";
var heads=['年级班级名称','排序'];
$(document).ready(function(){
    showTreeTable();
    validateData();


});

/**
 * 初始化菜单树表格
 *
 */
function showTreeTable() {
    $("#treeTable").empty();
    $.ajax({
        type : 'POST',
        url : contextPath + 'classes/getAllClassess',
        beforeSend : function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        dataType : "json",
        success : function(result) {
            $.TreeTable("treeTable",heads,result);
        }
    });
}

/**
 * 清空pid
 */
function clearPid(){
    paid = "";
    meid = "";
    mename = "";

}

/**
 * 新增
 */
function addClasses(){

    if(meid == ""){
        showModal("classesModal");
    }else {
        $("#form_pid").val(meid);
        $("#form_parent").val(mename);
        showModal("classesModal");
    }
}
/**
 * 保存
 */
function saveClasses(){
//保存
    $.ajax({
        type : "POST",
        data : $("#classesForm").serialize(),
        url : contextPath+"classes/saveOrEditEntity",
        beforeSend : function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        dataType : "json",
        success: function(result){
            if(result == 1){
                hideModal('classesModal');
                showTreeTable();
                clearForm();
                showAlert("保存成功",'success');
            }else{
                showAlert("保存失败",'error');
            }
        }
    });
}

/**
 * 编辑
 */
function editClasses(){
    if(meid==""){
        swal({
            title: "提示",
            text: "请选中要编辑的项！"
        });
        return;
    }
    $.ajax({
        type : "POST",
        data : {id:meid},
        url : contextPath+"classes/getEditEntityInfo",
        beforeSend : function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        dataType : "json",
        success: function(result){
            $('#form_parent').val(result.parent);
            $('#form_id').val(result.id);
            $('#form_name').val(result.name);
            $('#form_pid').val(result.pid);
            $('#form_sort').val(result.sort);
            showModal("classesModal");
        }
    });
}

/**
 * 删除
 */
function deleteClasses(){
    if(meid == ""){
        swal({
            title: "提示",
            text: "请选中要删除的项！"
        });
        return;
    }
    var node = jQuery('#treeTable').treetable('childs', meid);
    getNodes(node);
    swal({
        title: "是否确定删除?",
        text: "你将会删除这条记录以及其所有下级记录!",
        type: "warning",
        showCancelButton: true,
        confirmButtonColor: "#DD6B55",
        confirmButtonText: "Yes, delete it!",
        closeOnConfirm: false
    }, function () {
        $.ajax({
            type : "POST",
            data : {ids:ids},
            dataType:"json",
            url : contextPath+"classes/deleteClasses",
            beforeSend : function(xhr) {
                xhr.setRequestHeader(header, token);
            },
            success: function(result){
                if(result == 1){
                    ids="";
                    showTreeTable();
                    swal("删除成功!", "", "success");
                }else{
                    swal("删除失败!", "", "error");
                }
            }
        });
    });
    // $.ajax({
    //     type : "POST",
    //     data : {ids:ids},
    //     dataType:"json",
    //     url : contextPath+"classes/checkClassesUsed",
    //     beforeSend : function(xhr) {
    //         xhr.setRequestHeader(header, token);
    //     },
    //     success: function(result){
    //         if(result){
    //
    //         }else{
    //             swal("有菜单被分配，不能删除!", "", "error");
    //         }
    //     }
    // });
}
//删除用
var ids="";
function getNodes(node){
    if(ids==""){
        ids += node.id;
    }else {
        ids += ","+ node.id;
    }
    if(node.children.length > 0){
        for(var i = 0;i < node.children.length;i++){
            getNodes(node.children[i]);
        }
    }
}
//******************************************************表单验证*****************************************************************
/**
 * 验证数据
 */
function validateData(){
     $("#classesForm").validate({
        rules: {
            name: {
                required: true,
                maxlength: 100,
                remote : {//远程地址只能输出"true"或"false"
                    url : contextPath + "classes/verifyTheRepeat",
                    type : "POST",
                    dataType : "json",//如果要在页面输出其它语句此处需要改为json
                    beforeSend : function(xhr) {
                        xhr.setRequestHeader(header, token);
                    },
                    data : {
                        id : function(){
                            return $("#form_id").val();
                        },
                        pid : function(){
                            return $("#form_pid").val();
                        }
                    }
                },
            },
            sort:{
                digits:true
            }

        },
        messages : {
            name : {
                required : "不能为空",
                maxlength : "不超过100个字符",
                remote : "班级名已存在",
            },
            sort:{
                digits:"请输入正整数"
            }
        },
        submitHandler : function(form) {
            saveClasses();
        }
    });
}
/**
 * 清空表单
 */
function clearForm(){
    $('#classesForm')[0].reset();
    $('#form_pid').val(null);
    $('#form_id').val(null);
    $('#classesForm').validate().resetForm();
    paid = "";
    meid = "";
    mename = "";
    ids = "";
}