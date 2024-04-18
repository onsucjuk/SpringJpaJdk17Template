$(document).ready(function () {
    // 버튼 클릭했을때, 발생되는 이벤트 생성함(onclick 이벤트와 동일함)
    $("#btnIndutyAnalysis").on("click", function () {

        indutyAnalysis();

    })
})

document.addEventListener("DOMContentLoaded", function() {

    //select 박스 요소
    let selectElement = document.querySelector('.selectBox');

    // 업종 분류 기본 선택 표시
    let defaultIndutyClicked = document.getElementById("defaultIndutyClicked");
    defaultIndutyClicked.classList.add("clicked");

    // 시장 분석 분류 기본 선택 표시
    let defaultListClicked = document.getElementById("defaultListClicked");
    defaultListClicked.classList.add("clicked");

    // 업종 선택 불러오기
    let indutySorts = document.querySelectorAll(".indutySort");

    // 업종 리스트 불러오기
    let indutySelectList = document.querySelectorAll(".indutySelectList");

    // 시장 분석 선택 불러오기
    let listSorts = document.querySelectorAll(".listSort");

    // 업종 선택
    let indutySelectSorts = document.querySelectorAll(".indutySelectSort");

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

            let text = this.textContent.trim();
            let id = "";

            console.log("업종 선택 " + text);

            if (text==="외식업") {

                id = "foodInduty";

                visibleList(id)

            } else if (text==="서비스업") {

                id = "serviceInduty";

                visibleList(id)

            } else if (text==="소매업") {

                id = "retailInduty";

                visibleList(id)

            } else { // 전체 클릭시 indutySelectSort.clicked 초기화

                id = "AllInduty"

                let prevClicked = document.querySelector(".indutySelectSort.clicked");
                if (prevClicked) {
                    prevClicked.classList.remove("clicked");
                }

                visibleList(id)

            }
        });
    });

    // 시장 분석 클릭시 이벤트
    // 클릭된 div 강조 및 rankList 내용 변경
    listSorts.forEach(function(listSort) {
        listSort.addEventListener("click", function () {
            // 클릭된 요소의 클래스에 "clicked" 클래스 추가
            let prevClicked = document.querySelector(".listSort.clicked");

            let prevClickedText = prevClicked.textContent.trim();
            let nowClickedText = this.textContent.trim();

            console.log("prevClickedText : " + prevClickedText);
            console.log("nowClickedText : " + nowClickedText);

            if (!(prevClickedText === nowClickedText)) {

                prevClicked.classList.remove("clicked");
                // 현재 클릭된 요소에 "clicked" 클래스 추가
                this.classList.add("clicked");

                //테이블 내용 변경 매커니즘
                makeSiList(nowClickedText);

            }
        });
    });

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


    // select 요소의 값이 변경될 때마다 이벤트를 처리합니다.
    selectElement.addEventListener('change', function() {
        // 선택된 값 가져오기
        let nowClickedText = document.querySelector(".listSort.clicked").textContent.trim();

        //테이블 내용 변경 매커니즘
        makeSiList(nowClickedText);
        moveMapPoint();

    });

});

function back(indutySort, indutyNm) {



}

