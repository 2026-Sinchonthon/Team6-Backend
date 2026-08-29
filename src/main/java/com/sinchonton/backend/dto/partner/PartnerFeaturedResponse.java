package com.sinchonton.backend.dto.partner;

import com.sinchonton.backend.domain.partner.Partner;

public record PartnerFeaturedResponse(
        Long partnerId,
        String partnerName,
        String naverMapUrl,
        String occupiedSchoolName,
        String occupiedCollegeName,
        String occupationMessage,
        Integer occupiedUserCount,
        Integer discountRate
) {

    public static PartnerFeaturedResponse from(Partner partner) {
        return new PartnerFeaturedResponse(
                partner.getId(),
                partner.getName(),
                partner.getNaverMapUrl(),
                partner.getOccupiedSchoolName(),
                partner.getOccupiedCollegeName(),
                partner.getOccupiedSchoolName() + " " + partner.getOccupiedCollegeName() + "이 점령중",
                partner.getOccupiedUserCount(),
                partner.getDiscountRate()
        );
    }
}
