package com.tsingtec.admin.controller.sys;

import com.tsingtec.admin.controller.GenericController;
import com.tsingtec.admin.entity.Menu;
import com.tsingtec.admin.service.MenuService;
import com.tsingtec.admin.vo.req.menu.MenuEditReqVO;
import com.tsingtec.admin.vo.resp.menu.HomeMenuRespVO;
import com.tsingtec.admin.vo.resp.menu.MenuDetailRespVO;
import com.tsingtec.admin.vo.resp.menu.MenuInfo;
import com.tsingtec.admin.vo.resp.menu.MenuNodeRespVO;
import com.tsingtec.commons.mapper.BeanMapper;
import com.tsingtec.commons.support.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequestMapping("/manager")
@RestController
public class MenuController extends GenericController {

    @Autowired
    private MenuService menuService;

    /**
     * 获取用户的权限菜单
     * @return
     */
    @GetMapping("/menu/home")
    public R<HomeMenuRespVO> getHomeInfo(){
        List<Menu> menus = menuService.menuTreeList(this.getAid());
        //menus.removeIf(menu->!menu.getStatus());
        List<MenuInfo> homeMenus = BeanMapper.mapList(menus, MenuInfo.class);
        HomeMenuRespVO respVO = new HomeMenuRespVO();
        respVO.setMenuInfo(homeMenus);
        return R.ok(respVO);
    }

    @GetMapping("/menu")
    public R<List<MenuNodeRespVO>> list(){
        return R.ok(BeanMapper.mapList(menuService.findAll(),MenuNodeRespVO.class));
    }

    @PostMapping("/menu")
    public R addMenu(@RequestBody @Valid MenuEditReqVO vo){
        Menu menu = new Menu();
        BeanMapper.mapExcludeNull(vo,menu);
        menuService.save(menu);
        return R.ok();
    }

    @DeleteMapping("/menu")
    public R deleted(@RequestBody Long id){
        menuService.deleteById(id);
        return R.ok();
    }

    @PutMapping("/menu")
    public R updateMenu(@RequestBody @Valid MenuEditReqVO vo){
        Menu menu =menuService.findById(vo.getId());
        BeanMapper.mapExcludeNull(vo,menu);
        menuService.update(menu);
        return R.ok();
    }

    @PutMapping("/menu/status")
    public R updateMenuStatus(){

        return R.ok();
    }


    @GetMapping("/menu/{id}")
    public R<MenuDetailRespVO> detail(@PathVariable("id") Long id){
        Menu menu = menuService.findById(id);
        MenuNodeRespVO respVO = menu==null?new MenuNodeRespVO():BeanMapper.map(menu,MenuNodeRespVO.class);
        return R.ok(new MenuDetailRespVO(respVO,BeanMapper.mapList(menuService.selectAllMenuByTree(this.getAid(),id),MenuNodeRespVO.class)));
    }
}
