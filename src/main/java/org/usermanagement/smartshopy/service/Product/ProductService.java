package org.usermanagement.smartshopy.service.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.usermanagement.smartshopy.dto.request.CreateProductDTO;
import org.usermanagement.smartshopy.dto.request.UpdateProductDTO;
import org.usermanagement.smartshopy.dto.response.ProductDTO;

import java.util.List;

public interface ProductService {
    ProductDTO createProduct(CreateProductDTO dto);
    ProductDTO getProductById(Long id);
    Page<ProductDTO> getAllProducts(Pageable pageable);
    List<ProductDTO> getActiveProducts();
    ProductDTO updateProduct(Long id, UpdateProductDTO dto);
    void deleteProduct(Long id);
    ProductDTO toggleProductStatus(Long id, boolean active);
}
