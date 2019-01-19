package com.fez.activiti.repository;

import com.fez.activiti.entity.TbNodeInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by H.J
 * 2019/1/19
 */
public interface NodeInfoRepository extends JpaRepository<TbNodeInfo,String> {

    void deleteAllByNodeId(String nodeId);
    List<TbNodeInfo> findAllByNodeId(String nodeId);
}
