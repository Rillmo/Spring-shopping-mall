package shopping.mall.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping.mall.domain.Cart;
import shopping.mall.domain.Member;
import shopping.mall.repository.CartRepository;
import shopping.mall.repository.MemberRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    // 회원가입
    @Transactional
    public Long join(Member member) {
        validateDuplicateUserId(member);
        return memberRepository.save(member);
    }

    // 멤버아이디 중복검증
    public void validateDuplicateUserId(Member member) {
        List<Member> findNickNameList = memberRepository.findByUserId(member.getUserId());
        if (!findNickNameList.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 아이디입니다.");
        }
    }

    // 전체 회원 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    // 특정 회원 userId로 조회
    public List<Member> findByUserId(String userId) {
        return memberRepository.findByUserId(userId);
    }

    // 특정 회원 id값으로 조회
    public Member findById(Long id) {
        return memberRepository.findOne(id);
    }

    // 회원탈퇴
    @Transactional
    public Long deleteMember(Member member) {
        return memberRepository.delete(member);
    }

    // 회원 수정
    @Transactional
    public void updateMember(Long id, String name, String userId, String phoneNum, String email) {
        Member member = memberRepository.findOne(id);
        member.setName(name);
        member.setUserId(userId);
        member.setPhoneNum(phoneNum);
        member.setEmail(email);
        member.setFinalUpdateDate(LocalDate.now());
    }

}
