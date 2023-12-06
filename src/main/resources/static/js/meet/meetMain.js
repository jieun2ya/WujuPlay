

    document.addEventListener('DOMContentLoaded', function () {
        var meetCreationForm = document.getElementById('meetCreationForm');

        // 모임 등록 눌렀을시 발생함수
        meetCreationForm.addEventListener('submit', function (event) {
            if (!meetAddCheck()) {
                event.preventDefault(); // 폼 제출 막기
            }
        });
    });

    // 모임 등록 유효성 검사
    function meetAddCheck() {
        console.log("check! check!");

        // 필수 입력 항목들의 값을 가져오기
        var meetName = document.getElementById('meetName').value;
        var meetInfo = document.getElementById('meetInfo').value;
        var sportsSelect = document.getElementById('sportsSelect').value;
        var placeName = document.getElementById('placeName').value;
        var meetDate = document.getElementById('meetDate').value;
        var maxNumber = document.getElementById('maxNumber').value;

        // 각 필드가 비어 있는지 확인
        if (!meetName || !meetInfo || !sportsSelect || !placeName || !meetDate || !maxNumber) {
            alert('모든 필수 항목을 입력하세요.');
            return false;
        }

        // 모집 인원이 2명 이상인지 확인
        if (isNaN(parseInt(maxNumber)) || parseInt(maxNumber) < 2) {
            alert('모집인원은 숫자이고 2명 이상이어야 합니다.');
            return false;
        }


        return true; // 모든 유효성 검사가 통과되면 true 반환
    }

    var mapContainer = document.getElementById('map'), // 지도를 표시할 div
        mapOption = {
            level: 3 // 지도의 확대 레벨
        };

    // 페이지 로드했을때 날씨 정보 가져와서 뿌려주기
    document.addEventListener('DOMContentLoaded', function () {
        // 페이지 로드 시 자동으로 날씨 정보 가져오기
        fetchWeatherInfo();

    });




     // 날씨
    function fetchWeatherInfo() {
        navigator.geolocation.getCurrentPosition(
            function (position) {
                var lat = position.coords.latitude;
                var lon = position.coords.longitude;
                console.log("lat3",lat);
                console.log("lon3",lon);


    mapOption.center = new kakao.maps.LatLng(lat, lon);

                var apiKey = '923344e357b24073dcfc3e700a39725b';
                var apiUrl = `https://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${lon}&appid=${apiKey}`;

                fetch(apiUrl)
                    .then(response => response.json())
                    .then(data => {
                        const weatherDescription = data.weather[0].description;
                        const temperature = data.main.temp;
                        const weatherResultDiv = document.getElementById('weather-result');

                        // 아이콘 정보 가져오기
                        const iconCode = data.weather[0].icon;
                        const iconUrl = `http://openweathermap.org/img/w/${iconCode}.png`;

                        // 온도를 섭씨로 변환
                        const temperatureCelsius = temperature - 273.15;

                        // 아이콘을 표시하는 HTML 코드
                        let iconHtml = `<img src="${iconUrl}" alt="${weatherDescription}">`;
                        iconHtml += `현재 날씨: ${weatherDescription}, 온도: ${temperatureCelsius.toFixed(2)}°C`;

                        // 결과 텍스트와 아이콘을 표시
                        weatherResultDiv.innerHTML = iconHtml;

                    })
                    .catch(error => {
                        console.error('날씨 정보를 가져오는 중 에러 발생:', error);
                    });
            },
            function (error) {
                console.error('위치 정보를 가져오는 중 에러 발생:', error);
            }
        );
    }