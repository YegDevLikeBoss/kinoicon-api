package com.kinoicon.api.repository.impl;

import com.kinoicon.api.model.entity.FeaturedEntity;
import com.kinoicon.api.model.entity.FeaturedRowEntity;
import com.kinoicon.api.model.entity.FeaturedRowItemEntity;
import com.kinoicon.api.model.rowmapper.FeaturedRowItemRowMapper;
import com.kinoicon.api.model.rowmapper.FeaturedRowRowMapper;
import com.kinoicon.api.repository.FeaturedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Featured is a singleton aggregate: one "featured" row plus its ordered "featured_row"
 * children, each with ordered "featured_row_item" children. Reads assemble the whole tree;
 * writes (PUT) replace the rows/items wholesale, since the API sends the full nested
 * structure on every update rather than incremental patches.
 */
@Repository
@RequiredArgsConstructor
public class FeaturedRepositoryImpl implements FeaturedRepository {

    private static final FeaturedRowRowMapper ROW_MAPPER = new FeaturedRowRowMapper();
    private static final FeaturedRowItemRowMapper ITEM_ROW_MAPPER = new FeaturedRowItemRowMapper();

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<FeaturedEntity> find() {
        List<Long> ids = jdbcTemplate.queryForList("SELECT id FROM featured ORDER BY id LIMIT 1", Long.class);
        if (ids.isEmpty()) {
            return Optional.empty();
        }
        long featuredId = ids.get(0);

        String title = jdbcTemplate.queryForObject("SELECT title FROM featured WHERE id = ?", String.class, featuredId);

        List<FeaturedRowEntity> rows = jdbcTemplate.query(
                "SELECT id, featured_id, position, kind, title FROM featured_row WHERE featured_id = ? ORDER BY position",
                ROW_MAPPER, featuredId);

        for (FeaturedRowEntity row : rows) {
            List<FeaturedRowItemEntity> items = jdbcTemplate.query(
                    "SELECT id, row_id, position, item_kind, item_short_id FROM featured_row_item WHERE row_id = ? ORDER BY position",
                    ITEM_ROW_MAPPER, row.getId());
            row.setItems(items);
        }

        FeaturedEntity entity = new FeaturedEntity();
        entity.setId(featuredId);
        entity.setTitle(title);
        entity.setRows(rows);
        return Optional.of(entity);
    }

    @Override
    @Transactional
    public void replace(FeaturedEntity entity) {
        jdbcTemplate.update("UPDATE featured SET title = ? WHERE id = ?", entity.getTitle(), entity.getId());

        jdbcTemplate.update("DELETE FROM featured_row WHERE featured_id = ?", entity.getId());
        if (entity.getRows() == null) {
            return;
        }
        int rowPosition = 0;
        for (FeaturedRowEntity row : entity.getRows()) {
            Long rowId = jdbcTemplate.queryForObject(
                    "INSERT INTO featured_row (featured_id, position, kind, title) VALUES (?, ?, ?, ?) RETURNING id",
                    Long.class, entity.getId(), rowPosition++, row.getKind(), row.getTitle());

            int itemPosition = 0;
            if (row.getItems() != null) {
                for (FeaturedRowItemEntity item : row.getItems()) {
                    jdbcTemplate.update(
                            "INSERT INTO featured_row_item (row_id, position, item_kind, item_short_id) VALUES (?, ?, ?, ?)",
                            rowId, itemPosition++, item.getItemKind(), item.getItemShortId());
                }
            }
        }
    }
}
