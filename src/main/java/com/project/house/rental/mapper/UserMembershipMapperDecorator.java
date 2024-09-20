package com.project.house.rental.mapper;

import com.project.house.rental.dto.UserMembershipDto;
import com.project.house.rental.entity.Membership;
import com.project.house.rental.entity.UserMembership;
import com.project.house.rental.repository.MembershipRepository;
import jakarta.persistence.NoResultException;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class UserMembershipMapperDecorator implements UserMembershipMapper {

    @Autowired
    @Qualifier("delegate")
    private UserMembershipMapper delegate;
    @Autowired
    private MembershipRepository membershipRepository;

    @Override
    public UserMembership toEntity(UserMembershipDto userMembershipDto) {
        UserMembership userMembership = delegate.toEntity(userMembershipDto);

        Membership membership = membershipRepository.findById(userMembershipDto.getMembershipId())
                .orElseThrow(() -> new NoResultException("Không tìm thấy hạng mức thành viên với id: " + userMembershipDto.getMembershipId()));

        userMembership.setMembership(membership);
        return userMembership;
    }

    @Override
    public void updateEntityFromDto(UserMembershipDto userMembershipDto, @MappingTarget UserMembership userMembership) {
        delegate.updateEntityFromDto(userMembershipDto, userMembership);

        Membership membership = membershipRepository.findById(userMembershipDto.getMembershipId())
                .orElseThrow(() -> new NoResultException("Không tìm thấy hạng mức thành viên với id: " + userMembershipDto.getMembershipId()));

        userMembership.setMembership(membership);
    }
}
