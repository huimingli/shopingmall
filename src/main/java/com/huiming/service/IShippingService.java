package com.huiming.service;

import com.github.pagehelper.PageInfo;
import com.huiming.common.ServerResponse;
import com.huiming.pojo.Shipping;

/*
 * @author:15737
 * @createtime:2017/11/13 16:52
 * @desc:
*/
public interface IShippingService {
    ServerResponse add(Integer userId, Shipping shipping);
    ServerResponse<String> delete(Integer userId,Integer shippingId);
    ServerResponse update(Integer userId, Shipping shipping);
    ServerResponse<Shipping> select(Integer userId, Integer shippingId);
    ServerResponse<PageInfo> list(Integer userId, int pageNum, int pageSize);
}
