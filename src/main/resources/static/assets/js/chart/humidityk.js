// 基于准备好的dom，初始化echarts实例
$(document).ready(function() {

        var myChart = echarts.init(document.getElementById('main'));


    $.get("/sensor/temphumk/list",{tSid:'111_05_A_0_008105',hSid:'111_40_A_0_008105'},function(result){
        var data={};
        data.days=[];
        data.tvalue=[];
        data.hvalue=[];
        for(var i=0;i<result.length;i++){
            data.days.push(result[i].days);
            data.tvalue.push(result[i].tavg);
            data.hvalue.push(result[i].havg);
        }
       var  option = {
            title : {
                text: '温湿控效率日K线',
                subtext: '浦东10kV文登南泉站0.4kV室',
                x: 'center',
                align: 'right'
            },
            grid: {
                bottom: 80
            },
            tooltip : {
                trigger: 'axis',
                axisPointer: {
                    animation: false
                }
            },
            legend: {
                data:['温度','湿度'],
                x: 'left'
            },
            dataZoom: [
                {
                    show: true,
                    realtime: true

                },
                {
                    type: 'inside',
                    realtime: true

                }
            ],
            xAxis : [
                {
                    type : 'category',
                    boundaryGap : false,
                    axisLine: {onZero: false},
                    data :  data.days
                }
            ],
            yAxis: [
                {
                    name: '温度(℃)',
                    type: 'value',
                },
                {
                    name: '湿度(%RH)',
                    nameLocation: 'start',
                    type: 'value'
                }
            ],
            series: [
                {
                    name:'温度',
                    type:'line',
                    hoverAnimation: false,


                    data: data.tvalue
                },
                {
                    name:'湿度',
                    type:'line',
                    yAxisIndex:1,
                    hoverAnimation: false,

                    data:  data.hvalue
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