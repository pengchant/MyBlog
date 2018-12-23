package com.pengchant.mapper;

import com.pengchant.form.MyCaredUser;
import com.pengchant.form.MyFansUser;
import com.pengchant.model.FriendShip;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FriendShipMapper {
    int deleteByPrimaryKey(Integer friendId);

    int insert(FriendShip record);

    int insertSelective(FriendShip record);

    FriendShip selectByPrimaryKey(Integer friendId);

    int updateByPrimaryKeySelective(FriendShip record);

    int updateByPrimaryKey(FriendShip record);

    /**
     * 查询我的关注
     *
     * @param myuserid
     * @return
     */
    List<MyCaredUser> querymycared(@Param("myuserid") String myuserid);


    /**
     * 取消关注
     * @param myuserid
     * @param caredid
     * @return
     */
    int cancelcared(@Param("myuserid") String myuserid, @Param("caredid") String caredid);


    /**
     * 查询我的粉丝
     * @param myuserid
     * @return
     */
    List<MyFansUser> selectmyfans(@Param("myuserid")String myuserid);


    /**
     * 添加关注
     * @param caredid
     * @param myuserid
     * @return
     */
    int addcared(@Param("caredid")String caredid,@Param("myuserid")String myuserid);


    /**
     * 查询是否已经关注某人
     * @param myuserid
     * @param bloguserid
     * @return
     */
    int hascared(@Param("myuserid")String myuserid, @Param("bloguserid")String bloguserid);

}