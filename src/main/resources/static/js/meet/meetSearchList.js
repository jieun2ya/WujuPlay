
// 페이지 번호를 클릭하면 이 함수가 호출되고, 선택한 페이지로 이동
function goToSearchPage(page) {

        var searchType = document.getElementById("searchType").value;
        var keyword = document.getElementById("keyword").value;
        var searchPeriod = document.getElementById("searchPeriod").value;
        var startDate = document.getElementById("startDate").value;
        var endDate = document.getElementById("endDate").value;

        // AJAX를 이용하여 데이터를 서버로 전송
        $.ajax({
            type: "POST",
            url: "/meet/search",
            contentType: "application/json",
            data: JSON.stringify({
                searchType: searchType,
                keyword: keyword,sd
                searchPeriod: searchPeriod,
                startDate: startDate,
                endDate: endDate,
                page: page
            }),
            success: function (data) {
                // 성공적으로 데이터를 받았을 때의 처리
                // 여기에서는 받은 데이터를 사용하여 원하는 동작을 수행할 수 있습니다.
                //console.log(data);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                // 오류가 발생했을 때의 처리
                console.error("Ajax 오류 발생: " + textStatus, errorThrown);
            }
        });
    }