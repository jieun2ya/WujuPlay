const weatherButton = document.getElementById('weather-btn');
const weatherResultDiv = document.getElementById('weather-result');

weatherButton.addEventListener('click', () => {
    console.log("ddddd");
  navigator.geolocation.getCurrentPosition(success, error);
});

const success = (position) => {
  const { latitude, longitude } = position.coords;
  const apiKey = '923344e357b24073dcfc3e700a39725b';
  const apiUrl = `https://api.openweathermap.org/data/2.5/weather?lat=${latitude}&lon=${longitude}&appid=${apiKey}`;

  fetch(apiUrl)
    .then(response => response.json())
    .then(data => {
      const weatherResultDiv = document.getElementById('weather-result');
      if (weatherResultDiv) {
          const weatherDescription = data.weather[0].description;
          const temperature = data.main.temp;
          const iconCode = data.weather[0].icon;

          // 아이콘 이미지 URL 구성 (OpenWeatherMap에서 아이콘을 제공하는 URL에 아이콘 코드를 추가)
          const iconUrl = `http://openweathermap.org/img/w/${iconCode}.png`;

          // 결과를 표시할 HTML 생성
          const resultText = `현재 날씨: ${weatherDescription}, 온도: ${temperature}°C`;
          const resultHtml = `<img src="${iconUrl}" alt="날씨 아이콘" /> ${resultText}`;

          // 결과를 화면에 표시
          weatherResultDiv.innerHTML = resultHtml;
      } else {
          console.error('weather-result가 존재하지 않습니다.');
      }
     })
    .catch(error => {
      console.error('날씨 정보를 가져오는 중 에러 발생:', error);
    });
};

const error = (err) => {
  console.error('위치 정보를 가져오는 중 에러 발생:', err);
};