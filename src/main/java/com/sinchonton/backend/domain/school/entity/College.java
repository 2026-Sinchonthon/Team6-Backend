package com.sinchonton.backend.domain.school.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/** 단과대. {@link School} 소속. 같은 학교 안에서 이름이 중복되지 않습니다. */
@Entity
@Table(name = "colleges", uniqueConstraints = @UniqueConstraint(columnNames = {"school_id", "name"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class College {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long schoolId;

    @Column(nullable = false)
    private String name;

    public College(Long schoolId, String name) {
        this.schoolId = schoolId;
        this.name = name;
    }
}
