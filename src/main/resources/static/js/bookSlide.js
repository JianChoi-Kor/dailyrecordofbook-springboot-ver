

//client rolling banner

window.onload = function() {
	var bannerLeft = 0;
	var first = 1;
	var last;
	var imgCnt = 0;
	var $img = $(".book-slide-container img");
	var $first;
	var $last;

	$img.each(function() {   // 5px 간격으로 배너 처음 위치 시킴
		$(this).css("left", bannerLeft);
		bannerLeft += $(this).width();
		$(this).attr("id", "banner" + (++imgCnt));  // img에 id 속성 추가
	});


	if (imgCnt > 9) {                //배너 9개 이상이면 이동시킴



		last = imgCnt;

		setInterval(function() {
			$img.each(function() {
				$(this).css("left", $(this).position().left - 1); // 1px씩 왼쪽으로 이동
			});
			$first = $("#banner" + first);
			$last = $("#banner" + last);
			if ($first.position().left < -200) {    // 제일 앞에 배너 제일 뒤로 옮김
				$first.css("left", $last.position().left + $last.width());
				first++;
				last++;
				if (last > imgCnt) { last = 1; }
				if (first > imgCnt) { first = 1; }
			}
		}, 50);   //여기 값을 조정하면 속도를 조정할 수 있다.(위에 1px 이동하는 부분도 조정하면 

		//깔끔하게 변경가능하다           

	}
};




const bookModalElem = document.querySelector('#bookModal')

const modBookImgElem = bookModalElem.querySelector('#modBookImg')
const modBookTitleElem = bookModalElem.querySelector('#modBookTitle')
const modCommunityInfoElem = bookModalElem.querySelector('#modCommunityInfo')

function openModal(bookPk) {
	
	console.log(`bookData${bookPk}`)
	var data = document.querySelector(`#bookData${bookPk}`)

	var {booktitle, communityinfo, bookimg} = data.dataset
	
	modBookImgElem.src = bookimg
	modBookTitleElem.value = booktitle
	modCommunityInfoElem.value = communityinfo

	bookModalElem.classList.remove('hide')

}



//	모달 닫기 버튼
var modalCloseElem = document.querySelector('#modClose')

modalCloseElem.addEventListener('click', function() {
	bookModalElem.classList.add('hide')
})












