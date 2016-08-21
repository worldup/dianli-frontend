// 基于准备好的dom，初始化echarts实例
$(document).ready(function() {

    var myChart = echarts.init(document.getElementById('main'));


    $.get(contextPath+"sensor/data/realdata",{sid:sid,idx:idx},function(result){
        var data=result;

       var option = {
            title: {
                text: '传感器K线',
                subtext: sName,
                x: 'center',
                align: 'right'
            },
            tooltip : {
                trigger: 'axis'
            },
            legend: {
                data:['值'],
                x: 'left'
            },
            toolbox: {
                show:true,
                feature: {
                    magicType : {show: true, type: ['line', 'bar']},
                    restore : {show: true},
                    saveAsImage: {}
                }
            },

            xAxis : [
                {
                    type : 'category',
                    boundaryGap : false,
                    splitLine: {
                        show: false
                    },
                    axisLabel:{
                        rotate:45
                    },
                    data :data.days
                }
            ],
            yAxis : [
                {
                    name: '值',
                    type : 'value',
                    splitLine: {
                        show: false
                    },
                    max:2*cvalue

                }
            ],
           dataZoom: [
               {
                   type: 'inside',
                   z:-1,
                   top:'bottom'

               },
               {
                   show: true,
                   z:-1,
                   type: 'slider',
                   top:'bottom'
               }
           ],
            series : [
                {
                    name:'值',
                    type:'line',
                   // smooth:true,
                    data:data.values,
                    markPoint:{
                        data:[{
                            name: '最高值',
                            // 支持 'average', 'min', 'max'
                            type: 'max'
                        },]
                    },
                    markLine:{

                        symbol: ['none'],
                        data:[{
                            name: '阈值',
                            yAxis: +cvalue,
                            scale:true
                        }]
                    }
                }

            ]
        };



        myChart.setOption(option)
    });

    window.onresize = function () {
            myChart.resize()
        }
    }
)