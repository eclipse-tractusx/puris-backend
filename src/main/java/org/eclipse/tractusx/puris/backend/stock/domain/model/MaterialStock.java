package org.eclipse.tractusx.puris.backend.stock.domain.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@DiscriminatorValue("MaterialStock")
@Getter
@Setter
@ToString(callSuper = true)
public class MaterialStock extends Stock {

}