package com.example.optlock;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import java.util.UUID;

@Data
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table
public class SampleEntity {
    @Id
    @GeneratedValue
    @Column(/*name = "id", */nullable = false)
    private UUID id;

    @Version
    @Column(/*name = "opt_lock",*/ nullable = false)
    private short optLock;

    @Column(/*name = "value_a", */nullable = false)
    private String valueA;

    @Column(/*name = "limit_type_code", */nullable = false)
    private String valueB;


}
