package com.bh.admin.mapper.user;

import java.util.List;

import com.bh.admin.pojo.user.MemberUserAddress;


public interface MemberUserAddressMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MemberUserAddress record);

    int insertSelective(MemberUserAddress record);

    MemberUserAddress selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MemberUserAddress record);

    int updateByPrimaryKey(MemberUserAddress record);
    
    /** *********************************以下是chengfengyun*******************************************/
    List<MemberUserAddress> selectAllBymId(Integer mId);
    List<MemberUserAddress> selectAllAddressByisDel(Integer mId);
    
    int selectCountBymIdAndDefaultId(Integer mId);
    
    int updateBymIdAndAndDefaultId(Integer mId);
    
    //根据mId和id更新默认的收货地址
    int updateDefaultAddress(MemberUserAddress address);
    
    /** ********************************** 2017-9-30 ******************************************/
    /**   根据用户选择默认的地址 2017-9-30 **/
    MemberUserAddress selecUseraddresstBySelect(MemberUserAddress address);
    
    /**2017-10-30更新is_del***/
    int updateByisDelSelective(MemberUserAddress memberUserAddress);
    
    int updateDefaultIdAddress(int id);
    List<MemberUserAddress> selectAddressByOrderMain(MemberUserAddress address);
    
    
    
    
    /*scj-获取用户默认地址*/
    MemberUserAddress getByUserId(Integer mId);
}