package himedia.oneshot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminController {

    @GetMapping("/member-list")
    public String memberList(@RequestParam(required = false) int page, Model model) {

//        model.addAttribute();
        return "/admin/member_list";
    }
}
