$(document).ready(function () {

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

    let firstDto = seoulList.at(seoulList.length - 1);
    let secondDto = seoulList.at(seoulList.length - 2);

    // monthSales 값 추출
    let firstSales = getMonthSales(firstDto);
    let secondSales = getMonthSales(secondDto);

    // storeSales 값 축출
    let firstStore = getStoreCount(firstDto);
    let secondStore = getStoreCount(secondDto);

    // closeStoreSales 값 축출
    let firstClose = getCloseStoreCount(firstDto);
    let secondClose = getCloseStoreCount(secondDto);


    /* 매출률, 점포률, 폐업률*/
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


    let salesDiv = document.getElementById("sales")
    let storeDiv = document.getElementById("store")
    let closeDiv = document.getElementById("close")

    salesDiv.innerText = `${firstSales.toFixed(2)}(${salesDiff.toFixed(2)}) ${salesRate.toFixed(2)}`
    storeDiv.innerText = `${firstStore.toFixed(2)}(${storeDiff.toFixed(2)}) ${storeRate.toFixed(2)}`
    closeDiv.innerText = `${firstClose.toFixed(2)}(${closeDiff.toFixed(2)}) ${closeRate.toFixed(2)}`

})