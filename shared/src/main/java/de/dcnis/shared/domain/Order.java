package de.dcnis.shared.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    private Integer id;
    private String product;
    private int quantity;
    private BigDecimal price;
    private BigDecimal totalAmount;

}
