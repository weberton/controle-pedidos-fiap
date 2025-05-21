package br.com.fiap.controlepedidos.core.application.ports;

import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.Category;
import br.com.fiap.controlepedidos.core.domain.entities.Cart;
import br.com.fiap.controlepedidos.core.domain.entities.CartItem;
import br.com.fiap.controlepedidos.core.domain.entities.Customer;
import br.com.fiap.controlepedidos.core.domain.entities.Product;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class CartsRepositoryTest {
    @Autowired
    private CartsRepository cartsRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private Flyway flyway;
    private List<Product> products;

    @BeforeEach
    void setup() {
        flyway.clean();
        flyway.migrate();
        products = createProducts();
    }


    @Test
    void save_whenThereIsOneItem_shouldSaveCartWithOneItem() {
        //Given
        var coke = products.getFirst();
        var item1 = CartItem.builder()
                .product(coke)
                .priceCents(coke.getPrice())
                .quantity(2)
                .build();
        var items = List.of(item1);

        var cart = Cart.builder()
                .items(items)
                .build();

        var expectedCartTotalCents = cart.getItems().stream().mapToInt(CartItem::calculateSubTotalCents).sum();
        var expectedItemSubTotalCents = item1.getPriceCents() * item1.getQuantity();
        //When
        var savedCart = this.cartsRepository.save(cart);
        var findedCart = this.cartsRepository.findById(savedCart.getId()).orElseThrow();

        //Then
        assertThat(findedCart).isEqualTo(savedCart);
        assertThat(savedCart.getId()).isNotNull();
        assertThat(savedCart.getCustomer()).isNull();
        assertThat(savedCart.getTotalCents()).isEqualTo(expectedCartTotalCents);
        assertThat(savedCart.getItems()).hasSize(1);

        var savedItem = savedCart.getItems().getFirst();
        assertThat(savedItem.getId()).isNotNull();
        assertThat(savedItem.getProduct()).isEqualTo(coke);
        assertThat(savedItem.getPriceCents()).isEqualTo(item1.getPriceCents());
        assertThat(savedItem.getQuantity()).isEqualTo(item1.getQuantity());
        assertThat(savedItem.getSubtotalCents()).isEqualTo(expectedItemSubTotalCents);
    }

    @Test
    void save_whenThereAreMultipleItems_shouldSaveCartWithMultipleItems() {
        //Given
        var coke = products.getFirst();
        var bigMac = products.getLast();

        var cartItemCoke = CartItem.builder()
                .product(coke)
                .priceCents(coke.getPrice())
                .quantity(2)
                .build();

        var cartItemBigMac = CartItem.builder()
                .product(bigMac)
                .priceCents(bigMac.getPrice())
                .quantity(2)
                .build();

        var cart = new Cart();
        cart.addItem(cartItemCoke);
        cart.addItem(cartItemBigMac);

        var expectedCartTotalCents = cart.getItems().stream().mapToInt(CartItem::calculateSubTotalCents).sum();
        var expectedCokeSubTotalCents = cartItemCoke.getPriceCents() * cartItemCoke.getQuantity();
        var expectedBigMacSubTotalCents = cartItemBigMac.getPriceCents() * cartItemBigMac.getQuantity();

        //When
        var savedCart = this.cartsRepository.save(cart);
        var findedCart = this.cartsRepository.findById(savedCart.getId()).orElseThrow();

        //Then
        assertThat(findedCart).isEqualTo(savedCart);
        assertThat(savedCart.getId()).isNotNull();
        assertThat(savedCart.getCustomer()).isNull();
        assertThat(savedCart.getTotalCents()).isEqualTo(expectedCartTotalCents);
        assertThat(savedCart.getItems()).hasSize(2);

        var savedItemCoke = savedCart.getItems().getFirst();
        assertThat(savedItemCoke.getId()).isNotNull();
        assertThat(savedItemCoke.getProduct()).isEqualTo(coke);
        assertThat(savedItemCoke.getPriceCents()).isEqualTo(cartItemCoke.getPriceCents());
        assertThat(savedItemCoke.getQuantity()).isEqualTo(cartItemCoke.getQuantity());
        assertThat(savedItemCoke.getSubtotalCents()).isEqualTo(expectedCokeSubTotalCents);

        var savedItemBigMac = savedCart.getItems().getLast();
        assertThat(savedItemBigMac.getId()).isNotNull();
        assertThat(savedItemBigMac.getProduct()).isEqualTo(bigMac);
        assertThat(savedItemBigMac.getPriceCents()).isEqualTo(cartItemBigMac.getPriceCents());
        assertThat(savedItemBigMac.getQuantity()).isEqualTo(cartItemCoke.getQuantity());
        assertThat(savedItemBigMac.getSubtotalCents()).isEqualTo(expectedBigMacSubTotalCents);
    }

    @Test
    void save_whenThereIsCustomer_saveCartWithCustomer() {
        //Given
        var customer = createCustomer();
        var coke = products.getFirst();
        var item1 = CartItem.builder()
                .product(coke)
                .priceCents(coke.getPrice())
                .quantity(2)
                .build();
        var items = List.of(item1);

        var cart = Cart.builder()
                .items(items)
                .customer(customer)
                .build();

        //When
        var savedCart = this.cartsRepository.save(cart);
        var findedCart = this.cartsRepository.findById(savedCart.getId()).orElseThrow();

        //Then
        assertThat(findedCart.getCustomer()).isEqualTo(customer);
    }

    @Test
    void delete_whenCartIsDeleted_shouldNotFindCart() {
        //Given
        var coke = products.getFirst();
        var item1 = CartItem.builder()
                .product(coke)
                .priceCents(coke.getPrice())
                .quantity(2)
                .build();
        var items = List.of(item1);

        var cart = Cart.builder()
                .items(items)
                .build();

        //when
        var savedCart = this.cartsRepository.save(cart);
        this.cartsRepository.delete(savedCart);

        //then
        var foundCart = this.cartsRepository.findById(savedCart.getId());
        assertThat(foundCart).isEmpty();
    }

    private Customer createCustomer() {
        var newCustomer = Customer.builder()
                .cpf("68141754017")
                .name("Joao da Silva")
                .email("joao@gmail.com")
                .build();
        return this.customerRepository.save(newCustomer);
    }

    private List<Product> createProducts() {
        Product newProduct1 = Product.builder()
                .price(550) //$5,50
                .name("Coca Cola")
                .category(Category.BEBIDA)
                .description("Coca Cola - 2 Litros")
                .build();

        Product newProduct2 = Product.builder()
                .price(2250) //$22,5
                .name("Big Mac")
                .category(Category.LANCHE)
                .description("he Big Mac is a double-decker hamburger made by McDonald's")
                .build();
        return this.productRepository.saveAll(List.of(newProduct1, newProduct2));
    }
}
