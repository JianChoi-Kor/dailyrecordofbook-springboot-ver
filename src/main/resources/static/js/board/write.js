
window.onload = function() {
    if(!document.getElementById("sessionUser")) {
        alert('로그인이 필요한 서비스입니다.');
        location.href="/user/login";
    } else {
        var roleElem = document.getElementById("role");
        if(roleElem.value === 'TEMP') {
            if(confirm('추가정보 입력이 필요한 기능입니다. 추가정보를 입력하시겠습니까?')) {
                location.href='/user/addInfo';
            } else {
                location.href='/main';
            }
        }
    }
}
