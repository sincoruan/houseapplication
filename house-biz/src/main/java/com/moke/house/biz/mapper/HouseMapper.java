package com.moke.house.biz.mapper;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.moke.house.common.model.Community;
import com.moke.house.common.model.House;
import com.moke.house.common.model.HouseUser;
import com.moke.house.common.model.User;
import com.moke.house.common.model.UserMsg;
import com.moke.house.common.page.PageParams;

@Mapper
public interface HouseMapper {
    public List<House>  selectPageHouses(@Param("house") House house, @Param("pageParams") PageParams pageParams);
    
    public Long selectPageCount(@Param("house") House query);
	
	public int insert(User account);

	public List<Community> selectCommunity(Community community);

	public int insert(House house);

	public HouseUser selectHouseUser(@Param("userId") Long userId, @Param("id") Long houseId, @Param("type") Integer integer);
	
	public HouseUser selectSaleHouseUser(@Param("id") Long houseId);

	public int insertHouseUser(HouseUser houseUser);

	public int insertUserMsg(UserMsg userMsg);

	public int updateHouse(House updateHouse);
	
	public  int downHouse(Long id);

	public int deleteHouseUser(@Param("id") Long id, @Param("userId") Long userId, @Param("type") Integer value);
	
}
