package com.zerowaste.zwb.entity;

import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import com.zerowaste.zwb.enums.WasteTypeEnum;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "waste")
@TypeDef(name = "pgsql_enum", typeClass = PostgreSQLEnumType.class)
public class WasteEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    private Integer codeNum;

    private String codeName;

    private String codeDescription;

    @Enumerated(EnumType.STRING)
    @Type(type = "pgsql_enum")
    private WasteTypeEnum wasteType;

    private boolean isShown;
    private boolean isAddedByUsers;
}
