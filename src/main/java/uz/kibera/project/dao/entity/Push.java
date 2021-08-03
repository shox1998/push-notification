package uz.kibera.project.dao.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import uz.kibera.project.dao.AbstractBaseEntity;
import uz.kibera.project.dao.entity.types.PushType;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "pushes")
public class Push extends AbstractBaseEntity<UUID> {
    @Id
    private UUID id;

    private String title;

    @Column(name = "type")
    private PushType pushType;

    @Lob
    @Column(columnDefinition = "text")
    private String content;

}
