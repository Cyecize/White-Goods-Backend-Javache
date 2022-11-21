package com.cyecize.app.api.user.address;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "user_addresses")
@Getter
@Setter
@ToString
public class UserAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String fullName;

    private String email;

    private String phoneNumber;

    private String country;

    private String city;

    private String addressLine;

    private String notes;

    private Boolean preferredAddress;
}
