package com.sinchonton.backend.dto.partner;

import com.sinchonton.backend.domain.partner.Partner;

public record PartnerSummaryResponse(
        Long id,
        String name,
        String category,
        String address,
        Integer discountRate,
        String imageUrl,
        String naverMapUrl,
        String occupiedSchoolName,
        String occupiedCollegeName,
        String occupationMessage,
        Integer occupiedUserCount
) {

    public static PartnerSummaryResponse from(Partner partner) {
        return new PartnerSummaryResponse(
                partner.getId(),
                partner.getName(),
                partner.getCategory(),
                partner.getAddress(),
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
