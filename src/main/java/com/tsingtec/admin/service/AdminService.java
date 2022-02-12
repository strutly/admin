package com.tsingtec.admin.service;

import com.tsingtec.admin.constants.Constants;
import com.tsingtec.admin.entity.Admin;
import com.tsingtec.admin.entity.Role;
import com.tsingtec.admin.repository.AdminRepository;
import com.tsingtec.admin.repository.RoleRepository;
import com.tsingtec.admin.vo.resp.admin.AdminRoleRespVO;
import com.tsingtec.commons.exception.ServiceException;
import com.tsingtec.commons.mapper.BeanMapper;
import com.tsingtec.commons.support.ApiCodeEnum;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author lj
 * @Date 2020/3/29 13:54
 * @Version 1.0
 */
@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private RoleRepository roleRepository;

    private static final String HASH_ALGORITHM = "SHA-256";
    private static final int HASH_INTERATIONS = 1024;
    private static final int SALT_SIZE = 8;

    public static String generateSalt() {
        int byteLen = SALT_SIZE >> 1;
        SecureRandomNumberGenerator secureRandom = new SecureRandomNumberGenerator();
        return secureRandom.nextBytes(byteLen).toHex();
    }

    /**
     * 获取加密后的密码，需要指定 hash迭代的次数
     * @param password       需要加密的密码
     * @param salt           盐
     * @return 加密后的密码
     */
    public static String encryptPassword(String password, String salt) {
        SimpleHash hash = new SimpleHash(HASH_ALGORITHM, password, salt, HASH_INTERATIONS);
        return hash.toString();
    }

    public Admin findById(Long id) {
        return adminRepository.findById(id).orElse(null);
    }

    @Transactional
    public void save(Admin admin) {
        adminRepository.save(admin);
    }

    public Page<Admin> page(Admin admin,Pageable pageable) {
        return adminRepository.findAll((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<Predicate>();
            /**
             * 非超级管理员不能显示超级管理账户
             */
            if(!admin.getIfSuper()){
                predicates.add(criteriaBuilder.isFalse(root.get("ifSuper")));
                predicates.add(criteriaBuilder.equal(root.get("createdId"),admin.getCreatedId()));
            }
            if (!StringUtils.isEmpty(admin.getName())){
                predicates.add(criteriaBuilder.like(root.get("name"),"%"+admin.getName()+"%"));
            }
            if (!StringUtils.isBlank(admin.getLoginName())){
                predicates.add(criteriaBuilder.like(root.get("loginName"),"%"+admin.getLoginName()+"%"));
            }
            return criteriaQuery.where(predicates.toArray(new Predicate[0])).getRestriction();
        },pageable);
    }

    @Transactional
    public void addAdmin(Admin vo) {
        Admin admin = findByLoginName(vo.getLoginName());
        if(admin!=null){
            throw new ServiceException("该登录账号已存在,请修改后再保存", ApiCodeEnum.BAD_REQUEST);
        }
        admin = BeanMapper.map(vo,Admin.class);
        String salt = generateSalt();
        String password = encryptPassword(vo.getPassword(),salt);
        admin.setSalt(salt);
        admin.setPassword(password);
        adminRepository.save(admin);
    }

    public Admin findByLoginName(String loginName) {
        return adminRepository.findByLoginName(loginName);
    }

    @Transactional
    public void updateAdmin(Admin vo) {
        Admin admin = findByLoginName(vo.getLoginName());
        //不是本身这个账号
        if(admin!=null && !admin.getId().equals(vo.getId())){
            throw new ServiceException("该登录账号已存在,请修改后再保存", ApiCodeEnum.BAD_REQUEST);
        }
        admin = findById(vo.getId());
        String salt = generateSalt();
        String password = encryptPassword(vo.getPassword(),salt);
        admin.setSalt(salt);
        admin.setName(vo.getName());
        admin.setLoginName(vo.getLoginName());
        admin.setPassword(password);
        admin.setStatus(vo.getStatus());
        adminRepository.save(admin);
    }

    @Transactional
    public void updatePwd(Long id, String oldPwd,String newPwa) {
        Admin admin = findById(id);
        String salt = admin.getSalt();
        if(admin.getPassword().equals(encryptPassword(oldPwd,salt))){
            String password = encryptPassword(newPwa,salt);
            admin.setPassword(password);
            adminRepository.save(admin);
        }else{
            throw new ServiceException("原密码不对,请重新输入", ApiCodeEnum.BAD_REQUEST);
        }
    }

    /**
     * 超级账号不能删除
     * 不是自己创建的不能删除
     * @param aids
     */
    public void deleteById(Admin admin ,List<Long> aids) {
        List<Admin> admins = adminRepository.findAllById(aids);
        /**
         * 去除超级账号
         * 去除不是自己创建的账号
         */
        admins.removeIf(a-> a.getIfSuper() || (!admin.getIfSuper() && !a.getCreatedId().equals(admin.getId())));
        admins.forEach(a -> {
            a.setDelStatus(Constants.DELETE_FLAG);
        });
        adminRepository.saveAll(admins);
    }

    /**
     * 根据aid 获取权限id 集合
     * @param aid
     * @return
     */
    private List<Long>  getRidsByAid(Long aid){
        Admin admin = findById(aid);
        Set<Role> roles = admin.getRoles();
        List<Long> rids = roles.stream().map(role -> role.getId()).collect(Collectors.toList());
        return rids;
    }

    /**
     * 只能设置自己创建的
     * @param admin 创建者
     * @param aid 账号id
     * @return 获取账号的权限设置
     */
    public AdminRoleRespVO getAdminRole(Admin admin, Long aid) {
        List<Role> roles = roleRepository.findByCreatedId(admin.getId());
        List<Long> rids = getRidsByAid(aid);
        AdminRoleRespVO adminRoleRespVO = new AdminRoleRespVO();
        adminRoleRespVO.setAllRole(roles);
        adminRoleRespVO.setOwnRole(rids);
        return adminRoleRespVO;
    }

    @Transactional
    public void setAdminRole(Long createId,Long aid, List<Long> roleIds) {
        List<Role> roles = roleRepository.findAllById(roleIds);
        /**
         * 不能设定不是本账号新增的权限
         */
        roles.removeIf(role -> !role.getCreatedId().equals(createId));
        Admin admin = findById(aid);
        admin.setRoles(new HashSet<>(roles));
        adminRepository.save(admin);
    }
}
