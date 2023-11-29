
// 모임참여하기 버튼 눌렀을때 동작하는 함수
function meetMemberAdd(meetId){
    console.log("meetId" + meetId)
    if(meetId == 0){
        alert("로그인 후 이용할 수 있습니다");
        return false;
    }else{
        $.ajax({
            url : "/meet/checkMemberDetail",
            type : "GET",
            success : function(data) {
                console.log(data);

                if(data == "1"){
                    let link = "/meet/meetMemberAdd/"+meetId;
                    location.href=link;
                }else {
                    var userConfirmed = confirm("모임열기 및 참여를 위해서는 최초1회 정보등록이 필요합니다. 해당페이지로 이동하시겠습니까?");
                    if (userConfirmed) {
                       window.location.href = "/auth/signupDetail";
                    }

                }
            }
        });
    }
}

// 모임나가기 버튼 눌렀을때 동작하는 함수
function meetLeave(meetId){

    $.ajax({
        url : "/meet/meetMemberDelete/"+meetId,
        type : "GET",
        success : function(data) {
            console.log("모임나가기 함수바리");
            console.log(data);

            alert("모임나가기가 정상적으로 처리되었습니다.");
            location.reload();
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.error("Ajax 오류 발생: " + textStatus, errorThrown);
        }
    });

}

// 모임수정 페이지로 이동
function meetModify(meetId) {
    window.location.href = '/meet/meetUpdate/' + meetId;
}

