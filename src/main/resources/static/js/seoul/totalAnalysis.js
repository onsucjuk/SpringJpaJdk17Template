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
    salesDiv.innerText = `${firstSales.toFixed(2)}만`
    salesDiffDiv.innerText = `(${salesDiff.toFixed(2)} 만)`
    salesRateDiv.innerText = `${salesRate.toFixed(2)}%`

    if (salesDiff > 0) {
        salesRateDiv.className = 'raise';
    } else if (salesDiff < 0) {
        salesRateDiv.className = 'decrease';
    }

    // 점포수 데이터
    storeDiv.innerText = `${firstStore}(개)`
    storeDiffDiv.innerText = `(${storeDiff} 개)`
    storeRateDiv.innerText = `${storeRate.toFixed(2)}%`

    if (storeDiff > 0) {
        storeRateDiv.className = 'raise';
    } else if (storeDiff < 0) {
        storeRateDiv.className = 'decrease';
    }

    // 폐업수 데이터
    closeDiv.innerText = `${firstClose}(개)`
    closeDiffDiv.innerText = `(${closeDiff} 개)`
    closeRateDiv.innerText = `${closeRate.toFixed(2)}%`

    if (closeDiff > 0) {
        closeRateDiv.className = 'decrease';
    } else if (closeDiff < 0) {
        closeRateDiv.className = 'raise';
    }


    /* 차트 만들기 */
    // 3개년 차트

    let pSeoulList = convertList(seoulList);
    let pSeoulIndutyList = convertList(seoulIndutyList);
    let pGuList = convertList(guList);


    makeRevenueChart(pSeoulList, pSeoulIndutyList, pGuList)
})

function getMonthSales(dto) {
    // monthSales가 메서드일 경우 호출하고, 속성일 경우 그대로 반환합니다.
    return typeof dto.monthSales === 'function' ? dto.monthSales() : dto.monthSales;
}

function getStoreCount(dto) {
    // monthSales가 메서드일 경우 호출하고, 속성일 경우 그대로 반환합니다.
    return typeof dto.storeCount === 'function' ? dto.storeCount() : dto.storeCount;
}

function getCloseStoreCount(dto) {
    // monthSales가 메서드일 경우 호출하고, 속성일 경우 그대로 반환합니다.
    return typeof dto.closeStoreCount === 'function' ? dto.closeStoreCount() : dto.closeStoreCount;
}

function convertList(array) {

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

function roundUpMaxNumber(num) {
    let power = Math.pow(10, Math.floor(Math.log10(num))); // 주어진 숫자의 자릿수에 해당하는 10의 거듭제곱 계산
    let roundedNum = Math.ceil(num / power) * power; // 주어진 숫자를 올림하여 10의 거듭제곱으로 곱하여 최종 결과를 얻음
    return roundedNum;
}