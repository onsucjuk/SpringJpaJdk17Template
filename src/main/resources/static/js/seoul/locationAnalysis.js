document.addEventListener("DOMContentLoaded", function() {

    // 분석 분류 default 설정
    let defaultListClicked = document.getElementById("defaultListClicked");
    defaultListClicked.classList.add("clicked");

    // 분석 선택 불러오기
    let listSorts = document.querySelectorAll(".listSort");
    // 분석 선택 이벤트 핸들러 등록
    listSorts.forEach(function(listSort) {
        listSort.addEventListener("click", listSortClickHandler);
    });


    // clicked되어진 요소 가져오기(defalut값)
    let nowClickedText = document.querySelector(".listSort.clicked").textContent.trim();
    // 내용 보여주기
    makeSiList(nowClickedText)
});


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
        makeSiList(nowClickedText)
    }
}

function makeSiList(nowClickedText) {

    // 현재 페이지 url 가져오기
    const currentUrl = window.location.href;
    const urlObj = new URL(currentUrl);
    // url 파라미터 가져오기
    const params = new URLSearchParams(urlObj.search);

    // 주고객층 나이대, 성별, 시간 정보 가져오기
    const maxAge = params.get('ageSales');
    const maxGender = params.get('genderSales');
    const maxTime = params.get('timeSales');

    // rankDiv를 가져오기
    const rankDiv = document.getElementById('btnBnav2');
    // 초기화
    rankDiv.innerHTML = '';

    // 클릭된 값에 따라 다른 테이블 헤더와 데이터 로드
    if (nowClickedText === "매출액") {

        for(let i = 0; i < salesList.length; i++) {

            let tempDTO = salesList.at(i)

            let locationNm = getLocationNm(tempDTO)
            let monthSales = getMonthSales(tempDTO).toFixed(2)

            let row = document.createElement("div");
            if (i === 0) {
                row.className = "comment_inline indutyList gold-border";
            } else if (i === 1) {
                row.className = "comment_inline indutyList silver-border";
            } else {
                row.className = "comment_inline indutyList bronze-border";
            }
            row.id = "rank" + (i + 1);

            row.innerHTML = `
            <p class="title" id="rank${i + 1}Number">${i + 1} 위</p>
            <p class="title" id="rank${i + 1}LocationName">${locationNm}</p>
            <p id="rank${i + 1}Content">${monthSales} 만</p>
        `;

            rankDiv.appendChild(row);
        }

    } else if (nowClickedText === "매출률") {

        for(let i = 0; i < salesRateList.length; i++) {

            let tempDTO = salesRateList.at(i)

            let locationNm = getLocationNm(tempDTO)
            let monthSales = getFmonthSales(tempDTO)
            let salesDiff = getSalesDiff(tempDTO).toFixed(2)
            let salesRate = getSalesRate(tempDTO)

            let row = document.createElement("div");
            if (i === 0) {
                row.className = "comment_inline indutyList gold-border";
            } else if (i === 1) {
                row.className = "comment_inline indutyList silver-border";
            } else {
                row.className = "comment_inline indutyList bronze-border";
            }
            row.id = "rank" + (i + 1);

            row.innerHTML = `
            <p class="title" id="rank${i + 1}Number">${i + 1} 위</p>
            <p class="title" id="rank${i + 1}LocationName">${locationNm}</p>
            <p id="rank${i + 1}Content">
                ${salesDiff > 0 ? monthSales + '만' + '<br>(' + salesDiff + '만)<br><strong>' + salesRate + '% up!</strong>' : 
                salesDiff < 0 ? monthSales + '만' + '<br>(' + salesDiff + '만)<br><strong>' + salesRate + '% down</strong>' :
                '0 (0원) 0%'}
            </p>
        `;

            rankDiv.appendChild(row);
        }

    } else if (nowClickedText === "점포수") {

        for(let i = 0; i < storeList.length; i++) {

            let tempDTO = storeList.at(i)

            let locationNm = getLocationNm(tempDTO)
            let storeCount = getStoreCount(tempDTO)

            let row = document.createElement("div");
            if (i === 0) {
                row.className = "comment_inline indutyList gold-border";
            } else if (i === 1) {
                row.className = "comment_inline indutyList silver-border";
            } else {
                row.className = "comment_inline indutyList bronze-border";
            }
            row.id = "rank" + (i + 1);

            row.innerHTML = `
            <p class="title" id="rank${i + 1}Number">${i + 1} 위</p>
            <p class="title" id="rank${i + 1}LocationName">${locationNm}</p>
            <p id="rank${i + 1}Content">${storeCount} 개</p>
        `;

            rankDiv.appendChild(row);
        }

    } else if (nowClickedText === "나이대") {

        for(let i = 0; i < ageList.length; i++) {

            let tempDTO = ageList.at(i)

            let locationNm = getLocationNm(tempDTO)
            let ageSales = getAgeSales(tempDTO, maxAge).toFixed(2)

            let row = document.createElement("div");
            if (i === 0) {
                row.className = "comment_inline indutyList gold-border";
            } else if (i === 1) {
                row.className = "comment_inline indutyList silver-border";
            } else {
                row.className = "comment_inline indutyList bronze-border";
            }
            row.id = "rank" + (i + 1);

            row.innerHTML = `
            <p class="title" id="rank${i + 1}Number">${i + 1} 위</p>
            <p class="title" id="rank${i + 1}LocationName">${locationNm}</p>
            <p id="rank${i + 1}Content">${ageSales} 만</p>
        `;

            rankDiv.appendChild(row);
        }

    } else if (nowClickedText === "성별") {

        for(let i = 0; i < genderList.length; i++) {

            let tempDTO = genderList.at(i)

            let locationNm = getLocationNm(tempDTO)
            let genderSales = getGenderSales(tempDTO, maxGender).toFixed(2)

            let row = document.createElement("div");
            if (i === 0) {
                row.className = "comment_inline indutyList gold-border";
            } else if (i === 1) {
                row.className = "comment_inline indutyList silver-border";
            } else {
                row.className = "comment_inline indutyList bronze-border";
            }
            row.id = "rank" + (i + 1);

            row.innerHTML = `
            <p class="title" id="rank${i + 1}Number">${i + 1} 위</p>
            <p class="title" id="rank${i + 1}LocationName">${locationNm}</p>
            <p id="rank${i + 1}Content">${genderSales} 만</p>
        `;

            rankDiv.appendChild(row);
        }

    } else if (nowClickedText === "시간대") {

        for(let i = 0; i < timeList.length; i++) {

            let tempDTO = timeList.at(i)

            let locationNm = getLocationNm(tempDTO)
            let timeSales = getTimeSales(tempDTO, maxTime).toFixed(2)

            let row = document.createElement("div");
            if (i === 0) {
                row.className = "comment_inline indutyList gold-border";
            } else if (i === 1) {
                row.className = "comment_inline indutyList silver-border";
            } else {
                row.className = "comment_inline indutyList bronze-border";
            }
            row.id = "rank" + (i + 1);

            row.innerHTML = `
            <p class="title" id="rank${i + 1}Number">${i + 1} 위</p>
            <p class="title" id="rank${i + 1}LocationName">${locationNm}</p>
            <p id="rank${i + 1}Content">${timeSales} 만</p>
        `;

            rankDiv.appendChild(row);
        }

    } else {

        alert("분석할 수 없는 탭입니다. 분석 분류를 제대로 선택해주세요.")
        location.reload()

    }

    let rankedList = getRankedLocation();

    setCluster(rankedList)
}

