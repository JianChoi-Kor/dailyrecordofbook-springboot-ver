

window.onload = function() {
    if(!document.getElementById("sessionUser")) {
        alert('올바르지 않은 접근입니다.');
        history.back();
    }
}

function withDraw() {
    const withDraw_formElem = document.querySelector('#withDraw_form');
    let passwordElem = withDraw_formElem.password;

    if(passwordElem.value == '') {
        alert('비밀번호를 입력하세요.');
        passwordElem.focus();
        return;
    }

    let param = {
        originPassword: passwordElem.value
    }

    fetch('/user/withDraw', {
        method: 'post',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(param)
    }).then(function(res) {
        return res.json();
    }).then(function(result) {
        proc(result);
    })

    function proc(result) {
        if(confirm('정말로 탈퇴하시겠습니까')) {

            switch(result) {
                case 0:
                alert('회원 탈퇴 되었습니다.');
                location.href='/logout';
                return;

                case 1:
                alert('비밀번호가 일치하지 않습니다.');
                return;
            }
        }
    }
}