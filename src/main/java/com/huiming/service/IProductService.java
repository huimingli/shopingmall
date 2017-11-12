package com.huiming.service;

import com.github.pagehelper.PageInfo;
import com.huiming.common.ServerResponse;
import com.huiming.pojo.Product;
import com.huiming.vo.ProductDetailVo;

/*
 * @author:15737
 * @createtime:2017/11/10 15:10
 * @desc:
*/
public interface IProductService {
    ServerResponse saveOrUpdateProduct(Product product);
    ServerResponse<String> setSaleStatus(Integer productId,Integer status);
    ServerResponse<ProductDetailVo> manageProductDetail(Integer productId);
    ServerResponse getProductList(int pageNum,int pageSize);
    ServerResponse<PageInfo> searchProduct(String productName, Integer productId , int pageNum, int pageSize);
    ServerResponse<ProductDetailVo> getProductDetail(Integer productId);
    ServerResponse<PageInfo> getProductByKeywordCategory(String keyword,Integer categoryId,String orderBy,int pageNum,int pageSize);

}
