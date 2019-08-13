
var heads=['菜单名称','菜单代码','路径','菜单类型','排序'];
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
        type : 'GET',
        url : contextPath + 'menu/getAllMenus',
        dataType : "json",
        success : function(result) {
            console.log(result);
            $.TreeTable("treeTable",heads,result);
        }
    });
}
var ids="";
function getNodes(node){
    ids += node.id+",";
    if(node.children.length > 0){
        for(var i = 0;i < node.children.length;i++){
            getNodes(node.children[i]);
        }
    }
}

/**
 * 保存
 */
function saveMenu(){
//保存
    $.ajax({
        type : "GET",
        data : $("#menuForm").serialize(),
        url : contextPath+"menu/saveOrEditEntity",
        success: function(result){
            if(result == 1){
                hideModal('menuModal');
                clearForm();
                reloadTable();
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
function editMenu(){

}

//******************************************************表单验证*****************************************************************
/**
 * 验证数据
 */
function validateData(){
     $("#menuForm").validate({
        rules: {
            code: {
                required: true,
                maxlength: 20,
                remote : {//远程地址只能输出"true"或"false"
                    url : contextPath + "menu/verifyTheRepeat",
                    type : "get",
                    dataType : "json",//如果要在页面输出其它语句此处需要改为json
                    data : {
                        id : function(){
                            return $("#form_id").val();
                        }
                    }
                },
            },
            name: {
                required: true,
                maxlength: 20
            },
            url:{
                required: true,
                maxlength: 100
            },
            type:{
                required: true,
            },
            sort:{
                digits:true
            }

        },
        messages : {
            code : {
                required : "不能为空",
                maxlength : "不超过20个字符",
                remote : "角色名已存在",
            },
            name : {
                required: "不能为空",
                maxlength : "不超过20个字符",
            },
            url:{
                required: "不能为空",
                maxlength: "不超过100个字符",
            },
            type:{
                required: "请选择类型",
            },
            sort:{
                digits:"请输入正整数"
            }
        },
        submitHandler : function(form) {
            saveMenu();

        }
    });
}
/**
 * 清空表单
 */
function clearForm(){
    $('#menuForm')[0].reset();
    $('#form_type').val("");
    $('#menuForm').validate().resetForm();
}