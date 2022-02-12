package com.tsingtec.admin.repository;

import com.tsingtec.admin.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {

    /**
     * 取消角色与用户之间的关系
     * @param ids 角色ID
     * @return 影响结果
     */
    @Modifying
    @Query(value = "DELETE FROM sys_user_role WHERE rid in ?1", nativeQuery = true)
    void cancelUserJoinByRid(@Param(value = "ids") List<Long> ids);

    List<Role> findByCreatedId(Long aid);

    /**
     * 取消角色与菜单之间的关系
     * @param ids 角色ID
     * @return 影响结果
     */
    @Modifying
    @Query(value = "DELETE FROM sys_role_menu WHERE rid in ?1", nativeQuery = true)
    void cancelMenuJoinByRid(@Param(value = "ids") List<Long> ids);

    /**
     * 取消角色与菜单之间的关系
     * @param id 角色ID
     * @return 影响结果
     */
    @Modifying
    @Query(value = "DELETE FROM sys_role_menu WHERE mid = ?1", nativeQuery = true)
    void cancelMenuJoinByMid(@Param(value = "id") Long id);

}
