package com.example.demo.domain.userprofile;

import com.example.demo.core.generic.AbstractEntity;
import com.example.demo.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.UUID;

/**
 * UserProfile Entity - Erweiterte Benutzerinformationen
 *
 * Diese Entity repräsentiert das Benutzerprofil mit zusätzlichen Informationen
 * wie Adresse, Geburtsdatum und Profilbild. Jeder User kann genau ein Profil haben.
 *
 * Komponenten:
 * - JPA Entity mit OneToOne-Beziehung zu User
 * - Lombok für Getter/Setter und Builder-Pattern
 * - Validierung über Bean Validation in DTO-Schicht
 */
@Entity
@Table(name = "user_profile")
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class UserProfile extends AbstractEntity {

    // Bidirektionale OneToOne-Beziehung zu User - jeder User hat maximal ein Profil
    @OneToOne
    @JoinColumn(name = "id_user", referencedColumnName = "id", nullable = false, unique = true)
    private User user;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "birthdate", nullable = false)
    private LocalDate birthdate;

    @Column(name = "profile_img_url")
    private String profileImgUrl;

    @Column(name = "age")
    private Integer age;

    public UserProfile(UUID id, User user, String address, LocalDate birthdate, String profileImgUrl, Integer age) {
        super(id);
        this.user = user;
        this.address = address;
        this.birthdate = birthdate;
        this.profileImgUrl = profileImgUrl;
        this.age = age;
    }

    /**
     * Prüft ob das Profil dem angegebenen User gehört
     * Wird für Autorisierungsprüfungen verwendet
     */
    public boolean isOwnedBy(User currentUser) {
        return this.user != null && this.user.getId().equals(currentUser.getId());
    }
}