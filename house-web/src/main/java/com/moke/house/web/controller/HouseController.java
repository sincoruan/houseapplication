package com.moke.house.web.controller;

import com.moke.house.biz.service.HouseService;
import com.moke.house.common.constants.CommonConstants;
import com.moke.house.common.model.House;
import com.moke.house.common.page.PageData;
import com.moke.house.common.page.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class HouseController {
    @Autowired
    private HouseService houseService;

//    @Autowired
//    private RecommendService recommendService;

    @RequestMapping("/house/list")
    public String houseList(Integer pageSize, Integer pageNum, House query, ModelMap modelMap){
        PageData<House> ps =  houseService.queryHouse(query, PageParams.build(pageSize, pageNum));
//        List<House> hotHouses =  recommendService.getHotHouse(CommonConstants.RECOM_SIZE);
//        modelMap.put("recomHouses", hotHouses);
        modelMap.put("ps", ps);
        modelMap.put("vo", query);
        return "house/listing";
    }
}
