package ru.alsushop.AlsuShop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.alsushop.AlsuShop.model.Person;
import ru.alsushop.AlsuShop.model.Product;
import ru.alsushop.AlsuShop.model.ProductType;
import ru.alsushop.AlsuShop.repository.ProductsRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ProductsService {
    private final ProductsRepository productsRepository;

    @Autowired
    public ProductsService(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    public List<Product> findAll(String sort){
        return productsRepository.findAll(Sort.by(sort));
    }

    public List<Product> findAll(int offset, int limit, String sort){
        return productsRepository.findAll(PageRequest.of(offset,limit, Sort.by(sort))).getContent();
    }

    public Product findOne(int id){
        Optional<Product> optionalProduct = productsRepository.findById(id);
        return optionalProduct.orElse(null);
    }

    public List<Product> searchByProductName(String query){
        return productsRepository.findByNameStartingWith(query);
    }

    @Transactional
    public void save(Product product) {
        product.setAddedAt(new Date());
        productsRepository.save(product);
    }

    @Transactional
    public void update(int id, Product updateProduct) {
        Product productToBeUpdated = productsRepository.findById(id).get();

        updateProduct.setId(id);
        updateProduct.setAddedAt(productToBeUpdated.getAddedAt());
        // updateProduct.setProductType(productToBeUpdated.getProductType()); // чтобы не терялась связь при обновлении

        // TODO

        updateProduct.setOrders(productToBeUpdated.getOrders());
        productsRepository.save(updateProduct);
    }

    @Transactional
    public void delete(int id) {
        productsRepository.deleteById(id);
    }

    public ProductType getProductType(int id) {
        Product product = productsRepository.findById(id).orElse(null);

        return product.getProductType();
    }

/*    @Transactional
    public void setItemOwner(int id, Item updateItem) {
        updateItem.setOwner(Person);
        itemRepositories.save(updateItem);
    }*/

/*    @Transactional
    public void release(int id){
        productsRepository.findById(id).ifPresent(
                // TODO
                item -> {
                    item.setOwner(null);
                    item.setTakenAt(null);
                });
    }

    @Transactional
    public void assign(int id, Person selectedPerson) {
        productsRepository.findById(id).ifPresent(
                // TODO
                item -> {
                    item.setOwner(selectedPerson);
                    item.setTakenAt(new Date());
                });
    }*/

    // TODO
/*    public List<Product> findByOrder(Person person) {
        return productsRepository.findByOwner(person);
    }

    public List<Product> findByName(String name) {
        return productsRepository.findByName(name);
    }*/
}
