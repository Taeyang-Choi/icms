package com.ogp.icms.cctv.dao;

import com.ogp.icms.cctv.domain.CameraError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
public interface CameraErrorRepository extends JpaRepository<CameraError, Long> {

    Optional<CameraError> findByCctvId(Long cctvId);

    List<CameraError> findByDataNot(String s);
}
