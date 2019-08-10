var table;
$(document).ready(function(){
    initTable();
    validateData();
});

/**
 * 初始化表格
 */
function initTable(){
    table = $('.dataTables-example').DataTable({
        dom: '<"html5buttons"B>lTfgitp',
        "serverSide": true,     // true表示使用后台分页
        "ajax": {
            "url": contextPath+"role/getTableJson",  // 异步传输的后端接口url
            "type": "GET"      // 请求方式
        },
        "columns": [
            { "data": "id","visible": false },
            { "data": "authority",render : CONSTANT.DATA_TABLES.RENDER.ELLIPSIS },
            { "data": "name" },
            { "data": null,'render':function (data, type, row, meta) {
                //data  和 row  是数据
                    return '<button type="button" onclick="alert('+data.id+')" class="btn btn-primary btn-xs" >按钮</button>';// class="btn btn-w-m btn-link"
                },orderable : false },

        ],
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
        language:CONSTANT.DATA_TABLES.DEFAULT_OPTION.language
        /*{
            lengthMenu:"每页显示 _MENU_条数据",
            sSearch: "搜索: ",
            info:"第_PAGE_/_PAGES_页, 显示第_START_到第_END_, 搜索到_TOTAL_/_MAX_条",
            infoFiltered:"",
            sProcessing: "正在加载数据，请稍等",
            zeroRecords:"抱歉，没有数据",
            paginate:{
                previous: "上一页",
                next: "下一页",
                first: "第一页",
                last: "最后"
            }
        }*/


    });
}
/**
 * 验证数据
 */
function validateData(){
    $("#form").validate({
        rules: {
            password: {
                required: true,
                minlength: 3
            },
            url: {
                required: true,
                url: true
            },
            number: {
                required: true,
                number: true
            },
            min: {
                required: true,
                minlength: 6
            },
            max: {
                required: true,
                maxlength: 4
            }
        }
    });
}


/*常量*/
var CONSTANT = {
    DATA_TABLES : {
        DEFAULT_OPTION : { //DataTables初始化选项
            language: {
                "sProcessing":   "处理中...",
                "sLengthMenu":   "每页 _MENU_ 项",
                "sZeroRecords":  "没有匹配结果",
                "sInfo":         "当前显示第 _START_ 至 _END_ 项，共 _TOTAL_ 项。",//共 _TOTAL_ 项
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