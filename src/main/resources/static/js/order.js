//订单模块js

$(function (){
    $("#table-title li").click(function (){
        $(this).css({
            color:"#eaac00",
            borderBottom:"1px solid #eaac00"
        }).siblings().css({
            color:"#000000",
            borderBottom:"none"
        });
    });

    $("#tab-title li").click(function (){
        $(this).addClass("action").siblings().removeClass("action");
        var index=$(this).index();
        $("#tab-content li").eq(index).css("display","block").siblings().css("display","none");
    });
});