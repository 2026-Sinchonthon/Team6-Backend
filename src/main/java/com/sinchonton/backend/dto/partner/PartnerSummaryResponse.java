package com.sinchonton.backend.dto.partner;

import com.sinchonton.backend.domain.partner.Partner;

public record PartnerSummaryResponse(
        Long id,
        String name,
        String category,
        String address,
        Integer discountRate,
        String benefitTitle,
        String imageUrl,
        String mapUrl,
        String naverMapUrl,
        String statusMessage,
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
                partner.getBenefitTitle(),
                partner.getImageUrl(),
                partner.getNaverMapUrl(),
                partner.getNaverMapUrl(),
                partner.getStatusMessage(),
                partner.getOccupiedSchoolName(),
                partner.getOccupiedCollegeName(),
                partner.getStatusMessage(),
                partner.getOccupiedUserCount()
        );
    }
}
