package himedia.oneshot.repository;

import himedia.oneshot.entity.Inquiry;

import java.util.List;
import java.util.Optional;

public interface InquiryRepository {
    Inquiry saveInquiry(Inquiry inquiry);
    void replyInquiry(Long id, String title, String content);
    Optional<Inquiry> findById(Long id);
    List<Inquiry> findListByType(String type);

}
