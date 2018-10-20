package app.priceModifiers;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import javax.naming.OperationNotSupportedException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

class PriceService {

// RECURSIVE
//    public double calculate(double price) {
//        return calculatePriceWithOffers(price, priceModifiers);
//    }
//
//    private double calculatePriceWithOffers(double price, List<PriceModifier> priceModifiers) {
//        if (priceModifiers.isEmpty()) {
//            return price;
//        }
//
//        return calculatePriceWithOffers(priceModifiers.get(0).modify(price),
//                priceModifiers.subList(1, priceModifiers.size()));
//    }

    public double calculate(double price, SpecialOffer specialOffer) {
        return specialOffer.getPriceModifiers()
                .stream()
                .reduce(price,
                        (currentPrice, modifier) -> modifier.modify(currentPrice),
                        ((aDouble, aDouble2) -> {
                            throw new UnsupportedOperationException();
                        }));
    }
}

class SpecialOffer {
    private final Map<String, String> offerContext;

    @Getter
    private List<PriceModifier> priceModifiers;

    SpecialOffer(Map<String, String> offerContext) {
        this.offerContext = offerContext;
        this.priceModifiers = new LinkedList<>();
    }

    public void createPriceModifier(Predicate<Map<String, String>> condition, Function<Double, Double> modifier) {
        PriceModifier priceModifier = new PriceModifier(offerContext, condition, modifier);
        priceModifiers.add(priceModifier);
    }
}

@RequiredArgsConstructor
class PriceModifier {

    private final Map<String, String> offerContext;
    private final Predicate<Map<String, String>> condition;
    private final Function<Double, Double> modifier;

    double modify(double baseValue) {
        if (condition.test(offerContext)) {
            return modifier.apply(baseValue);
        }

        return baseValue;
    }
}

