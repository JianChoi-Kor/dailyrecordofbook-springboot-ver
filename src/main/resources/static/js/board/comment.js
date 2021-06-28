


// 댓글 쓰기
function writeComment() {

    const cmtFormElem = document.querySelector('#cmtForm');
    let cmtContentElem = cmtFormElem.cmtContent;

    if(cmtContentElem.value.trim() === '') {
        alert('댓글 내용을 작성해주세요.');
        return;
    }

    const boardIdxElem = document.querySelector('#boardIdx');
    const commentWriterIdxElem = document.querySelector('#sessionUserIdx');

    let params = {
        boardIdx: boardIdxElem.value,
        cmtContent: cmtContentElem.value,
        writerIdx: commentWriterIdxElem.value
    }

    fetch('/cmt', {
        method: 'post',
        headers: {
            'Content-Type' : 'application/json'
        },
        body: JSON.stringify(params)
    }).then(function(res) {
        return res.json()
    }).then(function(result) {
        if (result === 0) {
            cmtContentElem.value = '';
            getCommentList();
            setTimeout(likeLoad(), 1700);
        } else if(result === 1){
            alert('댓글 쓰기에 실패했습니다.');
        } else {
            alert('잘못된 요청입니다.');
        }
    })
}

// 댓글 출력
let cmtListElem = document.querySelector('#cmtList');
const boardIdxElem = document.querySelector('#boardIdx');
function getCommentList() {
    fetch(`/cmt?boardIdx=${boardIdxElem.value}`)
    .then(res => res.json())
    .then(result => {
        clearView();
        createView(result);
        setTimeout(likeLoad(), 500);
    })

    // 댓글창 초기화
    function clearView() {
        cmtListElem.innerHTML = '';
    }

    // 댓글 리스트 출력
    function createView(result) {
        if(result.length === 0) {
            return
        }
        result.forEach(function(item) {
            let contentDivElem = document.createElement('div');
            contentDivElem.innerHTML = createRecord(item);
            cmtListElem.append(contentDivElem);
        })
    }

    function createRecord(item) {
        if(item.useAt == "1") {
            item.cmtContent = '삭제된 댓글입니다.';
        }
        if(item.writerProfile == null) {
            item.writerProfile = '/images/profile.jpg';
        }

        let regDate = item.regDate.replace('T', ' ');

        let html = '';

        html += '<div class="oneMod hidden" id="oneMod'+item.idx+'">';
        html += addUpdForm(item);
        html += '</div>';

        let loginUserIdx = '';
        if(document.getElementById('userIdx')) {
            loginUserIdx = document.getElementById('userIdx').value;
        }

        html += '<div class="oneCmt" id="oneCmt'+item.idx+'">'
        html +=     '<div>'
        html +=         '<img class="cmtImg" src="'+item.writerProfile+'">'
        html +=     '</div>'
        html += 	'<div class="cmt_right">'
        html += 		'<div class="cmt_top_line">'
        html +=				"<span>"+item.writerName+"</span>"
        html +=				"&nbsp&nbsp"
        html +=				'<span>'+regDate+'&nbsp&nbsp&nbsp&nbsp&nbsp</span>'

        if(item.useAt !== "1") {
        html +=				'<div class="likeForm">'
        html +=					'<i class="far fa-heart" id="heart-icon'+item.idx+'" onclick="liked('+item.idx+' ,'+loginUserIdx+')"></i>'

        if(item.likeTotal !== 0) {
        html +=                 '<span>&nbsp 좋아요  </span>'
        html +=                 '<span id=like'+item.idx+'>'+item.likeTotal+'</span>'
        } else {
        html +=                 '<span>&nbsp 좋아요  </span>'
        html +=                 '<span id=like'+item.idx+'></span>'
        }
        html +=				'</div>'
        }

        else {
        html +=				'<div class="likeForm hidden">'
        html +=					'<i class="far fa-heart" id="heart-icon'+item.idx+'" onclick="liked('+item.idx+' ,'+loginUserIdx+')"></i>'
        html +=				'</div>'
        }

        html +=			"</div>"
        html +=			"<div>"
        html +=				'<p class="oneContent">'+item.cmtContent+'</p>'
        html += 		"</div>"

        // 자신이 쓴 댓글이라면 삭제, 수정버튼 추가
        let intUserIdx = parseInt(loginUserIdx);
		if(intUserIdx === item.writerIdx && item.useAt !== "1") {
		html +=			"<div>"
		html +=				'<input type="button" class="cmt_btn" id="mod_btn'+item.idx+'" value="수정" onclick="makeModFrm('+item.idx+')">'
		html +=				"&nbsp"
		html +=				'<input type="button" class="cmt_btn" id="del_btn'+item.idx+'" value="삭제" onclick="delAjax('+item.boardIdx+', '+item.idx+', '+intUserIdx+')">'
		html +=			"</div>"
		}

		html +=		"</div>"


		// 사용자 role
        if(document.getElementById('role')) {
            let role = document.getElementById('role').value;
            if(role === "ADMIN" && item.useAt !== "1") {
                html += "<div>"
                html += "&nbsp&nbsp&nbsp"
                html +=		'<input type="button" class="cmt_btn" id="del_btn'+item.idx+'" value="관리자 댓글 삭제" onclick="delAjax('+item.boardIdx+', '+item.idx+', '+intUserIdx+')">'
                html += "</div>"
                }
            }
		html +=	"</div>"

        return html;
    }
}
getCommentList();

