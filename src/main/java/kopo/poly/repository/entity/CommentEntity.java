package kopo.poly.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "BOARD_COMMENTS")
@DynamicInsert
@DynamicUpdate
@Builder
@Cacheable
@Entity
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_seq")
    private Long commentSeq;

    @NonNull
    @Column(name = "notice_seq", nullable = false)
    private Long noticeSeq;

    @NonNull
    @Column(name = "user_id", nullable = false)
    private String userId;

    @NonNull
    @Column(name = "comment_contents", length = 150, nullable = false)
    private String commentContents;

    @NonNull
    @Column(name = "comment_reg_id", updatable = false)
    private String commentRegId;

    @Column(name = "comment_reg_dt", updatable = false)
    private String commentRegDt;

    @Column(name = "comment_chg_id")
    private String commentChgId;

    @Column(name = "comment_chg_dt")
    private String commentChgDt;

}
