package org.esrakonya.backend.common.core.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSoldEvent {
    private Long productId;
    private Integer quantitySold;
    private Integer remainingStock;
    private LocalDateTime timestamp;
}
