function checkEmail() {
    var accountEmail = $("#accountEmail").val();
    var emailValidationMsg = $("#emailValidationMsg");


    // AJAX to check email duplication
    $.ajax({
        url: "check-email",
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


$("#password").on("input", checkPasswordMatch);  // #password 내용이 변경될 때마다 checkPasswordMatch() 함수를 호출

function validatePassword() {
    var password = $("#password").val();
    var regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&#])[A-Za-z\d@$!%*?&#]{8,}$/;

    if (!regex.test(password)) {
        $("#password").css("border", "1px solid red");
        $("#passwordMsg").removeClass('text-success').addClass('text-danger');

        $("#passwordMsg").text("비밀번호는 최소 8자리, 하나의 대문자, 하나의 소문자, 숫자, 특수 문자를 포함해야 합니다.");
        return false;
    } else {
        $("#password").css("border", "1px solid green");
        $("#passwordMsg").removeClass('text-danger').addClass('text-success');
        $("#passwordMsg").text("사용 가능합니다.");  // <--- 이 부분이 '사용 가능합니다' 메시지를 출력하는 부분입니다.
        return true;
    }
}

//비밀번호 일치 함수
function checkPasswordMatch() {
    var password = $("#password").val();
    var confirmPassword = $("#confirmed_password").val();

    if (confirmPassword === "") {  // 비밀번호 재확인란이 비어 있으면 아무런 메시지도 출력하지 않습니다.
        $("#confirmed_password").css("border", "");
        $("#confirmed_passwordMsg").text("");
        return;
    }

    if (password !== confirmPassword) {
        $("#confirmed_password").css("border", "1px solid red");
        $("#confirmed_passwordMsg").removeClass('text-success').addClass('text-danger');
        $("#confirmed_passwordMsg").text("비밀번호가 일치하지 않습니다.");
    } else {
        $("#confirmed_password").css("border", "1px solid green");
        $("#confirmed_passwordMsg").removeClass('text-danger').addClass('text-success');
        $("#confirmed_passwordMsg").text("비밀번호가 일치합니다.");
    }
}

function checkNickName() {
    var profileNickname = $("#profileNickname").val();
    var nickNameValidationMsg = $("#nickNameValidationMsg");


    // AJAX to check email duplication
    $.ajax({
        url: "check-nickName",
        type: "POST",
        data: profileNickname,
        dataType: "json",
        contentType: "application/json; charset=utf-8",

        success: function (data) {
            if (!data.exist) {
             // Email is available
                nickNameValidationMsg.text("사용 가능한 닉네임 입니다.");
                nickNameValidationMsg.removeClass("error-message").addClass("success-message");
            } else {
            // Email is already in use
                nickNameValidationMsg.text("이미 사용 중인 닉네임 입니다.");
                nickNameValidationMsg.addClass("error-message");

            }
        },
        error: function (error) {
            // Handle AJAX error
            console.error("닉네임 중복 확인 오류:", error);
            nickNameValidationMsg.text("서버 오류가 발생했습니다. 다시 시도해주세요.");
            nickNameValidationMsg.removeClass("success-message").addClass("error-message");
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

    // 비밀번호 검사
    if (!$("#password").val()) {
        alert("비밀번호를 입력해주세요.");
        $("#password").focus();
        return false;
    } else if (!validatePassword()) {
        alert("비밀번호 형식이 올바르지 않습니다.");
        $("#password").focus();
        return false;
    }

    // 비밀번호 재확인 검사
    if (!$("#confirmed_password").val()) {
        alert("비밀번호 재확인을 입력해주세요.");
        $("#confirmed_password").focus();
        return false;
    } else if ($("#password").val() !== $("#confirmed_password").val()) {
        alert("비밀번호가 일치하지 않습니다.");
        $("#confirmed_password").focus();
        return false;
    }

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

    // nickname 검사
    var nickname = $("#profileNickname").val();
    if (!nickname) {
        alert("닉네임을 입력해주세요.");
        $("#profileNickname").focus();
        return false;
    } else if (nickname.length > 10) {
        alert("닉네임은 10자 이내로 입력해주세요.");
        $("#profileNickname").focus();
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