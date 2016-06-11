// 基于准备好的dom，初始化echarts实例
$(document).ready(function() {

        var myChart = echarts.init(document.getElementById('main'));

        $.get("/sensor/threephase/list",
            {aSid:'111_A4_A_0_00CF74',bSid:'111_C4_A_0_00CF74',cSid:'111_B4_A_0_00CF74'},function(result){
            var data={};
                data.days=[];
                data.value=[];
            for(var i=0;i<result.length;i++){
                data.days.push(result[i].days);
                data.value.push(result[i].value);
            }

                var option = {

                    title: {
                        text: '三项电流不平衡日K线',
                        subtext: '青浦银涛3号配电站银涛3号低压柜3号',
                        x: 'center',
                        align: 'right'
                    },
                    legend: {
                        data:['电流不平衡度'],
                        x: 'left'
                    },
                    toolbox: {
                        show : true,
                        feature : {

                            saveAsImage : {show: true}
                        }
                    },
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {
                            animation: false
                        }
                    },

                    xAxis : [
                        {
                            type : 'category',
                            boundaryGap : false,
                            splitLine: {
                                show: false
                            },
                            data :data.days
                        }
                    ],
                    yAxis: {
                        type: 'value',
                        splitLine: {
                            show: false
                        },
                        min:-1,
                        max:5
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
                        name: '三项电流不平衡',
                        type: 'line',
                        smooth:true,

                        showSymbol: false,
                        hoverAnimation: false,

                        markLine:{
                            data:[{
                                name: '阈值',
                                yAxis: 1
                            }]
                        },
                        data: data.value
                    }]
                };

                myChart.setOption(option)

            });

        window.onresize = function () {
            myChart.resize()
        }
    }
)