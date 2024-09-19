package com.project.house.rental.service.impl;

import com.project.house.rental.common.PageInfo;
import com.project.house.rental.constant.FilterConstant;
import com.project.house.rental.dto.MembershipDto;
import com.project.house.rental.dto.params.MembershipParams;
import com.project.house.rental.entity.Membership;
import com.project.house.rental.entity.Membership_;
import com.project.house.rental.mapper.MembershipMapper;
import com.project.house.rental.repository.MembershipRepository;
import com.project.house.rental.service.MembershipService;
import com.project.house.rental.specification.MembershipSpecifition;
import com.project.house.rental.utils.HibernateFilterHelper;
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

        List<Membership> membershipList = membershipRepository.findAll();

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
}
