function checkEmail() {
    var accountEmail = $("#accountEmail").val();
    var emailValidationMsg = $("#emailValidationMsg");


    // AJAX to check email duplication
    $.ajax({
        url: "/auth/check-email",
        type: "POST",
        data: accountEmail,
        dataType: "json",
        contentType: "application/json; charset=utf-8",

        success: function (data) {
            if (data.exist) {
             // Email is available
                emailValidationMsg.text("사용 가능한 이메일입니다.");
                emailValidationMsg.removeClass("error-message").addClass("success-message");
            } else {
            // Email is already in use
                emailValidationMsg.text("이미 사용 중인 이메일입니다.");
                emailValidationMsg.addClass("error-message");

            }
        },
        error: function (error) {
            // Handle AJAX error
            console.error("이메일 중복 확인 오류:", error);
            emailValidationMsg.text("서버 오류가 발생했습니다. 다시 시도해주세요.");
            emailValidationMsg.removeClass("success-message").addClass("error-message");
        }
    });
}

 function sample5_execDaumPostcode() {
      new daum.Postcode({
          oncomplete: function(data) {
              var addr = data.address;
              document.getElementById("sample5_address").value = addr;
          }
      }).open();
 }


function validateForm() {

    // 네임 검사
    var name = $("#name").val();
    if (!name) {
        alert("이름을 입력해주세요.");
        $("#name").focus();
        return false;
    } else if (!/^[가-힣]{1,10}$/.test(name)) {
        alert("이름은 한글로 10자 이내로 입력해주세요.");
        $("#name").focus();
        return false;
    }

    // 이메일 검사
    var email = $("#accountEmail").val();
    if (email && !/^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/.test(email)) {
        alert("올바른 이메일 형식을 입력해주세요.");
        $("#accountEmail").focus();
        return false;
    }

    var address = $("#sample5_address").val();
    if (!address) {
        alert("주소를 입력해주세요.");
        $("#sample5_address").focus();
        return false;
    }
    // 모든 유효성 검사를 통과하면 폼 제출
    return true;
}


$(document).ready(function () {
    $("form").on("submit", function (e) {
        var isFormValid = validateForm(); // 기존 유효성 검사

        // 두 검사 중 하나라도 실패하면 폼 제출을 막습니다.
        if (!isFormValid) {
            e.preventDefault();
        }
    });
});