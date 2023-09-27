package ru.clothingstore.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clothingstore.model.cart.Cart;
import ru.clothingstore.repository.CartRepository;
import ru.clothingstore.service.Impl.CartServiceImpl;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @InjectMocks
    private CartServiceImpl cartService;

    @Mock
    private CartRepository cartRepository;

    @Test
    void updateCart() {
        Cart cart = new Cart();
        cartService.updateCart(cart);

        verify(cartRepository, times(1)).save(cart);
    }
}