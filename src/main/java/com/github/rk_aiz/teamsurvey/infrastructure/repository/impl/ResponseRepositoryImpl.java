package com.github.rk_aiz.teamsurvey.infrastructure.repository.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.github.rk_aiz.teamsurvey.domain.model.Response;
import com.github.rk_aiz.teamsurvey.infrastructure.entity.ResponseEntity;
import com.github.rk_aiz.teamsurvey.infrastructure.mapper.mybatis.ResponseMapper;
import com.github.rk_aiz.teamsurvey.infrastructure.repository.ResponseRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ResponseRepositoryImpl implements ResponseRepository {

    private final ResponseMapper responseMapper;

    @Override
    public List<Response> findAll() {
        return responseMapper.selectAll().stream().map(ResponseEntity::toModel).toList();
    }

    @Override
    public Response findById(Integer id) {
        ResponseEntity entity = responseMapper.selectById(id);
        if (entity == null)
            return null;

        return entity.toModel();
    }

    @Override
    public List<Response> findByUsername(String username) {
        return responseMapper.selectByUsername(username)
                .stream().map(ResponseEntity::toModel).toList();
    }

    @Override
    public void remove(Integer id) {
        // 子要素の削除はDBの外部キー制約(ON DELETE CASCADE)に任せる
        responseMapper.delete(id);
    }

    @Override
    public void add(Response response) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'add'");
    }

    @Override
    public void set(Response response) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'set'");
    }
}
