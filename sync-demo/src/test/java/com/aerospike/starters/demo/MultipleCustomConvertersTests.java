package com.aerospike.starters.demo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.aerospike.convert.AerospikeCustomConverters;
import org.springframework.data.aerospike.core.AerospikeTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/*
    Verifies that multiple AerospikeCustomConverters beans are aggregated correctly
    by the starter's auto-configuration (AerospikeDataConfigurationSupport.customConversions).
    Each bean contributes its own group of converters independently.
*/
@SpringBootTest
public class MultipleCustomConvertersTests {

    @TestConfiguration
    static class ConverterConfig {

        @Bean
        public AerospikeCustomConverters priceConverters() {
            return new AerospikeCustomConverters(List.of(
                    PriceConverters.PriceToMapConverter.INSTANCE,
                    PriceConverters.MapToPriceConverter.INSTANCE
            ));
        }

        @Bean
        public AerospikeCustomConverters addressConverters() {
            return new AerospikeCustomConverters(List.of(
                    AddressConverters.AddressToMapConverter.INSTANCE,
                    AddressConverters.MapToAddressConverter.INSTANCE
            ));
        }
    }

    @Autowired
    private AerospikeTemplate template;

    @AfterEach
    void tearDown() {
        template.findAll(Product.class).forEach(template::delete);
    }

    @Test
    void savesAndReadsProductWithBothConverters() {
        Product product = new Product("p1", "Widget",
                new Price(19.99, "USD"),
                new Address("123 Main St", "Springfield", "US"));

        template.save(product);
        Product result = template.findById("p1", Product.class);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Widget");
        assertThat(result.getPrice()).isEqualTo(new Price(19.99, "USD"));
        assertThat(result.getAddress()).isEqualTo(new Address("123 Main St", "Springfield", "US"));
    }

    @Test
    void savesAndReadsMultipleProducts() {
        Product product1 = new Product("p1", "Widget",
                new Price(9.99, "EUR"),
                new Address("1 Rue de Rivoli", "Paris", "FR"));
        Product product2 = new Product("p2", "Gadget",
                new Price(29.99, "GBP"),
                new Address("10 Downing St", "London", "UK"));

        template.save(product1);
        template.save(product2);

        Product result1 = template.findById("p1", Product.class);
        Product result2 = template.findById("p2", Product.class);

        assertThat(result1.getPrice()).isEqualTo(new Price(9.99, "EUR"));
        assertThat(result1.getAddress().city()).isEqualTo("Paris");

        assertThat(result2.getPrice().currency()).isEqualTo("GBP");
        assertThat(result2.getAddress().country()).isEqualTo("UK");
    }

    @Test
    void updatesProductWithConvertedFields() {
        Product product = new Product("p1", "Widget",
                new Price(19.99, "USD"),
                new Address("123 Main St", "Springfield", "US"));
        template.save(product);

        product.setPrice(new Price(24.99, "CAD"));
        product.setAddress(new Address("456 Elm St", "Toronto", "CA"));
        template.save(product);

        Product result = template.findById("p1", Product.class);
        assertThat(result.getPrice()).isEqualTo(new Price(24.99, "CAD"));
        assertThat(result.getAddress()).isEqualTo(new Address("456 Elm St", "Toronto", "CA"));
    }
}
