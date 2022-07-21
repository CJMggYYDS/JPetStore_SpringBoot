//用户注册改善js
$(function (){
   var flag_username=false;
   var flag_password=false;

   $("#username").bind('input propertychange',function (){
      let username_text=$("#username").val();
      if(username_text===""){
         document.getElementById("username").style.borderColor="red";
         $("#isExistInfo").show().html("用户名不能为空");
         $("#isExistInfo").addClass("error");
         flag_username=false;
         return;
      }

      $.get("/account/usernameIsExist",{"username":username_text},function (data,status,xhr){
         var responseInfo=xhr.responseText;
         if(responseInfo==="Exist"){
            document.getElementById("username").style.borderColor="red";
            $("#isExistInfo").show().html("用户名已存在，不可用");
            $("#isExistInfo").removeClass("right");
            $("#isExistInfo").addClass("error");
            flag_username=false;
         }
         else if(responseInfo==="Not Exist"){
            document.getElementById("username").style.borderColor="green";
            $("#isExistInfo").show().html("用户名可用");
            $("#isExistInfo").removeClass("error");
            $("#isExistInfo").addClass("right");
            flag_username=true;
         }
      })
   });

   $("#password").bind('input propertychange',function (){
      let pwd_text=$("#password").val();
      if(pwd_text.length<=6){
         document.getElementById("password").style.borderColor="red";
         $("#isPwd").show().html("密码长度应大于6位");
         $("#isPwd").removeClass("right");
         $("#isPwd").addClass("error");
         flag_password=false;
      }
      else{
         document.getElementById("password").style.borderColor="green";
         $("#isPwd").show().html("密码合法");
         $("#isPwd").removeClass("error");
         $("#isPwd").addClass("right");
         flag_password=true;
      }
   });

   $("#repeatPassword").bind('input propertychange',function (){
      let pwd=$("#password").val();
      let rePwd=$("#repeatPassword").val();
      if(rePwd===""&&pwd!==""){
         document.getElementById("repeatPassword").style.borderColor="red";
         $("#isRePwd").show().html("请确认密码");
         $("#isRePwd").removeClass("right");
         $("#isRePwd").addClass("error");
      }
      else if(rePwd===pwd&&flag_password){
         document.getElementById("repeatPassword").style.borderColor="green";
         $("#isRePwd").show().html("密码合法");
         $("#isRePwd").removeClass("error");
         $("#isRePwd").addClass("right");
      }
      else if(!flag_password){
         document.getElementById("repeatPassword").style.borderColor="green";
         $("#isRePwd").show().html("新密码有误");
         $("#isRePwd").removeClass("right");
         $("#isRePwd").addClass("error");
      }
      else{
         document.getElementById("repeatPassword").style.borderColor="red";
         $("#isRePwd").show().html("确认密码有误");
         $("#isRePwd").removeClass("right");
         $("#isRePwd").addClass("error");
      }
   });
});