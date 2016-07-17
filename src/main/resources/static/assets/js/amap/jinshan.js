
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
            position: [sensor.lng,sensor.lat]
        });


        var  infowindow = new AMap.InfoWindow({
            content: '<h3 class="title">'+sensor.name+'</h1><div class="content">'+
            '<ul style="padding-left: 0">  ' +
            '<li  style="list-style: none" style="text-align:center"> <span class="fa fa-location-arrow" >'+ sensor.address+'  </span> </li>  ' +
            '<li style="list-style: none" style="text-align:center" > <span class="fa fa-phone" style="text-align:center">'+sensor.tel+'</li> ' +
            '<li style="list-style: none" style="text-align:center" > <span class="fa fa-fire" aria-hidden="true" style="text-align:center"> '+sensor.label+'</li> ' +
            '<li style="list-style:none" style="text-align:center"><span><i class="fa fa-bar-chart" aria-hidden="true"></i><a target="_blank" href = "'+sensor.url+'">进入传感器监控页面</a></span></li>'+
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


