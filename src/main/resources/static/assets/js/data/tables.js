var dataUrl=contextPath+"sensor/data/sensordata";
var curPage=1;
initPagination(sensorPageSize);
if(sensorPageSize>0){
    showSensorPage(1)
}
$("#searchBtn").on("click",function(){
    showSensorPage(1,$("#searchSName").val());

})
function showSensorPage(page,sensorName){
    var data={startPage:page};
    if( $.trim(sensorName).length>0){
       data.sensorName=sensorName;
    }
    $.post(dataUrl,data,function(result){
        $("#databody").html(result.data);
        initPagination(result.pageSize);
    });

}
function initPagination(pageSize){
    new Pagination({
        wrap: $('.am-pagination'),
        count: pageSize,
        prevText: '上一页',
        nextText: '下一页',
        callback: function(page) {
            curPage=page;
            showSensorPage(page)
        }});
}
function autoRefresh(){
    showSensorPage(curPage,$("#searchSName").val());
}
setInterval(autoRefresh, 30000);