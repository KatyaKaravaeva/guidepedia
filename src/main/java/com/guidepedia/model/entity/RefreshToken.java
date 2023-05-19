package com.guidepedia.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "refresh", schema = "public")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch= FetchType.EAGER)
    @JoinColumn(name = "userid", referencedColumnName = "id")
    private UserEntity user;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(name = "expirydate", nullable = false)
    private String expiryDate;

    //getters and setters

}