$(document).ready(function () {
    // 버튼 클릭했을때, 발생되는 이벤트 생성함(onclick 이벤트와 동일함)
    $("#btnIndutyAnalysis").on("click", function () {

        indutyAnalysis();

    })

})
document.addEventListener("DOMContentLoaded", function() {

    // 시장 분석 분류 기본 선택 표시
    let defaultListClicked = document.getElementById("defaultListClicked");
    defaultListClicked.classList.add("clicked");
    // 업종 분류 기본 선택 표시
    let defaultIndutyClicked = document.getElementById("defaultIndutyClicked");
    defaultIndutyClicked.classList.add("clicked");


    // 업종 선택 불러오기
    let indutySorts = document.querySelectorAll(".indutySort");
    // 업종 선택 클릭시 이벤트
    indutySorts.forEach(function(indutySort) {
        indutySort.addEventListener("click", function() {
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

                visibleList(id)

            }
        });
    });


    // 분석 선택 불러오기
    let listSorts = document.querySelectorAll(".listSort");
    // 분석 선택 이벤트 핸들러 등록
    listSorts.forEach(function(listSort) {
        listSort.addEventListener("click", listSortClickHandler);
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


    //select 박스 요소
    let selectElement = document.querySelector('.selectBox');
    // 이벤트 핸들러 등록
    selectElement.addEventListener('change', selectChangeHandler);

});

function selectChangeHandler () {

    console.log("서울 시장 분석 시작!");
    // 선택된 값 가져오기
    let nowClickedText = document.querySelector(".listSort.clicked").textContent.trim();

    //테이블 내용 변경 매커니즘
    makeSiList(nowClickedText);
    moveMapPoint();

}

function induSelectChangeHandler () {

    console.log("서울 시장 분석 시작!");
    // 선택된 값 가져오기
    let nowClickedText = document.querySelector(".listSort.clicked").textContent.trim();

    //테이블 내용 변경 매커니즘
    makeInduList(nowClickedText);
    moveMapPointAndDraw();

}

function listSortClickHandler() {
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
}


// 업무 분석 선택
function induListSortClickHandler() {
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
        makeInduList(nowClickedText);
        moveMapPointAndDraw();
    }
}

