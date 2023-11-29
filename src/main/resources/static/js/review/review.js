// 수정 시에 썸네일 이미지와 추가 이미지 표시
    window.addEventListener('load', function() {
        var thumbnailPreview = document.getElementById('thumbnail-preview');
        var thumbnailRemoveButton = document.getElementById('thumbnail-remove-button');
        var additionalImagesPreview = document.getElementById('additionalImages-preview');

        // 기존에 선택된 썸네일 이미지 표시
        var selectedThumbnailContainer = document.getElementById('selected-thumbnail-container');
        var selectedThumbnailPreview = document.getElementById('selected-thumbnail-preview');
        var selectedThumbnailRemoveButton = document.getElementById('selected-thumbnail-remove-button');


//        if (selectedThumbnailContainer) {
//            selectedThumbnailRemoveButton.addEventListener('click', function() {
//                selectedThumbnailContainer.style.display = 'none';
//            });
//        }

        // 기존에 선택된 추가 이미지 표시
        var selectedImagesPreview = document.getElementById('selectedImages-preview');
        //var selectedImageRemoveButtons = selectedImagesPreview.getElementsByTagName('button');

        // 확인용
        //console.log(selectedImageRemoveButtons);

       // var idList = [];

//        for (var i = 0; i < selectedImageRemoveButtons.length; i++) {
//            var selectedImageRemoveButton = selectedImageRemoveButtons[i];
//            let fileId = selectedImageRemoveButton.id.replace('selected-img-remove-button', '');

//            selectedImageRemoveButton.addEventListener('click', function() {
//                this.parentNode.style.display = 'none';
//                idList.push(fileId);
//                console.log(idList);
//            });
//        }

        // 폼 제출 시 idList 값을 폼 필드에 할당
        var reviewForm = document.getElementById('reviewForm');
        var idListField = document.getElementById('idListField');

//        reviewForm.addEventListener('submit', function() {
//            idListField.value = idList.join(',');
//        });
    });

    // 썸네일 이미지 미리보기
    var thumbnailInput = document.getElementById('thumbnail');
    var thumbnailPreviewContainer = document.getElementById('thumbnail-preview-container');
    var thumbnailPreview = document.getElementById('thumbnail-preview');
    var thumbnailRemoveButton = document.getElementById('thumbnail-remove-button');

    thumbnailInput.onchange = function (e) {
        var reader = new FileReader();
        reader.onload = function (event) {
            thumbnailPreview.src = event.target.result;
            thumbnailPreview.style.display = 'block';
            thumbnailRemoveButton.style.display = 'block';
        }
        reader.readAsDataURL(e.target.files[0]);
    }

    thumbnailRemoveButton.addEventListener('click', function() {
        thumbnailInput.value = null; // 이미지 선택 취소
        thumbnailPreview.style.display = 'none';
        thumbnailPreview.src = '';
        thumbnailRemoveButton.style.display = 'none';
    });

    // 추가 이미지 미리보기
    var additionalImagesInput = document.getElementById('additionalImages');
    var additionalImagesPreview = document.getElementById('additionalImages-preview');

    additionalImagesInput.onchange = function (e) {
        var files = e.target.files;
        additionalImagesPreview.innerHTML = '';
        for (var i = 0; i < files.length; i++) {
            var reader = new FileReader();
            reader.onload = function (event) {
                var img = document.createElement('img');
                img.src = event.target.result;
                img.style.maxWidth = '200px';
                img.style.maxHeight = '200px';
                additionalImagesPreview.appendChild(img);
            }
            reader.readAsDataURL(files[i]);
        }
    }

    var idList = [];


    function removeImage(fileId){
        console.log("fileId"+fileId);

        var detailImg = document.getElementById('selectedImages-preview'+fileId);
        console.log("detailImg"+detailImg);
        detailImg.style.display = 'none';
        idList = idList.fileId;
        console.log("idList" + idList);
        idListField.value = idList.join(',');
        console.log("idListField" + idListField.value);

    }