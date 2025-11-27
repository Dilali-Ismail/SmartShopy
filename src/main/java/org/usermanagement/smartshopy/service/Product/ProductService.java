package org.usermanagement.smartshopy.service.Product;

import org.usermanagement.smartshopy.dto.request.CreateProductDTO;
import org.usermanagement.smartshopy.dto.request.UpdateProductDTO;
import org.usermanagement.smartshopy.dto.response.ProductDTO;

import java.util.List;

public interface ProductService {
    ProductDTO createProduct(CreateProductDTO dto);
    ProductDTO getProductById(Long id);
    List<ProductDTO> getAllProducts();
    List<ProductDTO> getActiveProducts();
    ProductDTO updateProduct(Long id, UpdateProductDTO dto);
    void deleteProduct(Long id);
    ProductDTO toggleProductStatus(Long id, boolean active);
}
