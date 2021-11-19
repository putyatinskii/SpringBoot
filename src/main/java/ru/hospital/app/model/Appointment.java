package ru.hospital.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.UUID;

@Data
@Entity
@Table(name = "appointments")
@AllArgsConstructor
@NoArgsConstructor
public class Appointment {

    @Id
    @Column(name = "record_id")
    private UUID id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "record_id")
    private Record record;

    private String description;

    @Column(name = "is_sick")
    private Boolean isSick;

    @Column(name= "is_closed")
    private Boolean isClosed;

    @Column(name= "is_visit")
    private Boolean isVisited;
}
