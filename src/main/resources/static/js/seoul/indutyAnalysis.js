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
        tbody.appendChild(spinner); // 테이블 내부에 스피너 추가

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
                                    <div class="table_cell px-4 py-3 seq">${dto.storeCount}만</div>
                                </div>
                                <div class="table_small">
                                    <div class="table_cell">점포수 증가율</div>
                                    <div class="table_cell flex-1 px-4 py-3 seq ${dto.storeDiff > 0 ? 'increase' : dto.storeDiff < 0 ? 'decrease' : 'none'}">
                                        ${dto.storeDiff > 0 ? '(' + dto.storeDiff + '만) ' + dto.storeRate + '% up !' :
                        dto.storeDiff < 0 ? '(' + dto.storeDiff + '만) ' + dto.storeRate + '% down' :
                            '(0원) 0%'}
                                    </div>
                                </div>
                                `;
                    resultContainer.appendChild(row);
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
                                    <div class="table_cell px-4 py-3 seq">${dto.closeStoreCount}만</div>
                                </div>
                                <div class="table_small">
                                    <div class="table_cell">폐업수 증가율</div>
                                    <div class="table_cell flex-1 px-4 py-3 seq ${dto.closeStoreDiff > 0 ? 'increase' : dto.closeStoreDiff < 0 ? 'decrease' : 'none'}">
                                        ${dto.closeStoreDiff > 0 ? '(' + dto.closeStoreDiff + '만) ' + dto.closeStoreRate + '% up !' :
                        dto.closeStoreDiff < 0 ? '(' + dto.closeStoreDiff + '만) ' + dto.closeStoreRate + '% down' :
                            '(0원) 0%'}
                                    </div>
                                </div>
                                `;
                    resultContainer.appendChild(row);

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