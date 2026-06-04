package com.sheashepherd.ghostnet.repository;

import com.sheashepherd.ghostnet.model.Geisternetz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeisternetzDatenquelle extends JpaRepository<Geisternetz, Long> {
}