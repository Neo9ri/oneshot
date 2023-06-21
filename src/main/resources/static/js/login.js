$(window).ready(function() {
$("#loginForm").on("keypress", function (event) {
    var keyPressed = event.keyCode || event.which;
    if (keyPressed === 13) {
        event.preventDefault();
        login();
    }
});
});

function login(){
    var id = document.querySelector('#loginId').value;
    var pw = document.querySelector('#pw').value;

    $.ajax({
        type: 'POST',
        data: {loginId : id,
               pw: pw},
        url: "/login",
        success: function(loginResult){
                    console.log("ajax 통신 성공");
                    if(loginResult.loginSuccess){
                            if(loginResult.auth == 'A'){
                                window.location.href = "/"
                            } else if(loginResult.auth == 'M'){
                                window.location.href = "/member-list"
                            } else {
                                alert("로그인 실패\n: 정지된 회원입니다.");
                                window.location.href = "/logout"
                            }
                    } else {
                        alert("로그인 실패\n: 아이디 또는 비밀번호가 일치하지 않습니다. 다시 입력해주세요.");
                    }
               },
        error: function(){
                  console.log('ajax 통신 에러');
               }
    })
}