package com.zerowaste.zwb.repository;

import com.zerowaste.zwb.entity.WasteEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface WasteRepository extends CrudRepository<WasteEntity, UUID> {

    @Query("from WasteEntity w where w.codeNum = :codeNum")
    WasteEntity findWasteByCode(String codeNum);

    @Query("from WasteEntity w where w.codeName = :codeName")
    WasteEntity findWasteByCodeName(String codeName);

}
