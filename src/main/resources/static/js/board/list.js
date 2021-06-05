
function write_action() {
    let categoryIdxElem = document.getElementById('categoryIdx');
    location.href='/board/write?category=' + categoryIdxElem.value;
}

function searchList(page) {
    const categoryForm = document.getElementById('categoryForm');
    let input = document.getElementById('page');
    if(page) {
        if(typeof page === "number") {
            page = parseInt(page);
        }
        input.value = page;
    } else {
        input.value = '0';
    }
    categoryForm.submit();
}

function openDetail(boardIdx) {
    location.href='/board/' + boardIdx + '?flag=detail';
}