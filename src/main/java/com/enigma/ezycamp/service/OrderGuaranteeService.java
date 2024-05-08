package com.enigma.ezycamp.service;

import com.enigma.ezycamp.entity.OrderGuaranteeImage;
import org.springframework.web.multipart.MultipartFile;

public interface OrderGuaranteeService {
    OrderGuaranteeImage addGuarantee(MultipartFile image);
}
