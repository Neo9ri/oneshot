package himedia.oneshot.service;

import himedia.oneshot.entity.Inquiry;
import himedia.oneshot.repository.JdbcInquiryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class InquiryService {

    private final JdbcInquiryRepository inquiryRepository;

    public void saveInquiry(Inquiry inquiry){
        inquiryRepository.saveInquiry(inquiry);
    }
    public void replyInquiry(Long id, String answer){
        inquiryRepository.replyInquiry(id,answer);
    }

    public Optional<Inquiry> findById(Long id){
        return inquiryRepository.findById(id);
    }

    public List<Inquiry> findListByType(String type){
        return inquiryRepository.findListByType(type);
    }

    public List<Inquiry> findListByInquirerId(Long inquirer_id){
        return inquiryRepository.findListByInquirerId(inquirer_id);
    }
}
