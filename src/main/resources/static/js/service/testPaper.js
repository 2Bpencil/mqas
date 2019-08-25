var testQuestionTable;//表格对象
var $table = "#testQuestion_table";
var testPaperId = '';
var testPaperTypeId = '';
var initTableFirst  = true;
$(document).ready(function(){
    initTree()
    validateData();
});
/**
 * 初始化表格
 */
function initTable(){
    if(initTableFirst){
        initTableFirst = false;
    }
	testQuestionTable = $($table).DataTable({
        dom: '<"html5buttons"B>lTfgitp',
        "serverSide": true,     // true表示使用后台分页
        "ajax": {
            "url": contextPath+"testPaper/getTableJson",  // 异步传输的后端接口url
            "type": "POST",      // 请求方式
            beforeSend : function(xhr) {
                xhr.setRequestHeader(header, token);
            },
            data:{testPaperTypeId:testPaperTypeId},
        },
        "columns": [
            { "data": "id",
                "visible": false ,
                "searchable":false,
            },
            { "data": "name",
                render : CONSTANT.DATA_TABLES.RENDER.ELLIPSIS,
                width: '60%'

            },
            { "data": "knowledge_name",
                width: '20%'
            },
            { "data": null,
                "searchable":false,
                'orderable' : false ,
                width: '20%',
                'render':function (data, type, row, meta) {
                //data  和 row  是数据
                    var buttons = '';
                    buttons+='<button type="button" onclick="editTestQuestion('+data.id+')" class="btn btn-primary btn-xs" >编辑</button>&nbsp;&nbsp;';
                    buttons+='<button type="button" onclick="deleteTestQuestion('+data.id+')" class="btn btn-primary btn-xs" >删除</button>&nbsp;&nbsp;';
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
    $("#testPaperForm").validate({
        rules: {
            name: {
                required: true,
                maxlength: 50,
                remote : {//远程地址只能输出"true"或"false"
                            url : contextPath + "testPaper/verifyTheRepeat",
                            type : "POST",
                            dataType : "json",//如果要在页面输出其它语句此处需要改为json
                            beforeSend : function(xhr) {
                                xhr.setRequestHeader(header, token);
                            },
                            data : {
                                id : function(){
                                    return $("#testPaperForm_id").val();
                                }
                            }
                        },
            },
        },
        messages : {
            name : {
                required: "不能为空",
                maxlength : "不超过50个字符",
                remote : "试卷名称已存在",
            },
        },
        submitHandler : function(form) {
            //保存
            $.ajax({
                type : "POST",
                data : $("#testPaperForm").serialize(),
                url : contextPath+"testPaper/saveOrEditTestPaper",
                beforeSend : function(xhr) {
                    xhr.setRequestHeader(header, token);
                },
                success: function(result){
                    if(result == 1){
                        hideModal('testPaperModal');
                        clearTestPaperForm();
                        showAlert("保存成功",'success');
                        initTree();
                    }else{
                        showAlert("保存失败",'error');
                    }
                }
            });

        }
    });
    $("#testPaperTypeForm").validate({
        rules: {
            name: {
                required: true,
                maxlength: 50
            },
        },
        messages : {
            name : {
                required: "不能为空",
                maxlength : "不超过50个字符",
            },
        },
        submitHandler : function(form) {
            //保存
            $.ajax({
                type : "POST",
                data : $("#testPaperTypeForm").serialize(),
                url : contextPath+"testPaper/saveOrEditTestPaperType",
                beforeSend : function(xhr) {
                    xhr.setRequestHeader(header, token);
                },
                success: function(result){
                    if(result == 1){
                        hideModal('testPaperTypeModal');
                        clearTestPaperTypeForm();
                        showAlert("保存成功",'success');
                        initTree();
                    }else{
                        showAlert("保存失败",'error');
                    }
                }
            });

        }
    });
    $("#testQuestionForm").validate({
        rules: {
            name: {
                required: true,
                maxlength: 50
            },
            knowledgeCode:{
                required: true,
                maxlength: 50
            },
        },
        messages : {

            name : {
                required: "不能为空",
                maxlength : "不超过50个字符",
            },
            knowledgeCode:{
                required: "不能为空",
                maxlength : "不超过50个字符",
            },
        },
        submitHandler : function(form) {
            //保存
            $.ajax({
                type : "POST",
                data : $("#testQuestionForm").serialize(),
                url : contextPath+"testPaper/saveOrEditTestQuestion",
                beforeSend : function(xhr) {
                    xhr.setRequestHeader(header, token);
                },
                success: function(result){
                    if(result == 1){
                        hideModal('TestQuestionModal');
                        reloadTable();
                        clearTestQuestionForm();
                        showAlert("保存成功",'success');
                    }else{
                        showAlert("保存失败",'error');
                    }
                }
            });

        }
    });
}
/**
 * 新增试卷
 */
function addTestPaper(){

    showModal("testPaperModal");
}
function editTestPaper(){
    if(testPaperId == ''){
        swal("请选中试卷!", "", "error")
        return;
    }
    $.ajax({
        type : "POST",
        data : {id:testPaperId},
        dataType:"json",
        url : contextPath+"testPaper/getTestPaperInfo",
        beforeSend : function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function(result){
            $('#testPaperForm_id').val(result.id);
            $('#testPaperForm_name').val(result.name);
            showModal("testPaperModal");
        }
    });
}
/**
 * 新增试题类型
 */
function addTestPaperType(){
    if(testPaperId == ''){
        swal("请选中试卷!", "", "error");
        return;
    }
    $('#testPaperTypeForm_testPaperId').val(testPaperId);
    showModal("testPaperTypeModal");
}
function editTestPaperType(){
    if(testPaperTypeId == ''){
        swal("请选中试题类型!", "", "error");
        return;
    }
    $.ajax({
        type : "POST",
        data : {id:testPaperTypeId},
        dataType:"json",
        url : contextPath+"testPaper/getTestPaperTypeInfo",
        beforeSend : function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function(result){
            $('#testPaperTypeForm_id').val(result.id);
            $('#testPaperTypeForm_testPaperId').val(result.testPaperId);
            $('#testPaperTypeForm_name').val(result.name);
            showModal("testPaperTypeModal");
        }
    });
}

/**
 * 新增试卷小题
 */
function addTestQuestion(){
    if(testPaperTypeId == ''){
        swal("请选中试题类型!", "", "error");
        return;
    }
    $('#testQuestionForm_testPaperTypeId').val(testPaperTypeId);
    showModal("TestQuestionModal");
}
function editTestQuestion(id){
    $.ajax({
        type : "POST",
        data : {id:id},
        dataType:"json",
        url : contextPath+"testPaper/getTestQuestionInfo",
        beforeSend : function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function(result){
            $('#testQuestionForm_id').val(result.id);
            $('#testQuestionForm_testPaperTypeId').val(result.testPaperTypeId);
            $('#testQuestionForm_name').val(result.name);
            $('#testQuestionForm_knowledgeCode').val(result.knowledgeCode);
            showModal("TestQuestionModal");
        }
    });
}


/**
 * 刷新表格
 */
function reloadTable(){
    testQuestionTable.settings()[0].ajax.data={testPaperTypeId:testPaperTypeId};
    testQuestionTable.ajax.reload();
}
/**
 * 删除
 * @param id
 */
function deleteTestPaper(){
    if(testPaperId == ''){
        swal("请选中试卷!", "", "error");
        return;
    }
    swal({
        title: "是否确定删除?",
        text: "你将会删除该试卷下的所有记录!",
        type: "warning",
        showCancelButton: true,
        confirmButtonColor: "#DD6B55",
        confirmButtonText: "Yes, delete it!",
        closeOnConfirm: false
    }, function () {
        $.ajax({
            type : "POST",
            data : {id:testPaperId},
            dataType:"json",
            url : contextPath+"testPaper/deleteTestPaper",
            beforeSend : function(xhr) {
                xhr.setRequestHeader(header, token);
            },
            success: function(result){
                if(result == 1){
                    initTree();
                    testPaperId = '';
                    testPaperTypeId = '';
                    reloadTable();
                    swal("删除成功!", "", "success");
                }else{
                    swal("删除失败!", "", "error");
                }
            }
        });
    });
}
function deleteTestPaperType(){
    if(testPaperTypeId == ''){
        swal("请选中试题类型!", "", "error");
        return;
    }
    swal({
        title: "是否确定删除?",
        text: "你将会删除该题型下所有信息!",
        type: "warning",
        showCancelButton: true,
        confirmButtonColor: "#DD6B55",
        confirmButtonText: "Yes, delete it!",
        closeOnConfirm: false
    }, function () {
        $.ajax({
            type : "POST",
            data : {id:testPaperTypeId},
            dataType:"json",
            url : contextPath+"testPaper/deleteTestPaperType",
            beforeSend : function(xhr) {
                xhr.setRequestHeader(header, token);
            },
            success: function(result){
                if(result == 1){
                    initTree();
                    testPaperTypeId = '';
                    reloadTable();
                    swal("删除成功!", "", "success");
                }else{
                    swal("删除失败!", "", "error");
                }
            }
        });
    });
}
function deleteTestQuestion(id){
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
            url : contextPath+"testPaper/deleteTestQuestion",
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
 * 初始化树
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
            url : contextPath+"testPaper/getTestPaperTree",
            beforeSend : function(xhr) {
                xhr.setRequestHeader(header, token);
            },
        },
        callback : {
            onAsyncSuccess : zTreeOnAsyncSuccess,//异步加载树完成后回调函数
            onClick: zTreeOnClick
        }
    };
    $.fn.zTree.init($("#testPaperGroup"), setting, zNodes);

}

/**
 * 树节点点击事件
 * @param event
 * @param treeId
 * @param treeNode
 */
function zTreeOnClick(event, treeId, treeNode) {
    //试卷
    if(treeNode.value == 0){
        testPaperId = (treeNode.id+"").replace('testPaper','');
        testPaperTypeId = '';
    }else{
    //题型
        testPaperTypeId = treeNode.id;
        //刷新表格
        if(initTableFirst){
            initTable();
        }else{
            reloadTable();
        }
    }
}

/*
 * 异步加载树完成后回调函数
 */
function zTreeOnAsyncSuccess(event, treeId, treeNode, msg) {
    var  treeObj = $.fn.zTree.getZTreeObj("testPaperGroup");
    treeObj.expandAll(true);
    var nodes = treeObj.getNodes();
    var nodeArr = [];
    for (var i = 0; i <nodes.length ; i++) {
        nodeArr = getAllNodes(nodes[i],nodeArr);
    }
    if(nodeArr.length>0){
        classesId= nodeArr[0].id;
        var parentNode = nodeArr[0].getParentNode();

    }
}
function expandAll(){
    var  treeObj = $.fn.zTree.getZTreeObj("testPaperGroup");
    treeObj.expandAll(true);
}
function closeAll(){
    var  treeObj = $.fn.zTree.getZTreeObj("testPaperGroup");
    treeObj.expandAll(false);
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
function clearTestPaperForm(){
    $('#testPaperForm')[0].reset();
    $('#form_id').val(null);
    $('#testPaperForm').validate().resetForm();
}
function clearTestPaperTypeForm(){
    $('#testPaperTypeForm')[0].reset();
    $('#testPaperTypeForm_id').val(null);
    $('#testPaperTypeForm_testPaperId').val(null);
    $('#testPaperTypeForm').validate().resetForm();
}
function clearTestQuestionForm(){
    $('#testQuestionForm')[0].reset();
    $('#testQuestionForm_id').val(null);
    $('#testQuestionForm_testPaperTypeId').val(null);
    $('#testQuestionForm').validate().resetForm();
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