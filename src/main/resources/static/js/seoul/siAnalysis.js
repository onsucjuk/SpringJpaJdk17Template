document.addEventListener("DOMContentLoaded", function() {

    // 업종 분류 기본 선택 표시
    let defaultIndutyClicked = document.getElementById("defaultIndutyClicked");
    defaultIndutyClicked.classList.add("clicked");

    // 시장 분석 분류 기본 선택 표시
    let defaultListClicked = document.getElementById("defaultListClicked");
    defaultListClicked.classList.add("clicked");

    // 업종 선택 불러오기
    let indutySorts = document.querySelectorAll(".indutySort");

    // 시장 분석 선택 불러오기
    let listSorts = document.querySelectorAll(".listSort");

    // 업종 선택 클릭시 이벤트
    indutySorts.forEach(function(indutySort) {
        indutySort.addEventListener("click", function() {
            // 클릭된 요소의 클래스에 "clicked" 클래스 추가
            let prevClicked = document.querySelector(".indutySort.clicked");
            if (prevClicked) {
                prevClicked.classList.remove("clicked");
            }

            // 현재 클릭된 요소에 "clicked" 클래스 추가
            this.classList.add("clicked");
        });
    });

    // 시장 분석 클릭시 이벤트
    listSorts.forEach(function(listSort) {
        listSort.addEventListener("click", function() {
            // 클릭된 요소의 클래스에 "clicked" 클래스 추가
            let prevClicked = document.querySelector(".listSort.clicked");
            if (prevClicked) {
                prevClicked.classList.remove("clicked");
            }

            // 현재 클릭된 요소에 "clicked" 클래스 추가
            this.classList.add("clicked");
        });
    });
});