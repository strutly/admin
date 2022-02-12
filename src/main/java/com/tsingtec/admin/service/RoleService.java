package com.tsingtec.admin.service;

import com.tsingtec.admin.constants.Constants;
import com.tsingtec.admin.entity.Menu;
import com.tsingtec.admin.entity.Role;
import com.tsingtec.admin.repository.RoleRepository;
import com.tsingtec.admin.vo.resp.menu.MenuNodeRespVO;
import com.tsingtec.commons.mapper.BeanMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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

@Slf4j
@Service
public class RoleService {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private MenuService menuService;

	@Transactional
	public void save(Role role,List<Long> mids) {
		List<Menu> menus = menuService.findAllById(mids);
		role.setMenus(new HashSet<>(menus));
		roleRepository.save(role);
	}

	public Role findById(Long id){
		return roleRepository.findById(id).orElse(null);
	}

	public Role getByAidAndId(Long aid,Long id) {
		Role role = roleRepository.findById(id).orElse(new Role());
		Set<Menu> menus = role.getMenus();
		Set<Long> checkList = menus.stream().map(menu -> menu.getId()).collect(Collectors.toSet());
		List<Menu> allMenus = menuService.getMenu(aid);
		allMenus.forEach(menu -> {
			if(checkList.contains(menu.getId())){
				menu.setChecked(true);
			}
		});
		role.setChilidren(BeanMapper.mapList(allMenus, MenuNodeRespVO.class));
		return role;
	}


	public Page<Role> page(Role role,Pageable pageable) {
		return roleRepository.findAll((root, criteriaQuery, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<Predicate>();
			/**
			 * 非超级用户只能查看非超级权限
			 */
			if(!role.getIfSuper()){
				predicates.add(criteriaBuilder.isFalse(root.get("ifSuper")));
				predicates.add(criteriaBuilder.equal(root.get("createId"),role.getCreatedId()));
			}
			if (StringUtils.isNotBlank(role.getName())){
				predicates.add(criteriaBuilder.like(root.get("name"),role.getName()+"%"));
			}
			return criteriaQuery.where(predicates.toArray(new Predicate[0])).getRestriction();
		},pageable);
	}

	@Transactional
	public void deleteBatch(Long aid,List<Long> rids) {

		List<Role> roles = roleRepository.findAllById(rids);
		/**
		 * 移除超级权限以及不是本身创建的权限
		 */
		roles.removeIf(role -> role.getIfSuper() || !role.getCreatedId().equals(aid));

		rids = roles.stream().map(role -> role.getId()).collect(Collectors.toList());

		roles.forEach(role -> role.setDelStatus(Constants.DELETE_FLAG));

		roleRepository.saveAll(roles);

		roleRepository.cancelMenuJoinByRid(rids);

	}

}
