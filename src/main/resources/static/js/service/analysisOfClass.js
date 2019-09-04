var distributionEchart;
var classId;
var startDate;//当前日期
var endDate;//前七天日期
$(document).ready(function(){
    //当前时间
    var curDate = new Date();
    //前七天
    var preDate = new Date(curDate.getTime() - 24*60*60*1000*7);
    endDate = curDate.format("yyyy-MM-dd");
    startDate = preDate.format("yyyy-MM-dd");
    $("#start_time").val(startDate);
    $("#end_time").val(endDate);
    classId = $('#classId').val();

    distributionEchart = echarts.init(document.getElementById('distribution'));

    initWrongQuestionDistribution();




});



/**
 * 返回学生管理页面
 */
function backPrevious() {
    window.location.href=contextPath+"student/studentManage";
}

/**
 * 初始化  错题分布图
 */
function initWrongQuestionDistribution(){
    $.ajax({
        type : "POST",
        data : {classId:classId},
        url : contextPath + "classes/wrongQuestionDistribution",
        beforeSend : function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        dataType:"json",
        success : function(result){
            console.log(result);
            distributionData = result.data;
            distributionOption.legend.data = result.legend;
            var seriesData = [];
            for (var i = 0; i < result.legend.length; i++) {
                var seriesObj = {};
                seriesObj.name = result.legend[i];
                seriesObj.data = result.data[i];
                seriesObj.type= 'scatter';
                seriesObj.symbolSize = function (data) {
                    return 20;
                };
                seriesObj.itemStyle ={
                    normal: {
                        shadowBlur: 10,
                            shadowColor: 'rgba(25, 100, 150, 0.5)',
                            shadowOffsetY: 5,
                    }
                };
                seriesData.push(seriesObj);
            }
            distributionOption.series = seriesData;

            distributionEchart.setOption(distributionOption);
        }
    });


}
var distributionData = [
    [[2,10,'张三','整数型'],[5,9,'哈哈','整数型']],
    [[5,6,'李四','小数型']],
    [[7,15,'王二','负数']]
];

var distributionOption = {

    legend: {
        right: 5,
        data: ['整数型', '小数型','负数']
    },
    xAxis: {
        name:'错题间隔时间（天）',
        splitLine: {
            lineStyle: {
                type: 'dashed'
            }
        },
        minInterval: 1, //设置成1保证坐标轴分割刻度显示成整数。
    },
    yAxis: {
        name:'错题个数',
        splitLine: {
            lineStyle: {
                type: 'dashed'
            }
        },
        minInterval: 1, //设置成1保证坐标轴分割刻度显示成整数。
        scale: true
    },
    tooltip: {
        padding: 10,
        backgroundColor: '#222',
        borderColor: '#777',
        borderWidth: 1,
        formatter: function (obj) {
            var value = obj.value;
            // return value;
            return '<div style="border-bottom: 1px solid rgba(255,255,255,.3); font-size: 18px;padding-bottom: 7px;margin-bottom: 7px">'
                + '学生：'+ value[2]
                + '</div>'
                + '错题数：'+value[1]+ '<br>'
                + '错误间隔时长：'+value[0]+ '天<br>';

        }
    },
    series: [{
        name: '整数型',
        data: distributionData[0],
        type: 'scatter',
        symbolSize: function (data) {
            return 20;
        },
        itemStyle: {
            normal: {
                shadowBlur: 10,
                shadowColor: 'rgba(120, 36, 50, 0.5)',
                shadowOffsetY: 5,
            }
        }
    }, {
        name: '小数型',
        data: distributionData[1],
        type: 'scatter',
        symbolSize: function (data) {
            return 20;
        },
        itemStyle: {
            normal: {
                shadowBlur: 10,
                shadowColor: 'rgba(25, 100, 150, 0.5)',
                shadowOffsetY: 5,
            }
        }
    }, {
        name: '负数',
        data: distributionData[2],
        type: 'scatter',
        symbolSize: function (data) {
            return 20;
        },
        itemStyle: {
            normal: {
                shadowBlur: 10,
                shadowColor: 'rgba(25, 100, 150, 0.5)',
                shadowOffsetY: 5,
            }
        }
    }]
};


//时间格式化
Date.prototype.format = function (format) {
    var args = {
        "M+": this.getMonth() + 1,
        "d+": this.getDate(),
        "h+": this.getHours(),
        "m+": this.getMinutes(),
        "s+": this.getSeconds(),
        "q+": Math.floor((this.getMonth() + 3) / 3), //quarter

        "S": this.getMilliseconds()
    };
    if (/(y+)/.test(format)) format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var i in args) {
        var n = args[i];

        if (new RegExp("(" + i + ")").test(format)) format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? n : ("00" + n).substr(("" + n).length));
    }
    return format;
};
