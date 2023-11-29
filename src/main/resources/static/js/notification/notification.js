
    var memberId = document.getElementById("memberId").value;

    // SSE 관련 - sseEmitter 생성
    const eventSource = new EventSource("/notifications/subscribe/" + memberId);


    eventSource.addEventListener('message', event => {
        console.log(event)
        incCount(event.data);
    });

    eventSource.addEventListener('mergeRequest', event => {
        console.log(event)
        document.getElementById("myMeetName").innerText = event.data;
        document.getElementById("content").style.display = "block";
    });

    eventSource.onopen = open => {
        console.log("connection established");
    }
    eventSource.onerror = error => {
      eventSource.close();
    };