package com.kibikalo.pastebin.repository;

import com.kibikalo.pastebin.model.Paste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PasteRepository extends JpaRepository<Paste, Long> {

    @Query(value = "SELECT * FROM paste WHERE hash = :hash", nativeQuery = true)
    Paste findByHash(@Param("hash") String hash);
}
