package himedia.oneshot.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class Notice {
    Long id;
    Date date_created;
    String title;
    String content;
    Date date_updated;

    public Notice(String title, String content, Date date_created){
        this.title = title;
        this.content = content;
        this.date_created =  date_created;
    }
}
