package himedia.oneshot.repository;

import himedia.oneshot.entity.Inquiry;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Slf4j
class JdbcInquiryRepositoryTest {

    @Autowired
    JdbcInquiryRepository inquiryRepository;
    Date today = new Date();
    @Test
    void save() {
        //given
        Inquiry beforeSave = new Inquiry("P", 2L,"상품문의드려요","몇개까지 살 수 있나요?", today);
        log.info("저장전 문의 번호 >>{}",beforeSave.getId());
        //when
        Inquiry afterSave = inquiryRepository.saveInquiry(beforeSave);
        log.info("저장 후 문의번호>>{}",afterSave.getId());
        //then
        assertThat(afterSave.getId()).isEqualTo(beforeSave.getId());
    }

    @Test
    void reply() {
        //given
        Inquiry beforeSave = new Inquiry("D",3L,"배송문의 드려요","언제쯤 배송 되나요?", today);
        Inquiry afterSave = inquiryRepository.saveInquiry(beforeSave);

        //when
        Inquiry replied = inquiryRepository.replyInquiry(afterSave.getId(), "배송은 배송사 사정에따라 차이가 있습니다.");
        log.info("id>>{}", replied.getId());
        log.info("답변시간>>{}",replied.getDate_replied());
        log.info("답변내용>>{}",replied.getAnswer());
        //then
        assertThat(inquiryRepository.findById(replied.getId()).get().getAnswer()).isNotEmpty();

    }

    @Test
    void findId() {
        //given
        Inquiry beforeSave = new Inquiry("D",3L,"배송문의 드려요","언제쯤 배송 되나요?",today);
        Inquiry afterSave = inquiryRepository.saveInquiry(beforeSave);
        //when
        Optional<Inquiry> foundInquiry = inquiryRepository.findById(afterSave.getId());
        //then
        assertThat(foundInquiry).isPresent();
    }

    @Test
    void findByType() {
        //given
        List<Inquiry> before = inquiryRepository.findListByType("P");
        //when
        Inquiry beforeSave = new Inquiry("D",2L,"배송문의 드려요","퀵으로 받을 수 있나요?",today);
        inquiryRepository.saveInquiry(beforeSave);
        List<Inquiry> after = inquiryRepository.findListByType("P");
        //then
        assertThat(before.size()).isEqualTo(after.size());
    }

    @Test
    void findByInquirerId() {
        //given
        List<Inquiry> before = inquiryRepository.findListByInquirerId(3L);
        //when
        Inquiry beforeSave = new Inquiry("P",3L,"상품 문의 드립니다.","몇미리인가요? 박스로 구매 가능한가요?",today);
        inquiryRepository.saveInquiry(beforeSave);
        List<Inquiry> after = inquiryRepository.findListByInquirerId(3L);
        //then
        assertThat(before.size()).isNotEqualTo(after.size());
    }
}