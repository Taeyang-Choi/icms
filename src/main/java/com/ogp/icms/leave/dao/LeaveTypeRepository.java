package com.ogp.icms.leave.dao;

import com.ogp.icms.leave.domain.LeaveType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveTypeRepository extends JpaRepository<LeaveType, Long>  {
}
