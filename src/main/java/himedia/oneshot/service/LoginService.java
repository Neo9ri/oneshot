package himedia.oneshot.service;

import himedia.oneshot.dto.LoginDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Service
@Slf4j
public class LoginService {

    public void loginCheck(HttpServletRequest request, Model model){
        HttpSession session = request.getSession();
        if (session.getAttribute("user")!=null){
            LoginDTO loginUser = (LoginDTO) session.getAttribute("user");
            model.addAttribute("user", loginUser);
            log.info("로그인 여부 확인");
            log.info("로그인 정보 >> " + loginUser.getLoginSuccess());
            log.info("로그인 유저 정보 세팅 완료");
        }
        else {
            log.info("메인 페이지 로그인 유저 정보 없음");
            request.setAttribute("user", new LoginDTO());
        }
    }
}
