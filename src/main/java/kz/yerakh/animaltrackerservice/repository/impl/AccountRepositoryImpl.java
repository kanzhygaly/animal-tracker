package kz.yerakh.animaltrackerservice.repository.impl;

import kz.yerakh.animaltrackerservice.dto.AccountRequest;
import kz.yerakh.animaltrackerservice.dto.AccountSearchCriteria;
import kz.yerakh.animaltrackerservice.model.Account;
import kz.yerakh.animaltrackerservice.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AccountRepositoryImpl implements AccountRepository {

    private static final String SELECT_BY_ID = "SELECT account_id, first_name, last_name, email FROM account WHERE account_id = ?";
    private static final String SELECT_BY_EMAIL = "SELECT account_id, first_name, last_name, email FROM account WHERE email = ?";
    private static final String SELECT = "SELECT account_id, first_name, last_name, email FROM account";
    private static final String WHERE = " WHERE";
    private static final String AND = " AND";
    private static final String LIKE_FIRST_NAME = " LOWER(first_name) LIKE LOWER(?)";
    private static final String LIKE_LAST_NAME = " LOWER(last_name) LIKE LOWER(?)";
    private static final String LIKE_EMAIL = " LOWER(email) LIKE LOWER(?)";
    private static final String LIMIT_AND_OFFSET = " ORDER BY account_id LIMIT ? OFFSET ?";
    private static final String INSERT = "INSERT INTO account(first_name, last_name, email, password) VALUES(?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE account SET first_name = ?, last_name = ?, email = ?, password = ? " +
            "WHERE account_id = ?";
    private static final String DELETE = "DELETE FROM account WHERE account_id = ?";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Account> findByParams(AccountSearchCriteria accountSearchCriteria) {
        ArrayList<Object> params = new ArrayList<>();
        var where = new StringBuilder(WHERE);
        if (accountSearchCriteria.firstName() != null && !accountSearchCriteria.firstName().isEmpty()) {
            where.append(LIKE_FIRST_NAME);
            params.add(accountSearchCriteria.firstName());
        }
        if (accountSearchCriteria.lastName() != null && !accountSearchCriteria.lastName().isEmpty()) {
            if (where.length() > 6) {
                where.append(AND);
            }
            where.append(LIKE_LAST_NAME);
            params.add(accountSearchCriteria.lastName());
        }
        if (accountSearchCriteria.email() != null && !accountSearchCriteria.email().isEmpty()) {
            if (where.length() > 6) {
                where.append(AND);
            }
            where.append(LIKE_EMAIL);
            params.add(accountSearchCriteria.email());
        }

        var query = new StringBuilder(SELECT);
        if (!params.isEmpty()) {
            query.append(where);
        }
        query.append(LIMIT_AND_OFFSET);
        params.add(accountSearchCriteria.size());
        params.add(accountSearchCriteria.from());

        return jdbcTemplate.query(query.toString(), new Account.AccountRowMapper(), params.toArray());
    }

    @Override
    public Optional<Account> findById(Integer accountId) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(SELECT_BY_ID, new Account.AccountRowMapper(), accountId));
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Account> findByEmail(String email) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(SELECT_BY_EMAIL, new Account.AccountRowMapper(), email));
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public int save(AccountRequest accountRequest) {
        return jdbcTemplate.update(INSERT, accountRequest.firstName(), accountRequest.lastName(),
                accountRequest.email(), accountRequest.password());
    }

    @Override
    public int update(Integer accountId, AccountRequest accountRequest) {
        return jdbcTemplate.update(UPDATE, accountRequest.firstName(), accountRequest.lastName(),
                accountRequest.email(), accountRequest.password(), accountId);
    }

    @Override
    public int delete(Integer accountId) {
        return jdbcTemplate.update(DELETE, accountId);
    }
}
