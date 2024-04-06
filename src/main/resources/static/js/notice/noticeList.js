$(document).ready(function () {

    $("#btnListReg").on("click", function () {
        location.href = "/notice/noticeReg";
    })

})

// 상세보기 이동
function doDetail(seq) {
    location.href = "/notice/noticeInfo?nSeq=" + seq;
}

