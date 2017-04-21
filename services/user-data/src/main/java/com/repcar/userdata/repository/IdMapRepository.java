package com.repcar.userdata.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.repcar.userdata.domain.IdMap;

/**
 * @author <a href="mailto:imishev@repcarpro.com">Ivan Mishev</a>
 */
public interface IdMapRepository extends JpaRepository<IdMap, Long> {

    IdMap findByWeakId(String weakId);

}
