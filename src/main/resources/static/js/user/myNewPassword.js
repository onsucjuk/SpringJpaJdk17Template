$(document).ready(function () {

    $("#btnBack").on("click", function () {
        location.href="/user/myPage";
    })

    /* 비밀번호 변경 */
    $("#btnUpdatePassword").on("click", function () {

        let f = document.getElementById("f");

        if (f.password.value === "") {
            alert("새로운 비밀번호를 입력하세요.");
            f.password.focus();
            return;
        }

        if (f.password2.value === "") {
            alert("비밀번호 확인을 입력하세요.");
            f.password2.focus();
            return;
        }

        if (f.password.value !== f.password2.value) {
            alert("입력한 비밀번호가 일치하지 않습니다.");
            f.password.focus();
            return;
        }

        $.ajax({
                url: "/user/myUpdatePassword",
                type: "post",
                dataType: "JSON",
                data: $("#f").serialize(), // form 태그 내 input 등 객체를 자동으로 전송할 형태로 변경하기
                success: function (json) {

                    alert(json.msg); // 메시지 띄우기

                    if (json.result===0) {

                        location.href = "/user/myPage";

                    } else if (json.result===1){


                    } else {

                        location.href = "/user/myPage";

                    }
                }
                })

        /*window.location.href = "/user/login";*/

    });


});