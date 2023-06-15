package shopping.mall.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shopping.mall.domain.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
}
