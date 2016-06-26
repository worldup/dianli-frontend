// 基于准备好的dom，初始化echarts实例
$(document).ready(function() {

        var myChart = echarts.init(document.getElementById('main'));


    $.get("/sensor/temphumk/list",{tSid:tSid,hSid:hSid},function(result){
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
                subtext: sName,
                x: 'center',
                align: 'right'
            },
            grid: {
                bottom: 80
            },
           toolbox: {
               show : true,
               feature : {

                   saveAsImage : {show: true}
               }
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
                    realtime: true,
                    top:'bottom',
                    z:-1
                },
                {
                    type: 'inside',
                    realtime: true,
                    top:'bottom',
                    z:-1

                }
            ],
            xAxis : [
                {
                    type : 'category',
                    boundaryGap : false,
                    splitLine: {
                        show: false
                    },
                    axisLine: {onZero: false},
                    axisLabel:{
                        interval:0 ,
                        rotate:45,
                        formatter:function(val){
                            if(val){
                                var tmp= val.split("-");
                                if(tmp[2]%5==0){
                                    return val;
                                }
                            }

                           return  "";
                        }
                    },
                    data :  data.days
                }
            ],
            yAxis: [
                {
                    name: '温度(℃)',
                    type: 'value',
                    splitLine: {
                        show: false
                    }
                },
                {
                    name: '湿度(%RH)',
                    nameLocation: 'start',
                    type: 'value',
                    splitLine: {
                        show: false
                    }
                }
            ],
            series: [
                {
                    name:'温度',
                    type:'line',
                    hoverAnimation: false,
                    data: data.tvalue,
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
                            yAxis: 28,
                            scale:true
                        }]
                    }
                },
                {
                    name:'湿度',
                    type:'line',
                    yAxisIndex:1,
                    hoverAnimation: false,

                    data:  data.hvalue,
                    markPoint:{
                        data:[{
                            name: '最高值',
                            symbolSize:[70,40],
                            // 支持 'average', 'min', 'max'
                            type: 'max'
                        },]
                    },
                    markLine:{

                        symbol: ['none'],
                        data:[{
                            name: '阈值',
                            yAxis: 58,
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