function makeSiList(nowClickedText) {

    // 테이블 내용 변경
    let rankList = document.getElementById("rankList");
    let headerContainer = rankList.querySelector(".theader");
    let resultContainer = rankList.querySelector("#results");

// select 요소를 가져옴
    let selectElement = document.getElementById("guSelect");
    let selectedValue = "";

    if (selectElement) {
        // 선택된 옵션의 값(value)을 가져옴
        selectedValue = selectElement.value;
    }

// 가져온 값 확인
    console.log("선택된 값:", selectedValue);

// headerContainer 초기화
    headerContainer.innerHTML = "";
// 테이블 내용 초기화
    resultContainer.innerHTML = "";

// 클릭된 값에 따라 다른 테이블 헤더와 데이터 로드
    if (nowClickedText === "매출액") {

        // 로딩 스피너 추가
        const spinner = document.createElement("div");
        spinner.className = "spinner"; // 스피너에 CSS 클래스 적용
        resultContainer.appendChild(spinner); // 테이블 내부에 스피너 추가

        // 기본값이 매출액이므로 리로드
        $.ajax({
            url: "/seoul/siSalesList",
            type: "post",
            dataType: "JSON",
            data: { "selectedValue": selectedValue },
            success: function (json) {

                let rSalesList = json;

                headerContainer.innerHTML = `
                <div class="table_header px-4 py-3 td-title-bg">순위</div>
                <div class="table_header px-4 py-3 td-title-bg">업종명</div>
                <div class="table_header px-4 py-3 td-title-bg">점포당 매출액</div>
                <div class="table_header px-4 py-3 td-title-bg">매출액 증가율</div>
            `;
                resultContainer.appendChild(headerContainer);

                // 예시 데이터 로드 및 채우기
                for (let i = 0; i < rSalesList.length; i++) {
                    let dto = rSalesList[i];
                    let row = document.createElement("div");
                    row.className = "table_row bg-white dark:divide-gray-700 dark:bg-gray-800 flex-1";
                    row.innerHTML = `
                    <div class="table_small">
                        <div class="table_cell">순위</div>
                        <div class="table_cell px-4 py-3 seq">${i + 1}</div>
                    </div>
                    <div class="table_small">
                        <div class="table_cell">업종명</div>
                        <div class="table_cell px-4 py-3 seq">${dto.indutyNm}</div>
                    </div>
                    <div class="table_small">
                        <div class="table_cell">점포당 매출액</div>
                        <div class="table_cell px-4 py-3 seq">${dto.fMonthSales}만</div>
                    </div>
                    <div class="table_small">
                        <div class="table_cell">매출액 증가율</div>
                        <div class="table_cell flex-1 px-4 py-3 seq ${dto.salesDiff > 0 ? 'increase' : dto.salesDiff < 0 ? 'decrease' : 'none'}">
                            ${dto.salesDiff > 0 ? '(' + dto.salesDiff + '만) ' + dto.salesRate + '% up !' :
                        dto.salesDiff < 0 ? '(' + dto.salesDiff + '만) ' + dto.salesRate + '% down' :
                            '(0원) 0%'}
                        </div>
                    </div>
                `;
                    resultContainer.appendChild(row);
                }
            },
            error: function (xhr, status, error) {
                console.error("AJAX request error:", status, error);

                headerContainer.innerHTML = `
                <div class="table_header px-4 py-3 td-title-bg">순위</div>
                <div class="table_header px-4 py-3 td-title-bg">업종명</div>
                <div class="table_header px-4 py-3 td-title-bg">점포당 매출액</div>
                <div class="table_header px-4 py-3 td-title-bg">매출액 증가율</div>
            `;
                resultContainer.appendChild(headerContainer);
            },
            complete: function () {
                // AJAX 요청이 완료된 후에 로딩 스피너 삭제
                spinner.remove();
            }
        });
    }

    else if (nowClickedText === "점포수") {

        // 로딩 스피너 추가
        const spinner = document.createElement("div");
        spinner.className = "spinner"; // 스피너에 CSS 클래스 적용
        resultContainer.appendChild(spinner); // 테이블 내부에 스피너 추가

        $.ajax({
            url : "/seoul/siStoreList",
            type : "post",
            dataType: "JSON",
            data: { "selectedValue": selectedValue },
            success: function (json) {

                let rStoreList = json;

                headerContainer.innerHTML = `
                    <div class="table_header px-4 py-3 td-title-bg">순위</div>
                    <div class="table_header px-4 py-3 td-title-bg">업종명</div>
                    <div class="table_header px-4 py-3 td-title-bg">점포수</div>
                    <div class="table_header px-4 py-3 td-title-bg">점포수 증가율</div>
                `;

                resultContainer.appendChild(headerContainer);

                // 예시 데이터 로드 및 채우기
                for (let i = 0; i < rStoreList.length; i++) {
                    let dto = rStoreList[i];
                    let row = document.createElement("div");
                    row.className = "table_row bg-white dark:divide-gray-700 dark:bg-gray-800 flex-1";
                    row.innerHTML = `
                                <div class="table_small">
                                    <div class="table_cell">순위</div>
                                    <div class="table_cell px-4 py-3 seq">${i + 1}</div>
                                </div>
                                <div class="table_small">
                                    <div class="table_cell">업종명</div>
                                    <div class="table_cell px-4 py-3 seq">${dto.indutyNm}</div>
                                </div>
                                <div class="table_small">
                                    <div class="table_cell">점포수 매출액</div>
                                    <div class="table_cell px-4 py-3 seq">${dto.storeCount}개</div>
                                </div>
                                <div class="table_small">
                                    <div class="table_cell">점포수 증가율</div>
                                    <div class="table_cell flex-1 px-4 py-3 seq ${dto.storeDiff > 0 ? 'increase' : dto.storeDiff < 0 ? 'decrease' : 'none'}">
                                        ${dto.storeDiff > 0 ? '(' + dto.storeDiff + '개) ' + dto.storeRate + '% up !' :
                                        dto.storeDiff < 0 ? '(' + dto.storeDiff + '개) ' + dto.storeRate + '% down' :
                                        '(0원) 0%'}
                                    </div>
                                </div>
                                `;
                    resultContainer.appendChild(row);
                }


            },
            error: function(xhr, status, error) {
                console.error("AJAX request error:", status, error);

                headerContainer.innerHTML = `
                    <div class="table_header px-4 py-3 td-title-bg">순위</div>
                    <div class="table_header px-4 py-3 td-title-bg">업종명</div>
                    <div class="table_header px-4 py-3 td-title-bg">점포수</div>
                    <div class="table_header px-4 py-3 td-title-bg">점포수 증가율</div>
                `;

                resultContainer.appendChild(headerContainer);
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
        resultContainer.appendChild(spinner); // 테이블 내부에 스피너 추가

        $.ajax({
            url: "/seoul/siStoreCloseList",
            type: "post",
            dataType: "JSON",
            data: { "selectedValue": selectedValue },
            success: function (json) {

                let rStoreCloseList = json;

                headerContainer.innerHTML = `
                    <div class="table_header px-4 py-3 td-title-bg">순위</div>
                    <div class="table_header px-4 py-3 td-title-bg">업종명</div>
                    <div class="table_header px-4 py-3 td-title-bg">폐업수</div>
                    <div class="table_header px-4 py-3 td-title-bg">폐업수 증가율</div>
                `;

                resultContainer.appendChild(headerContainer);

                // 예시 데이터 로드 및 채우기
                for (let i = 0; i < rStoreCloseList.length; i++) {
                    let dto = rStoreCloseList[i];
                    let row = document.createElement("div");
                    row.className = "table_row bg-white dark:divide-gray-700 dark:bg-gray-800 flex-1";
                    row.innerHTML = `
                                <div class="table_small">
                                    <div class="table_cell">순위</div>
                                    <div class="table_cell px-4 py-3 seq">${i + 1}</div>
                                </div>
                                <div class="table_small">
                                    <div class="table_cell">업종명</div>
                                    <div class="table_cell px-4 py-3 seq">${dto.indutyNm}</div>
                                </div>
                                <div class="table_small">
                                    <div class="table_cell">폐업수</div>
                                    <div class="table_cell px-4 py-3 seq">${dto.closeStoreCount}개</div>
                                </div>
                                <div class="table_small">
                                    <div class="table_cell">폐업수 증가율</div>
                                    <div class="table_cell flex-1 px-4 py-3 seq ${dto.closeStoreDiff > 0 ? 'decrease' : dto.closeStoreDiff < 0 ? 'increase' : 'none'}">
                                        ${dto.closeStoreDiff > 0 ? '(' + dto.closeStoreDiff + '개) ' + dto.closeStoreRate + '% up' :
                                        dto.closeStoreDiff < 0 ? '(' + dto.closeStoreDiff + '개) ' + dto.closeStoreRate + '% down ! ' :
                                        '(0개) 0%'}
                                    </div>
                                </div>
                                `;
                    resultContainer.appendChild(row);

                }
            },
            error: function(xhr, status, error) {
                console.error("AJAX request error:", status, error);

                headerContainer.innerHTML = `
                    <div class="table_header px-4 py-3 td-title-bg">순위</div>
                    <div class="table_header px-4 py-3 td-title-bg">업종명</div>
                    <div class="table_header px-4 py-3 td-title-bg">폐업수</div>
                    <div class="table_header px-4 py-3 td-title-bg">폐업수 증가율</div>
                `;

                resultContainer.appendChild(headerContainer);

            },
            complete: function () {
                // AJAX 요청이 완료된 후에 로딩 스피너 삭제
                spinner.remove();
            }
        })

    }

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

function delVisible(id) {

    let elementToShow = document.getElementById(id);
    elementToShow.style.display = "none";

}

function setVisible(id) {

    let elementToShow = document.getElementById(id);
    elementToShow.style.display = "block";

}


/* ###################################
 *
 *
 *              업종 분석 관련 기능
 *
 *
 * ####################################
 * */
function indutyAnalysis() {

    console.log("업종 분류 UI로 변환 시작!")

    // 선택된 업종 내역 가져오기
    let indudySort = document.querySelector(".indutySort.clicked").textContent.trim();
    let indutySelected = "";
    // 업종 소분류 선택 여부
    let temp = document.querySelector(".indutySelectSort.clicked")

    console.log("선택된 대분류 업종 : " + indudySort);
    console.log("선택된 업종 분석 : " + indutySelected);

    // select 요소를 가져옴
    let selectElement = document.getElementById("guSelect");
    // 선택된 옵션의 값(value)을 가져옴
    let selectedValue = selectElement.value;
    // 가져온 값 확인
    console.log("선택된 값:", selectedValue);


    // 테이블 내용 변경
    // 제목 변경
    let title = "서울 업종 분석";
    changeTitle(title);
    // DataHead(선택 업종 표시)
    addDataHead(indudySort, indutySelected, temp);

    // select Id 변경 및 event변경
    let selectId = "guSelect";
    let chgId = "induGuSelect";
    chgSelectId(selectId, chgId);

    //버튼 변경
    let buttonName = "btnIndutyAnalysis";
    let chgButtonName = "btnSeoulAnalysis";
    let chgName = "시장 분석 돌아가기";
    let chgFunc = "seoulMarketback()";
    let addButtonName = "btnTotalAnalysis";
    let addName = "종합 분석 보기";
    let addFunc = "goTotalAnalysis()";
    changeButton(buttonName, chgButtonName, chgName, chgFunc, addButtonName, addName, addFunc);

    //업종 선택 안 보이게 하기
    let id = "dataBottom";
    delVisible(id);

    chgListSort(selectId);

    selectElement.value = selectedValue
    // 변경된 값에 따라 화면 갱신
    selectElement.dispatchEvent(new Event('change'));

}


// 서울 시장 분석으로 이동하기
function seoulMarketback() {

    // select 요소를 가져옴
    let selectedElement = document.getElementById("induGuSelect");
    // 선택된 옵션의 값(value)을 가져옴
    let selectedValue = selectedElement.value;
    // 가져온 값 확인
    console.log("선택된 값:", selectedValue);

    // 제목 변경
    let title = "서울시 시장 현황";
    changeTitle(title);

    // select Id 변경
    let selectId = "induGuSelect";
    let chgId = "guSelect";
    chgSelectId(selectId, chgId);

    // 선택 업종 보기 삭제
    let delId = "backButton";
    delElement(delId);

    //업종 선택 보이게 하기
    let id = "dataBottom";
    setVisible(id);

    //버튼 변경
    let buttonName = "btnSeoulAnalysis";
    let chgButtonName = "btnIndutyAnalysis";
    let chgName = "업종 분석 하기";
    let chgFunc = "indutyAnalysis()";
    changeButton(buttonName, chgButtonName, chgName, chgFunc);

    //버튼 삭제
    let delButtonName = "btnTotalAnalysis";
    delButton(delButtonName);

    // 분석 선택 변경
    chgListSort(selectId)

    // select 요소 가져오기
    let selectElement = document.getElementById("guSelect");
    selectElement.value = selectedValue;
    // 변경된 값에 따라 화면 갱신
    selectElement.dispatchEvent(new Event('change'));

}



function chgListSort(selectId) {

    console.log("현재 화면 : " + selectId);

    // 기존 이벤트 핸들러 제거
    // 분석 선택 불러오기
    let previousSelectElements = document.querySelectorAll(".listSort");

    if(selectId==="guSelect") { // 시장 분석 -> 업종 분석

        // 분석 선택 변경 폐업수 -> 주고객층
        document.querySelectorAll('.listSort').forEach(function(item) {
            if (item.textContent.trim() === '폐업수') {
                item.innerHTML = '<img src="/img/seoul/customer.png" alt="customer"/> 주고객층'; // 텍스트 변경
            }
        });


        // 이전 이벤트 핸들러 제거
        previousSelectElements.forEach(function(item) {
            item.removeEventListener('click', listSortClickHandler);
        });

        // 새로운 이벤트 핸들러 등록
        previousSelectElements.forEach(function(item) {
            item.addEventListener("click", induListSortClickHandler);
        });

        // 이전에 클릭된 소분류가 있다면 초기화
        let prevSortClicked = document.querySelector(".listSort.clicked");
        if (prevSortClicked) {
            prevSortClicked.classList.remove("clicked");
        }

        // 시장 분석 분류 기본 선택 표시
        let defaultListClicked = document.getElementById("defaultListClicked");
        defaultListClicked.classList.add("clicked");

    } else { // 업종 분석 -> 시장분석

            // 분석 선택 변경 주고객층 -> 폐업수
            document.querySelectorAll('.listSort').forEach(function(item) {
                if (item.textContent.trim() === '주고객층') {
                    item.innerHTML = '<img src="/img/seoul/close.png" alt="close"/> 폐업수'; // 텍스트 변경
                }
            });

        // 이전 이벤트 핸들러 제거
        previousSelectElements.forEach(function(item) {
            item.removeEventListener('click', induListSortClickHandler);
        });

        // 새로운 이벤트 핸들러 등록
        previousSelectElements.forEach(function(item) {
            item.addEventListener("click", listSortClickHandler);
        });

        // 이전에 클릭된 소분류가 있다면 초기화
        let prevSortClicked = document.querySelector(".listSort.clicked");
        if (prevSortClicked) {
            prevSortClicked.classList.remove("clicked");
        }

        // 시장 분석 분류 기본 선택 표시
        let defaultListClicked = document.getElementById("defaultListClicked");
        defaultListClicked.classList.add("clicked");

    }

}

function chgSelectId(elementId, chgElementId) {

    // select 요소 가져오기
    let selectElement = document.getElementById(elementId);

    // ID 속성 변경
    selectElement.setAttribute("id", chgElementId);

    console.log("select 이벤트 바꾸기 : 현재 " + elementId);

    // 기존 이벤트 핸들러 제거
    let previousSelectElement = document.querySelector('.selectBox');

    if(elementId==="guSelect") { // 시장분석 -> 업종분석

        previousSelectElement.removeEventListener('change', selectChangeHandler);

        previousSelectElement.addEventListener('change', induSelectChangeHandler);

    } else { // elementId === "induGuSelect"

        previousSelectElement.removeEventListener('change', induSelectChangeHandler);
        previousSelectElement.addEventListener('change', selectChangeHandler);
        // 새로운 이벤트 핸들러 정의

    }

}

function changeTitle(title) {

    let pageTitle = document.getElementById("siAnalysisTitle");

    pageTitle.innerText = title;

    // 헤드 타이틀 변경
    document.title = title;

}

function changeButton(buttonName, chgButtonName, chgName, chgFunc, addButtonName, addName, addFunc) {

    console.log("버튼 교체")

    // 새로운 버튼을 생성하고 추가
    // 이 버튼의 아이디 값을 chgButtonName 줘야함
    let newButton = $('<button id="' + chgButtonName + '" class="inlineButton flex items-left justify-between w-1/10 px-4 py-2 text-sm font-medium leading-5 text-white transition-colors duration-150 bg-purple-600 border border-transparent rounded-lg active:bg-purple-600 hover:bg-purple-700 focus:outline-none focus:shadow-outline-purple" type="button"><div class="w-full"><span class="px-4 py-3 text-sm font-semibold">' + chgName +  '</span><span class="ml-2" aria-hidden="true"></span></div></button>');
    $("#" + buttonName).parent().append(newButton);

    // 새로운 버튼에 클릭 이벤트 추가
    newButton.on("click", function () {

            // chgFunc 문자열로 된 함수명 호출
            eval(chgFunc);

    });

    console.log("addButton 존재 여부 : " + addButtonName);

    if (addButtonName) {

        let addButton = $('<button id="' + addButtonName + '" class="inlineButton flex items-left justify-between w-1/10 px-4 py-2 text-sm font-medium leading-5 text-white transition-colors duration-150 bg-purple-600 border border-transparent rounded-lg active:bg-purple-600 hover:bg-purple-700 focus:outline-none focus:shadow-outline-purple" type="button"><div class="w-full"><span class="px-4 py-3 text-sm font-semibold">' + addName +  '</span><span class="ml-2" aria-hidden="true"></span></div></button>');
        $("#" + buttonName).parent().append(addButton);

        addButton.on("click", function () {

            // chgFunc 문자열로 된 함수명 호출
            eval(addFunc);

        });

        console.log("2버튼 생성 완료");

    } else {

        console.log("1버튼 생성 완료");

    }

    // 원래의 버튼을 제거
    $("#" + buttonName).remove();

}

function delButton(delButtonName) {

    $("#" + delButtonName).remove();

    console.log(delButtonName + "버튼 삭제 완료");

}

function delElement(delId) {

    // 삭제할 요소 가져오기
    let delElement = document.getElementById(delId);

    console.log("삭제할 요소 : " + delElement);

    // 부모 요소에서 삭제할 요소 제거
    delElement.parentNode.removeChild(delElement);

}

function addDataHead(indudySort, indutySelected, temp) {

    let dataHead = document.getElementById("dataHead");
    let selectList = dataHead.querySelector(".selectList");

    // dataHead 초기화
    dataHead.innerHTML = "";

    if (temp) { // 소분류가 있으면..

        indutySelected = temp.textContent.trim();

        // 선택 업종 div 추가
        dataHead.innerHTML = `
                                    <div class="selectedInduty" id="backButton">
                                        <p class="textInline" th:thext="' 분석 업종 : '"></p>
                                        <p class="textInline" id="indutySortSelected"></p>
                                        <p class="textInline sign"> > </p>
                                        <p  class="textInline" id="indutyNameSelected"></p>
                                    </div>
                                `;
        // 선택된 분석 업종 현황 값 추가
        document.getElementById(id="indutySortSelected").textContent = indudySort;
        document.getElementById(id="indutyNameSelected").textContent = indutySelected;


    } else { // 소분류 없으면 대분류만 존재


        // 선택 업종 div 추가
        dataHead.innerHTML = `
                                    <div class="selectedInduty" id="backButton">
                                        <p class="textInline" th:thext="' 분석 업종 : '"></p>
                                        <p class="textInline" id="indutySortSelected"></p>
                                    </div>
                                `;

        // 선택된 분석 업종 현황
        document.getElementById(id="indutySortSelected").textContent = indudySort;

    }

    // back 버튼에 click 이벤트 추가 [임시 버튼 설정 나중에 기능 변경 예정]
    document.getElementById("backButton").addEventListener("click", function() {

        seoulMarketback();

    });
    dataHead.append(selectList);

}

// 업종 분석 테이블 만들기
// makeSiList 참고하기!!
function makeInduList(nowClickedText) {

    // 테이블 내용 변경
    let rankList = document.getElementById("rankList");
    let headerContainer = rankList.querySelector(".theader");
    let resultContainer = rankList.querySelector("#results");

    // select 요소를 가져옴
    let selectElement = document.getElementById("induGuSelect");
    let selectedValue = "";

    // 선택된 업종 가져오기
    let indutySort = document.getElementById("indutySortSelected");
    let indutyName = document.getElementById("indutyNameSelected");
    let indutyNameValue = "";
    let indutySortValue = indutySort.innerText;

    if (indutyName) { // 소분류 업종의 값이 있다면 추가 없으면 ""

        indutyNameValue = indutyName.innerText;

    }

    console.log("대분류 업종 : " + indutySortValue + " 소분류 업종 : " + indutyNameValue)


    if (selectElement) {

        // 선택된 옵션의 값(value)을 가져옴
        selectedValue = selectElement.value;

    }

    // 가져온 값 확인
    console.log("선택된 값:", selectedValue);

    // headerContainer 초기화
    headerContainer.innerHTML = "";
    // 테이블 내용 초기화
    resultContainer.innerHTML = "";

    // 클릭된 값에 따라 다른 테이블 헤더와 데이터 로드
    if (nowClickedText === "매출액") {

        // 로딩 스피너 추가
        const spinner = document.createElement("div");
        spinner.className = "spinner"; // 스피너에 CSS 클래스 적용
        resultContainer.appendChild(spinner); // 테이블 내부에 스피너 추가

        // 기본값이 매출액이므로 리로드
        $.ajax({
            url: "/seoul/guSalesList",
            type: "post",
            dataType: "JSON",
            data: {
                "selectedValue": selectedValue,
                "indutySortValue": indutySortValue,
                "indutyNameValue": indutyNameValue
            },
            success: function (json) {

                let rSalesList = json;

                headerContainer.innerHTML = `
                    <div class="table_header px-4 py-3 td-title-bg">순위</div>
                    <div class="table_header px-4 py-3 td-title-bg">업종명</div>
                    <div class="table_header px-4 py-3 td-title-bg">점포당 매출액</div>
                    <div class="table_header px-4 py-3 td-title-bg">매출액 증가율</div>
                 `;
                resultContainer.appendChild(headerContainer);

                // 예시 데이터 로드 및 채우기
                for (let i = 0; i < rSalesList.length; i++) {
                    let dto = rSalesList[i];
                    let row = document.createElement("div");
                    row.className = "table_row bg-white dark:divide-gray-700 dark:bg-gray-800 flex-1";
                    row.innerHTML = `
                    <div class="table_small">
                        <div class="table_cell">순위</div>
                        <div class="table_cell px-4 py-3 seq">${i + 1}</div>
                    </div>
                    <div class="table_small">
                        <div class="table_cell">업종명</div>
                        <div class="table_cell px-4 py-3 seq">${dto.seoulLocationNm}</div>
                    </div>
                    <div class="table_small">
                        <div class="table_cell">점포당 매출액</div>
                        <div class="table_cell px-4 py-3 seq">${dto.fMonthSales}만</div>
                    </div>
                    <div class="table_small">
                        <div class="table_cell">매출액 증가율</div>
                        <div class="table_cell flex-1 px-4 py-3 seq ${dto.salesDiff > 0 ? 'increase' : dto.salesDiff < 0 ? 'decrease' : 'none'}">
                            ${dto.salesDiff > 0 ? '(' + dto.salesDiff + '만) ' + dto.salesRate + '% up !' :
                        dto.salesDiff < 0 ? '(' + dto.salesDiff + '만) ' + dto.salesRate + '% down' :
                            '(0원) 0%'}
                        </div>
                    </div>
                `;
                    resultContainer.appendChild(row);
                }


            },
            error: function (xhr, status, error) {
                console.error("AJAX request error:", status, error);

                headerContainer.innerHTML = `
                    <div class="table_header px-4 py-3 td-title-bg">순위</div>
                    <div class="table_header px-4 py-3 td-title-bg">업종명</div>
                    <div class="table_header px-4 py-3 td-title-bg">점포당 매출액</div>
                    <div class="table_header px-4 py-3 td-title-bg">매출액 증가율</div>
                 `;
                resultContainer.appendChild(headerContainer);

            },
            complete: function () {
                // AJAX 요청이 완료된 후에 로딩 스피너 삭제
                spinner.remove();
            }

        })
    } else if (nowClickedText === "점포수") { // 여기 수정 할 차례

        // 로딩 스피너 추가
        const spinner = document.createElement("div");
        spinner.className = "spinner"; // 스피너에 CSS 클래스 적용
        resultContainer.appendChild(spinner); // 테이블 내부에 스피너 추가

        $.ajax({
            url: "/seoul/guStoreList",
            type: "post",
            dataType: "JSON",
            data: {
                "selectedValue": selectedValue,
                "indutySortValue": indutySortValue,
                "indutyNameValue": indutyNameValue
            },
            success: function (json) {

                let rStoreList = json;

                headerContainer.innerHTML = `
                    <div class="table_header px-4 py-3 td-title-bg">순위</div>
                    <div class="table_header px-4 py-3 td-title-bg">업종명</div>
                    <div class="table_header px-4 py-3 td-title-bg">점포수</div>
                    <div class="table_header px-4 py-3 td-title-bg">점포수 증가율</div>
                `;

                resultContainer.appendChild(headerContainer);

                // 예시 데이터 로드 및 채우기
                for (let i = 0; i < rStoreList.length; i++) {
                    let dto = rStoreList[i];
                    let row = document.createElement("div");
                    row.className = "table_row bg-white dark:divide-gray-700 dark:bg-gray-800 flex-1";
                    row.innerHTML = `
                                <div class="table_small">
                                    <div class="table_cell">순위</div>
                                    <div class="table_cell px-4 py-3 seq">${i + 1}</div>
                                </div>
                                <div class="table_small">
                                    <div class="table_cell">업종명</div>
                                    <div class="table_cell px-4 py-3 seq">${dto.seoulLocationNm}</div>
                                </div>
                                <div class="table_small">
                                    <div class="table_cell">점포수</div>
                                    <div class="table_cell px-4 py-3 seq">${dto.storeCount}개</div>
                                </div>
                                <div class="table_small">
                                    <div class="table_cell">점포수 증가율</div>
                                    <div class="table_cell flex-1 px-4 py-3 seq ${dto.storeDiff > 0 ? 'increase' : dto.storeDiff < 0 ? 'decrease' : 'none'}">
                                        ${dto.storeDiff > 0 ? '(' + dto.storeDiff + '개) ' + dto.storeRate + '% up !' :
                                        dto.storeDiff < 0 ? '(' + dto.storeDiff + '개) ' + dto.storeRate + '% down' :
                                        '(0개) 0%'}
                                    </div>
                                </div>
                                `;
                    resultContainer.appendChild(row);
                }


            },
            error: function (xhr, status, error) {
                console.error("AJAX request error:", status, error);

                headerContainer.innerHTML = `
                    <div class="table_header px-4 py-3 td-title-bg">순위</div>
                    <div class="table_header px-4 py-3 td-title-bg">업종명</div>
                    <div class="table_header px-4 py-3 td-title-bg">점포수</div>
                    <div class="table_header px-4 py-3 td-title-bg">점포수 증가율</div>
                `;

                resultContainer.appendChild(headerContainer);
            },
            complete: function () {
                // AJAX 요청이 완료된 후에 로딩 스피너 삭제
                spinner.remove();
            }

        })

    } else { // 나머지는 주고객층 하나

        // 로딩 스피너 추가
        const spinner = document.createElement("div");
        spinner.className = "spinner"; // 스피너에 CSS 클래스 적용
        resultContainer.appendChild(spinner); // 테이블 내부에 스피너 추가

        $.ajax({
            url: "/seoul/guCustomerList",
            type: "post",
            dataType: "JSON",
            data: {
                "selectedValue": selectedValue,
                "indutySortValue": indutySortValue,
                "indutyNameValue": indutyNameValue
            },
            success: function (json) {

                let rCustomerList = json;

                headerContainer.innerHTML = `
                    <div class="table_header px-4 py-3 td-title-bg">나이대</div>
                    <div class="table_header px-4 py-3 td-title-bg">지역명</div>
                    <div class="table_header px-4 py-3 td-title-bg">점포당 매출액</div>
                    <div class="table_header px-4 py-3 td-title-bg">주고객층 비율</div>
                `;

                resultContainer.appendChild(headerContainer);

                // 예시 데이터 로드 및 채우기
                for (let i = 0; i < rCustomerList.length; i++) {
                    let dto = rCustomerList[i];
                    let row = document.createElement("div");
                    row.className = "table_row bg-white dark:divide-gray-700 dark:bg-gray-800 flex-1";
                    row.innerHTML = `
                                <div class="table_small">
                                    <div class="table_cell">나이대</div>
                                    <div class="table_cell px-4 py-3 seq">${(i + 1) * 10 + '대'}</div>
                                </div>
                                <div class="table_small">
                                    <div class="table_cell">지역명</div>
                                    <div class="table_cell px-4 py-3 seq">${dto.seoulLocationNm}</div>
                                </div>
                                <div class="table_small">
                                    <div class="table_cell">점포당 매출액</div>
                                    <div class="table_cell px-4 py-3 seq">${dto.fMonthSales}만</div>
                                </div>
                                <div class="table_small">
                                    <div class="table_cell">주고객층 비율</div>
                                    <div class="table_cell flex-1 px-4 py-3 seq ageSales">
                                        ${'(' + dto['age' + ((i + 1) * 10) + 'Sale'] + '만) ' + dto['age' + ((i + 1) * 10) + 'SalesRate'] + '%'}
                                    </div>
                                </div>
                                `
                    resultContainer.appendChild(row);

                }

                //최고 수치에 top속성 부여
                addClassTop();

            },
            error: function (xhr, status, error) {
                console.error("AJAX request error:", status, error);

                headerContainer.innerHTML = `
                    <div class="table_header px-4 py-3 td-title-bg">나이대</div>
                    <div class="table_header px-4 py-3 td-title-bg">지역명</div>
                    <div class="table_header px-4 py-3 td-title-bg">점포당 매출액</div>
                    <div class="table_header px-4 py-3 td-title-bg">주고객층 비율</div>
                `;

                resultContainer.appendChild(headerContainer);
            },
            complete: function () {
                // AJAX 요청이 완료된 후에 로딩 스피너 삭제
                spinner.remove();
            }
        })

    }
}

function addClassTop() {
    // 테이블 요소를 가져옴
    let table = document.getElementById("rankList");

    // 최대 비율 및 매출 초기값 설정
    let maxPercentage = 0;
    let maxSales = 0;
    let maxRow;

    // 테이블의 각 행을 반복
    let rows = table.querySelectorAll('.table_row');
    for (let i = 0; i < rows.length; i++) {
        // 현재 행에서 % 값 및 매출 추출
        let percentageText = rows[i].querySelector(".ageSales").textContent;
        let percentageMatch = percentageText.match(/(\d+(\.\d+)?)%/);
        let salesText = rows[i].querySelector(".px-4.py-3.seq").textContent;
        let sales = parseFloat(salesText.replace('만', '').replace(/,/g, ''));

        // 문자열을 부동 소수점 숫자로 변환
        let percentageValue = parseFloat(percentageMatch[1]);

        // 현재 비율이 최대 비율보다 높으면 최대값 및 해당 행 갱신
        if (percentageValue > maxPercentage || (percentageValue === maxPercentage && sales > maxSales)) {
            maxPercentage = percentageValue;
            maxSales = sales;
            maxRow = rows[i];
        }
    }

    // 최대 비율을 가진 행에 "top" 클래스 추가
    maxRow.classList.add("top");
}

// 종합 분석으로 이동하기
function goTotalAnalysis() {

    let guValue = document.getElementById("induGuSelect").value
    let indutySort = document.getElementById("indutySortSelected").innerText;
    let indutyName = document.getElementById("indutyNameSelected");
    let induty = "";

    var selectElement = document.getElementById('induGuSelect');
    var guName = selectElement.options[selectElement.selectedIndex].text;

    console.log("선택된 구 : " + guValue);
    console.log("선택된 업종 분류 : " + indutySort);
    console.log("선택된 업종명 : " + indutyName);
    console.log("선택된 구 이름 : " + guName);

    if (guValue!=null && indutySort!=null) { // 값이 null이 아니라면 실행

        if (guValue === "11") {

            alert("자세한 종합 분석을 위해서 자치구를 선택해주세요.")

            $("#induGuSelect").focus();

            return;

        }

        if (indutyName) {

            induty = indutyName.innerText;

        } else {

            alert("자세한 종합 분석을 위해서 업종을 '소분류'로 '자세히' 선택해주세요.")

            document.getElementById("indutySortSelected").scrollIntoView();

            return;

        }

        /*if(indutyName===null) { // 구체적 업종명 없으면 대분류

            induty = indutySort;

        } else { // 있으면 업종명

            induty = indutyName.innerText;

        }*/

        window.open("/seoul/totalAnalysis?guSelect=" + guValue + "&guName=" + guName + "&induty=" + induty, "_blank");

    } else {

        alert("선택된 업종 분류나 지역에 문제가 있습니다. 확인 후 다시 시도해주세요.");
        $("#indutySortSelected").focus();

    }

}

/* #################################
 *
 *
 *              맵 관련 기능
 *
 *
 * ##################################
 * */

// 시장 분석 맵 이동
function moveMapPoint() {

    // select 요소를 가져옴
    let selectElement = document.getElementById("guSelect");

    // 선택된 옵션의 값(value)을 가져옴
    let selectedValue =  "";

    if (selectElement) {

        selectedValue = selectElement.value;

    }

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

// 업종 분석 이동하고 순위 그리기
// 나중에 그리기 업데이트 할 것!!
function moveMapPointAndDraw() {

    let selectElement = document.getElementById("induGuSelect");
    let selectedValue = ""

    // 선택된 옵션의 값(value)을 가져옴
    if(selectElement) {
        selectedValue = selectElement.value;
    }

    // 클릭된 값에 따라 다른 테이블 헤더와 데이터 로드
    if (selectedValue === "11") {

        //서울 중심점(맵 시작 Default 위치)
        let lat = 37.551914;
        let lon = 126.991851;

        // 기본 지도로 돌아가기(zoom 7, 서울 중심점)
        defaultTo(lat, lon);

    }

    else  { // 임시로 시장분석거 그대로 이동만함

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