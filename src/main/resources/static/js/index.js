$(document).ready(function() {


    initGradeInfo();
    radarMap();
    teacherInfo();
    studentNumTrend();



});

/**
 * 初始化年级信息
 */
function initGradeInfo() {
    $.ajax({
        type : "POST",
        data : {},
        url : contextPath + "index/getClassesInfo",
        beforeSend : function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        dataType:"json",
        success : function(result){
            $('#gradeInfo').html(null);
            var divs = '';
            for (var i = 0; i <result.length ; i++) {
                var div = '<div class="col-lg-2">\n' +
                    '                        <div class="ibox float-e-margins">' +
                    '                            <div class="ibox-title">' +
                    '                                <span class="label label-success pull-right"><i class="fa fa-tags"></i></span>' +
                    '                                <h5>'+result[i].gradeName+'</h5>\n' +
                    '                            </div>' +
                    '                            <div class="ibox-content">' +
                    '                                <div class="stat-percent font-bold text-success">'+result[i].classesNum+'</div>' +
                    '                                <small>班级数量</small>' +
                    '                                <h2 class="no-margins">学生人数：'+result[i].studentsNum+'人</h2>' +
                    '                            </div>' +
                    '                        </div>' +
                    '                    </div>';
                divs += div;
            }
            $('#gradeInfo').html(divs);
        }
    });
}
//雷达
var radarOption = {
    title: {
        text: ''
    },
    tooltip: {},
    legend: {
        data: ['语文','数学', '英语']
    },
    radar: {
        // shape: 'circle',
        name: {
            textStyle: {
                color: '#fff',
                backgroundColor: '#999',
                borderRadius: 3,
                padding: [3, 5]
            }
        },
        indicator: [
            { name: '一年级', max: 100},
            { name: '二年级', max: 120},
            { name: '三年级', max: 130},
            { name: '四年级', max: 140},
            { name: '五年级', max: 150},
            { name: '六年级', max: 160}
        ]
    },
    series: [{
        name: '预算 vs 开销（Budget vs spending）',
        type: 'radar',
        // areaStyle: {normal: {}},
        data : [
            {
                value : [20, 38, 69, 58, 80, 50],
                name : '语文'
            },
            {
                value : [30, 94, 68, 91, 120, 110],
                name : '数学'
            },
            {
                value : [68, 65, 120, 34, 89, 158],
                name : '英语'
            }
        ]
    }]
};

/**
 * 雷达图
 */
function radarMap() {
    var radarMap = echarts.init(document.getElementById('radarMap'));
    radarMap.setOption(radarOption);
}

var teacherOption = {
    tooltip: {
        trigger: 'axis',
        axisPointer: { // 坐标轴指示器，坐标轴触发有效
            type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
        }
    },
    legend: {
        data: ['语文', '数学', '英语'],
        align: 'right',
        right: 10
    },
    grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
    },
    xAxis: [{
        type: 'category',
        data: ['一年级', '二年级', '三年级', '四年级', '五年级', '六年级']
    }],
    yAxis: [{
        type: 'value',
        name: '人数',
        minInterval: 1, //设置成1保证坐标轴分割刻度显示成整数。
        axisLabel: {
            formatter: '{value}'
        }
    }],
    series: [{
        name: '语文',
        type: 'bar',
        data: [3, 4, 3, 2, 2,3]
    }, {
        name: '数学',
        type: 'bar',
        data: [2, 1, 5, 2, 1,6]
    }, {
        name: '英语',
        type: 'bar',
        data: [1, 4, 2, 1,3,6]
    }]
};

/**
 * 老师信息
 */
function teacherInfo() {

    $.ajax({
        type : "POST",
        data : {},
        url : contextPath + "index/getTeacherInfo",
        beforeSend : function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        dataType:"json",
        success : function(result){
            var teacher = echarts.init(document.getElementById('teacherInfo'));
            teacherOption.series=result;
            teacher.setOption(teacherOption);
        }
    });

}

var studentNumTrendOption = {
    tooltip: {
        trigger: 'axis'
    },
    legend: {
        data: ['学生总人数']
    },
    xAxis: {
        type: 'category',
        boundaryGap: false,
        data: ['2019-1', '2019-2', '2019-3', '2019-4', '2019-5', '2019-6', '2019-7', '2019-8', '2019-9', '2019-10', '2019-11', '2019-12',]
    },
    yAxis: {
        type: 'value',
        name: '人数',
        minInterval: 1, //设置成1保证坐标轴分割刻度显示成整数。
        axisLabel: {
            formatter: '{value}'
        }
    },
    grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
    },
    toolbox: {
        feature: {
            saveAsImage: {}
        }
    },
    series: [{
        name:'学生总人数',
        data: [200, 251, 302, 500, 213, 247, 400, 251, 302, 500, 213, 247],
        type: 'line',
        areaStyle: {}
    }]
};

/**
 * 学生走势图
 */
function studentNumTrend(){
    $.ajax({
        type : "POST",
        data : {},
        url : contextPath + "index/studentNumTrend",
        beforeSend : function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        dataType:"json",
        success : function(result){
            var studentNumTrend = echarts.init(document.getElementById('studentNumTrend'));
            // teacherOption.series=result;
            studentNumTrend.setOption(studentNumTrendOption);
        }
    });
}