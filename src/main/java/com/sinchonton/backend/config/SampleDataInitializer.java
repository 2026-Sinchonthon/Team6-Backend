package com.sinchonton.backend.config;

import com.sinchonton.backend.domain.partner.Partner;
import com.sinchonton.backend.domain.partner.PartnerRepository;
import com.sinchonton.backend.domain.school.entity.School;
import com.sinchonton.backend.domain.school.repository.SchoolRepository;
import com.sinchonton.backend.domain.schoolstat.SchoolStudyStat;
import com.sinchonton.backend.domain.schoolstat.SchoolStudyStatRepository;
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

            if (partnerRepository.count() == 0) {
                partnerRepository.save(new Partner(
                        "신촌 몰입 스터디카페",
                        "스터디카페",
                        "서울 서대문구 연세로 12",
                        "신촌역과 가까운 조용한 집중형 스터디카페입니다.",
                        10,
                        "https://images.unsplash.com/photo-1497366754035-f200968a6e72",
                        "https://map.naver.com/p/search/%EC%8B%A0%EC%B4%8C%20%EC%8A%A4%ED%84%B0%EB%94%94%EC%B9%B4%ED%8E%98",
                        "연세대학교",
                        "공과대학",
                        82
                ));
                partnerRepository.save(new Partner(
                        "이대 앞 집중 라운지",
                        "스터디라운지",
                        "서울 서대문구 이화여대길 34",
                        "조용한 좌석과 팀플룸을 함께 제공하는 제휴 공간입니다.",
                        8,
                        "https://images.unsplash.com/photo-1524758631624-e2822e304c36",
                        "https://map.naver.com/p/search/%EC%9D%B4%EB%8C%80%20%EC%8A%A4%ED%84%B0%EB%94%94%EB%9D%BC%EC%9A%B4%EC%A7%80",
                        "이화여자대학교",
                        "인문과학대학",
                        71
                ));
                partnerRepository.save(new Partner(
                        "홍대 밤샘 스터디룸",
                        "스터디룸",
                        "서울 마포구 와우산로 21",
                        "늦은 시간까지 이용하기 좋은 팀 스터디룸입니다.",
                        7,
                        "https://images.unsplash.com/photo-1517502884422-41eaead166d4",
                        "https://map.naver.com/p/search/%ED%99%8D%EB%8C%80%20%EC%8A%A4%ED%84%B0%EB%94%94%EB%A3%B8",
                        "홍익대학교",
                        "공과대학",
                        58
                ));
            }
        };
    }

    private School getOrCreateSchool(String name) {
        return schoolRepository.findByName(name)
                .orElseGet(() -> schoolRepository.save(new School(name)));
    }
}
