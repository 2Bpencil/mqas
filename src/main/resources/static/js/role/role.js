$(document).ready(function(){
    initTable();
    validateData();
});

/**
 * 初始化表格
 */
function initTable(){
    $('.dataTables-example').DataTable({
        dom: '<"html5buttons"B>lTfgitp',
        "serverSide": true,     // true表示使用后台分页
        "ajax": {
            "url": contextPath+"role/getTableJson",  // 异步传输的后端接口url
            "type": "GET"      // 请求方式
        },
        "columns": [
            { "data": "id","visible": false },
            { "data": "authority" },
            { "data": "name" },
            { "data": null,'render':function (data, type, row, meta) {
                //data  和 row  是数据
                    return '<button type="button" onclick="alert('+data.id+')" class="btn btn-primary btn-xs" >test</button>';// class="btn btn-w-m btn-link"
                } },

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
        language:{
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
        }


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