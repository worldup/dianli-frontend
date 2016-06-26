// 基于准备好的dom，初始化echarts实例
$(document).ready(function() {

        var myChart = echarts.init(document.getElementById('main'));


    $.get("/sensor/kdata/list",{sid:sid,idx:'0'},function(result){
        var data={};
        data.days=[];
        data.smax=[];
        data.smin=[];
        data.savg=[];
        for(var i=0;i<result.length;i++){
            var tmp=result[i];
            data.days.push(tmp.days);
            data.smax.push(tmp.smax);
            data.smin.push(tmp.smin);
            data.savg.push(tmp.savg);
        }
       var option = {
            title: {
                text: '智能除湿温度日K线',
                subtext: sName,
                x: 'center',
                align: 'right'
            },
            tooltip : {
                trigger: 'axis'
            },
            legend: {
                data:['最高','最低','平均'],
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
                    name: '温度(℃)',
                    type : 'value',
                    splitLine: {
                        show: false
                    },
                    max:50

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
                    name:'最高',
                    type:'line',
                   // smooth:true,
                    data:data.smax,
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
                            yAxis: 40,
                            scale:true
                        }]
                    }
                },
                {
                    name:'最低',
                    type:'line',
                     smooth:true,
                    lineStyle: {
                        normal: {opacity: 1}
                    },
                    data:data.smin
                },
                {
                    name:'平均',
                    type:'line',
                 //   smooth:true,
                    lineStyle: {
                        normal: {
                          //  width:1,
                          //  color:'#ccc',
                            opacity: 0.5
                        }
                    },
                    data:data.savg
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