function getMonthSales(dto) {
    // monthSales가 메서드일 경우 호출하고, 속성일 경우 그대로 반환합니다.
    return typeof dto.monthSales === 'function' ? dto.monthSales() : dto.monthSales;
}

function getFmonthSales(dto) {
    // monthSales가 메서드일 경우 호출하고, 속성일 경우 그대로 반환합니다.
    return typeof dto.fMonthSales === 'function' ? dto.fMonthSales() : dto.fMonthSales;
}

function getSalesDiff(dto) {
    // monthSales가 메서드일 경우 호출하고, 속성일 경우 그대로 반환합니다.
    return typeof dto.salesDiff === 'function' ? dto.salesDiff() : dto.salesDiff;
}

function getSalesRate(dto) {
    // monthSales가 메서드일 경우 호출하고, 속성일 경우 그대로 반환합니다.
    return typeof dto.salesRate === 'function' ? dto.salesRate() : dto.salesRate;
}

function getLocationNm(dto) {
    // monthSales가 메서드일 경우 호출하고, 속성일 경우 그대로 반환합니다.
    let locationNm = typeof dto.seoulLocationNm === 'function' ? dto.seoulLocationNm() : dto.seoulLocationNm;

    return locationNm.replace(/\?/g, '.');
}


function getAgeSales(dto, age) {

    let tempSales = 0;

    // 나이대 별 매출액
    if (age === "10") {
        tempSales = typeof dto.age10Sales === 'function' ? dto.age10Sales() : dto.age10Sales;
    } else if (age === "20") {
        tempSales = typeof dto.age20Sales === 'function' ? dto.age20Sales() : dto.age20Sales;
    } else if (age === "30") {
        tempSales = typeof dto.age30Sales === 'function' ? dto.age30Sales() : dto.age30Sales;
    } else if (age === "40") {
        tempSales = typeof dto.age40Sales === 'function' ? dto.age40Sales() : dto.age40Sales;
    } else if (age === "50") {
        tempSales = typeof dto.age50Sales === 'function' ? dto.age50Sales() : dto.age50Sales;
    } else if (age === "60") {
        tempSales = typeof dto.age60Sales === 'function' ? dto.age60Sales() : dto.age60Sales;
    }

    console.log("maxAge : " + age)
    console.log("ageSales : " + tempSales)

    return tempSales;
}

function getStoreCount(dto) {
    // storeCount 메서드일 경우 호출하고, 속성일 경우 그대로 반환합니다.
    return typeof dto.storeCount === 'function' ? dto.storeCount() : dto.storeCount;
}

