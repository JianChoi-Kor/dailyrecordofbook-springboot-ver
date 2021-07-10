
window.onload = function() {
    if(document.getElementById("sessionUser")) {
        alert('이미 로그인되어 있습니다.');
        location.href="/main";
    }
}

function findEmail() {
    const findEmail_formElem = document.querySelector('#findEmail_form');

    let realNameElem = findEmail_formElem.realName;
    let birthElem = findEmail_formElem.birth;
    let phoneElem = findEmail_formElem.phone;
    let errMsgElem = document.querySelector('#errMsg');

    if (realNameElem.value === '') {
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

    var chk_number=/^[0-9]*$/;
    if(!chk_number.test(birthElem.value)) {
        alert('생년월일은 숫자만 입력해주세요.');
        birthElem.focus();
        return;
    }
    if(!chk_number.test(phoneElem.value)) {
        alert('휴대폰번호는 숫자만 입력해주세요.');
        phoneElem.focus();
        return;
    }

    let param = {
        realName : realNameElem.value,
        birth : birthElem.value,
        phone : phoneElem.value
    }

    fetch('/user/findEmail', {
        method: 'post',
        headers: {
            'Content-Type' : 'application/json'
        },
        body: JSON.stringify(param)
    }).then(function(res) {
        return res.json();
    }).then(function(result) {
        proc(result.result);
    })
    function proc(result) {
        if(result === 'notFound') {
            errMsgElem.innerHTML = '입력된 정보에 해당하는 회원을 찾을 수 없습니다.';
            return;
        } else {
            errMsgElem.innerHTML = '조회된 아이디는 " ' + result + ' " 입니다.';
        }

    }
}

function resetError() {
    let errMsgElem = document.querySelector('#errMsg');
    errMsgElem.innerHTML = '';
}

