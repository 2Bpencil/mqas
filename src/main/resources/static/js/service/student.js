var classesId = '';
var studentTable;//表格对象
var studentValidator;//表单验证
var $table = "#student_table";
$(document).ready(function(){
    initTree()
    validateData();
});
/**
 * 初始化表格
 */
function initTable(){
	studentTable = $($table).DataTable({
        dom: '<"html5buttons"B>lTfgitp',
        "serverSide": true,     // true表示使用后台分页
        "ajax": {
            "url": contextPath+"student/getTableJson",  // 异步传输的后端接口url
            "type": "POST",      // 请求方式
            beforeSend : function(xhr) {
                xhr.setRequestHeader(header, token);
            },
            data:{classesId:classesId},
        },
        "columns": [
            { "data": "id",
                "visible": false ,
                "searchable":false,
            },
            { "data": "name",
                render : CONSTANT.DATA_TABLES.RENDER.ELLIPSIS,
                width: '20%'

            },
            { "data": "age",
                "searchable":false,
                width: '20%'
            },
            { "data": "sex",
                "searchable":false,
                render : function(data,type, row, meta) {
                    return ( parseInt(data) === 1?"男":"女");
                },
                width: '20%'
            },
            { "data": "phone",
                "searchable":false,
                'orderable' : false ,
                width: '20%'
            },
            { "data": null,
                "searchable":false,
                'orderable' : false ,
                width: '20%',
                'render':function (data, type, row, meta) {
                //data  和 row  是数据
                    var buttons = '';
                    buttons+='<button type="button" onclick="editStudent('+data.id+')" class="btn btn-primary btn-xs" >编辑</button>&nbsp;&nbsp;';
                    buttons+='<button type="button" onclick="deleteStudent('+data.id+')" class="btn btn-primary btn-xs" >删除</button>&nbsp;&nbsp;';
                    buttons+='<button type="button" onclick="uploadWrongQuestion('+data.id+')" class="btn btn-primary btn-xs"  >上传错题</button>';
                    return buttons;
                }
            },
        ],
        "order": [[ 0, 'desc' ]],
        buttons: [
            { extend: 'copy'},
            {extend: 'csv'},
            {extend: 'excel', title: 'ExampleFile'},
            {extend: 'pdf', title: 'ExampleFile'},

            {extend: 'print',
                customize: function (win){
                    $(win.document.body).addClass('white-bg');
                    $(win.document.body).css('font-size', '10px');

                    $(win.document.body).find('table')
                        .addClass('compact')
                        .css('font-size', 'inherit');
                }
            }
        ],
        language:CONSTANT.DATA_TABLES.DEFAULT_OPTION.language,
        autoWidth:false,
        processing: true,
    });
    $($table).on( 'error.dt', function ( e, settings, techNote, message ){
        //这里可以接管错误处理，也可以不做任何处理
    }).DataTable();
}
/**
 * 验证数据
 */
function validateData(){
    jQuery.validator.addMethod("cellPhone", function(value, element) {
        return this.optional(element)
            || /^1[0-9]\d{1}\d{4}\d{4}( x\d{1,6})?$/.test(value);
    }, "联系方式无效");
    studentValidator= $("#studentForm").validate({
        rules: {
            // authority: {
            //     required: true,
            //     maxlength: 50,
            //     remote : {//远程地址只能输出"true"或"false"
            //         url : contextPath + "student/verifyTheRepeat",
            //         type : "POST",
            //         dataType : "json",//如果要在页面输出其它语句此处需要改为json
            //         beforeSend : function(xhr) {
            //             xhr.setRequestHeader(header, token);
            //         },
            //         data : {
            //             id : function(){
            //                 return $("#form_id").val();
            //             }
            //         }
            //     },
            // },
            name: {
                required: true,
                maxlength: 50
            },
            age:{
                required: true,
                digits:true,
            },
            sex:{
                required: true,
            },
            phone: {
                cellPhone : 'required'
            },


        },
        messages : {
            // authority : {
            //     required : "不能为空",
            //     maxlength : "不超过50个字符",
            //     remote : "角色名已存在",
            // },
            name : {
                required: "不能为空",
                maxlength : "不超过50个字符",
            },
            age:{
                required: "不能为空",
                digits:"请输入正整数",
            },
            sex:{
                required: "请选择性别",
            },
            phone: {

            },
        },
        submitHandler : function(form) {
            saveStudent();

        }
    });
}
/**
 * 保存
 */
