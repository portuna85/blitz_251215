package com.blitz.springboot.domain.user;

import com.blitz.springboot.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * 사용자 엔티티
 * SRP: 사용자 도메인 로직과 데이터를 관리
 * OCP: 확장 가능하도록 설계
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String picture;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Builder
    public User(String name, String email, String picture, Role role) {
        validateName(name);
        validateEmail(email);
        validateRole(role);

        this.name = name;
        this.email = email;
        this.picture = picture;
        this.role = role;
    }

    /**
     * 사용자 정보 업데이트 (이름, 프로필 사진)
     *
     * @param name 변경할 이름
     * @param picture 변경할 프로필 사진 URL
     * @return 업데이트된 User 인스턴스
     */
    public User update(String name, String picture) {
        validateName(name);

        this.name = name;
        this.picture = picture;

        return this;
    }

    /**
     * 사용자의 권한 키 조회
     *
     * @return 권한 키 (예: "ROLE_USER")
     */
    public String getRoleKey() {
        return this.role.getKey();
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("이름은 필수입니다");
        }
    }

    private void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("이메일은 필수입니다");
        }
        // 간단한 이메일 형식 검증
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("올바른 이메일 형식이 아닙니다");
        }
    }

    private void validateRole(Role role) {
        if (role == null) {
            throw new IllegalArgumentException("권한은 필수입니다");
        }
    }
}
