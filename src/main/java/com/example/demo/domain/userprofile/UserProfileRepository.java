package com.example.demo.domain.userprofile;

import com.example.demo.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * UserProfileRepository - Datenzugriff für UserProfile
 *
 * Komponenten:
 * - Spring Data JPA Repository mit CRUD-Operationen
 * - Custom JPQL Queries für komplexe Suchfunktionen
 * - Pagination Support für Admin-Funktionen
 */
@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {

    Optional<UserProfile> findByUser(User user);

    Optional<UserProfile> findByUserId(UUID userId);

    boolean existsByUserId(UUID userId);

    /**
     * Erweiterte Filtersuche für Admin-Funktionen
     * Kombiniert mehrere optionale Filter mit AND-Verknüpfung
     * NULL-Parameter werden ignoriert (dynamische WHERE-Klausel)
     */
    @Query("SELECT up FROM UserProfile up WHERE " +
            "(:address IS NULL OR LOWER(up.address) LIKE LOWER(CONCAT('%', :address, '%'))) AND " +
            "(:minAge IS NULL OR up.age >= :minAge) AND " +
            "(:maxAge IS NULL OR up.age <= :maxAge)")
    Page<UserProfile> findProfilesWithFilters(
            @Param("address") String address,
            @Param("minAge") Integer minAge,
            @Param("maxAge") Integer maxAge,
            Pageable pageable
    );

    /**
     * Volltext-Suche über User- und Profil-Felder
     * Durchsucht firstName, lastName, email und address mit OR-Verknüpfung
     * Case-insensitive durch LOWER()-Funktionen
     */
    @Query("SELECT up FROM UserProfile up WHERE " +
            "LOWER(up.user.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(up.user.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(up.user.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(up.address) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<UserProfile> searchProfiles(@Param("searchTerm") String searchTerm, Pageable pageable);
}