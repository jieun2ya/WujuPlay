     // 합치기 요청한 모임들 불러오기
      function listRequests(id){
         var params = {hostId : id};
        $.ajax({
          url: "/merge/requests",
          type: "GET",
          dataType: "text",
          //contentType: "application/json;charset=UTF-8",
          data : params,
          success : function (data) {
                $('#mergeTable').replaceWith(data);
                $('input[name=hostMyMeetId]').attr('value', id);
                },
              error : function(XMLHttpRequest, textStatus, errorThrown){ // 비동기 통신이 실패할경우 error 콜백으로 들어옵니다.
                  alert("통신 실패.")
              }
          });
      }

      // 내 모임정보 불러오기
      function info(id){
         var params = {hostId : id};
        $.ajax({
          url: "/meet/info",
          type: "GET",
          //contentType: "application/json;charset=UTF-8",
          dataType: "text",
          data : params,
          success : function (data) {
                $("#myMeetInfo").replaceWith(data);
                },
              error : function(XMLHttpRequest, textStatus, errorThrown){ // 비동기 통신이 실패할경우 error 콜백으로 들어옵니다.
                  alert("통신 실패.")
              }
          });
      }

        // 합류가능한 모임 불러오기
      function getMergeables(id){
         var params = {hostId : id};
        $.ajax({
          url: "/merge/find",
          type: "GET",
          //contentType: "application/json;charset=UTF-8",
          dataType: "text",
          data : params,
          success : function (data) {
                console.log(data);
                $('#mergeableMeets').replaceWith(data);
                $('input[name=enrollingMyMeetId]').attr('value', id);
                },
              error : function(XMLHttpRequest, textStatus, errorThrown){ // 비동기 통신이 실패할경우 error 콜백으로 들어옵니다.
                  alert("통신 실패.")
              }
          });
      }