package com.sinchonton.backend.domain.user.entity;

import com.sinchonton.backend.global.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    @Column(nullable = false, unique = true)
    private String socialId;

    @Column(nullable = false)
    private String nickname;

    private String profileImage;

    /** 학교 인증 전에는 null. 온보딩에서 채워집니다. */
    private String school;

    /** 학과. 학교와 함께 온보딩에서 채워집니다. */
    private String department;

    @Builder(access = AccessLevel.PRIVATE)
    private User(String email, String socialId, String nickname, String profileImage) {
        this.email = email;
        this.socialId = socialId;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

    public static User create(String email, String socialId, String nickname, String profileImage) {
        return User.builder()
                .email(email)
                .socialId(socialId)
                .nickname(nickname)
                .profileImage(profileImage)
                .build();
    }

    /**
     * 온보딩에서 학교 · 학과를 설정합니다.
     * 값이 유효한 학교/학과인지는 서비스 계층에서 검증합니다.
     */
    public void updateSchool(String school, String department) {
        this.school = school;
        this.department = department;
    }

    /** 온보딩(학교 인증 · 학과 선택)을 마쳤는지 여부. */
    public boolean hasCompletedOnboarding() {
        return school != null && department != null;
    }
}
