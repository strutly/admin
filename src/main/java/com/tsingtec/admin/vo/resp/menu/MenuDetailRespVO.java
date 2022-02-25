package com.tsingtec.admin.vo.resp.menu;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @Author lj
 * @Date 2022/2/23 15:08
 * @Version 1.0
 */
@Data
@AllArgsConstructor
public class MenuDetailRespVO {
    private MenuNodeRespVO menuNodeRespVO;
    private List<MenuNodeRespVO> children;

}
