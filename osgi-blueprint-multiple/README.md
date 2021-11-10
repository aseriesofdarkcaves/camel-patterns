# osgi-blueprint-multiple

This contains bits of PoC code to figure out various ways you can structure RouteBuilder classes.

## Multiple CamelContexts via Blueprint (not officially supported, won't work in Camel 3)

- Create classes that extend RouteBuilder as many times as required to define various Routes
- Bootstrap these using multiple CamelContext elements in the blueprint file
- Remember that direct isn't going to work here because it can only communicate within a single CamelContext, so you'll
  need to use something like the direct-vm component

## Single CamelContext via Blueprint (preferred)

- Create classes that extend RouteBuilder as many times as required to define various Routes
- Create a "super" RouteBuilder that adds the other RouteBuilder classes to the "super" CamelContext via the addRoutes(
  RoutesBuilder) method
- Bootstrap the "super" RouteBuilder as a single CamelContext element in the blueprint file
- Since all Routes were added to a single CamelContext, direct works just fine
