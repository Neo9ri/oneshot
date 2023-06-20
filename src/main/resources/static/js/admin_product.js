
    function onClickThumbnailUpload() { // 썸네일 파일 첨부하기 함수
      let thumbnailImageButton = document.getElementById("thumbnail");
      thumbnailImageButton.click()
    }

    function showThumbnail(input) { // 썸네일 미리보기 함수
      if (input.files && input.files[0]) {
          const reader = new FileReader();
          reader.onload = function (e) {
              document.getElementById('thumbnailPreview').style.display='block';
          document.getElementById('thumbnailPreview').src = e.target.result;
        };
        reader.readAsDataURL(input.files[0]);
      } else {
        document.getElementById('thumbnailPreview').src = "";
      }
    }

    function onClickExpUpload() { // 상품설명 파일 첨부하기 함수
      let expImageButton = document.getElementById("exp");
      expImageButton.addEventListener("change", handleExpUpload);
      expImageButton.click();
    }

    function handleExpUpload(event) {
        const input = event.target;
        const fileArr = Array.from(input.files);

        if (fileArr.length>2) {
            // 최소1개이상 최대2개 선택 가능 , 범위 벗어나면 경고창후 새로고침
            alert("이미지는 최소 1개, 최대 2개까지 첨부가 가능합니다.");
            input.value = "";
            location.reload();
        }
    }

//상품설명 여러개 미리보기 함수
    function showMultipleExp(input) {

        const multipleExpPreview = document.getElementById('multipleExpPreview');

        const images = multipleExpPreview.querySelectorAll('img');
        images.forEach((image) => {
            multipleExpPreview.removeChild(image);
        });


        if(input.files) {
            const fileArr = Array.from(input.files);

            fileArr.forEach((file,index) => {
                const reader = new FileReader();
                const exp = document.createElement('img');
                exp.setAttribute('id',`exp${index+1}`);
                multipleExpPreview.appendChild(exp);
                reader.onload = e => {
                    exp.src = e.target.result;
                };

                reader.readAsDataURL(file);
                multipleExpPreview.appendChild(document.createElement('br'));
            });
        }
    }