package himedia.oneshot.service;

import himedia.oneshot.dto.LoginDTO;
import himedia.oneshot.entity.Member;
import himedia.oneshot.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Service
@Slf4j
public class LoginService {

    private final MemberRepository memberRepository;

    public LoginService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * 로그인 정보를 컨트롤러의 모델에 넣어주는 메소드입니다.
     * LoginDTO의 필드를 사용할 수 있습니다.
     * @param request 클라이언트에게 요청받은 정보
     * @param model 로그인 정보를 컨트롤러에서 넘겨주기 위한 모델 타입
     * @see LoginDTO
     */
    public void loginCheck(HttpServletRequest request, Model model){
        HttpSession session = request.getSession();
        if (session.getAttribute("user")!=null){
            LoginDTO loginUser = (LoginDTO) session.getAttribute("user");
            model.addAttribute("user", loginUser);
            log.info("로그인 성공 여부 >> " + loginUser.getLoginSuccess());
        }
        else {
            session.setAttribute("user", new LoginDTO());
            log.info("비로그인 유저");
        }
    }

    public void loginCheck(HttpServletRequest request, Model model, LoginDTO loginData){
        LoginDTO loginResult = new LoginDTO();
        Optional<Member> loginMember = memberRepository.findByLoginId(loginData.getLoginId());

        if (loginMember.isPresent() && loginMember.get().getPw().equals(loginData.getPw())) {
            Member member = loginMember.get();
            long id = member.getId();
            String loginId = member.getLogin_id();
            String name = member.getName();
            String auth = member.getAuthority();
            loginResult = new LoginDTO(id, loginId, name, auth, true);
        }
        request.getSession().setAttribute("user", loginResult);
        model.addAttribute("user", loginResult);
        log.info("로그인 성공 여부 >> " + loginResult.getLoginSuccess());
    }


//    /**
//     * 로그인 기능 테스트 확인용입니다.
//     * 사용하지 마십시오.
//     */
//    public LoginDTO loginCheck(LoginDTO loginData){
//        Optional<Member> loginMember = memberRepository.findByLoginId(loginData.getLoginId());
//
//        if (loginMember.isPresent() && loginMember.get().getPw().equals(loginData.getPw())) {
//            Member member = loginMember.get();
//            long id = member.getId();
//            String loginId = member.getLogin_id();
//            String name = member.getName();
//            String auth = member.getAuthority();
//            LoginDTO loginResult = new LoginDTO(id, loginId, name, auth, true);
//            return loginResult;
//        } else
//            return new LoginDTO();
//    }
}
