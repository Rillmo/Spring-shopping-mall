package shopping.mall.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping.mall.domain.Item;
import shopping.mall.domain.Member;
import shopping.mall.repository.ItemRepository;
import shopping.mall.repository.MemberRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemRepository;

    // 상품 등록
    @Transactional
    public void saveItem(Item item) {
        validateDuplicateName(item);
        itemRepository.save(item);
    }

    // 상품명 중복검증
    public void validateDuplicateName(Item item) {
        List<Item> findItemNameList = itemRepository.findByName(item.getName());
        if (!findItemNameList.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 상품명입니다.");
        }
    }

    // 상품 전체조회
    public List<Item> findAllItems() {
        return itemRepository.findAll();
    }

    // 상품 ID로 조회
    public Item findItemById(Long id) {
        return itemRepository.findOne(id);
    }

    // 상품명으로 조회
    public List<Item> findItemByName(String name) {
        return itemRepository.findByName(name);
    }

    @Transactional
    // 상품 삭제
    public Long deleteItem(Long id) {
        return itemRepository.delete(id);
    }

    // 상품 수정
    @Transactional
    public void updateItem(Long id, String name, int price, int stockQuantity, String detail, int grade)
    {
        Item item = itemRepository.findOne(id);
        item.setName(name);
        item.setPrice(price);
        item.setStockQuantity(stockQuantity);
        item.setDetail(detail);
        item.setGrade(grade);
        item.setFinalUpdateDate(LocalDate.now());
    }

}
