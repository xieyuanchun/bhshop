package com.bh.user.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.bh.user.pojo.MemberShop;
import com.bh.user.pojo.POSParam;
import com.bh.user.pojo.SysLog;
import com.bh.user.pojo.MBusEntity;

public interface MemberShopMapper {
    int deleteByPrimaryKey(Integer mId);

    int insert(MemberShop record);

    int insertSelective(MemberShop record);

    MemberShop selectByPrimaryKey(Integer mId);

    int updateByPrimaryKeySelective(MemberShop record);

    int updateByPrimaryKeyWithBLOBs(MemberShop record);

    int updateByPrimaryKey(MemberShop record);
    
    /**  ***********************************************以下是chengfengyun************************************************ */
    List<Map<String, Object>> selectShop(MemberShop memberShop);
    
    //选择未审核的的信息status表示：0正常1删除2锁定,3未审核,4审核
    List<MemberShop> selectAllShopByStatus();
    
    List<MemberShop> selectStatusByPage(Integer startPage,Integer pageSize);
    int selectStatusByPageCount();
    
    int updateShopStatus(MemberShop memberShop);
    
    //查询最大的对象
    MemberShop selectMaxColumn();
    
    List<MemberShop> selectMemberShopByParams(MemberShop memberShop);
    List<MemberShop> selectMemberShopList(MemberShop memberShop);
    int updateStep(MemberShop memberShop);
    int insertAdmin(MemberShop memberShop);
    int selectBymId(Integer mId);
    String selectUsernameBymId(Integer mId);
    int selectByUsername(String username);
    int selectMemberShopByPhone(MemberShop memberShop);
    int updateShop();
    List<MemberShop> selectMemberShop(@Param(value="isBhshop") Integer isBhshop);
    List<MemberShop> selectShopByTokne(@Param(value="token") String token);
    List<MemberShop> selectPosList(POSParam param);
    int updateHandleStatusBymIds(@Param(value="mIds") List<String> mIds);
    MBusEntity selectAllByUsername(String username);
    int updateMemberShop(MemberShop memberShop);
    int updateMemberShopByOrderNo(MemberShop memberShop);
    int updateSysUserPwd(MBusEntity entity);
    //程凤云2018-3-22-免审核押金支付
    int updateMemberShopByDescNo(MemberShop memberShop);
    //跟据depositNo查询memberShop
    MemberShop checkIsPaySeccuss(String depositNo);
    //查询免审核上架的商家
    List<MemberShop> selectDepositList(MemberShop memberShop);
    MemberShop selectShopMsg(@Param(value="mId") Integer mId);
    //结束

    //统计审核通过的商家总数
	int countShopCheckPass(Integer shopStatus);

	List<SysLog> getUserLoginLog(String username);
	
	
	List<MemberShop> selectAllName();

	int phoneVerify(String phone);

	MemberShop getMemberShopByOrderNo(String orderNo);
	

	
    
    
}