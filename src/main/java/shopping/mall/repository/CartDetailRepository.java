package shopping.mall.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shopping.mall.domain.Cart;
import shopping.mall.domain.CartDetail;
import shopping.mall.domain.Item;

import java.util.List;

@Repository
public interface CartDetailRepository extends JpaRepository<CartDetail, Long> {
    CartDetail findByCartIdAndItemId(Long cartId, Long ItemId);

    CartDetail findByCartId(Long cartId);
}
