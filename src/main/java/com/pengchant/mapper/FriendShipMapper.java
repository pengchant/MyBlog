package com.pengchant.mapper;

import com.pengchant.model.FriendShip;

public interface FriendShipMapper {
    int deleteByPrimaryKey(Integer friendId);

    int insert(FriendShip record);

    int insertSelective(FriendShip record);

    FriendShip selectByPrimaryKey(Integer friendId);

    int updateByPrimaryKeySelective(FriendShip record);

    int updateByPrimaryKey(FriendShip record);
}