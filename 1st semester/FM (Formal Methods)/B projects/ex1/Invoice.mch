/* Invoice
 * Author: Adrian
 * Creation date: 24/01/2022
 */
MACHINE
    Invoice
    
USES
    Client
    
SETS
    INVOICE
    
VARIABLES
    invoices,
    invoiceClient,
    invoiceDiscount,
    invoiceAllowed,
    invoiceTotal
    
INVARIANT
    invoices <: INVOICE &
    invoiceClient : invoices --> clients &
    invoiceDiscount : invoices --> NATURAL &
    invoiceAllowed : invoices --> NATURAL &
    invoiceTotal : invoices --> NATURAL &
    !invc . (invc : invoices => 
        invoiceDiscount(invc) = clientDiscount(invoiceClient(invc)) &
        invoiceAllowed(invc) = clientAllowance(invoiceClient(invc)) &
        invoiceTotal(invc) <= clientAllowance(invoiceClient(invc))
    )
    
INITIALISATION
    invoices,
    invoiceClient,
    invoiceDiscount,
    invoiceAllowed,
    invoiceTotal := {}, {}, {}, {}, {}
    
OPERATIONS
    newInvoice <-- createInvoice(clientVal) = 
    PRE
        clientVal : clients &
        clientCategory(clientVal) /= dubious
    THEN
        ANY invc
        WHERE invc : INVOICE - invoices
        THEN
            invoices := invoices \/ { invc } ||
            invoiceClient(invc) := clientVal ||
            invoiceDiscount(invc) := clientDiscount(clientVal) ||
            invoiceAllowed(invc) := clientAllowance(clientVal) ||
            invoiceTotal(invc) := 0 || 
            newInvoice := invc
        END
    END;
    
    deleteInvoice(invoiceVal) = 
    PRE
        invoiceVal : invoices
    THEN
        invoices := invoices - { invoiceVal } ||
        invoiceClient := { invoiceVal } <<| invoiceClient ||
        invoiceDiscount := { invoiceVal } <<| invoiceDiscount ||
        invoiceAllowed := { invoiceVal } <<| invoiceAllowed ||
        invoiceTotal := { invoiceVal } <<| invoiceTotal
    END

END
