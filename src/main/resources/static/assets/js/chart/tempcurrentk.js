// 基于准备好的dom，初始化echarts实例
$(document).ready(function() {
    $("#sid-select").find("option").each(function(){
        if($(this).text() === sName){
            $(this).attr('selected', 'selected');
        }
    })
    $("#sid-select").on("change",function(){
        window.location.href=contextPath+$(this).val();
    })
        var myChart = echarts.init(document.getElementById('main'));


    $.get("/sensor/tempcurrentk/list",{tSid:tSid,hSid:hSid},function(result){
        var data={};
        data.tSid=result.tSid;
        data.hSid=result.hSid;

        data.tSid.days=[];
        data.hSid.days=[];

        data.tSid.data=[];
        data.hSid.data=[];

        for(var i=0;i<data.tSid.length;i++){
            data.tSid.days.push(data.tSid[i].days);
            data.tSid.data.push(data.tSid[i].savg);
        }
        for(var i=0;i<data.hSid.length;i++){
            data.hSid.days.push(data.hSid[i].days);
            data.hSid.data.push(data.hSid[i].savg);
        }

       var  option = {
            title : {
                text: '温度电流日K线',
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
                data:[{name:'温度'},
                    {name:'电流'},{name:'温度1'},{name:'电流1'}],
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
                    data :  data.tSid.days
                },
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
                    data : data.hSid.days
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
                    name: '电流(A)',
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
                    symbol:'roundRect',
                    hoverAnimation: false,
                    data: data.tSid.data,
                    markPoint:{
                        data:[{
                            name: '最高值',
                            // 支持 'average', 'min', 'max'
                            type: 'max'
                        },]
                    },
                    lineStyle:{
                        normal:{
                            color:'#009966'
                        }
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
                    name:'电流',
                    type:'line',
                    yAxisIndex:1,
                    symbol:'roundRect',
                    hoverAnimation: false,
                    lineStyle:{
                       normal:{
                           color:'#FF3300'
                       }
                    },
                    data:  data.hSid.data,
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