$(document).ready(function () {
    // 버튼 클릭했을때, 발생되는 이벤트 생성함(onclick 이벤트와 동일함)
    $("#btnTotalAnalysis").on("click", function () {

        // 현재 페이지 url 가져오기
        const currentUrl = window.location.href;
        const urlObj = new URL(currentUrl);
        // url 파라미터 가져오기
        const params = new URLSearchParams(urlObj.search);

        // 지역, 지역코드, 업종명 가져오기
        const guValue = params.get('guSelect');
        const induty = params.get('indutyNm');
        const guName = params.get('guName');


        location.href = "/seoul/totalAnalysis?guSelect=" + guValue + "&guName=" + guName + "&induty=" + induty

    })

    $("#btnCloseWindow").on("click", function () {
        window.close() // 창 닫기
    })

})


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
    const maxDiv = document.getElementById('maxContents');
    // 초기화
    rankDiv.innerHTML = '';
    maxDiv.innerHTML = '';

    // 클릭된 값에 따라 다른 테이블 헤더와 데이터 로드
    if (nowClickedText === "매출액") {

        for(let i = 0; i < salesList.length; i++) {

            let tempDTO = salesList.at(i)

            let locationNm = getLocationNm(tempDTO)
            let locationCd = getLocationCd(tempDTO)
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
            <p class="locationCd" id="rank${i + 1}LocationCd">${locationCd}</p>
            <p id="rank${i + 1}Content">${monthSales} 만</p>
        `;

            rankDiv.appendChild(row);
        }

    } else if (nowClickedText === "매출률") {

        for(let i = 0; i < salesRateList.length; i++) {

            let tempDTO = salesRateList.at(i)

            let locationNm = getLocationNm(tempDTO)
            let locationCd = getLocationCd(tempDTO)
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
            <p class="locationCd" id="rank${i + 1}LocationCd">${locationCd}</p>
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
            let locationCd = getLocationCd(tempDTO)
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
            <p class="locationCd" id="rank${i + 1}LocationCd">${locationCd}</p>
            <p id="rank${i + 1}Content">${storeCount} 개</p>
        `;

            rankDiv.appendChild(row);
        }

    } else if (nowClickedText === "나이대") {

        for(let i = 0; i < ageList.length; i++) {

            let tempDTO = ageList.at(i)

            let locationNm = getLocationNm(tempDTO)
            let locationCd = getLocationCd(tempDTO)
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
            <p class="locationCd" id="rank${i + 1}LocationCd">${locationCd}</p>
            <p id="rank${i + 1}Content">${ageSales} 만</p>
        `;

            rankDiv.appendChild(row);
        }

        let hTag = document.createElement("h4");
        hTag.className = "indutyAndLocation";
        hTag.innerText = `| 주고객층 : ${maxAge}대 |`;

        maxDiv.appendChild(hTag)

    } else if (nowClickedText === "성별") {

        for(let i = 0; i < genderList.length; i++) {

            let tempDTO = genderList.at(i)

            let locationNm = getLocationNm(tempDTO)
            let locationCd = getLocationCd(tempDTO)
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
            <p class="locationCd" id="rank${i + 1}LocationCd">${locationCd}</p>
            <p id="rank${i + 1}Content">${genderSales} 만</p>
        `;

            rankDiv.appendChild(row);

        }

        let gender = "";
        if(maxGender==="male") {
            gender = "남성"
        } else {
            gender = "여성"
        }

        let hTag = document.createElement("h4");
        hTag.className = "indutyAndLocation";
        hTag.innerText = `| 주고객 성별 : ${gender} |`;

        maxDiv.appendChild(hTag)

    } else if (nowClickedText === "시간대") {

        for(let i = 0; i < timeList.length; i++) {

            let tempDTO = timeList.at(i)

            let locationNm = getLocationNm(tempDTO)
            let locationCd = getLocationCd(tempDTO)
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
            <p class="locationCd" id="rank${i + 1}LocationCd">${locationCd}</p>
            <p id="rank${i + 1}Content">${timeSales} 만</p>
        `;

            rankDiv.appendChild(row);

        }

        let time = "";
        if(maxTime==="0006") {
            time = "00시~06시"
        } else if(maxTime==="0611") {
            time = "06시~11시"
        } else if(maxTime==="1114") {
            time = "11시~14시"
        } else if(maxTime==="1417") {
            time = "14시~17시"
        } else if(maxTime==="1721") {
            time = "17시~21시"
        } else if(maxTime==="2124") {
            time = "21시~24시"
        }

        let hTag = document.createElement("h4");
        hTag.className = "maxContents";
        hTag.innerText = `| 피크 타임 : ${time} |`;

        maxDiv.appendChild(hTag)

    } else {

        alert("분석할 수 없는 탭입니다. 분석 분류를 제대로 선택해주세요.")
        return;

    }

    let rankedList = getRankedLocation();

    setMapData(rankedList)
}

function getMonthSales(dto) {
    // monthSales가 메서드일 경우 호출하고, 속성일 경우 그대로 반환합니다.
    return typeof dto.monthSales === 'function' ? dto.monthSales() : dto.monthSales;
}

function getLat(dto) {

    // lat가 메서드일 경우 호출하고, 속성일 경우 그대로 반환합니다.
    return typeof dto.lat === 'function' ? dto.lat() : dto.lat;
}


function getLon(dto) {

    // lon가 메서드일 경우 호출하고, 속성일 경우 그대로 반환합니다.
    return typeof dto.lon === 'function' ? dto.lon() : dto.lon;
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

function getLocationCd(dto) {
    // monthSales가 메서드일 경우 호출하고, 속성일 경우 그대로 반환합니다.
    return  typeof dto.seoulLocationCd === 'function' ? dto.seoulLocationCd() : dto.seoulLocationCd;
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

let map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다
let circleList = [];
let points = [];


function setMapData(rankedList) {

    // 지도를 재설정할 범위정보를 가지고 있을 LatLngBounds 객체를 생성합니다
    let bounds = new kakao.maps.LatLngBounds();

    // 원 초기화
    if(circleList) {
        for(let i = 0; i < circleList.length; i++) {

            let circle = circleList.at(i)

            circle.setMap(null)

        }
    }

    // points 초기화
    points = []

    let location1 = rankedList.at(0)
    let location2 = rankedList.at(1)
    let location3 = rankedList.at(2)

    console.log("지역 위도 경도 찾기 !! : " + location1 + ", " + location2 + ", " + location3)

    $.ajax({
        url: "/seoul/getDongLatLon",
        type: "post",
        dataType: "JSON",
        data: {"location1": location1,
               "location2": location2,
               "location3": location3,
        },
        success: function (json) {

            for(let i = 0; i < json.length; i++) {

                let dto = json.at(i)

                let lat = getLat(dto);
                let lon = getLon(dto);

                console.log("return lat : " + lat);
                console.log("return lon : " + lon);

                if (i===2) {

                    console.log(i+1 + "번째 그림그리기!!")
                    let number = 2000;
                    let color = '#CD7F32';
                    let back = '#E8C39E';
                    let zIndex = 2;

                    drawCircle(number, color, back, lat, lon, zIndex)

                    //points에 위도, 경도 저장
                    addPoint(lat, lon);

                    // 바운스에 저장
                    bounds.extend(points[i])

                }

                else if (i===1) {

                    console.log(i+1 + "번째 그림그리기!!")
                    let number = 2250;
                    let color = '#C0C0C0';
                    let back = '#D3D3D3';
                    let zIndex = 3;

                    drawCircle(number, color, back, lat, lon, zIndex)

                    //points에 위도, 경도 저장
                    addPoint(lat, lon);

                    // 바운스에 저장
                    bounds.extend(points[i])

                }

                else if(i===0) {

                    console.log(i+1 + "번째 그림그리기!!")

                    let number = 2500;
                    let color = '#FFD700';
                    let back = '#FFFACD';
                    let zIndex = 4;

                    drawCircle(number, color, back, lat, lon, zIndex)

                    //points에 위도, 경도 저장
                    addPoint(lat, lon);

                    // 바운스에 저장
                    bounds.extend(points[i])

                }

                else {

                    alert("지역의 위도, 경도 정보가 없습니다.")
                    return;

                }

            }

            setBounds(bounds)

        },
        error: function (xhr, status, error) {
            console.error("AJAX request error:", status, error);
        }
    })
}

function drawCircle(number, color, back, lat, lon, zIndex) {


        let circle = new kakao.maps.Circle({
            center: new kakao.maps.LatLng(lat, lon),  // 원의 중심좌표 입니다
            radius: number, // 미터 단위의 원의 반지름입니다
            strokeWeight: 5, // 선의 두께입니다
            strokeColor: color, // 선의 색깔입니다
            strokeOpacity: 1, // 선의 불투명도 입니다 1에서 0 사이의 값이며 0에 가까울수록 투명합니다
            strokeStyle: 'dashed', // 선의 스타일 입니다
            fillColor: back, // 채우기 색깔입니다
            fillOpacity: 0.7,  // 채우기 불투명도 입니다
            zIndex: zIndex
        });

        // 원 리스트에 저장
        circleList.push(circle)

        // 지도에 원을 표시합니다
        circle.setMap(map);
}

function getRankedLocation() {

    let tempList = [];

    for(let i = 1; i < 4; i++) {

        let locationCd = document.getElementById("rank" + i + "LocationCd").innerText

        if (locationCd) {
            tempList.push(locationCd)
            console.log(`${i}위 ${locationCd}`)
        } else {
            console.error("Element not found for rank" + i + "$LocationCd");
        }
    }
    return tempList;
}

function addPoint(lat, lon) {
    let point = new kakao.maps.LatLng(lat, lon);
    points.push(point);
}

function setBounds(bounds) {
    // LatLngBounds 객체에 추가된 좌표들을 기준으로 지도의 범위를 재설정합니다
    // 이때 지도의 중심좌표와 레벨이 변경될 수 있습니다
    map.setBounds(bounds, 150, 150, 150, 150);

    let level = map.getLevel();

    console.log("map level : " + level)

    if(level<6) {

        map.setLevel(6)
        console.log("바뀐 지도 레벨 : " + level)

    }
}