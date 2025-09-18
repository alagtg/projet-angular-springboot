package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.demo.entity.Vol;

import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

@Repository
public interface VolRepository extends JpaRepository<Vol, Long> {

    List<Vol> findByVilleDepartAndVilleArrivee(String villeDepart, String villeArrivee);

    // 🔒 Verrouillage pessimiste (optionnel)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT v FROM Vol v WHERE v.id = :id")
    Optional<Vol> lockById(Long id);
}
