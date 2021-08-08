

window.onload = function() {
    if(document.getElementById("sessionUser")) {
        alert('이미 로그인되어 있습니다.');
        location.href="/main";
    }
}

function login_action() {

    var login_formElem = document.querySelector('#login_form');

    var emailElem = login_formElem.userEmail;
    var passwordElem = login_formElem.userPw;
    var errMsgElem = document.querySelector('#errMsg');

    if (emailElem.value === '') {
        errMsgElem.innerHTML = '이메일을 입력해주세요.';
        emailElem.focus();
        return false;
    } else if (passwordElem.value === '') {
        errMsgElem.innerHTML = '비밀번호를 입력해주세요.';
        passwordElem.focus();
        return false;
    }

    if(emailElem.value !== '' && passwordElem.value !== '') {
        errMsgElem.innerHTML = '';
    }

    var param = {
        email : emailElem.value,
        password : passwordElem.value
    }

    fetch('/user/login', {
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(param)
    }).then(function(res) {
        return res.json();
    }).then(function(result) {
        proc(result);
    })

    function proc(result) {
        switch (result) {
            case 0:
                location.href = '/main';
                break;
            case 1:
                errMsgElem.innerHTML = '아이디, 비밀번호가 일치하지 않습니다.';
                break;
            case 2:
                errMsgElem.innerHTML = '이메일 인증이 진행되지 않은 회원입니다.';
                break;
            case 3:
                errMsgElem.innerHTML = '소셜 회원으로 가입된 계정입니다. 소셜 로그인을 이용해주세요.';
                break;
        }
    }

}