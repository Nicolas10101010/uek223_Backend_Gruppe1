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

@Entity
@Table(name = "user_profile")
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class UserProfile extends AbstractEntity {

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

    public boolean isOwnedBy(User currentUser) {
        return this.user != null && this.user.getId().equals(currentUser.getId());
    }
}