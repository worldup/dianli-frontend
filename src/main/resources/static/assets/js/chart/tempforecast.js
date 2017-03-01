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

        $.get("/sensor/tempforecast/list",
            {aSid:aSid,bSid:bSid,cSid:cSid},function(result){
            var data={};
                data.days=[];
                data.fvalue=[];
                data.rvalue=[];
            for(var i=0;i<result.length;i++){
                data.days.push(result[i].days);
                data.fvalue.push(result[i].fvalue);
                data.rvalue.push(result[i].rvalue);
            }

                var option = {

                    title: {
                        text: sName,
                        subtext:'数据预测指标' ,
                        x: 'center',
                        align: 'right'
                    },
                    color:["#CCFFCC","#99CC66"],
                    legend: {
                        data:[{name:'预测温度'},
                            {name:'实际温度'}],
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
                            axisLabel:{
                                rotate:45},
                            data :data.days
                        }
                    ],
                    yAxis: [
                        {
                            name: '预测温度(℃)',
                            type: 'value',
                            splitLine: {
                                show: false
                            }
                        },
                        {
                            name: '实际温度(℃)',
                            type: 'value',
                            splitLine: {
                                show: false
                            }
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
                    series: [{
                        name: '预测温度',
                        type: 'line',
                        smooth:true,

                        showSymbol: false,
                        hoverAnimation: false,


                        data: data.fvalue
                    },
                        {
                            name: '实际温度',
                            type: 'line',
                            smooth:true,

                            showSymbol: false,
                            hoverAnimation: false,

                            markLine:{
                                data:[{
                                    name: '阈值',
                                    yAxis: 150
                                }]
                            },
                            data: data.rvalue
                        }]
                };

                myChart.setOption(option)

            });

        window.onresize = function () {
            myChart.resize()
        }
    }
)