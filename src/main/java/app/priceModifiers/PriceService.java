package app.priceModifiers;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
class PriceService {
    private OfferContext offerContext;
    private List<PriceModifier> priceModifiers = new LinkedList<>();

    public PriceService(User currentUser) {
        offerContext = new OfferContext(currentUser);
        priceModifiers.add(new PriceModifier(offerContext, x -> offerContext.getCurrentUser().age>18, x -> x*0.5));
        priceModifiers.add(new PriceModifier(offerContext, x -> offerContext.getCurrentUser().age==30, x -> x-10));
    }

    public double calculate(double price) {
        return calculatePriceWithOffers(price, priceModifiers);
    }

    private double calculatePriceWithOffers(double price, List<PriceModifier> priceModifiers) {
        if (priceModifiers.isEmpty()) {
            return price;
        }

        return calculatePriceWithOffers(priceModifiers.get(0).modify(price),
                priceModifiers.subList(1, priceModifiers.size()));
    }
}

@RequiredArgsConstructor
class PriceModifier {

    private final OfferContext offerContext;
    private final Predicate<OfferContext> condition;
    private final Function<Double, Double> modifier;

    double modify(double baseValue) {
        if (condition.test(offerContext)) {
            return modifier.apply(baseValue);
        }

        return baseValue;
    }
}

@Getter
@RequiredArgsConstructor
class OfferContext {
    private final User currentUser;
}

@AllArgsConstructor
class User {
    int age;
}