function getGenderSales(dto, gender) {
    // 나이대 별 매출액
    let genderSales = 0;

    if(gender==="male"){
        genderSales = typeof dto.maleSales === 'function' ? dto.maleSales() : dto.maleSales;
    } else {
        genderSales = typeof dto.femaleSales === 'function' ? dto.femaleSales() : dto.femaleSales;
    }

    return genderSales;
}

function getTimeSales(dto, time) {
    // 나이대 별 매출액
    let tempSales = 0;

    if(time==="0006") {
        tempSales = typeof dto.time0006Sales === 'function' ? dto.time0006Sales() : dto.time0006Sales;
    } else if(time==="0611") {
        tempSales = typeof dto.time0611Sales === 'function' ? dto.time0611Sales() : dto.time0611Sales;
    } else if(time==="1114") {
        tempSales = typeof dto.time1114Sales === 'function' ? dto.time1114Sales() : dto.time1114Sales;
    } else if(time==="1417") {
        tempSales = typeof dto.time1417Sales === 'function' ? dto.time1417Sales() : dto.time1417Sales;
    } else if(time==="1721") {
        tempSales = typeof dto.time1721Sales === 'function' ? dto.time1721Sales() : dto.time1721Sales;
    } else {
        tempSales = typeof dto.time2124Sales === 'function' ? dto.time2124Sales() : dto.time2124Sales;
    }

    return tempSales;
}

/* 여기서부터 지도 관련 기능 */

let mapContainer = document.getElementById('map_div'), // 지도를 표시할 div
    mapOption = {
        center: new kakao.maps.LatLng(37.551914, 126.991851), //지도의 중심좌표.
        level: 8 //지도의 레벨(확대, 축소 정도)
    };

function setCluster(rankedList) {

    let indutyNm1 = rankedList.at(0)
    let indutyNm2 = rankedList.at(1)
    let indutyNm3 = rankedList.at(2)

// 마커 클러스터러를 생성합니다
        let clusterer = new kakao.maps.MarkerClusterer({
        map: map, // 마커들을 클러스터로 관리하고 표시할 지도 객체
        averageCenter: true, // 클러스터에 포함된 마커들의 평균 위치를 클러스터 마커 위치로 설정
        minLevel: 10, // 클러스터 할 최소 지도 레벨
        calculator: [10, 30, 50], // 클러스터의 크기 구분 값, 각 사이값마다 설정된 text나 style이 적용된다
        texts: getTexts, // texts는 ['삐약', '꼬꼬', '꼬끼오', '치멘'] 이렇게 배열로도 설정할 수 있다
        styles: [{ // calculator 각 사이 값 마다 적용될 스타일을 지정한다
            width: '30px', height: '30px',
            background: 'rgba(51, 204, 255, .8)',
            borderRadius: '15px',
            color: '#000',
            textAlign: 'center',
            fontWeight: 'bold',
            lineHeight: '31px'
        },
            {
                width: '40px', height: '40px',
                background: 'rgba(255, 153, 0, .8)',
                borderRadius: '20px',
                color: '#000',
                textAlign: 'center',
                fontWeight: 'bold',
                lineHeight: '41px'
            },
            {
                width: '50px', height: '50px',
                background: 'rgba(255, 51, 204, .8)',
                borderRadius: '25px',
                color: '#000',
                textAlign: 'center',
                fontWeight: 'bold',
                lineHeight: '51px'
            },
            {
                width: '60px', height: '60px',
                background: 'rgba(255, 80, 80, .8)',
                borderRadius: '30px',
                color: '#000',
                textAlign: 'center',
                fontWeight: 'bold',
                lineHeight: '61px'
            }
        ]
    });

    $.ajax({
        url: "/seoul/getDongLatLon",
        type: "post",
        dataType: "JSON",
        data: {"indutyNm1": indutyNm1,
               "indutyNm2": indutyNm2,
               "indutyNm3": indutyNm3,
        },
        success: function (json) {

            for(let i = 0; json.length; i++) {

                let dto = json.at(i)

                let lat = dto.lat;
                let lon = dto.lon;

                console.log("return lat : " + lat);
                console.log("return lon : " + lon);

                let markers = new kakao.maps.Marker({
                    position: new kakao.maps.LatLng(lat, lon)
                });

                clusterer.addMarkers(markers);

            }

        },
        error: function (xhr, status, error) {
            console.error("AJAX request error:", status, error);
        }
    })

}

function getRankedLocation() {

    let tempList = [];

    for(let i = 1; i < 4; i++) {

        let locationNm = document.getElementById("rank" + i + "LocationName").innerText

        if (locationNm) {

            tempList.push(locationNm)
            console.log(`${i}위 ${locationNm}`)

        } else {
            console.error("Element not found for rank" + i + "LocationName");
        }

    }

    return tempList;

}

// 클러스터 내부에 삽입할 문자열 생성 함수입니다
function getTexts( count ) {

    // 한 클러스터 객체가 포함하는 마커의 개수에 따라 다른 텍스트 값을 표시합니다
    if(count < 10) {
        return '삐약';
    } else if(count < 30) {
        return '꼬꼬';
    } else if(count < 50) {
        return '꼬끼오';
    } else {
        return '치멘';
    }
}