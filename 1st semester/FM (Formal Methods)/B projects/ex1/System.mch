/* System
 * Author: Adrian
 * Creation date: 24/01/2022
 */
MACHINE
    System
    
INCLUDES
    Line, Invoice, Client, Product
    
PROMOTES
    createClient,
    createProduct,
    createInvoice,
    createLine

OPERATIONS
    deleteLinesAndInvoice(invoiceVal) = 
    PRE
        invoiceVal : invoices
    THEN
        deleteLinesOfInvoice(invoiceVal) ||
        deleteInvoice(invoiceVal)
    END

END
