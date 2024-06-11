$(document).ready(function () {

    $("#btnBack").on("click", function () {
        location.href = "/user/interestList";
    })

    $("#btnTotalAnalysis").on("click", function () {

        // 지역, 지역코드, 업종명 가져오기
        const guValue = "";
        const induty = "";
        const guName = "";


        location.href = "/seoul/totalAnalysis?guSelect=" + guValue + "&guName=" + guName + "&induty=" + induty

    })

})