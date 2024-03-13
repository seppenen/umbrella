package org.umbrella.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EntrepreneurDto {
    private Long id;
    private String businessName;
    private String type;
    private String businessId;
    private String email;
    private String phone;
    private String website;
    @JsonIgnore
    private boolean deleted;
    private EntrepreneurAddressDto address;
}
