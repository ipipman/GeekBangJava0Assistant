package org.geektimes.projects.user.repository.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.geektimes.projects.user.domain.User;

/**
 * <p>
 *
 * <p>
 *
 * @author liuwei
 * @date 2021/5/12
 */
@Mapper
public interface UserMapper {

    boolean dropTable();

    boolean createTable();

    boolean insert(@Param("user") User user);

    User selectById(@Param("userId") Long userId);

    boolean deleteById(@Param("userId") Long userId);

}