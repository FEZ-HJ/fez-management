package com.fez.activiti.service;

import com.fez.activiti.entity.TbNode;
import com.fez.activiti.repository.TbNodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by H.J
 * 2019/1/19
 */
@Service
public class TbNodeService {

    @Autowired
    private TbNodeRepository repository;

    public void save(TbNode tbNode) {
        repository.save(tbNode);
    }

    public void save(List<TbNode> tbNodes) {
        repository.saveAll(tbNodes);
    }

    public void deleteAllByProcDefId(String procDefId) {
        repository.deleteAllByProcDefId(procDefId);
    }

    public List<TbNode> findAllByProcDefId(String procDefId) {
        return repository.findAllByProcDefIdAndNodeType(procDefId,null);
    }
}
