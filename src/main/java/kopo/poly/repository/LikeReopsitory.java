package kopo.poly.repository;

import kopo.poly.repository.entity.LikeEntity;
import kopo.poly.repository.entity.LikePK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeReopsitory extends JpaRepository<LikeEntity, LikePK> {
}
