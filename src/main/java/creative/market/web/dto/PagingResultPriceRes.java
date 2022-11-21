package creative.market.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PagingResultPriceRes<T> {

    private T result;
    private int totalPrice;
    private int curPageNum;
    private int totalNum;
}
