package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> { //@Repository 생략 가능

    //https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation
    //https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-methods.query-creation
    //https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.limit-query-result

    //최신버전 https://docs.spring.io/spring-data/commons/docs/2.5.0/reference/html/#reference
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age); //파라미터가 많아지면 이름이 너무 길어짐

    //@Query(name = "Member.findByUsername") //@NamedQuery의 name, 관례를 따르면 생략 가능
    List<Member> findByUsername(@Param("username") String username); //@Query 파라미터 넣어줌

    @Query("select m from Member m where m.username = :username and m.age = :age") //이 방식이 장점이 많음, 이름이 없는 @NamedQuery(정적쿼리)와 같음, 문법 오류 잡을 수 있다, 권장!
    List<Member> findUser(@Param("username") String username, @Param("age") int age); //파라미터 바인딩은 이름 기반으로 하기

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") List<String> names);

    List<Member> findListByUsername(String username); //컬렉션
    Member findMemberByUsername(String username); //단건
    Optional<Member> findOptionalByUsername(String username); //단건 Optional

    // @Query(value = "select m from Member m left join m.team t", countQuery = "select count(m.username) from Member m") //쿼리가 복잡해지면 카운트 쿼리도 복잡해짐, 쿼리 분리해주기
    @Query(value = "select m from Member m")
    Page<Member> findByAge(int age, Pageable pageable); //Pageable은 인터페이스. 실제 사용할 때는 해당 인터페이스를 구현한 PageRequest를 사용함
    //Slice<Member> findByAge(int age, Pageable pageable); //Pageable은 인터페이스. 실제 사용할 때는 해당 인터페이스를 구현한 PageRequest를 사용함

    @Modifying(clearAutomatically = true) //꼭 Modifying 붙여줘야 함!, clear true로 설정하면 벌크 연산 후 자동으로 clear해줌!!
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    @Query("select m from Member m left join fetch m.team") //Member를 조회할 때 연관된 team을 함께 가져옴, team의 모든 값도 채움
    List<Member> findMemberFetchJoin();

    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    @EntityGraph(attributePaths = {"team"})
    List<Member> findEntityGraphByUsername(@Param("username") String username);

    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);
}
