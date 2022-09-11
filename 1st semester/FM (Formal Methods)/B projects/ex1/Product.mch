/* Product
 * Author: Adrian
 * Creation date: 24/01/2022
 */
MACHINE
    Product
    
SETS
    PRODUCT; STATUS = {available, sold_out}
    
VARIABLES
    products, 
    productPrice,
    productStatus,
    productSubstitute
    
INVARIANT
    products <: PRODUCT &
    productPrice : products --> NATURAL &
    productStatus : products --> STATUS &
    productSubstitute : products --> PRODUCT &
    !prdct . (prdct : products => 
        prdct /= productSubstitute(prdct) & 
        (productSubstitute(prdct) : PRODUCT - products or productStatus(productSubstitute(prdct)) = available)
    )
 
INITIALISATION
    products,
    productPrice,
    productStatus,
    productSubstitute := {}, {}, {}, {}
    
OPERATIONS
    newProduct <-- createProduct(priceVal, statusVal, substituteVal) = 
    PRE
        priceVal : NATURAL & 
        statusVal : STATUS &
        substituteVal : PRODUCT &
        (substituteVal : PRODUCT - products or productStatus(substituteVal) = available)
    THEN
        ANY prdct
        WHERE prdct : PRODUCT - products
        THEN
            products := products \/ { prdct } ||
            productPrice(prdct) := priceVal ||
            productStatus(prdct) := statusVal ||
            productSubstitute(prdct) := substituteVal ||
            newProduct := prdct
        END

    END

END
