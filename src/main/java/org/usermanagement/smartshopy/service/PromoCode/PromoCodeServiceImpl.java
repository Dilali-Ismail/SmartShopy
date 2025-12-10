package org.usermanagement.smartshopy.service.PromoCode;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.usermanagement.smartshopy.dto.request.CreatePromoCodeDTO;
import org.usermanagement.smartshopy.dto.response.PromoCodeDTO;
import org.usermanagement.smartshopy.entity.PromoCode;
import org.usermanagement.smartshopy.exception.BadRequestException;
import org.usermanagement.smartshopy.mapper.PromoCodeMapper;
import org.usermanagement.smartshopy.repository.PromoCodeRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PromoCodeServiceImpl implements PromoCodeService {

    private final PromoCodeRepository promoCodeRepository;
    private final PromoCodeMapper promoCodeMapper;
    @Override
    public PromoCodeDTO createPromocode(CreatePromoCodeDTO dto) {

        if (promoCodeRepository.existsByCode(dto.getCode())) {
            throw new BadRequestException("Le code promo existe déjà");
        }

        PromoCode promoCode = PromoCode.builder()
                .code(dto.getCode().toUpperCase())
                .discountPercentage(dto.getDiscountPercentage())
                .maxUsages(dto.getMaxUsages())
                .currentUsages(0)
                .build();

        promoCodeRepository.save(promoCode);

        return promoCodeMapper.toDTO(promoCode);

    }

    @Override
    public List<PromoCodeDTO> getAllPromocodes(){
        return promoCodeRepository.findAll().stream().map(promoCodeMapper::toDTO).collect(Collectors.toList());
    }

}
