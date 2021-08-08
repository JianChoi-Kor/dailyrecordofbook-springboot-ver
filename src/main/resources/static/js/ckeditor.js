/**
 * 
 */

ClassicEditor.create(document.querySelector('#editor'), {
	ckfinder : {
		uploadUrl : '/board/imageUpload'
	}
}).then(editor => {
	window.editor = editor
	})
.catch(error => {
	console.error(error)
})





