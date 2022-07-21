//搜索自动提示js

$(function (){
    $("#searchInput").keyup(function (){
        var keyword=$("#searchInput").val();
        if(keyword===""){
            $("#auto").css("display","none");
            return;
        }
        $.ajax({
            type: "GET",
            url: "http://localhost:8080/catalog/AutoFindProductAjax",
            data: {"keyword":keyword},
            dataType: "json",
            success: function (data){
                var htm="";
                for(var i=0;i<data.length;i++){
                    var reg=new RegExp("("+keyword+")","i");
                    htm+="<div id='auto_out' onclick='setSearch_onclick(this)' onmouseout='changeBackColor_out(this)' onmouseover='changeBackColor_over(this)'>"+data[i].name.replace(reg,"<strong>$1</strong>")+"</div>";
                }
                $("#auto").html(htm);
                $("#auto").css("display","block");
            }
        });
    });
});
function setSearch_onclick(div){
    $("#searchInput").val(div.innerText);
    $("#auto").css("display","none");
}

function changeBackColor_out(div){
    $(div).css("background","");
}

function changeBackColor_over(div) {
    $(div).css("background", "#666");
    $(div).css("cursor", "pointer");
}