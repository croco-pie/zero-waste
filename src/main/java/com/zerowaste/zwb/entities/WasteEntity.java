package com.zerowaste.zwb.entities;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
@Table(name = "waste", schema = "zero_waste")
public class WasteEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    private String codeNum;

    private String codeName;

    private UUID codeId;

    private String codeDescription;
}
