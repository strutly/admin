package com.tsingtec.admin.service;

import com.google.common.collect.Lists;
import com.tsingtec.admin.constants.Constants;
import com.tsingtec.admin.entity.Admin;
import com.tsingtec.admin.entity.Menu;
import com.tsingtec.admin.entity.Role;
import com.tsingtec.admin.repository.AdminRepository;
import com.tsingtec.admin.repository.MenuRepository;
import com.tsingtec.commons.exception.ServiceException;
import com.tsingtec.commons.mapper.BeanMapper;
import com.tsingtec.commons.support.ApiCodeEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MenuService {

	@Autowired
	private MenuRepository menuRepository;

	@Autowired
	private AdminRepository adminRepository;

	public Menu findById(Long id) {
		return menuRepository.findById(id).orElse(null);
	}

	@Transactional
	public void save(Menu vo) {
		menuRepository.save(vo);
	}

	@Transactional
	public void deleteById(Long id) {
		Menu menu = findById(id);
		/**
		 * step1
		 * 判断是否有下级元素
		 */
		List<Menu> childs = menuRepository.findByPid(id);

		if(!childs.isEmpty()){
			throw new ServiceException("该菜单权限存在子集关联，不允许删除", ApiCodeEnum.BAD_REQUEST);
		}
		/**
		 * step2
		 * 删除role_menu
		 */
		menuRepository.cancelMenuJoin(id);
		/**
		 * step3
		 * 删除该menu
		 */
		menu.setDelStatus(Constants.DELETE_FLAG);

		menuRepository.save(menu);
	}

	@Transactional
	public void update(Menu vo) {
		Menu menu = findById(vo.getId());
		if(null == menu){
			throw new ServiceException("修改对象不存在", ApiCodeEnum.BAD_REQUEST);
		}
		/**
		 * 当类型变更或者是父级变更时进行验证
		 */
		if(menu.getType()!=vo.getType()||!menu.getPid().equals(vo.getPid())){
			verify(vo);
		}
		BeanMapper.mapExcludeNull(vo,menu);
		menuRepository.save(menu);
	}


	/**
	 * 操作后的菜单类型是目录的时候 父级必须为目录
	 * 操作后的菜单类型是菜单的时候 父类必须为目录类型
	 * 操作后的菜单类型是按钮的时候 父类必须为菜单类型
	 * 操作的不能有
	 */
	private void verify(Menu vo){
		Menu parent = findById(vo.getPid());
		switch (vo.getType()){
			case 0:
				if(parent!=null){
					if(!parent.getType().equals(Constants.CATALOGUE)){
						throw new ServiceException("父类必须为目录类型",ApiCodeEnum.BAD_REQUEST);
					}
				}else if(!vo.getPid().equals(Constants.PARENT)){
					throw new ServiceException("父类必须为目录类型",ApiCodeEnum.BAD_REQUEST);
				}
				break;
			case 1:
				if(parent == null || !parent.getType().equals(Constants.CATALOGUE)){
					throw new ServiceException("父类必须为目录类型",ApiCodeEnum.BAD_REQUEST);
				}
				if(StringUtils.isEmpty(vo.getUrl())){
					throw new ServiceException("请添加地址URL",ApiCodeEnum.BAD_REQUEST);
				}
				break;
			case 2:
				if(parent == null || parent.getType().equals(Constants.MENU)){
					throw new ServiceException("父类必须为菜单类型",ApiCodeEnum.BAD_REQUEST);
				}
				if(StringUtils.isEmpty(vo.getPerms())){
					throw new ServiceException("请添加授权码",ApiCodeEnum.BAD_REQUEST);
				}
				if(StringUtils.isEmpty(vo.getUrl())){
					throw new ServiceException("请添加地址URL",ApiCodeEnum.BAD_REQUEST);
				}
				break;
		}
		List<Menu> list = menuRepository.findByPid(vo.getId());
		if (!list.isEmpty()) {
			throw new ServiceException("子级菜单不为空,请重新修改",ApiCodeEnum.BAD_REQUEST);
		}
	}

	public List<Menu> findAllTree() {
		List<Menu> menus = menuRepository.findAll(Sort.by(Sort.Direction.DESC,"pid","orderNum"));
		menus.forEach(menu -> {
			Menu parent = findById(menu.getPid());
			if(null!=parent){
				menu.setPidName(parent.getTitle());
			}
		});
		return getChildAll(Constants.PARENT,menus);
	}

	public List<Menu> findAll(){
		return menuRepository.findAll();
	}

	public List<Menu> getMenu(Long aid) {
		Admin admin = adminRepository.findById(aid).get();
		Set<Role> roles = admin.getRoles();
		HashSet<Menu> menus = new HashSet<>();
		for(Role role:roles){
			menus.addAll(role.getMenus());
		}
		return menus.stream().sorted(Comparator.comparing(Menu::getPid).thenComparing(Menu::getOrderNum).reversed()).collect(Collectors.toList());
	}

	/**
	 * 根据用户id 获取左侧菜单树
	 * @param aid
	 * @return
	 */
	public List<Menu> menuTreeList(Long aid) {
		List<Menu> menus = getMenu(aid);
		/**
		 * 去除按钮
		 */
		menus.removeIf(menu -> menu.getType().equals(Constants.BTN));
		return getTree(menus);
	}

	private List<Menu> getTree(List<Menu> menus){
		List<Menu> list = Lists.newArrayList();
		menus.forEach(menu -> {
			if(menu.getPid().equals(Constants.PARENT)){
				menu.setChildren(getChildAll(menu.getId(),menus));
				list.add(menu);
			}
		});
		return list;
	}

	private List<Menu> getChildAll(Long id,List<Menu> all){
		//子类
		List<Menu> children = all.stream().filter(x -> x.getPid().equals(id)).collect(Collectors.toList());
		//后辈中的非子类
		List<Menu> successor = all.stream().filter(x -> x.getPid()!=id).collect(Collectors.toList());

		children.forEach(x ->
				{
					getChildAll(x.getId(),successor).forEach(
							y -> {
								x.getChildren().add(y);
							}
					);
				}
		);
		return children;
	}



	public List<Menu> findAllById(List<Long> mids) {
		return menuRepository.findAllById(mids);
	}

	/**
	 * 根据菜单id 获取树
	 * @param menuId
	 * @return
	 */
	public List<Menu> selectAllMenuByTree(Long aid,Long menuId) {

		List<Menu> menus = getMenu(aid);
		/**
		 * 去除自己本身以及按钮
		 */
		menus.removeIf(menu -> menu.getId().equals(menuId) || menu.getType().equals(Constants.BTN));

		//新增顶级目录是为了方便添加一级目录
		Menu menu = new Menu();
		menu.setId(Constants.PARENT);
		menu.setTitle("默认顶级菜单");
		menu.setSpread(true);
		menu.setChildren(getChildAll(Constants.PARENT,menus));
		return Lists.newArrayList(menu);
	}
}
