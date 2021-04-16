package com.zerowaste.zwb.repositories;

import com.zerowaste.zwb.entities.MaterialCodesEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface MaterialCodesRepository extends CrudRepository<MaterialCodesEntity, UUID> {
}
