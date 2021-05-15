
window.onload = function() {
    if(!document.getElementById("sessionUser")) {
        alert('올바르지 않은 접근입니다.');
        history.back();
    }
}

function changePw() {
    const changePassword_formElem = document.querySelector('#changePassword_form');
    let emailElem = changePassword_formElem.email;
    let originPasswordElem = changePassword_formElem.originPassword;
    let newPasswordElem = changePassword_formElem.newPassword;
    let newPasswordReElem = changePassword_formElem.newPasswordRe;

    if(originPasswordElem.value === '') {
        alert('기존 비밀번호를 입력해주세요.');
        originPasswordElem.focus();
        return;
    } else if(newPasswordElem.value === '') {
        alert('새로운 비밀번호를 입력해주세요.');
        newPasswordElem.focus();
        return;
    } else if(!CheckPassword(emailElem.value, newPasswordElem.value)) {
        newPasswordElem.focus();
        return;
    }

    if(newPasswordElem.value !== newPasswordReElem.value) {
        alert('새로운 비밀번호가 일치하지 않습니다.');
        newPasswordElem.focus();
        return;
    }

    if(originPasswordElem.value === newPasswordElem.value) {
        alert('기존의 비밀번호와 다른 비밀번호를 입력해주세요.');
        return;
    }

    let param = {
        originPassword: originPasswordElem.value,
        newPassword: newPasswordElem.value,
        newPasswordRe: newPasswordReElem.value
    }

    fetch('/user/changePassword', {
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
        switch(result) {
            case 0:
            alert('비밀번호가 변경되었습니다. 다시 로그인해주세요.');
            location.href='/logout';
            return;

            case 1:
            alert('비밀번호가 틀립니다.');
            return;

            case 2:
            alert('새로운 비밀번호가 일치하지 않습니다.');
            return;

            case 3:
            alert('기존의 비밀번호와 다른 비밀번호를 입력해주세요.');
            return;

            case 4:
            alert('올바르지 않은 접근입니다.');
            return;
        }
    }

}





// 비밀번호 유효성 검사
function CheckPassword(uem, upw){
	// 이메일 @ 앞부분 자르기
	var uid = uem.substring(0, uem.indexOf('@'))
	if (!/^[a-zA-Z0-9!@#$%^*+=-]{8,20}$/.test(upw)) {
		alert("비밀번호는 숫자와 영문자 조합으로 8~20자리를 사용해야 합니다.");
		return false;
	}
	var chk_num = upw.search(/[0-9]/g);
	var chk_eng = upw.search(/[a-z]/ig);
	if (chk_num < 0 || chk_eng < 0) {
		alert("비밀번호는 숫자와 영문자를 혼용하여야 합니다.");
		return false;
	}
	if (/(\w)\1\1\1/.test(upw)) {
		alert("비밀번호에 같은 문자를 4번 이상 사용하실 수 없습니다.");
		return false;
	}
	if (upw.search(uid) > -1) {
		alert("ID가 포함된 비밀번호는 사용하실 수 없습니다.");
		return false;
	}
	return true;
}