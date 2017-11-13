package com.huiming.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.huiming.common.ServerResponse;
import com.huiming.dao.ShippingMapper;
import com.huiming.pojo.Shipping;
import com.huiming.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/*
 * @author:15737
 * @createtime:2017/11/13 16:52
 * @desc:
*/
@Service("iShippingService")
public class ShippingServiceImpl implements IShippingService {
    @Autowired
    private ShippingMapper shippingMapper;

    public ServerResponse add(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
        int rowCount = shippingMapper.insert(shipping);
        if (rowCount > 0) {
            Map result = Maps.newHashMap();
            result.put("shippingId", shipping.getId());
            return ServerResponse.createBySuccess("success add address", result);
        }
        return ServerResponse.createByErrorMessage("fail to add address");
    }

    public ServerResponse<String> delete(Integer userId, Integer shippingId) {
        int rowCount = shippingMapper.deleteByShippingIdUserId(userId, shippingId);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("success to delete address");
        }
        return ServerResponse.createByErrorMessage("fail to delete address");
    }

    public ServerResponse update(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
        int rowCount = shippingMapper.updateByShipping(shipping);
        if (rowCount > 0) {
            return ServerResponse.createBySuccess("success to update address");
        }
        return ServerResponse.createByErrorMessage("fail to update address");
    }

    public ServerResponse<Shipping> select(Integer userId, Integer shippingId) {
        Shipping shipping = shippingMapper.selectByShippingIdAndUserId(userId, shippingId);
        if (shipping == null) {
            return ServerResponse.createByErrorMessage("cannot find this address");
        }
        return ServerResponse.createBySuccess("success to select shipping ", shipping);
    }

    public ServerResponse<PageInfo> list(Integer userId,int pageNum,int pageSize){
        PageHelper.startPage(pageNum, pageSize);
        List<Shipping> shippingList = shippingMapper.selectByUserId(userId);
        PageInfo pageInfo = new PageInfo(shippingList);
        return ServerResponse.createBySuccess(pageInfo);
    }
}