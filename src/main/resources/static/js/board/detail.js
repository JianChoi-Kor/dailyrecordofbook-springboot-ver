
function modify_action() {
    if(confirm('수정하시겠습니까?')) {
        let boardIdx = document.getElementById('boardIdx');
        let categoryIdx = document.getElementById('categoryIdx');
        location.href= '/board/' + boardIdx.value + '?category=' + categoryIdx.value + '&flag=modify';
    }
}

function delete_action() {
    if(confirm('삭제하시겠습니까?')) {
        let boardIdx = document.getElementById('boardIdx');
        let sessionUserIdx = document.getElementById('sessionUserIdx');
        let categoryIdx = document.getElementById('categoryIdx');

        let params = {
            boardIdx : boardIdx.value,
            sessionUserIdx : sessionUserIdx.value
        }

        fetch('/board/delete', {
            method: 'post',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(params)
        }).then(function(res) {
            return res.json()
        }).then(function(result) {
            proc(result)
        })

        function proc(result) {
            switch (result) {
                case 0:
                    alert('글이 삭제 되었습니다.');
                    location.href = '/board/list?category=' + categoryIdx.value;
                    break;
                case 1:
                    alert('잘못된 접근입니다.');
                    break;
                case 2:
                    alert('글 삭제에 실패했습니다.');
                    break;
                case 3:
                    alert('잘못된 요청입니다.');
                    break;
            }
        }
    }
}

function close_action() {
    if(confirm('모집 마감으로 변경하시겠습니까?')) {
        let boardIdx = document.getElementById('boardIdx');
        let sessionUserIdx = document.getElementById('sessionUserIdx');
        let categoryIdx = document.getElementById('categoryIdx');

        let params = {
            boardIdx : boardIdx.value,
            sessionUserIdx : sessionUserIdx.value
        }

        fetch('/board/close', {
            method: 'post',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(params)
        }).then(function(res) {
            return res.json()
        }).then(function(result) {
            proc(result)
        })

        function proc(result) {
            switch (result) {
                case 0:
                    alert('모집 마감으로 변경되었습니다.');
                    location.href = '/board/list?category=12';
                    break;
                case 1:
                    alert('잘못된 접근입니다');
                    break;
                case 2:
                    alert('오류가 발생했습니다.');
                    break;
            }
        }
    }
}

function chkLogin() {
    if(!document.getElementById("sessionUser")) {
            if(confirm('로그인이 필요한 서비스입니다. 로그인 하시겠습니까?')) {
                location.href="/user/login";
            };
        }
}