package com.kinoicon.api.service;

import com.kinoicon.api.model.enums.SizeType;
import com.kinoicon.api.model.request.FilmRequest;

public interface FilmService {
    Object getById(long id, SizeType size, boolean authenticated);
    long create();
    void update(long id, FilmRequest request);
    void delete(long id);
}
