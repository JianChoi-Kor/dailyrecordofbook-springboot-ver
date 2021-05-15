
window.onload = function() {
    if(!document.getElementById("sessionUser")) {
        alert('올바르지 않은 접근입니다.');
        history.back();
    } else {
        var roleElem = document.getElementById("role");
        if(roleElem.value !== 'TEMP') {
            alert('올바르지 않은 접근입니다.');
            history.back();
        }
    }
}




function add_action() {

    var join_formElem = document.querySelector('#join_form');

    var realNameElem = join_formElem.realName;
    var phoneElem = join_formElem.phone;
    var birthElem = join_formElem.birth;
    var genderElem = join_formElem.gender;
    var searchInfoElem = join_formElem.searchInfo;
    var readingVolumeElem = join_formElem.readingVolume;
    var bestElem = join_formElem.best;

    if (realNameElem.value === '') {
        alert ('이름을 입력해주세요.');
        realNameElem.focus();
        return false;
    } else if (!CheckName(realNameElem.value)) {
        realNameElem.focus();
        return false;
    } else if (birthElem.value === '') {
        alert ('생년월일을 입력해주세요.');
        birthElem.focus();
        return false;
    } else if (!CheckNumber(birthElem.value, 6)) {
        alert ('생년월일은 6자리 숫자로만 입력해주세요.');
        birthElem.focus();
        return false;
    } else if (genderElem.value === '') {
        alert ('성별을 체크해주세요.');
        genderElem.focus();
        return false;
    }


    if (phoneElem.value !== '') {
        if(!CheckNumber(phoneElem.value, 11)) {
            alert ('휴대폰번호는 11자리 숫자로만 입력해주세요.');
            phoneElem.focus();
            return false;
        }
    }

    var param = {
        realName: realNameElem.value,
        phone: phoneElem.value,
        birth: birthElem.value,
        gender: genderElem.value,
        searchInfo: searchInfoElem.value,
        readingVolume: readingVolumeElem.value,
        best: bestElem.value
    }

    fetch('/user/addInfo', {
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
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
                alert('추가 정보가 저장되었습니다.');
                history.back();
                break;
            case 1:
                alert('추가 정보 저장 실패');
                location.href = '/main';
                break;
        }
    }



}


// 이름 유효성 검사
function CheckName(str) {
    var chk_kor = /[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]/;
    if (!chk_kor.test(str)) {
        alert("이름을 정확하게 입력해주세요.");
        return false;
    }
    if (str.length < 2 || str.length > 5) {
        alert("이름을 정확하게 입력해주세요.");
        return false;
    }
    return true;
}

// 생년월일, 휴대폰번호 유효성 검사
function CheckNumber(str, length) {
    var chk_number=/^[0-9]*$/;
    if(!chk_number.test(str)) {
        return false;
    }
    if(parseInt(str.length) !== parseInt(length)) {
        return false;
    }
    return true;
}