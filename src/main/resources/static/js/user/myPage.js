
let profileImgElem = document.getElementById('profileImg');

function profileUpload() {
    if(profileImgElem.files.length === 0) {
        alert('이미지를 선택해주세요.');
        return;
    }

    var formData = new FormData();
    formData.append('profileImg', profileImgElem.files[0]);

    fetch('/user/myPage', {
        method: 'post',
        body: formData
    }).then(res => res.json())
    .then(result => {
        if(result === 0) {
            alert('이미지가 업로드 되었습니다.');
            location.reload();
        } else {
            alert('이미지 업로드에 실패하였습니다.');
        }
    })
}
