package shopping.mall.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import shopping.mall.Form.ItemForm;
import shopping.mall.Form.MemberForm;
import shopping.mall.domain.Cart;
import shopping.mall.domain.Item;
import shopping.mall.domain.Member;
import shopping.mall.service.MemberService;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("/new")
    public String create(@Validated MemberForm memberForm, BindingResult result) {
        if (result.hasErrors()) {
            return "members/createMemberForm";
        }
        // 비밀번호 일치확인
        if (!memberForm.getPassword().equals(memberForm.getPasswordCheck())) {
            result.rejectValue("passwordCheck", "passwordInCorrect",
                    "비밀번호가 일치하지 않습니다.");
            return "members/createMemberForm";
        }
        Member member = new Member();
        member.setName(memberForm.getName());
        member.setUserId(memberForm.getUserId());
        member.setPassword(passwordEncoder.encode(memberForm.getPassword()));
        member.setPhoneNum(memberForm.getPhoneNum());
        member.setEmail(memberForm.getEmail());
        member.setJoinDate(LocalDate.now());
        try {
            memberService.join(member);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            result.rejectValue("userId", "duplicateUserId", e.getMessage());
            return "members/createMemberForm";
        }
        return "members/loginForm";
    }

    // 로그인
    @GetMapping("/login")
    public String login() {
        return "members/loginForm";
    }

    // 마이페이지
    @GetMapping("/myPage")
    public String myPage(Model model, Principal principal) {
        Member member = memberService.findByUserId(principal.getName()).get(0);
        model.addAttribute("member", member);
        return "members/myPage";
    }

    // 회원 조회(관리자)
    @GetMapping("/list")
    public String memberList(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }

    // 회원 수정
    @GetMapping("/{id}/update")
    public String updateForm(@PathVariable("id") Long id, Model model) {
        Member member = memberService.findById(id);
        MemberForm memberForm = new MemberForm();
        memberForm.setId(id);
        memberForm.setName(member.getName());
        memberForm.setUserId(member.getUserId());
        memberForm.setEmail(member.getEmail());
        memberForm.setPhoneNum(member.getPhoneNum());
        model.addAttribute("memberForm", memberForm);
        return "/members/updateMemberForm";
    }

    @PostMapping("/{id}/update")
    public String updateItem(@ModelAttribute("memberForm") MemberForm memberForm) {
        memberService.updateMember(memberForm.getId(), memberForm.getUserId(), memberForm.getName(), memberForm.getPhoneNum(),
                memberForm.getEmail());
        return "redirect:/members/list";
    }

    // 회원 삭제
    @GetMapping("/{id}/delete")
    public String memberDelete(@PathVariable("id") Long id) {
        Member member = memberService.findById(id);
        memberService.deleteMember(member);
        return "redirect:/members/list";
    }
}