function makeSiList(nowClickedText) {

    // 테이블 내용 변경
    let rankList = document.getElementById("rankList");
    let thead = rankList.querySelector("thead");
    let tbody = rankList.querySelector("tbody");

    // select 요소를 가져옴
    let selectElement = document.getElementById("guSelect");

    // 선택된 옵션의 값(value)을 가져옴
    let selectedValue = selectElement.value;

    // 가져온 값 확인
    console.log("선택된 값:", selectedValue);

    // thead 초기화
    thead.innerHTML = "";
    // 테이블 내용 초기화
    tbody.innerHTML = "";

    // 클릭된 값에 따라 다른 테이블 헤더와 데이터 로드
    if (nowClickedText === "매출액") {

        // 로딩 스피너 추가
        const spinner = document.createElement("div");
        spinner.className = "spinner"; // 스피너에 CSS 클래스 적용
        tbody.appendChild(spinner); // 테이블 내부에 스피너 추가

        // 기본값이 매출액이므로 리로드
        $.ajax({
            url : "/seoul/siSalesList",
            type : "post",
            dataType: "JSON",
            data: { "selectedValue": selectedValue },
            success: function (json) {

                let rSalesList = json;

                thead.innerHTML = `
                            <tr class="text-xs font-semibold tracking-wide text-left text-white uppercase border-b dark:border-gray-700 bg-gray-50 dark:text-gray-400 dark:bg-gray-800">
                            <th class="px-4 py-3 td-title-bg">순위</th>
                            <th class="px-4 py-3 td-title-bg">업종명</th>
                            <th class="px-4 py-3 td-title-bg">매출액</th>
                            <th class="px-4 py-3 td-title-bg">매출액 증가율</th>
                            </tr>
                            `;
                // 예시 데이터 로드 및 채우기
                for (let i = 0; i < rSalesList.length; i++) {
                    let dto = rSalesList[i];
                    let row = document.createElement("tr");
                    row.innerHTML = `
                                <td class="px-4 py-3 seq">${i + 1}</td>
                                <td class="px-4 py-3 seq">${dto.indutyNm}</td>
                                <td class="px-4 py-3 seq">${dto.fMonthSales}만</td>
                                <td class="px-4 py-3 seq">${'(' + dto.salesDiff + '만) ' + dto.salesRate + '% up !'}</td>
                                `;
                    tbody.appendChild(row);
                }


            },
            error: function(xhr, status, error) {
                console.error("AJAX request error:", status, error);
            },
            complete: function () {
                // AJAX 요청이 완료된 후에 로딩 스피너 삭제
                spinner.remove();
            }

        })
    }

    else if (nowClickedText === "점포수") {

        // 로딩 스피너 추가
        const spinner = document.createElement("div");
        spinner.className = "spinner"; // 스피너에 CSS 클래스 적용
        tbody.appendChild(spinner); // 테이블 내부에 스피너 추가

        $.ajax({
            url : "/seoul/siStoreList",
            type : "post",
            dataType: "JSON",
            data: { "selectedValue": selectedValue },
            success: function (json) {

                let rStoreList = json;

                thead.innerHTML = `
                            <tr class="text-xs font-semibold tracking-wide text-left text-white uppercase border-b dark:border-gray-700 bg-gray-50 dark:text-gray-400 dark:bg-gray-800">
                            <th class="px-4 py-3 td-title-bg">순위</th>
                            <th class="px-4 py-3 td-title-bg">업종명</th>
                            <th class="px-4 py-3 td-title-bg">점포수</th>
                            <th class="px-4 py-3 td-title-bg">점포수 증가율</th>
                            </tr>
                            `;
                // 예시 데이터 로드 및 채우기
                for (let i = 0; i < rStoreList.length; i++) {
                    let dto = rStoreList[i];
                    let row = document.createElement("tr");
                    row.innerHTML = `
                                <td class="px-4 py-3 seq">${i + 1}</td>
                                <td class="px-4 py-3 seq">${dto.indutyNm}</td>
                                <td class="px-4 py-3 seq">${dto.storeCount}개</td>
                                <td class="px-4 py-3 seq">${'(' + dto.storeDiff + '개) ' + dto.storeRate + '% up !'}</td>
                                `;
                    tbody.appendChild(row);
                }


            },
            error: function(xhr, status, error) {
                console.error("AJAX request error:", status, error);
            },
            complete: function () {
                // AJAX 요청이 완료된 후에 로딩 스피너 삭제
                spinner.remove();
            }

        })

    } else { // 나머지는 폐업수 하나

        // 로딩 스피너 추가
        const spinner = document.createElement("div");
        spinner.className = "spinner"; // 스피너에 CSS 클래스 적용
        tbody.appendChild(spinner); // 테이블 내부에 스피너 추가

        $.ajax({
            url: "/seoul/siStoreCloseList",
            type: "post",
            dataType: "JSON",
            data: { "selectedValue": selectedValue },
            success: function (json) {

                let rStoreCloseList = json;

                thead.innerHTML = `
                                <tr class="text-xs font-semibold tracking-wide text-left text-white uppercase border-b dark:border-gray-700 bg-gray-50 dark:text-gray-400 dark:bg-gray-800">
                                    <th class="px-4 py-3 td-title-bg">순위</th>
                                    <th class="px-4 py-3 td-title-bg">업종명</th>
                                    <th class="px-4 py-3 td-title-bg">폐업수</th>
                                    <th class="px-4 py-3 td-title-bg">폐업수 증가율</th>
                                </tr>
                            `;
                // 예시 데이터 로드 및 채우기
                for (let i = 0; i < rStoreCloseList.length; i++) {
                    let dto = rStoreCloseList[i];
                    let row = document.createElement("tr");
                    row.innerHTML = `
                                <td class="px-4 py-3 seq">${i + 1}</td>
                                <td class="px-4 py-3 seq">${dto.indutyNm}</td>
                                <td class="px-4 py-3 seq">${dto.closeStoreCount}개</td>
                                <td class="px-4 py-3 seq">${'(' + dto.closeStoreDiff + '개) ' + dto.closeStoreRate + '% up !'}</td>
                                `;
                    tbody.appendChild(row);

                }
            },
            error: function(xhr, status, error) {
                console.error("AJAX request error:", status, error);
            },
            complete: function () {
                // AJAX 요청이 완료된 후에 로딩 스피너 삭제
                spinner.remove();
            }
        })

    }

}

