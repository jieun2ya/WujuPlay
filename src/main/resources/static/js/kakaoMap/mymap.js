// 마커를 담을 배열입니다
var markers = [];
var datas = [];

var mapContainer = document.getElementById('map'), // 지도를 표시할 div
    mapOption = {
        level: 6 // 지도의 확대 레벨
    };

// 마커를 추가하는 함수
function addMarker(y, x) {
    // 마커가 표시될 위치입니다
    var markerPosition  = new kakao.maps.LatLng(y, x);

    // 마커를 생성합니다
    var marker = new kakao.maps.Marker({
        position: markerPosition
    });

    // 마커가 지도 위에 표시되도록 설정합니다
    marker.setMap(map);
}

// 고정된 기본 좌표 : 왕십리역
var lat = 37.561268363317176; // 위도
var lon = 127.03710337610202; // 경도

// 지도의 중심좌표 설정
mapOption.center = new kakao.maps.LatLng(lat, lon);

// 지도를 생성합니다
map = new kakao.maps.Map(mapContainer, mapOption);

$(document).ready(function() {
    // Add markers for each meet
    $("span[id^='meet-']").each(function() {
        var y = $(this).data('y');
        var x = $(this).data('x');
        console.log("x : "+x);
        console.log("y : "+y);

        addMarker(y, x);
    });
});

