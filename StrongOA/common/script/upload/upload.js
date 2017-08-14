
document.write('<script type="text/javascript" src="' + scriptPath + '/upload/ajaxfileupload.js"></script>');
document.write('<script type="text/javascript" src="' + scriptPath + '/upload/jquery.progressbar.min.js"></script>');
		
var intervalProcess;
var htmlInputFile = '<input type="file" id="ufUpload" name="ufUpload" onchange="ajaxFileUpload();"/>';
var uploadUrl = root + '/upload';
var uploadProgressUrl = root + '/upload-progress';
var config = {
		boxImage: scriptPath + "/upload/images/progressbar.gif",
		barImage: scriptPath + "/upload/images/progressbg_green.gif"
	};
var idProgressBox;
var idProgressBar;
var idProgressDel;
var idUploadFilename;
var fileNum;

function ufInit(fileNum){
	this.fileNum = fileNum;
	return '<div id="ufUploadList"><div id="ufAddFile"><span>' + htmlInputFile +
		'</span></div><div class="ufUploaded">还可以添加<b id="ufFileNum">'+fileNum+'</b>个文件</div></div>';
}

function ufAddFile(){
	var id = new Date().getTime();
	idProgressBox = "ufUploadProgressBox" + id;
	idProgressBar = "ufUploadProgressBar" + id;
	idProgressDel = "ufUploadProgressDel" + id;
	idUploadFilename = "ufUploadFilename" + id;
	var htmlProgressBox = '<div id="' +idProgressBox + '">' +
		'文件：<span id="' + idUploadFilename +'"></span>&nbsp;&nbsp;' +
		'<a href="#" onclick="ufDelete(\'' + idProgressBox + '\');return false;">删除</a> ' + 
		'<span id="' + idProgressBar + '"></span></div>';
	$("#ufUploadList").append(htmlProgressBox);
	$("#"+idProgressBar).progressBar(config);
}

function ajaxFileUpload(){
	if(confirm("是否上传")){
		ufAddFile();
		
		$.ajaxFileUpload({
				url:uploadUrl, 
				secureuri:false,
				fileElementId:'ufUpload',
				dataType: 'json',
				data: {filePath:filePath},
				success: function (data, status){
					clearInterval(intervalProcess);
					$("#"+idProgressBar).progressBar(100);
					$("#"+idProgressBar).html('<span class="ufUploaded">已上传</span>' +
						'<input type="hidden" name="ufUploadedFileName" value="' +
						data.file + '"/>');
				},
				error: function (data, status, e){
					alert(e);
				}
		});
		//$("#ufUploadProgressBox").show();
		intervalProcess = setInterval("ufProgress()", 50);
		$("#"+idUploadFilename).html(ufUploadFilename());
		$("#ufFileNum").html(--fileNum);
		if(fileNum <= 0){
			$("#ufUpload").attr("disabled", true);
		}
	}
	else{
		$("#ufAddFile").html("");
		$("#ufAddFile").html(htmlInputFile);
	}
}

function ufProgress(){
	$.get(uploadProgressUrl + "?" + Math.random(), function(response){
		var percentage = Math.floor(response);
		$("#"+idProgressBar).progressBar(percentage);
	});
}

$(function(){
	//$("#ufUploadProgressBar").progressBar(config);
	//$("#ufUploadProgressBox").hide();
});

function ufUploadFilename(){
	var str = $("input[id^=jUploadFile]").attr("value");
	var pos = str.lastIndexOf("\\");
	str = str.substring(pos + 1);
	return str;
}

function ufDelete(idBox){
	if(confirm("是否删除")){
		var id = $("iframe[id^=jUploadFrame]").attr("id");
		if(id != null){
			$("#"+id).remove();
			var formId = $("form[id^=jUploadForm]").attr("id");
			$("#"+formId).remove();
		}
		$("#"+idBox).remove();
		$("#ufFileNum").html(++fileNum);
		if(fileNum > 0){
			$("#ufUpload").attr("disabled", false);
		}
	}
}