package shopping.mall.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping.mall.domain.Cart;
import shopping.mall.domain.CartDetail;
import shopping.mall.domain.Item;
import shopping.mall.domain.Member;
import shopping.mall.repository.CartDetailRepository;
import shopping.mall.repository.CartRepository;
import shopping.mall.repository.MemberRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {

    private final CartRepository cartRepository;
    private final CartDetailRepository cartDetailRepository;

    @Transactional
    // 장바구니 생성
    public Cart createCart(Member member) {
        Cart cart = Cart.createCart(member);
        cartRepository.save(cart);
        return cart;
    }

    @Transactional
    // 장바구니에 상품 추가
    public CartDetail addCart(Member member, Item item, int count) {

        // 상품 재고 초과시 오류발생
        if (count > item.getStockQuantity()) {
            throw new IllegalStateException("상품 재고 부족");
        }

        // 상품 재고 count만큼 감소
        item.removeStock(count);

        Cart cart = cartRepository.findByMemberId(member.getId());

        // 장바구니가 비었다면 생성
        if(cart == null){
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }

        // cartDetail 조회
        CartDetail cartDetail = cartDetailRepository.findByCartIdAndItemId(cart.getId(), item.getId());

        // 동일한 item의 cartDetail이 없으면 생성
        if (cartDetail == null) {
            cartDetail = CartDetail.createCartDetail(cart, item, count);
            cartDetailRepository.save(cartDetail);
            cart.setCount(cart.getCount()+1);
        } else {
            cartDetail.addCount(count);
        }
        return cartDetail;
    }

    // 회원id로 장바구니 조회
    public Cart findCartByMemberId(Long id) {
        return cartRepository.findByMemberId(id);
    }

    // 장바구니 상세 전체조회
    public List<CartDetail> memberCartView(Cart cart){
        List<CartDetail> findCartDetails = cartDetailRepository.findAll();
        List<CartDetail> cartDetails = new ArrayList<>();

        for(CartDetail findCartDetail : findCartDetails){
            if(findCartDetail.getCart().getId() == cart.getId()){
                cartDetails.add(findCartDetail);
            }
        }

        return cartDetails;
    }

    @Transactional
    //장바구니에서 특정 상품 삭제(개수만큼)
    public CartDetail subtractCart(Member member, Item item, int count) {
        Cart cart = cartRepository.findByMemberId(member.getId());
        CartDetail cartDetail = cartDetailRepository.findByCartId(cart.getId());
        // 상품개수가 0이되면 해당 cartDetail 삭제됨
        if (cartDetail.getCount() - count <= 0) {
            item.addStock(cartDetail.getCount());
            cartDeleteDetail(member, cartDetail);
        } else {
            item.addStock(count);
            cartDetail.subtractCount(count);
        }
        return cartDetail;
    }

    @Transactional
    // 장바구니 상세 삭제
    public void cartDeleteDetail(Member member, CartDetail cartDetail) {
        Cart cart = cartRepository.findByMemberId(member.getId());
        cart.setCount(cart.getCount()-1);
        cartDetail.getItem().addStock(cartDetail.getCount());
        cartDetailRepository.delete(cartDetail);
    }

    @Transactional
    // 장바구니 상세 전체삭제
    public void cartDeleteAll(Cart cart) {
        List<CartDetail> cartDetails = cartDetailRepository.findAll();

        for(CartDetail cartDetail : cartDetails){
            if(cartDetail.getCart().getId() == cart.getId()){
                cartDetail.getItem().addStock(cartDetail.getCount());
                cart.setCount(cart.getCount()-1);
                cartDetailRepository.delete(cartDetail);
            }
        }
    }

    @Transactional
    // 징바구니 리셋
    public void cartReset(Cart cart) {
        cart.setCount(0);
        List<CartDetail> cartDetails = cartDetailRepository.findAll();
        for (CartDetail cartDetail : cartDetails) {
            if (cartDetail.getCart().getId() == cart.getId()) {
                cartDetailRepository.delete(cartDetail);
            }
        }
    }
}
