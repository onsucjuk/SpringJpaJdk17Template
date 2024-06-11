$(document).ready(function () {
    $("#btnLogin").on("click", function () {
        location.href = "/user/login";
    })

    $("#btnBnav").on("click", function () {
        location.href = "/Bnav/SiAnalysis";
    })


    $("#btnNotice").on("click", function () {
        location.href = "/notice/noticeList";
    })

    $("#btnEdit").on("click", function () {

        location.href = "/user/myPageEdit";

    })

    $("#btnNewPassword").on("click", function () {

        location.href = "/user/myNewPassword";

    })

    $("#btnInterest").on("click", function () {

        location.href = "/user/interestList";

    })

    $("#btnWithdrawal").on("click", function () {

        location.href = "/user/withdrawal";

    })

})