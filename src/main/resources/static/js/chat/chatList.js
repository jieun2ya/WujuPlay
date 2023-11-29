
  $(document).ready(getUnreadMap);

    function getUnreadMap(){
    $.ajax({
     url: "/chat/unreadmap",
     type: "GET",
     //contentType: "application/json;charset=UTF-8",
     dataType: "text",
     success : function (data) {
            $('#chatMap').replaceWith(data);
           },
         error : function(XMLHttpRequest, textStatus, errorThrown){ // 비동기 통신이 실패할경우 error 콜백으로 들어옵니다.
             alert("통신 실패.")
         }
     });
    }

