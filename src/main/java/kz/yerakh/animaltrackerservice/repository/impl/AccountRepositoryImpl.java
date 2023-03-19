package kz.yerakh.animaltrackerservice.repository.impl;

import kz.yerakh.animaltrackerservice.dto.AccountRequest;
import kz.yerakh.animaltrackerservice.dto.AccountSearchCriteria;
import kz.yerakh.animaltrackerservice.model.Account;
import kz.yerakh.animaltrackerservice.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AccountRepositoryImpl implements AccountRepository {

    private static final String SELECT_BY_ID = "SELECT account_id, first_name, last_name, email FROM account WHERE account_id = ?";
    private static final String SELECT = "SELECT account_id, first_name, last_name, email FROM account";
    private static final String WHERE = " WHERE";
    private static final String AND = " AND";
    private static final String LIKE_FIRST_NAME = " LOWER(first_name) LIKE LOWER(?)";
    private static final String LIKE_LAST_NAME = " LOWER(last_name) LIKE LOWER(?)";
    private static final String LIKE_EMAIL = " LOWER(email) LIKE LOWER(?)";
    private static final String LIMIT_AND_OFFSET = " ORDER BY account_id LIMIT ? OFFSET ?";
    private static final String INSERT = "INSERT INTO account(first_name, last_name, email, password) VALUES(?, ?, ?, ?)" +
            " RETURNING account_id";
    private static final String UPDATE = "UPDATE account SET first_name = ?, last_name = ?, email = ?, password = ? " +
            "WHERE account_id = ?";
    private static final String DELETE = "DELETE FROM account WHERE account_id = ?";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Account> find(AccountSearchCriteria payload) {
        ArrayList<Object> params = new ArrayList<>();
        var where = new StringBuilder(WHERE);
        if (payload.firstName() != null && !payload.firstName().isEmpty()) {
            where.append(LIKE_FIRST_NAME);
            params.add(payload.firstName());
        }
        if (payload.lastName() != null && !payload.lastName().isEmpty()) {
            if (where.length() > 6) {
                where.append(AND);
            }
            where.append(LIKE_LAST_NAME);
            params.add(payload.lastName());
        }
        if (payload.email() != null && !payload.email().isEmpty()) {
            if (where.length() > 6) {
                where.append(AND);
            }
            where.append(LIKE_EMAIL);
            params.add(payload.email());
        }

        var query = new StringBuilder(SELECT);
        if (!params.isEmpty()) {
            query.append(where);
        }
        query.append(LIMIT_AND_OFFSET);
        params.add(payload.size());
        params.add(payload.from());

        return jdbcTemplate.query(query.toString(), new Account.AccountRowMapper(), params.toArray());
    }

    @Override
    public Optional<Account> find(Integer accountId) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(SELECT_BY_ID, new Account.AccountRowMapper(), accountId));
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public Integer save(AccountRequest payload) {
        return jdbcTemplate.queryForObject(INSERT, Integer.class, payload.firstName(), payload.lastName(),
                payload.email(), payload.password());
    }

    @Override
    public int update(Integer accountId, AccountRequest payload) {
        return jdbcTemplate.update(UPDATE, payload.firstName(), payload.lastName(),
                payload.email(), payload.password(), accountId);
    }

    @Override
    public int delete(Integer accountId) {
        return jdbcTemplate.update(DELETE, accountId);
    }
}
