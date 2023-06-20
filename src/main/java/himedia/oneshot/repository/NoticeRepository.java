package himedia.oneshot.repository;

import himedia.oneshot.entity.Notice;

import java.util.List;
import java.util.Optional;

public interface NoticeRepository {
    Notice saveNotice(Notice Notice);
    Notice updateNotice(Long id,String title, String content);
    Optional<Notice> findById(Long id);
    List<Notice> findAll();
    void deleteNotice(Long id);

}

