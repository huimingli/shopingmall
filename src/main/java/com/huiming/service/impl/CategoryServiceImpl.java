package com.huiming.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.huiming.common.ServerResponse;
import com.huiming.dao.CategoryMapper;
import com.huiming.pojo.Category;
import com.huiming.service.ICategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Set;

/*
 * @author:15737
 * @createtime:2017/11/09 16:12
 * @desc:
*/
@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService {
    private   Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);
    @Autowired
    private CategoryMapper categoryMaper;

   public  ServerResponse addCategory(String categoryName, Integer parentId){
        if(parentId == null || StringUtils.isBlank(categoryName)){
            return ServerResponse.createByErrorMessage("param is wrong");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);
        int rowCount = categoryMaper.insert(category);
        if(rowCount > 0){
            return ServerResponse.createBySuccessMessage("add category successfully");
        }
        return ServerResponse.createByErrorMessage("fail to add category");
    }

    @Override
    public ServerResponse updateCategoryName(Integer categoryId, String categoryName) {
       if (categoryId == null || StringUtils.isBlank(categoryName)){
           return ServerResponse.createByErrorMessage("param is wrong");
       }
       Category category = new Category();
       category.setId(categoryId);
       category.setName(categoryName);
       int rowCount = categoryMaper.updateByPrimaryKeySelective(category);
       if(rowCount>0){
           return ServerResponse.createBySuccessMessage("success to update categoryname");
       }
       return ServerResponse.createByErrorMessage("fail to update categoryname");
    }

    public ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId){
       List<Category> categories = categoryMaper.selectCategoryChildrenByParentId(categoryId);
       if(CollectionUtils.isEmpty(categories)){
           logger.info("did not find sub category");
       }
       return ServerResponse.createBySuccess(categories);
    }

    /**
     * 递归查询分类本节点的id以及孩子节点的id
     * @param categoryId
     * @return
     */
    public ServerResponse selectCategoryAndChildrenById(Integer categoryId){

        Set<Category>  categorySet = Sets.newHashSet();
        findChildCategory(categorySet,categoryId);

        List<Integer> categoryIdList = Lists.newArrayList();
        if(categoryId != null){
            for (Category category:categorySet){
                categoryIdList.add(category.getId());
            }
        }
        return ServerResponse.createBySuccess(categoryIdList);
    }

    private Set<Category> findChildCategory(Set<Category> categorySet,Integer categoryId){
        Category category = categoryMaper.selectByPrimaryKey(categoryId);
        if(category != null){
            categorySet.add(category);
        }
        List<Category> categories = categoryMaper.selectCategoryChildrenByParentId(categoryId);
        for (Category category1:categories){
            findChildCategory(categorySet, category1.getId());
        }
        return categorySet;
    }
}
