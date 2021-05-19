
window.onload = function() {
    if(document.getElementById("sessionUser")) {
        alert('이미 로그인되어 있습니다.');
        location.href="/main";
    }
}

function findPassword() {
    const findPassword_formElem = document.querySelector('#findPassword_form');

    let emailElem = findPassword_formElem.email;
    let realNameElem = findPassword_formElem.realName;
    let birthElem = findPassword_formElem.birth;
    let phoneElem = findPassword_formElem.phone;
    let errMsgElem = document.querySelector('#errMsg');

    if(emailElem.value === '') {
        alert('이메일을 입력해주세요.');
        emailElem.focus();
        return;
    } else if (realNameElem.value === '') {
        alert('이름을 입력해주세요.');
        realNameElem.focus();
        return;
    } else if (birthElem.value === '') {
        alert('생년월일을 입력해주세요.');
        birthElem.focus();
        return;
    } else if (phoneElem.value === '') {
        alert('휴대폰번호를 입력해주세요.');
        phoneElem.focus();
        return;
    }

    let param = {
        email : emailElem.value,
        realName : realNameElem.value,
        birth : birthElem.value,
        phone : phoneElem.value
    }

    fetch('/user/findPassword', {
        method: 'post',
        headers: {
            'Content-Type' : 'application/json'
        },
        body: JSON.stringify(param)
    }).then(function(res) {
        return res.json();
    }).then(function(result) {
        proc(result)
    })

    function proc(result) {
        if(result === 0) {
            alert('입력하신 이메일로 임시 비밀번호가 발송되었습니다.');
            emailElem.value = '';
            realNameElem.value = '';
            birthElem.value = '';
            phoneElem.value = '';
            return;
        } else {
            errMsgElem.innerHTML = '입력하신 정보가 일치하지 않습니다.';
            return;
        }
    }
}