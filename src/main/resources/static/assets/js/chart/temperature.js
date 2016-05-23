// 基于准备好的dom，初始化echarts实例
var myChart = echarts.init(document.getElementById('main'));

var base = +new Date(2016, 1, 1);
var oneDay = 24 * 3600 * 1000;
var date = [];

var dataMax= [];
var dataMin= [];
var dataAvg= [];

for (var i = 1; i < 144; i++) {
    var now = new Date(base += oneDay);
    date.push([now.getFullYear(), now.getMonth() + 1, now.getDate()].join('-'));
    var min=Math.round(Math.random()*10+60);
    var max=Math.round(Math.random()*(100-90)+90);
    var avg=Math.round(Math.random()*(80-70)+70);
    dataMin.push(min);
    dataMax.push(max);
    dataAvg.push(avg);
}

option = {


    title: {
        text: '堆叠区域图'
    },
    xAxis: {
        type: 'category',
        boundaryGap : false,
        data: date
    },
    yAxis: {
        type: 'value',
    },
    legend: {
        data:['max','avg','min']
    },
    series: [
        {
            name:'avg',
            type:'line',
            smooth:true,


            markLine:{
                data:[{
                    name: '阈值',
                    yAxis: 100
                },]
            },
            data: dataAvg
        },
        {
            name:'max',
            type:'line',
            smooth:true,

            data: dataMax
        },
        {
            name:'min',
            type:'line',
            smooth:true,

            data: dataMin
        }
    ]
};


// 使用刚指定的配置项和数据显示图表。
myChart.setOption(option);
window.onresize=function(){
    myChart.resize()
}