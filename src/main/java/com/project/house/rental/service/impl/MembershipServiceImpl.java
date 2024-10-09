package com.project.house.rental.service.impl;

import com.project.house.rental.common.PageInfo;
import com.project.house.rental.constant.FilterConstant;
import com.project.house.rental.dto.MembershipDto;
import com.project.house.rental.dto.params.MembershipParams;
import com.project.house.rental.entity.Membership;
import com.project.house.rental.entity.Membership_;
import com.project.house.rental.exception.ConflictException;
import com.project.house.rental.exception.CustomRuntimeException;
import com.project.house.rental.mapper.MembershipMapper;
import com.project.house.rental.repository.MembershipRepository;
import com.project.house.rental.service.MembershipService;
import com.project.house.rental.specification.MembershipSpecifition;
import com.project.house.rental.utils.HibernateFilterHelper;
import jakarta.persistence.NoResultException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MembershipServiceImpl implements MembershipService {
    private final MembershipRepository membershipRepository;
    private final HibernateFilterHelper hibernateFilterHelper;

    public MembershipServiceImpl(MembershipRepository membershipRepository, HibernateFilterHelper hibernateFilterHelper) {
        this.membershipRepository = membershipRepository;
        this.hibernateFilterHelper = hibernateFilterHelper;
    }

    @Override
    public List<MembershipDto> getAllMemberships() {
        hibernateFilterHelper.enableFilter(FilterConstant.DELETE_MEMBERSHIP_FILTER);

        List<Membership> membershipList = membershipRepository.findAll(Sort.by(Sort.Direction.ASC, Membership_.CREATED_AT));

        hibernateFilterHelper.disableFilter(FilterConstant.DELETE_MEMBERSHIP_FILTER);

        return membershipList.stream()
                .map(MembershipMapper.INSTANCE::toDto)
                .toList();
    }

    @Override
    public Map<String, Object> getAllMembershipsWithParams(MembershipParams membershipParams) {
        Specification<Membership> spec = MembershipSpecifition.searchByName(membershipParams.getName());

        Sort sort = switch (membershipParams.getSortBy()) {
            case "nameAsc" -> Sort.by(Membership_.NAME);
            case "nameDesc" -> Sort.by(Membership_.NAME).descending();
            case "priceAsc" -> Sort.by(Membership_.PRICE);
            case "priceDesc" -> Sort.by(Membership_.PRICE).descending();
            case "refreshAsc" -> Sort.by(Membership_.REFRESH);
            case "refreshDesc" -> Sort.by(Membership_.REFRESH).descending();
            case "priorityAsc" -> Sort.by(Membership_.PRIORITY);
            case "priorityDesc" -> Sort.by(Membership_.PRIORITY).descending();
            case "createdAtAsc" -> Sort.by(Membership_.CREATED_AT);
            case "createdAtDesc" -> Sort.by(Membership_.CREATED_AT).descending();
            default -> Sort.by(Membership_.ID).descending();
        };

        if (membershipParams.getPageNumber() < 0) {
            membershipParams.setPageNumber(0);
        }

        if (membershipParams.getPageSize() <= 0) {
            membershipParams.setPageSize(10);
        }

        Pageable pageable = PageRequest.of(
                membershipParams.getPageNumber(),
                membershipParams.getPageSize(),
                sort
        );

        hibernateFilterHelper.enableFilter(FilterConstant.DELETE_MEMBERSHIP_FILTER);

        Page<Membership> membershipPage = membershipRepository.findAll(spec, pageable);

        hibernateFilterHelper.disableFilter(FilterConstant.DELETE_MEMBERSHIP_FILTER);

        PageInfo pageInfo = new PageInfo(membershipPage);

        List<MembershipDto> membershipDtoList = membershipPage.stream()
                .map(MembershipMapper.INSTANCE::toDto)
                .toList();

        return Map.of(
                "pageInfo", pageInfo,
                "data", membershipDtoList
        );
    }

    @Override
    public MembershipDto getMembershipById(long id) {
        Membership membership = membershipRepository.findByIdWithFilter(id);

        if (membership == null) {
            throw new NoResultException("Không tìm thấy Memberships với id = " + id);
        }

        return MembershipMapper.INSTANCE.toDto(membership);
    }

    @Override
    public MembershipDto updateMembership(long id, MembershipDto membershipDto) {
        hibernateFilterHelper.enableFilter(FilterConstant.DELETE_MEMBERSHIP_FILTER);

        Membership membership = membershipRepository.findByIdWithFilter(id);

        if (membership == null) {
            throw new NoResultException("Không tìm thấy hạng mức thành viên với id = " + id);
        }

        Membership existingMembership = membershipRepository.findByNameIgnoreCase(membershipDto.getName());

        hibernateFilterHelper.disableFilter(FilterConstant.DELETE_MEMBERSHIP_FILTER);

        if (existingMembership != null && existingMembership.getId() != id) {
            throw new ConflictException("Tên hạng mức thành viên đã tồn tại");
        }

        MembershipMapper.INSTANCE.updateFromDto(membershipDto, membership);
        membership = membershipRepository.save(membership);

        return MembershipMapper.INSTANCE.toDto(membership);
    }

    @Override
    public void deleteMembershipById(long id) {
        Membership membership = membershipRepository.findById(id)
                .orElseThrow(() -> new NoResultException("Không tìm thấy 'Membership' với id = " + id));

        membershipRepository.deleteById(membership.getId());
    }

    @Override
    public void deleteMultipleMemberships(List<Long> ids) {
        List<Membership> membershipList = membershipRepository.findAllById(ids);
        membershipRepository.deleteAll(membershipList);
    }

    @Override
    public MembershipDto createMembership(MembershipDto membershipDto) {
        hibernateFilterHelper.enableFilter(FilterConstant.DELETE_MEMBERSHIP_FILTER);

        Membership existingMembership = membershipRepository.findByNameIgnoreCase(membershipDto.getName());

        hibernateFilterHelper.disableFilter(FilterConstant.DELETE_MEMBERSHIP_FILTER);

        if (existingMembership != null) {
            throw new ConflictException("Tên hạng mức thành viên đã tồn tại");
        }

        Membership membership = MembershipMapper.INSTANCE.toEntity(membershipDto);
        membership = membershipRepository.save(membership);
        return MembershipMapper.INSTANCE.toDto(membership);
    }

    @Override
    public void updatePrice(long id, double price) throws CustomRuntimeException {
        Membership membership = membershipRepository.findById(id)
                .orElseThrow(() -> new NoResultException("Không tìm thấy 'Membership' với id = " + id));

        if (price < 0) {
            throw new CustomRuntimeException("Số tiền không thể nhỏ hơn 0!");
        }

        membership.setPrice(price);
        membershipRepository.save(membership);
    }
}
