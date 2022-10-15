package creative.market.service.query;

import creative.market.domain.category.KindGrade;
import creative.market.domain.product.Product;
import creative.market.repository.ProductRepository;
import creative.market.repository.dto.ProductSearchConditionReq;
import creative.market.service.dto.ProductDetailRes;
import creative.market.service.dto.ProductShortInfoRes;
import creative.market.util.WholesaleAndRetailUtils;
import creative.market.repository.dto.LatestRetailAndWholesaleDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductQueryService {
    private final ProductRepository productRepository;
    private final WholesaleAndRetailUtils wholesaleAndRetailUtils;
    // 신뢰 등급, 백분위 얻는 서비스 있어야 함

    //신뢰 등급, 백분위 얻는 코드 추가해야 함
    public List<ProductShortInfoRes> productShortInfoList(ProductSearchConditionReq condition) {
        List<Product> findProducts = productRepository.findProductByCondition(condition);

        //신뢰등급, 백분위 찾아오는 함수 넣어야 함
        return findProducts.stream()
                .map(product -> new ProductShortInfoRes(product, "미완성등급", 0)) // 신뢰등급, 백분위 찾아오는 함수 넣어야 함.
                .collect(Collectors.toList());
    }

    public ProductDetailRes productDetailInfo(Long productId) {
        Product product = productRepository.findByIdWithSellerAndKindGrade(productId)
                .orElseThrow(() -> new NoSuchElementException("상품이 존재하지 않습니다."));
        KindGrade kindGrade = product.getKindGrade();

        LatestRetailAndWholesaleDTO retailAndWholesalePriceResult = wholesaleAndRetailUtils.getLatestPriceInfo(kindGrade);// 최근 도소매 정보(단위 변환 + 도소매 단위 다른 경우 처리)
        //신뢰등급, 백분위 찾아오는 함수 넣어야 함
        String sellerRank = "미완성 등급";
        int sellerPercent = 0;
        int productAvgPrice = productRepository.findProductAvgPrice(kindGrade.getId()).intValue();

        return new ProductDetailRes(product,sellerRank,sellerPercent,productAvgPrice,retailAndWholesalePriceResult);
    }
}