function indutyAnalysis() {

    let indudySort = document.querySelector(".indutySort.clicked").textContent.trim();
    let indutySelected = "";

    if (indudySort=="전체") { // 업종 분류가 전체면 선택된 업종도 "전체"

        indutySelected = "전체";

    } else { // 나머지 경우[외식업, 서비스업, 소매업 클릭되어있음) 선택된 업종명

        indutySelected = document.querySelector(".indutySelectSort.clicked").textContent.trim();

    }

    console.log("선택된 대분류 업종 : " + indudySort);
    console.log("선택된 업종 분석 : " + indutySelected);

    $.ajax({
        url : "/seoul/setIndutyInfo",
        type : "post",
        dataType: "JSON",
        data: { "indudySort" : indudySort,
            "indutySelected" : indutySelected},
        success:
            function (json) { // 호출이 성공했다면..

                if (json.result > 0) {

                    location.href = "/seoul/indutyAnalysis"; // 공지사항 리스트 이동

                } else {

                    alert("알 수 없는 문제가 발생했습니다. 다시 시도해주세요.");

                }
            }
    })

}

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


function moveMapPoint() {

    // select 요소를 가져옴
    let selectElement = document.getElementById("guSelect");

    // 선택된 옵션의 값(value)을 가져옴
    let selectedValue = selectElement.value;

    // 가져온 값 확인
    console.log("moveMapPoint 선택된 값:", selectedValue);

    // 클릭된 값에 따라 다른 테이블 헤더와 데이터 로드
    if (selectedValue === "11") {

        //서울 중심점(맵 시작 Default 위치)
        let lat = 37.551914;
        let lon = 126.991851;

        // 기본 지도로 돌아가기(zoom 7, 서울 중심점)
        defaultTo(lat, lon);

    }

    else  {

        $.ajax({
            url : "/seoul/getLatLon",
            type : "post",
            dataType: "JSON",
            data: { "selectedValue": selectedValue },
            success: function (json) {

                let lat = json.lat;
                let lon = json.lon;

                console.log("return lat : " + lat);
                console.log("return lon : " + lon);

                panTo(lat,lon);

            },
            error: function(xhr, status, error) {
                console.error("AJAX request error:", status, error);
            }
        })

    }

}

// 지도를 띄우는 코드
let container = document.getElementById('map_div'); //지도를 담을 영역의 DOM 레퍼런스
let options = { //지도를 생성할 때 필요한 기본 옵션
    center: new kakao.maps.LatLng(37.551914, 126.991851), //지도의 중심좌표.
    level: 9 //지도의 레벨(확대, 축소 정도)
};

let map = new kakao.maps.Map(container, options); //지도 생성 및 객체 리턴

function panTo(lat, lon) {
    // 이동할 위도 경도 위치를 생성합니다
    let moveLatLon = new kakao.maps.LatLng(lat, lon);

    map.setLevel(7);
    // 지도 중심을 부드럽게 이동시킵니다
    // 만약 이동할 거리가 지도 화면보다 크면 부드러운 효과 없이 이동합니다
    map.panTo(moveLatLon);

}

function defaultTo(lat, lon) {
    // 이동할 위도 경도 위치를 생성합니다
    let moveLatLon = new kakao.maps.LatLng(lat, lon);

    map.setLevel(9);
    // 지도 중심을 부드럽게 이동시킵니다
    // 만약 이동할 거리가 지도 화면보다 크면 부드러운 효과 없이 이동합니다
    map.panTo(moveLatLon);

}