function saveStudent(){
    $('#form_classesId').val(classesId);
    //保存
    $.ajax({
        type : "POST",
        data : $("#studentForm").serialize(),
        url : contextPath+"student/saveOrEditEntity",
        beforeSend : function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function(result){
            if(result == 1){
                hideModal('studentModal');
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
 * 新增学生
 */
function addStudent(){
    if(classesId == ""){
        swal({
            title: "提示",
            text: "请选选择班级！"
        });
        return;
    }
    showModal("studentModal");
}
/**
 * 编辑
 * @param id
 */
function editStudent(id){
    $.ajax({
        type : "POST",
        data : {id:id},
        dataType:"json",
        url : contextPath+"student/getEntityInfo",
        beforeSend : function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function(result){
            $('#form_id').val(result.id);
            $('#form_age').val(result.age);
            $('#form_name').val(result.name);
            $('#form_phone').val(result.phone);
            showModal("studentModal");
        }
    });
}
/**
 * 刷新表格
 */
function reloadTable(){
    studentTable.ajax.reload();
}
/**
 * 删除角色
 * @param id
 */
function deleteStudent(id){
    swal({
        title: "是否确定删除?",
        text: "你将会删除这条记录!",
        type: "warning",
        showCancelButton: true,
        confirmButtonColor: "#DD6B55",
        confirmButtonText: "Yes, delete it!",
        closeOnConfirm: false
    }, function () {
        $.ajax({
            type : "POST",
            data : {id:id},
            dataType:"json",
            url : contextPath+"student/deleteStudent",
            beforeSend : function(xhr) {
                xhr.setRequestHeader(header, token);
            },
            success: function(result){
                if(result == 1){
                    reloadTable();
                    swal("删除成功!", "", "success");
                }else{
                    swal("删除失败!", "", "error");
                }
            }
        });
    });
}

/**
 * 初始化班级树
 */
function initTree(){
    var zNodes = []; //zTree的数据属性
    var setting = { //zTree的参数配置
        data : {
            simpleData : {
                enable : true
            }
        },
        async : {
            enable : true,
            type : "GET",
            url : contextPath+"classes/getClassTreeForStudent",
            beforeSend : function(xhr) {
                xhr.setRequestHeader(header, token);
            },
        },
        callback : {
            onAsyncSuccess : zTreeOnAsyncSuccess,//异步加载树完成后回调函数
            onClick: zTreeOnClick
        }
    };
    $.fn.zTree.init($("#classesGroup"), setting, zNodes);

}

/**
 * 树节点点击事件
 * @param event
 * @param treeId
 * @param treeNode
 */
function zTreeOnClick(event, treeId, treeNode) {
    if(treeNode.value == 1){
        classesId =treeNode.id;
        var parentNode = treeNode.getParentNode();
        $('#className').html(parentNode.name+treeNode.name+"学生列表");
        studentTable.settings()[0].ajax.data={classesId:classesId};
        reloadTable();
    }
}

/*
 * 异步加载树完成后回调函数
 */
function zTreeOnAsyncSuccess(event, treeId, treeNode, msg) {
    var  treeObj = $.fn.zTree.getZTreeObj("classesGroup");
    treeObj.expandAll(true);
    var nodes = treeObj.getNodes();
    var nodeArr = [];
    for (var i = 0; i <nodes.length ; i++) {
        nodeArr = getAllNodes(nodes[i],nodeArr);
    }
    if(nodeArr.length>0){
        classesId= nodeArr[0].id;
        var parentNode = nodeArr[0].getParentNode();
        $('#className').html(parentNode.name+nodeArr[0].name+"学生列表");
    }
    initTable();
}

/**
 * 获取所有叶子节点
 */
function getAllNodes(node,nodeArr){
    if(node.isParent){//是父节点
        for (var i = 0; i <node.children.length ; i++) {
            getAllNodes(node.children[i],nodeArr);
        }
    }else{
        if(node.value==1){
            nodeArr.push(node);
        }
    }
    return nodeArr;
}



/**
 * 清空表单
 */
function clearForm(){
    $('#studentForm')[0].reset();
    $('#form_id').val(null);
    $('#form_classesId').val(null);
    $('#studentForm').validate().resetForm();
}
/**
 * 清空表单
 */
function clearWrongForm(){
    $('#wrongQuestionForm')[0].reset();
}

/**
 * 上传错题
 */
function uploadWrongQuestion(id) {
    $('#form_student_id').val(id);
    showModal('wrongQuestionModal')
}
/**
 * 保存错题
 */
function saveWrongQuestion() {

    // $('#wrongQuestionForm')[0].submit();
    // clearWrongForm();
    // showAlert("保存成功",'success');

    var formData = new FormData();
    formData.append("file",$("#form_file")[0].files[0]);
    formData.append("studentId",$('#form_student_id').val());
    $.ajax({
        type : "POST",
        data : formData,
        contentType : false, //必须
        processData : false,
        dataType:"json",
        url : contextPath+"student/saveWrongQuestion",
        beforeSend : function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function(result){
            if(result == 1){
                hideModal('wrongQuestionModal');
                clearWrongForm();
                showAlert("保存成功",'success');
            }else{
                showAlert("保存失败",'error');
            }
        }
    });

    // $.ajaxFileUpload({  //Jquery插件上传文件
    //     url: contextPath+"student/saveWrongQuestion",
    //     beforeSend : function(xhr) {
    //         xhr.setRequestHeader(header, token);
    //     },
    //     type : "POST",
    //     secureuri: false, //是否需要安全协议，一般设置为false
    //     fileElementId: "form_file", //type="file"的id
    //     dataType: "json",  //返回值类型
    //     data:{studentId:$('#form_student_id').val()},
    //     success: function (result){
    //
    //         if(result == 1){
    //             hideModal('wrongQuestionModal');
    //             clearWrongForm();
    //             showAlert("保存成功",'success');
    //         }else{
    //             showAlert("保存失败",'error');
    //         }
    //     },
    // });
}

/*常量*/
var CONSTANT = {
    DATA_TABLES : {
        DEFAULT_OPTION : { //DataTables初始化选项
            language: {
                "sProcessing":   "处理中...",
                "sLengthMenu":   "每页 _MENU_ 项",
                "sZeroRecords":  "没有匹配结果",
                "sInfo":         "当前显示第 _START_ 至 _END_ 项，查询到 _TOTAL_ 项，共_MAX_项。",//共 _TOTAL_ 项  搜索到_TOTAL_/_MAX_条
                "sInfoEmpty":    "当前显示第 0 至 0 项，共 0 项",
                "sInfoFiltered": "(由 _MAX_ 项结果过滤)",
                "sInfoPostFix":  "",
                "sSearch":       "搜索:",
                "sUrl":          "",
                "sEmptyTable":     "表中数据为空",
                "sLoadingRecords": "载入中...",
                "sInfoThousands":  ",",
                "oPaginate": {
                    "sFirst":    "首页",
                    "sPrevious": "上页",
                    "sNext":     "下页",
                    "sLast":     "末页",
                    "sJump":     "跳转"
                },
                "oAria": {
                    "sSortAscending":  ": 以升序排列此列",
                    "sSortDescending": ": 以降序排列此列"
                }
            },
            autoWidth: false,	//禁用自动调整列宽
            stripeClasses: ["odd", "even"],//为奇偶行加上样式，兼容不支持CSS伪类的场合
            order: [],			//取消默认排序查询,否则复选框一列会出现小箭头
            processing: false,	//隐藏加载提示,自行处理
            serverSide: true,	//启用服务器端分页
            searching: false	//禁用原生搜索
        },
        COLUMN: {
            CHECKBOX: {	//复选框单元格
                className: "td-checkbox",
                orderable: false,
                width: "30px",
                data: null,
                render: function (data, type, row, meta) {
                    return '<input type="checkbox" class="iCheck">';
                }
            }
        },
        RENDER: {	//常用render可以抽取出来，如日期时间、头像等
            ELLIPSIS: function (data, type, row, meta) {
                data = data||"";
                return '<span title="' + data + '">' + data + '</span>';
            }
        }
    }
};