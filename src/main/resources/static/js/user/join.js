

function join_action() {

    var join_formElem = document.querySelector('#join_form');

    var emailElem = join_formElem.email;
    var passwordElem = join_formElem.password;
    var passwordReElem = join_formElem.passwordRe;
    var realNameElem = join_formElem.realName;
    var phoneElem = join_formElem.phone;
    var birthElem = join_formElem.birth;
    var genderElem = join_formElem.gender;
    var searchInfoElem = join_formElem.searchInfo;
    var readingVolumeElem = document.querySelector('#readingVolume');
    var bestElem = join_formElem.best;
    var emailChkElem = document.getElementById('email_chk');

    email = emailElem.value;
    password = passwordElem.value;
    passwordRe = passwordReElem.value;
    realName = realNameElem.value;

    if (email === '') {
        alert ('이메일을 입력해주세요.');
        emailElem.focus();
        return false;
    } else if (!CheckEmail(email)) {
        alert ('잘못된 이메일 형식입니다.');
        emailElem.focus();
        return false;
    } else if (password === '') {
        alert ('비밀번호를 입력해주세요.');
        passwordElem.focus();
        return false;
    } else if (!CheckPassword(email, password)) {
        passwordElem.focus();
        return false;
    } else if (realName === '') {
        alert ('이름을 입력해주세요.');
        realNameElem.focus();
        return false;
    } else if (!CheckName(realName)) {
        realNameElem.focus();
        return false;
    } else if (phoneElem.value === '') {
        alert ('휴대폰번호를 입력해주세요.');
        phoneElem.focus();
        return false;
    } else if (birthElem.value === '') {
        alert ('생년월일을 입력해주세요.');
        birthElem.focus();
        return false;
    } else if (genderElem.value === '') {
        alert ('성별을 체크해주세요.');
        genderElem.focus();
        return false;
    }

    if (passwordElem.value !== passwordReElem.value) {
        passwordReElem.focus();
        alert ('비밀번호가 일치하지 않습니다.');
        return false;
    }

    console.log(':::::::::::' + emailChkElem.innerHTML + ':::::::::::::')

    if (emailChkElem.innerHTML !== '가입할 수 있는 이메일입니다.') {
        emailElem.focus();
        alert ('이메일 중복 체크를 진행해주세요.');
        return false;
    }


    join_formElem.submit();
}

// 이메일 유효성 검사
function CheckEmail(str) {
	var reg_email = /^([0-9a-zA-Z_\.-]+)@([0-9a-zA-Z_-]+)(\.[0-9a-zA-Z_-]+){1,2}$/;

	if (!reg_email.test(str)) {
		return false;
	}
	else {
		return true;
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
		alert("비밀번호는 숫자와 영무자를 혼용하여야 합니다.");
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

// 이름 유효성 검사
function CheckName(str) {
    var chk_kor = /[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]/;
    if (!chk_kor.test(str)) {
        alert("이름을 정확하게 입력해주세요111.");
        return false;
    }

    if (str.length < 2 || str.length > 5) {
        alert("이름을 정확하게 입력해주세요222.");
        return false;
    }
    return true;
}


function email_chk() {

    var join_formElem = document.querySelector('#join_form');
    var emailElem = join_formElem.email;
    var emailChkElem = document.getElementById('email_chk');

    var email = emailElem.value;

    if (email === '') {
        alert ('이메일을 입력해주세요.');
        emailElem.focus();
       return false;
    } else if (!CheckEmail(email)) {
        alert ('잘못된 이메일 형식입니다.');
        emailElem.focus();
        return false;
    }

    var param = {
        email: emailElem.value
    }

    fetch('/user/emailChk', {
        method: 'post',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(param)
    }).then(function(res) {
        return res.json()
    }).then(function(result) {
        proc(result);
    })

    function proc(result) {
        switch (result) {
            case 0:
                emailChkElem.innerHTML = '가입할 수 있는 이메일입니다.';
                break;
            case 1:
                emailChkElem.innerHTML = '이미 가입된 이메일입니다.';
                break;
        }
    }
}

function modefied() {
    var emailChkElem = document.getElementById('email_chk');
    emailChkElem.innerHTML = '';
}