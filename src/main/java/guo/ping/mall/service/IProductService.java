package guo.ping.mall.service;

import guo.ping.mall.common.ServerResponse;
import guo.ping.mall.pojo.Product;
import guo.ping.mall.vo.ProductDetailVo;

/**
 * @author Kingdompin@163.com
 * @date 2018/3/3 下午12:44
 */
public interface IProductService {

    ServerResponse saveOrUpdateProduct(Product product);

    ServerResponse setSaleStatus(Integer productId, Integer status);

    ServerResponse<ProductDetailVo> manageProductDetail(Integer productId);
}
