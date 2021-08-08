/**
 * 
 */

document.addEventListener('DOMContentLoaded', function() {
    var initialLocaleCode = 'ko';
	var calendarEl = document.getElementById('calendar');

	var calendar = new FullCalendar.Calendar(calendarEl, {
	    locale: initialLocaleCode,
		googleCalendarApiKey: 'AIzaSyDlM8_zkE1xCUPSOeV6HekOEBJz6hAO9cA',
		eventSources: [
			{
				googleCalendarId: 'gdi46gu1b41dm9vdk5c8b7vk1g@group.calendar.google.com',
				className: '책방일지 글쓰기 모임',
				color: '#f4b536', //rgb,#ffffff 등의 형식으로 할 수 있어요.
				textColor: 'black'
			},
			{
				googleCalendarId: 'p04ngm7h01vdu9odk1hfa45g8s@group.calendar.google.com',
				className: '책방일지 영화 모임',
				color: '#364ba8', //rgb,#ffffff 등의 형식으로 할 수 있어요.
				textColor: 'black'
			},
			{
				googleCalendarId: '7ro9v4kp8k7qerevi5ght47128@group.calendar.google.com',
				className: '책방일지 정규 모임',
				color: '#f14624', //rgb,#ffffff 등의 형식으로 할 수 있어요.
				textColor: 'black'
			},
			{
				googleCalendarId: 'gpnbcqvigoi6g8jtas8ur6k83s@group.calendar.google.com',
				className: '책방일지 하루 독서 모임',
				color: '#15743e', //rgb,#ffffff 등의 형식으로 할 수 있어요.
				textColor: 'black'
			}
			
		]
	});
	calendar.render();
});


