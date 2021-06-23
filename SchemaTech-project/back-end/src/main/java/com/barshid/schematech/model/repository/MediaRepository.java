package com.barshid.schematech.model.repository;

import com.barshid.schematech.model.bo.Media;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaRepository extends CrudRepository<Media,Long> {

}
