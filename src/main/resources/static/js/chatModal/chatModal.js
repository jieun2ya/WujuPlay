$(document).ready(getUnreadCounts);


$('#myModal').draggable({
  handle: ".modal-header"
});

$(document).on('show.bs.modal','#myModal', function () {
  $(".modal iframe").attr('src', "http://www.wujuplay.site:8090/chat/mychat");
})

$(document).on('hide.bs.modal','#myModal', function ()  {
  $(".modal iframe").attr('src'," ");
})

// 안 읽은 메시지 수 받아오기
function getUnreadCounts(){
$.ajax({
  url: "/chat/unread",
  type: "GET",
  contentType: "application/json;charset=UTF-8",
  success : function (data) {
        const countElem = document.getElementById('unreadCount');
        countElem.innerText = data;
        },
      error : function(XMLHttpRequest, textStatus, errorThrown){ // 비동기 통신이 실패할경우 error 콜백으로 들어옵니다.
          alert("통신 실패.")
      }
  });
}

// 만약 지금 들어와있는 채팅방이 아니면 알림수 +1
function incCount(chatRoomId){
    console.log("inccount:" + chatRoomId)
    const countElem = document.getElementById('unreadCount');
    let count = countElem.innerText;
    count = parseInt(count) + 1;
    var childChatRoomDiv = document.getElementById("myFrame").contentWindow.document.getElementById("chatId");
    if (childChatRoomDiv){
        const roomId = childChatRoomDiv.value;
            console.log("roomId:" + chatRoomId)
        if (chatRoomId != roomId){
            console.log("not equal");
            countElem.innerText = count;
        }
    }
    else{
        countElem.innerText = count;
    }
}


function checkCurrPage(){
    var chatRoomId = document.getElementById("myFrame").contentWindow.document.getElementById("chatId").value;
    if (chatRoomId){
        leaveChat(chatRoomId);
    }
}

function leaveChat(chatRoomId){
   var params = {chatRoomId : chatRoomId};
  $.ajax({
    url: "/message/leaveChatRoom",
    type: "GET",
    contentType: "application/json;charset=UTF-8",
    data : params,
    success : function (data) {},
    error : function(XMLHttpRequest, textStatus, errorThrown){ // 비동기 통신이 실패할경우 error 콜백으로 들어옵니다.
            alert("통신 실패.")
        }
    });
}
