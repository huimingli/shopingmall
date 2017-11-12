package com.huiming.service;


import com.huiming.common.ServerResponse;
import com.huiming.pojo.Category;

import java.util.List;

/*
 * @author:15737
 * @createtime:2017/11/09 16:12
 * @desc:
*/
public interface ICategoryService {
    ServerResponse addCategory(String categoryName, Integer parentId);
    ServerResponse updateCategoryName(Integer categoryId,String categoryName);
    ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId);
    ServerResponse< List<Integer> > selectCategoryAndChildrenById(Integer categoryId);
}
