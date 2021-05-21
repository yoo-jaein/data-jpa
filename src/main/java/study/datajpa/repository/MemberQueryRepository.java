package study.datajpa.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class MemberQueryRepository {

    private final EntityManager em;

    //복잡한 화면을 구성하기 위한 쿼리들은 항상 custom repository로 만들 필요가 없다.
    //새로운 클래스로 분리하고 스프링 빈으로 등록하여 사용해도 된다.
}
