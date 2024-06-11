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
    let salesRateDiv = document.getElementById("salesRateQuarter")

    let storeDiv = document.getElementById("storeQuarter")
    let storeRateDiv = document.getElementById("storeRateQuarter")

    let closeDiv = document.getElementById("closeQuarter")
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




    $("#btnBack").on("click", function () {
        location.href = "/user/interestList";
    })

    $("#btnTotalAnalysis").on("click", function () {

        window.open("/seoul/totalAnalysis?guSelect=" + guValue + "&guName=" + guName + "&induty=" + induty, "_blank");

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
