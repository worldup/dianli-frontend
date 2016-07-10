sensorArr=[];
sensorArr.push({name:"上海强盛化工有限公司",addr:"上海市金山区夏盛路239弄105号",tel:"021-37285507",url:"/sensor/chart/temperaturek.html?sid=111_05_A_0_00CD01",LngLat:{lng:121.281098,lat:30.725774},sensors:[{name:"焚烧炉",status:"正常"}]});
sensorArr.push({name:"上海金山环境再生能源有限公司",addr:"上海市金山区金山卫镇海金路728号",tel:"021-xxxxxx",url:"/sensor/chart/humidityk.html?tSid=111_05_A_0_0043BA&hSid=111_40_A_0_00CD4E",LngLat:{lng:121.279388,lat:30.707399},sensors:[{name:"焚烧炉",status:"正常"}]});
var sensorsStr =  JSON.stringify(sensorArr);
sensorArr=JSON.parse(sensorsStr);
(function(sensorArr){
    var map = new AMap.Map('container',{
        resizeEnable: true,
        zoom: 14,
        center: [121.280243, 30.7165865]
    });
    AMap.plugin(['AMap.ToolBar','AMap.Scale'],function(){
        var toolBar = new AMap.ToolBar();
        var scale = new AMap.Scale();
        map.addControl(toolBar);
        map.addControl(scale);
    })
    for(var i=0;i<sensorArr.length;i++ ){
        var sensor=sensorArr[i];

        var marker = new AMap.Marker({
            position: [sensor.LngLat.lng,sensor.LngLat.lat]
        });
        var  infowindow = new AMap.InfoWindow({
            content: '<h3 class="title">'+sensor.name+'</h1><div class="content">'+
            '<ul style="padding-left: 0">  ' +
            '<li  style="list-style: none" style="text-align:center"> <span class="fa fa-location-arrow" >'+ sensor.addr+'  </span> </li>  ' +
            '<li style="list-style: none" style="text-align:center" > <span class="fa fa-phone" style="text-align:center">'+sensor.tel+'</li> ' +
            '<li style="list-style: none" style="text-align:center" > <span class="fa fa-fire" aria-hidden="true" style="text-align:center">设备:'+sensor.sensors[0].name +' 状态:<span style="color: green">'+sensor.sensors[0].status+'</span></li> ' +
            '<li style="list-style:none" style="text-align:center"><span><i class="fa fa-bar-chart" aria-hidden="true"></i><a target="_blank" href = "\'"+sensor.url+"\'">进入传感器监控页面</a></span></li>'+
            ' </ul>'+
            '</div>',
            offset: new AMap.Pixel(0, -30),
            size:new AMap.Size(280,0)
        });

        AMap.event.addListener(marker, 'click', function(infowindow,marker) {
            return function(){
                infowindow.open(map, marker.getPosition())
            }
        }(infowindow,marker))

        marker.setMap(map);

    }
})(sensorArr)


