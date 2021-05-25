
window.onload = function() {
    if(!document.getElementById("sessionUser")) {
        alert('로그인이 필요한 서비스입니다.');
        location.href="/user/login";
    }
}

