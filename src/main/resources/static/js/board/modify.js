


window.onload = function() {

    const write_formElem = document.getElementById('write_form');
    let writerIdx = write_formElem.writerIdx;

    if(!document.getElementById("sessionUser")) {
        alert('잘못된 접근입니다.');
        location.href="/user/login";
    } else {
        let sessionUserIdx = write_formElem.sessionUserIdx;
        if(writerIdx.value != sessionUserIdx.value) {
            alert('잘못된 접근입니다.');
            location.href="/main";
        }
    }
}

