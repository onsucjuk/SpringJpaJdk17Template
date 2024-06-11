package kopo.poly.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "INTEREST_INDUTY")
@DynamicInsert
@DynamicUpdate
@Builder
@Entity
public class InterestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "interest_seq")
    private Long interestSeq;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "seoul_location_cd", nullable = false)
    private String seoulLocationCd;

    @Column(name = "seoul_location_nm", nullable = false)
    private String seoulLocationNm;

    @Column(name = "induty_nm", nullable = false)
    private String indutyNm;

    @Column(name = "induty_cd", nullable = false)
    private String indutyCd;


}
