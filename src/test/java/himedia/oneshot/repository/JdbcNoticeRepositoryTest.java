//package himedia.oneshot.repository;
//
//import himedia.oneshot.entity.Notice;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Date;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//@Transactional
//@Slf4j
//class JdbcNoticeRepositoryTest {
//
//    @Autowired
//    JdbcNoticeRepository noticeRepository;
//
//    Date today = new Date();
//    @Test
//    void saveNotice() {
//        //given
//        Notice before = new Notice("배송공지","배송일정에 관해 공지합니다.",today);
//        log.info("{}",before.getId());
//        //when
//        Notice after = noticeRepository.saveNotice(before);
//        log.info("{}",after.getId());
//        //then
//        assertThat(after.getId()).isEqualTo(before.getId());
//    }
//
//    @Test
//    void updateNotice() {
//        //given
//        Notice before = new Notice("배송공지","배송일정에 관해 공지합니다.",today);
//        log.info("{}",before.getTitle());
//        Notice after = noticeRepository.saveNotice(before);
//        //when
//        Notice updated = noticeRepository.updateNotice(after.getId(),"휴일공지","휴일공지합니다.");
//        log.info("{}",updated.getTitle());
//        //then
//        assertThat(after.getTitle()).isNotEqualTo(updated.getTitle());
//    }
//
//    @Test
//    void findById() {
//        //given
//        Notice before = new Notice("안내사항","안내드립니다.",today);
//        Notice after = noticeRepository.saveNotice(before);
//        //when
//        Optional<Notice> id = noticeRepository.findById(after.getId());
//        //then
//        assertThat(id.isPresent()).isEqualTo(true);
//    }
//
//    @Test
//    void findAll() {
//        //given
//        List<Notice> before = noticeRepository.findAll();
//        log.info("{}",before.size());
//        Notice notice = new Notice("배송공지","배송공지합니다.",today);
//        noticeRepository.saveNotice(notice);
//        //when
//        List<Notice> after = noticeRepository.findAll();
//        //then
//        assertThat(before.size()).isNotEqualTo(after.size());
//    }
//
//    @Test
//    void deleteNotice() {
//        //given
//        Notice before = new Notice("휴무안내","휴무 공지드립니다.",today);
//        Notice after = noticeRepository.saveNotice(before);
//        log.info("{}",after.getTitle());
//        //when
//        noticeRepository.deleteNotice(after.getId());
//        log.info("{}",noticeRepository.findById(after.getId()));
//        //then
//        assertThat(noticeRepository.findById(after.getId())).isEmpty();
//    }
//}