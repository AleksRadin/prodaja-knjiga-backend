package com.example.prodajaKnjigaBackend.user.domain;

import com.example.prodajaKnjigaBackend.chatMessage.domain.ChatMessageEntity;
import com.example.prodajaKnjigaBackend.chatRoom.domain.ChatRoomEntity;
import com.example.prodajaKnjigaBackend.favorite.domain.FavoriteEntity;
import com.example.prodajaKnjigaBackend.listing.domain.ListingEntity;
import com.example.prodajaKnjigaBackend.report.domain.ReportEntity;
import com.example.prodajaKnjigaBackend.review.domain.ReviewEntity;
import com.example.prodajaKnjigaBackend.user.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email", name = "UNIQUE_email")
})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "firstname", nullable = false, length = 50)
    private String firstname;

    @Column(name = "lastname", nullable = false, length = 50)
    private String lastname;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private UserRole role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.MERGE)
    @JsonIgnore
    private List<ListingEntity> listingEntities;

    @OneToMany(mappedBy = "reviewer")
    @JsonIgnore
    private List<ReviewEntity> reviewsGiven;

    @OneToMany(mappedBy = "reviewed")
    @JsonIgnore
    private List<ReviewEntity> reviewsReceived;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<ReportEntity> reports;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<FavoriteEntity> favorites;

    @OneToMany(mappedBy = "user1")
    @JsonIgnore
    private List<ChatRoomEntity> initiatedChats;

    @OneToMany(mappedBy = "user2")
    @JsonIgnore
    private List<ChatRoomEntity> joinedChats;

    @OneToMany(mappedBy = "sender")
    @JsonIgnore
    private List<ChatMessageEntity> sentMessages;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public String getUsername() {
        return email;
    }

}
