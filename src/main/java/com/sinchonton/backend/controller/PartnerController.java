package com.sinchonton.backend.controller;

import com.sinchonton.backend.dto.partner.PartnerDetailResponse;
import com.sinchonton.backend.dto.partner.PartnerFeaturedResponse;
import com.sinchonton.backend.dto.partner.PartnerSummaryResponse;
import com.sinchonton.backend.global.common.response.ApiResponse;
import com.sinchonton.backend.service.PartnerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/partners")
@Tag(name = "Partner", description = "제휴 탭에서 사용하는 스터디카페/식당/카페 목업 조회 API")
public class PartnerController {

    private final PartnerService partnerService;

    @GetMapping("/featured")
    @Operation(summary = "대표 제휴처 조회", description = "제휴 탭 상단 등에 노출할 대표 제휴처와 점령 문구를 조회합니다.")
    public ApiResponse<PartnerFeaturedResponse> getFeaturedPartner() {
        return ApiResponse.success(partnerService.getFeaturedPartner());
    }

    @GetMapping
    @Operation(summary = "제휴처 목록 조회", description = "제휴처 카드 목록에 사용할 이름, 주소, 이미지, 혜택 문구, 상태 문구, 지도 URL을 조회합니다.")
    public ApiResponse<List<PartnerSummaryResponse>> getPartners() {
        return ApiResponse.success(partnerService.getPartners());
    }

    @GetMapping("/{id}")
    @Operation(summary = "제휴처 상세 조회", description = "선택한 제휴처의 상세 설명, 주소, 혜택 문구, 지도 URL, 상태 문구를 조회합니다.")
    public ApiResponse<PartnerDetailResponse> getPartner(@PathVariable Long id) {
        return ApiResponse.success(partnerService.getPartner(id));
    }
}
