package com.huiming.service;

import com.huiming.common.ServerResponse;
import com.huiming.vo.CartVo;

/*
 * @author:15737
 * @createtime:2017/11/12 15:02
 * @desc:
*/
public interface ICartService {
    ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count);
    ServerResponse<CartVo> update(Integer userId, Integer productId, Integer count);
    ServerResponse<CartVo> delete(Integer userId, String productIds);
    ServerResponse<CartVo> list(Integer userId);
    ServerResponse<CartVo> selectOrUnSelect(Integer userId, Integer productId, Integer checked);
    ServerResponse<Integer> getCartProductCount(Integer userId);
}
