
window.onload = function() {
    if(document.getElementById("sessionUser")) {
        alert('이미 로그인되어 있습니다.');
        history.back();
    }
}

// 약관 한번에 동의하는 function
function chkAll() {
	var chk1_allElem = document.querySelector('#chk1_all')
	
	if(chk1_allElem.checked == true) {
		document.querySelectorAll('input[type="checkbox"]')
		.forEach(checkbox => checkbox.checked = true)
	} else if(chk1_allElem.checked == false) {
		document.querySelectorAll('input[type="checkbox"]')
		.forEach(checkbox => checkbox.checked = false)
	}
}

//	약관 동의 확인 function
var terms_contentElem = document.querySelector('#terms_content')
terms_content.onsubmit = function(e) {
	e.preventDefault();
	
	var chk1_1Elem = document.querySelector('#chk1_1')
	var chk1_2Elem = document.querySelector('#chk1_2')
	
	if(chk1_1Elem.checked == false) {
		alert('이용 약관에 동의하지 않았습니다.')
		return false;
	}
	
	if(chk1_2Elem.checked == false) {
		alert('개인정보 수집 및 이용에 동의하지 않았습니다.')
		return false;
	}

	if(chk1_1Elem.checked == true && chk1_2Elem.checked == true) {
	    location.href = '/user/join'
	}
}







