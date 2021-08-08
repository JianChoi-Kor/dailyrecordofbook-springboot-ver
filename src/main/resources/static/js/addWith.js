
function addWith() {

    var with_formElem = document.getElementById('with_form');

    var memberElem = with_formElem.member;
    var bookElem = with_formElem.book;
    var topicElem = with_formElem.topic;

    if(memberElem.value === "" || bookElem.value === "" || topicElem.value === "") {
        alert('값을 입력해주세요.');
        return;
    }

    var param = {
        member: memberElem.value,
        book: bookElem.value,
        topic: topicElem.value
    }

    fetch('/addWith', {
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(param)
    }).then(function(res) {
        return res.json();
    }).then(function(result) {
        if(result === 0) {
            if(confirm('등록에 성공했습니다. 메인화면으로 이동하시겠습니까?')) {
                location.href = '/main';
            }
        } else {
            alert('등록에 실패했습니다.');
        }
    })
}