package com.sinchonton.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 임시 버전입니다. A가 User 엔티티를 완성하면 필드명을 맞춰서 병합해야 합니다.
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;

    private Long schoolId;
    private Long collegeId;     // 단과대
    private Long departmentId;  // 학과
}