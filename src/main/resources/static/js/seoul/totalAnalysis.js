$(document).ready(function () {



    let firstDto = guList.at(guList.length - 1);
    let secondDto = guList.at(guList.length - 2);

    // monthSales 값 추출
    let firstSales = getMonthSales(firstDto);
    let secondSales = getMonthSales(secondDto);

    // storeSales 값 축출
    let firstStore = getStoreCount(firstDto);
    let secondStore = getStoreCount(secondDto);

    // closeStoreSales 값 축출
    let firstClose = getCloseStoreCount(firstDto);
    let secondClose = getCloseStoreCount(secondDto);


    /!* 매출률, 점포률, 폐업률*!/
    // 매출
    let salesDiff = firstSales - secondSales;
    let salesRate = 0

    if(firstSales) {

       salesRate = (salesDiff / firstSales) * 100

    }

    // 점포
    let storeDiff = firstStore - secondStore;
    let storeRate = 0;
    if (firstStore) {

        storeRate = (storeDiff / firstStore) * 100

    }

    // 폐업
    let closeDiff = firstClose - secondClose;
    let closeRate = 0;
    if (firstClose) {

        closeRate = (closeDiff / firstClose) * 100

    }

    let salesDiv = document.getElementById("salesQuarter")
    let salesDiffDiv = document.getElementById("salesDiffQuarter")
    let salesRateDiv = document.getElementById("salesRateQuarter")

    let storeDiv = document.getElementById("storeQuarter")
    let storeDiffDiv = document.getElementById("storeDiffQuarter")
    let storeRateDiv = document.getElementById("storeRateQuarter")

    let closeDiv = document.getElementById("closeQuarter")
    let closeDiffDiv = document.getElementById("closeDiffQuarter")
    let closeRateDiv = document.getElementById("closeRateQuarter")

    // 매출액 데이터
    salesDiv.innerText = `${firstSales.toFixed(2)}만 (${salesDiff.toFixed(2)} 만)`
    salesRateDiv.innerText = `${salesRate.toFixed(2)}%`

    if (salesDiff > 0) {
        salesRateDiv.className = 'raise';
    } else if (salesDiff < 0) {
        salesRateDiv.className = 'decrease';
    }

    // 점포수 데이터
    storeDiv.innerText = `${firstStore}(개) (${storeDiff} 개)`
    storeRateDiv.innerText = `${storeRate.toFixed(2)}%`

    if (storeDiff > 0) {
        storeRateDiv.className = 'raise';
    } else if (storeDiff < 0) {
        storeRateDiv.className = 'decrease';
    }

    // 폐업수 데이터
    closeDiv.innerText = `${firstClose}(개) (${closeDiff} 개)`
    closeRateDiv.innerText = `${closeRate.toFixed(2)}%`

    if (closeDiff > 0) {
        closeRateDiv.className = 'decrease';
    } else if (closeDiff < 0) {
        closeRateDiv.className = 'raise';
    }


    /* 차트 만들기 */
    // 3개년 차트

    let pSeoulList = convertMarketList(seoulList);
    let pSeoulIndutyList = convertMarketList(seoulIndutyList);
    let pGuList = convertMarketList(guList);

    let storeGuList = convertStoreList(guList);
    let sotredList = convertSortedList(sortedList);

    let tempAgeList = guList.at(guList.length-1);

    let ageList = getAgeMarket(tempAgeList);

    console.log("tempAgeList : " + tempAgeList)
    console.log("ageList : " + ageList)

    makeRevenueChart(pSeoulList, pSeoulIndutyList, pGuList)
    makeStoreChart(storeGuList)
    makeCloseChart(storeGuList)
    makeDonutLocaion(sotredList)
    makeDonutAge(ageList)

    $("#btnCloseWindow").on("click", function () {
        window.close() // 창 닫기
    })

    $("#btnLocationAnalysis").on("click", function () {
        locationAnalysis(); // 창업 지역 추천
    })

})

