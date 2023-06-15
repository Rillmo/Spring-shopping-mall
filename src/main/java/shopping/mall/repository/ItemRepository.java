package shopping.mall.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shopping.mall.domain.Item;

import java.util.List;

@Repository
public class ItemRepository {

    @PersistenceContext
    EntityManager em;

    public Long save(Item item) {
        em.persist(item);
        return item.getId();
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class).getResultList();
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findByName(String name) {
        return em.createQuery("select i from Item i where i.name =: name", Item.class)
                .setParameter("name", name)
                .getResultList();
    }

    public Long delete(Long id) {
        em.createQuery("delete from Item i where i.id =: id")
                .setParameter("id", id)
                .executeUpdate();
        return id;
    }

}
