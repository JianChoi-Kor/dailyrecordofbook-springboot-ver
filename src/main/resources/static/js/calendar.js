/**
 * 
 */

document.addEventListener('DOMContentLoaded', function() {
    var initialLocaleCode = 'ko';
	var calendarEl = document.getElementById('calendar');

	var calendar = new FullCalendar.Calendar(calendarEl, {
	    locale: initialLocaleCode,
		googleCalendarApiKey: 'AIzaSyACLKDbnkR8cLpsoGr33lnmMT8Ftbgh_0A',
		eventSources: [
			{
				googleCalendarId: 'pg1qvs2cfdappkqfttigprhb30@group.calendar.google.com',
				className: '책방일지 글쓰기 모임 일정',
				color: '#277aed', //rgb,#ffffff 등의 형식으로 할 수 있어요.
				textColor: 'black'
			},
			{
				googleCalendarId: 'l1abekpirhbbgiqne13mum6lbc@group.calendar.google.com',
				className: '책방일지 독서 모임 일정',
				color: '#e5bc47', //rgb,#ffffff 등의 형식으로 할 수 있어요.
				textColor: 'black'
			},
			{
				googleCalendarId: 'aalu6idmcmm5ljpboi2s53fkk8@group.calendar.google.com',
				className: '책방일지 회의 일정',
				color: '#ff4828', //rgb,#ffffff 등의 형식으로 할 수 있어요.
				textColor: 'black'
			},
			{
				googleCalendarId: 'ko.south_korea#holiday@group.v.calendar.google.com',
				className: '대한민국의 휴일',
				color: '#00743e', //rgb,#ffffff 등의 형식으로 할 수 있어요.
				textColor: 'black'
			}
			
		]
	});
	calendar.render();
});