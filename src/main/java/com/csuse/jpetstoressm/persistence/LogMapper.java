package com.csuse.jpetstoressm.persistence;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LogMapper {

    void insertLog(@Param("username") String username, @Param("logInfo") String logInfo);
}
