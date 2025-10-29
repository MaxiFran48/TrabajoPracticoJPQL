package org.example;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString(exclude = {"cliente", "detallesFactura"})
@Builder
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer puntoVenta;
    private Long nroComprobante;
    private Double total;

    private LocalDate fechaComprobante;

    @Temporal(TemporalType.DATE)
    private Date fechaAlta;

    private Date fechaBaja;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clienteId_fk")
    private Cliente cliente;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "factura")
    private List<FacturaDetalle> detallesFactura;

    public void addDetalleFactura(FacturaDetalle detalle){
        if(this.detallesFactura == null)
            this.detallesFactura = new ArrayList<FacturaDetalle>();

        detalle.setFactura(this);
        this.detallesFactura.add(detalle);
    }

    @Transient
    public String getStrProVentaNroComprobante(){
        return this.getPuntoVenta() + "-" + this.getNroComprobante();
    }

    @Transient
    public double calcularTotal(){
        double totalFac = 0;
        for(FacturaDetalle det : detallesFactura){
            totalFac += det.calcularSubTotal();
        }
        this.total = totalFac;
        return totalFac;
    }


}
