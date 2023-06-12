package himedia.oneshot.controller;

import himedia.oneshot.service.Pagination;
import himedia.oneshot.entity.Member;
import himedia.oneshot.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Controller
public class AdminController {
    private final MemberService memberService;

    public AdminController(MemberService memberService){

        this.memberService = memberService;
    }

    @GetMapping("/member-list")
    public String memberList(@RequestParam(required = false) Integer page, Model model) {

        // 목록 구현 -- START
        List<Member> members = memberService.makeMemberList();
        int totalItem = members.size();
        int requestPage;
        try {
            requestPage = page.intValue();
        } catch (NullPointerException npe) {
            requestPage = 1;
        };
        Pagination pagination = new Pagination(totalItem,10, requestPage);
        model.addAttribute(pagination);

        int fromIndex = pagination.getFromIndex();
        int toIndex = pagination.getToIndex();

        try {
            members = members.subList(fromIndex, toIndex);
            model.addAttribute("members", members);
        } catch (IndexOutOfBoundsException ioobe) {
            if (members.size() != 0){
                toIndex = members.size();
                members = members.subList(fromIndex,toIndex);
                model.addAttribute("members", members);
            }
        }
        // 목록 구현 -- END
        return "/admin/member_list";
    }
}
