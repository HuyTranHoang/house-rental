package com.project.house.rental.service;

import com.project.house.rental.dto.PropertyImageDto;

import java.io.IOException;
import java.util.List;

public interface PropertyImageService {
    List<PropertyImageDto> findByPropertyId(long id);

    void deleteByPropertyId(long id) throws IOException;
}
