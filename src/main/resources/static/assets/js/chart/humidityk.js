// 基于准备好的dom，初始化echarts实例
$(document).ready(function() {

        var myChart = echarts.init(document.getElementById('main'));


    $.get("/sensor/temphumk/list",{tSid:tSid,hSid:hSid,tSid1:tSid1,hSid1:hSid1},function(result){
        var data={};
        data.tSid=result.tSid;
        data.hSid=result.hSid;
        data.tSid1=result.tSid1;
        data.hSid1=result.hSid1;
        data.tSid.days=[];
        data.hSid.days=[];
        data.tSid1.days=[];
        data.hSid1.days=[];
        data.tSid.data=[];
        data.hSid.data=[];
        data.tSid1.data=[];
        data.hSid1.data=[];
        for(var i=0;i<data.tSid.length;i++){
            data.tSid.days.push(data.tSid[i].days);
            data.tSid.data.push(data.tSid[i].savg);
        }
        for(var i=0;i<data.hSid.length;i++){
            data.hSid.days.push(data.hSid[i].days);
            data.hSid.data.push(data.hSid[i].savg);
        }
        for(var i=0;i<data.tSid1.length;i++){
            data.tSid1.days.push(data.tSid1[i].days);
            data.tSid1.data.push(data.tSid1[i].savg);
        }
        for(var i=0;i<data.hSid1.length;i++){
            data.hSid1.days.push(data.hSid1[i].days);
            data.hSid1.data.push(data.hSid1[i].savg);
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
           color:["#CCFFCC","#99CC66","#FFCCCC","#FF6666"],
            legend: {
                data:[{name:'冷凝温度'},
                    {name:'冷凝湿度'},{name:'加热温度'},{name:'加热湿度'}],
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
                    data :  data.tSid1.days
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
                    data : data.hSid1.days
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
                    type: 'value',
                    splitLine: {
                        show: false
                    }
                }


            ],
            series: [
                {
                    name:'冷凝温度',
                    type:'line',
                    smooth:'true',
                    hoverAnimation: false,
                    showSymbol:false,
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
                            color:'#CCFFCC'
                        }
                    }
                },
                {
                    name:'冷凝湿度',
                    type:'line',
                    smooth:'true',
                    yAxisIndex:1,
                    showSymbol:false,
                    hoverAnimation: false,
                    lineStyle:{
                       normal:{
                           color:'#99CC66'
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
                    }
                },
                {
                    name:'加热温度',
                    type:'line',
                    smooth:'true',
                    hoverAnimation: false,
                    showSymbol:false,
                    data: data.tSid1.data,
                    markPoint:{
                        data:[{
                            name: '最高值',
                            // 支持 'average', 'min', 'max'
                            type: 'max'
                        },]
                    },
                    lineStyle:{
                        normal:{
                            color:'#FFCCCC'
                        }
                    }
                },
                {
                    name:'加热湿度',
                    type:'line',
                    smooth:'true',
                    yAxisIndex:1,
                    showSymbol:false,
                    hoverAnimation: false,
                    lineStyle:{
                        normal:{
                            color:'#FF6666'
                        }
                    },
                    data:  data.hSid1.data,
                    markPoint:{
                        data:[{
                            name: '最高值',
                            symbolSize:[70,40],
                            // 支持 'average', 'min', 'max'
                            type: 'max'
                        },]
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