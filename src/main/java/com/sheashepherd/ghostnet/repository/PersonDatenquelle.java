package com.sheashepherd.ghostnet.repository;

import com.sheashepherd.ghostnet.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonDatenquelle extends JpaRepository<Person, Long> {
}