function getMonthSales(dto) {
    // monthSales가 메서드일 경우 호출하고, 속성일 경우 그대로 반환합니다.
    return typeof dto.monthSales === 'function' ? dto.monthSales() : dto.monthSales;
}

function getStoreCount(dto) {
    // storeCount 메서드일 경우 호출하고, 속성일 경우 그대로 반환합니다.
    return typeof dto.storeCount === 'function' ? dto.storeCount() : dto.storeCount;
}

function getCloseStoreCount(dto) {
    // closeStoreCount 메서드일 경우 호출하고, 속성일 경우 그대로 반환합니다.
    return typeof dto.closeStoreCount === 'function' ? dto.closeStoreCount() : dto.closeStoreCount;
}

function getSeoulLocationNm(dto) {
    // seoulLocationNm 메서드일 경우 호출하고, 속성일 경우 그대로 반환합니다.
    return typeof dto.seoulLocationNm === 'function' ? dto.seoulLocationNm() : dto.seoulLocationNm;
}

function getSeoulLocationCd(dto) {
    // seoulLocationCd 메서드일 경우 호출하고, 속성일 경우 그대로 반환합니다.
    return typeof dto.seoulLocationCd === 'function' ? dto.seoulLocationCd() : dto.seoulLocationCd;
}

function getAgeMarket(dto) {
    // 나이대 별 매출액
    let age10Sales = typeof dto.age10Sales === 'function' ? dto.age10Sales() : dto.age10Sales;
    let age20Sales = typeof dto.age20Sales === 'function' ? dto.age20Sales() : dto.age20Sales;
    let age30Sales = typeof dto.age30Sales === 'function' ? dto.age30Sales() : dto.age30Sales;
    let age40Sales = typeof dto.age40Sales === 'function' ? dto.age40Sales() : dto.age40Sales;
    let age50Sales = typeof dto.age50Sales === 'function' ? dto.age50Sales() : dto.age50Sales;
    let age60Sales = typeof dto.age60Sales === 'function' ? dto.age60Sales() : dto.age60Sales;

    let rArray = [age10Sales.toFixed(2), age20Sales.toFixed(2), age30Sales.toFixed(2), age40Sales.toFixed(2), age50Sales.toFixed(2), age60Sales.toFixed(2)]

    return rArray;
}

function convertMarketList(array) {

    let rArray = [];
    let i;

    for(i = 0; i < array.length; i++)
    {

        let temp = array.at(i);
        let tempSales = parseFloat(getMonthSales(temp)).toFixed(2);
        rArray.push(tempSales);

    }

    return rArray;

}

function convertStoreList(array) {

    let rArray = [];
    let storeArray = [];
    let closeArray = [];
    let i;

    for(i = 0; i < array.length; i++)
    {

        let temp = array.at(i)
        let tempStore = getStoreCount(temp)
        let tempClose = getCloseStoreCount(temp)


        storeArray.push(tempStore)
        closeArray.push(tempClose)

    }

    rArray.push(storeArray);
    rArray.push(closeArray);

    return rArray;

}

function convertSortedList(array) {

    let rArray = [];
    let salesArray = [];
    let locationArray = [];
    let rank = 0;
    let i;

    let locationNm = getSeoulLocationNm(pDTO);

    for(i = 0; i < array.length; i++)
    {

        let temp = array.at(i)
        let tempSales = getMonthSales(temp).toFixed(2)
        let tempLocation = getSeoulLocationNm(temp)

        salesArray.push(tempSales)
        locationArray.push(tempLocation)

        if(tempLocation===locationNm){

            rank = i+1;

        }

    }

    rArray.push(salesArray)
    rArray.push(locationArray)
    rArray.push(rank)

    return rArray;

}

