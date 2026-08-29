package com.sinchonton.backend.service;

import com.sinchonton.backend.domain.partner.Partner;
import com.sinchonton.backend.domain.partner.PartnerRepository;
import com.sinchonton.backend.dto.partner.PartnerDetailResponse;
import com.sinchonton.backend.dto.partner.PartnerFeaturedResponse;
import com.sinchonton.backend.dto.partner.PartnerSummaryResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class PartnerService {

    private final PartnerRepository partnerRepository;

    public PartnerFeaturedResponse getFeaturedPartner() {
        Partner partner = partnerRepository.findTopByOrderByOccupiedUserCountDesc()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Partner not found"));
        return PartnerFeaturedResponse.from(partner);
    }

    public List<PartnerSummaryResponse> getPartners() {
        return partnerRepository.findAll()
                .stream()
                .map(PartnerSummaryResponse::from)
                .toList();
    }

    public PartnerDetailResponse getPartner(Long id) {
        Partner partner = partnerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Partner not found"));
        return PartnerDetailResponse.from(partner);
    }
}
