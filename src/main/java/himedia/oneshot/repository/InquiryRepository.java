package himedia.oneshot.repository;

import himedia.oneshot.entity.Inquiry;

import java.util.List;
import java.util.Optional;

public interface InquiryRepository {
    Inquiry saveInquiry(Inquiry inquiry);
    Inquiry replyInquiry(Long id, String answer);
    Optional<Inquiry> findById(Long id);
    List<Inquiry> findListByType(String type);
    List<Inquiry> findListByInquirerId(Long inquirer_id);

}
