package com.tsingtec.admin.repository;

import com.tsingtec.admin.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long>, JpaSpecificationExecutor<Menu> {


    List<Menu> findByPid(Long pid);

    /**
     * 取消角色与菜单之间的关系
     * @param ids 角色ID
     * @return 影响结果
     */
    @Modifying
    @Query(value = "DELETE FROM sys_role_menu WHERE mid = ?1", nativeQuery = true)
    void cancelMenuJoin(@Param(value = "ids") Long ids);
}
