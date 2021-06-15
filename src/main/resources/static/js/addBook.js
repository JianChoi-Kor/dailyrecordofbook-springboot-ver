
window.onload = function() {
    if(!document.getElementById("sessionUser")) {
        alert('로그인이 필요한 서비스입니다.');
        location.href="/user/login";
    }
}

function fileBrowse(name) {
    let input = $("input[name=" + name + "]")[0];
    var inputFile = document.getElementById(name);

    if(document.getElementById('prevFile')) {
        document.getElementById('prevFile').remove();
    }

    if(input.value !== null) {
        var prevFile = document.createElement('input');
        prevFile.type = 'file';
        prevFile.id = 'prevFile';
        prevFile.files = input.files;
        inputFile.appendChild(prevFile);
    }
    input.click();
}

function previewImage(event, targetObj) {
    var files = targetObj.files;
    var file = files[0];

    var inputImage = targetObj.value;

    var savedImage = document.getElementById('prevFile');

    if(files.length === 0) {
        targetObj.files = savedImage.files;
        return;
    }

    if(file.size > 10000000) {
        alert('최대 10MB까지 업로드 가능합니다.');
        targetObj.files = savedImage.files;
        return;
    }

    if(!(file.type.match(/image.jpg/) || file.type.match(/image.png/) || file.type.match(/image.jpeg/))) {
        alert('허용된 확장자가 아닙니다.');
        targetObj.files = savedImage.files;
        return;
    }

    if(!document.getElementById('loadImage')) {
        let loadImage = document.createElement('img');
        loadImage.id = 'loadImage';

        let imageDiv = document.getElementById('imageDiv');
        imageDiv.appendChild(loadImage);

        loadImage.src = URL.createObjectURL(event.target.files[0]);
        loadImage.onload = function() {
            URL.revokeObjectURL(loadImage.src);
        }
    } else {
        let loadImage = document.getElementById('loadImage');
        loadImage.src = URL.createObjectURL(event.target.files[0]);
        loadImage.onload = function() {
            URL.revokeObjectURL(loadImage.src);
        }
    }
}
