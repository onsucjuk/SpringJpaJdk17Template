$(document).ready(function () {

    $("#btnBack").on("click", function () {
        location.href="/user/myPage";
    })

    /* 비밀번호 변경 */
    $("#btnWithdrawal").on("click", function () {

        let f = document.getElementById("f");

        if (f.agree.value === "") {
            alert("\"탈퇴합니다.\" 를 입력해주세요.");
            f.agree.focus();
            return;
        }

        if (f.agree.value !== "탈퇴합니다.") {
            alert("\"탈퇴합니다.\"를 정확하게 입력해주세요.");
            f.agree.focus();
            return;
        }

        $.ajax({
            url: "/user/deleteUserInfo",
            type: "post",
            dataType: "JSON",
            data: $("#f").serialize(), // form 태그 내 input 등 객체를 자동으로 전송할 형태로 변경하기
            success: function (json) {

                alert(json.msg); // 메시지 띄우기

                if (json.result===0) {

                    location.href = "/html/index";

                } else if (json.result===1){


                } else {

                    location.href = "/html/index";

                }
            }
        })

    });


});