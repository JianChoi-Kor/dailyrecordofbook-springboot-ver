
function modify_action() {
    if(confirm('수정하시겠습니까?')) {
        let boardIdx = document.getElementById('boardIdx');
        location.href= '/board/' + boardIdx.value + '?flag=modify';
    }
}

function delete_action() {
    if(confirm('삭제하시겠습니까?')) {
        let boardIdx = document.getElementById('boardIdx');
        let sessionUserIdx = document.getElementById('sessionUserIdx');

        let params = {
            boardIdx : boardIdx.value,
            sessionUserIdx : sessionUserIdx.value
        }


    }
}