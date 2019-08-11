var roleTable;//表格对象
var roleValidator;//表单验证
var $table = "#role_table";
$(document).ready(function(){
    initTable();
    validateData();
});

/**
 * 初始化表格
 */
function initTable(){
    roleTable = $($table).DataTable({
        dom: '<"html5buttons"B>lTfgitp',
        "serverSide": true,     // true表示使用后台分页
        "ajax": {
            "url": contextPath+"role/getTableJson",  // 异步传输的后端接口url
            "type": "GET"      // 请求方式
        },
        "columns": [
            { "data": "id",
                "visible": false ,
                "searchable":false,
            },
            { "data": "authority",
                render : CONSTANT.DATA_TABLES.RENDER.ELLIPSIS,
                width: '40%'

            },
            { "data": "name",
                width: '40%'
            },
            { "data": null,
                "searchable":false,
                'orderable' : false ,
                width: '15%',
                'render':function (data, type, row, meta) {
                //data  和 row  是数据
                    var buttons = '';
                    buttons+='<button type="button" onclick="editRole('+data.id+')" class="btn btn-primary btn-xs" >编辑</button>';
                    buttons+='<button type="button" onclick="deleteRole('+data.id+')" class="btn btn-primary btn-xs" >删除</button>';
                    buttons+='<button type="button" onclick="assignmentMenu('+data.id+')" class="btn btn-primary btn-xs" >分配菜单</button>';
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
        processing: false,
    });
    $($table).on( 'error.dt', function ( e, settings, techNote, message ){
        //这里可以接管错误处理，也可以不做任何处理
    }).DataTable();
}
/**
 * 验证数据
 */
function validateData(){
    roleValidator= $("#roleForm").validate({
        rules: {
            authority: {
                required: true,
                maxlength: 50
            },
            name: {
                required: true,
                maxlength: 50
            },
        },
        messages : {
            authority : {
                required : "不能为空",
                maxlength : "不超过50个字符",
                remote : "名称已存在",
            },
            name : {
                required: "不能为空",
                maxlength : "不超过50个字符",
            }
        },
        submitHandler : function(form) {
            saveRole();

        }
    });
}

/**
 * 保存角色
 */
function saveRole(){
    //保存
    $.ajax({
        type : "GET",
        data : $("#roleForm").serialize(),
        url : contextPath+"role/saveOrEditEntity",
        success: function(result){
            if(result == 1){
                $("#roleModal" ).modal('hide');
                reloadTable();
                showAlert("保存成功",'success');
            }else{
                showAlert("保存失败",'error');
            }
        }
    });
}

/**
 * 编辑角色
 * @param id
 */
function editRole(id){
    $.ajax({
        type : "GET",
        data : {id:id},
        dataType:"json",
        url : contextPath+"role/getEntityInfo",
        success: function(result){
            $('#form_id').val(result.id);
            $('#form_authority').val(result.authority);
            $('#form_name').val(result.name);
            reloadTable();
            $("#roleModal" ).modal('show');
        }
    });
}

/**
 * 刷新表格
 */
function reloadTable(){
    roleTable.ajax.reload();
}

/**
 * 删除角色
 * @param id
 */
function deleteRole(id){
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
            type : "GET",
            data : {id:id},
            dataType:"json",
            url : contextPath+"role/deleteRole",
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
 * 分配菜单
 * @param id
 */
function assignmentMenu(id){

}
/**
 * 清空表单
 */
function clearForm(){
    roleValidator.resetForm();
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