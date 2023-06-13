    const gnbbar = document.querySelector(".gnbbar");
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

    // swiper
    var swiper = new Swiper(".mySwiper", {
      spaceBetween: 30,
      centeredSlides: true,
      autoplay: {
        delay: 2500,
        disableOnInteraction: false,
      },
      pagination: {
        el: ".swiper-pagination",
        clickable: true,
      },
      navigation: {
        nextEl: ".swiper-button-next",
        prevEl: ".swiper-button-prev",
      },
    });
