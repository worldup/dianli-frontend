// 基于准备好的dom，初始化echarts实例
$(document).ready(function() {

        var myChart = echarts.init(document.getElementById('main'));


        $.get("/sensor/data/listdaydata",{sid:sid,idx:'0',date:+new Date()},function(result){
            var data={};
            data.days=[];
            data.sv=[];
            for(var i=0;i<result.length;i++){
                var tmp=result[i];
                console.log(tmp.tmin,tmp.tmax,tmp.sv)
                data.days.push(tmp.tmin);
                data.days.push(tmp.tmax);
                data.sv.push(tmp.sv);
                data.sv.push(tmp.sv);
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
                        data:data.sv,
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