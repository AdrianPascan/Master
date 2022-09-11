/* Line
 * Author: Adrian
 * Creation date: 24/01/2022
 */
MACHINE
    Line
    
USES
    Product, Invoice
    
SETS
    LINE
    
VARIABLES
    lines,
    lineInvoice,
    lineArticle,
    lineQuantity,
    lineUnitCost
    
INVARIANT
    lines <: LINE &
    lineInvoice : lines --> invoices &
    lineArticle : lines --> products &
    lineQuantity : lines --> NATURAL1 &
    lineUnitCost : lines --> NATURAL &
    !ln . (ln : lines => lineUnitCost(ln) = productPrice(lineArticle(ln)) )
    
INITIALISATION
    lines,
    lineInvoice,
    lineArticle,
    lineQuantity,
    lineUnitCost := {}, {}, {}, {}, {}
    
OPERATIONS
    newLine <-- createLine(invoiceVal, articleVal, quantityVal) =
    PRE
        invoiceVal : invoices &
        articleVal : products & 
        quantityVal : NATURAL1 &
        (productStatus(articleVal) = available or productSubstitute(articleVal) : products) &
        (100 - invoiceDiscount(invoiceVal)) / 100 * (invoiceTotal(invoiceVal) + productPrice(articleVal) * quantityVal) <= invoiceAllowed(invoiceVal)
    THEN
        ANY ln
        WHERE ln : LINE - lines
        THEN
            lines := lines \/ { ln } ||
            lineInvoice(ln) := invoiceVal ||
            invoiceTotal(invoiceVal) := invoiceTotal(invoiceVal) + productPrice(articleVal) * quantityVal ||
            IF productStatus(articleVal) = available 
            THEN
                lineArticle(ln) := articleVal
            ELSE
                lineArticle(ln) := productSubstitute(articleVal)
            END ||
            lineQuantity(ln) := quantityVal ||
            lineUnitCost(ln) := productPrice(articleVal) ||
            newLine := ln
        END
    END;
    
    deleteLinesOfInvoice(invoiceVal) =
    PRE
        invoiceVal : invoices
    THEN
        lines := lines - lineInvoice~[{ invoiceVal }] ||
        lineInvoice := lineInvoice |>> { invoiceVal } ||
        lineArticle := lineInvoice~[{ invoiceVal }] <<| lineArticle ||
        lineQuantity := lineInvoice~[{ invoiceVal }] <<| lineQuantity ||
        lineUnitCost := lineInvoice~[{ invoiceVal }] <<| lineUnitCost
    END

END
