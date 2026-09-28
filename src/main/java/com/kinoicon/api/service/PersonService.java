package com.kinoicon.api.service;

import com.kinoicon.api.model.enums.SizeType;
import com.kinoicon.api.model.request.PersonRequest;

public interface PersonService {
    Object getById(long id, SizeType size, boolean authenticated);
    long create();
    void update(long id, PersonRequest request);
    void delete(long id);
}
