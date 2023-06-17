package himedia.oneshot.service;

import himedia.oneshot.entity.Notice;
import himedia.oneshot.repository.JdbcNoticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class NoticeService {

    private final JdbcNoticeRepository noticeRepository;

    public void saveNotice(Notice notice){
        noticeRepository.saveNotice(notice);
    }

    public void updateNotice(Long id,String title, String content){
        noticeRepository.updateNotice(id, title, content);
    }

    public Optional<Notice> findById(Long id){
        return noticeRepository.findById(id);
    }

    public List<Notice> findAll(){
        return noticeRepository.findAll();
    }

    public void deleteNotice(Long id){noticeRepository.deleteNotice(id);}


}
