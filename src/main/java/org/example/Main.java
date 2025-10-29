package org.example;

import funciones.FuncionApp;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("example-unit");
            EntityManager em = emf.createEntityManager();

            // Persistir la entidad UnidadMedida en estado "gestionada"
            em.getTransaction().begin();
            // Crear una nueva entidad UnidadMedida en estado "nueva"
            UnidadMedida unidadMedida = UnidadMedida.builder()
                    .denominacion("Kilogramo")
                    .build();
            UnidadMedida unidadMedidapote = UnidadMedida.builder()
                    .denominacion("pote")
                    .build();

            em.persist(unidadMedida);
            em.persist(unidadMedidapote);


            // Crear una nueva entidad Categoria en estado "nueva"
            Categoria categoria = Categoria.builder()
                    .denominacion("Frutas")
                    .esInsumo(true)
                    .build();

            // Crear una nueva entidad Categoria en estado "nueva"
            Categoria categoriaPostre = Categoria.builder()
                    .denominacion("Postre")
                    .esInsumo(false)
                    .build();

            // Persistir la entidad Categoria en estado "gestionada"

            em.persist(categoria);
            em.persist(categoriaPostre);


            // Crear una nueva entidad ArticuloInsumo en estado "nueva"
            ArticuloInsumo articuloInsumo = ArticuloInsumo.builder()
                    .denominacion("Manzana").codigo(FuncionApp.generateUniqueCode())
                    .precioCompra(1.5)
                    .precioVenta(5d)
                    .stockActual(100)
                    .stockMaximo(200)
                    .esParaElaborar(true)
                    .unidadMedida(unidadMedida)
                    .build();


            ArticuloInsumo articuloInsumoPera = ArticuloInsumo.builder()
                    .denominacion("Pera").codigo(FuncionApp.generateUniqueCode())
                    .precioCompra(2.5)
                    .precioVenta(10d)
                    .stockActual(130)
                    .stockMaximo(200)
                    .esParaElaborar(true)
                    .unidadMedida(unidadMedida)
                    .build();

            // Persistir la entidad ArticuloInsumo en estado "gestionada"

            em.persist(articuloInsumo);
            em.persist(articuloInsumoPera);

            Imagen manza1 = Imagen.builder().denominacion("Manzana Verde").
                    build();
            Imagen manza2 = Imagen.builder().denominacion("Manzana Roja").
                    build();

            Imagen pera1 = Imagen.builder().denominacion("Pera Verde").
                    build();
            Imagen pera2 = Imagen.builder().denominacion("Pera Roja").
                    build();




            // Agregar el ArticuloInsumo a la Categoria
            categoria.getArticulos().add(articuloInsumo);
            categoria.getArticulos().add(articuloInsumoPera);
            // Actualizar la entidad Categoria en la base de datos

         // em.merge(categoria);

            // Crear una nueva entidad ArticuloManufacturadoDetalle en estado "nueva"
            ArticuloManufacturadoDetalle detalleManzana = ArticuloManufacturadoDetalle.builder()
                    .cantidad(2)
                    .articuloInsumo(articuloInsumo)
                    .build();


            ArticuloManufacturadoDetalle detallePera = ArticuloManufacturadoDetalle.builder()
                    .cantidad(2)
                    .articuloInsumo(articuloInsumoPera)
                    .build();

            // Crear una nueva entidad ArticuloManufacturado en estado "nueva"
            ArticuloManufacturado articuloManufacturado = ArticuloManufacturado.builder()
                    .denominacion("Ensalada de frutas")
                    .descripcion("Ensalada de manzanas y peras ")
                    .codigo(FuncionApp.generateUniqueCode())
                    .precioVenta(150d)
                    .tiempoEstimadoMinutos(10)
                    .preparacion("Cortar las frutas en trozos pequeños y mezclar")
                    .unidadMedida(unidadMedidapote)
                    .build();

            articuloManufacturado.getImagenes().add(manza1);
            articuloManufacturado.getImagenes().add(pera1);

            categoriaPostre.getArticulos().add(articuloManufacturado);
            // Crear una nueva entidad ArticuloManufacturadoDetalle en estado "nueva"

            // Agregar el ArticuloManufacturadoDetalle al ArticuloManufacturado
            articuloManufacturado.getDetalles().add(detalleManzana);
            articuloManufacturado.getDetalles().add(detallePera);
            // Persistir la entidad ArticuloManufacturado en estado "gestionada"
            categoriaPostre.getArticulos().add(articuloManufacturado);
            em.persist(articuloManufacturado);
            em.getTransaction().commit();

            // modificar la foto de manzana roja
            em.getTransaction().begin();
            articuloManufacturado.getImagenes().add(manza2);
            // también agrego la imagen de pera roja para usar la variable y evitar advertencia
            articuloManufacturado.getImagenes().add(pera2);
            em.merge(articuloManufacturado);
            em.getTransaction().commit();

            //creo y guardo un cliente
            em.getTransaction().begin();
            Cliente cliente = Cliente.builder()
                    .cuit(FuncionApp.generateRandomCUIT())
                    .razonSocial("Juan Perez")
                    .build();
            em.persist(cliente);
            em.getTransaction().commit();
            
            // Obtener el ID del cliente creado
            Long clienteId = cliente.getId();

            //creo y guardo una factura
            em.getTransaction().begin();

            FacturaDetalle detalle1 = new FacturaDetalle(3, articuloInsumo);
            detalle1.calcularSubTotal();
            FacturaDetalle detalle2 = new FacturaDetalle(3, articuloInsumoPera);
            detalle2.calcularSubTotal();
            FacturaDetalle detalle3 = new FacturaDetalle(3, articuloManufacturado);
            detalle3.calcularSubTotal();

            Factura factura = Factura.builder()
                    .puntoVenta(2024)
                    .fechaAlta(new Date())
                    .fechaComprobante(LocalDate.now().minusMonths(1))
                    .cliente(cliente)
                    .nroComprobante(FuncionApp.generateRandomNumber())
                    .build();
            factura.addDetalleFactura(detalle1);
            factura.addDetalleFactura(detalle2);
            factura.addDetalleFactura(detalle3);
            factura.calcularTotal();

            em.persist(factura);
            em.getTransaction().commit();

            // Consultas JPQL
            // 1. Listar todos los clientes
            Query query1 = em.createQuery("SELECT c FROM Cliente c");
            List<Cliente> clientes = query1.getResultList();

            System.out.println("\nLista de todos los clientes:");
            clientes.forEach(System.out::println);

            // 2. Listar todas las facturas del ultimo mes
            Query query2 = em.createQuery("SELECT f FROM Factura f WHERE f.fechaComprobante BETWEEN :inicioMes AND :finMes");
            query2.setParameter("inicioMes", LocalDate.now().minusMonths(1).withDayOfMonth(1));
            query2.setParameter("finMes", LocalDate.now().withDayOfMonth(1).minusDays(1));

            List<Factura> facturas = query2.getResultList();
            System.out.println("\nLista de todos los facturas:");
            facturas.forEach(System.out::println);

            // 3. El cliente que mas facturas ha generado
            Query query3 = em.createQuery("SELECT c.id, c.razonSocial, COUNT(f) AS cantidadFacturas FROM Cliente c JOIN c.facturas f GROUP BY c.id, c.razonSocial ORDER BY cantidadFacturas DESC");

            Object[] resultado = (Object[]) query3.getResultList().get(0);
            System.out.println("\nCliente: " + resultado[1]);

            // 4. Lista de los productos mas vendidos
            Query query4 = em.createQuery("SELECT a.id, a.denominacion, SUM(fd.cantidad) AS cantidadVendida FROM FacturaDetalle fd JOIN fd.articulo a GROUP BY a.id, a.denominacion ORDER BY cantidadVendida DESC");

            List<Object[]> detalles = query4.getResultList();
            System.out.println("\nArticulos mas vendidos: ");
            detalles.forEach(detalle -> System.out.println("Denominacion: " + detalle[1]));


            // 5. Facturas de los ultimos 3 meses de un cliente especifico
            Query query5 = em.createQuery("SELECT f FROM Cliente c JOIN c.facturas f WHERE c.id = :id AND f.fechaComprobante BETWEEN :inicioPeriodo AND :finPeriodo");
            query5.setParameter("id", 2L);
            query5.setParameter("inicioPeriodo", LocalDate.now().minusMonths(3).withDayOfMonth(1));
            query5.setParameter("finPeriodo", LocalDate.now().withDayOfMonth(1).minusDays(1));

            List<Factura> facturas1 = query5.getResultList();
            System.out.println("\nFacturas de los ultimos 3 meses del cliente pedido: ");
            facturas1.forEach(System.out::println);

            // 6. Monto total facturado por un cliente
            Query query6 = em.createQuery("SELECT SUM(f.total) as totalFacturado FROM Cliente c JOIN c.facturas f WHERE c.id = :id GROUP BY c.id");
            query6.setParameter("id", 2L);

            Double totalFacturado = (Double) query6.getSingleResult();
            System.out.println("\nTotal facturado por el cliente: " + totalFacturado);

            // 7. Listar los articulos de una factura en especifico
            Query query7 = em.createQuery("SELECT a FROM Factura f JOIN f.detallesFactura df JOIN df.articulo a WHERE f.id = :id");
            query7.setParameter("id", 3L);

            List<Articulo> articulos = query7.getResultList();
            System.out.println("\nArticulos de la factura pedida: ");
            articulos.forEach(System.out::println);

            // 8. Articulo mas caro en una factura
            Query query8 = em.createQuery("SELECT a FROM Factura f JOIN f.detallesFactura df JOIN df.articulo a WHERE f.id = :id ORDER BY a.precioVenta DESC");
            query8.setMaxResults(1);
            query8.setParameter("id", 3L);

            Articulo articulo = (Articulo) query8.getSingleResult();
            System.out.println("\nArticulo mas caro de la factura pedida: " + articulo.denominacion + ", con un precio de: " + articulo.precioVenta);

            // 9. Total de facturas en el sistema
            Query query9 = em.createQuery("SELECT COUNT(f) FROM Factura f");

            Long cantidadTotalFacturas = (Long) query9.getSingleResult();
            System.out.println("\nCantidad de facturas generadas: " + cantidadTotalFacturas);

            // 10. Listar facturas con un total mayor a un monto especifico
            Query query10 = em.createQuery("SELECT f FROM Factura f WHERE f.total > :monto");
            query10.setParameter("monto", 200d);

            List<Factura> facturasMonto = query10.getResultList();
            System.out.println("\nFacturas con total mayor a 200: ");
            facturasMonto.forEach(System.out::println);

            // 11. Facturas que tengan un articulo especifico, filtrando por nombre de articulo
            Query query11 = em.createQuery("SELECT f FROM Factura f JOIN f.detallesFactura df JOIN df.articulo a WHERE a.denominacion = :nombreArticulo");
            query11.setParameter("nombreArticulo", "Manzana");

            List<Factura> facturasArticulo = query11.getResultList();
            System.out.println("\nFacturas que contienen el articulo pedido");
            facturasArticulo.forEach(System.out::println);

            // 12. Listar todos los Artículos cuyo código coincida total o parcialmente
            Query query12 = em.createQuery("SELECT a FROM Articulo a WHERE a.codigo LIKE :codigo");
            query12.setParameter("codigo", "%AB%");

            List<Articulo> articulosCodigo = query12.getResultList();
            System.out.println("\nArticulos que tienen o contienen el codigo solicitado");
            articulosCodigo.forEach(System.out::println);

            // 13. Listar articulos con precio mayor al precio promedio de todos los articulos
            Query query13 = em.createQuery("SELECT a1 FROM Articulo a1 WHERE a1.precioVenta > (SELECT AVG(a2.precioVenta) AS precioPromedio FROM Articulo a2)");

            List<Articulo> articulosPromedio =  query13.getResultList();
            System.out.println("\nArticulos con precio mayor al promedio: ");
            articulosPromedio.forEach(System.out::println);


            // Ejemplo de clausula EXISTS

            // Seleccionamos los articulos que hayan tenido al menos una venta
            Query query14 = em.createQuery("SELECT a FROM Articulo a WHERE EXISTS (SELECT fd FROM FacturaDetalle fd WHERE fd.articulo = a)");
            List<Articulo> articulosExists = query14.getResultList();

            System.out.println("\nArticulos con al menos una venta:");
            articulosExists.forEach(System.out::println);


            // Cerrar el EntityManager y el EntityManagerFactory
            em.close();
            emf.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

/*

Manejo del Ciclo de Estados en JPA
El ciclo de estados en JPA (Java Persistence API) define los diferentes estados que puede tener una entidad en relación con el contexto de persistencia (EntityManager). Comprender y manejar correctamente estos estados es crucial para trabajar eficazmente con JPA. Los estados del ciclo de vida de una entidad en JPA son:

New (Nuevo):

Una entidad está en estado "New" cuando ha sido creada pero aún no ha sido persistida en la base de datos.
Managed (Gestionado):

Una entidad está en estado "Managed" cuando está asociada con un contexto de persistencia (EntityManager) y cualquier cambio en la entidad se reflejará automáticamente en la base de datos.
Detached (Desconectado):

Una entidad está en estado "Detached" cuando ya no está asociada con un contexto de persistencia. Los cambios en la entidad no se reflejarán automáticamente en la base de datos.
Removed (Eliminado):

Una entidad está en estado "Removed" cuando ha sido marcada para su eliminación en la base de datos.
*/
