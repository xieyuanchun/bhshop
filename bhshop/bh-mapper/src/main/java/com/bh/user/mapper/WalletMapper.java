package com.bh.user.mapper;
import java.util.List;

import com.bh.user.pojo.Wallet;
public interface WalletMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Wallet record);

    int insertSelective(Wallet record);

    Wallet selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Wallet record);

    int updateByPrimaryKey(Wallet record);
    
    List<Wallet> listPage(Wallet entity);
    //zlk 根据用户id获取钱包信息
    List<Wallet> getWalletByUid(Integer getmId);
    //zlk
    int updateByUid(Wallet record);
   
}