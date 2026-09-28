package com.kinoicon.api.service.impl;

import com.kinoicon.api.exception.BaseException;
import com.kinoicon.api.model.entity.FeaturedEntity;
import com.kinoicon.api.model.entity.FeaturedRowEntity;
import com.kinoicon.api.model.entity.FeaturedRowItemEntity;
import com.kinoicon.api.model.entity.FilmEntity;
import com.kinoicon.api.model.entity.PersonEntity;
import com.kinoicon.api.model.mapper.FeaturedMapper;
import com.kinoicon.api.model.request.FeaturedItemRequest;
import com.kinoicon.api.model.request.FeaturedRequest;
import com.kinoicon.api.model.request.FeaturedRowRequest;
import com.kinoicon.api.model.response.FeaturedItemResponse;
import com.kinoicon.api.model.response.FeaturedResponse;
import com.kinoicon.api.model.response.FeaturedRowResponse;
import com.kinoicon.api.repository.FeaturedRepository;
import com.kinoicon.api.repository.FilmRepository;
import com.kinoicon.api.repository.PersonRepository;
import com.kinoicon.api.service.FeaturedService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Featured is a singleton (mirrors Featured.objects.first()). Each row item references a
 * film or person only by short_id + kind, so building the response requires resolving the
 * referenced entity's uuid; this is done here, in the service, with a couple of batched
 * lookups rather than one lookup per item.
 */
@Service
@RequiredArgsConstructor
public class FeaturedServiceImpl implements FeaturedService {

    private final FeaturedRepository featuredRepository;
    private final FilmRepository filmRepository;
    private final PersonRepository personRepository;

    @Override
    public FeaturedResponse get() {
        FeaturedEntity entity = featuredRepository.find().orElseThrow(BaseException::featuredNotFound);

        List<FeaturedRowEntity> rows = entity.getRows() == null ? List.of() : entity.getRows();

        List<Long> filmIds = rows.stream().flatMap(r -> safeItems(r).stream())
                .filter(i -> "film".equals(i.getItemKind())).map(FeaturedRowItemEntity::getItemShortId).distinct().toList();
        List<Long> personIds = rows.stream().flatMap(r -> safeItems(r).stream())
                .filter(i -> "person".equals(i.getItemKind())).map(FeaturedRowItemEntity::getItemShortId).distinct().toList();

        Map<Long, String> filmUuids = filmRepository.findByIds(filmIds).stream()
                .collect(Collectors.toMap(FilmEntity::getId, f -> f.getUuid().toString()));
        Map<Long, String> personUuids = personRepository.findByIds(personIds).stream()
                .collect(Collectors.toMap(PersonEntity::getId, p -> p.getUuid().toString()));

        List<FeaturedRowResponse> rowResponses = rows.stream().map(row -> {
            List<FeaturedItemResponse> items = safeItems(row).stream().map(item -> {
                String uuid = "film".equals(item.getItemKind())
                        ? filmUuids.get(item.getItemShortId())
                        : personUuids.get(item.getItemShortId());
                return FeaturedMapper.toItemResponse(item, uuid);
            }).toList();
            return FeaturedMapper.toRowResponse(row, items);
        }).toList();

        return FeaturedMapper.toResponse(entity, rowResponses);
    }

    @Override
    public void update(FeaturedRequest request) {
        FeaturedEntity existing = featuredRepository.find().orElseThrow(BaseException::featuredNotFound);

        FeaturedEntity toSave = new FeaturedEntity();
        toSave.setId(existing.getId());
        toSave.setTitle(request.title() != null ? request.title() : existing.getTitle());

        if (request.featured() != null) {
            toSave.setRows(request.featured().stream().map(this::toRowEntity).toList());
        } else {
            toSave.setRows(existing.getRows());
        }

        featuredRepository.replace(toSave);
    }

    private FeaturedRowEntity toRowEntity(FeaturedRowRequest rowRequest) {
        FeaturedRowEntity row = new FeaturedRowEntity();
        row.setKind(rowRequest.kind());
        row.setTitle(rowRequest.title());
        row.setItems(rowRequest.items() == null ? List.of() : rowRequest.items().stream().map(this::toItemEntity).toList());
        return row;
    }

    private FeaturedRowItemEntity toItemEntity(FeaturedItemRequest itemRequest) {
        FeaturedRowItemEntity item = new FeaturedRowItemEntity();
        item.setItemKind(itemRequest.kind());
        item.setItemShortId(itemRequest.short_id());
        return item;
    }

    private List<FeaturedRowItemEntity> safeItems(FeaturedRowEntity row) {
        return row.getItems() == null ? List.of() : row.getItems();
    }
}
