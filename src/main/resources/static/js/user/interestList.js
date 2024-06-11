$(document).ready(function () {

    $("#btnListReg").on("click", function () {
        location.href = "/user/interestReg";
    })

})

// 상세보기 이동
function doDetail(seq) {
    location.href = "/user/interestInfo?nSeq=" + seq;
}

function doEdit(seq) {
    location.href = "/user/interestEdit?nSeq=" + seq;
}

function doDel(seq) {

    console.log("관심 업종 번호 : " + seq);

    if (confirm("삭제하시겠습니까?")) {
        $.ajax({
            url: "/user/deleteInterest",
            type: "post",
            dataType: "JSON",
            data: {
                "interestSeq": seq,
            },
            success: function (json) {

                if (json.result === 1) {
                    alert(json.msg)
                    location.href = "/user/interestList"
                } else {
                    alert(json.msg)
                    return;
                }

            },
            error: function (xhr, status, error) {
                console.error("AJAX request error:", status, error);
            }
        })
    } else {

        console.log("삭제 취소!");
    }
}
