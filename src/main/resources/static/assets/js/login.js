$(function () {
//用户名鼠标离开事件
   $("#username1").focus(function(){
      var value = $(this).val();
      if(value ==this.defaultValue){
         $(this).val("");
      }
   }).blur(function(){
      var value = $(this).val();
      if(value ==""){
         $(this).val(this.defaultValue);
      }else{
        $('#validateUser').hide();
      }
   });

//密码框鼠标离开事件
   $("#password2").focus(function(){
      $("#password1").show().focus();
      $(this).hide();
   });
   $("#password1").blur(function(){
      var value = $(this).val();
      if(value==""){
        $("#password2").show();
        $(this).hide();
      }else{
        $('#validatePassword').hide();
      }
   });

//验证不能为空
   $("#login_btn").on('click',function(){
        var Oval = $("#username1").val();
        var Pval = $("#password1").val();
        if(Oval==''|| Oval=='请输入用户名'){
            $('#validateUser').show();
            return false;
        }if(Pval==''){
            $('#validatePassword').show();
            return false;
        }
   });
})