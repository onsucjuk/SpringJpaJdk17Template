package kopo.poly.repository.entity;

import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USER_INFO")
@DynamicInsert
@DynamicUpdate
@Builder
@Entity
public class UserInfoEntity implements Serializable {

    @Id
    @Column(name = "user_id")
    private String userId;

    @NonNull
    @Column(name = "user_name", length = 100, nullable = false)
    private String userName;

    @NonNull
    @Column(name = "password", length = 100, nullable = false)
    private  String password;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "addr1", nullable = false)
    private String addr1;

    @Column(name = "addr2", nullable = false)
    private String addr2;

    @Column(name = "reg_dt", updatable = false)
    private String regDt;


}
