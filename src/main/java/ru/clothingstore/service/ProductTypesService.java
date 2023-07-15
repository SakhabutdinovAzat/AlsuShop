package ru.clothingstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clothingstore.model.Product;
import ru.clothingstore.model.ProductType;
import ru.clothingstore.repository.ProductTypesRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ProductTypesService {

    private final ProductTypesRepository productTypesRepository;

    @Autowired
    public ProductTypesService(ProductTypesRepository productTypesRepository) {
        this.productTypesRepository = productTypesRepository;
    }

    public List<ProductType> findAll(){
        return productTypesRepository.findAll();
    }

    public List<ProductType> findAll(String sort){
        return productTypesRepository.findAll(Sort.by(sort));
    }

    public List<ProductType> findAll(int offset, int limit, String sort){
        return productTypesRepository.findAll(PageRequest.of(offset,limit, Sort.by(sort))).getContent();
    }

    public ProductType findOne(int id){
        Optional<ProductType> optionalProductType = productTypesRepository.findById(id);
        return optionalProductType.orElse(null);
    }

    public List<ProductType> searchByProductTypeName(String query){
        return productTypesRepository.findByNameStartingWith(query);
    }

    @Transactional
    public void save(ProductType productType) {
        productTypesRepository.save(productType);
    }

    @Transactional
    public void update(int id, ProductType updateProductType) {
        ProductType productTypeToBeUpdated = productTypesRepository.findById(id).get();

        updateProductType.setId(id);
        updateProductType.setProducts(productTypeToBeUpdated.getProducts()); // чтобы не терялась связь при обновлении

        productTypesRepository.save(updateProductType);
    }

    @Transactional
    public void delete(int id) {
        productTypesRepository.deleteById(id);
    }

    public List<Product> getProductType(int id) {

        return productTypesRepository.findById(id).get().getProducts();
    }


}
