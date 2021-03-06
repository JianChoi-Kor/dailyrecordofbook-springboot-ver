
// 글 고유 번호
let boardIdx = document.getElementById('boardIdx').value;
// 로그인 유저 고유 번호
let userIdx = ''
if(document.getElementById('userIdx')) {
    userIdx = document.getElementById('userIdx').value;
}

// 좋아요 클릭
function liked(cmtIdx, loginUserIdx) {
    if(isNaN(loginUserIdx)) {
        if(confirm('로그인이 필요한 서비스입니다. 로그인 하시겠습니까?')) {
            location.href = '/user/login';
            return;
        }
    } else {
        likeMotion(cmtIdx, loginUserIdx);
    }
}

// 좋아요 모션
function likeMotion(cmtIdx, loginUserIdx) {

    var iconElem = document.querySelector(`#heart-icon${cmtIdx}`);

    if(iconElem.classList.contains('far')) {
        likeAddAjax(cmtIdx, loginUserIdx);
    } else {
        likeDelAjax(cmtIdx, loginUserIdx);
    }

    if(iconElem.classList.contains('fas')) {
        iconElem.classList.remove('fas');
        iconElem.classList.add('far');
    } else {
        iconElem.classList.remove('far');
        iconElem.classList.add('fas');
    }
}


// 좋아요 DB 저장
function likeAddAjax(cmtIdx, loginUserIdx) {
    let params = {
        cmtIdx: cmtIdx,
        userIdx: loginUserIdx,
        boardIdx: boardIdx
    }

    fetch('/liked', {
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(params)
    }).then(function(res) {
        return res.json();
    }).then(function(result) {
        if(result === 0) {
            var likeSpan = document.querySelector(`#like${cmtIdx}`);
            if(likeSpan.innerText === "") {
                likeSpan.innerText = 0;
            }
            var plusLike = parseInt(likeSpan.innerText) + 1;
            likeSpan.innerText = plusLike;
        }
    })
}

// 좋아요 DB 삭제
function likeDelAjax(cmtIdx, loginUserIdx) {
    fetch(`/liked?boardIdx=${boardIdx}&cmtIdx=${cmtIdx}&userIdx=${loginUserIdx}`, {
        method: 'delete'
    }).then(res => res.json())
    .then(result => {
        if(result === 0) {
            var likeSpan = document.querySelector(`#like${cmtIdx}`);
            var minusLike = parseInt(likeSpan.innerText) - 1;
            if(minusLike <= 0) {
                minusLike = "";
            }
            likeSpan.innerText = minusLike;
        }
    })
}

function likeLoad() {
    fetch(`/liked?boardIdx=${boardIdx}`)
    .then(res => res.json())
    .then(result => {
        proc(result);
    })

    function proc(result) {
        if(result.length === 0) {
            return;
        }
        result.forEach(function(item) {
            if(item.userIdx == userIdx) {
                var cmtId = document.querySelector(`#heart-icon${item.cmtIdx}`);

                if(cmtId != null) {
                    cmtId.classList.remove('far');
                    cmtId.classList.add('fas');
                } else {
                    setTimeout(location.reload(), 500);
                }
            }
        })
    }
}

setTimeout(likeLoad(), 1700);

