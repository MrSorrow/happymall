package guo.ping.mall.service;

import guo.ping.mall.common.ServerResponse;

/**
 * @author Kingdompin@163.com
 * @date 2018/3/1 下午1:43
 */
public interface ICategoryService {
    ServerResponse addCategory(String categoryName, Integer parentId);

    ServerResponse updateCategoryName(Integer categoryId, String categoryName);

    ServerResponse getChildrenParallelCategory(Integer categoryId);

    ServerResponse getCategoryAndDeepChildrenCategory(Integer categoryId);
}
