$(document).ready(function () {
    handleSelectChange()
})

function handleSelectChange() {
    const selectBox = document.getElementById("areaSelect");
    const selectedValue = selectBox.value;

    // 선택된 value 값을 공백 기준으로 split
    const [serialNo, lat, lon] = selectedValue.split(" ");

    console.log("Serial No:", serialNo);
    console.log("Latitude:", lat);
    console.log("Longitude:", lon);

    // 맵 이동
    panTo(lat, lon);

    // 유동 인구 그래프 분석(데이터 가져오기)
    $.ajax({
            url: "/seoul/walkAnalysis",
            type: "post", // 전송방식은 Post
            dataType: "JSON", // 전송 결과는 JSON으로 받기
            data: {"serialNo": serialNo}, // form 태그 내 input 등 객체를 자동으로 전송할 형태로 변경하기
            success:
                function (json) { // /seoul/walkAnalysis 호출이 성공했다면..
                    let array = json.timeVisitor;
                    console.log("시간 데이터 어레이 : " + array)

                    // 막대 그래프 그리기
                    makeBarChart(array)

                }
        }
    )

}


// 지도를 띄우는 코드
let container = document.getElementById('map_div'); //지도를 담을 영역의 DOM 레퍼런스
let options = { //지도를 생성할 때 필요한 기본 옵션
    center: new kakao.maps.LatLng(37.551914, 126.991851), //지도의 중심좌표.
    level: 3 //지도의 레벨(확대, 축소 정도)
};

let map = new kakao.maps.Map(container, options); //지도 생성 및 객체 리턴

function panTo(lat, lon) {
    // 이동할 위도 경도 위치를 생성합니다
    let moveLatLon = new kakao.maps.LatLng(lat, lon);

    map.setLevel(3);
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

let myChart;  // 전역 변수로 차트 객체 선언
function makeBarChart(array) {

    // 기존 차트가 있을 경우 삭제
    if (myChart) {
        myChart.destroy();
    }

    let timeArray = []
    for(let i = 0; i < 24; i++){
        if(i<10) {
            timeArray[i] = "0" + i + "시"
        } else {
            timeArray[i] = i + "시"
        }
    }

    myChart = new Chart(document.getElementById('myBarChart'), {
        type: 'bar',
        data: {
            labels: timeArray,
            datasets: [{
                label: "유동인구",
                data: array,
                backgroundColor: '#87CEEB',
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
                        min : 0,
                        stepSize: 25,
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