// 새로 열리는 댓글 수정 form
function addUpdForm(item) {

    let loginUserIdx = '';
    if(document.getElementById('userIdx')) {
        loginUserIdx = document.getElementById('userIdx').value;
    }

    var addHtml = ''

    addHtml += 	"<div>"
    addHtml += 		'<img class="cmtImg" src="'+item.writerProfile+'">'
    addHtml += 	"</div>"
    addHtml += 	'<div class="cmt_right">'
    addHtml += 		"<div>"
    addHtml +=				"<span>"+item.writerName+"</span>"
    addHtml +=				"&nbsp&nbsp"
    addHtml +=				"<span>"+item.regDate+"</span>"
    addHtml +=		"</div>"
    addHtml +=		"<div>"
    addHtml +=			'<textarea name="newCmtContent" id="new_cmt_content'+item.idx+'" class="modWrite_content">'+item.cmtContent+'</textarea>'
    addHtml += 		"</div>"

    addHtml +=		"<div>"
    addHtml +=			'<input type="button" class="cmt_btn" id="mod_btn'+item.idx+'" value="수정" onclick="modAjax('+item.boardIdx+', '+item.idx+', '+loginUserIdx+')">'
    addHtml +=			"&nbsp"
    addHtml +=			'<input type="button" class="cmt_btn" id="del_btn'+item.idx+'" value="취소" onclick="canModFrm('+item.idx+')">'
    addHtml +=		"</div>"
    addHtml +=	"</div>"

    return addHtml;
}

function makeModFrm(cmtIdx) {
    let cmtUpdId1 = 'oneMod' + cmtIdx;
    let newUpdFrm = document.getElementById(`${cmtUpdId1}`);
    newUpdFrm.classList.remove('hidden');

    let cmtUpdId2 = 'oneCmt' + cmtIdx;
    let oriUpdFrm = document.getElementById(`${cmtUpdId2}`)
    oriUpdFrm.classList.add('hidden');
}

function canModFrm(cmtIdx) {
    let cmtUpdId1 = 'oneMod' + cmtIdx
    let newUpdFrm = document.getElementById(`${cmtUpdId1}`)
    newUpdFrm.classList.add('hidden')

    let cmtUpdId2 = 'oneCmt' + cmtIdx
    let oriUpdFrm = document.getElementById(`${cmtUpdId2}`)
    oriUpdFrm.classList.remove('hidden')
}

function delAjax(boardIdx, cmtIdx, loginUserIdx) {
    if(confirm('삭제하시겠습니까?')) {
        fetch(`/cmt?boardIdx=${boardIdx}&idx=${cmtIdx}&loginUserIdx=${loginUserIdx}`, {
            method: 'delete'
        }).then(res => res.json())
        .then(result => {
            if(result === 0) {
                getCommentList();
            } else {
                alert('댓글 삭제에 실패했습니다.');
            }
        })
    }
}

function modAjax(boardIdx, cmtIdx, loginUserIdx) {
    let newCmtContentId = 'new_cmt_content' + cmtIdx;
    let newCmtContentElem = document.getElementById(`${newCmtContentId}`);

    if(newCmtContentElem.value.trim() === '') {
        alert('댓글 내용을 작성해주세요.');
        return;
    }

    let params = {
        boardIdx: boardIdx,
        idx: cmtIdx,
        cmtContent: newCmtContentElem.value,
        loginUserIdx: loginUserIdx
    }

    fetch('/cmt', {
        method: 'put',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(params)
    }).then(res => res.json())
    .then(result => {
        if(result === 0) {
            getCommentList();
        } else {
            alert('댓글 수정에 실패했습니다.');
        }
    })
}