package com.example.prodajaKnjigaBackend.chatRoom.domain;
import com.example.prodajaKnjigaBackend.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "chat_room")
public class ChatRoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user1_id", nullable = false)
    private UserEntity user1;

    @ManyToOne
    @JoinColumn(name = "user2_id", nullable = false)
    private UserEntity user2;
}
