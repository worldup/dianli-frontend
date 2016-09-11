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

        $.get("/sensor/threephase/list",
            {aSid:aSid,bSid:bSid,cSid:cSid},function(result){
            var data={};
                data.days=[];
                data.value=[];
            for(var i=0;i<result.length;i++){
                data.days.push(result[i].days);
                data.value.push(result[i].value);
            }

                var option = {

                    title: {
                        text: sName,
                        subtext:'注：不平衡度为(max(A,B,C)-min(A,B,C))/min(A,B,C)*100' ,
                        x: 'center',
                        align: 'right'
                    },
                    legend: {
                        data:['电流不平衡度(%)'],
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
                    yAxis: {
                        type: 'value',
                        splitLine: {
                            show: false
                        },
                        min:0,
                        max:500
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
                                yAxis: 100
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