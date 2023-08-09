package com.ogp.icms.cctv.dao;

import com.ogp.icms.cctv.domain.Camera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CameraRepository extends JpaRepository<Camera, Long> {

    /**
     * VMS 카메라 벌크 삭제
     */

    @Modifying
    @Query("delete from Camera c")
    public void vmsCameraBulkDelete();

    Optional<Camera> findByCctvIndex(String workCctv);
}
