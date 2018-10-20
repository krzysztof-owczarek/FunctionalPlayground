package app.priceModifiers

import spock.lang.Shared
import spock.lang.Specification

class PriceSpec extends Specification {

    @Shared priceService = new PriceService()

    def "test modifiers for user age"() {
        given: "PriceService and logged user, +18 years old"
        def offerContext = new HashMap()
        offerContext.put("age", "19")

        def specialOffer = new SpecialOffer(offerContext)
        specialOffer.createPriceModifier({ x -> Integer.valueOf(offerContext.get("age"))>18 }, { x -> x*0.5 })

        when: "price 100 with offers"
        def priceWithOffers = priceService.calculate(100, specialOffer)

        then: "expect price 50% lower"
        priceWithOffers == 50
    }

    def "test modifiers for 2 combined modifications"() {
        given: "PriceService and logged user, exactly 30 years old, fires two modifiers"
        def offerContext = new HashMap()
        offerContext.put("age", "30")

        def specialOffer = new SpecialOffer(offerContext)
        specialOffer.createPriceModifier({ x -> Integer.valueOf(offerContext.get("age"))>18 }, { x -> x*0.5 })
        specialOffer.createPriceModifier({ x -> Integer.valueOf(offerContext.get("age"))==30 }, { x -> x-10 })

        when: "price 100 with offers"
        def priceWithOffers = priceService.calculate(100, specialOffer)

        then: "expect price 50% and -10 lower"
        priceWithOffers == 40
    }

    def "test modifiers for 2 combined modifications and another 5\$ for every Bob!"() {
        given: "PriceService and logged user, exactly 30 years old, fires two modifiers"
        def offerContext = new HashMap()
        offerContext.put("age", "30")
        offerContext.put("name", "Bob")

        def specialOffer = new SpecialOffer(offerContext)
        specialOffer.createPriceModifier({ x -> Integer.valueOf(offerContext.get("age"))>18 }, { x -> x*0.5 })
        specialOffer.createPriceModifier({ x -> Integer.valueOf(offerContext.get("age"))==30 }, { x -> x-10 })
        specialOffer.createPriceModifier({ x -> "Bob".equals(offerContext.get("name")) }, { x -> x-5 })

        when: "price 100 with offers"
        def priceWithOffers = priceService.calculate(100, specialOffer)

        then: "expect price 50% and -10 lower and -5 lower"
        priceWithOffers == 35
    }
}
