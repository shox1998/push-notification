package uz.kibera.project.dao;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@MappedSuperclass
@EntityListeners( AuditingEntityListener.class )
public abstract class AbstractBaseEntity<E> implements Persistable<E>, Serializable {

    private transient boolean isNew = false;

    @Override
    public boolean isNew() {
        return isNew;
    }

    private boolean deleted = false;

    @EqualsAndHashCode.Exclude
    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime created;

    @EqualsAndHashCode.Exclude
    @LastModifiedDate
    @Column(name = "modified_at", nullable = false)
    private LocalDateTime modified;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
