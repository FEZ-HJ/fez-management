package com.fez.activiti.service;

import com.fez.activiti.entity.TbNodeInfo;
import com.fez.activiti.repository.NodeInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by H.J
 * 2019/1/19
 */
@Service
public class NodeInfoService {

    @Autowired
    private NodeInfoRepository repository;

    public void save(TbNodeInfo tbNodeInfo){
        repository.save(tbNodeInfo);
    }

    public void save(List<TbNodeInfo> list){
        repository.saveAll(list);
    }

    public void deleteAllByNodeId(String nodeId){
        repository.deleteAllByNodeId(nodeId);
    }

    public List<TbNodeInfo> findAllByNodeId(String nodeId){
        return repository.findAllByNodeId(nodeId);
    }
}