function makeRevenueChart(array1, array2, array3) {

    let max1 = Math.max(...array1); // array1의 최대값
    let max2 = Math.max(...array2); // array2의 최대값
    let max3 = Math.max(...array3); // array3의 최대값

    let maxOverall = Math.max(max1, max2, max3); // 세 개의 최대값 중에서 가장 큰 값

    let roundedNum = roundUpMaxNumber(maxOverall);


    Chart.defaults.global.defaultFontFamily = '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
    Chart.defaults.global.defaultFontColor = '#292b2c';
    Chart.defaults.global.defaultFontStyle = 'bold';
// -- Area Chart Example
    let ctx = document.getElementById("myAreaChart");
    let myLineChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: ["2022년 1분기", "2022년 2분기", "2022년 3분기", "2022년 4분기", "2023년 1분기", "2023년 2분기", "2023년 3분기"],
            datasets: [{
                label: "서울 전체 업종 전체 매출액",
                lineTension: 0.3,
                backgroundColor: "rgba(2,117,216,0.2)",
                borderColor: "rgba(2,117,216,1)",
                pointRadius: 5,
                pointBackgroundColor: "rgba(2,117,216,1)",
                pointBorderColor: "rgba(255,255,255,0.8)",
                pointHoverRadius: 5,
                pointHoverBackgroundColor: "rgba(2,117,216,1)",
                pointHitRadius: 20,
                pointBorderWidth: 2,
                data: array1,
            }, {
                label: "서울 전체 선택 업종 매출액",
                lineTension: 0.3,
                backgroundColor: "rgba(255,99,132,0.2)",
                borderColor: "rgba(255,99,132,1)",
                pointRadius: 5,
                pointBackgroundColor: "rgba(255,99,132,1)",
                pointBorderColor: "rgba(255,255,255,0.8)",
                pointHoverRadius: 5,
                pointHoverBackgroundColor: "rgba(255,99,132,1)",
                pointHitRadius: 20,
                pointBorderWidth: 2,
                data: array2,
            }, {
                label: "선택 지역(구)의 선택 업종 매출액",
                lineTension: 0.3,
                backgroundColor: "rgba(75,192,192,0.2)",
                borderColor: "rgba(75,192,192,1)",
                pointRadius: 5,
                pointBackgroundColor: "rgba(75,192,192,1)",
                pointBorderColor: "rgba(255,255,255,0.8)",
                pointHoverRadius: 5,
                pointHoverBackgroundColor: "rgba(75,192,192,1)",
                pointHitRadius: 20,
                pointBorderWidth: 2,
                data: array3,
            }],
        },
        options: {
            scales: {
                xAxes: [{
                    time: {
                        unit: 'date'
                    },
                    gridLines: {
                        display: false
                    },
                    ticks: {
                        maxTicksLimit: 7
                    }
                }],
                yAxes: [{
                    ticks: {
                        min: 0,
                        max: roundedNum,
                        maxTicksLimit: 5
                    },
                    gridLines: {
                        color: "rgba(0, 0, 0, .125)",
                    }
                }],
            },
            legend: {
                display: true
            }
        }
    });

}

function makeStoreChart(array) {

    let storeArray = array[0]

    console.log("storeArray : " + storeArray)

    let myChart = new Chart(document.getElementById('myBarStoreChart'), {
        type: 'bar',
        data: {
            labels: ["2022년 1분기", "2022년 2분기", "2022년 3분기", "2022년 4분기", "2023년 1분기", "2023년 2분기", "2023년 3분기"],
            datasets: [{
                label: "점포수",
                data: storeArray,
                backgroundColor: "rgba(2,117,216,1)",
                borderColor: 'transparent',
                borderWidth: 2.5,
                barPercentage: 0.4,
            }]
        },
        options: {
            scales: {
                yAxes: [{
                    gridLines: {},
                    ticks: {
                        stepSize: 15,
                    },
                }],
                xAxes: [{
                    gridLines: {
                        display: false,
                    }
                }]
            }
        }
    })


}

