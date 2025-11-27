package org.usermanagement.smartshopy.service.Product;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.usermanagement.smartshopy.dto.request.CreateProductDTO;
import org.usermanagement.smartshopy.dto.request.UpdateProductDTO;
import org.usermanagement.smartshopy.dto.response.ProductDTO;
import org.usermanagement.smartshopy.entity.Product;
import org.usermanagement.smartshopy.exception.NotFoundException;
import org.usermanagement.smartshopy.mapper.ProductMapper;
import org.usermanagement.smartshopy.repository.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductDTO createProduct(CreateProductDTO dto) {

        Product product = productMapper.toEntity(dto);
        product.setActive(true);
        productRepository.save(product);
        return productMapper.toDTO(product);
    }

    public ProductDTO getProductById(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Produit introuvable avec l'ID : " + id));

        return productMapper. toDTO(product);
    }

    public List<ProductDTO> getAllProducts() {

        return productRepository.findAll().stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> getActiveProducts() {

        return productRepository.findByActiveTrue().stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ProductDTO updateProduct(Long id, UpdateProductDTO dto) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Produit introuvable avec l'ID : " + id));

        productMapper.updateEntityFromDTO(dto, product);

        productRepository.save(product);

        return productMapper.toDTO(product);
    }

    public void deleteProduct(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Produit introuvable avec l'ID : " + id));
        productRepository.delete(product);

    }

    public ProductDTO toggleProductStatus(Long id, boolean active) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Produit introuvable avec l'ID : " + id));

        product.setActive(active);
        productRepository.save(product);

        return productMapper. toDTO(product);
    }















}
