$(document).ready(function () {
    $("#btnLogin").on("click", function () {
        location.href = "/user/login";
    })

    $("#btnBnav").on("click", function () {
        location.href = "/seoul/siAnalysis";
    })


    $("#btnNotice").on("click", function () {
        location.href = "/notice/noticeList";
    })

    $("#mainMypage").on("click", function () {
        location.href = "/user/myPage";
    })

    $("#mainLogin").on("click", function () {
        location.href = "/user/login";
    })

    $("#mainLogOut").on("click", function () {

        $.ajax({
            url: "/user/logout",
            type: "post",
            dataType: "JSON",
            success: function (json) {

                if (json.result === 1) {
                    alert(json.msg);
                    location.href = "/html/index";
                } else {
                    alert(json.msg);
                }
            }
        })
    })

})