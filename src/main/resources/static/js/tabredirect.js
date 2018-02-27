$(function(){
	$("#chooseListType").change(function(){
		var url = $(this).val();
		if (/MSIE (\d+\.\d+);/.test(navigator.userAgent)){
		    var referLink = document.createElement('a');
		    referLink.href = url;
		    referLink.style="display:none;";
		    document.body.appendChild(referLink);
		    referLink.click();
		} else {
			window.location.href = url;
		}
	});
});