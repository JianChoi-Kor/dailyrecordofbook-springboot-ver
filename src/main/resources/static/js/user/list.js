

window.onload = function() {
    if(!document.getElementById("sessionUser")) {
        alert('올바르지 않은 접근입니다.');
        history.back();
    } else {
        var roleElem = document.getElementById("role");
        if(roleElem.value !== 'ADMIN') {
            alert('올바르지 않은 접근입니다.');
            history.back();
        }
    }
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