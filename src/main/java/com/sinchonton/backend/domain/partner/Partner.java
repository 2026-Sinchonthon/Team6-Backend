package com.sinchonton.backend.domain.partner;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Partner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String category;

    private String address;

    private String description;

    private Integer discountRate;

    private String benefitTitle;

    private String imageUrl;

    private String naverMapUrl;

    private String statusMessage;

    private String occupiedSchoolName;

    private String occupiedCollegeName;

    private Integer occupiedUserCount;

    public Partner(
            String name,
            String category,
            String address,
            String description,
            Integer discountRate,
            String benefitTitle,
            String imageUrl,
            String naverMapUrl,
            String statusMessage,
            String occupiedSchoolName,
            String occupiedCollegeName,
            Integer occupiedUserCount
    ) {
        this.name = name;
        this.category = category;
        this.address = address;
        this.description = description;
        this.discountRate = discountRate;
        this.benefitTitle = benefitTitle;
        this.imageUrl = imageUrl;
        this.naverMapUrl = naverMapUrl;
        this.statusMessage = statusMessage;
        this.occupiedSchoolName = occupiedSchoolName;
        this.occupiedCollegeName = occupiedCollegeName;
        this.occupiedUserCount = occupiedUserCount;
    }
}
