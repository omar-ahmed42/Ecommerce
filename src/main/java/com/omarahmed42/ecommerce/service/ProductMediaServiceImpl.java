package com.omarahmed42.ecommerce.service;

import com.omarahmed42.ecommerce.exception.ProductMediaNotFoundException;
import com.omarahmed42.ecommerce.model.ProductMedia;
import com.omarahmed42.ecommerce.repository.ProductMediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductMediaServiceImpl implements ProductMediaService {

    private final ProductMediaRepository productMediaRepository;

    @Autowired
    public ProductMediaServiceImpl(ProductMediaRepository productMediaRepository) {
        this.productMediaRepository = productMediaRepository;
    }

    @Transactional
    @Override
    public void addProductMedia(ProductMedia productMedia) {
        // Call the appropriate service provider to store the media files (Such as Amazon S3) - in the controller -
        productMediaRepository.save(productMedia); // Store the url for the corresponding product
    }

    @Transactional
    @Override
    public void addProductMedia(List<ProductMedia> productMedia) {
        productMediaRepository.saveAll(productMedia);
    }

    @Transactional
    @Override
    public void deleteProductMedia(ProductMedia productMedia) {
        productMediaRepository.save(productMedia);
    }

    @Transactional
    @Override
    public void deleteProductMedia(List<ProductMedia> productMedia) {
        productMediaRepository.deleteAll(productMedia);
    }

    @Transactional
    @Override
    public void deleteProductMediaByProductId(byte[] productId) {
        productMediaRepository.deleteAllByProductId(productId);
    }

    @Transactional
    @Override
    public void updateProductMedia(ProductMedia productMedia) {
        productMediaRepository
                .findById(productMedia.getId())
                .ifPresentOrElse(
                        presentMedia -> productMediaRepository.save(productMedia),
                        () -> {
                            throw new ProductMediaNotFoundException("Product media not found");
                        });

    }


}
