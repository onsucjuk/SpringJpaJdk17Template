$(document).ready(function () {

    /* 비정상적인 접근 및 회원정보가 없는 경우 뒤로 가기 */
    /*    let msg = [[${msg}]]
        if (msg.length > 0) {
            alert(msg);
            history.back();
        }*/

    const pass = "[[${session.NEW_PASSWORD}]]";

    if(!pass) {
        alert("비정상적인 접근 입니다.")
        location.href="/user/passProc";
    }

    /* 로그인 화면 이동 */
    $("#btnLogin").on("click", function () {
        location.href = "/user/passProc";
    });

    /* 비밀번호 찾기 */
    $("#btnUpdatePassword").on("click", function () {

        let f = document.getElementById("f");

        if (f.password.value === "") {
            alert("새로운 비밀번호를 입력하세요.");
            f.password.focus();
            return;
        }

        if (f.password2.value === "") {
            alert("검증을 위한 새로운 비밀번호를 입력하세요.");
            f.password2.focus();
            return;
        }

        if (f.password.value !== f.password2.value) {
            alert("입력한 비밀번호가 일치하지 않습니다.");
            f.password.focus();
            return;
        }

        $.ajax({
                url: "/user/updatePassword",
                type: "post",
                dataType: "JSON",
                data: $("#f").serialize()
            }
        )

        alert("비밀번호가 변경되었습니다.");
        window.location.href = "/user/login";
    });
});