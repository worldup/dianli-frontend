// 基于准备好的dom，初始化echarts实例
$(document).ready(function() {

        var myChart = echarts.init(document.getElementById('main'));


    $.get("http://localhost:8080/sensor/kdata/list",{sid:'111_05_A_0_00CD01',idx:'0'},function(result){
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
                text: '日k线'
            },
            tooltip : {
                trigger: 'axis'
            },
            legend: {
                data:['最高','最低','平均','最终']
            },
            toolbox: {
                show:true,
                feature: {
                    magicType : {show: true, type: ['line', 'bar']},
                    restore : {show: true},
                    saveAsImage: {}
                }
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            xAxis : [
                {
                    type : 'category',
                    boundaryGap : false,
                    data :data.days
                }
            ],
            yAxis : [
                {
                    type : 'value',
                    max:50

                }
            ],
           dataZoom: [
               {
                   type: 'inside'

               },
               {
                   show: true,
                   type: 'slider'

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
                    },
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