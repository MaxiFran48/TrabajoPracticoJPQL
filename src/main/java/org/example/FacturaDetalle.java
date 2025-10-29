package org.example;

import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString(exclude = {"factura", "articulo"})
@Builder
public class FacturaDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double cantidad;
    private double subTotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "articuloId_fk")
    private Articulo articulo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facturaId_fk", nullable = false)
    private Factura factura;

    public FacturaDetalle(double cantidad, Articulo articulo) {
        this.cantidad = cantidad;
        this.articulo = articulo;
    }

    @Transient
    public double calcularSubTotal(){
        this.subTotal = this.cantidad * this.getArticulo().getPrecioVenta();
        return this.subTotal;
    }
}
