package study.datajpa.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item implements Persistable<String> {

    //SimpleJpaRepository의 save() 참고
    //JPA 식별자 생성 전략이 @GeneratedValue면 save() 호출 시점에 식별자 값이 없으므로 새로운 엔티티로 인식하고 정상 동작한다.
    //만약 @Id만 사용하고 직접 할당이면 이미 식별자 값이 있는 상태로 save()를 호출한다. 따라서 이 경우 merge()가 호출된다.
    //merge()는 우선 DB를 호출해서 값을 확인하고, 값이 없으면 새로운 엔티티로 인지하므로 매우 비효율적이다.
    //따라서 Persistable을 사용해서 새로운 엔티티 확인 여부를 직접 구현하는게 효과적이다. 이 때 등록 시간(createdDate)을 조합해서 사용하면 편리하다.

    @Id
    private String id;

    @CreatedDate
    private LocalDateTime createdDate;

    public Item(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return createdDate == null;
    }
}
