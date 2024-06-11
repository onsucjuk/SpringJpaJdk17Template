$(document).ready(function () {
    // 버튼 클릭했을때, 발생되는 이벤트 생성함(onclick 이벤트와 동일함)
    $("#btnInterestReg").on("click", function () {

        let selectElement = document.getElementById('guSelect');
        let guValue = selectElement.value
        let guName = selectElement.options[selectElement.selectedIndex].text;

        // 대분류 업종 가져오기
        let prevInduClicked = document.querySelector(".indutySort.clicked");
        let sortIndutyCd = ""
        if(prevInduClicked) {
            sortIndutyCd = prevInduClicked.innerText;
        } else { // 대분류 업종이 없다면
            alert("대분류를 선택해주세요.")
            return;
        }

        // 소분류 업종 가져오기
        let prevSortClicked = document.querySelector(".indutySelectSort.clicked");
        let sortIndutyNm = ""

        if(prevSortClicked) {
            sortIndutyNm = prevSortClicked.innerText;
        } else { // 소분류 업종이 없다면
            alert("자세한 업종을 선택해주세요.")
            return;
        }

        console.log("선택된 구 코드 : " + guValue);
        console.log("선택된 구 이름 : " + guName);
        console.log("대분류 코드 : " + sortIndutyCd);
        console.log("소분류 업종명 : " + sortIndutyNm);

        $.ajax({
            url : "/user/insertInterest",
            type : "post",
            dataType: "JSON",
            data: { "seoulLocationCd" : guValue,
                    "seoulLocationNm" : guName,
                    "indutyCd" : sortIndutyCd,
                    "indutyNm" : sortIndutyNm
            },
            success: function (json) {

                if(json.result === 1) {
                    alert(json.msg)
                    location.href="/user/interestList"
                } else {
                    alert(json.msg)
                    return;
                }

            },
            error: function(xhr, status, error) {
                console.error("AJAX request error:", status, error);
            }
        })



    })

    $("#btnBack").on("click", function () {
        location.href = "/user/interestList";
    })

})

document.addEventListener("DOMContentLoaded", function() {

    let defaultIndutyClicked = document.getElementById("defaultIndutyClicked");
    defaultIndutyClicked.classList.add("clicked");

    let defaultText = defaultIndutyClicked.innerText
    let defaultId = ""

    if (defaultText === "외식업") {

        defaultId = "foodInduty";

        visibleList(defaultId)

    } else if (defaultText === "서비스업") {

        defaultId = "serviceInduty";

        visibleList(defaultId)

    } else if (defaultText === "소매업") {

        defaultId = "retailInduty";

        visibleList(defaultId)

    } else {

        alert("잘못된 업종 선택입니다. 다시 시도해주세요.")
        return;

    }

    // 업종 선택 불러오기
    let indutySorts = document.querySelectorAll(".indutySort");
    // 업종 선택 클릭시 이벤트
    indutySorts.forEach(function (indutySort) {
        indutySort.addEventListener("click", function () {
            // 클릭된 요소의 클래스에 "clicked" 클래스 추가
            let prevClicked = document.querySelector(".indutySort.clicked");
            if (prevClicked) {
                prevClicked.classList.remove("clicked");
            }

            // 이전에 클릭된 소분류가 있다면 초기화
            let prevSortClicked = document.querySelector(".indutySelectSort.clicked");
            if (prevSortClicked) {
                prevSortClicked.classList.remove("clicked");
            }

            // 현재 클릭된 요소에 "clicked" 클래스 추가
            this.classList.add("clicked");

            let text = this.textContent.trim();
            let id = "";

            console.log("업종 선택 " + text);

            if (text === "외식업") {

                id = "foodInduty";

                visibleList(id)

            } else if (text === "서비스업") {

                id = "serviceInduty";

                visibleList(id)

            } else if (text === "소매업") {

                id = "retailInduty";

                visibleList(id)

            } else {

                alert("잘못된 업종 선택입니다. 다시 시도해주세요.")
                return;

            }
        });
    });

    // 업종 선택
    let indutySelectSorts = document.querySelectorAll(".indutySelectSort");
    // 업종 선택 클릭시
    indutySelectSorts.forEach(function(indutySelectSort) {
        indutySelectSort.addEventListener("click", function () {
            // 클릭된 요소의 클래스에 "clicked" 클래스 추가
            let prevClicked = document.querySelector(".indutySelectSort.clicked");
            if (prevClicked) {
                prevClicked.classList.remove("clicked");
            }

            // 현재 클릭된 요소에 "clicked" 클래스 추가
            this.classList.add("clicked");

            let nowClickedText = this.textContent.trim();

            console.log("현재 클릭된 업종" + nowClickedText);

        });
    });
})


function visibleList(id) {

    // 선택된 id에 해당하는 요소를 보이도록 변경
    let elementToShow = document.getElementById(id);
    if (elementToShow) {
        elementToShow.style.display = "grid";
    }

    // 다른 요소들은 숨김 처리
    let allElements = document.querySelectorAll('.indutySelectList');
    allElements.forEach(function(element) {
        if (element.id !== id) {
            element.style.display = "none";
        }
    });
}

function delVisible(id) {

    let elementToShow = document.getElementById(id);
    elementToShow.style.display = "none";

}

function setVisible(id) {

    let elementToShow = document.getElementById(id);
    elementToShow.style.display = "block";

}
