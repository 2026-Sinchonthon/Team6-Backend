package com.sinchonton.backend.dto.partner;

import com.sinchonton.backend.domain.partner.Partner;

public record PartnerDetailResponse(
        Long id,
        String name,
        String category,
        String address,
        String description,
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

    public static PartnerDetailResponse from(Partner partner) {
        return new PartnerDetailResponse(
                partner.getId(),
                partner.getName(),
                partner.getCategory(),
                partner.getAddress(),
                partner.getDescription(),
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
