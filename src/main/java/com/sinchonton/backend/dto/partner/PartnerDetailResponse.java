package com.sinchonton.backend.dto.partner;

import com.sinchonton.backend.domain.partner.Partner;

public record PartnerDetailResponse(
        Long id,
        String name,
        String category,
        String address,
        String description,
        Integer discountRate,
        String imageUrl,
        String naverMapUrl,
        String occupiedSchoolName,
        String occupiedCollegeName,
        String occupationMessage,
        Integer occupiedUserCount
) {

    public static PartnerDetailResponse from(Partner partner) {
        return new PartnerDetailResponse(
                partner.getId(),
                partner.getName(),
                partner.getCategory(),
                partner.getAddress(),
                partner.getDescription(),
                partner.getDiscountRate(),
                partner.getImageUrl(),
                partner.getNaverMapUrl(),
                partner.getOccupiedSchoolName(),
                partner.getOccupiedCollegeName(),
                partner.getOccupiedSchoolName() + " " + partner.getOccupiedCollegeName() + "이 점령중",
                partner.getOccupiedUserCount()
        );
    }
}
