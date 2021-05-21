package study.datajpa.repository;

import org.springframework.beans.factory.annotation.Value;

public interface UsernameOnly {

    //@Value("#{target.username + ' ' + target.age}") 오픈 프로젝션, 엔티티 전체 가져와서 뽑아 쓰는 것
    String getUsername();
}
