package com.ecom.project.service;

import com.ecom.project.entity.Product;
import com.ecom.project.entity.Seller;
import com.ecom.project.exceptions.ProductException;
import com.ecom.project.request.CreateProductRequest;
import org.springframework.data.domain.Page;


import java.util.List;

public interface ProductService {
    public Product createProduct(CreateProductRequest req , Seller seller)throws Exception;
    public void deleteProduct(long productId) throws Exception;
    public Product updateProduct(long productId,Product product) throws ProductException;
    Product findProductById(long productId) throws  ProductException;
    List<Product>searchProducts(String query) throws Exception;
    public Page<Product> getAllProducts(String category , String brand, String color, String sizes,
                                        Integer minPrice, Integer maxPrice, Integer minDiscount,
                                        String sort, String stock, Integer pageNumber);
    public Product updateProductStock(long productId)throws Exception;
    List<Product> getProductBySellerId(long sellerId);
}
