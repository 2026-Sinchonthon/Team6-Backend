package com.sinchonton.backend.config;

import com.sinchonton.backend.domain.partner.Partner;
import com.sinchonton.backend.domain.partner.PartnerRepository;
import com.sinchonton.backend.domain.school.entity.School;
import com.sinchonton.backend.domain.school.repository.SchoolRepository;
import com.sinchonton.backend.domain.schoolstat.SchoolStudyStat;
import com.sinchonton.backend.domain.schoolstat.SchoolStudyStatRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SampleDataInitializer {

    private final SchoolRepository schoolRepository;
    private final SchoolStudyStatRepository schoolStudyStatRepository;
    private final PartnerRepository partnerRepository;

    @Bean
    CommandLineRunner initializeSampleData() {
        return args -> {
            School yonsei = getOrCreateSchool("연세대학교");
            School sogang = getOrCreateSchool("서강대학교");
            School ewha = getOrCreateSchool("이화여자대학교");
            School myongji = getOrCreateSchool("명지대학교");
            School hongik = getOrCreateSchool("홍익대학교");

            if (schoolStudyStatRepository.count() == 0) {
                schoolStudyStatRepository.save(new SchoolStudyStat(yonsei.getId(), 37.5658, 126.9386, 12840L, 72));
                schoolStudyStatRepository.save(new SchoolStudyStat(sogang.getId(), 37.5512, 126.9409, 14620L, 81));
                schoolStudyStatRepository.save(new SchoolStudyStat(ewha.getId(), 37.5618, 126.9468, 16890L, 94));
                schoolStudyStatRepository.save(new SchoolStudyStat(myongji.getId(), 37.5802, 126.9231, 7640L, 43));
                schoolStudyStatRepository.save(new SchoolStudyStat(hongik.getId(), 37.5515, 126.9250, 10370L, 59));
            }

            List<String> samplePartnerNames = List.of(
                    "신촌 몰입 스터디카페",
                    "이대 앞 집중 라운지",
                    "홍대 밤샘 스터디룸",
                    "작심스터디카페 신촌숲길점",
                    "독수리다방",
                    "미분당 신촌본점"
            );
            partnerRepository.deleteAll(partnerRepository.findAllByNameIn(samplePartnerNames));

            if (partnerRepository.findAllByNameIn(samplePartnerNames).isEmpty()) {
                partnerRepository.save(new Partner(
                        "작심스터디카페 신촌숲길점",
                        "스터디카페",
                        "서울특별시 마포구 노고산동 56-74",
                        "시간권 구매 시 20% 할인을 제공하는 제휴 스터디카페입니다.",
                        20,
                        "시간권 20% 할인",
                        "https://images.unsplash.com/photo-1497366754035-f200968a6e72",
                        "https://kko.to/F5KDJL1he2",
                        "현재 홍익대 15명 열공 중",
                        "홍익대학교",
                        null,
                        15
                ));
                partnerRepository.save(new Partner(
                        "독수리다방",
                        "식당/카페",
                        "서울특별시 서대문구 명물길 36 8층",
                        "1위 학교 학생에게 음료 15% 혜택을 제공하는 제휴 카페입니다.",
                        15,
                        "1위 학교 음료 15%",
                        "https://images.unsplash.com/photo-1524758631624-e2822e304c36",
                        "https://kko.to/s-46CSdkKC",
                        "현재 신촌 대학생 42명 집중 중",
                        "신촌",
                        "대학생",
                        42
                ));
                partnerRepository.save(new Partner(
                        "미분당 신촌본점",
                        "식당/카페",
                        "서울특별시 서대문구 연세로5길 26-7 1층",
                        "Sin:Time 유저라면 누구나 결제 금액 5% 할인을 받을 수 있는 제휴 매장입니다.",
                        5,
                        "결제 금액 5% 할인",
                        "https://images.unsplash.com/photo-1517502884422-41eaead166d4",
                        "https://kko.to/1ns4xGtAjx",
                        "Sin:Time 유저 누구나 상시 혜택",
                        null,
                        null,
                        0
                ));
            }
        };
    }

    private School getOrCreateSchool(String name) {
        return schoolRepository.findByName(name)
                .orElseGet(() -> schoolRepository.save(new School(name)));
    }
}
