package com.sinchonton.backend.dto.partner;

import com.sinchonton.backend.domain.partner.Partner;

public record PartnerFeaturedResponse(
        Long partnerId,
        String partnerName,
        String category,
        String categoryCode,
        String imageUrl,
        String image,
        String mapUrl,
        String naverMapUrl,
        String benefitTitle,
        String statusMessage,
        String discount,
        String status,
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
                partner.getCategory(),
                toCategoryCode(partner.getCategory()),
                partner.getImageUrl(),
                partner.getImageUrl(),
                partner.getNaverMapUrl(),
                partner.getNaverMapUrl(),
                partner.getBenefitTitle(),
                partner.getStatusMessage(),
                partner.getBenefitTitle(),
                partner.getStatusMessage(),
                partner.getOccupiedSchoolName(),
                partner.getOccupiedCollegeName(),
                partner.getStatusMessage(),
                partner.getOccupiedUserCount(),
                partner.getDiscountRate()
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
