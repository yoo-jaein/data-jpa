package study.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    private static MemberDto apply(Member member) {
        return new MemberDto(member.getId(), member.getUsername(), null);
    }

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id) {
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member) { //도메인 클래스 컨버터, 이 Member 엔티티는 조회용으로만 사용해야 한다!
        return member.getUsername();
    }

    @GetMapping("/members") //http://localhost:8080/members?page=0&size=5&sort=id,desc&sort=username,desc 파라미터
    public Page<MemberDto> list(@PageableDefault(size = 5, sort = "username") Pageable pageable) { //@PageableDefault로 default 개별 설정
        return memberRepository.findAll(pageable)
                .map(MemberDto::new);
    }

    //@PostConstruct //스프링 애플리케이션이 올라올 때 실행된다
    public void init() {
        for (int i = 0; i < 100; i++) {
            memberRepository.save(new Member("user" + i, i));
        }
    }
}
