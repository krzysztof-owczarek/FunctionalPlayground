package app.priceModifiers

import spock.lang.Specification

class PriceSpec extends Specification {
    def "test modifiers"() {
        given: "PriceService and logged user, +18 years old"
        def currentUser = new User(19)
        def priceService = new PriceService(currentUser)

        when: "price 100 with offers"
        def priceWithOffers = priceService.calculate(100)

        then: "expect price 50% lower"
        priceWithOffers == 50
    }

    def "test modifiers2"() {
        given: "PriceService and logged user, exactly 30 years old, fires two modifiers"
        def currentUser = new User(30)
        def priceService = new PriceService(currentUser)

        when: "price 100 with offers"
        def priceWithOffers = priceService.calculate(100)

        then: "expect price 50% and -10 lower"
        priceWithOffers == 40
    }
}
