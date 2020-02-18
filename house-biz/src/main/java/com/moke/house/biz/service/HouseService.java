package com.moke.house.biz.service;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.moke.house.biz.mapper.HouseMapper;
import com.moke.house.common.model.Community;
import com.moke.house.common.model.House;
import com.moke.house.common.page.PageData;
import com.moke.house.common.page.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HouseService {

    @Autowired
    private HouseMapper houseMapper;

    @Value("${file.prefix}")
    private String imgPrefix;

    @Autowired
    private FileService fileService;
//
//    @Autowired
//    private AgencyService agencyService;

    @Autowired
    private MailService mailService;

    public PageData<House> queryHouse(House query, PageParams pageParams){
        List<House> houses = Lists.newArrayList();
        if(!Strings.isNullOrEmpty(query.getName()))
        {
            Community community = new Community();
            community.setName(query.getName());
            List<Community> communities  = houseMapper.selectCommunity(community);
            if(!communities.isEmpty()){
                query.setCommunityId(communities.get(0).getId());
            }
        }
        houses = queryAndSetImg(query,pageParams);
        Long count = houseMapper.selectPageCount(query);
        return PageData.buildPage(houses, count, pageParams.getPageSize(), pageParams.getPageNum());
    }
    public List<House> queryAndSetImg(House query, PageParams pageParams) {
        List<House> houses =   houseMapper.selectPageHouses(query, pageParams);
        houses.forEach(h ->{
            h.setFirstImg(imgPrefix + h.getFirstImg());
            h.setImageList(h.getImageList().stream().map(img -> imgPrefix + img).collect(Collectors.toList()));
            h.setFloorPlanList(h.getFloorPlanList().stream().map(img -> imgPrefix + img).collect(Collectors.toList()));
        });
        return houses;
    }
}
