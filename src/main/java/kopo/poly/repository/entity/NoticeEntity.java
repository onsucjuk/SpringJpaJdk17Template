package kopo.poly.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "OWNER_BOARD")
@DynamicInsert
@DynamicUpdate
@Builder
@Cacheable
@Entity
public class NoticeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_seq")
    private Long noticeSeq;

    @NonNull
    @Column(name = "title", length = 500, nullable = false)
    private String title;
    @NonNull
    @Column(name = "contents", nullable = false)
    private String contents;

    @NonNull
    @Column(name = "user_id", nullable = false)
    private String userId;

    @NonNull
    @Column(name = "read_cnt", nullable = false)
    private Long readCnt;

    @Column(name = "reg_id", updatable = false)
    private String regId;

    @Column(name = "reg_dt", updatable = false)
    private String regDt;

    @Column(name = "chg_id")
    private String chgId;

    @Column(name = "chg_dt")
    private String chgDt;

}
