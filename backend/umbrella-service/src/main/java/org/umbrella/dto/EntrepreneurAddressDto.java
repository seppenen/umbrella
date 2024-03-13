package org.umbrella.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class EntrepreneurAddressDto  {

    Long id;
    String street;
    String city;
    String state;
    String country;
}
