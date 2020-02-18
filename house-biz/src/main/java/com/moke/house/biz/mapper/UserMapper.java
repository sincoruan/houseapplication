package com.moke.house.biz.mapper;

import java.util.List;

import com.moke.house.common.model.User;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface UserMapper {

	public List<User>  selectUsers();
	
	public int insert(User account);

	public int delete(String email);

	public int update(User updateUser);

	public List<User> selectUsersByQuery(User user);
}
