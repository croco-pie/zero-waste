package com.zerowaste.zwb.repository;

import com.zerowaste.zwb.entity.WasteEntity;
import com.zerowaste.zwb.enums.WasteTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WasteRepository extends JpaRepository<WasteEntity, UUID> {

    List<WasteEntity> findAllByCodeNumOrCodeName(Integer codeNum, String codeName);

    List<WasteEntity> findAllByCodeNum(Integer codeNum);

    List<WasteEntity> findAllByIsShownAndWasteTypeIs(boolean isShown, WasteTypeEnum type);

    List<WasteEntity> findAllByIsShown(boolean isShown);
}
