var dataUrl=contextPath+"sensor/data/sensordata";
var curPage=1;
var pagination=initPagination(sensorPageSize);
if(sensorPageSize>0){
    showSensorPage(1)
}
$("#searchBtn").on("click",function(){
    showSensorPage(curPage);

})
function showSensorPage(page){
   var sensorName= $("#searchSName").val()
    var data={startPage:page};
    if( $.trim(sensorName).length>0){
        data.sensorName=sensorName;
    }
    $.post(dataUrl,data,function(result){
        $("#databody").html(result.data);

    });
}
function initPagination(pageSize){
    return new Pagination({
        wrap: $('.am-pagination'),
        count: pageSize,
        current:curPage,
        prevText: '上一页',
        nextText: '下一页',
        callback: function(page) {
            curPage=page;
            showSensorPage(page)
        }
    });
}
function getTree(){
    var tree=[];
    $(sensorTrees).each(function(idx,val){
        var nodes=[];
        var pnode={};
        pnode.text=val.k;
        pnode.icon= "fa  fa-university";
        pnode.selectedIcon="fa  fa-university";
        pnode.color="#0e90d2";
        pnode.backColor="#FFFFFF";
        pnode.selectable=false;
        pnode.state={
            expanded : false
        };
        $(val.v).each(function(i,v){
           nodes.push({
               text: v,
               icon: "fa fa-tachometer",
               selectedIcon: "fa fa-tachometer",
               color: "#0e90d2",
               backColor: "#FFFFFF",
               href: "#node-1",
               selectable: true
           })
        });
        pnode.nodes=nodes;
        tree.push(pnode)
    })
    return tree;
}
//增加tree
$('#tree').treeview({data: getTree()});
$('#tree').on('nodeSelected', function(event, data) {
    var data={startPage:1,sensorName:data.text};

    $.post(dataUrl,data,function(result){
        $("#databody").html(result.data);

    });});
/*$('#tree').on('nodeUnselected', function(event, data) {
    var data={startPage:1};

    $.post(dataUrl,data,function(result){
        $("#databody").html(result.data);

    });});*/
/*function autoRefresh(){
    showSensorPage(curPage,$("#searchSName").val());
}*/
//setInterval(autoRefresh, 30000);