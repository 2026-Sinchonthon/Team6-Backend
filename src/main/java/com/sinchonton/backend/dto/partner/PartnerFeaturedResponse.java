package com.sinchonton.backend.dto.partner;

import com.sinchonton.backend.domain.partner.Partner;

public record PartnerFeaturedResponse(
        Long partnerId,
        String partnerName,
        String mapUrl,
        String naverMapUrl,
        String benefitTitle,
        String statusMessage,
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
                partner.getNaverMapUrl(),
                partner.getBenefitTitle(),
                partner.getStatusMessage(),
                partner.getOccupiedSchoolName(),
                partner.getOccupiedCollegeName(),
                partner.getStatusMessage(),
                partner.getOccupiedUserCount(),
                partner.getDiscountRate()
        );
    }
}