function makeCloseChart(array) {

    let closeArray = array[1]

    console.log("closeArray : " + closeArray)

    let myChart = new Chart(document.getElementById('myBarCloseChart'), {
        type: 'bar',
        data: {
            labels: ["2022년 1분기", "2022년 2분기", "2022년 3분기", "2022년 4분기", "2023년 1분기", "2023년 2분기", "2023년 3분기"],
            datasets: [{
                label: "폐업수",
                data: closeArray,
                backgroundColor: "rgba(75,192,192,1)",
                borderColor: 'transparent',
                borderWidth: 2.5,
                barPercentage: 0.4,
            }]
        },
        options: {
            scales: {
                yAxes: [{
                    gridLines: {},
                    ticks: {
                        stepSize: 15,
                    },
                }],
                xAxes: [{
                    gridLines: {
                        display: false,
                    }
                }]
            }
        }
    })


}

function makeDonutLocaion(array) {

    let salesArray = array[0];
    let locationArray = array[1];
    let number = locationArray.length;
    let locationNm = getSeoulLocationNm(pDTO);
    let rank = array[2];


    let pCircle = document.getElementById("circleMarket")

    pCircle.innerHTML = `매출액 기준 | <span style="color: rgba(75,192,192,1);">${locationNm}</span> | ${number}개 행정 구역 중  <span style="color: rgba(75,192,192,1);">${rank} 위</span>`

    // Create the backgroundColor array based on the rank
    let backgroundColor = salesArray.map((_, index) => {
        return index === rank - 1 ? 'rgba(75,192,192,1)' : 'rgba(2,117,216,0.2)';
    });

    // Create the borderColor array (all black borders)
    let borderColor = salesArray.map(() => '#3f3f45');

    let myChart = new Chart(document.getElementById('myCircleMarketChart'), {
        type: 'doughnut',
        data: {
            labels: locationArray,
            datasets: [{
                label: '지역별 매출액',
                data: salesArray,
                backgroundColor: backgroundColor,
                borderColor: borderColor,
                borderWidth: 2, // Set the border width
                hoverOffset: 4
            }]
        },
        options: {
            legend: {
                display: false
            }
        }
    });
}


function makeDonutAge(array) {

    let ageArray = ["10대", "20대", "30대", "40대", "50대", "60대 이상"]

    let sum = array.reduce((accumulator, currentValue) => Number(accumulator) + Number(currentValue), 0);
    console.log("sum : " + sum)
    // 최대값 구하기
    let max = Math.max(...array).toFixed(2); // spread operator를 사용하여 배열 전체를 전달
    let maxAsString = max.toString();

    console.log("array : " + array)

    let maxIndex = array.indexOf(maxAsString);

    console.log("최대값 : ", max);
    console.log("최대값의 인덱스 : ", maxIndex);

    let maxAge = (maxIndex+1)*10
    let ageRate = ((max/sum)*100).toFixed(2)

    let pCircle = document.getElementById("circleAge")

    pCircle.innerHTML = `주 고객층 | <span style="color: rgba(75,192,192,1);">${maxAge}대</span> | <span style="color: rgba(75,192,192,1);">${ageRate}%</span> `

    // Create the backgroundColor array based on the rank
    let backgroundColor = array.map((_, index) => {
        return index === maxIndex ? 'rgba(75,192,192,1)' : 'rgba(2,117,216,0.2)';
    });

    // Create the borderColor array (all black borders)
    let borderColor = array.map(() => '#3f3f45');

    let myChart = new Chart(document.getElementById('myCircleAgeChart'), {
        type: 'doughnut',
        data: {
            labels: ageArray,
            datasets: [{
                label: '지역별 매출액',
                data: array,
                backgroundColor: backgroundColor,
                borderColor: borderColor,
                borderWidth: 2, // Set the border width
                hoverOffset: 4
            }]
        },
        options: {
            legend: {
                display: false
            }
        }
    });
}

// 창업 지역 추천으로 이동
function locationAnalysis() {

    alert("작업 중 입니다.")

}

function roundUpMaxNumber(num) {
    let power = Math.pow(10, Math.floor(Math.log10(num))); // 주어진 숫자의 자릿수에 해당하는 10의 거듭제곱 계산
    let roundedNum = Math.ceil(num / power) * power; // 주어진 숫자를 올림하여 10의 거듭제곱으로 곱하여 최종 결과를 얻음
    return roundedNum;
}