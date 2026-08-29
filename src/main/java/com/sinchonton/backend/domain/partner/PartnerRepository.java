package com.sinchonton.backend.domain.partner;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartnerRepository extends JpaRepository<Partner, Long> {

    List<Partner> findAllByNameIn(Collection<String> names);

    Optional<Partner> findTopByOrderByOccupiedUserCountDesc();
}
