    const gnbbar = document.querySelector(".gnbbar");

    if(gnbbar != null){
        const gnbmenu = document.querySelectorAll("nav.gnb>ul>li");
        gnbmenu.forEach((v, index) => {
              if (index !== 0) {
                v.addEventListener("mouseover", (e) => {
                  gnbbar.style.display = "block";
                });

                v.addEventListener("mouseout", (e) => {
                  gnbbar.style.display = "none";
                });
              }
            });
    }


// upButton
    window.addEventListener("scroll",function(){
      const upButton = document.getElementById("up");
      if(window.scrollY>500){
        upButton.classList.add('show');
      }else upButton.classList.remove('show');
    })
    const scrollToTop = ()=>{
      window.scrollTo({
        top:0,
        behavior:"smooth"
      });
    }