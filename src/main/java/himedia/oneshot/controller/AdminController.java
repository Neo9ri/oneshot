package himedia.oneshot.controller;

import himedia.oneshot.entity.Member;
import himedia.oneshot.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AdminController {
    private final MemberService memberService;

    public AdminController(MemberService memberService){

        this.memberService = memberService;
    }

    @GetMapping("/member-list")
    public String memberList(Model model) {
        List<Member> members = memberService.findAllMember();
        model.addAttribute("members", members);
        return "/admin/member_list";
    }
}
