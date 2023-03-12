package com.project.book.common.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@DynamicInsert
@DynamicUpdate
@MappedSuperclass
@SuperBuilder
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @CreatedDate
    @Column(name = "created_at", columnDefinition = "DATETIME", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "delete_yn")
    @Enumerated(EnumType.STRING)
    private EntityStatus.DeleteYn deleteYn;

    public void changeDeleteYn(EntityStatus.DeleteYn s) {
        this.deleteYn = s;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.deleteYn = this.deleteYn == null ? EntityStatus.DeleteYn.N : this.deleteYn;
    }
}
