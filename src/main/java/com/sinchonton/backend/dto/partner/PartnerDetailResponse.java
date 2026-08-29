package com.sinchonton.backend.dto.partner;

import com.sinchonton.backend.domain.partner.Partner;

public record PartnerDetailResponse(
        Long id,
        String name,
        String category,
        String categoryCode,
        String address,
        String description,
        Integer discountRate,
        String benefitTitle,
        String imageUrl,
        String image,
        String mapUrl,
        String naverMapUrl,
        String statusMessage,
        String discount,
        String status,
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
                toCategoryCode(partner.getCategory()),
                partner.getAddress(),
                partner.getDescription(),
                partner.getDiscountRate(),
                partner.getBenefitTitle(),
                partner.getImageUrl(),
                partner.getImageUrl(),
                partner.getNaverMapUrl(),
                partner.getNaverMapUrl(),
                partner.getStatusMessage(),
                partner.getBenefitTitle(),
                partner.getStatusMessage(),
                partner.getOccupiedSchoolName(),
                partner.getOccupiedCollegeName(),
                partner.getStatusMessage(),
                partner.getOccupiedUserCount()
        );
    }

    private static String toCategoryCode(String category) {
        return switch (category) {
            case "스터디카페" -> "study-cafe";
            case "식당/카페" -> "food-cafe";
            case "프린트" -> "print";
            default -> "all";
        };
    }
}
