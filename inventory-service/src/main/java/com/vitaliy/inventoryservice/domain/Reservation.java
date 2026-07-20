package com.vitaliy.inventoryservice.domain;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "reservations",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_reservation_id",
                        columnNames = "reservation_id"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(
            name = "reservation_id",
            nullable = false,
            unique = true
    )
    private String reservationId;


    private String productId;

    private Integer quantity;

    private Instant createdAt;

    @Column(nullable = false)
    @Builder.Default
    private boolean released = false;
}