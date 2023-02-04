package com.zerowaste.zwb.repository;

import com.zerowaste.zwb.entity.WasteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WasteRepository extends JpaRepository<WasteEntity, UUID> {

    List<WasteEntity> findAllByCodeNumOrCodeName(String codeNum, String codeName);
}
