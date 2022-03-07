package de.dcnis.shared.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    private Integer id;
    private Integer senderUserId;
    private Integer receiverUserId;
    private BigDecimal amount;

}
