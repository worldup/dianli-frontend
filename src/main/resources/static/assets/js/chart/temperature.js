// 基于准备好的dom，初始化echarts实例
$(document).ready(function() {

        var myChart = echarts.init(document.getElementById('main'));


    var data = [];


    option = {
        title: {
            text: '动态数据 + 时间坐标轴'
        },
        tooltip: {
            trigger: 'axis',
            formatter: function (params) {
                params = params[0];
                var date = new Date(params.value[0]);
                return date.getDate() + '/' + (date.getMonth() + 1) + '/' + date.getFullYear() + ' : ' + params.value[1];
            },
            axisPointer: {
                animation: false
            }
        },
        toolbox: {
            show : true,
            feature : {
                mark : {show: true},
                dataView : {show: true, readOnly: false},
                magicType : {show: true, type: ['line', 'bar']},
                restore : {show: true},
                saveAsImage : {show: true}
            }
        },
        xAxis: {
            type: 'time',
            splitLine: {
                show: false
            }
        },
        yAxis: {
            type: 'value',
            scale: true,
            splitLine: {
                show: false
            }
        },
        dataZoom: [
            {
                type: 'inside'

            },
            {
                show: true,
                type: 'slider'

            }
        ],
        series: [{
            name: '模拟数据',
            type: 'line',
            //smooth:true,
            areaStyle: {
                normal: {
                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                        offset: 0,
                        color: 'rgb(255, 158, 68)'
                    }, {
                        offset: 1,
                        color: 'rgb(255, 70, 131)'
                    }])
                }
            },

            showSymbol: false,
            hoverAnimation: false,
            markPoint:{
                data:[{
                    name: '平均线',
                    // 支持 'average', 'min', 'max'
                    type: 'max'
                },]
            },
            markLine:{
                data:[{
                    name: '阈值',
                    yAxis: 25
                }]
            },
            data: data
        }]
    };
    myChart.setOption(option)

        $.get("/sensor/data/list",{sid:'T111_05_A_0_00CD01_0',start:'1',end:'2465092000000'},function(result){
            var data=[];
            for(var i=0;i<result.length;i++){

                data.push([new Date(result[i].createtime).toString(),result[i].svalue]);
            }
            myChart.setOption({
                series: [{
                    data: data
                }]
            });
            });

        window.onresize = function () {
            myChart.resize()
        }
    }
)