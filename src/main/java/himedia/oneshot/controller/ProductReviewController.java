package himedia.oneshot.controller;

import himedia.oneshot.service.LoginService;
import himedia.oneshot.service.ProductReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ProductReviewController {
    private final ProductReviewService reviewService;
    private final LoginService loginService;


}
