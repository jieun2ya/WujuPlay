
// 모임눌렀을시 디테일 보여주는 함수
function meetDetail(meetId){
    // 디테일 눌러놓은거 있으면 다 숨기기
    hideMeetDetails();

    $.ajax({
        url : "/meet/meetDetailAjax/"+meetId,
        type : "GET",
        success : function(data) {

            // 데이터 사용 예시
            var meetName = data.meet.meetName;
            var meetMembers = data.meetMembers;
            var attendYN = data.attendYN;
            var loginMemberId = data.loginMemberId;

            let addHTML;
            // login 안했거나, 해당 모임 참여하지 않은 상태일때 보이는 상세
            if(loginMemberId == null || attendYN == null){
                addHTML = "<div>" + "</div>";
                addHTML += "<div>" + "</div>";
                addHTML += "<div>" + "</div>";
                addHTML += "<div>" + "</div>";
            }else{
            }

            // 페이지가 로드될 때 실행되는 코드
            hideMeetDetails();

        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.error("Ajax 오류 발생: " + textStatus, errorThrown);
        }
    });
}

// 장소 meetdetail 눌러놓은거 있을시 전부 숨기기
function hideMeetDetails() {
    // id가 "meetDetail_"로 시작하는 모든 요소를 선택
    var meetDetailElements = document.querySelectorAll('[id^="meetDetail_"]');

    // 선택된 모든 요소에 대해 style을 변경하여 숨김
    meetDetailElements.forEach(function (element) {
        element.style.display = 'none';
    });
}

// 모임검색
function searchMeetList() {
    var meetSearchForm = document.getElementById('meetSearchForm');
    // 폼 데이터를 서버로 전송
    meetSearchForm.submit();

}

// 날짜를 'yyyy-MM-dd' 형식의 문자열로 변환하는 함수
function formatDate(date) {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
}

// 폼 전송 시에 호출되는 함수
function searchMeetList() {
    // 날짜를 'yyyy-MM-dd' 형식으로 변환
    var startDate = formatDate(new Date(document.getElementById("startDate").value));
    var endDate = formatDate(new Date(document.getElementById("endDate").value));

}