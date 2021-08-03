package uz.kibera.project.dao.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import uz.kibera.project.dao.AbstractBaseEntity;
import uz.kibera.project.dao.entity.types.NoticeType;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "notices")
public class Notice extends AbstractBaseEntity<UUID> {
    @Id
    private UUID id;

    private String title;

    @Lob
    @Column(columnDefinition = "text")
    private String content;

    @Column(name = "type")
    private NoticeType noticeType;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "from_date")
    private LocalDateTime fromDate;

    @Column(name = "to_date")
    private LocalDateTime toDate;

}
