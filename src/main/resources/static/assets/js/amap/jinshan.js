var map = new AMap.Map('container',{
    resizeEnable: true,
    zoom: 14,
    center: [121.280243, 30.7165865]
});
//金山强盛化工
var qsMarker = new AMap.Marker({
    position: [121.281098,30.725774]
});
//金山再生能源
var zsMarker = new AMap.Marker({
    position: [121.279388,30.707399]
});
qsMarker.setMap(map);
zsMarker.setMap(map);

var qsInfowindow = new AMap.InfoWindow({
    content: '<h3 class="title">上海强盛化工有限公司</h1><div class="content">'+
    '<ul style="padding-left: 0">  ' +
    '<li  style="list-style: none" style="text-align:center"> <span class="fa fa-location-arrow" > 上海市金山区夏盛路239弄105号    </span> </li>  ' +
    '<li style="list-style: none" style="text-align:center" > <span class="fa fa-phone" style="text-align:center">021-37285507</li> ' +
    '<li style="list-style:none" style="text-align:center"><span><i class="fa fa-bar-chart" aria-hidden="true"></i><a target="_blank" href = "/sensor/chart/temperaturek.html?sid=111_05_A_0_00CD01">进入传感器监控页面</a></span></li>'+
    ' </ul>'+
    '</div>',
    offset: new AMap.Pixel(0, -30),
    size:new AMap.Size(280,0)
});
var zsInfowindow = new AMap.InfoWindow({
    content: '<h3 class="title">上海金山环境再生能源有限公司</h1><div class="content">'+
    '<ul style="padding-left: 0" >  ' +
    '<li style="list-style: none" style="text-align:center"> <span class="fa fa-location-arrow">上海市金山区金山卫镇海金路728号</span> </li>  ' +
    '<li style="list-style: none" style="text-align:center" > <span class="fa fa-phone"> </li>  ' +
    '<li style="list-style:none" style="text-align:center"><span><i class="fa fa-bar-chart" aria-hidden="true"></i> <a target="_blank" href = "/sensor/chart/humidityk.html?tSid=111_05_A_0_0043BA&hSid=111_40_A_0_00CD4E">进入传感器监控页面</a></span></li>'+
    '</ul>'+
    '</div>',
    offset: new AMap.Pixel(0, -30),
    size:new AMap.Size(280,0)
});
qsInfowindow.open(map,qsMarker.getPosition());
// zsInfowindow.open(map,zsMarker.getPosition());
qsMarker.on('click',function(e){
    qsInfowindow.open(map,e.target.getPosition());
})
zsMarker.on('click',function(e){
    zsInfowindow.open(map,e.target.getPosition());
})
AMap.plugin(['AMap.ToolBar','AMap.Scale'],function(){
    var toolBar = new AMap.ToolBar();
    var scale = new AMap.Scale();
    map.addControl(toolBar);
    map.addControl(scale);
})
