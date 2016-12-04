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


    $.get("/sensor/contacttempcurrentk/list",{ctSid:ctSid,tSid:tSid,cSid:cSid},function(result){
        var data={};
        data.ctSid=result.ctSid;
        data.tSid=result.tSid;
        data.cSid=result.cSid;
        data.ctSid.days=[];
        data.tSid.days=[];
        data.cSid.days=[];
        data.ctSid.data=[];
        data.tSid.data=[];
        data.cSid.data=[];
        for(var i=0;i<data.ctSid.length;i++){
            data.ctSid.days.push(data.ctSid[i].days);
            data.ctSid.data.push(data.ctSid[i].savg);
        }
        for(var i=0;i<data.tSid.length;i++){
            data.tSid.days.push(data.tSid[i].days);
            data.tSid.data.push(data.tSid[i].savg);
        }
        for(var i=0;i<data.cSid.length;i++){
            data.cSid.days.push(data.cSid[i].days);
            data.cSid.data.push(data.cSid[i].savg);
        }

 
       var  option = {
            title : {
                text: '触点温度电流日K线',
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
           color:["#CCFFCC","#99CC66","#FFCCCC"],
            legend: {
                data:[{name:'触点温度'},
                    {name:'环境温度'},{name:'电流'}],
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
                    data :  data.ctSid.days
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
                    data : data.tSid.days
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
                    data :  data.cSid.days
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
                    name:'触点温度',
                    type:'line',
                    smooth:'true',
                    hoverAnimation: false,
                    showSymbol:false,
                    data: data.ctSid.data,
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
                    name:'环境温度',
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
                            color:'#99CC66'
                        }
                    }
                },
                {
                    name:'电流',
                    type:'line',
                    smooth:'true',
                    yAxisIndex:1,
                    showSymbol:false,
                    hoverAnimation: false,
                    lineStyle:{
                       normal:{
                           color:'#FFCCCC'
                       }
                    },
                    data:  data.cSid.data,
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