Highly inspired by this article. https://medium.com/@aleksandaradulovic/a-functional-composable-way-to-implement-chain-of-responsibility-5c42664fca93

I thought the solution presented in the article was interesting, 
so I decided to try creating my own project with a similar setup. 
Since the author seems to have used Zuul filters, which appear 
to be deprecated, I chose to use Spring API Gateway MVC instead.

### Design patterns

Chain of Responsibility is often used in conjunction with Composite which is also used in this case.
https://refactoring.guru/design-patterns/chain-of-responsibility


Requires Java 21 and Maven.