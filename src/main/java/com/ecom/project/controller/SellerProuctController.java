package com.ecom.project.controller;

import com.ecom.project.entity.Product;
import com.ecom.project.entity.Seller;
import com.ecom.project.exceptions.ProductException;
import com.ecom.project.exceptions.SellerException;
import com.ecom.project.request.CreateProductRequest;
import com.ecom.project.service.ProductService;
import com.ecom.project.service.SellerService;
import com.ecom.project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sellers/products")
public class SellerProuctController {
    private final ProductService productService;
    private final SellerService sellerService;
    private final UserService userService;

    @GetMapping()
    public ResponseEntity<List<Product>> getProductBySellerId(@RequestHeader ("Authorization") String jwt) throws Exception {
        Seller seller=sellerService.getSellerProfile(jwt);
        List<Product> products=productService.getProductBySellerId(seller.getId());
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Product> createProduct(@RequestBody CreateProductRequest req,
                                                 @RequestHeader ("Authorization")String jwt) throws Exception {
        Seller seller=sellerService.getSellerProfile(jwt);
        Product product=productService.createProduct(req,seller);
        return new ResponseEntity<>(product,HttpStatus.CREATED);
    }
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void>deleteProduct(@PathVariable long productId) throws Exception {
        try {
            productService.deleteProduct(productId);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (ProductException pe){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable long productId,
                                                 @RequestBody Product product) throws ProductException {

            Product updateProduct=productService.updateProduct(productId,product);
            return new ResponseEntity<>(updateProduct,HttpStatus.OK);


        }
    }


